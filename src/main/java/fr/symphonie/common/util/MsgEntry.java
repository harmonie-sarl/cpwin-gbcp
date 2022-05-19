package fr.symphonie.common.util;

public class MsgEntry {

	public static final String USER_NAME = "login.username";
	public static final String EXERCICE = "cpwin.exercice";
	public static final String DATE_LIMITE = "cpwin.dateLimite";
	public static final String GROUPE_DEST = "cpwin.groupeDest";
	public static final String SYNTHESE_LIST_Excel = "cpwin.tableauBord.synthese";
	public static final String DEPARTEMENT_LIST_Excel = "cpwin.tableauBord.prefecture";
	public static final String SF_DEPARTEMENT_LIST_Excel = "cpwin.tableauBord.detail.prefecture";
	public static final String DRJS_LIST_Excel = "cpwin.tableauBord.drjs";
	public static final String REGION_LIST_Excel = "cpwin.tableauBord.region";
	public static final String GROUPE_DEST_WITH_ARG = "cpwin.tableauBord.groupedest";
	public static final String PROGRAMME_WITH_ARG = "cpwin.tableauBord.programme";
	public static final String LAST_EXEC_DATE_MSG = "cpwin.tableauBord.lastExecutionDateMsg";
	public static final String DETAILS_REGION_LIST_Excel = "cpwin.tableauBord.details.region";
	public static final String CODE_PAYS = "code.pays";
	public static final String CODE_BANQUE_ABSORBEE = "code.banque.absorbe";
	public static final String DATE_DE_FIN_DIFFUSION = "date.de.fin.diffusion";
	public static final String AVEC_IBANS_OUVERTS = "avec.ibans.ouverts";
	public static final String CODE_TIERS = "code.tiers";
	public static final String ADRESSE = "adresse";
	public static final String DATE_DERNIERE_NOTIFICATION = "date.derniere.notification";
	//public static final String AJAX_MIN_QL = "ajax.min.QL";
	//public static final String AJAX_MAX_RESULT = "ajax.max.result";
	public static final String AJAX_MSG = "ajax.title.msg";
	public static final String SERVICE_CONVERSION_ERROR = "service.conversion.error";

	public static final String AUCUN_ENVELOP_MSG = "import.aucun.envelop";
	public static final String ENVELOP_INEXISTANT_MSG = "import.envelop.inexistant";
	public static final String ENVELOP_DEJA_VENTILLE = "import.envelop.ventillation.encours";
	public static final String SERVICE_INEXISTANT_MSG = "import.enveloppe.ligneBude.service.enexistant";
	public static final String PROGRAM_INEXISTANT_MSG = "import.enveloppe.ligneBude.program.enexistant";
	public static final String DESTINATION_INEXISTANT_MSG = "import.enveloppe.ligneBude.destination.enexistant";
	public static final String NIVEAU_DEST = "niveau.dest";
	public static final String NIVEAU_ORIGINE = "niv.orig";
	

	/**
	 * Messages pour validateur montant
	 */
	public static final String MNT_CONVERT_ERR_MSG = "montant.convert.error.msg";

	/*** pour les colonnes export excel ****/
	public static final String CPWIN_CREDIT = "cpwin.credit";
	public static final String CPWIN_OUVERT = "cpwin.ouvert";
	public static final String CPWIN_CREDITS = "cpwin.credits";
	public static final String CPWIN_REPORTES = "cpwin.reportes";
	public static final String CPWIN_MNT = "cpwin.montant";
	public static final String CPWIN_ENG = "cpwin.engage";
	public static final String CPWIN_POUR_MNT = "cpwin.pourCentageMontant";
	public static final String CPWIN_ENG_CO = "cpwin.engageCO";
	public static final String CPWIN_PEC = "cpwin.pec";
	public static final String CPWIN_REPORTS = "cpwin.desReports";
	public static final String CPWIN_DENG = "cpwin.dengagement";
	public static final String CPWIN_ENGAGEMENT = "cpwin.engagement";
	public static final String CPWIN_PEC_CO = "cpwin.pecCO";
	public static final String CPWIN_MNT_PEC = "cpwin.montantPEC";
	public static final String CPWIN_TOT = "cpwin.total";
	public static final String CPWIN_TOT_ACSE = "cpwin.totalAcse";
	public static final String CODE_BANQUE = "code.banque";
	public static final String DESIGNATION = "designation";
	public static final String DATE_FIN_DIFFUSION = "date.fin.diffusion";
	public static final String NBRE_IBANS_OUVERTS = "nombre.ibans.ouverts";
	public static final String CPWIN_ETABLISSEMENTS_ABSORBES = "cpwin.etablissements.absorbes";
	public static final String CPWIN_NOTIFICATION_TIERS = "cpwin.notification.tiers";

