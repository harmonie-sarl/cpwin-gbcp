package fr.symphonie.budget.core.model.cf;

import java.math.BigDecimal;
import java.math.RoundingMode;

import fr.symphonie.budget.core.model.edition.Tab2;
import fr.symphonie.budget.core.model.edition.util.ABE;
import fr.symphonie.budget.core.model.plt.DetailLigneTresorerie;
import fr.symphonie.budget.core.model.plt.PlanTresorerie;

public class Rapprochement {
	private ABE tab2;
	private PlanTresorerie<DetailLigneTresorerie> tab7;
	public Rapprochement(ABE tab2, PlanTresorerie<DetailLigneTresorerie> tab7) {
		super();
		this.tab2 = tab2;
		this.tab7 = tab7;
	}
	public Tab2 getTab2() {
		return tab2;
	}
	public void setTab2(ABE tab2) {
		this.tab2 = tab2;
	}
	public PlanTresorerie<DetailLigneTresorerie> getTab7() {
		return tab7;
	}
	public void setTab7(PlanTresorerie<DetailLigneTresorerie> tab7) {
		this.tab7 = tab7;
	}

	private BigDecimal getEcart(BigDecimal decimal1, BigDecimal decimal2) {
		BigDecimal mnt1 = decimal1 != null ? decimal1 : new BigDecimal(0d);
		BigDecimal mnt2 = decimal2 != null ? decimal2 : new BigDecimal(0d);
		return mnt1.subtract(mnt2);
	}

	public BigDecimal getRecGlobalPers() {
		DetailLigneTresorerie ligne=getTab7().getDetailLigne(21);
		return getSomme(ligne);
	}
	public BigDecimal getRecGlobalFonc() {
		DetailLigneTresorerie ligne=getTab7().getDetailLigne(22);
		return getSomme(ligne);
	}
	public BigDecimal getRecGlobalInter() {
		DetailLigneTresorerie ligne=getTab7().getDetailLigne(23);
		return getSomme(ligne);
	}
	public BigDecimal getRecGlobalInves() {
		DetailLigneTresorerie ligne=getTab7().getDetailLigne(24);
		return getSomme(ligne);
	}
	public BigDecimal getRecFlechePers() {
		DetailLigneTresorerie ligne=getTab7().getDetailLigne(25);
		return getSomme(ligne);
	}
	public BigDecimal getRecFlecheFonc() {
		DetailLigneTresorerie ligne=getTab7().getDetailLigne(26);
		return getSomme(ligne);
	}
	public BigDecimal getRecFlecheInter() {
		DetailLigneTresorerie ligne=getTab7().getDetailLigne(27);
		return getSomme(ligne);
	}
	public BigDecimal getRecFlecheInves() {
		DetailLigneTresorerie ligne=getTab7().getDetailLigne(28);
		return getSomme(ligne);
	}
	public BigDecimal getTotPers() {
		return getRecGlobalPers().add(getRecFlechePers());
	}
	public BigDecimal getTotFonc() {
		return getRecGlobalFonc().add(getRecFlecheFonc());
	}
	public BigDecimal getTotInter() {
		return getRecGlobalInter().add(getRecFlecheInter());
	}
	public BigDecimal getTotInves() {
		return getRecGlobalInves().add(getRecFlecheInves());
	}
	public BigDecimal getEcartPers() {
		return getTab2().getDepence().getPersonnel().getDataCP().getMontant().subtract(getTotPers());
	}
	public BigDecimal getEcartFonc() {
		return getTab2().getDepence().getFonctionnement().getDataCP().getMontant().subtract(getTotFonc());
	}
	public BigDecimal getEcartInter() {
		return getTab2().getDepence().getIntervention().getDataCP().getMontant().subtract(getTotInter());
	}
	public BigDecimal getEcartInves() {
		return getTab2().getDepence().getInvestissement().getDataCP().getMontant().subtract(getTotInves());
	}
	public BigDecimal getSubChrgSrvc() {
		DetailLigneTresorerie ligne=getTab7().getDetailLigne(3);
		return getSomme(ligne);
	}
	
	public BigDecimal getAutrFinancEtat() {
		DetailLigneTresorerie ligne=getTab7().getDetailLigne(4);
		return getSomme(ligne);
	}
	
