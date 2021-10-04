package fr.symphonie.tools.recette;

import java.io.File;
import java.io.Serializable;
import java.math.BigDecimal;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;

import org.apache.logging.log4j.util.Strings;
import org.primefaces.event.CellEditEvent;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.symphonie.budget.core.model.pluri.LigneBudgetaireAE;
import fr.symphonie.budget.core.service.IGestionTiersService;
import fr.symphonie.common.core.ICommonService;
import fr.symphonie.common.model.AppCfgConfig;
import fr.symphonie.common.util.BudgetHelper;
import fr.symphonie.common.util.Constant;
import fr.symphonie.common.util.MsgEntry;
import fr.symphonie.common.util.Util;
import fr.symphonie.cpwin.model.Tiers;
import fr.symphonie.exception.MissingConfiguration;
import fr.symphonie.tools.common.DataListBean;
import fr.symphonie.tools.common.GenericExcelImportor;
import fr.symphonie.tools.gts.core.GtsService;
import fr.symphonie.tools.gts.model.LigneRecette;
import fr.symphonie.tools.gts.model.LiqRecette;
import fr.symphonie.tools.recette.model.ImportedData;
import fr.symphonie.util.HandlerJSFMessage;
import fr.symphonie.util.Helper;
import fr.symphonie.util.model.SimpleEntity;

@ManagedBean(name="impRecBean")
@SessionScoped
public class ImportRecetteBean extends GenericExcelImportor<ImportedData>  implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2969929490927123701L;
	private static final Logger logger = LoggerFactory.getLogger(ImportRecetteBean.class);
	@ManagedProperty (value="#{gtsService}")
	private GtsService service;
	@ManagedProperty (value="#{commonService}")
	private ICommonService commonService;
	@ManagedProperty(value = "#{gestionTiersService}")
	private IGestionTiersService gestionTiersService;
	/**
	 * Les attribues
	 */
	//private String compteProduit;
	private String compteTva;
	private String compteClient;
	private Integer noLbi;
	private Integer noEnv;
	private String objet;
	private String modePaie=null;
	private Tiers selectedTiers;
	private BigDecimal amount;
	private float tva;
	private ImportedData recetToDelete;

	
	/**
	 * Les listes
	 */
	private List<ImportedData> importedDataList;
	private List<LiqRecette> liqRecetteList;
	private List<String> listTva;	
	/**
	 * Totaux
	 */
	private BigDecimal totalMntRemiseLiqReq;
	private BigDecimal totalMntTvaLiqReq;
	private BigDecimal totalMntHtLiqReq;
	private BigDecimal totalMntTtcLiqReq;
	/**
	 * les Tree
	 */
	private TreeNode liqRecetteTree;
	
	/**
	 * Status elements
	 */
	private boolean validationCompleted=false;
	private boolean generateLiqExecuted;
	
	
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
		setLiqRecetteList(createLiqRecList(list));
		calculateTotauxLiqRec(getLiqRecetteList());
		
	  } catch (Exception e) {
		HandlerJSFMessage.addErrorMessage(Util.getErrorMessageFromException(MsgEntry.FAILED, e));
		e.printStackTrace();
	   }
		
	}
/**
 * Géraration des liquidations
 */