	/*** pour les colonnes export excel enveloppes budgetaires ****/
	public static final String ENVELOP_BUDG_REPORT_TITRE = "envelop.budg.titre";
	public static final String LIGN_BUDG_REPORT_TITRE = "lign.budg.titre";
	public static final String GROUPE_NATURE = "nature";
	public static final String GROUPE_DESTINATION = "destination";
	public static final String PROGRAMME = "programme";
	public static final String MONTANT = "montant.ae";
	public static final String NATRURE_DESTINATION = "nature.destination";
	public static final String SERVICE = "service";
	/** Pour la validation **/
	public static final String CPWIN_DATE_LIMIT_ERROR = "cpwin.dateLimite.error";
	/** Pour le reporting **/
	public static final String REPORTING_PATH = "reporting.path";
	public static final String CPWIN_REPORTING_SYN = "cpwin.rep.synthese";
	public static final String CPWIN_REPORTING_TITACSE = "cpwin.rep.titleAcse";
	public static final String CPWIN_REPORTING_DEPT = "cpwin.rep.prefecture";
	public static final String CPWIN_REPORTING_TITDEPT = "cpwin.rep.titleDept";
	public static final String CPWIN_REPORTING_REG = "cpwin.rep.region";
	public static final String CPWIN_REPORTING_DETAILS_REG = "cpwin.rep.detailRegion";
	public static final String CPWIN_REPORTING_DETAILS_ALLREG = "cpwin.rep.detailsAllRegions";
	public static final String CPWIN_REPORTING_TITREG = "cpwin.rep.titleRegion";
	public static final String CPWIN_REPORTING_TITRE_DETAILS_REGION = "cpwin.rep.title.details.region";
	public static final String CPWIN_REPORTING_DEPTREG = "cpwin.rep.deptRegion";
	public static final String CPWIN_REPORTING_TITDEPTREG = "cpwin.rep.titleDeptRegion";
	public static final String CPWIN_REPORTING_TITALLREG = "cpwin.rep.titleAllRegion";
	public static final String CPWIN_REPORTING_DEPT_P147 = "cpwin.rep.prefecture.P147";
	public static final String CPWIN_REPORTING_AVIS_REGLEMENT = "cpwin.rep.avis.reglement";
	public static final String CPWIN_REPORTING_AVIS_REGLEMENT_PIECES = "cpwin.rep.avis.reglement.pieces";
	/** Pour le print List **/
	public static final String CPWIN_PRINT_SYNTHESE = "cpwin.print.synthese";
	public static final String CPWIN_PRINT_DEPT = "cpwin.print.dept";
	public static final String CPWIN_PRINT_DRJSCS = "cpwin.print.drjscs";
	public static final String CPWIN_PRINT_REGION = "cpwin.print.region";
	public static final String CPWIN_PRINT_DETAILS_REGION = "cpwin.print.detail.region";
	public static final String CPWIN_PRINT_DETAILS_TOUS_REGIONS = "cpwin.print.detail.tous.regions";
	public static final String CPWIN_PRINT_SUMMARY_REGIONS_PRINT = "cpwin.print.summary.regions";
	/** Pour le print List des charts **/
	public static final String CPWIN_PRINT_CHART_SIEGE = "cpwin.printChart.siege";
	public static final String CPWIN_PRINT_CHART_DEPT = "cpwin.printChart.dept";
	public static final String CPWIN_PRINT_CHART_DRJSCS = "cpwin.printChart.drjscs";
	public static final String CPWIN_PRINT_CHART_DEPT_DRJSCS = "cpwin.printChart.deptDrjscs";
	/** Les mois **/
	public static final String JANVIER = "janvier";
	public static final String FEVRIER = "fevrier";
	public static final String MARS = "mars";
	public static final String AVRIL = "avril";
	public static final String MAI = "mai";
	public static final String JUIN = "juin";
	public static final String JUILLET = "juillet";
	public static final String AOUT = "aout";
	public static final String SEPTEMBRE = "septembre";
	public static final String OCTOBRE = "octobre";
	public static final String NOVEMBRE = "novembre";
	public static final String DECEMBRE = "decembre";
	public static final String CPWIN_DELAI_INCORRECT = "cpwin.delai.incorrect";
	/** pour les paramètres des charts **/
	public static final String POUR_DEPT = "pour.dept";
	public static final String POUR_SIEGE = "pour.siege";
	public static final String POUR_DRJSCS = "pour.drjscs";
	public static final String POUR_DEPT_DRJSCS = "pour.dept.drjscs";
	public static final String POUR_TOUS = "pour.tous";
	/** pour le module de suivi financier **/
	public static final String EXERCICE_ORIGINE = "exercice.origine";
	public static final String REFERENCE_ENGAGEMENT = "reference.engagement";
	public static final String POURCENTAGE = "pourcentage";
	public static final String MANDATE_EXERCICE = "mandates.exercice";
	public static final String RESTE_PAYER = "reste.payer";
	public static final String AJOUT_ABSORPTION_REUSSI = "ajout.absorption.reussi";
	public static final String UPDATE_ABSORPTION_REUSSI = "update.absorption.reussi";
	public static final String ERROR_ABSORPTION = "error.absorption";
	public static final String CPWIN_REQUIRED = "cpwin.required";
	public static final String FORMAT_DATE_INVALIDE = "date.format.invalide";
	public static final String DOCUMENT_REQUIRED = "document.required";
	/** pour le module du Tiers */
	public static final String DATE_FIN_INFERIEUR = "date.fin.inferieur";
	public static final String DATE_DEB_SUPERIEUR = "date.deb.superieur";
	public static final String CPWIN_ADRESSE_REQUIRED = "cpwin.adresse.required";
	public static final String CPWIN_ADRESSE_OFFICIELLE = "cpwin.adresse.officielle";
	public static final String INVALIDE_IBAN = "iban.invalide";
	public static final String CPWIN_EMAIL_INVALIDE = "email.invalide";
	public static final String CODE_TIERS_REQUIRED = "tiers.code.required";
	public static final String REQUIRED_FIELDS = "requiredMsg";
	public static final String TIERS_EXISTE = "tiers.existe";
	public static final String TIERS_SAISI_PT = "tiers.saisi";
	public static final String TIERS_CONSULT_PT = "tiers.consult";
	public static final String TIERS_MODIF_PT = "tiers.modif";
	// public static final String ADD_TIERS_SU_MSG = "tiers.ajout.success";
	public static final String ENREG_TIERS_SU_MSG = "tiers.enreg.success";
	// public static final String UPDATE_TIERS_SU_MSG = "tiers.update.success";
	// public static final String ADD_TIERS_FAIL_MSG = "tiers.ajout.failure";
	public static final String ENREG_TIERS_FAIL_MSG = "tiers.enreg.failure";
	public static final String MENU_BUDGET_PLURI = "menu.budg.pluri.annuel";
	// public static final String ADRESSAGE_BUDGET_PLURI =
	// "menu.budg.pluri.annuel";
	public static final String MONTANT_ENGAG_AUTOR = "montant.engagement.autorise";
	public static final String MONTANT_CP_LIST_NON_AUTORISE = "montant.credit.non.autorie";
	/** Prefix pour les messages d'erreur **/
	public static final String SUCCES = "operation.succes";
	public static final String FAILED = "operation.failed";
	/*** Messages pour la gestion budgétaire ***/
	public static final String DUPLICATE_ENVELOPP_MSG = "duplicate.envelopp.msg";

