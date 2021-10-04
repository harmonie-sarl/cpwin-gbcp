package fr.symphonie.budget.core.dao.ps;

import static fr.symphonie.budget.core.dao.ps.ColumnName.*;
import static fr.symphonie.budget.core.dao.ps.ColumnName.co;
import static fr.symphonie.budget.core.dao.ps.ColumnName.ej;
import static fr.symphonie.budget.core.dao.ps.ColumnName.nat_grp;
import static fr.symphonie.budget.core.dao.ps.ColumnName.nb;
import static fr.symphonie.budget.core.dao.ps.ColumnName.nom_nat_grp;
import static fr.symphonie.budget.core.dao.ps.ColumnName.sf_ex;
import static fr.symphonie.budget.core.dao.ps.ColumnName.sf_ex_ant;
import static fr.symphonie.budget.core.dao.ps.ColumnName.tot;

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

public class PsBudgsuiviexecdepImp extends GenericPs{
	private static PsBudgsuiviexecdepImp instance=null;
	public static final String SP_NAME="PsBudgsuiviexecdepImp";
	private static final Logger logger = LoggerFactory.getLogger(PsBudgsuiviexecdepImp.class);
	private static final String RESULT_LIST = "RESULT_LIST";
	protected PsBudgsuiviexecdepImp(JdbcTemplate jdbcTemplate) {
		super(jdbcTemplate, SP_NAME);
		RowMapper<Map<String, Object>> rowMapper=new RowMapper<Map<String, Object>>(){

			@Override
			public Map<String, Object> mapRow(ResultSet rs, int rowNum)
					throws SQLException {
				Map<String, Object> map=new HashMap<String, Object>();
				map.put(section, rs.getString(section));
				map.put(nat_grp, rs.getString(nat_grp).trim());
				map.put(nom_nat_grp, rs.getString(nom_nat_grp).trim());
				map.put(co, rs.getDouble(co));
				map.put(ej, rs.getDouble(ej));
				map.put(cne, rs.getDouble(cne));
				map.put(sf_ex, rs.getDouble(sf_ex));
				map.put(sf_ex_ant, rs.getDouble(sf_ex_ant));
				map.put(tot, rs.getDouble(tot));
				map.put(dr, rs.getDouble(dr));
				map.put(net, rs.getDouble(net));
				map.put(nb, rs.getDouble(nb));
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
	public static PsBudgsuiviexecdepImp getInstance(JdbcTemplate jdbcTemplate) {
		if(instance==null)instance=new PsBudgsuiviexecdepImp(jdbcTemplate);
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
