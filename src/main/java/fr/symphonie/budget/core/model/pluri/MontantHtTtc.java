package fr.symphonie.budget.core.model.pluri;

import java.math.BigDecimal;

public class MontantHtTtc {
	private BigDecimal ht;
	private BigDecimal ttc;

	public MontantHtTtc() {
		this(BigDecimal.ZERO,BigDecimal.ZERO);
	}

	public MontantHtTtc(BigDecimal ht, BigDecimal ttc) {
		super();
		this.ht = ht;
		this.ttc = ttc;
	}
	public MontantHtTtc(double ht, double ttc) {
		this( new BigDecimal( ht), new BigDecimal( ttc));
	}

	public BigDecimal getHt() {
		return ht;
	}

	public void setHt(BigDecimal ht) {
		this.ht = ht;
	}

	public BigDecimal getTtc() {
		return ttc;
	}

	public void setTtc(BigDecimal ttc) {
		this.ttc = ttc;
	}
	
	public void setTtcDouble(double ttc) {
		this.ttc = new BigDecimal( ttc);
//		this.ttc = ttc;
	}
	public void setHtDouble(double ht) {
		this.ht =  new BigDecimal( ht);
//		this.ht = ht;
	}
}
