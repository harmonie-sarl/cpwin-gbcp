package fr.symphonie.budget.core.model.edition;

import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.symphonie.budget.core.model.edition.util.DataItem;
import fr.symphonie.budget.core.model.edition.util.DataRefEnum;
import fr.symphonie.budget.core.model.edition.util.GenericTab;

public class Tab4 extends GenericTab {
	 private static final Logger logger = LoggerFactory.getLogger(Tab4.class);
	private DataItem soldBudgDeficit;
	private DataItem soldBudgExcedent;
	private DataItem b1;
	private DataItem b2;
	private DataItem compteTiersC1;
	private DataItem compteTiersC2;
	private DataItem e1;
	private DataItem e2;
	private DataItem sousTotOper1;
	private DataItem sousTotOper2;
	private DataItem abondTresorie;
	private DataItem prelevTresorie;
	private DataItem abondTresorieFleche;
	private DataItem prelevTresorieFleche;
	private DataItem abondTresorieNonFleche;
	private DataItem prelevTresorieNonFleche;
	private DataItem totalBesoin;
	private DataItem totalFinanc;
	
	
	
	
	public Tab4(Edition e) {
		super(e);
		this.soldBudgDeficit = new DataItem(DataRefEnum.ref_01);
		this.soldBudgExcedent =  new DataItem(DataRefEnum.ref_05);
		this.b1 =  new DataItem(DataRefEnum.ref_02);
		this.b2 =  new DataItem(DataRefEnum.ref_06);
		this.compteTiersC1 =  new DataItem(DataRefEnum.ref_03);
		this.compteTiersC2 = new DataItem(DataRefEnum.ref_07);
		this.e1 =  new DataItem(DataRefEnum.ref_04);
		this.e2 =  new DataItem(DataRefEnum.ref_08);
		this.sousTotOper1 =  new DataItem(DataRefEnum.ref_09);
		this.sousTotOper2 =  new DataItem(DataRefEnum.ref_10);
		this.abondTresorie =  new DataItem(DataRefEnum.ref_11);
		this.prelevTresorie =  new DataItem(DataRefEnum.ref_14);
		this.abondTresorieFleche =  new DataItem(DataRefEnum.ref_12);
		this.prelevTresorieFleche =  new DataItem(DataRefEnum.ref_15);
		this.abondTresorieNonFleche =  new DataItem(DataRefEnum.ref_13);
		this.prelevTresorieNonFleche =  new DataItem(DataRefEnum.ref_16);
		this.totalBesoin =  new DataItem(DataRefEnum.ref_17);
		this.totalFinanc =  new DataItem(DataRefEnum.ref_18);
	}
	
//	public Tab4(Edition e) {
//		super(e);
//		this.soldBudgDeficit = new DataItem(DataRefEnum.ref_01);
//		this.soldBudgExcedent =  new DataItem(DataRefEnum.ref_06);
//		this.b1 =  new DataItem(DataRefEnum.ref_02);
//		this.b2 =  new DataItem(DataRefEnum.ref_07);
//		this.compteTiersC1 =  new DataItem(DataRefEnum.ref_03);
//		this.compteTiersC2 = new DataItem(DataRefEnum.ref_08);
//		this.e1 =  new DataItem(DataRefEnum.ref_04);
//		this.e2 =  new DataItem(DataRefEnum.ref_09);
//		this.sousTotOper1 =  new DataItem(DataRefEnum.ref_05);
//		this.sousTotOper2 =  new DataItem(DataRefEnum.ref_10);
//		this.abondTresorie =  new DataItem(DataRefEnum.ref_11);
//		this.prelevTresorie =  new DataItem(DataRefEnum.ref_14);
//		this.abondTresorieFleche =  new DataItem(DataRefEnum.ref_12);
//		this.prelevTresorieFleche =  new DataItem(DataRefEnum.ref_15);
//		this.abondTresorieNonFleche =  new DataItem(DataRefEnum.ref_13);
//		this.prelevTresorieNonFleche =  new DataItem(DataRefEnum.ref_16);
//		this.totalBesoin =  new DataItem(DataRefEnum.ref_17);
//		this.totalFinanc =  new DataItem(DataRefEnum.ref_18);
//	}
	public DataItem getSoldBudgDeficit() {
		return soldBudgDeficit;
	}
	public void setSoldBudgDeficit(DataItem soldBudgDeficit) {
		this.soldBudgDeficit = soldBudgDeficit;
	}
	public DataItem getSoldBudgExcedent() {
		return soldBudgExcedent;
	}
	public void setSoldBudgExcedent(DataItem soldBudgExcedent) {
		this.soldBudgExcedent = soldBudgExcedent;
	}
	public DataItem getB1() {
		return b1;
	}
	public void setB1(DataItem b1) {
		this.b1 = b1;
	}
	public DataItem getB2() {
		return b2;
	}
	public void setB2(DataItem b2) {
		this.b2 = b2;
	}
	public DataItem getCompteTiersC1() {
		return compteTiersC1;
	}
	public void setCompteTiersC1(DataItem compteTiersC1) {
		this.compteTiersC1 = compteTiersC1;
	}
	public DataItem getCompteTiersC2() {
		return compteTiersC2;
	}
	public void setCompteTiersC2(DataItem compteTiersC2) {
		this.compteTiersC2 = compteTiersC2;
	}
	public DataItem getE1() {
		return e1;
	}
	public void setE1(DataItem e1) {
		this.e1 = e1;
	}
	public DataItem getE2() {
		return e2;
	}
	public void setE2(DataItem e2) {
		this.e2 = e2;
	}
	public DataItem getSousTotOper1() {
		return sousTotOper1;
	}
	public void setSousTotOper1(DataItem sousTotOper1) {
		this.sousTotOper1 = sousTotOper1;
	}
	public DataItem getSousTotOper2() {
		return sousTotOper2;
	}
	public void setSousTotOper2(DataItem sousTotOper2) {
		this.sousTotOper2 = sousTotOper2;
	}
	public DataItem getAbondTresorie() {
		return abondTresorie;
	}
	public void setAbondTresorie(DataItem abondTresorie) {
		this.abondTresorie = abondTresorie;
	}
	public DataItem getPrelevTresorie() {
		return prelevTresorie;
	}
	public void setPrelevTresorie(DataItem prelevTresorie) {
		this.prelevTresorie = prelevTresorie;
	}
	public DataItem getAbondTresorieFleche() {
		return abondTresorieFleche;
	}
	public void setAbondTresorieFleche(DataItem abondTresorieFleche) {
		this.abondTresorieFleche = abondTresorieFleche;
	}
	public DataItem getPrelevTresorieFleche() {
		return prelevTresorieFleche;
	}
	public void setPrelevTresorieFleche(DataItem prelevTresorieFleche) {
		this.prelevTresorieFleche = prelevTresorieFleche;
	}
	public DataItem getAbondTresorieNonFleche() {
		return abondTresorieNonFleche;
	}
	public void setAbondTresorieNonFleche(DataItem abondTresorieNonFleche) {
		this.abondTresorieNonFleche = abondTresorieNonFleche;
	}
	public DataItem getPrelevTresorieNonFleche() {
		return prelevTresorieNonFleche;
	}
	public void setPrelevTresorieNonFleche(DataItem prelevTresorieNonFleche) {
		this.prelevTresorieNonFleche = prelevTresorieNonFleche;
	}
	public DataItem getTotalBesoin() {
		return totalBesoin;
	}
	public void setTotalBesoin(DataItem totalBesoin) {
		this.totalBesoin = totalBesoin;
	}
	public DataItem getTotalFinanc() {
		return totalFinanc;
	}
	public void setTotalFinanc(DataItem totalFinanc) {
		this.totalFinanc = totalFinanc;
	}
	
