package fr.symphonie.budget.core.model.demat;

import java.util.Date;

public class GenericDemat implements Comparable<GenericDemat> {
	private int exercice;
	private String codeBudget;
	private String code;
	private Integer reference;
	private String libelle;
	private double montant;
	private String auteurMaj;
	private Date dateMaj;

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
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public Integer getReference() {
		return reference;
	}
	public void setReference(Integer reference) {
		this.reference = reference;
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
	public String getAuteurMaj() {
		return auteurMaj;
	}
	public void setAuteurMaj(String auteurMaj) {
		this.auteurMaj = auteurMaj;
	}
	public Date getDateMaj() {
		return dateMaj;
	}
	public void setDateMaj(Date dateMaj) {
		this.dateMaj = dateMaj;
	}
	@Override
	public int compareTo(GenericDemat o) {
		return this.getReference().compareTo(o.getReference());
	}

}
