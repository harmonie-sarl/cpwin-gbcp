package fr.symphonie.budget.core.model.plt;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Transient;

import fr.symphonie.common.util.BudgetHelper;
import fr.symphonie.impor.AttributImport;
import fr.symphonie.impor.ConfigImport;
import fr.symphonie.impor.Importable;
import fr.symphonie.util.model.Trace;

public  class GenericLigneTresorerie implements Cloneable,Importable {
	//private static final Logger logger = LoggerFactory.getLogger(GenericLigneTresorerie.class);
	private LigneTresorerie ligne;
	private BigDecimal montant01;
	private BigDecimal montant02;
	private BigDecimal montant03;
	private BigDecimal montant04;
	private BigDecimal montant05;
	private BigDecimal montant06;
	private BigDecimal montant07;
	private BigDecimal montant08;
	private BigDecimal montant09;
	private BigDecimal montant10;
	private BigDecimal montant11;
	private BigDecimal montant12;
	
	
	private Trace trace;
	@Transient
	private List<GenericLigneTresorerie> detailList;
	@Transient
	private List<Ecriture> ecritureList;
	
	public GenericLigneTresorerie() {
		super();
		this.detailList=new ArrayList<GenericLigneTresorerie>();
	}
	public GenericLigneTresorerie(LigneTresorerie ligne) {
		this();
		setLigne(ligne);
		setNumero(ligne.getNumero());
	}

	public void setNumero(Integer numero) {
		
	}
	public LigneTresorerie getLigne() {
		return ligne;
	}

	public void setLigne(LigneTresorerie ligne) {
		this.ligne = ligne;
	}

	public BigDecimal getMontant01() {
		return montant01;
	}

	public void setMontant01(BigDecimal montant01) {
		this.montant01 = montant01;
	}

	public BigDecimal getMontant02() {
		return montant02;
	}

	public void setMontant02(BigDecimal montant02) {
		this.montant02 = montant02;
	}

	public BigDecimal getMontant03() {
		return montant03;
	}

	public void setMontant03(BigDecimal montant03) {
		this.montant03 = montant03;
	}

	public BigDecimal getMontant04() {
		return montant04;
	}

	public void setMontant04(BigDecimal montant04) {
		this.montant04 = montant04;
	}

	public BigDecimal getMontant05() {
		return montant05;
	}

	public void setMontant05(BigDecimal montant05) {
		this.montant05 = montant05;
	}

	public BigDecimal getMontant06() {
		return montant06;
	}

	public void setMontant06(BigDecimal montant06) {
		this.montant06 = montant06;
	}

	public BigDecimal getMontant07() {
		return montant07;
	}

	public void setMontant07(BigDecimal montant07) {
		this.montant07 = montant07;
	}

	public BigDecimal getMontant08() {
		return montant08;
	}

	public void setMontant08(BigDecimal montant08) {
		this.montant08 = montant08;
	}

	public BigDecimal getMontant09() {
		return montant09;
	}

	public void setMontant09(BigDecimal montant09) {
		this.montant09 = montant09;
	}

	public BigDecimal getMontant10() {
		return montant10;
	}

	public void setMontant10(BigDecimal montant10) {
		this.montant10 = montant10;
	}

	public BigDecimal getMontant11() {
		return montant11;
	}

	public void setMontant11(BigDecimal montant11) {
		this.montant11 = montant11;
	}

	public BigDecimal getMontant12() {
		return montant12;
	}

	public void setMontant12(BigDecimal montant12) {
		this.montant12 = montant12;
	}

	public Trace getTrace() {
		return trace;
	}

