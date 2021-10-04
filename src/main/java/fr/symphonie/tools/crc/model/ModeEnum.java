package fr.symphonie.tools.crc.model;

import fr.symphonie.tools.gts.model.PeriodeEnum;

public enum ModeEnum {
	RECETTE(1,"RECETTE","Recette"),
	DEPENSE(2,"DEPENSE","Dépense");
	
	private int ordre;
	private String mode;	
	private String libelle;
	
	private ModeEnum(int ordre,String mode, String libelle) {
		this.ordre=ordre;
		this.mode = mode;
		this.libelle = libelle;
	}
	public String getStatus() {
		return mode;
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
