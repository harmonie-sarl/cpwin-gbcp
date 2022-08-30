package fr.symphonie.common.model;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.util.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.symphonie.common.core.ICommonService;
import fr.symphonie.common.util.BudgetHelper;
import fr.symphonie.common.util.MsgEntry;
import fr.symphonie.config.ConfigParam;
import fr.symphonie.cpwin.core.model.PieceTypeEnum;
import fr.symphonie.exception.MissingConfiguration;
import fr.symphonie.util.HandlerJSFMessage;

public class AppCfgConfig {
	private static final Logger logger = LoggerFactory.getLogger(AppCfgConfig.class);
	private ICommonService service;
	private static AppCfgConfig instance;
	public static AppCfgConfig getInstance(){
		if(instance==null)instance=new AppCfgConfig();
		return instance;
	}
	private AppCfgConfig() {
		super();
		this.service=BudgetHelper.getCommonService();
	}
	
/**
 * Déclarations des paramétres CfgConfig
 */
	private ConfigParam tiersNeutralise=new ConfigParam("tTiersNeutralise") ;
	private ConfigParam etablisInitiales=new ConfigParam("tEtablisInitiales") ;
	private ConfigParam codeInfoCp=new ConfigParam("tCodeInfoCp");
	private ConfigParam planComptable=new ConfigParam("tPlanComptable");
	private ConfigParam siren=new ConfigParam("tEtablisSIREN");
	private ConfigParam siret=new ConfigParam("tEtablisSIRET");
	private ConfigParam tiersDematPath=new ConfigParam("ttiersdemat");
	private ConfigParam ttresorerielastecr=new ConfigParam("ttresorerielastecr",false);
	private ConfigParam codeCollectiviteRMH=new ConfigParam("tPesDivRembId");
	private ConfigParam gbcp=new ConfigParam("fGBCP",false);
	
	// DAS Params
	private ConfigParam adresseLigne1=new ConfigParam("tEtablisAdresseLigne1");
	private ConfigParam adresseLigne2=new ConfigParam("tEtablisAdresseLigne2");
	private ConfigParam codeApe=new ConfigParam("tEtablisCodeApe");
	private ConfigParam etablisCP=new ConfigParam("tEtablisCP");
	private ConfigParam etablisNom=new ConfigParam("tEtablisNom");	
	private ConfigParam etablisVille=new ConfigParam("tEtablisVille");
	// GTS params 
	private ConfigParam modePaieLib=new ConfigParam("TrModePaieRegleA");
	// Configuration des licences
	/**
	 * suivi cp
	 */
	private ConfigParam suiviCP=new ConfigParam("fsuivi_cp") ;
	/**
	 * suivi recettes
	 */
	private ConfigParam suiviRecette=new ConfigParam("fsuivi_rec") ;
	/**
	 * compte financier infra-annuel 
	 */
	private ConfigParam suiviCF=new ConfigParam("fsuivi_cf") ;
	/**
	 * PLT 
	 */
	private ConfigParam plt=new ConfigParam("fplt_p") ;
	/**
	 * Compte financier annuel
	 */
	private ConfigParam compteFinancier=new ConfigParam("fcf_imp") ;
	/**
	 * Pilotage AE - CP
	 */
	private ConfigParam pilotageAECP=new ConfigParam("ftb_aecp") ;
	/**
	 * Remboursement billetterie
	 */
	private ConfigParam rembSaisie=new ConfigParam("fremb_saisie") ;
	private ConfigParam rembConsult=new ConfigParam("fremb_consult") ;
	private ConfigParam rembSaisieRMH=new ConfigParam("fremb_saisie_rmh") ;
	/**
	 * meta4dai
	 */
	private ConfigParam meta4dai=new ConfigParam("fmeta4dai") ;
	/**
	 * Nantes ENSA: etudiant
	 */
	private ConfigParam nantesEtudiant=new ConfigParam("fnantesEtudiant") ;
	/**
	 * CRC Module
	 */
	private ConfigParam crcLicence=new ConfigParam("fcrc") ;
	/**
	 * Signature Module
	 */
	private ConfigParam signatureLicence=new ConfigParam("fsignature") ;
	private ConfigParam sfSignatLicence=new ConfigParam("fsignature_sf") ;
	private ConfigParam dpSignatLicence=new ConfigParam("fsignature_dp") ;
	private ConfigParam drSignatLicence=new ConfigParam("fsignature_dr") ;
	private ConfigParam dvSignatLicence=new ConfigParam("fsignature_dv") ;
	private ConfigParam daSignatLicence=new ConfigParam("fsignature_da") ;
	private ConfigParam ejSignatLicence=new ConfigParam("fsignature_ej") ;
	private ConfigParam arSignatLicence=new ConfigParam("fsignature_ar") ;
	private ConfigParam asfSignatLicence=new ConfigParam("fsignature_asf") ;
	private ConfigParam vsfSignatLicence=new ConfigParam("fsignature_vsf") ;
	private ConfigParam liqSignatLicence=new ConfigParam("fsignature_liq") ;
	private ConfigParam orSignatLicence=new ConfigParam("fsignature_or") ;
	private ConfigParam opSignatLicence=new ConfigParam("fsignature_op") ;
	private ConfigParam lrecSignatLicence=new ConfigParam("fsignature_lrec") ;
	/**
	 * Signature required Params
	 */
	private ConfigParam daSignatRequised=new ConfigParam("fSignature_da_requise") ;
	private ConfigParam ejSignatRequised=new ConfigParam("fSignature_ej_requise") ;
	private ConfigParam liqSignatRequised=new ConfigParam("fSignature_liq_requise");
	private ConfigParam asfSignatRequised=new ConfigParam("fSignature_asf_requise");
	private ConfigParam vsfSignatRequised=new ConfigParam("fSignature_vsf_requise");
	private ConfigParam orSignatRequised=new ConfigParam("fSignature_or_requise");
	private ConfigParam opSignatRequised=new ConfigParam("fSignature_op_requise");
	private ConfigParam lrecSignatRequised=new ConfigParam("fSignature_lrec_requise");
	private ConfigParam arSignatRequised=new ConfigParam("fSignature_ar_requise");
	
	
	
	
	
