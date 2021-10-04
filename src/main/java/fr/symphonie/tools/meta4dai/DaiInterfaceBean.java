package fr.symphonie.tools.meta4dai;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;

import org.apache.commons.collections.CollectionUtils;
import org.primefaces.model.StreamedContent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.symphonie.budget.ui.beans.pluri.DialogHelper;
import fr.symphonie.budget.ui.excel.ExcelHandler;
import fr.symphonie.budget.ui.excel.ExcelModelEnum;
import fr.symphonie.common.core.ICommonService;
import fr.symphonie.common.util.MsgEntry;
import fr.symphonie.common.util.Util;
import fr.symphonie.tools.common.GenericExcelImportor;
import fr.symphonie.tools.meta4dai.model.ImportedData;
import fr.symphonie.tools.meta4dai.model.InternalEngagement;
import fr.symphonie.tools.meta4dai.model.LbData;
import fr.symphonie.tools.meta4dai.model.PaymentItem;
import fr.symphonie.tools.meta4dai.model.Period;
import fr.symphonie.tools.meta4dai.model.Period.STATUS;
import fr.symphonie.util.HandlerJSFMessage;
import fr.symphonie.util.Helper;
import fr.symphonie.util.model.SimpleEntity;
import fr.symphonie.util.model.Trace;

@ManagedBean(name = "daiBean")
@SessionScoped
public class DaiInterfaceBean extends GenericExcelImportor<ImportedData> implements Serializable {

	public enum TaskEnum {
			PERIOD
	}

	private static final long serialVersionUID = -3820991428325777496L;
	private static final Logger logger =LoggerFactory.getLogger(DaiInterfaceBean.class);
	/**
	 * Injected services
	 */
	@ManagedProperty (value="#{commonService}")
	private ICommonService commonService;
	/**
	 * Input elements
	 */
	private String codePeriod;
	private List<Period> periodList;
	private List<String> periodExerciseList;
	
	/**
	 * Output elements
	 */
	private List<PaymentItem> daiList;
	//la ligne budgétaire interne sous-traitante
	private Integer noLbiStr;
	private DisplayStruct selectedEi;
	private List<DisplayStruct> displayList;
	/**
	 * Status elements
	 */
	private boolean importCompleted;
	private boolean oldDataLoading;
	private boolean simulationCompleted;
	private boolean configurationCompleted;
	private boolean validationCompleted;
	
	public void executeImport() {
		logger.info("executeImport : Début file={}",getImportFileUploadEvent().getFile().getFileName());
		try {
			List<ImportedData> importedData=super.importFile(0, ImportedData.class);
			List<PaymentItem> paymentList=processData(importedData);
			if(!hasErrors()) {
				setDaiList(paymentList);
				setImportCompleted(true);
			}
			
		} catch (Exception e) {
			HandlerJSFMessage.addErrorMessage(Util.getErrorMessageFromException(MsgEntry.FAILED, e));
		}
		logger.info("importer : DaiList={} : Fin ",getDaiList());
	}

	private List<PaymentItem> processData(List<ImportedData> importedData) {
		logger.info("processData : Début ");
		checkData(importedData);
		return fillCompletData(importedData);
		
		
	}
	private void checkData(List<ImportedData> importedData) {
		logger.info("checkData : Début ");
		BigDecimal amount=null;
		Integer index=null;
		String msg=null;
		List<Integer> inputEiList=importedData.stream().map(x-> x.getNumeroEI()).collect(Collectors.toList());
		List<Integer> foundEiList=commonService.verifyEi(getExec(),inputEiList);
		List<Integer> duplicates =checkDuplicatesEi(importedData);
		if(!CollectionUtils.isEmpty(duplicates)) {
			getErrorReport().add(new SimpleEntity("duplicates", "Duplication des no_ei non acceptée: "+duplicates));
		}
		
		for(ImportedData data:importedData) {
			logger.debug("checkData: {}",data);
			index=data.getRowNum();
			amount=data.getAmount();
			if((!data.getExercice().equals(getExec()))||(!data.getPeriod().equals(getSelectedPeriod().getNumero()))){
				msg=HandlerJSFMessage.formatMessage(HandlerJSFMessage.getErrorMessage("periode.incorrect"), new Object[]{data.getExercice().toString(),data.getPeriod(),getExercice().toString(),getSelectedPeriod().getNumero()});
				addErrorToReport(data, msg);
			}
			
			//if((amount!=null)&&(amount.doubleValue()<0)) {
			if((amount!=null)&&(amount.doubleValue()==0)) {
				msg=String.format("%.2f : Montant non valide",amount.floatValue());
				addErrorToReport(data, index+"-"+data.getNumeroEI(), msg);
			}
			if(!foundEiList.contains(data.getNumeroEI())) {
				msg=String.format("Ei N° %d non trouvé", data.getNumeroEI());
				addErrorToReport(data, index+"-"+data.getNumeroEI(), msg);
			}

		}
		logger.info("checkData : Fin ");
		
	}

