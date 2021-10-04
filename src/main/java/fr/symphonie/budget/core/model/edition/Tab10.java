package fr.symphonie.budget.core.model.edition;

import java.util.Arrays;
import java.util.List;

import fr.symphonie.budget.core.model.edition.util.DataItem;
import fr.symphonie.budget.core.model.edition.util.DataRefEnum;
import fr.symphonie.budget.core.model.edition.util.GenericTab;

public class Tab10 extends GenericTab {
	
	private DataItem nivInitResteApayer;
	private DataItem nivInitFondRoulement;
	private DataItem besoinFondRoul;
	private DataItem nivInitTresorie;
	private DataItem nivInitTresorieFleche;
	private DataItem nivInitTresorieNonFlech;
	private DataItem autorEngag;
	private DataItem resultatPatrimonial;
	private DataItem caf;
	private DataItem varFondRoul;
	private DataItem opDetteFinanc;
	private DataItem opCompteSansFlux;
	private DataItem variationStock;
	private DataItem chargeCreanceIrrecouvrable;
	private DataItem produitDiversGestCourante;
	private DataItem opBudgComptaDiffr;
	private DataItem ecartProduitAnterieure;
	private DataItem ecartProduitEncours;
	private DataItem ecartChargeAnterieure;
	private DataItem ecartChargeEncours;
	private DataItem soldebudg;
	private DataItem decalageFluxNoBudg;
	private DataItem variationTresorie;
	private DataItem variTresorFleche;
	private DataItem variTresorNonFleche;
	private DataItem variBesoinFondRoul;
	private DataItem varRestPay;
	private DataItem nivFinalRestPay;
	private DataItem nivFinalFondRoul;
	private DataItem nivFinalBesoinFondRoul;
	private DataItem nivFinalTresorie;
	private DataItem nivFinalTresFlech;
	private DataItem nivFinalTresNonFlech;
	
	
	
	
	
	public Tab10(Edition e) {
		super(e);
		this.nivInitResteApayer=new DataItem(DataRefEnum.ref_01);
		this.nivInitFondRoulement=new DataItem(DataRefEnum.ref_02);
		this.besoinFondRoul=new DataItem(DataRefEnum.ref_03);
		this.nivInitTresorie=new DataItem(DataRefEnum.ref_04);
		this.nivInitTresorieFleche=new DataItem(DataRefEnum.ref_05);
		this.nivInitTresorieNonFlech=new DataItem(DataRefEnum.ref_06);
		this.autorEngag=new DataItem(DataRefEnum.ref_07);
		this.resultatPatrimonial=new DataItem(DataRefEnum.ref_08);
		this.caf=new DataItem(DataRefEnum.ref_09);
		this.varFondRoul=new DataItem(DataRefEnum.ref_10);
		this.opDetteFinanc=new DataItem(DataRefEnum.ref_11);
		this.opCompteSansFlux=new DataItem(DataRefEnum.ref_12);
		this.variationStock=new DataItem(DataRefEnum.ref_13);
		this.chargeCreanceIrrecouvrable=new DataItem(DataRefEnum.ref_14);
		this.produitDiversGestCourante=new DataItem(DataRefEnum.ref_15);
		this.opBudgComptaDiffr=new DataItem(DataRefEnum.ref_16);
		this.ecartProduitAnterieure=new DataItem(DataRefEnum.ref_17);
		this.ecartProduitEncours=new DataItem(DataRefEnum.ref_18);
		this.ecartChargeAnterieure=new DataItem(DataRefEnum.ref_19);
		this.ecartChargeEncours=new DataItem(DataRefEnum.ref_20);
		this.soldebudg=new DataItem(DataRefEnum.ref_21);
		this.decalageFluxNoBudg=new DataItem(DataRefEnum.ref_22);
		this.variationTresorie=new DataItem(DataRefEnum.ref_23);
		this.variTresorFleche=new DataItem(DataRefEnum.ref_24);
		this.variTresorNonFleche=new DataItem(DataRefEnum.ref_25);
		this.variBesoinFondRoul=new DataItem(DataRefEnum.ref_26);
		this.varRestPay=new DataItem(DataRefEnum.ref_27);
		this.nivFinalRestPay=new DataItem(DataRefEnum.ref_28);
		this.nivFinalFondRoul=new DataItem(DataRefEnum.ref_29);
		this.nivFinalBesoinFondRoul=new DataItem(DataRefEnum.ref_30);
		this.nivFinalTresorie=new DataItem(DataRefEnum.ref_31);
		this.nivFinalTresFlech=new DataItem(DataRefEnum.ref_32);
		this.nivFinalTresNonFlech=new DataItem(DataRefEnum.ref_33);
	} 
	public DataItem getBesoinFondRoul() {
		return besoinFondRoul;
	}
	public void setBesoinFondRoul(DataItem besoinFondRoul) {
		this.besoinFondRoul = besoinFondRoul;
	}
	public DataItem getNivInitTresorieNonFlech() {
		return nivInitTresorieNonFlech;
	}
	public void setNivInitTresorieNonFlech(DataItem nivInitTresorieNonFlech) {
		this.nivInitTresorieNonFlech = nivInitTresorieNonFlech;
	}
	
