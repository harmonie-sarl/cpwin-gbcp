package fr.symphonie.budget.core.dao.ps.mapper;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import fr.symphonie.tools.meta4dai.DisplayStruct;

public class Meta4DaiStructRowMapper  implements RowMapper<DisplayStruct> {

	@Override
	public DisplayStruct mapRow(ResultSet rs, int rowNum) throws SQLException {
		BigDecimal mt_tot=null,mt_paie=null,mt_autre=null, dispo_ei=null;
		DisplayStruct item=new DisplayStruct();
		
		item.setCodeDest(rs.getString("code_dest"));
		item.setCodeNature(rs.getString("code_nature"));
		item.setCodeDirection(rs.getString("code_direction"));
		item.setCodeService(rs.getString("code_service"));
		item.setCodeProg(rs.getString("code_prog"));
		item.setLibProg(rs.getString("lib_prog"));
		item.setNoEi(rs.getInt("no_ei"));
		item.setLibEi(rs.getString("lib_ei"));
		mt_tot=rs.getBigDecimal("mt_tot");
		mt_paie=rs.getBigDecimal("mt_paie");
		mt_autre=rs.getBigDecimal("mt_autre");
		dispo_ei=rs.getBigDecimal("dispo_ei");
		
		if(mt_tot==null) mt_tot=new BigDecimal(0);
		if(mt_paie==null) mt_paie=new BigDecimal(0);
		if(mt_autre==null) mt_autre=new BigDecimal(0);
		if(dispo_ei==null) dispo_ei=new BigDecimal(0);
		item.setMtTot(mt_tot.setScale(2, BigDecimal.ROUND_HALF_UP));
		item.setMtPaie(mt_paie.setScale(2, BigDecimal.ROUND_HALF_UP));
		item.setMtAutre(mt_autre.setScale(2, BigDecimal.ROUND_HALF_UP));
		item.setDispoEi(dispo_ei.setScale(2, BigDecimal.ROUND_HALF_UP));
		
		return item;
	}

}
