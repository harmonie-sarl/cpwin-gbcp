package fr.symphonie.budget.ui.excel.edition;

import java.util.List;
import java.util.Map;

import fr.symphonie.budget.core.model.edition.Edition;
import fr.symphonie.budget.ui.excel.ExcelDocType;
import fr.symphonie.common.util.BudgetHelper;
import fr.symphonie.util.model.SimpleBean;

public class EditionExcelDocType  implements ExcelDocType {

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
		// TODO Auto-generated method stub
		return null;
	}
	public Edition getDataObject(){
		return BudgetHelper.getEditionBean().getEditionBi();
	}
	@Override
	public String getFileNamePrefix(){
		String prefix="";
		try{
		String nomConfig=getDataObject().getNomConfig();
		String exercice=""+getDataObject().getExercice();
		prefix= nomConfig+"_"+exercice+"_";
		}
		catch(Exception e){}
		return prefix;
	}

}
