package fr.symphonie.budget.core.model.pluri;

import javax.persistence.Transient;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.symphonie.budget.ui.beans.pluri.BudgetPluriannuelBean;
import fr.symphonie.util.Helper;
import fr.symphonie.util.model.SimpleEntity;
import fr.symphonie.util.model.Trace;

public class LigneBudgetaireAE {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = LoggerFactory.getLogger(LigneBudgetaireAE.class);

	private int exercice;
	private String codeBudget;
	private String groupDest;
	private String groupNat;
	private String destination;
	private String nature;
	private String programme;
	private String service;
	private Integer noLbi;
	private Integer noEnv;
	private double montantAE;
	private EnvelopBudg envelop;
	private Trace trace;
	private String typeDepRec;
	@Transient
	private SimpleEntity progSE;
	@Transient
	private SimpleEntity serviceSE;
	@Transient
	private ComplexEntity destNat;
	@Transient
	private boolean newLb=false;
	public LigneBudgetaireAE() {
		super();
		
	}
	public LigneBudgetaireAE(EnvelopBudg envelop) {
		this();
		setEnvelop( envelop);
		setNewLb(true);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((codeBudget == null) ? 0 : codeBudget.hashCode());
		result = prime * result + ((destNat == null) ? 0 : destNat.hashCode());
		result = prime * result
				+ ((destination == null) ? 0 : destination.hashCode());
		result = prime * result + exercice;
		result = prime * result
				+ ((groupDest == null) ? 0 : groupDest.hashCode());
		result = prime * result
				+ ((groupNat == null) ? 0 : groupNat.hashCode());
		result = prime * result + ((nature == null) ? 0 : nature.hashCode());
		result = prime * result
				+ ((programme == null) ? 0 : programme.hashCode());
		result = prime * result + ((service == null) ? 0 : service.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof LigneBudgetaireAE))
			return false;
		LigneBudgetaireAE other = (LigneBudgetaireAE) obj;
		if (codeBudget == null) {
			if (other.codeBudget != null)
				return false;
		} else if (!codeBudget.equals(other.codeBudget))
			return false;
		if (destNat == null) {
			if (other.destNat != null)
				return false;
		} else if (!destNat.equals(other.destNat))
			return false;
		if (destination == null) {
			if (other.destination != null)
				return false;
		} else if (!destination.equals(other.destination))
			return false;
		if (exercice != other.exercice)
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
		if (nature == null) {
			if (other.nature != null)
				return false;
		} else if (!nature.equals(other.nature))
			return false;
		if (programme == null) {
			if (other.programme != null)
				return false;
		} else if (!programme.equals(other.programme))
			return false;
		if (service == null) {
			if (other.service != null)
				return false;
		} else if (!service.equals(other.service))
			return false;
		return true;
	}

	public double getMontantAE() {
		return montantAE;
	}
	public double getSommeMontant(){
		if (logger.isDebugEnabled()) {
			logger.debug("getSommeMontant() - start getListLigneBudg :"+envelop.getListLigneBudg()); //$NON-NLS-1$
		}

		EnvelopBudg envelop=((BudgetPluriannuelBean)Helper.findBean("bpBean")).getEnvelopBudg();
		int somme=0;
		
		for (LigneBudgetaireAE lb : envelop.getListLigneBudg()) {		
			somme+=lb.getMontantAE();			
		}

		if (logger.isDebugEnabled()) {
			logger.debug("getSommeMontant() - end"); //$NON-NLS-1$
		}
		return somme;
	}
	
	public void setMontantAE(double montantAE) {
		this.montantAE = montantAE;
	}
	public double getResteAV() {
		EnvelopBudg envelop=((BudgetPluriannuelBean)Helper.findBean("bpBean")).getEnvelopBudg();
		double reste=0;
		reste= (envelop.getMontantAE())-getSommeMontant();
		return reste;
	}
	
	public boolean isReserve() {
		return false;
	}

