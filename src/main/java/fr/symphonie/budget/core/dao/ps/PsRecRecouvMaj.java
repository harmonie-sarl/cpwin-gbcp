package fr.symphonie.budget.core.dao.ps;

import java.sql.Types;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SqlParameter;

import fr.symphonie.sp.GenericPs;

public class PsRecRecouvMaj extends GenericPs {
	private static final Logger logger = LoggerFactory.getLogger(PsRecRecouvMaj.class);
	private static PsRecRecouvMaj instance=null;

	private static final String SP_NAME = "psRectitrerecouv";
	//private static final String last_max_no_ecr = "@last_max_no_ecr ";

	private PsRecRecouvMaj(JdbcTemplate jdbcTemplate) {
		super(jdbcTemplate, SP_NAME);
		declareParameter(new SqlParameter(NUM_EXEC, Types.SMALLINT));
		//declareParameter(new SqlParameter(last_max_no_ecr , Types.INTEGER));
		compile();
	}
	
	public static PsRecRecouvMaj getInstance(JdbcTemplate jdbcTemplate) {
		if(instance==null)instance=new PsRecRecouvMaj(jdbcTemplate);
		return instance;
	}
	public void execute(Integer exercice, Integer lastRecouvNoEcr){
		Map<String, Object> inputs = new HashMap<String, Object> ();
		inputs.put(NUM_EXEC, exercice);
	//	inputs.put(last_max_no_ecr, lastRecouvNoEcr);
		 super.execute(inputs);
	}
	@Override
	public String getCreateur() {
		return null;
	}

	@Override
	protected Logger getLogger() {
		return logger;
	}

}
