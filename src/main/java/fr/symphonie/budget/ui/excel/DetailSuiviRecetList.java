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

public class DetailSuiviRecetList implements ExcelDocType{

	@Override
	public String[] getColumnsKey() {
		String[] columnskey = new String[] {MsgEntry.EXEC_ORIG,MsgEntry.NO_TITRC,MsgEntry.MNT_INITIAL,MsgEntry.NET_HT,MsgEntry.NET_TTC,MsgEntry.RECET_HT,MsgEntry.SOLD_TTC,
				MsgEntry.TIERS,MsgEntry.NOM_TIERS,  MsgEntry.OBJET,MsgEntry.ORIGINE,MsgEntry.SERVICE,MsgEntry.NAT_GRP,  MsgEntry.PROGRAMME };
		return columnskey;
	}

	@Override
	public String getTitle() {
		
		return HandlerJSFMessage.getMessageByKey(MsgEntry.TOTAL);
	}

	@Override
	public List<SimpleBean> getCriteres() {
		List<SimpleBean> criteres=new ArrayList<SimpleBean>();
		SearchBean search=(SearchBean) Helper.findBean("searchBean");
//		LoaderBean loader=(LoaderBean) Helper.findBean("loaderBean");
		
		criteres.add(new SimpleBean(HandlerJSFMessage.getMessageByKey(MsgEntry.EXERCICE),
				""+search.getExercice()));
		criteres.add(new SimpleBean(HandlerJSFMessage.getMessageByKey(MsgEntry.BUDGET),
				""+search.getCodeBudget()));
		
		
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
	public String getFileNamePrefix()  {
		return MSGenerator.GENERATED;
	}

}
