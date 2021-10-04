package fr.symphonie.tools.sct;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.event.ValueChangeEvent;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.util.Strings;
import org.iban4j.IbanUtil;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.StreamedContent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.symphonie.budget.ui.beans.NavigationBean.Action;
import fr.symphonie.budget.ui.excel.ExcelHandler;
import fr.symphonie.budget.ui.excel.ExcelModelEnum;
import fr.symphonie.common.core.ICommonService;
import fr.symphonie.common.model.AppCfgConfig;
import fr.symphonie.common.model.IFlatLine;
import fr.symphonie.common.util.BudgetHelper;
import fr.symphonie.common.util.Constant;
import fr.symphonie.common.util.MsgEntry;
import fr.symphonie.common.util.SepaHelper;
import fr.symphonie.common.util.Util;
import fr.symphonie.cpwin.model.pk.SepaBicPK;
import fr.symphonie.cpwin.model.sepa.Actor;
import fr.symphonie.cpwin.model.sepa.Bic;
import fr.symphonie.exception.MissingConfiguration;
import fr.symphonie.tools.common.GenericExcelImportor;
import fr.symphonie.tools.sct.model.PaymentModeEnum;
import fr.symphonie.tools.sct.model.RefundsBatch;
import fr.symphonie.tools.sct.model.RefundsItem;
import fr.symphonie.util.HandlerJSFMessage;
import fr.symphonie.util.Helper;
import fr.symphonie.util.model.SimpleEntity;
import fr.symphonie.util.model.Trace;

@ManagedBean(name = "sctBean")
@SessionScoped
public class SepaCreditTransfertBean extends GenericExcelImportor<RefundsItem> implements Serializable{

	private static final long serialVersionUID = -5167181403987385560L;
	private static final Logger logger =LoggerFactory.getLogger(SepaCreditTransfertBean.class);

	@ManagedProperty (value="#{commonService}")
	private ICommonService commonService;
	private List<RefundsItem> creditTransfertList;
	private List<RefundsItem> sepaFrPaymentList;
	private List<RefundsItem> nonSepaFrPaymentList;
	private List<RefundsItem> nonSepaPaymentList;
//	private List<String> dateEchangeList;
	private List<String> dateReglementList;
	private List<String> paysNonSepaFrList;
	private String dateReglement;
	private String objet;
	private boolean programEnabled = false;
	private String codeProg;
	private String libProgramme;
	private String tva;
	private List<String> listTva;	

	private boolean validationFrCompleted=false;
	private boolean validationOutFrCompleted=false;
	private boolean validationOutSepaCompleted=false;
	private boolean validationCompleted;
	private boolean generationCompleted=false;
//	private boolean generateSepaFrExecuted=false;
//	private boolean generateHorsSepaFrExecuted=false;
	private boolean validateRMH=false;
	private boolean generateRMHExecuted=false;
	
	
	private RefundsBatch vague;
	
	private int vagueOrClient;
	private List<Integer> vagueIds;
	private List<String> vagueExerciseList;
	private List<Integer> notGeneratedVagueIds;
	private List<RefundsItem> refundsByCustomer;
	
	private Integer noVague;
	private Actor selectedCustomer;
	private boolean enabledConsultData=false;
	
	private Integer matriculeCounter;
	
	
	public void importer() {
		logger.info("importer : Début file={}",getImportFileUploadEvent().getFile().getFileName());
		try {
			List<RefundsItem> importedData=super.importFile(0, RefundsItem.class);
			processData(importedData);
			
		} catch (Exception e) {
			//e.printStackTrace();
			HandlerJSFMessage.addErrorMessage(Util.getErrorMessageFromException(MsgEntry.FAILED, e));
		}
		logger.info("importer : Fin ");
	}
	
	private void processData(List<RefundsItem> importedData) throws MissingConfiguration {
		logger.info("processData : Début ");
		fillCompletData(importedData);
		checkData(importedData);
		
		if(!hasErrors()) {
			setCreditTransfertList(importedData);
			calculateLists();
		}
		//logger.info("processData : Fin : {}",importedData.size());
	}

	private void saveVague() {
		logger.debug("saveVague ...");
		RefundsBatch vague=new RefundsBatch(getExec(),getNextNoVague(),getSourceFileName(),getCrc32Value());
		vague.setTrace(Helper.createTrace());
		commonService.saveVague(vague);	
		logger.debug("saveVague : end");
	}

