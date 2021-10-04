package fr.symphonie.budget.core.model.edition.util;

import java.math.BigDecimal;

import fr.symphonie.util.model.SimpleEntity;

public class RecByOrig implements Comparable<RecByOrig> {
	private SimpleEntity origine;
	private DataItem dataR11;
	private DataItem dataR12;
	private DataItem dataR13;
	private DataItem dataR14;
	private DataItem dataR15;
	private DataItem dataR22;
	private DataItem dataR24;
	private DataItem dataR25;
	private DataItem totale;
	public RecByOrig() {
		super();		
		dataR11=new DataItem();
		dataR12=new DataItem();
		dataR13=new DataItem();
		dataR14=new DataItem();
		dataR15=new DataItem();		
		dataR22=new DataItem();
		dataR24=new DataItem();
		dataR25=new DataItem();
		totale=new DataItem();
	}
	
	public RecByOrig(SimpleEntity origine) {
		super();
		this.origine = origine;
		dataR11=new DataItem(DataRefEnum.ref_14,origine.getCode());
		dataR12=new DataItem(DataRefEnum.ref_15,origine.getCode());
		dataR13=new DataItem(DataRefEnum.ref_16,origine.getCode());
		dataR14=new DataItem(DataRefEnum.ref_17,origine.getCode());
		dataR15=new DataItem(DataRefEnum.ref_18,origine.getCode());
		
		dataR22=new DataItem(DataRefEnum.ref_20,origine.getCode());
		dataR24=new DataItem(DataRefEnum.ref_21,origine.getCode());
		dataR25=new DataItem(DataRefEnum.ref_22,origine.getCode());
		totale=new DataItem(DataRefEnum.ref_23,origine.getCode());

	}
	public SimpleEntity getOrigine() {
		return origine;
	}
	public void setOrigine(SimpleEntity origine) {
		this.origine = origine;
	}
	public DataItem getDataR11() {
		return dataR11;
	}
	public void setDataR11(DataItem dataR11) {
		this.dataR11 = dataR11;
	}
	public DataItem getDataR12() {
		return dataR12;
	}
	public void setDataR12(DataItem dataR12) {
		this.dataR12 = dataR12;
	}
	public DataItem getDataR13() {
		return dataR13;
	}
	public void setDataR13(DataItem dataR13) {
		this.dataR13 = dataR13;
	}
	public DataItem getDataR14() {
		return dataR14;
	}
	public void setDataR14(DataItem dataR14) {
		this.dataR14 = dataR14;
	}
	public DataItem getDataR15() {
		return dataR15;
	}
	public void setDataR15(DataItem dataR15) {
		this.dataR15 = dataR15;
	}
	public DataItem getDataR22() {
		return dataR22;
	}
	public void setDataR22(DataItem dataR22) {
		this.dataR22 = dataR22;
	}
	public DataItem getDataR24() {
		return dataR24;
	}
	public void setDataR24(DataItem dataR24) {
		this.dataR24 = dataR24;
	}
	public DataItem getDataR25() {
		return dataR25;
	}
	public void setDataR25(DataItem dataR25) {
		this.dataR25 = dataR25;
	}
	public DataItem getTotale() {
		return totale;
	}
	public void setTotale(DataItem totale) {
		this.totale = totale;
	}
	
	public double getSomme(){
		return getDataR11().getMontantDouble()+getDataR12().getMontantDouble()+
				getDataR13().getMontantDouble()+getDataR14().getMontantDouble()+
				getDataR15().getMontantDouble()+getDataR22().getMontantDouble()+
				getDataR24().getMontantDouble()+getDataR25().getMontantDouble();
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((origine == null) ? 0 : origine.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		RecByOrig other = (RecByOrig) obj;
		if (origine == null) {
			if (other.origine != null)
				return false;
		} else if (!origine.equals(other.origine))
			return false;
		return true;
	}
	public void setMontant(String nature, BigDecimal montant) {
		
		if (NatGrpEnum.parse(nature) != null) {
			switch (NatGrpEnum.parse(nature)) {
			case NAT_GRP_R11:
				getDataR11().setMontant(montant);
				break;
			case NAT_GRP_R12:
				getDataR12().setMontant(montant);
				break;
			case NAT_GRP_R13:
				getDataR13().setMontant(montant);
				break;
			case NAT_GRP_R14:
				getDataR14().setMontant(montant);
				break;
			case NAT_GRP_R15:
				getDataR15().setMontant(montant);
				break;
			case NAT_GRP_R22:
				getDataR22().setMontant(montant);
				break;
			case NAT_GRP_R24:
				getDataR24().setMontant(montant);
				break;
			case NAT_GRP_R25:
				getDataR25().setMontant(montant);
				break;
			default:
				break;
			}
		}
	}
	@Override
	public int compareTo(RecByOrig o) {
		return this.getOrigine().compareTo(o.getOrigine());
	}
public BigDecimal getMontant(String nature) {
	BigDecimal montant=null;
		
		if (NatGrpEnum.parse(nature) != null) {
			switch (NatGrpEnum.parse(nature)) {
			case NAT_GRP_R11:
				montant=getDataR11().getMontant();
				break;
			case NAT_GRP_R12:
				montant=getDataR12().getMontant();
				break;
			case NAT_GRP_R13:
				montant=getDataR13().getMontant();
				break;
			case NAT_GRP_R14:
				montant=getDataR14().getMontant();
				break;
			case NAT_GRP_R15:
				montant=getDataR15().getMontant();
				break;
			case NAT_GRP_R22:
				montant=getDataR22().getMontant();
				break;
			case NAT_GRP_R24:
				montant=getDataR24().getMontant();
				break;
			case NAT_GRP_R25:
				montant=getDataR25().getMontant();
				break;
			default:
				break;
			}
		}
		return montant;
	}
	
}
