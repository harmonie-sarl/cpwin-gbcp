package fr.symphonie.budget.core.model.plt;

import javax.persistence.Transient;

import fr.symphonie.cpwin.model.Tiers;
import fr.symphonie.util.model.Trace;

public class ParamCompteLtr {
	private Integer exercice;
	/**
	 * Le code compte
	 */
	private String code;
	private String codeTiers;
	private Integer noLtrDep;
	private Integer noLtrRec;
	private Trace trace;
	
	@Transient
	private String libCompte;
	@Transient
	private String libTiers;
	
	@Transient
	private String libLtrDep;
	@Transient
	private String libLtrRec;
	@Transient
	private Tiers tiers;
	public String getCode() {
		return code;
	}
	public void setCode(String codeCompte) {
		this.code = codeCompte;
	}
	public String getCodeTiers() {
		return codeTiers;
	}
	public void setCodeTiers(String codeTiers) {
		this.codeTiers = codeTiers;
	}
	public Integer getNoLtrDep() {
		return noLtrDep;
	}
	public void setNoLtrDep(Integer noLtrDep) {
		this.noLtrDep = noLtrDep;
	}
	public Integer getNoLtrRec() {
		return noLtrRec;
	}
	public void setNoLtrRec(Integer noLtrRec) {
		this.noLtrRec = noLtrRec;
	}
	public Trace getTrace() {
		return trace;
	}
	public void setTrace(Trace trace) {
		this.trace = trace;
	}
	public String getLibCompte() {
		return libCompte;
	}
	public void setLibCompte(String libCompte) {
		this.libCompte = libCompte;
	}
	public String getLibTiers() {
		return libTiers;
	}
	public void setLibTiers(String libTiers) {
		this.libTiers = libTiers;
	}
	public String getLibLtrDep() {
		return libLtrDep;
	}
	public void setLibLtrDep(String libLtrDep) {
		this.libLtrDep = libLtrDep;
	}
	public String getLibLtrRec() {
		return libLtrRec;
	}
	public void setLibLtrRec(String libLtrRec) {
		this.libLtrRec = libLtrRec;
	}
	
public String getCodeLibCompte(){
		
		String SEPARATOR="-";
		StringBuffer description=new StringBuffer();	
		description.append((this.code==null)?"":this.code);
		description.append((this.libCompte==null)?"":SEPARATOR+this.libCompte);		
		String returnString = description.toString();
		return returnString;
	}	

public String getCodeLibTiers(){
	
	String SEPARATOR="-";
	StringBuffer description=new StringBuffer();	
	description.append((this.codeTiers==null)?"":this.codeTiers);
	description.append((this.libTiers==null)?"":SEPARATOR+this.libTiers);		
	String returnString = description.toString();
	return returnString;
}	
	
public boolean isCodeComptNull() {
	return (getCode()==null)||
            (getCode().trim().isEmpty()); 
             
}
public boolean isCodeTiersNull() {
	return (getCodeTiers()==null)||
            (getCodeTiers().trim().isEmpty()); 
             
}

public boolean checkRequired() {
	if(isCodeComptNull())return false;
	return true;
}

public Integer getExercice() {
	return exercice;
}
public void setExercice(Integer exercice) {
	this.exercice = exercice;
}
@Override
public int hashCode() {
	final int prime = 31;
	int result = 1;
	result = prime * result + ((code == null) ? 0 : code.hashCode());
	result = prime * result + ((exercice == null) ? 0 : exercice.hashCode());
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
	ParamCompteLtr other = (ParamCompteLtr) obj;
	if (code == null) {
		if (other.code != null)
			return false;
	} else if (!code.equals(other.code))
		return false;
	if (exercice == null) {
		if (other.exercice != null)
			return false;
	} else if (!exercice.equals(other.exercice))
		return false;
	return true;
}
@Override
public String toString() {
	return "ParamCompteLtr [exercice=" + exercice + ", code=" + code + ", codeTiers=" + codeTiers + ", noLtrDep="
			+ noLtrDep + ", noLtrRec=" + noLtrRec + "]";
}
public Tiers getTiers() {
	return tiers;
}
public void setTiers(Tiers tiers) {
	this.tiers = tiers;
}


}
