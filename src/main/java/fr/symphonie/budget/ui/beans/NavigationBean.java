package fr.symphonie.budget.ui.beans;

import java.io.File;
import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.symphonie.budget.core.model.plt.PeriodeEnum;
import fr.symphonie.budget.ui.beans.edition.CompteFinancierBean;
import fr.symphonie.budget.ui.beans.edition.DematBean;
import fr.symphonie.budget.ui.beans.pluri.BudgetPluriannuelBean;
import fr.symphonie.budget.ui.excel.ExcelModelEnum;
import fr.symphonie.common.IBasicBean;
import fr.symphonie.common.model.AppCfgConfig;
import fr.symphonie.common.util.BudgetHelper;
import fr.symphonie.common.util.MsgEntry;
import fr.symphonie.doc.MSGenerator;
import fr.symphonie.exception.MissingConfiguration;
import fr.symphonie.tools.common.DataListBean;
import fr.symphonie.tools.crc.CrcBean;
import fr.symphonie.tools.das.ui.DasBean;
import fr.symphonie.tools.gts.ui.GtsBean;
import fr.symphonie.tools.meta4dai.DaiInterfaceBean;
import fr.symphonie.tools.nantes.StudentBean;
import fr.symphonie.tools.recette.ImportRecetteBean;
import fr.symphonie.tools.sct.SepaCreditTransfertBean;
import fr.symphonie.util.HandlerJSFMessage;
import fr.symphonie.util.Helper;
import fr.symphonie.util.model.Libelle;
import fr.symphonie.util.model.User;
import fr.symphonie.web.jsf.beans.UserBean;

@ManagedBean
@SessionScoped
public class NavigationBean implements Serializable {
	
	private static final String SASI_TIERS_OUTCOME="saisi_tiers";
    private static final String GESTION_TIERS_OUTCOME="gestion_tiers";
    private static final String UTILISATEUR_OUTCOME="utilisateur";
    private static final String LOGIN_OUTCOME="login";
    private static final String SAISIR_BUDGET_PLURI="saisi_budget_pluri";
    private static final String ADRESS_BUDGET_PLURI_SAISI="adressage_budget_pluri";
    private static final String ADRESS_BUDGET_PLURI_IMPORT="adressage_budget_pluri_import";
    private static final String ADRESS_BUDGET_PLURI_CPDEST="adressage_budget_pluri_cpDest";
    
    private static final String SAISI_ETAB_ABS_OUTCOME="saisi_etablissement_absorbe";
    private static final String NOTIFIER_ETAB_ABS_OUTCOME="notifier_etablissement_absorbe";
    private static final String AVIS_REGELEMENT_OUTCOME="avis_reglement";
    private static final String INTERLOCUTEUR_RECOUVREMENT_OUTCOME="interlocuteur_recouvrement";
    private static final String GESTION_EDITIONS_OUTCOME="gestion_editions";
    private static final String EDITIONS_BR_OUTCOME="editions_br";
    private static final String SITUATION_PATRIMONIALE_OUTCOME="situation_patrimoniale";
    private static final String TABLEAU_BORD_OUTCOME="tableau_bord";
    private static final String SAISI_CP_BR_OUTCOME="saisi_credit_paie_br";
    private static final String VENTIL_SERV_BR_OUTCOME="ventil_serv_br";
    private static final String VENTIL_DEST_BR_OUTCOME="ventil_dest_br";
    private static final String VENTIL_GEST_BR_OUTCOME="ventil_gest_br";
//    private static final String   TABLEAU_BORD_PAR_ENV_OUTCOME="par_enveloppe";
    private static final String VENTIL_AE_RECETTE_ORIGINE="ventil_ae_recette_origine";
    private static final String EXTERN_DATA_OUTCOME="donnees_externes";
    public static final String SUIVI_CP_OUTCOME = "suivi_cp";
    public static final String SUIVI_AE_OUTCOME = "suivi_ae";
    public static final String SUIVI_RECETTE_OUTCOME = "suivi_rec";
    private static final String INTERFACE_MINEFI_OUTCOME="interface_minefi";
  //  private static final String COMPTE_FINANCIER_OUTCOME="compte_financier";
    private static final String PARAM_BILAN_OUTCOME="param_bilan";
    private static final String VAL_BILAN_OUTCOME="val_bilan";
    private static final String PARAM_CR_OUTCOME="param_cr";
    private static final String VAL_CR_OUTCOME="val_cr";
    private static final String VAL_CF_OUTCOME="val_cf";
    private static final String  AJUST_TRESORERIE_OUTCOME="ajust_tres";
    private static final String  SIMUL_TRESORERIE_OUTCOME="simul_tres";
    private static final String  VENTILL_TRESORERIE_OUTCOME="ventil_tres";
    private static final String  IMPORT_TIERS_OUTCOME   ="importTiers";
    private static final String  CONSULT_TRESORERIE_OUTCOME="consult_tres";
    private static final String  IMPORT_HONORAIRE_OUTCOME="import_honoraire";
    private static final String  GENERER_HONORAIRE_OUTCOME="generer_honoraire";
    private static final String  SUIVI_TRS_HONORAIRE_OUTCOME="suivi_tiers_honoraire";
    private static final String  CONSULT_TIERS_OUTCOME   ="consultTiers";
    private static final String  MODIF_DEPOIEMENT_OUTCOME   ="modifDeploi";
    private static final String  CONSULT_DEPOIEMENT_OUTCOME   ="consultDeploi";
    private static final String  INIT_TRESORERIE_OUTCOME   ="init_tres";
    private static final String  GTS_IMPORT_OUTCOME   ="gts_import";
    private static final String  GTS_ARTICLE_OUTCOME="ref_artcile";
     private static final String  GTS_PERIODE_OUTCOME="ref_periode";
    private static final String  GTS_CLIENT_OUTCOME="gts_client";
    private static final String  PARAM_TRESORERIE_OUTCOME="param_tres";
    private static final String  GTS_INIT_OUTCOME="gts_init";
    private static final String  GTS_GENERATION_OUTCOME="gts_generation";
    private static final String  GTS_CONSULTATION_OUTCOME="gts_consultataion";
    private static final String  CONCIL_TRESORERIE_OUTCOME="concil_tres";
    private static final String  CHARGEMENT_EDITIONS_OUTCOME="chargement_editions";
    private static final String  CHARGEMENT_EDIT_BR_OUTCOME="chargement_edit_br";
    private static final String  SUIVI_CF_OUTCOME="suivi_cf";
    private static final String  IMPORT_RECETTE_OUTCOME="import_recette";
    private static final String  SUIVI_SF_OUTCOME="suivi_sf";
    private static final String  remboursement_SAISIE_OUTCOME="remboursement_saisie";
    private static final String  remboursement_GENE_OUTCOME="remboursement_gene";
    private static final String  remboursement_CONSULT_OUTCOME="remboursement_consult";
    private static final String  PAYMEN_SEPA_SAISIE_OUTCOME="PAYMEN_SEPA_saisie";
    private static final String  META4DAI_SAISI_OUTCOME="meta4dai_saisi";
    private static final String  META4DAI_CONSULT_OUTCOME="meta4dai_consult";
    private static final String  META4DAI_INIT_OUTCOME="meta4dai_init";
    private static final String   IMPORT_DEPENSE_OUTCOME="import_depense";
    private static final String  ETUDIANT_CHARGEMENT_OUTCOME="etudiant_chargement";
    private static final String  ETUDIANT_MISEAJOUR_OUTCOME="etudiant_miseajour";
    private static final String  ETUDIANT_GENERATION_DP_OUTCOME="etudiant_generation_dp";
//    private static final String  CRC_IMPRESSION_PJ_OUTCOME="crc_impression_pj";
//    private static final String  CRC_MANDATS_OUTCOME="crc_mandats";
    private static final String  CRC_DEPENSE_OUTCOME="crc_depense";
    private static final String  CRC_RECETTE_OUTCOME="crc_recette";
    private static final String  SIGNATURE_OUTCOME="signature";
//    private static final String  META4DAI_PERIODE_OUTCOME="meta4dai_periode";
//    private static final String  META4DAI_LBC_OUTCOME="meta4dai_lbc";
    private static final String  GENERATION_DP_OUTCOME="generation_dp";
    
    
    
    
    
   
    
    
   
 
    private static final Logger logger = LoggerFactory.getLogger(NavigationBean.class);

