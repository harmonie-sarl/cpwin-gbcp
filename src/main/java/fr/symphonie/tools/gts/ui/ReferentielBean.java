package fr.symphonie.tools.gts.ui;

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
import fr.symphonie.tools.gts.core.GtsService;
import fr.symphonie.tools.gts.model.Article;
import fr.symphonie.tools.gts.model.ArticleDetails;
import fr.symphonie.tools.gts.model.ClientGts;
import fr.symphonie.tools.gts.model.ImportedData;
import fr.symphonie.tools.gts.model.PeriodeEnum;
import fr.symphonie.tools.gts.model.PeriodeGts;
import fr.symphonie.util.HandlerJSFMessage;
import fr.symphonie.util.Helper;
import fr.symphonie.util.model.SimpleEntity;

@ManagedBean(name="gtsRefBean")
@SessionScoped
public class ReferentielBean extends GenericBean implements Serializable {

	private static final String ZERO = "0";
	/**
	 * 
	 */
	private static final long serialVersionUID = -2630437756702391656L;
	private static final Logger logger = LoggerFactory.getLogger(DasBean.class);
	
	@ManagedProperty (value="#{gtsService}")
	private GtsService service;
	
	private String codeArticle;
	private String tvaArticle;
	private Integer numPeriode;
	private List<Article> listArticles;
	private List<PeriodeGts> listPeriodes;
	
	private Article selectedArticle;
	private ArticleDetails selectedDetailArtc;
	private static final String AUTRE="Autre";
	private PeriodeGts selectedPeriode;
	private String codeGts;
    private List<ClientGts> listClients;
	private ClientGts selectedClient;

	private boolean updateMode;
	private boolean updateDetailMode;
	private List<String> listTvaArtcile;
	
	private List<Integer> listNumPeriode;

	
	public void resetDynamicList() {		
		setListArticles(null);
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
		case GTS_ARTICLE:
			searchCondition=BudgetHelper.prepareSearchKey(getCodeArticle());
			setListArticles(service.getArticles(searchCondition));
			resultSize=getListArticles().size();
			break;
		case GTS_PERIODE:			
			setListPeriodes(service.getPeriodes(getExercice(),getNumPeriode(),null));
			break;
		case GTS_CLIENT:
			searchCondition=BudgetHelper.prepareSearchKey(getCodeGts());
			setListClients(service.getClientGtsList(searchCondition));
			resultSize=getListClients().size();
			loadAdressClientGts();
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
		setCodeArticle(null);
		setNumPeriode(null);
		setSelectedArticle(null);
		setSelectedDetailArtc(null);
		setSelectedPeriode(null);
		setCodeGts(null);
		setSelectedClient(null);
		setTvaArticle(null);
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

	public String getCodeArticle() {
		return codeArticle;
	}
	public void setCodeArticle(String codeArticle) {
		this.codeArticle = codeArticle;
		resetDynamicList();
	}
	public List<Article> getListArticles() {
		return listArticles;
	}
	public void setListArticles(List<Article> listArticles) {
		this.listArticles = listArticles;
	}
	
	/**
	 * prération de la mise à jour 
	 * d'un article
	 */
	public void gotoUpdateArticle()
	{
		setUpdateMode(true);
		DialogHelper.openArticleDialog();
	}
	/**
	 * préparation de l'ajout d'un nouvel
	 * article
	 */
	public void gotoAddArticle()
	{
		getDataListBean().reset();
		Article article=new Article();
		article.setTrace(Helper.createTrace());
		setSelectedArticle(article);	
		setUpdateMode(false);
		DialogHelper.openArticleDialog();
	}
	public Article getSelectedArticle() {
		return selectedArticle;
	}
	public void setSelectedArticle(Article selectedArticle) {
		this.selectedArticle = selectedArticle;
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
		PeriodeGts periode=(PeriodeGts)entity;
		if(!isUpdateMode())
		getListNumPeriode().add(periode.getNumero());
	case GTS_CLIENT:
		loadAdressClientGts();
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
	case GTS_ARTICLE:
		Article loadedA=service.getArticle(getSelectedArticle().getCode().trim());
         if(loadedA!=null){
             msgKey=MsgEntry.DUPLICATE_ARTICLE_MSG;
        }             
         break;
	case GTS_PERIODE:
		 break;	
	case GTS_CLIENT:
		ClientGts loadedC=service.getClientGts(getSelectedClient().getCode().trim());
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
		case GTS_ARTICLE:
             if(!getSelectedArticle().checkRequired()){
                 msgKey=MsgEntry.CPWIN_REQUIRED;
            }             
             break;
		case GTS_PERIODE:
			 break;		
		case GTS_CLIENT:
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
		case GTS_ARTICLE:
			return true;
		case GTS_PERIODE:
			return true;
		case GTS_CLIENT:
			return true;	
		default:
			return true;
		}
	
	}
	@SuppressWarnings("unchecked")
	private <T extends Object> T getCurrentEntity(){
		T entity=null;
		switch (getCurrentAction()) {
		case GTS_ARTICLE:
			entity= (T) getSelectedArticle();
			break;
		case GTS_PERIODE:
			entity= (T) getSelectedPeriode();
			break;
		case GTS_CLIENT:
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
		case GTS_ARTICLE:
			list= (List<T>) getListArticles();
			break;
		case GTS_PERIODE:
			list=(List<T>) getListPeriodes();
			break;
		case GTS_CLIENT:
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
		case GTS_ARTICLE:
			setListArticles((List<Article>) list);
			break;
		case GTS_PERIODE:
			setListPeriodes((List<PeriodeGts>) list);
			break;
		case GTS_CLIENT:
			setListClients((List<ClientGts>) list);
			break;
		default:
			break;
		}
	}
	private void setCurrentEntity(Object o){
		switch (getCurrentAction()) {
		case GTS_ARTICLE:
			setSelectedArticle((Article) o);
			break;
		case GTS_PERIODE:
			setSelectedPeriode((PeriodeGts) o);
			break;
		case GTS_CLIENT:
			setSelectedClient((ClientGts) o);
			break;
		default:
			break;
		}
	}
	public ArticleDetails getSelectedDetailArtc() {
		return selectedDetailArtc;
	}

