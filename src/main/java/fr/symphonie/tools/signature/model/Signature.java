package fr.symphonie.tools.signature.model;

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

}
