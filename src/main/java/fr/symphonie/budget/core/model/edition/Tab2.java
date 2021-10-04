package fr.symphonie.budget.core.model.edition;

import java.util.Arrays;
import java.util.List;

import fr.symphonie.budget.core.model.edition.util.DataItem;
import fr.symphonie.budget.core.model.edition.util.DataRefEnum;
import fr.symphonie.budget.core.model.edition.util.Depence;
import fr.symphonie.budget.core.model.edition.util.GenericTab;
import fr.symphonie.budget.core.model.edition.util.Recette;

public class Tab2 extends GenericTab {

	
	private Depence depence;
	private Recette recette;
	private DataItem recetGlobal;
	private DataItem totalRecetFleche;
	private DataItem totalDepAe;
	private DataItem totalDepCp;
	private DataItem totalRecette;
	private DataItem soldBudgExcedent;
	private DataItem soldBudgDeficit;

	public Tab2(Edition e) {
		super(e);
		this.depence= new Depence();
		this.recette = new Recette();
		this.recetGlobal =new DataItem(DataRefEnum.ref_13);
		this.totalRecetFleche = new DataItem(DataRefEnum.ref_19);
		this.totalDepAe = new DataItem(DataRefEnum.ref_23);
		this.totalDepCp = new DataItem(DataRefEnum.ref_24);
		this.totalRecette = new DataItem(DataRefEnum.ref_25);
		this.soldBudgExcedent = new DataItem(DataRefEnum.ref_26);
		this.soldBudgDeficit = new DataItem(DataRefEnum.ref_27);
	}

	public Depence getDepence() {
		return depence;
	}

	public void setDepence(Depence depence) {
		this.depence = depence;
	}

	public Recette getRecette() {
		return recette;
	}

	public void setRecette(Recette recette) {
		this.recette = recette;
	}
	
	public DataItem getRecetGlobal() {
		return recetGlobal;
	}

	public void setRecetGlobal(DataItem recetGlobal) {
		this.recetGlobal = recetGlobal;
	}

	public DataItem getTotalRecetFleche() {
		return totalRecetFleche;
	}

	public void setTotalRecetFleche(DataItem totalRecetFleche) {
		this.totalRecetFleche = totalRecetFleche;
	}

	public DataItem getTotalDepAe() {
		return totalDepAe;
	}

	public void setTotalDepAe(DataItem totalDepAe) {
		this.totalDepAe = totalDepAe;
	}

	public DataItem getTotalDepCp() {
		return totalDepCp;
	}

	public void setTotalDepCp(DataItem totalDepCp) {
		this.totalDepCp = totalDepCp;
	}

	public DataItem getTotalRecette() {
		return totalRecette;
	}

	public void setTotalRecette(DataItem totalRecette) {
		this.totalRecette = totalRecette;
	}

	public DataItem getSoldBudgExcedent() {
		return soldBudgExcedent;
	}

	public void setSoldBudgExcedent(DataItem soldBudgExcedent) {
		this.soldBudgExcedent = soldBudgExcedent;
	}

	public DataItem getSoldBudgDeficit() {
		return soldBudgDeficit;
	}

	public void setSoldBudgDeficit(DataItem soldBudgDeficit) {
		this.soldBudgDeficit = soldBudgDeficit;
	}

	@Override
	public List<DataItem> getItems() {
		DataItem[] tab = new DataItem[] {
				recetGlobal,
				totalRecetFleche,
				totalDepAe,
				totalDepCp,
				totalRecette,
				soldBudgExcedent,
				soldBudgDeficit,
				getDepence().getPersonnel().getDataAE(),getDepence().getPersonnel().getDataCP(),getDepence().getFonctionnement().getDataAE(),getDepence().getFonctionnement().getDataCP(),
				getDepence().getIntervention().getDataAE(),getDepence().getIntervention().getDataCP(),getDepence().getInvestissement().getDataAE(),getDepence().getInvestissement().getDataCP(),
				getDepence().getContribution().getDataAE(),getDepence().getContribution().getDataCP(),
				//Rec data
				getRecette().getDataR11(),getRecette().getDataR12(),getRecette().getDataR13(),getRecette().getDataR14(),getRecette().getDataR15(),getRecette().getDataR22(),getRecette().getDataR24(),getRecette().getDataR25()
				
		};
		return Arrays.asList(tab);
	}
	
	@Override
public void setDefaults(){
		
		getRecetGlobal().setMontantDouble(getParent().getRecetGlobal());
		getTotalRecetFleche().setMontantDouble(getParent(). getTotalRecetFleche());
		getTotalDepAe().setMontantDouble(getParent().getTotalDepAe());
		getTotalDepCp().setMontantDouble(getParent().getTotalDepCp());
		getTotalRecette().setMontantDouble(getParent().getTotalRecette());
		getSoldBudgExcedent().setMontantDouble(getParent().getSoldBudgExcedent());
		getSoldBudgDeficit().setMontantDouble(getParent().getSoldBudgDeficit());

		
	}
	
	
	public void resetRecette()
	{
		this.recette = new Recette();
		this.recetGlobal =new DataItem(DataRefEnum.ref_13);
		this.totalRecetFleche = new DataItem(DataRefEnum.ref_19);
		this.totalRecette = new DataItem(DataRefEnum.ref_25);
		this.soldBudgDeficit = new DataItem(DataRefEnum.ref_27);
	}
	public void resetDepense()
	{
		this.depence= new Depence();
		this.totalDepAe = new DataItem(DataRefEnum.ref_23);
		this.totalDepCp = new DataItem(DataRefEnum.ref_24);
		this.soldBudgExcedent = new DataItem(DataRefEnum.ref_26);
	}
	public void initialize() {
		getDepence().setMontants(0, 0, 0, 0, 0, 0, 0, 0);
	}
}