	/**
	 * 
	 */
	private static final long serialVersionUID = -8914512188002593156L;
	
	private Action currentAction;
	private Menu currentMenu=Menu.BUDGET_PLURI;
	public NavigationBean() {
		super();
	}

	@PostConstruct
		private void initialize() {
			if (logger.isDebugEnabled()) {
				logger.debug("####### initialisation - start");
			}
			try{
				String reportingPath=AppCfgConfig.getInstance().getReportingPath();
				MSGenerator.EXPORT_OUTPUT_PATH=reportingPath+File.separator+"excel"+File.separator;
				//MSGenerator.EXPORT_OUTPUT_PATH=HandlerJSFMessage.getConfigParam(MsgEntry.REPORTING_PATH)+File.separator+"excel"+File.separator;

			}catch (Exception e) {
				//e.printStackTrace();
				logger.error("error in instanciation of config parameters",e);
			}
		}
	public String getAutoLogin(){
	System.out.println("############# autologin ###########"+Helper.getRequestParameter("login"));
		return LOGIN_OUTCOME;
	}
	
	public String goToGestionTiers()
	{
//		String returnString = "gestion_tiers";		
		GestionTiersBean  gestionTiersBean=(GestionTiersBean)Helper
			.findBean("gestionTiersBean");
		gestionTiersBean.resetTiersElements();
//		return returnString;
		 prepare(Action.GESTION_TIERS);
		return Action.GESTION_TIERS.getOutcome();
	}
	
	
	public String saisiBudgPluri() {

		BudgetPluriannuelBean bpBean = (BudgetPluriannuelBean) Helper
				.findBean("bpBean");
		SearchBean searchBean = BudgetHelper.getSearchBean();	
		bpBean.reset();
		searchBean.resetSearchElements();
		prepare(Action.SAISI_BUDGET_PLURI);
		return Action.SAISI_BUDGET_PLURI.getOutcome();
	}
	
	public String adressageBudgPluriSaisi() {

		BudgetPluriannuelBean bpBean = (BudgetPluriannuelBean) Helper
				.findBean("bpBean");
		SearchBean searchBean = BudgetHelper.getSearchBean();	
		searchBean.resetSearchElements();
		bpBean.reset();
      //  bpBean.resetDynamicList();
		prepare(Action.ADRESSAGE_BUDGET_PLURI_SAISI);
		return Action.ADRESSAGE_BUDGET_PLURI_SAISI.getOutcome();
	}
	
	public String adressageBudgPluriImport() {

		BudgetPluriannuelBean bpBean = (BudgetPluriannuelBean) Helper
				.findBean("bpBean");
		bpBean.reset();
//		bpBean.setBudget(null);
		bpBean.resetImport();
		
		

		prepare(Action.ADRESSAGE_BUDGET_PLURI_IMPORT);
		return Action.ADRESSAGE_BUDGET_PLURI_IMPORT.getOutcome();
	}
	
	public String adressageBudgPluriCpDest() {

		BudgetPluriannuelBean bpBean = (BudgetPluriannuelBean) Helper
				.findBean("bpBean");
		bpBean.reset();
		//bpBean.resetDynamicList();
		SearchBean searchBean = BudgetHelper.getSearchBean();
		searchBean.resetSearchElements();
		prepare(Action.ADRESSAGE_BUDGET_PLURI_CPDEST);
		return Action.ADRESSAGE_BUDGET_PLURI_CPDEST.getOutcome();
	}
	public String ventillationCpGest() {

		BudgetPluriannuelBean bpBean = getBpBean();
		SearchBean searchBean = BudgetHelper.getSearchBean();
		searchBean.resetSearchElements();
		bpBean.reset();
		//bpBean.resetDynamicList();

		prepare(Action.VENTILLATION_CP_GEST);
		return Action.VENTILLATION_CP_GEST.getOutcome();
	}
	public enum Licence{
		SUIVIE_CP,
		SUIVIE_RECETTE,
		SUIVIE_CF,
		PLT,
		AE_CP,
		CF,
		REMB_SAISIE,
		REMB_SAISIE_RMH,
		REMB_CONSULT,
		META4DAI,
		NantesEtudiant,
		CRC,
		SIGNATURE,SIGNATURE_SF,SIGNATURE_DP,SIGNATURE_DR,SIGNATURE_DV,SIGNATURE_DA,SIGNATURE_EJ,SIGNATURE_ASF,SIGNATURE_VSF,SIGNATURE_LIQ,SIGNATURE_OR,SIGNATURE_OP,SIGNATURE_LREC,SIGNATURE_AR,SIGNATURE_MDT,SIGNATURE_TR,
		DP_GENERATION,SIGNATURE_DA_REQUISE,SIGNATURE_EJ_REQUISE,SIGNATURE_ASF_REQUISE,SIGNATURE_VSF_REQUISE,SIGNATURE_LIQ_REQUISE,SIGNATURE_OR_REQUISE,SIGNATURE_OP_REQUISE,SIGNATURE_LREC_REQUISE,SIGNATURE_AR_REQUISE,SIGNATURE_MDT_REQUISE,SIGNATURE_TR_REQUISE
	}
	
