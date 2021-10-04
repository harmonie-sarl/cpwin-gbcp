package fr.symphonie.budget.core.model.pluri;

import javax.persistence.Transient;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.symphonie.util.model.Trace;

public class CreditPaiement {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = LoggerFactory.getLogger(CreditPaiement.class);

	private int exerciceAE;
	private String codeBudget;
	private int exerciceCP;
	private String groupDest;
	private String groupNat;
	private String programme;
	private double montant;
	private Trace trace;
	private EnvelopBudg envelop;

	@Transient
	private boolean newCredit=false;
	public CreditPaiement() {
		super();
	}
	
	public CreditPaiement(EnvelopBudg envelop) {
		this();
		setNewCredit(true);
		setEnvelop(envelop) ;
		if(envelop!=null)
		setMontant(envelop.getMntVentile());
	}

	public int getExercice() {
		return exerciceAE;
	}
	public void setExercice(int exercice) {
		if (logger.isDebugEnabled()) {
			logger.debug("setExercice(String) - start"); //$NON-NLS-1$
		}
		
		this.exerciceAE = exercice;

		if (logger.isDebugEnabled()) {
			logger.debug("setExercice(String) - end: "+exercice); //$NON-NLS-1$
		}
	}
	public double getMontant() {
		return montant;
	}
	public void setMontant(double montant) {
		this.montant = montant;
	}

	public int getExerciceAE() {
		return exerciceAE;
	}

	public void setExerciceAE(int exerciceAE) {
		this.exerciceAE = exerciceAE;
	}

	public String getCodeBudget() {
		return codeBudget;
	}

	public void setCodeBudget(String codeBudget) {
		this.codeBudget = codeBudget;
	}

	public int getExerciceCP() {
		return exerciceCP;
	}

	public void setExerciceCP(int exerciceCP) {
		this.exerciceCP = exerciceCP;
	}

	public String getGroupDest() {
		return groupDest;
	}

	public void setGroupDest(String groupDest) {
		this.groupDest = groupDest;
	}

	public String getGroupNat() {
		return groupNat;
	}

	public void setGroupNat(String groupNat) {
		this.groupNat = groupNat;
	}

	public String getProgramme() {
		return programme;
	}

	public void setProgramme(String programme) {
		this.programme = programme;
	}

	public Trace getTrace() {
		return trace;
	}

	public void setTrace(Trace trace) {
		this.trace = trace;
	}

	public void setEnvelop(EnvelopBudg envelop) {
		this.envelop = envelop;
		refresh();
		
	}

	public EnvelopBudg getEnvelop() {
		return envelop;
	}
	public void refresh(){
		if(getEnvelop()!=null){
			setExercice(getEnvelop().getExercice());
			setExerciceAE(getEnvelop().getExercice());
			setCodeBudget(getEnvelop().getCodeBudget());
			setGroupDest(getEnvelop().getGroupDest());
			setGroupNat(getEnvelop().getGroupNat());
			setProgramme(getEnvelop().getProgramme());
		}
	}

	public void setNewCredit(boolean newCredit) {
		this.newCredit = newCredit;
	}

	public boolean isNewCredit() {
		return newCredit;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((codeBudget == null) ? 0 : codeBudget.hashCode());
		result = prime * result + exerciceAE;
		result = prime * result + exerciceCP;
		result = prime * result + ((groupDest == null) ? 0 : groupDest.hashCode());
		result = prime * result + ((groupNat == null) ? 0 : groupNat.hashCode());
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
		CreditPaiement other = (CreditPaiement) obj;
		if (codeBudget == null) {
			if (other.codeBudget != null)
				return false;
		} else if (!codeBudget.equals(other.codeBudget))
			return false;
		if (exerciceAE != other.exerciceAE)
			return false;
		if (exerciceCP != other.exerciceCP)
			return false;
		if (groupDest == null) {
			if (other.groupDest != null)
				return false;
		} else if (!groupDest.equals(other.groupDest))
			return false;
		if (groupNat == null) {
			if (other.groupNat != null)
				return false;
		} else if (!groupNat.equals(other.groupNat))
			return false;
		return true;
	}
	
	
}