	public static final String MODIF_INTERLOC_RECOUVREMENT_MSG = "modif.interloc.recouvrement";
	public static final String PAS_DE_MANDATS_A_IMPRIMER = "pas.de.mandats.a.imprimer";
	public static final String tiers_demat_path = "tiers.demat.path";

	public static final String MENU_PAR_ENVELOP = "menu.tabl.bord.parenvelop";
	public static final String MENU_PAR_COMPRECV2 = "menu.tabl.bord.parComptRecV2";
	public static final String MENU_SUIVI_CP = "menu.tabl.bord.suiviCP";
	public static final String MENU_SUIVI_REC = "menu.tabl.bord.suiviRECETTE";
	public static final String MENU_COMPTE_REND_GEST = "menu.tabl.bord.comptRenduGestion";
	public static final String MENU_COMPTE_COMPTBLE_DEP = "menu.tabl.bord.parccdep";
	public static final String MENU_PARAMETRAGE = "menu.parametrage";

	public static final String MENU_TAB_BORD = "menu.tabl.bord";
	public static final String MENU_BUDG_INITIAL = "menu.budg.pluri.bi";
	public static final String MENU_BUDG_RECTIF = "menu.budg.pluri.br";
	public static final String MENU_DEPLOIMENT = "menu.budg.pluri.deploi";
	public static final String MENU_SAISI_CP = "menu.saisi.cp";
	public static final String MENU_VENTILATION = "menu.ventillation";
	public static final String MENU_EDITIONS = "menu.editions";
	public static final String MENU_VENTIL_SERV_PROG = "ventillation.service";
	public static final String MENU_VENTIL_DEST = "ventillation.dest";
	public static final String MENU_VENTIL_GEST = "ventillation.gest";
	public static final String MENU_TIERS = "menu.tiers";
	public static final String MENU_VENTIL_AE_RECETTE_ORIGINE = "ventillation.ae.recette.origine";
	public static final String MENU_EXTERN_DATA = "btn.edition.externe.data";
	public static final String MENU_SITUA_PATRIM = "menu.situation.patrim";

