package fr.symphonie.budget.core.model.edition.pk;

import java.io.Serializable;

public class OperationPK implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 7981074640343115296L;
	private int exercice;
	private String codeBudget;
	private Integer noMouvement;
	private String code;
	
	public OperationPK() {
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
	public Integer getNoMouvement() {
		return noMouvement;
	}
	public void setNoMouvement(Integer noMouvement) {
		this.noMouvement = noMouvement;
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
		result = prime * result + ((noMouvement == null) ? 0 : noMouvement.hashCode());
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
		OperationPK other = (OperationPK) obj;
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
		if (noMouvement == null) {
			if (other.noMouvement != null)
				return false;
		} else if (!noMouvement.equals(other.noMouvement))
			return false;
		return true;
	}
	
}
