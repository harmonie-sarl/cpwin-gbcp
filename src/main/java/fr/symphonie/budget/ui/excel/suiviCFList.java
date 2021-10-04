package fr.symphonie.budget.ui.excel;

import java.util.HashMap;
import java.util.Map;

import fr.symphonie.budget.ui.beans.edition.CompteFinancierBean;
import fr.symphonie.budget.ui.excel.edition.EditionExcelHelper;

public class suiviCFList extends CFSaisiList {
	
	public suiviCFList() {
		super();
		
	}
	@Override
	public Map<String, Object> getSpecificValues() {
		Map<String, Object> map = new HashMap<String, Object>();
		
		EditionExcelHelper.globalData(map,getCfBean());
		EditionExcelHelper.createABEMap(map, getCfBean().getCf().getAbe());
	    EditionExcelHelper.createTab3Map(map, getCfBean().getCf().getTab3());
	    EditionExcelHelper.createEfeMap(map, getCfBean().getCf().getEfe());
	    EditionExcelHelper.createTab7Map(map,getCfBean().getCf().getPlanTresorerie());
		nivDestOrigData(map,getCfBean());
		EditionExcelHelper.createRapprochMap(map,getCfBean().getCfRapproche());
	    EditionExcelHelper.createTab6(map, getCfBean().getCf());
		EditionExcelHelper.createTab8Map(map,getCfBean().getCf().getTab8());
		//EditionExcelHelper.createTabContrImpSF(map,getCfBean());
	
		
		return map;

}
	private void nivDestOrigData(Map<String, Object> map, CompteFinancierBean cfBean) {
		map.put("niv.distination",cfBean.getDepNivDest());
		map.put("niv.origine", cfBean.getRecNivOrig());
		
	}

}
