package fr.symphonie.budget.core.model.edition.util;

public class DepenceItem {
	
	private DataItem dataAE;
	private DataItem dataCP;
	
//	private static final Logger logger = LoggerFactory.getLogger(DepenceItem.class);
	

	public DepenceItem() {
		super();
	}
	

	public DepenceItem(DataRefEnum refAE,DataRefEnum refCP) {
		this();
		dataAE=new DataItem(refAE);
		dataCP=new DataItem(refCP);
	
	}
	public DepenceItem(DataRefEnum refAE, DataRefEnum refCP, String prefix) {
		this();
		dataAE=new DataItem(refAE,prefix);
		dataCP=new DataItem(refCP,prefix);
	}


	public void setMontants(double montanAE, double montanCP){
		getDataAE().setMontantDouble(montanAE);
		getDataCP().setMontantDouble(montanCP);
	}

	public double getMontanAE() {
		return getDataAE().getMontantDouble();
	}

	public double getMontanCP() {
		return getDataCP().getMontantDouble();
	}

	public DataItem getDataAE() {
		return dataAE;
	}

	public void setDataAE(DataItem dataAE) {
		this.dataAE = dataAE;
	}

	public DataItem getDataCP() {
		return dataCP;
	}

	public void setDataCP(DataItem dataCP) {
		this.dataCP = dataCP;
	}
	
	
}
