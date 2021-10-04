package fr.symphonie.budget.core.model.pluri.pk;

import java.io.Serializable;

public class ModificationPK implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1921640396617846170L;
	private Integer exercice;
	private String type;
	private Integer numero;
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
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((exercice == null) ? 0 : exercice.hashCode());
		result = prime * result + ((numero == null) ? 0 : numero.hashCode());
		result = prime * result + ((type == null) ? 0 : type.hashCode());
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
		if (!(obj instanceof ModificationPK)) {
			return false;
		}
		ModificationPK other = (ModificationPK) obj;
		if (exercice == null) {
			if (other.exercice != null) {
				return false;
			}
		} else if (!exercice.equals(other.exercice)) {
			return false;
		}
		if (numero == null) {
			if (other.numero != null) {
				return false;
			}
		} else if (!numero.equals(other.numero)) {
			return false;
		}
		if (type == null) {
			if (other.type != null) {
				return false;
			}
		} else if (!type.equals(other.type)) {
			return false;
		}
		return true;
	}
	
}
