package fr.symphonie.tools.gts.core.ps;

import java.sql.Types;
import java.util.HashMap;
import java.util.Map;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;

import fr.symphonie.tools.gts.model.LiqRecette;

public class PsLrecCree extends GenericGtsPs {

	public static final String SP_NAME="psLrecCree";
	private static PsLrecCree instance=null;
	public static PsLrecCree getInstance(JdbcTemplate jdbcTemplate) {
		if(instance==null)instance=new PsLrecCree(jdbcTemplate,SP_NAME);
		return instance;
	}
	
	protected PsLrecCree(JdbcTemplate jdbcTemplate, String name) {
		super(jdbcTemplate, name);
		declareParameter(new SqlParameter(NUM_EXEC, Types.SMALLINT));
		declareParameter(new SqlParameter(lib_lrec, Types.VARCHAR));
		declareParameter(new SqlParameter(ref_lrec, Types.VARCHAR));
		declareParameter(new SqlParameter(document, Types.VARCHAR));
		declareParameter(new SqlParameter(tiers_orig, Types.VARCHAR));
		declareParameter(new SqlParameter(debiteur, Types.VARCHAR));
		declareParameter(new SqlParameter(code_cat, Types.VARCHAR));
		 declareParameter(new SqlParameter(type_piece, Types.VARCHAR));
		 declareParameter(new SqlParameter(no_piece, Types.INTEGER)); 
		 declareParameter(new SqlParameter(imput_ht, Types.VARCHAR)); 
		 declareParameter(new SqlParameter(imput_ttc, Types.VARCHAR)); 
		 declareParameter(new SqlParameter(imput_tva, Types.VARCHAR)); 
		 
		 declareParameter(new SqlParameter(no_lbi, Types.INTEGER));
		 declareParameter(new SqlOutParameter(no_lrec, Types.INTEGER));
		 declareParameter(new SqlParameter(silencieux, Types.BIT)); 
		 declareParameter(new SqlParameter(createur, Types.VARCHAR));
		 declareParameter(new SqlParameter(num_exec_cde, Types.SMALLINT));
		 declareParameter(new SqlParameter(no_cde, Types.INTEGER));
		 declareParameter(new SqlParameter(no_ech, Types.INTEGER));
		 declareParameter(new SqlParameter(adr_tiers, Types.INTEGER));
		 declareParameter(new SqlParameter(ref_cde, Types.VARCHAR));
		 declareParameter(new SqlParameter(moda_paie, Types.VARCHAR));
		 declareParameter(new SqlParameter(date_dem, Types.VARCHAR));
		 
		 declareParameter(new SqlParameter(facture_soldant, Types.BIT));
		 declareParameter(new SqlOutParameter(no_dem, Types.INTEGER));
		 declareParameter(new SqlParameter(central, Types.BIT));
		 declareParameter(new SqlParameter(num_exec_dos, Types.SMALLINT));
		 declareParameter(new SqlParameter(code_dossier, Types.VARCHAR));
		 declareParameter(new SqlParameter(num_exec_piece , Types.SMALLINT));
		 declareParameter(new SqlParameter(code_emprunt, Types.VARCHAR));
		 
		 declareParameter(new SqlParameter(no_inv, Types.VARCHAR));
		 declareParameter(new SqlParameter(piece_externe, Types.BIT));
		 declareParameter(new SqlParameter(type_piece_externe, Types.VARCHAR));
		 declareParameter(new SqlParameter(inventaire_externe, Types.BIT));
		 declareParameter(new SqlParameter(exec_inv,  Types.SMALLINT));
		 declareParameter(new SqlParameter(no_ordre,Types.INTEGER));
		 declareParameter(new SqlParameter(code_util, Types.VARCHAR));
		
		compile();
	}
	public Integer execute(LiqRecette liqRec,String creator){
		getLogger().debug("execute : LiqRecette: {}",liqRec);
		Integer noLrec = null;
		Map<String, Object> inputs = new HashMap<String, Object>();
		Integer exercice = liqRec.getExercice();

		inputs.put(NUM_EXEC, exercice);
		inputs.put(lib_lrec, liqRec.getLibelle() );
		inputs.put(ref_lrec, "");
		inputs.put(document, null);
		inputs.put(tiers_orig, liqRec.getTiersOrigine());
		inputs.put(debiteur, liqRec.getDebiteur());
		inputs.put(code_cat,liqRec.getTiers().isRegisseur()?"3": "1");
		inputs.put(no_inv, null);
		inputs.put(piece_externe, 0);
		inputs.put(type_piece_externe, null);
		inputs.put(inventaire_externe, 0);
		inputs.put(exec_inv, 0);
		inputs.put(no_ordre, 0);
		inputs.put(code_util, getCodeUser());
		inputs.put(type_piece, null);
		inputs.put(no_piece, null); 
		inputs.put(num_exec_piece , null);
		
			 
		inputs.put(imput_ht, liqRec.getImputHt()); 
		inputs.put(imput_ttc, liqRec.getImputTtc()); 
		inputs.put(imput_tva, liqRec.getImputTva());  
			 
			 inputs.put(no_lbi, liqRec.getNoLbi());
			 inputs.put(no_lrec, null);
			 inputs.put(silencieux, 0); 
			 inputs.put(createur, creator);
			 inputs.put(num_exec_cde, null);
			 inputs.put(no_cde, null);
			 inputs.put(no_ech, null);
			 inputs.put(adr_tiers, liqRec.getNoAdresse());
			 inputs.put(ref_cde, null);

			 inputs.put(moda_paie, liqRec.getModePaie());
			 inputs.put(date_dem, null);			 
			 inputs.put(facture_soldant, 0);
			 inputs.put(no_dem, null);
			 inputs.put(central, 0);
			 inputs.put(num_exec_dos, null);
			 inputs.put(code_dossier, null);			
			 inputs.put(code_emprunt, null);			
			
			 Map<String, Object> result = super.execute(inputs);
			 noLrec=(Integer) result.get(no_lrec);
			 getLogger().debug("execute : noLrec: {}",noLrec);
		return noLrec;
		
	}
	
	
}
