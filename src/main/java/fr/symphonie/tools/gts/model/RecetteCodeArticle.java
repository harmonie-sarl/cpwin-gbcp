package fr.symphonie.tools.gts.model;

public class RecetteCodeArticle {
	
	private String codeArticle;
//	private double mntTotal;
	private double dontRegisseur;
	private double dontClient;
	
	
	public RecetteCodeArticle(String codeArticle) {
		super();
		this.codeArticle=codeArticle;
	}
	public String getCodeArticle() {
		return codeArticle;
	}
	public void setCodeArticle(String codeArticle) {
		this.codeArticle = codeArticle;
	}
	public double getMntTotal() {
//		return mntTotal;
		return getDontClient()+getDontRegisseur();
	}
//	public void setMntTotal(double mntTotal) {
//		this.mntTotal = mntTotal;
//	}
	public double getDontRegisseur() {
		return dontRegisseur;
	}
	public void setDontRegisseur(double dontRegisseur) {
		this.dontRegisseur = dontRegisseur;
	}
	public double getDontClient() {
		return dontClient;
	}
	public void setDontClient(double dontClient) {
		this.dontClient = dontClient;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((codeArticle == null) ? 0 : codeArticle.hashCode());
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
		RecetteCodeArticle other = (RecetteCodeArticle) obj;
		if (codeArticle == null) {
			if (other.codeArticle != null)
				return false;
		} else if (!codeArticle.equals(other.codeArticle))
			return false;
		return true;
	}
	
	


	
	
	
}
