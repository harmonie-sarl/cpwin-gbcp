package fr.symphonie.budget.ui.excel;

import java.util.HashMap;
import java.util.Map;

import fr.symphonie.budget.core.model.plt.DetailLigneTresorerie;
import fr.symphonie.budget.core.model.plt.PlanTresorerie;
import fr.symphonie.budget.ui.beans.pluri.PlanTresorerieBean;
import fr.symphonie.budget.ui.excel.edition.EditionExcelDocType;
import fr.symphonie.budget.ui.excel.edition.EditionExcelHelper;
import fr.symphonie.common.util.BudgetHelper;

public class TresorerieExcelData extends EditionExcelDocType  {
	
	
	
	@Override
	public String getTitle() {
		if(getPlanObject().getPlan()!=null)
		return getPlanObject().getPlan().getTitle();
		return " ";
	}

	private PlanTresorerieBean getPlanObject(){
		return BudgetHelper.getPlanTresorerieBean();
	}
	
	@Override
	public Map<String, Object> getSpecificValues() {
		Map<String, Object> map = new HashMap<String, Object>();
		PlanTresorerie<DetailLigneTresorerie> planTresorerie=getPlanObject().getPlan();

		if(planTresorerie!=null)
		EditionExcelHelper.createTab7Map(map, planTresorerie);
		
		return map;
	}

	public String getFileNamePrefix() {
		String prefix = "";
		try {
			Integer periode = getPlanObject().getPeriode();
			String nomConfig = getPlanObject().getNomConfig();
			String exercice = "" + getPlanObject().getExercice();
			prefix = nomConfig + "_" + exercice + "_" + periode + "_";

		} catch (Exception e) {
		}
		return prefix;

	}
}
