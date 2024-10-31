package fr.symphonie.budget.core.model.edition.util;

import fr.symphonie.common.util.Constant;

public class Recette {
	
	private DataItem dataR11;
	private DataItem dataR12;
	private DataItem dataR13;
	private DataItem dataR14;
	private DataItem dataR15;
	private DataItem dataR22;
	private DataItem dataR24;
	private DataItem dataR25;
	private DataItem dataR26;
	private DataItem dataR27;
	
	
//	private static final Logger logger = LoggerFactory.getLogger(Recette.class);
	
	
	public Recette() {
		super();
		dataR11=new DataItem(DataRefEnum.ref_14);
		dataR12=new DataItem(DataRefEnum.ref_15);
		dataR13=new DataItem(DataRefEnum.ref_16);
		dataR14=new DataItem(DataRefEnum.ref_17);
		dataR15=new DataItem(DataRefEnum.ref_18);
		
		dataR22=new DataItem(DataRefEnum.ref_20);
		dataR24=new DataItem(DataRefEnum.ref_21);
		dataR25=new DataItem(DataRefEnum.ref_22);
		dataR26=new DataItem(DataRefEnum.ref_28);
		dataR27=new DataItem(DataRefEnum.ref_29);
		


	}
	
	public void setMontants(double r11, double r12, double r13, double r14, double r15, double r22, double r24, double r25) {
	
		getDataR11().setMontantDouble(r11);
		getDataR12().setMontantDouble(r12);
		getDataR13().setMontantDouble(r13);
		getDataR14().setMontantDouble(r14);
		getDataR15().setMontantDouble(r15);
		getDataR22().setMontantDouble(r22);
		getDataR24().setMontantDouble(r24);
		getDataR25().setMontantDouble(r25);
	}
	public void setMontantsFor2024(double r11, double r12, double r13, double r14, double r15, double r22, double r24, double r25, double r17, double r21) {
		
		getDataR11().setMontantDouble(r11);
		getDataR12().setMontantDouble(r12);
		getDataR13().setMontantDouble(r13);
		getDataR14().setMontantDouble(r14);
		getDataR15().setMontantDouble(r15);
		getDataR22().setMontantDouble(r22);
		getDataR24().setMontantDouble(r24);
		getDataR25().setMontantDouble(r25);
		getDataR26().setMontantDouble(r17);
		getDataR27().setMontantDouble(r21);
	}
	public void setMontants(double[] montants){
		setMontants(montants[Constant.R11], montants[Constant.R12], montants[Constant.R13], montants[Constant.R14], montants[Constant.R15], montants[Constant.R22], montants[Constant.R24], montants[Constant.R25]);
	}
	public void setMontantsFor2024(double[] montants){
		setMontantsFor2024(montants[Constant.R11], montants[Constant.R12], montants[Constant.R13], montants[Constant.R14], montants[Constant.R15], montants[Constant.R22], montants[Constant.R24], montants[Constant.R25],montants[Constant.R17],montants[Constant.R21]);
	}

	public double getR11() {
		return getDataR11().getMontantDouble();
	}

	public double getR12() {
		return getDataR12().getMontantDouble();
	}
	public double getR13() {
		return getDataR13().getMontantDouble();
	}

	public double getR14() {
		return getDataR14().getMontantDouble();
	}

	public double getR15() {
		return getDataR15().getMontantDouble();
	}

	public double getR22() {
		return getDataR22().getMontantDouble();
	}

	public double getR24() {
		return getDataR24().getMontantDouble();
	}

	public double getR25() {
		return getDataR25().getMontantDouble();
	}
	public double getR26() {
		return getDataR26().getMontantDouble();
	}
	public double getR27() {
		return getDataR27().getMontantDouble();
	}

	public DataItem getDataR11() {
		return dataR11;
	}





	public void setDataR11(DataItem dataR11) {
		this.dataR11 = dataR11;
	}





	public DataItem getDataR12() {
		return dataR12;
	}





	public void setDataR12(DataItem dataR12) {
		this.dataR12 = dataR12;
	}





	public DataItem getDataR13() {
		return dataR13;
	}





	public void setDataR13(DataItem dataR13) {
		this.dataR13 = dataR13;
	}





	public DataItem getDataR14() {
		return dataR14;
	}





	public void setDataR14(DataItem dataR14) {
		this.dataR14 = dataR14;
	}





	public DataItem getDataR15() {
		return dataR15;
	}





	public void setDataR15(DataItem dataR15) {
		this.dataR15 = dataR15;
	}





	public DataItem getDataR22() {
		return dataR22;
	}





	public void setDataR22(DataItem dataR22) {
		this.dataR22 = dataR22;
	}





	public DataItem getDataR24() {
		return dataR24;
	}





	public void setDataR24(DataItem dataR24) {
		this.dataR24 = dataR24;
	}





	public DataItem getDataR25() {
		return dataR25;
	}


	public void setDataR25(DataItem dataR25) {
		this.dataR25 = dataR25;
	}
	public DataItem getDataR26() {
		return dataR26;
	}


	public void setDataR26(DataItem dataR26) {
		this.dataR26 = dataR26;
	}
	
	public DataItem getDataR27() {
		return dataR27;
	}


	public void setDataR27(DataItem dataR27) {
		this.dataR27 = dataR27;
	}





	@Override
	public String toString() {
		return "Recette [r11=" + getR11() + ", r12=" + getR12() + ", r13=" + getR13() + ", r14=" + getR14() + ", r15=" + getR15() + ", r22="
				+ getR22() + ", r24=" + getR24() + ", r25=" + getR25() + ", r26=" + getR26() + ", r27=" + getR27() + "]";
	}
	
	
	
	
	


}
