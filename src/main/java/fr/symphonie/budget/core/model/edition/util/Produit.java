package fr.symphonie.budget.core.model.edition.util;

import java.util.ArrayList;
import java.util.List;

public class Produit extends GenericTab{
	private List<CRItem> fonctionnement;
	private List<CRItem> financier;
	private CRItem totFonct;
	private CRItem totFinanc;
	private CRItem perte;
	private CRItem totale;
	public Produit() {
		super();
		setFonctionnement(createList(CRType.fonctionnement));
		setFinancier(createList(CRType.financier));
		totFonct=new CRItem(CRMsgKey.PR_FO_TOT.getKey(),CRMsgKey.PR_FO_TOT.getRef());
		totFinanc=new CRItem(CRMsgKey.PR_FI_TOT.getKey(),CRMsgKey.PR_FI_TOT.getRef());
		perte=new CRItem(CRMsgKey.PERTE.getKey(),CRMsgKey.PERTE.getRef());
		totale=new CRItem(CRMsgKey.PR_TOT.getKey(),CRMsgKey.PR_TOT.getRef());
	}
	private List<CRItem> createList(CRType type){
		List<CRItem> result=new ArrayList<CRItem>();
		List<CRMsgKey> params=CRMsgKey.getGroupe(false, type);
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
	public CRItem getTotFinanc() {
		return totFinanc;
	}
	public void setTotFinanc(CRItem totFinanc) {
		this.totFinanc = totFinanc;
	}
	public CRItem getPerte() {
		return perte;
	}
	public void setPerte(CRItem perte) {
		this.perte = perte;
	}
	public CRItem getTotale() {
		return totale;
	}
	public void setTotale(CRItem totale) {
		this.totale = totale;
	}
	

	public List<DataItem> getItems() {
		List<DataItem> items = new ArrayList<DataItem>();
		
		items.addAll(getFonctionnementItems());
		items.addAll(getFinancierItems());
		items.addAll(getTotFonct().getItems());
		items.addAll(getTotFinanc().getItems());
		items.addAll(getPerte().getItems());
		items.addAll(getTotale().getItems());
		

		return items;
	}
	
	public List<DataItem> getItemsAnt() {
		List<DataItem> items = new ArrayList<DataItem>();
		
		items.addAll(getFonctionnementItemsAnt());
		items.addAll(getFinancierItemsAnt());
		items.addAll(getTotFonct().getItemsAnt());
		items.addAll(getTotFinanc().getItemsAnt());
		items.addAll(getPerte().getItemsAnt());
		items.addAll(getTotale().getItemsAnt());
		

		return items;
	}
	
	private List<DataItem> getFonctionnementItems() {		
		return getItems(getFonctionnement());
	}
	
	private List<DataItem> getFinancierItems() {
		return getItems(getFinancier());
	}
	private List<DataItem> getItems(List<CRItem> list) {
		List<DataItem> items = new ArrayList<DataItem>();
		if (list != null)
			for (CRItem item : list) {
				items.addAll(item.getItems());
			}
		return items;
	}
	private List<DataItem> getFonctionnementItemsAnt() {		
		return getItemsAnt(getFonctionnement());
	}
	
	private List<DataItem> getFinancierItemsAnt() {
		return getItemsAnt(getFinancier());
	}
	private List<DataItem> getItemsAnt(List<CRItem> list) {
		List<DataItem> items = new ArrayList<DataItem>();
		if (list != null)
			for (CRItem item : list) {
				items.addAll(item.getItemsAnt());
			}
		return items;
	}
	@Override
	public void setDefaults() {
  getTotFonct().getN().setMontantDouble(getTotFonctN());
  getTotFonct().getN().setMontantDouble(getTotFonctAnt());
  getTotFinanc().getN().setMontantDouble(getTotFinanctN());
  getTotFinanc().getN().setMontantDouble(getTotFinancAnt());
  getTotale().getN().setMontantDouble(getTotaleProduitN());
  getTotale().getAnt().setMontantDouble(getTotaleProduitAnt());

		
	}
	
	public double getTotaleProduitAnt() {
		return getTotFonctAnt()+getTotFinancAnt();
	}
	public double getTotaleProduitN() {
		return getTotFonctN()+getTotFinanctN();
	}
	private double getTotFinancAnt() {
		double result=0d;
		double mnt=0;
		for (CRItem operation : getFinancier()) {
			mnt=operation.getAnt().getMontantDouble();

			result+=mnt;
		}
		return result;	
	}
	private double getTotFinanctN() {
		double result=0d;
		double mnt=0;
		for (CRItem operation : getFinancier()) {
			mnt=operation.getN().getMontantDouble();

			result+=mnt;
		}
		return result;		
	}
	private double getTotFonctAnt() {
		double result=0d;
		double mnt=0;
		for (CRItem operation : getFonctionnement()) {
			mnt=operation.getAnt().getMontantDouble();

			result+=mnt;
		}
		return result;	
	}
	private double getTotFonctN() {
			double result=0d;
			double mnt=0;
			for (CRItem operation : getFonctionnement()) {
				mnt=operation.getN().getMontantDouble();

				result+=mnt;
			}
			return result;		
		}
	
}
