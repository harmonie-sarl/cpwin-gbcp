package fr.symphonie.budget.core.dao.ps;

import static fr.symphonie.budget.core.dao.ps.ColumnName.CP_col;
import static fr.symphonie.budget.core.dao.ps.ColumnName.EJ_col;
import static fr.symphonie.budget.core.dao.ps.ColumnName.No_ej_col;
import static fr.symphonie.budget.core.dao.ps.ColumnName.besoin_col;
import static fr.symphonie.budget.core.dao.ps.ColumnName.co_col;
import static fr.symphonie.budget.core.dao.ps.ColumnName.code_dest_col;
import static fr.symphonie.budget.core.dao.ps.ColumnName.code_nature_col;
import static fr.symphonie.budget.core.dao.ps.ColumnName.code_prog_col;
import static fr.symphonie.budget.core.dao.ps.ColumnName.code_service_col;
import static fr.symphonie.budget.core.dao.ps.ColumnName.code_tiers_col;
import static fr.symphonie.budget.core.dao.ps.ColumnName.compl_col;
import static fr.symphonie.budget.core.dao.ps.ColumnName.cred_annul_col;
import static fr.symphonie.budget.core.dao.ps.ColumnName.differe_col;
import static fr.symphonie.budget.core.dao.ps.ColumnName.disp_ej_col;
import static fr.symphonie.budget.core.dao.ps.ColumnName.dispo_col;
import static fr.symphonie.budget.core.dao.ps.ColumnName.dp_ant_col;
import static fr.symphonie.budget.core.dao.ps.ColumnName.dp_ex;
import static fr.symphonie.budget.core.dao.ps.ColumnName.dp_net_col;
import static fr.symphonie.budget.core.dao.ps.ColumnName.dp_tot_col;
import static fr.symphonie.budget.core.dao.ps.ColumnName.dr;
import static fr.symphonie.budget.core.dao.ps.ColumnName.flech_glob_col;
import static fr.symphonie.budget.core.dao.ps.ColumnName.mttot_ej_col;
import static fr.symphonie.budget.core.dao.ps.ColumnName.no_lbi_col;
import static fr.symphonie.budget.core.dao.ps.ColumnName.nom_tiers_col;
import static fr.symphonie.budget.core.dao.ps.ColumnName.objet_col;
import static fr.symphonie.budget.core.dao.ps.ColumnName.prevu_ex_col;
import static fr.symphonie.budget.core.dao.ps.ColumnName.rap_act_col;
import static fr.symphonie.budget.core.dao.ps.ColumnName.rap_ant_col;
import static fr.symphonie.budget.core.dao.ps.ColumnName.rap_diff_col;
import static fr.symphonie.budget.core.dao.ps.ColumnName.rap_ex_col;
import static fr.symphonie.budget.core.dao.ps.ColumnName.rap_ini_col;

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

public class PsBudgSuiviAECP extends GenericBudgetPs {

	private static PsBudgSuiviAECP instanceStyle1 = null;
	// private static PsBudgSuiviAECP instanceStyle2=null;
	private static PsBudgSuiviAECP instanceStyle3 = null;
	public static final String SP_NAME = "PsBudgSuiviAECP";
	private static final Logger logger = LoggerFactory.getLogger(PsBudgSuiviAECP.class);
	private static final String RESULT_LIST = "RESULT_LIST";

	public enum StyleEnum {
		Style1(1), Style2(2), Style3(3);

		private Integer value;

		private StyleEnum(Integer value) {
			this.value = value;

		}

		public Integer getValue() {
			return value;
		}

	}

	@Override
	protected Logger getLogger() {
		return logger;
	}

	/**
	 * @style int = 0, style de retour
	 * @num_exec_cp smallint, exercice CP
	 * @code_budg TBUDGET = '%', optionnel : budget
	 * @dest_grp TCODE = '%', optionnel : groupe de fongibilité des destinations
	 * @nat_grp TCODE = '%', optionnel : groupe de fongibilité des natures
	 * @code_dest TCODE = '%', optionnel : destination
	 * @code_nature TCODE = '%', optionnel : nature
	 * @code_service TCODE = '%', optionnel : service
	 * @code_prog TCODE = '%', optionnel : programme
	 * 
	 * 
	 * 
	 **/

