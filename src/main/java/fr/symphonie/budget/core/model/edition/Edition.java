package fr.symphonie.budget.core.model.edition;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import javax.persistence.Transient;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.symphonie.budget.core.model.edition.util.ABE;
import fr.symphonie.budget.core.model.edition.util.DataItem;
import fr.symphonie.budget.core.model.edition.util.Depence;
import fr.symphonie.budget.core.model.edition.util.EFE;
import fr.symphonie.budget.core.model.edition.util.Recette;
import fr.symphonie.budget.core.model.edition.util.TFP;
import fr.symphonie.budget.core.model.plt.DetailLigneTresorerie;
import fr.symphonie.budget.core.model.plt.PlanTresorerie;

public class Edition {
	
	private Tab2 tab2;
	private Tab3 tab3;
	private Tab5 tab5;
	private Tab4 tab4;
	private Tab6 tab6;
	private PlanTresorerie<DetailLigneTresorerie> tab7;
	private Tab8 tab8;
	private Tab10 tab10;
	private ABE abe;
	private EFE efe;
    private String nomConfig;
    private Integer exercice;
    private String codeBudget;
    private Integer niveau;
    private Bilan bilan;
    private CR cr;

    /**
     * Attribut loaded
     * peut être utilisé pour 
     * dire que cet objet est
     * récemment créé/chargé/autre
     * selon besoin
     */
    @Transient
    private boolean loaded=false ;
   

	public boolean isLoaded() {
		return loaded;
	}
	public void setLoaded(boolean loaded) {
		this.loaded = loaded;
	}
	private static final Logger logger = LoggerFactory.getLogger(Edition.class);
    
	public Edition() {
		super();
		this.tab2=new Tab2(this);
		this.setTab3(new Tab3(this));
		this.tab5=new Tab5(this);
		this.tab4=new Tab4(this);
		this.tab6=new Tab6(this);
		this.tab8=new Tab8(this);
		this.abe=new ABE(this);
		this.efe=new EFE(this);
		this.setTab10(new Tab10(this));
		this.tab7=new PlanTresorerie<DetailLigneTresorerie>();
		logger.debug("Nouvelle edition");
	}
	/**
	 * postionner les élements calculables
	 */
	public void setDefaultData(){
		
		setInternalDefaultData(true);		
		
	}
	public void setInternalDefaultData(boolean initialization){
		

		getTab2().setDefaults();
		getTab6().getCaf().setDefaults();
		getTab6().getCrp().setDefaults();
		getTab6().getTfp().setDefaults();
		getTab6().getTfp().setDefaults();
		getTab6().getVfr().setDefaults();
		getTab8().setDefaults();
		getTab10().setDefaults();
		getTab4().setDefaults();

		if(initialization){
			
		getTab3().setDefaults();
		
		}		
		
	}

	public Tab2 getTab2() {
		return tab2;
	}

	public void setTab2(Tab2 tab2) {
		this.tab2 = tab2;
	}

	public Tab6 getTab6() {
		return tab6;
	}

	public void setTab6(Tab6 tab6) {
		this.tab6 = tab6;
	}

	private Depence getDepence() {
		return getTab2().getDepence();
	}

	private Recette getRecette() {
		return getTab2().getRecette();
	}

	public double getDepPersAe() {
		return getDepence().getPersonnel().getMontanAE();
	}

	public double getDepPersCp() {
		return getDepence().getPersonnel().getMontanCP();
	}

	public double getDepFoncAe() {
		return getDepence().getFonctionnement().getMontanAE();
	}

	public double getDepFoncCp() {
		return getDepence().getFonctionnement().getMontanCP();
	}

	public double getDepInvestAe() {
		return getDepence().getInvestissement().getMontanAE();
	}

	public double getDepInvestCp() {
		return getDepence().getInvestissement().getMontanCP();
	}

	public double getDepIntervAe() {
		return getDepence().getIntervention().getMontanAE();
	}

	public double getDepIntervCp() {
		return getDepence().getIntervention().getMontanCP();
	}

	public double getRecetR11() {
		return getRecette().getR11();
	}

	public double getRecetR12() {
		return getRecette().getR12();
	}

