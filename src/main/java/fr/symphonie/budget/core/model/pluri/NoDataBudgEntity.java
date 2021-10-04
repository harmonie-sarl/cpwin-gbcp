package fr.symphonie.budget.core.model.pluri;

public class NoDataBudgEntity {
	
	private double dotation;
	private double reprise;
	private double valeur_compt;
	private double produit_cession;
	private double subv_inves;
	private double rembours_caution;
	private double cautio_recu;
	
	 public NoDataBudgEntity( ) {
	    super();
	   }
	public void setDotation(double dotation) {
		this.dotation = dotation;
	}
	public double getDotation() {
		return dotation;
	}
	public void setReprise(double reprise) {
		this.reprise = reprise;
	}
	public double getReprise() {
		return reprise;
	}
	public void setValeur_compt(double valeur_compt) {
		this.valeur_compt = valeur_compt;
	}
	public double getValeur_compt() {
		return valeur_compt;
	}
	public void setProduit_cession(double produit_cession) {
		this.produit_cession = produit_cession;
	}
	public double getProduit_cession() {
		return produit_cession;
	}
	public void setSubv_inves(double subv_inves) {
		this.subv_inves = subv_inves;
	}
	public double getSubv_inves() {
		return subv_inves;
	}
	public void setRembours_caution(double rembours_caution) {
		this.rembours_caution = rembours_caution;
	}
	public double getRembours_caution() {
		return rembours_caution;
	}
	public void setCautio_recu(double cautio_recu) {
		this.cautio_recu = cautio_recu;
	}
	public double getCautio_recu() {
		return cautio_recu;
	}

}
