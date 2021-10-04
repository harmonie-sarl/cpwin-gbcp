package fr.symphonie.tools.recette.model;

import java.math.BigDecimal;

import fr.symphonie.common.util.BudgetHelper;
import fr.symphonie.cpwin.model.Tiers;
import fr.symphonie.impor.AttributImport;
import fr.symphonie.impor.ConfigImport;
import fr.symphonie.impor.Importable;

public class ImportedData implements Importable {

	private String codeTiers;
	private BigDecimal montant;
	private float tauxTva;
	private static ConfigImport configImport=null;
	private Tiers tiers;
	@Override
	public void setValue(AttributImport attribut, Object value) {
		String strValue=null;
		BigDecimal bdValue=null;
		if(value instanceof String)
			strValue=(value!=null)?((String) value).trim():null;
			else if(value instanceof  BigDecimal)
				bdValue=(value!=null)?(BigDecimal) value:null;
		String attributName=attribut.getName();
		switch (attributName){

		case "CodeTiers":			 
			setCodeTiers(strValue);
			break;
		case "Montant":
			setMontant(bdValue);
			break;
		case "TauxTva":
			setTauxTva(bdValue.floatValue());
			break;

		}
		
	}

	@Override
	public ConfigImport getConfigImport() {
		if(configImport==null) configImport=BudgetHelper.loadConfigImport("recette.import.config");
		return configImport;
	}

	public String getCodeTiers() {
		return codeTiers;
	}

	public void setCodeTiers(String codeTiers) {
		this.codeTiers = codeTiers;
	}

	public BigDecimal getMontant() {
		return montant;
	}

	public void setMontant(BigDecimal montant) {
		this.montant = montant;
	}

	public float getTauxTva() {
		return tauxTva;
	}

	public void setTauxTva(float tauxTva) {
		this.tauxTva = tauxTva;
	}

	public Tiers getTiers() {
		return tiers;
	}

	public void setTiers(Tiers tiers) {
		this.tiers = tiers;
	}

	@Override
	public void setRowNum(Integer rowNum) {
		
	}
	@Override
	public Integer getRowNum() {
		return null;
	}

}
