package fr.symphonie.budget.core.model.cf;

import java.util.Date;

public class CfGenericDemat {
	private int exercice;
	private String codeBudget;
	private Integer numeroLigne;
	private String libelle;
	private double montant;
	private String auteurCrea;
	private Date dateCrea;
	
	public CfGenericDemat() {
		super();
	}
	public int getExercice() {
		return exercice;
	}
	public void setExercice(int exercice) {
		this.exercice = exercice;
	}
	public String getCodeBudget() {
		return codeBudget;
	}
	public void setCodeBudget(String codeBudget) {
		this.codeBudget = codeBudget;
	}
	public Integer getNumeroLigne() {
		return numeroLigne;
	}
	public void setNumeroLigne(Integer numeroLigne) {
		this.numeroLigne = numeroLigne;
	}
	public String getLibelle() {
		return libelle;
	}
	public void setLibelle(String libelle) {
		this.libelle = libelle;
	}
	public double getMontant() {
		return montant;
	}
	public void setMontant(double montant) {
		this.montant = montant;
	}
	public String getAuteurCrea() {
		return auteurCrea;
	}
	public void setAuteurCrea(String auteurCrea) {
		this.auteurCrea = auteurCrea;
	}
	public Date getDateCrea() {
		return dateCrea;
	}
	public void setDateCrea(Date dateCrea) {
		this.dateCrea = dateCrea;
	}

	
}
