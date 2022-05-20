package fr.symphonie.tools.lemans.bt.ui;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;

import org.apache.logging.log4j.util.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.symphonie.budget.core.model.pluri.LigneBudgetaireAE;
import fr.symphonie.budget.core.service.IGestionTiersService;
import fr.symphonie.budget.ui.beans.GenericBean;
import fr.symphonie.budget.ui.beans.GestionTiersBean;
import fr.symphonie.budget.ui.beans.NavigationBean.Action;
import fr.symphonie.budget.ui.beans.pluri.DialogHelper;
import fr.symphonie.common.util.BudgetHelper;
import fr.symphonie.common.util.Constant;
import fr.symphonie.common.util.MsgEntry;
import fr.symphonie.common.util.Util;
import fr.symphonie.cpwin.model.Adresse;
import fr.symphonie.tools.common.DataListBean;
import fr.symphonie.tools.das.ui.DasBean;





import fr.symphonie.tools.gts.model.PeriodeEnum;

import fr.symphonie.tools.lemans.bt.core.LemansService;
import fr.symphonie.tools.lemans.bt.model.Client;
//import fr.symphonie.tools.lemans.bt.model.ImportedData;
import fr.symphonie.tools.lemans.bt.model.Period;
import fr.symphonie.tools.lemans.bt.model.Spectacle;
import fr.symphonie.tools.lemans.bt.model.SpectacleDetails;
import fr.symphonie.util.HandlerJSFMessage;
import fr.symphonie.util.Helper;
import fr.symphonie.util.model.SimpleEntity;

@ManagedBean(name="lemansRefBean")
@SessionScoped
public class ReferentielBean extends GenericBean implements Serializable{


	private static final String ZERO = "0";
	/**
	 * 
	 */
	private static final long serialVersionUID = -2630437756702391656L;
	private static final Logger logger = LoggerFactory.getLogger(DasBean.class);
	
	@ManagedProperty (value="#{lemanService}")
	private LemansService service;
	
	private String codeSpectacle;
	private String tvaSpectacle;
	private Integer numPeriode;
	private List<Spectacle> listSpectacles;
	private List<Period> listPeriodes;
	
	private Spectacle selectedSpectacle;
	private SpectacleDetails selectedDetailSpec;
	private static final String AUTRE="Autre";
	private Period selectedPeriode;
	private String codeGts;
    private List<Client> listClients;
	private Client selectedClient;

	private boolean updateMode;
	private boolean updateDetailMode;
	private List<String> listTvaSpectacle;
	
	private List<Integer> listNumPeriode;

	
	public void resetDynamicList() {		
		setListSpectacles(null);
		setListPeriodes(null);
		setListClients(null);
	}

    /**
     * toutes les opération de recherche
     */
	@Override
	public void search() {
		String searchCondition =null;
		Integer resultSize=0;
		
		Action action=BudgetHelper.getNavigationBean().getCurrentAction();
		logger.debug("search for action : {} ",action);
		try {
		switch(action){
		case LEMANS_SPECTACLE:
			searchCondition=BudgetHelper.prepareSearchKey(getCodeSpectacle());
			setListSpectacles(service.getSpectacles(searchCondition));
			resultSize=getListSpectacles().size();
			break;
		case LEMANS_PERIODE:			
			setListPeriodes(service.getPeriodes(getExercice(),getNumPeriode(),null));
			break;
		case LEMANS_CLIENT:
			searchCondition=BudgetHelper.prepareSearchKey(getCodeGts());
			setListClients(service.getClientLemansList(searchCondition));
			resultSize=getListClients().size();
			loadAdressClient();
			break;
		default:
			break;
		}
		if((Strings.isBlank(searchCondition)) && (resultSize==Constant.maxResult)){
			String msg=HandlerJSFMessage.formatMessage(HandlerJSFMessage.getErrorMessage("search.maxResult"), new Object[]{Constant.maxResult});
			HandlerJSFMessage.addWarnMessage(msg);
		}
		}
		catch(Exception e) {
			HandlerJSFMessage.addErrorMessage(Util.getErrorMessageFromException(MsgEntry.FAILED, e));
			e.printStackTrace();
		}
		logger.debug("search : End ");
	}


