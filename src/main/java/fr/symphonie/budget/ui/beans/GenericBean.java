package fr.symphonie.budget.ui.beans;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.symphonie.budget.core.model.DepRecEnum;
import fr.symphonie.budget.core.model.demat.Export;
import fr.symphonie.budget.core.model.demat.ExportInfo;
import fr.symphonie.budget.core.model.demat.GenericDemat;
import fr.symphonie.budget.core.model.edition.Edition;
import fr.symphonie.budget.core.model.edition.TabsEnum;
import fr.symphonie.budget.core.model.edition.util.ABE;
import fr.symphonie.budget.core.model.edition.util.DataItem;
import fr.symphonie.budget.core.model.edition.util.EFE;
import fr.symphonie.budget.core.model.edition.util.RecByOrig;
import fr.symphonie.budget.core.model.plt.DetailLigneTresorerie;
import fr.symphonie.budget.core.model.plt.EtatPeriodeEnum;
import fr.symphonie.budget.core.model.plt.Periode;
import fr.symphonie.budget.core.model.plt.PeriodeEnum;
import fr.symphonie.budget.core.model.plt.PlanTresorerie;
import fr.symphonie.budget.core.service.DematService;
import fr.symphonie.budget.ui.beans.NavigationBean.Action;
import fr.symphonie.budget.ui.beans.NavigationBean.Menu;
import fr.symphonie.budget.ui.beans.edition.EditionHelper;
import fr.symphonie.common.model.AppCfgConfig;
import fr.symphonie.common.util.BudgetHelper;
import fr.symphonie.common.util.MsgEntry;
import fr.symphonie.common.util.Util;
import fr.symphonie.excel.ExcelFileImportor;
import fr.symphonie.exception.MissingConfiguration;
import fr.symphonie.util.HandlerJSFMessage;
import fr.symphonie.util.Helper;
import fr.symphonie.util.model.SimpleEntity;
import fr.symphonie.util.model.Trace;

public abstract class GenericBean extends ExcelFileImportor {
	
	private static final Logger logger = LoggerFactory.getLogger(GenericBean.class);
	private List<Export> exportList;
	private String codeExport;
	private Export selectedExport;
	private Edition edition;
	private boolean recRecouveDataRefreshed=false;
	private Integer recNivOrig=null;
	private List<Integer> recNivDestList;
	private List<Integer> nivDestList;
	private String depNivDest;
	private boolean saveDematExecuted=false;

	public boolean isCommonRequiredDone(){
		int exercice=getExercice();
		String codeBudget=getCodeBudget();
		if(exercice==0)return false;
		if((codeBudget==null)||(codeBudget.trim().isEmpty()))return false;
		return true;
	}
	public boolean isRequiredDataDone(){		
		if(!isCommonRequiredDone()) return false;	
		return true;
	}
	public SearchBean getSearchBean() {
		return BudgetHelper.getSearchBean();
	}

	public String getCodeBudget() {
		return getSearchBean().getCodeBudget();
	}
	public Integer getExercice() {
		return getSearchBean().getCurentExercice();
	}
	public Edition loadFromEditionData(boolean forceDematData,Integer niveau,Integer periodeTresorerie) throws MissingConfiguration {
		logger.debug("loadFromEditionData: start");
		int exercice=getExercice();
		String codeBudget=getCodeBudget();
	//	Integer periodeTresorerie=getPeriode() ;
		
		Edition e=BudgetHelper.getEditionBean().loadEditionLogic(exercice,codeBudget,niveau,periodeTresorerie,forceDematData);
		logger.debug("loadFromEditionData: end");
		return e;
	}
	protected Integer getPeriode() {
		return null;
	}
	public void onExerciceChange(){
		logger.debug("onExerciceChange ...");	
		reset();		
		List<String> list=BudgetHelper.getLoaderBean().getListBudgets();
		if (list.size()==1)	getSearchBean().setCodeBudget(list.get(0));	
		//reset();
		try {
			prepare();
		} catch (Exception e) {
			HandlerJSFMessage.addErrorMessage(Util.getErrorMessageFromException(MsgEntry.FAILED, e));
			logger.error("onExerciceChange: ERROR",e);;
		}
	}
	public Action getCurrentAction(){
		return BudgetHelper.getNavigationBean().getCurrentAction();
	}
	public String getNomConfig() throws MissingConfiguration {
		return AppCfgConfig.getInstance().getEtablisInitiales();
	}
	public boolean isDemat(){
		
		Action action=BudgetHelper.getNavigationBean().getCurrentAction();
		
		return (Action.INTERFACE_MINEFI==action)||((Action.VAL_CF==action));
	}
	public abstract void search();
	public abstract void reset();
	public abstract void validate();

