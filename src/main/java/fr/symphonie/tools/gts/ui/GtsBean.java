package fr.symphonie.tools.gts.ui;

import java.io.File;
import java.io.Serializable;
import java.math.BigDecimal;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;

import org.apache.logging.log4j.util.Strings;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.TreeNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.symphonie.budget.core.model.pluri.LigneBudgetaireAE;
import fr.symphonie.budget.core.service.IBudgetPluriannuelService;
import fr.symphonie.budget.ui.beans.NavigationBean.Action;
import fr.symphonie.budget.ui.excel.ExcelHandler;
import fr.symphonie.budget.ui.excel.ExcelModelEnum;
import fr.symphonie.common.model.AppCfgConfig;
import fr.symphonie.common.util.BudgetHelper;
import fr.symphonie.common.util.Constant;
import fr.symphonie.common.util.MsgEntry;
import fr.symphonie.common.util.Util;
import fr.symphonie.excel.ExcelFileImportor;
import fr.symphonie.exception.MissingConfiguration;
import fr.symphonie.tools.common.DataListBean;
import fr.symphonie.tools.gts.core.GtsService;
import fr.symphonie.tools.gts.model.Article;
import fr.symphonie.tools.gts.model.ArticleDetails;
import fr.symphonie.tools.gts.model.ClientGts;
import fr.symphonie.tools.gts.model.ImportedData;
import fr.symphonie.tools.gts.model.LigneRecette;
import fr.symphonie.tools.gts.model.LiqRecette;
import fr.symphonie.tools.gts.model.PeriodeEnum;
import fr.symphonie.tools.gts.model.PeriodeGts;
import fr.symphonie.tools.gts.model.RecetteCodeArticle;
import fr.symphonie.tools.gts.model.RecetteLigneBudg;
import fr.symphonie.tools.gts.util.GtsHelper;
import fr.symphonie.util.HandlerJSFMessage;
import fr.symphonie.util.Helper;
import fr.symphonie.util.model.SimpleEntity;
@ManagedBean(name="gtsBean")
@SessionScoped
public class GtsBean extends ExcelFileImportor  implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -7138528065558687263L;

	private static final Logger logger = LoggerFactory.getLogger(GtsBean.class);

	private static final String DEFAULT_refCommande = "refCde01";
	@ManagedProperty(value = "#{budgetPluriService}")
	private IBudgetPluriannuelService bpService;
	
	@ManagedProperty (value="#{gtsService}")
	private GtsService service;
	
	private Integer numPeriode;
	
	/**
	 * Les listes
	 */
	private List<ImportedData> importedDataList;
	private List<PeriodeGts> periodesList;
	private List<PeriodeGts> traitePeriodesList;
	private List<PeriodeGts> chargePeriodesList;
	private List<RecetteLigneBudg> recetteLigneBudgList;
	private List<RecetteCodeArticle> recetteArticLeList;
	private List<LiqRecette> liqRecetteList;
	private TreeNode liqRecetteTree;
	private List<LiqRecette> differeLiqRecetteList;
	private TreeNode liqRecetteDiffTree;
	private BigDecimal totalMntRemiseLiqReq;
	private BigDecimal totalMntTvaLiqReq;
	private BigDecimal totalMntHtLiqReq;
	private BigDecimal totalMntTtcLiqReq;
	
	private double totalMntRecArtic;
	private double totalDontRegisRecArtic;
	private double totalDontCltRecArtic;
	
	private double totalMntRecLb;
	private double totalDontRegisRecLb;
	private double totalDontCltRecLb;

	private BigDecimal totalMntRemiseLiqReqDiff;
	private BigDecimal totalMntTvaLiqReqDiff;
	private BigDecimal totalMntHtLiqReqDiff;
	private BigDecimal totalMntTtcLiqReqDiff;
	
	
	public void importer(){
		
		try{
		String inputFile=getSavedPath()+File.separator+getImportFileName();
		logger.debug("importer  --> file={} : Début ...",inputFile);
		List<ImportedData> list=importFile(inputFile, 0, ImportedData.class);
		logger.debug("importer list: {}",list.size());
		checkData(list, false);		
		if(hasErrors()){
			cleaningErrorReport();
			return;
		}
		setImportedDataList(list);
		prepareDisplayData();
		calculateTotauxLiqRec(getLiqRecetteList());
		calculateTotauxRecetArticLe(getRecetteArticLeList());
		calculateTotauxRecetLb(getRecetteLigneBudgList());
	
	  } catch (Exception e) {
		HandlerJSFMessage.addErrorMessage(Util.getErrorMessageFromException(MsgEntry.FAILED, e));
		e.printStackTrace();
	   }
		
	}
	

	private void prepareDisplayData() {
	prepareDataByLB();
	prepareDataByArticle();
	
}


	private void prepareDataByArticle() {
		List<RecetteCodeArticle> byArticleList=new ArrayList<>();
		RecetteCodeArticle item=null;
		int index=0;
		double montant=0;
		
		for(ImportedData data:getImportedDataList()){
		
			item=new RecetteCodeArticle(data.getCodeArticle());
			index=byArticleList.indexOf(item);
			if(index>=0) item=byArticleList.get(index);
			else byArticleList.add(item);
			if(data.getClientGts().isRegisseur()){
				montant=item.getDontRegisseur()+data.getMontant();
				item.setDontRegisseur(montant);
			}
			else{
				montant=item.getDontClient()+data.getMontant();
				item.setDontClient(montant);
			}
		}
		setRecetteArticLeList(byArticleList);
		
	}


	private void prepareDataByLB() {
		List<RecetteLigneBudg> byLbList=new ArrayList<>();
		RecetteLigneBudg item=null;
		ArticleDetails detail=null;
		int index=0;
		double montant=0;
		for(ImportedData data:getImportedDataList()){			
			detail=data.getArticle().getDetail();
			item=new RecetteLigneBudg(detail);
			index=byLbList.indexOf(item);
			if(index>=0) item=byLbList.get(index);
			else byLbList.add(item);
			if(data.getClientGts().isRegisseur()){
				montant=item.getDontRegisseur()+data.getMontant();
				item.setDontRegisseur(montant);
			}
			else{
				montant=item.getDontClient()+data.getMontant();
				item.setDontClient(montant);
			}
		}
		setRecetteLigneBudgList(byLbList);
		
	}


	private void checkData(List<ImportedData> list, boolean forSearch) {
		ClientGts client=null;
		Article article=null;
		ArticleDetails detail=null;
		SimpleEntity s=null;
		int compteur=0;
		for(ImportedData data:list){
			compteur++;
			logger.debug("completeData {}: {} ; {},{},{}",compteur,data.getCodeClientGts(),data.getCodeArticle(),data.getExercice(),data.getNumero());
			//logger.debug("completeData: {} ; {}",getExercice(),getNumPeriode());
			client=service.getClientGts(data.getCodeClientGts());
			article=service.getArticle(data.getCodeArticle());
			if((!data.getExercice().equals(getExercice()))||(!data.getNumero().equals(getNumPeriode()))){
				s=new SimpleEntity(data.getExercice()+""+data.getNumero(),HandlerJSFMessage.formatMessage(HandlerJSFMessage.getErrorMessage("gts.periode.incorrect"), new Object[]{data.getExercice().toString(),data.getNumero(),getExercice().toString(),getNumPeriode()}));
				getErrorReport().add(s);
				logger.debug("completeData: {}",s.getDesignation());
			}
			if(client==null){
				s=new SimpleEntity(data.getCodeClientGts(),HandlerJSFMessage.formatMessage(HandlerJSFMessage.getErrorMessage("gts.client.notFound"), new Object[]{data.getCodeClientGts()}));
				getErrorReport().add(s);
				logger.debug("completeData: {}",s.getDesignation());
			}
			if(article==null){
				s=new SimpleEntity(data.getCodeArticle(),HandlerJSFMessage.formatMessage(HandlerJSFMessage.getErrorMessage("gts.article.notFound"), new Object[]{data.getCodeArticle()}));
				getErrorReport().add(s);
				logger.debug("completeData: {}",s.getDesignation());
			}
			else{
				detail=article.getDetailItem(getExercice());
				if(detail==null){
					s=new SimpleEntity(data.getCodeArticle()+"/"+data.getCodeArticle(),HandlerJSFMessage.formatMessage(HandlerJSFMessage.getErrorMessage("gts.article.detail.notFound"), new Object[]{data.getCodeArticle(),getExercice()}));
					getErrorReport().add(s);
					logger.debug("completeData: {}",s.getDesignation());
				}
				article.setDetail(detail);
			}
			GtsHelper.getReferentielBean().loadAdressTiers(client);
			data.setClientGts(client);
			data.setArticle(article);
			if(forSearch)continue;
			data.setTrace(Helper.createTrace());
			if(Strings.isBlank(data.getRefCommande()))data.setRefCommande(DEFAULT_refCommande);
		}
	
}


	@Override
	protected String getSavedPath() throws MissingConfiguration {
		Path path=Constant.getProjectRootPath().resolve("GTS").resolve(""+getExercice());	
		return path.toString();
	}

	@Override
	protected String getImportFileName() {
		String fileName=null;
		fileName=Constant.getPrefixGtsFileName()+getNumPeriode()+".xls";		
		return fileName;
	}

	@Override
	protected void resetDynamicList() {
		setImportedDataList(null);
        setErrorReport(null);
        setRecetteArticLeList(null);
        setRecetteLigneBudgList(null); 
        setLiqRecetteList(null);
        setDiffereLiqRecetteList(null);
        setLiqRecetteTree(null);
        setLiqRecetteDiffTree(null);
	}

	public Integer getNumPeriode() {
		return numPeriode;
	}

	public void setNumPeriode(Integer numPeriode) {
		this.numPeriode = numPeriode;
		resetDynamicList();
	}
	public Integer getExercice() {
		return getDataListBean().getExercice();
	}
	private DataListBean getDataListBean(){
		return BudgetHelper.getDataListBean();
	}


	public List<ImportedData> getImportedDataList() {
		return importedDataList;
	}


	public void setImportedDataList(List<ImportedData> importedDataList) {
		this.importedDataList = importedDataList;
	}
	
	public List<PeriodeGts> getPeriodesList() {
		if(periodesList==null){
			if(getExercice()!=null)
				setPeriodesList(service.getPeriodes(getExercice(), null,false));
		}	
		
		return periodesList;
	}


	public void setPeriodesList(List<PeriodeGts> periodesList) {
		this.periodesList = periodesList;
	}

	
	public boolean isCommonRequiredDone(){
		Integer exercice=getExercice();
		Integer numPeriode=getNumPeriode();
		if(exercice==null)return false;
		if(numPeriode==null)return false;
		
		switch (getCurrentAction()) {
		case GTS_IMPORT:		
			if(!controlerPeriode())return false;			
		case GTS_GENERATION:						
			return true;	
		default:					
		}		
		
		return true;
	}
	

	public boolean isRequiredDataDone() {		
		switch (getCurrentAction()) {
		case GTS_IMPORT:		
			if (!isCommonRequiredDone())
				return false;
			if (getImportFileUploadEvent() == null)
				return false;
			return true;
		case GTS_GENERATION:case GTS_CONSULTATION:	
			if (!isCommonRequiredDone())
				return false;			
			return true;	
		default:
			return true;		
		}
	}
	
	public Action getCurrentAction(){
		return BudgetHelper.getNavigationBean().getCurrentAction();
	}
	/**
	 * Enregistrement des données importées à partir du fichier
	 */
	public void save() {	
		try {
			List<ImportedData> list=getImportedDataList();
			PeriodeGts periode=getSelectedPeriode();
			if(getImportedDataList().isEmpty()) return;	
			logger.debug("saveGtsList: size ={}",list.size());
			service.saveImport(getExercice(),list, periode);			
		
			HandlerJSFMessage.addInfo(MsgEntry.SUCCES);
		} catch (Exception e) {
			e.printStackTrace();
			HandlerJSFMessage.addErrorMessage(Util.getErrorMessageFromException(MsgEntry.FAILED, e));
		}
	}
	
	/**
	 * Géraration des liquidations
	 */
	public void generer()
	{
		logger.debug("generer : Début ");
		try{

		service.genererLiqRecette(getLiqRecetteList(),"LREC");		
		service.saveGeneration(getLiqRecetteList(),getSelectedPeriode());
		
		//Génération des liq de recette différées pour les anciens periodes
		service.genererLiqRecette(getDiffereLiqRecetteList(),"LREC");		
		service.saveGeneration(getDiffereLiqRecetteList(),null);
		HandlerJSFMessage.addInfo(MsgEntry.SUCCES);
		} catch (Exception e) {
			e.printStackTrace();
			HandlerJSFMessage.addErrorMessage(Util.getErrorMessageFromException(MsgEntry.FAILED, e));
		}
		
		logger.debug("generer : End ");
	}
	
	public void setService(GtsService service) {
		this.service = service;
	}

	
	public void resetEntetelist() {
		setImportFileUploadEvent(null);
		setPeriodesList(null);	
		setTraitePeriodesList(null);
		setChargePeriodesList(null);
	}
	
	public void reset()
	{
		resetEntetelist();
		resetDynamicList();
		resetOthers();
	}
	
	public void resetOthers()
	{
		setNumPeriode(null);	
		
	}
	
	public void exreciceLsitener()
	{
		reset();
	}

	public String getErreursCount() {
		if (getErrorReport() == null)
			return "";
		return "(" + getErrorReport().size() + ")";
	}

	public String getLiqRecetteCount() {
		if (getLiqRecetteList() == null)
			return "";
		return "(" + getLiqRecetteList().size() + ")";

	}

	public String getRecetCodeArticCount() {
		if (getRecetteArticLeList() == null)
			return "";
		return "(" + getRecetteArticLeList().size() + ")";
	}

	public String getRecetLigneBudgCount() {
		if (getRecetteLigneBudgList() == null)
			return "";
		return "(" + getRecetteLigneBudgList().size() + ")";	
	}

	public boolean isImportDataVisible()
	{
		if(isErrorReportVisible()) return false;
		if(!isRecetteArticLeVisible()&&!isRecetteLigneBudgVisible()&&!isRecetteArticLeVisible())return false;
		
		return true;
	}
	
	public boolean isErrorReportVisible()
	{
		if(getErrorReport()==null) return false;
		if(getErrorReport().isEmpty())return false;
		return true;
	}
	
	public boolean isSaveAutorized()
	{		
		if(isErrorReportVisible())return false;
		if(!isRecetteArticLeVisible()&&!isRecetteLigneBudgVisible()&&!isRecetteArticLeVisible())return false;
		if(getSelectedPeriode().isAuPlusCharge())
		return true;
		return false;
	}
	



	public List<RecetteLigneBudg> getRecetteLigneBudgList() {
		return recetteLigneBudgList;
	}


	public void setRecetteLigneBudgList(List<RecetteLigneBudg> recetteLigneBudgList) {
		this.recetteLigneBudgList = recetteLigneBudgList;
	}


	public List<RecetteCodeArticle> getRecetteArticLeList() {
		return recetteArticLeList;
	}


	public void setRecetteArticLeList(List<RecetteCodeArticle> recetteArticLeList) {
		this.recetteArticLeList = recetteArticLeList;
	}

   public boolean isDataVisible()
   {
	if(isErrorReportVisible()) return true;
	if(isRecetteArticLeVisible()||isRecetteLigneBudgVisible()||isRecetteArticLeVisible())return true;
		return false; 
   }
   

   
	public boolean isLiqRecetteVisible()
	{
		if(getLiqRecetteList()==null)return false;
		if(getLiqRecetteList().isEmpty())return false;
		return true;
	}
	
	public PeriodeGts getSelectedPeriode()
	{
		return  getPeriode(getNumPeriode()); 
	}

	public PeriodeGts getPeriode(Integer numero)
	{
		if(getPeriodesList()!=null)
		for (PeriodeGts periode : getPeriodesList()) {
			if(periode.getNumero()!=numero)continue;
			return periode;
		}
		return null;
	
	}

	public boolean isGenerateAutorized() {
		
		if ((!isLiqRecetteVisible()) && (!isLiqRecetteDiffVisible()))
			return false;
		if(getSelectedPeriode()==null)return false;
		if (!getSelectedPeriode().isCharge())
			return false;
		return true;
	}



	public void periodeListener()
	{
		controlerPeriode();
	
	}
	
	private boolean  controlerPeriode()
	{
		PeriodeGts periode=getSelectedPeriode();
		PeriodeGts periodeBefore=null;
		String msgArg=null;
		String msgKey=null;
		boolean isWarn=false;
		String msg=null;
		
		if(periode==null) return false;
		if(periode.getEtat()==null)return false;
		switch (periode.getEtat()) {
		case TRAITE:
			msgKey=MsgEntry.PERIODE_TRAITE_ERROR;
			msgArg=Integer.toString(periode.getNumero());
			break;
		case CHARGE:
			msgKey=MsgEntry.PERIODE_CHARGE_WARN;
			msgArg=Integer.toString(periode.getNumero());
			isWarn=true;
			break;
		case OUVERT:
			for(int i=1; i<periode.getNumero();i++){
			periodeBefore=getPeriode(i);
			if(periodeBefore!=null)
			if(!periodeBefore.isAuMoinCharge()){
				msgArg=Integer.toString(periodeBefore.getNumero());
				msgKey=MsgEntry.PERIODE_AU_MOIN_CHARGE_ERROR;
				break;
			}	
			}
			break;	
		default:
			break;
		}
		if(msgKey==null)return true;
		if(isWarn){
			HandlerJSFMessage.addWarn(msgKey);
			return true;
		}
		if(msgArg==null){
			HandlerJSFMessage.addError(msgKey);
			return false;
		}	
		else
		{
			msg=HandlerJSFMessage.formatMessage(HandlerJSFMessage.getErrorMessage(msgKey), new Object[]{msgArg});	
		     HandlerJSFMessage.addErrorMessage(msg);
		     return false;
		}
	}
	
	public boolean isRecetteLigneBudgVisible()
	{
		if(getRecetteLigneBudgList()==null)return false;
		if(getRecetteLigneBudgList().isEmpty())return false;
		return true;
	}
	
	public boolean isRecetteArticLeVisible()
	{
		if(getRecetteArticLeList()==null)return false;
		if(getRecetteArticLeList().isEmpty())return false;
		return true;
	}
	
	public void search()
	{	    
	    logger.debug("search  --> Début ...");
	    try{
		switch (getCurrentAction()) {
		case GTS_GENERATION:
			List<ImportedData> listData=service.getImportedData(getExercice(), getNumPeriode(),null,null);
			checkData(listData, true);
			setImportedDataList(listData);
			prepareDisplayData();
			setLiqRecetteList(createLiqRecList(listData));
	
			setLiqRecetteTree(null);
			setLiqRecetteDiffTree(null);
			calculateTotauxRecetArticLe(getRecetteArticLeList());
			calculateTotauxRecetLb(getRecetteLigneBudgList());
			//Charger les liquidations de recette differées
			setDiffereLiqRecetteList(service.getLiqRecetteDiffere(getExercice()));
			
			logger.debug("search list GTS_GENERATION: {}",listData==null?0:listData.size());
			break;
		case GTS_CONSULTATION:
			List<LiqRecette> listLiquid=service.getLiqRecettes(getExercice(), getNumPeriode());
			completeLbData(listLiquid);
			setLiqRecetteList(listLiquid);
			setLiqRecetteTree(null);
			setLiqRecetteDiffTree(null);
			logger.debug("search list GTS_CONSULTATION: {}",listLiquid==null?0:listLiquid.size());
				break;
		default:
			break;
		}
		calculateTotauxLiqRec(getLiqRecetteList());
		calculateTotauxLiqRecDiff(getDiffereLiqRecetteList());
	    }catch(Exception e){
	    	HandlerJSFMessage.addErrorMessage(Util.getErrorMessageFromException(MsgEntry.FAILED, e));
	    }
		
	}
	private void completeLbData(List<LiqRecette> listLiquid) {
		LigneBudgetaireAE lbi=null;
		for(LiqRecette lrec:listLiquid){
			lbi=bpService.getLbi(lrec.getExercice(),lrec.getNoLbi());
			lrec.setDestination(lbi.getDestination());
			lrec.setNature(lbi.getNature());
			lrec.setService(lbi.getService());
			lrec.setProgramme(lbi.getProgramme());
		}
		
	}
	
	private void calculateTotauxLiqRec(List<LiqRecette> listLiquid) {
		resetTotauxLiqRec();
		if(listLiquid==null)return;
		for (LiqRecette liqRecette : listLiquid) {
			totalMntHtLiqReq=totalMntHtLiqReq.add(liqRecette.getTotalMntHtLiqReq());
			totalMntRemiseLiqReq=totalMntRemiseLiqReq.add(liqRecette.getTotalMntRemiseLiqReq());
			totalMntTtcLiqReq=totalMntTtcLiqReq.add(liqRecette.getTotalMntTtcLiqReq());
			totalMntTvaLiqReq=totalMntTvaLiqReq.add(liqRecette.getTotalMntTvaLiqReq());	
		}
	}


	private void resetTotauxLiqRec() {
		setTotalMntHtLiqReq(new BigDecimal(0));
		setTotalMntRemiseLiqReq(new BigDecimal(0));
		setTotalMntTtcLiqReq(new BigDecimal(0));
		setTotalMntTvaLiqReq(new BigDecimal(0));		
	}


	public StreamedContent getXlsRecetLignBudg(){
		StreamedContent returnStreamedContent = null;
		ExcelHandler excel = new ExcelHandler(ExcelModelEnum.GTS_IMPORT_LB,getRecetteLigneBudgList());
		returnStreamedContent = excel.getExcelFile();
		if (logger.isDebugEnabled()) {
			logger.debug("getXlsRecetLignBudg() - end"); //$NON-NLS-1$
		}
		return returnStreamedContent;
		

	}  
  
  public StreamedContent getXlsRecetCodeArticl(){
		StreamedContent returnStreamedContent = null;
		ExcelHandler excel = new ExcelHandler(ExcelModelEnum.GTS_IMPORT_ARTICL,getRecetteArticLeList());
		returnStreamedContent = excel.getExcelFile();
		if (logger.isDebugEnabled()) {
			logger.debug("getXlsRecetCodeArticl() - end"); //$NON-NLS-1$
		}
		return returnStreamedContent;
		

	}