	@Override
	public void reset() {
		resetDynamicList();
		resetEnteteList();	
		setCodeSpectacle(null);
		setNumPeriode(null);
		setSelectedSpectacle(null);
		setSelectedDetailSpec(null);
		setSelectedPeriode(null);
		setCodeGts(null);
		setSelectedClient(null);
		setTvaSpectacle(null);
		setUpdateMode(false);
		setUpdateDetailMode(false);
	}


	@Override
	public void validate() {
		
	}
	
	@Override
	public boolean isRequiredDataDone() {
		switch (getCurrentAction()) {
		case GTS_PERIODE:
			return (getExercice()!=null);	
		default:
			return true;
		}
	}
	
	public void resetEnteteList() {
	}
	
	@Override
	public boolean isCommonRequiredDone() {
		if(getExercice()==0)return false;		
		return true;
	}
	
	public void save(){
		try{
			
			HandlerJSFMessage.addInfo(MsgEntry.SUCCES);
		} catch (Exception e) {
			e.printStackTrace();
			HandlerJSFMessage.addErrorMessageFromException(MsgEntry.FAILED, e);
		}
	}

	public String getCodeSpectacle() {
		return codeSpectacle;
	}
	public void setCodeSpectacle(String codeSpectacle) {
		this.codeSpectacle = codeSpectacle;
		resetDynamicList();
	}
	public List<Spectacle> getListSpectacles() {
		return listSpectacles;
	}
	public void setListSpectacles(List<Spectacle> listSpectacles) {
		this.listSpectacles = listSpectacles;
	}
	
