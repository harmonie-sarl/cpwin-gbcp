package fr.symphonie.tools.gts.model;

import java.math.BigDecimal;

import org.apache.logging.log4j.util.Strings;

import fr.symphonie.util.Helper;
import fr.symphonie.util.model.SimpleEntity;

public class LigneRecette {
	private Integer exercice;//ne pas afficher
	private Integer numero;
	private Integer noLiqRec;//ne pas afficher
	private String libelle;
	private SimpleEntity prestation; // Article
	private Integer quantite;
	private BigDecimal pu;
	private float tauxRemise;
	private BigDecimal mntRemise;
	private float tauxTva;
	private BigDecimal mntTva;
	private BigDecimal mntHt;
	private BigDecimal mntTtc;
	private LiqRecette liqRecette;//ne pas afficher
	
	public LigneRecette(ImportedData data) {
		this();
		this.exercice=data.getExercice();
		getPrestation().setCode(data.getCodeArticle());
		getPrestation().setDesignation(data.getArticle().getLibelle());
		//this.libelle=data.getArticle().getLibelle();
		this.libelle=Strings.isBlank(data.getObjet())?data.getArticle().getLibelle():data.getObjet();
		this.quantite=data.getQuantite();
		this.pu=data.getUsedPu();
		this.tauxRemise=data.getTauxRemise();
		this.tauxTva=data.getArticle().getDetail().getTva();
		calculateMontants();
	}	
	
	public LigneRecette(LiqRecette liqRecette) {
		this();
		this.liqRecette=liqRecette;
	}	
	
	
	
