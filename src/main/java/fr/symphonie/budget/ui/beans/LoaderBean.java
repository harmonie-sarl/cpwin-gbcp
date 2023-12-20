package fr.symphonie.budget.ui.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;

import org.apache.logging.log4j.util.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.symphonie.budget.core.model.pluri.ComplexEntity;
import fr.symphonie.budget.core.model.pluri.CreditPaiement;
import fr.symphonie.budget.core.model.pluri.EnvelopBudg;
import fr.symphonie.budget.core.service.IBudgetPluriannuelService;
import fr.symphonie.budget.ui.beans.pluri.BudgetPluriannuelBean;
import fr.symphonie.util.Helper;
import fr.symphonie.util.model.SimpleEntity;


@ManagedBean(name="loaderBean")
@SessionScoped
public class LoaderBean implements Serializable{
	/**
	 * Logger for this class 
	 */
	private static final Logger logger = LoggerFactory.getLogger(LoaderBean.class);

	/**
	 * 
	 */
	private static final long serialVersionUID = 8548912856653342339L;


	@ManagedProperty(value = "#{budgetPluriService}")
	private IBudgetPluriannuelService budgetPluriService;

	private List<String> exercicAeList=null;
	private List<String> exercicCpList=null;
	private List<SimpleEntity> listPrograme=null;
	private List<SimpleEntity> listService=null;
	private List<String> listBudgets=null;
	private List<String> allExecList=null;
	private List<String> withClosedExercises;
	private List<ComplexEntity>groupDestList=null; 
	private List<String> listBudgetsCp=null;
	private List<ComplexEntity>listEnvBdgVentilParOrigin=null;
	private List<String> exerciceSuiviCpList=null;
	private List<String> listBudgetsSuiviCp=null;
	private List<SimpleEntity> listDirectionSuiviCp=null;
	private List<SimpleEntity> listServiceSuiviCp=null;
	private List<SimpleEntity> listFongDestSuiviCp=null;
	private List<SimpleEntity> listDestinationSuiviCp=null;
	private List<SimpleEntity> listProgrammeSuiviCp=null;
	private List<SimpleEntity> listNatureGroupeSuiviCp=null;
	private List<SimpleEntity> listNatureSuiviCp=null;
	private List<SimpleEntity> listNatureGroupe=null;
	private List<Integer> depenseNivDestList=new ArrayList<Integer>(Arrays.asList(1,2,3,4));
	private List<SimpleEntity> listGetionnaires=null;
	private List<String> listNoLtr=new ArrayList<String>(Arrays.asList("3","4","5","6","7","8","9","10"));
	private List<SimpleEntity> compteProduitList;
	private List<String> exercicInfoCentreList=null;
	final static int ALL = 0,ALL_AEXERCICE=1, ALL_PROGRAMME=2 , ALL_ENVELOPPE=3,ALL_SERVICE=4, ALL_EXERCICE_AE=5, ALL_BUDGET=6, GROUP_DEST=7, ALL_EXERCICE_CP=8, BUDG_CP=9,ENV_BUDG_VENT_ORIGIN=10,
			EXEC_SUIVI_CP=11, BUDG_SUIVI_CP=12, DIRECTION_SUIVI_CP=13,SERVICE_SUIVI_CP=14,FONG_DEST_SUIVI_CO=15,DESTINATION_SUIVI_CP=16,PROGRAMME_SUIVI_CP=17, NATURE_GRP_SUIVI_CP=18,NATURE_SUIVI_CP=19,NAt_GRP=20,GEST_LIST=21,EXERCICE_INFOCENTRE_2024=22;


	public void setBudgetPluriService(IBudgetPluriannuelService budgetPluriService) {
		this.budgetPluriService = budgetPluriService;
	}

	
	public List<Integer> getNotUsedExercices(){
		if (logger.isDebugEnabled()) {
			logger.debug("getNotUsedExercices() - start"); //$NON-NLS-1$
		}

		EnvelopBudg envelop=((BudgetPluriannuelBean)Helper.findBean("bpBean")).getEnvelopBudg();

        	return getNotUsedExerciceList(envelop);
	}

