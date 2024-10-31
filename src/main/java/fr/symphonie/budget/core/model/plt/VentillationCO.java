package fr.symphonie.budget.core.model.plt;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import fr.symphonie.cpwin.model.Visa;
import fr.symphonie.cpwin.model.VisaEnum;

public class VentillationCO extends GenericLigneTresorerie {
	protected Integer exercice;
	protected Integer numero;	
	private BigDecimal budgetInitiale;

	
	public VentillationCO() {
		super();
		setBudgetInitiale(new BigDecimal(0));
	}
	

	public BigDecimal getBudgetInitiale() {
		return budgetInitiale;
	}

	public void setBudgetInitiale(BigDecimal budgetInitiale) {
		this.budgetInitiale = budgetInitiale;
	}
	
	public Integer getNumero() {
		return numero;
	}
@Override
	public void setNumero(Integer numero) {
		this.numero = numero;
	}
	public Integer getExercice() {
		return exercice;
	}

	public void setExercice(Integer exercice) {
		this.exercice = exercice;
	}
	public boolean isBiDisabled(){
		Integer[] LIGNE_WITH_BI={3,4,5,6,7,8,9,10,21,22,23,24};
		List<Integer> list=Arrays.asList(LIGNE_WITH_BI);
		return list.contains(getNumero());
	}
	public boolean isBiDisabled2024(){
		Integer[] LIGNE_WITH_BI={3,4,5,6,7,8,9,10,11,12,23,24,25,26};
		List<Integer> list=Arrays.asList(LIGNE_WITH_BI);
		return list.contains(getNumero());
	}
	public DetailLigneTresorerie convert(){
		DetailLigneTresorerie detailLigne=new DetailLigneTresorerie( (GenericLigneTresorerie)this);
		detailLigne.setExercice(this.getExercice());
		Visa visa = new Visa();
		visa.setVisa(VisaEnum.Auto);visa.setAuteur("");
		detailLigne.setPrevisionnel(visa);
		visa = new Visa();
		visa.setVisa(VisaEnum.Auto);visa.setAuteur("");
		detailLigne.setRealise(visa);
		
		return detailLigne;
	}

}
