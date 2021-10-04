package fr.symphonie.budget.core.dao.ps.tiers;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.HashMap;
import java.util.Map;

import org.iban4j.IbanUtil;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.SqlReturnResultSet;

import fr.symphonie.budget.core.dao.ps.GenericBudgetPs;
import fr.symphonie.cpwin.model.nantes.Iban;

public class PsTiersIbanCree extends GenericBudgetPs {
	private static PsTiersIbanCree instance = null;
	public static final String SP_NAME = "PsTiersIbanCree";

	protected PsTiersIbanCree(JdbcTemplate jdbcTemplate) {
		super(jdbcTemplate, SP_NAME);
		declareParameter(new SqlReturnResultSet(RESULT_LIST,new RowMapper<Integer>() {
			@Override
			public Integer mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rs.getInt(1);
			}
		}));
		declareParameter(new SqlParameter(code_tiers, Types.VARCHAR));
		declareParameter(new SqlParameter(detenteur, Types.VARCHAR));
		declareParameter(new SqlParameter(banque, Types.VARCHAR));
		declareParameter(new SqlParameter(bic, Types.VARCHAR));
		declareParameter(new SqlParameter(iban, Types.VARCHAR));
		declareParameter(new SqlOutParameter(no_iban, Types.INTEGER));
		declareParameter(new SqlParameter(silencieux, Types.BIT));
		declareParameter(new SqlParameter(code_util, Types.VARCHAR));
		

		compile();
	}

	public static PsTiersIbanCree getInstance(JdbcTemplate jdbcTemplate) {
		if (instance == null)
			instance = new PsTiersIbanCree(jdbcTemplate);
		return instance;
	}

	public Integer createIban(String codeTiers, Iban ibanObj) {
		getLogger().debug("create iban for tiers {}",codeTiers);
		Map<String, Object> inputs = new HashMap<String, Object>();

		inputs.put(code_tiers, codeTiers);
		inputs.put(detenteur, ibanObj.getDetenteur());
		inputs.put(banque, IbanUtil.getBankCode(ibanObj.getIban()));
		inputs.put(bic, ibanObj.getBic());
		inputs.put(iban, ibanObj.getIban());
		inputs.put(no_iban, null);
		inputs.put(silencieux, 0);
		inputs.put(code_util, getCodeUser());

		
		Map<String, Object> resultMap = super.execute(inputs);
		Integer result = (Integer) resultMap.get(no_iban);
		getLogger().debug("create iban for tiers {} -> no_iban={}",codeTiers,result);
		return result;
	}

}
