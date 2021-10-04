package fr.symphonie.budget.ui.beans.edition;

import java.io.File;
import java.io.Serializable;
import java.math.BigDecimal;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;

import org.primefaces.event.TabChangeEvent;
import org.primefaces.model.StreamedContent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.symphonie.budget.core.model.Ventillation;
import fr.symphonie.budget.core.model.edition.Edition;
import fr.symphonie.budget.core.model.edition.EditionBiBr;
import fr.symphonie.budget.core.model.edition.TabsEnum;
import fr.symphonie.budget.core.model.edition.util.CAF;
import fr.symphonie.budget.core.model.edition.util.DataItem;
import fr.symphonie.budget.core.model.edition.util.DepByDest;
import fr.symphonie.budget.core.model.edition.util.NatGrpEnum;
import fr.symphonie.budget.core.model.edition.util.Operation;
import fr.symphonie.budget.core.model.edition.util.RecByOrig;
import fr.symphonie.budget.core.model.plt.DetailLigneTresorerie;
import fr.symphonie.budget.core.model.plt.EncDecEnum;
import fr.symphonie.budget.core.model.plt.EtatPeriodeEnum;
import fr.symphonie.budget.core.model.plt.LigneTresorerie;
import fr.symphonie.budget.core.model.plt.Periode;
import fr.symphonie.budget.core.model.plt.PeriodeEnum;
import fr.symphonie.budget.core.model.plt.PlanTresorerie;
import fr.symphonie.budget.core.model.pluri.BudgetCpDest;
import fr.symphonie.budget.core.model.pluri.EtatEnum;
import fr.symphonie.budget.core.service.IBudgetPluriannuelService;
import fr.symphonie.budget.ui.beans.GenericBean;
import fr.symphonie.budget.ui.beans.pluri.DialogHelper;
import fr.symphonie.budget.ui.excel.ExcelHandler;
import fr.symphonie.budget.ui.excel.ExcelModelEnum;
import fr.symphonie.common.util.BudgetHelper;
import fr.symphonie.common.util.Constant;
import fr.symphonie.common.util.MsgEntry;
import fr.symphonie.cpwin.model.Visa;
import fr.symphonie.cpwin.model.VisaEnum;
import fr.symphonie.doc.MSGenerator;
import fr.symphonie.exception.MissingConfiguration;
import fr.symphonie.util.HandlerJSFMessage;
import fr.symphonie.util.Helper;
import fr.symphonie.util.model.SimpleEntity;
import fr.symphonie.util.model.Trace;

@ManagedBean(name = "editionBean")
@SessionScoped
public class EditionBean extends GenericBean implements Serializable  {

	private static final String I = "I";
	
	private static final String R = "R";
	private static final String D = "D";
	private static final String _775 = "775%";
	private static final String _656 = "656%";
	private static final String _78 = "78%";
	private static final String _7813 = "7813";
	private static final String _68 = "68%";
	/**
	 * 
	 */
	private static final long serialVersionUID = 4285755898277392288L;
	private static String ORDRE_1 = "1";
	private static String ORDRE_2 = "2";
	private static String ORDRE_3 = "3";
	private static String ORDRE_4 = "4";
	private static String ORDRE_5 = "5";

	@ManagedProperty(value = "#{budgetPluriService}")
	private IBudgetPluriannuelService budgetPluriService;

	/**
	 * Logger for this class
	 */
	private static final Logger logger = LoggerFactory.getLogger(EditionBean.class);
	

	public void setBudgetPluriService(IBudgetPluriannuelService budgetPluriService) {
		this.budgetPluriService = budgetPluriService;
	}

	private Edition editionBi;
	private EtatEnum etatCp;
	private Boolean editionGenerated=null;
	private boolean regenerate;

	private EtatPeriodeEnum statusJanvier;
    
	public Edition getEditionBi() {
		return editionBi;
	}

	public void setEditionBi(Edition editionBi) {
		this.editionBi = editionBi;
	}

	@Override
	public void search() {
		Edition e=null;
		Integer exercice=getExercice();
		String codeBudget=getCodeBudget();
		try {
			e=loadEditionLogic(exercice, codeBudget,getNiveau(),null,false);
			setEditionBi(e);
		} catch (Exception e1) {

			HandlerJSFMessage.addErrorMessage(getErrorMessageFromException(MsgEntry.FAILED, e1));
			e1.printStackTrace();
		}
		

	}
	public Edition loadEditionLogic(int exercice,String codeBudget,Integer niveau,Integer periodeTresorerie, boolean forceDematData) throws MissingConfiguration{
		Edition e=new Edition();
		if(niveau==null) niveau=getNiveau();
		e.setExercice(exercice);		
		e.setCodeBudget(codeBudget);
		e.setNiveau(niveau);
		e.setNomConfig(getNomConfig());
		
		if((!forceDematData) &&(!isEditionGenerated())){
			initializEdition(e);
			e.setDefaultData();
		}
		else{
			logger.debug("loadEditionLogic: load edition From Saved Demat Data: ");
			loadFromSavedData(e);
		}
		loadPlanTresorerie(e,periodeTresorerie);

		return e;
	}

	private void loadFromSavedData(Edition e) {
		logger.debug("loadBiBrData - start");
		
		 EditionHelper helper=new EditionHelper();
			List<EditionBiBr> bibrList=budgetPluriService.getDematBiBrList(e.getExercice(),e.getCodeBudget(),e.getNiveau(),null);
			helper.fillEdition(e, bibrList);

			loadDestination(e);
			loadTab5(e);
			logger.debug("loadBiBrData - end");
	}

