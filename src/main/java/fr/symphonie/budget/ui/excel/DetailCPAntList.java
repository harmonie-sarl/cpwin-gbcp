package fr.symphonie.budget.ui.excel;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import fr.symphonie.budget.ui.beans.LoaderBean;
import fr.symphonie.budget.ui.beans.SearchBean;
import fr.symphonie.common.util.MsgEntry;
import fr.symphonie.doc.MSGenerator;
import fr.symphonie.util.HandlerJSFMessage;
import fr.symphonie.util.Helper;
import fr.symphonie.util.model.SimpleBean;

public class DetailCPAntList implements ExcelDocType {

	@Override
	public String[] getColumnsKey() {
		String[] columnskey = new String[] {MsgEntry.CODE_DEST,MsgEntry.CODE_NAT,MsgEntry.CODE_SERV,MsgEntry.CODE_PROG,MsgEntry.NOM_PROG,MsgEntry.NUM_EJ,MsgEntry.RAP_01,
				MsgEntry.TIERS,  MsgEntry.OBJET,MsgEntry.NUM_EI,MsgEntry.DP_SAISI,  MsgEntry.DP_EMISE,MsgEntry.CP_DP_PEC,  MsgEntry.CREDI_ANNUL, MsgEntry.RESTE_APAYER };
		return columnskey;
	}

	@Override
	public String getTitle() {
		return HandlerJSFMessage.getMessageByKey(MsgEntry.SUR_AE_ANT);
	}

	@Override
	public List<SimpleBean> getCriteres() {
		List<SimpleBean> criteres=new ArrayList<SimpleBean>();
		SearchBean search=(SearchBean) Helper.findBean("searchBean");
		LoaderBean loader=(LoaderBean) Helper.findBean("loaderBean");
		
		criteres.add(new SimpleBean(HandlerJSFMessage.getMessageByKey(MsgEntry.EXERCICE),
				""+search.getExercice()));
		criteres.add(new SimpleBean(HandlerJSFMessage.getMessageByKey(MsgEntry.BUDGET),
				""+search.getCodeBudget()));
		criteres.add(new SimpleBean(HandlerJSFMessage.getMessageByKey(MsgEntry.GROUPE_NATURE),
				""+loader.getlibGroupNature(search.getCodeNatureGrp())));
		
		return criteres;
	}

	@Override
	public Map<String, String> getSpecificColumns() {
		return null;
	}

	@Override
	public Map<String, Object> getSpecificValues() {
		return null;
	}

	@Override
	public String getFileNamePrefix() {
		return MSGenerator.GENERATED;
	}

}
