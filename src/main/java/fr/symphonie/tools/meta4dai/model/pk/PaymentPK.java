package fr.symphonie.tools.meta4dai.model.pk;

import java.io.Serializable;

public class PaymentPK implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -6481114392142607393L;
	private Integer exercice;
	private String budget;
	private String period;
	private Integer noEI;
	
	
	public PaymentPK() {
		super();
	}
	public Integer getExercice() {
		return exercice;
	}
	public void setExercice(Integer exercice) {
		this.exercice = exercice;
	}
	
	public String getBudget() {
		return budget;
	}
	public void setBudget(String budget) {
		this.budget = budget;
	}
	public String getPeriod() {
		return period;
	}
	public void setPeriod(String period) {
		this.period = period;
	}
	public Integer getNoEI() {
		return noEI;
	}
	public void setNoEI(Integer noEI) {
		this.noEI = noEI;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((budget == null) ? 0 : budget.hashCode());
		result = prime * result + ((exercice == null) ? 0 : exercice.hashCode());
		result = prime * result + ((noEI == null) ? 0 : noEI.hashCode());
		result = prime * result + ((period == null) ? 0 : period.hashCode());
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
		PaymentPK other = (PaymentPK) obj;
		if (budget == null) {
			if (other.budget != null)
				return false;
		} else if (!budget.equals(other.budget))
			return false;
		if (exercice == null) {
			if (other.exercice != null)
				return false;
		} else if (!exercice.equals(other.exercice))
			return false;
		if (noEI == null) {
			if (other.noEI != null)
				return false;
		} else if (!noEI.equals(other.noEI))
			return false;
		if (period == null) {
			if (other.period != null)
				return false;
		} else if (!period.equals(other.period))
			return false;
		return true;
	}
	
	
}
