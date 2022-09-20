package fr.symphonie.tools.nantes;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import javax.faces.context.FacesContext;
import javax.faces.event.PhaseId;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.iban4j.IbanUtil;
import org.primefaces.event.CellEditEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.symphonie.budget.core.model.pluri.LigneBudgetaireAE;
import fr.symphonie.budget.ui.converter.EmailValidator;
import fr.symphonie.common.core.ICommonService;
import fr.symphonie.common.model.AppCfgConfig;
import fr.symphonie.common.util.BudgetHelper;
import fr.symphonie.common.util.Constant;
import fr.symphonie.common.util.EmailSender;
import fr.symphonie.common.util.MsgEntry;
import fr.symphonie.common.util.SepaHelper;
import fr.symphonie.common.util.Util;
import fr.symphonie.cpwin.core.demat.IDematService;
import fr.symphonie.cpwin.core.model.PieceTypeEnum;
import fr.symphonie.cpwin.model.Tiers;
import fr.symphonie.cpwin.model.nantes.EmailStatus;
import fr.symphonie.cpwin.model.nantes.EtudiantPj;
import fr.symphonie.cpwin.model.nantes.EtudiantStatus;
import fr.symphonie.cpwin.model.pk.SepaBicPK;
import fr.symphonie.cpwin.model.sepa.Bic;
import fr.symphonie.exception.MissingConfiguration;
import fr.symphonie.tools.common.GenericExcelImportor;
import fr.symphonie.tools.nantes.model.Etudiant;
import fr.symphonie.util.FileHelper;
import fr.symphonie.util.HandlerJSFMessage;
import fr.symphonie.util.Helper;
import fr.symphonie.util.model.SimpleEntity;
import fr.symphonie.util.model.Trace;

public class StudentBean extends GenericExcelImportor<Etudiant> implements Serializable {
	private static final Logger logger = LoggerFactory.getLogger(StudentBean.class);
	private static final long serialVersionUID = -6546852817449427784L;
	
	private ICommonService commonService;
	
	/**
	 * Status elements
	 */
	private boolean importCompleted;
	private boolean saveCompleted;
	private boolean notificationStarted;
	private boolean relanceStarted;
	private boolean transfertCompleted;

	/**
	 * Output elements
	 */
	private List<Etudiant> etudiantList;
	private EtudiantStatus selectedStatus;
	private Etudiant selectedEtudiant;

