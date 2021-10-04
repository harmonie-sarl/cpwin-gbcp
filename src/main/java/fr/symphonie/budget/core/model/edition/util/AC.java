package fr.symphonie.budget.core.model.edition.util;

import java.util.ArrayList;
import java.util.List;

public class AC extends GenericTab {

	private BilanItem stocks;

	private BilanItem creanceEP;
	private BilanItem creanceCCR;
	private BilanItem creanceRedev;
	private BilanItem avanceAcmptCmd;
	private BilanItem creanceCorrespOp;
	private BilanItem creanceAutreDebit;

	private BilanItem chargeConstat;
	private BilanItem totalActifCric;

	public AC() {
		super();
		this.stocks = new BilanItem(DataRefEnum.cf_ref_057,DataRefEnum.cf_ref_058,DataRefEnum.cf_ref_059,DataRefEnum.cf_ref_060);

		this.creanceEP = new BilanItem(DataRefEnum.cf_ref_061,DataRefEnum.cf_ref_062,DataRefEnum.cf_ref_063,DataRefEnum.cf_ref_064);
		this.creanceCCR = new BilanItem(DataRefEnum.cf_ref_065,DataRefEnum.cf_ref_066,DataRefEnum.cf_ref_067,DataRefEnum.cf_ref_068);
		this.creanceRedev = new BilanItem(DataRefEnum.cf_ref_069,DataRefEnum.cf_ref_070,DataRefEnum.cf_ref_071,DataRefEnum.cf_ref_072);
		this.avanceAcmptCmd = new BilanItem(DataRefEnum.cf_ref_073,DataRefEnum.cf_ref_074,DataRefEnum.cf_ref_075,DataRefEnum.cf_ref_076);
		this.creanceCorrespOp = new BilanItem(DataRefEnum.cf_ref_077,DataRefEnum.cf_ref_078,DataRefEnum.cf_ref_079,DataRefEnum.cf_ref_080);
		this.creanceAutreDebit = new BilanItem(DataRefEnum.cf_ref_081,DataRefEnum.cf_ref_082,DataRefEnum.cf_ref_083,DataRefEnum.cf_ref_084);

		this.chargeConstat = new BilanItem(DataRefEnum.cf_ref_085,DataRefEnum.cf_ref_086,DataRefEnum.cf_ref_087,DataRefEnum.cf_ref_088);
		this.totalActifCric = new BilanItem(DataRefEnum.cf_ref_089,DataRefEnum.cf_ref_090,DataRefEnum.cf_ref_091,DataRefEnum.cf_ref_092);
	}

