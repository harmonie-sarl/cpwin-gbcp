package fr.symphonie.common.util;

import static fr.symphonie.budget.core.dao.ps.ColumnName.CP_col;
import static fr.symphonie.budget.core.dao.ps.ColumnName.EI_col;
import static fr.symphonie.budget.core.dao.ps.ColumnName.EJ_col;
import static fr.symphonie.budget.core.dao.ps.ColumnName.No_ej_col;
import static fr.symphonie.budget.core.dao.ps.ColumnName.TIERS_col;
import static fr.symphonie.budget.core.dao.ps.ColumnName.besoin_col;
import static fr.symphonie.budget.core.dao.ps.ColumnName.ca_col;
import static fr.symphonie.budget.core.dao.ps.ColumnName.co_col;
import static fr.symphonie.budget.core.dao.ps.ColumnName.code_budg_col;
import static fr.symphonie.budget.core.dao.ps.ColumnName.code_dest_col;
import static fr.symphonie.budget.core.dao.ps.ColumnName.code_nature_col;
import static fr.symphonie.budget.core.dao.ps.ColumnName.code_prog_col;
import static fr.symphonie.budget.core.dao.ps.ColumnName.code_service_col;
import static fr.symphonie.budget.core.dao.ps.ColumnName.code_tiers_col;
import static fr.symphonie.budget.core.dao.ps.ColumnName.compl_col;
import static fr.symphonie.budget.core.dao.ps.ColumnName.cred_annul_col;
import static fr.symphonie.budget.core.dao.ps.ColumnName.differe_col;
import static fr.symphonie.budget.core.dao.ps.ColumnName.disp_ej_col;
import static fr.symphonie.budget.core.dao.ps.ColumnName.dispo_col;
import static fr.symphonie.budget.core.dao.ps.ColumnName.dp_ant_col;
import static fr.symphonie.budget.core.dao.ps.ColumnName.dp_emise_col;
import static fr.symphonie.budget.core.dao.ps.ColumnName.dp_ex;
import static fr.symphonie.budget.core.dao.ps.ColumnName.dp_net_col;
import static fr.symphonie.budget.core.dao.ps.ColumnName.dp_pec_col;
import static fr.symphonie.budget.core.dao.ps.ColumnName.dp_saisie_col;
import static fr.symphonie.budget.core.dao.ps.ColumnName.dp_tot_col;
import static fr.symphonie.budget.core.dao.ps.ColumnName.dr;
import static fr.symphonie.budget.core.dao.ps.ColumnName.ej_htd_cp_col;
import static fr.symphonie.budget.core.dao.ps.ColumnName.flech_glob_col;
import static fr.symphonie.budget.core.dao.ps.ColumnName.lib_cej_col;
import static fr.symphonie.budget.core.dao.ps.ColumnName.mttot_ej_col;
import static fr.symphonie.budget.core.dao.ps.ColumnName.nat_grp_col;
import static fr.symphonie.budget.core.dao.ps.ColumnName.nom_prog_col;
import static fr.symphonie.budget.core.dao.ps.ColumnName.nom_tiers_col;
import static fr.symphonie.budget.core.dao.ps.ColumnName.num_exec_cp_col;
import static fr.symphonie.budget.core.dao.ps.ColumnName.objet_col;
import static fr.symphonie.budget.core.dao.ps.ColumnName.prevu_ex_col;
import static fr.symphonie.budget.core.dao.ps.ColumnName.rap_act_col;
import static fr.symphonie.budget.core.dao.ps.ColumnName.rap_ant_col;
import static fr.symphonie.budget.core.dao.ps.ColumnName.rap_col;
import static fr.symphonie.budget.core.dao.ps.ColumnName.rap_diff_col;
import static fr.symphonie.budget.core.dao.ps.ColumnName.rap_ex_col;
import static fr.symphonie.budget.core.dao.ps.ColumnName.rap_ini_col;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import org.apache.logging.log4j.util.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;

