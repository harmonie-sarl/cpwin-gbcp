package fr.symphonie.tools.sct.model;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Transient;

import fr.symphonie.common.util.BudgetHelper;
import fr.symphonie.common.util.Util;
import fr.symphonie.cpwin.model.sepa.Actor;
import fr.symphonie.impor.AttributImport;
import fr.symphonie.impor.ConfigImport;
import fr.symphonie.impor.Importable;
import fr.symphonie.util.model.Trace;

public class RefundsItem implements Importable
{
	private Integer id;
	private Integer exercice;
	private Integer noVague;
	private String paymentMode;
	private Actor actor;	
	private BigDecimal amount;
	private Integer noEcriture;
	private Integer noBordereau;
	private Date dateBordereau;
	private String object;
	private Trace trace;

	private static ConfigImport configImport=null;
	
	public RefundsItem() {
		super();
		setAmount(BigDecimal.ZERO);
		setActor(new Actor());
	}
	
	public RefundsItem(String paymentMode) {
		this();
		this.paymentMode = paymentMode;
	}

	public RefundsItem(String paymentMode, Integer noEcriture, Integer noBordereau, Date dateBordereau) {
		this();
		this.paymentMode = paymentMode;
		this.noEcriture = noEcriture;
		this.noBordereau = noBordereau;
		this.dateBordereau = dateBordereau;
	}

	public String getPaymentMode() {
		return paymentMode;
	}
	public void setPaymentMode(String paymentMode) {
		this.paymentMode = paymentMode;
	}
	public BigDecimal getAmount() {
		return amount;
	}
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
	public Integer getNoEcriture() {
		return noEcriture;
	}
	public void setNoEcriture(Integer noEcriture) {
		this.noEcriture = noEcriture;
	}
	public Integer getNoBordereau() {
		return noBordereau;
	}
	public void setNoBordereau(Integer noBordereau) {
		this.noBordereau = noBordereau;
	}
	public Date getDateBordereau() {
		return dateBordereau;
	}
	public void setDateBordereau(Date dateBordereau) {
		this.dateBordereau = dateBordereau;
	}
	public String getObject() {
		return object;
	}
	public void setObject(String object) {
		this.object = object;
	}	
	public Actor getActor() {
		return actor;
	}
	public void setActor(Actor actor) {
		this.actor = actor;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getExercice() {
		return exercice;
	}
	public void setExercice(Integer exercice) {
		this.exercice = exercice;
	}
	public Integer getNoVague() {
		return noVague;
	}
	public void setNoVague(Integer noVague) {
		this.noVague = noVague;
	}
	public Trace getTrace() {
		return trace;
	}
	public void setTrace(Trace trace) {
		this.trace = trace;
	}
	
	@Override
	public void setValue(AttributImport attribut, Object value) {
		String strValue=null;
		BigDecimal bdValue=null;
		if(value instanceof String)
			strValue=(value!=null)?((String) value).trim():null;
			else if(value instanceof  BigDecimal)
				bdValue=(value!=null)?(BigDecimal) value:null;
		String attributName=attribut.getName();
		switch (attributName){

		case "NOM":			 
			getActor().setName(strValue);
			break;
		case "PRENOM":
			getActor().setFirstName(strValue);
			break;
		case "MONTANT":
			setAmount(bdValue);
			break;
		case "IBAN":
			if(strValue!=null) strValue=strValue.replaceAll(" ", "");
			getActor().setIban(Util.removeNotSpecialCharacters(strValue));
			break;
		case "BIC":
			if(strValue!=null) strValue=strValue.replaceAll(" ", "").toUpperCase();
			getActor().setBic(strValue);
			break;
		case "OBJET":
			setObject(Util.removeNotSpecialCharacters(strValue));			
			break;
//		case "MATRICULE":
//			getActor().setCode(strValue);			
//			break;

		}
		
	}
	@Override
	public ConfigImport getConfigImport() {
		if(configImport==null) configImport=BudgetHelper.loadConfigImport("sct.import.config");
		return configImport;
	}

	public PaymentModeEnum getDisplayedPaymentMode() {
		return PaymentModeEnum.valueOf(getPaymentMode());
	}

	@Override
	public void setRowNum(Integer rowNum) {
		this.rowNum=rowNum;
		
	}
	@Override
	public Integer getRowNum() {
		return getConfigImport().getStartedRowIndex()+rowNum;
	}
	@Transient
	private Integer rowNum;

	@Override
	public String toString() {
		return "RefundsItem [actor=" + actor + ", object=" + object + ",amount="+amount+"]";
	}
	
	

}
