package fr.symphonie.budget.core.model.plt;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Transient;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.symphonie.budget.core.model.edition.util.NatGrpEnum;
import fr.symphonie.budget.core.model.edition.util.NatGrpEnum2024;
import fr.symphonie.common.util.BudgetHelper;
import fr.symphonie.common.util.Constant;
import fr.symphonie.util.Helper;
import fr.symphonie.util.model.SimpleEntity;

public class PlanTresorerie<T extends GenericLigneTresorerie > {
	private static final Logger logger = LoggerFactory.getLogger(PlanTresorerie.class);
	
	private Integer exercice;
	private Integer periode;
	 private String nomConfig;
	private List<T> encaissements=new ArrayList<>();
	private List<T> decaissements=new ArrayList<>();
	private T totaleEncaiss;
	private T totaleDecaiss;
	private T soldeMois;
	private T soldeCumule;
	private T soldeInitial;
	
	private List<T> globalEncaiss;
	private List<T> globalDecaiss;
	@Transient
	Map<Integer,T> mapOfLigne;
	Map<Integer,BigDecimal> bpMap;
	
	@Transient
	private List<SimpleEntity>noLignesEncass=new ArrayList<>();
	@Transient
	private List<SimpleEntity>noLignesDecaiss=new ArrayList<>();
	
	@Transient
	private T varSoldeMois;
	
	public PlanTresorerie() {
		super();
		this.globalEncaiss=new ArrayList<>();
		this.globalDecaiss=new ArrayList<>();
	}
	
	public PlanTresorerie(int exercice, int periode) {
		this();
		this.exercice = exercice;
		this.periode = periode;
	}

	public Integer getExercice() {
		return exercice;
	}

	public void setExercice(Integer exercice) {
		this.exercice = exercice;
	}

	public Integer getPeriode() {
		return periode;
	}

	public String getNomConfig() {
		return nomConfig;
	}

	public void setNomConfig(String nomConfig) {
		this.nomConfig = nomConfig;
	}

	public void setPeriode(Integer periode) {
		this.periode = periode;
	}

	public List<T> getEncaissements() {
		return encaissements;
	}
	public void setEncaissements(List<T> encaissements) {
		this.encaissements = encaissements;
	}
	public List<T> getDecaissements() {
		return decaissements;
	}
	public void setDecaissements(List<T> decaissements) {
		this.decaissements = decaissements;
	}
	
	
	public T getTotaleEncaiss() {
		return totaleEncaiss;
	}

	public void setTotaleEncaiss(T totaleEncaiss) {
		this.totaleEncaiss = totaleEncaiss;
	}

	public T getTotaleDecaiss() {
		return totaleDecaiss;
	}

	public void setTotaleDecaiss(T totaleDecaiss) {
		this.totaleDecaiss = totaleDecaiss;
	}

	public T getSoldeMois() {
		return soldeMois;
	}

	public void setSoldeMois(T soldeMois) {
		this.soldeMois = soldeMois;
	}

	public T getSoldeCumule() {
		return soldeCumule;
	}

	public void setSoldeCumule(T soldeCumule) {
		this.soldeCumule = soldeCumule;
	}

	public T getSoldeInitial() {
		return soldeInitial;
	}

	public void setSoldeInitial(T soldeInitial) {
		this.soldeInitial = soldeInitial;
	}

	public List<T> getGlobalEncaiss() {
		return globalEncaiss;
	}

	public void setGlobalEncaiss(List<T> globalEncaiss) {
		this.globalEncaiss = globalEncaiss;
	}

	public List<T> getGlobalDecaiss() {
		return globalDecaiss;
	}

	public void setGlobalDecaiss(List<T> globalDecaiss) {
		this.globalDecaiss = globalDecaiss;
	}

	public static <E extends GenericLigneTresorerie> List<E> filterByGlobale(List<E> detailList, Integer parent) {
		List<E> result=new ArrayList<>();
		for(E d:detailList){
			if(d.getLigne().isGlobal()) continue;
			if(d.getLigne().getNumeroParent()==parent)result.add(d);
		}
		return result;
	}

