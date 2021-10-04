package fr.symphonie.budget.core.model.edition;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.symphonie.budget.core.model.edition.util.DataItem;
import fr.symphonie.budget.core.model.edition.util.GenericTab;
import fr.symphonie.budget.core.model.edition.util.TDD;
import fr.symphonie.budget.core.model.edition.util.TRO;

public class Tab3 extends GenericTab {
	 private static final Logger logger = LoggerFactory.getLogger(Tab3.class);
	private TDD tdd;
	private TRO tro;
	
	
	public Tab3(Edition e) {
		super(e);
		this.tdd = new TDD(e);
		this.tro = new TRO(e);
	}
	public TDD getTdd() {
		return tdd;
	}
	public void setTdd(TDD tdd) {
		this.tdd = tdd;
	}
	public TRO getTro() {
		return tro;
	}
	public void setTro(TRO tro) {
		this.tro = tro;
	}
	
	public void setDefaults() {
		getTdd().setDefaults();
		getTro().setDefaults();
		
		double c=getTro().getTotale().getTotale().getMontantDouble();
		double b=getTdd().getTotale().getTotale().getMontanCP();
		double solde=c-b;
		if(solde>0) getTdd().getSoldeBudgExcedent().setMontantDouble(solde);
		else getTro().getSoldeBudgDeficit().setMontantDouble(Math.abs(solde));
		logger.debug("setDefaults :solde= "+solde);
	}
	@Override
	public List<DataItem> getItems() {
		// TODO Auto-generated method stub
		return null;
	}
}
