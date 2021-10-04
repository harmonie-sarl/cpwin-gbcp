package fr.symphonie.budget.ui.viewmodel;

import java.math.BigDecimal;

import fr.symphonie.budget.core.model.edition.util.NatGrpEnum;
import fr.symphonie.util.model.SimpleEntity;

public class VentilationImput {
//	private static final Logger logger = LoggerFactory.getLogger(VentilationImput.class);
	private SimpleEntity account;
	private String nature;
	private BigDecimal amount;
	
	public VentilationImput(String accountCode, String accountLabel, String nature, double amount) {
		super();
		this.account = new SimpleEntity(accountCode, accountLabel);
		this.nature = nature;
		this.amount = new BigDecimal(amount);
	}
	public SimpleEntity getAccount() {
		return account;
	}
	public void setAccount(SimpleEntity account) {
		this.account = account;
	}
	public String getNature() {
		return nature;
	}
	public void setNature(String nature) {
		this.nature = nature;
	}
	public BigDecimal getAmount() {
		return amount;
	}
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
	public BigDecimal getAmount1() {
		return getAmountByNature(NatGrpEnum.NAT_GRP_1.getCode());
	}
	public BigDecimal getAmount2() {
		return getAmountByNature(NatGrpEnum.NAT_GRP_2.getCode());
	}
	public BigDecimal getAmount3() {
		return getAmountByNature(NatGrpEnum.NAT_GRP_3.getCode());
	}
	public BigDecimal getAmount4() {
		return getAmountByNature(NatGrpEnum.NAT_GRP_4.getCode());
	}
	private BigDecimal getAmountByNature(String natureCode) {
		//logger.debug("getAmountByNature: nature={}, natureCode={}",getNature(),natureCode);
		if((getNature()== null)||(natureCode==null)) return null;
		if(!getNature().equals(natureCode))return null;
		//logger.debug("getAmountByNature: natureCode={} --> amount={}",natureCode,getAmount());
		return getAmount();
	}
	

}
