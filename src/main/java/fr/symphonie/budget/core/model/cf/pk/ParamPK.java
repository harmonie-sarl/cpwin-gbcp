package fr.symphonie.budget.core.model.cf.pk;

import java.io.Serializable;

public class ParamPK implements Serializable{
	
	
	public ParamPK() {
		super();
	}
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -7222651337218090921L;
	private int exercice;
	private String codeBudget;
	private String type;
	private String codeCompte;
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
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getCodeCompte() {
		return codeCompte;
	}
	public void setCodeCompte(String codeCompte) {
		this.codeCompte = codeCompte;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((codeBudget == null) ? 0 : codeBudget.hashCode());
		result = prime * result + ((codeCompte == null) ? 0 : codeCompte.hashCode());
		result = prime * result + exercice;
		result = prime * result + ((type == null) ? 0 : type.hashCode());
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
		ParamPK other = (ParamPK) obj;
		if (codeBudget == null) {
			if (other.codeBudget != null)
				return false;
		} else if (!codeBudget.equals(other.codeBudget))
			return false;
		if (codeCompte == null) {
			if (other.codeCompte != null)
				return false;
		} else if (!codeCompte.equals(other.codeCompte))
			return false;
		if (exercice != other.exercice)
			return false;
		if (type == null) {
			if (other.type != null)
				return false;
		} else if (!type.equals(other.type))
			return false;
		return true;
	}
}