	private void loadTab5(Edition e) {
		
		List<Operation> ops=budgetPluriService.getDematBiBRTab5(e.getExercice(),e.getCodeBudget(),e.getNiveau());
		if((ops!=null)&&(!ops.isEmpty()))
		{
		for(Operation op:ops){
			if(op.isType())
				e.getTab5().getOperationsBudg().add(op);
			else
				e.getTab5().getOperationsnoBudg().add(op);
		}
		}
		else{
			createTab5(e);
		}
	}

	private void initializEdition(Edition e) {
		logger.debug("initializEdition: Niveau={}",e.getNiveau());
		boolean isBi=e.getNiveau()==0;
		if(isBi)
		{
		loadTab2(e);
		loadTab3(e);
		loadTab6(e);
		createTab5(e);
		}
		else{
			logger.debug("load BR");
			loadTab2BR(e);
			loadTab3BR(e);
			loadTab6BR(e);
			createTab5BR(e);
		}
	}

	
	private void loadPlanTresorerie(Edition e, Integer periodeTresorerie) throws MissingConfiguration {
		//Integer biPeriode=1;
		if(periodeTresorerie==null)periodeTresorerie=1;
		e.setTab7(BudgetHelper.getPlanTresorerieBean().loadPlanTresorerie(e.getExercice(), periodeTresorerie));
		
	}

	private void createTab5BR(Edition e) {
		// TODO Auto-generated method stub
		
	}

	private void createTab5(Edition e) {
		
		Operation[] datas=null;
		int ex=e.getExercice();
		String codeBudg=e.getCodeBudget();
		int niveau=e.getNiveau();
		
		datas=new Operation[]{
		new Operation(ex,codeBudg,niveau,true,"1",HandlerJSFMessage.getMessage("tab5.op1")),
		new Operation(ex,codeBudg,niveau,true,"2",HandlerJSFMessage.getMessage("tab5.op1")),
		new Operation(ex,codeBudg,niveau,true,"3",HandlerJSFMessage.getMessage("tab5.op2")),
		new Operation(ex,codeBudg,niveau,true,"4",HandlerJSFMessage.getMessage("tab5.op2")),
		new Operation(ex,codeBudg,niveau,true,"5",HandlerJSFMessage.getMessage("tab5.op3")),
		new Operation(ex,codeBudg,niveau,true,"6",HandlerJSFMessage.getMessage("tab5.op3")),
		};
		e.getTab5().setOperationsBudg(new ArrayList<Operation>(Arrays.asList(datas)));
		
		datas=new Operation[]{
		new Operation(ex,codeBudg,niveau,false,"7",HandlerJSFMessage.getMessage("tab5.op4")),
		new Operation(ex,codeBudg,niveau,false,"8",HandlerJSFMessage.getMessage("tab5.op5"))};
		e.getTab5().setOperationsnoBudg(new ArrayList<Operation>(Arrays.asList(datas)));
		
	}

	private void loadTab6(Edition e) {
		CAF noBudgData = e.getTab6().getCaf();
		int exercice=e.getExercice();
		noBudgData.insert(0, budgetPluriService.getNoDataBudg1(exercice, _68));
		noBudgData.insert(1, -(budgetPluriService.getNoDataBudg2(exercice, _78, _7813)));
		noBudgData.insert(2, budgetPluriService.getNoDataBudg1(exercice, _656));
		noBudgData.insert(3, -(budgetPluriService.getNoDataBudg1(exercice, _775)));
		noBudgData.insert(4, -(budgetPluriService.getNoDataBudg4(exercice, _7813)));

	}
	private void loadTab6BR(Edition e) {
		CAF noBudgData = e.getTab6().getCaf();
		int exercice=e.getExercice();
		noBudgData.insert(0, budgetPluriService.getBrNoDataBudg1(exercice,e.getNiveau(), _68));
		noBudgData.insert(1, -(budgetPluriService.getBrNoDataBudg2(exercice,getNiveau(), _78, _7813)));
		noBudgData.insert(2, budgetPluriService.getBrNoDataBudg1(exercice,e.getNiveau(), _656));
		noBudgData.insert(3, -(budgetPluriService.getBrNoDataBudg1(exercice,e.getNiveau(), _775)));
		noBudgData.insert(4, -(budgetPluriService.getBrNoDataBudg4(exercice,e.getNiveau(), _7813)));
		
	}
	
	private void loadDestination( Edition e) {
	logger.debug("loadDestination : start");
		SimpleEntity s=null;
		
		List<SimpleEntity> destinations = budgetPluriService.getDestinationTab3List(e.getExercice(), null);
		logger.debug("loadDestination: : " + destinations.size());
		for(DepByDest dep:e.getTab3().getTdd().getDepByDestList()){
			//logger.debug("loadDestination: dep.getDest().getCode()= "+dep.getDest().getCode()+"destinations= "+destinations);
			 s=Helper.getSelectedSimpleEntity(dep.getDest().getCode(), destinations);
			// logger.debug("loadDestination  s= "+s);
			 if(s!=null)	 
			 dep.getDest().setDesignation(s.getDesignation());
//			 logger.debug("loadDestination: s.designation: "+s.getDesignation());
			// logger.debug("loadDestination: dep.designation: " + dep.getDest().getDesignation() );
		}
		for(RecByOrig rec:e.getTab3().getTro().getRecByOrigList()){
			 s=Helper.getSelectedSimpleEntity(rec.getOrigine().getCode(), destinations);
			 if(s!=null)
			 rec.getOrigine().setDesignation(s.getDesignation());
		}
		logger.debug("loadDestination : end");
	}

