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
import fr.symphonie.common.util.Constant;
import fr.symphonie.tools.nantes.model.Etudiant;

public class PsTiersCree extends GenericBudgetPs {
	private static PsTiersCree instance = null;
	public static final String SP_NAME = "PsTiersCree";

	protected PsTiersCree(JdbcTemplate jdbcTemplate) {
		super(jdbcTemplate, SP_NAME);
		declareParameter(new SqlReturnResultSet(RESULT_LIST,new RowMapper<String>() {
			@Override
			public String mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rs.getString(1);
			}
		}));

		declareParameter(new SqlParameter(code_tiers, Types.VARCHAR));
		declareParameter(new SqlParameter(nom_tiers, Types.VARCHAR));
		declareParameter(new SqlParameter(sigle, Types.VARCHAR));
		declareParameter(new SqlParameter(enseigne, Types.VARCHAR));
		declareParameter(new SqlParameter(capital, Types.VARCHAR));
		declareParameter(new SqlParameter(forme_soc, Types.VARCHAR));
		declareParameter(new SqlParameter(no_siren, Types.VARCHAR));
		declareParameter(new SqlParameter(code_ape, Types.VARCHAR));
		declareParameter(new SqlParameter(code_euro, Types.VARCHAR));
		declareParameter(new SqlParameter(nom_resp, Types.VARCHAR));
		declareParameter(new SqlParameter(nom_contact, Types.VARCHAR));
		declareParameter(new SqlParameter(tel_contact, Types.VARCHAR));
		declareParameter(new SqlParameter(fax_contact, Types.VARCHAR));
		declareParameter(new SqlParameter(mail_contact, Types.VARCHAR));
		declareParameter(new SqlParameter(est_confidentiel, Types.BIT));
		declareParameter(new SqlParameter(code_paie, Types.VARCHAR));
		declareParameter(new SqlParameter(delai, Types.INTEGER));
		declareParameter(new SqlParameter(observations, Types.VARCHAR));
		declareParameter(new SqlParameter(est_soumis_cmp, Types.BIT));
		declareParameter(new SqlParameter(flag_cession, Types.VARCHAR));
		declareParameter(new SqlParameter(flag_judiciaire, Types.VARCHAR));
		declareParameter(new SqlParameter(doublon, Types.VARCHAR));
		declareParameter(new SqlParameter(silencieux, Types.BIT));
		declareParameter(new SqlParameter(code_service, Types.VARCHAR));
		declareParameter(new SqlParameter(no_siret_ep, Types.VARCHAR));
		declareParameter(new SqlParameter(regie_dep, Types.BIT));
		declareParameter(new SqlParameter(regie_rec, Types.BIT));
		declareParameter(new SqlParameter(type_tiers, Types.VARCHAR));
		declareParameter(new SqlParameter(civilite, Types.VARCHAR));
		declareParameter(new SqlParameter(nom_pers_phys, Types.VARCHAR));
		declareParameter(new SqlParameter(prenom, Types.VARCHAR));
		declareParameter(new SqlParameter(compl_nom, Types.VARCHAR));
		declareParameter(new SqlParameter(cat_tiers, Types.VARCHAR));
		declareParameter(new SqlParameter(nat_jur, Types.VARCHAR));
		declareParameter(new SqlParameter(nat_id_tiers, Types.VARCHAR));
		declareParameter(new SqlParameter(code_regie_dep, Types.VARCHAR));
		declareParameter(new SqlParameter(code_regie_rec, Types.VARCHAR));
		declareParameter(new SqlParameter(force_code_tiers, Types.BIT));
		declareParameter(new SqlParameter(est_artiste, Types.BIT));
		declareParameter(new SqlParameter(art_nom_usage, Types.VARCHAR));
		declareParameter(new SqlParameter(art_nir, Types.VARCHAR));
		declareParameter(new SqlParameter(art_organisme, Types.VARCHAR));
		declareParameter(new SqlParameter(art_num_ordre, Types.VARCHAR));
		declareParameter(new SqlParameter(est_generique, Types.BIT));

		compile();
	}

	public static PsTiersCree getInstance(JdbcTemplate jdbcTemplate) {
		if (instance == null)
			instance = new PsTiersCree(jdbcTemplate);
		return instance;
	}

