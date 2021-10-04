package fr.symphonie.tools.gts.core.ps;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;

import fr.symphonie.sp.GenericPs;

public  class GenericGtsPs extends GenericPs {
	protected GenericGtsPs(JdbcTemplate jdbcTemplate, String name) {
		super(jdbcTemplate, name);
	}

	private static final Logger logger = LoggerFactory.getLogger(GenericGtsPs.class);

	@Override
	public String getCreateur() {
		return "GTS";
	}

	@Override
	protected Logger getLogger() {
		return logger;
	}
	protected static final String imput_ht = "@imput_ht";
	protected static final String lib_lrec = "@lib_lrec";
	protected static final String ref_lrec = "@ref_lrec";
	protected static final String document = "@document";
	protected static final String tiers_orig = "@tiers_orig";
	protected static final String debiteur = "@debiteur";
	protected static final String code_cat = "@code_cat";
	public static final String no_lrec = "@no_lrec";
	protected static final String num_exec_cde = "@num_exec_cde";
	protected static final String no_cde = "@no_cde";
	protected static final String no_ech = "@no_ech";
	protected static final String adr_tiers = "@adr_tiers";
	protected static final String ref_cde = "@ref_cde";
	protected static final String moda_paie = "@moda_paie";
	protected static final String date_dem = "@date_dem";
	protected static final String facture_soldant = "@facture_soldant";
	protected static final String no_dem = "@no_dem";
	protected static final String num_exec_dos = "@num_exec_dos";
	protected static final String code_dossier = "@code_dossier";
	protected static final String num_exec_piece = "@num_exec_piece";

	protected static final String no_inv = "@no_inv";         
	protected static final String piece_externe = "@piece_externe";       
	protected static final String type_piece_externe = "@type_piece_externe";  
	protected static final String inventaire_externe = "@inventaire_externe"; 
	protected static final String exec_inv = "@exec_inv";          
    protected static final String no_ordre = "@no_ordre" ;
    
    protected static final String totalise = "@totalise";
    protected static final String nolig_pres = "@nolig_pres";
}