	public enum Action {
		SAISI_TIERS(SASI_TIERS_OUTCOME,MsgEntry.TIERS_SAISI_PT), CONSULTATION_TIERS(SASI_TIERS_OUTCOME,MsgEntry.TIERS_CONSULT_PT), MODIFICATION_TIERS(
		SASI_TIERS_OUTCOME,MsgEntry.TIERS_MODIF_PT),
//		GESTION_TIERS(GESTION_TIERS_OUTCOME,Menu.GESTION_TIERS),
		SAISI_BUDGET_PLURI(SAISIR_BUDGET_PLURI,Menu.BUDGET_PLURI,Menu.BUDGET_INITIAL ,MsgEntry.MENU_SAISI_CP,ExcelModelEnum.ENVELOP_BUDG_LIST),
		ADRESSAGE_BUDGET_PLURI_SAISI(ADRESS_BUDGET_PLURI_SAISI,Menu.BUDGET_PLURI,Menu.BUDGET_INITIAL,Menu.VENTILLATION,MsgEntry.MENU_VENTIL_SERV_PROG), 
		ADRESSAGE_BUDGET_PLURI_IMPORT(ADRESS_BUDGET_PLURI_IMPORT,MsgEntry.MENU_BUDGET_PLURI),
		ADRESSAGE_BUDGET_PLURI_CPDEST(ADRESS_BUDGET_PLURI_CPDEST,Menu.BUDGET_PLURI,Menu.BUDGET_INITIAL,Menu.VENTILLATION,MsgEntry.MENU_VENTIL_DEST),
	    VENTILLATION_CP_GEST("ventillation_cp_gest",Menu.BUDGET_PLURI,Menu.BUDGET_INITIAL,Menu.VENTILLATION ,MsgEntry.MENU_VENTIL_GEST),
		//DEPLOIEMENT("deploiement",Menu.BUDGET_PLURI,MsgEntry.MENU_DEPLOIMENT),
		UTILISATEUR(UTILISATEUR_OUTCOME,Menu.MENU_PARAMETRAGE), SAISI_ETAB_ABS(SAISI_ETAB_ABS_OUTCOME), NOTIFIER_ETAB_ABS(NOTIFIER_ETAB_ABS_OUTCOME),
		INTERLOCUTEUR_RECOUVREMENT(INTERLOCUTEUR_RECOUVREMENT_OUTCOME),AVIS_REGELEMENT(AVIS_REGELEMENT_OUTCOME),
		GESTION_EDITIONS(GESTION_EDITIONS_OUTCOME,Menu.BUDGET_PLURI,Menu.BUDGET_INITIAL,Menu.EDITIONS,MsgEntry.MENU_SAISI),
		EDITIONS_BR(EDITIONS_BR_OUTCOME,Menu.BUDGET_PLURI,Menu.BUDGET_RECTIF,Menu.EDITIONS,MsgEntry.MENU_SAISI),
		SITUATION_PATRIMONIALE(SITUATION_PATRIMONIALE_OUTCOME,Menu.BUDGET_PLURI,Menu.BUDGET_INITIAL,MsgEntry.MENU_SITUA_PATRIM),/*TABLEAU_BORD(TABLEAU_BORD_OUTCOME),*/
		SAISI_CP_BR(SAISI_CP_BR_OUTCOME,Menu.BUDGET_PLURI,Menu.BUDGET_RECTIF,MsgEntry.MENU_SAISI_CP),
		VENTIL_SERV_BR(VENTIL_SERV_BR_OUTCOME,MsgEntry.MENU_BUDGET_PLURI) ,
		VENTIL_DEST_BR(VENTIL_DEST_BR_OUTCOME,Menu.BUDGET_PLURI,Menu.BUDGET_RECTIF,Menu.VENTILLATION ,MsgEntry.MENU_VENTIL_DEST) ,
		VENTIL_GEST_BR(VENTIL_GEST_BR_OUTCOME,Menu.BUDGET_PLURI,Menu.BUDGET_RECTIF,Menu.VENTILLATION ,MsgEntry.MENU_VENTIL_GEST),
		TABLEAU_BORD_PAR_ENV(TABLEAU_BORD_OUTCOME,Menu.BUDGET_PLURI,Menu.TAB_BORD,MsgEntry.MENU_PAR_ENVELOP), 
		TABLEAU_BORD_PAR_COMPREC_V2(TABLEAU_BORD_OUTCOME,Menu.BUDGET_PLURI,Menu.TAB_BORD,MsgEntry.MENU_PAR_COMPRECV2), 
		TABLEAU_BORD_SUIVI_CP(TABLEAU_BORD_OUTCOME,Menu.BUDGET_PLURI,Menu.TAB_BORD,MsgEntry.MENU_SUIVI_CP), 
		TABLEAU_BORD_SUIVI_REC(TABLEAU_BORD_OUTCOME,Menu.BUDGET_PLURI,Menu.TAB_BORD,MsgEntry.MENU_SUIVI_REC), 
		TABLEAU_BORD_CR_GESTION(TABLEAU_BORD_OUTCOME,Menu.BUDGET_PLURI,Menu.TAB_BORD,MsgEntry.MENU_COMPTE_REND_GEST),
		TABLEAU_BORD_COMPTE_DEP(TABLEAU_BORD_OUTCOME,Menu.BUDGET_PLURI,Menu.TAB_BORD,MsgEntry.MENU_COMPTE_COMPTBLE_DEP) ,
		VENTILATION_RECETTE_ORIGINE(VENTIL_AE_RECETTE_ORIGINE,Menu.BUDGET_PLURI,Menu.BUDGET_INITIAL,Menu.VENTILLATION,MsgEntry.MENU_VENTIL_AE_RECETTE_ORIGINE),
		EXTERN_DATA(EXTERN_DATA_OUTCOME,Menu.BUDGET_PLURI,Menu.BUDGET_INITIAL,MsgEntry.MENU_EXTERN_DATA),
		SUIVI_CP(SUIVI_CP_OUTCOME,Menu.BUDGET_PLURI,Menu.SUIVI,MsgEntry.CP),
		SUIVI_AE(SUIVI_AE_OUTCOME,Menu.BUDGET_PLURI,Menu.TAB_BORD,MsgEntry.SUIVI_AE_CP),
		SUIVI_REC(SUIVI_RECETTE_OUTCOME,Menu.BUDGET_PLURI,Menu.SUIVI,MsgEntry.RECETTES_MINUS),
		SUIVI_CF(SUIVI_CF_OUTCOME,Menu.BUDGET_PLURI,Menu.SUIVI,MsgEntry.COMPTE_FINANCIER),
		SUIVI_SF(SUIVI_SF_OUTCOME,Menu.BUDGET_PLURI,Menu.SUIVI,MsgEntry.SUIVI_SF),
		INTERFACE_MINEFI(INTERFACE_MINEFI_OUTCOME,Menu.BUDGET_PLURI,MsgEntry.INTERFACE_MINEFI),
		PARAM_BILAN(PARAM_BILAN_OUTCOME,Menu.BUDGET_PLURI,Menu.COMPTE_FINANCIER,Menu.CF_BILAN,MsgEntry.MENU_PARAMETRAGE),
		VAL_BILAN(VAL_BILAN_OUTCOME,Menu.BUDGET_PLURI,Menu.COMPTE_FINANCIER,Menu.CF_BILAN,MsgEntry.VALIDATION),
		VAL_CR(VAL_CR_OUTCOME,Menu.BUDGET_PLURI,Menu.COMPTE_FINANCIER,Menu.CF_CR,MsgEntry.VALIDATION),		
		PARAM_CR(PARAM_CR_OUTCOME,Menu.BUDGET_PLURI,Menu.COMPTE_FINANCIER,Menu.CF_CR,MsgEntry.PARAM_CR),
		VAL_CF(VAL_CF_OUTCOME,Menu.BUDGET_PLURI,Menu.COMPTE_FINANCIER,Menu.CF_CF,MsgEntry.MENU_SAISI),
		AJUST_TRESORERIE(AJUST_TRESORERIE_OUTCOME,Menu.BUDGET_PLURI,Menu.MENU_TRESORERIE,MsgEntry.MENU_AJUSTEMENT), 
		SIMUL_TRESORERIE(SIMUL_TRESORERIE_OUTCOME,Menu.BUDGET_PLURI,Menu.MENU_TRESORERIE,MsgEntry.MENU_SIMULATION),
		VENTIL_TRESORERIE(VENTILL_TRESORERIE_OUTCOME,Menu.BUDGET_PLURI,Menu.MENU_TRESORERIE,MsgEntry.MENU_VENTILATION),
		IMPORT_TIERS_DAS(IMPORT_TIERS_OUTCOME,Menu.MENU_TOOLS,Menu.MENU_DAS, Menu.MENU_TIERS,MsgEntry.IMPORT),
	    GESTION_TIERS(GESTION_TIERS_OUTCOME,Menu.MENU_TOOLS,MsgEntry.MENU_CPWIN),
		CONSULT_TRESORERIE(CONSULT_TRESORERIE_OUTCOME,Menu.BUDGET_PLURI,Menu.MENU_TRESORERIE,MsgEntry.MENU_CONSULT) ,
		PARAM_TRESORERIE(PARAM_TRESORERIE_OUTCOME,Menu.BUDGET_PLURI,Menu.MENU_TRESORERIE,MsgEntry.MENU_PARAM) ,
		IMPORT_HONORAIRE(IMPORT_HONORAIRE_OUTCOME,Menu.MENU_TOOLS,Menu.MENU_DAS,Menu.MENU_HONORAIRE,MsgEntry.IMPORT),
		GENERER_HONORAIRE(GENERER_HONORAIRE_OUTCOME,Menu.MENU_TOOLS,Menu.MENU_DAS,Menu.MENU_HONORAIRE,MsgEntry.GENERER),
		SUIVI_HONORAIRE(SUIVI_TRS_HONORAIRE_OUTCOME,Menu.MENU_TOOLS,Menu.MENU_DAS,Menu.MENU_HONORAIRE,MsgEntry.SUIVI_TIERS),
		CONSULT_TIERS_DAS(CONSULT_TIERS_OUTCOME,Menu.MENU_TOOLS,Menu.MENU_DAS, Menu.MENU_TIERS,MsgEntry.MENU_CONSULT),
		MODIF_DEPOIEMENT(MODIF_DEPOIEMENT_OUTCOME,Menu.BUDGET_PLURI,Menu.MENU_DEPLOIEMENT,MsgEntry.MENU_SAISI),
		CONSULT_DEPOIEMENT(CONSULT_DEPOIEMENT_OUTCOME,Menu.BUDGET_PLURI,Menu.MENU_DEPLOIEMENT,MsgEntry.CONSULT_DEPLOI),
		INIT_TRESORERIE(INIT_TRESORERIE_OUTCOME,Menu.BUDGET_PLURI,Menu.MENU_TRESORERIE,MsgEntry.MENU_INITIALISATION),
		GTS_ARTICLE(GTS_ARTICLE_OUTCOME,Menu.MENU_TOOLS,Menu.MENU_BILLETTERIE,Menu.MENU_REFERENTIEL,MsgEntry.MENU_ARTICLE),
		GTS_PERIODE(GTS_PERIODE_OUTCOME,Menu.MENU_TOOLS,Menu.MENU_BILLETTERIE,Menu.MENU_REFERENTIEL,MsgEntry.MENU_PERIODE),
		GTS_CLIENT(GTS_CLIENT_OUTCOME,Menu.MENU_TOOLS,Menu.MENU_BILLETTERIE,Menu.MENU_REFERENTIEL,MsgEntry.MENU_CLIENT),
		GTS_IMPORT(GTS_IMPORT_OUTCOME,Menu.MENU_TOOLS,Menu.MENU_BILLETTERIE,MsgEntry.IMPORT),
		GTS_GENERATION(GTS_GENERATION_OUTCOME,Menu.MENU_TOOLS,Menu.MENU_BILLETTERIE,MsgEntry.GENERATION),
		GTS_CONSULTATION(GTS_CONSULTATION_OUTCOME,Menu.MENU_TOOLS,Menu.MENU_BILLETTERIE,MsgEntry.MENU_CONSULT),
		CONCIL_TRESORERIE(CONCIL_TRESORERIE_OUTCOME,Menu.BUDGET_PLURI,Menu.MENU_TRESORERIE,MsgEntry.MENU_CONCILIATION),
		CHARGEMENT_EDITIONS(CHARGEMENT_EDITIONS_OUTCOME,Menu.BUDGET_PLURI,Menu.BUDGET_INITIAL,Menu.EDITIONS,MsgEntry.CHARGEMENT),
		CHARGEMENT_EDIT_BR(CHARGEMENT_EDIT_BR_OUTCOME,Menu.BUDGET_PLURI,Menu.BUDGET_RECTIF,Menu.EDITIONS,MsgEntry.CHARGEMENT),
		IMPORT_RECETTE(IMPORT_RECETTE_OUTCOME,Menu.MENU_TOOLS,MsgEntry.IMPORT_RECETTE),
		remboursement_SAISIE(remboursement_SAISIE_OUTCOME,Menu.MENU_TOOLS,Menu.MENU_REMBOURSEMENT,Menu.MENU_SAISIE,MsgEntry.SEPA),
		remboursement_GENE(remboursement_GENE_OUTCOME,Menu.MENU_TOOLS,Menu.MENU_REMBOURSEMENT,MsgEntry.GENERATION),
		remboursement_CONSULT(remboursement_CONSULT_OUTCOME,Menu.MENU_TOOLS,Menu.MENU_REMBOURSEMENT,MsgEntry.MENU_CONSULT),
		PAYMEN_SEPA(PAYMEN_SEPA_SAISIE_OUTCOME,Menu.MENU_TOOLS,Menu.MENU_REMBOURSEMENT,Menu.MENU_SAISIE,MsgEntry.RMH_DDFiP),
		meta4dai_SAISIE(META4DAI_SAISI_OUTCOME,Menu.MENU_TOOLS,Menu.MENU_META4DAI,MsgEntry.MENU_SAISI),
		meta4dai_CONSULT(META4DAI_CONSULT_OUTCOME,Menu.MENU_TOOLS,Menu.MENU_META4DAI,MsgEntry.MENU_CONSULT),
		etudiant_chargement(ETUDIANT_CHARGEMENT_OUTCOME,Menu.MENU_TOOLS,Menu.MENU_ETUDIANT,MsgEntry.ETUDIANT_CHARGEMENT),
		IMPORT_DEPENSE(IMPORT_DEPENSE_OUTCOME,Menu.MENU_TOOLS,MsgEntry.IMPORT_DEPENSE),
		ETUDIANT_MISEAJOUR(ETUDIANT_MISEAJOUR_OUTCOME,Menu.MENU_TOOLS,Menu.MENU_ETUDIANT,MsgEntry.ETUDIANT_MISEAJOUR),
		ETUDIANT_GENERATION_DP(ETUDIANT_GENERATION_DP_OUTCOME,Menu.MENU_TOOLS,Menu.MENU_ETUDIANT,MsgEntry.ETUDIANT_GENERATIONDP),
		CRC_DEPENSE(CRC_DEPENSE_OUTCOME,Menu.MENU_TOOLS,Menu.MENU_CRC,MsgEntry.CRC_DEPENSE),
		CRC_RECETTE(CRC_RECETTE_OUTCOME,Menu.MENU_TOOLS,Menu.MENU_CRC,MsgEntry.CRC_RECETTE),
		SIGNATURE(SIGNATURE_OUTCOME,Menu.MENU_TOOLS,MsgEntry.MENU_SIGNATURE),
		INITIALIZE_META4DAI(META4DAI_INIT_OUTCOME,Menu.MENU_TOOLS,Menu.MENU_META4DAI,MsgEntry.MENU_INITIALISATION),
		INITIALIZE_GTS(GTS_INIT_OUTCOME,Menu.MENU_TOOLS,Menu.MENU_BILLETTERIE,MsgEntry.MENU_INITIALISATION),
		DP_GENERATION(GENERATION_DP_OUTCOME,Menu.MENU_TOOLS,MsgEntry.DP_SAISIE),
		;
		
		
			
