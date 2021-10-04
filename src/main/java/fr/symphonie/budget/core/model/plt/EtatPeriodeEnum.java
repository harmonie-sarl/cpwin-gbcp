package fr.symphonie.budget.core.model.plt;

public enum EtatPeriodeEnum {
Previsionnel("P","Pr�visionnel","En attente d'ajustement"),
Valide("V","Pr�visionnel valid�","En attente de simulation"),
Cloture("C","Cl�tur�e",null);

private String value;
private String libelle;
private String commentaire;


private EtatPeriodeEnum(String value,String libelle,String commentaire) {
	this.value = value;
	this.libelle=libelle;
	this.commentaire=commentaire;
}


public String getValue() {
	return value;
}

public String getLibelle() {
	return libelle;
}


public String getCommentaire() {
	return commentaire;
}


public static EtatPeriodeEnum parse(String str){
	
	for(EtatPeriodeEnum v: EtatPeriodeEnum.values()){
		if(v.getValue().equals(str))
			return v;
	}
	return null;
}
}
