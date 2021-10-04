package fr.symphonie.budget.core.dao.ps;

import static fr.symphonie.budget.core.dao.ps.ColumnName.nat_grp_col;
import static fr.symphonie.budget.core.dao.ps.ColumnName.nom_nat_grp_col;


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

public class PsNatGrpListe extends GenericBudgetPs{ 	
	

	private static PsNatGrpListe instance=null;
	public static final String SP_NAME="PsNatGrpListe";
	private static final Logger logger = LoggerFactory.getLogger(PsNatGrpListe.class);
	private static final String RESULT_LIST = "RESULT_LIST";

	
	@Override
	protected Logger getLogger() {
		return logger;
	}
	
	/**
	            @style              int		= 0,         Style liste 
			    @num_exec           int		= 0,         exercice 
			    @code_budg          TBUDGET = null,		 budget 
			    @dest_grp           TCODE	= null,      groupe de fongibilité de destinations 
			    @code_dest          TCODE	= null,		 destination 
			    @nat_grp            TCODE	= null,		 groupe de fongibilité de natures (non utilisé) 
			    @code_nature        TCODE	= null,      nature (non utilisé) 			   
			    @num_nomenc         TCODE	= null,      compte (non utilisé) 
			    @num_nomenc_interne TCODE	= null,
			    @code_gest          TCODE	= null,      pole gestionnqire  
			    @freserve           bit		= 0,         identifie avec ligne de reserve (non utilisé) 
			    @depenses           bit		= 1,         identifie nature de type depense 
			    @recettes           bit		= 1,         identifie nature de type recette 
			    @central            bit		= 1,
			    @fongible           int		= null,
			    @consult            bit		= 0,         si 1 : seulement les BudgInterneLignes (le cas échéant) qui ont actions = 0) 
			    @ouvert             int		= null,
			    @code_util			TCODE	= null,		 utilisateur pour cpwin Web 
			    @mdt_ordre          int		= 2          mandat d'ordre des LBI : 0 = non / 1 = oui / 2 = tout   

          **/
	
	
	protected PsNatGrpListe(JdbcTemplate jdbcTemplate) {
		super(jdbcTemplate, SP_NAME);
		RowMapper<Map<String, Object>> rowMapper = new RowMapper<Map<String, Object>>() {

			@Override
			public Map<String, Object> mapRow(ResultSet rs, int rowNum) throws SQLException {
				Map<String, Object> map = new HashMap<String, Object>();
				map.put(nat_grp_col, rs.getString(nat_grp_col));
				map.put(nom_nat_grp_col, rs.getString(nom_nat_grp_col));

				return map;
			};
		};

		declareParameter(new SqlReturnResultSet(RESULT_LIST, rowMapper));

		declareParameter(new SqlParameter(STYLE, Types.INTEGER));
		declareParameter(new SqlParameter(num_exec, Types.INTEGER));
		declareParameter(new SqlParameter(code_budg, Types.VARCHAR));
		declareParameter(new SqlParameter(dest_grp, Types.VARCHAR));
		declareParameter(new SqlParameter(code_dest, Types.VARCHAR));
		declareParameter(new SqlParameter(nat_grp, Types.VARCHAR));
		declareParameter(new SqlParameter(code_nature, Types.VARCHAR));
		declareParameter(new SqlParameter(num_nomenc, Types.VARCHAR));
		declareParameter(new SqlParameter(num_nomenc_interne, Types.VARCHAR));
		declareParameter(new SqlParameter(code_gest, Types.VARCHAR));
		declareParameter(new SqlParameter(freserve, Types.BIT));
		declareParameter(new SqlParameter(depenses, Types.BIT));
		declareParameter(new SqlParameter(recettes, Types.BIT));
		declareParameter(new SqlParameter(central, Types.BIT));
		declareParameter(new SqlParameter(fongible, Types.INTEGER));
		declareParameter(new SqlParameter(consult, Types.BIT));
		declareParameter(new SqlParameter(ouvert, Types.INTEGER));
		declareParameter(new SqlParameter(code_util, Types.VARCHAR));
		declareParameter(new SqlParameter(mdt_ordre, Types.INTEGER));

		compile();
	}
	
	
//	psNatGrpListe 3, Exercice CP, Budget, Fong.Dest., Destination, '', '', '', '', '', 1, 1, 1, Profil Central [Booléan], null, 0, null, Utilisateur

	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getData(int numexec, String codeBudg, String codeFongDest, String codeDest, String codeNature) {
		logger.debug("getData - start");
		Map<String, Object> inputs = new HashMap<String, Object>();

//		inputs.put(STYLE, 2);

		inputs.put(STYLE,3);
		inputs.put(num_exec,numexec);
		inputs.put(code_budg,codeBudg);
		inputs.put(dest_grp, codeFongDest);
		inputs.put(code_dest, codeDest);
		inputs.put(nat_grp,"");
		inputs.put(code_nature, codeNature);
		inputs.put(num_nomenc, "");
		inputs.put(num_nomenc_interne, "");
		inputs.put(code_gest, "");
		inputs.put(freserve, 1);
		inputs.put(depenses, 1);
		inputs.put(recettes, 1);
		inputs.put(central, 1);
		inputs.put(fongible, null);
		inputs.put(consult, 0);
		inputs.put(ouvert, null);
		inputs.put(code_util, getCodeUser());
		inputs.put(mdt_ordre, 2);
		
		
		Map<String, Object> result = super.execute(inputs);
		List<Map<String, Object>> list = (List<Map<String, Object>>) result.get(RESULT_LIST);
		logger.debug("getData: " + list.size() + "-  end");

		return list;
	}
	
	

	public static PsNatGrpListe getInstance(JdbcTemplate jdbcTemplate) {
		if (instance == null)
			instance = new PsNatGrpListe(jdbcTemplate);
		return instance;
	}
	
	
	
	
	
}
