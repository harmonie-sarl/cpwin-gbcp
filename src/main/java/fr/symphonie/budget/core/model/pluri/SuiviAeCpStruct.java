package fr.symphonie.budget.core.model.pluri;

import javax.persistence.Transient;

public class SuiviAeCpStruct extends CpGestionnaire {
	
	
	
	@Transient
	private String codeDest;
	@Transient
	private String codeNature;
	@Transient
	private String codeService;
	@Transient
	private String codeProg;
	@Transient
	private String flechGlob;
	@Transient
	private double rapInitiale;
	@Transient
	private double compl;
	@Transient
	private double dpAnt;
	@Transient
	private String tiers;
	@Transient
	private double rapAnt;
	@Transient
	private double co;
	@Transient
	private double ej;
	@Transient
	private double dpEx;
	@Transient
	private double rapEx;
	@Transient
	private double dpTot;
	@Transient
	private double dr;
	@Transient
	private double dpNet;
	@Transient
	private double rapAct;
	@Transient
	private double ca;
	@Transient
	private double dispEj;	
	@Transient
	private double rapDiff;	
	@Transient
	private double CP;	
	@Transient
	private String dispo;	
	@Transient
	private double besoin;	
	
	
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


	public double getRapInitiale() {
		return rapInitiale;
	}


	public void setRapInitiale(double rapInitiale) {
		this.rapInitiale = rapInitiale;
	}


	public double getCompl() {
		return compl;
	}


	public void setCompl(double compl) {
		this.compl = compl;
	}


	public double getDpAnt() {
		return dpAnt;
	}


	public void setDpAnt(double dpAnt) {
		this.dpAnt = dpAnt;
	}


	public double getRapAnt() {
		return rapAnt;
	}


	public void setRapAnt(double rapAnt) {
		this.rapAnt = rapAnt;
	}


	public double getCo() {
		return co;
	}


	public void setCo(double co) {
		this.co = co;
	}


	public double getEj() {
		return ej;
	}


	public void setEj(double ej) {
		this.ej = ej;
	}


	public double getDpEx() {
		return dpEx;
	}


	public void setDpEx(double dpEx) {
		this.dpEx = dpEx;
	}


	public double getRapEx() {
		return rapEx;
	}


	public void setRapEx(double rapEx) {
		this.rapEx = rapEx;
	}





	public double getDpTot() {
		return dpTot;
	}


	public void setDpTot(double dpTot) {
		this.dpTot = dpTot;
	}


	public double getDr() {
		return dr;
	}


	public void setDr(double dr) {
		this.dr = dr;
	}


	public double getDpNet() {
		return dpNet;
	}


	public void setDpNet(double dpNet) {
		this.dpNet = dpNet;
	}


	public double getCa() {
		return ca;
	}


	public void setCa(double ca) {
		this.ca = ca;
	}


	public double getDispEj() {
		return dispEj;
	}


	public void setDispEj(double dispEj) {
		this.dispEj = dispEj;
	}


	public double getRapDiff() {
		return rapDiff;
	}


	public void setRapDiff(double rapDiff) {
		this.rapDiff = rapDiff;
	}


	public double getCP() {
		return CP;
	}


	public void setCP(double cP) {
		CP = cP;
	}


	public String getDispo() {
		return dispo;
	}


	public void setDispo(String dispo) {
		this.dispo = dispo;
	}


	public double getBesoin() {
		return besoin;
	}


	public void setBesoin(double besoin) {
		this.besoin = besoin;
	}


	public String getNomProg() {
		return nomProg;
	}


	public void setNomProg(String nomProg) {
		this.nomProg = nomProg;
	}


	public String getTiers() {
		return tiers;
	}


	public void setTiers(String tiers) {
		this.tiers = tiers;
	}
	public double getRapAct() {
		return rapAct;
	}


	public void setRapAct(double rapAct) {
		this.rapAct = rapAct;
	}

	public String getFlechGlob() {
		return flechGlob;
	}


	public void setFlechGlob(String flechGlob) {
		this.flechGlob = flechGlob;
	}


	
	

}