import fr.symphonie.budget.core.model.plt.GenericLigneTresorerie;
import fr.symphonie.budget.core.model.plt.Periode;
import fr.symphonie.budget.core.model.plt.PlanTresorerie;
import fr.symphonie.budget.core.model.pluri.SuiviAeCpStruct;
import fr.symphonie.budget.core.model.pluri.SuiviPluriStruct;
import fr.symphonie.budget.core.model.pluri.suiviCpStruct;
import fr.symphonie.budget.core.service.DematService;
import fr.symphonie.budget.core.service.IBudgetPluriannuelService;
import fr.symphonie.budget.core.service.IGestionTiersService;
import fr.symphonie.budget.core.service.IPlanTresorerieService;
import fr.symphonie.budget.ui.beans.ApplicationBean;
import fr.symphonie.budget.ui.beans.CommonBean;
import fr.symphonie.budget.ui.beans.GestionTiersBean;
import fr.symphonie.budget.ui.beans.LoaderBean;
import fr.symphonie.budget.ui.beans.NavigationBean;
import fr.symphonie.budget.ui.beans.SearchBean;
import fr.symphonie.budget.ui.beans.edition.CompteFinancierBean;
import fr.symphonie.budget.ui.beans.edition.DematBean;
import fr.symphonie.budget.ui.beans.edition.EditionBean;
import fr.symphonie.budget.ui.beans.pluri.BudgetPluriannuelBean;
import fr.symphonie.budget.ui.beans.pluri.PlanTresorerieBean;
import fr.symphonie.common.InitializationBean;
import fr.symphonie.common.core.ICommonService;
import fr.symphonie.cpwin.core.demat.IDematService;
import fr.symphonie.impor.ConfigImport;
import fr.symphonie.tools.common.DataListBean;
import fr.symphonie.tools.dp.DpBean;
import fr.symphonie.tools.gts.core.GtsService;
import fr.symphonie.tools.gts.ui.ReferentielBean;
import fr.symphonie.tools.signature.SignatureBean;
import fr.symphonie.util.HandlerJSFMessage;
import fr.symphonie.util.Helper;
import fr.symphonie.util.model.SimpleEntity;
import fr.symphonie.util.model.User;
import fr.symphonie.web.jsf.beans.UserBean;

public class BudgetHelper {

