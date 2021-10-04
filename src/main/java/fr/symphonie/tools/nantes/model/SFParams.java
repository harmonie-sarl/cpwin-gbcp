package fr.symphonie.tools.nantes.model;

import org.apache.commons.lang.StringUtils;

public class SFParams {
	private String compteProduit;
	private String codeGest;
	private Integer numeroEO;
	private String codePRM;
	
	public SFParams() {
		super();
	}
	public SFParams( String codeGest, Integer numeroEO, String codePRM) {
		this();
		this.codeGest = codeGest;
		this.numeroEO = numeroEO;
		this.codePRM = codePRM;
	}
	public String getCompteProduit() {
		return compteProduit;
	}
	public void setCompteProduit(String compteProduit) {
		this.compteProduit = compteProduit;
	}
	public String getCodeGest() {
		return codeGest;
	}
	public void setCodeGest(String codeGest) {
		this.codeGest = codeGest;
	}
	public Integer getNumeroEO() {
		return numeroEO;
	}
	public void setNumeroEO(Integer numeroEO) {
		this.numeroEO = numeroEO;
	}
	public String getCodePRM() {
		return codePRM;
	}
	public void setCodePRM(String codePRM) {
		this.codePRM = codePRM;
	}
	public boolean isValid() {
		if(StringUtils.isBlank(codeGest)) return false;
		if(StringUtils.isBlank(codePRM)) return false;
//		if(StringUtils.isBlank(compteProduit)) return false;
		if(numeroEO==null)return false;
		return true;
	}

}