	private void checkData(List<RefundsItem> importedData) {
		logger.info("checkData : Début ");
		SimpleEntity s=null;
		String name=null, bic=null, iban=null, objet=null;
		BigDecimal amount=null;
		Integer index=null;
		for(RefundsItem sct:importedData) {
			name=sct.getActor().getFullName();
			bic=sct.getActor().getBic();
			iban=sct.getActor().getIban();
			amount=sct.getAmount();
			objet=sct.getObject();
			index=sct.getRowNum();
			logger.info("checkData row {}  ",sct);
			if(!StringUtils.isBlank(iban)) {
				if(!SepaHelper.isValidIban(iban)) {
				s=new SimpleEntity(String.format("%s - %s",name,iban),String.format("ligne [%d] - [%s] : %s : IBAN non valide",index,name,iban));
				getErrorReport().add(s);
				}
				else if(SepaHelper.isFrenchIban(iban))
				{
					bic=getBIC(iban);
					if(StringUtils.isBlank(bic)) {
						s=new SimpleEntity(String.format("%s - %s",name,iban),String.format("ligne [%d] - [%s] : %s : BIC non trouvé",index,name,iban));
						getErrorReport().add(s);
					}
					else {
						sct.getActor().setBic(bic);
					}					
				}
				else if(StringUtils.isBlank(bic)) {
					s=new SimpleEntity(String.format("%s - %s",name,bic),String.format("ligne [%d] - [%s] : %s : BIC Obligatoire pour SEPA hors France",index,name,bic));
					getErrorReport().add(s);
				}
				else if(!SepaHelper.isValidBic(bic)) {
					s=new SimpleEntity(String.format("%s - %s",name,bic),String.format("ligne [%d] - [%s] : %s : BIC non valide",index,name,bic));
					getErrorReport().add(s);
				}
			}			
			
			if((amount!=null)&&(amount.doubleValue()<=0)) {
				s=new SimpleEntity(index+"-"+name,String.format("ligne [%d] - [%s] : %.2f : Montant non valide",index,name,amount.floatValue()));
				getErrorReport().add(s);
			}
			if(StringUtils.isBlank(objet)) {
				s=new SimpleEntity(index+"-"+name,String.format("ligne [%d] - [%s] : %s : Objet non renseigné",index,name,objet));
				getErrorReport().add(s);
			}

		}
		logger.info("checkData : Fin ");
		
	}

	private void fillCompletData(List<RefundsItem> importedData) throws MissingConfiguration {
		Integer counter=null;
		boolean isRMH=isRMH();

		if(isRMH)  counter=getMatriculeCounter()+1;			
		for(RefundsItem item : importedData) {
			if(StringUtils.isBlank(item.getObject())) item.setObject(getObjet());
			if(isRMH) item.getActor().setCode(String.format("%015d", counter++));
		
		}
		setMatriculeCounter(counter);
	}
	private String getBIC(String iban) {
		if(StringUtils.isBlank(iban) )return null;
		String codeBank=IbanUtil.getBankCode(iban);
		String branchCode=SepaHelper.getIbanBranchCode(iban);

		SepaBicPK pk=new SepaBicPK(codeBank,branchCode);
		List<Bic> result= commonService.findBicList(pk, null);
		String bic=!result.isEmpty()?result.get(0).getValue():null;
//		logger.info("getBIC iban={} ---> bic={}  ",iban,bic);
		return bic;
	}

	@Override
	protected String getSavedPath() throws MissingConfiguration {
		return Constant.getProjectRootPath().resolve("Sct").toString();
	}

	@Override
	protected String getImportFileName() {
		return getSourceFileName();
	}

	public List<RefundsItem> getCreditTransfertList() {
		return creditTransfertList;
	}

	public void setCreditTransfertList(List<RefundsItem> creditTransfertList) {
		this.creditTransfertList = creditTransfertList;
	}

	public void reset() {
		resetDynamicList();
		resetEnteteList();
	}
	@Override
	public void resetDynamicList() {
		super.resetDynamicList();
		setMatriculeCounter(null);
		setCreditTransfertList(null);
	
		setSepaFrPaymentList(null);
		setNonSepaFrPaymentList(null);
		setNonSepaPaymentList(null);
		
		setValidationCompleted(false);
		setValidationFrCompleted(false);
		setValidationOutFrCompleted(false);
		setValidationOutSepaCompleted(false);
		setValidateRMH(false);		
		setGenerateRMHExecuted(false);
		setGenerationCompleted(false);		
		
		setRefundsByCustomer(null);
		setVague(null);
		
	}
	private void resetEnteteList() {
		setNoVague(null);
		setDateReglementList(null);
		setDateReglement(null);
		setCodeProg(null);
		setImportFileUploadEvent(null);
		setObjet(null);
		setProgramEnabled(false);
		setTva(null);
		setEnabledConsultData(false);	
		setVagueIds(null);
		setVagueOrClient(1);
		setNotGeneratedVagueIds(null);
		setCodeBudget(null);
		setListBudgets(null);
//		setVagueExerciseList(null);
	}

