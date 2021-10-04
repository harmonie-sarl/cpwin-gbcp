package fr.symphonie.budget.core.model.pluri.pk;

import java.io.Serializable;

public class ModificationCpLignePK implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2735359476897273167L;
	
	private Integer exercice;
	private String codeBudget;
	private String type;
	private Integer numero;
	private Integer noLbgCp;
	public Integer getExercice() {
		return exercice;
	}
	public void setExercice(Integer exercice) {
		this.exercice = exercice;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
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
	public String getCodeBudget() {
		return codeBudget;
	}
	public void setCodeBudget(String codeBudget) {
		this.codeBudget = codeBudget;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((codeBudget == null) ? 0 : codeBudget.hashCode());
		result = prime * result + ((exercice == null) ? 0 : exercice.hashCode());
		result = prime * result + ((noLbgCp == null) ? 0 : noLbgCp.hashCode());
		result = prime * result + ((numero == null) ? 0 : numero.hashCode());
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
		ModificationCpLignePK other = (ModificationCpLignePK) obj;
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
		if (type == null) {
			if (other.type != null)
				return false;
		} else if (!type.equals(other.type))
			return false;
		return true;
	}
	

}
