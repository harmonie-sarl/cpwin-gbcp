package fr.symphonie.budget.ui.excel;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import fr.symphonie.common.util.MsgEntry;
import fr.symphonie.doc.MSGenerator;
import fr.symphonie.util.HandlerJSFMessage;
import fr.symphonie.util.model.SimpleBean;

public class ExistantList implements ExcelDocType{

	@Override
	public String[] getColumnsKey() {
		String[] columnskey = new String[] { MsgEntry.NUM_TIERS, MsgEntry.TIERS_TYPE, MsgEntry.RAISON_SOCIAL,
				MsgEntry.LOGIN_LASTNAME, MsgEntry.LOGIN_FIRST_NAME, MsgEntry.BUR_DISTR, MsgEntry.NUM_VOIE,
				MsgEntry.NAT_VOIE, MsgEntry.NOM_VOIE, MsgEntry.SIRET, MsgEntry.DAS_PROFESSION, };
		return columnskey;
	}

	@Override
	public String getTitle() {
		return HandlerJSFMessage.getMessageByKey(MsgEntry.CONSULT_TIERS);
	}

	@Override
	public List<SimpleBean> getCriteres() {
		List<SimpleBean> criteres=new ArrayList<SimpleBean>();
		
		
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
