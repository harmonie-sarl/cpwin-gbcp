package fr.symphonie.tools.lemans.bt.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import fr.symphonie.util.model.Trace;
import lombok.Data;
@Data
@Entity
@Table(name = "bt_spectacle")
public class Spectacle {
	@Id
	@Column(length = 20)
	private String code;
	@Column(length = 50)
	private String libelle;
	
	/**
	 * Détails par exercice
	 */
	@OneToMany(mappedBy = "spectacle",fetch = FetchType.EAGER,  orphanRemoval = true, cascade = CascadeType.ALL)
	private List<SpectacleDetails> details= new ArrayList<SpectacleDetails>();
	@Embedded
	private Trace trace;
	@Transient
	private SpectacleDetails detail;
	
	public void setCode(String code) {
		this.code = code;
		refreshDetailsValues();
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
		for (SpectacleDetails detail : details) {
			detail.setCode(this.getCode());
		}
	}
	public SpectacleDetails getDetailItem(int exercice){
		if(getDetails()==null)return null;
		for(SpectacleDetails d:getDetails()){
			if(d.getExercice()==exercice) return d;
		}
		return null;
	}
	
}
