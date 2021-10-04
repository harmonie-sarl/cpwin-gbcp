package fr.symphonie.budget.core.dao.ps;

import static fr.symphonie.budget.core.dao.ps.ColumnName.*;

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

public class PsGbcpSuiviCP extends GenericBudgetPs {

	
	private static PsGbcpSuiviCP instanceStyle0=null;
	private static PsGbcpSuiviCP instanceStyle12=null;
	public static final String SP_NAME="psGbcpSuiviCP";
	private static final Logger logger = LoggerFactory.getLogger(PsGbcpSuiviCP.class);
	private static final String RESULT_LIST = "RESULT_LIST";
	
	public enum StyleEnum {
		Style0(0), Style12(1);

		private Integer value;

		private StyleEnum(Integer value) {
			this.value = value;

		}

		public Integer getValue() {
			return value;
		}

//		public static StyleEnum parse(Integer str) {
//			StyleEnum enumeration = null;
//			if (str == null)
//				return enumeration;
//			for (StyleEnum item : StyleEnum.values()) {
//				if (item.getValue() == str) {
//					enumeration = item;
//					break;
//				}
//			}
//			return enumeration;
//		}
	}
	
	@Override
	protected Logger getLogger() {
		return logger;
	}
/**
	        @style			int			= 0,	 style de retour 
			@num_exec_cp    smallint,            exercice CP 
			@code_budg      TBUDGET		= '%',   optionnel : budget 
			@dest_grp       TCODE		= '%',   optionnel : groupe de fongibilité des destinations 
			@nat_grp        TCODE		= '%',   optionnel : groupe de fongibilité des natures 
			@code_dest      TCODE		= '%',   optionnel : destination 
			@code_nature    TCODE		= '%',	 optionnel : nature 
			@code_direction TCODE		= '%',   optionnel : direction 
			@code_service   TCODE		= '%',   optionnel : service 
			@code_prog      TCODE		= '%',   optionnel : programme 
			@fTorigine		bit			= 0,	 pour les styles 1 et 2 : indique un résultat uniquement sur le tiers origine EJ 
			@central        bit			= 1,     indique si profil central 
			@gestion        bit			= 1,     1 : filtrage sur DroitUtilGest, 0: filtrage sur DroitUtilInterne 
			@code_util		TCODE		= ''	 Utilisateur CPwin Web 
			
			
**/	


	
	RowMapper<Map<String, Object>> rowMapperStyle0 = new RowMapper<Map<String, Object>>() {

		@Override
		public Map<String, Object> mapRow(ResultSet rs, int rowNum) throws SQLException {
			Map<String, Object> map = new HashMap<String, Object>();

			map.put(num_exec_cp_col, rs.getInt(num_exec_cp_col));
			map.put(code_budg_col, rs.getString(code_budg_col));
			map.put(ant_rap_col, rs.getDouble(ant_rap_col));
			map.put(ant_dp_emise_col, rs.getDouble(ant_dp_emise_col));
			map.put(ant_rae_col, rs.getDouble(ant_rae_col));
			map.put(exec_mt_ej_col, rs.getDouble(exec_mt_ej_col));
			map.put(exec_dp_emise_col, rs.getDouble(exec_dp_emise_col));
			map.put(exec_rae_col, rs.getDouble(exec_rae_col));
			
			/***nouvelles colonnes**/
			map.put(bi_col, rs.getDouble(bi_col));
			map.put(br_acc_col , rs.getDouble(br_acc_col));
			map.put(br_projet_col , rs.getDouble(br_projet_col));
			map.put(co_col , rs.getDouble(co_col));
			map.put(dispo_servv_col , rs.getDouble(dispo_servv_col));
			map.put(dispo_officiel_col , rs.getDouble(dispo_officiel_col));
			map.put(ej_exec_col , rs.getDouble(ej_exec_col));
			map.put(rap_ini_col , rs.getDouble(rap_ini_col));
			map.put(a_payer_col , rs.getDouble(a_payer_col));
			map.put(dp_em_col , rs.getDouble(dp_em_col));
			map.put(dp_pec_col, rs.getDouble(dp_pec_col));
			map.put(ca_col, rs.getDouble(ca_col));
			
			return map;
		};
	};
	
	
	RowMapper<Map<String, Object>> rowMapperStyle12 = new RowMapper<Map<String, Object>>() {

		@Override
		public Map<String, Object> mapRow(ResultSet rs, int rowNum) throws SQLException {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put(num_exec_cp_col, rs.getInt(num_exec_cp_col));
			map.put(code_budg_col, rs.getString(code_budg_col));
			map.put(nat_grp_col, rs.getString(nat_grp_col));
			map.put(code_dest_col, rs.getString(code_dest_col));
			map.put(code_nature_col, rs.getString(code_nature_col));
			map.put(code_service_col, rs.getString(code_service_col));
			map.put(code_prog_col, rs.getString(code_prog_col));
			map.put(EJ_col, rs.getString(EJ_col));
			map.put(ej_htd_cp_col, rs.getDouble(ej_htd_cp_col));
			map.put(TIERS_col, rs.getString(TIERS_col));
			map.put(lib_cej_col, rs.getString(lib_cej_col));
			map.put(EI_col, rs.getString(EI_col));
			map.put(dp_saisie_col, rs.getDouble(dp_saisie_col));
			map.put(dp_emise_col, rs.getDouble(dp_emise_col));
			map.put(dp_pec_col, rs.getDouble(dp_pec_col));
			map.put(ca_col, rs.getDouble(ca_col));
			map.put(rap_col, rs.getDouble(rap_col));
			map.put(nom_prog_col, rs.getString(nom_prog_col));
			return map;
		};
	};

	
	protected PsGbcpSuiviCP(JdbcTemplate jdbcTemplate, StyleEnum style) {

		super(jdbcTemplate, SP_NAME);
		RowMapper<Map<String, Object>> rowMapper = null;
		switch (style) {
		case Style0:
			rowMapper = rowMapperStyle0;
			break;
		case Style12:
			rowMapper = rowMapperStyle12;
			break;
		default:
			break;
		}

		declareParameter(new SqlReturnResultSet(RESULT_LIST, rowMapper));
		declareParameter(new SqlParameter(STYLE, Types.INTEGER));
		declareParameter(new SqlParameter(num_exec_cp, Types.INTEGER));
		declareParameter(new SqlParameter(code_budg, Types.VARCHAR));
		declareParameter(new SqlParameter(dest_grp, Types.VARCHAR));
		declareParameter(new SqlParameter(nat_grp, Types.VARCHAR));
		declareParameter(new SqlParameter(code_dest, Types.VARCHAR));
		declareParameter(new SqlParameter(code_nature, Types.VARCHAR));
		declareParameter(new SqlParameter(code_direction, Types.VARCHAR));
		declareParameter(new SqlParameter(code_service, Types.VARCHAR));
		declareParameter(new SqlParameter(code_prog, Types.VARCHAR));
		declareParameter(new SqlParameter(fTorigine, Types.BIT));
		declareParameter(new SqlParameter(central, Types.BIT));
		declareParameter(new SqlParameter(gestion, Types.BIT));
		declareParameter(new SqlParameter(code_util, Types.VARCHAR));

		compile();
	}
	
	
	
	


	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getData(

			int style, int numExecCp, String codeBudg, String destGrp, String natGrp, String codeDest,
			String codeNature, String codeDirection, String codeService, String codeProg,Integer tiersOrigine

	) {
		logger.debug("getData - start");
		Map<String, Object> inputs = new HashMap<String, Object>();

		inputs.put(STYLE, style);
		inputs.put(num_exec_cp, numExecCp);
		inputs.put(code_budg, codeBudg);
		inputs.put(dest_grp, destGrp);
		inputs.put(nat_grp, natGrp);
		inputs.put(code_dest, codeDest);
		inputs.put(code_nature, codeNature);
		inputs.put(code_direction, codeDirection);
		inputs.put(code_service, codeService);
		inputs.put(code_prog, codeProg);
		inputs.put(fTorigine, (tiersOrigine==null)?0:tiersOrigine);
		inputs.put(central, 1);
		inputs.put(gestion, 1);
		inputs.put(code_util, getCodeUser());

		Map<String, Object> result = super.execute(inputs);
		List<Map<String, Object>> list = (List<Map<String, Object>>) result.get(RESULT_LIST);
		logger.debug("getData: " + list.size() + "-  end");

		return list;
	}
		

	            
	            
	public static PsGbcpSuiviCP getInstanceStyle0(JdbcTemplate jdbcTemplate) {
		if (instanceStyle0 == null)
			instanceStyle0 = new PsGbcpSuiviCP(jdbcTemplate, StyleEnum.Style0);
		return instanceStyle0;
	}
	
	public static PsGbcpSuiviCP getInstanceStyle12(JdbcTemplate jdbcTemplate) {
		if (instanceStyle12 == null)
			instanceStyle12 = new PsGbcpSuiviCP(jdbcTemplate, StyleEnum.Style12);
		return instanceStyle12;
	}
	
}
