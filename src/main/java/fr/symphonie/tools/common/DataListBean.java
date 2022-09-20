package fr.symphonie.tools.common;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;

import org.apache.logging.log4j.util.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.symphonie.budget.core.model.DepRecEnum;
import fr.symphonie.budget.core.model.pluri.LigneBudgetaireAE;
import fr.symphonie.budget.core.service.IBudgetPluriannuelService;
import fr.symphonie.common.util.BudgetHelper;
import fr.symphonie.common.util.Constant;
import fr.symphonie.util.Helper;
import fr.symphonie.util.model.SimpleEntity;
@ManagedBean
@SessionScoped
public class DataListBean implements Serializable{
	private static final long serialVersionUID = -6520992360063213966L;

	/** 
	 * Logger for this class
	 */
	private static final Logger logger = LoggerFactory.getLogger(DataListBean.class);
	
	@ManagedProperty(value = "#{budgetPluriService}")
	private IBudgetPluriannuelService bpService;
	
	private List<LigneBudgetaireAE> recetteInterneLbList;
	private List<LigneBudgetaireAE> depenseInterneLbList;
	private String exec;
	private String codeDest;
	private String codeNature;
	private String codeService;
	private String codeProg;
	private String compteProduit;
	private String compteTva;
	private String compteClient;
	private String codeBudget;
	private String codeGest;
	private String compteCharge;
	private List<SimpleEntity> compteProduitList;
	private List<SimpleEntity> compteChargeList;
	//Mode recette
	private List<SimpleEntity> destinationList;
	private List<SimpleEntity> natureList;
	private List<SimpleEntity> serviceList;
	private List<SimpleEntity> programmeList;	
	//Mode depense
	private List<SimpleEntity> dpDestinationList;
	private List<SimpleEntity> dpNatureList;
	private List<SimpleEntity> dpServiceList;
	private List<SimpleEntity> dpProgrammeList;
	private List<String> codeGestList;
	private LigneBudgetaireAE selectedLb;
	
	
	
	private List<String> listBudgets=null;

