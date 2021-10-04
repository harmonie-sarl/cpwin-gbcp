package fr.symphonie.tools.das.model.pk;

import java.io.Serializable;

public class HonoraireID implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 4811290978844200592L;
	private Integer exercice;
	private String codeTiers;
	
	public HonoraireID() {
		super();
	}
	public Integer getExercice() {
		return exercice;
	}
	public void setExercice(Integer exercice) {
		this.exercice = exercice;
	}
	public String getCodeTiers() {
		return codeTiers;
	}
	public void setCodeTiers(String codeTiers) {
		this.codeTiers = codeTiers;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((codeTiers == null) ? 0 : codeTiers.hashCode());
		result = prime * result + ((exercice == null) ? 0 : exercice.hashCode());
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
		HonoraireID other = (HonoraireID) obj;
		if (codeTiers == null) {
			if (other.codeTiers != null)
				return false;
		} else if (!codeTiers.equals(other.codeTiers))
			return false;
		if (exercice == null) {
			if (other.exercice != null)
				return false;
		} else if (!exercice.equals(other.exercice))
			return false;
		return true;
	}
	
}