	private void resetConsultationMode() {		
		setRefundsByCustomer(null);
		setVague(null);
		setEnabledConsultData(false);
		setCreditTransfertList(null);
		setVagueIds(null);
//		setVagueExerciseList(null);
		
	}
	public String getDateReglement() {
		return dateReglement;
	}

	public void setDateReglement(String dateReglement) {
		this.dateReglement = dateReglement;
	}
	public void executeGeneration() {

		try {
			Integer noEcritureRBSF = generate(getSepaFrPaymentList(), PaymentModeEnum.RBSF.getValue());
			Integer noEcritureRBSI = generate(getNonSepaFrPaymentList(), PaymentModeEnum.RBSI.getValue());
			
			String message = HandlerJSFMessage.formatMessage(HandlerJSFMessage.getMessage(MsgEntry.REMB_GENERATION_MSG),
					new Object[] { getNextNoVague(), PaymentModeEnum.RBSF.getValue(), noEcritureRBSF,PaymentModeEnum.RBSI.getValue(), noEcritureRBSI});
			HandlerJSFMessage.addInfoMessage(message);
			
			setGenerationCompleted(true);
			if(isActionGeneration()) {
				setNotGeneratedVagueIds(null);
				setNoVague(null);
			}
		} catch (Exception e) {
			HandlerJSFMessage.addErrorMessage(Util.getErrorMessageFromException(MsgEntry.FAILED, e));
			logger.error("genererSepa: ", e.getMessage());
		}
	}


	private Integer generate(List<RefundsItem> paymentList, String paymentMode) {
		return commonService.refundsGeneration(getExec(),getCodeBudget(),paymentMode,getDateReglement(),getNextNoVague());
	}
	
	private void save(List<RefundsItem> paymentList, String paymentMode) {
		logger.debug("save : {}",paymentMode);
		Integer noVague=getNextNoVague();
		Integer exercice=Helper.stringToInt(getExercice());
		Trace trace=Helper.createTrace();
		boolean isRMH=isRMH();
		paymentList.forEach(x -> {
			x.setTrace(trace);
			x.setPaymentMode(paymentMode);
			x.setNoVague(noVague);
			x.setExercice(exercice);
			if(isRMH) x.setDateBordereau(trace.getDateCreation());
		});
		commonService.saveSepaPaymentList(paymentList);
		logger.debug("save : end");
	}

public ICommonService getCommonService() {
		return commonService;
	}

public void setCommonService(ICommonService commonService) {
		this.commonService = commonService;
	}
	
	public boolean isCommonRequiredDone() {
		String exercice = getExercice();
		if (exercice == null || exercice.isEmpty())
			return false;
		return true;
	}
	public boolean isRequiredDataDone() {

		if (!isCommonRequiredDone())
			return false;

		if (getImportFileUploadEvent() == null)
			return false;
		if((!isRMH()) && StringUtils.isBlank(getDateReglement()))
			return false;
		return true;
	}
	public boolean isSearchDataDone() {

		if (!isCommonRequiredDone())
			return false;

		if (getNoVague() == null)
			return false;
		
		return true;
	}

	public boolean isImportedDataVisible() {

		return !CollectionUtils.isEmpty(getCreditTransfertList());
	}
	
	public boolean isDataVisible()
	   {
		if(isErrorReportVisible()) return true;
		if(isImportedDataVisible())return true;
			return false; 
			
	   }

	public String getObjet() {
		if(getCodeProg()!=null)
			return getLibProgramme();
		return objet;
	}

	public void setObjet(String objet) {
		this.objet = objet;
	}
	
//	public List<String> getDateEchangeList() {
//		if(dateEchangeList==null) {
//			dateEchangeList=commonService.getDateEchangeList();
//		}
//		return dateEchangeList;
//	}
//
//	public void setDateEchangeList(List<String> dateEchangeList) {
//		this.dateEchangeList = dateEchangeList;
//	}

	public List<String> getDateReglementList() {
		if(dateReglementList==null) {
		load(TaskEnum.dateReglement);
		}
		return dateReglementList;
	}

	public void setDateReglementList(List<String> dateReglementList) {
		this.dateReglementList = dateReglementList;
	}
		public List<RefundsItem> getSepaFrPaymentList() {
		return sepaFrPaymentList;
	}

	public void setSepaFrPaymentList(List<RefundsItem> sepaFrPaymentList) {
		this.sepaFrPaymentList = sepaFrPaymentList;
	}

