package fr.symphonie.tools.das.ui;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.annotation.ManagedBean;
import javax.enterprise.context.SessionScoped;
import javax.faces.annotation.ManagedProperty;

//import javax.faces.bean.ManagedBean;
//import javax.faces.bean.ManagedProperty;
//import javax.faces.bean.SessionScoped;

import org.primefaces.event.SelectEvent;
import org.primefaces.model.StreamedContent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.symphonie.budget.core.service.IGestionTiersService;
import fr.symphonie.budget.ui.beans.NavigationBean.Action;
import fr.symphonie.budget.ui.beans.SearchBean;
import fr.symphonie.budget.ui.beans.pluri.DialogHelper;
import fr.symphonie.budget.ui.excel.ExcelHandler;
import fr.symphonie.budget.ui.excel.ExcelModelEnum;
import fr.symphonie.common.util.BudgetHelper;
import fr.symphonie.common.util.Constant;
import fr.symphonie.common.util.MsgEntry;
import fr.symphonie.common.util.Util;
import fr.symphonie.cpwin.model.Tiers;
import fr.symphonie.excel.ExcelFileImportor;
import fr.symphonie.exception.MissingConfiguration;
import fr.symphonie.tools.das.Das2FileStruct;
import fr.symphonie.tools.das.DasHelper;
import fr.symphonie.tools.das.Das2FileStruct.ILine;
import fr.symphonie.tools.das.core.DasService;
import fr.symphonie.tools.das.model.BTQCEnum;
import fr.symphonie.tools.das.model.Honoraire;
import fr.symphonie.tools.das.model.TiersDas;
import fr.symphonie.tools.das.model.TiersImportedData;
import fr.symphonie.tools.das.model.TypeDasEnum;
import fr.symphonie.util.HandlerJSFMessage;
import fr.symphonie.util.Helper;
import fr.symphonie.util.model.SimpleEntity;


@ManagedBean
@SessionScoped
public class DasBean extends ExcelFileImportor implements Serializable {
	
	private static final long serialVersionUID = -6042858108321184494L;	
	private static final Logger logger = LoggerFactory.getLogger(DasBean.class);
	@ManagedProperty (value="#{dasService}")
	private DasService service;
	@ManagedProperty(value = "#{gestionTiersService}")
	private IGestionTiersService tiersService;
	private List<SimpleEntity> tiersImportedData;
	private List<TiersDas> existantList;
	private List<TiersDas> nouveauList;
	private TiersDas selectedTiers;
	private TiersDas tiersForSearch;
	
	private boolean modeProd=false;
	//DAS Honoraires
	private List<Honoraire> honoraires;
	private Honoraire selectedHonoraire;
	private Integer exerciceTest=null;
	
    List<BTQCEnum> btqcList = Arrays.asList(BTQCEnum.values());
    
	private String codeTiers;
	private String nomTiers;
	private Das2FileStruct dasStruct=null;
    private String numSiret;
    
	public void search() {
		resetDynamicList();
		logger.info("search: exercice --> {}",getExercice());
		try{
		if(isTiersImport())
		setTiersImportedData(importTiers());
		else if(isHonoraireImport()){
			setHonoraires(ImportHonoraires());
			Collections.sort(getHonoraires());
		}
		else if(isHonoraireGenerate()){
			setHonoraires(loadHonoraires());
			logger.info("genereer honoraire taile de la liste:{}",getHonoraires().size());
		}	
		
		else if (isHonoraireSuivi()) {
			setHonoraires(loadHonorairesSuivi());
			logger.info("genereer honoraire taile de la liste:{}", getHonoraires().size());

		}
		
		else if(isConsultTiers())
		{
			setExistantList(loadTiers(getCodeTiers(), getNumSiret()));
			logger.info("genereer honoraire taile de la liste:{}", getExistantList().size());
	
		}
		} catch (Exception e) {
			e.printStackTrace();
			HandlerJSFMessage.addErrorMessage(Util.getErrorMessageFromException(MsgEntry.FAILED, e));
			
		}

	}