	public List<Integer>  getNotUsedExerciceList(EnvelopBudg envelop){
		
		List<Integer> result = new ArrayList<Integer>();
		List<CreditPaiement> creditPayList = envelop.getCpList();
		List<Integer> usedExercices = getUsedExercices(creditPayList);
		for (Integer exercice : getAllExercicList()) {
			if(usedExercices.contains(exercice))continue;
			result.add(exercice);
			
		}
		
		return result;
	}

		
    public List<Integer> getAllExercicList() {
    	
    	BudgetPluriannuelBean bean=((BudgetPluriannuelBean)Helper.findBean("bpBean"));
    	List<Integer> result = new ArrayList<Integer>();
    	int exerciceAE=bean.getBudget().getExercice();
    	for (int i=0; i<4;i++)
		{

		result.add(exerciceAE+i);
		}
    	
		return result;
	}
    
	public List<Integer> getUsedExercices(List<CreditPaiement> creditPayList) {
		if (logger.isDebugEnabled()) {
			logger.debug("getUsedExercices(List<CreditPaiement>) - start"); //$NON-NLS-1$
		}

	List<Integer> result = new ArrayList<Integer>();
	for (CreditPaiement credit : creditPayList) {
		if(result.contains(credit.getExercice()))continue;
		result.add(Integer.valueOf(credit.getExercice()));
		
	}

		if (logger.isDebugEnabled()) {
			logger.debug("getUsedExercices(List<CreditPaiement>) - end"); //$NON-NLS-1$
		}
		return result;
	}

	public void setListPrograme(List<SimpleEntity> listPrograme) {
		this.listPrograme = listPrograme;
	}

	public List<SimpleEntity> getListPrograme() {
		
		
		if(listPrograme==null)
			loaderFactory(ALL_PROGRAMME);
		return listPrograme;
	}

	public void setAllExecList(List<String> allExecList) {
		this.allExecList = allExecList;
	}

	public List<String> getAllExecList() {
		if (allExecList == null)
			loaderFactory(ALL_AEXERCICE);
		return allExecList;
	}

