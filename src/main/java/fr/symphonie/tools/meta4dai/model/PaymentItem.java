package fr.symphonie.tools.meta4dai.model;

import java.math.BigDecimal;

import javax.persistence.Transient;

import fr.symphonie.util.model.Trace;

public class PaymentItem {
	private Integer exercice;
	private String budget;
	private String period;
	private Integer noEI;
	private String objetEI;
	private BigDecimal amount;
	private Integer noDAI;
	private String objetDAI;
	private Integer noLBI;
	private Integer noLMI;
	private Integer noLBC;
	private Trace trace;
	
	private BigDecimal dispoEi;
	@Transient
	private BigDecimal dispoLMI;
	private BigDecimal dispoApres;
	@Transient
	private LbData lb;
	@Transient
	private boolean notFoundEI;
	@Transient
	private boolean addEiRequest;
	
	
	public PaymentItem() {
		super();
		this.lb=new LbData();
		this.notFoundEI=false;
		this.addEiRequest=false;
	}
	public PaymentItem(ImportedData data) {
		this();
		this.exercice=data.getExercice();
		this.period=""+data.getPeriod();
		this.noEI=data.getNumeroEI();
		this.amount=data.getAmount();
	}
	public Integer getExercice() {
		return exercice;
	}
	public void setExercice(Integer exercice) {
		this.exercice = exercice;
	}
	
	public String getBudget() {
		return budget;
	}
	public void setBudget(String budget) {
		this.budget = budget;
	}
	public String getPeriod() {
		return period;
	}
	public void setPeriod(String period) {
		this.period = period;
	}
	public Integer getNoEI() {
		return noEI;
	}
	public void setNoEI(Integer noEI) {
		this.noEI = noEI;
	}
	public String getObjetEI() {
		return objetEI;
	}
	public void setObjetEI(String objetEI) {
		this.objetEI = objetEI;
	}
	public BigDecimal getAmount() {
		return amount;
	}
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
	public Integer getNoDAI() {
		return noDAI;
	}
	public void setNoDAI(Integer noDAI) {
		this.noDAI = noDAI;
	}
	public String getObjetDAI() {
		return objetDAI;
	}
	public void setObjetDAI(String objetDAI) {
		this.objetDAI = objetDAI;
	}
	public Integer getNoLBI() {
		return noLBI;
	}
	public void setNoLBI(Integer noLBI) {
		this.noLBI = noLBI;
	}
	public Integer getNoLMI() {
		return noLMI;
	}
	public void setNoLMI(Integer noLMI) {
		this.noLMI = noLMI;
	}
	public Integer getNoLBC() {
		return noLBC;
	}
	public void setNoLBC(Integer noLBC) {
		this.noLBC = noLBC;
	}
	public Trace getTrace() {
		return trace;
	}
	public void setTrace(Trace trace) {
		this.trace = trace;
	}
	public BigDecimal getDispoEi() {
		return dispoEi;
	}
	public void setDispoEi(BigDecimal dispoEi) {
		this.dispoEi = dispoEi;
	}
	public BigDecimal getDispoLMI() {
		return dispoLMI;
	}
	public void setDispoLMI(BigDecimal dispoLMI) {
		this.dispoLMI = dispoLMI;
	}
	public BigDecimal getDispoApres() {
		return dispoApres;
	}
	public void setDispoApres(BigDecimal dispoApres) {
		this.dispoApres = dispoApres;
	}
	public LbData getLb() {
		return lb;
	}
	public void setLb(LbData lb) {
		this.lb = lb;
	}
	public boolean isNotFoundEI() {
		return notFoundEI;
	}
	public void setNotFoundEI(boolean notFoundEI) {
		this.notFoundEI = notFoundEI;
	}
	public boolean isAddEiRequest() {
		return addEiRequest;
	}
	public void setAddEiRequest(boolean addEiRequest) {
		this.addEiRequest = addEiRequest;
	}
	@Override
	public String toString() {
		return "PaymentItem [exercice=" + exercice + ", budget=" + budget + ", period=" + period + ", noEI=" + noEI
				+ ", amount=" + amount + "]";
	}
	
	public boolean isNonFonctNature() {
		return !getLb().getNature().equalsIgnoreCase("F");
	}
	public boolean isInsufficientAvailable() {
		return getDispoApres().signum()<0;
	}
	

}