	public void loadTab3(Edition e) {
		
		loadAeDest(e);
		loadCpDest(e);		
		loadRecetteOrinig(e);

	}
	private void loadTab3BR(Edition e) {
		// TODO load AE par destination remplir AE for depList
		List<DepByDest> depList=e.getTab3().getTdd().getDepByDestList();
		List<Ventillation> ventAEList=budgetPluriService.getBrDepAEByDest(e.getExercice(),e.getNiveau(),D);
		DepByDest depItem=null;
		for(Ventillation vent:ventAEList){
			depItem=getDepItem(vent.getDest(), depList);
			depItem.setMontantAE(vent.getNature().getCode(),new BigDecimal(vent.getMontant()));
		}
		//TODO load CP par destination: remplir cp for depList
		List<Ventillation> ventCPList=budgetPluriService.getBrDepCpByDest(e.getExercice(),e.getNiveau());
		for(Ventillation vent:ventCPList){
			depItem=getDepItem(vent.getDest(), depList);
			depItem.setMontantCP(vent.getNature().getCode(),new BigDecimal(vent.getMontant()));
		}
		//TODO Load Recette
		List<RecByOrig> recList=e.getTab3().getTro().getRecByOrigList();
		List<Ventillation> ventRectList=budgetPluriService.getBrDepAEByDest(e.getExercice(),e.getNiveau(),R);
		RecByOrig recItem=null;
		for(Ventillation vent:ventRectList){
			recItem=getRecItem(vent.getDest(), recList);
			recItem.setMontant(vent.getNature().getCode(),new BigDecimal(vent.getMontant()));
		}
		
	}
	private void loadRecetteOrinig( Edition e) {
		int exercice=e.getExercice();
		List<RecByOrig> recByOrigList=e.getTab3().getTro().getRecByOrigList();

		List<BudgetCpDest> recetDestList = budgetPluriService.getRecetDestList(exercice, R);
	//	logger.debug("private void loadRecetteOrinig(): aeDestList size: " + recetDestList.size());
		RecByOrig rec=null;
		List<String> filtredCodes=getFiltredDestCodes(recetDestList);
		for (BudgetCpDest item : recetDestList) {
			rec=null;
			String codeDest=item.getDestination();
			if(!filtredCodes.contains(codeDest))continue;
			//Search if related element exist
			SimpleEntity s=new SimpleEntity(codeDest, item.getLibelle());
			int index=recByOrigList.indexOf(new RecByOrig(s));
			if(index!=-1)rec=recByOrigList.get(index);
			if(rec==null){
				rec=new RecByOrig(s);
				recByOrigList.add(rec);
			}
			
			rec.setMontant(item.getGroupNat(), item.getMontant());
			
		}
		

	}

	private void loadAeDest(Edition e) {
		int exercice=e.getExercice();
		List<DepByDest> depList=e.getTab3().getTdd().getDepByDestList();
		List<BudgetCpDest> aeDestList = budgetPluriService.getAeDestinationList(exercice, D);
		//logger.debug("loadAeDestBis: aeDestList size: " + aeDestList.size());
		DepByDest dep=null;
		List<String> filtredCodes=getFiltredDestCodes(aeDestList);
		for (BudgetCpDest item : aeDestList) {
			dep=null;
			String codeDest=item.getDestination();
			if(!filtredCodes.contains(codeDest))continue;
			//Search if related element exist
			SimpleEntity s=new SimpleEntity(codeDest, item.getLibelle());
			int index=depList.indexOf(new DepByDest(s));
			if(index!=-1) dep=depList.get(index);
			
			if(dep==null){
				dep=new DepByDest(s);
				depList.add(dep);
			}
			
			dep.setMontantAE(item.getGroupNat(), item.getMontant());

		}


	}
	private void loadCpDest( Edition e) {
		int exercice=e.getExercice();
		int niveau=e.getNiveau();
		List<DepByDest> depList=e.getTab3().getTdd().getDepByDestList();
		List<BudgetCpDest> cpDestList = budgetPluriService.getBudgetCpDest(exercice, getSearchBean().getCodeBudget(),
				null,niveau);
	//	logger.debug("loadCpDestBis() : cpDestList size: " + cpDestList.size());
		DepByDest dep=null;
		SimpleEntity s=null;
	//	BigDecimal montantCP=null;
		List<String> filtredCodes=getFiltredDestCodes(cpDestList);
		for (BudgetCpDest item : cpDestList) {
			dep=null;
			String codeDest=item.getDestination();
			if(!filtredCodes.contains(codeDest))continue;
			//Search if related element exist
			s=new SimpleEntity(codeDest, item.getLibelle());
			int index=depList.indexOf(new DepByDest(s));
			if(index!=-1) dep=depList.get(index);
			
			if(dep==null){
				dep=new DepByDest(s);
				depList.add(dep);
			}
			
			dep.setMontantCP(item.getGroupNat(), item.getMontantCP());

		}
		}
	