	public void generateFile(){
		logger.debug("generateFile: start");
		Das2FileStruct dasStruct=null;
		String[] dasParams=null;
		String path=null;
		Integer exercice=isModeProd()?getExercice():getExerciceTest();
		try {
			dasParams=loadDasParams(exercice);
			path=getDasGeneratePath();
		
			dasStruct=createDasStruct(dasParams,getHonoraires());
			generateDematFile(path,dasStruct);
			
			setDasStruct(dasStruct);
			HandlerJSFMessage.addInfo(MsgEntry.SUCCES);
		} catch (Exception e) {
			HandlerJSFMessage.addErrorMessage(Util.getErrorMessageFromException(MsgEntry.FAILED, e));
			e.printStackTrace();
		} 
		logger.debug("generateFile: end");
		
	}
	private void generateDematFile(String path, Das2FileStruct dasStruct) {
		String targetFile=path+dasStruct.getGeneratedFileName();
		try (PrintWriter  out = new PrintWriter(new OutputStreamWriter(
			      new BufferedOutputStream(new FileOutputStream(targetFile)), "US-ASCII")) )
		{
			 out.println(DasHelper.display(dasStruct.getEnregistrement010()));
			 out.println(DasHelper.display(dasStruct.getEnregistrement020()));
			 for(ILine item:dasStruct.getEnregistrements210()){
				 out.println(DasHelper.display(item));
			 }			
			 
			 out.println(DasHelper.display(dasStruct.getEnregistrement300()));
			 out.println(DasHelper.display(dasStruct.getEnregistrement310()));

		  out.flush();
		} catch (UnsupportedEncodingException|FileNotFoundException e) {
		  e.printStackTrace();
		} 
		
	}

	private Das2FileStruct createDasStruct(String[] dasParams, List<Honoraire> honoraires) {
		return new Das2FileStruct(dasParams, honoraires);
	}

	private String getDasGeneratePath() throws MissingConfiguration {
		StringBuilder path=new StringBuilder(getSavedPath());
		path.append(File.separator);
		path.append(getExercice());
		path.append(File.separator);
		 Path p =Paths.get(path.toString());
		if (Files.notExists(p)) {
			 try {
				 logger.debug("getDasGeneratePath: Files.createDirectories");
				Files.createDirectories(p);
			} catch (IOException e) {
				e.printStackTrace();
			}
		 }
		
		
		return path.toString();
	}

	private String[] loadDasParams(Integer exercice) throws MissingConfiguration {
		
		String[] params= service.getEtablisConfigParams();
		params[Constant.EXERCICE]=""+exercice;
		String[] adrs=new String[]{params[Constant.AdresseLigne1],params[Constant.AdresseLigne2]};
		params[Constant.ADRESSE_N_VOIE]=DasHelper.getNumVoie(adrs);
		params[Constant.NATURE_ET_NOM_VOIE]=DasHelper.getNatureEtNomVoie(adrs);
		params[Constant.Mode]=isModeProd()?null:"Test";
		return params;
	}