	private ConfigParam dpGenerationLicence=new ConfigParam("fdpGeneration") ;
	

	
	/**
	 * Déclaration des properties config
	 */
	private ConfigParam mailInfoDisable=new ConfigParam(MsgEntry.MAIL_INFO_DISABLED);
	private ConfigParam reportingPath=new ConfigParam(MsgEntry.REPORTING_PATH);
	private ConfigParam rootPath=new ConfigParam("document.root");
	private ConfigParam dematPath=new ConfigParam("tDematPath");
	private String tiersPjPath;
	/**
	 * Mail Properties
	 */
	private ConfigParam signatureEmailTo=new ConfigParam("tSignatureAgtcEmail") ;
	private ConfigParam mailPropertiesFrom=new ConfigParam("tMailPropertiesFrom") ;
	
	
	
	public String getSuiviCP()throws MissingConfiguration {
		return getDbConfigValue(suiviCP);
	}
	public String getSuiviRecette() throws MissingConfiguration{
		return getDbConfigValue(suiviRecette);
	}
	public String getSuiviCF() throws MissingConfiguration{
		return getDbConfigValue(suiviCF);
	}
	public String getPlt() throws MissingConfiguration{
		return getDbConfigValue(plt);
	}
	public String getPilotageAECP() throws MissingConfiguration{
		return getDbConfigValue(pilotageAECP);
	}
	public String getCompteFinancier() throws MissingConfiguration{
		return getDbConfigValue(compteFinancier);
	}
		
	public String getRembSaisie() throws MissingConfiguration{
		return getDbConfigValue(rembSaisie);
	}
	public String getRembSaisieRMH() throws MissingConfiguration{
		return getDbConfigValue(rembSaisieRMH);
	}
	public String getRembConsult() throws MissingConfiguration{
		return getDbConfigValue(rembConsult);
	}
	public String getMeta4Dai() throws MissingConfiguration{
		return getDbConfigValue(meta4dai);
	}
	public String getNantesEtudiant() throws MissingConfiguration{
		return getDbConfigValue(nantesEtudiant);
	}
	public String getCrcLicence() throws MissingConfiguration{
		return getDbConfigValue(crcLicence);
	}
	public String getSignatureLicence() throws MissingConfiguration{
		return getDbConfigValue(signatureLicence);
	}
	public String getDpGenerationLicence() throws MissingConfiguration{
		return getDbConfigValue(dpGenerationLicence);
	}
	
