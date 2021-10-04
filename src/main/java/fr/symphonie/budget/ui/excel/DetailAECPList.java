package fr.symphonie.budget.ui.excel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fr.symphonie.budget.ui.beans.LoaderBean;
import fr.symphonie.budget.ui.beans.SearchBean;
import fr.symphonie.budget.ui.beans.pluri.BudgetPluriannuelBean;
import fr.symphonie.common.util.BudgetHelper;
import fr.symphonie.common.util.MsgEntry;
import fr.symphonie.doc.MSGenerator;
import fr.symphonie.util.HandlerJSFMessage;
import fr.symphonie.util.Helper;
import fr.symphonie.util.model.SimpleBean;

public class DetailAECPList implements ExcelDocType {

	@Override
	public String[] getColumnsKey() {
		String[] columnskey = new String[] { MsgEntry.CODE_DEST, MsgEntry.CODE_NAT, MsgEntry.CODE_SERV,
				MsgEntry.CODE_PROG,MsgEntry.AE_CP_FLECHE, MsgEntry.RAP_01, MsgEntry.CREDI_ANNUL, MsgEntry.COMPL, MsgEntry.DPANT,
				MsgEntry.RAP_ANT, MsgEntry.AE_CO, MsgEntry.EJ, MsgEntry.DP_EX, MsgEntry.RAP_EXERC, MsgEntry.DP_TOT,
				MsgEntry.DR, MsgEntry.DP_NET, MsgEntry.RAP_ACT, MsgEntry.NON_ENG, MsgEntry.A_PAYER_SUR,
				MsgEntry.CP_VOTE, MsgEntry.DISPO, MsgEntry.BESOIN };
		return columnskey;
	}

	@Override
	public String getTitle() {
		return HandlerJSFMessage.getMessageByKey(MsgEntry.SUIVI_AE_CP);
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
		Map<String, Object> map = new HashMap<String, Object>();
		BudgetPluriannuelBean bpBean = getBpBean();

		map.put("totCpVote", bpBean.getTotCpVote());
		// map.put("totDispo",bpBean.getTotd);

		return map;
	}

	@Override
	public String getFileNamePrefix() {
		return MSGenerator.GENERATED;
	}

	public BudgetPluriannuelBean getBpBean() {
		BudgetPluriannuelBean bean = BudgetHelper.getBpBean();

		return bean;
	}

}
