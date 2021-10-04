package fr.symphonie.tools.gts.model;

public enum PeriodeEnum {

OUVERT(1,"OUVERT","Ouvert"),
CHARGE(2,"CHARGE","Chargé"),
//EN_COURS(3,"EN_COURS","En cours"),
TRAITE(4,"TRAITE","Traité");

	private int ordre;
	private String status;	
	private String libelle;
	
	private PeriodeEnum(int ordre,String etat, String libelle) {
		this.ordre=ordre;
		this.status = etat;
		this.libelle = libelle;
	}
	public String getStatus() {
		return status;
	}
	public String getLibelle() {
		return libelle;
	}
	
	public int getOrdre() {
		return ordre;
	}
	public static PeriodeEnum parse(String str){
		PeriodeEnum enumeration=null;
		if(str==null)return enumeration;
		for(PeriodeEnum item:PeriodeEnum.values()){
			if(item.getStatus().equals(str.trim())){
				enumeration=item;
				break;
			}			
		}	
		return enumeration;
	}
}