	public Integer getFirstExecElement(){
		
		return getNotUsedExercices().get(0);
		
	}
	public void loaderFactory(int listType) {
//		List<Map<String, Object>> dataList=null;
//		Map<String, Object> item=null;
//		List<String> execList=null;
		String execStr=getSearchBean().getExercice();
		Integer execValue=((execStr==null)||(execStr.trim().isEmpty()))?null:Integer.parseInt(execStr.trim());
		String codeBudget=getSearchBean().getCodeBudget();
		String codeDirection=getSearchBean().getCodeDirection();
		String codeFongDest=getSearchBean().getCodeFongDest();
		String codeService =getSearchBean().getCodeService();
		String codeDest=getSearchBean().getCodeDest();
		String codeProg=getSearchBean().getCodeProg();
		String codeNature=getSearchBean().getCodeNature();
		String codeNatureGrp=getSearchBean().getCodeNatureGrp();
		boolean isCodeBudgetNotNull=!Strings.isBlank(codeBudget);
		switch (listType) {
		case ALL_AEXERCICE:
			setAllExecList(budgetPluriService.getAllExercice(true));
			break;
		case ALL_SERVICE:
			setListService(budgetPluriService.getAllServices());
			break;
		case ALL_PROGRAMME:
			setListPrograme(budgetPluriService.getAllPrograms());
			break;
		case ALL_EXERCICE_AE:
			//setExercicAeList(budgetPluriService.getAllExercieAe());
			setExercicAeList((budgetPluriService.getAllExercieAe()).stream() .filter(x->Integer.valueOf(x)<=2023).collect(Collectors.toList()));
		
			break;
		case EXERCICE_INFOCENTRE_2024:
			//setExercicInfoCentreList(Arrays.asList( "2024"));
			setExercicInfoCentreList((budgetPluriService.getAllExercieAe()).stream() .filter(x->Integer.valueOf(x)>=2024).collect(Collectors.toList()));
			//setExercicAeList((budgetPluriService.getAllExercieAe()).stream() .filter(x->Integer.valueOf(x)>=2024).collect(Collectors.toList()));
			
			break;
		case ALL_EXERCICE_CP:
			setExercicCpList(budgetPluriService.getAllExercieCp());
			break;
		case ALL_BUDGET:
//			setListBudgets(budgetPluriService.getListBudgets(getNumExec()));
			setListBudgets(budgetPluriService.getListBudgets(getSearchBean().getCurentExercice()));
			break;
		case GROUP_DEST:
			setGroupDestList(budgetPluriService.getGroupDestList(getNumExec()));
			break;
		case BUDG_CP:
			setListBudgetsCp(budgetPluriService.getListBudgets(Helper.stringToInt(getSearchBean().getExerciceCp())));
			break;
		case ENV_BUDG_VENT_ORIGIN:
			setListEnvBdgVentilParOrigin(budgetPluriService.getEnvlpBdgVentilParOrigin(Helper.stringToInt(getSearchBean().getExerciceCp())));
			break;
		case EXEC_SUIVI_CP:
			setExerciceSuiviCpList(budgetPluriService.getListExecSuiviCp());
			break;
		case BUDG_SUIVI_CP:
			if(execValue!=null)
			setListBudgetsSuiviCp(budgetPluriService.getListBudgetSuiviCp( execValue));
			break;
		case DIRECTION_SUIVI_CP:
			if((execValue!=null)&&isCodeBudgetNotNull)
			setListDirectionSuiviCp(budgetPluriService.getListDirectionsSuiviCp(execValue, codeBudget));
			break;
		case SERVICE_SUIVI_CP:
//			if ((execValue != null) && (codeBudget != null) && (codeDirection != null))
			if ((execValue != null) && isCodeBudgetNotNull)
				setListServiceSuiviCp(budgetPluriService.getListServicesSuiviCp(execValue, codeBudget, codeDirection));	
			break;
		case FONG_DEST_SUIVI_CO:
			if((execValue!=null)&&isCodeBudgetNotNull)
				setListFongDestSuiviCp(budgetPluriService.getListFongDestSuiviCp(execValue, codeBudget));
			break;
		case DESTINATION_SUIVI_CP:         
//			if ((execValue != null) && (codeBudget != null) && (codeDirection != null) && (codeFongDest != null)
//					&& (codeService != null))
			if ((execValue != null) && isCodeBudgetNotNull)
				setListDestinationSuiviCp(budgetPluriService.getListDestinationSuiviCp(execValue, codeBudget, codeFongDest,codeDirection, codeService));
	
			break;
		case PROGRAMME_SUIVI_CP:
//			if ((execValue != null) && (codeBudget != null) && (codeDirection != null) && (codeFongDest != null)
//					&& (codeService != null)&&(codeDest!=null))
			if ((execValue != null) && isCodeBudgetNotNull)
			setListProgrammeSuiviCp(budgetPluriService.getListProgrammeSuiviCp(execValue, codeBudget, codeFongDest, codeDest, codeDirection, codeService));
			break;
		case NATURE_GRP_SUIVI_CP:
//			if ((execValue != null) && (codeBudget != null)  && (codeFongDest != null)
//					&&(codeDest!=null))
			if ((execValue != null) && isCodeBudgetNotNull )
			setListNatureGroupeSuiviCp(budgetPluriService.getListNatureGroupeSuiviCp(execValue, codeBudget, codeFongDest, codeDest, codeNature));
//		    Collections.sort(getListNatureGroupeSuiviCp());
			break;
		case NATURE_SUIVI_CP:
//			if ((execValue != null) && (codeBudget != null) && (codeDirection != null) && (codeFongDest != null)
//					&& (codeService != null)&&(codeDest!=null)&&(codeProg!=null))
			if ((execValue != null) && isCodeBudgetNotNull)
			setListNatureSuiviCp(budgetPluriService.getListNatureSuiviCp(execValue, codeBudget, codeFongDest, codeDest, codeProg,codeNatureGrp, codeDirection, codeService));
		        break;
		case NAt_GRP:
			setListNatureGroupe(budgetPluriService.getNatureGroupeList());
			break;
		case GEST_LIST:
			setListGetionnaires(budgetPluriService.getGestionnairesList());
			break;
		default:
		break;
		}
	}
	public void setListService(List<SimpleEntity> listService) {
		this.listService = listService;
	}
	public List<SimpleEntity> getListService() {
		if(listService==null)
			loaderFactory(ALL_SERVICE);
			return listService;
		
	}
	
	public void setListBudgets(List<String> listBudgets) {
		this.listBudgets = listBudgets;
	}
	public List<String> getListBudgets() {	
		if(listBudgets==null)
			loaderFactory(ALL_BUDGET);
		return listBudgets;
	}
	public void setExercicAeList(List<String> exercicAeList) {
		this.exercicAeList = exercicAeList;
	}
	public List<String> getExercicAeList() {
		if(exercicAeList==null)
			loaderFactory(ALL_EXERCICE_AE);
		return exercicAeList;
	}
	public void setGroupDestList(List<ComplexEntity> groupDestList) {
		this.groupDestList = groupDestList;
	}
	public List<ComplexEntity> getGroupDestList() {
		if(groupDestList==null)
			loaderFactory(GROUP_DEST);
		return groupDestList;
	}
	