		private Menu mainMenu;
		private Menu subMenu;
		private Menu subMenu1;
		private Libelle libelle;
		
		private ExcelModelEnum excelModel;
		public Menu getMainMenu() {
			return mainMenu;
		}
		Action( String outcome) {
			this.mainMenu=null;
			subMenu=null;
			msgKey=null;
			this.outcome = outcome;

		}
		private Action(String outcome,Menu menu) {
			this.outcome = outcome;
			this.mainMenu=menu;
			subMenu=null;
			msgKey=null;
		}
		private Action(String outcome,Menu menu,Menu subMenu,String msgKey) {
			this(outcome,menu);
			this.setSubMenu(subMenu);
			this.msgKey=msgKey;
		}
		private Action(String outcome,Menu menu,Menu subMenu,Menu subMenu1,String msgKey) {
			this(outcome,menu,subMenu,msgKey);
			this.setSubMenu1(subMenu1);
		}
		private Action(String outcome,Menu menu,Menu subMenu,String msgKey,ExcelModelEnum excel) {
			this(outcome,menu,subMenu,msgKey);
			this.setExcelModel(excel);
		}
		Action(String outcome,String msgKey) {
			this.outcome = outcome;
			this.msgKey = msgKey;
			

		}
		private Action(String outcome,String msgKey,ExcelModelEnum excel) {
			this(outcome,msgKey);
			this.setExcelModel(excel);
		}
		
		
		private void setSubMenu(Menu subMenu) {
			this.subMenu = subMenu;
		}