	public List<RefundsItem> getNonSepaFrPaymentList() {
		return nonSepaFrPaymentList;
	}

	public void setNonSepaFrPaymentList(List<RefundsItem> nonSepaFrPayementList) {
		this.nonSepaFrPaymentList = nonSepaFrPayementList;
	}

	public List<RefundsItem> getNonSepaPaymentList() {
		return nonSepaPaymentList;
	}

	public void setNonSepaPaymentList(List<RefundsItem> nonSepaPaymentList) {
		this.nonSepaPaymentList = nonSepaPaymentList;
	}

	public List<String> getPaysNonSepaFrList() {
		if(paysNonSepaFrList==null) {
			load(TaskEnum.paysNonSepaFR);
			logger.debug("paysNonSepaFrList: {}",this.paysNonSepaFrList);
		}
		return paysNonSepaFrList;
	}

	public void setPaysNonSepaFrList(List<String> paysNonSepaFrList) {
		this.paysNonSepaFrList = paysNonSepaFrList;
	}
	public  Predicate<RefundsItem> isSepaOrNonSepa() {
        return x ->  (getPaysNonSepaFrList().contains(x.getActor().getIban().substring(0, 2)));
    }
	public  Predicate<RefundsItem> isFrenchIban() {
        return x -> SepaHelper.isFrenchIban(x.getActor().getIban()) ;
    }
	private void calculateLists() {
		if(getCreditTransfertList()==null || getCreditTransfertList().isEmpty())return;
		Map<Boolean, List<RefundsItem>> listFrOrNonFr = getCreditTransfertList().stream()
				.collect(Collectors.partitioningBy(isFrenchIban()));
		setSepaFrPaymentList(listFrOrNonFr.get(true));
		
		Map<Boolean, List<RefundsItem>> listSepaOrNonSepa = listFrOrNonFr.get(false).stream()
				.collect(Collectors.partitioningBy(isSepaOrNonSepa()));
		setNonSepaFrPaymentList(listSepaOrNonSepa.get(true));
		setNonSepaPaymentList(listSepaOrNonSepa.get(false));
		logger.debug("calculateLists: SepaFrPaymentList: {}, NonSepaFrPayementList: {}, NonSepaPayementList: {}  ",getSepaFrPaymentList().size(),getNonSepaFrPaymentList().size(),getNonSepaPaymentList().size());
	}
	private void calculateLists(List<RefundsItem> refundsList) {
		if(refundsList==null || refundsList.isEmpty())return;
		Map<Boolean, List<RefundsItem>> listSfOrNonSf = refundsList.stream()
				.collect(Collectors.partitioningBy(x -> PaymentModeEnum.RBSF.getValue().equalsIgnoreCase(x.getPaymentMode())));
		setSepaFrPaymentList(listSfOrNonSf.get(true));
		Map<Boolean, List<RefundsItem>> listSIOrNonSI = listSfOrNonSf.get(false).stream()
				.collect(Collectors.partitioningBy(x -> PaymentModeEnum.RBSI.getValue().equalsIgnoreCase(x.getPaymentMode())));
		setNonSepaFrPaymentList(listSIOrNonSI.get(true));
		setNonSepaPaymentList(listSIOrNonSI.get(false));

		logger.debug("calculateLists: SepaFrPaymentList: {}, NonSepaFrPayementList: {}, NonSepaPayementList: {}  ",getSepaFrPaymentList().size(),getNonSepaFrPaymentList().size(),getNonSepaPaymentList().size());
	}
	
	public void resetProgram() {
		setCodeProg(null);
	}

	public boolean isProgramEnabled() {
		return programEnabled;
	}

	public void setProgramEnabled(boolean programEnabled) {
		this.programEnabled = programEnabled;
	}
	
	public void saveData() {
		
		try {
			save(getSepaFrPaymentList(),PaymentModeEnum.RBSF.getValue());
			save(getNonSepaFrPaymentList(),PaymentModeEnum.RBSI.getValue());
			save(getNonSepaPaymentList(),PaymentModeEnum.RBHSI.getValue());
			saveVague();
			setValidationCompleted(true);
			
			String message = HandlerJSFMessage.formatMessage(HandlerJSFMessage.getMessage(MsgEntry.REMB_VALIDATION_MSG),
					new Object[] { getNextNoVague()});
			HandlerJSFMessage.addInfoMessage(message);
		} catch (Exception e) {
			e.printStackTrace();
			HandlerJSFMessage.addErrorMessage(Util.getErrorMessageFromException(MsgEntry.FAILED, e));
			logger.error("saveData: Error: ", e.getMessage());
		}
	}
	