	private List<Integer> checkDuplicatesEi(List<ImportedData> importedData) {
		logger.debug("checkDuplicatesEi");
		List<Integer> duplicates =importedData.stream().map(d -> d.getNumeroEI())		
				.collect( Collectors.groupingBy( Function.identity(), Collectors.counting() ) )
				.entrySet()
				.stream()
				.filter( p -> p.getValue()>1)
				.map( Map.Entry::getKey )
			    .collect( Collectors.toList() );
		logger.debug("checkDuplicatesEi: {}",duplicates);
		return duplicates;
	}

	private List<PaymentItem> fillCompletData(List<ImportedData> importedData) {
		List<PaymentItem> list=new ArrayList<PaymentItem>();
		for(ImportedData data: importedData) {
			list.add(new PaymentItem(data));
		}
		return list;
		
	}
	private List<PaymentItem> loadPaymentData(boolean withLbcCkeck){
		logger.debug("loadPaymentData ...");
		List<PaymentItem> list= commonService.loadMeta4DaiPaymentData(getExec(),getCodeBudget(),getCodePeriod());
		loadLbData(list);
		
		list.stream().forEach(x ->
		{
			if(x.getDispoEi()!=null) {
				x.setDispoApres(x.getDispoEi().subtract(x.getAmount()));
				}
			if(withLbcCkeck)
			if(x.getNoLBC()==null) {
				x.setNotFoundEI(true);
				x.setAddEiRequest(true);
			}
			
		}
		);
		
		logger.debug("loadPaymentData {} éléments chargés",list.size());
		return list;
	}

	private void loadLbData(List<PaymentItem> list) {
		
		List<Integer> inputLbiList=list.stream().map(x-> x.getNoLBI()).distinct().collect(Collectors.toList());
		List<LbData> lbDataList=commonService.getLbData(getExec(),inputLbiList);
		if(!CollectionUtils.isEmpty(lbDataList))
		list.stream().forEach(x -> {
			LbData lb = lbDataList.stream().filter(l -> l.getNumero().equals(x.getNoLBI())).collect(Collectors.toList())
					.get(0);
			x.setLb(lb);
		});
		
	}

	private void checkLoadedData(List<PaymentItem> list) {
		logger.debug("checkLoadedData ...");
		
		if(isInsufficientAvailable(list)) {
			logger.debug("checkLoadedData : InsufficientAvailable");
			displayMessage(MsgEntry.DISPO_Insuffisant, null, false);
		}
		if(isNonFonctNature(list)) {
			logger.debug("checkLoadedData : NonFonctNature");
			displayMessage(MsgEntry.NATURE_NON_F, null, false);
		}
		logger.debug("checkLoadedData: end");
		
		
	}
	private boolean isNonFonctNature(List<PaymentItem> list) {
		return list.stream().filter(x -> x.isNonFonctNature()).count()>0;
	}
	private boolean isInsufficientAvailable(List<PaymentItem> list) {
		return list.stream().filter(x -> x.isInsufficientAvailable()).count()>0;
	}
	private boolean isAddEiRequested(List<PaymentItem> list) {
		return list.stream().filter(x -> x.isAddEiRequest()).count()>0;
	}

	
	public void reset() {
		resetDynamicList();
		resetEnteteList();
		resetStatus();
	}

	private void resetEnteteList() {
		setPeriodList(null);
		setCodePeriod(null);
		setListBudgets(null);
		setCodeBudget(null);
		setNoLbiStr(null);
		
	}