	private List<Etudiant> searchEtudiantList;
	private String selectedMatricule;
	private boolean student = true;
	


public StudentBean() {
		super();
		logger.debug("StudentBean: INIT: start");
		this.commonService=BudgetHelper.getCommonService();
		logger.debug("StudentBean: INIT: commonService={}",commonService);
	}

@Override
protected ICommonService getCommonService() {
	return this.commonService;
}

public void executeImport() {
	logger.info("executeImport : Début file={}", getImportFileUploadEvent().getFile().getFileName());
	try {
		List<Etudiant> importedData = super.importFile(0, Etudiant.class);
		processData(importedData);
		if (!hasErrors()) {
			setEtudiantList(importedData);
			setImportCompleted(true);
			setSaveCompleted(false);
			setNotificationStarted(false);
		}

	} catch (Exception e) {
		HandlerJSFMessage.addErrorMessage(Util.getErrorMessageFromException(MsgEntry.FAILED, e));
	}
	logger.info("importer :  Fin ");
}

@Override
public void reset() {
	super.reset();
	setEtudiantList(null);
	setImportCompleted(false);
	setSaveCompleted(false);
	setTransfertCompleted(false);
	setNotificationStarted(false);
	setRelanceStarted(false);
	resetEnteteList();
	resetSearch();
}

public void executeSave() {

	logger.debug("executeSave ...");
	try {
		List<Etudiant> updateableList=getEtudiantList().stream().filter(x-> x.isUpdateable()).collect(Collectors.toList());
		logger.debug("executeSave: {} updateable item  ...",updateableList.size());
		
		saveEtudiantList(updateableList);
		HandlerJSFMessage.addInfo(MsgEntry.SUCCES);
		setSaveCompleted(true);

	} catch (Exception e) {
		HandlerJSFMessage.addErrorMessage(Util.getErrorMessageFromException(MsgEntry.FAILED, e));
	}
	logger.debug("executeSave : end");

}
private void saveEtudiantList(List<Etudiant> list) {
	Trace trace = Helper.createTrace();
	list.stream().forEach(x-> {
		if(x.getTrace()==null) {
		x.setTrace(trace);
		}
		else {
			x.getTrace().setAuteurMaj(trace.getAuteurMaj());
			x.getTrace().setDateMaj(trace.getDateMaj());
		}
	});
	getCommonService().createOrUpdateList(list);
	
}

private void notifyByEmail(List<Etudiant> list, String contextPath) {
	if (list == null)	return;
	try {
	
	Integer EMAIL_OFFSET=50;
	Integer iterationCount=(list.size()/EMAIL_OFFSET) + 1;
	Integer iterationSize=0;
	EmailSender emailSender=getEmailSender(contextPath);
	
	String subjectTemplate = HandlerJSFMessage.getMessage(MsgEntry.ETUDIANT_MAIL_SUBJECT);
	String messageTemplate = HandlerJSFMessage.getMessage(MsgEntry.ETUDIANT_MAIL_MESSAGE);
	String site=HandlerJSFMessage.getConfigParam(MsgEntry.ETUDIANT_PUBLIC_SITE);
			
	List<Etudiant> iterationList=null;
	logger.debug("notifyByEmail: iterationCount={}, EMAIL_OFFSET={} ",iterationCount,EMAIL_OFFSET);
	for(int i=1; i<=iterationCount;i++) {
		iterationList=list.stream().skip((i-1)*iterationSize).limit(EMAIL_OFFSET).collect(Collectors.toList());
		iterationSize=iterationList.size();
		logger.debug("############### notifyByEmail: iteration={} ,{} email to send,  matricules plage :  from {} to {} ###############",i,iterationSize,iterationList.get(0).getMatricule(),iterationList.get(iterationSize-1).getMatricule());
		emailSender.sendInformationMail(iterationList,subjectTemplate,messageTemplate,getEmaiFrom(),site);
		logger.debug("############### notifyByEmail: iteration={} : Finished ###############",i);
	}
	}
	catch(Exception e) {
		logger.error("notifyByEmail: error: {}",e.getMessage());
	}
	finally {
	updateEmailStatus(EmailStatus.ENCOURS, EmailStatus.NON);
	}

}

	private String getEmaiFrom() throws MissingConfiguration {
	return AppCfgConfig.getInstance().getMailPropertiesFrom();
}

	private void relanceByEmail(List<Etudiant> list, String contextPath) {
		if (list == null)
			return;
		try {
			Integer EMAIL_OFFSET = 50;
			Integer iterationCount = (list.size() / EMAIL_OFFSET) + 1;
			Integer iterationSize = 0;
			EmailSender emailSender = getEmailSender(contextPath);
			String subjectTemplate = HandlerJSFMessage.getMessage(MsgEntry.ETUDIANT_RELANCE_SUBJECT);
			String messageTemplate = HandlerJSFMessage.getMessage(MsgEntry.ETUDIANT_RELANCE_MESSAGE);

			List<Etudiant> iterationList = null;
			logger.debug("relanceByEmail: iterationCount={}, EMAIL_OFFSET={} ", iterationCount, EMAIL_OFFSET);
			for (int i = 1; i <= iterationCount; i++) {
				iterationList = list.stream().skip((i - 1) * iterationSize).limit(EMAIL_OFFSET)
						.collect(Collectors.toList());
				iterationSize = iterationList.size();
				logger.debug(
						"############### relanceByEmail: iteration={} ,{} email to send,  matricules plage :  from {} to {} ###############",
						i, iterationSize, iterationList.get(0).getMatricule(),
						iterationList.get(iterationSize - 1).getMatricule());
				emailSender.sendRelanceMail(iterationList, subjectTemplate, messageTemplate,getEmaiFrom());
				logger.debug("############### relanceByEmail: iteration={} : Finished ###############", i);
			}
		} catch (Exception e) {
			logger.error("relanceByEmail: error: {}", e.getMessage());
		} finally {
			updateEmailStatus(EmailStatus.RELANCE_ENC, EmailStatus.ENVOYE);
		}
	}

private void updateEmailStatus(EmailStatus from, EmailStatus to) {
	getCommonService().updateEtudiantEmailStatus(from,to);	
}