	private List<String> getFiltredDestCodes(List<BudgetCpDest> cpDestList) {
		
		Map<String, String> codeDesMap = intializeCodeDest(cpDestList);

		String[] codesDest = new String[]{ codeDesMap.get(ORDRE_1),  codeDesMap.get(ORDRE_2),
				codeDesMap.get(ORDRE_3),  codeDesMap.get(ORDRE_4),
				 codeDesMap.get(ORDRE_5)};
	//	logger.debug("getFiltredDestCodes :  " + codesDest.toString());
		return Arrays.asList(codesDest);
	}

	private Map<String, String> intializeCodeDest(List<BudgetCpDest> cpDestList) {
		Map<String, String> map = new HashMap<String, String>();
		Set<String> distinctCodeDest = new HashSet<String>();
		for (BudgetCpDest item : cpDestList) {

			distinctCodeDest.add(item.getDestination());

		}
		TreeSet<String> myTreeSet = new TreeSet<String>();
		myTreeSet.addAll(distinctCodeDest);

		int i = 1;
		
		for (String d : myTreeSet) {
			map.put("" + i,  d);
			//logger.debug("inintializeCodeDest: distinctCodeDest: "+i+"-->" + d);
			i++;
		}
		return map;
	}

	private void loadTab2(Edition e) {
		loadDepences(e);
		loadRecettes(e);

	}
	private void loadTab2BR(Edition e) {
		// TODO DEP AE
		Map<String,Double> brAEByNatGrp=budgetPluriService.getBrDepAEByNatGrp(e.getExercice(),e.getNiveau());
		double[] AE=extractDepenseByNatGrp(brAEByNatGrp);
		// TODO DEP CP
		Map<String,Double> brCPByNatGrp=budgetPluriService.getBrDepCPByNatGrp(e.getExercice(),e.getNiveau());
		double[] CP=extractDepenseByNatGrp(brCPByNatGrp);
		e.getTab2().getDepence().setMontants(AE[Constant.PERSONNEL], AE[Constant.FONCTIONNEMENT], AE[Constant.INTERVENTION], AE[Constant.INVESTISSEMENT],
				CP[Constant.PERSONNEL], CP[Constant.FONCTIONNEMENT], CP[Constant.INTERVENTION], CP[Constant.INVESTISSEMENT]);
		// TODO RECETTE
		
		Map<String,Double> brRecette=budgetPluriService.getBrRecette(e.getExercice(),e.getNiveau());
		double[] RECETTE=extractRecetteByNatGrp(brRecette);
		logger.debug("loadTab2BR :RECETTE:{},{},{},{},{},{},{},{}",RECETTE[Constant.R11],RECETTE[Constant.R12],RECETTE[Constant.R13],RECETTE[Constant.R14],RECETTE[Constant.R15],RECETTE[Constant.R22],RECETTE[Constant.R24],RECETTE[Constant.R25]);
		logger.debug("loadTab2BR :e.getTab2().getRecette:{}",e.getTab2().getRecette());
		e.getTab2().getRecette().setMontants(
				RECETTE[Constant.R11],
				RECETTE[Constant.R12],
				RECETTE[Constant.R13],
				RECETTE[Constant.R14],
				RECETTE[Constant.R15],
				RECETTE[Constant.R22],
				RECETTE[Constant.R24],
				RECETTE[Constant.R25]);
	}
	private void loadDepences(Edition e) {
		
		Map<String,Double> bpAE=budgetPluriService.getBpDepAe(e.getExercice());
		double[] AE=extractDepenseByNatGrp(bpAE);
		
		Map<String,Double> montantsCP=budgetPluriService.getMontantDepCP(e.getExercice());
		double[] CP=extractDepenseByNatGrp(montantsCP);
		
		e.getTab2().getDepence().setMontants(AE[Constant.PERSONNEL], AE[Constant.FONCTIONNEMENT], AE[Constant.INTERVENTION], AE[Constant.INVESTISSEMENT],
				CP[Constant.PERSONNEL], CP[Constant.FONCTIONNEMENT], CP[Constant.INTERVENTION], CP[Constant.INVESTISSEMENT]);
		

	}
	public static double[] extractDepenseByNatGrp(Map<String,Double> map){
		double[] montants=new double[4];
		for(String key:map.keySet()){
			Double mnt=map.get(key);
			if(mnt==null)mnt=0d;
			switch(NatGrpEnum.parse(key)){
			case NAT_GRP_1:
				montants[Constant.PERSONNEL]=mnt;
				break;
			case NAT_GRP_2:
				montants[Constant.FONCTIONNEMENT]=mnt;
				break;
			case NAT_GRP_3:
				montants[Constant.INVESTISSEMENT]=mnt;
				break;
			case NAT_GRP_4:
				montants[Constant.INTERVENTION]=mnt;
				break;
			default:
				break;
			}
		}

		return montants;
	}