public List<LiqRecette> getLiqRecetteList() {
	return this.liqRecetteList;
}


public void setLiqRecetteList(List<LiqRecette> liqRecetteList) {
	this.liqRecetteList = liqRecetteList;
}  
  
private List<LiqRecette> createLiqRecList(List<ImportedData> dataList) throws MissingConfiguration{
	logger.debug("createLiqRecList --> Début");
	List<LiqRecette> liqRecList=new ArrayList<>();
	if(dataList==null) return liqRecList;
	LiqRecette liqRec=null;
	LigneRecette ligneRec=null;
	
	for(ImportedData data:dataList){
		liqRec=regrouperLiqRec(data,liqRecList);
		if(liqRec==null){
			liqRec=createLiqRec(data);
			liqRecList.add(liqRec);
		}
		ligneRec=createLigneRecette(data);
		ligneRec.setNumero(liqRec.getDetails().size()+1);
		ligneRec.setLiqRecette(liqRec);
		liqRec.getDetails().add(ligneRec);
	}
	logger.debug("createLiqRecList --> Fin : size of list {}",liqRecList.size());
	return liqRecList;
}

private LigneRecette createLigneRecette(ImportedData data) {
	LigneRecette ligne=new LigneRecette(data);
	logger.debug("createLigneRecette : {}",ligne);
	return ligne;
}

