package fr.symphonie.budget.core.model.edition.util;

import java.util.ArrayList;
import java.util.List;

public class Charge extends GenericTab{ 
	private List<CRItem> fonctionnement;
	private List<CRItem> intervention;
	private List<CRItem> financier;
	private CRItem totFonct;
	private CRItem totInter;
	private CRItem totFinanc;
	private CRItem engagement;
	private CRItem totFonctInter;
	private CRItem impot;
	private CRItem benefice;
	private CRItem totale;
	public Charge() {
		super();
		setFonctionnement(createList(CRType.fonctionnement));
		setIntervention(createList(CRType.intervention));
		setFinancier(createList(CRType.financier));
		totFonct=new CRItem(CRMsgKey.CH_FO_TOT.getKey(),CRMsgKey.CH_FO_TOT.getRef());
		totInter=new CRItem(CRMsgKey.CH_IN_TOT.getKey(),CRMsgKey.CH_IN_TOT.getRef());
		totFinanc=new CRItem(CRMsgKey.CH_FI_TOT.getKey(),CRMsgKey.CH_FI_TOT.getRef());
		engagement=new CRItem(CRMsgKey.CH_IN_ENG.getKey(),CRMsgKey.CH_IN_ENG.getRef());
		totFonctInter=new CRItem(CRMsgKey.CH_FO_IN_TOT.getKey(),CRMsgKey.CH_FO_IN_TOT.getRef());
		impot=new CRItem(CRMsgKey.IMPOT.getKey(),CRMsgKey.IMPOT.getRef());
		benefice=new CRItem(CRMsgKey.BENEFICE.getKey(),CRMsgKey.BENEFICE.getRef());
		totale=new CRItem(CRMsgKey.CH_TOT.getKey(),CRMsgKey.CH_TOT.getRef());
	}
	
	private List<CRItem> createList(CRType type){
		List<CRItem> result=new ArrayList<CRItem>();
		List<CRMsgKey> params=CRMsgKey.getGroupe(true, type);
		for(CRMsgKey p:params){
			result.add(new CRItem(p.getKey(), p.getRef(),p.getStyle()));
		}
		return result;
	}
	public List<CRItem> getFonctionnement() {
		return fonctionnement;
	}
	public void setFonctionnement(List<CRItem> fonctionnement) {
		this.fonctionnement = fonctionnement;
	}
	public List<CRItem> getIntervention() {
		return intervention;
	}
	public void setIntervention(List<CRItem> intervention) {
		this.intervention = intervention;
	}
	public List<CRItem> getFinancier() {
		return financier;
	}
	public void setFinancier(List<CRItem> financier) {
		this.financier = financier;
	}

	public CRItem getTotFonct() {
		return totFonct;
	}

	public void setTotFonct(CRItem totFonct) {
		this.totFonct = totFonct;
	}

	public CRItem getTotInter() {
		return totInter;
	}

	public void setTotInter(CRItem totInter) {
		this.totInter = totInter;
	}

	public CRItem getTotFinanc() {
		return totFinanc;
	}

	public void setTotFinanc(CRItem totFinanc) {
		this.totFinanc = totFinanc;
	}

	public CRItem getEngagement() {
		return engagement;
	}

	public void setEngagement(CRItem engagement) {
		this.engagement = engagement;
	}

	public CRItem getTotFonctInter() {
		return totFonctInter;
	}

	public void setTotFonctInter(CRItem totFonctInter) {
		this.totFonctInter = totFonctInter;
	}

	public CRItem getImpot() {
		return impot;
	}

	public void setImpot(CRItem impot) {
		this.impot = impot;
	}

	public CRItem getBenefice() {
		return benefice;
	}

	public void setBenefice(CRItem benefice) {
		this.benefice = benefice;
	}

	public CRItem getTotale() {
		return totale;
	}

	public void setTotale(CRItem totale) {
		this.totale = totale;
	}

	public List<DataItem> getItems() {
		List<DataItem> items=new ArrayList<DataItem>();
		items.addAll(getFonctionnementItems());
		items.addAll(getInterventionItems());
		items.addAll(getFinancierItems());
		
		items.addAll(getTotFonct().getItems());
		items.addAll(getTotInter().getItems());
		items.addAll(getTotFinanc().getItems());
		items.addAll(getEngagement().getItems());
		items.addAll(getTotFonctInter().getItems());
		items.addAll(getImpot().getItems());
		items.addAll(getBenefice().getItems());
		items.addAll(getTotale().getItems());
		return items;
	}
	
