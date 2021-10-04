package fr.symphonie.budget.ui.excel.edition;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.symphonie.budget.core.model.cf.Rapprochement;
import fr.symphonie.budget.core.model.edition.Edition;
import fr.symphonie.budget.core.model.edition.Tab10;
import fr.symphonie.budget.core.model.edition.Tab2;
import fr.symphonie.budget.core.model.edition.Tab3;
import fr.symphonie.budget.core.model.edition.Tab4;
import fr.symphonie.budget.core.model.edition.Tab6;
import fr.symphonie.budget.core.model.edition.Tab8;
import fr.symphonie.budget.core.model.edition.TabsEnum;
import fr.symphonie.budget.core.model.edition.util.ABE;
import fr.symphonie.budget.core.model.edition.util.DataItem;
import fr.symphonie.budget.core.model.edition.util.DepByDest;
import fr.symphonie.budget.core.model.edition.util.EFE;
import fr.symphonie.budget.core.model.edition.util.RecByOrig;
import fr.symphonie.budget.core.model.plt.DetailLigneTresorerie;
import fr.symphonie.budget.core.model.plt.GenericLigneTresorerie;
import fr.symphonie.budget.core.model.plt.PlanTresorerie;
import fr.symphonie.budget.ui.beans.GenericBean;
import fr.symphonie.budget.ui.beans.edition.CompteFinancierBean;
import fr.symphonie.budget.ui.doc.CustomHashMap;
import fr.symphonie.budget.ui.viewmodel.VentilationImput;
import fr.symphonie.exception.MissingConfiguration;

public class EditionExcelHelper {
	
	private static final Logger logger = LoggerFactory.getLogger(EditionExcelHelper.class);

public static void createEncaissMap(Map<String, Object> map, PlanTresorerie<DetailLigneTresorerie> plan) {
	logger.debug("createEncaissMap: {}/{}",plan.getExercice(),plan.getPeriode());
		List<? extends GenericLigneTresorerie> data = plan.getEncaissements();
		String prefix="encaiss";
		if (data != null) {
			for (GenericLigneTresorerie element : data) {
//				logger.debug("createEncaissMap: {}",element.getLigne().getNumero());
				map.put(prefix+element.getLigne().getNumero(), element);       

			}
				
			}
		
	}
	public static void createDecaissMap(Map<String, Object> map, PlanTresorerie<DetailLigneTresorerie> plan) {
		
		logger.debug("createDecaissMap: {}/{}",plan.getExercice(),plan.getPeriode());
		List<DetailLigneTresorerie> data = plan.getDecaissements();
		String prefix="decaiss";

		if (data != null) {
			for (GenericLigneTresorerie element : data) {
//				logger.debug("createDecaissMap: {}",element.getLigne().getNumero());
				map.put(prefix+element.getLigne().getNumero(), element);  
				}  }

		
	}
	public static void createTab2Map(Map<String, Object> map ,Tab2 tab2) {	
		createMap(TabsEnum.ABP, map, tab2.getItems());		

	}
	 public static void createTab3Map(Map<String, Object> map,Tab3 tab3){
		 
		 createAeDestMap(map, tab3);
		 createRecetDestMap(map, tab3);
		 
	 }
	
	
	public static void createTab4Map(Map<String, Object> map,Tab4 tab4) {		
		
		createMap(TabsEnum.EFP, map, tab4.getItems());		
		
		
	}
	public static void createABEMap(Map<String, Object> map, ABE e) {
		createMap(TabsEnum.ABE, map, e.getItems());
	}
	public static void createEfeMap(Map<String, Object> map, EFE efe){
		createMap(TabsEnum.EFE, map, efe.getItems());
	}
	
