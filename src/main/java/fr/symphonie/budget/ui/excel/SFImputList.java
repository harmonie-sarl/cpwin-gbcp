package fr.symphonie.budget.ui.excel;

import java.util.HashMap;
import java.util.Map;

import fr.symphonie.budget.ui.excel.edition.EditionExcelHelper;

public class SFImputList extends CFSaisiList{


	public SFImputList() {
		super();
		
	}
	
	@Override
	public Map<String, Object> getSpecificValues() {
		Map<String, Object> map = new HashMap<String, Object>();
		EditionExcelHelper.globalData(map,getCfBean());
		EditionExcelHelper.createTabContrImpSF(map,getCfBean());
	
		
		return map;

}
}
