package fr.symphonie.budget.core.model.pluri.pk;

import java.io.Serializable;

public class EnvelopInternePK implements Serializable {

	private static final long serialVersionUID = 3758720170532251655L;
	private int exercice;
	private String codeBudget;
	private int exerciceAE;
	private String groupDest;
	private String groupNat;
	private String destination;
	private String nature;
	private String programme;
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
	public int getExerciceAE() {
		return exerciceAE;
	}
	public void setExerciceAE(int exerciceAE) {
		this.exerciceAE = exerciceAE;
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
	public String getDestination() {
		return destination;
	}
	public void setDestination(String destination) {
		this.destination = destination;
	}
	public String getNature() {
		return nature;
	}
	public void setNature(String nature) {
		this.nature = nature;
	}
	public String getProgramme() {
		return programme;
	}
	public void setProgramme(String programme) {
		this.programme = programme;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((codeBudget == null) ? 0 : codeBudget.hashCode());
		result = prime * result
				+ ((destination == null) ? 0 : destination.hashCode());
		result = prime * result + exercice;
		result = prime * result + exerciceAE;
		result = prime * result
				+ ((groupDest == null) ? 0 : groupDest.hashCode());
		result = prime * result
				+ ((groupNat == null) ? 0 : groupNat.hashCode());
		result = prime * result + ((nature == null) ? 0 : nature.hashCode());
		result = prime * result
				+ ((programme == null) ? 0 : programme.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof EnvelopInternePK))
			return false;
		EnvelopInternePK other = (EnvelopInternePK) obj;
		if (codeBudget == null) {
			if (other.codeBudget != null)
				return false;
		} else if (!codeBudget.equals(other.codeBudget))
			return false;
		if (destination == null) {
			if (other.destination != null)
				return false;
		} else if (!destination.equals(other.destination))
			return false;
		if (exercice != other.exercice)
			return false;
		if (exerciceAE != other.exerciceAE)
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
		if (nature == null) {
			if (other.nature != null)
				return false;
		} else if (!nature.equals(other.nature))
			return false;
		if (programme == null) {
			if (other.programme != null)
				return false;
		} else if (!programme.equals(other.programme))
			return false;
		return true;
	}
	
}
