package fr.symphonie.tools.nantes.model;

import java.math.BigDecimal;
import java.util.Optional;

import org.apache.commons.collections.CollectionUtils;

import fr.symphonie.cpwin.model.Iban;
import fr.symphonie.cpwin.model.Tiers;

public class Operation {
//private Etudiant actor;
private Tiers actor;
private BigDecimal amount;
private Integer noDp;
private String libelle;
private String modePaie;

public Operation() {
	super();
}

public Operation(Tiers actor, BigDecimal amount,String libelle) {
	this();
	this.actor = actor;
	this.amount = amount;
	this.libelle=libelle;
}

public Tiers getActor() {
	return actor;
}
public void setActor(Tiers actor) {
	this.actor = actor;
}
public BigDecimal getAmount() {
	return amount;
}
public void setAmount(BigDecimal amount) {
	this.amount = amount;
}

public Integer getNoDp() {
	return noDp;
}

public void setNoDp(Integer noDp) {
	this.noDp = noDp;
}

public String getLibelle() {
	return libelle;
}

public void setLibelle(String libelle) {
	this.libelle = libelle;
}

public String getModePaie() {
	return modePaie;
}

public void setModePaie(String modePaie) {
	this.modePaie = modePaie;
}
public Iban getIban() {
	if(getActor()==null)return null;
	if(CollectionUtils.isEmpty(getActor().getIbanList()))return null;
	Optional<Iban> iban= getActor().getIbanList().stream().filter(b-> b.isOuvert() && b.isValide()).findFirst();
	return iban.isPresent()?iban.get():null;
}

}
