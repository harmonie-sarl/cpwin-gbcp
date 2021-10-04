package fr.symphonie.budget.core.model.edition.util;

import java.util.Arrays;
import java.util.List;

import fr.symphonie.budget.core.model.edition.Edition;
import fr.symphonie.util.Helper;

public class TFP  extends GenericTab{
	private DataItem insufAutofinanc;
	private DataItem capaciteAutofinanc;
	private DataItem depInvestCp;
	private DataItem financActifEtat;
	private DataItem financActifTiersAutre;
	private DataItem autreRessource;

	private DataItem rembCautions;
	private DataItem rembDettes;
	private DataItem cautionRecu;
	private DataItem augDettes;

	private DataItem totalEmploi;
	private DataItem totalRessource;
	private DataItem apportFondRoulement;
	private DataItem prelevFondRoulement;
	
	
public TFP(Edition e) {	
		super(e);

		insufAutofinanc = new DataItem(DataRefEnum.ref_01);
		capaciteAutofinanc = new DataItem(DataRefEnum.ref_05);
		depInvestCp = new DataItem(DataRefEnum.ref_02);
		financActifEtat = new DataItem(DataRefEnum.ref_06);
		financActifTiersAutre = new DataItem(DataRefEnum.ref_07);
		autreRessource = new DataItem(DataRefEnum.ref_08);

		rembCautions = new DataItem(DataRefEnum.ref_13);
		cautionRecu = new DataItem(DataRefEnum.ref_14);		
		rembDettes = new DataItem(DataRefEnum.ref_03);	
		augDettes = new DataItem(DataRefEnum.ref_09);

		totalEmploi = new DataItem(DataRefEnum.ref_04);
		totalRessource = new DataItem(DataRefEnum.ref_10);
		apportFondRoulement = new DataItem(DataRefEnum.ref_11);
		prelevFondRoulement = new DataItem(DataRefEnum.ref_12);
}
@Override
public void setDefaults() {
	setvalues(getParent().getTab6InsufAutofinanc(), getParent().getTab6CapaciteAutofinanc(), getParent().getDepInvestCp());
//	getInsufAutofinanc().setMontantDouble(getParent().getTab6InsufAutofinanc());
//	getCapaciteAutofinanc().setMontantDouble(getParent().getTab6CapaciteAutofinanc());
//	getDepInvestCp().setMontantDouble(getParent().getDepInvestCp());

	
//	getTotalEmploi().setMontantDouble(getParent(). getTab6TotalEmploi());
//	getTotalRessource().setMontantDouble(getParent().getTab6TotalRessource());
//	getApportFondRoulement().setMontantDouble(getParent().getTab6apportFondRoulement());
//	getPrelevFondRoulement().setMontantDouble(getParent().getTab6PrelevFondRoulement());	
	refreshTotaux();
	
}
public void setvalues(double insufAutofinanc,double capaciteAutofinanc,double depInvestCp ){
	getInsufAutofinanc().setMontantDouble(insufAutofinanc);
	getCapaciteAutofinanc().setMontantDouble(capaciteAutofinanc);
	getDepInvestCp().setMontantDouble(depInvestCp);
}
public DataItem getRembCautions() {
	return rembCautions;
}
public void setRembCautions(DataItem rembCautions) {
	this.rembCautions = rembCautions;
}
public DataItem getRembDettes() {
	return rembDettes;
}
public void setRembDettes(DataItem rembDettes) {
	this.rembDettes = rembDettes;
}
public DataItem getCautionRecu() {
	return cautionRecu;
}
public void setCautionRecu(DataItem cautionRecu) {
	this.cautionRecu = cautionRecu;
}
public DataItem getAugDettes() {
	return augDettes;
}
public void setAugDettes(DataItem augDettes) {
	this.augDettes = augDettes;
}
public DataItem getInsufAutofinanc() {
	return insufAutofinanc;
}
public void setInsufAutofinanc(DataItem insufAutofinanc) {
	this.insufAutofinanc = insufAutofinanc;
}
public DataItem getCapaciteAutofinanc() {
	return capaciteAutofinanc;
}
public void setCapaciteAutofinanc(DataItem capaciteAutofinanc) {
	this.capaciteAutofinanc = capaciteAutofinanc;
}
public DataItem getDepInvestCp() {
	return depInvestCp;
}
public void setDepInvestCp(DataItem depInvestCp) {
	this.depInvestCp = depInvestCp;
}
public DataItem getFinancActifEtat() {
	return financActifEtat;
}
public void setFinancActifEtat(DataItem financActifEtat) {
	this.financActifEtat = financActifEtat;
}
public DataItem getFinancActifTiersAutre() {
	return financActifTiersAutre;
}
public void setFinancActifTiersAutre(DataItem financActifTiersAutre) {
	this.financActifTiersAutre = financActifTiersAutre;
}
public DataItem getTotalEmploi() {
	return totalEmploi;
}
public void setTotalEmploi(DataItem totalEmploi) {
	this.totalEmploi = totalEmploi;
}
public DataItem getTotalRessource() {
	return totalRessource;
}
public void setTotalRessource(DataItem totalRessource) {
	this.totalRessource = totalRessource;
}
public DataItem getApportFondRoulement() {
	return apportFondRoulement;
}
public void setApportFondRoulement(DataItem apportFondRoulement) {
	this.apportFondRoulement = apportFondRoulement;
}
public DataItem getPrelevFondRoulement() {
	return prelevFondRoulement;
}
public void setPrelevFondRoulement(DataItem prelevFondRoulement) {
	this.prelevFondRoulement = prelevFondRoulement;
}


@Override
public List<DataItem> getItems() {
	DataItem[] tab = new DataItem[] { insufAutofinanc, capaciteAutofinanc, depInvestCp, financActifEtat,
			financActifTiersAutre,autreRessource, rembCautions, rembDettes, cautionRecu, augDettes, totalEmploi, totalRessource,
			apportFondRoulement, prelevFondRoulement };
	return Arrays.asList(tab);
}
public DataItem getAutreRessource() {
	return autreRessource;
}
public void setAutreRessource(DataItem autreRessource) {
	this.autreRessource = autreRessource;
}
public void refreshTotaux(){
	double totalEmploi=getInsufAutofinanc().getMontantDouble()+getDepInvestCp().getMontantDouble()+getRembCautions().getMontantDouble()+getRembDettes().getMontantDouble();
	double totalRessource=getCapaciteAutofinanc().getMontantDouble()+getFinancActifEtat().getMontantDouble()+getFinancActifTiersAutre().getMontantDouble()+getAutreRessource().getMontantDouble()+
			getCautionRecu().getMontantDouble()+getAugDettes().getMontantDouble();
	
	
	getTotalEmploi().setMontantDouble(Helper.round(totalEmploi, 2));
	getTotalRessource().setMontantDouble(Helper.round(totalRessource, 2));
	double ecart=getTotalRessource().getMontantDouble()-getTotalEmploi().getMontantDouble();
	getApportFondRoulement().setMontantDouble(ecart>0?ecart:0);
	getPrelevFondRoulement().setMontantDouble(ecart<0?-ecart:0);
	VFR vfr=getParent().getTab6().getVfr();
	vfr.getVariationFondRoul().setMontantDouble(ecart);
	
}

}
