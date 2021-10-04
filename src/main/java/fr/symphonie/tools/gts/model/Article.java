package fr.symphonie.tools.gts.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Transient;

import fr.symphonie.util.model.Trace;

public class Article {
	private String code;
	private String libelle;
	
	/**
	 * Détails par exercice
	 */
	private List<ArticleDetails> details= new ArrayList<ArticleDetails>();
	private Trace trace;
	@Transient
	private ArticleDetails detail;
	public String getCode() {
		return code;
	}
	
	public void setCode(String code) {
		this.code = code;
		refreshDetailsValues();
	}
	public String getLibelle() {
		return libelle;
	}
	public void setLibelle(String libelle) {
		this.libelle = libelle;
	}
	
	public List<ArticleDetails> getDetails() {
		return details;
	}
	public void setDetails(List<ArticleDetails> details) {
		this.details = details;
	}
	public Trace getTrace() {
		return trace;
	}
	public void setTrace(Trace trace) {
		this.trace = trace;
	}
	@Override
	public String toString() {
		return "Article [code=" + code + ", libelle=" + libelle + ", details=" + details + "]";
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((code == null) ? 0 : code.hashCode());
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
		Article other = (Article) obj;
		if (code == null) {
			if (other.code != null)
				return false;
		} else if (!code.equals(other.code))
			return false;
		return true;
	}
	public boolean isCodeNull() {
		return (getCode()==null)||
	            (getCode().trim().isEmpty()); 
	             
	}
	public boolean isLibelleNull() {
		return (getLibelle()==null)||
	            (getLibelle().trim().isEmpty()); 
	             
	}
	public boolean checkRequired() {
		if(isCodeNull())return false;
		if(isLibelleNull())return false;
		return true;
	}

	private void refreshDetailsValues() {
		if(details==null) return;
		for (ArticleDetails detail : details) {
			detail.setCode(this.getCode());
		}
	}
	public ArticleDetails getDetailItem(int exercice){
		//ArticleDetails result=null;
		if(getDetails()==null)return null;
		for(ArticleDetails d:getDetails()){
			if(d.getExercice()==exercice) return d;
		}
		return null;
	}

	public ArticleDetails getDetail() {
		return detail;
	}

	public void setDetail(ArticleDetails detail) {
		this.detail = detail;
	}
	
}