	public static void createSoldinitial(Map<String, Object> map, PlanTresorerie<DetailLigneTresorerie> plan) {
		
		map.put("si.montant01", plan.getSoldeInitial().getMontant01());
		map.put("si.montant02", plan.getSoldeInitial().getMontant02());
		map.put("si.montant03", plan.getSoldeInitial().getMontant03());
		map.put("si.montant04", plan.getSoldeInitial().getMontant04());
		map.put("si.montant05", plan.getSoldeInitial().getMontant05());
		map.put("si.montant06", plan.getSoldeInitial().getMontant06());
		map.put("si.montant07", plan.getSoldeInitial().getMontant07());
		map.put("si.montant08", plan.getSoldeInitial().getMontant08());
		map.put("si.montant09", plan.getSoldeInitial().getMontant09());
		map.put("si.montant10", plan.getSoldeInitial().getMontant10());
		map.put("si.montant11", plan.getSoldeInitial().getMontant11());
		map.put("si.montant12", plan.getSoldeInitial().getMontant12());
		
	}
 public static void createAeDestMap(Map<String, Object> map,Tab3 tab3) {
		
		
		List<Map<String, Object>> niveau1 = new ArrayList<Map<String, Object>>();
		Map<String, Object> itemNiveau1 = null;
		List<DepByDest> data = tab3.getTdd().getDepByDestList();
//		
		
		if (data != null) {
			for (DepByDest element : data) {
				itemNiveau1 = new CustomHashMap<String, Object>();
				itemNiveau1.put("nomDest", element.getDest().getCodeAndLibelle());
				itemNiveau1.put("personnelAe", element.getPersonnel().getDataAE().getMontant());
				itemNiveau1.put("personnelCp", element.getPersonnel().getDataCP().getMontant());
				itemNiveau1.put("fonctionAe", element.getFonctionnement().getDataAE().getMontant());
				itemNiveau1.put("fonctionCp", element.getFonctionnement().getDataCP().getMontant());
				itemNiveau1.put("interventionAe", element.getIntervention().getDataAE().getMontant());
				itemNiveau1.put("interventionCp", element.getIntervention().getDataCP().getMontant());
				itemNiveau1.put("investAe", element.getInvestissement().getDataAE().getMontant());
				itemNiveau1.put("investCp", element.getInvestissement().getDataCP().getMontant());
				itemNiveau1.put("totalDestAE", element.getTotale().getDataAE().getMontant());
				itemNiveau1.put("totalDestCP", element.getTotale().getDataCP().getMontant());

				niveau1.add(itemNiveau1);

				

			}
			
			map.put("niveau", niveau1);
			map.put("total.pers.ae", tab3.getTdd().getTotale().getPersonnel().getDataAE().getMontant());
			map.put("total.pers.cp", tab3.getTdd().getTotale().getPersonnel().getDataCP().getMontant());
			
			map.put("total.fonct.ae", tab3.getTdd().getTotale().getFonctionnement().getDataAE().getMontant());
			map.put("total.fonct.cp", tab3.getTdd().getTotale().getFonctionnement().getDataCP().getMontant());
			
			map.put("total.interv.ae", tab3.getTdd().getTotale().getIntervention().getDataAE().getMontant());
			map.put("total.interv.cp", tab3.getTdd().getTotale().getIntervention().getDataCP().getMontant());
			
			map.put("total.invest.ae", tab3.getTdd().getTotale().getInvestissement().getDataAE().getMontant());
			map.put("total.invest.cp", tab3.getTdd().getTotale().getInvestissement().getDataCP().getMontant());
			
			map.put("total.ae", tab3.getTdd().getTotale().getTotaleAE());
			map.put("total.cp", tab3.getTdd().getTotale().getTotaleCP());
			map.put("sold.budg.excedent", tab3.getTdd().getSoldeBudgExcedent().getMontant());
		}

	}

