package fr.symphonie.tools.lemans.bt.model;



import javax.persistence.Transient;

import fr.symphonie.cpwin.model.Adresse;
import fr.symphonie.cpwin.model.Tiers;
import fr.symphonie.util.model.OuiNonEnum;
import fr.symphonie.util.model.Trace;
import lombok.Data;
@Data
public class Client {
private String code;
private String nom;
private String codeCpwin;
private Integer noAdresseCpwin;
private boolean regisseur;
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
