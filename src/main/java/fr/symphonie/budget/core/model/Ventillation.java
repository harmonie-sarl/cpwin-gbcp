package fr.symphonie.budget.core.model;

import fr.symphonie.util.model.SimpleEntity;

public class Ventillation {
	private SimpleEntity dest;
	private SimpleEntity nature;
	private double montant;
	public Ventillation(String codeDest,String libDest,String codeNature,double vent){
		this.dest=new SimpleEntity(codeDest,libDest);
		this.nature=new SimpleEntity(codeNature, "");
		this.montant=vent;
	}
	public SimpleEntity getDest() {
		return dest;
	}
	public void setDest(SimpleEntity dest) {
		this.dest = dest;
	}
	public SimpleEntity getNature() {
		return nature;
	}
	public void setNature(SimpleEntity nature) {
		this.nature = nature;
	}
	public double getMontant() {
		return montant;
	}
	public void setMontant(double montant) {
		this.montant = montant;
	}
	
	

}
