package fr.symphonie.budget.core.dao.ps.mapper;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import fr.symphonie.budget.core.model.pluri.BalanceItem;
import fr.symphonie.util.model.SimpleEntity;

public class BalanceRowMapper implements RowMapper<BalanceItem>{

	@Override
	public BalanceItem mapRow(ResultSet rs, int rowNum) throws SQLException {
		
		BalanceItem item=new BalanceItem();
		BigDecimal m1=null,m2=null,tot=null;
		
		item.setCompte(new SimpleEntity(rs.getString("num_nomenc"),rs.getString("nom_cpt")));
		
		m1=rs.getBigDecimal("be_deb");m2=rs.getBigDecimal("debit");tot=rs.getBigDecimal("tot_deb");
		if(m1==null)m1=new BigDecimal(0);
		if(m2==null)m2=new BigDecimal(0);
		if(tot==null)tot=new BigDecimal(0);
		item.setDebit(item.new OpBalance(m1.setScale(2, BigDecimal.ROUND_HALF_UP), m2.setScale(2, BigDecimal.ROUND_HALF_UP), tot.setScale(2, BigDecimal.ROUND_HALF_UP)));
		
		m1=rs.getBigDecimal("be_cre");m2=rs.getBigDecimal("credit");tot=rs.getBigDecimal("tot_cre");
		if(m1==null)m1=new BigDecimal(0);
		if(m2==null)m2=new BigDecimal(0);
		if(tot==null)tot=new BigDecimal(0);
		item.setCredit(item.new OpBalance(m1.setScale(2, BigDecimal.ROUND_HALF_UP), m2.setScale(2, BigDecimal.ROUND_HALF_UP), tot.setScale(2, BigDecimal.ROUND_HALF_UP)));
		
		m1=rs.getBigDecimal("solde_deb");m2=rs.getBigDecimal("solde_cre");
		if(m1==null)m1=new BigDecimal(0);
		if(m2==null)m2=new BigDecimal(0);
		item.setSolde(item.new Solde(m1.setScale(2, BigDecimal.ROUND_HALF_UP), m2.setScale(2, BigDecimal.ROUND_HALF_UP)));
		
		item.setNiveau(rs.getString("niveau"));
		return item;
	}

}