	// /**
	// * Logger for this class
	// */
	 private static final Logger logger = LoggerFactory.getLogger(BudgetHelper.class);
	//
	public final static char DOLLAR_PREFIX = '$';
	private static ResourceBundle editionBundle;
	private static ResourceBundle importConfigBundele;
	private static SimpleDateFormat formatFR = new SimpleDateFormat("dd MMMM yyyy", Locale.FRENCH);
	private static SimpleDateFormat formatDE = new SimpleDateFormat("dd MMMM yyyy", Locale.GERMAN);
	static {
		try {
			setEditionBundle(ResourceBundle.getBundle("edition", new Locale("fr")));
			setImportConfigBundele(ResourceBundle.getBundle("import-config", new Locale("fr")));

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static String getEditionMsg(String key) {
		String result = getEditionBundle().getString(key);

		return result;
	}
	public static SignatureBean getSignatureBean() {
		String beanName = "signatBean";
		return (SignatureBean) Helper.findBean(beanName);
	}
	public static ReferentielBean getReferentielBean() {
		String beanName = "gtsRefBean";
		return (ReferentielBean) Helper.findBean(beanName);
	}

	public static DataListBean getDataListBean() {
		String beanName = "dataListBean";
		return (DataListBean) Helper.findBean(beanName);
	}

	public static PlanTresorerieBean getPlanTresorerieBean() {
		String beanName = "pltBean";
		return (PlanTresorerieBean) Helper.findBean(beanName);
	}

	public static IPlanTresorerieService getPlanTresorerieService() {
		String beanName = "pltService";
		return (IPlanTresorerieService) Helper.findBean(beanName);
	}

	public static CommonBean getCommonBean() {
		String beanName = "commonBean";
		return (CommonBean) Helper.findBean(beanName);
	}

	public static LoaderBean getLoaderBean() {
		String beanName = "loaderBean";
		return (LoaderBean) Helper.findBean(beanName);
	}

	public static NavigationBean getNavigationBean() {
		String beanName = "navigationBean";
		return (NavigationBean) Helper.findBean(beanName);
	}

	public static UserBean getUserBean() {
		String beanName = "userBean";
		return (UserBean) Helper.findBean(beanName);
	}

	public static BudgetPluriannuelBean getBpBean() {
		String beanName = "bpBean";
		return (BudgetPluriannuelBean) Helper.findBean(beanName);
	}

	public static SearchBean getSearchBean() {
		String beanName = "searchBean";
		return (SearchBean) Helper.findBean(beanName);
	}

	public static IBudgetPluriannuelService getBudgetService() {
		String beanName = "budgetPluriService";
		return (IBudgetPluriannuelService) Helper.findBean(beanName);
	}

	public static ApplicationBean getApplicationBean() {
		String beanName = "appBean";
		return (ApplicationBean) Helper.findBean(beanName);
	}

	public static ICommonService getCommonService() {
		String beanName = "commonService";
		return (ICommonService) Helper.findBean(beanName);
	}
	@SuppressWarnings("unchecked")
	public static fr.symphonie.util.ICommonService<User> getutilCommonService(){
		String beanName = "utilCommonService";
		return (fr.symphonie.util.ICommonService<User>) Helper.findBean(beanName);
	}

	public static EditionBean getEditionBean() {
		String beanName = "editionBean";
		return (EditionBean) Helper.findBean(beanName);
	}

	public static CompteFinancierBean getCompteFinancierBean() {
		String beanName = "cfBean";
		return (CompteFinancierBean) Helper.findBean(beanName);
	}

	public static DematBean getDematBean() {
		String beanName = "dematBean";
		return (DematBean) Helper.findBean(beanName);
	}
	public static InitializationBean getInitializationBean() {
		String beanName = "initBean";
		return (InitializationBean) Helper.findBean(beanName);
	}
	public static DpBean getDpBean() {
		String beanName = "dpBean";
		return (DpBean) Helper.findBean(beanName);
	}


	public static GestionTiersBean getGestionTiersBean() {
		String beanName = "gestionTiersBean";
		return (GestionTiersBean) Helper.findBean(beanName);
	}

	public static IGestionTiersService getTiersService() {
		String beanName = "gestionTiersService";
		return (IGestionTiersService) Helper.findBean(beanName);
	}
	public static GtsService getGtsService() {
		String beanName = "gtsService";
		return (GtsService) Helper.findBean(beanName);
	}

	public static String format(Double value) {
		Locale langue = Helper.getCurrentLocal();
		NumberFormat nf = NumberFormat.getNumberInstance(langue);
		DecimalFormat myFormatter = (DecimalFormat) nf;
		myFormatter.applyPattern(Constant.FORMAT_MNT_PATTERN);
		return myFormatter.format(value);
	}

	public static double getMontant(String montanObject) {
		double dbl;
		Locale langue = Helper.getCurrentLocal();
		NumberFormat nf = NumberFormat.getNumberInstance(langue);
		DecimalFormat myFormatter = (DecimalFormat) nf;
		myFormatter.applyPattern(Constant.FORMAT_MNT_PATTERN);
		try {
			dbl = myFormatter.parse(montanObject).doubleValue();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return 0d;
		}
		return dbl;
	}

	public static List<String> toDataList(List<Map<String, Object>> dataMap, String colomnName) {

		// Map<String, Object> item=null;
		List<String> dataList = null;

		// dataList=
		// PsExecListe.getInstance(jdbcHelper.getJdbcTemplate()).getData();

		if (dataMap != null) {

			dataList = new ArrayList<String>();
			for (Map<String, Object> item : dataMap) {
				dataList.add(item.get(colomnName).toString());

			}
		}

		return dataList;
	}

	public static List<SimpleEntity> toSEntityList(List<Map<String, Object>> dataMap, String colomn1, String colomn2,
			String colomn3) {

		List<SimpleEntity> dataList = null;
		SimpleEntity e = null;
		boolean colomn1Null = (colomn1 == null);
		boolean colomn2Null = (colomn2 == null);
		boolean colomn3Null = (colomn3 == null);

		if (dataMap != null) {

			dataList = new ArrayList<SimpleEntity>();
			for (Map<String, Object> item : dataMap) {

				e = new SimpleEntity(colomn1Null ? null : item.get(colomn1).toString(),
						colomn2Null ? null : item.get(colomn2).toString());
				e.setAdditionalValue(colomn3Null ? null : item.get(colomn3).toString());
				dataList.add(e);

			}
		}

		return dataList;
	}

	public static List<suiviCpStruct> toSuiviCPStruct(List<Map<String, Object>> dataMap) {

		List<suiviCpStruct> dataList = null;
		suiviCpStruct s = null;
		if (dataMap != null) {
			dataList = new ArrayList<suiviCpStruct>();
			for (Map<String, Object> item : dataMap) {
				s = new suiviCpStruct();

				s.setExerciceCP((Integer) item.get(num_exec_cp_col));
				s.setCodeBudget(item.get(code_budg_col).toString());
				s.setGroupNat(item.get(nat_grp_col).toString());
				s.setCodeDest(item.get(code_dest_col).toString());
				s.setCodeNature(item.get(code_nature_col).toString());
				s.setCodeService(item.get(code_service_col).toString());
				s.setCodeProg(item.get(code_prog_col).toString());
				s.setEj(item.get(EJ_col).toString());
				s.setEjReserve((Double) item.get(ej_htd_cp_col));
				s.setTiers(item.get(TIERS_col).toString());
				s.setLibelle(item.get(lib_cej_col).toString());
				s.setEi(item.get(EI_col).toString());
				s.setDpSaisie((Double) item.get(dp_saisie_col));
				s.setDpEmise((Double) item.get(dp_emise_col));
				s.setDpPec((Double) item.get(dp_pec_col));
				s.setCa((Double) item.get(ca_col));
				s.setRap((Double) item.get(rap_col));
				s.setNomProg(item.get(nom_prog_col).toString());

				dataList.add(s);
			}

		}
		return dataList;

	}

	public static String eliminateSeparator(String source, String separator) {
		String destination = source;
		if (destination != null) {
			destination = destination.replace(separator, "").trim();
			if (destination.isEmpty())
				destination = null;

		}
		return destination;
	}

	public static ResourceBundle getEditionBundle() {
		return editionBundle;
	}

	public static void setEditionBundle(ResourceBundle editionBundle) {
		BudgetHelper.editionBundle = editionBundle;
	}

	public static <T extends GenericLigneTresorerie> PlanTresorerie<T> getInstanceOfPlanTresorerie(Class<T> type) {
		PlanTresorerie<T> p = new PlanTresorerie<>();
		try {
			p.setTotaleEncaiss(type.newInstance());
			p.setTotaleDecaiss(type.newInstance());
			p.setSoldeCumule(type.newInstance());
			p.setSoldeMois(type.newInstance());
			p.setSoldeInitial(type.newInstance());
			p.setVarSoldeMois(type.newInstance());
		} catch (InstantiationException | IllegalAccessException e) {
			e.printStackTrace();
		}
		return p;
	}

	public static Periode getPeriode(List<Periode> periodList, Integer numPeriode) {
		for (Periode p : periodList) {
			if (p.getNumero().equals(numPeriode))
				return p;
		}
		return null;
	}

	public static List<String> getKeys(List<SimpleEntity> list) {
		List<String> codeList = new ArrayList<>();
		for (SimpleEntity s : list) {
			codeList.add(s.getCode());
		}
		return codeList;
	}

	public static Date toDate(String simpleDateFormat) {
		if ((simpleDateFormat == null) || (simpleDateFormat.trim().isEmpty()))
			return null;
		try {
			if (Helper.getCurrentLocal().getLanguage().equals("fr"))
				return formatFR.parse(simpleDateFormat);
			if (Helper.getCurrentLocal().getLanguage().equals("de"))
				return formatDE.parse(simpleDateFormat);
		} catch (ParseException e) {
			e.printStackTrace();
		}

		return null;

	}

	public static void addSpecialSuccesMsg(String specialSucces, Object[] params) {
		String msg = HandlerJSFMessage.getErrorMessage(MsgEntry.SUCCES);
		msg += " : " + HandlerJSFMessage.getMessageByKey(specialSucces);
		msg = HandlerJSFMessage.formatMessage(msg, params);
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "", msg));

	}