	/**
	 * prération de la mise à jour 
	 * d'un spectacle
	 */
	public void gotoUpdateSpectacle()
	{
		setUpdateMode(true);
		DialogHelper.openSpectacleDialog();
	}
	/**
	 * préparation de l'ajout d'un nouvel
	 * spectacle
	 */
	public void gotoAddSpectacle()
	{
		getDataListBean().reset();
		Spectacle spectacle=new Spectacle();
		spectacle.setTrace(Helper.createTrace());
		setSelectedSpectacle(spectacle);	
		setUpdateMode(false);
		DialogHelper.openSpectacleDialog();
	}
	public Spectacle getSelectedSpectacle() {
		return selectedSpectacle;
	}
	public void setSelectedSpectacle(Spectacle selectedSpectacle) {
		this.selectedSpectacle = selectedSpectacle;
	}
	/**
	 * fermer la fenettre dialog en cours 
	 */
	public void closeCurrentDialog() {
		DialogHelper.closeDialog(null);		
	}
	/**
	 * Methode commune pour 
	 * ajouter ou mettre a jour 
	 * les entitées de ce module
	 */
	public <T extends Object> void addOrUpdate()
	{
		T entity=getCurrentEntity();

		logger.info("addOrUpdate()  entity={}",entity);
		if(!checkDupicated())return;
	    if(!checkRequiredElements())return;
	    if(!checkIntegrityElements())return;
		try {	
			entity=service.save(entity);
	
			afterSave(entity);
			
			closeCurrentDialog();
			HandlerJSFMessage.addInfo(MsgEntry.SUCCES);	
		} catch (Exception e) {
			e.printStackTrace();
			HandlerJSFMessage.addErrorMessage(Util.getErrorMessageFromException(MsgEntry.FAILED, e));
		}
		logger.info("addOrUpdate() -->  End");
	}
private <T extends Object> void afterSave(T entity) {
	setCurrentEntity(entity);
	if(getCurrentList()==null)
		setCurrentList(new ArrayList<T>());	
	if(!isUpdateMode())
	getCurrentList().add(entity);
	switch (getCurrentAction()) {
	
	case GTS_PERIODE:
		Period periode=(Period)entity;
		if(!isUpdateMode())
		getListNumPeriode().add(periode.getNumero());
	case GTS_CLIENT:
		loadAdressClient();
		break;		
	default:
		break;
	}
	
	}

private boolean checkDupicated()
{
	String msgKey=null;
	if(isUpdateMode())return true;
	switch (getCurrentAction()) {
	case LEMANS_SPECTACLE:
		Spectacle loadedA=service.getSpectacle(getSelectedSpectacle().getCode().trim());
         if(loadedA!=null){
             msgKey=MsgEntry.DUPLICATE_SPECTACLE_MSG;
        }             
         break;
	case LEMANS_PERIODE:
		 break;	
	case LEMANS_CLIENT:
		Client loadedC=service.getClient(getSelectedClient().getCode().trim());
		logger.info("loadedC: "+loadedC);
        if(loadedC!=null){
            msgKey=MsgEntry.DUPLICATE_CLIENT_GTS_MSG;
       }
		 break;			 
	default:
		 break;
	}
	if (msgKey == null)
		return true;
	else {
		HandlerJSFMessage.addError(msgKey);
		return false;
	}
}

	
	/**
	 * Vérifier les elements obligatoires 
	 * pour la saisi
	 * @return
	 */
	private boolean checkRequiredElements() {
		String msgKey=null;
		switch (getCurrentAction()) {
		case LEMANS_SPECTACLE:
             if(!getSelectedSpectacle().checkRequired()){
                 msgKey=MsgEntry.CPWIN_REQUIRED;
            }             
             break;
		case LEMANS_PERIODE:
			 break;		
		case LEMANS_CLIENT:
			   if(!getSelectedClient().checkRequired()){
	                 msgKey=MsgEntry.CPWIN_REQUIRED;
	            }             
	             break;
		default:
			 break;
		}
		if (msgKey == null)
			return true;
		else {
			HandlerJSFMessage.addError(msgKey);
			return false;
		}
	
	}
     /**
      * Vérifier l'intégrité des données saisis
      * @return
      */
	private boolean checkIntegrityElements()
	 {
		switch (getCurrentAction()) {
		case LEMANS_SPECTACLE:
			return true;
		case LEMANS_PERIODE:
			return true;
		case LEMANS_CLIENT:
			return true;	
		default:
			return true;
		}
	
	}
	@SuppressWarnings("unchecked")
	private <T extends Object> T getCurrentEntity(){
		T entity=null;
		switch (getCurrentAction()) {
		case LEMANS_SPECTACLE:
			entity= (T) getSelectedSpectacle();
			break;
		case LEMANS_PERIODE:
			entity= (T) getSelectedPeriode();
			break;
		case LEMANS_CLIENT:
			entity=(T) getSelectedClient();
			break;
		default:
			break;
		}
		return entity;
	}
	@SuppressWarnings("unchecked")
	private <T extends Object> List<T> getCurrentList(){
		List<T> list=null;
		switch (getCurrentAction()) {
		case LEMANS_SPECTACLE:
			list= (List<T>) getListSpectacles();
			break;
		case LEMANS_PERIODE:
			list=(List<T>) getListPeriodes();
			break;
		case LEMANS_CLIENT:
			list=(List<T>) getListClients();
			break;
		default:
			break;
		}
		return list;
	}
	@SuppressWarnings("unchecked")
	private <T extends Object> void setCurrentList(List<T> list){
		
		switch (getCurrentAction()) {
		case LEMANS_SPECTACLE:
			setListSpectacles((List<Spectacle>) list);
			break;
		case LEMANS_PERIODE:
			setListPeriodes((List<Period>) list);
			break;
		case LEMANS_CLIENT:
			setListClients((List<Client>) list);
			break;
		default:
			break;
		}
	}
	private void setCurrentEntity(Object o){
		switch (getCurrentAction()) {
		case LEMANS_SPECTACLE:
			setSelectedSpectacle((Spectacle) o);
			break;
		case LEMANS_PERIODE:
			setSelectedPeriode((Period) o);
			break;
		case LEMANS_CLIENT:
			setSelectedClient((Client) o);
			break;
		default:
			break;
		}
	}
	public SpectacleDetails getSelectedDetailSpec() {
		return selectedDetailSpec;
	}

