package fr.symphonie.tools.signature.model;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

import javax.persistence.Transient;

import fr.symphonie.util.model.Trace;

public class Signature {
//	private static final Logger logger=LoggerFactory.getLogger("signature-tools");
	private Long id;
	private String codeUtil;
	private Date startDate;
	private Date endDate;
	private String function;
	private byte[] signatureData;
	private boolean sf;
	private boolean dp;
	private boolean dr;
	private boolean dv;
	private boolean lrec;
	private boolean ar;
	private boolean da;
	private boolean liq;
	private boolean liqOr;
	private boolean ej;
	private boolean vsf;
	private boolean op;
	private boolean asf;
	private boolean mdt;
	private boolean tr;
	
	private BigDecimal seuilLrec;
	private BigDecimal seuilAr;
	private BigDecimal seuilDa;
	private BigDecimal seuilEj;
	private BigDecimal seuilAsf;
	private BigDecimal seuilVsf;
	private BigDecimal seuilLiq;
	private BigDecimal seuilOr;
	private BigDecimal seuilOp;
	private BigDecimal seuilMdt;
	private BigDecimal seuilTr;
	
	
	public BigDecimal getSeuilLrec() {
		return seuilLrec;
	}

	public void setSeuilLrec(BigDecimal seuilLrec) {
		executeUpdate(this.seuilLrec,seuilLrec);
		this.seuilLrec = seuilLrec;
	}

	public BigDecimal getSeuilAr() {
		return seuilAr;
	}

	public void setSeuilAr(BigDecimal seuilAr) {
		executeUpdate(this.seuilAr,seuilAr);
		this.seuilAr = seuilAr;
	}

	public BigDecimal getSeuilDa() {
		return seuilDa;
	}

	public void setSeuilDa(BigDecimal seuilDa) {
		executeUpdate(this.seuilDa,seuilDa);
		this.seuilDa = seuilDa;
	}

	public BigDecimal getSeuilEj() {
		return seuilEj;
	}

	public void setSeuilEj(BigDecimal seuilEj) {
		executeUpdate(this.seuilEj,seuilEj);
		this.seuilEj = seuilEj;
	}

	public BigDecimal getSeuilAsf() {
		return seuilAsf;
	}

	public void setSeuilAsf(BigDecimal seuilAsf) {
		executeUpdate(this.seuilAsf,seuilAsf);
		this.seuilAsf = seuilAsf;
	}

	public BigDecimal getSeuilVsf() {
		return seuilVsf;
	}

	public void setSeuilVsf(BigDecimal seuilVsf) {
		executeUpdate(this.seuilVsf,seuilVsf);
		this.seuilVsf = seuilVsf;
	}

	public BigDecimal getSeuilLiq() {
		return seuilLiq;
	}

	public void setSeuilLiq(BigDecimal seuilLiq) {
		executeUpdate(this.seuilLiq,seuilLiq);
		this.seuilLiq = seuilLiq;
	}

	public BigDecimal getSeuilOr() {
		return seuilOr;
	}

	public void setSeuilOr(BigDecimal seuilOr) {
		executeUpdate(this.seuilOr,seuilOr);
		this.seuilOr = seuilOr;
	}

	public BigDecimal getSeuilOp() {
		return seuilOp;
	}

	public void setSeuilOp(BigDecimal seuilOp) {
		executeUpdate(this.seuilOp,seuilOp);
		this.seuilOp = seuilOp;
	}

	private Trace trace;
	@Transient
	private boolean updated;

	public Signature() {
		super();
		setUpdated(false);
	}

	public String getCodeUtil() {
		return codeUtil;
	}

	public void setCodeUtil(String codeUtil) {
		executeUpdate(this.codeUtil,codeUtil);
		this.codeUtil = codeUtil;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		executeUpdate(this.startDate,startDate);
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		executeUpdate(this.endDate,endDate);
		this.endDate = endDate;
	}

	public String getFunction() {
		return function;
	}

	public void setFunction(String function) {
		executeUpdate(this.function,function);
		this.function = function;
	}

	public byte[] getSignatureData() {
		return signatureData;
	}

