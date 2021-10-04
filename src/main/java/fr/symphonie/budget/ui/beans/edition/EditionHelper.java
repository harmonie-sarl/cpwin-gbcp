package fr.symphonie.budget.ui.beans.edition;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.symphonie.budget.core.model.demat.GenericDemat;
import fr.symphonie.budget.core.model.edition.EditionBiBr;
import fr.symphonie.budget.core.model.edition.Edition;
import fr.symphonie.budget.core.model.edition.TabsEnum;
import fr.symphonie.budget.core.model.edition.util.DataItem;

public class EditionHelper {
	 private static final Logger logger = LoggerFactory.getLogger(EditionHelper.class);

private List<DataItem> dataABP=null;
private List<DataItem> dataCRP=null;
private List<DataItem> dataTFP=null;
private List<DataItem> dataVFR=null;
private List<DataItem> dataCAFP=null;
private List<DataItem> dataEFP=null;
private List<DataItem> dataABE=null;
private List<DataItem> dataEFE=null;
private List<DataItem> dataTAB10=null;
private List<DataItem> dataTAB3_TDD=null;
private List<DataItem> dataTAB3_TRO=null;


private List<DataItem> dataTAB8;

public EditionHelper(){
	super();
	this.dataABE=new ArrayList<DataItem>();
	this.dataABP=new ArrayList<DataItem>();
	this.dataCAFP=new ArrayList<DataItem>();
	this.dataCRP=new ArrayList<DataItem>();
	this.dataEFE=new ArrayList<DataItem>();
	this.dataEFP=new ArrayList<DataItem>();
	this.dataTFP=new ArrayList<DataItem>();
	this.dataVFR=new ArrayList<DataItem>();
	this.dataTAB10=new ArrayList<DataItem>();
	this.dataTAB8=new ArrayList<DataItem>();
	this.dataTAB3_TDD=new ArrayList<DataItem>();
	this.dataTAB3_TRO=new ArrayList<DataItem>();

	
}

public void fillEdition(Edition target,List<EditionBiBr> from){
	setData(from);
	fillEdition(target);
	
}


public void fillEdition(Edition target) {
	logger.debug("fillEdition: start");
	loadTab2(target);
	loadTab3(target);
	loadTab4(target);
	loadTab6(target);
	loadTab8(target);
	loadTab10(target);
	target.getAbe().setData(dataABE);
	target.getEfe().setData(dataEFE);
	
	logger.debug("fillEdition: end");
	
	
}

	private void loadTab4(Edition target) {
	//	afficher(dataEFP);
		target.getTab4().setData(dataEFP);
		
	}


	private void loadTab10(Edition target) {
		target.getTab10().setData(dataTAB10);	
		
	}
	private void loadTab8(Edition target) {
		target.getTab8().setData(dataTAB8);	
		
	}

	private void loadTab6(Edition target) {
		target.getTab6().getCrp().setData(dataCRP);
		target.getTab6().getCaf().setData(dataCAFP);
		target.getTab6().getTfp().setData(dataTFP);
		target.getTab6().getVfr().setData(dataVFR);
	}

	private void loadTab3(Edition target) {
		
		target.getTab3().getTdd().setData(dataTAB3_TDD);
		target.getTab3().getTro().setData(dataTAB3_TRO);
	}

	private void loadTab2(Edition target) {
		target.getTab2().setData(dataABP);

	}
public void setData(TabsEnum t, List<? extends GenericDemat> dematList){
	
	List<DataItem> data=getDataMap(t);
	if(data!=null)
	data.addAll(convertToMap(dematList));

}
public static List<DataItem> convertToMap(List<? extends GenericDemat> dataList) {
	List<DataItem> map=new ArrayList<DataItem>();
	for(GenericDemat item:dataList){
		map.add(new DataItem(item.getLibelle(),item.getMontant()));
	}
	return map;
}

public void setData(List<EditionBiBr> bibrList) {
	logger.debug("setData: bibrList="+bibrList.size());
	List<DataItem> map=null;
	for(EditionBiBr item:bibrList){
		//logger.debug("setData: item"+item.getRefData()+", "+item.getTypeData()+","+item.getMontant());
		map=getDataMap(item.getTypeData());
		if(map!=null)
		map.add(new DataItem(item.getRefData(),item.getMontant()));	
//		logger.debug("setData: item type: "+item.getTypeData()+","+item.getMontant());
//		logger.debug("setData: size map"+map.size());
	}
	
	logger.debug("setData: map size={}",(map!=null?map.size():0));
}
private List<DataItem> getDataMap(TabsEnum t){
	List<DataItem> map=null;
	if(t==null)return map;
	switch(t){
	case ABP:
		map=dataABP;
		break;
	case CRP:
		map=dataCRP;
		break;
	case CAFP:
		map=dataCAFP;
		break;
	case TFP:
		map=dataTFP;
		break;
	case SPP2:
		map=dataVFR;
		break;	
	case EFP:
		map=dataEFP;
		break;
	case EFE:
		map=dataEFE;
		break;
	case ABE:
		map=dataABE;
		break;
	case TAB8:
		map=dataTAB8;
		break;
	case TAB10:
		map=dataTAB10;
		break;
	case TAB3_TDD:
		map=dataTAB3_TDD;
		break;
	case TAB3_TRO:
		map=dataTAB3_TRO;
		break;	
	default:
		break;
	}
	return map;
}


public List<DataItem> getDataEFP() {
	return dataEFP;
}


public void setDataEFP(List<DataItem> dataEFP) {
	this.dataEFP = dataEFP;
}

public static void afficher(List<DataItem> list){
	for(DataItem item:list){
	logger.debug("afficher : type="+item.getUid()+"-->"+item.getMontantDouble());
	}
}

public void fillEdition(Edition e, Map<TabsEnum, List<DataItem>> dataMap) {
	for(TabsEnum t:dataMap.keySet()) {
		getDataMap(t).addAll(dataMap.get(t));
	}
	fillEdition(e);
	
}
}
