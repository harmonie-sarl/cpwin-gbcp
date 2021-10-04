package fr.symphonie.budget.core.model.edition.util;

import java.util.ArrayList;
import java.util.List;

public class TresA extends GenericTab {

	private BilanItem valMobPlac;
	private BilanItem disponibilit;
	private BilanItem autre;

	private BilanItem totalTres;

	private BilanItem compteReg;
	private BilanItem ecartConvers;

	private BilanItem totalGenActif;

	public TresA() {
		super();

		this.valMobPlac = new BilanItem(DataRefEnum.cf_ref_093,DataRefEnum.cf_ref_094,DataRefEnum.cf_ref_095,DataRefEnum.cf_ref_096);
		this.disponibilit = new BilanItem(DataRefEnum.cf_ref_097,DataRefEnum.cf_ref_098,DataRefEnum.cf_ref_099,DataRefEnum.cf_ref_100);
		this.autre = new BilanItem(DataRefEnum.cf_ref_101,DataRefEnum.cf_ref_102,DataRefEnum.cf_ref_103,DataRefEnum.cf_ref_104);
		this.totalTres = new BilanItem(DataRefEnum.cf_ref_105,DataRefEnum.cf_ref_106,DataRefEnum.cf_ref_107,DataRefEnum.cf_ref_108);
		this.compteReg = new BilanItem(DataRefEnum.cf_ref_109,DataRefEnum.cf_ref_110,DataRefEnum.cf_ref_111,DataRefEnum.cf_ref_112);
		this.ecartConvers = new BilanItem(DataRefEnum.cf_ref_113,DataRefEnum.cf_ref_114,DataRefEnum.cf_ref_115,DataRefEnum.cf_ref_116);
		this.totalGenActif = new BilanItem(DataRefEnum.cf_ref_117,DataRefEnum.cf_ref_118,DataRefEnum.cf_ref_119,DataRefEnum.cf_ref_120);

	}

	@Override
	public List<DataItem> getItems() {
		List<DataItem> items=new ArrayList<DataItem>();	
		
		items.addAll(getValMobPlac().getItems());
		items.addAll(getDisponibilit().getItems());		
		items.addAll(getAutre().getItems());		
		items.addAll(getTotalTres().getItems());
		items.addAll(getCompteReg().getItems());
		items.addAll(getEcartConvers().getItems());
		items.addAll(getTotalGenActif().getItems());	
		
		return items;
	}
	public List<DataItem> getItemsAnt() {
		List<DataItem> items=new ArrayList<DataItem>();			
		items.add(getValMobPlac().getNetAnt());
		items.add(getDisponibilit().getNetAnt());		
		items.add(getAutre().getNetAnt());		
		items.add(getTotalTres().getNetAnt());
		items.add(getCompteReg().getNetAnt());
		items.add(getEcartConvers().getNetAnt());
		items.add(getTotalGenActif().getNetAnt());	
		
		return items;
	}

	@Override
	public void setDefaults() {
		/*******private BilanItem totalTres*********/
		getTotalTres().getBrutN().setMontantDouble(
				getValMobPlac().getBrutN().getMontantDouble()+
				getDisponibilit().getBrutN().getMontantDouble()+
				getAutre().getBrutN().getMontantDouble());
		getTotalTres().getAmortDeprN().setMontantDouble(
				getValMobPlac().getAmortDeprN().getMontantDouble()+
				getDisponibilit().getAmortDeprN().getMontantDouble()+
				getAutre().getAmortDeprN().getMontantDouble());
		getTotalTres().getNetN().setMontantDouble(
				getValMobPlac().getNetN().getMontantDouble()+
				getDisponibilit().getNetN().getMontantDouble()+
				getAutre().getNetN().getMontantDouble());
		getTotalTres().getNetAnt().setMontantDouble(
				getValMobPlac().getNetAnt().getMontantDouble()+
				getDisponibilit().getNetAnt().getMontantDouble()+
				getAutre().getNetAnt().getMontantDouble());
		
		/*******private BilanItem totalTres*********/
		getTotalGenActif().getBrutN().setMontantDouble(
				getCompteReg().getBrutN().getMontantDouble()+
				getEcartConvers().getBrutN().getMontantDouble());
		getTotalGenActif().getAmortDeprN().setMontantDouble(
				getCompteReg().getAmortDeprN().getMontantDouble()+
				getEcartConvers().getAmortDeprN().getMontantDouble());
		getTotalGenActif().getNetN().setMontantDouble(
				getCompteReg().getNetN().getMontantDouble()+
				getEcartConvers().getNetN().getMontantDouble());
		getTotalGenActif().getNetAnt().setMontantDouble(
				getCompteReg().getNetAnt().getMontantDouble()+
				getEcartConvers().getNetAnt().getMontantDouble());
		
				

	}

	public BilanItem getValMobPlac() {
		return valMobPlac;
	}

	public void setValMobPlac(BilanItem valMobPlac) {
		this.valMobPlac = valMobPlac;
	}

	public BilanItem getDisponibilit() {
		return disponibilit;
	}

	public void setDisponibilit(BilanItem disponibilit) {
		this.disponibilit = disponibilit;
	}

	public BilanItem getAutre() {
		return autre;
	}

	public void setAutre(BilanItem autre) {
		this.autre = autre;
	}

	public BilanItem getTotalTres() {
		return totalTres;
	}

	public void setTotalTres(BilanItem totalTres) {
		this.totalTres = totalTres;
	}

	public BilanItem getCompteReg() {
		return compteReg;
	}

	public void setCompteReg(BilanItem compteReg) {
		this.compteReg = compteReg;
	}

	public BilanItem getEcartConvers() {
		return ecartConvers;
	}

	public void setEcartConvers(BilanItem ecartConvers) {
		this.ecartConvers = ecartConvers;
	}

	public BilanItem getTotalGenActif() {
		return totalGenActif;
	}

	public void setTotalGenActif(BilanItem totalGenActif) {
		this.totalGenActif = totalGenActif;
	}
	
	

}