	public void createGlobale(T globalLigne){
		if(!globalLigne.getLigne().isGlobal())return;
		List<T> detailList=getDetailList(globalLigne.getLigne().getTypeOp());
		globalLigne.getDetailList().addAll(filterByGlobale(detailList,globalLigne.getLigne().getNumero()));

		detailList.add(globalLigne);
		getGlobaleList(globalLigne.getLigne().getTypeOp()).add(globalLigne);
	}
	public List<T> getDetailList(EncDecEnum typeOp) {

		switch (typeOp) {
		case Encaissement:
			return getEncaissements();
		case Decaissement:			
			return getDecaissements();

		}
		return null;
	}
	private List<T> getGlobaleList(EncDecEnum typeOp) {

		switch (typeOp) {
		case Encaissement:
			return getGlobalEncaiss();
		case Decaissement:			
			return getGlobalDecaiss();

		}
		return null;
	}
	public void sort(){
		Collections.sort(getEncaissements(),new Comparator<GenericLigneTresorerie>() {

			@Override
			public int compare(GenericLigneTresorerie o1, GenericLigneTresorerie o2) {
				return o1.getLigne().getOrdre().compareTo(o2.getLigne().getOrdre());
			}
		});
		
		Collections.sort(getDecaissements(),new Comparator<GenericLigneTresorerie>() {

			@Override
			public int compare(GenericLigneTresorerie o1, GenericLigneTresorerie o2) {
				return o1.getLigne().getOrdre().compareTo(o2.getLigne().getOrdre());
			}
		});
	}
	public void calculateTotaux(){
		calculateTotauxEncDec();
		calculateSoldeMois();
		calculateSoldeCumule();
		
	}
	public void calculateTotauxEncDec(){
		calculateGlobalTotaux(EncDecEnum.Encaissement);
		calculateGlobalTotaux(EncDecEnum.Decaissement);
		calculateTotaleEncaiss();
		calculateTotaleDecaiss();
		
	}
	private void calculateSoldeCumule() {
		getSoldeCumule().reset();
		getSoldeCumule().add(getSoldeMois());
	
		BigDecimal soldeCumule=new BigDecimal(0);
		for(int i=1;i<13;i++){
			soldeCumule=(getSoldeCumule().getMontant(i)).add(getSoldeInitial().getMontant(i));
			getSoldeCumule().setMontant(i,soldeCumule);
			getSoldeInitial().setMontant(i+1,soldeCumule);
			
		}
		
		
	}
	public void calculateSoldeMois() {
		getSoldeMois().reset();
		getSoldeMois().add(getTotaleEncaiss());
		getSoldeMois().substraction(getTotaleDecaiss());
		
	}
	
	public void calculateVarSoldeMois() {
		logger.debug("calculateVarSoldeMois: start");
		boolean refresh=true;
		//if((refreshArgs!=null) &&(refreshArgs.length>0))refresh=refreshArgs[0];
		if(refresh) calculateTotauxEncDec();
		getVarSoldeMois().reset();
		getVarSoldeMois().add(getTotaleEncaiss());
		getVarSoldeMois().substraction(getTotaleDecaiss());
		getVarSoldeMois().substraction(getSoldeMois());
		
		logger.debug("calculateVarSoldeMois: getVarSoldeMois()={}",getVarSoldeMois());
	}
	private void calculateTotaleEncaiss() {
		getTotaleEncaiss().reset();
		for(GenericLigneTresorerie global:getGlobalEncaiss()){
			getTotaleEncaiss().add(global);
		}		
	}
	private void calculateTotaleDecaiss() {
		getTotaleDecaiss().reset();
		for(GenericLigneTresorerie global:getGlobalDecaiss()){
			getTotaleDecaiss().add(global);
		}		
	}
	
	private void calculateGlobalTotaux(EncDecEnum typeOp) {
		for(GenericLigneTresorerie global:getGlobaleList(typeOp)){
			global.calculateGlobale();
		}		
	}
	
	
	public List<SimpleEntity> getNoLignesEncass() {
		if (noLignesEncass.isEmpty())
			this.noLignesEncass = getNoLignes(EncDecEnum.Encaissement);
		return noLignesEncass;
	}

