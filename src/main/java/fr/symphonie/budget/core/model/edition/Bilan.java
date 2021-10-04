package fr.symphonie.budget.core.model.edition;

import java.util.ArrayList;
import java.util.List;

import fr.symphonie.budget.core.model.edition.util.AC;
import fr.symphonie.budget.core.model.edition.util.AI;
import fr.symphonie.budget.core.model.edition.util.DF;
import fr.symphonie.budget.core.model.edition.util.DNF;
import fr.symphonie.budget.core.model.edition.util.DataItem;
import fr.symphonie.budget.core.model.edition.util.FP;
import fr.symphonie.budget.core.model.edition.util.PRC;
import fr.symphonie.budget.core.model.edition.util.TresA;
import fr.symphonie.budget.core.model.edition.util.TresP;

public class Bilan  {
	
	
	 private  AI ai;
	 private  AC ac;	
	 private  TresA tresA;
	 private  FP fp;
	 private  PRC prc;
	 private  DF df;
	 private  DNF dnf;
	 private  TresP tresP;
	
	
	//private static final Logger logger = LoggerFactory.getLogger(Bilan.class);


	public Bilan() {
		super();
		this.ai = new AI() ;
		this.ac = new AC();
		this.tresA = new TresA();
		this.fp = new FP();
		this.prc = new PRC();
		this.df = new DF();
		this.dnf = new DNF();
		this.tresP = new TresP();
	}

	
	

	public AI getAi() {
		return ai;
	}




	public void setAi(AI ai) {
		this.ai = ai;
	}




	public AC getAc() {
		return ac;
	}




	public void setAc(AC ac) {
		this.ac = ac;
	}




	public TresA getTresA() {
		return tresA;
	}




	public void setTresA(TresA tresA) {
		this.tresA = tresA;
	}




	public FP getFp() {
		return fp;
	}




	public void setFp(FP fp) {
		this.fp = fp;
	}




	public PRC getPrc() {
		return prc;
	}




	public void setPrc(PRC prc) {
		this.prc = prc;
	}




	public DF getDf() {
		return df;
	}




	public void setDf(DF df) {
		this.df = df;
	}




	public DNF getDnf() {
		return dnf;
	}




	public void setDnf(DNF dnf) {
		this.dnf = dnf;
	}




	public TresP getTresP() {
		return tresP;
	}




	public void setTresP(TresP tresP) {
		this.tresP = tresP;
	}
	
	public List<DataItem> getItems() {
		List<DataItem> items=new ArrayList<DataItem>();
		items.addAll(getAi().getItems());
		items.addAll(getAc().getItems());
		items.addAll(getTresA().getItems());
		items.addAll(getFp().getItems());
		items.addAll(getPrc().getItems());
		items.addAll(getDf().getItems());
		items.addAll(getDnf().getItems());
		items.addAll(getTresP().getItems());	
		return items;
	}
	
	
	public List<DataItem> getItemsAnt() {
		List<DataItem> items=new ArrayList<DataItem>();
		items.addAll(getAi().getItemsAnt());
		items.addAll(getAc().getItemsAnt());
		items.addAll(getTresA().getItemsAnt());
		items.addAll(getFp().getItemsAnt());
		items.addAll(getPrc().getItemsAnt());
		items.addAll(getDf().getItemsAnt());
		items.addAll(getDnf().getItemsAnt());
		items.addAll(getTresP().getItemsAnt());	
		return items;
	}
	
	public void setDefaults() {
		getAi().setDefaults();
		getAc().setDefaults();
		getTresA().setDefaults();
		getFp().setDefaults();
		getPrc().setDefaults();
		getDf().setDefaults();
		getDnf().setDefaults();
		getTresP().setDefaults();
	}
	
}
