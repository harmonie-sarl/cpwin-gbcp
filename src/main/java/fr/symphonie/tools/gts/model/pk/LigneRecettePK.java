package fr.symphonie.tools.gts.model.pk;

import java.io.Serializable;

public class LigneRecettePK implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -2962038948877960713L;
	private Integer exercice;
	private Integer numero;
	private Integer noLiqRec;

	public LigneRecettePK() {
		// TODO Auto-generated constructor stub
	}

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

	public Integer getNoLiqRec() {
		return noLiqRec;
	}

	public void setNoLiqRec(Integer noLiqRec) {
		this.noLiqRec = noLiqRec;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((exercice == null) ? 0 : exercice.hashCode());
		result = prime * result + ((noLiqRec == null) ? 0 : noLiqRec.hashCode());
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
		LigneRecettePK other = (LigneRecettePK) obj;
		if (exercice == null) {
			if (other.exercice != null)
				return false;
		} else if (!exercice.equals(other.exercice))
			return false;
		if (noLiqRec == null) {
			if (other.noLiqRec != null)
				return false;
		} else if (!noLiqRec.equals(other.noLiqRec))
			return false;
		if (numero == null) {
			if (other.numero != null)
				return false;
		} else if (!numero.equals(other.numero))
			return false;
		return true;
	}

}
