package fr.symphonie.tools.gts.core.ps;

import java.sql.Types;
import java.util.HashMap;
import java.util.Map;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SqlParameter;

import fr.symphonie.tools.gts.model.LigneRecette;

public class PsRecFactLigneCree extends GenericGtsPs {
	private static final String SP_NAME = "PsRecFactLigneCree";
	private static final String typlig_pres = "@typlig_pres";
	private static final String code_pres = "@code_pres";
	private static final String lib_pres = "@lib_pres";
	private static final String qte_pres = "@qte_pres";
	private static final String vhtu_pres = "@vhtu_pres";
	private static final String vtrem_pres = "@vtrem_pres";
	private static final String vrem_pres = "@vrem_pres";
	private static final String vttva_pres = "@vttva_pres";
	private static final String vht_pres = "@vht_pres";
	private static final String vtva_pres = "@vtva_pres";
	private static final String vttc_pres = "@vttc_pres";
	private static final String prix_ref = "@prix_ref";
	private static PsRecFactLigneCree instance=null;
	public static PsRecFactLigneCree getInstance(JdbcTemplate jdbcTemplate) {
		if(instance==null)instance=new PsRecFactLigneCree(jdbcTemplate,SP_NAME);
		return instance;
	}
	public PsRecFactLigneCree(JdbcTemplate jdbcTemplate, String name) {
		super(jdbcTemplate, name);
		declareParameter(new SqlParameter(NUM_EXEC, Types.SMALLINT));
		 declareParameter(new SqlParameter(no_lrec, Types.INTEGER));
		 declareParameter(new SqlParameter(nolig_pres, Types.INTEGER));
		 
		 declareParameter(new SqlParameter(typlig_pres, Types.VARCHAR));
		 declareParameter(new SqlParameter(code_pres, Types.VARCHAR));
		 declareParameter(new SqlParameter(lib_pres, Types.VARCHAR));
		 declareParameter(new SqlParameter(qte_pres, Types.INTEGER));
		 declareParameter(new SqlParameter(vhtu_pres, Types.DOUBLE));
		 declareParameter(new SqlParameter(vtrem_pres, Types.FLOAT));
		 declareParameter(new SqlParameter(vrem_pres, Types.DOUBLE));
		 declareParameter(new SqlParameter(vttva_pres, Types.FLOAT));
		 declareParameter(new SqlParameter(vht_pres, Types.DOUBLE));
		 declareParameter(new SqlParameter(vtva_pres, Types.DOUBLE));
		 declareParameter(new SqlParameter(vttc_pres, Types.DOUBLE));
		 declareParameter(new SqlParameter(prix_ref, Types.VARCHAR));
		 declareParameter(new SqlParameter(totalise, Types.BIT));
		 declareParameter(new SqlParameter(createur, Types.VARCHAR));
		 declareParameter(new SqlParameter(bypass_visa, Types.BIT));
		
		 declareParameter(new SqlParameter(central, Types.BIT));
		 compile();
	}
	public void execute(LigneRecette ligneRec,boolean totaliseFlag,String creator){
		getLogger().debug("execute : LigneRecette: {},totaliseFlag={}",ligneRec,totaliseFlag);
		Map<String, Object> inputs = new HashMap<String, Object>();
		
		inputs.put(NUM_EXEC, ligneRec.getExercice());
		 inputs.put(no_lrec, ligneRec.getNoLiqRec());
		 inputs.put(nolig_pres, ligneRec.getNumero());
		 
		 inputs.put(typlig_pres, "C");
		 inputs.put(code_pres, ligneRec.getPrestation().getCode());
		 inputs.put(lib_pres, ligneRec.getPrestation().getDesignation());
		 inputs.put(qte_pres, ligneRec.getQuantite());
		 inputs.put(vhtu_pres, ligneRec.getPu());
		 inputs.put(vtrem_pres, ligneRec.getTauxRemise());
		 inputs.put(vrem_pres, ligneRec.getMntRemise());
		 inputs.put(vttva_pres, ligneRec.getTauxTva());
		 inputs.put(vht_pres, ligneRec.getMntHt());
		 inputs.put(vtva_pres, ligneRec.getMntTva());
		 inputs.put(vttc_pres, ligneRec.getMntTtc());
		 inputs.put(prix_ref, "TTC");
		 inputs.put(totalise, totaliseFlag);
		 inputs.put(createur, creator);
		 inputs.put(bypass_visa, 0);		
		 inputs.put(central, 0);	
			
		 super.execute(inputs);
		 getLogger().debug("execute : End");
	}

}
