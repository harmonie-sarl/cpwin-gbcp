package fr.symphonie.budget.core.model.edition.util;

import java.math.BigDecimal;

import fr.symphonie.common.util.BudgetHelper;
import fr.symphonie.impor.AttributImport;
import fr.symphonie.impor.ConfigImport;
import fr.symphonie.impor.Importable;

public class DataItem implements Cloneable,Importable {
	//private static final Logger logger = LoggerFactory.getLogger(DataItem.class);
public static final String UID_SEPERATOR="_";
private DataRefEnum refData;
private String prefixRef;
private BigDecimal montant;

public DataItem() {
	super();
}
public DataItem(DataRefEnum ref){
	this();
	this.refData=ref;
}

public DataItem(DataRefEnum refData, String prefixRef) {
	this(refData);
	this.prefixRef = prefixRef;
}
public DataItem(DataRefEnum refData, double montant) {
	this(refData);
	setMontantDouble(montant);
}

public DataItem(DataRefEnum refData, String prefixRef, double montant) {
	this(refData);
	this.prefixRef = prefixRef;
	setMontantDouble(montant);
}

public DataItem(String uid,double montant){
	this();
	setMontantDouble(montant);
	if(uid!=null){
		if(uid.contains(UID_SEPERATOR))
		{
		String[] splits=uid.split(UID_SEPERATOR);
		this.prefixRef=splits[0];
		this.refData=DataRefEnum.parse(splits[1]);
		}
		else
		{
			this.refData=DataRefEnum.parse(uid);
		}
	}
}
public int getReference() {
	return getRefData().getCode();
}

public BigDecimal getMontant() {
//	montant=new BigDecimal(15005255);
	return montant;
}
public double getMontantDouble() {
	if(montant==null) return 0d;
	return montant.doubleValue();
}
public void setMontant(BigDecimal montant) {
	this.montant = montant;
}
public void setMontantDouble(double montant) {
	this.montant = new BigDecimal(montant);
}
public DataRefEnum getRefData() {
	return refData;
}
public void setRefData(DataRefEnum refData) {
	this.refData = refData;
}

public String getPrefixRef() {
	return prefixRef;
}
public void setPrefixRef(String prefixRef) {
	this.prefixRef = prefixRef;
}

public String getUid(){
	String uid=null;
	StringBuilder strBuilder=new StringBuilder();
	if((getPrefixRef()==null)||(getPrefixRef().trim().isEmpty()) )
			uid=getRefData().getCodeStr();
	else{
		strBuilder.append(getPrefixRef());
		strBuilder.append(UID_SEPERATOR);
		strBuilder.append(getRefData().getCodeStr());
		uid=strBuilder.toString();
	}
	return uid;
}
@Override
public int hashCode() {
	final int prime = 31;
	int result = 1;
	result = prime * result + ((prefixRef == null) ? 0 : prefixRef.hashCode());
	result = prime * result + ((refData == null) ? 0 : refData.hashCode());
	return result;
}
@Override
public boolean equals(Object obj) {
	if (this == obj)
		return true;
	if (obj == null)
		return false;
	if (getClass() != obj.getClass())
		return false;
	DataItem other = (DataItem) obj;
	if (prefixRef == null) {
		if (other.prefixRef != null)
			return false;
	} else if (!prefixRef.equals(other.prefixRef))
		return false;
	if (refData != other.refData)
		return false;
	return true;
}
public void add(double val) {
	if(this.montant==null)this.montant=new BigDecimal(val);
	else
	this.montant=this.montant.add(new BigDecimal(val));
	
}
public void reset() {
	setMontant(null);
	
}

@Override
public String toString() {
	return "DataItem [refData=" + refData + ", prefixRef=" + prefixRef + "]";
}
@Override
public Object clone() throws CloneNotSupportedException {
	DataItem data=(DataItem) super.clone();
	data.setMontant(new BigDecimal(this.getMontantDouble()));
	data.setRefData(this.getRefData());
	data.setPrefixRef(this.getPrefixRef());
	return data;
}
@Override
public void setValue(AttributImport attributConfig, Object value) {
	String strValue=null;
	BigDecimal bdValue=null;
	if(value instanceof String)
		strValue=(value!=null)?((String) value).trim():null;
		else if(value instanceof  BigDecimal)
			bdValue=(value!=null)?(BigDecimal) value:null;
	String attributName=attributConfig.getName();
	//logger.debug("setValue: {} --> '{}' / {}",attributName,strValue,bdValue);
	switch (attributName){
	case "prefix":
		setPrefixRef(strValue);
		break;
	case "refData":		
		setRefData(DataRefEnum.parse(strValue));
		break;
	case "montant":
		setMontant(bdValue);
		break;
	}
	
}
@Override
public ConfigImport getConfigImport() {
	if(configImport==null) configImport=BudgetHelper.loadConfigImport("gbcp.bi.import.config");
	return configImport;
}
private static ConfigImport configImport=null;

@Override
public void setRowNum(Integer rowNum) {
	// TODO Auto-generated method stub
	
}
@Override
public Integer getRowNum() {
	// TODO Auto-generated method stub
	return null;
}

}