	private void loadRecettes( Edition e) {
		Map<String,Double> bpRect=budgetPluriService.getBpRecetByNatGrp(e.getExercice());
		double[] RECETTE=extractRecetteByNatGrp(bpRect);
//		double r11 = budgetPluriService.getRecet(exercice, NatGrpEnum.NAT_GRP_R11.getCode());
		e.getTab2().getRecette().setMontants(RECETTE[Constant.R11], RECETTE[Constant.R12], RECETTE[Constant.R13], RECETTE[Constant.R14], RECETTE[Constant.R15], RECETTE[Constant.R22], RECETTE[Constant.R24], RECETTE[Constant.R25]);
	}
	public static double[] extractRecetteByNatGrp(Map<String,Double> map){
		double[] montants=new double[8];
		for(String key:map.keySet()){
			Double mnt=map.get(key);
			if(mnt==null)mnt=0d;
			logger.debug("extractRecetteByNatGrp: key -> {}",key);
			NatGrpEnum natGrp=NatGrpEnum.parse(key);
			if(natGrp==null) continue;
			switch(natGrp){
			
			case NAT_GRP_R11:
				montants[Constant.R11]=mnt;
				break;
			case NAT_GRP_R12:
				montants[Constant.R12]=mnt;
				break;
			case NAT_GRP_R13:
				montants[Constant.R13]=mnt;
				break;
			case NAT_GRP_R14:
				montants[Constant.R14]=mnt;
				break;
			case NAT_GRP_R15:
				montants[Constant.R15]=mnt;
				break;
			case NAT_GRP_R22:
				montants[Constant.R22]=mnt;
				break;
			case NAT_GRP_R24:
				montants[Constant.R24]=mnt;
				break;
			case NAT_GRP_R25:
				montants[Constant.R25]=mnt;
				break;
			default:
				break;
			
			}
		}
		return montants;
	}

	private int getNiveau() {

		return BudgetHelper.getBpBean().getNiveau();
	}

	public double getFinancActifEtat() {
		return budgetPluriService.getRessourceFAE(getExercice(), R, I);
	}
	@Override
	 public void resetDynamicList() {
			setEditionBi(null);
			setErrorReport(null);
	}
	 public boolean isEditable() {
		 EtatEnum etat=getEtatCp();
		 if(etat==null)return false;
		 logger.debug("isEditable: etat={} -> {}",etat,EtatEnum.ORDO==etat);
		 return EtatEnum.ORDO==etat;
		}
	 public boolean isJanvierEditable() {
		 if(!isCommonRequiredDone())return false;
		 if(statusJanvier==null) {
		Periode janvier=BudgetHelper.getPlanTresorerieBean().getPeriode(getExercice(), PeriodeEnum.Janvier.getValue());
		setStatusJanvier(janvier.getEtat());
		 }
		 return statusJanvier==EtatPeriodeEnum.Previsionnel;
		}
	 public boolean isPermitted() {
		 EtatEnum etat=getEtatCp();
		 if(etat==null)return false;
		 
			 switch(BudgetHelper.getNavigationBean().getCurrentAction())
			 {				
				case EDITIONS_BR: case CHARGEMENT_EDIT_BR:
					return (EtatEnum.ORDO==etat)||(EtatEnum.VALIDE==etat);
				default:
					//return (isEditable()) && (!isEditionGenerated() ||isRegenerate());
					return (isEditable() ||(isRegenerate()));
			 }
		
		}
	 
	private EtatEnum getEtatCp() {
		switch(BudgetHelper.getNavigationBean().getCurrentAction()){
				
		case EDITIONS_BR:
			return (BudgetHelper.getBpBean().getSelectedBudgModif()!=null)?BudgetHelper.getBpBean().getSelectedBudgModif().getEtatCp():null;
			
		default:
			if((etatCp==null)&&(isCommonRequiredDone())){
				etatCp=budgetPluriService.getBudgetEtatCp(getExercice());
			}
			return etatCp;
		}
		
		
	}

	public void setEtatCp(EtatEnum etatCp) {
		this.etatCp = etatCp;
	}

	@Override
	public void reset() {
		super.setImportFileUploadEvent(null);
		setEtatCp(null);
		setRegenerate(false);
		setEditionGenerated(null);
		resetDynamicList();
		setStatusJanvier(null);
	}

	public boolean isEditionGenerated() {
		if((editionGenerated==null)&&(isCommonRequiredDone())){
			if(BudgetHelper.getBpBean().isBI())
			editionGenerated=budgetPluriService.getEtatEditionBI(getExercice());
			else
			editionGenerated=(EtatEnum.VALIDE==getEtatCp());
		}
		logger.debug("isEditionGenerated {} -> {}",getExercice(),editionGenerated);
		return (editionGenerated==null?false:editionGenerated);
	}

	public void setEditionGenerated(Boolean editionGenerated) {
		this.editionGenerated = editionGenerated;
	}

	public boolean isRegenerate() {
		return regenerate;
	}

	public void setRegenerate(boolean regenerate) {
		this.regenerate = regenerate;
	}
	 public StreamedContent getXlsEditionBi(){
			StreamedContent returnStreamedContent = null;
			ExcelHandler excel = new ExcelHandler(ExcelModelEnum.EDITION_BI_BR,null);
			returnStreamedContent = excel.getExcelFile();
			
			return returnStreamedContent;			

	}
	 public StreamedContent getSavedEditionBi(){
		 logger.debug("getSavedEditionBi: start");
			StreamedContent returnStreamedContent = null;
			File file=new File(getSavedBiPath());
			 logger.debug("getSavedEditionBi: file="+file.getName());
			returnStreamedContent=Helper.getStreamedContentFile(file);
			 logger.debug("getSavedEditionBi: end=");
			return returnStreamedContent;			

	}
	public void validerEdition(){
		logger.debug("validerEdition: start");
		int noBiBr=getEditionBi().getNiveau();
		
		try{
		if(noBiBr==0)	generateEdition();
		saveEditionData();
		setEditionGenerated(null);
		HandlerJSFMessage.addInfo(MsgEntry.SUCCES);
//		}catch(Exception e){
//			e.printStackTrace();
//			HandlerJSFMessage.addErrorMessageFromException(MsgEntry.FAILED, e);
//		}
		} catch (Exception e) {
			HandlerJSFMessage.addErrorMessage(getErrorMessageFromException(MsgEntry.FAILED, e));
			e.printStackTrace();
		}
		
		logger.debug("validerEdition: end");
	}
	