private LiqRecette createLiqRec(ImportedData data) throws MissingConfiguration {
	LiqRecette liqRec=new LiqRecette(data);
	ArticleDetails articleDetail=data.getArticle().getDetail();
	liqRec.setDestination(articleDetail.getDestination().getCode());
	liqRec.setNature(articleDetail.getNature().getCode());
	liqRec.setService(articleDetail.getService().getCode());
	liqRec.setProgramme(articleDetail.getProgramme().getCode());
	
	String objet=Strings.isBlank(data.getObjet())?GtsHelper.Billetterie_OBJ:data.getObjet();
	liqRec.setLibelle(objet);
	liqRec.setModePaie(loadModePaie());
	liqRec.setTrace(Helper.createTrace());
	logger.debug("createLiqRec : {}",liqRec);
	return liqRec;
}

private String loadModePaie() throws MissingConfiguration {
	return AppCfgConfig.getInstance().getModePaieLib();
}

/**
 * Permet de regrouper les données importées par numero LB, code tiers et reference commande
 * @param data represente une ligne des données importées
 * @param liqRecList une Liquidation de recette
 * @return
 */
private LiqRecette regrouperLiqRec(ImportedData data, List<LiqRecette> liqRecList) {
	if((liqRecList==null)||(liqRecList.isEmpty()))return null;
	for(LiqRecette liqRec:liqRecList){
		if(!liqRec.getNoLbi().equals(data.getArticle().getDetail().getNoLbi())) continue;
		if(!liqRec.getTiersOrigine().equals(data.getClientGts().getCodeCpwin())) continue;
		if(!liqRec.getRefCommande().equals(data.getRefCommande()))continue;
		return liqRec;
	}
	return null;
}
  
  public StreamedContent getXlsLiqRecette(){
		StreamedContent returnStreamedContent = null;
		ExcelHandler excel = new ExcelHandler(ExcelModelEnum.GTS_IMPORT_LIQAG, getLiqRecetteList());
		returnStreamedContent = excel.getExcelFile();
		if (logger.isDebugEnabled()) {
			logger.debug("getXlsLiqRecette() - end"); //$NON-NLS-1$
		}
		return returnStreamedContent;		

	}
  
  public StreamedContent getXlsLiqRecetteConsult(){
		StreamedContent returnStreamedContent = null;
		ExcelHandler excel = new ExcelHandler(ExcelModelEnum.GTS_IMPORT_LIQAG, getLiqRecetteList());
		returnStreamedContent = excel.getExcelFile();
		if (logger.isDebugEnabled()) {
			logger.debug("getXlsLiqRecette() - end"); //$NON-NLS-1$
		}
		return returnStreamedContent;

	}