	public void setTrace(Trace trace) {
		this.trace = trace;
	}

	
	public List<Ecriture> getEcritureList() {
		return ecritureList;
	}
	public void setEcritureList(List<Ecriture> ecritureList) {
		this.ecritureList = ecritureList;
	}
	public List<GenericLigneTresorerie> getDetailList() {
		return detailList;
	}
	public void setDetailList(List<GenericLigneTresorerie> detailList) {
		this.detailList = detailList;
	}
	public void add(GenericLigneTresorerie ligne){
		if(ligne==null)return;
		for(int i=1;i<=12;i++){
			setMontant(i,getMontant(i).add(ligne.getMontant(i)));
		}
	
	}
	public void substraction(GenericLigneTresorerie ligne){
		if(ligne==null)return;
		for(int i=1;i<=12;i++){
			setMontant(i,getMontant(i).subtract(ligne.getMontant(i)));
		}
		
	}
	public BigDecimal getMontant(int index) {
		BigDecimal montant=null;
		switch (index) {
		case 1:
			montant=getMontant01();
			break;
		case 2:
			montant=getMontant02();
			break;
		case 3:
			montant=getMontant03();
			break;
		case 4:
			montant=getMontant04();
			break;
		case 5:
			montant=getMontant05();
			break;
		case 6:
			montant=getMontant06();
			break;
		case 7:
			montant=getMontant07();
			break;
		case 8:
			montant=getMontant08();
			break;
		case 9:
			montant=getMontant09();
			break;
		case 10:
			montant=getMontant10();
			break;
		case 11:
			montant=getMontant11();
			break;
		case 12:
			montant=getMontant12();
			break;

		default:
			break;
		}
		
		if(montant==null)montant=new BigDecimal(0);
		//logger.debug("getMontant :{} -> {}",index,montant);
		return montant;
	}
	public void setMontant(int index,BigDecimal montant) {
		switch (index) {
		case 1:
			setMontant01(montant);
			break;
		case 2:
			setMontant02(montant);
			break;
		case 3:
			setMontant03(montant);
			break;
		case 4:
			setMontant04(montant);
			break;
		case 5:
			setMontant05(montant);
			break;
		case 6:
			setMontant06(montant);
			break;
		case 7:
			setMontant07(montant);
			break;
		case 8:
			setMontant08(montant);
			break;
		case 9:
			setMontant09(montant);
			break;
		case 10:
			setMontant10(montant);
			break;
		case 11:
			setMontant11(montant);
			break;
		case 12:
			setMontant12(montant);
			break;

		default:
			break;
		}
	}
	public void reset(){
		montant01=new BigDecimal(0);
		montant02=new BigDecimal(0);
		montant03=new BigDecimal(0);
		montant04=new BigDecimal(0);
		montant05=new BigDecimal(0);
		montant06=new BigDecimal(0);
		montant07=new BigDecimal(0);
		montant08=new BigDecimal(0);
		montant09=new BigDecimal(0);
		montant10=new BigDecimal(0);
		montant11=new BigDecimal(0);
		montant12=new BigDecimal(0);
		//montant13=new BigDecimal(0);
	}
	public static String getMontantAttributeName(int periode) {
		String attributeName=null;
		PeriodeEnum p=PeriodeEnum.parse(periode);
		if(p==null) return null;
		switch (p) {
		case Aout:
			attributeName="montant08";
			break;
		case Avril:
			attributeName="montant04";
			break;
		case December:
			attributeName="montant12";
			break;
		case Fevrier:
			attributeName="montant02";
			break;
		case Janvier:
			attributeName="montant01";
			break;
		case Juillet:
			attributeName="montant07";
			break;
		case Juin:
			attributeName="montant06";
			break;
		case Mai:
			attributeName="montant05";
			break;
		case Mars:
			attributeName="montant03";
			break;
		case November:
			attributeName="montant11";
			break;
		case Octobre:
			attributeName="montant10";
			break;
		case September:
			attributeName="montant09";
			break;
			default:
		}	
		return attributeName;
	}
public BigDecimal getTotaleVariationAnnuelle(){
	BigDecimal total=new BigDecimal(getSomme(12));
/*	BigDecimal mnt=null;
	for(int i=1;i<=12;i++){
		mnt=getMontant(i);
		if(mnt==null)mnt=new BigDecimal(0);
		total=total.add(mnt);		
	}
	*/
	total=total.setScale(2, BigDecimal.ROUND_HALF_UP);
	
	return total;
				
	}
public void calculateGlobale() {
	if(!isGlobal())return;
	this.reset();
	for(GenericLigneTresorerie item:getDetailList()){
		this.add(item);
	}	
}
public boolean isGlobal()
{
	if(getLigne()==null)return false;
	return getLigne().isGlobal();
}
public boolean isBudgetaire() {
	if(getLigne()==null)return false;
	return getLigne().isBudgetaire();
}
public boolean isComptable() {
	if(getLigne()==null)return false;
	return getLigne().isComptable();
}
public BigDecimal getEcart() {
	
	return new BigDecimal(0);
}
public double getSomme(Integer periode) {
	double somme=0;
	for(int i=1;i<=periode;i++)somme+=getMontant(i).doubleValue();
	return somme;
}
@Override
protected GenericLigneTresorerie clone() throws CloneNotSupportedException {
	GenericLigneTresorerie lt=(GenericLigneTresorerie) super.clone();
	return lt;
	
}


public boolean montantNull(Integer index ){
	boolean result=false;
	BigDecimal montant=getMontant(index);
	if(montant==null)return true;
	result=montant.compareTo(BigDecimal.ZERO)==0;
	return result;
	
}

