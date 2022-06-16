package fr.symphonie.tools.lemans.bt.model;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import fr.symphonie.util.model.SimpleEntity;
import fr.symphonie.util.model.Trace;
import lombok.Data;
import lombok.ToString;
@Data
@Entity
@Table(name = "bt_detailSpectacle")
@IdClass(value = fr.symphonie.cpwin.model.pk.YearlyCodeKey.class)
public class SpectacleDetails {
	@Id
	@Column(name = "num_exec")
	private Integer exercice;
	/**
	 * Code Spectacle
	 */
	@Id
	@Column(name = "code_spectacle")
	private String code;
	/**
	 * Le taux TVA
	 */
	@Column(name = "taux_tva")
	private float tva;
	/**
	 * Adresse budgétaire
	 */
	@Embedded
	@AttributeOverrides({
	@AttributeOverride(name="code",column = @Column(name="code_dest")),
	//@AttributeOverride(name="designation",column = @Column(name="lib_dest"))
	})
	private SimpleEntity destination;
	@Embedded
	@AttributeOverrides({
	@AttributeOverride(name="code",column = @Column(name="code_nature")),
	//@AttributeOverride(name="designation",column = @Column(name="lib_nature"))
	})
	private SimpleEntity nature;
	@Embedded
	@AttributeOverrides({
	@AttributeOverride(name="code",column = @Column(name="code_service")),
	//@AttributeOverride(name="designation",column = @Column(name="lib_service"))
	})
	private SimpleEntity service;
	@Embedded
	@AttributeOverrides({
	@AttributeOverride(name="code",column = @Column(name="code_prog")),
	//@AttributeOverride(name="designation",column = @Column(name="lib_prog"))
	})
	private SimpleEntity programme;
	/**
	 * Compte de produit
	 */
	@Embedded
	@AttributeOverrides({
	@AttributeOverride(name="code",column = @Column(name="cpt_produit")),
	//@AttributeOverride(name="designation",column = @Column(name="lib_cpt_prod"))
	})
	private SimpleEntity compteProduit;
	@Column(name = "cpt_tiers",length = 10)
	private String compteClient;;
	@Embedded
	private Trace trace;
	@ManyToOne
	@JoinColumn(name = "code_spectacle",referencedColumnName = "code",  insertable = false,updatable = false)
	@ToString.Exclude
	private Spectacle spectacle;
	@Column(name = "no_lbi")
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
