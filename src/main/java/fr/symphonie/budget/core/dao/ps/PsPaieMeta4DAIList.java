package fr.symphonie.budget.core.dao.ps;

import java.sql.Types;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.SqlReturnResultSet;

import fr.symphonie.budget.core.dao.ps.mapper.Meta4DaiStructRowMapper;
import fr.symphonie.tools.meta4dai.DisplayStruct;

public class PsPaieMeta4DAIList extends GenericBudgetPs {
	
	private static PsPaieMeta4DAIList instance=null;
	public static final String SP_NAME="PsPaieMeta4DAIList";
	
	protected PsPaieMeta4DAIList(JdbcTemplate jdbcTemplate) {
		super(jdbcTemplate, SP_NAME);
		
		RowMapper<DisplayStruct> rowMapper=new Meta4DaiStructRowMapper();
		declareParameter(new SqlReturnResultSet(RESULT_LIST,rowMapper));
		
		declareParameter(new SqlParameter(num_exec, Types.INTEGER));
		
		compile();
	}
	public static PsPaieMeta4DAIList getInstance(JdbcTemplate jdbcTemplate) {
		if(instance==null)instance=new PsPaieMeta4DAIList(jdbcTemplate);
		return instance;
	}
	@SuppressWarnings("unchecked")
	public List<DisplayStruct> execute(Integer exercice){
		getLogger().debug("execute: {}",exercice);
		Map<String, Object> inputs = new HashMap<String, Object> ();

		inputs.put(num_exec, exercice);
		Map<String, Object> result = super.execute(inputs);
		List<DisplayStruct> resultList=(List<DisplayStruct>) result.get(RESULT_LIST);
		
		return resultList;
		
	}

}
