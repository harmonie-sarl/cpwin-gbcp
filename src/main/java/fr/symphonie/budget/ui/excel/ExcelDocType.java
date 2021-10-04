package fr.symphonie.budget.ui.excel;

import java.util.List;
import java.util.Map;

import fr.symphonie.util.model.SimpleBean;

public interface ExcelDocType {
	String[]getColumnsKey();
	 String getTitle() ;
	 List<SimpleBean> getCriteres();
	 Map<String,String> getSpecificColumns();
	 Map<String,Object> getSpecificValues();
	 String getFileNamePrefix();
}