	@Override
	public void reset() {
		resetDynamicList();
		resetEnteteList();	
		setSelectedTiers(null);
		setSelectedHonoraire(null);
		setTiersForSearch(null);
		setCodeTiers(null);
		setNumSiret(null);
	}
	public void save(){
		try{
			if(isTiersImport())
			{
			saveDasList(getNouveauList());
			saveDasList(getExistantList());
			}
			else if(isHonoraireImport()){
				saveDasList(getHonoraires());
			}
			else if(isConsultTiers())
			{
				saveDasList(getExistantList());
			}
			
			HandlerJSFMessage.addInfo(MsgEntry.SUCCES);
		} catch (Exception e) {
			e.printStackTrace();
			HandlerJSFMessage.addErrorMessage(Util.getErrorMessageFromException(MsgEntry.FAILED, e));
		}
	}
	private <T extends Object> void saveDasList(List<T> dasList) {		
		if(dasList.isEmpty()) return;
		
		logger.debug("saveDasList: size ={}",dasList.size());
		service.saveList(dasList);
		
	}
	public void rapprochement(){
		logger.debug("rapprochement: debut service={}",service);
		setErrorReport(new ArrayList<SimpleEntity>());
		setNouveauList(new ArrayList<TiersDas>());
		
		List<String> codeList=BudgetHelper.getKeys(getTiersImportedData());
		logger.debug("codeList: {}",codeList.size());
		setExistantList(service.loadTiers(codeList));
		
		List<SimpleEntity> tempList=calculateNotExist(getTiersImportedData(),getExistantList());		
		Tiers cpwinTiers=null;
		for(SimpleEntity s:tempList){
			cpwinTiers=searchCpwinTiers(s.getCode());
			if(cpwinTiers==null) 
			    getErrorReport().add(s);
			else getNouveauList().add(DasHelper.convert(cpwinTiers,TypeDasEnum.parse(s.getDesignation())));
		}
		logger.debug("rapprochement: Fin: Existant: {}, Nouveau: {}, code erron�: {}",getExistantList().size(),getNouveauList().size(),getErrorReport().size());
	}
	private Tiers searchCpwinTiers(String code) {
		Tiers t=tiersService.getTiersWoIban(code);
		logger.debug("searchCpwinTiers: {}",t);
		if((t!=null)&&((t.getCode()==null)||(t.getCode().trim().isEmpty())))
			t=null;
		return t;
	}
	private List<SimpleEntity> calculateNotExist(List<SimpleEntity> list, List<TiersDas> tiersList) {
		List<SimpleEntity> result=new ArrayList<>();
		TiersDas t=null;
		for(SimpleEntity s:list){
			t=new TiersDas(s.getCode());
			if(tiersList.contains(t)) continue;
			result.add(s);
		}
		return result;
	}

	private List<Honoraire> loadHonoraires() {
		logger.debug("loadHonoraires: exercice={}",getExercice());
		List<Honoraire> hList=service.loadHonoraires(getExercice());
		logger.debug("loadHonoraires: hList={}",hList.size());
		loadBeneficiaires(hList);
		return hList;
	}
	private List<Honoraire> ImportHonoraires() throws MissingConfiguration {
		List<Honoraire> hList=null;
		
		hList=importFile( 0, Honoraire.class);
					
		for(Honoraire h:hList){
			h.setExercice(getExercice());
			h.setTrace(Helper.createTrace());
		}
		loadBeneficiaires(hList);
		return hList;
	}
	private void loadBeneficiaires(List<Honoraire> hList){
		List<SimpleEntity> errorList=new ArrayList<>();
		List<String> codeList=DasHelper.getCodesTiers(hList);
		logger.debug("loadBeneficiaires: codeList: {}",codeList.size());
		List<TiersDas> tiersList=service.loadTiers(codeList);
		String errorMsg=null;
		int index=0;
		for(Honoraire h:hList){
			index=tiersList.indexOf(h.getBeneficiaire());
			if(index==-1){
				errorMsg=HandlerJSFMessage.formatMessage(HandlerJSFMessage.getErrorMessage(MsgEntry.TIERS_NOT_FOUND),new String[]{h.getBeneficiaire().getNumero()});
				errorList.add(new SimpleEntity(h.getBeneficiaire().getNumero(), errorMsg));
				continue;
			}
			h.setBeneficiaire(tiersList.get(index));
		}
		getErrorReport().addAll(errorList);
		logger.debug("loadBeneficiaires:end");
		
	}
	private List<SimpleEntity> importTiers() throws MissingConfiguration{
		List<SimpleEntity> result=new ArrayList<>();
		List<TiersImportedData> list=importFile(0, TiersImportedData.class);
		for(TiersImportedData t:list)result.add(t);
		return result;
	}


	public List<SimpleEntity> getTiersImportedData() {
		return tiersImportedData;
	}
	public void setTiersImportedData(List<SimpleEntity> tiersImportedData) {
		this.tiersImportedData = tiersImportedData;
	}
	
