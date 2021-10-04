package fr.symphonie.tools.gts.model;



import javax.persistence.Transient;

import fr.symphonie.cpwin.model.Adresse;
import fr.symphonie.cpwin.model.Tiers;
import fr.symphonie.util.model.OuiNonEnum;
import fr.symphonie.util.model.Trace;

public class ClientGts {
private String code;
private String nom;
private String codeCpwin;
private Integer noAdresseCpwin;
private boolean regisseur;
private Trace trace;
//private static final Logger logger = LoggerFactory.getLogger(ClientGts.class);

@Transient
private Tiers tiersCpwin;
@Transient
private Adresse adresse;

public String getCode() {
	return code;
}
public void setCode(String code) {
	this.code = code;
}
public String getNom() {
	return nom;
}
public void setNom(String nom) {
	this.nom = nom;
}
public String getCodeCpwin() {
	return codeCpwin;
}
public void setCodeCpwin(String codeCpwin) {
	this.codeCpwin = codeCpwin;
}
public Integer getNoAdresseCpwin() {
	return noAdresseCpwin;
}
public void setNoAdresseCpwin(Integer noAdresseCpwin) {
	this.noAdresseCpwin = noAdresseCpwin;
}
public Trace getTrace() {
	return trace;
}
public void setTrace(Trace trace) {
	this.trace = trace;
}
public boolean isRegisseur() {
	return regisseur;
}
public void setRegisseur(boolean regisseur) {
	this.regisseur = regisseur;
}
public Tiers getTiersCpwin() {
	return tiersCpwin;
}

public void setTiersCpwin(Tiers tiersCpwin) {
	this.tiersCpwin = tiersCpwin;
	if(tiersCpwin==null)
		setCodeCpwin(null);	
	else 
		setCodeCpwin(tiersCpwin.getCode());

}
@Override
public String toString() {
	return "ClientGts [code=" + code + ", nom=" + nom + ", codeCpwin=" + codeCpwin + ", noAdresseCpwin="
			+ noAdresseCpwin + ", regisseur=" + regisseur + "]";
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
public Adresse getAdresse() {
	return adresse;
}
public void setAdresse(Adresse adresse) {
	this.adresse = adresse;
}
public String getRegis(){
	return isRegisseur()? OuiNonEnum.OUI.getDesignation():OuiNonEnum.NON.getDesignation();
}

}
