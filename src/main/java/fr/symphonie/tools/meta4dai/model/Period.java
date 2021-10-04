package fr.symphonie.tools.meta4dai.model;

import java.math.BigDecimal;

import fr.symphonie.util.model.Trace;

public class Period {
	private Integer exercice;
	private String budget;
	private String code;
	private String etat;
	private String object;
	private BigDecimal total;
	private Trace trace;
	public Integer getExercice() {
		return exercice;
	}
	public void setExercice(Integer exercice) {
		this.exercice = exercice;
	}

	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getBudget() {
		return budget;
	}
	public void setBudget(String budget) {
		this.budget = budget;
	}
	public STATUS getEtat() {
		return STATUS.parse(etat);
	}
	
	public void setEtat(STATUS etat) {
		String result=etat==null?null:etat.getStatut();
		this.etat = result;
	}
	public BigDecimal getTotal() {
		return total;
	}
	public void setTotal(BigDecimal total) {
		this.total = total;
	}
	public Trace getTrace() {
		return trace;
	}
	public void setTrace(Trace trace) {
		this.trace = trace;
	}
	
	public String getObject() {
		return object;
	}
	public void setObject(String object) {
		this.object = object;
	}
	public boolean isTraite()
	{
		return STATUS.TRAITE==getEtat();
	}
	
	public boolean isCharge()
	{
		return STATUS.ENCOURS==getEtat();
	}
	
	public boolean isOuvert()
	{
		return STATUS.OUVERT==getEtat();
	}
	public boolean isAuMoinCharge() {
		STATUS etat=getEtat();
	    if(etat==null) return false;	    
		return etat.getOrdre()>=STATUS.ENCOURS.getOrdre();
	}
	public boolean isAuPlusCharge() {
		STATUS etat=getEtat();
	    if(etat==null) return false;	    
		return etat.getOrdre()<=STATUS.ENCOURS.getOrdre();
	}
	public enum STATUS{
		OUVERT(1,"N","Non traitée"),
		ENCOURS(2,"E","En cours"),
		TRAITE(3,"V","Traitée et validée");
		private int ordre;
		private String statut;	
		private String libelle;
		STATUS(int ordre,String value,String lib){
			this.ordre=ordre;
			this.statut=value;
			this.libelle=lib;
			
		}
		
		public int getOrdre() {
			return ordre;
		}

		public String getStatut() {
			return statut;
		}
		public String getLibelle() {
			return libelle;
		}
		public static STATUS parse(String str){
			STATUS enumeration=null;
			if(str==null)return enumeration;
			for(STATUS item:STATUS.values()){
				if(item.getStatut().equals(str.trim())){
					enumeration=item;
					break;
				}			
			}	
			return enumeration;
		}
		
	}
	public int getNumero() {
		
		return Integer.valueOf(getCode());
	}
}
