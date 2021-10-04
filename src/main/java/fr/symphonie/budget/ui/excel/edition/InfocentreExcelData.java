package fr.symphonie.budget.ui.excel.edition;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fr.symphonie.budget.core.model.edition.Edition;
import fr.symphonie.budget.core.model.pluri.BalanceItem;
import fr.symphonie.budget.ui.beans.edition.DematBean;
import fr.symphonie.budget.ui.doc.CustomHashMap;
import fr.symphonie.common.util.BudgetHelper;

public class InfocentreExcelData extends EditionExcelDocType {
	private DematBean getDematObject(){
		return BudgetHelper.getDematBean();
	}
	@Override
	public Edition getDataObject(){
		
		return getDematObject().getEdition();
	}

	@Override
	public Map<String, Object> getSpecificValues() {
		
		Edition edition = getDataObject();

		Map<String, Object> map = new HashMap<String, Object>();
		EditionExcelHelper.globalData(map,getDematBean());
		EditionExcelHelper.createTab2Map(map, edition.getTab2());
		EditionExcelHelper.createTab3Map(map, edition.getTab3());
		EditionExcelHelper.createTab4Map(map, edition.getTab4());
		
		EditionExcelHelper.createTab6(map, edition);
	
		EditionExcelHelper.createABEMap(map, edition.getAbe());
		EditionExcelHelper.createEfeMap(map, edition.getEfe());
		EditionExcelHelper.createTab7Map(map, edition.getPlanTresorerie());
		createBalanceMap(map, getDematObject().getBalance());
		if(getDematBean().isAnnuelDef()){
		EditionExcelHelper.createTSBCEMap(map, edition.getTab10());
		}
		return map;
	}

	private void createBalanceMap(Map<String, Object> map, List<BalanceItem> data) {

		List<Map<String, Object>> balance = new ArrayList<Map<String, Object>>();
		Map<String, Object> item = null;

		if (data != null) {
			for (BalanceItem element : data) {
				item = new CustomHashMap<String, Object>();
				item.put("code", element.getCompte().getCode());
				item.put("designation", element.getCompte().getDesignation());
				item.put("dbentree", element.getDebit().getEntree());
				item.put("dbmontant", element.getDebit().getMontant());
				item.put("dbtotal", element.getDebit().getTotale());
				item.put("crdentree", element.getCredit().getEntree());
				item.put("crdmontant", element.getCredit().getMontant());
				item.put("crdtotal", element.getCredit().getTotale());
				item.put("solddebit", element.getSolde().getDebit());
				item.put("soldcredit", element.getSolde().getCredit());
				balance.add(item);
			}
			map.put("balance", balance);

		}

	}
	@Override
	public String getFileNamePrefix(){
		
		String codeExport=getDematObject().getCodeExport();
		return super.getFileNamePrefix()+"_"+codeExport+"_";
	}
	
	public DematBean getDematBean() {
		DematBean bean= BudgetHelper.getDematBean();
		return bean;
	}

}
