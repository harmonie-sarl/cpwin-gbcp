package fr.symphonie.budget.core.model.pluri.pk;

import java.io.Serializable;

public class CreditPaiementPK implements Serializable {

	private static final long serialVersionUID = 1661083486052633787L;
	private int exerciceAE;
	private String codeBudget;
	private int exerciceCP;
	private String groupDest;
	private String groupNat;
//	private String programme;
	public int getExerciceAE() {
		return exerciceAE;
	}
	public void setExerciceAE(int exerciceAE) {
		this.exerciceAE = exerciceAE;
	}
	public String getCodeBudget() {
		return codeBudget;
	}
	public void setCodeBudget(String codeBudget) {
		this.codeBudget = codeBudget;
	}
	public int getExerciceCP() {
		return exerciceCP;
	}
	public void setExerciceCP(int exerciceCP) {
		this.exerciceCP = exerciceCP;
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
//	public String getProgramme() {
//		return programme;
//	}
//	public void setProgramme(String programme) {
//		this.programme = programme;
//	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((codeBudget == null) ? 0 : codeBudget.hashCode());
		result = prime * result + exerciceAE;
		result = prime * result + exerciceCP;
		result = prime * result
				+ ((groupDest == null) ? 0 : groupDest.hashCode());
		result = prime * result
				+ ((groupNat == null) ? 0 : groupNat.hashCode());
//		result = prime * result
//				+ ((programme == null) ? 0 : programme.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof CreditPaiementPK))
			return false;
		CreditPaiementPK other = (CreditPaiementPK) obj;
		if (codeBudget == null) {
			if (other.codeBudget != null)
				return false;
		} else if (!codeBudget.equals(other.codeBudget))
			return false;
		if (exerciceAE != other.exerciceAE)
			return false;
		if (exerciceCP != other.exerciceCP)
			return false;
		if (groupDest == null) {
			if (other.groupDest != null)
				return false;
		} else if (!groupDest.equals(other.groupDest))
			return false;
		if (groupNat == null) {
			if (other.groupNat != null)
				return false;
		} else if (!groupNat.equals(other.groupNat))
			return false;
//		if (programme == null) {
//			if (other.programme != null)
//				return false;
//		} else if (!programme.equals(other.programme))
//			return false;
		return true;
	}
	
}