	public List<SimpleEntity> getNoLignesDecaiss() {
		if (noLignesDecaiss.isEmpty())
			this.noLignesDecaiss = getNoLignes(EncDecEnum.Decaissement);
		return noLignesDecaiss;
	}

	
	public List<SimpleEntity> getNoLignes(EncDecEnum typeOp) {
		SimpleEntity entity;
		List<SimpleEntity>noLignes=new ArrayList<>();
		List<? extends GenericLigneTresorerie> listDepart=getDetailList(typeOp);	
		if(listDepart!=null)
		for (GenericLigneTresorerie ligne : listDepart) {
			//entity= new SimpleEntity(ligne.getLigne().getNumero()+"", ligne.getLigne().getLibelle());
			entity= new SimpleEntity(ligne.getLigne().getNumero()+"", ligne.getLigne().getDesignation());
			if(noLignes.contains(entity))continue;
			entity.setRequired(ligne.isGlobal());
			noLignes.add(entity);
		}
		return noLignes;	
	}
public BigDecimal getTotaleEcart(){
		BigDecimal totale=new BigDecimal(0);
		for(EncDecEnum typeOp:EncDecEnum.values()){			
			for(GenericLigneTresorerie d:getDetailList(typeOp)){
				if(d.isGlobal()) continue;
				if(d.isComptable())continue;
				
				totale=totale.add(d.getEcart());
				//logger.debug("getTotaleEcart---: "+totale);
			}
		}
		logger.debug("totale: "+totale);
		return totale;
	}
	public String getTitle(){
		String title=null;
		Integer numero=getPeriode();
		if(numero==null)return "";
			if(numero==1){
				title="Prévision initiale";
			}
			else if(numero>PeriodeEnum.December.getValue())
				title=PeriodeEnum.parse(numero).getLibelle();
			else{
				title= "Réalisé "+PeriodeEnum.parse(numero-1).getLibelle()+" – Prévision "+PeriodeEnum.parse(numero).getLibelle();
			}
			return title;
			
		}

	public DetailLigneTresorerie getDetailLigne(Integer numeroLigne) {
	
		return (DetailLigneTresorerie) mapOfLigne.get(numeroLigne);
	}
	private VentillationCO getVentillationCO(Integer numeroLigne) {
		
		return (VentillationCO) mapOfLigne.get(numeroLigne);
	}
	
	public void refreshBudgetInitial()
	{
		logger.debug("refreshBudgetInitial: debut");
		VentillationCO ligne21=getVentillationCO(21),ligne22=getVentillationCO(22),ligne23=getVentillationCO(23),ligne24=getVentillationCO(24);
		VentillationCO ligne25=getVentillationCO(25),ligne26=getVentillationCO(26),ligne27=getVentillationCO(27),ligne28=getVentillationCO(28);
		BigDecimal bpPersonnel=bpMap.get(21),bpFonct=bpMap.get(22),bpInterv=bpMap.get(23),bpInvess=bpMap.get(24);
		ligne21.setBudgetInitiale(bpPersonnel.subtract(ligne25.getBudgetInitiale()));
		ligne22.setBudgetInitiale(bpFonct.subtract(ligne26.getBudgetInitiale()));
		ligne23.setBudgetInitiale(bpInterv.subtract(ligne27.getBudgetInitiale()));
		ligne24.setBudgetInitiale(bpInvess.subtract(ligne28.getBudgetInitiale()));
		
		logger.debug("refreshBudgetInitial: ligne21={} ,ligne22={} ,ligne23={} ,ligne24={} , ",ligne21,ligne22,ligne23,ligne24);

	}
	public void refreshBudgetInitial2024()
	{
		logger.debug("refreshBudgetInitial: debut");
		VentillationCO ligne23=getVentillationCO(23),ligne24=getVentillationCO(24),ligne25=getVentillationCO(25),ligne26=getVentillationCO(26);
		VentillationCO ligne27=getVentillationCO(27),ligne28=getVentillationCO(28),ligne29=getVentillationCO(29),ligne30=getVentillationCO(30);
		BigDecimal bpPersonnel=bpMap.get(23),bpFonct=bpMap.get(24),bpInterv=bpMap.get(25),bpInvess=bpMap.get(26);
		ligne23.setBudgetInitiale(bpPersonnel.subtract(ligne27.getBudgetInitiale()));
		ligne24.setBudgetInitiale(bpFonct.subtract(ligne28.getBudgetInitiale()));
		ligne25.setBudgetInitiale(bpInterv.subtract(ligne29.getBudgetInitiale()));
		ligne26.setBudgetInitiale(bpInvess.subtract(ligne30.getBudgetInitiale()));
		
		logger.debug("refreshBudgetInitial2024: ligne23={} ,ligne24={} ,ligne25={} ,ligne26={} , ",ligne23,ligne24,ligne25,ligne26);

	}