	public boolean isRequiredDataDone() {
		
		switch (getCurrentAction()) {
		case IMPORT_TIERS_DAS:
		case IMPORT_HONORAIRE:
			if (!isCommonRequiredDone())
				return false;
			if (getImportFileUploadEvent() == null)
				return false;
			return true;	
		case SUIVI_HONORAIRE:case CONSULT_TIERS_DAS:
			return true;
		default:
			if (!isCommonRequiredDone())
				return false;
			return true;		
		}
	}
	public boolean isCommonRequiredDone() {
		if(getExercice()==0)return false;		
		return true;
	}
	public List<TiersDas> getExistantList() {
		return existantList;
	}
	public void setExistantList(List<TiersDas> existantList) {
		this.existantList = existantList;
	}
	public List<TiersDas> getNouveauList() {
		return nouveauList;
	}
	public void setNouveauList(List<TiersDas> nouveauList) {
		this.nouveauList = nouveauList;
	}
	
	public void onExerciceChange() {
		reset();
	}
	
	public void resetDynamicList() {		
		setTiersImportedData(null);
		setErrorReport(null);
		setNouveauList(null);
		setExistantList(null);
		setHonoraires(null);
		setModeProd(false);
		setDasStruct(null);
	}

	public void resetEnteteList() {
		setImportFileUploadEvent(null);
	}
	
	public void openTiersDialog() {	
		DialogHelper.openTiersView();
	}
	
	public void closeTiersDialog() {
		DialogHelper.closeDialog(null);		
	}
	public TiersDas getSelectedTiers() {
		return selectedTiers;
	}
	public void setSelectedTiers(TiersDas selectedTiers) {
		this.selectedTiers = selectedTiers;
	}
	
	
	public void setService(DasService service) {
		this.service = service;
	}
	public void setTiersService(IGestionTiersService tiersService) {
		this.tiersService = tiersService;
	}
	public String getTiersImportedCount() {
		if(getTiersImportedData()==null)return "";
		return "("+getTiersImportedData().size()+")";
	}
	public String getNotFoundCount() {
		if(getErrorReport()==null)return "";
		return "("+getErrorReport().size()+")";
	}

	public String getNouveauCount() {
		if(getNouveauList()==null)return "";
		return "("+getNouveauList().size()+")";
	}

	public String getExistantCount() {
		if(getExistantList()==null)return "";
		return "("+getExistantList().size()+")";
	}
	public List<Honoraire> getHonoraires() {
		return honoraires;
	}
	public void setHonoraires(List<Honoraire> honoraires) {
		this.honoraires = honoraires;
	}

	private boolean isHonoraireImport() {
		Action action=BudgetHelper.getNavigationBean().getCurrentAction();		
		logger.debug("action:{}",action);
		return (action==Action.IMPORT_HONORAIRE);
	}
	private boolean isHonoraireGenerate() {
		Action action=BudgetHelper.getNavigationBean().getCurrentAction();
		logger.debug("action:{}",action);
		return (action==Action.GENERER_HONORAIRE);
	}

	private boolean isTiersImport() {
		Action action=BudgetHelper.getNavigationBean().getCurrentAction();
		logger.debug("action:{}",action);
		return (action==Action.IMPORT_TIERS_DAS);
	}
	
	private boolean isHonoraireSuivi() {
		Action action=BudgetHelper.getNavigationBean().getCurrentAction();	
		logger.debug("action:{}",action);
		return (action==Action.SUIVI_HONORAIRE);
	}
	public String getHonorairesSuivi()
	{
		if(getHonoraires()==null)return "";
		return "("+getHonoraires().size()+")";
	}
	
	public String getNotFoundHonoCount() {
		if(getErrorReport()==null)return "";
		return "("+getErrorReport().size()+")";		
		
	}
	public Honoraire getSelectedHonoraire() {
		return selectedHonoraire;
	}

