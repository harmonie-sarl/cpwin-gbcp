package fr.symphonie.budget.ui.excel;

import fr.symphonie.common.util.MsgEntry;
import fr.symphonie.util.HandlerJSFMessage;

public class DetailSuiviRecetExecList extends DetailSuiviRecetList {
	
	@Override
	public String[] getColumnsKey() {
		String[] columnskey = new String[] {MsgEntry.EXEC_ORIG,MsgEntry.NO_TITRC,MsgEntry.MNT_INITIAL,MsgEntry.MNT_NET_HT,MsgEntry.MNT_NET_TTC,MsgEntry.RECET_HT,MsgEntry.SOLD_TTC,
				MsgEntry.AR_HT,MsgEntry.AR_TTC,MsgEntry.TIERS,MsgEntry.NOM_TIERS,  MsgEntry.OBJET,MsgEntry.ORIGINE,MsgEntry.SERVICE,MsgEntry.NAT_GRP,  MsgEntry.PROGRAMME };
		return columnskey;
	}
	
	@Override
	public String getTitle() {
		
		return HandlerJSFMessage.getMessageByKey(MsgEntry.TR_EXER);
	}

}
