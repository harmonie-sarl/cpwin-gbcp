package fr.symphonie.tools.lemans.bt.ui;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import org.apache.commons.collections.CollectionUtils;
import org.primefaces.event.FileUploadEvent;
import org.slf4j.Logger;

import fr.symphonie.common.util.MsgEntry;
import fr.symphonie.common.util.Util;
import fr.symphonie.tools.common.CommonToolsBean;
import fr.symphonie.tools.common.excel.IExcelImportor;
import fr.symphonie.tools.common.model.FileImportTrace;
import fr.symphonie.tools.common.model.ImportPeriod;
import fr.symphonie.tools.lemans.bt.model.Vente;
import fr.symphonie.tools.lemans.bt.model.VenteItem;
import fr.symphonie.util.HandlerJSFMessage;
import fr.symphonie.util.Helper;
import fr.symphonie.util.model.SimpleEntity;
import fr.symphonie.util.model.Trace;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@ManagedBean(name = "btBean")
@ViewScoped
@Slf4j
public class BilletterieBean extends CommonToolsBean {

	@ManagedProperty (value="#{memoryExcelImportor}")
	@Setter
	private IExcelImportor importService;
	@Getter
	private List<Vente> ventes = null;
	@Getter
	private List<VenteItem> venteDetails = null;
	/**
	 * Input elements
	 */
	@Getter
	@Setter
	private String codePeriod;
	private List<ImportPeriod> periodList;
	
	/**
	 * For controle
	 */
	@Getter
	@Setter
	private boolean oldDataLoading;
	@Getter
	@Setter
	private boolean importCompleted;
	@Getter
	@Setter
	private boolean saveCompleted;

	public void fileUploadHandler(FileUploadEvent event) {
		reset();
		setImportFileUploadEvent(event);
		importService.fileUploadHandler(event);
		try {
			List<FileImportTrace> vagueList = getCommonService().getImportHistoryList(getExerciceInt(),getModuleName(), importService.getCrc32Value());
			if (!vagueList.isEmpty()) {
				String msg = HandlerJSFMessage.formatMessage(HandlerJSFMessage.getMessage(MsgEntry.DUPLICAT_CRC_MSG),
						new Object[] { vagueList.get(0).getId() });
				HandlerJSFMessage.addWarnMessage(msg);
			}
		} catch (Exception e) {
			HandlerJSFMessage.addErrorMessage(Util.getErrorMessageFromException(MsgEntry.FAILED, e));
			log.error("genererSepaFr: ", e.getMessage());
		}
		
	}

	public void executeImport() {
		int VENTE_SHEET_NO = 0;
		int PAIEMENT_SHEET_NO = 1;
		this.ventes = importService.importFile(VENTE_SHEET_NO, Vente.class);
		if (!hasErrors()) {
			this.venteDetails = importService.importFile(PAIEMENT_SHEET_NO, VenteItem.class);
		}
		if (!hasErrors()) {
			processData();
			fillCompletData();
		}
		setImportCompleted(true);
		

	}

	private void fillCompletData() {
		Trace trace = Helper.createTrace();
		if (ventes != null) {
			ventes.stream().forEach(v -> {
				v.setExercice(getExerciceInt());
				v.setBudget(getCodeBudget());
				v.setPeriode(getCodePeriod());
				v.setTrace(trace);
			});
		}
		if (venteDetails != null) {
			venteDetails.stream().forEach(v -> {
				v.setExercice(getExerciceInt());
				v.setBudget(getCodeBudget());
				v.setPeriode(getCodePeriod());
				v.setTrace(trace);
			});
		}

	}

	private void processData() {
		checkData();
		
	}

	private void checkData() {
		List<Integer> duplicates =checkDuplicatesVente(getVentes());
		if(!CollectionUtils.isEmpty(duplicates)) {
			getErrorReport().add(new SimpleEntity("duplicates", "Duplication des 'Num Encaiss' non acceptée: "+duplicates));
		}
		
	}

	public void executeSaveData() {

		try {
			
			getCommonService().saveList(ventes);
			getCommonService().saveList(venteDetails);
			FileImportTrace vague = saveImportTrace();

			 String message = HandlerJSFMessage.formatMessage(HandlerJSFMessage.getMessage(MsgEntry.REMB_VALIDATION_MSG),new
			 Object[] { vague.getId()});
			 HandlerJSFMessage.addInfoMessage(message);
			 setSaveCompleted(true);
//			HandlerJSFMessage.addInfo(MsgEntry.SUCCES);
		} catch (Exception e) {
			e.printStackTrace();
			HandlerJSFMessage.addErrorMessage(Util.getErrorMessageFromException(MsgEntry.FAILED, e));
			log.error("executeSaveData: Error: ", e.getMessage());
		}
	}

	public boolean isErrorReportVisible() {
		if (!hasErrors())	return false;
		return true;
	}
	public boolean hasErrors(){
		return !getErrorReport().isEmpty();
	}

	public List<SimpleEntity> getErrorReport() {
		return importService.getErrorReport();
	}

	public String getErreursCount() {
		return "(" + getErrorReport().size() + ")";
	}

	@Override
	protected void executeTask(String taskName) throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	protected String[] getTasks() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void prepare() throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public void reset() {
		resetDynamicList();
		setImportFileUploadEvent(null);
		importService.reset();

	}