    public void executeFrValidation() {
    	setValidationFrCompleted(true);   	
		
	}
    public void executeOutFrValidation() {
    	setValidationOutFrCompleted(true);		
 	}

	public String getTva() {
		return tva;
	}

	public void setTva(String tva) {
		this.tva = tva;
	}
	public List<String> getListTva() {
		if(listTva==null)
		{
			listTva=Arrays.asList(Constant.TVA_LIST);
		}
		return listTva;
	}

	public void setListTva(List<String> listTva) {
		this.listTva = listTva;
	}
	public boolean isImportAutorized() {	

		if(getDateReglement()==null || getDateReglement().isEmpty())
			return false;
		if (Strings.isBlank(getObjet()))
			return false;
		if (!isImportedDataVisible())
			return false;		
		
		return true;
	}
	public boolean isValidationFrCompleted() {
		return validationFrCompleted;
	}

	public void setValidationFrCompleted(boolean canGenerateSepaFr) {
		this.validationFrCompleted = canGenerateSepaFr;
	}

	public boolean isValidationOutFrCompleted() {
		return validationOutFrCompleted;
	}

	public void setValidationOutFrCompleted(boolean canGenerateHorsSepaFr) {
		this.validationOutFrCompleted = canGenerateHorsSepaFr;
	}

	public RefundsBatch getVague() {
		return vague;
	}

	public void setVague(RefundsBatch vague) {
		this.vague = vague;
	}
	public void vagueClientChangeListener(ValueChangeEvent e) {
		resetConsultationMode();

	}
	public void loadVague() {
		logger.debug("loadVague: vague={}",getNoVague());
		List<RefundsItem> refundsList = commonService.getRefundsByVague(getNoVague());
		
		setCreditTransfertList(refundsList);
		calculateLists(refundsList);
	}
	public void consultRembours() {
		
		setCreditTransfertList(null);
		if(isConsultationByVague()) {
			if(getNoVague()==null)return;
			consultationByVague();
		}
		else {
			
			if((getSelectedCustomer()==null) || StringUtils.isBlank(getSelectedCustomer().getName())) return;
			consultationByCustomer();
		}
		setEnabledConsultData(true);
	}

	private void consultationByCustomer() {
		String customerName=getSelectedCustomer().getName();
		logger.debug("consultationByCustomer: customerName={}",customerName);
		List<RefundsItem> refundsList=commonService.getRefundsByCustomer(getExec(),customerName);
		setRefundsByCustomer(refundsList);
		logger.debug("consultationByCustomer: customerName={} --> size={}",customerName,((refundsList!=null)?refundsList.size():0));
		
	}

	private void consultationByVague() {
		
		List<RefundsItem> refundsList = commonService.getRefundsByVague(getNoVague());
		
		setCreditTransfertList(refundsList);
		calculateLists(refundsList);

		setVague(new RefundsBatch(Helper.stringToInt(getExercice()), getNoVague()));
		getVague().setSepaFr(prepareVagueItem(getSepaFrPaymentList(), PaymentModeEnum.RBSF.getValue()));
		getVague().setHorsSepaFr(prepareVagueItem(getNonSepaFrPaymentList(), PaymentModeEnum.RBSI.getValue()));
		getVague().setHorsSepa(prepareVagueItem(getNonSepaPaymentList(), PaymentModeEnum.RBHSI.getValue()));
	}

	private RefundsItem prepareVagueItem(List<RefundsItem> refundsList, String paymentMode) {
		RefundsItem vagueItem = new RefundsItem(paymentMode);
		if (!refundsList.isEmpty()) {
			RefundsItem firstItem = refundsList.get(0);
			vagueItem.setDateBordereau(firstItem.getDateBordereau());
			vagueItem.setNoBordereau(firstItem.getNoBordereau());
			vagueItem.setNoEcriture(firstItem.getNoEcriture());
			vagueItem.setPaymentMode(paymentMode);
			vagueItem.setActor(new Actor());
			Double sum = refundsList.stream().map(x -> x.getAmount())
					.collect(Collectors.summingDouble(BigDecimal::doubleValue));
			vagueItem.setAmount(new BigDecimal(sum));
		}
		return vagueItem;
	}

	public int getVagueOrClient() {
		return vagueOrClient;
	}

	public void setVagueOrClient(int vagueOrClient) {
		this.vagueOrClient = vagueOrClient;
		
	}
	
	public boolean isTabVagueEnabled() {
		if(vagueOrClient==1)return true;
		return false;
	}
	public boolean isConsultationByVague() {
		return (vagueOrClient==1);
	}

