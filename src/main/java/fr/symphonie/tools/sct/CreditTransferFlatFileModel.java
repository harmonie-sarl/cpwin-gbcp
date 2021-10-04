package fr.symphonie.tools.sct;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import fr.symphonie.common.model.IFlatLine;
import fr.symphonie.common.util.Util;
import fr.symphonie.cpwin.model.sepa.Operation;
import fr.symphonie.cpwin.model.sepa.Protocol;
import fr.symphonie.impor.AttributImport;
import fr.symphonie.impor.TypeEnum;

public class CreditTransferFlatFileModel {
	static final String _SEPARATOR = " ";
	static final String DATE_FORMAT="yyyy-MM-dd";
private IFlatLine headerLine;
private List<IFlatLine> body;
private IFlatLine footerLine;
private BigDecimal totalAmount;

public CreditTransferFlatFileModel(Protocol params, List<? extends Operation> creditTransfertList, Date dateEchange, Date dateReglement) {
	this.headerLine=createHeader(params, dateEchange);
	this.body=new ArrayList<IFlatLine>();
	this.totalAmount=BigDecimal.ZERO;
	for(Operation sct:creditTransfertList) {
		body.add(createBodyItem(params,sct,dateReglement));
		totalAmount=totalAmount.add(sct.getAmount());
	}
	this.footerLine=createFooter(params,creditTransfertList.size(),totalAmount,dateReglement);
	
}
private IFlatLine createHeader(Protocol params, Date dateEchange) {
	return new IFlatLine() {
		
		@Override
		public AttributImport[] getAttributs() {
			int i=0;
			AttributImport[] attributs={
					new AttributImport(i++,TypeEnum.Alpha,15,"PAIN.001.001.02"),
					new AttributImport(i++,TypeEnum.Alpha,4,_SEPARATOR),
					new AttributImport(i++,TypeEnum.Alpha,10,Util.formatDate(dateEchange,DATE_FORMAT)),
					new AttributImport(i++,TypeEnum.Alpha,70,Util.controlSpecial(params.getDebtor().getName())),
					new AttributImport(i++,TypeEnum.Alpha,142,_SEPARATOR),
					new AttributImport(i++,TypeEnum.Alpha,35,getRefTechnique(params.getIdTransfert(),dateEchange)),
					new AttributImport(i++,TypeEnum.Alpha,45,_SEPARATOR),
					new AttributImport(i++,TypeEnum.Alpha,70,Util.controlSpecial(params.getUltimateDebtor().getName())),
					new AttributImport(i++,TypeEnum.Alpha,142,_SEPARATOR),
					new AttributImport(i++,TypeEnum.Alpha,35,getRefMetier()),
					new AttributImport(i++,TypeEnum.Alpha,2,"1D"),
					new AttributImport(i++,TypeEnum.Alpha,6,params.getUltimateDebtor().getBic()),//CODIQUE
					new AttributImport(i++,TypeEnum.Alpha,1,"0"),
					new AttributImport(i++,TypeEnum.Alpha,793,_SEPARATOR),
					new AttributImport(i++,TypeEnum.Alpha,2,"01"),
					
			};
			return attributs;
		}
	};
}


private IFlatLine createBodyItem(Protocol params, Operation sct, Date dateReglement) {
return new IFlatLine() {
		
		@Override
		public AttributImport[] getAttributs() {
			int i=0;
			AttributImport[] attributs={
					new AttributImport(i++,TypeEnum.Alpha,15,"PAIN.001.001.02"),
					new AttributImport(i++,TypeEnum.Alpha,4,_SEPARATOR),
					new AttributImport(i++,TypeEnum.Alpha,10,Util.formatDate(dateReglement,DATE_FORMAT)),
					new AttributImport(i++,TypeEnum.Alpha,70,Util.controlSpecial(params.getDebtor().getName())),
					new AttributImport(i++,TypeEnum.Alpha,142,_SEPARATOR),
					new AttributImport(i++,TypeEnum.Alpha,35,_SEPARATOR),
					new AttributImport(i++,TypeEnum.Alpha,34,params.getDebtor().getIban()),
					new AttributImport(i++,TypeEnum.Alpha,11,params.getDebtor().getBic()),
					new AttributImport(i++,TypeEnum.Alpha,70,Util.controlSpecial(params.getUltimateDebtor().getName())),
					new AttributImport(i++,TypeEnum.Alpha,142,_SEPARATOR),
					new AttributImport(i++,TypeEnum.Alpha,35,params.getUltimateDebtor().getIban()),
					new AttributImport(i++,TypeEnum.Alpha,2,"1D"),
					new AttributImport(i++,TypeEnum.Alpha,6,params.getUltimateDebtor().getBic()),//CODIQUE
					new AttributImport(i++,TypeEnum.Alpha,1,"0"),
					new AttributImport(i++,TypeEnum.Alpha,26,Util.controlSpecial(getLibelle1(sct.getObject(),26))),
					new AttributImport(i++,TypeEnum.Numeric,17,Util.formatCurrencyWithoutSeparator(sct.getAmount())),					
					new AttributImport(i++,TypeEnum.Alpha,11,sct.getCreditor().getBic()),
					new AttributImport(i++,TypeEnum.Alpha,70,Util.controlSpecial(sct.getCreditor().getName())),
					new AttributImport(i++,TypeEnum.Alpha,142,_SEPARATOR),
					new AttributImport(i++,TypeEnum.Alpha,35,_SEPARATOR),// a verifier identifiant siret
					new AttributImport(i++,TypeEnum.Alpha,34,sct.getCreditor().getIban()),
					new AttributImport(i++,TypeEnum.Alpha,70,Util.controlSpecial(sct.getCreditor().getName())),
					new AttributImport(i++,TypeEnum.Alpha,142,_SEPARATOR),
					new AttributImport(i++,TypeEnum.Alpha,35,_SEPARATOR),// a verifier identifiant siret
					new AttributImport(i++,TypeEnum.Alpha,35,_SEPARATOR),
					new AttributImport(i++,TypeEnum.Alpha,3,_SEPARATOR),
					new AttributImport(i++,TypeEnum.Alpha,140,Util.controlSpecial(sct.getObject())),
					new AttributImport(i++,TypeEnum.Alpha,32,_SEPARATOR),
					new AttributImport(i++,TypeEnum.Alpha,1," "),
					new AttributImport(i++,TypeEnum.Alpha,2,"04"),
					
			};
			return attributs;
		}
	};
}

private IFlatLine createFooter(Protocol params, int count, BigDecimal totalAmount,Date dateReglement) {
return new IFlatLine() {
		
		@Override
		public AttributImport[] getAttributs() {
			int i=0;
			AttributImport[] attributs={
					new AttributImport(i++,TypeEnum.Alpha,15,"PAIN.001.001.02"),
					new AttributImport(i++,TypeEnum.Alpha,4,_SEPARATOR),
					new AttributImport(i++,TypeEnum.Alpha,10,Util.formatDate(dateReglement,DATE_FORMAT)),
					new AttributImport(i++,TypeEnum.Alpha,70,Util.controlSpecial(params.getDebtor().getName())),
					new AttributImport(i++,TypeEnum.Alpha,222,_SEPARATOR),					
					new AttributImport(i++,TypeEnum.Alpha,70,Util.controlSpecial(params.getUltimateDebtor().getName())),
					new AttributImport(i++,TypeEnum.Alpha,177,_SEPARATOR),
					new AttributImport(i++,TypeEnum.Alpha,2,"1D"),
					new AttributImport(i++,TypeEnum.Alpha,6,params.getUltimateDebtor().getBic()),//CODIQUE
					new AttributImport(i++,TypeEnum.Alpha,1,"0"),					
					new AttributImport(i++,TypeEnum.Numeric,8,""+count),
					new AttributImport(i++,TypeEnum.Alpha,18,_SEPARATOR),
					new AttributImport(i++,TypeEnum.Numeric,17,Util.formatCurrencyWithoutSeparator(totalAmount)),
					new AttributImport(i++,TypeEnum.Alpha,750,_SEPARATOR),
					new AttributImport(i++,TypeEnum.Alpha,2,"09"),
					
			};
			return attributs;
		}
	};
}
public IFlatLine getHeaderLine() {
	return headerLine;
}
public void setHeaderLine(IFlatLine headerLine) {
	this.headerLine = headerLine;
}
public List<IFlatLine> getBody() {
	return body;
}
public void setBody(List<IFlatLine> body) {
	this.body = body;
}
public IFlatLine getFooterLine() {
	return footerLine;
}
public void setFooterLine(IFlatLine footerLine) {
	this.footerLine = footerLine;
}
private String getRefTechnique(String idTransfert, Date dateEchange) {
	StringBuilder strBuilder=new StringBuilder(idTransfert);
	strBuilder.append("-DFT-SCT-");
	strBuilder.append(String.valueOf(Util.getYear(dateEchange)).substring(2));
	int quantieme=Util.getDayOfYear(dateEchange);
	strBuilder.append(quantieme<100?"0"+quantieme:""+quantieme);
	strBuilder.append("-001");
	return strBuilder.toString();
}
protected String getRefMetier() {
	//BORDEREAU 2020/15
	return "";
}
protected String getLibelle1(String libelle, int size) {
	if(libelle==null)return "";
	return (libelle.length()>size?libelle.substring(0, size-1):libelle);
}
}