	private void positionnerTvaArticle() {
		float tva = 0f;
		ArticleDetails detailArtic = getSelectedDetailArtc();
		tva = detailArtic == null ? 0f : detailArtic.getTva();
		if (getListTvaArtcile().contains(Float.toString(tva))) {
			setTvaArticle(Float.toString(tva));
		} else
			setTvaArticle(AUTRE);
	}
	
	public void setSelectedDetailArtc(ArticleDetails selectedDetailArtc) {
		this.selectedDetailArtc = selectedDetailArtc;
		refreshDataListValues(this.selectedDetailArtc);
		positionnerTvaArticle();
		
	}
    /**
     * préparation de l'ajout d'un nouveau détail
     * article
     */
	public void gotoAddDetailArticle() {
		setUpdateDetailMode(false);
		getDataListBean().reset();
		ArticleDetails detail = new ArticleDetails();
		detail.setArticle(getSelectedArticle());
		detail.setTrace(Helper.createTrace());
		detail.setTva(0f);
		setSelectedDetailArtc(detail);		
		DialogHelper.openDetailArticleDialog();
	}

	
	/**
	 * validation de l'ajout/modification de détail article à la liste
	 * sous article
	 */
	public void addOrUpdateArticleDetail()
	{
		completeArticleDetails(getSelectedDetailArtc());
        logger.info("public void addArticleDetail() detail article={}",getSelectedDetailArtc().toString());
		if(!checkRequiredDetail())return;
		if(!checkDupicatedDetail())return;

		if(!isUpdateDetailMode())
			getSelectedArticle().getDetails().add(getSelectedDetailArtc());
			closeCurrentDialog();
		
	}
	
