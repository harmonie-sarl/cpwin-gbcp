package fr.symphonie.budget.core.model.demat;

import javax.persistence.Transient;

import fr.symphonie.util.model.Trace;

public class Export implements Comparable<Export> {
	private int exercice;
	private String codeBudget;
	private String code;	
	private String status;
	
	private Trace trace;
	@Transient
	private ExportInfo info;
//	@Transient
//	private String libelle;
//	@Transient
//	private Integer order;
	
	public int getExercice() {
		return exercice;
	}
	public Export() {
		super();
		
	}
	
	public Export(int exercice, String codeBudget, String code) {
		super();
		this.exercice = exercice;
		this.codeBudget = codeBudget;
		this.code = code;
		
	}
	public void setExercice(int exercice) {
		this.exercice = exercice;
	}
	public String getCodeBudget() {
		return codeBudget;
	}
	public void setCodeBudget(String codeBudget) {
		this.codeBudget = codeBudget;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public StatusEnum getStatus() {
		return StatusEnum.parse(status);
	}
	public void setStatus(StatusEnum status) {
		this.status = status.getStatus();
	}
	public Trace getTrace() {
		return trace;
	}
	public void setTrace(Trace trace) {
		this.trace = trace;
	}
	public String getLibelle() {
		if(getInfo()!=null)
		return getInfo().getLibelle();
		return null;
	}
//	public void setLibelle(String libelle) {
//		this.libelle = libelle;
//	}
	public Integer getOrder() {
		if(getInfo()!=null)
		return getInfo().getOrder();
		return null;
	}
//	public void setOrder(Integer order) {
//		this.order = order;
//	}
	public String getCodeAndLibelle() {
		StringBuilder s = new StringBuilder();
		if (code != null) {
			s.append(code);
			if (getLibelle() != null) {
				s.append(" - ");
				s.append(getLibelle());
			}
		} else {
			if (getLibelle() != null) {
				s.append(getLibelle());
			}
		}
		return s.toString();
	}
	@Override
	public int compareTo(Export o) {
		if(o==null)return 0;
		return this.getOrder().compareTo(o.getOrder());
	}
	public boolean isValide() {
		if(getStatus()==null)return false;
		return (StatusEnum.VALIDE==getStatus())||(StatusEnum.TRAITE==getStatus());
	}
	@Override
	public String toString() {
		return "Export [exercice=" + exercice + ", code=" + code + ", status=" + status + "]";
	}
	
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((code == null) ? 0 : code.hashCode());
		result = prime * result + ((codeBudget == null) ? 0 : codeBudget.hashCode());
		result = prime * result + exercice;
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Export other = (Export) obj;
		if (code == null) {
			if (other.code != null)
				return false;
		} else if (!code.equals(other.code))
			return false;
		if (codeBudget == null) {
			if (other.codeBudget != null)
				return false;
		} else if (!codeBudget.equals(other.codeBudget))
			return false;
		if (exercice != other.exercice)
			return false;
		return true;
	}
	public ExportInfo getInfo() {
		return info;
	}
	public void setInfo(ExportInfo info) {
		this.info = info;
	}
	
}
