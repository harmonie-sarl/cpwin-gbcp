package fr.symphonie.budget.core.model.edition.util;

import java.util.ArrayList;
import java.util.List;

public class BilanItem {
	private DataItem brutN;
	private DataItem amortDeprN;
	private DataItem netN;
	private DataItem netAnt;
	
	public BilanItem(DataRefEnum ref_brut,DataRefEnum ref_amort,DataRefEnum ref_netn,DataRefEnum ref_netAnt) {
		super();
		brutN=new DataItem(ref_brut );
		amortDeprN=new DataItem(ref_amort);
		netN=new DataItem(ref_netn );
		netAnt=new DataItem(ref_netAnt );
	}
	public BilanItem(DataRefEnum ref_netn,DataRefEnum ref_netAnt) {
		super();
		//brutN=new DataItem(ref_brut );
		//amortDeprN=new DataItem(ref_amort);
		netN=new DataItem(ref_netn );
		netAnt=new DataItem(ref_netAnt );
	}
	public BilanItem(DataRefEnum ref_brut) {
		super();
		brutN=new DataItem(ref_brut );
		
	}
	public DataItem getBrutN() {
		return brutN;
	}
	public void setBrutN(DataItem brutN) {
		this.brutN = brutN;
	}
	public DataItem getAmortDeprN() {
		return amortDeprN;
	}
	public void setAmortDeprN(DataItem amortDeprN) {
		this.amortDeprN = amortDeprN;
	}
	public DataItem getNetN() {
		if((getBrutN()!=null)&&(getAmortDeprN()!=null)&&(netN!=null))
			netN.setMontantDouble(getBrutN().getMontantDouble()-getAmortDeprN().getMontantDouble());
		return netN;
	}
	public void setNetN(DataItem netN) {
		this.netN = netN;
	}
	public DataItem getNetAnt() {
		return netAnt;
	}
	public void setNetAnt(DataItem netAnt) {
		this.netAnt = netAnt;
	}
	
	public List<DataItem> getItems() {
		List<DataItem> items=new ArrayList<DataItem>();
		if(getBrutN()!=null)items.add(getBrutN());
		if(getAmortDeprN()!=null)items.add(getAmortDeprN());
		if(getNetN()!=null) items.add(getNetN());
		//if(getNetAnt()!=null)items.add(getNetAnt());
		
		return items;
	}
	 
}