	public static String getErrorMessageFromException(String genericKeyMsg, Exception e) {

		Throwable cause = null;
		String errorMsg = null;
		if (e != null) {
			cause = e;
			errorMsg = cause.getMessage();
			if (cause.getCause() != null) {
				cause = cause.getCause();
				errorMsg += "\n " + cause.getMessage();
				if (cause.getCause() != null) {
					cause = cause.getCause();
					errorMsg += "\n " + cause.getMessage();
				}
			}
		}
		if (genericKeyMsg != null)
			return HandlerJSFMessage.getErrorMessage(genericKeyMsg) + ": " + errorMsg;
		else
			return errorMsg;
	}
	
	protected Edition loadFromDematData(TabsEnum[] tabs,Export export) {
		 logger.debug("loadDematData: start: ");
		 Edition e=new Edition();
		 e.setExercice(export.getExercice());
		 e.setCodeBudget(export.getCodeBudget());
		 EditionHelper helper=new EditionHelper();
		List<? extends GenericDemat> dematList=null;
	
		 for(TabsEnum t:tabs){
			 if(!t.isDemat()) continue;
			 dematList=loadDematList(t.getEntityType(),export);
			 helper.setData(t, dematList);
		 }
		 helper.fillEdition(e);
		 logger.debug("loadDematData: end: ");	
			return e;
		}
	protected List<? extends GenericDemat> loadDematList(Class<? extends GenericDemat> type,Export export) {
		List<? extends GenericDemat> list= getDematService().getDematList(type,export);
		
		return list;
	}
	private DematService getDematService() {
		
		return BudgetHelper.getDematService();
	}
	public void setExportList(List<Export> exportList) {
		this.exportList = exportList;
	}
public List<Export> getExportList() {
		
		if(exportList==null){
			if(!isCommonRequiredDone())return null;
			List<Export> allList=getDematService().getExportList(getExercice(),getCodeBudget());			
			exportList=filterPeriod(allList);
			completExport(exportList);
		
			
		}
		if(exportList!=null) Collections.sort(exportList);
		return exportList;
	}

private void completExport(List<Export> list) {
	Map<String,ExportInfo> infos=getInfoExportMap();
	ExportInfo info=null;
	for(Export e: list){
		info=infos.get(e.getCode());
		e.setInfo(info);
	}
	
}
public Map<String,ExportInfo> getInfoExportMap() {
	Map<String,ExportInfo> infoExportMap=null;
		List<ExportInfo> list=getDematService().getInfoExportList();
		if(!list.isEmpty()){
			infoExportMap=new HashMap<String,ExportInfo>();
		for(ExportInfo info: list){
			infoExportMap.put(info.getCode(), info);
		}
		}
	return infoExportMap;
}

protected List<Export> filterPeriod(List<Export> allList) {
	List<Export> result=new ArrayList<>();
	PeriodeEnum pEnum=null;
	Periode periode=null;
	List<Periode> periodList= BudgetHelper.getPlanTresorerieService().getPeriodeList(getExercice());
	
	for(Export exp:allList){
		pEnum=PeriodeEnum.parseBycodeInfoCentre(exp.getCode());
		if(pEnum==null)continue;
		periode=BudgetHelper.getPeriode(periodList,pEnum.getValue());
		if(periode==null)continue;
		if(periode.getEtat()==EtatPeriodeEnum.Cloture)result.add(exp);
	}
	return result;
}

public String getCodeExport() {
	return codeExport;
}
public void setCodeExport(String codeExport) {
	this.codeExport = codeExport;
	setSelectedExport(null);
	setEdition(null);
}
public Export getSelectedExport() {
	if(selectedExport==null)
	{
		 String selectedCode=getCodeExport();
		 
		 if(selectedCode!=null)
		 {
			 if(PeriodeEnum.Compte_financier.getCodeInfoCentre().equals(selectedCode))
				 selectedExport= getCfExport();
			 else
			 {
				 selectedExport=getExportByCode(selectedCode) ;
			 
			 }
		 }
	}
	return selectedExport;
}
public Export getExportByCode(String code) {
	Export export=null;
	for(Export e: getExportList())
	{
		 if(e.getCode().equals(code)){
			 export=e;
			 break;
		 }
			 
	 }
	return export;
}
public void setSelectedExport(Export selectedExport) {
	this.selectedExport = selectedExport;
}
public Edition getEdition() {
	return edition;
}
public void setEdition(Edition edition) {
	this.edition = edition;
}

protected void refreshRecetteRecouveData()  {
//	logger.debug("refreshRecetteRecouveData: start");
	Integer exercice=getExercice();
	if(!isRecRecouveDataRefreshed()){
		logger.debug("refreshRecetteRecouveData:  refresh is needed");
		BudgetHelper.refreshRecetteRecouveData(exercice);
		setRecRecouveDataRefreshed(true);
		
	}
//	logger.debug("refreshRecetteRecouveData: end");
	}
public boolean isRecRecouveDataRefreshed() {
	return recRecouveDataRefreshed;
}

public void setRecRecouveDataRefreshed(boolean recRecouveDataRefreshed) {
	this.recRecouveDataRefreshed = recRecouveDataRefreshed;
}
protected List<RecByOrig> createListRecByOrig( int exercice, String codeBudg,List<SimpleEntity> list){
	List<RecByOrig> result=null;
	Map<String,RecByOrig> map=new HashMap<>();
	RecByOrig item=null;
	String code_origine=null,nat_grp=null;
	double recHT=0;
	SimpleEntity destination=null;
	for(SimpleEntity se:list){
		//READ DATA
		code_origine=se.getCode();
		nat_grp=se.getDesignation();
		recHT=(double) se.getAdditionalValues().get("rec_ht");
		//CHECK IF ROW HAS CREATED
		item=map.get(code_origine) ;
		if(item==null){ 
			//CREATE ITEM
			item=new RecByOrig(new SimpleEntity(code_origine, ""));
			map.put(code_origine, item);
		}
		item.setMontant(nat_grp, new BigDecimal(recHT));
	}
	//CREATE LIST 
	result=new ArrayList<RecByOrig>( map.values());
	for(RecByOrig r:result){
		destination=BudgetHelper.getBudgetService().getBudgDestination(exercice, r.getOrigine().getCode(), codeBudg);
		if(destination!=null) r.getOrigine().setDesignation(destination.getDesignation());
	}
	Collections.sort(result);
	return result;
}
public Integer getRecNivOrig() {
	return recNivOrig;
}
public void setRecNivOrig(Integer recNivOrig) {
	this.recNivOrig = recNivOrig;
}

public List<Integer> getRecNivDestList() {
	if(recNivDestList==null){
		recNivDestList=new ArrayList<>();
		List<Integer> list=BudgetHelper.getBudgetService().getNivDestList(getExercice(),DepRecEnum.Recette);
		Collections.sort(list);
		Integer maxNiv=list.get(list.size()-1);
		for(int i=1;i<=maxNiv;i++)recNivDestList.add(i);
	}
	
	return recNivDestList;
}

public void setRecNivDestList(List<Integer> recNivDestList) {
	this.recNivDestList = recNivDestList;
}
public List<Integer> getNivDestList() {
	if(nivDestList==null){
		nivDestList=BudgetHelper.getBudgetService().getNivDestList(getExercice(),DepRecEnum.Depense);
	}
	return nivDestList;
}

public void setNivDestList(List<Integer> nivDestList) {
	this.nivDestList = nivDestList;
}
public void onRecNivDestChange(){	
	
}
public void onDepNivDestChange(){

}
public String getDepNivDest() {	
	logger.debug("getDepNivDest : {} ",this.depNivDest);
	return depNivDest;
}
public void setDepNivDest(String depNivDest) {
	this.depNivDest = depNivDest;
}

public Integer getDepNivDestInt() {
	if(getDepNivDest()==null)return null;
		if(getDepNivDest().trim().isEmpty())return null;
	return Integer.parseInt(getDepNivDest());
}
protected EFE loadEfeFromPLT(Edition edition,Integer periodeRealise) {
	double b1=0,b2=0,c1=0,c2=0,e1=0,e2=0;
	
	EFE efe=edition.getEfe();
	ABE abe=edition.getAbe();
//	TFP tfp=edition.getTab6().getTfp();
	efe.getSoldBudgDeficit().setMontantDouble(abe.getSoldBudgDeficit().getMontantDouble());
	efe.getSoldBudgExcedent().setMontantDouble(abe.getSoldBudgExcedent().getMontantDouble());
	PlanTresorerie<DetailLigneTresorerie> plan=edition.getPlanTresorerie();
	
//	b1=tfp.getRembCautions().getMontantDouble()+tfp.getRembDettes().getMontantDouble();//data8 + data10
//	b2=tfp.getCautionRecu().getMontantDouble()+ tfp.getAugDettes().getMontantDouble();//data9+data11
	b1=plan.getSomme(periodeRealise,34, 35)+plan.getSomme(periodeRealise,36, null);
	b2=plan.getSomme(periodeRealise,12, 13)+plan.getSomme(periodeRealise,14, null);
	c1=plan.getSomme(periodeRealise,31, 32);
	c2=plan.getSomme(periodeRealise,16, 17);
	e1=plan.getSomme(periodeRealise,33, null);
	e2=plan.getSomme(periodeRealise,18, null);
	
	efe.getB1().setMontantDouble(b1);efe.getB2().setMontantDouble(b2);
	efe.getCompteTiersC1().setMontantDouble(c1);efe.getCompteTiersC2().setMontantDouble(c2);
	efe.getE1().setMontantDouble(e1);efe.getE2().setMontantDouble(e2);
	double m1=0,m2=0,m=0;
	m1=plan.getSomme(periodeRealise,8, 9)+plan.getSomme(periodeRealise,10, null);
	m2=plan.getSomme(periodeRealise,25, 26)+plan.getSomme(periodeRealise,27, 28);
	m=m1-m2;
	efe.getAbondTresorieFleche().setMontantDouble(m>0?m:0);
	efe.getPrelevTresorieFleche().setMontantDouble(m<0?Math.abs(m):0);
	efe.setDefaults();
	return efe;
}
public Periode getPeriodePLT(List<Periode> periodeList,int noPeriode) {
	Periode period = null;
	int index = 0;
	
	if ((periodeList == null)||(periodeList.isEmpty()))
		return null;
	index = periodeList.indexOf(new Periode(noPeriode, null));
	if (index == -1) return null;
	period = periodeList.get(index);
	return period;
}
protected Export getCfExport() {
	
	String code= PeriodeEnum.Compte_financier.getCodeInfoCentre();
	
	Export cf=new Export(getExercice(), getCodeBudget(),code);
	ExportInfo info=new ExportInfo();
	info.setCode(code);
	info.setLibelle(PeriodeEnum.Compte_financier.getLibelle());
	cf.setInfo(info);
	
	return cf;
}
protected Export getAnnelDefExport() {
	
	String code= PeriodeEnum.Annuel_def.getCodeInfoCentre();
	
	Export cf=new Export(getExercice(), getCodeBudget(),code);
	ExportInfo info=new ExportInfo();
	info.setCode(code);
	info.setLibelle(PeriodeEnum.Annuel_def.getLibelle());
	cf.setInfo(info);
	
	return cf;
}

@Override
protected String getSavedPath() throws MissingConfiguration {
	return null;
}

@Override
protected String getImportFileName() {
	return null;
}
@Override
protected void resetDynamicList() {
	
}
public boolean isBI(){
	Menu menu=BudgetHelper.getNavigationBean().getCurrentAction().getSubMenu();
	return Menu.BUDGET_INITIAL==menu;
}
public String getDateArretPeriode(Export export,String pattern) {
	Date dateFin=getArretPeriode(export,pattern);	
	if(dateFin==null)return null;
	SimpleDateFormat formatter = new SimpleDateFormat(pattern==null?"dd/MM/yyyy":pattern,Locale.FRENCH);	
	String dateFinStr= formatter.format(dateFin);
	logger.debug("getDateArret: {}",dateFinStr);
	return dateFinStr;

	}

