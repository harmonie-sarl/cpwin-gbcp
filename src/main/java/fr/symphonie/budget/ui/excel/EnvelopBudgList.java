package fr.symphonie.budget.ui.excel;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


import fr.symphonie.budget.ui.beans.SearchBean;
import fr.symphonie.common.util.MsgEntry;
import fr.symphonie.doc.MSGenerator;
import fr.symphonie.util.HandlerJSFMessage;
import fr.symphonie.util.Helper;
import fr.symphonie.util.model.SimpleBean;



public class EnvelopBudgList implements ExcelDocType{

	@Override
	public String[] getColumnsKey() {
		String[] columnskey = new String[] {MsgEntry.GROUPE_NATURE,MsgEntry.GROUPE_DEST,
				MsgEntry.PROGRAMME,  MsgEntry.MONTANT };
		return columnskey;
	}

	@Override
	public String getTitle() {
		return HandlerJSFMessage.getMessageByKey(MsgEntry.ENVELOP_BUDG_REPORT_TITRE);
	
	}

	@Override
	public List<SimpleBean> getCriteres() {
		List<SimpleBean> criteres=new ArrayList<SimpleBean>();
		SearchBean search=(SearchBean) Helper.findBean("searchBean");
		
		criteres.add(new SimpleBean(HandlerJSFMessage.getMessageByKey(MsgEntry.EXERCICE),
				""+search.getExercice()));
		
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
