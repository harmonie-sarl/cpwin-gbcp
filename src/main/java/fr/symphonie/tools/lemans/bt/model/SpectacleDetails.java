package fr.symphonie.tools.lemans.bt.model;

import fr.symphonie.util.model.SimpleEntity;
import fr.symphonie.util.model.Trace;
import lombok.Data;
@Data
public class SpectacleDetails {
	private Integer exercice;
	/**
	 * Code Spectacle
	 */
	private String code;
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
	private Spectacle spectacle;
	private Integer noLbi;
	
	
	public void setSpectacle(Spectacle spectacle) {
		this.spectacle = spectacle;
		if(spectacle!=null)setCode(spectacle.getCode());
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
}
