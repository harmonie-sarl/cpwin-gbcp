package fr.symphonie.budget.core.model.pluri;


public enum ModifEnum {
DemandeModif("DM","Demande modification"),DEPLOIEMENT("VIRG","Déploiement budget");
private String code;
private String libelle;
private ModifEnum(String code, String libelle) {
	this.code = code;
	this.libelle = libelle;
}
public String getCode() {
	return code;
}
public String getLibelle() {
	return libelle;
}
public static ModifEnum parse(String str){
	ModifEnum enumeration=null;
	if(str==null)return enumeration;
	for(ModifEnum item:ModifEnum.values()){
		if(item.getCode()==null)continue;
		if(item.getCode().equals(str.trim())){
			enumeration=item;
			break;
		}			
	}	
	return enumeration;
}
}