	public double getRecetR13() {
		return getRecette().getR13();
	}

	public double getRecetR14() {
		return getRecette().getR14();
	}

	public double getRecetR15() {
		return getRecette().getR15();
	}

	public double getRecetR22() {
		return getRecette().getR22();
	}

	public double getRecetR24() {
		return getRecette().getR24();
	}

	public double getRecetR25() {
		return getRecette().getR25();
	}

	public double getNoDataBudg5() {
		return -(getTab6().getCaf().getData5().getMontantDouble());
	}

	public double getTab6TotalCharges() {
		return getDepPersCp() + getRecetR24() + getDepFoncCp();
	}

	public double getTab6AutreProduit() {
		return getRecetR15() + getRecetR25() + getNoDataBudg5();
	}

	public double getTab6TotalProduits() {
		return getRecetR11() + getRecetR22() + getRecetR13() + getRecetR14() + getTab6AutreProduit();
	}

	public double getTab6ResultPrevBenifce() {
		if (getTab6TotalProduits() > getTab6TotalCharges())
			return getTab6TotalProduits() - getTab6TotalCharges();
		return 0;
	}

	public double getTab6ResultPrevPerte() {
		if (getTab6TotalCharges() > getTab6TotalProduits())
			return getTab6TotalCharges() - getTab6TotalProduits();
		return 0;
	}

	public double getTab6TotalEquilibreCharge() {
		return getTab6TotalCharges() + getTab6ResultPrevBenifce();
	}

	public double getTab6TotalEquilibreProduit() {
		return getTab6TotalProduits() + getTab6ResultPrevPerte();
	}

	public double getTab6ResultPrevExerc() {		
		if (getTab6ResultPrevBenifce() > 0)
			return getTab6ResultPrevBenifce();
		return -(getTab6ResultPrevPerte());	

	}

	public double getNodataBudg1() {
		return getTab6().getCaf().getData1().getMontantDouble();
	}

	public double getNodataBudg2() {
		return getTab6().getCaf().getData2().getMontantDouble();
	}

	public double getNodataBudg3() {
		return getTab6().getCaf().getData3().getMontantDouble();
	}

	public double getNodataBudg4() {
		return getTab6().getCaf().getData4().getMontantDouble();
	}

	public double getNodataBudg5() {
		return getTab6().getCaf().getData5().getMontantDouble();
	}

	public double getTab6ResultCafIaf() {
		return getTab6ResultPrevExerc() + getNodataBudg1() + getNodataBudg2() + getNodataBudg3() + getNodataBudg4()
				+ getNoDataBudg5();		
	}

	public double getTab6InsufAutofinanc() {
		if (getTab6ResultCafIaf() < 0)
			return -getTab6ResultCafIaf();
		return 0;
	}

	public double getTab6CapaciteAutofinanc() {
		if (getTab6ResultCafIaf() > 0)
			return getTab6ResultCafIaf();
		return 0;

	}

	public double getTab6RemboursCaution() {
		return getTab6().getTfp().getRembCautions().getMontantDouble();
	}

	public double getTab6CautionRecu() {
		return getTab6().getTfp().getCautionRecu().getMontantDouble();
	}

	public double getTab6TotalEmploi() {
        TFP tfp=getTab6().getTfp();
		return getDepInvestCp() + getTab6InsufAutofinanc()+tfp.getRembCautions().getMontantDouble()
				+tfp.getRembDettes().getMontantDouble();
	}

	public double getTab6TotalRessource() {
		TFP tfp=getTab6().getTfp();		
		return getTab6CapaciteAutofinanc()+tfp.getCautionRecu().getMontantDouble()+tfp.getAugDettes().getMontantDouble();
	}

	public double getTab6apportFondRoulement() {
		if ((getTab6TotalRessource() - getTab6TotalEmploi()) > 0)
			return getTab6TotalRessource() - getTab6TotalEmploi();

		return 0;
	}

	public double getTab6PrelevFondRoulement() {
		if ((getTab6TotalEmploi() - getTab6TotalRessource()) > 0)
			return getTab6TotalEmploi() - getTab6TotalRessource();

		return 0;
	}

