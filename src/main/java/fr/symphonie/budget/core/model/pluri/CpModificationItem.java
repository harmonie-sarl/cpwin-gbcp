package fr.symphonie.budget.core.model.pluri;

import javax.persistence.Transient;

public class CpModificationItem {
//	private int id;
	private Integer exercice;
	private String codeBudget;
	private Integer numero;
	private Integer noLbgCp;
	//private String gestionnaire;
	private double montant;
	private CpModification parent;
	@Transient
	private CpGestionnaire cpGest;
//	public CpModificationItem(String gestionnaire, double montant) {
//		this();
//		this.gestionnaire = gestionnaire;
//		this.montant = montant;
//	}
	
	public CpModificationItem() {
		super();
	}

//	public int getId() {
//		return id;
//	}
//	public void setId(int id) {
//		this.id = id;
//	}
	
	public CpModificationItem(CpModification modification) {
		this();
		setExercice(modification.getExercice());
		setCodeBudget(modification.getCodeBudget());
		setNumero(modification.getNumero());
		setParent(modification);
	}

//	public String getGestionnaire() {
//		return gestionnaire;
//	}
	public Integer getExercice() {
		return exercice;
	}

	public void setExercice(Integer exercice) {
		this.exercice = exercice;
	}

	public String getCodeBudget() {
		return codeBudget;
	}

	public void setCodeBudget(String codeBudget) {
		this.codeBudget = codeBudget;
	}

	public Integer getNumero() {
		return numero;
	}

	public void setNumero(Integer numero) {
		this.numero = numero;
	}

	public Integer getNoLbgCp() {
		return noLbgCp;
	}

	public void setNoLbgCp(Integer noLbgCp) {
		this.noLbgCp = noLbgCp;
	}

//	public void setGestionnaire(String gestionnaire) {
//		this.gestionnaire = gestionnaire;
//	}
	public double getMontant() {
		return montant;
	}
	public void setMontant(double montant) {
		this.montant = montant;
	}

	public void setParent(CpModification parent) {
		this.parent = parent;
	}

	public CpModification getParent() {
		return parent;
	}

	public CpGestionnaire getCpGest() {
		return cpGest;
	}

	public void setCpGest(CpGestionnaire cpGest) {
		this.cpGest = cpGest;
	}
	public double getMontant(String natGrp){
		double result=0;
		if((natGrp!=null)&&(getCpGest()!=null)){
			result=(natGrp.equals(getCpGest().getGroupNat()))?getMontant():0;
		}
		return result;
	}
	
}
