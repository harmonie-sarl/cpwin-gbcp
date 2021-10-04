package fr.symphonie.budget.core.model.edition.util;

import fr.symphonie.budget.core.model.edition.Edition;
import fr.symphonie.budget.core.model.edition.Tab2;

public class ABE extends Tab2 {

	public ABE(Edition e) {
		super(e);
	}
	
	@Override
	public void setDefaults(){
		double TotalDepAe=0,TotalDepCp=0,solde=0;
			
			double recetGl=getRecette().getR11()+getRecette().getR12()+getRecette().getR13()+getRecette().getR14()+getRecette().getR15();
			getRecetGlobal().setMontantDouble(recetGl);
			double recetFle=getRecette().getR22()+getRecette().getR24()+getRecette().getR25();
			getTotalRecetFleche().setMontantDouble(recetFle);
			getTotalRecette().setMontantDouble(recetGl+recetFle);
			for(int i=0; i<=4;i++){
				TotalDepAe+=getDepence().getMontantAE(i);
				TotalDepCp+=getDepence().getMontantCP(i);
			}
			getTotalDepAe().setMontantDouble(TotalDepAe);
			getTotalDepCp().setMontantDouble(TotalDepCp);
			
			solde=getTotalRecette().getMontantDouble()-TotalDepCp;
			getSoldBudgExcedent().setMontantDouble(solde>0?solde:0);
			getSoldBudgDeficit().setMontantDouble(solde<0?Math.abs(solde):0);

			
		}

}
