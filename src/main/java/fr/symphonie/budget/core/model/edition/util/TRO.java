package fr.symphonie.budget.core.model.edition.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fr.symphonie.budget.core.model.DepRecEnum;
import fr.symphonie.budget.core.model.edition.Edition;
import fr.symphonie.util.model.SimpleEntity;

public class TRO extends GenericTab {
private List<RecByOrig> recByOrigList;
private RecByOrig totale;
private DataItem soldeBudgDeficit;

private List<DataItem> items=null;
DataItem[] staticItems = null;


public TRO(Edition e) {
	super(e);
	this.recByOrigList = new ArrayList<RecByOrig>();
	this.totale = new RecByOrig(new SimpleEntity(TOT_PREFIX,""));
	this.soldeBudgDeficit = new DataItem(DataRefEnum.ref_11);
	initalize();
}

private void initalize() {
	staticItems = new DataItem[] {
			totale.getDataR11(),totale.getDataR12(),totale.getDataR13(),totale.getDataR14(),totale.getDataR15(),
			totale.getDataR22(),totale.getDataR24(),totale.getDataR25(),totale.getTotale(),
			soldeBudgDeficit
	};
	
	
}
public List<RecByOrig> getRecByOrigList() {
	return recByOrigList;
}
public void setRecByOrigList(List<RecByOrig> recByOrigList) {
	this.recByOrigList = recByOrigList;
}
public RecByOrig getTotale() {
	return totale;
}
public void setTotale(RecByOrig totale) {
	this.totale = totale;
}
public DataItem getSoldeBudgDeficit() {
	return soldeBudgDeficit;
}
public void setSoldeBudgDeficit(DataItem soldeBudgDeficit) {
	this.soldeBudgDeficit = soldeBudgDeficit;
}
@Override
public List<DataItem> getItems(){
	List<DataItem> result=new ArrayList<DataItem>();
	if(items!=null) return items;
	
	result.addAll(Arrays.asList(staticItems));
	for(RecByOrig item:getRecByOrigList())	{
		result.add(item.getDataR11());result.add(item.getDataR12());result.add(item.getDataR13());
		result.add(item.getDataR14());result.add(item.getDataR15());
		result.add(item.getDataR22());result.add(item.getDataR24());result.add(item.getDataR25());
		result.add(item.getTotale());
		
	}
	setItems(result);
	return result;
}

@Override
public void preparData(List<DataItem> data) {
	if(data==null) return;
	RecByOrig dep=null;
	for(DataItem item:data){
	dep=null;
	if((item.getPrefixRef()==null)||(item.getPrefixRef().trim().isEmpty())) continue;
	if(TOT_PREFIX.equals(item.getPrefixRef()))continue;
	
	SimpleEntity s=new SimpleEntity(item.getPrefixRef(), "");
	int index=getRecByOrigList().indexOf(new RecByOrig(s));
	if(index!=-1)	dep=getRecByOrigList().get(index);
	if(dep==null){
		dep=new RecByOrig(s);
		getRecByOrigList().add(dep);
	}
	
	}
	setItems(null);
}
public void setDefaults() {
	
	for(RecByOrig item:getRecByOrigList()){
		item.getTotale().setMontantDouble(item.getSomme());
		
		getTotale().getDataR11().add(item.getDataR11().getMontantDouble());
		getTotale().getDataR12().add(item.getDataR12().getMontantDouble());
		getTotale().getDataR13().add(item.getDataR13().getMontantDouble());
		getTotale().getDataR14().add(item.getDataR14().getMontantDouble());
		getTotale().getDataR15().add(item.getDataR15().getMontantDouble());
		
		getTotale().getDataR22().add(item.getDataR22().getMontantDouble());
		getTotale().getDataR24().add(item.getDataR24().getMontantDouble());
		getTotale().getDataR25().add(item.getDataR25().getMontantDouble());
		getTotale().getTotale().add(item.getTotale().getMontantDouble());
	}

	}
public void setItems(List<DataItem> items) {
	this.items = items;
}

public Map<String,Double> getMapTotal(){
	Map<String,Double> montants=new HashMap<String, Double>();
	for(NatGrpEnum natgrp:NatGrpEnum.getNatGrp(DepRecEnum.Recette)){
		montants.put(natgrp.getCode(), getTotale().getMontant(natgrp.getCode()).doubleValue());
	}	
	return montants;
}




}