	public SearchBean getSearchBean(){
	return (SearchBean)Helper.findBean("searchBean");	
	}
	
	private int getNumExec(){
		return Helper.stringToInt(getSearchBean().getExercice());
	}
	
	public void resetDynamicList() {
		setListBudgets(null);
		setGroupDestList(null);
		setListBudgetsCp(null);
		setListBudgetsSuiviCp(null);
		setListDirectionSuiviCp(null);
		setListServiceSuiviCp(null);		
		setListFongDestSuiviCp(null);
		setListDestinationSuiviCp(null);
		setListProgrammeSuiviCp(null);
		setListNatureGroupeSuiviCp(null);
		setListNatureSuiviCp(null);
		setCompteProduitList(null);
	}


	public void setExercicCpList(List<String> exercicCpList) {
		this.exercicCpList = exercicCpList;
	}


	public List<String> getExercicCpList() {
		if(exercicCpList==null)
			loaderFactory(ALL_EXERCICE_CP);
		return exercicCpList;
	}


	public void setListBudgetsCp(List<String> listBudgetsCp) {
		this.listBudgetsCp = listBudgetsCp;
	}


	public List<String> getListBudgetsCp() {
		if(listBudgetsCp==null)
			loaderFactory(BUDG_CP);
		return listBudgetsCp;
	}


	public void setListEnvBdgVentilParOrigin(
			List<ComplexEntity> listEnvBdgVentilParOrigin) {
		this.listEnvBdgVentilParOrigin = listEnvBdgVentilParOrigin;
	}


	public List<ComplexEntity> getListEnvBdgVentilParOrigin() {
		if(listEnvBdgVentilParOrigin==null)
			loaderFactory(ENV_BUDG_VENT_ORIGIN);
		return listEnvBdgVentilParOrigin;
	}
	





	public List<String> getExerciceSuiviCpList() {
		if(exerciceSuiviCpList==null)
			loaderFactory(EXEC_SUIVI_CP);
		return exerciceSuiviCpList;
	}


	public void setExerciceSuiviCpList(List<String> exerciceSuiviCpList) {
		this.exerciceSuiviCpList = exerciceSuiviCpList;
	}


	public List<String> getListBudgetsSuiviCp() {
		if(listBudgetsSuiviCp==null)
			loaderFactory(BUDG_SUIVI_CP);		
		return listBudgetsSuiviCp;
	}


	public void setListBudgetsSuiviCp(List<String> listBudgetsSuiviCp) {
		this.listBudgetsSuiviCp = listBudgetsSuiviCp;
	}


	public List<SimpleEntity> getListDirectionSuiviCp() {
		if(listDirectionSuiviCp==null)
			loaderFactory(DIRECTION_SUIVI_CP);
		
		return listDirectionSuiviCp;
	}


	public void setListDirectionSuiviCp(List<SimpleEntity> listDirectionSuiviCp) {
		this.listDirectionSuiviCp = listDirectionSuiviCp;
	}


	public List<SimpleEntity> getListServiceSuiviCp() {
		if(listServiceSuiviCp==null)
			loaderFactory(SERVICE_SUIVI_CP);
		return listServiceSuiviCp;
	}


	public void setListServiceSuiviCp(List<SimpleEntity> listServiceSuiviCp) {
		this.listServiceSuiviCp = listServiceSuiviCp;
	}


	public List<SimpleEntity> getListFongDestSuiviCp() {		
		if(listFongDestSuiviCp==null)
			loaderFactory(FONG_DEST_SUIVI_CO);
			
		return listFongDestSuiviCp;
			
	}


	


	public void setListFongDestSuiviCp(List<SimpleEntity> listFongDestSuiviCp) {
		this.listFongDestSuiviCp = listFongDestSuiviCp;
	}


	public List<SimpleEntity> getListDestinationSuiviCp() {
		if (listDestinationSuiviCp == null)
			loaderFactory(DESTINATION_SUIVI_CP);
		if (listDestinationSuiviCp != null) 
			Collections.sort(listDestinationSuiviCp);		
		return listDestinationSuiviCp;
	}

	public void setListDestinationSuiviCp(List<SimpleEntity> listDestinationSuiviCp) {
		this.listDestinationSuiviCp = listDestinationSuiviCp;
	}