	public void reset(){
		resetRecette();
		resetDp();
		setCompteProduitList(null);
		setCodeDest(null);
		setCodeNature(null);
		setCodeService(null);
		setCodeProg(null);
		setCompteProduit(null);
		setCodeGest(null);
		setCompteClient(null);
		setCompteTva(null);
		BudgetHelper.getReferentielBean().setListNumPeriode(null);
		setSelectedLb(null);
		
		
	}
	private void resetRecette() {
		setRecetteLbList(null);
		setDestinationList(null);
		setNatureList(null);
		setServiceList(null);
		setProgrammeList(null);
		setCodeGestList(null);
	}
	private void resetDp() {
		setDepenseLbList(null);
		setDpDestinationList(null);
		setDpNatureList(null);
		setDpServiceList(null);
		setDpProgrammeList(null);
		setCompteChargeList(null);
		setCompteCharge(null);
		setCodeGestList(null);
	}
	public List<SimpleEntity> getDestinationList() {
//		logger.info("	1-public List<SimpleEntity> getDestinationList()");
		Set<SimpleEntity> list=new HashSet<>();
		
		if(destinationList==null)
		{
			
			for(LigneBudgetaireAE lb:getRecetteLbList()){
				list.add(new SimpleEntity(lb.getDestination(), ""));
			}
			setDestinationList(new ArrayList<>(list));
		}
		
		return destinationList;
	}
	public void setDestinationList(List<SimpleEntity> destinationList) {				
		this.destinationList = destinationList;
	}
	public List<SimpleEntity> getNatureList() {
		Set<SimpleEntity> list=new HashSet<>();
		if(natureList==null)
		{
			if(!Strings.isBlank(getCodeDest()))
			for(LigneBudgetaireAE lb:getRecetteLbList()){
				if(!getCodeDest().equalsIgnoreCase(lb.getDestination()))continue;
				list.add(new SimpleEntity(lb.getNature(), ""));
			}
			setNatureList(new ArrayList<>(list));
		}
		return natureList;
	}
	public void setNatureList(List<SimpleEntity> natureList) {
		this.natureList = natureList;
	}
	public List<SimpleEntity> getServiceList() {
		Set<SimpleEntity> list=new HashSet<>();
		if(serviceList==null)
		{
			if((!Strings.isBlank(getCodeDest()))&&(!Strings.isBlank(getCodeNature())))
			for(LigneBudgetaireAE lb:getRecetteLbList()){
				if(!getCodeDest().equalsIgnoreCase(lb.getDestination()))continue;
				if(!getCodeNature().equalsIgnoreCase(lb.getNature()))continue;
				list.add(new SimpleEntity(lb.getService(), ""));
			}
			setServiceList(new ArrayList<>(list));
		}
		return serviceList;
	}
	public void setServiceList(List<SimpleEntity> serviceList) {
		this.serviceList = serviceList;
	}
	public List<SimpleEntity> getProgrammeList() {
		Set<SimpleEntity> list=new HashSet<>();
		if(programmeList==null)
		{
			if((!Strings.isBlank(getCodeDest()))&&(!Strings.isBlank(getCodeNature()))&&(!Strings.isBlank(getCodeService())))
			for(LigneBudgetaireAE lb:getRecetteLbList()){
				if(!getCodeDest().equalsIgnoreCase(lb.getDestination()))continue;
				if(!getCodeNature().equalsIgnoreCase(lb.getNature()))continue;
				if(!getCodeService().equalsIgnoreCase(lb.getService()))continue;
				list.add(new SimpleEntity(lb.getProgramme(), ""));
			}
			setProgrammeList(new ArrayList<>(list));
		}
		return programmeList;
	}
	public void setProgrammeList(List<SimpleEntity> programmeList) {
		this.programmeList = programmeList;
	}
	public List<SimpleEntity> getCompteProduitList() {
		if(compteProduitList==null){
			if((!Strings.isBlank(getCodeDest()))&&(!Strings.isBlank(getCodeNature()))
					//&&(!Strings.isBlank(getCodeProg()))
					){
				logger.debug("getCompteProduitList:{}, {}, {}",getExercice(),getCodeDest(),getCodeNature());
				setCompteProduitList(bpService.getCompteProduitList(getExercice(),getCodeDest(),getCodeNature(),null));
			}
		}
		return compteProduitList;
	}
	public void setCompteProduitList(List<SimpleEntity> compteProduitList) {
		this.compteProduitList = compteProduitList;
	}
	private List<LigneBudgetaireAE> getRecetteLbList() {
		
		if(recetteInterneLbList==null){
			recetteInterneLbList=bpService.getInterneLbList(getExercice(), DepRecEnum.Recette,getCodeBudget());
		}
		return recetteInterneLbList;
	}
	public void setRecetteLbList(List<LigneBudgetaireAE> interneLbList) {
		this.recetteInterneLbList = interneLbList;
	}
private List<LigneBudgetaireAE> getDepenseLbList() {
		
		if(depenseInterneLbList==null){
			depenseInterneLbList=bpService.getInterneLbList(getExercice(), DepRecEnum.Depense,getCodeBudget());
		}
		return depenseInterneLbList;
	}
	public void setDepenseLbList(List<LigneBudgetaireAE> interneLbList) {
		this.depenseInterneLbList = interneLbList;
	}
	