	public void setSelectedHonoraire(Honoraire selectedHonoraire) {
		this.selectedHonoraire = selectedHonoraire;
	}
	
	public void closeHonoraireDialog()
	{
		DialogHelper.closeDialog(null);
	}
	
	public void openHonoraireDialog()
	{
		DialogHelper.openHonoraireView();
	}
	
	public boolean isModeProd() {
		return modeProd;
	}


	public void setModeProd(boolean modeProd) {
		this.modeProd = modeProd;
	}


	public Integer getExerciceTest() {
		return exerciceTest;
	}


	public void setExerciceTest(Integer exerciceTest) {
		this.exerciceTest = exerciceTest;
	}


	public String getCodeTiers() {
		return codeTiers;
	}


	public void setCodeTiers(String codeTiers) {
		this.codeTiers = codeTiers;
		resetDynamicList();
	}


	public String getNomTiers() {
		return nomTiers;
	}


	public void setNomTiers(String nomTiers) {
		this.nomTiers = nomTiers;
		resetDynamicList();
	}
	
	public Das2FileStruct getDasStruct() {
		return dasStruct;
	}

	public void setDasStruct(Das2FileStruct dasStruct) {
		this.dasStruct = dasStruct;
	}

	public StreamedContent getDasFile() {
		StreamedContent content = null;
		
		String fileName=getDasStruct().getGeneratedFileName();
		try {	
		String filePath=getDasGeneratePath()+fileName;	
				
			content =  Helper.getStreamedContentFile(new File(filePath));
			
			logger.info("T�l�chargement du fichier DAS2 effectu� avec succ�s");
		} catch (Exception e) {
			logger.error("Le fichier n'a pas �t� trouv�");
			e.printStackTrace();
			content = Helper.getStreamedContentFile(Helper.getFileNotFound());
		}
		return content;
	}
	
	private List<Honoraire> loadHonorairesSuivi() {
		String codeTiers=getTiersForSearch()!=null ?getTiersForSearch().getNumero():null;
		logger.debug("loadHonorairesForSuivi: exercice={}, code tiers={}",getExercice(),codeTiers);
		List<Honoraire> hList=service.loadHonoraires(getExercice(), codeTiers);
		logger.debug("loadHonorairesForSuivi: hList={}",hList.size());
		loadBeneficiaires(hList);
		return hList;
	}
	
	public BigDecimal getTotalHonoraires() {
		BigDecimal result = new BigDecimal(0);
		if (getHonoraires() == null)
			return null;
		for (Honoraire h : getHonoraires()) {
			result=result.add(h.getHonoraires());
		}
		return result;
	}

	public BigDecimal getTotalCommissions() {
		BigDecimal result = new BigDecimal(0);
		if (getHonoraires() == null)
			return null;
		for (Honoraire h : getHonoraires()) {
			result=result.add(h.getCommissions());
		}
		return result;
	}

	public BigDecimal getTotalCourtages() {
		BigDecimal result = new BigDecimal(0);
		if (getHonoraires() == null)
			return null;
		for (Honoraire h : getHonoraires()) {
			result=result.add(h.getCourtages());
		}
		return result;
	}

	public BigDecimal getTotalRistournes() {
		BigDecimal result = new BigDecimal(0);
		if (getHonoraires() == null)
			return null;
		for (Honoraire h : getHonoraires()) {
			result=result.add(h.getRistournes());
		}
		return result;
	}

	public BigDecimal getTotalJetonsPresence() {
		BigDecimal result = new BigDecimal(0);
		if (getHonoraires() == null)
			return null;
		for (Honoraire h : getHonoraires()) {
			result=result.add(h.getJetonsPresence());
		}
		return result;
	}

	public BigDecimal getTotalDroitsAuteur() {
		BigDecimal result = new BigDecimal(0);
		if (getHonoraires() == null)
			return null;
		for (Honoraire h : getHonoraires()) {
			result=result.add(h.getDroitsAuteur());
		}
		return result;
	}

