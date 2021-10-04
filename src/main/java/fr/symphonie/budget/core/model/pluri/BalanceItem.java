package fr.symphonie.budget.core.model.pluri;

import java.math.BigDecimal;

import fr.symphonie.util.model.SimpleEntity;

public class BalanceItem {
	private SimpleEntity compte;
	private OpBalance debit;
	private OpBalance credit;
	private Solde solde;
	private String niveau;
	
	public BalanceItem() {
		super();		
	}
	
	public SimpleEntity getCompte() {
		return compte;
	}
	public void setCompte(SimpleEntity compte) {
		this.compte = compte;
	}
	public OpBalance getDebit() {
		return debit;
	}
	public void setDebit(OpBalance debit) {
		this.debit = debit;
	}
	public OpBalance getCredit() {
		return credit;
	}
	public void setCredit(OpBalance credit) {
		this.credit = credit;
	}
	public Solde getSolde() {
		return solde;
	}
	public void setSolde(Solde solde) {
		this.solde = solde;
	}
	public String getNiveau() {
		return niveau;
	}
	public void setNiveau(String niveau) {
		this.niveau = niveau;
	}
	
	public class OpBalance{
		 private BigDecimal entree;
		 private BigDecimal montant;
		 private BigDecimal totale;
		 
		public OpBalance(BigDecimal entree, BigDecimal montant, BigDecimal totale) {
			super();
			this.entree = entree;
			this.montant = montant;
			this.totale = totale;
		}

		public BigDecimal getEntree() {
			return entree;
		}
		public void setEntree(BigDecimal entree) {
			this.entree = entree;
		}
		public BigDecimal getMontant() {
			return montant;
		}
		public void setMontant(BigDecimal montant) {
			this.montant = montant;
		}
		public BigDecimal getTotale() {
			return totale;
		}
		public void setTotale(BigDecimal totale) {
			this.totale = totale;
		}
		 	
	}
	
	public class Solde{
		 private BigDecimal debit;
		 private BigDecimal credit;
		 
		public Solde(BigDecimal debit, BigDecimal credit) {
			super();
			this.debit = debit;
			this.credit = credit;
		}
		public BigDecimal getDebit() {
			return debit;
		}
		public void setDebit(BigDecimal debit) {
			this.debit = debit;
		}
		public BigDecimal getCredit() {
			return credit;
		}
		public void setCredit(BigDecimal credit) {
			this.credit = credit;
		}
		 
	 }
}
 
 