	public static final String NUM_EJ = "noej";
	public static final String RAP_01 = "rap.01";
	public static final String TIERS = "tiers";
	public static final String OBJET = "cpwin.objet";
	public static final String NUM_EI = "cp.ei";
	public static final String DP_EMISE = "dp.emises";
	public static final String CP_DP_PEC = "cp.dp.pec";
	public static final String CREDI_ANNUL = "credits.annul";
	public static final String RESTE_APAYER = "reste.apayer";

	public static final String SUR_AE_ANT = "sur.ae.anterieur";
	public static final String SUR_AE_EXEC = "surae.exercice";
	public static final String BUDGET = "cpwin.budget";

	public static final String NUM_EXEC_CP = "num.exec.cp";
	public static final String CODE_BUDG = "cpwin.budget";
	public static final String NAT_GRP = "nat.grp1";
	public static final String CODE_DEST = "code.dest";
	public static final String CODE_NAT = "import.nature";
	public static final String CODE_SERV = "code.service";
	public static final String CODE_DIRECTION = "code.direction";
	public static final String CODE_PROG = "code.prog";
	public static final String DP_SAISI = "dp.saisie";
	public static final String MONTANTCP = "montant";
	public static final String NOM_PROG = "nom.prog";

	public static final String BR_EN_COURS_ERROR = "br.en.cours.error";

