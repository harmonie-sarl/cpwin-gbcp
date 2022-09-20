package fr.symphonie.budget.ui.beans;

import java.io.Serializable;
import java.util.Date;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.event.ValueChangeEvent;

import org.apache.logging.log4j.util.Strings;
import org.primefaces.event.SelectEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.symphonie.budget.ui.beans.NavigationBean.Action;
import fr.symphonie.budget.ui.beans.pluri.BudgetPluriannuelBean;
import fr.symphonie.common.util.BudgetHelper;
import fr.symphonie.cpwin.model.Tiers;
import fr.symphonie.util.Helper;
import fr.symphonie.util.model.SimpleEntity;

@ManagedBean 
@SessionScoped
public class SearchBean implements Serializable {

	/**
	 * Logger for this class 
	 */
	private static final Logger logger = LoggerFactory.getLogger(SearchBean.class);

	/**
	 * 
	 */
	private static final long serialVersionUID = -3720917872817589542L;
	private String exercice;
	private String exerciceCp;
	private String niveauDest;
	private Date dateArret;
	private String exerciceBR;
	private String codeDirection;
	private String codeService;
	private String codeFongDest;
	private String codeDest;
	private String codeProg;
	private String codeNatureGrp;
	private String codeNature;
//	private int tiersOrigine;
	private String libDirection;
	private String libService;
	private String libDestination;
	private String libProgramme;
	private String libNature;
	private String natGrp=null;
	private String noLtr=null;
//	private String codeTiers=null;
	
	private SimpleEntity fongDestination;
	private Tiers tiers;

private String codeBudget;
 
   public  void reset(){
	    resetSearchElements();
//	    resetDynamicList();	   
//		getBudgetPluriannuelBean().reset();
   }
	public void setExercice(String exercice) {
//		reset();		
//		getBudgetPluriannuelBean().reset();
		setCodeBudget(null);
//		resetDynamicList();
		this.exercice = exercice;	
		logger.info("setExercice: "+exercice);
		  
	}

	public String getExercice() {
	//	logger.info("getExercice: "+exercice);
		return exercice;
	}

	public void setCodeBudget(String codeBudget) {
		getLoaderBean().resetDynamicList();	
		getBudgetPluriannuelBean().reset();		
//		BudgetHelper.getEditionBean().resetDynamicList();
		BudgetHelper.getEditionBean().reset();
		this.codeBudget = codeBudget;
		positionneFongDest();


	}

	public String getCodeBudget() {
	//	logger.info("getCodeBudget: "+codeBudget);
		return codeBudget;
	}
	
	private LoaderBean getLoaderBean()
	{
		return((LoaderBean)Helper.findBean("loaderBean"));
	}
	
	private BudgetPluriannuelBean getBudgetPluriannuelBean()
	{
		return((BudgetPluriannuelBean)Helper.findBean("bpBean"));
	}
	
//	public void resetDynamicList()
//	{
//		getLoaderBean().resetDynamicList();
//		getBudgetPluriannuelBean().resetDynamicList();
//	}

	public void setNiveauDest(String niveauDest) {
		this.niveauDest = niveauDest;
	}

	public String getNiveauDest() {
		return niveauDest;
	}

	public void setDateArret(Date dateArret) {
		this.dateArret = dateArret;
	}

	public Date getDateArret() {
		return dateArret;
	}

	public void setExerciceCp(String exerciceCp) {
		setCodeBudget(null);
		this.exerciceCp = exerciceCp;
	}

	public String getExerciceCp() {
		return exerciceCp;
	}
	public void setExerciceBR(String exerciceBR) {
		setCodeBudget(null);
		this.exerciceBR = exerciceBR;
		//logger.info("setExerciceBR exerciceBR: "+exerciceBR);
	}
	public String getExerciceBR() {
		
	//	logger.info("getExerciceBR exerciceBR: "+exerciceBR);
		return exerciceBR;
	}
	