	public Integer getExercice(){
		Integer exercice=null;
		if(!Strings.isBlank(getExec()))
		exercice=Helper.stringToInt(getExec());
		return exercice;
	}
	public String getCodeDest() {
		return codeDest;
	}
	public void setCodeDest(String codeDest) {
		if((codeDest!=null)&&(codeDest.equalsIgnoreCase(this.codeDest))) return;
		this.codeDest = codeDest;
		setNatureList(null);
		setServiceList(null);
		setProgrammeList(null);
		setCompteProduitList(null);
		setDpNatureList(null);
		setDpServiceList(null);
		setDpProgrammeList(null);
		setCompteChargeList(null);
	}
	public String getCodeNature() {
		return codeNature;
	}
	public void setCodeNature(String codeNature) {
		if((codeNature!=null)&&(codeNature.equalsIgnoreCase(this.codeNature))) return;
		this.codeNature = codeNature;
		setServiceList(null);
		setProgrammeList(null);
		setCompteProduitList(null);
		setCodeGestList(null);
		setDpServiceList(null);
		setDpProgrammeList(null);
		setCompteChargeList(null);
	}
	public String getCodeService() {
		return codeService;
	}
	public void setCodeService(String codeService) {
		if((codeService!=null)&&(codeService.equalsIgnoreCase(this.codeService))) return;
		this.codeService = codeService;
		setProgrammeList(null);
		
		setDpProgrammeList(null);
	}
	public String getCodeProg() {
		return codeProg;
	}
	public void setCodeProg(String codeProg) {
		if((codeProg!=null)&&(codeProg.equalsIgnoreCase(this.codeProg))) return;
		this.codeProg = codeProg;
		setCompteProduitList(null);
		setCompteChargeList(null);
		setCodeGestList(null);	
	}
	public String getCompteProduit() {
		return compteProduit;
	}
	public void setCompteProduit(String compteProduit) {
		this.compteProduit = compteProduit;
	}
	public void setBpService(IBudgetPluriannuelService bpService) {
		this.bpService = bpService;
	}
	public String getExec() {
		return exec;
	}
	public void setExec(String exec) {
		logger.debug("setExec: {}",exec);
		if((exec!=null)&&(exec.equalsIgnoreCase(this.exec))) return;
		this.exec = exec;
		reset();	
	}
	
//	public void setGtsService(GtsService gtsService) {
//		this.gtsService = gtsService;
//	}
	
	public Object getNullValue(){
		return null;
	}
	
	public LigneBudgetaireAE finLB(String codeDest,String codeNature,String codeService,String codeProg) {
//		logger.debug("finLB: {},{},{},{},",codeDest,codeNature,codeService,codeProg);
		
			if((!Strings.isBlank(codeDest))&&(!Strings.isBlank(codeNature))&&(!Strings.isBlank(codeService))&&(!Strings.isBlank(codeProg)))
			for(LigneBudgetaireAE lb:getRecetteLbList()){
//				logger.debug("finLB: input lb={}",lb);
				if(!codeDest.equalsIgnoreCase(lb.getDestination()))continue;
				if(!codeNature.equalsIgnoreCase(lb.getNature()))continue;
				if(!codeService.equalsIgnoreCase(lb.getService()))continue;
				if(!codeProg.equalsIgnoreCase(lb.getProgramme()))continue;
//				logger.debug("finLB: found lb={}",lb);
				return lb;
			}
		
		return null;
	}
	

	public void setCodeBudget(String codeBudget) {
		this.codeBudget = codeBudget;
	}

	public String getCodeBudget() {
		return codeBudget;
	}
	public void setListBudgets(List<String> listBudgets) {
		this.listBudgets = listBudgets;
	}
	public List<String> getListBudgets() {	
		if(listBudgets==null)
		{
			if(getExercice()!=null)
				setListBudgets(bpService.getListBudgets(getExercice()));
		}
			
		return listBudgets;
	}
	public List<SimpleEntity> getDpDestinationList() {
		Set<SimpleEntity> list=new HashSet<>();
		
		if(dpDestinationList==null)
		{
			
			for(LigneBudgetaireAE lb:getDepenseLbList()){
				list.add(new SimpleEntity(lb.getDestination(), ""));
			}
			setDpDestinationList(new ArrayList<>(list));
			Collections.sort(dpDestinationList);
		}
		
		return dpDestinationList;
	}
	public void setDpDestinationList(List<SimpleEntity> dpDestinationList) {
		this.dpDestinationList = dpDestinationList;
	}
	public List<SimpleEntity> getDpNatureList() {
		Set<SimpleEntity> list=new HashSet<>();
		if(dpNatureList==null)
		{
			if(!Strings.isBlank(getCodeDest()))
			for(LigneBudgetaireAE lb:getDepenseLbList()){
				if(!getCodeDest().equalsIgnoreCase(lb.getDestination()))continue;
				list.add(new SimpleEntity(lb.getNature(), ""));
			}
			setDpNatureList(new ArrayList<>(list));
		}
		return dpNatureList;
	}
	public void setDpNatureList(List<SimpleEntity> dpNatureList) {
		this.dpNatureList = dpNatureList;
	}
	public List<SimpleEntity> getDpServiceList() {
		Set<SimpleEntity> list=new HashSet<>();
		if(dpServiceList==null)
		{
			if((!Strings.isBlank(getCodeDest()))&&(!Strings.isBlank(getCodeNature())))
			for(LigneBudgetaireAE lb:getDepenseLbList()){
				if(!getCodeDest().equalsIgnoreCase(lb.getDestination()))continue;
				if(!getCodeNature().equalsIgnoreCase(lb.getNature()))continue;
				if(lb.getService().equals(Constant.RESERVE))continue;
				list.add(new SimpleEntity(lb.getService(), ""));
			}
			setDpServiceList(new ArrayList<>(list));
		}
		return dpServiceList;
	}
	public void setDpServiceList(List<SimpleEntity> dpServiceList) {
		this.dpServiceList = dpServiceList;
	}
	public List<SimpleEntity> getDpProgrammeList() {
		Set<SimpleEntity> list=new HashSet<>();
		if(dpProgrammeList==null)
		{
			if((!Strings.isBlank(getCodeDest()))&&(!Strings.isBlank(getCodeNature()))&&(!Strings.isBlank(getCodeService())))
			for(LigneBudgetaireAE lb:getDepenseLbList()){
				if(!getCodeDest().equalsIgnoreCase(lb.getDestination()))continue;
				if(!getCodeNature().equalsIgnoreCase(lb.getNature()))continue;
				if(!getCodeService().equalsIgnoreCase(lb.getService()))continue;
				list.add(new SimpleEntity(lb.getProgramme(), ""));
			}
			setDpProgrammeList(new ArrayList<>(list));
		}
		return dpProgrammeList;
	}
	public void setDpProgrammeList(List<SimpleEntity> dpProgrammeList) {
		this.dpProgrammeList = dpProgrammeList;
	}
	
