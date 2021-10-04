package fr.symphonie.budget.core.dao.ps;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static fr.symphonie.budget.core.dao.ps.ColumnName.*;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.SqlReturnResultSet;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PsExecListe extends GenericBudgetPs{ 
 
	

	
	
	private static PsExecListe instance=null;
	public static final String SP_NAME="PsExecListe";
	private static final Logger logger = LoggerFactory.getLogger(PsExecListe.class);
	private static final String RESULT_LIST = "RESULT_LIST";
	private static final int TOUS =100;
	
/**	 @style		int		 = 0,		 Style de liste 
	 @num_exec	smallint = null,	 exercice 
	 @central	bit		 = 1,		 identifie mode central ou non 
	 @code_util	TCODE	 = null		 utilisateur pour cpwin Web 
**/
	
	@Override
	protected Logger getLogger() {
		return logger;
	}
	
	
	protected PsExecListe(JdbcTemplate jdbcTemplate) {
		super(jdbcTemplate, SP_NAME);
		RowMapper<Map<String, Object>> rowMapper=new RowMapper<Map<String, Object>>(){

			@Override
			public Map<String, Object> mapRow(ResultSet rs, int rowNum)
					throws SQLException {
				Map<String, Object> map=new HashMap<String, Object>();			
				map.put(num_exec_col, rs.getInt(num_exec_col));				
				return map;
			}
			;
		};
		declareParameter(new SqlReturnResultSet(RESULT_LIST,rowMapper));
		declareParameter(new SqlParameter(STYLE, Types.INTEGER));
		declareParameter(new SqlParameter(num_exec, Types.SMALLINT));
		declareParameter(new SqlParameter(central , Types.BIT));
		declareParameter(new SqlParameter(code_util , Types.VARCHAR));
		compile();
	}

	
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getData(int style){
		logger.debug("getData - start");
			Map<String, Object> inputs = new HashMap<String, Object> ();
			inputs.put(STYLE, style);
			inputs.put(num_exec, null);
			inputs.put(central, 1);
			inputs.put(code_util, null);
			Map<String, Object> result = super.execute(inputs);
			List<Map<String, Object>> list=(List<Map<String, Object>>) result.get(RESULT_LIST);
			logger.debug("getData: "+list.size()+"-  end");
	
			return list;	
		}
	/**
	 * 
	 * @return Liste les exercices ayant des budgets associés.
	 */
	public List<Map<String, Object>> getData() {

		return getData(TOUS);

	}
	
	public static PsExecListe getInstance(JdbcTemplate jdbcTemplate) {
		if(instance==null)instance=new PsExecListe(jdbcTemplate);
		return instance;
	}


}
