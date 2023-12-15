package fr.symphonie.tools.lemans.bt.model;



import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import fr.symphonie.cpwin.model.Adresse;
import fr.symphonie.cpwin.model.Tiers;
import fr.symphonie.util.model.OuiNonEnum;
import fr.symphonie.util.model.Trace;
import lombok.Data;
@Data
@Entity
@Table(name = "bt_client")
public class Client {
@Id
private String code;
@Column
private String nom;
@Column(name = "code_cpwin",length = 10)
private String codeCpwin;
@Column(name = "no_adr")
private Integer noAdresseCpwin;
@Column(name = "est_regisseur")
private boolean regisseur;
@Embedded
private Trace trace;
@Transient
private Tiers tiersCpwin;
@Transient
private Adresse adresse;


public void setTiersCpwin(Tiers tiersCpwin) {
	this.tiersCpwin = tiersCpwin;
	if(tiersCpwin==null)
		setCodeCpwin(null);	
	else 
		setCodeCpwin(tiersCpwin.getCode());

}

public boolean checkRequired() {
	if(isCodeCpwinNull())return false;
	if(isCodeNull())return false;
	return true;
}
public boolean isCodeCpwinNull() {
	return (getCodeCpwin()==null)||
            (getCodeCpwin().trim().isEmpty()); 
             
}
public boolean isCodeNull() {
	return (getCode()==null)||
            (getCode().trim().isEmpty()); 
             
}

public String getRegis(){
	return isRegisseur()? OuiNonEnum.OUI.getDesignation():OuiNonEnum.NON.getDesignation();
}

}
