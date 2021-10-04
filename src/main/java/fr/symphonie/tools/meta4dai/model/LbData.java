package fr.symphonie.tools.meta4dai.model;

public class LbData {
	private Integer numero;
	private String destination;
	private String nature;
	private String programme;
	private String service;
	private String direction;
	
	
	public LbData() {
		super();
	}
	public LbData(Integer numero, String destination, String nature, String programme, String service,String direction) {
		this();
		this.numero = numero;
		this.destination = destination;
		this.nature = nature;
		this.programme = programme;
		this.service = service;
		this.direction=direction;
	}
	public Integer getNumero() {
		return numero;
	}
	public void setNumero(Integer numero) {
		this.numero = numero;
	}
	public String getDestination() {
		return destination;
	}
	public void setDestination(String destination) {
		this.destination = destination;
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
	public String getService() {
		return service;
	}
	public void setService(String service) {
		this.service = service;
	}
	
	public String getDirection() {
		return direction;
	}
	public void setDirection(String direction) {
		this.direction = direction;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
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
		LbData other = (LbData) obj;
		if (numero == null) {
			if (other.numero != null)
				return false;
		} else if (!numero.equals(other.numero))
			return false;
		return true;
	}
	@Override
	public String toString() {
		return "LbI [ destination=" + destination + ", nature=" + nature + ", programme="
				+ programme + ", service=" + service + ", direction=" + direction + "]";
	}
	
	
}