	public DataItem getResultatPatrimonial() {
		return resultatPatrimonial;
	}
	public void setResultatPatrimonial(DataItem resultatPatrimonial) {
		this.resultatPatrimonial = resultatPatrimonial;
	}
	public DataItem getCaf() {
		return caf;
	}
	public void setCaf(DataItem caf) {
		this.caf = caf;
	}
	public DataItem getVarFondRoul() {
		return varFondRoul;
	}
	public void setVarFondRoul(DataItem varFondRoul) {
		this.varFondRoul = varFondRoul;
	}
	public DataItem getOpDetteFinanc() {
		return opDetteFinanc;
	}
	public void setOpDetteFinanc(DataItem opDetteFinanc) {
		this.opDetteFinanc = opDetteFinanc;
	}
	public DataItem getOpCompteSansFlux() {
		return opCompteSansFlux;
	}
	public void setOpCompteSansFlux(DataItem opCompteSansFlux) {
		this.opCompteSansFlux = opCompteSansFlux;
	}
	public DataItem getOpBudgComptaDiffr() {
		return opBudgComptaDiffr;
	}
	public void setOpBudgComptaDiffr(DataItem opBudgComptaDiffr) {
		this.opBudgComptaDiffr = opBudgComptaDiffr;
	}
	public DataItem getSoldebudg() {
		return soldebudg;
	}
	public void setSoldebudg(DataItem soldebudg) {
		this.soldebudg = soldebudg;
	}
	public DataItem getDecalageFluxNoBudg() {
		return decalageFluxNoBudg;
	}
	public void setDecalageFluxNoBudg(DataItem decalageFluxNoBudg) {
		this.decalageFluxNoBudg = decalageFluxNoBudg;
	}
	public DataItem getVariationTresorie() {
		return variationTresorie;
	}
	public void setVariationTresorie(DataItem variationTresorie) {
		this.variationTresorie = variationTresorie;
	}
	public DataItem getVariTresorFleche() {
		return variTresorFleche;
	}
	public void setVariTresorFleche(DataItem variTresorFleche) {
		this.variTresorFleche = variTresorFleche;
	}
	public DataItem getVariTresorNonFleche() {
		return variTresorNonFleche;
	}
	public void setVariTresorNonFleche(DataItem variTresorNonFleche) {
		this.variTresorNonFleche = variTresorNonFleche;
	}
	public DataItem getVariBesoinFondRoul() {
		return variBesoinFondRoul;
	}
	public void setVariBesoinFondRoul(DataItem variBesoinFondRoul) {
		this.variBesoinFondRoul = variBesoinFondRoul;
	}
	public DataItem getVarRestPay() {
		return varRestPay;
	}
	public void setVarRestPay(DataItem varRestPay) {
		this.varRestPay = varRestPay;
	}
	public DataItem getNivFinalRestPay() {
		return nivFinalRestPay;
	}
	public void setNivFinalRestPay(DataItem nivFinalRestPay) {
		this.nivFinalRestPay = nivFinalRestPay;
	}
	public DataItem getNivFinalFondRoul() {
		return nivFinalFondRoul;
	}
	public void setNivFinalFondRoul(DataItem nivFinalFondRoul) {
		this.nivFinalFondRoul = nivFinalFondRoul;
	}
	public DataItem getNivFinalBesoinFondRoul() {
		return nivFinalBesoinFondRoul;
	}
	public void setNivFinalBesoinFondRoul(DataItem nivFinalBesoinFondRoul) {
		this.nivFinalBesoinFondRoul = nivFinalBesoinFondRoul;
	}
	public DataItem getNivFinalTresorie() {
		return nivFinalTresorie;
	}
	public void setNivFinalTresorie(DataItem nivFinalTresorie) {
		this.nivFinalTresorie = nivFinalTresorie;
	}
	public DataItem getNivFinalTresFlech() {
		return nivFinalTresFlech;
	}
	public void setNivFinalTresFlech(DataItem nivFinalTresFlech) {
		this.nivFinalTresFlech = nivFinalTresFlech;
	}
	public DataItem getNivFinalTresNonFlech() {
		return nivFinalTresNonFlech;
	}
	public void setNivFinalTresNonFlech(DataItem nivFinalTresNonFlech) {
		this.nivFinalTresNonFlech = nivFinalTresNonFlech;
	}
	
