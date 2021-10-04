package fr.symphonie.budget.core.model.demat.pk;

import java.io.Serializable;

public class BiBrPK implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 9181820176609351662L;
	private int exercice;
	private String codeBudget;
	private String typeData;
	private String refData;
	private Integer noMouvement;
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
	public String getTypeData() {
		return typeData;
	}
	public void setTypeData(String typeData) {
		this.typeData = typeData;
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
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((codeBudget == null) ? 0 : codeBudget.hashCode());
		result = prime * result + exercice;
		result = prime * result + ((noMouvement == null) ? 0 : noMouvement.hashCode());
		result = prime * result + ((refData == null) ? 0 : refData.hashCode());
		result = prime * result + ((typeData == null) ? 0 : typeData.hashCode());
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
		BiBrPK other = (BiBrPK) obj;
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
		if (refData == null) {
			if (other.refData != null)
				return false;
		} else if (!refData.equals(other.refData))
			return false;
		if (typeData == null) {
			if (other.typeData != null)
				return false;
		} else if (!typeData.equals(other.typeData))
			return false;
		return true;
	}
	
	
}
