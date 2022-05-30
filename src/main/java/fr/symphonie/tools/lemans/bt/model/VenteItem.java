package fr.symphonie.tools.lemans.bt.model;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
@Table(name = "tb_detailsVente")
public class VenteItem implements Importable {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(columnDefinition = "Numeric")
	private Long id;
	@Column(name = "num_exec")
	private Integer exercice;
	@Column(name = "code_budg",length = 1)
	private String	budget;
	@Column(length = 3)
	private String	periode;
	@Column(name = "num_encaiss")
	private Integer numEncaiss;
	@Column(length = 50)
	private String objet;
	@Column(name = "code_spectacle")
	private String codeSpectacle;
	@Column
	private Date dateRep;	
	@Column
	private BigDecimal montant;
	
	//RelationShip
	@ManyToOne
	@JoinColumn(name = "num_encaiss",insertable = false,updatable = false)
	private Vente		vente;
	@Embedded
	private Trace trace;

	@Override
	public void setValue(AttributImport attribut, Object value) {			
		BigDecimal bdValue=null;
		String strValue=null;
		
		Date date =null;
		if(value instanceof String)
			strValue=(value!=null)?((String) value).trim():null;
		else if(value instanceof  BigDecimal) {
			bdValue=(value!=null)?(BigDecimal) value:null;
		}
		else if(value instanceof Date)
			date 	= (value!=null)?(Date) value:null;
		
		String attributName=attribut.getName();
		switch (attributName){
		case "numEncaiss":
			setNumEncaiss(bdValue.intValue());
			break;
		case "objet":
			setObjet(strValue);
			break;
		case "dateRep":	
			setDateRep(date);
			break;	
		case "spectacle":
			setCodeSpectacle(strValue);;
			break;
		case "montant":
			setMontant(bdValue);
			break;

		}
		
	}
	@Override
	public ConfigImport getConfigImport() {
		if(configImport==null) configImport=BudgetHelper.loadConfigImport("lemans.bt.modalites.import.config");
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
