package fr.symphonie.budget.ui.excel.edition;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.symphonie.budget.core.model.edition.Edition;
import fr.symphonie.budget.core.model.edition.Tab5;
import fr.symphonie.budget.core.model.edition.util.Operation;
import fr.symphonie.budget.ui.doc.CustomHashMap;

public class BiBrExcelData  extends EditionExcelDocType {
	
	private static final Logger logger = LoggerFactory.getLogger(BiBrExcelData.class);

	
	@Override
	public Map<String, Object> getSpecificValues() {
		Map<String, Object> map = new HashMap<String, Object>();
		
		Edition edition=getDataObject();

		Tab5 tab5= edition.getTab5();
		List<Operation> data = tab5.getOperationsBudg();
		
		List<Operation> data1 = tab5.getOperationsnoBudg();
		globalData(map,edition);
		EditionExcelHelper.createTab2Map(map, edition.getTab2());
	    EditionExcelHelper.createTab3Map(map, edition.getTab3());
	    EditionExcelHelper.createTab4Map(map,edition.getTab4());
	    createTab5Map(map,data,"comptebudg");
		createTab5Map(map,data1,"comptenobudg");

	    EditionExcelHelper.createTab6(map, edition);
		
		EditionExcelHelper.createTab7Map(map,edition.getPlanTresorerie());

		EditionExcelHelper.createTab8Map(map,edition.getTab8());
		EditionExcelHelper.createTab10Map(map,edition.getTab10());
		return map;

	}


	private static void globalData(Map<String, Object> map,Edition e) {
		map.put("exercice", e.getExercice());
		map.put("budget", e.getCodeBudget());
		map.put("exerciceAE", e.getExercice());
		map.put("nomConfig", e.getNomConfig());
		
	}

	

	private static void createTab5Map(Map<String, Object> map,  List<Operation> list,String nomExcelCell) {

		Map<String, Object> itemNiveau = null;
		List<Map<String, Object>> niveau = new ArrayList<Map<String, Object>>();

		if (list != null) {
			for (Operation element : list) {
				itemNiveau = new CustomHashMap<String, Object>();
				itemNiveau.put("operation", element.getDesignation());
				itemNiveau.put("compte", element.getCompte());
				itemNiveau.put("libelle", element.getLibelle());
				itemNiveau.put("debitc1", element.getDebit());
				itemNiveau.put("creditc2", element.getCredit());
				niveau.add(itemNiveau);

				
			}
			map.put(nomExcelCell, niveau);
	}
}
//	private static void createTab10Map(Map<String, Object> map,Tab10 tab10) {
//		map.put("niv.init.rap", tab10.getNivInitResteApayer().getMontant());
//		map.put("niv.init.froul", tab10.getNivInitFondRoulement().getMontant());
//		map.put("niv.init.bfroul", tab10.getBesoinFondRoul().getMontant());
//		map.put("niv.init.tresor", tab10.getNivInitTresorie().getMontant());
//		map.put("niv.init.tflech", tab10.getNivInitTresorieFleche().getMontant());
//		map.put("niv.init.tnonflech", tab10.getNivInitTresorieFleche().getMontant());
//		
//		map.put("variat.stock", tab10.getVariationStock().getMontant());
//		map.put("charg.irrec", tab10.getChargeCreanceIrrecouvrable().getMontant());
//		map.put("prod.gest.cour", tab10.getProduitDiversGestCourante().getMontant());
//		
//		map.put("ecartAnt", tab10.getEcartProduitAnterieure().getMontant());
//		map.put("ecartEncours", tab10.getEcartProduitEncours().getMontant());
//		map.put("ecartChargeAnt", tab10.getEcartChargeAnterieure().getMontant());
//		map.put("ecartChargeEncours", tab10.getEcartChargeEncours().getMontant());
//		map.put("variat.tres.fleche", tab10.getVariTresorFleche().getMontant());
//		map.put("variat.tres.nfleche", tab10.getVariTresorNonFleche().getMontant());
//		map.put("final.tres.fleche", tab10.getNivFinalTresFlech().getMontant());
//		map.put("final.tres.nfleche", tab10.getNivFinalTresNonFlech().getMontant());
//		
//
//	
//
//	}
	@Override
	public String getFileNamePrefix(){
		
		Integer niveau=getDataObject().getNiveau();
		logger.info("getFileNamePrefix: niveau "+niveau);
	
		String bibr=((niveau==null)||(niveau==0))?"BI":"BR_"+niveau;
		logger.info("getFileNamePrefix: bibr "+bibr);
		return super.getFileNamePrefix()+"_"+bibr+"_";
	}
	
	}
