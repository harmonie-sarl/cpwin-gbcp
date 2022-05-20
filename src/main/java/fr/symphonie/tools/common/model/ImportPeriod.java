package fr.symphonie.tools.common.model;

import fr.symphonie.tools.gts.model.PeriodeEnum;
import fr.symphonie.util.model.OuiNonEnum;
import fr.symphonie.util.model.Trace;
import lombok.Data;
@Data
public class ImportPeriod {
	private Integer id;
	private Integer exercice;
	private String budget;
	private String module;
	private String code;
	private String etat;
	private String object;
	private boolean closed;
	private Trace trace;
	
	public PeriodeEnum getEtat() {
		return PeriodeEnum.parse(etat);
	}
	
	public void setEtat(PeriodeEnum etat) {
		String result=etat==null?null:etat.getStatus();
		this.etat = result;
	}

	public boolean isEditable(){
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
