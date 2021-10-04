package fr.symphonie.tools.meta4dai.model;

import java.math.BigDecimal;

import javax.persistence.Transient;

import fr.symphonie.common.util.BudgetHelper;
import fr.symphonie.impor.AttributImport;
import fr.symphonie.impor.ConfigImport;
import fr.symphonie.impor.Importable;

public class ImportedData implements Importable{
	
	private Integer exercice;
	private Integer period;
	private Integer numeroEI;
	private BigDecimal amount;
	public Integer getExercice() {
		return exercice;
	}
	public void setExercice(Integer exercice) {
		this.exercice = exercice;
	}
	public Integer getPeriod() {
		return period;
	}
	public void setPeriod(Integer period) {
		this.period = period;
	}
	public Integer getNumeroEI() {
		return numeroEI;
	}
	public void setNumeroEI(Integer numeroEI) {
		this.numeroEI = numeroEI;
	}
	public BigDecimal getAmount() {
		return amount;
	}
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
	@Override
	public void setValue(AttributImport attribut, Object value) {			
		BigDecimal bdValue=null;	
		if(value instanceof  BigDecimal) {
			bdValue=(value!=null)?(BigDecimal) value:null;
		}
		
		String attributName=attribut.getName();
		switch (attributName){

		case "EXERCICE":			 
			setExercice(bdValue.intValue());
			break;
		case "PERIODE":
				setPeriod(bdValue.intValue());
			break;
		case "NO_EI":
			setNumeroEI(bdValue.intValue());
			break;
		case "MONTANT":
			setAmount(bdValue);
			break;

		}
		
	}
	@Override
	public ConfigImport getConfigImport() {
		if(configImport==null) configImport=BudgetHelper.loadConfigImport("meta4dai.import.config");
		return configImport;
	}
	private static ConfigImport configImport=null;
	@Override
	public String toString() {
		return "[exercice/periode=" + exercice + "/" + period + ", no_EI=" + numeroEI + ", montant="
				+ amount + "]";
	}
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