	public List<SimpleEntity> getListProgrammeSuiviCp() {
		if(listProgrammeSuiviCp==null)loaderFactory(PROGRAMME_SUIVI_CP);	
		return listProgrammeSuiviCp;
	}


	public void setListProgrammeSuiviCp(List<SimpleEntity> listProgrammeSuiviCp) {
		this.listProgrammeSuiviCp = listProgrammeSuiviCp;
	}


	public List<SimpleEntity> getListNatureGroupeSuiviCp() {
		if(listNatureGroupeSuiviCp==null)loaderFactory(NATURE_GRP_SUIVI_CP);
		if(listNatureGroupeSuiviCp!=null) Collections.sort(listNatureGroupeSuiviCp);
		return listNatureGroupeSuiviCp;
	}


	public void setListNatureGroupeSuiviCp(List<SimpleEntity> listNatureGroupeSuiviCp) {
		this.listNatureGroupeSuiviCp = listNatureGroupeSuiviCp;
	}


	public List<SimpleEntity> getListNatureSuiviCp() {
		if(listNatureSuiviCp==null)loaderFactory(NATURE_SUIVI_CP);		
		if(listNatureSuiviCp!=null) Collections.sort(listNatureSuiviCp);
		return listNatureSuiviCp;
	}


	public void setListNatureSuiviCp(List<SimpleEntity> listNatureSuiviCp) {
		this.listNatureSuiviCp = listNatureSuiviCp;
	}
	public String getlibGroupNature(String codeGroupNat) {
		if (codeGroupNat == null || codeGroupNat.trim().isEmpty())
			return null;
		if (getListNatureGroupeSuiviCp() == null)
			return null;
		for (SimpleEntity nat : getListNatureGroupeSuiviCp()) {
			if (!nat.getCode().trim().equalsIgnoreCase(codeGroupNat.trim()))
				continue;
			return nat.getDesignation();
		}
		return null;
	}


	public List<SimpleEntity> getListNatureGroupe() {
		if(listNatureGroupe==null)loaderFactory(NAt_GRP);		
		if(listNatureGroupe!=null) Collections.sort(listNatureGroupe);		
		return listNatureGroupe;
	}


	public void setListNatureGroupe(List<SimpleEntity> listNatureGroupe) {
		this.listNatureGroupe = listNatureGroupe;
	}


	public List<Integer> getDepenseNivDestList() {
		return depenseNivDestList;
	}


	public List<SimpleEntity> getListGetionnaires() {
		if(listGetionnaires==null)loaderFactory(GEST_LIST);		
		if(listGetionnaires!=null) Collections.sort(listGetionnaires);
		return listGetionnaires;	
	}


	public void setListGetionnaires(List<SimpleEntity> listGetionnaires) {
		this.listGetionnaires = listGetionnaires;
	}


	public List<String> getListNoLtr() {
		return listNoLtr;
	}


	public void setListNoLtr(List<String> listNoLtr) {
		this.listNoLtr = listNoLtr;
	}
	
	public List<SimpleEntity> getCompteProduitList() {
		if(compteProduitList==null){
			if((!Strings.isBlank(getSearchBean().getCodeDest()))&&(!Strings.isBlank(getSearchBean().getCodeNature()))
					//&&(!Strings.isBlank(getCodeProg()))
					){
				logger.debug("getCompteProduitList:{}, {}, {}",getSearchBean().getExercice(),getSearchBean().getCodeDest(),getSearchBean().getCodeNature());
				setCompteProduitList(budgetPluriService.getCompteProduitList(Helper.stringToInt(getSearchBean().getExercice()),getSearchBean().getCodeDest(),getSearchBean().getCodeNature(),null));
			}
		}
		return compteProduitList;
	}
	public void setCompteProduitList(List<SimpleEntity> compteProduitList) {
		this.compteProduitList = compteProduitList;
	}


	public List<String> getWithClosedExercises() {
		if(withClosedExercises==null) {
			setWithClosedExercises(budgetPluriService.getAllExercice(false));
		}
		return withClosedExercises;
	}


	public void setWithClosedExercises(List<String> withClosedExercises) {
		this.withClosedExercises = withClosedExercises;
	}


	public List<String> getExercicInfoCentreList() {
		if(exercicInfoCentreList==null)
			loaderFactory(EXERCICE_INFOCENTRE_2024);
		return exercicInfoCentreList;
	}


	public void setExercicInfoCentreList(List<String> exercicInfoCentreList) {
		this.exercicInfoCentreList = exercicInfoCentreList;
	}
	
}
