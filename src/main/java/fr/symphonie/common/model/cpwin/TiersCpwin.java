package fr.symphonie.common.model.cpwin;

import fr.symphonie.cpwin.model.Tiers;

public class TiersCpwin extends Tiers {
private String siret;
private String nomPersonne;
private String prenom;
public String getSiret() {
	return siret;
}
public void setSiret(String siret) {
	this.siret = siret;
}
public String getNomPersonne() {
	return nomPersonne;
}
public void setNomPersonne(String nomPersonne) {
	this.nomPersonne = nomPersonne;
}
public String getPrenom() {
	return prenom;
}
public void setPrenom(String prenom) {
	this.prenom = prenom;
}

}
