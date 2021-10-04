package fr.symphonie.budget.core.model.edition.util;

import java.util.ArrayList;
import java.util.List;

public class DF extends GenericTab{
	
	
	private BilanItem empOblig;
	private BilanItem empSouscrit;
	private BilanItem detFinanc;
	
	private BilanItem totalDetFinanc;
	
	
	
	public DF() {
		super();
		
		
		this.empOblig = new BilanItem(DataRefEnum.cf_ref_145,DataRefEnum.cf_ref_146);

		this.empSouscrit = new BilanItem(DataRefEnum.cf_ref_147,DataRefEnum.cf_ref_148);
		this.detFinanc = new BilanItem(DataRefEnum.cf_ref_149,DataRefEnum.cf_ref_150);
		this.totalDetFinanc = new BilanItem(DataRefEnum.cf_ref_151,DataRefEnum.cf_ref_152);
	
	}



	@Override
	public List<DataItem> getItems() {
		List<DataItem> items = new ArrayList<DataItem>();

		items.addAll(getEmpOblig().getItems());
		items.addAll(getEmpSouscrit().getItems());
		items.addAll(getDetFinanc().getItems());
		items.addAll(getTotalDetFinanc().getItems());

		return items;
	}

	public List<DataItem> getItemsAnt() {
		List<DataItem> items = new ArrayList<DataItem>();

		items.add(getEmpOblig().getNetAnt());
		items.add(getEmpSouscrit().getNetAnt());
		items.add(getDetFinanc().getNetAnt());
		items.add(getTotalDetFinanc().getNetAnt());

		return items;
	}

	@Override
	public void setDefaults() {
		getTotalDetFinanc().getNetN().setMontantDouble(getEmpOblig().getNetN().getMontantDouble()
				+ getEmpSouscrit().getNetN().getMontantDouble() + getDetFinanc().getNetN().getMontantDouble());
		getTotalDetFinanc().getNetAnt()
				.setMontantDouble(getEmpOblig().getNetAnt().getMontantDouble()
						+ getEmpSouscrit().getNetAnt().getMontantDouble()
						+ getDetFinanc().getNetAnt().getMontantDouble());

	}



	public BilanItem getEmpOblig() {
		return empOblig;
	}



	public void setEmpOblig(BilanItem empOblig) {
		this.empOblig = empOblig;
	}



	public BilanItem getEmpSouscrit() {
		return empSouscrit;
	}



	public void setEmpSouscrit(BilanItem empSouscrit) {
		this.empSouscrit = empSouscrit;
	}



	public BilanItem getDetFinanc() {
		return detFinanc;
	}



	public void setDetFinanc(BilanItem detFinanc) {
		this.detFinanc = detFinanc;
	}



	public BilanItem getTotalDetFinanc() {
		return totalDetFinanc;
	}



	public void setTotalDetFinanc(BilanItem totalDetFinanc) {
		this.totalDetFinanc = totalDetFinanc;
	}
	

	

}
