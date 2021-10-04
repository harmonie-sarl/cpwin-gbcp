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

public class DetailEjPluriList implements ExcelDocType {

	@Override
	public String[] getColumnsKey() {
		String[] columnskey = new String[] { MsgEntry.DESTINATION, MsgEntry.NATURE, MsgEntry.SERVICE,
				MsgEntry.PROGRAMME,MsgEntry.AE_CP_FLECHE, MsgEntry.NUM_EJ, MsgEntry.OBJET, MsgEntry.CODE_TIERS, MsgEntry.NOM_TIERS,
				MsgEntry.MONTANT_TOTAL, MsgEntry.PLAN_EX_COURANT, MsgEntry.PLAN_EX_SUP };
		return columnskey;
	}

	@Override
	public String getTitle() {
		return HandlerJSFMessage.getMessageByKey(MsgEntry.EJ_PLURI);
	}

	@Override
	public List<SimpleBean> getCriteres() {
		List<SimpleBean> criteres = new ArrayList<SimpleBean>();
		SearchBean search = (SearchBean) Helper.findBean("searchBean");
		LoaderBean loader = (LoaderBean) Helper.findBean("loaderBean");

		criteres.add(new SimpleBean(HandlerJSFMessage.getMessageByKey(MsgEntry.EXERCICE), "" + search.getExercice()));
		criteres.add(new SimpleBean(HandlerJSFMessage.getMessageByKey(MsgEntry.BUDGET), "" + search.getCodeBudget()));
		criteres.add(new SimpleBean(HandlerJSFMessage.getMessageByKey(MsgEntry.GROUPE_NATURE),
				"" + loader.getlibGroupNature(search.getCodeNatureGrp())));

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
