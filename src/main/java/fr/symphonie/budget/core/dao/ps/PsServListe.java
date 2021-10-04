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

public class PsServListe extends GenericBudgetPs{ 	
	

		private static PsServListe instance=null;
		public static final String SP_NAME="PsServListe";
		private static final Logger logger = LoggerFactory.getLogger(PsServListe.class);
		private static final String RESULT_LIST = "RESULT_LIST";
//		private static final int TOUS =100;
		
		@Override
		protected Logger getLogger() {
			return logger;
		}
		
/**	    @style          	int     = 0,
	    	    @num_exec       	int     = 0,
	    	    @code_budg      	TBUDGET = null,
	    	    @dest_grp       	TCODE   = null,
	    	    @code_dest      	TCODE   = null,
	    	    @code_prog      	TCODE   = null,
	    	    @nat_grp        	TCODE   = null,
	    	    @code_nature       	TCODE   = null,
	    	    @num_nomenc     	TCODE   = null,
	    	    @code_direction 	TCODE   = null,
	    	    @code_service   	TCODE   = null,
	    	    @freserve       	bit     = 0,
	    	    @code_dirnon    	TCODE   = null,
	    	    @depenses       	bit     = 1,
	    	    @recettes       	bit     = 1,
	    	    @central        	bit     = 0,
	    	    @fongible			int     = null,
	    	    @consult			bit     = 0,		si 1 : seulement les BudgInterneLignes (le cas échéant) qui ont actions=0) 
	    		@ouvert				int     = null,
	    		@code_util			TCODE   = null,	    utilisateur pour cpwin Web 
	    		@mdt_ordre          int     = 2,		 mandat ordre des LBI : (0) non,(1) oui, (2) tout  
	    		@gestion        	bit     = 0,		 si 1 : Service en fonction des DroitUtilGest (util uniquement en style 2) 
	    		@ServStr			bit     = 0			 si 1 : uniquement les services autorisant la sous-traitance 
	**/	
		
	
		protected PsServListe(JdbcTemplate jdbcTemplate) {
			super(jdbcTemplate, SP_NAME);
			RowMapper<Map<String, Object>> rowMapper=new RowMapper<Map<String, Object>>(){

				@Override
				public Map<String, Object> mapRow(ResultSet rs, int rowNum)
						throws SQLException {
					Map<String, Object> map=new HashMap<String, Object>();			
					map.put(code_service_col, rs.getString(code_service_col));
					map.put(nom_service_col, rs.getString(nom_service_col));
					
					return map;
				}
				;
			};
			
		            	
		declareParameter(new SqlReturnResultSet(RESULT_LIST, rowMapper));

		declareParameter(new SqlParameter(STYLE, Types.INTEGER));
		declareParameter(new SqlParameter(num_exec, Types.INTEGER));
		declareParameter(new SqlParameter(code_budg, Types.VARCHAR));
		declareParameter(new SqlParameter(dest_grp, Types.VARCHAR));
		declareParameter(new SqlParameter(code_dest, Types.VARCHAR));
		declareParameter(new SqlParameter(code_prog, Types.VARCHAR));
		declareParameter(new SqlParameter(nat_grp, Types.VARCHAR));
		declareParameter(new SqlParameter(code_nature, Types.VARCHAR));
		declareParameter(new SqlParameter(num_nomenc, Types.VARCHAR));
		declareParameter(new SqlParameter(code_direction, Types.VARCHAR));
		declareParameter(new SqlParameter(code_service, Types.VARCHAR));
		declareParameter(new SqlParameter(freserve, Types.BIT));
		declareParameter(new SqlParameter(code_dirnon, Types.VARCHAR));
		declareParameter(new SqlParameter(depenses, Types.BIT));
		declareParameter(new SqlParameter(recettes, Types.BIT));
		declareParameter(new SqlParameter(central, Types.BIT));
		declareParameter(new SqlParameter(fongible, Types.INTEGER));
		declareParameter(new SqlParameter(consult, Types.BIT));
		declareParameter(new SqlParameter(ouvert, Types.INTEGER));
		declareParameter(new SqlParameter(code_util, Types.VARCHAR));
		declareParameter(new SqlParameter(mdt_ordre, Types.INTEGER));
		declareParameter(new SqlParameter(gestion, Types.BIT));
		declareParameter(new SqlParameter(ServStr, Types.BIT));

		compile();
	}
		
	//	psServListe 2, Exercice CP, Budget, '', '', '', '', '', '', Direction, '', 1, '', 1, 1, Profil Central [Booléan], null, 0, null, Utilisateur, null, Habilitation sur droit de gestion [Booléen
		
		@SuppressWarnings("unchecked")
		public List<Map<String, Object>> getData(int numexec, String codeBudg , String codeDirection){
			logger.debug("getData - start");
				Map<String, Object> inputs = new HashMap<String, Object> ();
				
				
				inputs.put(STYLE, 2);
				inputs.put(num_exec,numexec);
				inputs.put(code_budg,codeBudg);
				inputs.put(dest_grp, "");
				inputs.put(code_dest, "");
				inputs.put(code_prog,"");
				inputs.put(nat_grp, "");
				inputs.put(code_nature,"");
				inputs.put(num_nomenc, "");
				inputs.put(code_direction,codeDirection);
				inputs.put(code_service, "");
				inputs.put(freserve, 1);
				inputs.put(code_dirnon,"");
				inputs.put(depenses, 1);
				inputs.put(recettes, 1);
				inputs.put(central, 0);
				inputs.put(fongible, null);
				inputs.put(consult, 0);
				inputs.put(ouvert,null);
				inputs.put(code_util, getCodeUser());
				inputs.put(mdt_ordre, null);
				inputs.put(gestion, 1);
				inputs.put(ServStr, 0);	
				
				
				
				Map<String, Object> result = super.execute(inputs);
				List<Map<String, Object>> list=(List<Map<String, Object>>) result.get(RESULT_LIST);
				logger.debug("getData: "+list.size()+"-  end");
		
				return list;	
			}
		
		
		public static PsServListe getInstance(JdbcTemplate jdbcTemplate) {
			if(instance==null)instance=new PsServListe(jdbcTemplate);
			return instance;
		}
		

}
