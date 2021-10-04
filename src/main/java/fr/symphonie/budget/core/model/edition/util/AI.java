package fr.symphonie.budget.core.model.edition.util;

import java.util.ArrayList;
import java.util.List;

public class AI extends GenericTab{
	
	private BilanItem immIncorp;
	private BilanItem terrains;
	private BilanItem constrcut;
	private BilanItem installTech;
	private BilanItem collection;
	private BilanItem biensHist;
	private BilanItem autreImmobCorp;
	private BilanItem immobMisConc;
	private BilanItem immobCorpEnC;
	private BilanItem avanceAcmptCmd;
	private BilanItem immobGreve;
	private BilanItem immobCorp;

	private BilanItem immobFinanc;
	private BilanItem totalActif;

	
	
	
	
  
  
	public AI() {
		super();
		this.immIncorp= new BilanItem(DataRefEnum.cf_ref_001,DataRefEnum.cf_ref_002,DataRefEnum.cf_ref_003,DataRefEnum.cf_ref_004);
		this.terrains= new BilanItem(DataRefEnum.cf_ref_005,DataRefEnum.cf_ref_006,DataRefEnum.cf_ref_007,DataRefEnum.cf_ref_008);
		this.constrcut= new BilanItem(DataRefEnum.cf_ref_009,DataRefEnum.cf_ref_010,DataRefEnum.cf_ref_011,DataRefEnum.cf_ref_012);
		this.installTech= new BilanItem(DataRefEnum.cf_ref_013,DataRefEnum.cf_ref_014,DataRefEnum.cf_ref_015,DataRefEnum.cf_ref_016);
		this.collection= new BilanItem(DataRefEnum.cf_ref_017,DataRefEnum.cf_ref_018,DataRefEnum.cf_ref_019,DataRefEnum.cf_ref_020);
		this.biensHist= new BilanItem(DataRefEnum.cf_ref_021,DataRefEnum.cf_ref_022,DataRefEnum.cf_ref_023,DataRefEnum.cf_ref_024);
		this.autreImmobCorp= new BilanItem(DataRefEnum.cf_ref_025,DataRefEnum.cf_ref_026,DataRefEnum.cf_ref_027,DataRefEnum.cf_ref_028);
		this.immobMisConc= new BilanItem(DataRefEnum.cf_ref_029,DataRefEnum.cf_ref_030,DataRefEnum.cf_ref_031,DataRefEnum.cf_ref_032);
		this.immobCorpEnC= new BilanItem(DataRefEnum.cf_ref_033,DataRefEnum.cf_ref_034,DataRefEnum.cf_ref_035,DataRefEnum.cf_ref_036);
		this.avanceAcmptCmd= new BilanItem(DataRefEnum.cf_ref_037,DataRefEnum.cf_ref_038,DataRefEnum.cf_ref_039,DataRefEnum.cf_ref_040);
		this.immobGreve= new BilanItem(DataRefEnum.cf_ref_041,DataRefEnum.cf_ref_042,DataRefEnum.cf_ref_043,DataRefEnum.cf_ref_044);
		this.immobCorp= new BilanItem(DataRefEnum.cf_ref_045,DataRefEnum.cf_ref_046,DataRefEnum.cf_ref_047,DataRefEnum.cf_ref_048);
		this.immobFinanc= new BilanItem(DataRefEnum.cf_ref_049,DataRefEnum.cf_ref_050,DataRefEnum.cf_ref_051,DataRefEnum.cf_ref_052);
		this.totalActif= new BilanItem(DataRefEnum.cf_ref_053,DataRefEnum.cf_ref_054,DataRefEnum.cf_ref_055,DataRefEnum.cf_ref_056);
		}


