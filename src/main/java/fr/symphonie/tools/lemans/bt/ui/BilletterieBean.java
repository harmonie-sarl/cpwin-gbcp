package fr.symphonie.tools.lemans.bt.ui;

import java.util.List;
import java.util.Optional;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.apache.commons.collections.CollectionUtils;
import org.primefaces.event.FileUploadEvent;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import fr.symphonie.common.util.MsgEntry;
import fr.symphonie.common.util.Util;
import fr.symphonie.tools.common.CommonToolsBean;
import fr.symphonie.tools.common.excel.IExcelImportor;
import fr.symphonie.tools.common.model.FileImportTrace;
import fr.symphonie.tools.common.model.ImportPeriod;
import fr.symphonie.tools.lemans.bt.dto.VenteDto;
import fr.symphonie.tools.lemans.bt.dto.VenteItemDto;
import fr.symphonie.util.HandlerJSFMessage;
import fr.symphonie.util.Helper;
import fr.symphonie.util.model.SimpleEntity;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@ManagedBean(name = "btBean")
@ViewScoped
@Slf4j
public class BilletterieBean extends CommonToolsBean {

	@Autowired
	@Qualifier("memory")
	private IExcelImportor importService;
	@Getter
	private List<VenteDto> ventes = null;
	@Getter
	private List<VenteItemDto> venteDetails = null;
	/**
	 * Input elements
	 */
	@Getter
	@Setter
	private String codePeriod;
	private List<ImportPeriod> periodList;
	private List<String> periodExerciseList;
	
	/**
	 * For controle
	 */
	@Getter
	@Setter
	private boolean oldDataLoading;
	@Getter
	@Setter
	private boolean importCompleted;

	public void fileUploadHandler(FileUploadEvent event) {
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
		this.ventes = importService.importFile(VENTE_SHEET_NO, VenteDto.class);
		if (!isErrorReportVisible()) {
			this.venteDetails = importService.importFile(PAIEMENT_SHEET_NO, VenteItemDto.class);
		}

	}

	public void executeSaveData() {

		try {
			saveImportTrace();

			// String message =
			// HandlerJSFMessage.formatMessage(HandlerJSFMessage.getMessage(MsgEntry.REMB_VALIDATION_MSG),new
			// Object[] { getNextNoVague()});
			// HandlerJSFMessage.addInfoMessage(message);
			HandlerJSFMessage.addInfo(MsgEntry.SUCCES);
		} catch (Exception e) {
			e.printStackTrace();
			HandlerJSFMessage.addErrorMessage(Util.getErrorMessageFromException(MsgEntry.FAILED, e));
			log.error("executeSaveData: Error: ", e.getMessage());
		}
	}

	public boolean isErrorReportVisible() {
		if (CollectionUtils.isEmpty(getErrorReport()))
			return false;
		return true;
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
		importService.reset();

	}

	@Override
	protected Logger getLogger() {
		return log;
	}
	private String getModuleName() {
		return "LEMANS_BT";
	}
	private void saveImportTrace() {
		log.debug("saveImportTrace ...");
		FileImportTrace vague=FileImportTrace.builder()
				.exercice(getExerciceInt())
				.module(getModuleName())
				.crc32(importService.getCrc32Value())
				.fileName(importService.getFileName())
				.trace(Helper.createTrace())
				.build();
		getCommonService().saveImportTrace(vague);	
		log.debug("saveImportTrace : end");
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
		
	}

	private void resetDynamicList() {
		// TODO Auto-generated method stub
		
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

}
