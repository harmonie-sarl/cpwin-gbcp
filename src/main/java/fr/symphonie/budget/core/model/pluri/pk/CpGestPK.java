package fr.symphonie.budget.core.model.pluri.pk;

import java.io.Serializable;

public class CpGestPK implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -150195834515106376L;
	private int exerciceCP;
	private String codeBudget;	
	private String groupNat;
	private String gestionnaire;
	public int getExerciceCP() {
		return exerciceCP;
	}
	public void setExerciceCP(int exerciceCP) {
		this.exerciceCP = exerciceCP;
	}
	public String getCodeBudget() {
		return codeBudget;
	}
	public void setCodeBudget(String codeBudget) {
		this.codeBudget = codeBudget;
	}
	public String getGroupNat() {
		return groupNat;
	}
	public void setGroupNat(String groupNat) {
		this.groupNat = groupNat;
	}
	public String getGestionnaire() {
		return gestionnaire;
	}
	public void setGestionnaire(String gestionnaire) {
		this.gestionnaire = gestionnaire;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((codeBudget == null) ? 0 : codeBudget.hashCode());
		result = prime * result + exerciceCP;
		result = prime * result
				+ ((gestionnaire == null) ? 0 : gestionnaire.hashCode());
		result = prime * result
				+ ((groupNat == null) ? 0 : groupNat.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof CpGestPK)) {
			return false;
		}
		CpGestPK other = (CpGestPK) obj;
		if (codeBudget == null) {
			if (other.codeBudget != null) {
				return false;
			}
		} else if (!codeBudget.equals(other.codeBudget)) {
			return false;
		}
		if (exerciceCP != other.exerciceCP) {
			return false;
		}
		if (gestionnaire == null) {
			if (other.gestionnaire != null) {
				return false;
			}
		} else if (!gestionnaire.equals(other.gestionnaire)) {
			return false;
		}
		if (groupNat == null) {
			if (other.groupNat != null) {
				return false;
			}
		} else if (!groupNat.equals(other.groupNat)) {
			return false;
		}
		return true;
	}
	
}
