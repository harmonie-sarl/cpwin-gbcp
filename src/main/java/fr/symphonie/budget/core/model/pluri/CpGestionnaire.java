package fr.symphonie.budget.core.model.pluri;

import java.math.BigDecimal;
import java.util.List;

import javax.persistence.Transient;

import fr.symphonie.common.util.Constant;
import fr.symphonie.util.Helper;

public class CpGestionnaire implements Cloneable {
	private int exerciceCP;
	private String codeBudget;	
	private String groupNat;
	private String gestionnaire;
	private BigDecimal budgetInitial=new BigDecimal(0);
	private double budgetRectificatif=0;
	private double creditOuvert=0;
	private double ejReserve;
	private double dispoEj;
	private double dpEmis;
	private double dpAdmis;
	private double dispoCo;
	private Integer noLbgCp;
	@Transient
	private String libelle;
	@Transient
	private double ejEnCours;
	@Transient
	private double ejPrecedent;
	@Transient
	private double montant;
	@Transient
	private BigDecimal varaitionBR;
	@Transient
	private String libelleGroupNat;
	@Transient
	private double cumuleBrAvant;
	@Transient
	private double montantTemp;
	@Transient
	private List<BudgetCpDest> ventByDest=null;
	public CpGestionnaire() {
		super();
	}
	public CpGestionnaire(int exerciceCP, String codeBudget, String groupNat) {
		this();
		this.exerciceCP = exerciceCP;
		this.codeBudget = codeBudget;
		this.groupNat = groupNat;
	}
	public CpGestionnaire(int exerciceCP, String codeBudget, String groupNat,String libelleGroupNat, String gestionnaire) {
		this();
		this.exerciceCP = exerciceCP;
		this.codeBudget = codeBudget;
		this.groupNat = groupNat;
		this.libelleGroupNat=libelleGroupNat;
		this.gestionnaire = gestionnaire;
		
	}
	public int getExerciceCP() {
		return exerciceCP;
	}
	public void setExerciceCP(int exerciceCP) {
		this.exerciceCP = exerciceCP;
	}
	public String getCodeBudget() {
		return codeBudget;
	}
	public void setCodeBudget(String codeBudget) {
		this.codeBudget = codeBudget;
	}
	public String getGroupNat() {
		return groupNat;
	}
	public void setGroupNat(String groupNat) {
		this.groupNat = groupNat;
	}
	
	public BigDecimal getBudgetInitial() {
		return budgetInitial;
	}
	public void setBudgetInitial(BigDecimal budgetInitial) {
		this.budgetInitial = budgetInitial;
	}
	public double getBudgetRectificatif() {
		return budgetRectificatif;
	}
	public void setBudgetRectificatif(double budgetRectificatif) {
		this.budgetRectificatif = budgetRectificatif;
	}
	public double getCreditOuvert() {
		return creditOuvert;
	}
	public void setCreditOuvert(double creditOuvert) {
		this.creditOuvert = creditOuvert;
	}
	public double getEjReserve() {
		return ejReserve;
	}
	public void setEjReserve(double ejReserve) {
		this.ejReserve = ejReserve;
	}
	public double getDispoEj() {
		return dispoEj;
	}
	public void setDispoEj(double dispoEj) {
		this.dispoEj = dispoEj;
	}
	public double getDpEmis() {
		return dpEmis;
	}
	public void setDpEmis(double dpEmis) {
		this.dpEmis = dpEmis;
	}
	public double getDpAdmis() {
		return dpAdmis;
	}
	public void setDpAdmis(double dpAdmis) {
		this.dpAdmis = dpAdmis;
	}
	public double getDispoCo() {
		return dispoCo;
	}
	public void setDispoCo(double dispoCo) {
		this.dispoCo = dispoCo;
	}
	public Integer getNoLbgCp() {
		return noLbgCp;
	}
	public void setNoLbgCp(Integer noLbgCp) {
		this.noLbgCp = noLbgCp;
	}
	public void setGestionnaire(String gestionnaire) {
		this.gestionnaire = gestionnaire;
	}
	public String getGestionnaire() {
		return gestionnaire;
	}
	public void setLibelle(String libelle) {
		this.libelle = libelle;
	}
	public String getLibelle() {
		return libelle;
	}
	public String getCodeAndLib() {
		if((getGestionnaire()==null)||(getGestionnaire().trim().isEmpty()))return "";
		return getGestionnaire()+"-"+getLibelle();
	}
	public void setEjEnCours(double coExerciceEnCours) {
		this.ejEnCours = coExerciceEnCours;
	}
	public double getEjEnCours() {
		return ejEnCours;
	}
	public void setEjPrecedent(double coExercicePrecedent) {
		this.ejPrecedent = coExercicePrecedent;
	}
	public double getEjPrecedent() {
		return ejPrecedent;
	}
	public double getTotalReserve(){
		return getEjEnCours()+getEjPrecedent();
	}

