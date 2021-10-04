package fr.symphonie.budget.ui.excel;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import fr.symphonie.budget.ui.beans.SearchBean;
import fr.symphonie.common.util.MsgEntry;
import fr.symphonie.doc.MSGenerator;
import fr.symphonie.tools.das.ui.DasBean;
import fr.symphonie.util.HandlerJSFMessage;
import fr.symphonie.util.Helper;
import fr.symphonie.util.model.SimpleBean;

public class HonoraireList implements ExcelDocType{

	@Override
	public String[] getColumnsKey() {
		String[] columnskey = new String[] { 
				MsgEntry.EXERCICE, 
//				MsgEntry.TIERS, MsgEntry.REMUN_VERSE,MsgEntry.DEDATIL_HONORAIRE,
				
				MsgEntry.NUM_TIERS, MsgEntry.RAISON_SOCIAL,
				MsgEntry.LOGIN_LASTNAME, MsgEntry.LOGIN_FIRST_NAME,
				MsgEntry.HONORAIRES, MsgEntry.COMISSIONS,
				MsgEntry.COURTAGE, MsgEntry.RISTOURNES,
				MsgEntry.JETON_PRESENCE, MsgEntry.DROIT_AUTEUR,
				MsgEntry.DROIT_INVENT, MsgEntry.AUTRES_REMUN,
				MsgEntry.INDEM_REMBOURS, MsgEntry.AVANTAGES_NATURE, 
				MsgEntry.RETENUE_SOURCE };
		return columnskey;
	}

	@Override
	public String getTitle() {
		return HandlerJSFMessage.getMessageByKey(MsgEntry.SUIVI_TIERS);
	
	}

	@Override
	public List<SimpleBean> getCriteres() {
		List<SimpleBean> criteres=new ArrayList<SimpleBean>();
		SearchBean search=(SearchBean) Helper.findBean("searchBean");
		DasBean dasBean=(DasBean)Helper.findBean("dasBean");
		
		criteres.add(new SimpleBean(HandlerJSFMessage.getMessageByKey(MsgEntry.EXERCICE),
				""+search.getExercice()));
		criteres.add(new SimpleBean(HandlerJSFMessage.getMessageByKey(MsgEntry.TIERS),
				(dasBean.getTiersForSearch()==null)?"":dasBean.getTiersForSearch().getDescription()));
		
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
