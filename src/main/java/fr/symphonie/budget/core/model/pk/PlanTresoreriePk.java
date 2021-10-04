package fr.symphonie.budget.core.model.pk;

import java.io.Serializable;

public class PlanTresoreriePk implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 8583664080074428525L;
	private Integer exercice;
	private Integer periode;
	private Integer numero;
	
	
	public PlanTresoreriePk() {
		super();
	}


	public Integer getExercice() {
		return exercice;
	}


	public void setExercice(Integer exercice) {
		this.exercice = exercice;
	}


	public Integer getPeriode() {
		return periode;
	}


	public void setPeriode(Integer periode) {
		this.periode = periode;
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
		result = prime * result + ((exercice == null) ? 0 : exercice.hashCode());
		result = prime * result + ((numero == null) ? 0 : numero.hashCode());
		result = prime * result + ((periode == null) ? 0 : periode.hashCode());
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
		PlanTresoreriePk other = (PlanTresoreriePk) obj;
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
		if (periode == null) {
			if (other.periode != null)
				return false;
		} else if (!periode.equals(other.periode))
			return false;
		return true;
	}
	
	
	
}
