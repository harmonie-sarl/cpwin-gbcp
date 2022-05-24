package fr.symphonie.tools.lemans.bt.dto;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Transient;

import fr.symphonie.common.util.BudgetHelper;
import fr.symphonie.impor.AttributImport;
import fr.symphonie.impor.ConfigImport;
import fr.symphonie.impor.Importable;
import lombok.Data;
@Data
public class VenteItemDto implements Importable {

	private Integer numEncaiss;
	private String objet;
	private Integer numSpectacle;
	private Date dateRep;	
	private BigDecimal montant;

	@Override
	public void setValue(AttributImport attribut, Object value) {			
		BigDecimal bdValue=null;
		String strValue=null;
		
		Date date =null;
		if(value instanceof String)
			strValue=(value!=null)?((String) value).trim():null;
		else if(value instanceof  BigDecimal) {
			bdValue=(value!=null)?(BigDecimal) value:null;
		}
		else if(value instanceof Date)
			date 	= (value!=null)?(Date) value:null;
		
		String attributName=attribut.getName();
		switch (attributName){
		case "numEncaiss":
			setNumEncaiss(bdValue.intValue());
			break;
		case "objet":
			setObjet(strValue);
			break;
		case "dateRep":	
			setDateRep(date);
			break;	
		case "numSpectacle":
			setNumSpectacle(bdValue.intValue());;
			break;
		case "montant":
			setMontant(bdValue);
			break;

		}
		
	}
	@Override
	public ConfigImport getConfigImport() {
		if(configImport==null) configImport=BudgetHelper.loadConfigImport("lemans.bt.modalites.import.config");
		return configImport;
	}
	private static ConfigImport configImport=null;
	
	@Override
	public void setRowNum(Integer rowNum) {
		this.rowNum=rowNum;
		
	}
	@Override
	public Integer getRowNum() {
		return getConfigImport().getStartedRowIndex()+rowNum;
	}
	@Transient
	private Integer rowNum;

}