 public static void createRecetDestMap(Map<String, Object> map,Tab3 tab3) {

		
		List<Map<String, Object>> niveau1 = new ArrayList<Map<String, Object>>();
		Map<String, Object> itemNiveau1 = null;
		
		
		List<RecByOrig> data = tab3.getTro().getRecByOrigList();
		if (data != null) {
			for (RecByOrig element : data) {
				itemNiveau1 = new CustomHashMap<String, Object>();
				itemNiveau1.put("nomOrig", element.getOrigine().getCodeAndLibelle());
				itemNiveau1.put("recetteAe", element.getDataR11().getMontant());
				itemNiveau1.put("recetteCp", element.getDataR12().getMontant());
				itemNiveau1.put("frecetteAe", element.getDataR13().getMontant());
				itemNiveau1.put("frecetteCp", element.getDataR14().getMontant());
				itemNiveau1.put("irecetteAe", element.getDataR15().getMontant());
				itemNiveau1.put("irecetteCp", element.getDataR22().getMontant());
				itemNiveau1.put("vrecetteAe", element.getDataR24().getMontant());
				itemNiveau1.put("vrecetteCp", element.getDataR25().getMontant());
				
				itemNiveau1.put("totalRec", element.getTotale().getMontant());
				niveau1.add(itemNiveau1);

				

			}
			map.put("recette", niveau1);
			map.put("total.recet.ae", tab3.getTro().getTotale().getDataR11().getMontant());
			map.put("total.recet.cp", tab3.getTro().getTotale().getDataR12().getMontant());
			
			map.put("total.frecet.ae", tab3.getTro().getTotale().getDataR13().getMontant());
			map.put("total.frecet.cp", tab3.getTro().getTotale().getDataR14().getMontant());
			
			map.put("total.irecet.ae", tab3.getTro().getTotale().getDataR15().getMontant());
			map.put("total.irecet.cp", tab3.getTro().getTotale().getDataR22().getMontant());
			
			map.put("total.vrecet.ae", tab3.getTro().getTotale().getDataR24().getMontant());
			map.put("total.vrecet.cp", tab3.getTro().getTotale().getDataR25().getMontant());
			
			map.put("total.totalrec", tab3.getTro().getTotale().getTotale().getMontant());
			map.put("solde.deficit", tab3.getTro().getSoldeBudgDeficit().getMontant());
		}

	}
 public static void createCRMap(Map<String, Object> map,Edition e ) {
	 TabsEnum[] usedTabs=new TabsEnum[]{TabsEnum.CRP,TabsEnum.CAFP,TabsEnum.TFP		};
		
		for(TabsEnum tab:usedTabs){
			EditionExcelHelper.createMap(tab, map, e.getDematItems(tab));
		}
	
	}
	public static void createTab7Map(Map<String, Object> map,PlanTresorerie<DetailLigneTresorerie> plan) {
		createSoldinitial(map,plan);
		createEncaissMap(map,plan);
		createDecaissMap(map,plan);
//		createSoldCumulMap(map,plan);
		
	}
	public static void createVFRMap(Map<String, Object> map,Tab6 tab6) {
		createMap(TabsEnum.SPP2, map, tab6.getVfr().getItems());

	}
	public static void createSoldCumulMap(Map<String, Object> map, PlanTresorerie<DetailLigneTresorerie> plan){
		
		map.put("solde.mois.1",plan.getSoldeMois().getMontant01());
		map.put("solde.mois.2",plan.getSoldeMois().getMontant02());
		map.put("solde.mois.3",plan.getSoldeMois().getMontant03());
		map.put("solde.mois.4",plan.getSoldeMois().getMontant04());
		map.put("solde.mois.5",plan.getSoldeMois().getMontant05());
		map.put("solde.mois.6",plan.getSoldeMois().getMontant06());
		map.put("solde.mois.7",plan.getSoldeMois().getMontant07());
		map.put("solde.mois.8",plan.getSoldeMois().getMontant08());
		map.put("solde.mois.9",plan.getSoldeMois().getMontant09());
		map.put("solde.mois.10",plan.getSoldeMois().getMontant10());
		map.put("solde.mois.11",plan.getSoldeMois().getMontant11());
		map.put("solde.mois.12",plan.getSoldeMois().getMontant12());
		map.put("solde.mois.13",plan.getSoldeMois().getTotaleVariationAnnuelle());
		
		map.put("cumule.1",plan.getSoldeCumule().getMontant01());
		map.put("cumule.2",plan.getSoldeCumule().getMontant02());
		map.put("cumule.3",plan.getSoldeCumule().getMontant03());
		map.put("cumule.4",plan.getSoldeCumule().getMontant04());
		map.put("cumule.5",plan.getSoldeCumule().getMontant05());
		map.put("cumule.6",plan.getSoldeCumule().getMontant06());
		map.put("cumule.7",plan.getSoldeCumule().getMontant07());
		map.put("cumule.8",plan.getSoldeCumule().getMontant08());
		map.put("cumule.9",plan.getSoldeCumule().getMontant09());
		map.put("cumule.10",plan.getSoldeCumule().getMontant10());
		map.put("cumule.11",plan.getSoldeCumule().getMontant11());
		map.put("cumule.12",plan.getSoldeCumule().getMontant12());

	}
	public static void createTab6(Map<String, Object> map, Edition edition) {
		TabsEnum[] usedTabs=new TabsEnum[]{TabsEnum.CRP,TabsEnum.CAFP,TabsEnum.TFP,
				TabsEnum.SPP2};
		
		for(TabsEnum tab:usedTabs){
			EditionExcelHelper.createMap(tab, map, edition.getDematItems(tab));
		}
	}
	public static void createMap(TabsEnum tab,Map<String, Object> map,List<DataItem> items){
		String key=null;
		if(items!=null)
		for(DataItem item:items){
			key=tab.getType()+"."+item.getRefData().getCodeStr();
//			logger.debug("createMap: key={}",key);
			map.put(key, item.getMontant());
		}
	}
	public static String createSommeGlobal(String colonne,String starPos, int size){
		String result="SOMME(DECALER(INDIRECT(\""+colonne+""+starPos+"\");0;0;"+size+";1))";
		return result;

	}
	public static void globalData(Map<String, Object> map,GenericBean bean) {
		try {
		map.put("exercice",bean.getExercice());
		map.put("budget", bean.getCodeBudget());
		map.put("exerciceAE",bean.getExercice());
		map.put("nomConfig", bean.getNomConfig());
		} catch (MissingConfiguration e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	public static void createTab8Map(Map<String, Object> map,Tab8 tab8) {
		map.put("recet.posit.a", tab8.getFinanDebutExec().getN().getMontant());
		map.put("perso.ae.cp", tab8.getPersonnelAeCp().getN().getMontant());
		map.put("fonc.ae", tab8.getFonctAE().getN().getMontant());
		map.put("fonc.cp", tab8.getFonctCP().getN().getMontant());
		map.put("interv.ae", tab8.getInterAE().getN().getMontant());
		map.put("interv.cp", tab8.getInterCP().getN().getMontant());
		map.put("invest.ae", tab8.getInvesAE().getN().getMontant());
		map.put("invest.cp", tab8.getInvesCP().getN().getMontant());
		map.put("autofinanc.op.flech", tab8.getAutofinancement().getN().getMontant());
		map.put("decaiss.rec.flech", tab8.getTresorerie().getN().getMontant());
		
	}

	public static void createRapprochMap(Map<String, Object> map, Rapprochement rapprochement) {

		map.put("glob.pers", rapprochement.getRecGlobalPers());
		map.put("flech.pers", rapprochement.getRecFlechePers());
		map.put("glob.fonct", rapprochement.getRecGlobalFonc());
		map.put("flech.fonct", rapprochement.getRecFlecheFonc());
		map.put("glob.interv", rapprochement.getRecGlobalInter());
		map.put("flech.interv", rapprochement.getRecFlecheInter());
		map.put("glob.invest", rapprochement.getRecGlobalInves());
		map.put("flech.invest", rapprochement.getRecFlecheInves());

		map.put("subv.charg.serv.public", rapprochement.getSubChrgSrvc());
		map.put("autre.financ.etat", rapprochement.getAutrFinancEtat());
		map.put("fiscalite.affect", rapprochement.getFiscAffecte());
		map.put("autre.financ.public", rapprochement.getAutrFinancPub());
		map.put("recet.propr", rapprochement.getRecetPropre());
		map.put("financ.etat.flech", rapprochement.getFinancEtatFleche());
		map.put("autre.financ.flech", rapprochement.getAutreFinancPubFleche());
		map.put("recet.propre.fleches", rapprochement.getRecetPropreFleche());

	}
	public static void createTabContrImpSF(Map<String, Object> map, CompteFinancierBean cfBean) {
		
		List<Map<String, Object>> imputations = new ArrayList<Map<String, Object>>();
		Map<String, Object> itemImput = null;
		List<VentilationImput> data = cfBean.getCfVentilationList() ;
		

		if (data != null) {
			for (VentilationImput element : data) {
				itemImput = new CustomHashMap<String, Object>();
				itemImput.put("compte", element.getAccount().getCode());
				itemImput.put("designation", element.getAccount().getDesignation());
				itemImput.put("amount1", element.getAmount1());
				itemImput.put("amount2", element.getAmount2());
				itemImput.put("amount3", element.getAmount3());
				itemImput.put("amount4", element.getAmount4());
				imputations.add(itemImput);
				
	}
			map.put("imput", imputations);
}
}
	public static void createTab10Map(Map<String, Object> map,Tab10 tab10) {
		map.put("niv.init.rap", tab10.getNivInitResteApayer().getMontant());
		map.put("niv.init.froul", tab10.getNivInitFondRoulement().getMontant());
		map.put("niv.init.bfroul", tab10.getBesoinFondRoul().getMontant());
		map.put("niv.init.tresor", tab10.getNivInitTresorie().getMontant());
		map.put("niv.init.tflech", tab10.getNivInitTresorieFleche().getMontant());
		map.put("niv.init.tnonflech", tab10.getNivInitTresorieNonFlech().getMontant());
		
		map.put("variat.stock", tab10.getVariationStock().getMontant());
		map.put("charg.irrec", tab10.getChargeCreanceIrrecouvrable().getMontant());
		map.put("prod.gest.cour", tab10.getProduitDiversGestCourante().getMontant());
		
		map.put("ecartAnt", tab10.getEcartProduitAnterieure().getMontant());
		map.put("ecartEncours", tab10.getEcartProduitEncours().getMontant());
		map.put("ecartChargeAnt", tab10.getEcartChargeAnterieure().getMontant());
		map.put("ecartChargeEncours", tab10.getEcartChargeEncours().getMontant());
		map.put("variat.tres.fleche", tab10.getVariTresorFleche().getMontant());
		map.put("variat.tres.nfleche", tab10.getVariTresorNonFleche().getMontant());
		map.put("final.tres.fleche", tab10.getNivFinalTresFlech().getMontant());
		map.put("final.tres.nfleche", tab10.getNivFinalTresNonFlech().getMontant());
		

	

	}
	public static void createTSBCEMap(Map<String, Object> map, Tab10 tab10) {
		createMap(TabsEnum.TAB10, map, tab10.getItems());
		
	}
}