	public String getTiersNeutralise() {
		try {
			return getDbConfigValue(tiersNeutralise);
		} catch (MissingConfiguration e) {
			return Strings.EMPTY;
		}
	}
	public String getEtablisInitiales() throws MissingConfiguration{
		return getDbConfigValue(etablisInitiales);
	}
	public String getCodeInfoCp() throws MissingConfiguration{
		return getDbConfigValue(codeInfoCp);
	}
	public String getPlanComptable() throws MissingConfiguration{
		return getDbConfigValue(planComptable);
	}
	public String geSiren() throws MissingConfiguration{
		return getDbConfigValue(siren);
	}
	public String getSiret() throws MissingConfiguration{
		return getDbConfigValue(siret);
	}
	public String getTiersDematPath() throws MissingConfiguration{
		return getDbConfigValue(tiersDematPath);
	}
	
	public String getAdresseLigne1() throws MissingConfiguration{
		return getDbConfigValue(adresseLigne1);
	}
	public String getAdresseLigne2() throws MissingConfiguration{
		return getDbConfigValue(adresseLigne2);
	}
	public String getCodeApe() throws MissingConfiguration{
		return getDbConfigValue(codeApe);
	}
	public String getEtablisCP() throws MissingConfiguration{
		return getDbConfigValue(etablisCP);
	}
	public String getEtablisNom() throws MissingConfiguration{
		return getDbConfigValue(etablisNom);
	}
	public String getEtablisVille() throws MissingConfiguration{
		return getDbConfigValue(etablisVille);
	}
	public String getModePaieLib() throws MissingConfiguration{
		return getDbConfigValue(modePaieLib);
	}
	
	public String getCodeCollectiviteRMH()throws MissingConfiguration {
		return getDbConfigValue(codeCollectiviteRMH);
	}
	public Integer getTtresorerielastecr()  {		
		Integer lastNoEcr=null;
		try {
			String value = getDbConfigValue(ttresorerielastecr);
			lastNoEcr=Integer.valueOf(value);
		} catch (Exception e) {
			lastNoEcr=0;
		}
		return lastNoEcr;
	}
	private String getDbConfigValue(ConfigParam param) throws MissingConfiguration {
		return getDbConfigValue(param, null);
	}
	private String getDbConfigValue(ConfigParam param,Integer exercise) throws MissingConfiguration {
		if((!param.isLoaded())||(!param.isSessionScoped())){
			param.setValue(service.getConfigParam(param.getName(), exercise));
			if(!StringUtils.isBlank(param.getValue())) {
				param.setLoaded(true);
			}
			
			logger.debug(param.toString());
		}	
		return param.getValue();		
	}
	
	public String getReportingPath() throws MissingConfiguration {
		return getPropertiesConfigValue(reportingPath);
	}
	public String getRootPath() throws MissingConfiguration {
		return getPropertiesConfigValue(rootPath);
	}
	public String getDematPath() throws MissingConfiguration {
		return getDbConfigValue(dematPath);
	}
	
