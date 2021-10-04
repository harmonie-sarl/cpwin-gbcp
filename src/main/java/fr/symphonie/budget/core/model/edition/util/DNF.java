package fr.symphonie.budget.core.model.edition.util;

import java.util.ArrayList;
import java.util.List;

public class DNF extends GenericTab{
	
	
	private BilanItem detFournisCmptRatt;
	private BilanItem detFiscSocial;
	private BilanItem avanceAcmptRecu;
	private BilanItem detCorrespOp;
	private BilanItem autreDetFinanc;	
	private BilanItem produiConstAvanc;	
	private BilanItem totalDetFinanc;
	
	
	public DNF() {
		super();
		this.detFournisCmptRatt = new BilanItem(DataRefEnum.cf_ref_153,DataRefEnum.cf_ref_154);
		this.detFiscSocial = new BilanItem(DataRefEnum.cf_ref_155,DataRefEnum.cf_ref_156);
		this.avanceAcmptRecu = new BilanItem(DataRefEnum.cf_ref_157,DataRefEnum.cf_ref_158);
		this.detCorrespOp = new BilanItem(DataRefEnum.cf_ref_159,DataRefEnum.cf_ref_160);
		this.autreDetFinanc = new BilanItem(DataRefEnum.cf_ref_161,DataRefEnum.cf_ref_162);
		this.produiConstAvanc = new BilanItem(DataRefEnum.cf_ref_163,DataRefEnum.cf_ref_164);
		this.totalDetFinanc = new BilanItem(DataRefEnum.cf_ref_165,DataRefEnum.cf_ref_166);		
	}


	@Override
	public List<DataItem> getItems() {
		List<DataItem> items = new ArrayList<DataItem>();

		items.addAll(getDetFournisCmptRatt().getItems());
		items.addAll(getDetFiscSocial().getItems());
		items.addAll(getAvanceAcmptRecu().getItems());
		items.addAll(getDetCorrespOp().getItems());
		items.addAll(getAutreDetFinanc().getItems());
		items.addAll(getProduiConstAvanc().getItems());
		items.addAll(getTotalDetFinanc().getItems());

		return items;
	}
	public List<DataItem> getItemsAnt() {
		List<DataItem> items = new ArrayList<DataItem>();

		items.add(getDetFournisCmptRatt().getNetAnt());
		items.add(getDetFiscSocial().getNetAnt());
		items.add(getAvanceAcmptRecu().getNetAnt());
		items.add(getDetCorrespOp().getNetAnt());
		items.add(getAutreDetFinanc().getNetAnt());
		items.add(getProduiConstAvanc().getNetAnt());
		items.add(getTotalDetFinanc().getNetAnt());

		return items;
	}


	@Override
	public void setDefaults() {
	getTotalDetFinanc().getNetN().setMontantDouble(
			getDetFournisCmptRatt().getNetN().getMontantDouble()+
			getDetFiscSocial().getNetN().getMontantDouble()+
			getAvanceAcmptRecu().getNetN().getMontantDouble()+
			getDetCorrespOp().getNetN().getMontantDouble()+
			getAutreDetFinanc().getNetN().getMontantDouble()+	
			getProduiConstAvanc().getNetN().getMontantDouble()+
			getTotalDetFinanc().getNetN().getMontantDouble());
	
	getTotalDetFinanc().getNetAnt().setMontantDouble(
			getDetFournisCmptRatt().getNetAnt().getMontantDouble()+
			getDetFiscSocial().getNetAnt().getMontantDouble()+
			getAvanceAcmptRecu().getNetAnt().getMontantDouble()+
			getDetCorrespOp().getNetAnt().getMontantDouble()+
			getAutreDetFinanc().getNetAnt().getMontantDouble()+	
			getProduiConstAvanc().getNetAnt().getMontantDouble()+
			getTotalDetFinanc().getNetAnt().getMontantDouble());
		
	}


	public BilanItem getDetFournisCmptRatt() {
		return detFournisCmptRatt;
	}


	public void setDetFournisCmptRatt(BilanItem detFournisCmptRatt) {
		this.detFournisCmptRatt = detFournisCmptRatt;
	}


	public BilanItem getDetFiscSocial() {
		return detFiscSocial;
	}


	public void setDetFiscSocial(BilanItem detFiscSocial) {
		this.detFiscSocial = detFiscSocial;
	}


	public BilanItem getAvanceAcmptRecu() {
		return avanceAcmptRecu;
	}


	public void setAvanceAcmptRecu(BilanItem avanceAcmptRecu) {
		this.avanceAcmptRecu = avanceAcmptRecu;
	}


	public BilanItem getDetCorrespOp() {
		return detCorrespOp;
	}


	public void setDetCorrespOp(BilanItem detCorrespOp) {
		this.detCorrespOp = detCorrespOp;
	}


	public BilanItem getAutreDetFinanc() {
		return autreDetFinanc;
	}


	public void setAutreDetFinanc(BilanItem autreDetFinanc) {
		this.autreDetFinanc = autreDetFinanc;
	}


	public BilanItem getProduiConstAvanc() {
		return produiConstAvanc;
	}


	public void setProduiConstAvanc(BilanItem produiConstAvanc) {
		this.produiConstAvanc = produiConstAvanc;
	}


	public BilanItem getTotalDetFinanc() {
		return totalDetFinanc;
	}


	public void setTotalDetFinanc(BilanItem totalDetFinanc) {
		this.totalDetFinanc = totalDetFinanc;
	}
	
	
	


}