	RowMapper<Map<String, Object>> rowMapperStyle1 = new RowMapper<Map<String, Object>>() {

		@Override
		public Map<String, Object> mapRow(ResultSet rs, int rowNum) throws SQLException {
			Map<String, Object> map = new HashMap<String, Object>();
			String flechGlob = rs.getString(flech_glob_col);
			map.put(code_dest_col, rs.getString(code_dest_col));
			map.put(code_nature_col, rs.getString(code_nature_col));
			map.put(code_service_col, rs.getString(code_service_col));
			map.put(code_prog_col, rs.getString(code_prog_col));
			map.put(flech_glob_col, flechGlob != null ? flechGlob.trim() : "");
			//map.put(flech_glob_col, rs.getString(flech_glob_col));
			map.put(rap_ini_col, rs.getDouble(rap_ini_col));
			map.put(cred_annul_col, rs.getDouble(cred_annul_col));
			map.put(compl_col, rs.getDouble(compl_col));
			map.put(dp_ant_col, rs.getDouble(dp_ant_col));
			map.put(rap_ant_col, rs.getDouble(rap_ant_col));
			map.put(co_col, rs.getDouble(co_col));
			map.put(EJ_col, rs.getDouble(EJ_col));
			map.put(dp_ex, rs.getDouble(dp_ex));
			map.put(rap_ex_col, rs.getDouble(rap_ex_col));
			map.put(dp_tot_col, rs.getDouble(dp_tot_col));
			map.put(dr, rs.getDouble(dr));
			map.put(dp_net_col, rs.getDouble(dp_net_col));
			map.put(rap_act_col, rs.getDouble(rap_act_col));
			map.put(disp_ej_col, rs.getDouble(disp_ej_col));
			map.put(rap_diff_col, rs.getDouble(rap_diff_col));
			map.put(CP_col, rs.getDouble(CP_col));
			map.put(dispo_col, rs.getString(dispo_col));
			map.put(besoin_col, rs.getDouble(besoin_col));

			return map;
		};
	};

	RowMapper<Map<String, Object>> rowMapperStyle3 = new RowMapper<Map<String, Object>>() {

		@Override
		public Map<String, Object> mapRow(ResultSet rs, int rowNum) throws SQLException {
			Map<String, Object> map = new HashMap<String, Object>();
			String flechGlob = rs.getString(flech_glob_col);
			
			map.put(code_dest_col, rs.getString(code_dest_col));
			map.put(code_nature_col, rs.getString(code_nature_col));
			map.put(code_service_col, rs.getString(code_service_col));
			map.put(code_prog_col, rs.getString(code_prog_col));
			map.put(flech_glob_col, flechGlob != null ? flechGlob.trim() : "");
			//map.put(flech_glob_col, rs.getString(flech_glob_col));
			map.put(no_lbi_col, rs.getInt(no_lbi_col));
			map.put(No_ej_col, rs.getInt(No_ej_col));
			map.put(objet_col, rs.getString(objet_col));
			map.put(code_tiers_col, rs.getString(code_tiers_col));
			map.put(nom_tiers_col, rs.getString(nom_tiers_col));
			map.put(mttot_ej_col, rs.getDouble(mttot_ej_col));
			map.put(prevu_ex_col, rs.getDouble(prevu_ex_col));
			map.put(differe_col, rs.getDouble(differe_col));

			return map;
		};
	};

	protected PsBudgSuiviAECP(JdbcTemplate jdbcTemplate, StyleEnum style) {

		super(jdbcTemplate, SP_NAME);
		RowMapper<Map<String, Object>> rowMapper = null;
		switch (style) {
		case Style1:
			rowMapper = rowMapperStyle1;
			break;
		case Style3:
			rowMapper = rowMapperStyle3;
			break;
		default:
			break;
		}

		declareParameter(new SqlReturnResultSet(RESULT_LIST, rowMapper));
		declareParameter(new SqlParameter(STYLE, Types.INTEGER));
		declareParameter(new SqlParameter(num_exec, Types.INTEGER));

		declareParameter(new SqlParameter(nat_grp, Types.VARCHAR));

		compile();
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getData(

			int style, int numExec, String natGrp) {
		logger.debug("getData - start");
		Map<String, Object> inputs = new HashMap<String, Object>();

		inputs.put(STYLE, style);
		inputs.put(num_exec, numExec);

		inputs.put(nat_grp, natGrp);

		Map<String, Object> result = super.execute(inputs);
		List<Map<String, Object>> list = (List<Map<String, Object>>) result.get(RESULT_LIST);
		logger.debug("getData: " + list.size() + "-  end");

		return list;
	}

	public static PsBudgSuiviAECP getInstanceStyle1(JdbcTemplate jdbcTemplate) {
		if (instanceStyle1 == null)
			instanceStyle1 = new PsBudgSuiviAECP(jdbcTemplate, StyleEnum.Style1);
		return instanceStyle1;
	}

	public static PsBudgSuiviAECP getInstanceStyle3(JdbcTemplate jdbcTemplate) {
		if (instanceStyle3 == null)
			instanceStyle3 = new PsBudgSuiviAECP(jdbcTemplate, StyleEnum.Style3);
		return instanceStyle3;
	}

}