	@Override
	public List<DataItem> getItems() {
		List<DataItem> items=new ArrayList<DataItem>();
		items.addAll(getStocks().getItems());
		items.addAll(getCreanceEP().getItems());
		items.addAll(getCreanceCCR().getItems());
		items.addAll(getCreanceRedev().getItems());
		items.addAll(getAvanceAcmptCmd().getItems());
		items.addAll(getCreanceCorrespOp().getItems());
		items.addAll(getCreanceAutreDebit().getItems());
		items.addAll(getChargeConstat().getItems());
		items.addAll(getTotalActifCric().getItems());		
		return items;
	}
	public List<DataItem> getItemsAnt() {
		List<DataItem> items=new ArrayList<DataItem>();
		items.add(getStocks().getNetAnt());
		items.add(getCreanceEP().getNetAnt());
		items.add(getCreanceCCR().getNetAnt());
		items.add(getCreanceRedev().getNetAnt());
		items.add(getAvanceAcmptCmd().getNetAnt());
		items.add(getCreanceCorrespOp().getNetAnt());
		items.add(getCreanceAutreDebit().getNetAnt());
		items.add(getChargeConstat().getNetAnt());
		items.add(getTotalActifCric().getNetAnt());			
		return items;
	}
	@Override
	public void setDefaults() {

		getTotalActifCric().getBrutN().setMontantDouble(getStocks().getBrutN().getMontantDouble()
				+ getCreanceEP().getBrutN().getMontantDouble() + getCreanceCCR().getBrutN().getMontantDouble()
				+ getCreanceRedev().getBrutN().getMontantDouble() + getAvanceAcmptCmd().getBrutN().getMontantDouble()
				+ getCreanceCorrespOp().getBrutN().getMontantDouble()
				+ getCreanceAutreDebit().getBrutN().getMontantDouble()
				+ getChargeConstat().getBrutN().getMontantDouble()

		);

		getTotalActifCric().getAmortDeprN().setMontantDouble(getStocks().getAmortDeprN().getMontantDouble()
				+ getCreanceEP().getAmortDeprN().getMontantDouble() + getCreanceCCR().getAmortDeprN().getMontantDouble()
				+ getCreanceRedev().getAmortDeprN().getMontantDouble()
				+ getAvanceAcmptCmd().getAmortDeprN().getMontantDouble()
				+ getCreanceCorrespOp().getAmortDeprN().getMontantDouble()
				+ getCreanceAutreDebit().getAmortDeprN().getMontantDouble()
				+ getChargeConstat().getAmortDeprN().getMontantDouble()

		);

		getTotalActifCric().getNetN().setMontantDouble(getStocks().getNetN().getMontantDouble()
				+ getCreanceEP().getNetN().getMontantDouble() + getCreanceCCR().getNetN().getMontantDouble()
				+ getCreanceRedev().getNetN().getMontantDouble() + getAvanceAcmptCmd().getNetN().getMontantDouble()
				+ getCreanceCorrespOp().getNetN().getMontantDouble()
				+ getCreanceAutreDebit().getNetN().getMontantDouble() + getChargeConstat().getNetN().getMontantDouble()

		);
		getTotalActifCric().getNetAnt().setMontantDouble(getStocks().getNetAnt().getMontantDouble()
				+ getCreanceEP().getNetAnt().getMontantDouble() + getCreanceCCR().getNetAnt().getMontantDouble()
				+ getCreanceRedev().getNetAnt().getMontantDouble() + getAvanceAcmptCmd().getNetAnt().getMontantDouble()
				+ getCreanceCorrespOp().getNetAnt().getMontantDouble()
				+ getCreanceAutreDebit().getNetAnt().getMontantDouble()
				+ getChargeConstat().getNetAnt().getMontantDouble());

	}	


	public BilanItem getStocks() {
		return stocks;
	}

	public void setStocks(BilanItem stocks) {
		this.stocks = stocks;
	}

	public BilanItem getCreanceEP() {
		return creanceEP;
	}

	public void setCreanceEP(BilanItem creanceEP) {
		this.creanceEP = creanceEP;
	}

	public BilanItem getCreanceCCR() {
		return creanceCCR;
	}

	public void setCreanceCCR(BilanItem creanceCCR) {
		this.creanceCCR = creanceCCR;
	}

	public BilanItem getCreanceRedev() {
		return creanceRedev;
	}

	public void setCreanceRedev(BilanItem creanceRedev) {
		this.creanceRedev = creanceRedev;
	}

	public BilanItem getAvanceAcmptCmd() {
		return avanceAcmptCmd;
	}

	public void setAvanceAcmptCmd(BilanItem avanceAcmptCmd) {
		this.avanceAcmptCmd = avanceAcmptCmd;
	}

	public BilanItem getCreanceCorrespOp() {
		return creanceCorrespOp;
	}

	public void setCreanceCorrespOp(BilanItem creanceCorrespOp) {
		this.creanceCorrespOp = creanceCorrespOp;
	}

	public BilanItem getCreanceAutreDebit() {
		return creanceAutreDebit;
	}

	public void setCreanceAutreDebit(BilanItem creanceAutreDebit) {
		this.creanceAutreDebit = creanceAutreDebit;
	}

	public BilanItem getChargeConstat() {
		return chargeConstat;
	}

	public void setChargeConstat(BilanItem chargeConstat) {
		this.chargeConstat = chargeConstat;
	}

	public BilanItem getTotalActifCric() {
		return totalActifCric;
	}

	public void setTotalActifCric(BilanItem totalActifCric) {
		this.totalActifCric = totalActifCric;
	}

}