	public LigneBudgetaireAE findDpLbi() {
		
		if((!Strings.isBlank(codeDest))&&(!Strings.isBlank(codeNature))&&(!Strings.isBlank(codeService))&&(!Strings.isBlank(codeProg)))
			for(LigneBudgetaireAE lb:getDepenseLbList()){
				if(!codeDest.equalsIgnoreCase(lb.getDestination()))continue;
				if(!codeNature.equalsIgnoreCase(lb.getNature()))continue;
				if(!codeService.equalsIgnoreCase(lb.getService()))continue;
				if(!codeProg.equalsIgnoreCase(lb.getProgramme()))continue;
				return lb;
			}
		
		return null;
		
	}
	public List<String> getCodeGestList() {
		if(codeGestList==null) {
			if(getSelectedLb()!=null) {
				List<String> list=bpService.getCodeGestList(getExercice(),getSelectedLb().getGroupNat());
				setCodeGestList(list);
			}
				
		}
		return codeGestList;
	}
	public void setCodeGestList(List<String> codeGestList) {
		this.codeGestList = codeGestList;
	}
	public LigneBudgetaireAE getSelectedLb() {
		return selectedLb;
	}
	public void setSelectedLb(LigneBudgetaireAE selectedLb) {
		this.selectedLb = selectedLb;
	}
	public String getCodeGest() {
		return codeGest;
	}
	public void setCodeGest(String codeGest) {
		this.codeGest = codeGest;
	}
	public String getCompteTva() {
		return compteTva;
	}
	public void setCompteTva(String compteTva) {
		this.compteTva = compteTva;
	}
	public String getCompteClient() {
		return compteClient;
	}
	public void setCompteClient(String compteClient) {
		this.compteClient = compteClient;
	}
	public void initializCtaCompteImput() {
		String[] comptes=bpService.getCtaCompteImput(getExercice(),getCompteCharge());
		setCompteClient(comptes[0]);
		setCompteTva(comptes[1]);
	}
	public List<SimpleEntity> getCompteChargeList() {
		if(compteChargeList==null){
			if(getSelectedLb()!=null)
			{
				logger.debug("getCompteChargeList:{}, {}, {}",getExercice(),getSelectedLb().getNoEnv());
				setCompteChargeList(bpService.getCompteChargeList(getExercice(),getSelectedLb().getNoEnv()));
			}
		}
		return compteChargeList;
	}
	public void setCompteChargeList(List<SimpleEntity> compteChargeList) {
		this.compteChargeList = compteChargeList;
	}
	public String getCompteCharge() {
		return compteCharge;
	}
	public void setCompteCharge(String compteCharge) {
		this.compteCharge = compteCharge;
	}
}