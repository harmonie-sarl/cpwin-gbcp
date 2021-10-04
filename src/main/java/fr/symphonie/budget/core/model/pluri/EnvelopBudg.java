package fr.symphonie.budget.core.model.pluri;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Transient;

import org.hibernate.annotations.Where;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.symphonie.util.Helper;
import fr.symphonie.util.model.SimpleEntity;
import fr.symphonie.util.model.Trace;

public class EnvelopBudg implements Comparable<EnvelopBudg>{
	/**
	 * Logger for this class
	 */
	private static final Logger logger = LoggerFactory.getLogger(EnvelopBudg.class);

private int exercice;
private String codeBudget;
private String groupDest;
private String groupNat;
private String type;

@Transient
private String programme;

@Transient
private String service;

private double montantAE;
private Trace trace;
@Transient
private String groupDestLib;
@Transient
private String groupNatLib;
@Transient
private String programmeLib;

//private List<EnvelopBudg> adrList;
private Set<CreditPaiement> listCreditPaiement;
@Where(clause="bp <> 0")
private Set<LigneBudgetaireAE> listLigneBudg;
//private Set<EnvelopInterneAE> listEnvelopInterne;

@Transient
private BudgetPluriannuel budget;
@Transient
private boolean newEnvelop=false;
@Transient
private double resteAV;
@Transient
private double adressResteAV;
@Transient
private List<CreditPaiement> cpList;

@Transient
private List<LigneBudgetaireAE> listLigneBudgView;
//@Transient
//private EnvelopBudgItem oldEnvelop;
@Transient
private SimpleEntity oldProgramme;
@Transient
private double oldMontantAE;
@Transient
private ComplexEntity groupDestNat;
@Transient
private SimpleEntity progSE;
@Transient
private boolean loaded=false;
public  EnvelopBudg() {
	super();

}


	public EnvelopBudg(BudgetPluriannuel budget, boolean newEnvelop) {
		this();
		this.setBudget(budget);
		this.setNewEnvelop(true);
	}

public double getMontantAE() {
	return montantAE;
}
public void setMontantAE(double montantAE) {
	this.montantAE = montantAE;
//	addCreditPaiement();
}

public void addCreditPaiement(int exercice) {
		if (logger.isDebugEnabled()) {
			logger.debug("addCreditPaiement() - start"); //$NON-NLS-1$
		}
		CreditPaiement credit = new CreditPaiement(this);
//		credit.setExerciceCP(getNextExerciceCp());
		credit.setExerciceCP(exercice);
		credit.setTrace(Helper.createTrace());
		addCreditPaiement(credit);

	
		if (logger.isDebugEnabled()) {
			logger.debug("addCreditPaiement():"+getCpList().size()+" - end"); //$NON-NLS-1$
		}	
}

//private int getNextExerciceCp() {
//	int execCP=getExercice();
//	if(!getCpList().isEmpty())
//	{
//	for(CreditPaiement cp:getCpList()){
//		if(cp.getExerciceCP()>execCP)execCP=cp.getExerciceCP();
//	}
//	execCP++;
//	}
//	logger.debug("getNextExerciceCp : "+execCP);
//	return execCP;
//}


public void setListCreditPaiement(Set<CreditPaiement> listCreditPaiement) {
	this.listCreditPaiement = listCreditPaiement;
}


public Set<CreditPaiement> getListCreditPaiement() {
	if(listCreditPaiement==null){
		listCreditPaiement=new HashSet<CreditPaiement>();
	}
	return listCreditPaiement;
}



public void setResteAV(double resteAV) {
		if (logger.isDebugEnabled()) {
			logger.debug("setResteAV(double) - start"+resteAV); //$NON-NLS-1$
		}

	this.resteAV = resteAV;
		if (logger.isDebugEnabled()) {
			logger.debug("setResteAV(double) - end"+resteAV); //$NON-NLS-1$
		}
}


public double getResteAV() {
	return resteAV;
}

public void setNewEnvelop(boolean newEnvelop) {
	this.newEnvelop = newEnvelop;
}


public boolean isNewEnvelop() {
	return newEnvelop;
}


public void setBudget(BudgetPluriannuel budget) {
	this.budget = budget;
	setCodeBudget(budget.getCodeBudget());
	setExercice(budget.getExercice());
//	setExerciceAE(budget.getExercice());
}


public BudgetPluriannuel getBudget() {
	return budget;
}



public void setListLigneBudg(Set<LigneBudgetaireAE> listLigneBudg) {
	this.listLigneBudg = listLigneBudg;
}


public Set<LigneBudgetaireAE> getListLigneBudg() {
	if(listLigneBudg==null)listLigneBudg=new HashSet<LigneBudgetaireAE>();
	return listLigneBudg;
}

