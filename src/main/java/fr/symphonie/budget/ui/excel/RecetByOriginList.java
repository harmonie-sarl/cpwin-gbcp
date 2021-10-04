package fr.symphonie.budget.ui.excel;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import fr.symphonie.budget.ui.beans.SearchBean;
import fr.symphonie.budget.ui.beans.pluri.BudgetPluriannuelBean;
import fr.symphonie.common.util.MsgEntry;
import fr.symphonie.doc.MSGenerator;
import fr.symphonie.util.HandlerJSFMessage;
import fr.symphonie.util.Helper;
import fr.symphonie.util.model.SimpleBean;

public class RecetByOriginList implements ExcelDocType{

	@Override
	public String[] getColumnsKey() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getTitle() {
		return HandlerJSFMessage.getMessageByKey(MsgEntry.MENU_VENTIL_AE_RECETTE_ORIGINE );
	}

	@Override
	public List<SimpleBean> getCriteres() {
		List<SimpleBean> criteres=new ArrayList<SimpleBean>();
		SearchBean search=(SearchBean) Helper.findBean("searchBean");
		BudgetPluriannuelBean bpBean=(BudgetPluriannuelBean) Helper.findBean("bpBean");
//		LoaderBean loader=(LoaderBean) Helper.findBean("loaderBean");
		
		criteres.add(new SimpleBean(HandlerJSFMessage.getMessageByKey(MsgEntry.EXERCICE),
				""+search.getExercice()));
		criteres.add(new SimpleBean(HandlerJSFMessage.getMessageByKey(MsgEntry.BUDGET),
				""+search.getCodeBudget()));
		criteres.add(new SimpleBean(HandlerJSFMessage.getMessageByKey(MsgEntry.NIVEAU_DEST),
				""+bpBean.getDepNivDest()));
		
		
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
