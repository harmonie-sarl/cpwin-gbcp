package fr.symphonie.budget.ui.excel;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import fr.symphonie.common.util.MsgEntry;
import fr.symphonie.doc.MSGenerator;
import fr.symphonie.tools.sct.SepaCreditTransfertBean;
import fr.symphonie.util.HandlerJSFMessage;
import fr.symphonie.util.Helper;
import fr.symphonie.util.model.SimpleBean;

public class RBConsultTiersList implements ExcelDocType{

	@Override
	public String[] getColumnsKey() {
		String[] columnskey = new String[] {MsgEntry.REMB_NUM_VAGUE,MsgEntry.REMB_NOM_CLIENT,MsgEntry.REMB_IBAN,MsgEntry.REMB_BIC,MsgEntry.REMB_MODE_PAIEMENT,MsgEntry.CPWIN_MNT,MsgEntry.OBJET,
				MsgEntry.REMB_NO_ECRITURE,MsgEntry.REMB_NO_BORDEREAU,  MsgEntry.REMB_DATE_BORDEREAU};
		return columnskey;
	}

	@Override
	public String getTitle() {
		return HandlerJSFMessage.getMessageByKey(MsgEntry.REMB_CONSULT_CLIENT );
	}

	@Override
	public List<SimpleBean> getCriteres() {
		List<SimpleBean> criteres=new ArrayList<SimpleBean>();
		SepaCreditTransfertBean sepa=(SepaCreditTransfertBean) Helper.findBean("sctBean");
		
		criteres.add(new SimpleBean(HandlerJSFMessage.getMessageByKey(MsgEntry.EXERCICE),
				""+sepa.getExercice()));
		criteres.add(new SimpleBean(HandlerJSFMessage.getMessageByKey(MsgEntry.BUDGET),
				""+sepa.getCodeBudget()));
		
		
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