	public DataItem getEcartProduitAnterieure() {
		return ecartProduitAnterieure;
	}
	public void setEcartProduitAnterieure(DataItem ecartProduitAnterieure) {
		this.ecartProduitAnterieure = ecartProduitAnterieure;
	}
	public DataItem getEcartProduitEncours() {
		return ecartProduitEncours;
	}
	public void setEcartProduitEncours(DataItem ecartProduitEncours) {
		this.ecartProduitEncours = ecartProduitEncours;
	}
	public DataItem getEcartChargeAnterieure() {
		return ecartChargeAnterieure;
	}
	public void setEcartChargeAnterieure(DataItem ecartChargeAnterieure) {
		this.ecartChargeAnterieure = ecartChargeAnterieure;
	}
	public DataItem getEcartChargeEncours() {
		return ecartChargeEncours;
	}
	public void setEcartChargeEncours(DataItem ecartChargeEncours) {
		this.ecartChargeEncours = ecartChargeEncours;
	}
	public DataItem getNivInitResteApayer() {
		return nivInitResteApayer;
	}
	public void setNivInitResteApayer(DataItem nivInitResteApayer) {
		this.nivInitResteApayer = nivInitResteApayer;
	}
	public DataItem getNivInitFondRoulement() {
		return nivInitFondRoulement;
	}
	public void setNivInitFondRoulement(DataItem nivInitFondRoulement) {
		this.nivInitFondRoulement = nivInitFondRoulement;
	}
	public DataItem getNivInitTresorie() {
		return nivInitTresorie;
	}
	public void setNivInitTresorie(DataItem nivInitTresorie) {
		this.nivInitTresorie = nivInitTresorie;
	}
	public DataItem getNivInitTresorieFleche() {
		return nivInitTresorieFleche;
	}
	public void setNivInitTresorieFleche(DataItem nivInitTresorieFleche) {
		this.nivInitTresorieFleche = nivInitTresorieFleche;
	}
	public DataItem getVariationStock() {
		return variationStock;
	}
	public void setVariationStock(DataItem variationStock) {
		this.variationStock = variationStock;
	}
	public DataItem getChargeCreanceIrrecouvrable() {
		return chargeCreanceIrrecouvrable;
	}
	public void setChargeCreanceIrrecouvrable(DataItem chargeCreanceIrrecouvrable) {
		this.chargeCreanceIrrecouvrable = chargeCreanceIrrecouvrable;
	}
	public DataItem getProduitDiversGestCourante() {
		return produitDiversGestCourante;
	}
	public void setProduitDiversGestCourante(DataItem produitDiversGestCourante) {
		this.produitDiversGestCourante = produitDiversGestCourante;
	}

