package fr.symphonie.budget.core.model.edition.util;

import fr.symphonie.budget.core.model.edition.Edition;
import fr.symphonie.budget.core.model.edition.Tab4;
import fr.symphonie.util.Helper;

public class EFE extends Tab4 {

	public EFE(Edition e) {
		super(e);
	}
	@Override	
	public void setDefaults()

	{
		getSoldBudgDeficit().setMontantDouble(getParent().getAbe().getSoldBudgDeficit().getMontantDouble());
		getSoldBudgExcedent().setMontantDouble(getParent().getAbe().getSoldBudgExcedent().getMontantDouble());
		
		double SousTotOper1=getSoldBudgDeficit().getMontantDouble()+getB1().getMontantDouble()+getCompteTiersC1().getMontantDouble()+getE1().getMontantDouble();
		double SousTotOper2=getSoldBudgExcedent().getMontantDouble()+getB2().getMontantDouble()+getCompteTiersC2().getMontantDouble()+getE2().getMontantDouble();
		getSousTotOper1().setMontantDouble(Helper.round(SousTotOper1, 2));
		getSousTotOper2().setMontantDouble(Helper.round(SousTotOper2, 2));
		
		double abonnement=getSousTotOper2().getMontantDouble()-getSousTotOper1().getMontantDouble();
		
		getAbondTresorie().setMontantDouble(abonnement>0?abonnement:0);
		getPrelevTresorie().setMontantDouble(abonnement<0?Math.abs(abonnement):0);
		
		double m1=getAbondTresorie().getMontantDouble();
		double m2=getAbondTresorieFleche().getMontantDouble();
		double m3=getPrelevTresorieFleche().getMontantDouble();
		
		getAbondTresorieNonFleche().setMontantDouble(m1==0?0:m1-m2+m3);
		
		m1=getPrelevTresorie().getMontantDouble();
		m2=getPrelevTresorieFleche().getMontantDouble();
		m3=getAbondTresorieFleche().getMontantDouble();
		
		getPrelevTresorieNonFleche().setMontantDouble(m1==0?0:m1-m2+m3);
		
		getTotalBesoin().setMontantDouble(SousTotOper1+getAbondTresorie().getMontantDouble());
		getTotalFinanc().setMontantDouble(SousTotOper2+getPrelevTresorie().getMontantDouble());


	}
}