	public void prepareVentil2024(Map<String, Double> bpByNatGrp) {
		BigDecimal bp=null;
		Double montant=null;
		NatGrpEnum2024 natGrp=null;
	//	mapOfLigne=new HashMap<>();
		bpMap=new HashMap<>();
		boolean initialMode=getTotaleBp()==0;
		logger.debug("prepareVentil: isInitialMode={}",initialMode);
		
		prepare();
		for(EncDecEnum typeOp:EncDecEnum.values()){
			
			for(T d:getDetailList(typeOp)){
				natGrp=null;
				if(d.isGlobal())continue;
				
				switch(d.getLigne().getNumero()){
				case 3:
					natGrp=NatGrpEnum2024.NAT_GRP_R11;
					break;
				case 4:
					natGrp=NatGrpEnum2024.NAT_GRP_R17;
					break;
				case 5:
					natGrp=NatGrpEnum2024.NAT_GRP_R12;
					break;
				case 6:
					natGrp=NatGrpEnum2024.NAT_GRP_R13;
					
					break;
				case 7:
					natGrp=NatGrpEnum2024.NAT_GRP_R14;
					break;
				case 8:
					natGrp=NatGrpEnum2024.NAT_GRP_R15;
					break;
				case 9:
					natGrp=NatGrpEnum2024.NAT_GRP_R21;
					break;
				case 10:
					natGrp=NatGrpEnum2024.NAT_GRP_R22;
					break;
				case 11:
					natGrp=NatGrpEnum2024.NAT_GRP_R24;
					break;
				case 12:
					natGrp=NatGrpEnum2024.NAT_GRP_R25;
					break;
//				case 21:
//					natGrp=NatGrpEnum2024.NAT_GRP_1;
//					//mapOfLigne.put(d.getLigne().getNumero(), d);
//					break;	
//				case 22:
//					natGrp=NatGrpEnum2024.NAT_GRP_2;
//					//mapOfLigne.put(d.getLigne().getNumero(), d);
//					break;
				case 23:
					natGrp=NatGrpEnum2024.NAT_GRP_1;
					//mapOfLigne.put(d.getLigne().getNumero(), d);
					break;		
				case 24:
					natGrp=NatGrpEnum2024.NAT_GRP_2;
					//mapOfLigne.put(d.getLigne().getNumero(), d);
					break;	
				case 25:
					natGrp=NatGrpEnum2024.NAT_GRP_4;
					//mapOfLigne.put(d.getLigne().getNumero(), d);
					break;	
				case 26:
					natGrp=NatGrpEnum2024.NAT_GRP_3;
					//mapOfLigne.put(d.getLigne().getNumero(), d);
					break;	
				 case 27:case 28:
					//mapOfLigne.put(d.getLigne().getNumero(), d);
					break;
				default:
					natGrp=null;
						break;
				}
				if(natGrp!=null){
					montant=bpByNatGrp.get(natGrp.getCode());
					bp=montant!=null?new BigDecimal(montant):new BigDecimal(0);
					bpMap.put(d.getLigne().getNumero(), bp);
					logger.debug("bpMap: bpMap={}",bpMap);
				}
				else{
					bp=new BigDecimal(0);
				}
				if(initialMode)
				((VentillationCO) d).setBudgetInitiale(bp);
				
			}
		}
		
	}
	public void prepareVentil(Map<String, Double> bpByNatGrp) {
		BigDecimal bp=null;
		Double montant=null;
		NatGrpEnum natGrp=null;
	//	mapOfLigne=new HashMap<>();
		bpMap=new HashMap<>();
		boolean initialMode=getTotaleBp()==0;
		logger.debug("prepareVentil: isInitialMode={}",initialMode);
		
		prepare();
		for(EncDecEnum typeOp:EncDecEnum.values()){
			
			for(T d:getDetailList(typeOp)){
				natGrp=null;
				if(d.isGlobal())continue;
				
				switch(d.getLigne().getNumero()){
				case 3:
					natGrp=NatGrpEnum.NAT_GRP_R11;
					break;
				case 4:
					natGrp=NatGrpEnum.NAT_GRP_R12;
					break;
				case 5:
					natGrp=NatGrpEnum.NAT_GRP_R13;
					break;
				case 6:
					natGrp=NatGrpEnum.NAT_GRP_R14;
					break;
				case 7:
					natGrp=NatGrpEnum.NAT_GRP_R15;
					break;
				case 8:
					natGrp=NatGrpEnum.NAT_GRP_R22;
					break;
				case 9:
					natGrp=NatGrpEnum.NAT_GRP_R24;
					break;
				case 10:
					natGrp=NatGrpEnum.NAT_GRP_R25;
					break;
				case 21:
					natGrp=NatGrpEnum.NAT_GRP_1;
					//mapOfLigne.put(d.getLigne().getNumero(), d);
					break;	
				case 22:
					natGrp=NatGrpEnum.NAT_GRP_2;
					//mapOfLigne.put(d.getLigne().getNumero(), d);
					break;
				case 23:
					natGrp=NatGrpEnum.NAT_GRP_4;
					//mapOfLigne.put(d.getLigne().getNumero(), d);
					break;		
				case 24:
					natGrp=NatGrpEnum.NAT_GRP_3;
					//mapOfLigne.put(d.getLigne().getNumero(), d);
					break;		
				case 25:case 26:case 27:case 28:
					//mapOfLigne.put(d.getLigne().getNumero(), d);
					break;
				default:
					natGrp=null;
						break;
				}
				if(natGrp!=null){
					montant=bpByNatGrp.get(natGrp.getCode());
					bp=montant!=null?new BigDecimal(montant):new BigDecimal(0);
					bpMap.put(d.getLigne().getNumero(), bp);
				}
				else{
					bp=new BigDecimal(0);
				}
				if(initialMode)
				((VentillationCO) d).setBudgetInitiale(bp);
				
			}
		}
		
	}