	private void positionnerTvaSpectacle() {
		float tva = 0f;
		SpectacleDetails detailArtic = getSelectedDetailSpec();
		tva = detailArtic == null ? 0f : detailArtic.getTva();
		if (getListTvaArtcile().contains(Float.toString(tva))) {
			setTvaSpectacle(Float.toString(tva));
		} else
			setTvaSpectacle(AUTRE);
	}
	
	public void setSelectedDetailSpec(SpectacleDetails selectedDetailArtc) {
		this.selectedDetailSpec = selectedDetailArtc;
		refreshDataListValues(this.selectedDetailSpec);
		positionnerTvaSpectacle();
		
	}
    /**
     * préparation de l'ajout d'un nouveau détail
     * spectacle
     */
	public void gotoAddDetailSpectacle() {
		setUpdateDetailMode(false);
		getDataListBean().reset();
		SpectacleDetails detail = new SpectacleDetails();
		detail.setSpectacle(getSelectedSpectacle());
		detail.setTrace(Helper.createTrace());
		detail.setTva(0f);
		setSelectedDetailSpec(detail);		
		DialogHelper.openDetailSpectacleDialog();
	}

	
	/**
	 * validation de l'ajout/modification de détail spectacle à la liste
	 * sous spectacle
	 */
	public void addOrUpdateSpectacleDetail()
	{
		completeSpectacleDetails(getSelectedDetailSpec());
        logger.info("public void addSpectacleDetail() detail spectacle={}",getSelectedDetailSpec().toString());
		if(!checkRequiredDetail())return;
		if(!checkDupicatedDetail())return;

		if(!isUpdateDetailMode())
			getSelectedSpectacle().getDetails().add(getSelectedDetailSpec());
			closeCurrentDialog();
		
	}
	
	/**
	 * controler les éléments obligatoires pour 
	 * un détail
	 *  (utiliser switch (key) en cas de multiples CRUDs) 
	 * @return
	 */
	private boolean checkRequiredDetail() {
		if (!getSelectedDetailSpec().checkRequired()) {
			HandlerJSFMessage.addError(MsgEntry.CPWIN_REQUIRED);
			return false;
		}
		return true;

	}
	private void completeSpectacleDetails(SpectacleDetails detail) {
		DataListBean dataList=getDataListBean();
		String codeDest=dataList.getCodeDest();
		String codeNature=dataList.getCodeNature();
		String codeService=dataList.getCodeService();
		String codeProg=dataList.getCodeProg();
		if(!isUpdateDetailMode())detail.setExercice(getExercice());
		detail.setDestination(new SimpleEntity(codeDest, ""));
		detail.setNature(new SimpleEntity(codeNature, ""));
		detail.setService(new SimpleEntity(codeService, ""));
		detail.setProgramme(new SimpleEntity(codeProg, ""));
		detail.setCompteProduit(new SimpleEntity(dataList.getCompteProduit(), ""));
		completeCompteImput(detail);
		
		LigneBudgetaireAE lb=dataList.finLB(codeDest, codeNature, codeService, codeProg);
		logger.debug("completeSpectacleDetails: lb={}",lb);
		if(lb!=null)detail.setNoLbi(lb.getNoLbi());
			
    }
	private void completeCompteImput(SpectacleDetails detail){
		String[] compteImput=service.getCtaCompteImput(getExercice(),detail.getCompteProduit().getCode());
		detail.setImputTtc(compteImput[0]);
		detail.setImputTva(compteImput[1]);
	}
	public void setService(LemansService service) {
		this.service = service;
	}

