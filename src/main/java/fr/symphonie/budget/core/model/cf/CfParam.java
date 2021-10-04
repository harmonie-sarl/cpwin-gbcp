package fr.symphonie.budget.core.model.cf;

import javax.persistence.Transient;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



public class CfParam {
	private int exercice;
	private String codeBudget;
	private String type;
	private String codeCompte;
	private String codeRef;
	private String codeRefTab6;
	@Transient
	private String libCompte;
	@Transient
	private CfReference reference;
	
	private static final Logger logger = LoggerFactory.getLogger(CfParam.class);
	
	public CfParam() {
		super();
		reference=new CfReference();
	}
	public CfParam(int exercice, String codeBudget, String type, String codeCompte, String libCompte) {
		this();
		this.exercice = exercice;
		this.codeBudget = codeBudget;
		this.type = type;
		this.codeCompte = codeCompte;
		this.libCompte = libCompte;
	}
	public int getExercice() {
		return exercice;
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
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getCodeCompte() {
		return codeCompte;
	}
	public void setCodeCompte(String codeCompte) {
		this.codeCompte = codeCompte;
	}
	public String getLibCompte() {
		return libCompte;
	}
	public void setLibCompte(String libCompte) {
		this.libCompte = libCompte;
	}
	public CfReference getReference() {
		return reference;
	}
	public void setReference(CfReference reference) {
//		logger.info("	public void setReference(CfReference "+reference+")");
		this.reference = reference;
		if(reference!=null)setCodeRef(reference.getCode());
//		logger.info("	public void setReference(reference.getCode()= "+reference.getCode()+")");
	}
	public String getCodeRef() {
		return codeRef;
	}
	public void setCodeRef(String codeRef) {
		logger.info("	public void setCodeRef(String codeRef)  "+codeRef+")");
		this.codeRef = codeRef;
	}
	public String getCodeRefTab6() {
		return codeRefTab6;
	}
	public void setCodeRefTab6(String codeRefTab6) {
		this.codeRefTab6 = codeRefTab6;
	}
	
	
}