		public Menu getSubMenu() {
			return subMenu;
		}
		private Action(String outcome,Menu menu,String msgKey) {
			this(outcome,menu);
//			this.setSubMenu(subMenu);
			this.msgKey=msgKey;
		}
		public void setExcelModel(ExcelModelEnum excelModel) {
			this.excelModel = excelModel;
		}

		public ExcelModelEnum getExcelModel() {
			return excelModel;
		}
		

		private String outcome;
		private String msgKey;

		public void setOutcome(String outcome) {
			this.outcome = outcome;
		}

		public String getOutcome() {
			return outcome;
		}

		public void setMsgKey(String msgKey) {
			this.msgKey = msgKey;
		}

		public String getMsgKey() {
			return msgKey;
		}
		public String getLibelle() {
			libelle=new Libelle();
			if(msgKey!=null){
				
			libelle.setLibelleFR(HandlerJSFMessage.getMessage(msgKey));
//			libelle.setLibelleDE(HandlerJSFMessage.getMessage(Langue.DE, msgKey));
			}
			
			return libelle.getLibelleFR();	
		}
		public String getNavigationPath(){
			String PATH_SEPARATOR=" > ";
			StringBuilder path=new StringBuilder();
			logger.info("mainMenu: "+mainMenu.getLibelle());
			path.append(mainMenu.getLibelle());
			if(subMenu!=null){path.append(PATH_SEPARATOR);path.append(subMenu.getLibelle());}
			if(subMenu1!=null){path.append(PATH_SEPARATOR);path.append(subMenu1.getLibelle());}
			if(msgKey!=null){path.append(PATH_SEPARATOR);path.append(getLibelle());}
			return path.toString();
			
		}
		public void setSubMenu1(Menu subMenu1) {
			this.subMenu1 = subMenu1;
		}

		public Menu getSubMenu1() {
			return subMenu1;
		}

	}
	private void prepare(Action action) {
		this.currentAction = action;
		setMenu(action.getMainMenu());
	}
	private String prepare(Action action,IBasicBean bean) {
		this.currentAction = action;
		bean.initialize();
		setMenu(action.getMainMenu());
		return action.getOutcome();
	}
	public Action getCurrentAction() {
		return currentAction;
	}
	public void setCurrentAction(Action currentAction) {
		this.currentAction = currentAction;
	}
	
	public String parametrageUtilisateur() {
		UserBean userBean = (UserBean) Helper.findBean("userBean");
		userBean.resetBean();
		userBean.setSelectedUser(new User());
		prepare(Action.UTILISATEUR);
		return Action.UTILISATEUR.getOutcome();
	}
	
	public String goToGestionEditions()
	{
		SearchBean searchBean = BudgetHelper.getSearchBean();		
		searchBean.reset();
		BudgetHelper.getEditionBean().reset();
		 prepare(Action.GESTION_EDITIONS);
		return Action.GESTION_EDITIONS.getOutcome();
	}
	public String goToGestionEditionsBR()
	{
//		 prepare(Action.EDITIONS_BR);
//		return Action.EDITIONS_BR.getOutcome();

		SearchBean searchBean = BudgetHelper.getSearchBean();		
		searchBean.reset();
		BudgetHelper.getEditionBean().reset();
		 prepare(Action.EDITIONS_BR);
		return Action.EDITIONS_BR.getOutcome();
	}
	
	public String gotoSaisiNoDataBudg()
	{
		 prepare(Action.SITUATION_PATRIMONIALE);
		return Action.SITUATION_PATRIMONIALE.getOutcome();
	}
	
	
	public String goToParEnvelop()
	{

		 prepare(Action.TABLEAU_BORD_PAR_ENV);
		 prepareTB();
		return Action.TABLEAU_BORD_PAR_ENV.getOutcome();
	}
	public String goToParComptRecV2()
	{

		 prepare(Action.TABLEAU_BORD_PAR_COMPREC_V2);
		 prepareTB();
		return Action.TABLEAU_BORD_PAR_COMPREC_V2.getOutcome();
	}
	public String goToSuiviCP()
	{

		 prepare(Action.TABLEAU_BORD_SUIVI_CP);
		 prepareTB();
		return Action.TABLEAU_BORD_SUIVI_CP.getOutcome();
	}
	
	public String goToSuiviREC()
	{

		 prepare(Action.TABLEAU_BORD_SUIVI_REC);
		 prepareTB();
		return Action.TABLEAU_BORD_SUIVI_REC.getOutcome();
	}
	public String goToTbCompteDepense()
	{

		 prepare(Action.TABLEAU_BORD_COMPTE_DEP);
		 prepareTB();
		return Action.TABLEAU_BORD_COMPTE_DEP.getOutcome();
	}
	private void prepareTB() {
		 BudgetHelper.getCommonBean().reset();
		 BudgetHelper.getSearchBean().setCodeBudget(null);
		 BudgetHelper.getSearchBean().setExercice(null);
		 BudgetHelper.getSearchBean().setDateArret(null);
		
	}
	public String goToComptRenduGestion()
	{

		 prepare(Action.TABLEAU_BORD_CR_GESTION);
		 prepareTB();
		return Action.TABLEAU_BORD_CR_GESTION.getOutcome();
	}

	public String goToEditionOutcome()
	{
		return Action.GESTION_EDITIONS.getOutcome();
		
	}
	private BudgetPluriannuelBean getBpBean() {
		return BudgetHelper.getBpBean();
	}
	
	public String saisiBR() {

		BudgetPluriannuelBean bpBean = (BudgetPluriannuelBean) Helper
				.findBean("bpBean");
		SearchBean searchBean = BudgetHelper.getSearchBean();
		searchBean.resetSearchElements();
		bpBean.reset();
	//	bpBean.resetDynamicList();
		prepare(Action.SAISI_CP_BR);
		return Action.SAISI_CP_BR.getOutcome();
	}
	
	public String ventilServBR() {

		BudgetPluriannuelBean bpBean = (BudgetPluriannuelBean) Helper
				.findBean("bpBean");
		bpBean.reset();
//		bpBean.setBudget(null);
		prepare(Action.VENTIL_SERV_BR);
		return Action.VENTIL_SERV_BR.getOutcome();
	}

