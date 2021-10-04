package fr.symphonie.budget.core.model.pluri;

import java.math.BigDecimal;

public class GlobalSuiviStruct {
	/**
	 * montant initial
	 */
	private MontantHtTtc initial;
	private MontantHtTtc rar;
	private MontantHtTtc solde;
	/**
	 * Annulation réduction
	 */
	private MontantHtTtc ar;
	/**
	 * opérations non budgétaires
	 */
	private MontantHtTtc onb;	

	private BigDecimal rec_ht;	
	
	
	public GlobalSuiviStruct() {
		this.initial=new MontantHtTtc();
		this.rar=new MontantHtTtc();
		this.solde=new MontantHtTtc();
		this.ar=new MontantHtTtc();
		this.onb=new MontantHtTtc();
		this.rec_ht=BigDecimal.ZERO;
	}

	public MontantHtTtc getInitial() {
		return initial;
	}

	public void setInitial(MontantHtTtc initial) {
		this.initial = initial;
	}

	public MontantHtTtc getRar() {
		return rar;
	}

	public void setRar(MontantHtTtc rar) {
		this.rar = rar;
	}

	public MontantHtTtc getSolde() {
		return solde;
	}

	public void setSolde(MontantHtTtc solde) {
		this.solde = solde;
	}

	public BigDecimal getRec_ht() {
		return rec_ht;
	}

	public void setRec_ht(BigDecimal rec_ht) {
		this.rec_ht = rec_ht;
	}

	public MontantHtTtc getAr() {
		return ar;
	}

	public void setAr(MontantHtTtc ar) {
		this.ar = ar;
	}

	public MontantHtTtc getOnb() {
		return onb;
	}

	public void setOnb(MontantHtTtc onb) {
		this.onb = onb;
	}

}
