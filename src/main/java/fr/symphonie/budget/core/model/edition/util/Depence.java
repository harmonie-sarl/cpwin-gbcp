package fr.symphonie.budget.core.model.edition.util;

import fr.symphonie.common.util.Constant;

public class Depence {
	
	private DepenceItem personnel;
	private DepenceItem fonctionnement;
	private DepenceItem intervention;
	private DepenceItem investissement;
	
	private DepenceItem contribution;
	private DepenceItem recherche;
	
	//private static final Logger logger = LoggerFactory.getLogger(Depence.class);
	
	public Depence() {
		super();
		this.personnel = new DepenceItem(DataRefEnum.ref_01,DataRefEnum.ref_02);
		this.contribution=new DepenceItem(DataRefEnum.ref_03,DataRefEnum.ref_04);
		this.fonctionnement = new DepenceItem(DataRefEnum.ref_05,DataRefEnum.ref_06);
		this.intervention = new DepenceItem(DataRefEnum.ref_07,DataRefEnum.ref_08);
		this.investissement = new DepenceItem(DataRefEnum.ref_09,DataRefEnum.ref_10);
		this.recherche = new DepenceItem(DataRefEnum.ref_11,DataRefEnum.ref_12);
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

	@Override
	public String toString() {
		return "Depence [personnel=" + personnel + ", fonctionnement=" + fonctionnement + ", intervention="
				+ intervention + ", investissement=" + investissement + "]";
	}

	public DepenceItem getContribution() {
		return contribution;
	}

	public void setContribution(DepenceItem contribution) {
		this.contribution = contribution;
	}
	public void setMontants(double personnelAE, double fonctionnementAE, double interventionAE, double investissementAE,
			double personnelCP, double fonctionnementCP, double interventionCP, double investissementCP) {
		getPersonnel().setMontants(personnelAE, personnelCP);
		getFonctionnement().setMontants(fonctionnementAE, fonctionnementCP);
		getIntervention().setMontants(interventionAE, interventionCP);
		getInvestissement().setMontants(investissementAE, investissementCP);
		
	}
	public void setMontants(double[] montantsAE, double[] montantsCP) {
		getPersonnel().setMontants(montantsAE[Constant.PERSONNEL], montantsCP[Constant.PERSONNEL]);
		getFonctionnement().setMontants(montantsAE[Constant.FONCTIONNEMENT], montantsCP[Constant.FONCTIONNEMENT]);
		getIntervention().setMontants(montantsAE[Constant.INTERVENTION], montantsCP[Constant.INTERVENTION]);
		getInvestissement().setMontants(montantsAE[Constant.INVESTISSEMENT], montantsCP[Constant.INVESTISSEMENT ]);
	}
	
	public double getMontantAE(Integer index){
		double montantAE=0;
		switch(index){
		case Constant.PERSONNEL:
			montantAE=getPersonnel().getMontanAE();
			break;
		case Constant.FONCTIONNEMENT:
			montantAE=getFonctionnement().getMontanAE();
			break;
		case Constant.INTERVENTION:
			montantAE=getIntervention().getMontanAE();
			break;
		case Constant.INVESTISSEMENT:
			montantAE=getInvestissement().getMontanAE();
			break;
		}
		return montantAE;
	}
	public double getMontantCP(Integer index){
		double montantCP=0;
		switch(index){
		case Constant.PERSONNEL:
			montantCP=getPersonnel().getMontanCP();
			break;
		case Constant.FONCTIONNEMENT:
			montantCP=getFonctionnement().getMontanCP();
			break;
		case Constant.INTERVENTION:
			montantCP=getIntervention().getMontanCP();
			break;
		case Constant.INVESTISSEMENT:
			montantCP=getInvestissement().getMontanCP();
			break;
		}
		return montantCP;
	}

	public DepenceItem getRecherche() {
		return recherche;
	}

	public void setRecherche(DepenceItem recherche) {
		this.recherche = recherche;
	}
}
