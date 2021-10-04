package fr.symphonie.budget.core.dao.ps;

import static fr.symphonie.budget.core.dao.ps.ColumnName.mnt_col;
import static fr.symphonie.budget.core.dao.ps.ColumnName.no_ltr_col;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.SqlReturnResultSet;

public class PsGbcpGetRFCpAeparDest extends GenericBudgetPs {

	private static PsGbcpGetRFCpAeparDest instance=null;
	public static final String SP_NAME="PsGbcpGetRFCpAeparDest";
	private static final String RESULT_LIST = "RESULT_LIST";
	
	private PsGbcpGetRFCpAeparDest(JdbcTemplate jdbcTemplate) {
		super(jdbcTemplate, SP_NAME);
		
		RowMapper<Map<String, Object>> rowMapper=new RowMapper<Map<String, Object>>(){
			
			@Override
			public Map<String, Object> mapRow(ResultSet rs, int rowNum)
					throws SQLException {
				Map<String, Object> map=new HashMap<String, Object>();			
				map.put(no_ltr_col, rs.getInt(no_ltr_col));
				map.put(mnt_col, rs.getDouble(mnt_col));
				
				return map;
			}
			;
		};
		declareParameter(new SqlReturnResultSet(RESULT_LIST, rowMapper));	
		   declareParameter(new SqlParameter(num_exec			, Types.INTEGER));	
		   declareParameter(new SqlParameter(niveau			, Types.INTEGER)); 		
		   declareParameter(new SqlParameter(modeCpAe			, Types.VARCHAR));
		   declareParameter(new SqlParameter(dateFin			, Types.VARCHAR));	
			compile();
		
	}
	public static PsGbcpGetRFCpAeparDest getInstance(JdbcTemplate jdbcTemplate) {
		if(instance==null)instance=new PsGbcpGetRFCpAeparDest(jdbcTemplate);
		return instance;
	}
	@SuppressWarnings("unchecked")
	private List<Map<String, Object>> execute(Integer numexec, Integer niveau,String mode,String dataFinStr){
		Map<String, Object> inputs = new HashMap<String, Object> ();
	   inputs.put(num_exec, numexec);	
	   inputs.put(GenericBudgetPs.niveau, niveau!=null? niveau:0);
	   inputs.put(modeCpAe, mode);
	   inputs.put(dateFin, dataFinStr);
	   Map<String, Object> result = super.execute(inputs);
		List<Map<String, Object>> list=(List<Map<String, Object>>) result.get(RESULT_LIST);

		return list;	
	}
	public List<Map<String, Object>> getCpData(Integer numexec, Integer niveau,String dateFinStr){	

		return execute(numexec,niveau,"CP",dateFinStr);	
	}
	public List<Map<String, Object>> getAeData(Integer numexec, Integer niveau,String dateFinStr){	

		return execute(numexec,niveau,"AE",dateFinStr);	
	}
}
