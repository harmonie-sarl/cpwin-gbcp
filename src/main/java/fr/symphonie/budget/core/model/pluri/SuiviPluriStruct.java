package fr.symphonie.budget.core.model.pluri;

import javax.persistence.Transient;

public class SuiviPluriStruct extends  SuiviAeCpStruct{

	
	public SuiviPluriStruct() {
		super();
   }
	
	@Transient
	private String codeTiers;
	@Transient
	private String nomTiers;
	@Transient
	private int numEj;
	@Transient
	private String objet;
	@Transient
	private double mntTotal;
	@Transient
	private double planCpCourant;
	@Transient
	private double planCpSup;
	public String getObjet() {
		return objet;
	}
	public void setObjet(String objet) {
		this.objet = objet;
	}
	public double getMntTotal() {
		return mntTotal;
	}
	public void setMntTotal(double mntTotal) {
		this.mntTotal = mntTotal;
	}
	public double getPlanCpCourant() {
		return planCpCourant;
	}
	public void setPlanCpCourant(double planCpCourant) {
		this.planCpCourant = planCpCourant;
	}
	public double getPlanCpSup() {
		return planCpSup;
	}
	public void setPlanCpSup(double planCpSup) {
		this.planCpSup = planCpSup;
	}
	public int getNumEj() {
		return numEj;
	}
	public void setNumEj(int numEj) {
		this.numEj = numEj;
	}
	public String getCodeTiers() {
		return codeTiers;
	}
	public void setCodeTiers(String codeTiers) {
		this.codeTiers = codeTiers;
	}
	public String getNomTiers() {
		return nomTiers;
	}
	public void setNomTiers(String nomTiers) {
		this.nomTiers = nomTiers;
	}
	
}
