package fr.symphonie.common.util;

import java.nio.file.Path;
import java.nio.file.Paths;

import fr.symphonie.common.model.AppCfgConfig;
import fr.symphonie.exception.MissingConfiguration;


public class Constant {
	public static final String HTML_CONTENT_TYPE="text/html";
	public static final String PDF_CONTENT_TYPE="application/pdf";
	public static final String PNG_CONTENT_TYPE="image/png";
	public static final String PDF="pdf";
	public static final String TIERS = "Tiers";
	private static Integer maxAjaxResult=null;
	private static Integer minQueryLenght=null;
	public static Integer maxResult=50;
	
	public static String NATURE_1="Nature1";
	public static String NATURE_2="Nature2";
	public static String NATURE_3="Nature3";
	public static String NATURE_4="Nature4";
	public static String DESTINATION_1="Destination1";
	public static String DESTINATION_2="Destination2";
	public static String DESTINATION_3="Destination3";
	public static String DESTINATION_4="Destination4";
	public static String RESERVE="RESERVE";
	public static String SERVICE_1="Service1";
	public static String SERVICE_2="Service2";
	public static String PROGRAMME_1="Programme1";
	public static String PROGRAMME_2="Programme2";
	public static String EXERCICE_2013="2013";
	public static String EXERCICE_2014="2014";
	public static String EXERCICE_2015="2015";
	public static String tiersDematPath="ttiersdemat";
	public static final String CHARGE_RECOUV_PARAM = "fChargeRecouv";
	public static final String TEL_RECOUV_PARAM = "fTelRecouv";
	public static final String MAIL_RECOUV_PARAM = "fMailRecouv";
	
	public static final String FORMAT_MNT_PATTERN = "###,##0.00";
	public static final String NANTES_ENSA_LOGO="nantes-ensa-logo.png";
	public static final String NANTES_ENSA_STUDENT_PREFIX="SD";
	public static final String NANTES_ENSA_RIB_FileName="Etudiant_RIB_%s.pdf";
	public static final String CRC_PIECE_FileName="CRC_%s%s%s%d.pdf";
	public static final String CRC_FileName="CRC_%s%s%s.pdf";
	
	public static final Integer IMG_SIGNATURE_WIDTH=250;
	public static final Integer IMG_SIGNATURE_HEIGHT=150;
	// Les niveaux Depence et recette 
	
	public static final int PERSONNEL = 0;
	public static final int FONCTIONNEMENT = 1;
	public static final int INVESTISSEMENT = 2;
	public static final int INTERVENTION = 3;
	public static final int R11 = 0;
	public static final int R12 = 1;
	public static final int R13 = 2;
	public static final int R14 = 3;
	public static final int R15 = 4;
	public static final int R22 = 5;
	public static final int R24 = 6;
	public static final int R25 = 7;
	public static final int R26 = 8;
	public static final int R27 = 9;
	public static final int NO_TIERS_SHEET = 0;
	public static final int NO_HONORAIRES_SHEET = 0;
	
	public static final int EXERCICE = 0;
	public static final int SIRET = 1;
	public static final int SIREN = 2;
	public static final int CODE_APE = 3;
	public static final int RAISON_SOCIALE = 4;
	public static final int ADRESSE_N_VOIE = 5;
	public static final int NATURE_ET_NOM_VOIE = 6;
	public static final int CODE_POSTAL_BUR_DISTR = 7;
	public static final int BUREAU_DISTRIBUTEUR = 8;	
	public static final int AdresseLigne1 = 9;
	public static final int AdresseLigne2 = 10;
	public static final int Mode = 11;
	public static final String NO_VIR_CP_COMPTEUR_NAME="CPVIR";
	public static final String[] TVA_LIST=new String[] {"0.0","2.1","5.5","10.0","20.0"};
	public static final String SPACE_SEPARATOR = " ";
	public static final String RembMatriculeSeq = "CtaRembMatricule";
	public static final int batch_size=30;
	
	//params pour autorisations Signature
	public static final String SF_PARAM = "fSignature_da";
	public static final String DP_PARAM = "fSignature_dp";
	public static final String DR_PARAM = "fSignature_dr";
	public static final String DV_PARAM = "fSignature_dv";
	public static final String DA_PARAM = "fSignature_da";
	public static final String EJ_PARAM = "fSignature_ej";
	public static final String ASF_PARAM = "fSignature_asf";
	public static final String VSF_PARAM = "fSignature_vsf";
	public static final String LIQ_PARAM = "fSignature_liq";
	public static final String OR_PARAM = "fSignature_or";
	public static final String OP_PARAM = "fSignature_op";
	public static final String LREC_PARAM = "fSignature_lrec";
	public static final String AR_PARAM = "fSignature_ar";
	public static Path getProjectRepoPath() throws MissingConfiguration{
		String path=AppCfgConfig.getInstance().getReportingPath();
		Path root=Paths.get(path).getParent();
		return root;
		}
	public static Path getProjectRootPath() throws MissingConfiguration{
		String path=AppCfgConfig.getInstance().getRootPath();
		Path root=Paths.get(path);
		return root;
		}
	public static String getGeneratedCRCFileName(int exercice,String compte) {
		return String.format("CRC_%d_%s.pdf", exercice,compte);
	}
	public static String getSavedTiersFileName(int exercice) {
		return "DAS2_tiers_"+exercice+".xls";
	}
	public static String getSavedHonorairesFileName(int exercice) {
		return "DAS2_honoraires_"+exercice+".xls";
	}
	public static String getSepaCreditTransfertFileName() {
		return "SEPA_Virement.xls";
	}
	public static String getPrefixGtsFileName() {
		return "GTS_recettes_";
	}
	public static String getPrefixImportRecetteFileName() {
		return "IMP_recettes_";
	}
	public static String getPAYMENRootPath() throws MissingConfiguration {
		return getProjectRootPath().resolve("PAYMEN").toString();
	}

	public static Integer getMaxAjaxResult() {
		int DEFAULT_MAX_RESULT = 20;
		maxAjaxResult = DEFAULT_MAX_RESULT;

		return maxAjaxResult;
	}

	public static String getMinAjaxQL() {
		int DEFAULT_MIN_QL = 3;
		minQueryLenght = DEFAULT_MIN_QL;

		return "" + minQueryLenght;
	}
}