	public void saveValues() {
		
		setOldMontantAE(getMontantAE());
//		setOldProgramme((SimpleEntity)getProgramme().clone());

	}
public void rollBackValues() {
   setMontantAE(getOldMontantAE());

//	setProgramme((SimpleEntity)getOldProgramme().clone());

	
}
//public void setOldEnvelop(EnvelopBudgItem oldEnvelop) {
//	this.oldEnvelop = oldEnvelop;
//}
//public EnvelopBudgItem getOldEnvelop() {
//	return oldEnvelop;
//}
public void setOldProgramme(SimpleEntity oldProgramme) {
	this.oldProgramme = oldProgramme;
}
public SimpleEntity getOldProgramme() {
	return oldProgramme;
}

public void setOldMontantAE(double oldMontantAE) {
	this.oldMontantAE = oldMontantAE;
}
public double getOldMontantAE() {
	return oldMontantAE;
}

	public double getMntVentile() {
		double result = 0d;
		result = getMontantAE() - getTotalMntCreditPaie();
		return result;

	}
	
	public boolean isAddAutorized1()
	{
		return (getMntVentile()>0);
	}
	public boolean isAdressAddAutorized()
	{
		return (getAdressResteAV()>0);
	}
	public double getTotalMntCreditPaie() {
		double result = 0d;
//		if (listCreditPaiement != null)
			for (CreditPaiement creditPaie : getCpList()) {
				result += creditPaie.getMontant();
			}
		return result;
	}
	public boolean isReserve(){
		
//		if(getService()==null)return false;
//		return Constant.RESERVE.equals(getService().getCode());
		return true;
	}
	
	public double getMntVentileAdress() {
		double result = 0d;
		result = getMontantAE() - getTotalMntLigneBudg();
		return result;

	}
	public double getTotalMntLigneBudg() {
		double result = 0d;
		if (getListLigneBudg() != null)
			for (LigneBudgetaireAE lb : getListLigneBudg()) {
				result += lb.getMontantAE();
			}
		return result;
	}

	public double getMaxMontantN() {		
		int numExec=getBudget().getExercice();
		return getMaxMontant(numExec); 
	}
	
	public double getMaxMontantN1() {
		int numExec=getBudget().getExercice();
		return getMaxMontant(numExec+1);
	}
	
	public double getMaxMontantN2() {
		int numExec=getBudget().getExercice();
		return getMaxMontant(numExec+2);
	}	
	
