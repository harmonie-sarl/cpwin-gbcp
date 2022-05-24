package fr.symphonie.tools.lemans.bt.model;

public enum TypeTiersEnum {
	
	REGISSEUR(1,"Régisseur"),
	CLIENT(2,"Client");
	
	private int ordre;
	
	private String libelle;
	private TypeTiersEnum(int ordre, String libelle) {
		this.ordre=ordre;
		this.libelle = libelle;
	}
	
	public String getLibelle() {
		return libelle;
	}
	
	public int getOrdre() {
		return ordre;
	}
	
		
		
	
	

}
