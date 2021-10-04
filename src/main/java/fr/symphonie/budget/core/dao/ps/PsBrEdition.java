package fr.symphonie.budget.core.dao.ps;

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

public class PsBrEdition extends GenericBudgetPs {

	private static PsBrEdition instance = null;
	public static final String SP_NAME = "psBrEdition";
	private static final Logger logger = LoggerFactory.getLogger(PsBrEdition.class);
	private static final String RESULT_LIST = "RESULT_LIST";

	@Override
	protected Logger getLogger() {
		return logger;
	}

	/**
	 * @session_id varchar(50),
	 * @num_exec smallint,
	 * @no_br smallint,
	 * @action char(1) I: insertion, D: suppression
	 * 
	 **/

	protected PsBrEdition(JdbcTemplate jdbcTemplate) {
		super(jdbcTemplate, SP_NAME);
		RowMapper<Map<String, Object>> rowMapper = new RowMapper<Map<String, Object>>() {

			@Override
			public Map<String, Object> mapRow(ResultSet rs, int rowNum) throws SQLException {
				Map<String, Object> map = new HashMap<String, Object>();
				// map.put(dest_grp_col, rs.getString(dest_grp_col));
				// map.put(nom_dest_grp_col, rs.getString(nom_dest_grp_col));
				return map;
			};
		};
		declareParameter(new SqlReturnResultSet(RESULT_LIST, rowMapper));
		declareParameter(new SqlParameter(session_id, Types.VARCHAR));
		declareParameter(new SqlParameter(num_exec, Types.INTEGER));
		declareParameter(new SqlParameter(no_br, Types.INTEGER));
		declareParameter(new SqlParameter(action, Types.VARCHAR));

		compile();
	}

	// psBrEdition ( @session_id varchar(50),@num_exec smallint,@no_br
	// smallint,@action char(1) /* I: insertion, D: suppression*/)

	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getData(String sessionId, int numExec, int noBr, String actionType) {

		logger.debug("getData - start");
		Map<String, Object> inputs = new HashMap<String, Object>();
		inputs.put(session_id, sessionId);
		inputs.put(num_exec, numExec);
		inputs.put(no_br, noBr);
		inputs.put(action, actionType);

		Map<String, Object> result = super.execute(inputs);
		List<Map<String, Object>> list = (List<Map<String, Object>>) result.get(RESULT_LIST);
		logger.debug("getData: " + list.size() + "-  end");

		return list;
	}

	public static PsBrEdition getInstance(JdbcTemplate jdbcTemplate) {
		if (instance == null)
			instance = new PsBrEdition(jdbcTemplate);
		return instance;
	}

}