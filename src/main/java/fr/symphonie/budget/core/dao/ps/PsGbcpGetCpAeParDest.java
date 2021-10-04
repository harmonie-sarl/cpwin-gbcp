package fr.symphonie.budget.core.dao.ps;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.SqlReturnResultSet;

import static fr.symphonie.budget.core.dao.ps.ColumnName.mnt_col;
import fr.symphonie.budget.core.model.edition.util.DepByDest;
import fr.symphonie.util.model.SimpleEntity;

public class PsGbcpGetCpAeParDest extends GenericBudgetPs {
	private static PsGbcpGetCpAeParDest instance=null;
	public static final String SP_NAME="PsGbcpGetCpAeParDest";
	private static final String RESULT_LIST = "RESULT_LIST";
	
	private PsGbcpGetCpAeParDest(JdbcTemplate jdbcTemplate) {
		super(jdbcTemplate, SP_NAME);
		
		RowMapper<SimpleEntity> rowMapper=new RowMapper<SimpleEntity>(){
			
			@Override
			public SimpleEntity mapRow(ResultSet rs, int rowNum)
					throws SQLException {
				SimpleEntity item=new SimpleEntity();
				String libDest=rs.getString("lib_dest");
				String natGrp=rs.getString("nat_grp");
				item.setCode(libDest!=null?libDest.trim():"");
				item.setDesignation(natGrp!=null?natGrp.trim():"");					
				item.getAdditionalValues().put(mnt_col, rs.getDouble(mnt_col));
				return item;
			}
			;
		};
		declareParameter(new SqlReturnResultSet(RESULT_LIST, rowMapper));	
	   declareParameter(new SqlParameter(num_exec			, Types.INTEGER));	
	   declareParameter(new SqlParameter(niveau			, Types.INTEGER)); 		
	   declareParameter(new SqlParameter(modeCpAe			, Types.VARCHAR));
	   declareParameter(new SqlParameter(dateFin			, Types.VARCHAR));	
		compile();
		
	}
	public static PsGbcpGetCpAeParDest getInstance(JdbcTemplate jdbcTemplate) {
		if(instance==null)instance=new PsGbcpGetCpAeParDest(jdbcTemplate);
		return instance;
	}
	
	
	@SuppressWarnings("unchecked")
	private List<SimpleEntity> execute(int numexec, Integer niv,String mode, String dateFinStr){
		getLogger().debug("execute - start");
			Map<String, Object> inputs = new HashMap<String, Object> ();
			
		inputs.put(num_exec,numexec);
		inputs.put(niveau,niv);
		inputs.put(modeCpAe,mode);
		inputs.put(dateFin,dateFinStr);	
			
			Map<String, Object> result = super.execute(inputs);
			List<SimpleEntity> list=(List<SimpleEntity>) result.get(RESULT_LIST);
			getLogger().debug("execute: "+list.size()+"-  end");
	
			return list;	
		}
	public List<DepByDest> getData(int numexec, Integer niv, String dateFinStr){
		getLogger().debug("getData - start");
		List<DepByDest> list=new ArrayList<>();
		List<SimpleEntity> listCP=execute(numexec, niv, "CP",dateFinStr);
		List<SimpleEntity> listAE=execute(numexec, niv, "AE",dateFinStr);
		DepByDest dep=null;
		BigDecimal cp=null,ae=null;
		for(SimpleEntity se:listCP){
			cp=new BigDecimal((Double)se.getAdditionalValues().get("mnt"));
			dep=new DepByDest(new SimpleEntity(se.getCode(), ""));
			int index=list.indexOf(dep);
			if(index==-1)list.add(dep);
			else	dep=list.get(index);			
			dep.setMontantCP(se.getDesignation(), cp);
			
		}
		for(SimpleEntity se:listAE){
			ae=new BigDecimal((Double)se.getAdditionalValues().get("mnt"));
			dep=new DepByDest(new SimpleEntity(se.getCode(), ""));
			int index=list.indexOf(dep);
			if(index==-1)list.add(dep);
			else	dep=list.get(index);
			dep.setMontantAE(se.getDesignation(), ae);			
			
		}
		
			return list;	
		}
	
	

}
