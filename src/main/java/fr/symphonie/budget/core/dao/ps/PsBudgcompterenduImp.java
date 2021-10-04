package fr.symphonie.budget.core.dao.ps;

import static fr.symphonie.budget.core.dao.ps.ColumnName.*;


import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Date;
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
import fr.symphonie.util.Helper;

public class PsBudgcompterenduImp extends GenericPs{
	private static PsBudgcompterenduImp instance=null;
	public static final String SP_NAME="PsBudgcompterenduImp";
	private static final Logger logger = LoggerFactory.getLogger(PsBudgcompterenduImp.class);
	private static final String RESULT_LIST = "RESULT_LIST";
	protected PsBudgcompterenduImp(JdbcTemplate jdbcTemplate) {
		super(jdbcTemplate, SP_NAME);
		RowMapper<Map<String, Object>> rowMapper=new RowMapper<Map<String, Object>>(){

			@Override
			public Map<String, Object> mapRow(ResultSet rs, int rowNum)
					throws SQLException {
				Map<String, Object> map=new HashMap<String, Object>();
				map.put(dest1, rs.getString(dest1).trim());
				map.put(lib_dest1, rs.getString(lib_dest1).trim());
				map.put(dest2, rs.getString(dest2).trim());
				map.put(lib_dest2, rs.getString(lib_dest2).trim());
				map.put(nat_grp , rs.getString(nat_grp).trim());
				map.put(ColumnName.no_lbi , rs.getInt(ColumnName.no_lbi));
				
				map.put(co, rs.getDouble(co));
				map.put(ej_new, rs.getDouble(ej_new));
				map.put(sf_new, rs.getDouble(sf_new));
				map.put(pct, rs.getDouble(pct));
				map.put(sf_old, rs.getDouble(sf_old));
				return map;
			}
			;
		};
		declareParameter(new SqlReturnResultSet(RESULT_LIST,rowMapper));
		declareParameter(new SqlParameter(NUM_EXEC, Types.SMALLINT));
		declareParameter(new SqlParameter("@date_fin", Types.VARCHAR));
		
		compile();
	}
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getData(int exercice, Date dateFin){
		logger.debug("getData - start");
		String dateFinStr=Helper.toSimpleFormat2(dateFin);
			Map<String, Object> inputs = new HashMap<String, Object> ();
			inputs.put(NUM_EXEC, exercice);
			inputs.put("@date_fin", dateFinStr);
			Map<String, Object> result = super.execute(inputs);
			List<Map<String, Object>> list=(List<Map<String, Object>>) result.get(RESULT_LIST);
			logger.debug("getData: "+list.size()+"-  end");
	
			return list;	
		}
	public static PsBudgcompterenduImp getInstance(JdbcTemplate jdbcTemplate) {
		if(instance==null)instance=new PsBudgcompterenduImp(jdbcTemplate);
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
