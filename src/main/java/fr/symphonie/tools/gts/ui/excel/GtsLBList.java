package fr.symphonie.tools.gts.ui.excel;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import fr.symphonie.budget.ui.excel.ExcelDocType;
import fr.symphonie.common.util.MsgEntry;
import fr.symphonie.doc.MSGenerator;
import fr.symphonie.tools.gts.ui.GtsBean;
import fr.symphonie.util.HandlerJSFMessage;
import fr.symphonie.util.Helper;
import fr.symphonie.util.model.SimpleBean;

public class GtsLBList implements ExcelDocType{

	@Override
	public String[] getColumnsKey() {
		String[] columnskey = new String[] { 
		MsgEntry.DESTINATION,MsgEntry.SERVICE,
		MsgEntry.NATURE,MsgEntry.PROGRAMME,MsgEntry.MNT_TOTAL, MsgEntry.DONT_REGISSEUR, MsgEntry.DONT_CLIENT };
		return columnskey;
	}

	@Override
	public String getTitle() {
		return HandlerJSFMessage.getMessageByKey(MsgEntry.RECET_LIGN_BUDG);
	}

	@Override
	public List<SimpleBean> getCriteres() {
		List<SimpleBean> criteres=new ArrayList<SimpleBean>();
		GtsBean gtsBean=(GtsBean) Helper.findBean("gtsBean");
		
		
		criteres.add(new SimpleBean(HandlerJSFMessage.getMessageByKey(MsgEntry.EXERCICE),
				""+gtsBean.getExercice()));
		criteres.add(new SimpleBean(HandlerJSFMessage.getMessageByKey(MsgEntry.PERIODE),
				""+gtsBean.getNumPeriode()));
		
		return criteres;
	}

	@Override
	public Map<String, String> getSpecificColumns() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<String, Object> getSpecificValues() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getFileNamePrefix() {
		return MSGenerator.GENERATED;
	}

}
