package fr.symphonie.tools.gts.model;

import fr.symphonie.util.model.OuiNonEnum;
import fr.symphonie.util.model.Trace;

public class PeriodeGts {
	private Integer exercice;
	private Integer numero;
	private String etat;
	private boolean closed;
	private Trace trace;
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
	public PeriodeEnum getEtat() {
		return PeriodeEnum.parse(etat);
	}
	
	public void setEtat(PeriodeEnum etat) {
		String result=etat==null?null:etat.getStatus();
		this.etat = result;
	}
	public boolean isClosed() {
		return closed;
	}
	public void setClosed(boolean closed) {
		this.closed = closed;
	}
	public Trace getTrace() {
		return trace;
	}
	public void setTrace(Trace trace) {
		this.trace = trace;
	}
//	public boolean isEditable(){
//		return Strings.isBlank(getEtat());
//	}
	public boolean isEditable(){
//		String result=getEtat()==null?null:getEtat().getStatus();
//		return Strings.isBlank(result);
		if (isOuvert()) return true;
		return false;
	}
	public String getFerme(){
		return isClosed()? OuiNonEnum.OUI.getDesignation():OuiNonEnum.NON.getDesignation();
	}
	
	public boolean isTraite()
	{
		return PeriodeEnum.TRAITE==getEtat();
	}
	
	public boolean isCharge()
	{
		return PeriodeEnum.CHARGE==getEtat();
	}
	
	public boolean isOuvert()
	{
		return PeriodeEnum.OUVERT==getEtat();
	}
	public boolean isAuMoinCharge() {
		PeriodeEnum etat=getEtat();
	    if(etat==null) return false;	    
		return etat.getOrdre()>=PeriodeEnum.CHARGE.getOrdre();
	}
	public boolean isAuPlusCharge() {
		PeriodeEnum etat=getEtat();
	    if(etat==null) return false;	    
		return etat.getOrdre()<=PeriodeEnum.CHARGE.getOrdre();
	}
	
	
}
