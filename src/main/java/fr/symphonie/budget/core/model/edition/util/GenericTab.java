package fr.symphonie.budget.core.model.edition.util;

import java.util.List;

import fr.symphonie.budget.core.model.edition.Edition;

public abstract class GenericTab  {
	//private static final Logger logger = LoggerFactory.getLogger(GenericTab.class);
	
	public static final String TOT_PREFIX="TOT";
	private Edition parent;
	
	public GenericTab(Edition parent) {
		super();
		this.parent = parent;
	}
	public GenericTab() {
		super();
	}
	public void setData(List<DataItem> data){
		if(data==null) return;
		preparData(data);
		for(DataItem item:data){
		//	logger.debug("setData: uid="+item.getUid()+", montant="+item.getMontantDouble());
			setDataItem(item);
		}
	}
	public void preparData(List<DataItem> data) {
		
		
	}
//	@Override
	public DataItem getDataItem(DataRefEnum ref,String prefix) {
//		logger.debug("getDataItem: ref="+ref+", prefix="+prefix);
		DataItem result= null;
		DataItem o=new DataItem(ref,prefix);
		int index=getItems().indexOf(o);
		if(index!=-1) result= getItems().get(index);
		//logger.debug("getDataItem: result="+result);
		return result;
	}
	
//	@Override
	public void setDataItem(DataItem item) {
		//logger.debug("setDataItem: ref="+ref+", value="+value);
		DataItem targetItem=getDataItem(item.getRefData(),item.getPrefixRef());
		if(targetItem!=null) targetItem.setMontant(item.getMontant());
		
	}
	public abstract List<DataItem> getItems();
	public Edition getParent() {
		return parent;
	}
	public void setParent(Edition parent) {
		this.parent = parent;
	} 
	public abstract void setDefaults();
}