	public String getTvaSpectacle() {
		float tva=getSelectedDetailSpec().getTva();
		if(!isTvaSpectacleAutre())
		{
			this.tvaSpectacle = Float.toString(tva);
		}
		return this.tvaSpectacle;
	}
	
	public void setTvaSpectacle(String tvaSpectacle) {
		this.tvaSpectacle = tvaSpectacle;
		if(!isTvaSpectacleAutre())
			if(getSelectedDetailSpec()!=null)
				getSelectedDetailSpec().setTva(Float.parseFloat(tvaSpectacle));
		
	}
	
	public boolean isTvaSpectacleAutre()
	{
		if(this.tvaSpectacle!=null)
		return AUTRE.equalsIgnoreCase(this.tvaSpectacle.trim());
	    return false;
	}
	
	public void resetTvaSpectacle()
	{
		setTvaSpectacle(ZERO);
	}
	private DataListBean getDataListBean(){
	return BudgetHelper.getDataListBean();
	}
	public Integer getNumPeriode() {
		return numPeriode;
	}
	public void setNumPeriode(Integer numPeriode) {
		this.numPeriode = numPeriode;
		resetDynamicList();
	}
	public List<Period> getListPeriodes() {
		return listPeriodes;
	}
	public void setListPeriodes(List<Period> listPeriodes) {
		this.listPeriodes = listPeriodes;
	}
	/**
	 * prération de la mise à jour 
	 * d'une période
	 */
	public void gotoUpdatePeriode()
	{
		setUpdateMode(true);
		DialogHelper.openPeriodeLemansDialog();
	}
	
	public Period getSelectedPeriode() {
		return selectedPeriode;
	}
	public void setSelectedPeriode(Period selectedPeriode) {
		this.selectedPeriode = selectedPeriode;
	}
	public String getCodeGts() {
		return codeGts;
	}
	public void setCodeGts(String codeGts) {
		this.codeGts = codeGts;
		resetDynamicList();
	}
	public List<Client> getListClients() {
		return listClients;
	}
	public void setListClients(List<Client> listClients) {
		this.listClients = listClients;
	}
	public Client getSelectedClient() {
		return selectedClient;
	}
	public void setSelectedClient(Client selectedClient) {
		this.selectedClient = selectedClient;
		loadTiersCpwin();
	}
	public void gotoUpdateClient()
	{
		setUpdateMode(true);
		DialogHelper.openClientDialog();
	}
	/**
	 * chargement 
	 * de l'entité client: cpwin tiers ..
	 */
	public void loadTiersCpwin() {
		Client client=getSelectedClient();
		if(client==null)return;
		String codeCpwin=client.getCodeCpwin();
		if((codeCpwin==null)||(codeCpwin.trim().isEmpty()))client.setTiersCpwin(null);
		IGestionTiersService service= BudgetHelper.getTiersService();
		client.setTiersCpwin(service.findTiers(codeCpwin));
	}
	public void gotoAddClient(){
		
		setUpdateMode(false);
		getDataListBean().reset();
		Client client=new Client();
		client.setTrace(Helper.createTrace());
		setSelectedClient(client);
		DialogHelper.openClientDialog();
	}
	@Override
	public Integer getExercice() {
		return getDataListBean().getExercice();
	}
	
	
	/**
	 * préparation de l'ajout d'une nouvelle
	 * periode
	 */
	public void gotoAddPeriode()
	{
		setUpdateMode(false);
		getDataListBean().reset();
		Period periode=new Period();
		periode.setEtat(PeriodeEnum.OUVERT);
		periode.setTrace(Helper.createTrace());
		periode.setExercice(getExercice());
		Integer maxNumero=0;
		for(Integer num:getListNumPeriode()){
			if(num.compareTo(maxNumero)>0)maxNumero=num;
		}
		periode.setNumero(maxNumero.intValue()+1);
		setSelectedPeriode(periode);	
		DialogHelper.openPeriodeLemansDialog();
	}
	public boolean isUpdateMode() {
		return updateMode;
	}
	public void setUpdateMode(boolean updateMode) {
		this.updateMode = updateMode;
	}
	
