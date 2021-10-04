package fr.symphonie.budget.ui.excel;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fr.symphonie.budget.core.model.plt.DetailLigneTresorerie;
import fr.symphonie.budget.core.model.plt.PlanTresorerie;
import fr.symphonie.budget.ui.excel.edition.EditionExcelHelper;
import fr.symphonie.common.util.BudgetHelper;
import fr.symphonie.doc.MSGenerator;
import fr.symphonie.util.model.SimpleBean;

public class AjustList implements ExcelDocType {


	@Override
	public String[] getColumnsKey() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getTitle() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<SimpleBean> getCriteres() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<String, String> getSpecificColumns() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<String, Object> getSpecificValues() {
		Map<String, Object> map = new HashMap<String, Object>();
		PlanTresorerie<DetailLigneTresorerie> planTresorerie=BudgetHelper.getPlanTresorerieBean().getPlan();
		if(planTresorerie!=null)
		createTab7Map(map,planTresorerie);
		
		return map;
	}
	private static void createTab7Map(Map<String, Object> map,PlanTresorerie<DetailLigneTresorerie> plan) {
		EditionExcelHelper.createSoldinitial(map, plan);
		EditionExcelHelper.createEncaissMap(map,plan);
		EditionExcelHelper.createDecaissMap(map,plan);
		
	}
	@Override
	public String getFileNamePrefix() {
		return MSGenerator.GENERATED;
	}
}
