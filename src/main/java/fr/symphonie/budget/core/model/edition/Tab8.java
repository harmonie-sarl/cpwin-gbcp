package fr.symphonie.budget.core.model.edition;

import java.util.Arrays;
import java.util.List;

import fr.symphonie.budget.core.model.edition.util.DataItem;
import fr.symphonie.budget.core.model.edition.util.DataRefEnum;
import fr.symphonie.budget.core.model.edition.util.GenericTab;
import fr.symphonie.budget.core.model.edition.util.RecetteFleche;
import fr.symphonie.util.Helper;

public class Tab8 extends GenericTab{
private RecetteFleche finanDebutExec;
private RecetteFleche personnelAeCp;
private RecetteFleche fonctAE;
private RecetteFleche fonctCP;
private RecetteFleche interAE;
private RecetteFleche interCP;
private RecetteFleche invesAE;
private RecetteFleche invesCP;

private RecetteFleche autofinancement;
private RecetteFleche tresorerie;


private RecetteFleche  totalRecetFleche;
private RecetteFleche  recetR22;
private RecetteFleche  recetR24;
private RecetteFleche  recetR25;
private RecetteFleche  totDepRecetCp;
private RecetteFleche  soldBudgBC;
private RecetteFleche  posOpFlechFfinExec;


public Tab8(Edition e)  {
	super(e);
	finanDebutExec=new RecetteFleche(DataRefEnum.ref_01);
	this.personnelAeCp=new RecetteFleche(DataRefEnum.ref_02);
	this.fonctAE=new RecetteFleche(DataRefEnum.ref_03);
	this.fonctCP=new RecetteFleche(DataRefEnum.ref_04);
	this.interAE=new RecetteFleche(DataRefEnum.ref_05);
	this.interCP=new RecetteFleche(DataRefEnum.ref_06);
	this.invesAE=new RecetteFleche(DataRefEnum.ref_07);
	this.invesCP=new RecetteFleche(DataRefEnum.ref_08);
	this.autofinancement=new RecetteFleche(DataRefEnum.ref_09);
	this.tresorerie=new RecetteFleche(DataRefEnum.ref_10);
	
	this.totalRecetFleche=new RecetteFleche(DataRefEnum.ref_11);
	this.recetR22=new RecetteFleche(DataRefEnum.ref_12);
	this.recetR24=new RecetteFleche(DataRefEnum.ref_13);
	this.recetR25=new RecetteFleche(DataRefEnum.ref_14);
	this.totDepRecetCp=new RecetteFleche(DataRefEnum.ref_15);
	this.soldBudgBC=new RecetteFleche(DataRefEnum.ref_16);
	this.posOpFlechFfinExec=new RecetteFleche(DataRefEnum.ref_17);
}
@Override
public void setDefaults() {
	//getTotalRecetFleche().getN().setMontantDouble(getParent().getTotalRecetFlecheN());
	getRecetR22(). getN().setMontantDouble(getParent().getRecetR22());
	getRecetR24(). getN().setMontantDouble(getParent().getRecetR24());
	getRecetR25().getN().setMontantDouble(getParent().getRecetR25());
	//getTotDepRecetCp(). getN().setMontantDouble(getParent().getTotDepRecetCp());
	//getSoldBudgBC().getN().setMontantDouble(getParent().getSoldBudgBC());
	getPosOpFlechFfinExec(). getN().setMontantDouble(getParent().getPosOpFlechFfinExec());
	
	setTotaux();
	
	
}
public void setTotaux(){
	double b=0,c=0;
	b=getRecetR22().getN().getMontantDouble()+getRecetR24().getN().getMontantDouble()+getRecetR25().getN().getMontantDouble();
	getTotalRecetFleche().getN().setMontantDouble(Helper.round(b, 2));
	c=getPersonnelAeCp().getN().getMontantDouble()+getFonctCP().getN().getMontantDouble()+getInterCP().getN().getMontantDouble()+getInvesCP().getN().getMontantDouble();
	
	getTotDepRecetCp().getN().setMontantDouble(Helper.round(c, 2));
	getSoldBudgBC().getN().setMontantDouble(Helper.round(b-c, 2));
}

public RecetteFleche getFinanDebutExec() {
	return finanDebutExec;
}
public void setFinanDebutExec(RecetteFleche finanDebutExec) {
	this.finanDebutExec = finanDebutExec;
}
public RecetteFleche getPersonnelAeCp() {
	return personnelAeCp;
}
public void setPersonnelAeCp(RecetteFleche personnelAeCp) {
	this.personnelAeCp = personnelAeCp;
}
public RecetteFleche getFonctAE() {
	return fonctAE;
}
public void setFonctAE(RecetteFleche fonctAE) {
	this.fonctAE = fonctAE;
}
public RecetteFleche getFonctCP() {
	return fonctCP;
}
public void setFonctCP(RecetteFleche fonctCP) {
	this.fonctCP = fonctCP;
}
public RecetteFleche getInterAE() {
	return interAE;
}
public void setInterAE(RecetteFleche interAE) {
	this.interAE = interAE;
}
public RecetteFleche getInterCP() {
	return interCP;
}
public void setInterCP(RecetteFleche interCP) {
	this.interCP = interCP;
}
public RecetteFleche getInvesAE() {
	return invesAE;
}
public void setInvesAE(RecetteFleche invesAE) {
	this.invesAE = invesAE;
}
public RecetteFleche getInvesCP() {
	return invesCP;
}
public void setInvesCP(RecetteFleche invesCP) {
	this.invesCP = invesCP;
}
public RecetteFleche getAutofinancement() {
	return autofinancement;
}
public void setAutofinancement(RecetteFleche autofinancement) {
	this.autofinancement = autofinancement;
}
public RecetteFleche getTresorerie() {
	return tresorerie;
}
public void setTresorerie(RecetteFleche tresorerie) {
	this.tresorerie = tresorerie;
}
@Override
public List<DataItem> getItems() {
	DataItem[] tab = new DataItem[] {
			getFinanDebutExec().getN(),
			getPersonnelAeCp().getN(),
			getFonctAE().getN(),
			getFonctCP().getN(),
			getInterAE().getN(),
			getInterCP().getN(),
			getInvesAE().getN(),
			getInvesCP().getN(),
			getAutofinancement().getN(),
			getTresorerie().getN(),
			getTotalRecetFleche().getN(),
			getRecetR22().getN(),
			getRecetR24().getN(),
			getRecetR25().getN(),
			getTotDepRecetCp().getN(),
			getSoldBudgBC().getN(),
			getPosOpFlechFfinExec().getN()
			
	};
	
	return Arrays.asList(tab);
}
public RecetteFleche getTotalRecetFleche() {
	return totalRecetFleche;
}
public void setTotalRecetFleche(RecetteFleche totalRecetFleche) {
	this.totalRecetFleche = totalRecetFleche;
}
public RecetteFleche getRecetR22() {
	return recetR22;
}
public void setRecetR22(RecetteFleche recetR22) {
	this.recetR22 = recetR22;
}
public RecetteFleche getRecetR24() {
	return recetR24;
}
public void setRecetR24(RecetteFleche recetR24) {
	this.recetR24 = recetR24;
}
public RecetteFleche getRecetR25() {
	return recetR25;
}
public void setRecetR25(RecetteFleche recetR25) {
	this.recetR25 = recetR25;
}
public RecetteFleche getTotDepRecetCp() {
	return totDepRecetCp;
}
public void setTotDepRecetCp(RecetteFleche totDepRecetCp) {
	this.totDepRecetCp = totDepRecetCp;
}
public RecetteFleche getSoldBudgBC() {
	return soldBudgBC;
}
public void setSoldBudgBC(RecetteFleche soldBudgBC) {
	this.soldBudgBC = soldBudgBC;
}
public RecetteFleche getPosOpFlechFfinExec() {
	return posOpFlechFfinExec;
}
public void setPosOpFlechFfinExec(RecetteFleche posOpFlechFfinExec) {
	this.posOpFlechFfinExec = posOpFlechFfinExec;
}


}