	/**
	 * controler les éléments obligatoires pour 
	 * un détail
	 *  (utiliser switch (key) en cas de multiples CRUDs) 
	 * @return
	 */
	private boolean checkRequiredDetail() {
		if (!getSelectedDetailArtc().checkRequired()) {
			HandlerJSFMessage.addError(MsgEntry.CPWIN_REQUIRED);
			return false;
		}
		return true;

	}
	private void completeArticleDetails(ArticleDetails detail) {
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
		logger.debug("completeArticleDetails: lb={}",lb);
		if(lb!=null)detail.setNoLbi(lb.getNoLbi());
			
    }
	private void completeCompteImput(ArticleDetails detail){
		String[] compteImput=service.getCtaCompteImput(getExercice(),detail.getCompteProduit().getCode());
		detail.setImputTtc(compteImput[0]);
		detail.setImputTva(compteImput[1]);
	}
	public void setService(GtsService service) {
		this.service = service;
	}

	public String getTvaArticle() {
		float tva=getSelectedDetailArtc().getTva();
		if(!isTvaArticleAutre())
		{
			this.tvaArticle = Float.toString(tva);
		}
		return this.tvaArticle;
	}
	
	public void setTvaArticle(String tvaArticle) {
		this.tvaArticle = tvaArticle;
		if(!isTvaArticleAutre())
			if(getSelectedDetailArtc()!=null)
				getSelectedDetailArtc().setTva(Float.parseFloat(tvaArticle));
		
	}
	
	public boolean isTvaArticleAutre()
	{
		if(this.tvaArticle!=null)
		return AUTRE.equalsIgnoreCase(this.tvaArticle.trim());
	    return false;
	}
	