	public LigneRecette() {
		super();
		this.prestation=new SimpleEntity();
		this.mntRemise=BigDecimal.ZERO;
		this.mntHt=BigDecimal.ZERO;
		this.mntTtc=BigDecimal.ZERO;
		this.mntTva=BigDecimal.ZERO;
	}
	public void calculateMontants() {
		double montant=0,ttc=0,ht=0,tva=0,remise=0;
		montant=getPu().doubleValue()*getQuantite();
		 remise=montant*getTauxRemise()/100;
		 ttc=montant-remise;
		 ht=ttc/(1+(getTauxTva()/100));
		 tva=ttc-ht;
		
		 int decimalPlace=2;
		mntRemise=new BigDecimal(Helper.round(remise, decimalPlace));
		mntTtc=new BigDecimal(Helper.round(ttc, decimalPlace));
		mntHt=new BigDecimal(Helper.round(ht, decimalPlace));
		mntTva=new BigDecimal(Helper.round(tva, decimalPlace));
		
		
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
	public Integer getNoLiqRec() {
		return noLiqRec;
	}
	public void setNoLiqRec(Integer noLiqRec) {
		this.noLiqRec = noLiqRec;
	}
	public SimpleEntity getPrestation() {
		return prestation;
	}
	public void setPrestation(SimpleEntity prestation) {
		this.prestation = prestation;
	}
	public Integer getQuantite() {
		return quantite;
	}
	public void setQuantite(Integer quantite) {
		this.quantite = quantite;
	}
	public BigDecimal getPu() {
		return pu;
	}
	public void setPu(BigDecimal pu) {
		this.pu = pu;
	}
	public float getTauxRemise() {
		return tauxRemise;
	}
	public void setTauxRemise(float tauxRemise) {
		this.tauxRemise = tauxRemise;
	}
	public BigDecimal getMntRemise() {
		return mntRemise;
	}
	public void setMntRemise(BigDecimal mntRemise) {
		this.mntRemise = mntRemise;
	}
	public float getTauxTva() {
		return tauxTva;
	}
	public void setTauxTva(float tauxTva) {
		this.tauxTva = tauxTva;
	}
	public BigDecimal getMntTva() {
		return mntTva;
	}
	public void setMntTva(BigDecimal mntTva) {
		this.mntTva = mntTva;
	}
	public BigDecimal getMntHt() {
		return mntHt;
	}
	public void setMntHt(BigDecimal mntHt) {
		this.mntHt = mntHt;
	}
	public BigDecimal getMntTtc() {
		return mntTtc;
	}
	public void setMntTtc(BigDecimal mntTtc) {
		this.mntTtc = mntTtc;
	}
	public LiqRecette getLiqRecette() {
		return liqRecette;
	}
	public void setLiqRecette(LiqRecette liqRecette) {
		this.liqRecette = liqRecette;
	}

	public String getLibelle() {
		return libelle;
	}

	public void setLibelle(String libelle) {
		this.libelle = libelle;
	}
	
	
	
	public Integer getNumeroOfChild()
	{
		if(isParent())return null;
		return getNumero();
	}

	
	public String getPrestationOfChild()
	{
		if(isParent())return null;
		return getPrestation().getCode();
	} 
	public Integer getQuantiteOfChild()
	{
		if(isParent())return null;
		return getQuantite();
	}
	public BigDecimal getPuOfChild()
	{
		if(isParent())return null;
		return getPu();
	}
	public Float getTauxRemiseOfChild()
	{
		if(isParent())return null;
		return getTauxRemise();
	}
	public BigDecimal getMntRemiseOfChild()
	{
		if(isParent())return null;
		return getMntRemise();
	}
	public Float getTauxTvaOfChild()
	{
		if(isParent())return null;
		return getTauxTva();
	}
	public BigDecimal getMntTvaOfChild()
	{
		if(isParent())return null;
		return getMntTva();
	}
	public BigDecimal getMntHtOfChild()
	{
		if(isParent())return null;
		return getMntHt();
	}
	public BigDecimal getMntTtcOfChild()
	{
		if(isParent())return null;
		return getMntTtc();
	}
	public String getDescLiqRecetteOfParent()
	{
		if(isParent())return getLiqRecette().getDescription();
		return null;
	}
	
	
	public Integer getNumeroOfParent()
	{
		
		if(isParent())return getLiqRecette().getNumero();
		return null;
	}
	
	public String getTiersOrigOfParent()
	{
		if(isParent())return getLiqRecette().getTiersOrigine();
		return null;
	}
	public String getRefCmdOfParent()
	{
		if(isParent())return getLiqRecette().getRefCommande();
		return null;
	}
	
	public String getDestOfParent()
	{
		if(isParent())return getLiqRecette().getDestination();
		return null;
	}
	public String getServOfParent()
	{
		if(isParent())return getLiqRecette().getService();
		return null;
	}
	public String getNatOfParent()
	{
		if(isParent())return getLiqRecette().getNature();
		return null;
	}
	public String getProgOfParent()
	{
		if(isParent())return getLiqRecette().getProgramme();
		return null;
	}

	public boolean isParent()
	{
		return getNumero()==null;
	}
	public String getCodeTrsOrigParent()
	{
		if(isParent())return getLiqRecette().getTiersOrigine();
		return null;
	}
	
	public String getNameTrsOrigParent()
	{
		if(isParent())
			if(getLiqRecette().getTiers()!=null)
				return getLiqRecette().getTiers().getNom();
		return null;
	}
	public String getAdressTrsOrigParent()
	{
		if(isParent())
			if(getLiqRecette().getTiers()!=null)
				if( getLiqRecette().getTiers().getAdresse()!=null)
				return getLiqRecette().getTiers().getAdresse().getAdresse();
		return null;
	}
	
	public String getObjectOfParent()
	{
		if(isParent())		
				return getLiqRecette().getLibelle();
		return null;
	}
	
	public Integer getPeriodOfParent()
	{
		if(isParent())		
				return getLiqRecette().getNumPeriode();
		return null;
	}
	
	public String getNameTrsParent()
	{
		if(isParent())
			if(getLiqRecette().getTiersCpwin()!=null)
				return getLiqRecette().getTiersCpwin().getNom();
		return null;
	}
	public String getAdressTrsParent()
	{
		if(isParent())
			if(getLiqRecette().getTiersCpwin()!=null)
				if( getLiqRecette().getTiersCpwin().getAdresse()!=null)
				return getLiqRecette().getTiersCpwin().getAdresse().getAdresse();
		return null;
	}
	
	
}