	public List<DataItem> getItemsAnt() {
		List<DataItem> items=new ArrayList<DataItem>();
		items.addAll(getFonctionnementItemsAnt());
		items.addAll(getInterventionItemsAnt());
		items.addAll(getFinancierItemsAnt());
		
		items.addAll(getTotFonct().getItemsAnt());
		items.addAll(getTotInter().getItemsAnt());
		items.addAll(getTotFinanc().getItemsAnt());
		items.addAll(getEngagement().getItemsAnt());
		items.addAll(getTotFonctInter().getItemsAnt());
		items.addAll(getImpot().getItemsAnt());
		items.addAll(getBenefice().getItemsAnt());
		items.addAll(getTotale().getItemsAnt());
		return items;
	}
	

	
	private List<DataItem> getFonctionnementItems() {
		return getItems(getFonctionnement());
	}
	
	private List<DataItem> getInterventionItems() {
		return getItems(getIntervention());
	}
	
	private List<DataItem> getFinancierItems() {
		return getItems(getFinancier());
	}
	
	private List<DataItem> getFonctionnementItemsAnt() {
		return getItemsAnt(getFonctionnement());
	}
	
	private List<DataItem> getInterventionItemsAnt() {
		return getItemsAnt(getIntervention());
	}
	
	private List<DataItem> getFinancierItemsAnt() {
		return getItemsAnt(getFinancier());
	}
	private List<DataItem> getItems(List<CRItem> list) {
		List<DataItem> items = new ArrayList<DataItem>();
		if (list != null)
			for (CRItem item : list) {
				items.addAll(item.getItems());
			}
		return items;
	}
	private List<DataItem> getItemsAnt(List<CRItem> list) {
		List<DataItem> items = new ArrayList<DataItem>();
		if (list != null)
			for (CRItem item : list) {
				items.addAll(item.getItemsAnt());
			}
		return items;
	}
	private double getTotChargeFonctN(){
		double result=0d;
		double mnt=0;
		for (CRItem operation : getFonctionnement()) {
			mnt=operation.getN().getMontantDouble();

			result+=mnt;
		}
		return result;		
	}
	private double getTotChargefonctAnt(){
		double result=0d;
		double mnt=0;
		for (CRItem operation : getFonctionnement()) {
			mnt=operation.getAnt().getMontantDouble();

			result+=mnt;
		}
		return result;		
	}
	

	@Override
	public void setDefaults() {
		getTotFonct().getN().setMontantDouble(getTotChargeFonctN());
		getTotFonct().getAnt().setMontantDouble(getTotChargefonctAnt());
		getTotFonctInter().getN().setMontantDouble(getTotFoncInterN());
		getTotFonctInter().getAnt().setMontantDouble(getTotFoncInterAnt());
		getTotInter().getN().setMontantDouble(getTotInterN());
		getTotInter().getAnt().setMontantDouble(getTotInterAnt());
		getTotFinanc().getN().setMontantDouble(getTotfinancN());
		getTotInter().getAnt().setMontantDouble(getTotfinancAnt());
		getTotale().getN().setMontantDouble(getTotaleChargeN());
		getTotale().getAnt().setMontantDouble(getTotaleChargeAnt());

		
	}



	public double getTotaleChargeN() {
		return getTotFoncInterN()+getTotfinancN()+getImpot().getN().getMontantDouble();
	}
	public double getTotaleChargeAnt() {
		return getTotFoncInterAnt()+getTotfinancAnt()+getImpot().getAnt().getMontantDouble();
	}

	public double getTotfinancAnt() {
		double result=0d;
		double mnt=0;
		for (CRItem operation : getFinancier()) {
			mnt=operation.getAnt().getMontantDouble();

			result+=mnt;
		}
		return result;		
	}

	public double getTotfinancN() {
		double result=0d;
		double mnt=0;
		for (CRItem operation : getFinancier()) {
			mnt=operation.getN().getMontantDouble();

			result+=mnt;
		}
		return result;		
	}

	private double getTotInterN() {
		double result=0d;
		double mnt=0;
		for (CRItem operation : getIntervention()) {
			mnt=operation.getN().getMontantDouble();

			result+=mnt;
		}
		return result;		
	}
	private double getTotInterAnt() {
		double result=0d;
		double mnt=0;
		for (CRItem operation : getIntervention()) {
			mnt=operation.getAnt().getMontantDouble();

			result+=mnt;
		}
		return result;		
	}
	private double getTotFoncInterN() {
		return getTotChargeFonctN()+getTotInterN()+getEngagement().getN().getMontantDouble();
	}
	private double getTotFoncInterAnt() {
		return getTotChargefonctAnt()+getTotInterAnt()+getEngagement().getAnt().getMontantDouble();
	}

	
		
}
