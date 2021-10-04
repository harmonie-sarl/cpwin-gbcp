package fr.symphonie.budget.core.dao.ps;

import static fr.symphonie.budget.core.dao.ps.ColumnName.*;


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

import fr.symphonie.sp.GenericPs;

public class PsBudgsuivireccptImp extends GenericPs{
	private static PsBudgsuivireccptImp instance=null;
	public static final String SP_NAME="PsBudgsuivireccptImp";
	private static final Logger logger = LoggerFactory.getLogger(PsBudgsuivireccptImp.class);
	private static final String RESULT_LIST = "RESULT_LIST";
	protected PsBudgsuivireccptImp(JdbcTemplate jdbcTemplate) {
		super(jdbcTemplate, SP_NAME);
		RowMapper<Map<String, Object>> rowMapper=new RowMapper<Map<String, Object>>(){

			@Override
			public Map<String, Object> mapRow(ResultSet rs, int rowNum)
					throws SQLException {
				Map<String, Object> map=new HashMap<String, Object>();
				map.put(niveau, rs.getString(niveau).trim());
				map.put(compte, rs.getString(compte).trim());
				map.put(libelle, rs.getString(libelle).trim());
				map.put(rec, rs.getDouble(rec));
				map.put(ar, rs.getDouble(ar));
				map.put(rb_tot, rs.getDouble(rb_tot));
				map.put(op_n_budg, rs.getDouble(op_n_budg));
				map.put(bcg, rs.getDouble(bcg));
				return map;
			}
			;
		};
		declareParameter(new SqlReturnResultSet(RESULT_LIST,rowMapper));
		declareParameter(new SqlParameter(NUM_EXEC, Types.SMALLINT));
		
		compile();
	}
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getData(int exercice){
		logger.debug("getData - start");
			Map<String, Object> inputs = new HashMap<String, Object> ();
			inputs.put(NUM_EXEC, exercice);
			Map<String, Object> result = super.execute(inputs);
			List<Map<String, Object>> list=(List<Map<String, Object>>) result.get(RESULT_LIST);
			logger.debug("getData: "+list.size()+"-  end");
	
			return list;	
		}
	public static PsBudgsuivireccptImp getInstance(JdbcTemplate jdbcTemplate) {
		if(instance==null)instance=new PsBudgsuivireccptImp(jdbcTemplate);
		return instance;
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
