package fr.symphonie.budget.core.model.pluri;

public enum EtatEnum {
OUVERT("Ouvert","Ouvert"),
VALIDE("Valide","Validé"),
ORDO("ORDO","Ordo"),
ENCOURS("ENCOURS","En cours");
private String etat;
private String libelle;
private EtatEnum(String etat, String libelle) {
	this.etat = etat;
	this.libelle = libelle;
}
public String getEtat() {
	return etat;
}
public String getLibelle() {
	return libelle;
}
public static EtatEnum parse(String str){
	EtatEnum enumeration=null;
	if(str==null)return enumeration;
	for(EtatEnum item:EtatEnum.values()){
		if(item.getEtat().equalsIgnoreCase(str.trim())){
			enumeration=item;
			break;
		}			
	}	
	return enumeration;
}
}