	public int getCurentExercice(){
		int result=0;
		int exercice=Helper.stringToInt(getExercice());
		int exerciceCp=Helper.stringToInt(getExerciceCp());
		int exerciceBr=Helper.stringToInt(getExerciceBR());
		NavigationBean bean=BudgetHelper.getNavigationBean();
		Action action=bean.getCurrentAction();
		switch (action) {
		case VENTILLATION_CP_GEST:case ADRESSAGE_BUDGET_PLURI_CPDEST:case MODIF_DEPOIEMENT:case CONSULT_DEPOIEMENT:case VENTILATION_RECETTE_ORIGINE:case VENTIL_DEST_BR:case VENTIL_GEST_BR:
			result=exerciceCp;
			break;
		case SAISI_CP_BR:case EDITIONS_BR:case CHARGEMENT_EDIT_BR:
		result=exerciceBr;
		break;
		case GESTION_EDITIONS: case INTERFACE_MINEFI:case SIMUL_TRESORERIE: case AJUST_TRESORERIE:case VENTIL_TRESORERIE:case CONCIL_TRESORERIE:
			result=exercice;
			break;
		default:
			result=exercice;
			break;
		}
		
		return result;
	}
	public void onDateArretSelect(SelectEvent event){
		if (event==null)
			return;
     Date dateArret=(Date)event.getObject();
     setDateArret(dateArret);
		
	}
	
	
	public String getCodeDirection() {
		return codeDirection;
	}
	public void setCodeDirection(String codeDirection) {
		
		
		this.codeDirection=BudgetHelper.eliminateSeparator(codeDirection, "-");
		setLibDirection(BudgetHelper.getlib(this.codeDirection, getLoaderBean().getListDirectionSuiviCp()));
		getBudgetPluriannuelBean().resetDynamicList();

	}
	
	public String getCodeService() {
		return codeService;
	}
	
	
	
	public void setCodeService(String codeService) {

		this.codeService=BudgetHelper.eliminateSeparator(codeService, "-");
		setLibService(BudgetHelper.getlib(this.codeService, getLoaderBean().getListServiceSuiviCp()));
		getBudgetPluriannuelBean().resetDynamicList();
//		resetDynamicList();
	}
	public String getCodeFongDest() {	
		return codeFongDest;
	}
	public void setCodeFongDest(String codeFongDest) {
		this.codeFongDest = codeFongDest;
		getBudgetPluriannuelBean().resetDynamicList();

	}
	public String getCodeDest() {
		return codeDest;
	}
	public void setCodeDest(String codeDest) {		
		this.codeDest =BudgetHelper.eliminateSeparator(codeDest, "-");
		setLibDestination(BudgetHelper.getlib(this.codeDest, getLoaderBean().getListDestinationSuiviCp()));
//		resetDynamicList();
		getBudgetPluriannuelBean().resetDynamicList();
		getLoaderBean().setCompteProduitList(null);
	}
	public String getCodeProg() {
		return codeProg;
	}
	public void setCodeProg(String codeProg) {
		this.codeProg =BudgetHelper.eliminateSeparator(codeProg, "-");
		setLibProgramme(BudgetHelper.getlib(this.codeProg, getLoaderBean().getListProgrammeSuiviCp()));
//		resetDynamicList();
		getBudgetPluriannuelBean().resetDynamicList();
	}
	public String getCodeNature() {
		return codeNature;
	}
	public void setCodeNature(String codeNature) {
		this.codeNature = BudgetHelper.eliminateSeparator(codeNature, "-");;
		setLibNature(BudgetHelper.getlib(this.codeNature, getLoaderBean().getListNatureSuiviCp()));
		getBudgetPluriannuelBean().resetDynamicList();
		getLoaderBean().setCompteProduitList(null);
	}
	public String getCodeNatureGrp() {
		return codeNatureGrp;
	}
	public void setCodeNatureGrp(String codeNatureGrp) {
		this.codeNatureGrp = codeNatureGrp;
		getBudgetPluriannuelBean().resetDynamicList();
		logger.info("setCodeNatureGrp: "+codeNatureGrp);
	}
	
