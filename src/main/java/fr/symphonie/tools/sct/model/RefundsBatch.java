package fr.symphonie.tools.sct.model;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.persistence.Transient;

import fr.symphonie.util.model.Trace;

public class RefundsBatch {
	private Integer id;
	private Integer exercice;
	private Integer numero;
	private String fileName;
	private long crc32;
	private Trace trace;
	@Transient
	private String status;
	@Transient
	private RefundsItem sepaFr;
	@Transient
	private RefundsItem horsSepaFr;
	@Transient
	private RefundsItem horsSepa;
	@Transient
	private BigDecimal total;
	
	
	public RefundsBatch() {
		super();
		setSepaFr(new RefundsItem());
		setHorsSepaFr(new RefundsItem());
		setHorsSepa(new RefundsItem());
	}
	
	public RefundsBatch(Integer exercice, Integer numero) {
		this();
		this.exercice = exercice;
		this.numero = numero;
	}
	
	public RefundsBatch(Integer exercice, Integer numero, String fileName, long crc32) {
		this(exercice,numero);
		this.fileName = fileName;
		this.crc32 = crc32;
	}

	public Integer getExercice() {
		return exercice;
	}
	public void setExercice(Integer exercice) {
		this.exercice = exercice;
	}
	public Integer getNumero() {
		return numero;
	}
	public void setNumero(Integer numero) {
		this.numero = numero;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public RefundsItem getSepaFr() {
		return sepaFr;
	}
	public void setSepaFr(RefundsItem sepaFr) {
		this.sepaFr = sepaFr;
	}
	public RefundsItem getHorsSepaFr() {
		return horsSepaFr;
	}
	public void setHorsSepaFr(RefundsItem horsSepaFr) {
		this.horsSepaFr = horsSepaFr;
	}
	public RefundsItem getHorsSepa() {
		return horsSepa;
	}
	public void setHorsSepa(RefundsItem horsSepa) {
		this.horsSepa = horsSepa;
	}
	public BigDecimal getTotal() {
		if(total==null) {
			BigDecimal tot=new BigDecimal(0);
			if(sepaFr.getAmount()!=null) tot=tot.add(sepaFr.getAmount());
			if(horsSepaFr.getAmount()!=null) tot=tot.add(horsSepaFr.getAmount());
			if(horsSepa.getAmount()!=null) tot=tot.add(horsSepa.getAmount());
			setTotal(tot);
		}
		return total;
	}
	public void setTotal(BigDecimal total) {
		this.total = total;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Trace getTrace() {
		return trace;
	}

	public void setTrace(Trace trace) {
		this.trace = trace;
	}

	public long getCrc32() {
		return crc32;
	}

	public void setCrc32(long crc32) {
		this.crc32 = crc32;
	}
	public List<RefundsItem> getItems(){
		return Stream.of(getSepaFr(),getHorsSepaFr(),getHorsSepa()).collect(Collectors.toList());
	}
	
}