	public double getReste(){
		return	Helper.round(getCreditOuvertTemp()-getTotalReserve(), 2);
	}
	public double getDisponible(){
		return	Helper.round(getCreditOuvert()+getMontantTemp()-getDpAdmis(), 2);
	}
	public void setMontant(double montant) {
		this.montant = montant;
	}
	public double getMontant() {
		return montant;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((codeBudget == null) ? 0 : codeBudget.hashCode());
		result = prime * result + exerciceCP;
		result = prime * result
				+ ((gestionnaire == null) ? 0 : gestionnaire.hashCode());
		result = prime * result
				+ ((groupNat == null) ? 0 : groupNat.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof CpGestionnaire)) {
			return false;
		}
		CpGestionnaire other = (CpGestionnaire) obj;
		if (codeBudget == null) {
			if (other.codeBudget != null) {
				return false;
			}
		} else if (!codeBudget.equals(other.codeBudget)) {
			return false;
		}
		if (exerciceCP != other.exerciceCP) {
			return false;
		}
		if (gestionnaire == null) {
			if (other.gestionnaire != null) {
				return false;
			}
		} else if (!gestionnaire.equals(other.gestionnaire)) {
			return false;
		}
		if (groupNat == null) {
			if (other.groupNat != null) {
				return false;
			}
		} else if (!groupNat.equals(other.groupNat)) {
			return false;
		}
		return true;
	}
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("CpGestionnaire [exerciceCP=");
		builder.append(exerciceCP);
		builder.append(", codeBudget=");
		builder.append(codeBudget);
		builder.append(", groupNat=");
		builder.append(groupNat);
		builder.append(", gestionnaire=");
		builder.append(gestionnaire);
		builder.append(", variation=");
		builder.append(getVaraitionBR());
		builder.append("]");
		return builder.toString();
	}
		
	/**
	 * @return the creditOuvertTemp
	 */
	public double getCreditOuvertTemp() {
		return getCreditOuvert()+getMontantTemp();
	}
	public void setVaraitionBR(BigDecimal varaitionBR) {
		this.varaitionBR = varaitionBR;
	}
	public BigDecimal getVaraitionBR() {
		if(varaitionBR==null)varaitionBR= new BigDecimal(getBudgetRectificatif());
		return varaitionBR;
	}
	
	public double getCoApresBR()
	{
		//return getCreditOuvert()+((getVaraitionBR()!=null)?getVaraitionBR().doubleValue():0d);
		return getCoAvant()+((getVaraitionBR()!=null)?getVaraitionBR().doubleValue():0d);
	}
	public void setLibelleGroupNat(String libelleGroupNat) {
		this.libelleGroupNat = libelleGroupNat;
	}
	public String getLibelleGroupNat() {
		return libelleGroupNat;
	}
	public String getCodeAndLibGroupNat() {
		if((getGroupNat()==null)||(getGroupNat().trim().isEmpty()))return "";
		return getGroupNat()+"-"+getLibelleGroupNat();
	}
	public boolean isVariationBrEqualZero(){
		if(getVaraitionBR()==null)return false;
		if(getVaraitionBR().equals(BigDecimal.ZERO))return true;
		return false;
	}
	public boolean isReserve(){
		return getGestionnaire().equalsIgnoreCase(Constant.RESERVE);
	}
	@Override
	public CpGestionnaire clone()  {
		CpGestionnaire obj=null;
		try {
			obj= (CpGestionnaire) super.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		return obj;
	}
	public double getTotalAeRap(){
		return getDisponible()+getEjPrecedent();
		
		
	}
	public double getCoAvant() {
		return getBudgetInitial().doubleValue()+getCumuleBrAvant();
	}
//	public void setCoAvant(double coAvant) {
//		this.coAvant = coAvant;
//	}
	public double getCumuleBrAvant() {
		return cumuleBrAvant;
	}
	public void setCumuleBrAvant(double cumuleBrAvant) {
		this.cumuleBrAvant = cumuleBrAvant;
	}
	public double getMontantTemp() {
		return montantTemp;
	}
	public void setMontantTemp(double montantTemp) {
		this.montantTemp = montantTemp;
	}
	public double getMontant(String natGrp){
		double result=0;
		if((natGrp!=null)){
			result=(natGrp.equals(getGroupNat()))?getMontant():0;
		}
		return result;
	}
	public List<BudgetCpDest> getVentByDest() {
		return ventByDest;
	}
	public void setVentByDest(List<BudgetCpDest> ventByDest) {
		this.ventByDest = ventByDest;
	}
	
}