	public BigDecimal getFiscAffecte() {
		DetailLigneTresorerie ligne=getTab7().getDetailLigne(5);
		return getSomme(ligne);
	}
	
	
	public BigDecimal getAutrFinancPub() {
		DetailLigneTresorerie ligne=getTab7().getDetailLigne(6);
		return getSomme(ligne);
	}
	
	public BigDecimal getRecetPropre() {
		DetailLigneTresorerie ligne=getTab7().getDetailLigne(7);
		return getSomme(ligne);
	}	
	
	public BigDecimal getFinancEtatFleche() {
		DetailLigneTresorerie ligne=getTab7().getDetailLigne(8);
		return getSomme(ligne);
	}	
	public BigDecimal getAutreFinancPubFleche() {
		DetailLigneTresorerie ligne=getTab7().getDetailLigne(9);
		return getSomme(ligne);
	}	
	public BigDecimal getRecetPropreFleche() {
		DetailLigneTresorerie ligne=getTab7().getDetailLigne(10);
		return getSomme(ligne);
	}	

	
	private BigDecimal getSomme(DetailLigneTresorerie ligne) {
		BigDecimal somme=BigDecimal.ZERO;
		int periode=getTab7().getPeriode();
		if(ligne!=null) {
			somme=new BigDecimal(ligne.getSomme(periode-1));
		}
		
		return somme.setScale(2, RoundingMode.HALF_UP);
	}
	public BigDecimal getTotalRecetGlobalise() {
		BigDecimal total = new BigDecimal(0);
		total = total.add(getSubChrgSrvc());
		total = total.add(getAutrFinancEtat());
		total = total.add(getFiscAffecte());
		total = total.add(getAutrFinancPub());
		total = total.add(getRecetPropre());
		total = total.setScale(2, RoundingMode.HALF_UP);
		return total;

	}

	public BigDecimal getTotalRecetFleche() {
		BigDecimal total = new BigDecimal(0);
		total = total.add(getFinancEtatFleche());
		total = total.add(getAutreFinancPubFleche());
		total = total.add(getRecetPropreFleche());
		total = total.setScale(2, RoundingMode.HALF_UP);
		return total;
	}
	
	public BigDecimal getTotalRecette() {
		BigDecimal total = new BigDecimal(0);
		total = total.add(getTotalRecetGlobalise());
		total = total.add(getTotalRecetFleche());	
		total = total.setScale(2, RoundingMode.HALF_UP);
		return total;
	}
	
	
	
	public BigDecimal getEcartRecetGlobalise() {	
		return getEcart(getTab2().getRecetGlobal().getMontant(),getTotalRecetGlobalise());
	}

	public BigDecimal getEcartRecetFleche() {
		return getEcart(getTab2().getTotalRecetFleche().getMontant(), getTotalRecetFleche());
	}

	public BigDecimal getEcartTotalRecette() {
		return getEcart(getTab2().getTotalRecette().getMontant(), getTotalRecette());
	}


	public BigDecimal getEcartSubChrgSrvc() {
		return getEcart(getTab2().getRecette().getDataR11().getMontant(), getSubChrgSrvc());
	}

	public BigDecimal getEcartAutrFinancEtat() {
		return getEcart(getTab2().getRecette().getDataR12().getMontant(), getAutrFinancEtat());
	}

	public BigDecimal getEcartFiscAffecte() {
		return getEcart(getTab2().getRecette().getDataR13().getMontant(), getFiscAffecte());
	}

	public BigDecimal getEcartAutrFinancPub() {
		return getEcart(getTab2().getRecette().getDataR14().getMontant(), getAutrFinancPub());
	}

	public BigDecimal getEcartRecetPropre() {
		return getEcart(getTab2().getRecette().getDataR15().getMontant(), getRecetPropre());
	}

	public BigDecimal getEcartFinancEtatFleche() {
		return getEcart(getTab2().getRecette().getDataR22().getMontant(), getFinancEtatFleche());
	}

	public BigDecimal getEcartAutreFinancPubFleche() {
		return getEcart(getTab2().getRecette().getDataR24().getMontant(), getAutreFinancPubFleche());
	}

	public BigDecimal getEcartRecetPropreFleche() {
		return getEcart(getTab2().getRecette().getDataR25().getMontant(), getRecetPropreFleche());
	}
	
}