public TreeNode getLiqRecetteTree() {
	 if(liqRecetteTree==null)liqRecetteTree=createLiqRecetteTree();
		return liqRecetteTree;

}

public void setLiqRecetteTree(TreeNode liqRecetteTree) {
	this.liqRecetteTree = liqRecetteTree;
}  


	public TreeNode createLiqRecetteTree() {
		TreeNode rootTree = new DefaultTreeNode();
		rootTree.setExpanded(true);
		TreeNode childTree = null;
		TreeNode parentTree = null;
		if (getLiqRecetteList() == null)
			return null;
		for (LiqRecette liqRec : getLiqRecetteList()) {
			parentTree = new DefaultTreeNode(new LigneRecette(liqRec), rootTree);
			parentTree.setExpanded(true);
			if (liqRec.getDetails() == null)
				continue;
			for (LigneRecette ligneLiqRec : liqRec.getDetails()) {
				childTree=new DefaultTreeNode(ligneLiqRec, parentTree);
				childTree.setExpanded(true);
			}
		}
		return rootTree;
	}
public void setBpService(IBudgetPluriannuelService bpService) {
		this.bpService = bpService;
	}
	
	public BigDecimal getTotalMntRemiseLiqReq() {
		return totalMntRemiseLiqReq;
	}


	public void setTotalMntRemiseLiqReq(BigDecimal totalMntRemiseLiqReq) {
		this.totalMntRemiseLiqReq = totalMntRemiseLiqReq;
	}


	public BigDecimal getTotalMntTvaLiqReq() {
		return totalMntTvaLiqReq;
	}


	public void setTotalMntTvaLiqReq(BigDecimal totalMntTvaLiqReq) {
		this.totalMntTvaLiqReq = totalMntTvaLiqReq;
	}


	public BigDecimal getTotalMntHtLiqReq() {
		return totalMntHtLiqReq;
	}


	public void setTotalMntHtLiqReq(BigDecimal totalMntHtLiqReq) {
		this.totalMntHtLiqReq = totalMntHtLiqReq;
	}


	public BigDecimal getTotalMntTtcLiqReq() {
		return totalMntTtcLiqReq;
	}


	public void setTotalMntTtcLiqReq(BigDecimal totalMntTtcLiqReq) {
		this.totalMntTtcLiqReq = totalMntTtcLiqReq;
	}


	public List<PeriodeGts> getTraitePeriodesList() {
		List<PeriodeGts> list=getPeriodesList();
		if((traitePeriodesList==null)&&(list!=null)){
			traitePeriodesList=new ArrayList<PeriodeGts>();
			for(PeriodeGts p:list){
				if(!p.isClosed()&&(p.getEtat()==PeriodeEnum.TRAITE))
					traitePeriodesList.add(p);
			}
		}
		return traitePeriodesList;
	}


	public void setTraitePeriodesList(List<PeriodeGts> traitePeriodesList) {
		this.traitePeriodesList = traitePeriodesList;
	}


	public List<PeriodeGts> getChargePeriodesList() {
		List<PeriodeGts> list=getPeriodesList();
		if((chargePeriodesList==null)&&(list!=null)){
			
			chargePeriodesList=new ArrayList<PeriodeGts>();
			for(PeriodeGts p:list){
				if(!p.isClosed()&&(p.getEtat()==PeriodeEnum.CHARGE))
					chargePeriodesList.add(p);
			}
		}
		return chargePeriodesList;
	}


	public void setChargePeriodesList(List<PeriodeGts> chargePeriodesList) {
		this.chargePeriodesList = chargePeriodesList;
	}
		
	private void resetTotauxRecetArticLe() {
		setTotalMntRecArtic(0);
		setTotalDontCltRecArtic(0);
        setTotalDontRegisRecArtic(0);
	}
	
	private void calculateTotauxRecetArticLe(List<RecetteCodeArticle> recetteArticLeList) {
		resetTotauxRecetArticLe();
		if(recetteArticLeList==null)return;
		for (RecetteCodeArticle recette : recetteArticLeList) {
			totalDontCltRecArtic+=recette.getDontClient();
			totalDontRegisRecArtic+=recette.getDontRegisseur();
			totalMntRecArtic+=recette.getMntTotal();
		}
	}

	private void resetTotauxRecetLb() {
		setTotalMntRecLb(0);
		setTotalDontCltRecLb(0);
        setTotalDontRegisRecLb(0);
	}
	
	private void calculateTotauxRecetLb(List<RecetteLigneBudg> recetteLigneBudgList) {
		resetTotauxRecetLb();
		if(recetteLigneBudgList==null)return;
		for (RecetteLigneBudg recette : recetteLigneBudgList) {
			totalDontCltRecLb+=recette.getDontClient();
			totalDontRegisRecLb+=recette.getDontRegisseur();
			totalMntRecLb+=recette.getMntTotal();
		}
	}
	

	public double getTotalMntRecArtic() {
		return totalMntRecArtic;
	}


	public void setTotalMntRecArtic(double totalMntRecArtic) {
		this.totalMntRecArtic = totalMntRecArtic;
	}


	public double getTotalDontRegisRecArtic() {
		return totalDontRegisRecArtic;
	}


	public void setTotalDontRegisRecArtic(double totalDontRegisRecArtic) {
		this.totalDontRegisRecArtic = totalDontRegisRecArtic;
	}


	public double getTotalDontCltRecArtic() {
		return totalDontCltRecArtic;
	}


	public void setTotalDontCltRecArtic(double totalDontCltRecArtic) {
		this.totalDontCltRecArtic = totalDontCltRecArtic;
	}


	public double getTotalMntRecLb() {
		return totalMntRecLb;
	}


	public void setTotalMntRecLb(double totalMntRecLb) {
		this.totalMntRecLb = totalMntRecLb;
	}


	public double getTotalDontRegisRecLb() {
		return totalDontRegisRecLb;
	}


	public void setTotalDontRegisRecLb(double totalDontRegisRecLb) {
		this.totalDontRegisRecLb = totalDontRegisRecLb;
	}


	public double getTotalDontCltRecLb() {
		return totalDontCltRecLb;
	}


	public void setTotalDontCltRecLb(double totalDontCltRecLb) {
		this.totalDontCltRecLb = totalDontCltRecLb;
	}


	public List<LiqRecette> getDiffereLiqRecetteList() {
		return differeLiqRecetteList;
	}


	public void setDiffereLiqRecetteList(List<LiqRecette> differeLiqRecetteList) {
		this.differeLiqRecetteList = differeLiqRecetteList;
	}

	   public boolean isLiqRecetteDiffVisible()
		{
			if(getDiffereLiqRecetteList()==null)return false;
			if(getDiffereLiqRecetteList().isEmpty())return false;
			return true;
		}
	   
	   
	   public String getLiqRecetteDiffCount() {
			if (getDiffereLiqRecetteList() == null)
				return "";
			return "(" + getDiffereLiqRecetteList().size() + ")";

		}
	   
	   public TreeNode createLiqRecetteDiffTree() {
			TreeNode rootTree = new DefaultTreeNode();
			rootTree.setExpanded(true);
			TreeNode childTree = null;
			TreeNode parentTree = null;
			if (getDiffereLiqRecetteList() == null)
				return null;
			for (LiqRecette liqRec : getDiffereLiqRecetteList()) {
				parentTree = new DefaultTreeNode(new LigneRecette(liqRec), rootTree);
				parentTree.setExpanded(true);
				if (liqRec.getDetails() == null)
					continue;
				for (LigneRecette ligneLiqRec : liqRec.getDetails()) {
					childTree=new DefaultTreeNode(ligneLiqRec, parentTree);
					childTree.setExpanded(true);
				}
			}
			return rootTree;
		}


	public TreeNode getLiqRecetteDiffTree() {	
		 if(liqRecetteDiffTree==null)liqRecetteDiffTree=createLiqRecetteDiffTree();
			return liqRecetteDiffTree;
	}


	public void setLiqRecetteDiffTree(TreeNode liqRecetteDiffTree) {
		this.liqRecetteDiffTree = liqRecetteDiffTree;
	}
	
	
	private void calculateTotauxLiqRecDiff(List<LiqRecette> listLiquid) {
		resetTotauxLiqRecDiff();
		if(listLiquid==null)return;
		for (LiqRecette liqRecette : listLiquid) {
			totalMntHtLiqReqDiff=totalMntHtLiqReqDiff.add(liqRecette.getTotalMntHtLiqReq());
			totalMntRemiseLiqReqDiff=totalMntRemiseLiqReqDiff.add(liqRecette.getTotalMntRemiseLiqReq());
			totalMntTtcLiqReqDiff=totalMntTtcLiqReqDiff.add(liqRecette.getTotalMntTtcLiqReq());
			totalMntTvaLiqReqDiff=totalMntTvaLiqReqDiff.add(liqRecette.getTotalMntTvaLiqReq());	
		}
	}
	
	private void resetTotauxLiqRecDiff() {
		setTotalMntHtLiqReqDiff(new BigDecimal(0));
		setTotalMntRemiseLiqReqDiff(new BigDecimal(0));
		setTotalMntTtcLiqReqDiff(new BigDecimal(0));
		setTotalMntTvaLiqReqDiff(new BigDecimal(0));		
	}
	
	public BigDecimal getTotalMntRemiseLiqReqDiff() {
		return totalMntRemiseLiqReqDiff;
	}


	public void setTotalMntRemiseLiqReqDiff(BigDecimal totalMntRemiseLiqReqDiff) {
		this.totalMntRemiseLiqReqDiff = totalMntRemiseLiqReqDiff;
	}


	public BigDecimal getTotalMntTvaLiqReqDiff() {
		return totalMntTvaLiqReqDiff;
	}


	public void setTotalMntTvaLiqReqDiff(BigDecimal totalMntTvaLiqReqDiff) {
		this.totalMntTvaLiqReqDiff = totalMntTvaLiqReqDiff;
	}


	public BigDecimal getTotalMntHtLiqReqDiff() {
		return totalMntHtLiqReqDiff;
	}


	public void setTotalMntHtLiqReqDiff(BigDecimal totalMntHtLiqReqDiff) {
		this.totalMntHtLiqReqDiff = totalMntHtLiqReqDiff;
	}


	public BigDecimal getTotalMntTtcLiqReqDiff() {
		return totalMntTtcLiqReqDiff;
	}


	public void setTotalMntTtcLiqReqDiff(BigDecimal totalMntTtcLiqReqDiff) {
		this.totalMntTtcLiqReqDiff = totalMntTtcLiqReqDiff;
	}


	public StreamedContent getXlsLiqRecetteDiff(){
		StreamedContent returnStreamedContent = null;
		ExcelHandler excel = new ExcelHandler(ExcelModelEnum.GTS_IMPORT_LIQAG, getDiffereLiqRecetteList());
		returnStreamedContent = excel.getExcelFile();
		if (logger.isDebugEnabled()) {
			logger.debug("getXlsLiqRecetteDiff() - end"); //$NON-NLS-1$
		}
		return returnStreamedContent;
		

	}
	
}