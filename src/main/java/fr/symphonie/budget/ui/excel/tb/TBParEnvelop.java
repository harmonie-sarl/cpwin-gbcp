package fr.symphonie.budget.ui.excel.tb;

import java.util.List;
import java.util.Map;

import fr.symphonie.budget.ui.beans.CommonBean;
import fr.symphonie.budget.ui.beans.pluri.BudgetPluriannuelBean;
import fr.symphonie.budget.ui.doc.CustomHashMap;
import fr.symphonie.budget.ui.doc.TBDocxHandler;
import fr.symphonie.budget.ui.doc.TbDocxStruct;
import fr.symphonie.budget.ui.excel.ExcelDocType;
import fr.symphonie.common.util.BudgetHelper;
import fr.symphonie.common.util.MsgEntry;
import fr.symphonie.doc.MSGenerator;
import fr.symphonie.util.HandlerJSFMessage;
import fr.symphonie.util.model.SimpleBean;

public class TBParEnvelop implements ExcelDocType {

	
	BudgetPluriannuelBean bean= BudgetHelper.getBpBean();
	CommonBean cBean = BudgetHelper.getCommonBean();

	Map<String, Object> context=new CustomHashMap<String, Object>();
	@Override
	public String[] getColumnsKey() {
		return null;
	}

	@Override
	public String getTitle() {
		return HandlerJSFMessage.getMessageByKey(MsgEntry.MENU_PAR_ENVELOP);
	}

	@Override
	public List<SimpleBean> getCriteres() {	
		return null;
	}

	@Override
	public Map<String, String> getSpecificColumns() {
		return null;
	}

	@Override
	public Map<String, Object> getSpecificValues() {
		TBDocxHandler tbDocx=new TBDocxHandler();
		TbDocxStruct tbStruct=cBean.createTbDocxStruct();
		context= tbDocx.getRepartParEnvelop(tbStruct);
		 tbDocx.generalData(context, tbStruct);
		return context;
	}
	@Override
	public String getFileNamePrefix() {
		return MSGenerator.GENERATED;
	}
}
