package fr.symphonie.budget.core.dao.ps;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;

import fr.symphonie.sp.GenericPs;
import fr.symphonie.util.Helper;

public class GenericBudgetPs  extends GenericPs{

	private static final Logger logger = LoggerFactory.getLogger(GenericBudgetPs.class);
	protected static final String RESULT_LIST = "RESULT_LIST";
	protected static final String RETURN_VALUE="RETURN_VALUE";
	
	protected GenericBudgetPs(JdbcTemplate jdbcTemplate, String name) {
		super(jdbcTemplate, name);
	}

	@Override
	public String getCreateur() {
		return null;
	}
	@Override
	protected Logger getLogger() {
		return logger;
	}
	

	public static final String STYLE="@style";
	
	public static final String num_exec="@num_exec";
	public static final String num_exec_cp="@num_exec_cp";
	
	public static final String central="@central";
	public static final String code_util="@code_util";
	
	public static final String mode="@mode";
	
	public static final String code_budg="@code_budg";

	
	public static final String BudgetOS="@BudgetOS";
	public static final String BudgetHBA="@BudgetHBA";
	
	
	
	public static final String code_dest="@code_dest";
	public static final String code_prog="@code_prog";
	public static final String nat_grp="@nat_grp";
	public static final String TCODE="@TCODE";
	public static final String code_nature="@code_nature";
	public static final String num_nomenc="@num_nomenc";
	public static final String code_direction="@code_direction";
	public static final String code_service="@code_service";
	public static final String freserve="@freserve";
	public static final String depenses="@depenses";
	public static final String recettes="@recettes";	
	public static final String fongible="@fongible";
	public static final String fTorigine="@fTorigine";	
	public static final String consult="@consult";
	public static final String ouvert="@ouvert";	
	public static final String mdt_ordre="@mdt_ordre";
	public static final String dest_grp="@dest_grp";
	public static final String code_dirnon="@code_dirnon";
	public static final String gestion="@gestion";
	public static final String ServStr="@ServStr";
	public static final String num_nomenc_interne="@num_nomenc_interne";
	public static final String code_gest="@code_gest";
	public static final String TypeDest="@TypeDest"; 
	public static final String ProgStr="@ProgStr";
	
	
	public static final String ant_rap="@ant_rap";
	public static final String ant_dp_emise="@ant_dp_emise";
	public static final String ant_rae="@ant_rae";
	public static final String exec_mt_ej="@exec_mt_ej";
	public static final String exec_dp_emise="@exec_dp_emise";
	public static final String exec_rae="@exec_rae";
	
	public static final String session_id="@session_id";
	public static final String no_br="@no_br";
	public static final String action="@action";
	
	public static final String cpt_deb = "@cpt_deb";
	public static final String cpt_fin = "@cpt_fin";
	public static final String date_fin = "@date_fin";
	public static final String totalise = "@totalise";
	public static final String excl_typecr ="@excl_typecr";
	public static final String definitive = "@definitive";
	public static final String ToutBudget = "@ToutBudget";
	public static final String MultiBudgetHorsA = "@MultiBudgetHorsA";
	
	public static final String dateFin = "@date";
	public static final String niveau = "@niveau";
	public static final String modeCpAe = "@modeCpAe";
	public static final String mode_paie = "@mode_paie";
	public static final String date_reglt = "@date_reglt";
	public static final String no_vague = "@no_vague";
	public static final String no_ecr = "@no_ecr";	
	public static final String periode = "@periode";
	/**
	 * Tiers Attribues
	 */
	
	public static final String code_tiers = "@code_tiers";
	public static final String nom_tiers = "@nom_tiers";
	public static final String sigle = "@sigle";
	public static final String enseigne = "@enseigne";
	public static final String capital = "@capital";
	public static final String forme_soc = "@forme_soc";
	public static final String no_siren = "@no_siren";
	public static final String code_ape = "@code_ape";
	public static final String code_euro = "@code_euro";
	public static final String nom_resp = "@nom_resp";
	public static final String nom_contact = "@nom_contact";
	public static final String tel_contact = "@tel_contact";
	public static final String fax_contact = "@fax_contact";
	public static final String mail_contact = "@mail_contact";
	public static final String est_confidentiel = "@est_confidentiel";
	public static final String code_paie = "@code_paie";
	public static final String delai = "@delai";
	public static final String est_soumis_cmp = "@est_soumis_cmp";
	public static final String flag_cession = "@flag_cession";
	public static final String flag_judiciaire = "@flag_judiciaire";
	public static final String doublon = "@doublon";
	public static final String no_siret_ep = "@no_siret_ep";
	public static final String regie_dep = "@regie_dep";
	public static final String regie_rec = "@regie_rec";
	public static final String type_tiers = "@type_tiers";
	public static final String civilite = "@civilite";
	public static final String nom_pers_phys = "@nom_pers_phys";
	public static final String prenom = "@prenom";
	public static final String compl_nom = "@compl_nom";
	public static final String cat_tiers = "@cat_tiers";
	public static final String nat_jur = "@nat_jur";
	public static final String nat_id_tiers = "@nat_id_tiers";
	public static final String code_regie_dep = "@code_regie_dep";
	public static final String code_regie_rec = "@code_regie_rec";
	public static final String force_code_tiers = "@force_code_tiers";
	public static final String est_artiste = "@est_artiste";
	public static final String art_nom_usage = "@art_nom_usage";
	public static final String art_nir = "@art_nir";
	public static final String art_organisme = "@art_organisme";
	public static final String art_num_ordre = "@art_num_ordre";
	public static final String est_generique = "@est_generique";
	
	/**
	 * Adresse
	 */
	public static final String adr1 = "@adr1";
	public static final String adr2 = "@adr2";
	public static final String bur_post = "@bur_post";
	public static final String code_post = "@code_post";
	public static final String ville = "@ville";
	public static final String cedex = "@cedex";
	public static final String pays = "@pays";
	public static final String no_tel = "@no_tel";
	public static final String no_fax = "@no_fax";
	public static final String type_adr = "@type_adr";	
	/**
	 * IBAN
	 */
	public static final String detenteur = "@detenteur";
	public static final String banque = "@banque";
	public static final String bic = "@bic";
	public static final String iban = "@iban";
	public static final String no_iban = "@no_iban";   
	public static final String no_eo = "@no_eo"; 
	public static final String libelle = "@libelle";  
	public static final String date_sf = "@date_sf";  
	public static final String code_prm = "@code_prm";  
	public static final String tiers = "@tiers";  
	public static final String no_adr = "@no_adr";  
	public static final String qte = "@qte";  
	public static final String ht_sf = "@ht_sf";  
	public static final String taux_tva = "@taux_tva";  
	public static final String ttc_sf = "@ttc_sf";  
	public static final String htd_sf = "@htd_sf";
	public static final String etat = "@etat";
	
	/**
	 * PJ
	 */
	public static final String id_piece = "@id_piece";
	public static final String code_piece = "@code_piece";
	public static final String num_exec_piece = "@num_exec_piece";
	public static final String chemin = "@chemin";
	public static final String nom_fichier = "@nom_fichier";
	public static final String nouveau_nom = "@nouveau_nom";
	
	public static final String piece_type = "@piece_type";
	public static final String DestPath = "@DestPath";
	
	public static final String no_mdt = "@no_mdt";
	public static final String no_pj = "@no_pj";
	
	public static final String compte = "@compte";
	public String getCodeUser(){
		String userName=Helper.getUserName();
		logger.info("getCodeUser: "+userName);
		return userName;
	}    
	
	
	
	
}
