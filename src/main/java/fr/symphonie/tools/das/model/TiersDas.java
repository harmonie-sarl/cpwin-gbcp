package fr.symphonie.tools.das.model;

import fr.symphonie.util.model.Trace;

public class TiersDas {
	private String numero;
	private String type;
	private String rs;
	private String nom;
	private String prenom;
	private AdresseDas adresse;
	private String siret;
	private String profession;
	private Trace trace;

	public TiersDas() {
		super();
		this.adresse = new AdresseDas();
	}

	public TiersDas(String numero) {
		this();
		this.numero = numero;
	}

	public String getNumero() {
		return numero;
	}

	public void setNumero(String numero) {
		this.numero = numero;
	}

	public TypeDasEnum getType() {
		return TypeDasEnum.parse(type);
	}

	public void setType(TypeDasEnum type) {
		this.type = type.getValue();
	}

	public String getRs() {
		return rs;
	}

	public void setRs(String rs) {
		this.rs = rs;
	}

	public String getNom() {
		return nom;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}

	public String getPrenom() {
		return prenom;
	}

	public void setPrenom(String prenom) {
		this.prenom = prenom;
	}

	public AdresseDas getAdresse() {
		return adresse;
	}

	public void setAdresse(AdresseDas adresse) {
		this.adresse = adresse;
	}

	public String getSiret() {
		return siret;
	}

	public void setSiret(String siret) {
		this.siret = siret;
	}

	public String getProfession() {
		return profession;
	}

	public void setProfession(String profession) {
		this.profession = profession;
	}

	public Trace getTrace() {
		return trace;
	}

	public void setTrace(Trace trace) {
		this.trace = trace;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((numero == null) ? 0 : numero.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TiersDas other = (TiersDas) obj;
		if (numero == null) {
			if (other.numero != null)
				return false;
		} else if (!numero.equals(other.numero))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "TiersDas [numero=" + numero + "]";
	}

	public boolean isEntreprise() {
		if (getType() == null)
			return true;
		if (getType() == TypeDasEnum.RS)
			return true;
		return false;
	}

	public String getDescription() {
		String description = numero + "-";
		if (isEntreprise())
			description += rs;
		else
			description += nom + "-" + prenom;
		return description;
	}
}
