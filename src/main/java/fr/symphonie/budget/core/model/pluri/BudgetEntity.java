package fr.symphonie.budget.core.model.pluri;

public class BudgetEntity {
	
	private Double depAe;
	private Double depCp;

	private Double recette;
	
	public void setDepAe(Double depAe) {
		this.depAe = depAe;
	}
	public Double getDepAe() {
		return depAe;
	}
	public void setDepCp(Double depCp) {
		this.depCp = depCp;
	}
	public Double getDepCp() {
		return depCp;
	}
	public void setRecette(Double recette) {
		this.recette = recette;
	}
	public Double getRecette() {
		return recette;
	}
	

}
