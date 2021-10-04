package fr.symphonie.budget.core.model.edition.util;

import java.math.BigDecimal;

import fr.symphonie.util.model.SimpleEntity;

public class DepByDest {
private SimpleEntity dest;
private DepenceItem personnel;
private DepenceItem fonctionnement;
private DepenceItem intervention;
private DepenceItem investissement;
private DepenceItem totale;

public DepByDest(SimpleEntity dest) {
	super();
	this.dest = dest;
	this.personnel=new DepenceItem(DataRefEnum.ref_01,DataRefEnum.ref_02,dest.getCode());
	this.fonctionnement=new DepenceItem(DataRefEnum.ref_03,DataRefEnum.ref_04,dest.getCode());
	this.intervention=new DepenceItem(DataRefEnum.ref_05,DataRefEnum.ref_06,dest.getCode());
	this.investissement=new DepenceItem(DataRefEnum.ref_07,DataRefEnum.ref_08,dest.getCode());
	this.totale=new DepenceItem(DataRefEnum.ref_09,DataRefEnum.ref_10,dest.getCode());
	
}
public SimpleEntity getDest() {
	return dest;
}
public void setDest(SimpleEntity dest) {
	this.dest = dest;
}
public DepenceItem getPersonnel() {
	return personnel;
}
public void setPersonnel(DepenceItem personnel) {
	this.personnel = personnel;
}
public DepenceItem getFonctionnement() {
	return fonctionnement;
}
public void setFonctionnement(DepenceItem fonctionnement) {
	this.fonctionnement = fonctionnement;
}
public DepenceItem getIntervention() {
	return intervention;
}
public void setIntervention(DepenceItem intervention) {
	this.intervention = intervention;
}
public DepenceItem getInvestissement() {
	return investissement;
}
public void setInvestissement(DepenceItem investissement) {
	this.investissement = investissement;
}
public DepenceItem getTotale() {
	return totale;
}
public void setTotale(DepenceItem totale) {
	this.totale = totale;
}
@Override
public int hashCode() {
	final int prime = 31;
	int result = 1;
	result = prime * result + ((dest == null) ? 0 : dest.hashCode());
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
	DepByDest other = (DepByDest) obj;
	if (dest == null) {
		if (other.dest != null)
			return false;
	} else if (!dest.equals(other.dest))
		return false;
	return true;
}
public double getTotaleAE() {
	return getPersonnel().getMontanAE()+getFonctionnement().getMontanAE()+
			getIntervention().getMontanAE()+getInvestissement().getMontanAE();
}
public double getTotaleCP() {
	return getPersonnel().getMontanCP()+getFonctionnement().getMontanCP()+
			getIntervention().getMontanCP()+getInvestissement().getMontanCP();
}
public void setMontantAE(String nature, BigDecimal ae) {
	
	if (NatGrpEnum.parse(nature) != null) {
		
		switch (NatGrpEnum.parse(nature)) {
		case NAT_GRP_1:
			getPersonnel().getDataAE().setMontant(ae);
			break;
		case NAT_GRP_2:
			getFonctionnement().getDataAE().setMontant(ae);
			break;
		case NAT_GRP_3:
			getInvestissement().getDataAE().setMontant(ae);
			break;
		case NAT_GRP_4:
			getIntervention().getDataAE().setMontant(ae);
			break;
		default:
			break;
		}
	}
	
}

public void setMontantCP(String nature, BigDecimal cp) {
	
	if (NatGrpEnum.parse(nature) != null) {
		
		switch (NatGrpEnum.parse(nature)) {
		case NAT_GRP_1:
			getPersonnel().getDataCP().setMontant(cp);
			break;
		case NAT_GRP_2:
			getFonctionnement().getDataCP().setMontant(cp);
			break;
		case NAT_GRP_3:
			getInvestissement().getDataCP().setMontant(cp);
			break;
		case NAT_GRP_4:
			getIntervention().getDataCP().setMontant(cp);
			break;
		default:
			break;
		}
	}
	
}
public BigDecimal getMontantAE(String nature) {
	
	BigDecimal montant=null;
	if (NatGrpEnum.parse(nature) != null) {
		
		switch (NatGrpEnum.parse(nature)) {
		case NAT_GRP_1:
			montant=getPersonnel().getDataAE().getMontant();
			break;
		case NAT_GRP_2:
			montant=getFonctionnement().getDataAE().getMontant();
			break;
		case NAT_GRP_3:
			montant=getInvestissement().getDataAE().getMontant();
			break;
		case NAT_GRP_4:
			montant=getIntervention().getDataAE().getMontant();
			break;
		default:
			break;
		}
	}
	return montant;
	
}
public BigDecimal getMontantCP(String nature) {
	
	BigDecimal montant=null;
	if (NatGrpEnum.parse(nature) != null) {
		
		switch (NatGrpEnum.parse(nature)) {
		case NAT_GRP_1:
			montant=getPersonnel().getDataCP().getMontant();
			break;
		case NAT_GRP_2:
			montant=getFonctionnement().getDataCP().getMontant();
			break;
		case NAT_GRP_3:
			montant=getInvestissement().getDataCP().getMontant();
			break;
		case NAT_GRP_4:
			montant=getIntervention().getDataCP().getMontant();
			break;
		default:
			break;
		}
	}
	return montant;
	
}

}
