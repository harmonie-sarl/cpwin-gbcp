package fr.symphonie.budget.core.model.edition.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DepenceDest {
	
	
	
	private MontantItem personnel;
	private MontantItem fonctionnement;
	private MontantItem intervention;
	private MontantItem investissement;
	
	
	private static final Logger logger = LoggerFactory.getLogger(DepenceDest.class);
	
	
//	public CpDest() {
//		super();
//		
//	}
	
	
	
	
	
	public DepenceDest()
	{
		super();
	}





	public DepenceDest(MontantItem personnel, MontantItem fonctionnement, MontantItem intervention, MontantItem investissement) {
	super();
	this.personnel = personnel;
	this.fonctionnement = fonctionnement;
	this.intervention = intervention;
	this.investissement = investissement;
	logger.debug("--construtios: "+this.toString());
}





	public MontantItem getPersonnel() {
		return personnel;
	}
	public void setPersonnel(MontantItem personnel) {
		this.personnel = personnel;
	}
	public MontantItem getFonctionnement() {
		return fonctionnement;
	}
	public void setFonctionnement(MontantItem fonctionnement) {
		this.fonctionnement = fonctionnement;
	}
	public MontantItem getIntervention() {
		return intervention;
	}
	public void setIntervention(MontantItem intervention) {
		this.intervention = intervention;
	}
	public MontantItem getInvestissement() {
		return investissement;
	}
	public void setInvestissement(MontantItem investissement) {
		this.investissement = investissement;
	}


	public double  getTotalInvest(){
		return getInvestissement().getTotal();
	
	}
	
	public double  getTotalPersonnel(){
		return getPersonnel().getTotal();
	
	}
	public double  getTotalFonctionnement(){
		return getFonctionnement().getTotal();
	
	}
	public double  getTotalIntervention(){
		return getIntervention().getTotal();
	
	}


	@Override
	public String toString() {
		return "DepenceDest [personnel=" + personnel + ", fonctionnement=" + fonctionnement + ", intervention="
				+ intervention + ", investissement=" + investissement + "]";
	}
	
    public double getTotalMontant(int index){
		
		return getPersonnel().getMontant(index)+getFonctionnement().getMontant(index)+getIntervention().getMontant(index)+getInvestissement().getMontant(index);
	}
	

}
