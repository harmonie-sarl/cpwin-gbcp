package fr.symphonie.budget.core.dao.ps;

import static fr.symphonie.budget.core.dao.ps.ColumnName.code_budg_col;

import java.sql.ResultSet;
import java.sql.SQLException;
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

public class PsBudgListe  extends GenericBudgetPs{
	
	
	private static PsBudgListe instance=null;
	public static final String SP_NAME="PsBudgListe";
	private static final Logger logger = LoggerFactory.getLogger(PsBudgListe.class);
	private static final String RESULT_LIST = "RESULT_LIST";
	private static final int TOUS =100;
	

/**	@mode		int		 = 0,		 style 
	@num_exec	smallint = null,	 exercice 
	@code_budg	TBUDGET	 = null,	 budget 
	@central	bit		 = 1,		 identifie mode central 
	@code_util	TCODE	 = null,	 utilisateur  pour cpwin Web 
	@BudgetOS	bit      = 0,		 indique ajout budget OS à la liste 
	@BudgetHBA  bit		 = 0		 indique ajout budget HBA à la liste 

**/	 
	
	@Override
	protected Logger getLogger() {
		return logger;
	}
	
	
	protected PsBudgListe(JdbcTemplate jdbcTemplate) {
		super(jdbcTemplate, SP_NAME);
		RowMapper<Map<String, Object>> rowMapper=new RowMapper<Map<String, Object>>(){

			@Override
			public Map<String, Object> mapRow(ResultSet rs, int rowNum)
					throws SQLException {
				Map<String, Object> map=new HashMap<String, Object>();			
				map.put(code_budg_col, rs.getString(code_budg_col));				
				return map;
			}
			;
		};
		declareParameter(new SqlReturnResultSet(RESULT_LIST,rowMapper));
		declareParameter(new SqlParameter(mode, Types.INTEGER));
		declareParameter(new SqlParameter(num_exec, Types.SMALLINT));
		declareParameter(new SqlParameter(code_budg, Types.VARCHAR));		
		declareParameter(new SqlParameter(central , Types.BIT));
		declareParameter(new SqlParameter(code_util , Types.VARCHAR));	
		declareParameter(new SqlParameter(BudgetOS , Types.BIT));		
		declareParameter(new SqlParameter(BudgetHBA , Types.BIT));
		compile();
	}
	

	
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getData(int style, int execCp, String codeBudg, int profilCentral, String codeUtil ){
		   logger.debug("getData - start");
			Map<String, Object> inputs = new HashMap<String, Object> ();
			inputs.put(mode, style );
			inputs.put(num_exec, execCp);
			inputs.put(code_budg, codeBudg);
			inputs.put(central, profilCentral);
			inputs.put(code_util, codeUtil);
			inputs.put(BudgetOS, 0);
			inputs.put(BudgetHBA, 0);
			Map<String, Object> result = super.execute(inputs);
			List<Map<String, Object>> list=(List<Map<String, Object>>) result.get(RESULT_LIST);
			logger.debug("getData: "+list.size()+"-  end");
	
			return list;	
		}
	/**
	 * 
	 * @param execCp
	 * @return Liste les budgets de l'exercice sur lesquels l'utilisateu
	 * r est habilité en droits internes et en droits de gestion dans le cas d'un profil non central.
	 */
	public List<Map<String, Object>> getData(int execCp) {
		return getData(TOUS, execCp, "", 1, getCodeUser());

	}
	
	public static PsBudgListe getInstance(JdbcTemplate jdbcTemplate) {
		if(instance==null)instance=new PsBudgListe(jdbcTemplate);
		return instance;
	}


}