	public boolean isTotalVarAnnuelNull() {
//		boolean result=false;
		BigDecimal montant=getTotaleVariationAnnuelle();
		if(montant==null)return true;
		return(montant.compareTo(BigDecimal.ZERO)==0);
		//logger.debug("public boolean isEcartNull(): "+result);
//		return result;
	}
	@Override
	public String toString() {
		return "GenericLigneTresorerie [montant01=" + montant01 + ", montant02=" + montant02 + ", montant03="
				+ montant03 + ", montant04=" + montant04 + ", montant05=" + montant05 + ", montant06=" + montant06
				+ ", montant07=" + montant07 + ", montant08=" + montant08 + ", montant09=" + montant09 + ", montant10="
				+ montant10 + ", montant11=" + montant11 + ", montant12=" + montant12 + "]";
	}
	@Override
	public void setValue(AttributImport attributConfig, Object value) {
		//String strValue=null;
		BigDecimal bdValue=null;
//		if(value instanceof String)
//			strValue=(value!=null)?((String) value).trim():null;
//			else 
		if(value instanceof  BigDecimal)
		bdValue=(value!=null)?(BigDecimal) value:BigDecimal.ZERO;
		String attributName=attributConfig.getName();
		switch (attributName){
		case "NoLtr":
			LigneTresorerie ligne=new LigneTresorerie();
			ligne.setNumero(bdValue.intValue());
			setLigne(ligne);
			break;
		case "montant01":		
			setMontant01(bdValue);
			break;
		case "montant02":		
			setMontant02(bdValue);
			break;
		case "montant03":		
			setMontant03(bdValue);
			break;
		case "montant04":		
			setMontant04(bdValue);
			break;
		case "montant05":		
			setMontant05(bdValue);
			break;
		case "montant06":		
			setMontant06(bdValue);
			break;
		case "montant07":		
			setMontant07(bdValue);
			break;			
		case "montant08":		
			setMontant08(bdValue);
			break;
		case "montant09":		
			setMontant09(bdValue);
			break;
		case "montant10":		
			setMontant10(bdValue);
			break;
		case "montant11":		
			setMontant11(bdValue);
			break;
		case "montant12":		
			setMontant12(bdValue);
			break;
		}
		
	}
	@Override
	public ConfigImport getConfigImport() {
		if(configImport==null) configImport=BudgetHelper.loadConfigImport("gbcp.plt.import.config");
		return configImport;
	}
	private static ConfigImport configImport=null;

	@Override
	public void setRowNum(Integer rowNum) {
		
	}
	@Override
	public Integer getRowNum() {
		return null;
	}

}