	@Override
	public List<DataItem> getItems() {
		List<DataItem> items=new ArrayList<DataItem>();
		items.addAll(getImmIncorp().getItems());
		items.addAll(getTerrains().getItems());
		items.addAll(getConstrcut().getItems());
		items.addAll(getInstallTech().getItems());
		items.addAll(getCollection().getItems());
		items.addAll(getBiensHist().getItems());
		items.addAll(getAutreImmobCorp().getItems());
		items.addAll(getImmobMisConc().getItems());
		items.addAll(getImmobCorpEnC().getItems());
		items.addAll(getAvanceAcmptCmd().getItems());
		items.addAll(getImmobGreve().getItems());
		items.addAll(getImmobCorp().getItems());
		items.addAll(getImmobFinanc().getItems());
		items.addAll(getTotalActif().getItems());		
		return items;
	}


	public List<DataItem> getItemsAnt() {
		List<DataItem> items=new ArrayList<DataItem>();
		items.add(getImmIncorp().getNetAnt());
		items.add(getTerrains().getNetAnt());
		items.add(getConstrcut().getNetAnt());
		items.add(getInstallTech().getNetAnt());
		items.add(getCollection().getNetAnt());
		items.add(getBiensHist().getNetAnt());
		items.add(getAutreImmobCorp().getNetAnt());
		items.add(getImmobMisConc().getNetAnt());
		items.add(getImmobCorpEnC().getNetAnt());
		items.add(getAvanceAcmptCmd().getNetAnt());
		items.add(getImmobGreve().getNetAnt());
		items.add(getImmobCorp().getNetAnt());
		items.add(getImmobFinanc().getNetAnt());
		items.add(getTotalActif().getNetAnt());		
		return items;
	}





public BilanItem getImmIncorp() {
		return immIncorp;
	}


	public void setImmIncorp(BilanItem immIncorp) {
		this.immIncorp = immIncorp;
	}


	public BilanItem getTerrains() {
		return terrains;
	}


	public void setTerrains(BilanItem terrains) {
		this.terrains = terrains;
	}


	public BilanItem getConstrcut() {
		return constrcut;
	}


	public void setConstrcut(BilanItem constrcut) {
		this.constrcut = constrcut;
	}


	public BilanItem getInstallTech() {
		return installTech;
	}


	public void setInstallTech(BilanItem installTech) {
		this.installTech = installTech;
	}


	public BilanItem getCollection() {
		return collection;
	}


	public void setCollection(BilanItem collection) {
		this.collection = collection;
	}


	public BilanItem getBiensHist() {
		return biensHist;
	}


	public void setBiensHist(BilanItem biensHist) {
		this.biensHist = biensHist;
	}


	public BilanItem getAutreImmobCorp() {
		return autreImmobCorp;
	}


	public void setAutreImmobCorp(BilanItem autreImmobCorp) {
		this.autreImmobCorp = autreImmobCorp;
	}


	public BilanItem getImmobMisConc() {
		return immobMisConc;
	}


	public void setImmobMisConc(BilanItem immobMisConc) {
		this.immobMisConc = immobMisConc;
	}


	public BilanItem getImmobCorpEnC() {
		return immobCorpEnC;
	}


	public void setImmobCorpEnC(BilanItem immobCorpEnC) {
		this.immobCorpEnC = immobCorpEnC;
	}


	public BilanItem getAvanceAcmptCmd() {
		return avanceAcmptCmd;
	}


	public void setAvanceAcmptCmd(BilanItem avanceAcmptCmd) {
		this.avanceAcmptCmd = avanceAcmptCmd;
	}


	public BilanItem getImmobGreve() {
		return immobGreve;
	}


	public void setImmobGreve(BilanItem immobGreve) {
		this.immobGreve = immobGreve;
	}


	public BilanItem getImmobCorp() {
		return immobCorp;
	}


	public void setImmobCorp(BilanItem immobCorp) {
		this.immobCorp = immobCorp;
	}


	public BilanItem getImmobFinanc() {
		return immobFinanc;
	}


