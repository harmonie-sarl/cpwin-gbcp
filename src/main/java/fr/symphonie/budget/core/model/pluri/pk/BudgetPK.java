package fr.symphonie.budget.core.model.pluri.pk;

import java.io.Serializable;

public class BudgetPK implements Serializable {
	private static final long serialVersionUID = 1776207840964300986L;
	private int exercice;
	private String codeBudget;
	
	public BudgetPK(int exerciceAE, String codeBudget) {
		super();
		this.exercice = exerciceAE;
		this.codeBudget = codeBudget;
	}
	
	public BudgetPK() {
		super();
	}

	public int getExercice() {
		return exercice;
	}
	public void setExercice(int exerciceAE) {
		this.exercice = exerciceAE;
	}
	public String getCodeBudget() {
		return codeBudget;
	}
	public void setCodeBudget(String codeBudget) {
		this.codeBudget = codeBudget;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((codeBudget == null) ? 0 : codeBudget.hashCode());
		result = prime * result + exercice;
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof BudgetPK))
			return false;
		BudgetPK other = (BudgetPK) obj;
		if (codeBudget == null) {
			if (other.codeBudget != null)
				return false;
		} else if (!codeBudget.equals(other.codeBudget))
			return false;
		if (exercice != other.exercice)
			return false;
		return true;
	}
	

}