	public void setSignatureData(byte[] signatureData) {
		this.signatureData = signatureData;
		setUpdated(true);
	}

	public boolean isSf() {
		return sf;
	}

	public void setSf(boolean sf) {
		executeUpdate(this.sf,sf);
		this.sf = sf;	
	}

	public boolean isDp() {
		return dp;
	}

	public void setDp(boolean dp) {
		executeUpdate(this.dp,dp);
		this.dp = dp;		
	}

	public boolean isDr() {
		return dr;
	}

	public void setDr(boolean dr) {
		executeUpdate(this.dr,dr);
		this.dr = dr;
	}

	public boolean isDv() {
		return dv;
	}

	public void setDv(boolean dv) {
		executeUpdate(this.dv,dv);
		this.dv = dv;
	}

	public boolean isLrec() {
		return lrec;
	}

	public void setLrec(boolean lrec) {
		executeUpdate(this.lrec,lrec);
		this.lrec = lrec;
	}

	public boolean isAr() {
		return ar; 
	}

	public void setAr(boolean ar) {
		executeUpdate(this.ar,ar);
		this.ar = ar;
	}

	public Trace getTrace() {
		return trace;
	}

	public void setTrace(Trace trace) {
		this.trace = trace;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	public boolean getActif() {

		LocalDate toDayDate = LocalDate.now();
		LocalDate datefin = Instant.ofEpochMilli(getEndDate().getTime()).atZone(ZoneId.systemDefault()).toLocalDate();

		return toDayDate.isBefore(datefin) || toDayDate.isEqual(datefin);

	}

	public boolean isUpdated() {
		return updated;
	}

	public void setUpdated(boolean updated) {
		this.updated = updated;
	}
	private void executeUpdate(Object oldValue, Object newValue) {
		
		if(isUpdated())return;
		if(oldValue==null && newValue==null) return;
		if(oldValue==null && newValue!=null) setUpdated(true);		
		else if(!oldValue.equals(newValue)) setUpdated(true);
		
	}

	public boolean isDa() {
		return da;
	}

	public void setDa(boolean da) {
		executeUpdate(this.da,da);
		this.da = da;
	}
	public boolean isLiq() {
		return liq;
	}

	public void setLiq(boolean liq) {
		executeUpdate(this.liq,liq);
		this.liq = liq;
	}

	public boolean isEj() {
		return ej;
	}

	public void setEj(boolean ej) {
		executeUpdate(this.ej,ej);
		this.ej = ej;
	}

	public boolean isVsf() {
		return vsf;
	}

	public void setVsf(boolean vsf) {
		executeUpdate(this.vsf,vsf);
		this.vsf = vsf;
	}

	public boolean isOp() {
		return op;
	}

	public void setOp(boolean op) {
		executeUpdate(this.op,op);
		this.op = op;
	}

	public boolean isAsf() {
		return asf;
	}

	public void setAsf(boolean asf) {
		executeUpdate(this.asf,asf);
		this.asf = asf;
	}

	public boolean isLiqOr() {
		return liqOr;
	}

	public void setLiqOr(boolean liqOr) {
		executeUpdate(this.liqOr,liqOr);
		this.liqOr = liqOr;
	}

	
	
	public boolean isMdt() {
		return mdt;
	}

	public void setMdt(boolean mdt) {
		executeUpdate(this.mdt,mdt);
		this.mdt = mdt;
	}

	public boolean isTr() {
		return tr;
	}

	public void setTr(boolean tr) {
		executeUpdate(this.tr,tr);
		this.tr = tr;
	}

	public BigDecimal getSeuilMdt() {
		return seuilMdt;
	}

	public void setSeuilMdt(BigDecimal seuilMdt) {
		executeUpdate(this.seuilMdt,seuilMdt);
		this.seuilMdt = seuilMdt;
	}

	public BigDecimal getSeuilTr() {
		return seuilTr;
	}

	public void setSeuilTr(BigDecimal seuilTr) {
		executeUpdate(this.seuilTr,seuilTr);
		this.seuilTr = seuilTr;
	}


}