	public BigDecimal getTotalDroitsInventeur() {
		BigDecimal result = new BigDecimal(0);
		if (getHonoraires() == null)
			return null;
		for (Honoraire h : getHonoraires()) {
			result=result.add(h.getDroitsInventeur());
		}
		return result;
	}

	public BigDecimal getTotalAutresRemunerations() {
		BigDecimal result = new BigDecimal(0);
		if (getHonoraires() == null)
			return null;
		for (Honoraire h : getHonoraires()) {
			result=result.add(h.getAutresRemunerations());
		}
		return result;
	}

	public BigDecimal getTotalIndemnitesRemb() {
		BigDecimal result = new BigDecimal(0);
		if (getHonoraires() == null)
			return null;
		for (Honoraire h : getHonoraires()) {
			result=result.add(h.getIndemnitesRemb());
		}
		return result;
	}

	public BigDecimal getTotalAvantagesEnNature() {
		BigDecimal result = new BigDecimal(0);
		if (getHonoraires() == null)
			return null;
		for (Honoraire h : getHonoraires()) {
			result=result.add(h.getAvantagesEnNature());
		}
		return result;
	}

	public BigDecimal getTotalRetenueAlaSource() {
		BigDecimal result = new BigDecimal(0);
		if (getHonoraires() == null)
			return null;
		for (Honoraire h : getHonoraires()) {
			result=result.add(h.getRetenueAlaSource());
		}
		return result;
	}

	
	public List<TiersDas> autoCompleteTiers(String prefix) {
		logger.debug("###### Auto Complete Tiers prefix="+ prefix);
		List<TiersDas> tiersList=null;
		
		try{
			 tiersList=searchLightTiers(prefix);
		}
		catch(Exception e){
			
		}
		
		if (logger.isDebugEnabled()) {
		
	logger.debug("###### Auto Complete Tiers prefix="+ prefix+ ", "+ tiersList.size()+ " �l�ments trouv�s - end");
		}
		return tiersList;
	}
	
	public  List<TiersDas> searchLightTiers(String prefix) {
		boolean searchAtBegin = !(Helper.isSpecialPrefix(prefix));
		String formatedPrefix = Helper.removeSpecialPrefix(prefix);
		List<TiersDas> retour = service.findLightTiersList(searchAtBegin, formatedPrefix, null, false);
		return retour;
	}

	public TiersDas getTiersForSearch() {
		return tiersForSearch;
	}

	public void setTiersForSearch(TiersDas tiersForSearch) {
	  	this.tiersForSearch = tiersForSearch;
	if(this.tiersForSearch!=null)  	logger.info("----public void setTiersForSearch:--	this.tiersForSearch="+	this.tiersForSearch.getDescription());
	    resetDynamicList();
	}
	  public void onTiersSelect(SelectEvent event) {
	   
	  }
	  public String getHonorairesCount() {
			if(getHonoraires()==null)return "";
			return "("+getHonoraires().size()+")";
		}

	public String getNumSiret() {
		return numSiret;
	}

	public void setNumSiret(String numSiret) {
		this.numSiret = numSiret;
		resetDynamicList();
	}
	private boolean isConsultTiers() {
		Action action=BudgetHelper.getNavigationBean().getCurrentAction();	
		logger.debug("action:{}",action);
		return (action==Action.CONSULT_TIERS_DAS);
	}

	public  List<TiersDas> loadTiers(String prefix, String numSiret) {
		boolean searchAtBegin = !(Helper.isSpecialPrefix(prefix));
		String formatedPrefix = Helper.removeSpecialPrefix(prefix);
		List<TiersDas> retour = service.findLightTiersList(searchAtBegin, formatedPrefix,numSiret, true);
		return retour;
	}
	
	public boolean isGenrateFileDisabled() {
		if (getDasStruct() != null)
			return true;
		if (isModeProd())
			return false;
		return ((getExerciceTest() == null) || (getExerciceTest() == 0));
	}

