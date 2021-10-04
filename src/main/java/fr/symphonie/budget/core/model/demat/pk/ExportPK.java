package fr.symphonie.budget.core.model.demat.pk;

import java.io.Serializable;

public class ExportPK implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 853163377670131917L;
	private int exercice;
	private String codeBudget;
	private String code;
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
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((code == null) ? 0 : code.hashCode());
		result = prime * result + ((codeBudget == null) ? 0 : codeBudget.hashCode());
		result = prime * result + exercice;
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ExportPK other = (ExportPK) obj;
		if (code == null) {
			if (other.code != null)
				return false;
		} else if (!code.equals(other.code))
			return false;
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