	public Date getArretPeriode(Export export, String pattern) {
		Calendar calendar =  Calendar.getInstance();
		Integer exercice = getExercice();
		if (isFevrier(export)) {
			calendar.set(exercice, Calendar.FEBRUARY, 1);
			int maxDay = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
			calendar.set(Calendar.DAY_OF_MONTH, maxDay);
		} else {
			String maskDate = export.getInfo().getMaskDate();
			if (maskDate == null)
				return null;

			int jour = Helper.stringToInt(maskDate.substring(0, 2));
			int month = Helper.stringToInt(maskDate.substring(3, 5));
			calendar.set(exercice, month - 1, jour);
		}
		return calendar.getTime();
	}


public boolean isFevrier(Export export) {
	PeriodeEnum pEnum=PeriodeEnum.parseBycodeInfoCentre(export.getCode());
	return (PeriodeEnum.Fevrier==pEnum);
}
public void saveDemat(Export export ){
	logger.debug("saveDemat: start");
	List<GenericDemat> dematList=new ArrayList<GenericDemat>();
	List<DataItem> items =null;
	List<? extends GenericDemat> list=null;
	try{
		if(export==null)export=getSelectedExport();
		for(TabsEnum type:TabsEnum.values()){
			//if(!type.isDemat())continue;
			if(!isTabDematRequested(type))continue;
			logger.debug("saveDemat: type :{}",type.getType());
			items=transforme(getEdition().getDematItems(type), type);
			list=prepareDematList(export,items,type.getEntityType());
			dematList.addAll(list);
		}	
		
		getDematService().updateDemat(dematList);
		setSaveDematExecuted(true);
		HandlerJSFMessage.addInfo(MsgEntry.SUCCES);
	} catch (Exception e) {
		HandlerJSFMessage.addErrorMessage(getErrorMessageFromException(MsgEntry.FAILED, e));
		e.printStackTrace();
	}
	logger.debug("saveDemat: end");
}
private boolean isTabDematRequested(TabsEnum type) {
	if(!type.isDemat()) return false;
	
	if(PeriodeEnum.Bi.getCodeInfoCentre().equals(getCodeExport())) {
		switch(type) {
		case ABE:				
		case BALANCE:				
		case BILAN:					
		case EFE:
			return false;			
		default:
			break;
		
		}
	}
	
	return true;
}
private List<DataItem> transforme(List<DataItem> items, TabsEnum type) {
	if(type!=TabsEnum.TFP) return items;
	DataItem rembCautions=null,cautionRecu=null,
			rembDettes=null,augDettes=null;
	List<DataItem> copy=Util.copy(items);
	for(DataItem data:copy){
		switch(data.getRefData()){
		case ref_03:
			rembDettes=data;
			break;
		case ref_09:
			augDettes=data;
			break;
		case ref_13:
			rembCautions=data;
			break;
		case ref_14:
			cautionRecu=data;
			break;
		default:
			break;
		}
	}
	rembDettes.add(rembCautions.getMontantDouble());
	augDettes.add(cautionRecu.getMontantDouble());

	return copy;
	
}
private List<? extends GenericDemat> prepareDematList(Export export,List<DataItem> updateList,Class<? extends GenericDemat> type){
//	logger.debug("prepareDematList: start : type={} ",type);
	
	Map<String,DataItem> updateMap=convertToMap(updateList);
	List<? extends GenericDemat> dematList=null;
	String reference=null;
	DataItem update=null;
	Trace trace=Helper.createTrace();
	dematList=loadDematList(type,export);
	
	for(GenericDemat item:dematList){
		reference=item.getLibelle();
		update=updateMap.get(reference);
		if(update==null)continue;
		item.setMontant(update.getMontantDouble());
		item.setAuteurMaj(trace.getAuteurMaj());
		item.setDateMaj(trace.getDateMaj());

	}
				
	//logger.debug("prepareDematList: end ");
	return dematList;
}
private Map<String, DataItem> convertToMap(List<DataItem> updateList) {
	Map<String, DataItem> map=new HashMap<String, DataItem>();
	for(DataItem item:updateList){
		map.put(item.getRefData().getCodeStr(), item);
	}
	return map;
}
/*
public List<? extends GenericDemat> loadDematList(Export export, Class<? extends GenericDemat> type, DematService service) {
	List<? extends GenericDemat> list= service.getDematList(type,export);
	return list;
}*/
public boolean isSaveDematExecuted() {
	return saveDematExecuted;
}
public void setSaveDematExecuted(boolean saveDematExecuted) {
	this.saveDematExecuted = saveDematExecuted;
}
protected  void prepare() throws Exception{
	logger.debug("prepare: ...");
}
}