	public double getTab6VariationFondRoul() {
		if (getTab6apportFondRoulement() > 0)
			return getTab6apportFondRoulement();
		return -(getTab6PrelevFondRoulement());

	}

	public double getTab6VariationBesoinFondRoul() {

		return getTab6VariationFondRoul() - getTab6VariTresorie();

	}

	public double getTab6VariTresorie() {		
		return getSoldBudgExcedent() - getSoldBudgDeficit();
	}

	public double getTab6nivFondRoul() {
		return getNivFinalFondRoul();
	}

	public double getTab6nivBesoinFroul() {
		return getNivFinalBesoinFondRoul();
	}

	public double getTab6NivTresorie() {
		return getNivFinalTresorie();
	}

	public double getTotalDepAe() {
		return getDepPersAe() + getDepFoncAe() + getDepIntervAe() + getDepInvestAe();
	}

	public double getTotalDepCp() {
		return getDepPersCp() + getDepFoncCp() + getDepIntervCp() + getDepInvestCp();

	}

	public double getRecetGlobal() {
		return getRecetR11() + getRecetR12() + getRecetR13() + getRecetR14() + getRecetR15();
	}

	public double getTotalRecetFleche() {
		return getRecetR22() + getRecetR24() + getRecetR25();
	}

	public double getTotalRecette() {
		return getRecetGlobal() + getTotalRecetFleche();
	}

	public double getSoldBudgExcedent() {
		if (getTotalRecette() > getTotalDepCp())
			return getTotalRecette() - getTotalDepCp();
		return 0;
	}

	public double getSoldBudgDeficit() {
		if (getTotalDepCp() > getTotalRecette())
			return getTotalDepCp() - getTotalRecette();
		return 0;
	}

	public double getCompteTiersC1(){		
		return getTab5().getTotDebitBudg();
		
	}
	
	public double getCompteTiersC2(){		
		return getTab5().getTotCreditBudg();
		
	}
	public double getSousTotOper1(){		
		return getSoldBudgDeficit()+getCompteTiersC1()+getTotDebitNonBudgC1()
		+getTab4Totalb1();		

	}
	public double getSousTotOper2(){			
		return getSoldBudgExcedent()+getCompteTiersC2()+getTotCreditNonBudgC2()
		+getTab4Totalb2();		
	}
	
	public double getTotDebitNonBudgC1(){
		return getTab5().getTotDebitNoBudg();		
	}

	public double getTotCreditNonBudgC2(){
		return getTab5().getTotCreditNoBudg();		
	}
	
	public double getAbondTresorie(){
		double sousTot=getSousTotOper2()-getSousTotOper1();
		if(sousTot>0)
		return sousTot;
		return 0;
	}
	
	public double getPrelevTresorie(){
		double sousTot=getSousTotOper1()-getSousTotOper2();
		if(sousTot>0)
		return sousTot;
		return 0;
	}
	
	public double getAbondTresorieFleche(){
		if(getSoldBudgBC()>0)
			return (getSoldBudgBC());
					return 0;

	}
	
	public double getPrelevTresorieFleche(){
		if(getSoldBudgBC()<0)
			return -(getSoldBudgBC());
					return 0;

	}
	
	public double getAbondTresorieNonFleche(){
		if(getAbondTresorie()==0)
			return 0;
		return getAbondTresorie()-getAbondTresorieFleche()+getPrelevTresorieFleche();
		
	}
	
	public double getPrelevTresorieNonFleche(){
		if(getPrelevTresorie()==0)
			return 0;
		return getPrelevTresorie()-getPrelevTresorieFleche()+getAbondTresorieFleche();
		
	}
	
	public double getTotalBesoin(){
		return getSousTotOper1()+getAbondTresorie();
	}
	
	public double getTotalFinanc(){
		return getSousTotOper2()+getPrelevTresorie();
	}
	
	public double getTotalRecetFlecheN(){
		return getRecetR22()+getRecetR24()+getRecetR25();	
		
	}
	
	public double getTotDepRecetCp(){
		
		double result =0d;
		double personnelAeCpN= getTab8().getPersonnelAeCp().getN().getMontantDouble();   
		double fonctCPn= getTab8().getFonctCP().getN().getMontantDouble();  
		double interCPn= getTab8().getInterCP().getN().getMontantDouble();   
		double invesCPn= getTab8().getInvesCP().getN().getMontantDouble(); 
		
		result=personnelAeCpN+fonctCPn+interCPn+invesCPn;
		return result;		
	}
	