	public static final String BI_NON_VALIDE_ERROR = "bi.non.valide.error";
	public static final String VENTIL_MISSED_ERR = "ventil.missed.err";
	public static final String VENTIL_A_VERIFIER_ERR = "ventil.verifier.err";
	public static final String BR_OUVERT_ERROR = "br.vant.ouvert.error";
	public static final String INTERFACE_MINEFI = "menu.interface.minefi";
	public static final String EDITION_REQUIRED_DATA = "gbcp.edition.requiredData.err";
	public static final String COMPTE_FINANCIER = "menu.compte.financier";
	public static final String PARAM_BILAN = "menu.param.bilan";
	public static final String PARAM_CR = "menu.param.cr";
	public static final String BILAN = "bilan";
	public static final String CR = "cr";
	public static final String CF = "cf";
	public static final String VALIDATION = "validation";
	public static final String MENU_TRESORERIE = "menu.tresorerie";
	public static final String MENU_SIMULATION = "menu.simulation";
	public static final String MENU_AJUSTEMENT = "menu.ajustement";

	public static final String TOTAL_VENTILE_ECRITURE_ERROR = "total.ventile.ecriture.err";
	public static final String MNT_ECRITURE = "ecriture.montant.sens";
	public static final String MENU_DAS = "menu.das";
	public static final String MENU_CPWIN = "menu.cpwin";
	public static final String MENU_CONSULT = "menu.consult";
	public static final String HONORAIRE = "honoraire";
	public static final String IMPORT = "importVentilation";
	public static final String GENERER = "generer";
	public static final String SUIVI_TIERS = "suivi.tiers";

	public static final String TIERS_NOT_FOUND = "tiers.notfound";
	public static final String TIERS_CONVERSION_ERROR = "tiers.conversion";
	public static final String REMUN_VERSE = "remun.verse";
	public static final String DEDATIL_HONORAIRE = "detail.honoraire";
	public static final String NUM_TIERS = "num.tiers";
	public static final String RAISON_SOCIAL = "raison.sociale";
	public static final String LOGIN_LASTNAME = "login.lastname";
	public static final String LOGIN_FIRST_NAME = "login.firstname";
	public static final String HONORAIRES = "honoraires";
	public static final String COMISSIONS = "commissions";
	public static final String COURTAGE = "courtage";
	public static final String RISTOURNES = "ristournes";
	public static final String JETON_PRESENCE = "jetons.presence";
	public static final String DROIT_AUTEUR = "droit.auteur";
	public static final String DROIT_INVENT = "droit.invent";
	public static final String AUTRES_REMUN = "autres.remun";
	public static final String INDEM_REMBOURS = "indemnites.remboursements";
	public static final String AVANTAGES_NATURE = "avantetages.nature";
	public static final String RETENUE_SOURCE = "retenue.source";
	public static final String MODIF_DEPLOI = "cpwin.modification";
	public static final String DUPLICATE_LIG_DEP_MSG = "duplicate.ligneDep.msg";
	public static final String CONSULT_DEPLOI = "menu.consult";
	public static final String PREVISION_REALISEE = "plan.tresorerie.initiale.error";
	public static final String ANNUEL_REALISEE = "plan.tresorerie.annuel.error";
	public static final String MENU_INITIALISATION = "menu.initialisation";
	public static final String DEPLOI_SUCCES = "deploiement.succes";
	public static final String MENU_SAISI = "menu.saisi";
	public static final String BI_CHRG_SUCCESS = "minef.bi.succes";
	public static final String BR_CHRG_SUCCESS = "minef.br.succes";
	public static final String NO_VALID_DATA_FILE_MSG = "no.valid.data.file";

