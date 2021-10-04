package fr.symphonie.budget.core.dao.ps.sf;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.SqlReturnResultSet;

import fr.symphonie.budget.core.dao.ps.GenericBudgetPs;
import fr.symphonie.budget.core.model.pluri.LigneBudgetaireAE;
import fr.symphonie.cpwin.model.Tiers;
import fr.symphonie.tools.nantes.model.Operation;
import fr.symphonie.tools.nantes.model.SFParams;
import fr.symphonie.util.Helper;

public class PsGbcpSFCreeSimple extends GenericBudgetPs {
	private static PsGbcpSFCreeSimple instance = null;
	public static final String SP_NAME = "PsGbcpSFCreeSimple";

	protected PsGbcpSFCreeSimple(JdbcTemplate jdbcTemplate) {
		super(jdbcTemplate, SP_NAME);
		declareParameter(new SqlReturnResultSet(RESULT_LIST,new RowMapper<Integer>() {
			@Override
			public Integer mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rs.getInt(1);
			}
		}));
		declareParameter(new SqlParameter(num_exec, Types.SMALLINT));
		declareParameter(new SqlParameter(code_budg, Types.VARCHAR));
		declareParameter(new SqlParameter(code_dest, Types.VARCHAR));
		declareParameter(new SqlParameter(code_nature, Types.VARCHAR));
		declareParameter(new SqlParameter(code_service, Types.VARCHAR));
		declareParameter(new SqlParameter(code_prog, Types.VARCHAR));
		declareParameter(new SqlParameter(code_gest, Types.VARCHAR));
		declareParameter(new SqlParameter(no_eo, Types.INTEGER));
		
		declareParameter(new SqlParameter(libelle, Types.VARCHAR));
		declareParameter(new SqlParameter(date_sf, Types.VARCHAR));
		declareParameter(new SqlParameter(code_prm, Types.VARCHAR));
		declareParameter(new SqlParameter(tiers, Types.VARCHAR));
		declareParameter(new SqlParameter(no_adr, Types.TINYINT));
		declareParameter(new SqlParameter(no_iban, Types.TINYINT));
		declareParameter(new SqlParameter(mode_paie, Types.VARCHAR));
		declareParameter(new SqlParameter(imput_htd, Types.VARCHAR));
		declareParameter(new SqlParameter(qte, Types.FLOAT));
		declareParameter(new SqlParameter(ht_sf, Types.DOUBLE));
		declareParameter(new SqlParameter(taux_tva, Types.FLOAT));
		declareParameter(new SqlParameter(ttc_sf, Types.DOUBLE));
		declareParameter(new SqlParameter(htd_sf, Types.DOUBLE));
		
		declareParameter(new SqlParameter(createur, Types.VARCHAR));
		declareParameter(new SqlParameter(silencieux, Types.BIT));
		
		compile();
	}

	public static PsGbcpSFCreeSimple getInstance(JdbcTemplate jdbcTemplate) {
		if (instance == null)
			instance = new PsGbcpSFCreeSimple(jdbcTemplate);
		return instance;
	}
	@SuppressWarnings("unchecked")
	public Integer createSF(SFParams sfParams,LigneBudgetaireAE lb, Operation op) {
		Map<String, Object> inputs = getDefaultValues();
		//Etudiant actor=op.getActor();
		Tiers actor=op.getActor();
		//remplissage
		inputs.put(num_exec, lb.getExercice());
		inputs.put(code_budg, lb.getCodeBudget());
		inputs.put(code_dest, lb.getDestination());
		inputs.put(code_nature, lb.getNature());
		inputs.put(code_service,lb.getService());
		inputs.put(code_prog, lb.getProgramme());	
		
//		inputs.put(tiers, actor.getCodeCpwin());
//		inputs.put(no_adr,actor.getAdresse().getNumero());
//		inputs.put(no_iban, actor.getIban().getNumero());
		
		inputs.put(tiers, actor.getCode());
		inputs.put(no_adr,actor.getAdresse().getOrdre());
		inputs.put(no_iban, op.getIban().getOrdre());
		
		inputs.put(ht_sf, op.getAmount().doubleValue());
		inputs.put(ttc_sf,  op.getAmount().doubleValue());
		inputs.put(htd_sf,  op.getAmount().doubleValue());
		inputs.put(libelle, op.getLibelle());
		inputs.put(imput_htd, sfParams.getCompteProduit());
				
		inputs.put(code_gest, sfParams.getCodeGest());
		inputs.put(no_eo, sfParams.getNumeroEO());			
		inputs.put(code_prm, sfParams.getCodePRM());	
		
		inputs.put(mode_paie, op.getModePaie());	
			
		
		//Execution
		
		Map<String, Object> resultMap = super.execute(inputs);
		Integer result = ((List<Integer>) resultMap.get(RESULT_LIST)).get(0);
		getLogger().debug("createSF : end : no_pre_sf={}", result);
		return result;
	}
	private Map<String, Object> getDefaultValues() {
		Map<String, Object> inputs = new HashMap<String, Object>();
		inputs.put(qte, 1);
		inputs.put(taux_tva, 0);
		inputs.put(createur, "SF");
		inputs.put(silencieux, 0);
		inputs.put(date_sf, Helper.toSimpleFormat2(new Date()));
		
		return inputs;
	}

}
