package fr.symphonie.budget.core.model.edition.util;

import java.math.BigDecimal;
import java.util.Arrays;

public class MontantItem {
	
	
	private BigDecimal[] montants;

//	private static final Logger logger = LoggerFactory.getLogger(MontantItem.class);
	
	
	public MontantItem() {
		super();
		montants= new BigDecimal[5];		
	}

	
	
	public void insert(int index,BigDecimal vaule)
	{
//		nb_audio = nb_audio+1;
//		logger.debug("public void insert(int "+index+",BigDecimal "+vaule+")");
		if(montants[index]!=null) return ;
		montants[index] = vaule;
	}
	




	@Override
	public String toString() {
		return "MontantItem [montants=" + Arrays.toString(montants) + "]";
	}



	public BigDecimal[] getMontants() {
		return montants;
	}



	public void setMontants(BigDecimal[] montants) {
		this.montants = montants;
	}



public double getMontant(int index){
	BigDecimal montant=montants[index];
	if(montant!=null) return  montant.doubleValue();
	return 0d;
}

public double getTotal(){
//	BigDecimal montant=montants[index];
	double result=0;
	if(getMontants()==null) return 0;
	for (BigDecimal mnt : getMontants()) {
		if(mnt==null)continue;
		result+=mnt.doubleValue();
	}
	return result;
}



//	public BigDecimal[] getCp() {
//		return montants;
//	}
//
//	public void setCp(BigDecimal[] cp) {
//		this.montants = cp;
//	}
	
	

}
