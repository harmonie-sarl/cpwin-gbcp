package fr.symphonie.budget.core.model.edition.util;

import java.util.Arrays;
import java.util.List;

import fr.symphonie.budget.core.model.edition.Edition;
import fr.symphonie.util.Helper;

public class CRP   extends GenericTab{
	
	 private DataItem depPersCp;
	 private DataItem subvEtat;
	 private DataItem chargePensCivile;	
	 private DataItem fiscAffect;
	 private DataItem autreFoncPersonnel;
	 private DataItem autresSubv;
	 private DataItem intervCasEcheant;
	 private DataItem autresProduit;
	 private DataItem totCharges;
	 private DataItem totProduits;
	 private DataItem resultPrevBenifce;
	 private DataItem resultPrevPerte;
	 private DataItem totalEquilibreCharge;
	 private DataItem totalEquilibreProduit;
	 
	 

	public CRP(Edition e) {
		 super(e);			
		 this.depPersCp = new DataItem(DataRefEnum.ref_01);
		 this.subvEtat = new DataItem(DataRefEnum.ref_05);
		 this.chargePensCivile = new DataItem(DataRefEnum.ref_02);	
		 this.fiscAffect = new DataItem(DataRefEnum.ref_06);
		 this.autreFoncPersonnel = new DataItem(DataRefEnum.ref_03);
		 this.autresSubv = new DataItem(DataRefEnum.ref_07);	
		 this.intervCasEcheant=  new DataItem(DataRefEnum.ref_04);
		 this.autresProduit = new DataItem(DataRefEnum.ref_08);
		 this.totCharges = new DataItem(DataRefEnum.ref_09);
		 this.totProduits = new DataItem(DataRefEnum.ref_10);
		 this.resultPrevBenifce = new DataItem(DataRefEnum.ref_11);
		 this.resultPrevPerte = new DataItem(DataRefEnum.ref_12);
		 this.totalEquilibreCharge = new DataItem(DataRefEnum.ref_13);
		 this.totalEquilibreProduit = new DataItem(DataRefEnum.ref_14);
	}
	@Override
	public void setDefaults() {
		getDepPersCp().setMontantDouble(getParent().getDepPersCp());
		getSubvEtat().setMontantDouble(getParent().getRecetR11()+getParent().getRecetR22());
		getFiscAffect().setMontantDouble(getParent().getRecetR13());
		getAutreFoncPersonnel().setMontantDouble(getParent().getRecetR24()+getParent().getDepFoncCp());
		getAutresSubv().setMontantDouble(getParent().getRecetR14());
		getIntervCasEcheant().setMontantDouble(getParent().getDepIntervCp());
		getAutresProduit().setMontantDouble(getParent().getRecetR15()+getParent().getRecetR25()-getParent().getNoDataBudg5());
		getTotCharges().setMontantDouble(getParent().getTab6TotalCharges());
		getTotProduits().setMontantDouble(getParent().getTab6TotalProduits());
		getResultPrevBenifce().setMontantDouble(getParent().getTab6ResultPrevBenifce());
		getResultPrevPerte().setMontantDouble(getParent().getTab6ResultPrevPerte());
		getTotalEquilibreCharge().setMontantDouble(getParent().getTab6TotalEquilibreCharge());
		getTotalEquilibreProduit().setMontantDouble(getParent().getTab6TotalEquilibreProduit());
		
	}
	

	public DataItem getChargePensCivile() {
		return chargePensCivile;
	}

	public void setChargePensCivile(DataItem chargePensCivile) {
		this.chargePensCivile = chargePensCivile;
	}

	public DataItem getDepPersCp() {
		return depPersCp;
	}

	public void setDepPersCp1(DataItem depPersCp) {
		this.depPersCp = depPersCp;
	}

	public DataItem getSubvEtat() {
		return subvEtat;
	}