	public void execListener()
	{
		resetDynamicList();
	}
	
	/**
	 * préparation de la modification
	 * d'un détail spectacle
	 */
	public void gotoUpdateDetailSpectacle() {
		setUpdateDetailMode(true);	
		DialogHelper.openDetailSpectacleDialog();
	}
	/**
	 * 
	 * @return true en cas de tentative d'ajout d'un détail
	 *  en double
	 *  (utiliser switch (key) en cas de multiples CRUDs) 
	 */
	
	private boolean checkDupicatedDetail() {
		boolean exist = false;
		if (isUpdateDetailMode())
			return true;
		if (getSelectedSpectacle().getDetails() == null)
			return true;
		exist = getSelectedSpectacle().getDetails().contains(getSelectedDetailSpec());
		if (!exist)
			return true;
		else
			HandlerJSFMessage.addError(MsgEntry.DUPLICATE_SPECTACLE_DETAIL_MSG);
		return false;

	}
	public boolean isUpdateDetailMode() {
		return updateDetailMode;
	}
	public void setUpdateDetailMode(boolean updateDetailMode) {
		this.updateDetailMode = updateDetailMode;
	}
	
	public boolean isAddDetailAutorized()
	{
		if(getSelectedSpectacle()==null)return false;
		if(getSelectedSpectacle().checkRequired())return true;
		return false;
	}
	/***
	 *  mise à jour des valeurs 
	 *  pour le bean DataListBean
	 *  à partir de l'objet SpectacleDetails
	 * @param detail
	 */
	private void refreshDataListValues(SpectacleDetails detail) {
		DataListBean dataList=getDataListBean();
		if(detail==null)return;

		if(detail.getExercice()!=null)dataList.setExec(detail.getExercice().toString());
		if(detail.getDestination()!=null)dataList.setCodeDest(detail.getDestination().getCode());
		if(detail.getNature()!=null)dataList.setCodeNature(detail.getNature().getCode());
		if(detail.getService()!=null)dataList.setCodeService(detail.getService().getCode());
		if(detail.getProgramme()!=null)dataList.setCodeProg(detail.getProgramme().getCode());
		if(detail.getCompteProduit()!=null)dataList.setCompteProduit(detail.getCompteProduit().getCode());
	
    }
	
	public <T> void delete(){
		
		logger.info("delete()--start");
			T entity=getCurrentEntity();
			
			  if(isWithChild()) return;
			  if(isUsedByImport(entity)) return;
			try {	
				   service.remove(entity);
					afterDelete(entity);
			} catch (Exception e) {
				e.printStackTrace();
				HandlerJSFMessage.addErrorMessage(Util.getErrorMessageFromException(MsgEntry.FAILED, e));
			}
			logger.info("delete() -->  End");
			
		}
	

	private <T extends Object> void afterDelete(T entity) {
		getCurrentList().remove(entity);
		
	}


