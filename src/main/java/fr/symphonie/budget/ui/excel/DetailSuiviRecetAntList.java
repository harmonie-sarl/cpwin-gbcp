package fr.symphonie.budget.ui.excel;

import fr.symphonie.common.util.MsgEntry;
import fr.symphonie.util.HandlerJSFMessage;

public class DetailSuiviRecetAntList extends DetailSuiviRecetList {
	
	
	@Override
	public String[] getColumnsKey() {
		String[] columnskey = new String[] {MsgEntry.EXEC_ORIG,MsgEntry.NO_TITRC,MsgEntry.MNT_INITIAL,MsgEntry.RAR_HT_01,MsgEntry.RAR_TTC_01,MsgEntry.RECET_HT,MsgEntry.SOLD_TTC,
				MsgEntry.ONB_HT,MsgEntry.ONB_TTC,MsgEntry.TIERS,MsgEntry.NOM_TIERS,  MsgEntry.OBJET,MsgEntry.ORIGINE,MsgEntry.SERVICE,MsgEntry.NAT_GRP,  MsgEntry.PROGRAMME };
		return columnskey;
	}
	
	@Override
	public String getTitle() {
		
		return HandlerJSFMessage.getMessageByKey(MsgEntry.TR_ANT);
	}

}
