package fr.symphonie.budget.ui.beans.edition;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;

import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.symphonie.budget.core.model.demat.Export;
import fr.symphonie.budget.core.model.demat.GenericDemat;
import fr.symphonie.budget.core.model.edition.Edition;
import fr.symphonie.budget.core.model.edition.Tab3;
import fr.symphonie.budget.core.model.edition.TabsEnum;
import fr.symphonie.budget.core.model.edition.util.ABE;
import fr.symphonie.budget.core.model.plt.EtatPeriodeEnum;
import fr.symphonie.budget.core.model.plt.Periode;
import fr.symphonie.budget.core.model.plt.PeriodeEnum;
import fr.symphonie.budget.core.model.pluri.BalanceItem;
import fr.symphonie.budget.core.service.DematService;
import fr.symphonie.budget.infocentre.DematFileStruct;
import fr.symphonie.budget.infocentre.DematFileStruct.Detail;
import fr.symphonie.budget.infocentre.DematFileStruct.DetailBalance;
import fr.symphonie.budget.infocentre.DematFileStruct.EndLine;
import fr.symphonie.budget.ui.beans.GenericBean;
import fr.symphonie.budget.ui.beans.LoaderBean;
import fr.symphonie.budget.ui.excel.ExcelHandler;
import fr.symphonie.budget.ui.excel.ExcelModelEnum;
import fr.symphonie.common.model.AppCfgConfig;
import fr.symphonie.common.util.BudgetHelper;
import fr.symphonie.common.util.MsgEntry;
import fr.symphonie.exception.MissingConfiguration;
import fr.symphonie.util.FileHelper;
import fr.symphonie.util.HandlerJSFMessage;
import fr.symphonie.util.Helper;

@ManagedBean (name="dematBeanv1")
@SessionScoped
public class DematBeanV1 extends GenericBean implements Serializable{

	private static final long serialVersionUID = 6024362867782647244L;
	/** 
	 * Logger for this class
	 */
	private static final Logger logger = LoggerFactory.getLogger(DematBean.class);
	@ManagedProperty(value = "#{dematService}")
	private DematService service;

	/**
	 * Result attributes
	 */
	private List<BalanceItem> balance;
	private boolean exportFileGenerated = false;
	private boolean saveDematExecuted=false;
	private Integer niveauUsed=null;
	
	@Override
	protected List<Export> filterPeriod(List<Export> allList) {
		List<Export> result=new ArrayList<>();
		PeriodeEnum pEnum=null;
		Periode periode=null;
		List<Periode> periodList= BudgetHelper.getPlanTresorerieService().getPeriodeList(getExercice());
		
		for(Export exp:allList){
			pEnum=PeriodeEnum.parseBycodeInfoCentre(exp.getCode());
			if(pEnum==null)continue;
			if(pEnum.getValue()==0)result.add(exp);
			periode=BudgetHelper.getPeriode(periodList,pEnum.getValue());
			if(periode==null)continue;
			if((periode.getEtat()==EtatPeriodeEnum.Cloture))result.add(exp);
		}
		return result;
	}
	
	
	public void load()
	{
		
	}
	public void search(){
		logger.debug("search: start");	
		Edition e=null;
		if(!isRequiredDataDone())return ;
		try{
		if(isExportValide()){
			e=loadDematData();
		}
		else{
			niveauUsed=getNiveau();
			e=loadFromEditionData(true,niveauUsed,getPeriode());

		}
		loadBalance();
		setEdition(e);	
		e.setTab3(new Tab3(e));// on attend les spec de chargement de l'onglet Tab3

		BudgetHelper.getEditionBeanV1().setEditionBi(e);
		setSaveDematExecuted(false);
		setExportFileGenerated(false);
		
	   } catch (Exception exp) {
		HandlerJSFMessage.addErrorMessage(getErrorMessageFromException(MsgEntry.FAILED, exp));
		exp.printStackTrace();
	}
	}

	private Integer getNiveau() {
		String dateFin=getDateArret("dd/MM/yyyy");
		return service.getMaxNumeroBr(getExercice(),dateFin);
	}
	public void saveDemat(){
		super.saveDemat(null);
	}

	private List<? extends GenericDemat> loadDematList(Class<? extends GenericDemat> type) {
		return super.loadDematList(type,getSelectedExport());
	}
	