	public List<Integer> getVagueIds() {
		if(vagueIds==null) {
			this.vagueIds=commonService.getVagueIds(Helper.stringToInt(getExercice()),false);
		}
		return vagueIds;
	}

	public void setVagueIds(List<Integer> vagueIds) {
		this.vagueIds = vagueIds;
	}

	public List<RefundsItem> getRefundsByCustomer() {
		return refundsByCustomer;
	}

	public void setRefundsByCustomer(List<RefundsItem> refundsByCustomer) {
		this.refundsByCustomer = refundsByCustomer;
	}
	public Integer getNextNoVague() {
		if(noVague==null) {
			this.noVague=commonService.getLastNoVague();
			if(this.noVague==null) this.noVague=0;
			this.noVague+=1;
		}
		return noVague;
	}

	public Integer getNoVague() {
		return noVague;
	}

	public void setNoVague(Integer noVague) {
		this.noVague = noVague;
	}
	
	
	public boolean isEnableVagPanel() {
		if(getNoVague()!=null) return true;
				return false;
	}

	public List<Actor> autoCompleteTiers(String prefix) {
		logger.debug("###### Auto Complete Tiers prefix=" + prefix);
		List<Actor> tiersList = null;

		try {
			tiersList = commonService.findRefundsTiersList(!(Helper.isSpecialPrefix(prefix)),
					Helper.removeSpecialPrefix(prefix));
		} catch (Exception e) {

		}

		if (logger.isDebugEnabled()) {

			logger.debug("###### Auto Complete Tiers prefix=" + prefix + ", " +((tiersList!=null)? tiersList.size():0)
					+ " éléments trouvés - end");
		}
		return tiersList;
	}

	
	public Actor getSelectedCustomer() {
		if(selectedCustomer==null) return new Actor();
		return selectedCustomer;
	}

	public void setSelectedCustomer(Actor selectedCustomer) {
		logger.debug("setSelectedCustomer : {}",selectedCustomer);
		this.selectedCustomer = selectedCustomer;
	}
	public boolean isEnabledConsultData() {
		return enabledConsultData;
	}

	public void setEnabledConsultData(boolean enabledConsultData) {
		this.enabledConsultData = enabledConsultData;
	}

	@Override
	public void fileUploadHandler(FileUploadEvent event) {
		super.fileUploadHandler(event);
		setNoVague(null);
		try {
			List<RefundsBatch> vagueList = commonService.getVagueList(getExec(), getCrc32Value());
			if (!vagueList.isEmpty()) {
				String msg = HandlerJSFMessage.formatMessage(HandlerJSFMessage.getMessage(MsgEntry.REMB_CRC_MSG),
						new Object[] { vagueList.get(0).getNumero() });
				HandlerJSFMessage.addWarnMessage(msg);
			}
		} catch (Exception e) {
			HandlerJSFMessage.addErrorMessage(Util.getErrorMessageFromException(MsgEntry.FAILED, e));
			logger.error("genererSepaFr: ", e.getMessage());
		}
	}
	
	public void executeOutSepaValidation() {
		setValidationOutSepaCompleted(true);

	}

	public String getCodeProg() {
		return codeProg;
	}

	public void setCodeProg(String codeProg) {
		logger.debug("setCodeProg: {}",codeProg);
		
		this.codeProg = codeProg;
		if(codeProg==null)setLibProgramme("");
		else
			setLibProgramme(BudgetHelper.getlib(codeProg, BudgetHelper.getLoaderBean().getListPrograme()));
	}

	public String getLibProgramme() {
		return libProgramme;
	}

	public void setLibProgramme(String libProgramme) {
		logger.debug("setLibProgramme: {}",libProgramme);
		this.libProgramme = libProgramme;
	}

	public StreamedContent getXlsImportedCustomerList() {
		ExcelHandler excel = null;
		logger.debug("getXlsImportedCustomerList: start");
		StreamedContent returnStreamedContent = null;
		excel = new ExcelHandler(ExcelModelEnum.REMB_CLIENT_CONSULT, getRefundsByCustomer());
		returnStreamedContent = excel.getExcelFile();
		logger.debug("getXlsImportedCustomerList: end");
		return returnStreamedContent;

	}
	public void genererRMH()
	{
		logger.debug("genererRMH: Début");
		List<RefundsItem> transfertList=new ArrayList<RefundsItem>();
		try {
			transfertList.addAll(getSepaFrPaymentList());
			transfertList.addAll(getNonSepaFrPaymentList());
			generateRMHFile(transfertList);
			saveRMH();
			saveVague();
			saveMatriculeCounter();
			setGenerateRMHExecuted(true);
			String message = HandlerJSFMessage.formatMessage(HandlerJSFMessage.getMessage(MsgEntry.RMH_DDFiP_VALID_MSG),
					new Object[] { getNextNoVague() });
			HandlerJSFMessage.addInfoMessage(message);
		} catch (Exception e) {
			e.printStackTrace();
			HandlerJSFMessage.addErrorMessage(Util.getErrorMessageFromException(MsgEntry.FAILED, e));
			logger.error("genererRMH: ", e.getMessage());
		} 
		logger.debug("genererRMH: end");

	}

