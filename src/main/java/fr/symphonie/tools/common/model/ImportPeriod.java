package fr.symphonie.tools.common.model;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import fr.symphonie.tools.gts.model.PeriodeEnum;
import fr.symphonie.util.model.OuiNonEnum;
import fr.symphonie.util.model.Trace;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "import_periode")
public class ImportPeriod {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(columnDefinition = "Numeric")
	private Integer id;
	@Column(name = "num_exec")
	private Integer exercice;
	@Column(name = "code_budg")
	private String budget;
	@Column(length = 20)
	private String module;
	@Column(length = 3)
	private String code;
	@Column(length = 20)
	private String etat;
	@Column(length = 50)
	private String object;
	@Column
	private boolean closed;
	@Embedded
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

	public Integer getNumero() {
		try {
			return Integer.valueOf(getCode());
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	
}
