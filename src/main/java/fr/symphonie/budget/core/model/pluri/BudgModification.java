package fr.symphonie.budget.core.model.pluri;

import java.util.Date;

import fr.symphonie.cpwin.model.Visa;
import fr.symphonie.cpwin.model.VisaEnum;
import fr.symphonie.util.Helper;
import fr.symphonie.util.model.Trace;

public class BudgModification  {
	private Integer exercice;
	private String type;
	private Integer numero;
	private String codeBudget;
	private String objet;
	private String etatCp;
	private Date dateModif;
	private Visa visaOrdo;
	private Visa visaCpt;
	private Visa visaCtrl;
	private Trace trace;
	public Integer getExercice() {
		return exercice;
	}
	public void setExercice(Integer exercice) {
		this.exercice = exercice;
	}
	public String getCodeBudget() {
		return codeBudget;
	}
	public void setCodeBudget(String codeBudget) {
		this.codeBudget = codeBudget;
	}
	public ModifEnum getType() {
		return ModifEnum.parse(type);
	}
	public void setType(ModifEnum type) {
		this.type = type.getCode();
	}
	public Integer getNumero() {
		return numero;
	}
	public void setNumero(Integer numero) {
		this.numero = numero;
	}
	public Visa getVisaOrdo() {
		return visaOrdo;
	}
	public void setVisaOrdo(Visa visaOrdo) {
		this.visaOrdo = visaOrdo;
	}
	public Visa getVisaCpt() {
		return visaCpt;
	}
	public void setVisaCpt(Visa visaCpt) {
		this.visaCpt = visaCpt;
	}
	public Visa getVisaCtrl() {
		return visaCtrl;
	}
	public void setVisaCtrl(Visa visaCtrl) {
		this.visaCtrl = visaCtrl;
	}
	public Trace getTrace() {
		return trace;
	}
	public void setTrace(Trace trace) {
		this.trace = trace;
	}
	public String getObjet() {
		return objet;
	}
	public void setObjet(String objet) {
		this.objet = objet;
	}
	public Date getDateModif() {
		return dateModif;
	}
	public void setDateModif(Date dateModif) {
		this.dateModif = dateModif;
	}
	
	
	public String getDescription(){
		
		String SEPARATOR="-";
		StringBuffer description=new StringBuffer();	
		description.append((this.getNumero()==null)?"":this.getNumero());
		description.append((this.getObjet()==null)?"":SEPARATOR+this.getObjet());
		description.append((this.getDateModif()!=null)?SEPARATOR+Helper.toSimpleFormat(this.getDateModif()):"");
		String returnString = description.toString();
		return returnString;
	}
	@Override
	public String toString()
	{
		return this.getDescription();
	}
	

//	 
	
	
	public boolean isOrdoAccept()
	{
		return isAccept(getVisaOrdo());
	}
	
	public boolean isCptAccept()
	{
		return isAccept(getVisaCpt());
	}
	public boolean isCtrlAccept()
	{
		return isAccept(getVisaCtrl());
	}
	
	 public boolean isAccept( Visa v ){

		 if((v!=null)&&(v.getVisa()!=null)&&(v.getVisa()==VisaEnum.Accepte))
			 return true;
		 return false;
	 }
	 public void setEtatCp(EtatEnum etatCp) {
			this.etatCp = etatCp.getEtat();
		}
		public EtatEnum getEtatCp() {
			return EtatEnum.parse(etatCp);
		}

	public boolean isEtatCpValide() {
		return EtatEnum.VALIDE == getEtatCp();
	}
	
	public boolean isEtatCpOuvert() {
		return EtatEnum.OUVERT == getEtatCp();
	}

}
