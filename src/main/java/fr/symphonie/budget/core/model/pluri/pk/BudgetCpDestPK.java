package fr.symphonie.budget.core.model.pluri.pk;

import java.io.Serializable;

public class BudgetCpDestPK implements Serializable{

	private static final long serialVersionUID = 5839791863359739900L;
	
	private String codeBudget;
	private int exerciceCP;
	private String groupNat;
	private String destination;
	private int niveau;
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
	public String getDestination() {
		return destination;
	}
	public void setDestination(String destination) {
		this.destination = destination;
	}
	public int getNiveau() {
		return niveau;
	}
	public void setNiveau(int niveau) {
		this.niveau = niveau;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((codeBudget == null) ? 0 : codeBudget.hashCode());
		result = prime * result + ((destination == null) ? 0 : destination.hashCode());
		result = prime * result + exerciceCP;
		result = prime * result + ((groupNat == null) ? 0 : groupNat.hashCode());
		result = prime * result + niveau;
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
		BudgetCpDestPK other = (BudgetCpDestPK) obj;
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
		if (exerciceCP != other.exerciceCP)
			return false;
		if (groupNat == null) {
			if (other.groupNat != null)
				return false;
		} else if (!groupNat.equals(other.groupNat))
			return false;
		if (niveau != other.niveau)
			return false;
		return true;
	}
	
	
}