	public void saveImportedBI(){
		logger.debug("saveImportedBI: start");

		try{
		saveEditionData();
		setEditionGenerated(null);
		getEditionBi().setLoaded(true);
		HandlerJSFMessage.addInfo(MsgEntry.SUCCES);
//		}catch(Exception e){
//			e.printStackTrace();
//			HandlerJSFMessage.addErrorMessageFromException(MsgEntry.FAILED, e);
//		}
		} catch (Exception e) {
			HandlerJSFMessage.addErrorMessage(getErrorMessageFromException(MsgEntry.FAILED, e));
			e.printStackTrace();
		}
		
		logger.debug("saveImportedBI: end");
	}
	public void saveImportedPLT(){
		logger.debug("saveImportedPLT: start");
		Trace trace=Helper.createTrace();
		Visa visa = new Visa();
		visa.setVisa(VisaEnum.Auto);
		visa.setAuteur(trace.getAuteurMaj());
		visa.setDate(trace.getDateMaj());
		try{
		BudgetHelper.getPlanTresorerieBean().validatePlanTresorerie(getEditionBi().getTab7(),EtatPeriodeEnum.Previsionnel,visa,visa);
		setStatusJanvier(EtatPeriodeEnum.Valide);
		HandlerJSFMessage.addInfo(MsgEntry.SUCCES);
//		}catch(Exception e){
//			e.printStackTrace();
//			HandlerJSFMessage.addErrorMessageFromException(MsgEntry.FAILED, e);
//		}
		} catch (Exception e) {
			HandlerJSFMessage.addErrorMessage(getErrorMessageFromException(MsgEntry.FAILED, e));
			e.printStackTrace();
		}
		logger.debug("saveImportedPLT: end");
	}
	public void saveImportedBR(){
		logger.debug("saveImportedBR: start");
		
		try{
		saveEditionData();
		setEditionGenerated(null);
		getEditionBi().setLoaded(true);
		HandlerJSFMessage.addInfo(MsgEntry.SUCCES);
//		}catch(Exception e){
//			e.printStackTrace();
//			HandlerJSFMessage.addErrorMessageFromException(MsgEntry.FAILED, e);
//		}
		} catch (Exception e) {
			HandlerJSFMessage.addErrorMessage(getErrorMessageFromException(MsgEntry.FAILED, e));
			e.printStackTrace();
		}
		logger.debug("saveImportedBR: end");
	}
	private void generateEdition() {
		getXlsEditionBi();
		
	}
	private void saveEditionData() {	
		int noBiBr=getEditionBi().getNiveau();			
		List<EditionBiBr> bibrList=createBiBrList(noBiBr);		
		budgetPluriService.saveDematBiBr(getExercice(),getCodeBudget(),noBiBr,bibrList);
		if(noBiBr==0)
		{			
		budgetPluriService.saveDematBiBrTab5(getEditionBi().getTab5());		
		budgetPluriService.updateBudgetEditionBiFlag(getExercice(), true);
		}
		
	}

	private List<EditionBiBr> createBiBrList(int noBiBr) {
		List<EditionBiBr> bibrList=new ArrayList<EditionBiBr>();
		List<DataItem> items=null;
		EditionBiBr bibrItem=null;
		Trace trace=Helper.createTrace();
		for(TabsEnum type:TabsEnum.values()){
			if(!type.isBibr())continue;
			items=getEditionBi().getDematItems(type);
			for(DataItem item:items){
				//logger.info("type="+type+", ref="+item.getUid());
				bibrItem=new EditionBiBr(getExercice(),getCodeBudget(),noBiBr,type,item);				
				bibrItem.setTrace(trace);				
				bibrList.add(bibrItem);
			//	logger.info(":bibrItem->{}",bibrItem);
			}			
		}	
		
		return bibrList;
		
	}

	public boolean checkRequiredData(){
		List<DataItem> items=getEditionBi().getEditableItems();
		for(DataItem item:items){
			if(item.getMontant()==null)return false;
		}
		
		return true;
	}
	
	private String getSavedBiPath(){
		String fileName=ExcelModelEnum.EDITION_BI_BR.getFileName();
		Integer exercice=getExercice();
		String codeBudget=getSearchBean().getCodeBudget();
		StringBuffer savingPath=new StringBuffer( MSGenerator.EXPORT_OUTPUT_PATH);
		if(exercice!=null)savingPath.append(exercice);
		if(codeBudget!=null)savingPath.append(File.separator+codeBudget);
		savingPath.append(File.separator+MSGenerator.GENERATED+fileName);
		logger.debug("getSavedPath: "+savingPath.toString());
		return savingPath.toString();
	}
	
	public void onEditionTabChange(TabChangeEvent event) {

		if(!isEditionGenerated()){
		logger.info("---- changement d'onglet ----");
		getEditionBi().setInternalDefaultData(false);
		}

	}
	
	
	