	public void executeNotifications() {
		try {
			List<Etudiant> list = getEtudiantList().stream().filter(x -> x.getEmailStatus() == EmailStatus.NON)
					.collect(Collectors.toList());
			logger.debug("executeNotifications ... {} email to send", list.size());
			String contextPath = Helper.getContextPath();
			if (!CollectionUtils.isEmpty(list)) {
				updateEmailStatus(EmailStatus.NON, EmailStatus.ENCOURS);
				Thread sendEmailsThread = new Thread(() -> {
					notifyByEmail(list, contextPath);
				});
				sendEmailsThread.setName(String.format("sendEmails-%s", sendEmailsThread.getName()));
				sendEmailsThread.start();
				setNotificationStarted(true);
			}
			HandlerJSFMessage.addInfo(MsgEntry.SUCCES);
		} catch (Exception e) {
			HandlerJSFMessage.addErrorMessage(Util.getErrorMessageFromException(MsgEntry.FAILED, e));
		}
		logger.debug("executeNotifications END");

	}
public void executeRelance() {
	try {
	List<Etudiant> list = getEtudiantList().stream().filter(x->x.getEmailStatus()==EmailStatus.ENVOYE).collect(Collectors.toList());
	logger.debug("executeRelance ... {} email to send", list.size());
	String contextPath=Helper.getContextPath();
	if(!CollectionUtils.isEmpty(list)) {
		updateEmailStatus(EmailStatus.ENVOYE, EmailStatus.RELANCE_ENC);
		Thread sendEmailsThread = new Thread(() -> {
			relanceByEmail(list,contextPath);
		});
		sendEmailsThread.setName(String.format("sendRelanceEmails-%s", sendEmailsThread.getName()));
		sendEmailsThread.start();
		setRelanceStarted(true);
	}
	
	HandlerJSFMessage.addInfo(MsgEntry.SUCCES);
	} catch (Exception e) {
		HandlerJSFMessage.addErrorMessage(Util.getErrorMessageFromException(MsgEntry.FAILED, e));
	}
	logger.debug("executeRelance END");

}
public void executeSearch() {
	if(!isRequirmentsStatus())return;
	setTransfertCompleted(false);
	setNotificationStarted(false);
	setRelanceStarted(false);
	
	EtudiantStatus status=getSelectedStatus();
	logger.debug("executeSearch: start status={}",status);
	List<Etudiant> list=getCommonService().getEtudiantList(status);
	switch (status) {
	case AVEC_ERREUR:
		
		List<Etudiant> operatedList =verifyBic(list);
		logger.debug("executeSearch status={}",status);
		if(!CollectionUtils.isEmpty(operatedList)) {
			saveEtudiantList(operatedList);
			list.removeAll(operatedList);
		}
		break;

	default:
		break;
	}
	loadPj(list);
	setEtudiantList(list);
	logger.debug("executeSearch end");
}

private List<Etudiant> verifyBic(List<Etudiant> list) {
	List<Etudiant> operatedList =new ArrayList<Etudiant>();
	list.stream().filter(x -> SepaHelper.isFrenchIban(x.getIban().getIban())).forEach(x -> {
		String bic=getBIC(x.getIban().getIban());
		if(!StringUtils.isBlank(bic)) {
			x.getIban().setBic(bic);
			x.setStatus(EtudiantStatus.COMPLET);
			operatedList.add(x);
		}
		
	});
	return operatedList;
	
}

private void loadPj(List<Etudiant> list) {
	list.stream().forEach(x -> {
		List<EtudiantPj> pjList=getCommonService().getEtudiantPjList(x.getMatricule());
		x.setPj(pjList);
	});
	
}

private void processData(List<Etudiant> importedData) {
	logger.info("processData : Début ");
	checkData(importedData);
	fillCompletData(importedData);
}

private void fillCompletData(List<Etudiant> importedData) {
	String etudiantValidMsg = HandlerJSFMessage.getMessage(MsgEntry.ETUDIANT_VALIDE);
	String etudiantFoundMsg = HandlerJSFMessage.getMessage(MsgEntry.ETUDIANT_TROUVE);
	List<Etudiant> eldersStudent=commonService.getEtudiantList(null);
	logger.debug("{} found items",eldersStudent.size());
	// les nouvels étudiants
	importedData.stream().filter(isNewStudent(eldersStudent)).forEach(x ->
	{
		logger.debug("{} new student",x.getMatricule());
		x.setPassword(Util.generateCommonLangPassword());
		x.setStatus(EtudiantStatus.NON_RENSEIGNE);
		x.setEmailStatus(EmailStatus.NON);
		x.setUpdateable(true);
	}		
	);
	// les anciens étudiants
	importedData.stream().filter(isOldStudent(eldersStudent)).forEach(x ->
	{
		logger.debug("{} old student",x.getMatricule());
		Optional<Etudiant> old = eldersStudent.stream().filter(o -> o.getMatricule().equals(x.getMatricule()) ).findFirst();
		if(old.isPresent()) {
			Etudiant etudiant =old.get();
			if(etudiant.isValid()) {
			//	logger.debug("{} valid student",x.getMatricule());
				x.setObservation(etudiantValidMsg);
				x.setUpdateable(false);
			}
			else {
			//	logger.debug("{} found student",x.getMatricule());
				x.setPassword(etudiant.getPassword());
				x.setStatus(etudiant.getStatus());
				x.setEmailStatus(etudiant.getEmailStatus());
				x.setEmailDate(etudiant.getEmailDate());
				x.setObservation(etudiantFoundMsg);
				x.setUpdateable(true);
				
			}
		}
		
	});

}
private Predicate<Etudiant> isNewStudent(List<Etudiant> eldersStudent) {
	return x -> !eldersStudent.contains(x);
}
private Predicate<Etudiant> isOldStudent(List<Etudiant> eldersStudent) {
	return x -> eldersStudent.contains(x);
}

public void executeTransfert() {
	logger.debug("executeTransfert : start");
	try {
		String pjDestPath=AppCfgConfig.getInstance().getTiersPjPath();
	List<Etudiant> list=getEtudiantList().stream().filter(x-> x.getCodeCpwin()==null).collect(Collectors.toList());
	for(Etudiant x :list) {
		String codeCpwin=getCommonService().createTiers(x);
		x.setCodeCpwin(codeCpwin);
		x.setStatus(EtudiantStatus.TRANSFERE);
		getCommonService().updateObject(x);
		createPj(x,pjDestPath);
	}
	setTransfertCompleted(true);
	HandlerJSFMessage.addInfo(MsgEntry.SUCCES);
	}
 catch (Exception e) {
	HandlerJSFMessage.addErrorMessage(Util.getErrorMessageFromException(MsgEntry.FAILED, e));
}
logger.debug("executeTransfert : end");
	
	
}

private void createPj(Etudiant x,String pjDestPath) throws IOException {
	String newPjName=null,extension=null,path=null;
	if(CollectionUtils.isEmpty(x.getPj()))return;
	
	logger.debug("createPj : start: tiers={}, nb pj={}",x.getCodeCpwin(),x.getPj().size());
	
	for(EtudiantPj pj:x.getPj())
	{
		newPjName=getCpwinDematService().getDematPjName(PieceTypeEnum.TIERS,x.getCodeCpwin());
		extension=FileHelper.getFileExtension(pj.getFileName());
		path=pjDestPath+File.separator+newPjName+"."+extension;
		byte[] originalData=getPjOriginalData(pj);
		savePj(originalData,path);
		getCpwinDematService().declareDematPj(PieceTypeEnum.TIERS,x.getCodeCpwin(), path,pj.getFileName(),newPjName);
	}
	logger.debug("createPj : end");
	
}

private void savePj(byte[] originalData,String path) throws IOException {
	
	logger.debug("savePj: pjDestPath={} ",path);
	
	try (FileOutputStream fileOutputStream =new FileOutputStream(path)) {
		// Reconstriuire fichier: decoder + ungzip
		//byte[] originalData=FileHelper.ungzip(FileHelper.decodeBase64(data));
		fileOutputStream.write(originalData);
		fileOutputStream.flush();		
	}	
}

private void checkData(List<Etudiant> importedData) {
	List<String> duplicates =checkDuplicates(importedData);
	if(!CollectionUtils.isEmpty(duplicates)) {
		getErrorReport().add(new SimpleEntity("duplicates", "Duplication des matricules non acceptée: "+duplicates));
	}
	String msg=null;
	for(Etudiant data:importedData) {

		if(!EmailValidator.validate(data.getEmail())) {
			msg=String.format("Email %s non valide", data.getEmail());
			addErrorToReport(data, data.getRowNum()+"-"+data.getMatricule(), msg);
		}
		
	}

}
private List<String> checkDuplicates(List<Etudiant> importedData) {
	logger.debug("checkDuplicates");
	List<String> duplicates =importedData.stream().map(d -> d.getMatricule())		
			.collect( Collectors.groupingBy( Function.identity(), Collectors.counting() ) )
			.entrySet()
			.stream()
			.filter( p -> p.getValue()>1)
			.map( Map.Entry::getKey )
		    .collect( Collectors.toList() );
	logger.debug("checkDuplicates: {}",duplicates);
	return duplicates;
}

@Override
protected String getModuleName() {
	return "Scolarite";
}

@Override
protected void prepare() throws Exception {
		
     }

@Override
protected SimpleEntity[] getRequirements() {
	return new SimpleEntity[] {};
}
public boolean isImportCompleted() {
	return importCompleted;
}

public void setImportCompleted(boolean importCompleted) {
	this.importCompleted = importCompleted;
}

public List<Etudiant> getEtudiantList() {
	if(etudiantList==null)return etudiantList;
	Collections.sort(etudiantList);
	return etudiantList;
}

public void setEtudiantList(List<Etudiant> etudiantList) {
	this.etudiantList = etudiantList;
}

public boolean isSaveCompleted() {
	return saveCompleted;
}

public void setSaveCompleted(boolean saveCompleted) {
	this.saveCompleted = saveCompleted;
}

public boolean isNotificationStarted() {
	return notificationStarted;
}

public void setNotificationStarted(boolean notificationStarted) {
	this.notificationStarted = notificationStarted;
}

public boolean isRequiredDataDone() {
	if (!isCommonRequiredDone())
		return false;
	if (getSelectedStatus()==null)
		return false;

	return true;
}
public boolean isImportAutorized() {
	if (!isCommonRequiredDone())
		return false;
	if (getImportFileUploadEvent() == null)
		return false;

	return true;
}
public boolean isDataVisible()
{
	if(isErrorReportVisible()) return true;
	if(isImportedDataVisible())return true;
	return false; 
}
public boolean isImportedDataVisible() {

	return !CollectionUtils.isEmpty(getEtudiantList());
}

public boolean isNotificationAutorized() {
	if(!isRequirmentsStatus())return false;
	if(isNotificationStarted()) return false;
	if(!isSaveCompleted()) return false;
	return true;
}

public EtudiantStatus getSelectedStatus() {
	return selectedStatus;
}

public void setSelectedStatus(EtudiantStatus selectedStatus) {
	this.selectedStatus = selectedStatus;
}
public  EtudiantStatus[] getFolderStatus() {
	List<EtudiantStatus> result= new ArrayList<EtudiantStatus>();
	for(EtudiantStatus item : EtudiantStatus.values()) {
		if(item!=EtudiantStatus.FERME) {
			result.add(item);
		}
	}
	return result.toArray(new EtudiantStatus[4]);
}
public boolean isTranfertAutorized() {
	if(!isRequirmentsStatus())return false;
	return !isTransfertCompleted();
}

public boolean isSaveAutorized() {
	if(!isRequirmentsStatus())return false;
	if(isSaveCompleted())return false;
	if(!isImportedDataVisible())return false;
	
	return getUpdateableItemCount()>0;
}
public boolean isRelanceMailAutorized() {
	if(isRelanceStarted())return false;
	
	return getRelanceItemCount()>0;
}
public boolean isNotificationMailAutorized() {
	if(!isRequirmentsStatus())return false;
	if(isNotificationStarted())return false;
	return getNotificationItemCount()>0;
	
}
public boolean isMissingBicMailAutorized() {
	if(!isRequirmentsStatus())return false;
	if(isNotificationStarted())return false;

	return getMissingBicItemCount()>0;
	
}
public boolean isAvecErreurStatus() {
	return getSelectedStatus()==EtudiantStatus.AVEC_ERREUR;
}
public boolean isNonRenseigneStatus() {
	return getSelectedStatus()==EtudiantStatus.NON_RENSEIGNE;
}
public boolean isCompletStatus() {
	return getSelectedStatus()==EtudiantStatus.COMPLET;
}
private void resetEnteteList() {
	setSelectedStatus(null);
	setListBudgets(null);
	setCodeBudget(null);
}

