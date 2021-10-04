package fr.symphonie.budget.core.dao.ps;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import fr.symphonie.budget.core.model.pluri.CpGestionnaire;

public class CpGestRowMapper implements RowMapper<CpGestionnaire> {

	@Override
	public CpGestionnaire mapRow(ResultSet rs, int rowNum) throws SQLException {
		CpGestionnaire p=new CpGestionnaire();
		String codeGest=rs.getString("code_gest");
		codeGest=(codeGest!=null)?codeGest.trim():codeGest;
		p.setGestionnaire(codeGest);
		p.setEjEnCours(rs.getDouble("ej_encours"));
		p.setEjPrecedent(rs.getDouble("ej_preced"));
		p.setDpAdmis(rs.getDouble("dp_adm"));
		return p;
	}

}