	private boolean isExportValide(){
		Export e=getSelectedExport();		
		if(e==null)return false;
		logger.debug("isExportValide: export: "+e.toString());	
		return e.isValide();
	}
	 public boolean isEditable() {	
		 boolean editable=true;
		 editable=!isExportValide();
		 return editable;
	 }
	 public boolean isRequiredDataDone(){
			
			if(!super.isRequiredDataDone()) return false;
			if(getSelectedExport()==null)return false;
			
			return true;
		}
	 private Edition loadDematData() {
		 logger.debug("loadDematData: start: ");
		 Edition e=new Edition();
		 EditionHelper helper=new EditionHelper();
		List<? extends GenericDemat> dematList=null;
	
		 for(TabsEnum t:TabsEnum.values()){
			 if(!t.isDemat()) continue;
			 dematList=loadDematList(t.getEntityType());
			 helper.setData(t, dematList);
		 }
		 helper.fillEdition(e);
		 logger.debug("loadDematData: end: ");	
			return e;
		}

	 public void reset() {
		    setEdition(null);
		    //BudgetHelper.getEditionBean().setEditionBi(null);
		    BudgetHelper.getEditionBeanV1().setEditionBi(null);
			setCodeExport(null);
			setSaveDematExecuted(false);
			setExportFileGenerated(false);
			setExportList(null);
			setNiveauUsed(null);
	}
	/**
		 * @return the loaderBean
		 */
	public LoaderBean getLoaderBean() {
		return BudgetHelper.getLoaderBean(); 
	}
	@Override
	public void validate() {
		// TODO Auto-generated method stub		
	}
	/**
	 * Elle définit la période du plan trésorerie à charger
	 * Pour l'info centre, on charge la période en ajustement donc explicement +1
	 */
	@Override
	protected Integer getPeriode() {
		String codeExport=getCodeExport();
		PeriodeEnum pEnum=PeriodeEnum.parseBycodeInfoCentre(codeExport);
		Integer p=pEnum.getValue();
		logger.debug("getPeriode: CodeExport={}, periode={}",codeExport,p);

		p= p>=PeriodeEnum.December.getValue()?p:p+1;
		logger.debug("getPeriode: periode demat={}",p);
		return p;
	}
	@Override
	public Edition loadFromEditionData(boolean forceDematData,Integer niveau,Integer periodeTresorerie) throws MissingConfiguration {
		Edition edition= super.loadFromEditionData(forceDematData,niveau,periodeTresorerie);
	
		Integer periodeRealise=periodeTresorerie>=PeriodeEnum.December.getValue()?periodeTresorerie:periodeTresorerie-1;
		//Load ABE and EFE
		if(isAnnuelDef()) {
			loadFromDematData(edition,TabsEnum.ABE);
			loadFromDematData(edition,TabsEnum.EFE);
			loadFromDematData(edition,TabsEnum.TAB10);
		}
		else {
			edition.setAbe(loadABE(edition,periodeRealise));
			edition.setEfe(loadEfeFromPLT(edition,periodeRealise));
		}
		
		return edition;
	}
	 private void loadFromDematData(Edition e,TabsEnum t) {
		 logger.debug("loadDematData: start: ");
		 EditionHelper helper=new EditionHelper();
		List<? extends GenericDemat> dematList=null;
			 dematList=loadDematList(t.getEntityType());
			 helper.setData(t, dematList);

		 helper.fillEdition(e);
		 logger.debug("loadDematData: end: ");	
		}
	
	private ABE loadABE(Edition edition,Integer periodeRealise) throws MissingConfiguration {
		
		ABE abe=edition.getAbe();
		
		Date dateFinPeriode=getDateFinPeriode(periodeRealise);
		String tiersNeutralise=AppCfgConfig.getInstance().getTiersNeutralise();
		Map<String,Double> aeEngage=service.getAeEngage(edition.getExercice(),dateFinPeriode,tiersNeutralise);
		double[] AE=EditionBean.extractDepenseByNatGrp(aeEngage);
		double[] CP=edition.getPlanTresorerie().getMontantsCpForDepence2024(periodeRealise);
		abe.getDepence().setMontants(AE, CP);
		double recette[]=edition.getPlanTresorerie().getMontantsRecetteFor2024(periodeRealise);
		abe.getRecette().setMontantsFor2024(recette);
		
		abe.setDefaults();
		
		return abe;
	}
	private void loadBalance() {
		
		String dateFin=getDateArret("dd/MM/yyyy");
		boolean isDefinitive=PeriodeEnum.Annuel_def.getCodeInfoCentre().equals(getCodeExport());
		setBalance(service.loadBalance(getExercice(),getCodeBudget(),dateFin,isDefinitive));
		
	}
	private Date getDateFinPeriode(Integer periodeRealise) {
		logger.debug("getDateFinPeriode: periodeRealise input={}",periodeRealise);
		Calendar c=Calendar.getInstance();
		if(periodeRealise>=12)return null;
		c.set(getExercice(), periodeRealise, 1);//le mois suivant
		Date date=c.getTime();
		logger.debug("getDateFinPeriode: periodeRealise={}, date={}",periodeRealise,date);
		return date;
	}
	public void setService(DematService service) {
		this.service = service;
	}
	public List<BalanceItem> getBalance() {
		return balance;
	}
	public void setBalance(List<BalanceItem> balance) {
		this.balance = balance;
	}

