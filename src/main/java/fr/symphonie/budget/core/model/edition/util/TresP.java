package fr.symphonie.budget.core.model.edition.util;

import java.util.ArrayList;
import java.util.List;

public class TresP extends GenericTab {
	 private  BilanItem autreElement;
	 private  BilanItem totalTres;
	
	 private  BilanItem compteRegl;
	 private  BilanItem ecartConvert;
	
	 private  BilanItem totalGenPassif;

	 
	 
	public TresP() {
		super();
		
		this.autreElement = new BilanItem(DataRefEnum.cf_ref_167,DataRefEnum.cf_ref_168);
		this.totalTres = new BilanItem(DataRefEnum.cf_ref_169,DataRefEnum.cf_ref_170);
		this.compteRegl = new BilanItem(DataRefEnum.cf_ref_171,DataRefEnum.cf_ref_172);
		this.ecartConvert = new BilanItem(DataRefEnum.cf_ref_173,DataRefEnum.cf_ref_174);
		this.totalGenPassif = new BilanItem(DataRefEnum.cf_ref_175,DataRefEnum.cf_ref_176);
	}

	@Override
	public List<DataItem> getItems() {
		List<DataItem> items = new ArrayList<DataItem>();

		items.addAll(getAutreElement().getItems());
		items.addAll(getTotalTres().getItems());
		items.addAll(getCompteRegl().getItems());
		items.addAll(getEcartConvert().getItems());
		items.addAll(getTotalGenPassif().getItems());
		
		
		return items;
	}
	public List<DataItem> getItemsAnt() {
		List<DataItem> items = new ArrayList<DataItem>();

		items.add(getAutreElement().getNetAnt());
		items.add(getTotalTres().getNetAnt());
		items.add(getCompteRegl().getNetAnt());
		items.add(getEcartConvert().getNetAnt());
		items.add(getTotalGenPassif().getNetAnt());
		
		
		return items;
	}
	@Override
	public void setDefaults() {
		/******** private BilanItem totalTres; **********/

		getTotalTres().getNetN().setMontantDouble(getAutreElement().getNetN().getMontantDouble());
		getTotalTres().getNetAnt().setMontantDouble(getAutreElement().getNetAnt().getMontantDouble());
		/******** private BilanItem totalGenPassif; **********/

		getTotalGenPassif().getNetN().setMontantDouble(
				getCompteRegl().getNetN().getMontantDouble() + getEcartConvert().getNetN().getMontantDouble());

		getTotalGenPassif().getNetAnt().setMontantDouble(getCompteRegl().getNetAnt().getMontantDouble()
				+ getEcartConvert().getNetAnt().getMontantDouble());

	}
	public BilanItem getAutreElement() {
		return autreElement;
	}
	public void setAutreElement(BilanItem autreElement) {
		this.autreElement = autreElement;
	}
	public BilanItem getTotalTres() {
		return totalTres;
	}
	public void setTotalTres(BilanItem totalTres) {
		this.totalTres = totalTres;
	}
	public BilanItem getCompteRegl() {
		return compteRegl;
	}
	public void setCompteRegl(BilanItem compteRegl) {
		this.compteRegl = compteRegl;
	}
	public BilanItem getEcartConvert() {
		return ecartConvert;
	}
	public void setEcartConvert(BilanItem ecartConvert) {
		this.ecartConvert = ecartConvert;
	}
	public BilanItem getTotalGenPassif() {
		return totalGenPassif;
	}
	public void setTotalGenPassif(BilanItem totalGenPassif) {
		this.totalGenPassif = totalGenPassif;
	}

}