	public void onStatusChange() {
		setEtudiantList(null);
		setImportCompleted(false);
		setSaveCompleted(false);
		setNotificationStarted(false);
	}

	public Etudiant getSelectedEtudiant() {
		return selectedEtudiant;
	}

	public void setSelectedEtudiant(Etudiant selectedEtudiant) {
		logger.debug("#################### setSelectedEtudiant: {}",selectedEtudiant);
		this.selectedEtudiant = selectedEtudiant;
	}
	public void onEtudiantSelected() {
		logger.debug("onEtudiantSelected: getSelectedMatricule={}",getSelectedMatricule());
		setSelectedEtudiant(searchEtudiantList.stream().filter(x->x.getMatricule().equals(getSelectedMatricule())).collect(Collectors.toList()).get(0));
	}

	public List<Etudiant> autoCompleteEtudiant(String prefix) {
		logger.debug("###### Auto Complete Etudiant prefix=" + prefix);

		try {
			searchEtudiantList = commonService.findEtudiantList(!(Helper.isSpecialPrefix(prefix)),
					Helper.removeSpecialPrefix(prefix));
		} catch (Exception e) {

		}

		if (logger.isDebugEnabled()) {

			logger.debug("###### Auto Complete Etudiant prefix=" + prefix + ", " +((searchEtudiantList!=null)? searchEtudiantList.size():0)
					+ " éléments trouvés - end");
		}
		return searchEtudiantList;
	}
	public List<Tiers> autoCompleteTiers(String prefix) {
		logger.debug("###### Auto Complete Tiers prefix=" + prefix);
		List<Tiers> result=null;
		try {
			result = commonService.findTiersList(!(Helper.isSpecialPrefix(prefix)),	Helper.removeSpecialPrefix(prefix),isStudent());
		} catch (Exception e) {

		}

		if (logger.isDebugEnabled()) {

			logger.debug("###### Auto Complete Tiers prefix=" + prefix + ", " +((result!=null)? result.size():0)
					+ " éléments trouvés - end");
		}
		return result;
	}