	private String getPropertiesConfigValue(ConfigParam param) throws MissingConfiguration {
		String value=null;
		if(!param.isLoaded()){
			try{
			value=ResourceBundle.getBundle("config").getString(param.getName());
			param.setValue(value);
			logger.debug(param.toString());	
			param.setLoaded(true);
			}
			catch(MissingResourceException  e){
				String error=HandlerJSFMessage.getErrorMessage("configuration.error");
				error=HandlerJSFMessage.formatMessage(error, new Object[]{param.getName()});
				logger.error("MissingConfiguration: pour {} ",param.getName());
				throw new MissingConfiguration(error);
			}
		}
	
		return param.getValue();
	}
	public String getTiersPjPath() throws MissingConfiguration {
		if(tiersPjPath==null) {
			tiersPjPath=BudgetHelper.getCpwinDematService().getDematPjDestPath(PieceTypeEnum.TIERS);
			if(tiersPjPath==null) {
				
			String paramName="tPJFile";
				String error=HandlerJSFMessage.getErrorMessage("configuration.error");
				error=HandlerJSFMessage.formatMessage(error, new Object[]{paramName});
				logger.error("MissingConfiguration: pour {} ",paramName);
				throw new MissingConfiguration(error);
			}
		}
		return tiersPjPath;
	}
	public void setTiersPjPath(String tiersPjPath) {
		this.tiersPjPath = tiersPjPath;
	}
	public String getGbcp(Integer exercice) throws MissingConfiguration {
		 return getDbConfigValue(gbcp,exercice);
	}
	public String getSignatureEmailTo() throws MissingConfiguration {
		return getDbConfigValue(signatureEmailTo);
		
	}
	public String getMailPropertiesFrom() throws MissingConfiguration {
		return getDbConfigValue(mailPropertiesFrom);
	}
	public boolean isMailInfoDisabled()  {
		String configuredValue=null;
		try {
		 configuredValue= getPropertiesConfigValue(mailInfoDisable);
		}
		catch(MissingConfiguration e) {
			//empty bloc here is normal
		}
		if(StringUtils.isBlank(configuredValue)) return false;
		
	
		return Boolean.valueOf(configuredValue);
	}
	public String getSfSignatLicence() throws MissingConfiguration{
		return getDbConfigValue(sfSignatLicence);
	}
	public String getDpSignatLicence() throws MissingConfiguration{
		return getDbConfigValue(dpSignatLicence);
	}
	public String getDrSignatLicence() throws MissingConfiguration{
		return getDbConfigValue(drSignatLicence);
	}
	public String getDvSignatLicence() throws MissingConfiguration{
		return getDbConfigValue(dvSignatLicence);
	}
	public String getDaSignatLicence() throws MissingConfiguration{
		return getDbConfigValue(daSignatLicence);
	}
	public String getEjSignatLicence() throws MissingConfiguration{
		return getDbConfigValue(ejSignatLicence);
	}
	public String getArSignatLicence() throws MissingConfiguration{
		return getDbConfigValue(arSignatLicence);
	}
	public String getAsfSignatLicence() throws MissingConfiguration{
		return getDbConfigValue(asfSignatLicence);
	}
	public String getVsfSignatLicence() throws MissingConfiguration{
		return getDbConfigValue(vsfSignatLicence);
	}
	public String getLiqSignatLicence() throws MissingConfiguration{
		return getDbConfigValue(liqSignatLicence);
	}
	public String getOrSignatLicence() throws MissingConfiguration{
		return getDbConfigValue(orSignatLicence);
	}
	public String getOpSignatLicence() throws MissingConfiguration{
		return getDbConfigValue(opSignatLicence);
	}
	public String getLrecSignatLicence() throws MissingConfiguration{
		return getDbConfigValue(lrecSignatLicence);
	}
	public String getDaSignatRequise() throws MissingConfiguration{
		return getDbConfigValue(daSignatRequised);
	}
	public String getEjSignatRequise() throws MissingConfiguration{
		return getDbConfigValue(ejSignatRequised);
	}
	public String getLiqSignatRequise() throws MissingConfiguration{
		return getDbConfigValue(liqSignatRequised);
	}
	public String getAsfSignatRequise() throws MissingConfiguration{
		return getDbConfigValue(asfSignatRequised);
	}
	public String getVsfSignatRequise() throws MissingConfiguration{
		return getDbConfigValue(vsfSignatRequised);
	}
	public String getOrSignatRequise() throws MissingConfiguration{
		return getDbConfigValue(orSignatRequised);
	}
	public String getOpSignatRequise() throws MissingConfiguration{
		return getDbConfigValue(opSignatRequised);
	}
	public String getLrecSignatRequise() throws MissingConfiguration{
		return getDbConfigValue(lrecSignatRequised);
	}
	public String getArSignatRequise() throws MissingConfiguration{
		return getDbConfigValue(arSignatRequised);
	}
	

	
}