	public List<BTQCEnum> getBtqcList() {
		return btqcList;
	}

	public void setBtqcList(List<BTQCEnum> btqcList) {
		this.btqcList = btqcList;
	}
	
	  public StreamedContent getXlsSuiviHonoraires(){
			StreamedContent returnStreamedContent = null;
			logger.debug("getXlsSuiviHonoraires: taille de la liste des honoraires: {}",(getHonoraires()==null)?0:getHonoraires().size());
			ExcelHandler excel = new ExcelHandler(ExcelModelEnum.SUIVI_HONORAIRES,getHonoraires());
			returnStreamedContent = excel.getExcelFile();
			if (logger.isDebugEnabled()) {
				logger.debug("getXlsSuiviHonoraires() - end"); //$NON-NLS-1$
			}
			return returnStreamedContent;	

		}

	@Override
	protected String getSavedPath() throws MissingConfiguration {
		return Constant.getProjectRootPath().resolve("Das2").toString();
	}

	@Override
	protected String getImportFileName() {
		String fileName=null;
		if(isTiersImport())fileName=Constant.getSavedTiersFileName(getExercice());
		else if(isHonoraireImport())fileName=Constant.getSavedHonorairesFileName(getExercice());
		return fileName;
	}
	public Integer getExercice() {
		return getSearchBean().getCurentExercice();
	}
	public SearchBean getSearchBean() {
		return BudgetHelper.getSearchBean();
	}
	public Action getCurrentAction(){
		return BudgetHelper.getNavigationBean().getCurrentAction();
	}
	
	public String getErreursCount() {
		if (getErrorReport() == null)
			return "";
		return "(" + getErrorReport().size() + ")";
	}
	
	public boolean isErrorReportVisible() {
		if (getErrorReport() == null)
			return false;
		if (getErrorReport().isEmpty())
			return false;
		return true;
	}

	public boolean isImportDataHonoraireVsble() {
		if (isErrorReportVisible())
			return false;
		if (!isHonorairesVisible())
			return false;
		return true;
	}

	public boolean isSaveHonoraireAutorized() {
		return isImportDataHonoraireVsble();
	}

	public boolean isHonorairesVisible() {
		if (getHonoraires() == null)
			return false;
		if (getHonoraires().isEmpty())
			return false;
		return true;
	}
	
	public boolean isDataHonoraireVisible() {
		if (isErrorReportVisible())
			return true;
		if (isHonorairesVisible())
			return true;
		return false;
	}

	public boolean isDataTiersVisible() {
		if (isErrorReportVisible())
			return true;
		if (isExistantVisible() || isNouveauVisible() || isTiersImportedDataVisible())
			return true;
		return false;
	}

		
	public boolean isImportDataTiersVsble() {
		if (isErrorReportVisible())
			return false;
		if (!isExistantVisible() && !isNouveauVisible() && !isTiersImportedDataVisible())
			return false;
		return true;
	}

	
	public boolean isSaveTiersAutorized() {	
		return isImportDataTiersVsble();
	}
	public boolean isExistantVisible() {
		if (getExistantList() == null)
			return false;
		if (getExistantList().isEmpty())
			return false;
		return true;
	}

	public boolean isNouveauVisible() {
		if (getNouveauList() == null)
			return false;
		if (getNouveauList().isEmpty())
			return false;
		return true;
	}

	public boolean isTiersImportedDataVisible() {
		if (getTiersImportedData() == null)
			return false;
		if (getTiersImportedData().isEmpty())
			return false;
		return true;
	}	
	
	 public StreamedContent getXlsExistantList(){
			StreamedContent returnStreamedContent = null;
			logger.debug("getXlsExistantList--start");
			ExcelHandler excel = new ExcelHandler(ExcelModelEnum.DAS_TIERS_CONSULT,getExistantList());
			returnStreamedContent = excel.getExcelFile();
			if (logger.isDebugEnabled()) {
				logger.debug("getXlsExistantList() - end"); 
			}
			return returnStreamedContent;	

		}
	
}
