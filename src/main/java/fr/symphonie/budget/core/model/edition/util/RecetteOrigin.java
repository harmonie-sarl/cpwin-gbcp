package fr.symphonie.budget.core.model.edition.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RecetteOrigin {
	
	
	
	private MontantItem recette;
	private MontantItem fRecette;
	private MontantItem iRecette;
	private MontantItem vRecette;
	
	
	private static final Logger logger = LoggerFactory.getLogger(DepenceDest.class);
	
	public RecetteOrigin() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	
	
	
	
	public RecetteOrigin(MontantItem recette, MontantItem fRecette, MontantItem iRecette, MontantItem vRecette) {
		super();
		this.recette = recette;
		this.fRecette = fRecette;
		this.iRecette = iRecette;
		this.vRecette = vRecette;
		logger.debug("--construtios: "+this.toString());
	}





	public MontantItem getRecette() {
		return recette;
	}
	public void setRecette(MontantItem recette) {
		this.recette = recette;
	}
	public MontantItem getfRecette() {
		return fRecette;
	}
	public void setfRecette(MontantItem fRecette) {
		this.fRecette = fRecette;
	}
	public MontantItem getiRecette() {
		return iRecette;
	}
	public void setiRecette(MontantItem iRecette) {
		this.iRecette = iRecette;
	}
	public MontantItem getvRecette() {
		return vRecette;
	}
	public void setvRecette(MontantItem vRecette) {
		this.vRecette = vRecette;
	}

	public double  getTotalRecette(){
		return getRecette().getTotal();
	
	}
	public double  getTotalfRecette(){
		return getfRecette().getTotal();
	
	}
	public double  getTotaliRecette(){
		return getiRecette().getTotal();
	
	}
	public double  getTotalvRecette(){
		return getvRecette().getTotal();
	
	}




	@Override
	public String toString() {
		return "RecetteOrigin [recette=" + recette + ", fRecette=" + fRecette + ", iRecette=" + iRecette + ", vRecette="
				+ vRecette + "]";
	}
	
	public double getTotalMontant(int index){
		
		return getRecette().getMontant(index)+getfRecette().getMontant(index)+getiRecette().getMontant(index)+getvRecette().getMontant(index);
	}
  
		 

}