	public double getSoldBudgBC(){
		return getTotalRecetFlecheN()-getTotDepRecetCp();
	}
	
	public double getFondRoul(){
		return getTab10().getNivInitFondRoulement().getMontantDouble();		
	}
	
	public double getBesoinFondRoul(){
		return getFondRoul()-getNiveauTresorie();		
	}
	
	public double getNiveauTresorie(){
		return getTab10().getNivInitTresorie().getMontantDouble();		
	}
	
	public double getNivInitTresorieFlech(){
		return getTab10().getNivInitTresorieFleche().getMontantDouble();		
	}
	
	public double getNivInitTresorieNonFlech(){
		return getNiveauTresorie()-getNivInitTresorieFlech();		
	}	
	
	public double getResultatPatrimonial() {
		if (getTab6ResultPrevBenifce() > 0)
			return getTab6ResultPrevBenifce();
		return -(getTab6ResultPrevPerte());
	}

	public double getVarFondRoul() {
		if (getTab6apportFondRoulement() > 0)
			return getTab6apportFondRoulement();
		return -(getTab6PrelevFondRoulement());
	}

	public double getRemboursCapital() {
		return getTab4Totalb1();
	}

	public double getNouveauCapital() {
		return getTab4Totalb2();
	}

	public double getOpDetteFinanc() {
		if (getTab4Totalb1() > 0)
			return getTab4Totalb1();
		return -(getTab4Totalb2());
	}

	public double getOpCompteSansFlux() {
		return getOpVarStock() + getOpChargeSurCreance() + getOpProduitDivers();
	}

	public double getOpVarStock() {
		return getTab10().getVariationStock().getMontantDouble();
	}

	public double getOpChargeSurCreance() {
		return getTab10().getChargeCreanceIrrecouvrable().getMontantDouble();
	}

	public double getOpProduitDivers() {
		return getTab10().getProduitDiversGestCourante().getMontantDouble();
	}

	public double getOpBudgComptaDiffr() {
		double result = 0d;

		double ecartProduitAnterieure = getTab10().getEcartProduitAnterieure().getMontantDouble();
		double ecartProduitEncours = getTab10().getEcartProduitEncours().getMontantDouble();
		double ecartChargeAnterieure = getTab10().getEcartChargeAnterieure().getMontantDouble();
		double ecartChargeEncours = getTab10().getEcartChargeEncours().getMontantDouble();

		result = ecartProduitAnterieure + ecartProduitEncours + ecartChargeAnterieure + ecartChargeEncours;
		return result;
	}

	public double getSoldebudg() {
		return getTab6VariationFondRoul() - getOpDetteFinanc() - getOpCompteSansFlux() - getOpBudgComptaDiffr();
	}

	public double getDecalageFluxNoBudg() {
		return getTab4Totalb1() + getCompteTiersC1() + getTotDebitNonBudgC1() -getTab4Totalb2()
				- getCompteTiersC2() - getTotCreditNonBudgC2();
	}

	public double getVariationTresorie() {
		return getSoldebudg() - getDecalageFluxNoBudg();
	}

	public double getVariTresorFleche() {
		return getAbondTresorieFleche() - getPrelevTresorieFleche();
	}

	public double getVariTresorNonFleche() {
		return getAbondTresorieNonFleche() - getPrelevTresorieNonFleche();
	}

	public double getVariBesoinFondRoul() {
		return getOpCompteSansFlux() + getOpBudgComptaDiffr() + getDecalageFluxNoBudg() + getOpDetteFinanc();
	}

	public double getVarRestPay() {
		return getTotalDepAe() - getTotalDepCp();
	}

	public double getNivInitRestPay() {
		return getTab10().getNivInitResteApayer().getMontantDouble();
	}

	public double getNivFinalRestPay() {
		return getNivInitRestPay() + getVarRestPay();
	}

	public double getNivFinalFondRoul() {
		return getFondRoul() + getTab6VariationFondRoul();
	}

