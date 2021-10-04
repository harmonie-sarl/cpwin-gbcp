package fr.symphonie.budget.core.model.plt;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Transient;

import fr.symphonie.cpwin.model.cpt.DebitCreditEnum;
import fr.symphonie.util.model.SimpleEntity;

public class Ecriture implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1041830753796190016L;
	private Integer exercice;
	private Integer numero;
	private Integer numeroOP;
	private String type;
	private String libelle;
	private String tiers;
	private String sens;
	private Double montant;
	private Integer exerciceOrigine;
	private String typePiece;
	private Integer numeroPiece;
	private Integer exerciceLigne;
	private Integer periode;
	private Integer numeroLigne1;
	private Double montantLigne1;
	private Integer numeroLigne2;
	private Double montantLigne2;
    
	@Transient
	private String libelleLigne1;
	@Transient
	private String libelleLigne2;
	@Transient
	private Integer numeroTemp1;
	@Transient
	private Double montantTemp1;
	@Transient
	private Integer numeroTemp2;
	@Transient
	private Double montantTemp2;
	@Transient
	DetailLigneTresorerie ltr1=null;
	@Transient
	DetailLigneTresorerie ltr2=null;
		
	
	public Ecriture() {
		super();
	}
	public Ecriture(Integer exercice, Integer periode) {
		this();
		this.exercice = exercice;
		this.periode = periode;
	}
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
	public Integer getNumeroOP() {
		return numeroOP;
	}
	public void setNumeroOP(Integer numeroOP) {
		this.numeroOP = numeroOP;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getLibelle() {
		return libelle;
	}
	public void setLibelle(String libelle) {
		this.libelle = libelle;
	}
	public String getTiers() {
		return tiers;
	}
	public void setTiers(String tiers) {
		this.tiers = tiers;
	}
	public DebitCreditEnum getSens() {
		return DebitCreditEnum.parse(sens);
	}
	public void setSens(DebitCreditEnum sens) {
		this.sens = sens.getSens();
	}
	public double getMontant() {
		return montant;
	}
	public void setMontant(double montant) {
		this.montant = montant;
	}
	public Integer getExerciceOrigine() {
		return exerciceOrigine;
	}
	public void setExerciceOrigine(Integer exerciceOrigine) {
		this.exerciceOrigine = exerciceOrigine;
	}
	public String getTypePiece() {
		return typePiece;
	}
	public void setTypePiece(String typePiece) {
		this.typePiece = typePiece;
	}
	public Integer getNumeroPiece() {
		return numeroPiece;
	}
	public void setNumeroPiece(Integer numeroPiece) {
		this.numeroPiece = numeroPiece;
	}
	
	public Integer getExerciceLigne() {
		return exerciceLigne;
	}
	public void setExerciceLigne(Integer exerciceLigne) {
		this.exerciceLigne = exerciceLigne;
	}
	public Integer getPeriode() {
		return periode;
	}
	public void setPeriode(Integer periode) {
		this.periode = periode;
	}
	public Integer getNumeroLigne1() {
		return numeroLigne1;
	}
	public void setNumeroLigne1(Integer numeroLigne1) {
		this.numeroLigne1 = numeroLigne1;
	}
	public double getMontantLigne1() {
		return (montantLigne1!=null)?montantLigne1:0;
	}
	public void setMontantLigne1(double montantLigne1) {
		this.montantLigne1 = montantLigne1;
	}
	public Integer getNumeroLigne2() {
		return numeroLigne2;
	}
	public void setNumeroLigne2(Integer numeroLigne2) {
		this.numeroLigne2 = numeroLigne2;
	}
	
	
	public double getMontantLigne2() {
		return (montantLigne2!=null)?montantLigne2:0;
	}
	public void setMontantLigne2(double montantLigne2) {
		this.montantLigne2 = montantLigne2;
	}
	
	public DetailLigneTresorerie getLtr1() {
		return ltr1;
	}
	public void setLtr1(DetailLigneTresorerie ltr1) {
		this.ltr1 = ltr1;
	}
	public DetailLigneTresorerie getLtr2() {
		return ltr2;
	}
	public void setLtr2(DetailLigneTresorerie ltr2) {
		this.ltr2 = ltr2;
	}
	public double getDebit(){
		return (getSens()==DebitCreditEnum.DEBIT)?getMontant():0;
	}
	public double getCredit(){
		return (getSens()==DebitCreditEnum.CREDIT)?getMontant():0;
	}
	
	public boolean isTotalValide() {
		double total =getMontantTemp1() +  getMontantTemp2();
		boolean result =( total == getMontant());
		return result;
	}
	
	public String getNumLig1Str() {

		try {
			return getNumeroLigne1().toString();
		} catch (Exception e) {
			return null;
		}
	}

	public void setNumLig1Str(String numLig1Str) {
		try {
			setNumeroTemp1(Integer.parseInt(numLig1Str.trim()));
		} catch (Exception e) {
			// pour traiter le cas de null pointer
		}
	}

	public String getNumLig2Str() {
		try {
			return getNumeroLigne2().toString();
		} catch (Exception e) {
			return null;
		}
	}

	public void setNumLig2Str(String numLig2Str) {
		try {
			setNumeroTemp2(Integer.parseInt(numLig2Str.trim()));
		} catch (Exception e) {
			// pour traiter le cas de null pointer
		}
	}
	public void setMontantLigne1(Double montantLigne1) {
		this.montantLigne1 = montantLigne1;
	}
	public void setMontantLigne2(Double montantLigne2) {
		this.montantLigne2 = montantLigne2;
	}
	private String getLibLigne(List<SimpleEntity> noLignes,String numLigne) {
		int index=0;
		SimpleEntity result=null;	
		if((noLignes==null)||(numLigne==null))return null;
		index=noLignes.indexOf(new SimpleEntity(numLigne, ""));
		if(index==-1)return null;
		result=noLignes.get(index);
		return result.getDesignation();
		
	}
	public void updateLibLigne1(List<SimpleEntity> noLignes) {
		setLibelleLigne1(getLibLigne(noLignes, getNumLig1Str()));
	}
	public void updateLibLigne2(List<SimpleEntity> noLignes) {
		setLibelleLigne2(getLibLigne(noLignes, getNumLig2Str()));
		
	}
	public String getLibelleLigne1() {
		return libelleLigne1;
	}
	public void setLibelleLigne1(String libelleLigne1) {
		this.libelleLigne1 = libelleLigne1;
	}
	public String getLibelleLigne2() {
		return libelleLigne2;
	}
	public void setLibelleLigne2(String libelleLigne2) {
		this.libelleLigne2 = libelleLigne2;
	}
	public Integer getNumeroTemp1() {
		return numeroTemp1;
	}
	public void setNumeroTemp1(Integer numeroTemp1) {
		this.numeroTemp1 = numeroTemp1;
	}
	public Double getMontantTemp1() {
		return montantTemp1;
	}
	public void setMontantTemp1(Double montantTemp1) {
		this.montantTemp1 = montantTemp1;
	}
	public Integer getNumeroTemp2() {
		return numeroTemp2;
	}
	public void setNumeroTemp2(Integer numeroTemp2) {
		this.numeroTemp2 = numeroTemp2;
	}
	public Double getMontantTemp2() {
		return montantTemp2;
	}
	public void setMontantTemp2(Double montantTemp2) {
		this.montantTemp2 = montantTemp2;
	}

	public void ajusterMontant2() {
		double montantAjust = (getMontantTemp1() == null) ? 0d : getMontantTemp1();
		double montant = getMontant() - montantAjust;
		setMontantTemp2(montant);
	}
	public void prepareValidate() {
		setNumeroLigne1(getNumeroTemp1());
		setMontantLigne1(getMontantTemp1());
		setNumeroLigne2(getNumeroTemp2());
		setMontantLigne2(getMontantTemp2());
		if((getLtr1()!=null) && (getLtr1().getLigne()!=null))
		setLibelleLigne1(getLtr1().getLigne().getDesignation());
		if((getLtr2()!=null) && (getLtr2().getLigne()!=null))
		setLibelleLigne2(getLtr2().getLigne().getDesignation());
		
	}
	
}