	public void prepareValidatEdit() {
		if (logger.isDebugEnabled()) {
			logger.info("prepareValidatEdit() - start"); //$NON-NLS-1$
		}
		if(!checkRequiredData()){
			logger.debug("prepareValidatEdit: checkRequiredData: "+false);
			HandlerJSFMessage.addError(MsgEntry.EDITION_REQUIRED_DATA);
			return ;
		}
		DialogHelper.openValidatEditDialog();
		if (logger.isDebugEnabled()) {
			logger.debug("prepareValidatEdit() - end"); //$NON-NLS-1$
		}
	}
	
	public void closeValidatEditDialog() {
		if (logger.isDebugEnabled()) {
			logger.debug("closeValidatEditDialog() - start"); //$NON-NLS-1$
		}		
		DialogHelper.closeDialog(null);
		if (logger.isDebugEnabled()) {
			logger.debug("closeValidatEditDialog() - end"); //$NON-NLS-1$
		}
    }

	@Override
	public void validate() {
		// TODO Auto-generated method stub
		
	}

	private DepByDest getDepItem(SimpleEntity dest,List<DepByDest> depList){
		//Search if related element exist
		DepByDest dep=null;
		int index=depList.indexOf(new DepByDest(dest));
		if(index!=-1) dep=depList.get(index);
		
		if(dep==null){
			dep=new DepByDest(dest);
			depList.add(dep);
		}
		return dep;
		
	}
	private RecByOrig getRecItem(SimpleEntity dest,List<RecByOrig> recList){
		//Search if related element exist
		RecByOrig rec=null;
		int index=recList.indexOf(new RecByOrig(dest));
		if(index!=-1) rec=recList.get(index);
		
		if(rec==null){
			rec=new RecByOrig(dest);
			recList.add(rec);
		}
		return rec;
		
	}
	
	public boolean isRequiredDataDone() {		
		switch (getCurrentAction()) {
		case CHARGEMENT_EDITIONS:		
			if (!isCommonRequiredDone())
				return false;
			if (getImportFileUploadEvent() == null)
				return false;
			return true;	
		case CHARGEMENT_EDIT_BR:	
		
			if (!isCommonRequiredDone())
				return false;			
			if (getImportFileUploadEvent() == null)
				return false;	
			if(BudgetHelper.getBpBean().getSelectedBudgModif()==null)				
				return false;		
			return true;
		default:
			return true;		
		}
	}
	
	@Override
	protected String getSavedPath() throws MissingConfiguration {
		Path path=Constant.getProjectRootPath().resolve("GBCP").resolve("Import");	
		return path.toString();
	}