	public double getNivFinalBesoinFondRoul() {
		return getNivFinalFondRoul() - getNivFinalTresorie();
	}

	public double getNivFinalTresorie() {
		return getNiveauTresorie() + getVariationTresorie();
	}

	public double getNivFinalTresFlech() {
		return getNivInitTresorieFlech() + getVariTresorFleche();
	}

	public double getNivFinalTresNonFlech() {
		return getNivInitTresorieNonFlech() + getVariTresorNonFleche();
	}
		
	
	public String getNomConfig() {
		return nomConfig;
	}

	public void setNomConfig(String nomConfig) {
		this.nomConfig = nomConfig;
	}
  

	public Tab5 getTab5() {
		return tab5;
	}

	public void setTab5(Tab5 tab5) {
		this.tab5 = tab5;
	}

	public Tab8 getTab8() {
		return tab8;
	}

	public void setTab8(Tab8 tab8) {
		this.tab8 = tab8;
	}

	public Tab10 getTab10() {
		return tab10;
	}

	public void setTab10(Tab10 tab10) {
		this.tab10 = tab10;
	}
	
	public double getPosOpFlechFfinExec()
	{
		double a=getTab8().getFinanDebutExec().getN().getMontantDouble();
		double b=getTotalRecetFlecheN();
		double c=getTotDepRecetCp();
		double d=getTab8().getAutofinancement().getN().getMontantDouble();
		double e=getTab8().getTresorerie().getN().getMontantDouble();
	
		return (a+b-c+d-e);
	}

	public Integer getExercice() {
		return exercice;
	}

	public void setExercice(Integer exercice) {
		this.exercice = exercice;
	}

	public String getCodeBudget() {
		return codeBudget;
	}

	public void setCodeBudget(String codeBudget) {
		this.codeBudget = codeBudget;
	}
	

	public List<DataItem> getEditableItems(){
		Depence dep=getTab2().getDepence();
		TFP tfp=getTab6().getTfp();
		DataItem[] tab=new DataItem[]{			
				dep.getContribution().getDataAE(),dep.getContribution().getDataCP(),
				getTab6().getCrp().getChargePensCivile(),
				tfp.getRembCautions(),tfp.getCautionRecu(),tfp.getRembDettes(),tfp.getAugDettes(),
				getTab8().getFinanDebutExec().getN(),getTab8().getPersonnelAeCp().getN(),getTab8().getFonctAE().getN(),
				getTab8().getFonctCP().getN(),getTab8().getInterAE().getN(),getTab8().getInterCP().getN(),getTab8().getInvesAE().getN(),
				getTab8().getInvesCP().getN(),getTab8().getAutofinancement().getN(),getTab8().getTresorerie().getN(),
				getTab10().getNivInitResteApayer(),getTab10().getNivInitFondRoulement(),getTab10().getNivInitTresorie(),getTab10().getNivInitTresorieFleche(),
				getTab10().getVariationStock(),getTab10().getChargeCreanceIrrecouvrable(),getTab10().getProduitDiversGestCourante(),getTab10().getEcartProduitAnterieure(),getTab10().getEcartProduitEncours(),
				getTab10().getEcartChargeAnterieure(),getTab10().getEcartChargeEncours()
				
				};		
		
		return Arrays.asList(tab);
	}
	public double getTab4Totalb1(){
		double rembCaution = getTab6().getTfp().getRembCautions().getMontantDouble();
		double rembDette= getTab6().getTfp().getRembDettes().getMontantDouble();
		return rembCaution+rembDette;
	}
	 