	@Override
	public List<DataItem> getItems() {
		DataItem[] tab = new DataItem[] {
				soldBudgDeficit,
				soldBudgExcedent,
				b1,
				b2,
				compteTiersC1,
				compteTiersC2,
				e1,
				e2,
				sousTotOper1,
				sousTotOper2,
				abondTresorie,
				prelevTresorie,
				abondTresorieFleche,
				prelevTresorieFleche,
				abondTresorieNonFleche,
				prelevTresorieNonFleche,
				totalBesoin,
				totalFinanc
		};
		
		return Arrays.asList(tab);
	}
	
	@Override	
	public void setDefaults()

	{
		logger.debug("setDefaults - start");
		getSoldBudgDeficit().setMontantDouble(getParent().getSoldBudgDeficit());
		getSoldBudgExcedent().setMontantDouble(getParent().getSoldBudgExcedent());
		getB1().setMontantDouble(getParent().getTab4Totalb1());
		getB2().setMontantDouble(getParent().getTab4Totalb2());
		getCompteTiersC1().setMontantDouble(getParent().getCompteTiersC1());
		getCompteTiersC2().setMontantDouble(getParent().getCompteTiersC2());
		
		getE1().setMontantDouble(getParent().getTotDebitNonBudgC1());
		getE2().setMontantDouble(getParent().getTotCreditNonBudgC2());
		getSousTotOper1().setMontantDouble(getParent().getSousTotOper1());
		getSousTotOper2().setMontantDouble(getParent().getSousTotOper2());
		getAbondTresorie().setMontantDouble(getParent().getAbondTresorie());
		getPrelevTresorie().setMontantDouble(getParent().getPrelevTresorie());
		getAbondTresorieFleche().setMontantDouble(getParent().getAbondTresorieFleche());
		getPrelevTresorieFleche().setMontantDouble(getParent().getPrelevTresorieFleche());
		getAbondTresorieNonFleche().setMontantDouble(getParent().getAbondTresorieNonFleche());
		getPrelevTresorieNonFleche().setMontantDouble(getParent().getPrelevTresorieNonFleche());
		getTotalBesoin().setMontantDouble(getParent().getTotalBesoin());
		getTotalFinanc().setMontantDouble(getParent().getTotalFinanc());
		logger.debug("setDefaults - end");

	}
}
