package fr.symphonie.budget.core.dao.ps;

import static fr.symphonie.budget.core.dao.ps.ColumnName.getAutreFonc_col;
import static fr.symphonie.budget.core.dao.ps.ColumnName.getAutresProduit_col;
import static fr.symphonie.budget.core.dao.ps.ColumnName.getAutresSubv_col;
import static fr.symphonie.budget.core.dao.ps.ColumnName.getData10_col;
import static fr.symphonie.budget.core.dao.ps.ColumnName.getData11_col;
import static fr.symphonie.budget.core.dao.ps.ColumnName.getData1_col;
import static fr.symphonie.budget.core.dao.ps.ColumnName.getData2_col;
import static fr.symphonie.budget.core.dao.ps.ColumnName.getData3_col;
import static fr.symphonie.budget.core.dao.ps.ColumnName.getData4_col;
import static fr.symphonie.budget.core.dao.ps.ColumnName.getData5_col;
import static fr.symphonie.budget.core.dao.ps.ColumnName.getData6_col;
import static fr.symphonie.budget.core.dao.ps.ColumnName.getData7_col;
import static fr.symphonie.budget.core.dao.ps.ColumnName.getData8_col;
import static fr.symphonie.budget.core.dao.ps.ColumnName.getData9_col;
import static fr.symphonie.budget.core.dao.ps.ColumnName.getDepPersCp_col;
import static fr.symphonie.budget.core.dao.ps.ColumnName.getFiscAffect_col;
import static fr.symphonie.budget.core.dao.ps.ColumnName.getSubvEtat_col;
import static fr.symphonie.budget.core.dao.ps.ColumnName.getinterventionCp_col;

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

public class PsCtabalanceParDate extends GenericBudgetPs{

	private static PsCtabalanceParDate instance=null;
	public static final String SP_NAME="PsCtabalanceParDate";
	private static final String RESULT_LIST = "RESULT_LIST";
	
	private PsCtabalanceParDate(JdbcTemplate jdbcTemplate) {
		super(jdbcTemplate, SP_NAME);
		
		RowMapper<Map<String, Object>> rowMapper=new RowMapper<Map<String, Object>>(){
			
			@Override
			public Map<String, Object> mapRow(ResultSet rs, int rowNum)
					throws SQLException {
				Map<String, Object> map=new HashMap<String, Object>();	
				
				map.put(getDepPersCp_col, rs.getDouble(getDepPersCp_col));
				map.put(getSubvEtat_col, rs.getDouble(getSubvEtat_col));
				map.put(getAutresSubv_col, rs.getDouble(getAutresSubv_col));
				map.put(getAutresProduit_col, rs.getDouble(getAutresProduit_col));
				map.put(getAutreFonc_col, rs.getDouble(getAutreFonc_col));
				map.put(getinterventionCp_col, rs.getDouble(getinterventionCp_col));
				map.put(getFiscAffect_col, rs.getDouble(getFiscAffect_col));

				map.put(getData1_col, rs.getDouble(getData1_col));
				map.put(getData2_col, rs.getDouble(getData2_col));
				map.put(getData3_col, rs.getDouble(getData3_col));
				map.put(getData4_col, rs.getDouble(getData4_col));
				map.put(getData5_col, rs.getDouble(getData5_col));
				map.put(getData6_col, rs.getDouble(getData6_col));
				map.put(getData7_col, rs.getDouble(getData7_col));
				map.put(getData8_col, rs.getDouble(getData8_col));
				map.put(getData9_col, rs.getDouble(getData9_col));
				map.put(getData10_col, rs.getDouble(getData10_col));
				map.put(getData11_col, rs.getDouble(getData11_col));
			
				return map;
			}
			;
		};
		declareParameter(new SqlReturnResultSet(RESULT_LIST, rowMapper));	
	   declareParameter(new SqlParameter(num_exec			, Types.INTEGER));	
	   declareParameter(new SqlParameter(code_budg			, Types.VARCHAR)); 	
	   declareParameter(new SqlParameter(dateFin			, Types.VARCHAR));	
		compile();
		
	}
	public static PsCtabalanceParDate getInstance(JdbcTemplate jdbcTemplate) {
		if(instance==null)instance=new PsCtabalanceParDate(jdbcTemplate);
		return instance;
	}
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getData(int numexec, String codeBudg,String dataFinStr){
		Map<String, Object> inputs = new HashMap<String, Object> ();
	   inputs.put(num_exec, numexec);	
	   inputs.put(code_budg, codeBudg);
	   inputs.put(dateFin, dataFinStr);
	   Map<String, Object> result = super.execute(inputs);
		List<Map<String, Object>> list=(List<Map<String, Object>>) result.get(RESULT_LIST);
		return list;	
	}
}