	public static String prepareSearchKey(String searchedBlock) {
		if (Strings.isBlank(searchedBlock))
			return null;
		String searchWord = Helper.removeCharacter(searchedBlock, DOLLAR_PREFIX);

		String condition = (isBeginWithDollar(searchedBlock)) ? "%" + searchWord : searchWord;
		condition = (isEndWithDollar(searchedBlock)) ? condition + "%" : condition;
		return condition;
	}

	public static boolean isBeginWithDollar(String prefix) {

		boolean result = false;
		if (prefix != null) {

			if (prefix.startsWith("" + DOLLAR_PREFIX))
				result = true;
		}
		return result;
	}

	public static boolean isEndWithDollar(String prefix) {
		boolean result = false;
		if (prefix != null) {

			if (prefix.endsWith("" + DOLLAR_PREFIX))
				result = true;
		}
		return result;
	}

	public static float round(float d, int decimalPlace) {
		BigDecimal bd = new BigDecimal(Float.toString(d));
		bd = bd.setScale(decimalPlace, BigDecimal.ROUND_HALF_UP);
		return bd.floatValue();
	}

	public static DematService getDematService() {
		String beanName = "dematService";
		return (DematService) Helper.findBean(beanName);
	}
	public static IDematService getCpwinDematService() {
		String beanName = "cpwinDematService";
		return (IDematService) Helper.findBean(beanName);
	}