	private void saveMatriculeCounter() throws MissingConfiguration {
		commonService.setCounterValue(null, Constant.RembMatriculeSeq, getMatriculeCounter()-1);
		
	}

	private void generateRMHFile(List<RefundsItem> transfertList) throws MissingConfiguration {
		logger.debug("generateRMHFile ...");
		Map<String, Object> params=new HashMap<String, Object>();
		params.put(PAYMEN_SEPA_993_FileModel.CODE_COL, AppCfgConfig.getInstance().getCodeCollectiviteRMH());
		params.put(PAYMEN_SEPA_993_FileModel.CODE_NAT_DEP, "PAY");
		PAYMEN_SEPA_993_FileModel fileModel=new PAYMEN_SEPA_993_FileModel(transfertList,params);
	
		String targetFile=PAYMEN_SEPA_993_FileModel.getTargetFile(Constant.getPAYMENRootPath());

		try (PrintWriter out = new PrintWriter(
				new OutputStreamWriter(new BufferedOutputStream(new FileOutputStream(targetFile)), "US-ASCII"))) {
			for (IFlatLine item : fileModel.getBody()) {
				out.println(PAYMEN_SEPA_993_FileModel.display(item));
			}
			out.flush();
		} catch (UnsupportedEncodingException | FileNotFoundException e) {
			e.printStackTrace();
		}
		logger.debug("generateRMHFile : end");

	}
	
	public void validateRMH() {			
			setValidateRMH(true);
	}
	private void saveRMH() {
		logger.debug("saveRMH : ...");
		save(getSepaFrPaymentList(), PaymentModeEnum.RBSF.getValue());
		save(getNonSepaFrPaymentList(), PaymentModeEnum.RBSI.getValue());
		save(getNonSepaPaymentList(), PaymentModeEnum.RBHSI.getValue());
		logger.debug("saveRMH : end");
	}

	public boolean isValidateRMH() {
		return validateRMH;
	}

	public void setValidateRMH(boolean validateRMH) {
		this.validateRMH = validateRMH;
	}

	public boolean isGenerateRMHExecuted() {
		return generateRMHExecuted;
	}

	public void setGenerateRMHExecuted(boolean generateRMHExecuted) {
		this.generateRMHExecuted = generateRMHExecuted;
	}
	public StreamedContent getRmhFile() {
		StreamedContent content = null;
		
		try {	
			String filePath=PAYMEN_SEPA_993_FileModel.getTargetFile(Constant.getPAYMENRootPath());
			content =  Helper.getStreamedContentFile(new File(filePath));
			
			logger.info("Téléchargement du fichier RMH DDFiP effectué avec succés");
		} catch (Exception e) {
			logger.error("Le fichier n'a pas été trouvé");
			e.printStackTrace();
			content = Helper.getStreamedContentFile(Helper.getFileNotFound());
		}
		return content;
	}
	public boolean isGenerateRMHAutorized() {
		if(!isValidateRMH()) return false;
		if(isGenerateRMHExecuted()) return false;
		return true;
	}
	
	public StreamedContent getXlsImportedVagueDetailsList() {
		ExcelHandler excel = null;
		logger.debug("getXlsImportedVagueDetailsList: start");
		StreamedContent returnStreamedContent = null;
		excel = new ExcelHandler(ExcelModelEnum.REMB_VAGUEDETAILS_CONSULT, getCreditTransfertList());
		returnStreamedContent = excel.getExcelFile();
		logger.debug("getXlsImportedVagueDetailsList: end");
		return returnStreamedContent;

	}
	public double getTotalByPaymentMode(String paymentMode) {
		logger.debug("getTotalByPaymentMode: {}",paymentMode);
		double total=0d;
		List<RefundsItem> list=null;
		
		if(PaymentModeEnum.RBSF.getValue().equals(paymentMode))list=getSepaFrPaymentList();
		else if(PaymentModeEnum.RBSI.getValue().equals(paymentMode))list=getNonSepaFrPaymentList();
		else if(PaymentModeEnum.RBHSI.getValue().equals(paymentMode))list=getNonSepaPaymentList();
		if(list!=null) {
			total=list.stream().mapToDouble(x -> x.getAmount().doubleValue()).sum();
		}
		return total;
	}
	private boolean isRMH() {
		return (BudgetHelper.getNavigationBean().getCurrentAction() == Action.PAYMEN_SEPA);
	}

