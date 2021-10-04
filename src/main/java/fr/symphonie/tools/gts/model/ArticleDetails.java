package fr.symphonie.tools.gts.model;

import java.math.BigDecimal;

import fr.symphonie.util.model.SimpleEntity;
import fr.symphonie.util.model.Trace;

public class ArticleDetails {
	private Integer exercice;
	/**
	 * Code Article
	 */
	private String code;
	/**
	 * Prix unitaire en TTC
	 */
	private BigDecimal pu;
	/**
	 * Le taux TVA
	 */
	private float tva;
	/**
	 * Adresse budgétaire
	 */
	private SimpleEntity destination;
	private SimpleEntity nature;
	private SimpleEntity service;
	private SimpleEntity programme;
	/**
	 * Compte de produit
	 */
	private SimpleEntity compteProduit;
	private String imputTtc;
	private String imputTva;
	private Trace trace;
	private Article article;
	private Integer noLbi;
	
	public Integer getExercice() {
		return exercice;
	}
	public void setExercice(Integer exercice) {
		this.exercice = exercice;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public SimpleEntity getDestination() {
		return destination;
	}
	public void setDestination(SimpleEntity destination) {
		this.destination = destination;
	}
	public SimpleEntity getNature() {
		return nature;
	}
	public void setNature(SimpleEntity nature) {
		this.nature = nature;
	}
	public SimpleEntity getService() {
		return service;
	}
	public void setService(SimpleEntity service) {
		this.service = service;
	}
	public SimpleEntity getProgramme() {
		return programme;
	}
	public void setProgramme(SimpleEntity programme) {
		this.programme = programme;
	}
	public SimpleEntity getCompteProduit() {
		return compteProduit;
	}
	public void setCompteProduit(SimpleEntity compteProduit) {
		this.compteProduit = compteProduit;
	}
	public Trace getTrace() {
		return trace;
	}
	public void setTrace(Trace trace) {
		this.trace = trace;
	}
	public Article getArticle() {
		return article;
	}
	public void setArticle(Article article) {
		this.article = article;
		if(article!=null)setCode(article.getCode());
	}
	
	public BigDecimal getPu() {
		return pu;
	}
	public void setPu(BigDecimal pu) {
		this.pu = pu;
	}
	public float getTva() {
		return tva;
	}
	public void setTva(float tva) {
		this.tva = tva;
	}
	@Override
	public String toString() {
		return "ArticleDetails [exercice=" + exercice + ", code=" + code + ", pu=" + pu + ", tva=" + tva
				+ ", destination=" + destination.getCode() + ", nature=" + nature.getCode() + ", service=" + service.getCode() + ", programme="
				+ programme.getCode() + ", compteProduit=" + compteProduit.getCode() + "]";
	}
	public boolean isExecNull() {
		return (getExercice()==null)||
	            (getExercice().intValue()==0); 
	             
	}
	public boolean isCompteProduitNull() {
		return (getCompteProduit().getCode()==null)||
	            (getCompteProduit().getCode().trim().isEmpty()); 
	             
	}
	public boolean isDestinationNull() {
		return (getDestination().getCode()==null)||
	            (getDestination().getCode().trim().isEmpty()); 
	             
	}
	public boolean isProgrammeNull() {
		return (getProgramme().getCode()==null)||
	            (getProgramme().getCode().trim().isEmpty()); 
	             
	}
	public boolean isNatureNull() {
		return (getNature().getCode()==null)||
	            (getNature().getCode().trim().isEmpty()); 
	             
	}
	public boolean isServiceNull() {
		return (getService().getCode()==null)||
	            (getService().getCode().trim().isEmpty()); 
	             
	}
	
	public boolean checkRequired() {
		if(isCompteProduitNull())return false;
		if(isDestinationNull())return false;
		if(isExecNull())return false;
		if(isNatureNull())return false;
		if(isProgrammeNull())return false;
		if(isServiceNull())return false;
		return true;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((code == null) ? 0 : code.hashCode());
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
		ArticleDetails other = (ArticleDetails) obj;
		if (code == null) {
			if (other.code != null)
				return false;
		} else if (!code.equals(other.code))
			return false;
		if (exercice == null) {
			if (other.exercice != null)
				return false;
		} else if (!exercice.equals(other.exercice))
			return false;
		return true;
	}
	public Integer getNoLbi() {
		return noLbi;
	}
	public void setNoLbi(Integer noLbi) {
		this.noLbi = noLbi;
	}
	public String getImputTtc() {
		return imputTtc;
	}
	public void setImputTtc(String imputTtc) {
		this.imputTtc = imputTtc;
	}
	public String getImputTva() {
		return imputTva;
	}
	public void setImputTva(String imputTva) {
		this.imputTva = imputTva;
	}
	
}
