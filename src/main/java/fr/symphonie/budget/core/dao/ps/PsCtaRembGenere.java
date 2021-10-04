package fr.symphonie.budget.core.dao.ps;

import java.sql.Types;
import java.util.HashMap;
import java.util.Map;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;

public class PsCtaRembGenere extends GenericBudgetPs {

	private static PsCtaRembGenere instance=null;
	public static final String SP_NAME="psCtaRembGenere";
	
	private PsCtaRembGenere(JdbcTemplate jdbcTemplate) {
		super(jdbcTemplate, SP_NAME);
		
		declareParameter(new SqlParameter(num_exec, Types.INTEGER));
		declareParameter(new SqlParameter(code_budg, Types.VARCHAR));
		declareParameter(new SqlParameter(no_vague, Types.INTEGER));
		declareParameter(new SqlParameter(mode_paie, Types.VARCHAR));
		declareParameter(new SqlParameter(date_reglt, Types.VARCHAR));
		declareParameter(new SqlOutParameter(no_ecr, Types.INTEGER));
		declareParameter(new SqlParameter(silencieux, Types.BIT));
		declareParameter(new SqlParameter(code_util, Types.VARCHAR));
		compile();
		
	}
	public static PsCtaRembGenere getInstance(JdbcTemplate jdbcTemplate) {
		if(instance==null)instance=new PsCtaRembGenere(jdbcTemplate);
		return instance;
	}
	public Integer execute(Integer numexec, String codeBudget, String paymentCode, String paymentDate,Integer vagueNo){
		Map<String, Object> inputs = new HashMap<String, Object> ();
	   inputs.put(num_exec, numexec);	
	   inputs.put(code_budg, codeBudget);
	   inputs.put(no_vague, vagueNo);
	   inputs.put(mode_paie, paymentCode);
	   inputs.put(date_reglt, paymentDate);
	   inputs.put(silencieux, 0);
	   inputs.put(code_util, getCodeUser());
	   Map<String, Object> result =	super.execute(inputs);
	   Integer noEcriture=(Integer) result.get(no_ecr);
	   return noEcriture;
		
	}
}