	public static String getlib(String codeEntity, List<SimpleEntity> listEntity) {
		String result = null;
		if (codeEntity == null)
			return null;
		if (listEntity == null)
			return null;
		for (SimpleEntity simpleEntity : listEntity) {
			if (!simpleEntity.getCode().trim().equalsIgnoreCase(codeEntity))
				continue;
			result = simpleEntity.getDesignation();
			return result;
		}
		return null;
	}

	public static void refreshRecetteRecouveData(Integer exercice) {
		getBudgetService().refreshRecetteRecouveData(exercice);
	}

	public static List<SuiviAeCpStruct> toSuiviAeCPStruct(List<Map<String, Object>> dataMap) {

		List<SuiviAeCpStruct> dataList = null;
		SuiviAeCpStruct s = null;
		if (dataMap != null) {
			dataList = new ArrayList<SuiviAeCpStruct>();
			for (Map<String, Object> item : dataMap) {
				s = new SuiviAeCpStruct();

				s.setCodeDest(item.get(code_dest_col).toString());
				s.setCodeNature(item.get(code_nature_col).toString());
				s.setCodeService(item.get(code_service_col).toString());
				s.setCodeProg(item.get(code_prog_col).toString());
				s.setFlechGlob(item.get(flech_glob_col).toString());
				s.setRapInitiale((Double) (item.get(rap_ini_col)));
				s.setCompl((Double) item.get(compl_col));
				s.setDpAnt((Double) item.get(dp_ant_col));
				s.setRapAnt((Double) item.get(rap_ant_col));
				s.setCo((Double) item.get(co_col));
				s.setEj((Double) item.get(EJ_col));
				s.setDpEx((Double) item.get(dp_ex));
				s.setRapEx((Double) item.get(rap_ex_col));
				s.setDpTot((Double) item.get(dp_tot_col));
				s.setDr((Double) item.get(dr));
				s.setDpNet((Double) item.get(dp_net_col));
				s.setRapAct((Double) item.get(rap_act_col));
				s.setCa((Double) item.get(cred_annul_col));
				s.setDispEj((Double) item.get(disp_ej_col));
				s.setRapDiff((Double) item.get(rap_diff_col));
				s.setCP((Double) item.get(CP_col));
				s.setDispo(item.get(dispo_col).toString());
				s.setBesoin((Double) item.get(besoin_col));

				dataList.add(s);
			}

		}
		return dataList;
	}

	public static List<SuiviPluriStruct> toSuiviPluriStruct(List<Map<String, Object>> dataMap) {

		List<SuiviPluriStruct> dataList = null;
		SuiviPluriStruct s = null;
		if (dataMap != null) {
			dataList = new ArrayList<SuiviPluriStruct>();
			for (Map<String, Object> item : dataMap) {
				s = new SuiviPluriStruct();

				s.setCodeDest(item.get(code_dest_col).toString());
				s.setCodeNature(item.get(code_nature_col).toString());
				s.setCodeService(item.get(code_service_col).toString());
				s.setCodeProg(item.get(code_prog_col).toString());
				s.setFlechGlob(item.get(flech_glob_col).toString());
				s.setNumEj((Integer) item.get(No_ej_col));
				s.setCodeTiers(item.get(code_tiers_col).toString());
				s.setNomTiers(item.get(nom_tiers_col).toString());
				s.setMntTotal((Double) item.get(mttot_ej_col));
				s.setObjet(item.get(objet_col).toString());
				s.setPlanCpCourant((Double) item.get(prevu_ex_col));
				s.setPlanCpSup((Double) item.get(differe_col));

				dataList.add(s);
			}

		}
		return dataList;

	}

	public static ResourceBundle getImportConfigBundele() {
		return importConfigBundele;
	}

	public static void setImportConfigBundele(ResourceBundle importConfigBundele) {
		BudgetHelper.importConfigBundele = importConfigBundele;
	}
	public static String getImportConfigParam(String key) {
		return getImportConfigBundele().getString(key);
	}
	public static ConfigImport loadConfigImport(String configImportKey) {
		ConfigImport result =null;
		String jsonConfigImport=getImportConfigParam(configImportKey);
		logger.debug("loadConfigImport: jsonConfigImport={}",jsonConfigImport);
		
		ObjectMapper mapper = new ObjectMapper();
		try {
			result = mapper.readValue(jsonConfigImport, ConfigImport.class);
		} catch (IOException e) {
			
			e.printStackTrace();
		}
		logger.debug("loadConfigImport: result={}",result);
		return result;
	}
	public static boolean getBooleanConfigParam(String key) {
		return Boolean.parseBoolean(HandlerJSFMessage.getConfigParam(key));
	}
}