	public void resetTvaArticle()
	{
		setTvaArticle(ZERO);
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
	public List<PeriodeGts> getListPeriodes() {
		return listPeriodes;
	}
	public void setListPeriodes(List<PeriodeGts> listPeriodes) {
		this.listPeriodes = listPeriodes;
	}
	/**
	 * prération de la mise à jour 
	 * d'une période
	 */
	public void gotoUpdatePeriode()
	{
		setUpdateMode(true);
		DialogHelper.openPeriodeGtsDialog();
	}
	
	public PeriodeGts getSelectedPeriode() {
		return selectedPeriode;
	}
	public void setSelectedPeriode(PeriodeGts selectedPeriode) {
		this.selectedPeriode = selectedPeriode;
	}
	public String getCodeGts() {
		return codeGts;
	}
	public void setCodeGts(String codeGts) {
		this.codeGts = codeGts;
		resetDynamicList();
	}
	public List<ClientGts> getListClients() {
		return listClients;
	}
	public void setListClients(List<ClientGts> listClients) {
		this.listClients = listClients;
	}
	public ClientGts getSelectedClient() {
		return selectedClient;
	}
	public void setSelectedClient(ClientGts selectedClient) {
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
		ClientGts client=getSelectedClient();
		if(client==null)return;
		String codeCpwin=client.getCodeCpwin();
		if((codeCpwin==null)||(codeCpwin.trim().isEmpty()))client.setTiersCpwin(null);
		IGestionTiersService service= BudgetHelper.getTiersService();
		client.setTiersCpwin(service.findTiers(codeCpwin));
	}
	public void gotoAddClient(){
		
		setUpdateMode(false);
		getDataListBean().reset();
		ClientGts client=new ClientGts();
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
		PeriodeGts periode=new PeriodeGts();
		periode.setEtat(PeriodeEnum.OUVERT);
		periode.setTrace(Helper.createTrace());
		periode.setExercice(getExercice());
		Integer maxNumero=0;
		for(Integer num:getListNumPeriode()){
			if(num.compareTo(maxNumero)>0)maxNumero=num;
		}
		periode.setNumero(maxNumero.intValue()+1);
		setSelectedPeriode(periode);	
		DialogHelper.openPeriodeGtsDialog();
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
	 * d'un détail article
	 */
	public void gotoUpdateDetailArticle() {
		setUpdateDetailMode(true);	
		DialogHelper.openDetailArticleDialog();
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
		if (getSelectedArticle().getDetails() == null)
			return true;
		exist = getSelectedArticle().getDetails().contains(getSelectedDetailArtc());
		if (!exist)
			return true;
		else
			HandlerJSFMessage.addError(MsgEntry.DUPLICATE_ARTICLE_DETAIL_MSG);
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
		if(getSelectedArticle()==null)return false;
		if(getSelectedArticle().checkRequired())return true;
		return false;
	}
	/***
	 *  mise à jour des valeurs 
	 *  pour le bean DataListBean
	 *  à partir de l'objet ArticleDetails
	 * @param detail
	 */
	private void refreshDataListValues(ArticleDetails detail) {
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


	public void deleteArticleDetail(){
		Article loaded=null;
		logger.info("deleteArticleDetail()--start");
			ArticleDetails detail=getSelectedDetailArtc();
			if(isUsedByImport(detail)) return;
			try {	
				   
				loaded=service.removeArticlDetail(detail);
				setSelectedArticle(loaded);
		
				getListArticles().set(getListArticles().indexOf(getSelectedArticle()), loaded);
				 getSelectedArticle().getDetails().remove(detail);
				   logger.info(" getSelectedArticle().getDetails(): {}", getSelectedArticle().getDetails().size());
			} catch (Exception e) {
				e.printStackTrace();
				HandlerJSFMessage.addErrorMessage(Util.getErrorMessageFromException(MsgEntry.FAILED, e));
			}
			logger.info("deleteArticleDetail() -->  End");
				
		
			
		}
	private boolean isUsedByImport(Object entity) {
		List<ImportedData> list=null;
		
		String errorMsg=null,errorKey=null;
		Object[] params=null;
		if(entity==null)return false;
		if(entity instanceof ArticleDetails){
			ArticleDetails instance=(ArticleDetails)entity;
			list=service.getImportedData(instance.getExercice(), null,instance.getCode(),null);
			if(list.isEmpty())return false;
			errorKey="gts.detail.article.used";
			params=new Object[]{instance.getExercice(),instance.getCode()};
			
		}
		else if(entity instanceof ClientGts){
			ClientGts instance=(ClientGts)entity;
			list=service.getImportedData(null, null,null,instance.getCode());
			if(list.isEmpty())return false;
			errorKey="gts.client.used";
			params=new Object[]{instance.getCode()};
		}
		else if(entity instanceof PeriodeGts){
			PeriodeGts instance=(PeriodeGts)entity;
			list=service.getImportedData(instance.getExercice(), instance.getNumero(),null,null);
			if(list.isEmpty())return false;
			errorKey="gts.periode.used";
			params=new Object[]{instance.getNumero(),instance.getExercice()};
		}
		if(errorKey!=null){
			errorMsg=HandlerJSFMessage.formatMessage(HandlerJSFMessage.getErrorMessage(errorKey),params );
			HandlerJSFMessage.addErrorMessage(errorMsg);
			return true;
		}
		
		return false;
	}

	public List<String> getListTvaArtcile() {
		if(listTvaArtcile==null)
		{
			listTvaArtcile= new ArrayList<>();
			listTvaArtcile.add(Float.toString(0f));
			listTvaArtcile.add(Float.toString(2.10f));
			listTvaArtcile.add(Float.toString(5.50f));
			listTvaArtcile.add(Float.toString(10f));
			listTvaArtcile.add(Float.toString(20f));
			listTvaArtcile.add(AUTRE);
		}
		return listTvaArtcile;
	}
	public GestionTiersBean getTiersBean() {
		return BudgetHelper.getGestionTiersBean();
	}

	private void loadAdressClientGts() {
		if((getListClients()==null)||(getListClients().isEmpty())) return;
		for (ClientGts client : getListClients()) {
			loadAdressTiers(client);
		}

}

	public void loadAdressTiers(ClientGts client) {
		if(client==null)return;
	Adresse	 adresse =getTiersBean().loadTiersAdress(client.getNoAdresseCpwin(), client.getCodeCpwin());
	if(adresse!=null)client.setAdresse(adresse);
		
	}
	
	public boolean isWithChild(){

		String msgKey=null;
		switch (getCurrentAction()) {
		case GTS_ARTICLE:
             if(getSelectedArticle().getDetails().size()!=0){
                 msgKey=MsgEntry.SUPPR_DETAIL_ARTICLE;
            }             
             break;
		case GTS_CLIENT:case GTS_PERIODE:
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
