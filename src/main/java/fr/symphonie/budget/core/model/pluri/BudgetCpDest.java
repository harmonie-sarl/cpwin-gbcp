package fr.symphonie.budget.core.model.pluri;

import java.math.BigDecimal;

import javax.persistence.Transient;




public class BudgetCpDest implements Comparable<BudgetCpDest> {
	//private static final Logger logger = LoggerFactory.getLogger(BudgetCpDest.class);
	private String codeBudget;
	private int exerciceCP;
	private String groupNat;
	private String destination;
	private int niveau;
	private BigDecimal montant=new BigDecimal(0);
	private double budgetRectificatif=0;
	private double creditOuvert=0;	
	@Transient
	private String libelle;
	@Transient
	private CreditPaiement cp;
	@Transient
	private double bi;
	@Transient
	private double totaleBrPrecedent;
	
	public BudgetCpDest(String codeBudget, int exerciceCP, String groupNat) {
		this();
		this.codeBudget = codeBudget;
		this.exerciceCP = exerciceCP;
		this.groupNat = groupNat;
	}
	public BudgetCpDest() {
		super();
	}
	/**
	 * @return the codeBudget
	 */
	public String getCodeBudget() {
		return codeBudget;
	}
	/**
	 * @param codeBudget the codeBudget to set
	 */
	public void setCodeBudget(String codeBudget) {
		this.codeBudget = codeBudget;
	}
	
	/**
	 * @return the groupNat
	 */
	public String getGroupNat() {
		return groupNat;
	}
	/**
	 * @param groupNat the groupNat to set
	 */
	public void setGroupNat(String groupNat) {
		this.groupNat = groupNat;
	}
	/**
	 * @return the destination
	 */
	public String getDestination() {
		return destination;
	}
	/**
	 * @param destination the destination to set
	 */
	public void setDestination(String destination) {
		this.destination = destination;
	}
	/**
	 * @return the montant
	 */
	public BigDecimal getMontant() {
		return montant;
	}
	/**
	 * @param montant the montant to set
	 */
	public void setMontant(BigDecimal montant) {
		this.montant = montant;
	}
	/**
	 * @return the libelle
	 */
	public String getLibelle() {
		return libelle;
	}
	/**
	 * @param libelle the libelle to set
	 */
	public void setLibelle(String libelle) {
		this.libelle = libelle;
	}
	
	public int getExerciceCP() {
		return exerciceCP;
	}
	public void setExerciceCP(int exerciceCP) {
		this.exerciceCP = exerciceCP;
	}

	public String getCodeAndLib() {
		if((getDestination()==null)||(getDestination().trim().isEmpty()))return "";
		return getDestination()+"-"+getLibelle();
	}
	public void setCp(CreditPaiement cp) {
		this.cp = cp;
	}
	public CreditPaiement getCp() {
		return cp;
	}
	public void setBudgetRectificatif(double budgetRectificatif) {
		this.budgetRectificatif = budgetRectificatif;
	}
	public double getBudgetRectificatif() {
		return budgetRectificatif;
	}
	public void setCreditOuvert(double creditOuvert) {
		this.creditOuvert = creditOuvert;
	}
	public double getCreditOuvert() {
		return creditOuvert;
	}
	
	@Override
	public int compareTo(BudgetCpDest o) {
		return String.valueOf(this.getDestination()).compareTo(String.valueOf(o.getDestination()));
	}
	public void setBudgetRec(BigDecimal budgetRec) {
		if(budgetRec==null)return;
		setBudgetRectificatif(budgetRec.doubleValue());
	}
	public BigDecimal getBudgetRec() {
		return   new BigDecimal(getBudgetRectificatif());
	
	}
	
	public boolean checkDest(String Dest)
	{
       return  getDestination().equalsIgnoreCase(Dest);
	}
	
	public int getInsertPosition(String codeDest1 ,String codeDest2 ,String codeDest3 ,String codeDest4 ,String codeDest5 )
	{
		int postion=-1;
		if(checkDest(codeDest1))postion=0;
		if(checkDest(codeDest2))postion=1;
		if(checkDest(codeDest3))postion=2;
		if(checkDest(codeDest4))postion=3;
		if(checkDest(codeDest5))postion=4;
	//	logger.debug("public int getCpInsertPosition(String "+codeDest1+" ,String "+codeDest2+" ,String "+codeDest3+" ,String "+codeDest4+" ,String "+codeDest5+" )");
		
	//	logger.debug("public int getCpInsertPosition(), postion="+postion);
		
		
		return postion;
	}
	public int getNiveau() {
		return niveau;
	}
	public void setNiveau(int niveau) {
		this.niveau = niveau;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((codeBudget == null) ? 0 : codeBudget.hashCode());
		result = prime * result + ((destination == null) ? 0 : destination.hashCode());
		result = prime * result + exerciceCP;
		result = prime * result + ((groupNat == null) ? 0 : groupNat.hashCode());
		result = prime * result + niveau;
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
		BudgetCpDest other = (BudgetCpDest) obj;
		if (codeBudget == null) {
			if (other.codeBudget != null)
				return false;
		} else if (!codeBudget.equals(other.codeBudget))
			return false;
		if (destination == null) {
			if (other.destination != null)
				return false;
		} else if (!destination.equals(other.destination))
			return false;
		if (exerciceCP != other.exerciceCP)
			return false;
		if (groupNat == null) {
			if (other.groupNat != null)
				return false;
		} else if (!groupNat.equals(other.groupNat))
			return false;
		if (niveau != other.niveau)
			return false;
		return true;
	}
	
	public BigDecimal getMontantCP(){
		if(0==getNiveau())return getMontant();
		return getBudgetRec();
	}
	public double getBi() {
		return bi;
	}
	public void setBi(double bi) {
		this.bi = bi;
	}
	public double getTotaleBrPrecedent() {
		return totaleBrPrecedent;
	}
	public void setTotaleBrPrecedent(double totaleBrPrecedent) {
		this.totaleBrPrecedent = totaleBrPrecedent;
	}
	public double getReste(){
		return getBi()+getTotaleBrPrecedent()+getBudgetRectificatif();
	}
	
}
