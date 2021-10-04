package fr.symphonie.budget.ui.excel;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import fr.symphonie.common.util.MsgEntry;
import fr.symphonie.doc.MSGenerator;
import fr.symphonie.tools.meta4dai.DaiInterfaceBean;
import fr.symphonie.util.HandlerJSFMessage;
import fr.symphonie.util.Helper;
import fr.symphonie.util.model.SimpleBean;

public class DAIConsultList implements ExcelDocType{
	

	@Override
	public String[] getColumnsKey() {
		String[] columnskey = new String[] {MsgEntry.CODE_DEST,MsgEntry.CODE_NAT,MsgEntry.CODE_SERV,MsgEntry.CODE_DIRECTION,MsgEntry.CODE_PROG,MsgEntry.NOM_PROG,MsgEntry.NUM_EI,MsgEntry.OBJET,MsgEntry.MONTANTCP,MsgEntry.DEP_PAIE,MsgEntry.AUTRES_DEP,MsgEntry.TOTAL_DEP,
				MsgEntry.DISPONIBLE};
		return columnskey;
	}

	@Override
	public String getTitle() {
		return HandlerJSFMessage.getMessageByKey(MsgEntry.META4DAI_DISPLAY_DATA);
	}

	@Override
	public List<SimpleBean> getCriteres() {
		DaiInterfaceBean dai=(DaiInterfaceBean) Helper.findBean("daiBean");
List<SimpleBean> criteres=new ArrayList<SimpleBean>();
		
		criteres.add(new SimpleBean(HandlerJSFMessage.getMessageByKey(MsgEntry.EXERCICE),
				""+dai.getExercice()));
		criteres.add(new SimpleBean(HandlerJSFMessage.getMessageByKey(MsgEntry.BUDGET),
				""+dai.getCodeBudget()));
		
		
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

