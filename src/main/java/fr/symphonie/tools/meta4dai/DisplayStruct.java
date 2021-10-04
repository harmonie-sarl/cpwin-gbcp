package fr.symphonie.tools.meta4dai;

import java.math.BigDecimal;

public class DisplayStruct {
	private String codeDest; 
	private String codeNature;
	private String codeDirection;
	private String codeService;
	private String codeProg;
	private String libProg;
	private Integer noEi;
	private String libEi;
	private BigDecimal mtTot;
	private BigDecimal mtPaie;
	private BigDecimal mtAutre;
	private BigDecimal dispoEi;
	
	
	public DisplayStruct() {
		super();
	}
	public DisplayStruct(String codeDest, String codeNature, String codeService, String codeProg, String libProg,
			Integer noEi, String libEi, BigDecimal mtTot, BigDecimal mtPaie, BigDecimal mtAutre, BigDecimal dispoEi) {
		this();
		this.codeDest = codeDest;
		this.codeNature = codeNature;
		this.codeService = codeService;
		this.codeProg = codeProg;
		this.libProg = libProg;
		this.noEi = noEi;
		this.libEi = libEi;
		this.mtTot = mtTot;
		this.mtPaie = mtPaie;
		this.mtAutre = mtAutre;
		this.dispoEi = dispoEi;
	}
	public String getCodeDest() {
		return codeDest;
	}
	public void setCodeDest(String codeDest) {
		this.codeDest = codeDest;
	}
	public String getCodeNature() {
		return codeNature;
	}
	public void setCodeNature(String codeNature) {
		this.codeNature = codeNature;
	}
	public String getCodeService() {
		return codeService;
	}
	public void setCodeService(String codeService) {
		this.codeService = codeService;
	}
	public String getCodeProg() {
		return codeProg;
	}
	public void setCodeProg(String codeProg) {
		this.codeProg = codeProg;
	}
	public String getLibProg() {
		return libProg;
	}
	public void setLibProg(String libProg) {
		this.libProg = libProg;
	}
	public Integer getNoEi() {
		return noEi;
	}
	public void setNoEi(Integer noEi) {
		this.noEi = noEi;
	}
	public String getLibEi() {
		return libEi;
	}
	public void setLibEi(String libEi) {
		this.libEi = libEi;
	}
	public BigDecimal getMtTot() {
		return mtTot;
	}
	public void setMtTot(BigDecimal mtTot) {
		this.mtTot = mtTot;
	}
	public BigDecimal getMtPaie() {
		return mtPaie;
	}
	public void setMtPaie(BigDecimal mtPaie) {
		this.mtPaie = mtPaie;
	}
	public BigDecimal getMtAutre() {
		return mtAutre;
	}
	public void setMtAutre(BigDecimal mtAutre) {
		this.mtAutre = mtAutre;
	}
	public BigDecimal getDispoEi() {
		return dispoEi;
	}
	public void setDispoEi(BigDecimal dispoEi) {
		this.dispoEi = dispoEi;
	}
	public String getCodeDirection() {
		return codeDirection;
	}
	public void setCodeDirection(String codeDirection) {
		this.codeDirection = codeDirection;
	}
	
	

}
