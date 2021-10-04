package fr.symphonie.budget.core.model.edition.util;

import java.util.ArrayList;
import java.util.List;

public class FP extends GenericTab {

	private BilanItem financActEtat;
	private BilanItem financActTiers;
	private BilanItem fondProp;
	private BilanItem ecartReeval;

	private BilanItem reserve;
	private BilanItem reportNouv;
	private BilanItem resultExec;
	private BilanItem provReg;

	private BilanItem totalFondProp;

	public FP() {
		super();

		this.financActEtat = new BilanItem(DataRefEnum.cf_ref_121,DataRefEnum.cf_ref_122);

		this.financActTiers = new BilanItem(DataRefEnum.cf_ref_123,DataRefEnum.cf_ref_124);
		this.fondProp = new BilanItem(DataRefEnum.cf_ref_125,DataRefEnum.cf_ref_126);
		this.ecartReeval = new BilanItem(DataRefEnum.cf_ref_127,DataRefEnum.cf_ref_128);
		this.reserve = new BilanItem(DataRefEnum.cf_ref_129,DataRefEnum.cf_ref_130);
		this.reportNouv = new BilanItem(DataRefEnum.cf_ref_131,DataRefEnum.cf_ref_132);
		this.resultExec = new BilanItem(DataRefEnum.cf_ref_133,DataRefEnum.cf_ref_134);

		this.provReg = new BilanItem(DataRefEnum.cf_ref_135,DataRefEnum.cf_ref_136);
		this.totalFondProp = new BilanItem(DataRefEnum.cf_ref_137,DataRefEnum.cf_ref_138);
	}

	@Override
	public List<DataItem> getItems() {
		List<DataItem> items = new ArrayList<DataItem>();

		items.addAll(getFinancActEtat().getItems());
		items.addAll(getFinancActTiers().getItems());
		items.addAll(getFondProp().getItems());
		items.addAll(getEcartReeval().getItems());
		items.addAll(getReserve().getItems());
		items.addAll(getReportNouv().getItems());
		items.addAll(getResultExec().getItems());
		items.addAll(getProvReg().getItems());
		items.addAll(getTotalFondProp().getItems());

		return items;
	}
	public List<DataItem> getItemsAnt() {
		List<DataItem> items = new ArrayList<DataItem>();

		items.add(getFinancActEtat().getNetAnt());
		items.add(getFinancActTiers().getNetAnt());
		items.add(getFondProp().getNetAnt());
		items.add(getEcartReeval().getNetAnt());
		items.add(getReserve().getNetAnt());
		items.add(getReportNouv().getNetAnt());
		items.add(getResultExec().getNetAnt());
		items.add(getProvReg().getNetAnt());
		items.add(getTotalFondProp().getNetAnt());

		return items;
	}

	@Override
	public void setDefaults() {
		getTotalFondProp().getNetN()
				.setMontantDouble(getFinancActEtat().getNetN().getMontantDouble()
						+ getFinancActTiers().getNetN().getMontantDouble()
						+ getFondProp().getNetN().getMontantDouble() + getEcartReeval().getNetN().getMontantDouble()
						+ getReserve().getNetN().getMontantDouble() + getReportNouv().getNetN().getMontantDouble()
						+ getResultExec().getNetN().getMontantDouble() + getProvReg().getNetN().getMontantDouble()

		);

		getTotalFondProp().getNetAnt().setMontantDouble(getFinancActEtat().getNetAnt().getMontantDouble()
				+ getFinancActTiers().getNetAnt().getMontantDouble()
				+ getFondProp().getNetAnt().getMontantDouble() + getEcartReeval().getNetAnt().getMontantDouble()
				+ getReserve().getNetAnt().getMontantDouble() + getReportNouv().getNetAnt().getMontantDouble()
				+ getResultExec().getNetAnt().getMontantDouble() + getProvReg().getNetAnt().getMontantDouble()

		);
		
		

	}

	public BilanItem getFinancActEtat() {
		return financActEtat;
	}

	public void setFinancActEtat(BilanItem financActEtat) {
		this.financActEtat = financActEtat;
	}

	public BilanItem getFinancActTiers() {
		return financActTiers;
	}

	public void setFinancActTiers(BilanItem financActTiers) {
		this.financActTiers = financActTiers;
	}

	public BilanItem getFondProp() {
		return fondProp;
	}

	public void setFondProp(BilanItem fondProp) {
		this.fondProp = fondProp;
	}

	public BilanItem getEcartReeval() {
		return ecartReeval;
	}

	public void setEcartReeval(BilanItem ecartReeval) {
		this.ecartReeval = ecartReeval;
	}

	public BilanItem getReserve() {
		return reserve;
	}

	public void setReserve(BilanItem reserve) {
		this.reserve = reserve;
	}

	public BilanItem getReportNouv() {
		return reportNouv;
	}

	public void setReportNouv(BilanItem reportNouv) {
		this.reportNouv = reportNouv;
	}

	public BilanItem getResultExec() {
		return resultExec;
	}

	public void setResultExec(BilanItem resultExec) {
		this.resultExec = resultExec;
	}

	public BilanItem getProvReg() {
		return provReg;
	}

	public void setProvReg(BilanItem provReg) {
		this.provReg = provReg;
	}

	public BilanItem getTotalFondProp() {
		return totalFondProp;
	}

	public void setTotalFondProp(BilanItem totalFondProp) {
		this.totalFondProp = totalFondProp;
	}
	
	
	

}
