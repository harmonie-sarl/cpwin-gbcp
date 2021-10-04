package fr.symphonie.budget.core.model.pluri.pk;

import java.io.Serializable;

public class EnvelopBudgPK implements Serializable {

	private static final long serialVersionUID = 2319352125355728779L;

	private String codeBudget;
	private int exercice;
	private String groupDest;
	private String groupNat;
//	private String type;
	public String getCodeBudget() {
		return codeBudget;
	}
	public void setCodeBudget(String codeBudget) {
		this.codeBudget = codeBudget;
	}
	public int getExercice() {
		return exercice;
	}
	public void setExercice(int exerciceAE) {
		this.exercice = exerciceAE;
	}
	public String getGroupDest() {
		return groupDest;
	}
	public void setGroupDest(String groupDest) {
		this.groupDest = groupDest;
	}
	public String getGroupNat() {
		return groupNat;
	}
	public void setGroupNat(String groupNat) {
		this.groupNat = groupNat;
	}

//	public void setType(String type) {
//		this.type = type;
//	}
//	public String getType() {
//		return type;
//	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((codeBudget == null) ? 0 : codeBudget.hashCode());
		result = prime * result + exercice;
		result = prime * result
				+ ((groupDest == null) ? 0 : groupDest.hashCode());
		result = prime * result
				+ ((groupNat == null) ? 0 : groupNat.hashCode());
//		result = prime * result + ((type == null) ? 0 : type.hashCode());
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
		if (!(obj instanceof EnvelopBudgPK)) {
			return false;
		}
		EnvelopBudgPK other = (EnvelopBudgPK) obj;
		if (codeBudget == null) {
			if (other.codeBudget != null) {
				return false;
			}
		} else if (!codeBudget.equals(other.codeBudget)) {
			return false;
		}
		if (exercice != other.exercice) {
			return false;
		}
		if (groupDest == null) {
			if (other.groupDest != null) {
				return false;
			}
		} else if (!groupDest.equals(other.groupDest)) {
			return false;
		}
		if (groupNat == null) {
			if (other.groupNat != null) {
				return false;
			}
		} else if (!groupNat.equals(other.groupNat)) {
			return false;
		}
//		if (type == null) {
//			if (other.type != null) {
//				return false;
//			}
//		} else if (!type.equals(other.type)) {
//			return false;
//		}
		return true;
	}
	
}
