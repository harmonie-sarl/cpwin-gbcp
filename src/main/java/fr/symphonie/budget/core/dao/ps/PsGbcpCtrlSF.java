package fr.symphonie.budget.core.dao.ps;

import static fr.symphonie.budget.core.dao.ps.ColumnName.imput_htd_col;
import static fr.symphonie.budget.core.dao.ps.ColumnName.mt_htd_col;
import static fr.symphonie.budget.core.dao.ps.ColumnName.lib_cpt_col;
import static fr.symphonie.budget.core.dao.ps.ColumnName.nat_grp_col;

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

public class PsGbcpCtrlSF extends GenericBudgetPs {
	private static PsGbcpCtrlSF instance=null;
	public static final String SP_NAME="PsGbcpCtrlSF";
	private static final String RESULT_LIST = "RESULT_LIST";
	
	private PsGbcpCtrlSF(JdbcTemplate jdbcTemplate) {
		super(jdbcTemplate, SP_NAME);
		
		RowMapper<Map<String, Object>> rowMapper=new RowMapper<Map<String, Object>>(){
			
			@Override
			public Map<String, Object> mapRow(ResultSet rs, int rowNum) throws SQLException {
				Map<String, Object> map = new HashMap<String, Object>();
				String nature = rs.getString(nat_grp_col);
				map.put(imput_htd_col, rs.getString(imput_htd_col));
				map.put(lib_cpt_col, rs.getString(lib_cpt_col));
				map.put(nat_grp_col, nature != null ? nature.trim() : "");
				map.put(mt_htd_col, rs.getDouble(mt_htd_col));

				return map;
			};
		};
		declareParameter(new SqlReturnResultSet(RESULT_LIST, rowMapper));
		declareParameter(new SqlParameter(num_exec, Types.INTEGER));
		compile();
		
	}
	public static PsGbcpCtrlSF getInstance(JdbcTemplate jdbcTemplate) {
		if(instance==null)instance=new PsGbcpCtrlSF(jdbcTemplate);
		return instance;
	}
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> execute(Integer numexec){
		Map<String, Object> inputs = new HashMap<String, Object> ();
	   inputs.put(num_exec, numexec);	
	   Map<String, Object> result = super.execute(inputs);
		List<Map<String, Object>> list=(List<Map<String, Object>>) result.get(RESULT_LIST);

		return list;	
	}
}
