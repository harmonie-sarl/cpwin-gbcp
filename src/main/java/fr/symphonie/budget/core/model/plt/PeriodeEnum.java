package fr.symphonie.budget.core.model.plt;

public enum PeriodeEnum {
	Bi(0,"00","Bi"),Janvier(1,"J1","Janvier"),Fevrier(2,"F1","Février"),Mars(3,"01","Mars"),Avril(4,"A2","Avril"),Mai(5,"M2","Mai"),Juin(6,"02","Juin"),
	Juillet(7,"J3","Juillet"),Aout(8,"A3","Août"),September(9,"03","Septembre"),Octobre(10,"O4","Octobre"),November(11,"N4","Novembre"),December(12,"04","Décembre"),
	Annuel_prov(13,"05","Annuel provisoire"),
	Annuel_def(14,"06","Annuel définitif","En attente de conciliation"),
	Compte_financier(14,"07","Compte financier");
	private Integer value;
	private String codeInfoCentre;
	private String libelle;
	private String commentaire;

	private PeriodeEnum(Integer value,String codeInfoCentre,String libelle) {
		this.value = value;
		this.libelle=libelle;
		this.codeInfoCentre=codeInfoCentre;
		this.commentaire=null;
	}
	private PeriodeEnum(Integer value,String codeInfoCentre,String libelle,String commentaire) {
		this(value,codeInfoCentre,libelle);
		this.commentaire=commentaire;
	}
	public static PeriodeEnum parse(Integer numero){
		if(numero!=null)
		for(PeriodeEnum p:PeriodeEnum.values()){
			if(numero.equals(p.getValue())) return p;
		}
		return null;
	}
	public static PeriodeEnum parseBycodeInfoCentre(String codeInfoCentre){
		if(codeInfoCentre==null)return null;
		for(PeriodeEnum p:PeriodeEnum.values()){
			if(codeInfoCentre.equals(p.getCodeInfoCentre())) return p;
		}
		return null;
	}
	public Integer getValue() {
		return value;
	}
	public String getLibelle() {
		return libelle;
	}
	public String getCodeInfoCentre() {
		return codeInfoCentre;
	}
	public String getCommentaire() {
		return commentaire;
	}
	public void setCommentaire(String commentaire) {
		this.commentaire = commentaire;
	}

}
