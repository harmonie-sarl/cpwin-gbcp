package fr.symphonie.budget.core.model.edition.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fr.symphonie.budget.core.model.DepRecEnum;
import fr.symphonie.budget.core.model.edition.Edition;
import fr.symphonie.util.model.SimpleEntity;

public class TDD extends GenericTab {
	// private static final Logger logger = LoggerFactory.getLogger(TDD.class);
	private List<DepByDest> depByDestList;
	private DepByDest totale;
	private DataItem soldeBudgExcedent;
	
	private List<DataItem> items=null;
	private DataItem[] staticItems = null;
	
	public TDD(Edition e) {
		super(e);
		this.depByDestList = new ArrayList<DepByDest>();
		this.totale = new DepByDest(new SimpleEntity(TOT_PREFIX,""));
		this.soldeBudgExcedent = new DataItem(DataRefEnum.ref_11);
		initalize();
	}
	
	private void initalize() {
		this.staticItems = new DataItem[] {
				totale.getPersonnel().getDataAE(),totale.getFonctionnement().getDataAE(),
				totale.getIntervention().getDataAE(),totale.getInvestissement().getDataAE(),
				totale.getPersonnel().getDataCP(),totale.getFonctionnement().getDataCP(),
				totale.getIntervention().getDataCP(),totale.getInvestissement().getDataCP(),
				totale.getTotale().getDataAE(),totale.getTotale().getDataCP(),
				soldeBudgExcedent
		};
		
		
	}

	public List<DepByDest> getDepByDestList() {
		return depByDestList;
	}
	public void setDepByDestList(List<DepByDest> depByDestList) {
		this.depByDestList = depByDestList;
	}
	public DepByDest getTotale() {
		return totale;
	}
	public void setTotale(DepByDest totale) {
		this.totale = totale;
	}
	public DataItem getSoldeBudgExcedent() {
		return soldeBudgExcedent;
	}
	public void setSoldeBudgExcedent(DataItem soldeBudgExcedent) {
		this.soldeBudgExcedent = soldeBudgExcedent;
	}
@Override
	public List<DataItem> getItems(){
		List<DataItem> result=new ArrayList<DataItem>();
		if(items!=null) return items;
		
		result.addAll(Arrays.asList(staticItems));
		for(DepByDest item:getDepByDestList())	{
			result.add(item.getPersonnel().getDataAE());
			result.add(item.getFonctionnement().getDataAE());
			result.add(item.getIntervention().getDataAE());
			result.add(item.getInvestissement().getDataAE());
			result.add(item.getPersonnel().getDataCP());result.add(item.getFonctionnement().getDataCP());
			result.add(item.getIntervention().getDataCP());result.add(item.getInvestissement().getDataCP());
			result.add(item.getTotale().getDataAE());result.add(item.getTotale().getDataCP());
		}
		setItems(result);
		return result;
	}

public void setItems(List<DataItem> items) {
	this.items = items;
}

@Override
public void preparData(List<DataItem> data) {
	if(data==null) return;
	DepByDest dep=null;
	for(DataItem item:data){
	dep=null;
	if((item.getPrefixRef()==null)||(item.getPrefixRef().trim().isEmpty())) continue;
	if(TOT_PREFIX.equals(item.getPrefixRef()))continue;
	
	SimpleEntity s=new SimpleEntity(item.getPrefixRef(), "");
	int index=getDepByDestList().indexOf(new DepByDest(s));
	if(index!=-1)	dep=getDepByDestList().get(index);
	if(dep==null){
		dep=new DepByDest(s);
		getDepByDestList().add(dep);
	}
	
	}
	setItems(null);
}
public void setDefaults() {
	
	for(DepByDest item:getDepByDestList()){
		item.getTotale().getDataAE().setMontantDouble(item.getTotaleAE());
		item.getTotale().getDataCP().setMontantDouble(item.getTotaleCP());
		getTotale().getPersonnel().getDataAE().add(item.getPersonnel().getMontanAE());
		getTotale().getPersonnel().getDataCP().add(item.getPersonnel().getMontanCP());
		getTotale().getFonctionnement().getDataAE().add(item.getFonctionnement().getMontanAE());
		getTotale().getIntervention().getDataAE().add(item.getIntervention().getMontanAE());
		getTotale().getInvestissement().getDataAE().add(item.getInvestissement().getMontanAE());
		
		getTotale().getFonctionnement().getDataCP().add(item.getFonctionnement().getMontanCP());
		getTotale().getIntervention().getDataCP().add(item.getIntervention().getMontanCP());
		getTotale().getInvestissement().getDataCP().add(item.getInvestissement().getMontanCP());
		getTotale().getTotale().getDataAE().add(item.getTotale().getMontanAE());
		getTotale().getTotale().getDataCP().add(item.getTotale().getMontanCP());
	}
	

	}
public Map<String,Double> getMapTotalAE(){
	Map<String,Double> montants=new HashMap<String, Double>();
	for(NatGrpEnum natgrp:NatGrpEnum.getNatGrp(DepRecEnum.Depense)){
		montants.put(natgrp.getCode(), getTotale().getMontantAE(natgrp.getCode()).doubleValue());
	}	
	return montants;
}

public Map<String,Double> getMapTotalCP(){
	Map<String,Double> montants=new HashMap<String, Double>();
	for(NatGrpEnum natgrp:NatGrpEnum.getNatGrp(DepRecEnum.Depense)){
		montants.put(natgrp.getCode(), getTotale().getMontantCP(natgrp.getCode()).doubleValue());
	}	
	return montants;
}
}