	public static final String MENU_REFERENTIEL = "referentiel";
	public static final String MENU_ARTICLE = "article";
	public static final String MENU_BILLETTERIE = "billetterie";
	public static final String MENU_PERIODE = "periode";
	public static final String DUPLICATE_ARTICLE_MSG = "article.existe";
	public static final String MENU_PARAM = "menu.parametrage";
	public static final String DUPLICATE_COMPTE_MSG = "duplicate.compte.msg";
	public static final String MENU_CLIENT = "clients";
	public static final String DUPLICATE_ARTICLE_DETAIL_MSG = "article.detail.existe";
	public static final String DUPLICATE_CLIENT_GTS_MSG = "client.gts.existe";
	public static final String PERIODE_TRAITE_ERROR = "periode.traite.error";
	public static final String PERIODE_CHARGE_WARN = "periode.charge.warn";
	public static final String PERIODE_AU_MOIN_CHARGE_ERROR = "periode.au.moins.charge.error";
	public static final String PERIODE_PRECEDENTE_NON_TRAITEE = "periode.precedente.non-traitee";
	public static final String PERIODE_PRECEDENTE_ENCOURS = "periode.precedente.en-cours";
	public static final String GENERATION = "generation";
	public static final String NUMERO_PERIODE = "numero";
	public static final String ETAT_PERIODE = "cpwin.etat";

	public static final String PERIODE = "periode";
	public static final String DESTINATION = "gts.destination";
	public static final String NATURE = "gts.nature";
	public static final String DONT_REGISSEUR = "dont.regisseur";
	public static final String DONT_CLIENT = "dont.clients";
	public static final String RECET_LIGN_BUDG = "recettes.ligne.budgetaire";
	public static final String RECET_CODE_ARTICLE = "recettes.code.article";
	public static final String CODE_ARTICLE = "code.article";
	public static final String SUPPR_DETAIL_ARTICLE = "suppression.article";
	public static final String MNT_TOTAL = "montant.total";
	public static final String LIQ_AGENERER = "liquid.recet.generer";
	public static final String PRESTATION = "prestation";
	public static final String QUANTITE = "quantite";
	public static final String PRIX_UNIT = "prix.unitaire";
	public static final String TAUX_REMISE = "taux.remise";
	public static final String MNT_REMISE = "mnt.remise";
	public static final String TAUX_TVA = "taux.tva";
	public static final String MNT_TVA = "mnt.tva";
	public static final String MNT_HT = "mnt.ht";
	public static final String MNT_TTC = "mnt.ttc";
	public static final String LIQ_REC="liquid.recet";
	public static final String NO_TITRC="no.titrec";
//	public static final String MNT_INI_HT="mnt.init.ht";
	public static final String MNT_INITIAL="rar.ini.ht";	
	public static final String RAR_HT_01="rar.ht.01.01";
	public static final String RAR_TTC_01="rar.ttc.01.01";	
	