	public void setSubvEtat(DataItem subvEtat) {
		this.subvEtat = subvEtat;
	}

	public DataItem getFiscAffect() {
		return fiscAffect;
	}

	public void setFiscAffect(DataItem fiscAffect) {
		this.fiscAffect = fiscAffect;
	}

	public DataItem getAutreFoncPersonnel() {
		return autreFoncPersonnel;
	}

	public void setAutreFoncPersonnel(DataItem autreFoncPersonnel) {
		this.autreFoncPersonnel = autreFoncPersonnel;
	}

	public DataItem getAutresSubv() {
		return autresSubv;
	}

	public void setAutresSubv(DataItem autresSubv) {
		this.autresSubv = autresSubv;
	}

	public DataItem getAutresProduit() {
		return autresProduit;
	}

	public void setAutresProduit(DataItem autresProduit) {
		this.autresProduit = autresProduit;
	}

	public DataItem getTotCharges() {
		return totCharges;
	}

	public void setTotCharges(DataItem totCharges) {
		this.totCharges = totCharges;
	}

	public DataItem getTotProduits() {
		return totProduits;
	}

	public void setTotProduits(DataItem totProduits) {
		this.totProduits = totProduits;
	}

	public DataItem getResultPrevBenifce() {
		return resultPrevBenifce;
	}

	public void setResultPrevBenifce(DataItem resultPrevBenifce) {
		this.resultPrevBenifce = resultPrevBenifce;
	}

	public DataItem getResultPrevPerte() {
		return resultPrevPerte;
	}

	public void setResultPrevPerte(DataItem resultPrevPerte) {
		this.resultPrevPerte = resultPrevPerte;
	}

	public DataItem getTotalEquilibreCharge() {
		return totalEquilibreCharge;
	}

	public void setTotalEquilibreCharge(DataItem totalEquilibreCharge) {
		this.totalEquilibreCharge = totalEquilibreCharge;
	}

	public DataItem getTotalEquilibreProduit() {
		return totalEquilibreProduit;
	}

	public void setTotalEquilibreProduit(DataItem totalEquilibreProduit) {
		this.totalEquilibreProduit = totalEquilibreProduit;
	}

	
	@Override
	public List<DataItem> getItems() {
		DataItem[] tab = new DataItem[] { chargePensCivile, depPersCp, subvEtat, fiscAffect, autreFoncPersonnel,
				autresSubv,intervCasEcheant, autresProduit, totCharges, totProduits, resultPrevBenifce, resultPrevPerte,
				totalEquilibreCharge, totalEquilibreProduit };
		return Arrays.asList(tab);
	}
	public DataItem getIntervCasEcheant() {
		return intervCasEcheant;
	}
	public void setIntervCasEcheant(DataItem intervCasEcheant) {
		this.intervCasEcheant = intervCasEcheant;
	}
	public void refreshTotaux(){
		double charge=getDepPersCp().getMontantDouble()+getAutreFoncPersonnel().getMontantDouble()+getIntervCasEcheant().getMontantDouble();
		double produit=getSubvEtat().getMontantDouble()+getFiscAffect().getMontantDouble()+getAutresSubv().getMontantDouble()+getAutresProduit().getMontantDouble();
		getTotCharges().setMontantDouble(Helper.round(charge, 2));
		getTotProduits().setMontantDouble(Helper.round(produit, 2));
		double benefice=getTotProduits().getMontantDouble()-getTotCharges().getMontantDouble();
		getResultPrevBenifce().setMontantDouble(benefice>0?benefice:0);
		getResultPrevPerte().setMontantDouble(benefice>0?0:-benefice);
		getTotalEquilibreCharge().setMontantDouble(getTotCharges().getMontantDouble()+getResultPrevBenifce().getMontantDouble());
		getTotalEquilibreProduit().setMontantDouble(getTotProduits().getMontantDouble()+getResultPrevPerte().getMontantDouble());
	}

}
