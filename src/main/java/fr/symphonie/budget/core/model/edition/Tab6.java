package fr.symphonie.budget.core.model.edition;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.symphonie.budget.core.model.edition.util.CAF;
import fr.symphonie.budget.core.model.edition.util.CRP;
import fr.symphonie.budget.core.model.edition.util.TFP;
import fr.symphonie.budget.core.model.edition.util.VFR;

public class Tab6  {
	
	private TFP tfp;

	private CAF caf;
	private CRP crp;
	private VFR vfr;

	private static final Logger logger = LoggerFactory.getLogger(Tab6.class);


	public Tab6(Edition e) {
		super();
		this.setTfp(new TFP(e));
		this.caf=new CAF(e);
		this.crp=new CRP(e);
		this.vfr=new VFR(e);
		logger.debug("--construtios: "+this.toString());
	}

	public CAF getCaf() {
		return caf;
		
	}


	public void setCaf(CAF noBudgData) {
		this.caf = noBudgData;		
	}


	@Override
	public String toString() {
		return "Tab6 [noBudgData=" + getCaf() + "]";
	}

	public TFP getTfp() {
		return tfp;
	}


	public void setTfp(TFP tfp) {
		this.tfp = tfp;
	}


	public CRP getCrp() {
		return crp;
	}


	public void setCrp(CRP crp) {
		this.crp = crp;
	}


	public VFR getVfr() {
		return vfr;
	}


	public void setVfr(VFR vfr) {
		this.vfr = vfr;
	}
	public void refreshTotaux(){
		getCrp().refreshTotaux();
		getCaf().refreshTotaux();
		
	}
	
	}