	 public StreamedContent getXlsDematBi(){
		StreamedContent returnStreamedContent = null;
		ExcelHandler excel=null;
		if(isAnnuelDef())
		{
			 excel = new ExcelHandler(ExcelModelEnum.DEMAT_INFOCENTRE_06_2024,null);
		}
		else if(isBiPeriod()) {
			 excel = new ExcelHandler(ExcelModelEnum.DEMAT_INFOCENTRE_BI_2024,null);
		}
		else {
	         excel = new ExcelHandler(ExcelModelEnum.DEMAT_INFOCENTRE_2024,null);}
		returnStreamedContent = excel.getExcelFile();
		
		return returnStreamedContent;			

	}
	public boolean isExportFileGenerated() {
		return exportFileGenerated;
	}
	public void setExportFileGenerated(boolean exportFileGenerated) {
		this.exportFileGenerated = exportFileGenerated;
	}
	public StreamedContent getDematZipFile() {
		StreamedContent content = null;
		InputStream stream =null;

		try {
		String zipFileName=getNomConfig()+"_"+getExercice()+"_"+getCodeExport()+".zip";
		String zipFilePath=getCurrentYearDematPath()+zipFileName;
		FileHelper.createZipForDirectory(getPeriodDematPath(),zipFilePath);
		
		    stream = new FileInputStream(zipFilePath);			
			content =  new DefaultStreamedContent(stream, "application/zip", zipFileName,"UTF-8");
			
			logger.info("Téléchargement des fichiers de demat effectué avec succés");
		} 
		
		catch (Exception  e) {
			logger.error("Le fichier n'a pas été trouvé");
			e.printStackTrace();
			content = Helper.getStreamedContentFile(Helper.getFileNotFound());
		}
		return content;
	}
	public void generateDematFiles(){
		logger.debug("generateFile: start");
		DematFileStruct dematStruct=null;
		Map<String,String> dematParams=null;
		String path=null;
		try {
			dematParams=loadDematParams();
			path=getPeriodDematPath();
			for(TabsEnum type:TabsEnum.values()){
				if(!isTabDematRequested(type))continue;
				dematStruct=createDematStruct(type,dematParams);
				generateDematFile(path,dematStruct);
			}
			if(!PeriodeEnum.Bi.getCodeInfoCentre().equals(getCodeExport())) {
			generateBalanceDematFile(dematParams,path);
			}
			setExportFileGenerated(true);
			HandlerJSFMessage.addInfo(MsgEntry.SUCCES);

		} catch (Exception e) {
			HandlerJSFMessage.addErrorMessage(getErrorMessageFromException(MsgEntry.FAILED, e));
			e.printStackTrace();
		}
		logger.debug("generateFile: end");
	}
	private boolean isTabDematRequested(TabsEnum type) {
		if(!type.isDemat()) return false;
		switch(type) {
		case ABE:												
		case EFE:
			return !PeriodeEnum.Bi.getCodeInfoCentre().equals(getCodeExport());
		case TAB10:
			return isAnnuelDef();
		default:
			break;		
		}
		
		return true;
	}
		private Map<String, String> loadDematParams() throws MissingConfiguration {
		Map<String,String> params=service.loadDematParams();
		params.put(DematFileStruct.AttributType.Type_doc.getName(), "03");
		params.put(DematFileStruct.AttributType.Code_budget.getName(), "01");
		params.put(DematFileStruct.AttributType.Exercice.getName(),""+ getExercice());
		params.put(DematFileStruct.AttributType.Rang.getName(), getCodeExport());
		
		String pattern="ddMMyyyy";
		String date_arret=getDateArret(pattern);
		if(date_arret==null){
		Calendar c=Calendar.getInstance();
		c.set(getExercice(), 11, 31);
		SimpleDateFormat formatter = new SimpleDateFormat(pattern,Locale.FRENCH);		
		date_arret= formatter.format(c.getTime());
		}
		params.put(DematFileStruct.AttributType.Date_arret.getName(), date_arret);
		logger.debug("loadDematParams: {} ",params);
		return params;
	}
	private DematFileStruct createDematStruct(TabsEnum type, Map<String, String> dematParams) {
		logger.info("createDematStruct: type={}",type);	
		DematFileStruct struct=new DematFileStruct(type, dematParams);
		List<? extends GenericDemat> dematList=loadDematList(type.getEntityType());
		
		Collections.sort(dematList);
		BigDecimal bd=null;
		for(GenericDemat data:dematList){
			bd=new BigDecimal( data.getMontant()).setScale(2, BigDecimal.ROUND_HALF_UP);
			DematFileStruct.Detail detail= struct.new Detail(data.getReference(),data.getLibelle(),bd);
			struct.getBody().add(detail);
		}
		int countOfLine=dematList.size()+2;
		DematFileStruct.EndLine endLine=struct.new EndLine(countOfLine);
		struct.setFooter(endLine);
		return struct;
	}
	
