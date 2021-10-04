package fr.symphonie.budget.core.model.pluri;

import javax.persistence.Transient;

import fr.symphonie.util.model.Trace;

public class ModificationCpLigne {
	private Integer exercice;
	private String type;
	private Integer numero;
	private Integer noLbgCp;
	private String codeBudget;
	private double montantBR;
	private Trace trace;
	private BudgModification budgModification;
	@Transient
	private String libObjetBr;
	
	
	public ModificationCpLigne() {
		super();
	}
	
	public ModificationCpLigne(BudgModification budgModification) {
		super();
		this.budgModification = budgModification;
		if(budgModification!=null){
			this.exercice=budgModification.getExercice();
			this.type=budgModification.getType().getCode();
			this.numero=budgModification.getNumero();
			this.codeBudget=budgModification.getCodeBudget();
		}
	}
	
	public BudgModification getBudgModification() {
		return budgModification;
	}
	public void setBudgModification(BudgModification budgModification) {
		this.budgModification = budgModification;
	}
	
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
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public Integer getNumero() {
		return numero;
	}
	public void setNumero(Integer numero) {
		this.numero = numero;
	}
	public double getMontantBR() {
		return montantBR;
	}
	public void setMontantBR(double montantBR) {
		this.montantBR = montantBR;
	}
	public Integer getNoLbgCp() {
		return noLbgCp;
	}
	public void setNoLbgCp(Integer noLbgCp) {
		this.noLbgCp = noLbgCp;
	}
	public Trace getTrace() {
		return trace;
	}
	public void setTrace(Trace trace) {
		this.trace = trace;
	}

	@Override
	public String toString() {
		return "ModificationCpLigne [exercice=" + exercice + ", type=" + type + ", numero=" + numero + ", noLbgCp="
				+ noLbgCp + "]";
	}
	public String getLibObjetBr() {
		return libObjetBr;
	}

	public void setLibObjetBr(String libObjetBr) {
		this.libObjetBr = libObjetBr;
	}

	

}
