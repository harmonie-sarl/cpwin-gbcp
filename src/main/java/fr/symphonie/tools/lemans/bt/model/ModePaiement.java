package fr.symphonie.tools.lemans.bt.model;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import fr.symphonie.util.model.OuiNonEnum;
import fr.symphonie.util.model.Trace;
import lombok.Data;


@Data
@Entity
@Table(name = "bt_modpaie")
public class ModePaiement {
	
	@Id
	private String Code;
	@Column
	private String designation;
	
	@Column(name = "est_regisseur")
	private boolean regisseur;
	
	@Column(name="avec_frais")
	private boolean frais;
	
	@Embedded
	private Trace trace;
	
	

		
	public String getRegis(){
		return isRegisseur()? OuiNonEnum.OUI.getDesignation():OuiNonEnum.NON.getDesignation();
	}
	
	public String getWithFrai(){
		return isFrais()? OuiNonEnum.OUI.getDesignation():OuiNonEnum.NON.getDesignation();
	}
	public boolean isCodeNull() {
		return (getCode()==null)||
	            (getCode().trim().isEmpty()); 
	             
	}
		
	
	
	
}