	public String ventilDestBR() {
		BudgetPluriannuelBean bpBean = getBpBean();
		SearchBean searchBean = BudgetHelper.getSearchBean();	
		bpBean.reset();
		//bpBean.resetDynamicList();
		searchBean.resetSearchElements();
		prepare(Action.VENTIL_DEST_BR);
		return Action.VENTIL_DEST_BR.getOutcome();

	}
	public String ventilGestBR() {

		BudgetPluriannuelBean bpBean = (BudgetPluriannuelBean) Helper
				.findBean("bpBean");
		SearchBean searchBean = BudgetHelper.getSearchBean();	
		bpBean.reset();
		//bpBean.resetDynamicList();
		searchBean.resetSearchElements();
		prepare(Action.VENTIL_GEST_BR);
		return Action.VENTIL_GEST_BR.getOutcome();
	}
	public enum Menu{
		BUDGET_PLURI(MsgEntry.MENU_BUDGET_PLURI),
		BUDGET_INITIAL(MsgEntry.MENU_BUDG_INITIAL),
		BUDGET_RECTIF(MsgEntry.MENU_BUDG_RECTIF),
		DEPLOIMENT(MsgEntry.MENU_BUDG_INITIAL),
		SAISI_CP_BI(MsgEntry.MENU_SAISI_CP),
		VENTILLATION(MsgEntry.MENU_VENTILATION),
		EDITIONS(MsgEntry.MENU_EDITIONS),
		VENTIL_SERV_PROG(MsgEntry.MENU_VENTIL_SERV_PROG),
		VENTIL_DEST(MsgEntry.MENU_VENTIL_DEST),
		VENTIL_GEST(MsgEntry.MENU_VENTIL_GEST),
		//GESTION_TIERS(MsgEntry.MENU_TIERS),
		TAB_BORD(MsgEntry.MENU_TAB_BORD),
		TB_ENVELOPPE(MsgEntry.MENU_PAR_ENVELOP),
		TB_COMPTE_RECV2(MsgEntry.MENU_PAR_COMPRECV2),
		TB_SUIVI_CP(MsgEntry.MENU_SUIVI_CP),
		TB_COMPT_REND_GEST(MsgEntry.MENU_COMPTE_REND_GEST),
		MENU_PARAMETRAGE(MsgEntry.MENU_PARAMETRAGE),
		INTERFACE_MINEFI(MsgEntry.INTERFACE_MINEFI),
		COMPTE_FINANCIER(MsgEntry.COMPTE_FINANCIER),
		CF_BILAN(MsgEntry.BILAN),
		CF_CR(MsgEntry.CR),
		CF_CF(MsgEntry.CF),
		MENU_TRESORERIE(MsgEntry.MENU_TRESORERIE),
		MENU_DEPLOIEMENT(MsgEntry.MENU_DEPLOIMENT),
		MENU_SIMULATION(MsgEntry.MENU_SIMULATION),
		MENU_AJUSTEMENT(MsgEntry.MENU_AJUSTEMENT),
		MENU_DAS(MsgEntry.MENU_DAS),
		MENU_HONORAIRE(MsgEntry.HONORAIRE),
		MENU_TIERS(MsgEntry.TIERS),
		MENU_REFERENTIEL(MsgEntry.MENU_REFERENTIEL),
		MENU_BILLETTERIE(MsgEntry.MENU_BILLETTERIE),
		SUIVI(MsgEntry.SUIVI),
		CP(MsgEntry.CP),
		RECETTES_MINUS(MsgEntry.RECETTES_MINUS),
		MENU_REMBOURSEMENT(MsgEntry.IMPORT_VIREMENT),
		MENU_SAISIE(MsgEntry.MENU_SAISI),
		MENU_META4DAI(MsgEntry.META4DAI),
		MENU_ETUDIANT(MsgEntry.ETUDIANT),
		MENU_CRC(MsgEntry.CRC),
		MENU_TOOLS(MsgEntry.MENU_OUTILS),
		MENU_SIGNATURE(MsgEntry.MENU_SIGNATURE);
		;
		private Libelle libelle;
		Menu(String msgKey){
			libelle=new Libelle();
			libelle.setLibelleFR(HandlerJSFMessage.getMessage( msgKey));
			
		}		
		public String getLibelle() {
			
			return libelle.getLibelleFR();	
		}
		}
	private void setMenu(Menu menu){

		this.currentMenu=menu;
			
	}
public Menu getMenu(){
	return this.currentMenu;
}
		
public String ventilationRectteParorigine() {

	BudgetPluriannuelBean bpBean = (BudgetPluriannuelBean) Helper
			.findBean("bpBean");
	bpBean.reset();
//	bpBean.resetDynamicList();
	prepare(Action.VENTILATION_RECETTE_ORIGINE);
	return Action.VENTILATION_RECETTE_ORIGINE.getOutcome();
}
public String goToExterneData()
{
	 prepare(Action.EXTERN_DATA);
	return Action.EXTERN_DATA.getOutcome();
}
public String suiviCP()
{
	BudgetPluriannuelBean bpBean = (BudgetPluriannuelBean) Helper
			.findBean("bpBean");
	SearchBean searchBean = (SearchBean) Helper
			.findBean("searchBean");
	
	searchBean.reset();
	bpBean.reset();
	//bpBean.resetDynamicList();
	 prepare(Action.SUIVI_CP);
	return Action.SUIVI_CP.getOutcome();
}
public String suiviREC()
{
	BudgetPluriannuelBean bpBean = (BudgetPluriannuelBean) Helper
			.findBean("bpBean");
	SearchBean searchBean = (SearchBean) Helper
			.findBean("searchBean");
	
	searchBean.reset();
	bpBean.reset();
	//bpBean.resetDynamicList();
	 prepare(Action.SUIVI_REC);
	return Action.SUIVI_REC.getOutcome();
}

public String goToInterfaceMinefi()
{
	
//	DematBean  dematBean=(DematBean)Helper
//		.findBean("dematBean");
	getDematBean().reset();

	 prepare(Action.INTERFACE_MINEFI);
	return Action.INTERFACE_MINEFI.getOutcome();
}
//public String goToCompteFinancier()
//{
//	
//	getCfBean().reset();
//
//	 prepare(Action.COMPTE_FINANCIER);
//	return Action.COMPTE_FINANCIER.getOutcome();
//}

public String goToParamBilan()
{
	getCfBean().reset();

	 prepare(Action.PARAM_BILAN);
	return Action.PARAM_BILAN.getOutcome();
}

public String goToParamCR()
{
	getCfBean().reset();

	 prepare(Action.PARAM_CR);
	return Action.PARAM_CR.getOutcome();
}
public String goToValBilan()
{
	getCfBean().reset();

	 prepare(Action.VAL_BILAN);
	return Action.VAL_BILAN.getOutcome();
}
public String goToValCR()
{
	getCfBean().reset();

	 prepare(Action.VAL_CR);
	return Action.VAL_CR.getOutcome();
}
public String goToValCF()
{
	
	getCfBean().reset();
	getCFBean().setPlTresEnabled(true);

	 prepare(Action.VAL_CF);
	return Action.VAL_CF.getOutcome();
}

public String goToSimulTresorerie()
{
	getPltBean().reset();

	 prepare(Action.SIMUL_TRESORERIE);
	return Action.SIMUL_TRESORERIE.getOutcome();
}

public String goToAjustTresorerie()
{
	getPltBean().reset();

	 prepare(Action.AJUST_TRESORERIE);
	return Action.AJUST_TRESORERIE.getOutcome();
}
public String goToVentilTresorerie()
{
	getPltBean().reset();

	 prepare(Action.VENTIL_TRESORERIE);
	return Action.VENTIL_TRESORERIE.getOutcome();
}

private CompteFinancierBean getCfBean(){
	return (CompteFinancierBean)Helper.findBean("cfBean");
}
private GenericBean getPltBean(){
	return (GenericBean)Helper.findBean("pltBean");
}
private DasBean getDasBean(){
	return (DasBean)Helper.findBean("dasBean");
}
private GenericBean getGtsRefBean(){
	return (GenericBean)Helper.findBean("gtsRefBean");
}
private DataListBean getDataListBean(){
	return (DataListBean)Helper.findBean("dataListBean");
}

private GtsBean getGtsBean(){
	return (GtsBean)Helper.findBean("gtsBean");
}


private ImportRecetteBean getImportRecetteBean()
{
	return (ImportRecetteBean)Helper.findBean("impRecBean");
	}
private DematBean getDematBean(){
	return (DematBean)Helper.findBean("dematBean");
}
public String goToImportTiersDas()
{	

	DasBean  dasBean=getDasBean();
	dasBean.reset();

	 prepare(Action.IMPORT_TIERS_DAS);
	return Action.IMPORT_TIERS_DAS.getOutcome();
}
public String goToConsultTresorerie()
{
	getPltBean().reset();

	 prepare(Action.CONSULT_TRESORERIE);
	return Action.CONSULT_TRESORERIE.getOutcome();
}
public String importHonoraire()
{
	DasBean  dasBean=getDasBean();
	dasBean.reset();

	 prepare(Action.IMPORT_HONORAIRE);
	return Action.IMPORT_HONORAIRE.getOutcome();
}

public String genererHonoraire()
{
	DasBean  dasBean=getDasBean();
	dasBean.reset();

	 prepare(Action.GENERER_HONORAIRE);
	return Action.GENERER_HONORAIRE.getOutcome();
}
public String suiviHonoraire()
{
	DasBean  dasBean=getDasBean();
	dasBean.reset();

	 prepare(Action.SUIVI_HONORAIRE);
	return Action.SUIVI_HONORAIRE.getOutcome();
}


public String goToConsultTiersDas()
{	

	DasBean  dasBean=getDasBean();
	dasBean.reset();

	 prepare(Action.CONSULT_TIERS_DAS);
	return Action.CONSULT_TIERS_DAS.getOutcome();
}
private void deploiement() {

	BudgetPluriannuelBean bpBean = getBpBean();
	SearchBean searchBean = BudgetHelper.getSearchBean();
	searchBean.resetSearchElements();
	bpBean.reset();
	//bpBean.resetDynamicList();
}
 public String goToModifDeploi(){
	 deploiement();
	 prepare(Action.MODIF_DEPOIEMENT);
		return Action.MODIF_DEPOIEMENT.getOutcome();
 }
 
