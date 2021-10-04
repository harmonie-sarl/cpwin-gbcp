package fr.symphonie.tools.gts.model;

import java.math.BigDecimal;

import javax.persistence.Transient;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.symphonie.common.util.BudgetHelper;
import fr.symphonie.impor.AttributImport;
import fr.symphonie.impor.ConfigImport;
import fr.symphonie.impor.Importable;
import fr.symphonie.util.Helper;
import fr.symphonie.util.model.Trace;

public class ImportedData implements Importable{
	private static final Logger logger = LoggerFactory.getLogger(ImportedData.class);
	private Integer id;
	private Integer exercice;
	private Integer numero;
	private String codeArticle;
	private BigDecimal pu;
	private float tauxRemise;
	private Integer quantite;
	private String codeClientGts;
	private String refCommande;
	private String serviceClient;
	private String objet;
	private Trace trace;
	@Transient
	private Article article;
	@Transient
	private ClientGts clientGts;
	

	
	public ImportedData() {
		super();
	}

	@Override
	public void setValue(AttributImport attribut, Object value) {
		String strValue=null;
		BigDecimal bdValue=null;
		if(value instanceof String)
			strValue=(value!=null)?((String) value).trim():null;
			else if(value instanceof  BigDecimal)
				bdValue=(value!=null)?(BigDecimal) value:null;
		String attributName=attribut.getName();
		switch (attributName){
		case "PERIODE":
			String execStr=strValue.substring(0, 4);
//			String periodeStr=strValue.substring(4, 6);
			String periodeStr=strValue.substring(4);
			setExercice(Integer.valueOf(execStr));setNumero(Integer.valueOf(periodeStr));
			break;
		case "CodeArticle":			 
			setCodeArticle(strValue);
			break;
		case "PU":
			setPu(bdValue);
			break;
		case "TAUXREMISE":
			setTauxRemise(bdValue.floatValue());
			break;
		case "QUANTITE":
			setQuantite(bdValue.intValue());
			break;
		case "TIERS":
			setCodeClientGts(strValue);
			break;
		case "RefCommande":
			setRefCommande(strValue);
			break;
		case "SERVICE":
			setServiceClient(strValue);
			break;
		case "Objet":
			setObjet(strValue);
			break;
		}
		
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Article getArticle() {
		return article;
	}

	public void setArticle(Article article) {
		this.article = article;
	}

	public ClientGts getClientGts() {
		return clientGts;
	}

	public void setClientGts(ClientGts clientGts) {
		this.clientGts = clientGts;
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

	public String getCodeArticle() {
		return codeArticle;
	}

	public void setCodeArticle(String codeArticle) {
		this.codeArticle = codeArticle;
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

	public Integer getQuantite() {
		return quantite;
	}

	public void setQuantite(Integer quantite) {
		this.quantite = quantite;
	}

	public String getCodeClientGts() {
		return codeClientGts;
	}

	public void setCodeClientGts(String codeClientGts) {
		this.codeClientGts = codeClientGts;
	}

	public String getRefCommande() {
		return refCommande;
	}

	public void setRefCommande(String refCommande) {
		this.refCommande = refCommande;
	}

	public String getServiceClient() {
		return serviceClient;
	}

	public void setServiceClient(String serviceClient) {
		this.serviceClient = serviceClient;
	}

	public Trace getTrace() {
		return trace;
	}

	public void setTrace(Trace trace) {
		this.trace = trace;
	}

	public double getMontant(){
		double result=0;
		BigDecimal prixUnitaire=getUsedPu();
		result=prixUnitaire.doubleValue()*getQuantite()*(1-getTauxRemise()/100);
		logger.debug("Montant: pu={}, quantité={}, taux remise={} -->{}",prixUnitaire.doubleValue(),getQuantite(),getTauxRemise(),result);
		
		return Helper.round(result, 2);
	}
	public BigDecimal getUsedPu(){
		if((getPu()==null)||(getPu().equals(BigDecimal.ZERO)))
			return getArticle().getDetail().getPu();
		return getPu();
	}

	@Override
	public ConfigImport getConfigImport() {
		if(configImport==null) configImport=BudgetHelper.loadConfigImport("gts.import.config");
		return configImport;
	}
	
	@Override
	public String toString() {
		return "ImportedData [codeArticle=" + codeArticle + ", codeClientGts=" + codeClientGts + "]";
	}

	public String getObjet() {
		return objet;
	}

	public void setObjet(String objet) {
		this.objet = objet;
	}

	private static ConfigImport configImport=null;



	@Override
	public void setRowNum(Integer rowNum) {
		
	}
	@Override
	public Integer getRowNum() {
		return null;
	}

}
