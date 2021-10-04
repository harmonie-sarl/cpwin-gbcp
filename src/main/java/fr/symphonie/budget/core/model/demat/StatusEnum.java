package fr.symphonie.budget.core.model.demat;

public enum StatusEnum {
EN_COURS("Encours","En cours"),
VALIDE("Valide","Validé"),
TRAITE("Traite","Traité");
	private String status;
	private String libelle;
	private StatusEnum(String etat, String libelle) {
		this.status = etat;
		this.libelle = libelle;
	}
	public String getStatus() {
		return status;
	}
	public String getLibelle() {
		return libelle;
	}
	public static StatusEnum parse(String str){
		StatusEnum enumeration=null;
		if(str==null)return enumeration;
		for(StatusEnum item:StatusEnum.values()){
			if(item.getStatus().equals(str.trim())){
				enumeration=item;
				break;
			}			
		}	
		return enumeration;
	}
}
