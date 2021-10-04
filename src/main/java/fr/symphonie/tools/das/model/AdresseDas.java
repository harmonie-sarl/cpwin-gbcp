package fr.symphonie.tools.das.model;

public class AdresseDas {
	//Complément d'adresse
	private String complement;
	//Adresse voie
	private VoieDas voie;
	//Adresse commune
	private String codeINSEE;
	private String commune;
	//Adresse bureau distributeur
	private String codePostale;
	private String bureau;
	
	
	public AdresseDas() {
		super();
		this.voie=new VoieDas();
	}
	public VoieDas getVoie() {
		if(voie==null)voie=new VoieDas();
		return voie;
	}
	public void setVoie(VoieDas voie) {
		this.voie = voie;
	}
	public String getBureau() {
		return bureau;
	}
	public void setBureau(String bureau) {
		this.bureau = bureau;
	}
	public String getComplement() {
		return complement;
	}
	public void setComplement(String complement) {
		this.complement = complement;
	}
	public String getCodePostale() {
		return codePostale;
	}
	public void setCodePostale(String codePostale) {
		this.codePostale = codePostale;
	}
	public String getCodeINSEE() {
		return codeINSEE;
	}
	public void setCodeINSEE(String codeINSEE) {
		this.codeINSEE = codeINSEE;
	}
	public String getCommune() {
		return commune;
	}
	public void setCommune(String commune) {
		this.commune = commune;
	}
	
	
	
}
