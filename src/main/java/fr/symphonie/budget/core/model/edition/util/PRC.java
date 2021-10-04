package fr.symphonie.budget.core.model.edition.util;

import java.util.ArrayList;
import java.util.List;

public class PRC extends GenericTab {

	private BilanItem provRisq;
	private BilanItem provCharg;
	private BilanItem totalProv;

	public PRC() {
		super();

		this.provRisq = new BilanItem(DataRefEnum.cf_ref_139,DataRefEnum.cf_ref_140);
		this.provCharg = new BilanItem(DataRefEnum.cf_ref_141,DataRefEnum.cf_ref_142);
		this.totalProv = new BilanItem(DataRefEnum.cf_ref_143,DataRefEnum.cf_ref_144);

	}

	@Override
	public List<DataItem> getItems() {
		List<DataItem> items = new ArrayList<DataItem>();

		items.addAll(getProvRisq().getItems());
		items.addAll(getProvCharg().getItems());
		items.addAll(getTotalProv().getItems());

		return items;
	}
	public List<DataItem> getItemsAnt() {
		List<DataItem> items = new ArrayList<DataItem>();

		items.add(getProvRisq().getNetAnt());
		items.add(getProvCharg().getNetAnt());
		items.add(getTotalProv().getNetAnt());

		return items;
	}
	@Override
	public void setDefaults() {
		getTotalProv().getNetN().setMontantDouble(
				getProvRisq().getNetN().getMontantDouble() + getProvCharg().getNetN().getMontantDouble());
		getTotalProv().getNetAnt().setMontantDouble(
				getProvRisq().getNetAnt().getMontantDouble() + getProvCharg().getNetAnt().getMontantDouble());

	}

	public BilanItem getProvRisq() {
		return provRisq;
	}

	public void setProvRisq(BilanItem provRisq) {
		this.provRisq = provRisq;
	}

	public BilanItem getProvCharg() {
		return provCharg;
	}

	public void setProvCharg(BilanItem provCharg) {
		this.provCharg = provCharg;
	}

	public BilanItem getTotalProv() {
		return totalProv;
	}

	public void setTotalProv(BilanItem totalProv) {
		this.totalProv = totalProv;
	}

	
	
	
}
