package fr.symphonie.tools.gts.model;

public class RecetteLigneBudg {

	private String destination;
	private String service;
	private String nature;
	private String programme;
//	private double mntTotal;
	private double dontRegisseur;
	private double dontClient;
	
	public RecetteLigneBudg(ArticleDetails detail) {
		super();
		this.destination=detail.getDestination().getCode();
		this.nature=detail.getNature().getCode();
		this.service=detail.getService().getCode();
		this.programme=detail.getProgramme().getCode();
	}
	public String getDestination() {
		return destination;
	}
	public void setDestination(String destination) {
		this.destination = destination;
	}
	public String getService() {
		return service;
	}
	public void setService(String service) {
		this.service = service;
	}
	public String getNature() {
		return nature;
	}
	public void setNature(String nature) {
		this.nature = nature;
	}
	public String getProgramme() {
		return programme;
	}
	public void setProgramme(String programme) {
		this.programme = programme;
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
		result = prime * result + ((destination == null) ? 0 : destination.hashCode());
		result = prime * result + ((nature == null) ? 0 : nature.hashCode());
		result = prime * result + ((programme == null) ? 0 : programme.hashCode());
		result = prime * result + ((service == null) ? 0 : service.hashCode());
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
		RecetteLigneBudg other = (RecetteLigneBudg) obj;
		if (destination == null) {
			if (other.destination != null)
				return false;
		} else if (!destination.equals(other.destination))
			return false;
		if (nature == null) {
			if (other.nature != null)
				return false;
		} else if (!nature.equals(other.nature))
			return false;
		if (programme == null) {
			if (other.programme != null)
				return false;
		} else if (!programme.equals(other.programme))
			return false;
		if (service == null) {
			if (other.service != null)
				return false;
		} else if (!service.equals(other.service))
			return false;
		return true;
	}
	
	
	
	
	

}