//	public String createTiers(Etudiant etudiant) {
//		getLogger().debug("createTiers : start : matricule={}, nom et prenom={}", etudiant.getMatricule(),
//				etudiant.getNomEtPrenom());
//		Map<String, Object> inputs = getDefaultValues();
//		inputs.put(nom_tiers, etudiant.getNomEtPrenom());
//		inputs.put(mail_contact, etudiant.getEmail());
//		inputs.put(observations, String.format("Etudiant/Matricule %s", etudiant.getMatricule()));
//		inputs.put(code_paie, etudiant.getPaymentWay());
//		Map<String, Object> resultMap = super.execute(inputs);
//		String result = (String) resultMap.get(code_tiers);
//		getLogger().debug("createTiers : end : code_tiers={}", result);
//		return result;
//	}
	@SuppressWarnings("unchecked")
	public String createTiersForEtudiant(Etudiant etudiant) {
		getLogger().debug("createTiersForEtudiant : start : matricule={}, nom et prenom={}", etudiant.getMatricule(),
				etudiant.getNomEtPrenom());

		Map<String, Object> inputs = new HashMap<String, Object>();

		inputs.put(code_tiers, Constant.NANTES_ENSA_STUDENT_PREFIX);
		inputs.put(nom_tiers, etudiant.getNomEtPrenom());
		inputs.put(sigle, null);
		inputs.put(enseigne, null);
		inputs.put(capital, null);
		inputs.put(forme_soc, "ETUDIANT");
		inputs.put(no_siren, null);
		inputs.put(code_ape, null);
		inputs.put(code_euro, null);
		inputs.put(nom_resp, null);
		inputs.put(nom_contact, null);
		inputs.put(tel_contact, null);
		inputs.put(fax_contact, null);
		inputs.put(mail_contact, etudiant.getEmail());
		inputs.put(est_confidentiel, 0);
		inputs.put(code_paie, etudiant.getPaymentWay());
		inputs.put(delai, null);
		inputs.put(observations, String.format("Etudiant-Matricule %s", etudiant.getMatricule()));
		inputs.put(est_soumis_cmp, 0);
		inputs.put(flag_cession, "");
		inputs.put(flag_judiciaire, "");
		inputs.put(doublon, null);
		inputs.put(silencieux, 0);
		inputs.put(code_service, null);
		inputs.put(no_siret_ep, null);
		inputs.put(regie_dep, 0);
		inputs.put(regie_rec, 0);
		inputs.put(type_tiers, null);
		inputs.put(civilite, null);
		inputs.put(nom_pers_phys, null);
		inputs.put(prenom, null);
		inputs.put(compl_nom, null);
		inputs.put(cat_tiers, null);
		inputs.put(nat_jur, null);
		inputs.put(nat_id_tiers, null);
		inputs.put(code_regie_dep, null);
		inputs.put(code_regie_rec, null);
		inputs.put(force_code_tiers, 0);//1 pour forcer code_tiers
		inputs.put(est_artiste, 0);
		inputs.put(art_nom_usage, null);
		inputs.put(art_nir, null);
		inputs.put(art_organisme, null);
		inputs.put(art_num_ordre, null);
		inputs.put(est_generique, 0);
		
		Map<String, Object> resultMap = super.execute(inputs);
		String result = ((List<String>) resultMap.get(RESULT_LIST)).get(0);
		//String result = (String) resultMap.get(code_tiers);
		getLogger().debug("createTiersForEtudiant : end : code_tiers={}", result);
		return result;
	}

//	private Map<String, Object> getDefaultValues() {
//		Map<String, Object> inputs = new HashMap<String, Object>();
//
//		inputs.put(code_tiers, "SD");
//		inputs.put(sigle, null);
//		inputs.put(enseigne, null);
//		inputs.put(capital, null);
//		inputs.put(forme_soc, "ETUDIANT");
//		inputs.put(no_siren, null);
//		inputs.put(code_ape, null);
//		inputs.put(code_euro, null);
//		inputs.put(nom_resp, null);
//		inputs.put(nom_contact, null);
//		inputs.put(tel_contact, null);
//		inputs.put(fax_contact, null);
//		inputs.put(est_confidentiel, 0);		
//		inputs.put(delai, null);
//		inputs.put(est_soumis_cmp, 0);
//		inputs.put(flag_cession, "");
//		inputs.put(flag_judiciaire, "");
//		inputs.put(doublon, null);
//		inputs.put(silencieux, 0);
//		inputs.put(code_service, null);
//		inputs.put(no_siret_ep, null);
//		inputs.put(regie_dep, 0);
//		inputs.put(regie_rec, 0);
//		inputs.put(type_tiers, null);
//		inputs.put(civilite, null);
//		inputs.put(nom_pers_phys, null);
//		inputs.put(prenom, null);
//		inputs.put(compl_nom, null);
//		inputs.put(cat_tiers, null);
//		inputs.put(nat_jur, null);
//		inputs.put(nat_id_tiers, null);
//		inputs.put(code_regie_dep, null);
//		inputs.put(code_regie_rec, null);
//		inputs.put(force_code_tiers, 0);
//		inputs.put(est_artiste, 0);
//		inputs.put(art_nom_usage, null);
//		inputs.put(art_nir, null);
//		inputs.put(art_organisme, null);
//		inputs.put(art_num_ordre, null);
//		inputs.put(est_generique, 0);
//		return inputs;
//	}
}