	private void resetSearch() {
		setSelectedMatricule(null);
		setSelectedEtudiant(null);
		this.searchEtudiantList=null;
	}

	public String getSelectedMatricule() {
		return selectedMatricule;
	}

	public void setSelectedMatricule(String selectedMatricule) {
		this.selectedMatricule = selectedMatricule;
	}

	public boolean isTransfertCompleted() {
		return transfertCompleted;
	}

	public void setTransfertCompleted(boolean transfertCompleted) {
		this.transfertCompleted = transfertCompleted;
	}
	public void onDpEdit(CellEditEvent event) {

	}

	public LigneBudgetaireAE getSelectedLb() {
		return BudgetHelper.getDataListBean().getSelectedLb();
	}

	public String getGroupNat() {		
		return (getSelectedLb()!=null)?getSelectedLb().getGroupNat():null;
	}

	@Override
	protected void resetDynamicList() {
		super.resetDynamicList();
		setEtudiantList(null);
		setImportCompleted(false);
	}

	public boolean isRelanceStarted() {
		return relanceStarted;
	}

	public void setRelanceStarted(boolean relanceStarted) {
		this.relanceStarted = relanceStarted;
	}
	public Long getUpdateableItemCount() {
		return getEtudiantList().parallelStream().filter(x-> x.isUpdateable()).count();
	}
	public Long getNotificationItemCount() {
		return getEtudiantList().parallelStream().filter(x-> x.getEmailStatus()==EmailStatus.NON).count();
	}
	public Long getRelanceItemCount() {
		return getEtudiantList().parallelStream().filter(x-> x.getEmailStatus()==EmailStatus.ENVOYE).count();
	}
	public Long getMissingBicItemCount() {
		return getEtudiantList().parallelStream()
				.filter(x-> x.getStatus()==EtudiantStatus.AVEC_ERREUR)
				.filter(x-> x.getEmailStatus()!=EmailStatus.BIC_MAIL_SEND)
				.filter(x-> x.getEmailStatus()!=EmailStatus.ENCOURS)
				.count();
	}
	/**
	 * Reconstriuire fichier: decoder + ungzip
	 * @param pj
	 * @return
	 * @throws IOException
	 */
	private byte[] getPjOriginalData(EtudiantPj pj) throws IOException {
		logger.debug("getPjOriginalData: fileDta={}",pj.getFileData());
		return FileHelper.ungzip(FileHelper.decodeBase64(pj.getFileData()));
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
	
	 public void executeMissingBICNotification() {
		logger.debug("executeMissingBICNotification: Début");
		try {
			List<Etudiant> list = getEtudiantList().stream()
					.filter(x -> x.getStatus() == EtudiantStatus.AVEC_ERREUR)
					.filter(x -> x.getEmailStatus()!=EmailStatus.BIC_MAIL_SEND)
					.filter(x -> x.getEmailStatus()!=EmailStatus.ENCOURS)					
					.collect(Collectors.toList());
			logger.debug("executeMissingBICNotification ... {} email to send", list.size());
			String contextPath = Helper.getContextPath();
			if (!CollectionUtils.isEmpty(list)) {
				//update email status
				list.stream().forEach(x -> x.setEmailStatus(EmailStatus.ENCOURS));
				saveEtudiantList(list);
				Thread sendEmailsThread = new Thread(() -> {
					notifyMissingBICByEmail(list, contextPath);
				});
				sendEmailsThread.setName(String.format("sendMissingBICEmails-%s", sendEmailsThread.getName()));
				sendEmailsThread.start();
				setNotificationStarted(true);
			}
			HandlerJSFMessage.addInfo(MsgEntry.SUCCES);
		} catch (Exception e) {
			HandlerJSFMessage.addErrorMessage(Util.getErrorMessageFromException(MsgEntry.FAILED, e));
		}
		 
		logger.debug("executeMissingBICNotification: Fin");
		
	}
	 
	private void notifyMissingBICByEmail(List<Etudiant> list, String contextPath) {
		if (list == null)	return;
		try {
		
		Integer EMAIL_OFFSET=50;
		Integer iterationCount=(list.size()/EMAIL_OFFSET) + 1;
		Integer iterationSize=0;
		EmailSender emailSender=getEmailSender(contextPath);
		
		String subjectTemplate = HandlerJSFMessage.getMessage(MsgEntry.BIC_MAIL_SUBJECT);
		String messageTemplate = HandlerJSFMessage.getMessage(MsgEntry.BIC_MAIL_MESSAGE);
		String to=HandlerJSFMessage.getConfigParam(MsgEntry.BIC_MAIL_TO);
		List<Etudiant> iterationList=null;
		logger.debug("notifyMissingBICByEmail: iterationCount={}, EMAIL_OFFSET={} ",iterationCount,EMAIL_OFFSET);
		for(int i=1; i<=iterationCount;i++) {
			iterationList=list.stream().skip((i-1)*iterationSize).limit(EMAIL_OFFSET).collect(Collectors.toList());
			iterationSize=iterationList.size();
			logger.debug("############### notifyMissingBICByEmail: iteration={} ,{} email to send,  matricules plage :  from {} to {} ###############",i,iterationSize,iterationList.get(0).getMatricule(),iterationList.get(iterationSize-1).getMatricule());
			
			emailSender.sendMissingBicMail(iterationList,subjectTemplate,messageTemplate,getEmaiFrom(),to);
			logger.debug("############### notifyMissingBICByEmail: iteration={} : Finished ###############",i);
		}
		}
		catch(Exception e) {
			logger.error("notifyByEmail: error: {}",e.getMessage());
		}
		finally {
		}
		
	}

	public StreamedContent getPdfStream() {
		logger.debug("##################### getPdfStream: start");
		StreamedContent stream=new DefaultStreamedContent();
		FacesContext context = FacesContext.getCurrentInstance();
		try {
        if (context.getCurrentPhaseId() == PhaseId.RENDER_RESPONSE) {
            // So, we're rendering the HTML. Return a stub StreamedContent so that it will generate right URL.
            return new DefaultStreamedContent();
        } else {
        	Etudiant etudiant=getSelectedEtudiant();
        	logger.debug("getPdfStream: getSelectedEtudiant: {}",etudiant);
            // So, browser is requesting the media. Return a real StreamedContent with the media bytes.
        	if((etudiant==null)||(CollectionUtils.isEmpty(etudiant.getPj()))) {
        		return new DefaultStreamedContent();
        	}
        	byte[] pjOriginalData=getPjOriginalData(etudiant.getPj().get(0));
            	stream= new DefaultStreamedContent(new ByteArrayInputStream(pjOriginalData), "application/pdf", String.format(Constant.NANTES_ENSA_RIB_FileName,etudiant.getMatricule()));
        }	
        } catch (IOException e) {
        	logger.error("getPdfStream: error {}",e.getMessage());
			//	e.printStackTrace();
		}
		logger.debug("getPdfStream: end");
        return stream;
    }

	@Override
	public void executeTask(String taskName) throws Exception {
		
		TaskEnum task=TaskEnum.valueOf(taskName);
		if(task==null) return;
		switch(task) {
		case MailPropertiesFrom:
			AppCfgConfig.getInstance().getMailPropertiesFrom();
			break;
		case EtudiantList:
			getCommonService().getEtudiantList(EtudiantStatus.AVEC_ERREUR);
			break;
		case EtudiantPJ:
			getCommonService().getEtudiantPjList("undefined");
			break;
		case MailSession:
			getEmailSender(null).getEmailSession();
			break;
		default:
			break;		
		}
	}
	
	private enum TaskEnum {
		EtudiantList,
		EtudiantPJ,
		MailPropertiesFrom,
		MailSession
	}

	@Override
	protected String[] getTasks() {
		String[] tasks=new String[TaskEnum.values().length];
		for(TaskEnum task:TaskEnum.values()) {
			tasks[task.ordinal()]=task.name();
		}
		return tasks;
	}

	public boolean isStudent() {
		return student;
	}

	public void setStudent(boolean student) {
		this.student = student;
	}	
	public IDematService getCpwinDematService() {
		return BudgetHelper.getCpwinDematService();
	}

}