	public void deleteSpectacleDetail(){
		Spectacle loaded=null;
		logger.info("deleteSpectacleDetail()--start");
			SpectacleDetails detail=getSelectedDetailSpec();
			if(isUsedByImport(detail)) return;
			try {	
				   
				loaded=service.removeSpectacleDetail(detail);
				setSelectedSpectacle(loaded);
		
				getListSpectacles().set(getListSpectacles().indexOf(getListSpectacles()), loaded);
				 getSelectedSpectacle().getDetails().remove(detail);
				   logger.info(" getSelectedSpectacle().getDetails(): {}", getSelectedSpectacle().getDetails().size());
			} catch (Exception e) {
				e.printStackTrace();
				HandlerJSFMessage.addErrorMessage(Util.getErrorMessageFromException(MsgEntry.FAILED, e));
			}
			logger.info("deleteSpectacleDetail() -->  End");
				
		
			
		}
	private boolean isUsedByImport(Object entity) {
//		List<ImportedData> list=null;
//		
//		String errorMsg=null,errorKey=null;
//		Object[] params=null;
//		if(entity==null)return false;
//		if(entity instanceof SpectacleDetails){
//			SpectacleDetails instance=(SpectacleDetails)entity;
//			list=service.getImportedData(instance.getExercice(), null,instance.getCode(),null);
//			if(list.isEmpty())return false;
//			errorKey="gts.detail.spectacle.used";
//			params=new Object[]{instance.getExercice(),instance.getCode()};
//			
//		}
//		else if(entity instanceof Client){
//			Client instance=(Client)entity;
//			list=service.getImportedData(null, null,null,instance.getCode());
//			if(list.isEmpty())return false;
//			errorKey="gts.client.used";
//			params=new Object[]{instance.getCode()};
//		}
//		else if(entity instanceof Period){
//			Period instance=(Period)entity;
//			list=service.getImportedData(instance.getExercice(), instance.getNumero(),null,null);
//			if(list.isEmpty())return false;
//			errorKey="gts.periode.used";
//			params=new Object[]{instance.getNumero(),instance.getExercice()};
//		}
//		if(errorKey!=null){
//			errorMsg=HandlerJSFMessage.formatMessage(HandlerJSFMessage.getErrorMessage(errorKey),params );
//			HandlerJSFMessage.addErrorMessage(errorMsg);
//			return true;
//		}
		
		return false;
	}

	public List<String> getListTvaArtcile() {
		if(listTvaSpectacle==null)
		{
			listTvaSpectacle= new ArrayList<>();
			listTvaSpectacle.add(Float.toString(0f));
			listTvaSpectacle.add(Float.toString(2.10f));
			listTvaSpectacle.add(Float.toString(5.50f));
			listTvaSpectacle.add(Float.toString(10f));
			listTvaSpectacle.add(Float.toString(20f));
			listTvaSpectacle.add(AUTRE);
		}
		return listTvaSpectacle;
	}
	public GestionTiersBean getTiersBean() {
		return BudgetHelper.getGestionTiersBean();
	}

	private void loadAdressClient() {
		if((getListClients()==null)||(getListClients().isEmpty())) return;
		for (Client client : getListClients()) {
			loadAdressTiers(client);
		}

}

	public void loadAdressTiers(Client client) {
		if(client==null)return;
	Adresse	 adresse =getTiersBean().loadTiersAdress(client.getNoAdresseCpwin(), client.getCodeCpwin());
	if(adresse!=null)client.setAdresse(adresse);
		
	}
	
	public boolean isWithChild(){

		String msgKey=null;
		switch (getCurrentAction()) {
		case LEMANS_SPECTACLE:
             if(getSelectedSpectacle().getDetails().size()!=0){
                 msgKey=MsgEntry.SUPPR_DETAIL_SPECTACLE;
            }             
             break;
		case LEMANS_CLIENT:case LEMANS_PERIODE:
			break;
			
		default:
			 break;
		}
		if (msgKey == null)
			return false;
		else {
			HandlerJSFMessage.addError(msgKey);
			return true;
		}
}
	public List<Integer> getListNumPeriode() {
		if(listNumPeriode==null){
			if(getExercice()!=null)
				setListNumPeriode(service.getListNumPeriode(getExercice()));
		}
		return listNumPeriode;
	}
	public void setListNumPeriode(List<Integer> listNumPeriode) {
		this.listNumPeriode = listNumPeriode;
	}
}
