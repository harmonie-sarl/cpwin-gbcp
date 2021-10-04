package fr.symphonie.budget.core.dao.ps;

import java.sql.Types;
import java.util.HashMap;
import java.util.Map;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SqlParameter;

public class PsPaieMeta4DAICree extends GenericBudgetPs{
	private static PsPaieMeta4DAICree instance=null;
	public static final String SP_NAME="PsPaieMeta4DAICree";
	
	protected PsPaieMeta4DAICree(JdbcTemplate jdbcTemplate) {
		super(jdbcTemplate, SP_NAME);
		
		declareParameter(new SqlParameter(num_exec, Types.INTEGER));
		declareParameter(new SqlParameter(code_budg, Types.VARCHAR));
		declareParameter(new SqlParameter(periode, Types.VARCHAR));
		declareParameter(new SqlParameter(code_util, Types.VARCHAR));
		compile();
	}
	public static PsPaieMeta4DAICree getInstance(JdbcTemplate jdbcTemplate) {
		if(instance==null)instance=new PsPaieMeta4DAICree(jdbcTemplate);
		return instance;
	}
	public Integer execute(Integer exercice,String codeBudget,String period){
		getLogger().debug("execute: {} - {} - {} ",exercice,codeBudget,period);
		Map<String, Object> inputs = new HashMap<String, Object> ();
	   inputs.put(num_exec, exercice);	
	   inputs.put(code_budg, codeBudget);
	   inputs.put(periode, period);
	   inputs.put(code_util, getCodeUser());
	   Map<String, Object> resultMap = super.execute(inputs);	  
	   Integer result=(Integer) resultMap.get(RETURN_VALUE);
	   
		return result;
	}
}
