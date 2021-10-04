package fr.symphonie.budget.core.model.edition.util;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.symphonie.common.util.BudgetHelper;

public class CRItem {
	private static final Logger logger = LoggerFactory.getLogger(CRItem.class);
	private String key;
	private String designation;
	private DataItem n;
	private DataItem ant;
	private String style;
	
	public CRItem(String key,DataRefEnum[] ref) {
		super();
		this.key = key;
		logger.debug("CRItem : key="+key+", ref="+ref);
		if((ref==null)||(ref.length==0)){
			n=new DataItem();
			ant=new DataItem();
		}
		else{
		n=new DataItem(ref[0]);
		ant=new DataItem(ref[1]);
		}
	}
	public CRItem(String key,DataRefEnum[] ref,String style) {
		this(key,ref);
		this.style=style;
	}
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public DataItem getN() {
		return n;
	}
	public void setN(DataItem n) {
		this.n = n;
	}
	public DataItem getAnt() {
		return ant;
	}
	public void setAnt(DataItem ant) {
		this.ant = ant;
	}
	public String getStyle() {
		if(style==null)return "";
		return style;
	}
	public void setStyle(String style) {
		this.style = style;
	}
	public String getDesignation() {
		if(designation==null)designation=BudgetHelper.getEditionMsg(key);
		return designation;
	}
	
	public List<DataItem> getItems() {
		List<DataItem> items=new ArrayList<DataItem>();
		if(getN().getRefData()!=null)
		items.add(getN());
		return items;
	}
	public List<DataItem> getItemsAnt() {
		List<DataItem> items=new ArrayList<DataItem>();
		if(getAnt().getRefData()!=null)
		items.add(getAnt());
		return items;
	}

}
