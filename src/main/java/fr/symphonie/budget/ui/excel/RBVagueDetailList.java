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

public class RBVagueDetailList implements ExcelDocType {

	
	SepaCreditTransfertBean sepa=(SepaCreditTransfertBean) Helper.findBean("sctBean");
	@Override
	public String[] getColumnsKey() {
		String[] columnskey = new String[] {MsgEntry.REMB_MODE_PAIEMENT,MsgEntry.REMB_NOM,MsgEntry.REMB_PRENOM,MsgEntry.CPWIN_MNT,MsgEntry.REMB_IBAN,MsgEntry.REMB_BIC,MsgEntry.OBJET};
		return columnskey;
	
	}

	@Override
	public String getTitle() {
		
		
	return  HandlerJSFMessage.formatMessage(HandlerJSFMessage.getMessage(MsgEntry.REMB_NUM_VAGUE_MSG),
				new Object[] { sepa.getNoVague() });
		//HandlerJSFMessage.addInfoMessage(message);
		//return HandlerJSFMessage.getMessageByKey(MsgEntry.REMB_CONSULT_CLIENT );
	}

	@Override
	public List<SimpleBean> getCriteres() {
		List<SimpleBean> criteres=new ArrayList<SimpleBean>();
		
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
