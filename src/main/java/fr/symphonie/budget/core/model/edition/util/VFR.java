package fr.symphonie.budget.core.model.edition.util;

import java.util.Arrays;
import java.util.List;

import fr.symphonie.budget.core.model.edition.Edition;

public class VFR extends GenericTab {
	
	
	
	private DataItem variationFondRoul;
	private DataItem variationBesoinFondRoul;
	private DataItem variTresorie;
	private DataItem nivFondRoul;
	private DataItem nivBesoinFroul;
	private DataItem nivTresorie;
	
	
	
	public VFR(Edition e) {
		super(e);
		this.variationFondRoul = new DataItem(DataRefEnum.ref_01);
		this.variationBesoinFondRoul = new DataItem(DataRefEnum.ref_02);
		this.variTresorie =  new DataItem(DataRefEnum.ref_03);
		this.nivFondRoul = new DataItem(DataRefEnum.ref_04);
		this.nivBesoinFroul = new DataItem(DataRefEnum.ref_05);
		this.nivTresorie = new DataItem(DataRefEnum.ref_06);
	}



	@Override
	public void setDefaults() {
		getVariationFondRoul().setMontantDouble(getParent().getTab6VariationFondRoul());
		getVariationBesoinFondRoul().setMontantDouble(getParent().getTab6VariationBesoinFondRoul());
		getVariTresorie().setMontantDouble(getParent(). getTab6VariTresorie());
		getNivFondRoul().setMontantDouble(getParent().getTab6nivFondRoul());
		getNivBesoinFroul().setMontantDouble(getParent().getTab6nivBesoinFroul());
		getNivTresorie().setMontantDouble(getParent().getTab6NivTresorie());
		
	}
	

	
	public DataItem getVariationFondRoul() {
		return variationFondRoul;
	}



	public void setVariationFondRoul(DataItem variationFondRoul) {
		this.variationFondRoul = variationFondRoul;
	}




	public DataItem getVariationBesoinFondRoul() {
		return variationBesoinFondRoul;
	}


	public void setVariationBesoinFondRoul(DataItem variationBesoinFondRoul) {
		this.variationBesoinFondRoul = variationBesoinFondRoul;
	}



	public DataItem getVariTresorie() {
		return variTresorie;
	}




	public void setVariTresorie(DataItem variTresorie) {
		this.variTresorie = variTresorie;
	}




	public DataItem getNivFondRoul() {
		return nivFondRoul;
	}




	public void setNivFondRoul(DataItem nivFondRoul) {
		this.nivFondRoul = nivFondRoul;
	}




	public DataItem getNivBesoinFroul() {
		return nivBesoinFroul;
	}



	public void setNivBesoinFroul(DataItem nivBesoinFroul) {
		this.nivBesoinFroul = nivBesoinFroul;
	}



	public DataItem getNivTresorie() {
		return nivTresorie;
	}



	public void setNivTresorie(DataItem nivTresorie) {
		this.nivTresorie = nivTresorie;
	}







	@Override
	public List<DataItem> getItems() {
		DataItem[] tab = new DataItem[] { variationFondRoul, variationBesoinFondRoul, variTresorie, nivFondRoul,
				nivBesoinFroul, nivTresorie };
		return Arrays.asList(tab);
	}



	

}
