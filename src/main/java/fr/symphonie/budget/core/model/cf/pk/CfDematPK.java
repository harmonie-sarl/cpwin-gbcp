package fr.symphonie.budget.core.model.cf.pk;

import java.io.Serializable;

public class CfDematPK implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -5239942422812798779L;
	private int exercice;
	private String codeBudget;
	private Integer numeroLigne;
	
	
	public CfDematPK() {
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
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((codeBudget == null) ? 0 : codeBudget.hashCode());
		result = prime * result + exercice;
		result = prime * result + ((numeroLigne == null) ? 0 : numeroLigne.hashCode());
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
		CfDematPK other = (CfDematPK) obj;
		if (codeBudget == null) {
			if (other.codeBudget != null)
				return false;
		} else if (!codeBudget.equals(other.codeBudget))
			return false;
		if (exercice != other.exercice)
			return false;
		if (numeroLigne == null) {
			if (other.numeroLigne != null)
				return false;
		} else if (!numeroLigne.equals(other.numeroLigne))
			return false;
		return true;
	}
	
	
}