	public static final String MNT_INI_TTC="mnt.init.ttc";
//	public static final String RAR_HT="rar.ht";
//	public static final String RAR_NET_HT="rar.net.ht";	
	public static final String MNT_NET_HT="mnt.net.ht";	
//	public static final String RAR_TTC="rar.ttc";
//	public static final String RAR_NET_TTC="rar.net.ttc";
	public static final String MNT_NET_TTC="mnt.net.ttc";	
//	public static final String SOLD_HT="solde.ht";
	public static final String RECET_HT="recet.ht";
	public static final String SOLD_TTC="solde.ttc";
	public static final String NO_LTR="no.lig.tr";
	public static final String TR_ANT="tr.anterieur";
	public static final String TR_EXER="tr.exercice";
	public static final String NO_LREC="no.lrec";
	public static final String TIERS_ORIGIN="cpwin.tiers.origine";
	public static final String REF_COMMAND="ref.commande";
	public static final String EXEC_ORIG="exec.orig";
	public static final String AR_HT="ar.ht";
	public static final String AR_TTC="ar.ttc";
	public static final String NOM_TIERS="nom.tiers";
	public static final String ORIGINE="origine";
	public static final String ONB_HT="onb.ht";
	public static final String ONB_TTC="onb.ttc";
	public static final String TOTAL="total";
	public static final String NET_HT="net.ht";
	public static final String NET_TTC="net.ttc";
	public static final String MENU_CONCILIATION = "menu.conciliation";
	public static final String CHARGEMENT = "chargement";
	public static final String SUIVI = "suivi";
	public static final String CP = "cp";
	public static final String RECETTES_MINUS = "recettes.minus";
	public static final String IMPORT_RECETTE = "menu.import.recette";
	public static final String AE = "ae";
	public static final String COMPL = "suiviae.compl";
	public static final String DPANT = "suiviae.dpAnt";
	public static final String RAP = "rap";
	public static final String AE_CO = "suiviae.aeco";
	public static final String EJ = "suiviae.ej";
	public static final String DP_EX = "suiviae.dpex";
	public static final String RAP_ANT = "suiviae.rapAnt";
	public static final String RAP_EXERC = "suiviae.rapExer";
	public static final String DP_TOT = "suiviae.dptot";
	public static final String DR = "suiviae.dr";
	public static final String DP_NET = "suiviae.dpnet";
	public static final String RAP_ACT = "suiviae.rapActual";
	public static final String NON_ENG = "suiviae.nonEngage";
	public static final String A_PAYER_SUR = "suiviae.aPayerSur";
	public static final String CP_VOTE = "suiviae.cpVote";
	public static final String DISPO = "suiviae.dispo";
	public static final String BESOIN = "suiviae.besoinCP";
	public static final String SUIVI_AE_CP = "suivi.aecp";
	public static final String MONTANT_TOTAL = "montant.total";
	public static final String PLAN_EX_COURANT = "plan.cp.excourant";
	public static final String PLAN_EX_SUP = "plan.cp.exsuperieur";
	public static final String EJ_PLURI = "ej.pluri";
	public static final String SUIVI_SF = "ctrlImputSF";
	public static final String AE_CP_FLECHE = "ae.cp.flechees";
	public static final String IMPORT_VIREMENT = "menu.rembours.masse";
	public static final String REMB_GENERATION_MSG ="rembours.generation.msg";
	public static final String REMB_VALIDATION_MSG ="rembours.validation.msg";
	public static final String REMB_CRC_MSG ="rembours.import.crc";
	public static final String REMB_HORS_SEPA_MSG ="sepa.paiement.hors";
	public static final String REMB_NUM_VAGUE ="num.vague";
	public static final String REMB_NOM_CLIENT ="rembours.nom.client";
	public static final String REMB_IBAN ="iban";
	public static final String REMB_BIC ="bic";
	public static final String REMB_MODE_PAIEMENT ="mode.paiement";
	public static final String REMB_NO_ECRITURE ="numero.ecriture";
	public static final String REMB_NO_BORDEREAU ="cpwin.num.bordereau";
	public static final String REMB_DATE_BORDEREAU ="rembours.date.bord";
	public static final String REMB_CONSULT_CLIENT ="rembours.consult.client";
	public static final String RMH_DDFiP_VALID_MSG ="rembours.RMH.DDFiP.msg";
	public static final String REMB_NOM ="nom";
	public static final String REMB_PRENOM ="prenom";
	public static final String REMB_NUM_VAGUE_MSG ="rembours.vague.num.details";
	public static final String RMH_DDFiP="rembours.RMH.DDFiP";
	public static final String SEPA="rembours.sepa";
	public static final String META4DAI="menu.meta4Dai";
	public static final String ETUDIANT="menu.nantes.etudiant";
	public static final String ETUDIANT_CHARGEMENT="menu.nantes.etudiant.chargement";
	public static final String ETUDIANT_MISEAJOUR="menu.nantes.etudiant.miseAjour";
	public static final String ETUDIANT_GENERATIONDP="menu.nantes.etudiant.generationDP";
	public static final String OBJET_EI="object.ei";
	public static final String DISPONIBLE_EI="disponible.ei";
	public static final String DISPONIBLE_APRES="disponible.apres";
	public static final String DISPONIBLE_LIMITE="disp.limite";
	public static final String PARAM_ABSENT="param.absent";
	public static final String BTN_ADD="btn.add";
	public static final String NUM_DAI="num.dai";
	public static final String META4DAI_IMPORTED_DATA="dai.paie.titre";
	public static final String DEP_PAIE="dep.paie";
	public static final String AUTRES_DEP="autres.paie";
	public static final String TOTAL_DEP="tot.dep";
	public static final String DISPONIBLE="disponible";
	public static final String META4DAI_DISPLAY_DATA="titre.excel.meta4dai.consult";
	public static final String CODE_NATURE = "code.nature";
	public static final String CRC="menu.crc";
	public static final String CRC_IMPRESSION_PJ="menu.crc.impression.pj";
	public static final String CRC_MANDATS="menu.mandats";
	public static final String CRC_DEPENSE="depenses.crc";
	public static final String CRC_RECETTE="recettes.minus";
	public static final String CRC_CONSULTATION="crc.consul.pj";
	public static final String DP_SAISIE="dp.saisie";
	
	
	

	
	/** Pour export excel das consult-tiers **/
	
