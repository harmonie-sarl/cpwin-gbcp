package fr.symphonie.budget.core.model.pluri;

import javax.persistence.Transient;

public class suiviCpStruct extends CpGestionnaire {
	
	
	
	@Transient
	private String codeDest;
	@Transient
	private String codeNature;
	@Transient
	private String codeService;
	@Transient
	private String codeProg;
	@Transient
	private String ej;
	@Transient
	private String tiers;
	@Transient
	private String ei;
	@Transient
	private double dpSaisie;
	@Transient
	private double dpEmise;
	@Transient
	private double dpPec;
	@Transient
	private double ca;
	@Transient
	private double rap;	
	@Transient
	private String nomProg;
	
	public String getCodeDest() {
		return codeDest;
	}
	public void setCodeDest(String codeDest) {
		this.codeDest = codeDest;
	}
	public String getCodeNature() {
		return codeNature;
	}
	public void setCodeNature(String codeNature) {
		this.codeNature = codeNature;
	}
	public String getCodeService() {
		return codeService;
	}
	public void setCodeService(String codeService) {
		this.codeService = codeService;
	}
	public String getCodeProg() {
		return codeProg;
	}
	public void setCodeProg(String codeProg) {
		this.codeProg = codeProg;
	}
	public String getEj() {
		return ej;
	}
	public void setEj(String ej) {
		this.ej = ej;
	}
	public String getTiers() {
		return tiers;
	}
	public void setTiers(String tiers) {
		this.tiers = tiers;
	}
	public String getEi() {
		return ei;
	}
	public void setEi(String ei) {
		this.ei = ei;
	}
	public double getDpSaisie() {
		return dpSaisie;
	}
	public void setDpSaisie(double dpSaisie) {
		this.dpSaisie = dpSaisie;
	}
	public double getDpEmise() {
		return dpEmise;
	}
	public void setDpEmise(double dpEmise) {
		this.dpEmise = dpEmise;
	}
	public double getDpPec() {
		return dpPec;
	}
	public void setDpPec(double dpPec) {
		this.dpPec = dpPec;
	}
	public double getCa() {
		return ca;
	}
	public void setCa(double ca) {
		this.ca = ca;
	}
	public double getRap() {
		return rap;
	}
	public void setRap(double rap) {
		this.rap = rap;
	}
	
	public String getNomProg() {
		return nomProg;
	}
	public void setNomProg(String nomProg) {
		this.nomProg = nomProg;
	}
	

}