	@Override
	protected String getImportFileName() {
		String fileName=null;
		fileName=(isBI()?"BI":"BR_"+getNiveau())+"_"+getExercice()+".xls";		
		return fileName;
	}

public void importerBI(){
	String inputFile=null;
		Edition bi=null;
		PlanTresorerie<DetailLigneTresorerie> plan=null;
		try{
		inputFile=getSavedPath()+File.separator+getImportFileName();
		logger.debug("importerBI  --> file={} : Début ...",inputFile);
		bi=importEdition(inputFile);
		plan=importPlanTresorerie(inputFile);
		if(hasErrors()){
			cleaningErrorReport();
			return;
		}
		bi.setTab7(plan);
		setEditionBi(bi);
		
		setStatusJanvier(null);
//		}
//		catch(Exception e){
//			HandlerJSFMessage.addErrorMessageFromException(MsgEntry.FAILED, e);
//		}
} catch (Exception e) {
	HandlerJSFMessage.addErrorMessage(getErrorMessageFromException(MsgEntry.FAILED, e));
	e.printStackTrace();
}
		logger.debug("importerBI : Fin}");
		
	}
public void importerBR(){
		String inputFile=null;
		Edition bi=null;
		
		try{
		inputFile=getSavedPath()+File.separator+getImportFileName();
		logger.debug("importerBR  --> file={} : Début ...",inputFile);
		bi=importEdition(inputFile);
		
		if(hasErrors()){
			cleaningErrorReport();
			return;
		}
		setEditionBi(bi);		
		}
//		catch(Exception e){
//			HandlerJSFMessage.addErrorMessageFromException(MsgEntry.FAILED, e);
//		}
		catch (Exception e) {
			HandlerJSFMessage.addErrorMessage(getErrorMessageFromException(MsgEntry.FAILED, e));
			e.printStackTrace();
		}
		
		logger.debug("importerBR : Fin}");
		
	}
private Edition importEdition(String inputFile) throws MissingConfiguration {
	logger.debug("importEdition  --> file={} : Début ...",inputFile);
//	setErrorReport(null);
	List<DataItem> list=importFile(inputFile, 0, DataItem.class);
	logger.debug("importEdition list: {}",list.size());
	Edition e=checkEditionData(list);		
	return e;
}
private PlanTresorerie<DetailLigneTresorerie> importPlanTresorerie(String inputFile) throws MissingConfiguration {
	List<SimpleEntity> savedErrorReport=new ArrayList<>(getErrorReport());
	PlanTresorerie<DetailLigneTresorerie> tab7=null;
	logger.debug("importPlanTresorerie  --> file={} : Début ...",inputFile);
	List<DetailLigneTresorerie> list=importFile(inputFile, 1, DetailLigneTresorerie.class);
	logger.debug("importPlanTresorerie list: {}",list.size());
	getErrorReport().addAll(savedErrorReport);
	tab7=checkPltData(list);		
	return tab7;
}
private PlanTresorerie<DetailLigneTresorerie> checkPltData(List<DetailLigneTresorerie> detailsList) throws MissingConfiguration {
	Integer exercice=getExercice();
	Integer periode=PeriodeEnum.Janvier.getValue();
	List<LigneTresorerie> lignes=BudgetHelper.getPlanTresorerieService().getLigneTresorerie(exercice, null, null);
	PlanTresorerie<DetailLigneTresorerie> p=BudgetHelper.getInstanceOfPlanTresorerie(DetailLigneTresorerie.class);
	p.setExercice(exercice);
	p.setPeriode(periode);
	p.setNomConfig(getNomConfig());
	
	createDetailsPLT(p,detailsList,lignes);
	createGlobalPlt(p,lignes);
	completPlt(p);
	
	
	
	return p;
}

private void completPlt(PlanTresorerie<DetailLigneTresorerie> p) {
	p.setSoldeInitial(new DetailLigneTresorerie());
	double solde01 = BudgetHelper.getPlanTresorerieService().getSoldeInitialJanvier(p.getExercice());
	p.getSoldeInitial().setMontant01(new BigDecimal(solde01));
	p.calculateTotaux();
	p.sort();
	p.prepare();
	
}

private void createGlobalPlt(PlanTresorerie<DetailLigneTresorerie> p, List<LigneTresorerie> lignes) {
	DetailLigneTresorerie global=null;
	for (LigneTresorerie ligne : lignes) {
		if(!ligne.isGlobal())continue;
		global =new DetailLigneTresorerie(ligne);
		p.createGlobale(global);
	}
}

private void createDetailsPLT(PlanTresorerie<DetailLigneTresorerie> plan, List<DetailLigneTresorerie> detailsList, List<LigneTresorerie> lignes) {
	Integer numero=null;Integer index=null;
	Integer exercice=plan.getExercice();
	LigneTresorerie l=null;
	SimpleEntity s=null;	
	
	for(DetailLigneTresorerie item:detailsList) {
		numero=item.getLigne().getNumero();
		if(numero==0)continue;
		item.setNumero(numero);
		item.setExercice(exercice);
		item.setPeriode(plan.getPeriode());		
		item.getLigne().setExercice(exercice);
		index=lignes.indexOf(item.getLigne());
		if(index==-1) {
			s=new SimpleEntity(""+numero ,"Numéro de ligne de trésorerie "+numero+" non valide");
			getErrorReport().add(s);
		}
		else {
			l=lignes.get(index);
			item.setLigne(l);
			EncDecEnum typeOp=l.getTypeOp();
			plan.getDetailList(typeOp).add(item);
		}
	}
	
}

private Edition checkEditionData(List<DataItem> list) throws MissingConfiguration {
	Edition e=new Edition();
	e.setNiveau(getNiveau());
	Map<TabsEnum,List<DataItem>> dataMap=new HashMap<TabsEnum, List<DataItem>>();
	e.setExercice(getExercice());
	e.setCodeBudget(getCodeBudget());
	e.setNomConfig(getNomConfig());
	TabsEnum typeTab=null;
	SimpleEntity s=null;
	int index=0;
	for(DataItem item:list) {
		index++;
		typeTab=TabsEnum.parse(item.getPrefixRef());
		if(typeTab==null) {
			s=new SimpleEntity(item.getPrefixRef(),"Nom de table "+item.getPrefixRef()+" non valide");
			getErrorReport().add(s);
		}
		else if(item.getRefData()==null) {
			s=new SimpleEntity(item.getPrefixRef()+index,"Reference  non valide");
			getErrorReport().add(s);
		}
		else {
			List<DataItem> l=dataMap.get(typeTab);
			if(l==null) {
				l=new ArrayList<DataItem>();
				dataMap.put(typeTab, l);
			}
			item.setPrefixRef(null);
			l.add(item);
		}
	}
	EditionHelper helper=new EditionHelper();
	helper.fillEdition(e, dataMap);
	return e;
}
	
	
	 public StreamedContent getImportEditionBiBr(){
		 StreamedContent returnStreamedContent = null;
		 ExcelHandler excel;
		 switch(BudgetHelper.getNavigationBean().getCurrentAction())
		 {				
			case CHARGEMENT_EDITIONS: 
				excel = new ExcelHandler(ExcelModelEnum.IMPORT_BI,null);
				returnStreamedContent = excel.getExcelFile();
				break;
			case CHARGEMENT_EDIT_BR: 
				excel = new ExcelHandler(ExcelModelEnum.IMPORT_BR,null);
				returnStreamedContent = excel.getExcelFile();
				break;
			default:
				break;
				
		 }
			
			return returnStreamedContent;	
				
				
				
					
	}
	 
		public boolean isErrorReportVisible()
		{
			if(getErrorReport()==null) return false;
			if(getErrorReport().isEmpty())return false;
			return true;
		}
		public String getErreursCount() {
			if (getErrorReport() == null)
				return "";
			return "(" + getErrorReport().size() + ")";
		}
		
		 public boolean isDataVisible()
		   {
			if(isErrorReportVisible()) return true;
			if(getEditionBi()!=null) return true;
			return false; 
		   }
		 
		 public boolean isImportDataVisible()
			{
				if(isErrorReportVisible()) return false;
				if(getEditionBi()==null) return false;
				
				return true;
			}

		public EtatPeriodeEnum getStatusJanvier() {
			return statusJanvier;
		}

		public void setStatusJanvier(EtatPeriodeEnum statusJanvier) {
			this.statusJanvier = statusJanvier;
		}	
		
		 
}