	public static final String TIERS_TYPE = "cpwin.type";
	public static final String BUR_DISTR = "bureau.distributeur";
	public static final String NUM_VOIE = "num.dans.voie";
	public static final String NAT_VOIE = "nature.voie";
	public static final String NOM_VOIE = "nom.voie";
	public static final String SIRET = "siret";
	public static final String DAS_PROFESSION = "das.profession";
	public static final String CONSULT_TIERS = "tiers.consult";
	
	/** Pour module Tiers import depenses **/
	public static final String IMPORT_DEPENSE = "menu.import.depense";
	public static final String ETUDIANTS_CHARGEMENT = "menu.chargement.scolarite";
	public static final String ETUDIANT_MAIL_SUBJECT="nantes.etudiant.mail1.subject";	
	public static final String ETUDIANT_MAIL_MESSAGE="nantes.etudiant.mail1.message";
	public static final String ETUDIANT_RELANCE_SUBJECT="nantes.etudiant.relance.subject";	
	public static final String ETUDIANT_RELANCE_MESSAGE="nantes.etudiant.relance.message";
	public static final String ETUDIANT_VALIDE="nantes.etudiant.valide";
	public static final String ETUDIANT_TROUVE="nantes.etudiant.trouve";
	public static final String BIC_MAIL_SUBJECT="nantes.etudiant.bic.manquant.subject";	
	public static final String BIC_MAIL_MESSAGE="nantes.etudiant.bic.manquant.message";
	public static final String ETUDIANT_PUBLIC_SITE="nantes.etudiant.site";
	public static final String BIC_MAIL_TO="nantes.support.mail";
	public static final String MAIL_INFO_DISABLED="mail.info.disabled";
	
	
	
	
	public static final String DISPO_Insuffisant="disponible.error";
	public static final String NATURE_NON_F="nature.F.error";
	public static final String MENU_OUTILS = "menu.outils";
	public static final String MENU_SIGNATURE = "menu.signature";
	public static final String SIGNATURE_ERROR ="signature.dimension.error";
	public static final String SIGNATURE_MAIL_SUBJECT="signature.mail.subject";	
	public static final String SIGNATURE_MAIL_MESSAGE="signature.mail.message";
	public static final String SIGNATURE_DUPLICAT="signature.duplicat";
	
	/** Pour module billetterie lemans**/
	public static final String MENU_LEMANS_BILLETTERIE = "billetterie.lemans";
	public static final String MENU_SPECTACLE = "spectacle";
	public static final String DUPLICATE_SPECTACLE_DETAIL_MSG = "spectacle.detail.existe";
	public static final String SUPPR_DETAIL_SPECTACLE = "suppression.spectacle";
	public static final String DUPLICATE_SPECTACLE_MSG = "spectacle.existe";
	
	
	
	
	
	
	
	
	
	

	
	
	
	
	
	
	
	
	
	
	
	
	

	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
		
}