	public void codeBudgetChangeListener(ValueChangeEvent e) {
//		setCodeDirection(null);
//		setCodeService(null);
//		setCodeDest(null);
//		setCodeProg(null);
//		setCodeNatureGrp(null);
//		setCodeNature(null);
//		setNoLtr(null);
		resetBudgetSearchElements();
	}
	
	public void codeProgChangeListener(ValueChangeEvent e) {
		setCodeNature(null);
		getLoaderBean().setListNatureSuiviCp(null);
	}

	public void codeDestChangeListener(ValueChangeEvent e) {		
		setCodeProg(null);
		setCodeNatureGrp(null);
		setCodeNature(null);		
		getLoaderBean().setListProgrammeSuiviCp(null);
		getLoaderBean().setListNatureGroupeSuiviCp(null);
		getLoaderBean().setListNatureSuiviCp(null);
	}

	public void codeFongDestChangeListener(ValueChangeEvent e) {		
		setCodeDest(null);
		setCodeProg(null);
		setCodeNatureGrp(null);
		setCodeNature(null);		
		getLoaderBean().setListDestinationSuiviCp(null);
		getLoaderBean().setListProgrammeSuiviCp(null);
		getLoaderBean().setListNatureGroupeSuiviCp(null);
		getLoaderBean().setListNatureSuiviCp(null);
	}

	public void codeServiceChangeListener(ValueChangeEvent e) {		
		setCodeDest(null);
		setCodeProg(null);	
		setCodeNature(null);		
		getLoaderBean().setListDestinationSuiviCp(null);
		getLoaderBean().setListProgrammeSuiviCp(null);
		getLoaderBean().setListNatureSuiviCp(null);
	}

	public void codeDirectionChangeListener(ValueChangeEvent e) {	
		setCodeService(null);	
		setCodeDest(null);
		setCodeProg(null);		
		setCodeNature(null);		
		getLoaderBean().setListServiceSuiviCp(null);
		getLoaderBean().setListDestinationSuiviCp(null);
		getLoaderBean().setListProgrammeSuiviCp(null);
		getLoaderBean().setListNatureSuiviCp(null);
		
	}
	
//	private String getlib(String codeDirection2, List<SimpleEntity> listDirectionSuiviCp) {
//		String result=null;
//		if(codeDirection2==null)return null;
//				if(listDirectionSuiviCp==null)return null;
//	for (SimpleEntity simpleEntity : listDirectionSuiviCp) {
//		if(!simpleEntity.getCode().trim().equalsIgnoreCase(codeDirection2)) continue;
//		result=simpleEntity.getDesignation();
//		return result;
//	}
//	return null;
//	}
	

     
	public void resetBudgetSearchElements() {
		setCodeDirection(null);
		setCodeService(null);
		setCodeDest(null);
		setCodeProg(null);
		setCodeNatureGrp(null);
		setCodeNature(null);
		setNoLtr(null);
//		setCodeTiers(null);
		setTiers(null);
	}
	
	public void execrciceChangeListener()
	{
//		setCodeDirection(null);
//		setCodeService(null);
		setCodeFongDest(null);
//		setCodeDest(null);
//		setCodeProg(null);
		setCodeNatureGrp(null);
//		setCodeNature(null);
		setCodeBudget(null);	
		resetBudgetSearchElements();
	}
	
	public void resetSearchElements() {
//		setCodeDirection(null);
//		setCodeService(null);
		setCodeFongDest(null);
//		setCodeDest(null);
//		setCodeProg(null);
		setCodeNatureGrp(null);
//		setCodeNature(null);
		setCodeBudget(null);
		setExercice(null);
		setExerciceCp(null);
		setExerciceBR(null);
		setNatGrp(null);
		resetBudgetSearchElements();
//		setNoLtr(null);
	}
//	public int getTiersOrigine() {
//		return tiersOrigine;
//	}
//	public void setTiersOrigine(int tiersOrigine) {
//		this.tiersOrigine = tiersOrigine;
//	}
	
//	public void trsOrgnChangeListener(ValueChangeEvent e) {	
//
//	}
	public String getLibDirection() {
		return libDirection;
	}
	public void setLibDirection(String libDirection) {
		this.libDirection = libDirection;
	}
	
