package fr.symphonie.tools.das.model;

import fr.symphonie.common.util.BudgetHelper;
import fr.symphonie.impor.AttributImport;
import fr.symphonie.impor.ConfigImport;
import fr.symphonie.impor.Importable;
import fr.symphonie.util.model.SimpleEntity;

public class TiersImportedData extends SimpleEntity implements Importable{

	@Override
	public void setValue(AttributImport attribut, Object value) {
		String strValue=null;
		if(value instanceof String)
			strValue=(value!=null)?((String) value).trim():null;
			int  index=	attribut.getIndex();	
		switch(index){
		case 0:
			setCode(strValue);
			break;
		case 1:
			setDesignation(strValue);
			break;
		}
		
	}


	@Override
	public ConfigImport getConfigImport() {
		if(configImport==null) configImport=BudgetHelper.loadConfigImport("das.tiers.import.config");
		return configImport;
	}
	private static ConfigImport configImport=null;
	@Override
	public void setRowNum(Integer rowNum) {
		
	}
	@Override
	public Integer getRowNum() {
		return null;
	}


}