	public void prepare() {
		mapOfLigne=new HashMap<>();
		for (EncDecEnum typeOp : EncDecEnum.values()) {

			for (T d : getDetailList(typeOp)) {
				if (d.isGlobal())
					continue;
				mapOfLigne.put(d.getLigne().getNumero(), d);
			}
		}
	}
	public double getTotaleBp() {
		double totale = 0;
		for (EncDecEnum typeOp : EncDecEnum.values()) {
			for (T d : getDetailList(typeOp)) {
				if (d.isGlobal())
					continue;
				totale += ((VentillationCO) d).getBudgetInitiale().doubleValue();
			}
		}
		return totale;
	}

	 public boolean isActiveValidateAdjuste() {
			return getTotaleEcart().compareTo(BigDecimal.ZERO) == 0 ;
		}

	public double[] getMontantsCpForDepence(Integer periodeRealise) {
		double[] montants=new double[4];
		montants[Constant.PERSONNEL]=getSomme(periodeRealise,21, 25);
		montants[Constant.FONCTIONNEMENT]=getSomme(periodeRealise,22,26);
		montants[Constant.INTERVENTION]=getSomme(periodeRealise,23, 27);
		montants[Constant.INVESTISSEMENT]=getSomme(periodeRealise,24, 28);
		return montants;
	}
	public double[] getMontantsCpForDepence2024(Integer periodeRealise) {
		double[] montants=new double[4];
		montants[Constant.PERSONNEL]=getSomme(periodeRealise,23, 27);
		montants[Constant.FONCTIONNEMENT]=getSomme(periodeRealise,24,28);
		montants[Constant.INTERVENTION]=getSomme(periodeRealise,25, 29);
		montants[Constant.INVESTISSEMENT]=getSomme(periodeRealise,26, 30);
		return montants;
	}
	public double getSomme(Integer periodeRealise,Integer numL1,Integer numL2){
		double somme=0;
		DetailLigneTresorerie l1=getDetailLigne(numL1);
		DetailLigneTresorerie l2=getDetailLigne(numL2);
		if(l1!=null)
			somme+=(l1.getSomme(periodeRealise));
		if(l2!=null)
			somme+=(l2.getSomme(periodeRealise))		;
				
		return Helper.round(somme, 2);
	}