public Integer getNoLbi() {
	return noLbi;
}

public void setNoLbi(Integer noLbi) {
	this.noLbi = noLbi;
}

public int getExercice() {
	return exercice;
}

public void setExercice(int exercice) {
	this.exercice = exercice;
}

public String getCodeBudget() {
	return codeBudget;
}

public void setCodeBudget(String codeBudget) {
	this.codeBudget = codeBudget;
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

public String getDestination() {
	return destination;
}

public void setDestination(String destination) {
	this.destination = destination;
}

public String getNature() {
	return nature;
}

public void setNature(String nature) {
	this.nature = nature;
}

public String getProgramme() {
	return programme;
}

public void setProgramme(String programme) {
	this.programme = programme;
}

public String getService() {
	return service;
}

public void setService(String service) {
	this.service = service;
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
		setCodeBudget(getEnvelop().getCodeBudget());
		setGroupDest(getEnvelop().getGroupDest());
		setGroupNat(getEnvelop().getGroupNat());
	}
}

public void setTrace(Trace trace) {
	this.trace = trace;
}

public Trace getTrace() {
	return trace;
}

public void setProgSE(SimpleEntity progSE) {
	this.progSE = progSE;
	if(progSE!=null){
		setProgramme(progSE.getCode());
	}
}


public SimpleEntity getProgSE() {
	if(progSE==null)progSE=new SimpleEntity();
	progSE.setCode(getProgramme());
	return progSE;
}

public void setServiceSE(SimpleEntity serviceSE) {
	this.serviceSE = serviceSE;
	if(serviceSE!=null){
		setService(serviceSE.getCode());
	}
}

public SimpleEntity getServiceSE() {
	if(serviceSE==null)serviceSE=new SimpleEntity();
	serviceSE.setCode(getService());
	return serviceSE;
}

public void setDestNat(ComplexEntity destNat) {
	this.destNat = destNat;
	if(destNat!=null){
		setDestination(destNat.getEntity1().getCode());
		setNature(destNat.getEntity2().getCode());
	}
}

public ComplexEntity getDestNat() {
	if(destNat==null){
		destNat=new ComplexEntity();
		destNat.getEntity1().setCode(getDestination());
		destNat.getEntity2().setCode(getNature());
	}
	return destNat;
}

public void setNewLb(boolean newLb) {
	this.newLb = newLb;
}

public boolean isNewLb() {
	return newLb;
}
	
public String getCodeAndLibGroupNat() {
	
	return getEnvelop().getCodeAndLibGroupNat();
}
public String getCodeAndLibGroupDest() {
	
	return getEnvelop().getCodeAndLibGroupDest();
}
public String getCodeAndLibProg() {
	
	return getProgramme();
}
public boolean isEnvelop(){
	return false;
}
public String getNatureDestination(){
	String retour="";
	retour=(getDestination()==null)?"":getDestination();
	retour+=(getNature()==null)?"/":"/"+getNature();
	
	return retour;
}
public String getCodeAndLibServ() {
	
	if((getService()==null)||(getService().trim().isEmpty()))return "";
		return getService();
	}
@Override
public String toString() {
	StringBuilder builder = new StringBuilder();
	builder.append("LigneBudgetaireAE [groupDest=");
	builder.append(groupDest);
	builder.append(", groupNat=");
	builder.append(groupNat);
	builder.append(", destination=");
	builder.append(destination);
	builder.append(", nature=");
	builder.append(nature);
	builder.append(", programme=");
	builder.append(programme);
	builder.append(", service=");
	builder.append(service);
	builder.append("]");
	return builder.toString();
}

public String getTypeDepRec() {
	return typeDepRec;
}

public void setTypeDepRec(String typeDepRec) {
	this.typeDepRec = typeDepRec;
}
public Integer getNoEnv() {
	return noEnv;
}
public void setNoEnv(Integer noEnv) {
	this.noEnv = noEnv;
}

}