	@Override
	public List<DataItem> getItems() {
		DataItem[] tab = new DataItem[] {
			     nivInitResteApayer,
				 nivInitFondRoulement,
				 besoinFondRoul,
				 nivInitTresorie,
				 nivInitTresorieFleche,
				 nivInitTresorieNonFlech,
				 autorEngag,
				 resultatPatrimonial,
				 caf,
				 varFondRoul,
				 opDetteFinanc,
				 opCompteSansFlux,
				 variationStock,
				 chargeCreanceIrrecouvrable,
				 produitDiversGestCourante,
				 opBudgComptaDiffr,
				 ecartProduitAnterieure,
				 ecartProduitEncours,
				 ecartChargeAnterieure,
				 ecartChargeEncours,
				 soldebudg,
				 decalageFluxNoBudg,
				 variationTresorie,
				 variTresorFleche,
				 variTresorNonFleche,
				 variBesoinFondRoul,
				 varRestPay,
				 nivFinalRestPay,
				 nivFinalFondRoul,
				 nivFinalBesoinFondRoul,
				 nivFinalTresorie,
				 nivFinalTresFlech,
				 nivFinalTresNonFlech
		};
		
		return Arrays.asList(tab);
	}
	public DataItem getAutorEngag() {
		return autorEngag;
	}
	public void setAutorEngag(DataItem autorEngag) {
		this.autorEngag = autorEngag;
	}
	
	@Override
	public void setDefaults(){
		
		
		getBesoinFondRoul().setMontantDouble(getParent().getBesoinFondRoul());
		getNivInitTresorieNonFlech().setMontantDouble(getParent().getNivInitTresorieNonFlech());
		getAutorEngag().setMontantDouble(getParent().getTotalDepAe());
		getResultatPatrimonial().setMontantDouble(getParent().getResultatPatrimonial());
		getCaf().setMontantDouble(getParent().getTab6CapaciteAutofinanc());
		getVarFondRoul().setMontantDouble(getParent().getVarFondRoul());
		getOpDetteFinanc().setMontantDouble(getParent().getOpDetteFinanc());
		getOpCompteSansFlux().setMontantDouble(getParent().getOpCompteSansFlux());
		getOpBudgComptaDiffr().setMontantDouble(getParent().getOpBudgComptaDiffr());
		getSoldebudg().setMontantDouble(getParent().getSoldebudg());
		getDecalageFluxNoBudg().setMontantDouble(getParent().getDecalageFluxNoBudg());
		getVariationTresorie().setMontantDouble(getParent().getVariationTresorie());
		getVariTresorFleche().setMontantDouble(getParent().getVariTresorFleche());
		getVariTresorNonFleche().setMontantDouble(getParent().getVariTresorNonFleche());
		getVariBesoinFondRoul().setMontantDouble(getParent().getVariBesoinFondRoul());
		getVarRestPay().setMontantDouble(getParent().getVarRestPay());
		getNivFinalRestPay().setMontantDouble(getParent().getNivFinalRestPay());
		getNivFinalFondRoul().setMontantDouble(getParent().getNivFinalFondRoul());
		getNivFinalBesoinFondRoul().setMontantDouble(getParent().getNivFinalBesoinFondRoul());
		getNivFinalTresorie().setMontantDouble(getParent().getNivFinalTresorie());
		getNivFinalTresFlech().setMontantDouble(getParent().getNivFinalTresFlech());
		getNivFinalTresNonFlech().setMontantDouble(getParent().getNivFinalTresNonFlech());
		
		
	}
	

}