	private double getMaxMontant(int numExec) {
		if (numExec == 0)
			return 0;
		if (getListCreditPaiement() == null)
			return 0;
		for (CreditPaiement credit : getListCreditPaiement()) {
			try {
				if (credit.getExercice() == 0)
					continue;
				if ((credit.getExercice()) != numExec)
					continue;
				return credit.getMontant();
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
		return 0;
	}
	
	public boolean isDynamicMontantNDepass() {
		return isMontantDepass(getMontantN(), getMaxMontant(2013));
	}
	
	public boolean isMontantNDepass() {
		return isMontantDepass(getMontantN(), getMaxMontantN());
	}

	public boolean isMontantN1Depass() {
		return isMontantDepass(getMontantN1(), getMaxMontantN1());
	}

	public boolean isMontantN2Depass() {
		return isMontantDepass(getMontantN2(), getMaxMontantN2());
	}
	
	private boolean isMontantDepass(double montant, double maxMontant)
	{
        if(montant>maxMontant)return true;

		return false;
		
	}
	
	public double getMontantN() {

		return getMontant(0);
	}

	public double getMontantN1() {

		return getMontant(1);
	}

	public double getMontantN2() {

		return getMontant(2);
	}

	private double getMontant(int n) {
		double result = 0;	
		if (getListLigneBudg() == null)
			return 0;
		for (LigneBudgetaireAE lb : getListLigneBudg()) {
//			if (n == 0)
				result += lb.getMontantAE();
//			if (n == 1)
//				result += lb.getMontantN1();
//			if (n == 2)
//				result += lb.getMontantN2();

		}
		return result;
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


//	public int getExerciceAE() {
//		return exerciceAE;
//	}
//
//
//	public void setExerciceAE(int exerciceAE) {
//		this.exerciceAE = exerciceAE;
//		this.exercice=exerciceAE;
//	}


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
		logger.debug("setProgramme : "+programme);
		this.programme = programme;
	}


	public void setTrace(Trace trace) {
		this.trace = trace;
	}


	public Trace getTrace() {
		return trace;
	}


	public void setCpList(List<CreditPaiement> cpList) {
		this.cpList = cpList;
	}


	public List<CreditPaiement> getCpList() {
		if(cpList==null){
			cpList=new ArrayList<CreditPaiement>(getListCreditPaiement());
			
		}
		return cpList;
	}


	public void setGroupDestNat(ComplexEntity groupDestNat) {
//		logger.debug("------public void setGroupDestNat(ComplexEntity groupDestNat) -------groupDestNat.getEntity1().getCode()="+groupDestNat.getEntity1().getCode());
//		logger.debug("------public void setGroupDestNat(ComplexEntity groupDestNat) -------groupDestNat.getEntity2().getCode()="+groupDestNat.getEntity2().getCode());

		this.groupDestNat = groupDestNat;
		if(groupDestNat!=null){
			setGroupDest(groupDestNat.getEntity1().getCode());
			setGroupNat(groupDestNat.getEntity2().getCode());
		}
        }


	public ComplexEntity getGroupDestNat() {
		if(groupDestNat==null){
//			groupDestNat=new ComplexEntity();
//			groupDestNat.getEntity1().setCode(getGroupDest());
//			groupDestNat.getEntity2().setCode(getGroupNat());
			groupDestNat=new ComplexEntity(getGroupDest(),getGroupDestLib(),getGroupNat(),getGroupNatLib());
		
		}
		return groupDestNat;
	}
	public List<LigneBudgetaireAE> getListLigneBudgView() {
		if(listLigneBudgView==null)
		listLigneBudgView= new ArrayList<LigneBudgetaireAE>(getListLigneBudg());		
		return listLigneBudgView;
	}

	private void addCreditPaiement(CreditPaiement credit) {
		if(credit==null)return;
		getCpList().add(credit);
		
	}
	public void validateSaisie(){
		if(getProgramme()==null)setProgramme("");
		 setResteAV(getBudget().getResteAV())	;
		 for(CreditPaiement cp:getCpList()){
			 cp.refresh();
			 if(!cp.isNewCredit())continue;
			 if(!getListCreditPaiement().contains(cp))getListCreditPaiement().add(cp);
		 }
		 setCpList(null);
	}
	public void validateAdressage(){
//		if(getProgramme()==null)setProgramme("");
//		 setResteAV(getBudget().getResteAV())	;
		 for(LigneBudgetaireAE lb:getListLigneBudgView()){
			 lb.refresh();
			 if(!lb.isNewLb())continue;
			 if(!getListLigneBudg().contains(lb))getListLigneBudg().add(lb);
		 }
		 setListLigneBudgView(null);
	}

	public void setGroupDestLib(String groupDestLib) {
		this.groupDestLib = groupDestLib;
	}


	public String getGroupDestLib() {
		return groupDestLib;
	}


	public void setGroupNatLib(String groupNatLib) {
		this.groupNatLib = groupNatLib;
	}


	public String getGroupNatLib() {
		return groupNatLib;
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
	public void setProgrammeLib(String programmeLib) {
		this.programmeLib = programmeLib;
	}


	public String getProgrammeLib() {
		return programmeLib;
	}

	public void setAdressResteAV(double adressResteAV) {
		this.adressResteAV = adressResteAV;
	}


	public double getAdressResteAV() {
		double mntVentille=0;
		for(LigneBudgetaireAE lb:getListLigneBudgView())
			mntVentille+=lb.getMontantAE();
		adressResteAV= getMontantAE()-mntVentille;
		return adressResteAV;
	}


	public void setListLigneBudgView(List<LigneBudgetaireAE> listLigneBudgView) {
		this.listLigneBudgView = listLigneBudgView;
	}


	public void addLigneBudgetaire() {
		LigneBudgetaireAE lb=new LigneBudgetaireAE(this);
		lb.setTrace(Helper.createTrace());
		getListLigneBudgView().add(lb);
	}

	public String getCodeAndLibGroupNat() {
		if((getGroupNat()==null)||(getGroupNat().trim().isEmpty()))return "";
		return getGroupNat()+"-"+getGroupNatLib();
	}
	public String getCodeAndLibGroupDest() {
		if((getGroupDest()==null)||(getGroupDest().trim().isEmpty()))return "";
		return getGroupDest()+"-"+getGroupDestLib();
	}
	public String getCodeAndLibProg() {
		if((getProgramme()==null)||(getProgramme().trim().isEmpty()))return "";
		return getProgramme()+"-"+getProgrammeLib();
	}
	public boolean isEnvelop(){
		return true;
	}
	
	public String getNatureDestination(){
		return "";
	}
	
	public CreditPaiement getCreditPaiement(int numExec)
	{
		CreditPaiement result=null;
		if(getListCreditPaiement()==null)return null;
		for (CreditPaiement credit : getListCreditPaiement()) {
			if(credit.getExerciceCP()!=numExec)continue;
			result=credit;break;
		}
		return result;
	}

	public void setLoaded(boolean loaded) {
		this.loaded = loaded;
	}


	public boolean isLoaded() {
		return loaded;
	}

	public void updateTrace(Trace t) {
		getTrace().setAuteurMaj(t.getAuteurMaj());
		getTrace().setDateMaj(t.getDateMaj());
		
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((codeBudget == null) ? 0 : codeBudget.hashCode());
		result = prime * result + exercice;
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
		if (!(obj instanceof EnvelopBudg))
			return false;
		EnvelopBudg other = (EnvelopBudg) obj;
		if (codeBudget == null) {
			if (other.codeBudget != null)
				return false;
		} else if (!codeBudget.equals(other.codeBudget))
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
		return true;
	}
	public String getCodeAndLibServ() {
		
		if((getService()==null)||(getService().trim().isEmpty()))return "";
			return getService();
		}


	public void setService(String service) {
		this.service = service;
	}


	public String getService() {
		return service;
	}


	public void setType(String type) {
		this.type = type;
	}


	public String getType() {
		return type;
	}


	@Override
	public int compareTo(EnvelopBudg o) {
		return String.valueOf(this.getGroupNat()).compareTo(String.valueOf(o.getGroupNat()));
	}


	
}
