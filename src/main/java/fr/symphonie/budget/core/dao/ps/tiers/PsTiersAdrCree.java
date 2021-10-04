package fr.symphonie.budget.core.dao.ps.tiers;

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

import fr.symphonie.budget.core.dao.ps.GenericBudgetPs;
import fr.symphonie.cpwin.model.nantes.Adresse;


public class PsTiersAdrCree extends GenericBudgetPs {
	private static PsTiersAdrCree instance = null;
	public static final String SP_NAME = "PsTiersAdrCree";

	protected PsTiersAdrCree(JdbcTemplate jdbcTemplate) {
		super(jdbcTemplate, SP_NAME);
		declareParameter(new SqlReturnResultSet(RESULT_LIST,new RowMapper<Integer>() {
			@Override
			public Integer mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rs.getInt(1);
			}
		}));
		declareParameter(new SqlParameter(code_tiers, Types.VARCHAR));
		declareParameter(new SqlParameter(adr1, Types.VARCHAR));
		declareParameter(new SqlParameter(adr2, Types.VARCHAR));
		declareParameter(new SqlParameter(bur_post, Types.VARCHAR));
		declareParameter(new SqlParameter(code_post, Types.VARCHAR));
		declareParameter(new SqlParameter(ville, Types.VARCHAR));
		declareParameter(new SqlParameter(cedex, Types.VARCHAR));

		declareParameter(new SqlParameter(pays, Types.VARCHAR));
		declareParameter(new SqlParameter(no_tel, Types.VARCHAR));
		declareParameter(new SqlParameter(no_fax, Types.VARCHAR));
		declareParameter(new SqlParameter(type_adr, Types.VARCHAR));
		declareParameter(new SqlParameter(silencieux, Types.BIT));

		compile();
	}

	public static PsTiersAdrCree getInstance(JdbcTemplate jdbcTemplate) {
		if (instance == null)
			instance = new PsTiersAdrCree(jdbcTemplate);
		return instance;
	}

	@SuppressWarnings("unchecked")
	public Integer createAdr(String codeTiers, Adresse adresse) {
		getLogger().debug("create adr pour le tiers {}",codeTiers);
		Map<String, Object> inputs = new HashMap<String, Object>();

		inputs.put(code_tiers, codeTiers);
		inputs.put(adr1, adresse.getAdresse1());
		inputs.put(adr2, adresse.getAdresse2());
		inputs.put(bur_post, null);
		inputs.put(code_post, adresse.getCodePostale());
		inputs.put(ville, adresse.getVille());
		inputs.put(cedex, null);
		inputs.put(pays, adresse.getPays());
		inputs.put(no_tel, null);
		inputs.put(no_fax, null);
		inputs.put(type_adr, "C");
		inputs.put(silencieux, 0);
		
		Map<String, Object> resultMap = super.execute(inputs);
		Integer no_adr = ((List<Integer>) resultMap.get(RESULT_LIST)).get(0);
		getLogger().debug("create adr {} pour le tiers {} terminé",no_adr,codeTiers);
		return no_adr;
		
	}
}
