package fr.symphonie.tools.das.model;

public class VoieDas {
	private String numero;
	private String type;
	private String natureEtNom;
	public String getNumero() {
		return numero;
	}
	public void setNumero(String numero) {
		this.numero = numero;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getNatureEtNom() {
		return natureEtNom;
	}
	public void setNatureEtNom(String natureEtNom) {
		this.natureEtNom = natureEtNom;
	}
	@Override
	public String toString() {
		return "VoieDas [numero=" + numero + ", type=" + type + ", natureEtNom=" + natureEtNom + "]";
	}
	
}