	public double[] getMontantsRecette(Integer periodeRealise) {
		double[] montants=new double[8];
		montants[Constant.R11]=getSomme(periodeRealise,3, null);
		montants[Constant.R12]=getSomme(periodeRealise,4,null);
		montants[Constant.R13]=getSomme(periodeRealise,5, null	);
		montants[Constant.R14]=getSomme(periodeRealise,6, null);
		montants[Constant.R15]=getSomme(periodeRealise,7, null);
		
		montants[Constant.R22]=getSomme(periodeRealise,8, null);
		montants[Constant.R24]=getSomme(periodeRealise,9,null);
		montants[Constant.R25]=getSomme(periodeRealise,10, null	);
		
		return montants;
	}
	public double[] getMontantsRecetteFor2024(Integer periodeRealise) {
		double[] montants=new double[10];
		montants[Constant.R11]=getSomme(periodeRealise,3, null);
		montants[Constant.R12]=getSomme(periodeRealise,4,null);
		montants[Constant.R13]=getSomme(periodeRealise,5, null	);
		montants[Constant.R14]=getSomme(periodeRealise,6, null);
		montants[Constant.R15]=getSomme(periodeRealise,7, null);
		
		montants[Constant.R22]=getSomme(periodeRealise,8, null);
		montants[Constant.R24]=getSomme(periodeRealise,9,null);
		montants[Constant.R25]=getSomme(periodeRealise,10, null	);
		montants[Constant.R17]=getSomme(periodeRealise,11, null	);
		montants[Constant.R21]=getSomme(periodeRealise,12, null	);
		
		return montants;
	}
	public static PlanTresorerie<DetailLigneTresorerie> convert(PlanTresorerie<VentillationCO> planVentil){
		DetailLigneTresorerie d=null;
		Integer periode=planVentil.getPeriode();
		PlanTresorerie<DetailLigneTresorerie> result=BudgetHelper.getInstanceOfPlanTresorerie(DetailLigneTresorerie.class);
		
		result.setExercice(planVentil.getExercice());
		logger.debug("convert: exercieplanVentil()={}",result.getExercice());
		result.setPeriode(periode);
		result.setNomConfig(planVentil.getNomConfig());
		for(VentillationCO item: planVentil.getDecaissements()){
			d=item.convert();d.setPeriode(periode);
			result.getDecaissements().add(d);
		}
		for(VentillationCO item: planVentil.getEncaissements()){
			d=item.convert();d.setPeriode(periode);
			result.getEncaissements().add(d);
			logger.debug("convert: encaissements={}",result.getEncaissements().size());
		}
		for(VentillationCO item: planVentil.getGlobalDecaiss()){
			d=item.convert();d.setPeriode(periode);
			result.getGlobalDecaiss().add(d);
		}
		for(VentillationCO item: planVentil.getGlobalEncaiss()){
			d=item.convert();d.setPeriode(periode);
			result.getGlobalEncaiss().add(d);
			logger.debug("convert: globalEncaissements={}",result.getGlobalEncaiss().size());
		}
		return result;
	}

	public T getVarSoldeMois() {
		return varSoldeMois;
	}

	public void setVarSoldeMois(T varSoldeMois) {
		this.varSoldeMois = varSoldeMois;
	}
		
	public BigDecimal getTotaleVarSoldeMois(){		
		return getVarSoldeMois().getTotaleVariationAnnuelle();
	}
	
}
