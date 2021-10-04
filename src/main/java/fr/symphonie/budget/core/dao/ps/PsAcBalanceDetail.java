package fr.symphonie.budget.core.dao.ps;

import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.SqlReturnResultSet;

import fr.symphonie.budget.core.dao.ps.mapper.BalanceRowMapper;
import fr.symphonie.budget.core.model.pluri.BalanceItem;

public class PsAcBalanceDetail extends GenericBudgetPs {
	private static PsAcBalanceDetail instance=null;
	public static final String SP_NAME="psAcBalanceDetailRecapImp";
	
		
	public static PsAcBalanceDetail getInstance(JdbcTemplate jdbcTemplate) {
		if(instance==null)instance=new PsAcBalanceDetail(jdbcTemplate);
		return instance;
	}
	protected PsAcBalanceDetail(JdbcTemplate jdbcTemplate) {
		super(jdbcTemplate, SP_NAME);
		RowMapper<BalanceItem> rowMapper=new BalanceRowMapper();
		declareParameter(new SqlReturnResultSet(RESULT_LIST,rowMapper));
		declareParameter(new SqlParameter(num_exec, Types.SMALLINT));
		declareParameter(new SqlParameter(code_budg, Types.VARCHAR));
		declareParameter(new SqlParameter(cpt_deb, Types.VARCHAR));
		declareParameter(new SqlParameter(cpt_fin, Types.VARCHAR));
		declareParameter(new SqlParameter(date_fin, Types.VARCHAR));
		
		declareParameter(new SqlParameter(totalise, Types.INTEGER));
		declareParameter(new SqlParameter(excl_typecr, Types.VARCHAR));
		declareParameter(new SqlParameter(definitive, Types.BIT));
		declareParameter(new SqlParameter(ToutBudget, Types.BIT));
		declareParameter(new SqlParameter(MultiBudgetHorsA, Types.BIT));
		compile();
		
	}
	
	@SuppressWarnings("unchecked")
	public List<BalanceItem> loadBalance(Integer exercice,String codeBudget,String dateFin, boolean isDefinitive){
		getLogger().info("loadBalance: {} / {} - {}",exercice,codeBudget,dateFin);
		List<BalanceItem> resultList=new ArrayList<>();
		Map<String, Object> inputs = new HashMap<String, Object> ();
		//2018,'A','0','ZZZZZZZZZZ','31/01/2018',0,'',0,0,0

		inputs.put(num_exec, exercice);
		inputs.put(code_budg, codeBudget);
		inputs.put(cpt_deb, "0");
		inputs.put(cpt_fin, "ZZZZZZZZZZ");
		inputs.put(date_fin, dateFin);
		inputs.put(totalise, 0);
		inputs.put(excl_typecr, "");
		inputs.put(definitive, isDefinitive);
		inputs.put(ToutBudget, 0);
		inputs.put(MultiBudgetHorsA, 0);
		
		
		Map<String, Object> result = super.execute(inputs);
		List<BalanceItem> list=(List<BalanceItem>) result.get(RESULT_LIST);
		for(BalanceItem item:list){
		//	getLogger().debug("BalanceItem: Niveau -> {}",item.getNiveau());
			if(item.getNiveau()==null)resultList.add(item);
		}
		getLogger().info("loadBalance: resultList -> {}",resultList.size());
		return resultList;
		
	}
	

}