	private void generateDematFile(String path, DematFileStruct dematStruct) {
		String targetFile=path + dematStruct.getGeneratedFileName();
		logger.debug("generateDematFile: {}",targetFile);
		try (PrintWriter  out = new PrintWriter(new OutputStreamWriter(
			      new BufferedOutputStream(new FileOutputStream(targetFile)), "US-ASCII")) )
		{
			 out.println(dematStruct.getHead().toString());
			 for(DematFileStruct.Detail d:dematStruct.getBody()){
				 out.println(d.toString());
			 }
			 out.println(dematStruct.getFooter().toString());

		  out.flush();
		} catch (UnsupportedEncodingException|FileNotFoundException e) {
		  e.printStackTrace();
		} 
		
	}
	private void generateBalanceDematFile(Map<String, String> dematParams, String path) {
		DematFileStruct struct=new DematFileStruct(TabsEnum.BALANCE, dematParams);
		struct.getHead().setFiller(83);
		List<BalanceItem> dematList=getBalance();
		int noLigne=2;
		for(BalanceItem data:dematList){
			DematFileStruct.DetailBalance detail= struct.new DetailBalance(noLigne,data);			
			struct.getBody().add(detail);
			noLigne++;
		}
		DematFileStruct.EndLine endLine=struct.new EndLine(noLigne);
		endLine.setFiller(136);
		struct.setFooter(endLine);
		
		generateDematFile(path, struct);
		
		
	}

private String getDateArret(String pattern) {
	String dateFin=getDateArretPeriode(getSelectedExport(),pattern);
	
	return dateFin;

	}
private String getPeriodDematPath() throws MissingConfiguration {
		
		StringBuilder path=new StringBuilder(getCurrentYearDematPath());
		
		path.append(getCodeExport());
		path.append(File.separator);
		 Path p =Paths.get(path.toString());
		if (Files.notExists(p)) {
			 try {
				 logger.debug("getPeriodDematPath: Files.createDirectories");
				Files.createDirectories(p);
			} catch (IOException e) {
				e.printStackTrace();
			}
		 }
		
		
		return path.toString();
	}
private String getCurrentYearDematPath() throws MissingConfiguration{
	StringBuilder path=new StringBuilder(getConfigureDematPath());
	path.append(File.separator);
	path.append(""+getExercice());
	path.append(File.separator);
	return path.toString();
}
private String getConfigureDematPath() throws MissingConfiguration{
return AppCfgConfig.getInstance().getDematPath();
}
public boolean isSaveDematExecuted() {
	return saveDematExecuted;
}
public void setSaveDematExecuted(boolean saveDematExecuted) {
	this.saveDematExecuted = saveDematExecuted;
}

	public boolean isGenerateFileDisabled() {
		if (!isSaveDematExecuted())
			return true;
		return isExportFileGenerated();
	}
	public Integer getNiveauUsed() {
		return niveauUsed;
	}
	public void setNiveauUsed(Integer niveauUsed) {
		this.niveauUsed = niveauUsed;
	}

	public String getBiBrDataLoaded() {
		String msgKey = null;
		if (getNiveauUsed() == null)
			return "";
		if (getNiveauUsed() == 0) {
			msgKey = MsgEntry.BI_CHRG_SUCCESS;
			return HandlerJSFMessage.getMessage(msgKey);
		} else {
			msgKey = MsgEntry.BR_CHRG_SUCCESS;
			String msg = HandlerJSFMessage.getMessage(msgKey);
			return HandlerJSFMessage.formatMessage(msg, new Object[] { getNiveauUsed() });
		}

	}
	public boolean isDecember(){
		PeriodeEnum pEnum=PeriodeEnum.parseBycodeInfoCentre(getCodeExport());
		return (PeriodeEnum.December==pEnum);
	}
	
	public boolean isBiPeriod(){
		PeriodeEnum pEnum=PeriodeEnum.parseBycodeInfoCentre(getCodeExport());
		return (PeriodeEnum.Bi==pEnum);
	}
	public boolean isAnnuelDef() {
		PeriodeEnum pEnum=PeriodeEnum.parseBycodeInfoCentre(getCodeExport());
		return (PeriodeEnum.Annuel_def==pEnum);
	}

}
