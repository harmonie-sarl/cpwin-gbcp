package fr.symphonie.budget.core.model.pk;

import java.io.Serializable;

public class EcriturePk implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -6193904546125053632L;
	private Integer exercice;
	private Integer numero;
	private Integer numeroOP;
	public Integer getExercice() {
		return exercice;
	}
	public void setExercice(Integer exercice) {
		this.exercice = exercice;
	}
	public Integer getNumero() {
		return numero;
	}
	public void setNumero(Integer numero) {
		this.numero = numero;
	}
	public Integer getNumeroOP() {
		return numeroOP;
	}
	public void setNumeroOP(Integer numeroOP) {
		this.numeroOP = numeroOP;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((exercice == null) ? 0 : exercice.hashCode());
		result = prime * result + ((numero == null) ? 0 : numero.hashCode());
		result = prime * result + ((numeroOP == null) ? 0 : numeroOP.hashCode());
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
		EcriturePk other = (EcriturePk) obj;
		if (exercice == null) {
			if (other.exercice != null)
				return false;
		} else if (!exercice.equals(other.exercice))
			return false;
		if (numero == null) {
			if (other.numero != null)
				return false;
		} else if (!numero.equals(other.numero))
			return false;
		if (numeroOP == null) {
			if (other.numeroOP != null)
				return false;
		} else if (!numeroOP.equals(other.numeroOP))
			return false;
		return true;
	}
	
	
}
