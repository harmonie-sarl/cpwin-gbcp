package fr.symphonie.budget.core.model.edition.util;

import java.util.Arrays;
import java.util.List;

import fr.symphonie.budget.core.model.edition.Edition;
import fr.symphonie.util.Helper;

public class CAF   extends GenericTab{

	private DataItem data1;
	private DataItem data2;
	private DataItem data3;
	private DataItem data4;
	private DataItem data5;
	
	private DataItem resultPrevExerc;
	private DataItem resultCafIaf;
	
	
	
	public CAF(Edition e) {
		super(e);
		data1 = new DataItem(DataRefEnum.ref_02);
		data2 = new DataItem(DataRefEnum.ref_03);
		data3 = new DataItem(DataRefEnum.ref_04);
		data4 = new DataItem(DataRefEnum.ref_05);
		data5 = new DataItem(DataRefEnum.ref_06);
		this.resultPrevExerc = new DataItem(DataRefEnum.ref_01);
		this.resultCafIaf = new DataItem(DataRefEnum.ref_07);
	}

	
	
	public void insert(int index,Double vaule)
	{
        int index2=index+1;

		//syncronisation des datats
		if(index2==1)data1.setMontantDouble(vaule);
		if(index2==2)data2.setMontantDouble(vaule);
		if(index2==3)data3.setMontantDouble(vaule);
		if(index2==4)data4.setMontantDouble(vaule);
		if(index2==5)data5.setMontantDouble(vaule);
	
	}
	

	public DataItem getData1() {
		return data1;
	}



	public void setData1(DataItem data1) {
		this.data1 = data1;
	}



	public DataItem getData2() {
		return data2;
	}



	public void setData2(DataItem data2) {
		this.data2 = data2;
	}



	public DataItem getData3() {
		return data3;
	}



	public void setData3(DataItem data3) {
		this.data3 = data3;
	}



	public DataItem getData4() {
		return data4;
	}



	public void setData4(DataItem data4) {
		this.data4 = data4;
	}



	public DataItem getData5() {
		return data5;
	}



	public void setData5(DataItem data5) {
		this.data5 = data5;
	}

	public DataItem getResultPrevExerc() {
		return resultPrevExerc;
	}



	public void setResultPrevExerc(DataItem resultPrevExerc) {
		this.resultPrevExerc = resultPrevExerc;
	}



	public DataItem getResultCafIaf() {
		return resultCafIaf;
	}



	public void setResultCafIaf(DataItem resultCafIaf) {
		this.resultCafIaf = resultCafIaf;
	}


   @Override
   public List<DataItem> getItems() {
		DataItem[] tab = new DataItem[] { data1, data2, data3, data4, data5, resultPrevExerc, resultCafIaf };
		return Arrays.asList(tab);
	}



@Override
public void setDefaults() {
	getResultPrevExerc().setMontantDouble(getParent().getTab6ResultPrevExerc());
	getResultCafIaf().setMontantDouble(getParent().getTab6ResultCafIaf());
		
}

public void refreshTotaux() {
	double resultPrevExerc=0;
	double b=getParent().getTab6().getCrp().getResultPrevBenifce().getMontantDouble();
	double perte=getParent().getTab6().getCrp().getResultPrevPerte().getMontantDouble();
	if (b > 0) resultPrevExerc=b;
	else resultPrevExerc=-perte;
	
	getResultPrevExerc().setMontantDouble(resultPrevExerc);
	double capacite=resultPrevExerc + getData1().getMontantDouble() - getData2().getMontantDouble() + getData3().getMontantDouble() - getData4().getMontantDouble()
	- getData5().getMontantDouble();
	capacite=Helper.round(capacite, 2);
	getResultCafIaf().setMontantDouble(capacite);
	
	//Refresh TFP
	TFP tfp=getParent().getTab6().getTfp();
	tfp.getInsufAutofinanc().setMontantDouble(capacite<0?-capacite:0);
	tfp.getCapaciteAutofinanc().setMontantDouble(capacite>0?capacite:0);
	tfp.refreshTotaux();
		
}

	

}
