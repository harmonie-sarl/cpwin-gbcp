package fr.symphonie.tools.lemans.bt.model;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import fr.symphonie.common.util.BudgetHelper;
import fr.symphonie.impor.AttributImport;
import fr.symphonie.impor.ConfigImport;
import fr.symphonie.impor.Importable;
import fr.symphonie.util.model.Trace;
import lombok.Data;
@Data
@Entity
@Table(name = "bt_vente")
public class Vente implements Importable {
	@Id
	@Column(name = "num_encaiss")
	private Integer numEncaiss;
	@Column(name = "num_exec")
	private Integer exercice;
	@Column(name = "code_budg",length = 1)
	private String	budget;
	@Column(length = 3)
	private String	periode;
	
	@Column(name = "date_encaiss")
	private Date dateEncaiss;	
	@Column(name = "mode_paie",length = 10)
	private String modeReglement;
	@Column(length = 50)
	private String nomPayeur;
	@Column
	private BigDecimal montant;
	
	// Relationship
	@Transient
	private Client client;
	@Transient
	private Spectacle spectacle;
	@OneToMany(mappedBy = "vente")
	private List<VenteItem> details;
	@Embedded
	private Trace trace;

	@Override
	public void setValue(AttributImport attribut, Object value) {			
		BigDecimal bdValue=null;
		String strValue=null;
		Date date	=	null;
		
		if(value instanceof String)
			strValue=(value!=null)?((String) value).trim():null;
		else if(value instanceof  BigDecimal) {
			bdValue=(value!=null)?(BigDecimal) value:null;
		}
		else if(value instanceof Date)
				date	= (value!=null)?(Date) value:null;
		
		String attributName=attribut.getName();
		switch (attributName){

		case "dateEncaiss":	
			setDateEncaiss(date);
			break;
		case "numEncaiss":
			setNumEncaiss(bdValue.intValue());
			break;
		case "modeReglement":
			setModeReglement(strValue);
			break;
		case "nomPayeur":
			setNomPayeur(strValue);
			break;
		case "montant":
			setMontant(bdValue);
			break;

		}
		
	}
	@Override
	public ConfigImport getConfigImport() {
		if(configImport==null) configImport=BudgetHelper.loadConfigImport("lemans.bt.vente.import.config");
		return configImport;
	}
	private static ConfigImport configImport=null;
	
	@Override
	public void setRowNum(Integer rowNum) {
		this.rowNum=rowNum;
		
	}
	@Override
	public Integer getRowNum() {
		return getConfigImport().getStartedRowIndex()+rowNum;
	}
	@Transient
	private Integer rowNum;

}