	@Override
	protected void resetDynamicList() {
		super.resetDynamicList();
		setDaiList(null);
		setDisplayList(null);
		
	}
	private void resetStatus() {
		setOldDataLoading(false);
		setSimulationCompleted(false);
		setValidationCompleted(false);
		setConfigurationCompleted(false);
		setImportCompleted(false);
	}

	public List<PaymentItem> getDaiList() {
		return daiList;
	}

	public void setDaiList(List<PaymentItem> daiList) {
		this.daiList = daiList;
	}

	public Integer getNoLbiStr() {
		if(noLbiStr==null) {
			this.noLbiStr=commonService.getMeta4DaiNoLbiStr(getExec());
			logger.debug("getNoLbiStr {} -> {}",getExec(),noLbiStr);
		}
		return noLbiStr;
	}

	public void setNoLbiStr(Integer noLbiStr) {
		this.noLbiStr = noLbiStr;
	}

	public String getCodePeriod() {
		return codePeriod;
	}

	public void setCodePeriod(String codePeriode) {
		logger.debug("setCodePeriod: {}",codePeriode);
		this.codePeriod = codePeriode;
	}

	public List<Period> getPeriodList() {
		if(CollectionUtils.isEmpty(periodList))load(TaskEnum.PERIOD);
		return periodList;
	}

	public void setPeriodList(List<Period> periodList) {
		this.periodList = periodList;
	}
	

	public ICommonService getCommonService() {
		return commonService;
	}

	public void setCommonService(ICommonService commonService) {
		this.commonService = commonService;
	}

	public boolean isOldDataLoading() {
		return oldDataLoading;
	}

	public void setOldDataLoading(boolean oldDataLoading) {
		this.oldDataLoading = oldDataLoading;
	}

	protected void prepare() {
		logger.debug("prepare ...");	
		
		load(TaskEnum.PERIOD);
		
		logger.debug("prepare .END.");	
	}
	private void load(TaskEnum task) {
		logger.debug("load {} ...",task.name());	
		switch(task) {
		case PERIOD:
			this.periodList = commonService.getMeta4daiPeriodList(getExec(), getCodeBudget());
			break;
		}
		logger.debug("load {} .END.",task.name());
	}