	public Integer getMatriculeCounter() throws MissingConfiguration {
		if(matriculeCounter==null) {
			matriculeCounter=commonService.getLastCounterValue(null, Constant.RembMatriculeSeq);
		}
		return matriculeCounter;
	}

	public void setMatriculeCounter(Integer matriculeCounter) {
		this.matriculeCounter = matriculeCounter;
	}
	public void onConsultChange() {
		resetConsultationMode();
	}

	public List<Integer> getNotGeneratedVagueIds() {
		if((notGeneratedVagueIds==null) && isCommonRequiredDone()) {
			this.notGeneratedVagueIds=commonService.getVagueIds(Helper.stringToInt(getExercice()),true);
		}
		
		return notGeneratedVagueIds;
	}

	public void setNotGeneratedVagueIds(List<Integer> notGeneratedVagueIds) {
		this.notGeneratedVagueIds = notGeneratedVagueIds;
	}

	public boolean isGenerationCompleted() {
		return generationCompleted;
	}

	public void setGenerationCompleted(boolean generateSepaExecuted) {
		this.generationCompleted = generateSepaExecuted;
	}

	public boolean isValidationAutorized() {
		if (isValidationCompleted())
			return false;
		if (!isValidationFrCompleted() && isDataFrFound())
			return false;
		if (!isValidationOutFrCompleted() && isDataOutFrFound())
			return false;
		if (!isValidationOutSepaCompleted() && isDataOutSepaFound())
			return false;
		return true;
	}

	public boolean isGenerationAutorized() {
		if (isGenerationCompleted())
			return false;
		if (isActionGeneration())
			return true;
		if (!isValidationCompleted())
			return false;
		return true;
	}
	private boolean isActionGeneration() {
		return (BudgetHelper.getNavigationBean().getCurrentAction() == Action.remboursement_GENE);
	}

	public boolean isValidationOutSepaCompleted() {
		logger.debug("isValidationOutSepaCompleted={}",validationOutSepaCompleted);
		return validationOutSepaCompleted;
	}

	public void setValidationOutSepaCompleted(boolean horsSepaValidated) {
		logger.debug("setValidationOutSepaCompleted={}",horsSepaValidated);
		this.validationOutSepaCompleted = horsSepaValidated;
	}

	public boolean isValidationCompleted() {
		return validationCompleted;
	}

	public void setValidationCompleted(boolean validationCompleted) {
		this.validationCompleted = validationCompleted;
	}
	public boolean isDataFrFound() {
		return !CollectionUtils.isEmpty(getSepaFrPaymentList());
	}
	public boolean isDataOutFrFound() {
		return !CollectionUtils.isEmpty(getNonSepaFrPaymentList());
	}
	public boolean isDataOutSepaFound() {
		return !CollectionUtils.isEmpty(getNonSepaPaymentList());
	}

	@Override
	protected String getModuleName() {
		return "Sct";
	}

	@Override
	protected void prepare() {
		
		
	}
	public enum TaskEnum {
		dateReglement,
		paysNonSepaFR
	}
	private void load(TaskEnum task) {
		logger.debug("load {} ...",task.name());	
		switch(task) {
		case dateReglement:
			setDateReglementList(commonService.getDateReglementList());
			break;
		case paysNonSepaFR:
			setPaysNonSepaFrList( commonService.getPaysByProtocole(PaymentModeEnum.RBSI.getValue()));
			break;
		
		
		}
		logger.debug("load {} .END.",task.name());
	}

	@Override
	protected SimpleEntity[] getRequirements() {
		List<SimpleEntity> list=new ArrayList<SimpleEntity>();
		list.add(new	SimpleEntity("CtaRemb","U"));
		list.add(new	SimpleEntity("CtaRembVague","U"));
		if(!isRMH())list.add(new	SimpleEntity("psCtaRembGenere","P"));
		
		return list.toArray(new SimpleEntity[0]);
	}

	public List<String> getVagueExerciseList() {
		if(vagueExerciseList==null) {
			this.vagueExerciseList=commonService.getVagueExercises()
					.stream().map(i-> String.valueOf(i))
					.collect(Collectors.toList());
		}
		return vagueExerciseList;
	}

	public void setVagueExerciseList(List<String> vagueExerciseList) {
		this.vagueExerciseList = vagueExerciseList;
	}	

}