	public void setImmobFinanc(BilanItem immobFinanc) {
		this.immobFinanc = immobFinanc;
	}


	public BilanItem getTotalActif() {
		return totalActif;
	}


	public void setTotalActif(BilanItem totalActif) {
		this.totalActif = totalActif;
	}


	@Override
	public void setDefaults() {

		getTotalActif().getBrutN().setMontantDouble(getImmIncorp().getBrutN().getMontantDouble()
				+ getTerrains().getBrutN().getMontantDouble() + getConstrcut().getBrutN().getMontantDouble()
				+ getInstallTech().getBrutN().getMontantDouble() + getCollection().getBrutN().getMontantDouble()
				+ getBiensHist().getBrutN().getMontantDouble() + getAutreImmobCorp().getBrutN().getMontantDouble()
				+ getImmobMisConc().getBrutN().getMontantDouble() + getImmobCorpEnC().getBrutN().getMontantDouble()
				+ getAvanceAcmptCmd().getBrutN().getMontantDouble() + getImmobGreve().getBrutN().getMontantDouble()
				+ getImmobCorp().getBrutN().getMontantDouble() + getImmobFinanc().getBrutN().getMontantDouble());

		getTotalActif().getAmortDeprN().setMontantDouble(getImmIncorp().getAmortDeprN().getMontantDouble()
				+ getTerrains().getAmortDeprN().getMontantDouble() + getConstrcut().getAmortDeprN().getMontantDouble()
				+ getInstallTech().getAmortDeprN().getMontantDouble()
				+ getCollection().getAmortDeprN().getMontantDouble() + getBiensHist().getAmortDeprN().getMontantDouble()
				+ getAutreImmobCorp().getAmortDeprN().getMontantDouble()
				+ getImmobMisConc().getAmortDeprN().getMontantDouble()
				+ getImmobCorpEnC().getAmortDeprN().getMontantDouble()
				+ getAvanceAcmptCmd().getAmortDeprN().getMontantDouble()
				+ getImmobGreve().getAmortDeprN().getMontantDouble() + getImmobCorp().getAmortDeprN().getMontantDouble()
				+ getImmobFinanc().getAmortDeprN().getMontantDouble());

		getTotalActif().getNetN().setMontantDouble(getImmIncorp().getNetN().getMontantDouble()
				+ getTerrains().getNetN().getMontantDouble() + getConstrcut().getNetN().getMontantDouble()
				+ getInstallTech().getNetN().getMontantDouble() + getCollection().getNetN().getMontantDouble()
				+ getBiensHist().getNetN().getMontantDouble() + getAutreImmobCorp().getNetN().getMontantDouble()
				+ getImmobMisConc().getNetN().getMontantDouble() + getImmobCorpEnC().getNetN().getMontantDouble()
				+ getAvanceAcmptCmd().getNetN().getMontantDouble() + getImmobGreve().getNetN().getMontantDouble()
				+ getImmobCorp().getNetN().getMontantDouble() + getImmobFinanc().getNetN().getMontantDouble());

		getTotalActif().getNetAnt().setMontantDouble(getImmIncorp().getNetAnt().getMontantDouble()
				+ getTerrains().getNetAnt().getMontantDouble() + getConstrcut().getNetAnt().getMontantDouble()
				+ getInstallTech().getNetAnt().getMontantDouble() + getCollection().getNetAnt().getMontantDouble()
				+ getBiensHist().getNetAnt().getMontantDouble() + getAutreImmobCorp().getNetAnt().getMontantDouble()
				+ getImmobMisConc().getNetAnt().getMontantDouble() + getImmobCorpEnC().getNetAnt().getMontantDouble()
				+ getAvanceAcmptCmd().getNetAnt().getMontantDouble() + getImmobGreve().getNetAnt().getMontantDouble()
				+ getImmobCorp().getNetAnt().getMontantDouble() + getImmobFinanc().getNetAnt().getMontantDouble());

	}

	

	


}
