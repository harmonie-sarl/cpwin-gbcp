package fr.symphonie.budget.core.model.edition;

import fr.symphonie.budget.core.model.edition.util.DataItem;
import fr.symphonie.util.model.Trace;

public class EditionBiBr {
	private int exercice;
	private String codeBudget;
	private String typeData;
	private String refData;
	private Integer noMouvement;
	private double montant;
	private Trace trace;
	
	public EditionBiBr() {
		super();
	}
	public EditionBiBr(int exercice, String codeBudget, int niveau, TabsEnum type, DataItem item) {
		this();
		this.exercice=exercice;
		this.codeBudget=codeBudget;
		this.noMouvement=niveau;
		this.typeData=type.getType();
		//this.setRefData(item.getRefData().getCodeStr());
		this.setRefData(item.getUid());
		this.setMontant(item.getMontantDouble());
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
	public TabsEnum getTypeData() {
		return TabsEnum.parse(typeData);
	}
	public void setTypeData(TabsEnum typeData) {
		this.typeData = typeData.getType();
	}
	public String getRefData() {
		return refData;
	}
	public void setRefData(String refData) {
		this.refData = refData;
	}
	public Integer getNoMouvement() {
		return noMouvement;
	}
	public void setNoMouvement(Integer noMouvement) {
		this.noMouvement = noMouvement;
	}
	public double getMontant() {
		return montant;
	}
	public void setMontant(double montant) {
		this.montant = montant;
	}
	public Trace getTrace() {
		return trace;
	}
	public void setTrace(Trace trace) {
		this.trace = trace;
	}
	@Override
	public String toString() {
		return "EditionBiBr [exercice=" + exercice + ", codeBudget=" + codeBudget + ", typeData=" + typeData
				+ ", refData=" + refData + ", noMouvement=" + noMouvement + ", montant=" + montant + ", trace=" + trace
				+ "]";
	}

	
}
