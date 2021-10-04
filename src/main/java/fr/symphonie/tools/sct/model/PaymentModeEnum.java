package fr.symphonie.tools.sct.model;

public enum PaymentModeEnum {
	RBSF,	RBSI,	RBHSI;
	
	public String getValue() {
		return this.name();
	}
	public String getLabel() {
		if(this==RBHSI) return "Paiements HORS  SEPA";		
		return this.name();
	}
}