 public String goToConsultDeploi(){
	 deploiement();
	 prepare(Action.CONSULT_DEPOIEMENT);
		return Action.CONSULT_DEPOIEMENT.getOutcome();
 }
 
 public String goToinitTresorerie()
 {
 	getPltBean().reset();
 	prepare(Action.INIT_TRESORERIE);
 	return Action.INIT_TRESORERIE.getOutcome();
 }
 

 
	public String goToGtsArtcile() {
		getGtsRefBean().reset();
		prepare(Action.GTS_ARTICLE);
		return Action.GTS_ARTICLE.getOutcome();
	}
	
	public String goToGtsPeriode() {
		getDataListBean().reset();
		getGtsRefBean().reset();
		prepare(Action.GTS_PERIODE);
		return Action.GTS_PERIODE.getOutcome();
	}
	
 public String goToParamTresorerie()
 {
	 
			getPltBean().reset();

			 prepare(Action.PARAM_TRESORERIE);
			return Action.PARAM_TRESORERIE.getOutcome();
		
 }
 public String goToRefClient()
 {
	 getGtsRefBean().reset();
	 prepare(Action.GTS_CLIENT);
	 return Action.GTS_CLIENT.getOutcome();
 }
 
 public String goToGtsImport()
 {
	 getGtsBean().reset();	 
	 prepare(Action.GTS_IMPORT);
	 return Action.GTS_IMPORT.getOutcome();
	 
 }
 
 
 public String goToGtsGeneration()
 {
	 getGtsBean().reset();	 
	 prepare(Action.GTS_GENERATION);
	 return Action.GTS_GENERATION.getOutcome(); 
 }
 
 
 public String goToGtsConsultation()
 {
	 getGtsBean().reset();	 
	 prepare(Action.GTS_CONSULTATION);
	 return Action.GTS_CONSULTATION.getOutcome(); 
 }
 
 public String goToConcilTresorerie()
 {
 	getPltBean().reset();
 	prepare(Action.CONCIL_TRESORERIE);
 	return Action.CONCIL_TRESORERIE.getOutcome();
 }
 
	public String goToChargementEditions()
	{
		SearchBean searchBean = BudgetHelper.getSearchBean();		
		searchBean.reset();
		BudgetHelper.getEditionBean().reset();
		 prepare(Action.CHARGEMENT_EDITIONS);
		return Action.CHARGEMENT_EDITIONS.getOutcome();
	}
	
	public String goToSuiviCF()
	{
		prepare(Action.SUIVI_CF);
		getCfBean().reset();
		getCFBean().setPlTresEnabled(false);
		return Action.SUIVI_CF.getOutcome();
	}
	public String goToSuiviSF()
	{
		prepare(Action.SUIVI_SF);
		getCfBean().reset();
		return Action.SUIVI_SF.getOutcome();
	}
	
	public String goToChargementEditBR()
	{
		SearchBean searchBean = BudgetHelper.getSearchBean();		
		searchBean.reset();
		BudgetHelper.getEditionBean().reset();
		 prepare(Action.CHARGEMENT_EDIT_BR);
		return Action.CHARGEMENT_EDIT_BR.getOutcome();
	}
	
	public String goToImportRecettes() {
		getImportRecetteBean().reset();		
		prepare(Action.IMPORT_RECETTE);
		return Action.IMPORT_RECETTE.getOutcome();
	}
	public String suiviAE()
	{
		BudgetPluriannuelBean bpBean = (BudgetPluriannuelBean) Helper
				.findBean("bpBean");
		SearchBean searchBean = (SearchBean) Helper
				.findBean("searchBean");
		
		searchBean.reset();
		bpBean.reset();
		bpBean.resetDynamicList();
		 prepare(Action.SUIVI_AE);
		return Action.SUIVI_AE.getOutcome();
	}
	public boolean isSuiviCpAuthorized() {
		return checkLicence(Licence.SUIVIE_CP);
	}
	public boolean isSuiviCFAuthorized() {
		return checkLicence(Licence.SUIVIE_CF);
	}
	public boolean isSuiviRecetteAuthorized() {
		return checkLicence(Licence.SUIVIE_RECETTE);
	}
	public boolean isPltAuthorized() {
		return checkLicence(Licence.PLT);
	}
	public boolean isAeCpAuthorized() {
		return checkLicence(Licence.AE_CP);
	}
	public boolean isCfAuthorized() {
		return checkLicence(Licence.CF);
	}
	public boolean isRembSaisieAuthorized() {
		return checkLicence(Licence.REMB_SAISIE);
	}
	public boolean isRembSaisieRMHAuthorized() {
		return checkLicence(Licence.REMB_SAISIE_RMH);
	}
	public boolean isRembConsultAuthorized() {
		return checkLicence(Licence.REMB_CONSULT);
	}
	public boolean isMeta4daiAuthorized() {
		return checkLicence(Licence.META4DAI);
	}
	public boolean isNantesEtudiantAuthorized() {
		return checkLicence(Licence.NantesEtudiant);
	}
	public boolean isCrcAuthorized() {
		return checkLicence(Licence.CRC);
	}
	public boolean isSignAuthorized() {
		return checkLicence(Licence.SIGNATURE);
	}
	public boolean isDpGenerationAuthorized() {
		return checkLicence(Licence.DP_GENERATION);
	}
	
