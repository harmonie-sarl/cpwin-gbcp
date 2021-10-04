package fr.symphonie.tools.gts.core.ps;

import java.sql.Types;
import java.util.HashMap;
import java.util.Map;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SqlParameter;

public class PsRecFactLigneSupprime extends GenericGtsPs {
	public static final String SP_NAME="PsRecFactLigneSupprime";
	
	private static PsRecFactLigneSupprime instance=null;
	public static PsRecFactLigneSupprime getInstance(JdbcTemplate jdbcTemplate) {
		if(instance==null)instance=new PsRecFactLigneSupprime(jdbcTemplate,SP_NAME);
		return instance;
	}
	protected PsRecFactLigneSupprime(JdbcTemplate jdbcTemplate, String name) {
		super(jdbcTemplate, name);
		declareParameter(new SqlParameter(NUM_EXEC, Types.SMALLINT));
		 declareParameter(new SqlParameter(no_lrec, Types.INTEGER));
		 declareParameter(new SqlParameter(nolig_pres, Types.INTEGER));
		 declareParameter(new SqlParameter(totalise, Types.BIT));
		 declareParameter(new SqlParameter(createur, Types.VARCHAR));
		 declareParameter(new SqlParameter(central, Types.BIT));
		 compile();
	}
	public void execute(Integer exercice,Integer numero_lrec,String creator){
		Map<String, Object> inputs = new HashMap<String, Object> ();
		inputs.put(NUM_EXEC, exercice);
		inputs.put(no_lrec, numero_lrec);
		inputs.put(nolig_pres, 1);
		inputs.put(totalise, 0);
		inputs.put(createur, creator);
		inputs.put(central, 1);
		super.execute(inputs);
	}


}
