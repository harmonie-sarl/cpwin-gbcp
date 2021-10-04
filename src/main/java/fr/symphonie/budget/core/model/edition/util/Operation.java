package fr.symphonie.budget.core.model.edition.util;

import fr.symphonie.util.model.Trace;

public class Operation {
private int exercice;
private String codeBudget;
private Integer noMouvement;
private String code;
private String designation;
private boolean type;

private String compte;
private String libelle;
private Double debit;
private Double credit;
private Trace trace;

public Operation() {
	super();
}

public Operation(int exercice, String codeBudget,Integer noMouvement,boolean type, String code, String designation) {
	this();
	this.exercice = exercice;
	this.codeBudget = codeBudget;
	this.type=type;
	this.code = code;
	this.designation = designation;
	this.noMouvement=noMouvement;
}

public String getDesignation() {
	return designation;
}
public void setDesignation(String designation) {
	this.designation = designation;
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

public String getCode() {
	return code;
}

public void setCode(String code) {
	this.code = code;
}

public String getCompte() {
	return compte;
}

public void setCompte(String compte) {
	this.compte = compte;
}

public String getLibelle() {
	return libelle;
}

public void setLibelle(String libelle) {
	this.libelle = libelle;
}

public Double getDebit() {
	return debit;
}

public void setDebit(Double debit) {
	this.debit = debit;
}

public Double getCredit() {
	return credit;
}

public void setCredit(Double credit) {
	this.credit = credit;
}

public Trace getTrace() {
	return trace;
}

public void setTrace(Trace trace) {
	this.trace = trace;
}
public double getCreditDouble() {
	if(credit==null) return 0d;
	return credit.doubleValue();
}
public double getDebitDouble() {
	if(debit==null) return 0d;
	return debit.doubleValue();
}

public Integer getNoMouvement() {
	return noMouvement;
}

public void setNoMouvement(Integer noMouvement) {
	this.noMouvement = noMouvement;
}

public boolean isType() {
	return type;
}

public void setType(boolean type) {
	this.type = type;
}

}
