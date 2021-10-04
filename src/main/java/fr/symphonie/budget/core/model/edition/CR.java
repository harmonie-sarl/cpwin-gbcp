package fr.symphonie.budget.core.model.edition;

import java.util.ArrayList;
import java.util.List;

import fr.symphonie.budget.core.model.edition.util.Charge;
import fr.symphonie.budget.core.model.edition.util.DataItem;
import fr.symphonie.budget.core.model.edition.util.Produit;

public class CR {
	private Charge charge;
	private Produit produit;

	public CR() {
		super();
		charge=new Charge();
		produit=new Produit();
	}

	public Charge getCharge() {
		return charge;
	}

	public void setCharge(Charge charge) {
		this.charge = charge;
	}

	public Produit getProduit() {
		return produit;
	}

	public void setProduit(Produit produit) {
		this.produit = produit;
	}
	public List<DataItem> getItems() {
		List<DataItem> items=new ArrayList<DataItem>();
		items.addAll(getCharge().getItems());
		items.addAll(getProduit().getItems());
		return items;
	}
	public List<DataItem> getItemsAnt() {
		List<DataItem> items=new ArrayList<DataItem>();
		items.addAll(getCharge().getItemsAnt());
		items.addAll(getProduit().getItemsAnt());
		return items;
	}
	public void setDefaults() {
		getCharge().setDefaults();
		getProduit().setDefaults();
		getProduit().getPerte().getN().setMontantDouble(getPerteN());
		getProduit().getPerte().getAnt().setMontantDouble(getPerteAnt());
		getCharge().getBenefice().getN().setMontantDouble(getTotBenificeN());
		getCharge().getBenefice().getAnt().setMontantDouble(getTotBenificeAnt());
	}
	private double getPerteAnt() {
		if(getCharge().getTotaleChargeAnt()>getProduit().getTotaleProduitAnt())
			return (getCharge().getTotaleChargeAnt()- getProduit().getTotaleProduitAnt());
		return 0;
	}
	private double getPerteN() {
		if(getCharge().getTotaleChargeN()>getProduit().getTotaleProduitN())
			return (getCharge().getTotaleChargeN()- getProduit().getTotaleProduitN());
		return 0;
	}
	private double getTotBenificeAnt() {
		if(getProduit().getTotaleProduitAnt()>getCharge().getTotaleChargeAnt())
			return (getProduit().getTotaleProduitAnt()- getCharge().getTotaleChargeAnt());
		return 0;
	}

	private double getTotBenificeN() {
		if(getProduit().getTotaleProduitN()>getCharge().getTotaleChargeN())
			return (getProduit().getTotaleProduitN()- getCharge().getTotaleChargeN());
		return 0;
	}
	
}