public void generer()
{
	logger.debug("generer : Début ");
	try{
		logger.debug("generer : getLiqRecetteList()-size: "+getLiqRecetteList().size());
	service.genererLiqRecette(getLiqRecetteList(),"LREC");
	setLiqRecetteTree(null);
	setGenerateLiqExecuted(true);
	HandlerJSFMessage.addInfo(MsgEntry.SUCCES);
	} catch (Exception e) {
		e.printStackTrace();
		HandlerJSFMessage.addErrorMessageFromException(MsgEntry.FAILED, e);
	}
	
	logger.debug("generer : End ");
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
private void checkData(List<ImportedData> list, boolean forSearch) {
	Tiers tiers=null;
	
	SimpleEntity s=null;
	for(ImportedData data:list){
		logger.debug("checkData: {} ; {},{}",data.getCodeTiers(),data.getMontant(),data.getTauxTva());
		tiers=BudgetHelper.getTiersService().findTiers(data.getCodeTiers());
		if(tiers==null){
			s=new SimpleEntity(data.getCodeTiers(),HandlerJSFMessage.formatMessage(HandlerJSFMessage.getErrorMessage("tiers.notfound"), new Object[]{data.getCodeTiers()}));
			getErrorReport().add(s);
			logger.debug("checkData: {}",s.getDesignation());
		}
		else {
			data.setTiers(tiers);
		}
		if((data.getMontant()!=null)&&(data.getMontant().compareTo(BigDecimal.ZERO)==0)) {
			s=new SimpleEntity(data.getCodeTiers(),"Montant 0 non accepté");
			getErrorReport().add(s);
			logger.debug("checkData: {}",s.getDesignation());
		}
		
	}

}
private List<LiqRecette> createLiqRecList(List<ImportedData> dataList) throws MissingConfiguration{
	logger.debug("createLiqRecList --> Début");
	logger.debug("dataList: "+dataList);
	List<LiqRecette> liqRecList=new ArrayList<>();
	if(dataList==null) return liqRecList;
	LiqRecette liqRec=null;
	LigneRecette ligneRec=null;
	
	for(ImportedData data:dataList){
		//liqRec=regrouperLiqRec(data,liqRecList);
		//if(liqRec==null){
			liqRec=createLiqRec(data);
			liqRecList.add(liqRec);
		//}
		ligneRec=createLigneRecette(data);
		ligneRec.setNumero(liqRec.getDetails().size()+1);
		ligneRec.setLiqRecette(liqRec);
		liqRec.getDetails().add(ligneRec);
	}
	logger.debug("createLiqRecList --> Fin : size of list {}",liqRecList.size());
	return liqRecList;
}
private LiqRecette createLiqRec(ImportedData data) throws MissingConfiguration {
	LiqRecette liqRec=new LiqRecette();
	DataListBean dataListBean=getDataListBean();
	liqRec.setExercice(getExec());
	
	liqRec.setTiersOrigine(data.getCodeTiers());
	liqRec.setDebiteur(data.getCodeTiers());
	liqRec.setNoAdresse(data.getTiers().getAdresse().getOrdre());
	liqRec.setImputHt(getCompteProduit());
	liqRec.setImputTtc(getCompteClient());
	liqRec.setImputTva(getCompteTva());
	liqRec.setNoLbi(getNoLbi());
	liqRec.setDiffere(false);
	liqRec.setTiersCpwin(data.getTiers());
	liqRec.setDestination(dataListBean.getCodeDest());
	liqRec.setNature(dataListBean.getCodeNature());
	liqRec.setService(dataListBean.getCodeService());
	liqRec.setProgramme(dataListBean.getCodeProg());
	liqRec.setLibelle(getObjet());
	liqRec.setModePaie(getModePaie());
	liqRec.setTrace(Helper.createTrace());
	
//	logger.debug("createLiqRec : {}",liqRec);
	return liqRec;
}
private LigneRecette createLigneRecette(ImportedData data) {
	LigneRecette ligne=new LigneRecette();
	ligne.setExercice(getExec());;
	ligne.getPrestation().setDesignation(getObjet());
	ligne.setLibelle(getObjet());
	ligne.setQuantite(1);
	ligne.setPu(data.getMontant());
	ligne.setTauxRemise(0);
	ligne.setTauxTva(data.getTauxTva());
	ligne.calculateMontants();
//	logger.debug("createLigneRecette : {}",ligne);
	return ligne;
}

	@Override
	protected String getSavedPath() throws MissingConfiguration {
		Path path=Constant.getProjectRootPath().resolve("impRecette").resolve(""+getExercice());	
		return path.toString();
	}

	@Override
	protected String getImportFileName() {
		String fileName=null;
		fileName=Constant.getPrefixImportRecetteFileName()+getExercice()+".xls";		
		return fileName;
	}
	public void reset()
	{
		resetEntetelist();
		resetDynamicList();
		resetOthers();
	}
	public void resetEntetelist() {
		getDataListBean().reset();
//		setImportFileUploadEvent(null);
		setNoEnv(null);setNoLbi(null);
		setCompteClient(null);setCompteTva(null);
		setObjet(null);
		setModePaie(null);
		setListBudgets(null);
		setCodeBudget(null);
		
	}
	
	@Override
	protected void resetDynamicList() {
		super.resetDynamicList();
		setImportedDataList(null);
        setLiqRecetteList(null);		
		setLiqRecetteTree(null);
		
		
	}
	public List<ImportedData> getImportedDataList() {
		if(importedDataList==null)importedDataList=new ArrayList<ImportedData>();
		return importedDataList;
	}
	public void setImportedDataList(List<ImportedData> importedDataList) {
		this.importedDataList = importedDataList;
	}
	public List<LiqRecette> getLiqRecetteList() {
		return liqRecetteList;
	}
	public void setLiqRecetteList(List<LiqRecette> liqRecetteList) {
		this.liqRecetteList = liqRecetteList;
	}
	public void setService(GtsService service) {
		this.service = service;	
	}
	public String getCompteProduit() {
		return getDataListBean().getCompteProduit();
	}
//	public void setCompteProduit(String compteProduit) {
//		this.compteProduit = compteProduit;
//	}
	public String getCompteTva() {
		return compteTva;
	}
	public void setCompteTva(String compteTva) {
		this.compteTva = compteTva;
		resetDynamicList();
	
		
	}
	public String getCompteClient() {
		return compteClient;
	}
	public void setCompteClient(String compteClient) {
		this.compteClient = compteClient;
		resetDynamicList();
		
	}
	public Integer getNoLbi() {
		return noLbi;
	}
	public void setNoLbi(Integer noLbi) {
		this.noLbi = noLbi;
	
	}
	public Integer getNoEnv() {
		return noEnv;
	}
	public void setNoEnv(Integer noEnv) {
		this.noEnv = noEnv;
	
	}
	public String getObjet() {
		return objet;
	}
	public void setObjet(String objet) {
		this.objet = objet;
//		resetDynamicList();
		resetOthers();
	}
	public GtsService getService() {
		return service;
	}
	public String getModePaie() {
		if(modePaie==null)
			try {
				load(TaskEnum.ModePaie);
			} catch (MissingConfiguration e) {
				modePaie="";
			}
			
		return modePaie;
	}
	public void setModePaie(String modePaie) {
		this.modePaie = modePaie;	
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

	public boolean isRequiredDataDone() {
//		logger.debug("isRequiredDataDone: {},{},{},{},{},",isCommonRequiredDone(),getNoLbi() ,getObjet() ,getCompteProduit(),getImportFileUploadEvent());
		if (!isCommonRequiredDone())
			return false;
		if (getNoLbi() == null)
			return false;	
		if (Strings.isBlank(getObjet()))
			return false;
		if (getCompteProduit() == null)
			return false;
		if (Strings.isBlank(getCompteClient()))
			return false;
		if (Strings.isBlank(getCompteTva()))
				return false;
		if (getImportFileUploadEvent() == null)
			return false;
		return true;
	}

//	public boolean isCommonRequiredDone() {
//		Integer exercice = getExercice();
//		if (exercice == null)
//			return false;		
//		return true;
//	}
	
	 public boolean isDataVisible()
	   {
		if(isErrorReportVisible()) return true;
		if(isImportedDataListVisible())return true;
			return false; 
	   }
	 	 
		public boolean isImportedDataListVisible()
		{
			if(getImportedDataList()==null)return false;
			if(getImportedDataList().isEmpty())return false;
			return true;
		}
		
		public boolean isImportDataVisible()
		{
//			if(isErrorReportVisible()) return false;
			if(!isImportedDataListVisible())return false;
			
			return true;
		}
		
		public String getRecetCount() {
			if (getImportedDataList() == null)
				return "";
			return "(" + getImportedDataList().size() + ")";	
		}
		
		public boolean isLiqRecetteVisible()
		{
			if(getLiqRecetteList()==null)return false;
			if(getLiqRecetteList().isEmpty())return false;
			return true;
		}
		
		public boolean isGenerateAutorized() {	
			if(!isValidationCompleted()) return false;
			if (!isLiqRecetteVisible())
				return false;		
			
			return true;
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
		private DataListBean getDataListBean(){
			return BudgetHelper.getDataListBean();
			}
		
	private  void completeCompteImput() {
		logger.debug("completeCompteImput: start");
		String[] compteImput = service.getCtaCompteImput(getExec(), getCompteProduit());
		logger.debug("completeCompteImput: compteImput: {},{}",compteImput[0],compteImput[1]);
		setCompteClient(compteImput[0]);
		setCompteTva(compteImput[1]);
	}
	private void searchLbi() {
		//logger.debug("searchLbi: start");
		DataListBean dataList=getDataListBean();
		String codeDest=dataList.getCodeDest(), codeNature=dataList.getCodeNature();
		String codeService=dataList.getCodeService(), codeProg=dataList.getCodeProg();
		LigneBudgetaireAE lb=dataList.finLB(codeDest, codeNature, codeService, codeProg);
		//logger.debug("searchLbi: lb={}",lb);
		if(lb!=null) {
			setNoLbi(lb.getNoLbi());
			setNoEnv(lb.getNoEnv());
		}
		else{
			setNoLbi(null);
			setNoEnv(null);
		}
		
	}
	
	public void selectDestListener()
	{
		resetDynamicList();
		searchLbi();
	}
	
	public void selectProgListener()
	{
		searchLbi();
		resetDynamicList();
	}
	public void selectCmptProdListener()
	{
		resetDynamicList();
		completeCompteImput();
	}
	
	public void onExerciceChange() {
		super.onExerciceChange();
		DataListBean dataBean=getDataListBean();
		dataBean.setExec(getExercice());
		dataBean.setListBudgets(getListBudgets());
		dataBean.setCodeBudget(getCodeBudget());	
		
	}
	@Override
	protected String getModuleName() {
		return "impRecette";
	}
	@Override
	protected void prepare() throws MissingConfiguration {
		load(TaskEnum.ModePaie);
		
	}
	private void load(TaskEnum task) throws MissingConfiguration {
		switch(task) {
		case ModePaie:
			setModePaie(AppCfgConfig.getInstance().getModePaieLib());
			break;
		
		
		}
		
	}
	@Override
	protected ICommonService getCommonService() {
		return commonService;
	}
	public IGestionTiersService getGestionTiersService() {
		return gestionTiersService;
	}
	public void setGestionTiersService(IGestionTiersService gestionTiersService) {
		this.gestionTiersService = gestionTiersService;
	}
	public void setCommonService(ICommonService commonService) {
		this.commonService = commonService;
	}
	public enum TaskEnum {
		ModePaie
	}
	@Override
	protected SimpleEntity[] getRequirements() {
		// TODO Auto-generated method stub
		return null;
	}
	public Tiers getSelectedTiers() {
		return selectedTiers;
	}
	public void setSelectedTiers(Tiers selectedTiers) {
		this.selectedTiers = selectedTiers;
	}
	public BigDecimal getAmount() {
		return amount;
	}
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
	public void executeAddRecette() {
		ImportedData data=new ImportedData();
		data.setCodeTiers(getSelectedTiers().getCode());
		data.setTiers(getSelectedTiers());
		data.setMontant(getAmount());
		data.setTauxTva(getTva());
		getImportedDataList().add(data);
		resetSearch();
	}
	public void onTiersSelected() {
		String codeTiers=getSelectedTiers().getCode();
		Tiers trs=BudgetHelper.getTiersService().findTiers(codeTiers);
		
		setSelectedTiers(trs);
//		logger.debug("onTiersSelected: getIbans: "+getSelectedTiers().getIbans());
//		setIban(getSelectedTiers().getIbans().iterator().next());
	
	}
	public List<String> getListTva() {
		if(listTva==null)
		{
			listTva=Arrays.asList(Constant.TVA_LIST);
		}
		return listTva;
	}

	
	public float getTva() {
		return tva;
	}
	public void setTva(float tva) {
		this.tva = tva;
	}

	private void resetSearch() {
		setSelectedTiers(null);
		setTva(0);
		setAmount(null);
		
	}
	public ImportedData getRecetToDelete() {
		return recetToDelete;
	}
	public void setRecetToDelete(ImportedData recetToDelete) {
		this.recetToDelete = recetToDelete;
	}
	public void executeDeleteRecette() {
		getImportedDataList().remove(recetToDelete);
    }
	public void executeValidateRecette() {
		
		try {
			setLiqRecetteList(createLiqRecList(getImportedDataList()));
			calculateTotauxLiqRec(getLiqRecetteList());
//			setImportedDataList(null);
			setValidationCompleted(true);
		} catch (Exception e) {
			HandlerJSFMessage.addErrorMessage(Util.getErrorMessageFromException(MsgEntry.FAILED, e));
			e.printStackTrace();
		   }
		
	
		
	}
	
public boolean isAdresseUnique() {
	if(getSelectedTiers().getAdressesList()!=null)
	{
	if(getSelectedTiers().getAdressesList().size()==1)
		return true;
	return false;
		
	}
	return false;
}
public boolean isValidationCompleted() {
	return validationCompleted;
}
public void setValidationCompleted(boolean validationCompleted) {
	this.validationCompleted = validationCompleted;
}
public boolean isGenerRecetteAutorized() {
	
	if(!isValidationCompleted()) return false;
	return true;
}

public boolean isValidateAutorized() {	
//	if(isValidationCompleted()) return false;
	if (!isImportDataVisible())
		return false;		
	
	return true;
}
public void onRecetEdit(CellEditEvent event) {

}
private void resetOthers() {
	setValidationCompleted(false);
    setGenerateLiqExecuted(false);
}
public boolean isGenerateLiqExecuted() {
	return generateLiqExecuted;
}
public void setGenerateLiqExecuted(boolean generateLiqExecuted) {
	this.generateLiqExecuted = generateLiqExecuted;
}
}
