package fr.symphonie.budget.core.model.pluri.pk;

import java.io.Serializable;

public class CpModificationLignePK implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -7569056327464094505L;
	private Integer exercice;
	private String codeBudget;
	private Integer numero;
	private Integer noLbgCp;
	public Integer getExercice() {
		return exercice;
	}
	public void setExercice(Integer exercice) {
		this.exercice = exercice;
	}
	public String getCodeBudget() {
		return codeBudget;
	}
	public void setCodeBudget(String codeBudget) {
		this.codeBudget = codeBudget;
	}
	public Integer getNumero() {
		return numero;
	}
	public void setNumero(Integer numero) {
		this.numero = numero;
	}
	public Integer getNoLbgCp() {
		return noLbgCp;
	}
	public void setNoLbgCp(Integer noLbgCp) {
		this.noLbgCp = noLbgCp;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((codeBudget == null) ? 0 : codeBudget.hashCode());
		result = prime * result + ((exercice == null) ? 0 : exercice.hashCode());
		result = prime * result + ((noLbgCp == null) ? 0 : noLbgCp.hashCode());
		result = prime * result + ((numero == null) ? 0 : numero.hashCode());
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
		CpModificationLignePK other = (CpModificationLignePK) obj;
		if (codeBudget == null) {
			if (other.codeBudget != null)
				return false;
		} else if (!codeBudget.equals(other.codeBudget))
			return false;
		if (exercice == null) {
			if (other.exercice != null)
				return false;
		} else if (!exercice.equals(other.exercice))
			return false;
		if (noLbgCp == null) {
			if (other.noLbgCp != null)
				return false;
		} else if (!noLbgCp.equals(other.noLbgCp))
			return false;
		if (numero == null) {
			if (other.numero != null)
				return false;
		} else if (!numero.equals(other.numero))
			return false;
		return true;
	}
	
}
