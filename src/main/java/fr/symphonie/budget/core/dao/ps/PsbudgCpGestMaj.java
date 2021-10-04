package fr.symphonie.budget.core.dao.ps;

import java.sql.Types;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.SqlReturnResultSet;

import fr.symphonie.budget.core.model.pluri.CpGestionnaire;
import fr.symphonie.sp.GenericPs;

public class PsbudgCpGestMaj extends GenericPs{
	private static PsbudgCpGestMaj instance=null;
	public static final String SP_NAME="psbudgcpgestmaj";
	private static final Logger logger = LoggerFactory.getLogger(PsbudgCpGestMaj.class);
	private static final String RESULT_LIST = "RESULT_LIST";
	private static final String NAT_GRP = "@nat_grp";
	protected PsbudgCpGestMaj(JdbcTemplate jdbcTemplate) {
		super(jdbcTemplate, SP_NAME);
		RowMapper<CpGestionnaire> rowMapper=new CpGestRowMapper();
		declareParameter(new SqlReturnResultSet(RESULT_LIST,rowMapper));
		declareParameter(new SqlParameter(NUM_EXEC, Types.SMALLINT));
		declareParameter(new SqlParameter(code_budg, Types.VARCHAR));
		declareParameter(new SqlParameter(NAT_GRP, Types.VARCHAR));
		compile();
	}
	public static PsbudgCpGestMaj getInstance(JdbcTemplate jdbcTemplate) {
		if(instance==null)instance=new PsbudgCpGestMaj(jdbcTemplate);
		return instance;
	}
	@SuppressWarnings("unchecked")
	public List<CpGestionnaire> getbudgCpGest(int exercice,String codeBudget,String groupNat){
	logger.debug("getbudgCpGest - start");
		Map<String, Object> inputs = new HashMap<String, Object> ();
		inputs.put(NUM_EXEC, exercice);
		inputs.put(code_budg, codeBudget);
		inputs.put(NAT_GRP, groupNat);
		Map<String, Object> result = super.execute(inputs);
		List<CpGestionnaire> list=(List<CpGestionnaire>) result.get(RESULT_LIST);
		logger.debug("getbudgCpGest: "+list.size()+"-  end");
		for(CpGestionnaire c:list){
			c.setExerciceCP(exercice);c.setCodeBudget(codeBudget);
			c.setGroupNat(groupNat);
		}
		return list;	
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
