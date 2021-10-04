package fr.symphonie.tools.gts.core.ps;

import java.sql.Types;
import java.util.HashMap;
import java.util.Map;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SqlParameter;

public class PsRecFactMajAdr extends GenericGtsPs {

	private static final String adr_debiteur = "@adr_debiteur";
	private static final String SP_NAME = "PsRecFactMajAdr";
	private static PsRecFactMajAdr instance=null;
	public static PsRecFactMajAdr getInstance(JdbcTemplate jdbcTemplate) {
		if(instance==null)instance=new PsRecFactMajAdr(jdbcTemplate,SP_NAME);
		return instance;
	}

	public PsRecFactMajAdr(JdbcTemplate jdbcTemplate, String name) {
		super(jdbcTemplate, name);
		declareParameter(new SqlParameter(NUM_EXEC, Types.SMALLINT));
		 declareParameter(new SqlParameter(no_lrec, Types.INTEGER));
		 declareParameter(new SqlParameter(adr_debiteur, Types.TINYINT));
		 declareParameter(new SqlParameter(bypass_visa, Types.BIT));
		 declareParameter(new SqlParameter(createur, Types.VARCHAR));
		 declareParameter(new SqlParameter(central, Types.BIT));
		 compile();
	}
	public void execute(Integer exercice,Integer numero_lrec,Integer noAdresse,String creator){
		Map<String, Object> inputs = new HashMap<String, Object> ();
		inputs.put(NUM_EXEC, exercice);
		inputs.put(no_lrec, numero_lrec);
		inputs.put(adr_debiteur, noAdresse);
		inputs.put(bypass_visa, 0);
		inputs.put(createur, creator);
		inputs.put(central, 1);
		super.execute(inputs);
	}

}