	public void onPeriodChange()
	{
		resetDynamicList();
		resetStatus();
		setImportFileUploadEvent(null);
		checkPeriod() ;
		if(isOldDataLoading()) {
			setDaiList(loadPaymentData(getSelectedPeriod().isCharge()));
		}
	
	}
	private boolean  checkPeriod()
	{
		Period selectedPeriod=getSelectedPeriod();
		Period periodeBefore=null;
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
		case ENCOURS:
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

	private Period getPeriod(String code)
	{
		logger.debug("getPeriod: code='{}'",code);
		if(getPeriodList()!=null) {
		for (Period period : getPeriodList()) {
			String itemCode=period.getCode();
			logger.debug("getPeriod: itemCode='{}' ",itemCode);
			if(!itemCode.equals(code))continue;
			return period;
		}
		}
		logger.debug("getPeriod: period not found");
		return null;
	
	}
	private Period getPeriod(int numero) {
		if(getPeriodList()!=null)
			for (Period period : getPeriodList()) {
				if(period.getNumero()!=numero)continue;
				return period;
			}
			return null;
	}
	
	public boolean isImportedDataVisible() {

		return !CollectionUtils.isEmpty(getDaiList());
	}

	public void executeValidation() {
		logger.debug("executeValidation ...");
		try {
			commonService.executeMeta4DaiValidate(getExec(), getCodeBudget(), getCodePeriod());
			updatePeriod(STATUS.TRAITE);
			setDaiList(loadPaymentData(false));
			HandlerJSFMessage.addInfo(MsgEntry.SUCCES);
		//	HandlerJSFMessage.addInfoMessage(HandlerJSFMessage.getMessage(MsgEntry.SUCCES));
			setValidationCompleted(true);

		} catch (Exception e) {
			HandlerJSFMessage.addErrorMessage(Util.getErrorMessageFromException(MsgEntry.FAILED, e));
		}
		logger.debug("executeValidation : end");
	}
	
	public void executeAddToParam() {
		logger.debug("executeAddToParam ...");
		List<InternalEngagement> addToParamList=getDaiList().stream().filter(x -> x.isAddEiRequest()).map(x -> new InternalEngagement(x,getNoLbiStr())).collect(Collectors.toList());
		logger.debug("executeAddToParam: {} à ajouter ",addToParamList);
		commonService.saveList(addToParamList);
		executeSimulation();
	}
	public boolean isDataVisible()
	   {
		if(isErrorReportVisible()) return true;
		if(isImportedDataVisible())return true;
		return false; 
	   }
	public boolean isImportedDaiDataVisible() {	
		return !CollectionUtils.isEmpty(getDaiList());
	}
	public StreamedContent getxlsImportedDaiList() {
		ExcelHandler excel = null;
		logger.debug("getxlsImportedDaiList: start");
		StreamedContent returnStreamedContent = null;
//		if(!isPeriodEditable())
//		{  
//			excel = new ExcelHandler(ExcelModelEnum.META4DAI_SAISIE_TRAITE, getDaiList());}
			
//		else {
			excel = new ExcelHandler(ExcelModelEnum.META4DAI_SAISIE, getDaiList());
//			}
			
		returnStreamedContent = excel.getExcelFile();
		logger.debug("getxlsImportedDaiList: end");
		return returnStreamedContent;
		
	}
	public StreamedContent getxlsDaiTraiteList() {
		ExcelHandler excel = null;
		logger.debug("getxlsDaiTraiteList: start");
		StreamedContent returnStreamedContent = null;
		 
			excel = new ExcelHandler(ExcelModelEnum.META4DAI_SAISIE_TRAITE, getDaiList());
			
		returnStreamedContent = excel.getExcelFile();
		logger.debug("getxlsDaiTraiteList: end");
		return returnStreamedContent;
		
	}

	public boolean isRequiredDataDone() {
		logger.debug("isRequiredDataDone: isCommonRequiredDone={}",isCommonRequiredDone());
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
	public boolean isValidationAutorized() {
		if (isValidationCompleted())
			return false;
		if (!isSimulationCompleted())
			return false;
		if(!isPeriodEditable()) return false;
		if(isNonFonctNature(getDaiList())) return false;
		if(isInsufficientAvailable(getDaiList())) return false;
		if(isAddEiRequested(getDaiList())) return false;
		
		return true;
	}
	public boolean isSimulationAutorized() {
		if(isSimulationCompleted())return false;
		if(!isImportedDataVisible())return false;
		if(!isPeriodEditable()) return false;
		return true;
	}
	public boolean isPeriodEditable() {
		Period p=getSelectedPeriod();
		if(p==null) return false;
		if(p.isTraite())return false;
		
		return true;
	}

	public Period getSelectedPeriod() {
		
		Period p= getPeriod(getCodePeriod());
		logger.debug("getSelectedPeriod: code={}, period={}",getCodePeriod(),p);
		return p;
	}
	public void executeSimulation()
	{
		logger.debug("executeSimulation: ...");
		try {
			if(getSelectedPeriod().isOuvert()) {
				saveData();
				updatePeriod(STATUS.ENCOURS);
			}
			if(getSelectedPeriod().isCharge() && isImportCompleted()) {
				removeData();
				saveData();
			}
			
			//invoke PS of Control
			commonService.executeMeta4DaiCheck(getExec(),getCodeBudget(),getCodePeriod());
			setDaiList(loadPaymentData(true));
			checkLoadedData(getDaiList());
			setSimulationCompleted(true);
		}
		catch (Exception e) {
			HandlerJSFMessage.addErrorMessage(Util.getErrorMessageFromException(MsgEntry.FAILED, e));
		}
		logger.debug("executeSimulation: end");
	}
	private void removeData() {
		logger.debug("removeData: ...");
		int count=commonService.removeMeta4DaiPaymentData(getExec(),getCodeBudget(),getCodePeriod());
		logger.debug("removeData: {} éléments supprimés",count);
		
	}

	private void saveData() {
		logger.debug("saveData ...");
		Trace trace=Helper.createTrace();
		String objetDai=getSelectedPeriod().getObject();
		List<PaymentItem> list=getDaiList();
			list.forEach(x ->{
				x.setTrace(trace);
				x.setPeriod(getCodePeriod());
				x.setExercice(getExec());
				x.setBudget(getCodeBudget());
				x.setObjetDAI(objetDai);
			});
			commonService.saveList(list);
			logger.debug("saveData {} éléments ajoutés",list.size());
			
	}
	private void updatePeriod(STATUS status) {
		Trace trace=Helper.createTrace();
		Period period=getSelectedPeriod();
		period.setEtat(status);
		period.getTrace().setDateMaj(trace.getDateMaj());
		period.getTrace().setAuteurMaj(trace.getAuteurMaj());
		
		commonService.updateObject(period);
		setPeriodList(null);
		logger.debug("updatePeriod: statut modifié à {}",status);
		
		
	}

	public boolean isImportCompleted() {
		return importCompleted;
	}

	public void setImportCompleted(boolean importCompleted) {
		this.importCompleted = importCompleted;
	}

	public boolean isSimulationCompleted() {
		return simulationCompleted;
	}

	public void setSimulationCompleted(boolean simulationCompleted) {
		this.simulationCompleted = simulationCompleted;
	}

	public boolean isConfigurationCompleted() {
		return configurationCompleted;
	}

	public void setConfigurationCompleted(boolean configurationCompleted) {
		this.configurationCompleted = configurationCompleted;
	}

	public boolean isValidationCompleted() {
		return validationCompleted;
	}

	public void setValidationCompleted(boolean validationCompleted) {
		this.validationCompleted = validationCompleted;
	}
	public boolean isAddToParamAutorized() {
		if(!isSimulationCompleted())return false;
		if(!isPeriodEditable()) return false;
		if(isNonFonctNature(getDaiList())) return false;
		if(isInsufficientAvailable(getDaiList())) return false;
		
		return isAddEiRequested(getDaiList());
	}

	public List<DisplayStruct> getDisplayList() {
		return displayList;
	}

	public void setDisplayList(List<DisplayStruct> displayList) {
		this.displayList = displayList;
	}
	
	public void searchConsultation() {
		logger.debug("searchConsultation: {}",getExec());
		List<DisplayStruct> list=commonService.executeMeta4DaiList(getExec());
		setDisplayList(list);
		logger.debug("searchConsultation: end");
	}
	public boolean isDisplayListVisible() {	
		return !CollectionUtils.isEmpty(getDisplayList());
	}
	public void openEIDetailsDialog() {	
		DialogHelper.openEiView();
	}

	public DisplayStruct getSelectedEi() {
		return selectedEi;
	}

	public void setSelectedEi(DisplayStruct selectedEi) {
		this.selectedEi = selectedEi;
	}
	
	public StreamedContent getXlsImportedDisplayList() {
		ExcelHandler excel = null;
		logger.debug("getXlsImportedDisplayList: start");
		StreamedContent returnStreamedContent = null;
		excel = new ExcelHandler(ExcelModelEnum.META4DAI_CONSULT, getDisplayList());
		returnStreamedContent = excel.getExcelFile();
		logger.debug("getXlsImportedDisplayList: end");
		return returnStreamedContent;

	}

	@Override
	protected String getModuleName() {
		return "meta4dai";
	}

	@Override
	protected SimpleEntity[] getRequirements() {
		return new SimpleEntity[] {
			new	SimpleEntity("PaieMeta4DAIperiode","U"),
			new	SimpleEntity("PaieMeta4DAIMvt","U"),
			new	SimpleEntity("PaieMeta4DAIEI","U"),
			new	SimpleEntity("PaieMeta4DAILBC","U"),
			new	SimpleEntity("PsPaieMeta4DAICree","P"),
			new	SimpleEntity("PsPaieMeta4DAICtrl","P"),
			new	SimpleEntity("PsPaieMeta4DAIList","P")
			};
	}

	public List<String> getPeriodExerciseList() {
		if(periodExerciseList==null) {
			this.periodExerciseList=commonService.getMeta4DaiExercices()
					.stream().map(i-> String.valueOf(i))
					.collect(Collectors.toList());
		}
		return periodExerciseList;
	}

	public void setPeriodExerciseList(List<String> periodExerciseList) {
		this.periodExerciseList = periodExerciseList;
	}	

	
	
}
