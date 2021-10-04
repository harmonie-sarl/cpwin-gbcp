package fr.symphonie.budget.core.dao.ps.tiers;

import java.sql.Types;
import java.util.HashMap;
import java.util.Map;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SqlParameter;

import fr.symphonie.budget.core.dao.ps.GenericBudgetPs;

public class PsTiersEtatMaj extends GenericBudgetPs {
	private static PsTiersEtatMaj instance = null;
	public static final String SP_NAME = "PsTiersEtatMaj";

	protected PsTiersEtatMaj(JdbcTemplate jdbcTemplate) {
		super(jdbcTemplate, SP_NAME);

		declareParameter(new SqlParameter(code_tiers, Types.VARCHAR));
		declareParameter(new SqlParameter(etat, Types.VARCHAR));
		compile();
	}

	public static PsTiersEtatMaj getInstance(JdbcTemplate jdbcTemplate) {
		if (instance == null)
			instance = new PsTiersEtatMaj(jdbcTemplate);
		return instance;
	}
	public void setOuvert(String tiers) {
		Map<String, Object> inputs = new HashMap<String, Object>();

		inputs.put(code_tiers, tiers);
		inputs.put(etat, "O");
		super.execute(inputs);
	}

}