   public double getTab4Totalb2(){
	   double cautionRecu = getTab6().getTfp().getCautionRecu().getMontantDouble();
		double augmDette= getTab6().getTfp().getAugDettes().getMontantDouble();
		return cautionRecu+augmDette;
	}
   public List<DataItem> getDematItems(TabsEnum type){
	   List<DataItem> items=null;
	   switch(type){
	   case ABP:
		   items=getTab2().getItems();
		   break;
	   case ABE:
		   items=getAbe().getItems();
		   break;
	   case EFP:
		   items=getTab4().getItems();
		   break;
	   case EFE:
		   items=getEfe().getItems();
		   break;
	   case TFP:
		   items=getTab6().getTfp().getItems();
		   break;
	   case SPP2:
		   items=getTab6().getVfr().getItems();
		   break;  	   
	   case CRP:
		   items=getTab6().getCrp().getItems();
		   break;		   
	   case CAFP:
		   items=getTab6().getCaf().getItems();
		   break;	
	   case TAB8:
		    items=getTab8().getItems();
		   break;
	   case TAB10:
		    items=getTab10().getItems();
		   break;
	   case TAB3_TDD:
		   items=getTab3().getTdd().getItems();
		   break;  
	   case TAB3_TRO:
		   items=getTab3().getTro().getItems();
		   break;  	
//	   case SPE1:
//		   items=getTab6().getTfp().getItems();
//		   break;
//	   case SPE2:
//		   items=getTab6().getVfr().getItems();
//		   break;
	default:
		break;
		   
	   }
	   
	return items;
	   
   }

	public Tab4 getTab4() {
		return tab4;
	}

	public void setTab4(Tab4 tab4) {
		this.tab4 = tab4;
	}
	public Tab3 getTab3() {
		return tab3;
	}
	public void setTab3(Tab3 tab3) {
		this.tab3 = tab3;
	}
	
	public void refreshTotauxTab10()
	{
		getTab10().setDefaults();
	}
	
	public void refreshTotauxTab6()
	{
		getTab6().getCaf().setDefaults();
		getTab6().getCrp().setDefaults();
		getTab6().getTfp().setDefaults();
		getTab6().getVfr().setDefaults();
	}
	
	
	
	
	public void refreshTotauxTab2() {
		getTab2().setDefaults();
	}

	public void refreshTotauxTab8() {
		getTab8().setDefaults();
	}
	public Integer getNiveau() {
		return niveau;
	}
	public void setNiveau(Integer niveau) {
		this.niveau = niveau;
	}
	public ABE getAbe() {
		return abe;
	}
	public void setAbe(ABE abe) {
		this.abe = abe;
	}
	public EFE getEfe() {
		return efe;
	}
	public void setEfe(EFE efe) {
		this.efe = efe;
	}
	
    public Bilan getBilan() {
		return bilan;
	}
	public void setBilan(Bilan bilan) {
		this.bilan = bilan;
	}
	public CR getCr() {
		return cr;
	}
	public void setCr(CR cr) {
		this.cr = cr;
	}
	public PlanTresorerie<DetailLigneTresorerie> getTab7() {
		return tab7;
	}
	public void setTab7(PlanTresorerie<DetailLigneTresorerie> tab7) {
		this.tab7 = tab7;
	}
	public PlanTresorerie<DetailLigneTresorerie> getPlanTresorerie() {
		return tab7;
	}
	
	public BigDecimal getDepPersAbeAe() {
		return getAbe().getDepence().getPersonnel().getDataAE().getMontant();
	}
	public BigDecimal getContribAbeAe() {
		return getAbe().getDepence().getContribution().getDataAE().getMontant();
	}
	public BigDecimal getContribAbeCp() {
		return getAbe().getDepence().getContribution().getDataCP().getMontant();
	}
	public BigDecimal getDepPersAbeCp() {
		return getAbe().getDepence().getPersonnel().getDataCP().getMontant();
	}
	public BigDecimal getDepFoncAbeAe() {
		return getAbe().getDepence().getFonctionnement().getDataAE().getMontant();
	}
	public BigDecimal getDepFoncAbeCp() {
		return getAbe().getDepence().getFonctionnement().getDataCP().getMontant();
	}
	public BigDecimal getDepIntervAbeAe() {
		return getAbe().getDepence().getIntervention().getDataAE().getMontant();
	}
	public BigDecimal getDepIntervAbeCp() {
		return getAbe().getDepence().getIntervention().getDataCP().getMontant();
	}
	public BigDecimal getDepInvestAbeAe() {
		return getAbe().getDepence().getInvestissement().getDataAE().getMontant();
	}
	public BigDecimal getDepInvestAbeCp() {
		return getAbe().getDepence().getInvestissement().getDataCP().getMontant();
	}
	public void refreshTotauxEFE()
	{
		getEfe().setDefaults();

	}
}