	private boolean checkLicence( Licence licence) {
		String configuredValue="0";
		try {
		switch(licence) {
		case AE_CP:
			configuredValue=AppCfgConfig.getInstance().getPilotageAECP();
			break;
		case PLT:
			configuredValue=AppCfgConfig.getInstance().getPlt();
			break;
		case SUIVIE_CF:
			configuredValue=AppCfgConfig.getInstance().getSuiviCF();
			break;
		case SUIVIE_CP:
			configuredValue=AppCfgConfig.getInstance().getSuiviCP();
			break;
		case SUIVIE_RECETTE:
			configuredValue=AppCfgConfig.getInstance().getSuiviRecette();
			break;
		case CF:
			configuredValue=AppCfgConfig.getInstance().getCompteFinancier();
			break;
		case REMB_SAISIE:
			configuredValue=AppCfgConfig.getInstance().getRembSaisie();
			break;
		case REMB_SAISIE_RMH:
			configuredValue=AppCfgConfig.getInstance().getRembSaisieRMH();
			break;
		case REMB_CONSULT:
			configuredValue=AppCfgConfig.getInstance().getRembConsult();
			break;
		case META4DAI:
			configuredValue=AppCfgConfig.getInstance().getMeta4Dai();
			break;
		case NantesEtudiant:
			configuredValue=AppCfgConfig.getInstance().getNantesEtudiant();
			break;
		case CRC:
			configuredValue=AppCfgConfig.getInstance().getCrcLicence();
			break;
		case SIGNATURE:
			configuredValue=AppCfgConfig.getInstance().getSignatureLicence();
			break;
		case DP_GENERATION:
			configuredValue=AppCfgConfig.getInstance().getDpGenerationLicence();
			break;
		default:
			break;
		
		
		}
		}
		catch(MissingConfiguration e) {
			//logger.error(e.getMessage());
		}
		
		return configuredValue==null ? false : configuredValue.equals("1");
	}
	private CompteFinancierBean getCFBean(){
		return (CompteFinancierBean)Helper.findBean("cfBean");
	}
	public boolean isGbcp() {		
		return BudgetHelper.getBooleanConfigParam("gbcp.module");
	}
	public boolean isDas() {		
		return BudgetHelper.getBooleanConfigParam("das.module");
	}
	public boolean isGts() {		
		return BudgetHelper.getBooleanConfigParam("gts.module");
	}
	public String goToInfocentreOutcome()
	{
		DematBean dematBean = getDematBean();
		dematBean.setCodeExport(PeriodeEnum.Annuel_def.getCodeInfoCentre());
		dematBean.search();
		prepare(Action.INTERFACE_MINEFI);
		return Action.INTERFACE_MINEFI.getOutcome();
		
	}
	public String goToRemboursSaisie() {
		getSctBean().reset();		
		prepare(Action.remboursement_SAISIE);
		return Action.remboursement_SAISIE.getOutcome();
	}
	private SepaCreditTransfertBean getSctBean()
	{
		return (SepaCreditTransfertBean)Helper.findBean("sctBean");
		}
	
	public String goToRemboursConsult() {
		SepaCreditTransfertBean bean=getSctBean();
		bean.reset();		
		bean.setVagueExerciseList(null);
		prepare(Action.remboursement_CONSULT);
		return Action.remboursement_CONSULT.getOutcome();
	}
	public String goToPAYMEN_SEPA() {
		getSctBean().reset();		
		prepare(Action.PAYMEN_SEPA);
		return Action.PAYMEN_SEPA.getOutcome();
	}
	public String goToRemboursGeneration() {
		getSctBean().reset();		
		prepare(Action.remboursement_GENE);
		return Action.remboursement_GENE.getOutcome();
	}
	
	public String goToMeta4daiSaisie() {
		//getDaiBean().reset();	
		getDaiBean().initialize();
		prepare(Action.meta4dai_SAISIE);
		return Action.meta4dai_SAISIE.getOutcome();
	}
	public String goToMeta4daiConsult() {
		getDaiBean().initialize();	
		getDaiBean().setPeriodExerciseList(null);
		prepare(Action.meta4dai_CONSULT);
		return Action.meta4dai_CONSULT.getOutcome();
	}
	private DaiInterfaceBean getDaiBean()
	{
		return (DaiInterfaceBean)Helper.findBean("daiBean");
		}
	
	public String goToImportDepenses() {
		//getImportRecetteBean().reset();		
		prepare(Action.IMPORT_DEPENSE);
		return Action.IMPORT_DEPENSE.getOutcome();
	}
	public String goToChargementEtudiant() {
		logger.debug("goToChargementEtudiant: start");
		
		IBasicBean bean=getStudentBean();
		bean.initialize();
		
		prepare(Action.etudiant_chargement);
		return Action.etudiant_chargement.getOutcome();
	}
	private StudentBean getStudentBean()
	{
		return (StudentBean)Helper.findBean("studentBean");
	}
	public String goToMiseAjourEtudiant() {
		IBasicBean bean=getStudentBean();
		bean.initialize();
		prepare(Action.ETUDIANT_MISEAJOUR);
		return Action.ETUDIANT_MISEAJOUR.getOutcome();
	}
	public String goToGenerationDP() {
		IBasicBean dpBean = BudgetHelper.getDpBean();
		dpBean.initialize();
		prepare(Action.ETUDIANT_GENERATION_DP);
		return Action.ETUDIANT_GENERATION_DP.getOutcome();
	}
	
	public String goToCrcDepense() {
		CrcBean bean = getCrcBean();
		bean.reset();
		prepare(Action.CRC_DEPENSE);
		return Action.CRC_DEPENSE.getOutcome();
	}

	public String goToCrcRecette() {
		CrcBean bean = getCrcBean();
		bean.reset();

		prepare(Action.CRC_RECETTE);
		return Action.CRC_RECETTE.getOutcome();
	}
	private CrcBean getCrcBean()
	{
		return (CrcBean)Helper.findBean("crcBean");
	}
	
	public String goToSignaturePage() {
		IBasicBean bean = BudgetHelper.getSignatureBean();
		bean.initialize();
		prepare(Action.SIGNATURE);
		return Action.SIGNATURE.getOutcome();
	}
	public String goToInitMeta4Dai() {
		IBasicBean bean = BudgetHelper.getInitializationBean();
		bean.initialize();
		prepare(Action.INITIALIZE_META4DAI);
		return Action.INITIALIZE_META4DAI.getOutcome();
	}
	
	public String goToInitGTS() {
		IBasicBean bean = BudgetHelper.getInitializationBean();
		bean.initialize();
		prepare(Action.INITIALIZE_GTS);
		return Action.INITIALIZE_GTS.getOutcome();
	}
	public String goToDpGeneration() {
		IBasicBean bean = BudgetHelper.getDpBean();
		return prepare(Action.DP_GENERATION,bean);
	}
//	public boolean checkRequiredSignature() {
//		if(isDaSignatureRequired()||isEjSignatureRequired()||isLiqSignatureRequired()||isAsfSignatureRequired()||isVsfSignatureRequired()||isOpSignatureRequired()||isOrSignatureRequired()
//				||isLrecSignatureRequired()||isArSignatureRequired())
//		return true;
//		return false;
//	}
	
	
	
	
}