	@Override
	protected Logger getLogger() {
		return log;
	}
	private String getModuleName() {
		return "LEMANS_BT";
	}
	private FileImportTrace saveImportTrace() {
		log.debug("saveImportTrace ...");
		FileImportTrace vague=FileImportTrace.builder()
				.exercice(getExerciceInt())
				.module(getModuleName())
				.crc32(importService.getCrc32Value())
				.fileName(importService.getFileName())
				.trace(Helper.createTrace())
				.build();
		getCommonService().saveImportTrace(vague);	
		log.debug("saveImportTrace: vagueId={} : end",vague.getId());
		return vague;
	}
	public List<ImportPeriod> getPeriodList() {
		if(CollectionUtils.isEmpty(periodList))load(TaskEnum.PERIOD);
		return periodList;
	}
	private void load(TaskEnum task) {
		log.debug("load {} ...",task.name());	
		switch(task) {
		case PERIOD:
			this.periodList = getCommonService().getPeriodList(getExerciceInt(), getCodeBudget(),getModuleName());
			break;
		}
		log.debug("load {} .END.",task.name());
	}
	public enum TaskEnum {
		PERIOD
}
	public void onPeriodChange()
	{
		resetDynamicList();
		resetStatus();
		setImportFileUploadEvent(null);
		checkPeriod() ;
		if(isOldDataLoading()) {
			
		}
	
	}

	private boolean  checkPeriod()
	{
		ImportPeriod selectedPeriod=getSelectedPeriod();
		ImportPeriod periodeBefore=null;
		String msgArg=null, msgKey=null;
		boolean isWarn=false;
		
		if(selectedPeriod==null) return false;
		if(selectedPeriod.getEtat()==null)return false;
		switch (selectedPeriod.getEtat()) {
		case TRAITE:
			msgKey=MsgEntry.PERIODE_TRAITE_ERROR;
			msgArg=selectedPeriod.getCode();
			setOldDataLoading(true);
			break;
		case CHARGE:
			msgKey=MsgEntry.PERIODE_CHARGE_WARN;
			msgArg=selectedPeriod.getCode();
			isWarn=true;
			setOldDataLoading(true);
			break;
		case OUVERT:
			if(selectedPeriod.getNumero()>1) {
				periodeBefore=getPeriod(selectedPeriod.getNumero()-1);
				if(periodeBefore!=null)	
				{
					msgArg=Integer.toString(periodeBefore.getNumero());
					if(periodeBefore.isOuvert()) {
						msgKey=MsgEntry.PERIODE_PRECEDENTE_NON_TRAITEE;
						setCodePeriod(null);
					}
					else if(periodeBefore.isCharge()) {
						msgKey=MsgEntry.PERIODE_PRECEDENTE_ENCOURS;
						isWarn=true;
					}
				}	
			}
			
			break;	
		default:
			break;
		}
		
		displayMessage(msgKey,msgArg,isWarn);
		if(msgKey==null || isWarn)return true;			
		return false;
		
		
	}
	public boolean isSaveAutorized() {
		if (isSaveCompleted())
			return false;
		return true;
	}
public ImportPeriod getSelectedPeriod() {		
	ImportPeriod p= getPeriod(getCodePeriod());
		log.debug("getSelectedPeriod: code={}, period={}",getCodePeriod(),p);
		return p;
	}
private ImportPeriod getPeriod(String code)
{
	log.debug("getPeriod: code='{}'",code);
	if(getPeriodList()==null) return null;
	Optional<ImportPeriod> optional = getPeriodList().stream().
		filter(p -> p.getCode().equalsIgnoreCase(code))
		.findFirst();

	return optional.isPresent()?optional.get():null;

}
private ImportPeriod getPeriod(int numero) {
	
	if(getPeriodList()==null) return null;
	Optional<ImportPeriod> optional = getPeriodList().stream().
		filter(p -> p.getNumero()==numero)
		.findFirst();
	return optional.isPresent()?optional.get():null;
}


	private void resetStatus() {
		setImportCompleted(false);
		setSaveCompleted(false);
		
	}

	private void resetDynamicList() {
		this.ventes			=	null;
		this.venteDetails	=	null;
		
	}
	public boolean isRequiredDataDone() {
		log.debug("isRequiredDataDone: isCommonRequiredDone={}",isCommonRequiredDone());
		if (!isCommonRequiredDone())
			return false;
		if (getSelectedPeriod() == null)
			return false;
		if(getSelectedPeriod().isTraite())
			return false;

		return true;
	}
	public boolean isImportAutorized() {

		if (!isRequiredDataDone())
			return false;
		if (getImportFileUploadEvent() == null)
			return false;
		if(!isPeriodEditable()) return false;

		return true;
	}
	public boolean isPeriodEditable() {
		ImportPeriod p=getSelectedPeriod();
		if(p==null) return false;
		if(p.isTraite())return false;
		
		return true;
	}
	public boolean isDataVisible()
	   {
		if(isErrorReportVisible()) return true;
		if(isImportedDataVisible())return true;
		return false; 
	   }
	public boolean isImportedDataVisible() {

		return !CollectionUtils.isEmpty(getVentes());
	}
	public String getSourceFileName() {
		return (getImportFileUploadEvent()!=null ?getImportFileUploadEvent().getFile().getFileName():"");
	}

	private List<Integer> checkDuplicatesVente(List<Vente> importedData) {
		log.debug("checkDuplicatesVente: start");
		List<Integer> duplicates =importedData.stream().map(d -> d.getNumEncaiss())		
				.collect( Collectors.groupingBy( Function.identity(), Collectors.counting() ) )
				.entrySet()
				.stream()
				.filter( p -> p.getValue()>1)
				.map( Map.Entry::getKey )
			    .collect( Collectors.toList() );
		log.debug("checkDuplicatesVente: size= {}: end ",duplicates.size());
		return duplicates;
	}

}
