package fr.symphonie.budget.core.model.pluri;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import javax.persistence.Transient;

import org.hibernate.annotations.Where;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.symphonie.cpwin.model.Visa;
import fr.symphonie.cpwin.model.VisaEnum;
import fr.symphonie.util.model.Trace;

public class BudgetPluriannuel {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = LoggerFactory.getLogger(BudgetPluriannuel.class);

	/**
	 * Exercice d'autorisation d'engagement
	 */
private int exercice;
private String codeBudget;
private Double recette;
private Double depense;// Somme des CP
/**
 * Visa de l'ordonnateur
 */
private Visa visaOrdo;
/**
 * visa du contrôle financier
 */
private Visa visaCF;
/**
 * visa de l'agent comptable
 */
private Visa visaAC;
private String etatCp;
private boolean editionBI;
@Where(clause="dep_rec='D' and nat_grp in('1','2','3','4' )")
private Set<EnvelopBudg> listEnvelopBudg;
private Trace trace;
@Transient
private List<EnvelopBudg> envelopList;

public int getExercice() {
	return exercice;
}
public void setExercice(int numExec) {
	this.exercice = numExec;
}
public String getCodeBudget() {
	return codeBudget;
}
public void setCodeBudget(String codeBudget) {
	this.codeBudget = codeBudget;
}
public Double getMontantAE() {
    
	return getDepense();
}
public void setMontantAE(Double montantAE) {
	setDepense(montantAE);
}
public Double getRecette() {
	return recette;
}
public void setRecette(Double recette) {
	this.recette = recette;
}
public Set<EnvelopBudg> getListEnvelopBudg() {
	
	return listEnvelopBudg;
}
public void setListEnvelopBudg(Set<EnvelopBudg> listEnvelopBudg) {
	this.listEnvelopBudg = listEnvelopBudg;
}

public double getResteAV() {
	if (logger.isDebugEnabled()) {
		logger.debug("getResteAV() - start: "); //$NON-NLS-1$
	}

double resteAV=getMontantAE()-getTotalMontantAE();

	if (logger.isDebugEnabled()) {
		logger.debug("getResteAV() - end :"); //$NON-NLS-1$
	}
   return resteAV;
}



	public double getTotalMontantAE() {

		double result = 0d;

		if (getListEnvelopBudg() != null)

			for (EnvelopBudg env : getListEnvelopBudg()) {
				result += env.getMontantAE();
			}
		return result;
	}
	public double getTotalMontantCP() {

		double result = 0d;

		if (getListEnvelopBudg() != null)

			for (EnvelopBudg env : getListEnvelopBudg()) {
				result += env.getTotalMntCreditPaie();
			}
		return result;
	}
	public boolean isAddAutorized()
	{
		
		return (getResteAV()>0)&&(!isVisee());
	}
	public void setDepense(Double depense) {
		this.depense = depense;
	}
	public Double getDepense() {
		return depense;
	}
	public void setEnvelopList(List<EnvelopBudg> envelopList) {
		this.envelopList = envelopList;
	}
	public List<EnvelopBudg> getEnvelopList() {
		return envelopList;
	}
	public void setTrace(Trace trace) {
		this.trace = trace;
	}
	public Trace getTrace() {
		return trace;
	}

	
	public List<EnvelopBudg> getListEnvelopBudgView() {
		List<EnvelopBudg> listEnvelopBudgView = new ArrayList<EnvelopBudg>(getListEnvelopBudg());
//		Collections.sort(listEnvelopBudgView);
		return listEnvelopBudgView;
	}
//	public void setListEnvBudg(List<EnvelopBudg> listEnvBudg) {
//		this.listEnvBudg = listEnvBudg;
//	}
	public List<EnvelopBudg> getListEnvBudg() {
		List<EnvelopBudg> listEnvBudg=new ArrayList<EnvelopBudg>(getListEnvelopBudg());
		Collections.sort(listEnvBudg);
		return listEnvBudg;
		
	}
	public void updateDepense() {
		setDepense(getTotalMontantCP());
		
	}
	public void updateTrace(Trace t) {
		getTrace().setAuteurMaj(t.getAuteurMaj());
		getTrace().setDateMaj(t.getDateMaj());
		
	}
	public Visa getVisaOrdo() {
		return visaOrdo;
	}
	public void setVisaOrdo(Visa visaOrdo) {
		this.visaOrdo = visaOrdo;
	}
	public Visa getVisaCF() {
		return visaCF;
	}
	public void setVisaCF(Visa visaCF) {
		this.visaCF = visaCF;
	}
	public Visa getVisaAC() {
		return visaAC;
	}
	public void setVisaAC(Visa visaAC) {
		this.visaAC = visaAC;
	}
 public boolean isVisee(){
	 boolean visee=isOrdoAccept()||isCfAccept()||isAcAccept();
		if (logger.isInfoEnabled()) {
			logger.info("isVisee() - " + visee); //$NON-NLS-1$
		}
	 
	 return visee;
 }
 public boolean isOrdoAccept(){
	 Visa v=getVisaOrdo();
	 if((v!=null)&&(v.getVisa()!=null)&&(v.getVisa()==VisaEnum.Accepte))
		 return true;
	 return false;
 }
 public boolean isCfAccept(){
	 Visa v=getVisaCF();
	 if((v!=null)&&(v.getVisa()!=null)&&(v.getVisa()==VisaEnum.Accepte))
		 return true;
	 return false;
 }
 public boolean isAcAccept(){
	 Visa v=getVisaAC();
	 if((v!=null)&&(v.getVisa()!=null)&&(v.getVisa()==VisaEnum.Accepte))
		 return true;
	 return false;
 }
public void setEtatCp(EtatEnum etatCp) {
	this.etatCp = etatCp.getEtat();
}
public EtatEnum getEtatCp() {
	return EtatEnum.parse(etatCp);
}
public boolean isEditionBI() {
	return editionBI;
}
public void setEditionBI(boolean editionBI) {
	this.editionBI = editionBI;
}


public boolean isValide()
{
	return EtatEnum.VALIDE==getEtatCp();
	}


}