	public String getLibService() {
		return libService;
	}
	public void setLibService(String libService) {
		this.libService = libService;
	}
	
	public String getLibDestination() {
		return libDestination;
	}
	public void setLibDestination(String libDestination) {
		this.libDestination = libDestination;
	}
	public String getLibProgramme() {
		return libProgramme;
	}
	public void setLibProgramme(String LibProgramme) {
		this.libProgramme = LibProgramme;
	}
	public String getLibNature() {
		return libNature;
	}
	public void setLibNature(String libNature) {
		this.libNature = libNature;
	
	}	
	
	public void codeNatureGrpChangeListnener(ValueChangeEvent event) {
//		setCodeNature(null);
		getLoaderBean().setListNatureSuiviCp(null);
	}
	public void codeNatureChangeListnener(ValueChangeEvent event) {
//		setCodeNatureGrp(null);
		getLoaderBean().setListNatureGroupeSuiviCp(null);
	}
	public String getNatGrp() {
		return natGrp;
	}
	public void setNatGrp(String natGrp) {
		this.natGrp = natGrp;
	}

		
		

	
	public void noLtrListener(ValueChangeEvent e) {
//		setCodeNature(null);
//		getLoaderBean().setListNatureSuiviCp(null);
	}
	public Integer getNoLtrInt()
	{
//		logger.info("	public Integer getNoLtrInt()=--"+getNoLtr()+"--");
//		logger.info("	public Integer Strings.isBlank(getNoLtr())=--"+Strings.isBlank(getNoLtr())+"--");
//		logger.info("	public Integer Helper.stringToInt(getNoLtr()=--"+Helper.stringToInt(getNoLtr())+"--");
//		
		if(Strings.isBlank(getNoLtr()))return null;
		return Helper.stringToInt(getNoLtr());
	}
	public String getNoLtr() {
		return noLtr;
	}
	public void setNoLtr(String noLtr) {
		this.noLtr = noLtr;
//		logger.info("public void setNoLtr(String noLtr) =--"+noLtr+"--");
		
		getBudgetPluriannuelBean().resetDynamicList();
	}
	
	public void positionneFongDest() {
		NavigationBean bean = BudgetHelper.getNavigationBean();
		Action action = bean.getCurrentAction();
		setCodeFongDest(null);
		setFongDestination(null);
		if((Strings.isBlank(getExercice()))||Strings.isBlank(getCodeBudget()))return;
		if(action!=null)
		switch (action) {
		case SUIVI_CP:
			setCodeFongDest("D");
			break;
		case SUIVI_REC:
			setCodeFongDest("R");
			break;
		default:
			break;
		}
		if (!Strings.isBlank(getCodeFongDest()))
			setFongDestination(new SimpleEntity(getCodeFongDest(),
					BudgetHelper.getlib(getCodeFongDest(), getLoaderBean().getListFongDestSuiviCp())));

	}
	public SimpleEntity getFongDestination() {
//		getLoaderBean().getListFongDestSuiviCp();		
		return fongDestination;
	}
	public void setFongDestination(SimpleEntity fongDestination) {
		this.fongDestination = fongDestination;
	}
//	public String getCodeTiers() {
//		return codeTiers;
//	}
//	public void setCodeTiers(String codeTiers) {
//		this.codeTiers = codeTiers;
//		getBudgetPluriannuelBean().resetDynamicList();
//	}
	public Tiers getTiers() {
		return tiers;
	}
	public void setTiers(Tiers tiers) {
		this.tiers = tiers;
		getBudgetPluriannuelBean().resetDynamicList();
	}
}