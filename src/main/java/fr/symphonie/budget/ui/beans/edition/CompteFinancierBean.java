package fr.symphonie.budget.ui.beans.edition;

import static fr.symphonie.budget.core.dao.ps.ColumnName.nat_grp;
import static fr.symphonie.budget.core.dao.ps.ColumnName.nb;
import static fr.symphonie.budget.core.dao.ps.ColumnName.net;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;

import org.primefaces.event.TabChangeEvent;
import org.primefaces.model.StreamedContent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.symphonie.budget.core.model.cf.CfGenericDemat;
import fr.symphonie.budget.core.model.cf.CfParam;
import fr.symphonie.budget.core.model.cf.CfReference;
import fr.symphonie.budget.core.model.cf.Rapprochement;
import fr.symphonie.budget.core.model.demat.Export;
import fr.symphonie.budget.core.model.demat.GenericDemat;
import fr.symphonie.budget.core.model.edition.Bilan;
import fr.symphonie.budget.core.model.edition.CR;
import fr.symphonie.budget.core.model.edition.Edition;
import fr.symphonie.budget.core.model.edition.Tab8;
import fr.symphonie.budget.core.model.edition.TabsEnum;
import fr.symphonie.budget.core.model.edition.util.ABE;
import fr.symphonie.budget.core.model.edition.util.DataItem;
import fr.symphonie.budget.core.model.edition.util.DepByDest;
import fr.symphonie.budget.core.model.edition.util.EFE;
import fr.symphonie.budget.core.model.edition.util.NatGrpEnum;
import fr.symphonie.budget.core.model.edition.util.RecByOrig;
import fr.symphonie.budget.core.model.edition.util.TDD;
import fr.symphonie.budget.core.model.edition.util.TRO;
import fr.symphonie.budget.core.model.edition.util.VFR;
import fr.symphonie.budget.core.model.plt.DetailLigneTresorerie;
import fr.symphonie.budget.core.model.plt.EtatPeriodeEnum;
import fr.symphonie.budget.core.model.plt.Periode;
import fr.symphonie.budget.core.model.plt.PeriodeEnum;
import fr.symphonie.budget.core.model.plt.PlanTresorerie;
import fr.symphonie.budget.core.service.DematService;
import fr.symphonie.budget.ui.beans.GenericBean;
import fr.symphonie.budget.ui.excel.ExcelHandler;
import fr.symphonie.budget.ui.excel.ExcelModelEnum;
import fr.symphonie.budget.ui.viewmodel.VentilationImput;
import fr.symphonie.common.util.BudgetHelper;
import fr.symphonie.common.util.MsgEntry;
import fr.symphonie.exception.MissingConfiguration;
import fr.symphonie.util.HandlerJSFMessage;
import fr.symphonie.util.Helper;
import fr.symphonie.util.model.SimpleEntity;
import fr.symphonie.util.model.Trace;

@ManagedBean(name = "cfBean")
@SessionScoped
public class CompteFinancierBean extends GenericBean implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -6631362827877636194L;
	private static final int defaultLevel = 1;

	private static final Logger logger = LoggerFactory.getLogger(CompteFinancierBean.class);

	@ManagedProperty(value = "#{dematService}")
	private DematService service;

	public void setService(DematService service) {
		this.service = service;
	}

	List<SimpleEntity> compteList = null;
	private List<CfParam> paramBilanList = null;
	private List<CfReference> refBilanList = null;
	private List<CfParam> paramCRList = null;
	private List<CfReference> refCRList = null;
	private List<CfReference> refTab6List = null;
	private List<CfReference> refTab2List = null;
	private List<CfReference> refEFEList = null;
	private List<CfReference> refSPE1List = null;
	private List<CfReference> refSPE2List = null;
	private Map<String, Double> balance = null;
	private boolean withLoadAnt = false;
	private Bilan bilan = null;
	private CR cr = null;
	private Boolean bilanValide = null;
	private Boolean crValide = null;
	private List<Export> cfExportList;
	private boolean withLoadPeriod = false;
	private List<Map<String, Object>> suiviByEnvelopData = null;
	private Rapprochement cfRapproche;
	private List<Periode> periodeList;
	private Integer periodeSuivi = null;
	private List<PeriodeEnum> periodeSuiviList;
	private Date dateFin;
	private boolean plTresEnabled = false;
	private List<VentilationImput> cfVentilationList;

	@Override
	public void search() {
		logger.debug("search: start");
		try {
			switch (getCurrentAction()) {

			case PARAM_BILAN:
				prepare(TabsEnum.BILAN);
				loadParamBilan();
				break;
			case PARAM_CR:
				prepare(TabsEnum.CR);
				loadParamCR();
				break;
			case VAL_BILAN:
				loadBilan();
				break;
			case VAL_CR:
				loadCR();
				break;
			case VAL_CF:
				loadCf();
				break;
			case SUIVI_CF:
				loadSuiviCf();
				break;
			case SUIVI_SF:
				loadSuiviSf();
				break;
			default:
				break;
			}
		} catch (Exception e) {
			HandlerJSFMessage.addErrorMessage(getErrorMessageFromException(MsgEntry.FAILED, e));
			e.printStackTrace();
		}

	}

	@Override
	public void validate() {
		try {
			switch (getCurrentAction()) {

			case PARAM_BILAN:
				saveParamBilan();
				break;
			case PARAM_CR:
				saveParamCR();
				break;
			case VAL_BILAN:
				saveBilan();
				break;
			case VAL_CR:
				saveCR();
				break;
			case VAL_CF:
				saveCf();
				break;
			default:
				break;
			}
			HandlerJSFMessage.addInfo(MsgEntry.SUCCES);
		} catch (Exception e) {
			HandlerJSFMessage.addErrorMessage(getErrorMessageFromException(MsgEntry.FAILED, e));
			e.printStackTrace();
		}
	}

	private void saveCf() {
		logger.debug("saveCf: start ");
		saveDemat();
		logger.debug("saveCf: end ");

	}

	public void saveDemat() {
		logger.debug("saveDemat: start");
		List<GenericDemat> dematList = new ArrayList<GenericDemat>();
		List<DataItem> items = null;
		List<? extends GenericDemat> list = null;
		try {
			for (TabsEnum type : TabsEnum.values()) {
				if (!type.isDemat())
					continue;
				items = getEdition().getDematItems(type);
				list = prepareDematList(items, type);
				dematList.addAll(list);
			}

			service.updateDemat(dematList);
			HandlerJSFMessage.addInfo(MsgEntry.SUCCES);
		
		} catch (Exception e) {
			HandlerJSFMessage.addErrorMessage(getErrorMessageFromException(MsgEntry.FAILED, e));
			e.printStackTrace();
		}
		logger.debug("saveDemat: end");
	}

	private List<? extends GenericDemat> prepareDematList(List<DataItem> dataList, TabsEnum type) {
		logger.debug("prepareDematList: start : type={} ", type);
		List<GenericDemat> dematList = new ArrayList<GenericDemat>();
		GenericDemat demat = null;
		CfReference ref = null;
		Trace trace = Helper.createTrace();
		loadRefList(type, false);
		for (DataItem item : dataList) {
			try {
				demat = type.getEntityType().newInstance();
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("Exception at type.getEntityType().newInstance()", e);
				return dematList;
			}
			ref = getCfReference(type, item.getUid());

			demat.setCode(PeriodeEnum.Compte_financier.getCodeInfoCentre());
			demat.setExercice(getExercice());
			demat.setCodeBudget(getCodeBudget());
			demat.setLibelle(ref.getCode());
			demat.setReference(ref.getNumeroLigne());
			demat.setMontant(item.getMontantDouble());
			demat.setAuteurMaj(trace.getAuteurMaj());
			demat.setDateMaj(trace.getDateMaj());
			dematList.add(demat);
		}
		logger.debug("prepareDematList: end ");
		return dematList;
	}

	private void saveCR() {
		loadRefCRList(false);
		save(TabsEnum.CR, getCr().getItems());
		setCrValide(null);
	}

	private void saveBilan() {
		save(TabsEnum.BILAN, getBilan().getItems());
		setBilanValide(null);
	}

	private void save(TabsEnum type, List<DataItem> items) {
		logger.debug("#### save: type={}, size of list={}", type.getType(), items.size());
		loadRefList(type, false);
		List<CfGenericDemat> dematList = prepareDematList(type, items);
		logger.debug("#### save: dematList size of list={}", dematList.size());
		service.updateList(dematList);
		service.updateEtatCf(getExercice(), getCodeBudget(), type.getType(), true);
		logger.debug("#### save: fin");
	}

	private List<CfGenericDemat> prepareDematList(TabsEnum type, List<DataItem> items) {
		logger.debug("prepareDematList: type={}", type);
		List<CfGenericDemat> dematList = new ArrayList<CfGenericDemat>();
		CfGenericDemat demat = null;
		Trace trace = Helper.createTrace();
		String codeRef = null;
		for (DataItem item : items) {
			codeRef = item.getRefData().getCodeStr();
			logger.debug("prepareDematList: codeRef={}", codeRef);
			CfReference ref = getCfReference(type, codeRef);
			if (ref == null) {
				logger.debug("prepareDematList: ref {} not found", codeRef);
				continue;
			}

			try {
				demat = type.getCfDematType().newInstance();
				demat.setExercice(getExercice());
				demat.setCodeBudget(getCodeBudget());
				demat.setLibelle(codeRef);
				demat.setMontant(item.getMontantDouble());
				demat.setAuteurCrea(trace.getAuteurCreation());
				demat.setDateCrea(trace.getDateCreation());
				demat.setNumeroLigne(ref.getNumeroLigne());

				dematList.add(demat);
			} catch (InstantiationException | IllegalAccessException e) {
				e.printStackTrace();
			}
			
		}
		return dematList;
	}

	private CfReference getCfReference(TabsEnum type, String codeRef) {
		CfReference result = null;
		List<CfReference> list = null;
		switch (type) {
		case BILAN:
			list = getRefBilanList();
			break;
		case CR:
			list = getRefCRList();
			break;
		case ABE:
			list = getRefTab2List();
			break;
		case EFE:
			list = getRefEFEList();
			break;
		case TAB6:
			list = getRefTab6List();
			break;
		default:
			break;
		}

		for (CfReference ref : list) {
			logger.debug("getCfReference: {} --> {}", codeRef, ref.getCode());
			if (ref.getCode().equals(codeRef)) {
				result = ref;
				break;
			}

		}
		return result;
	}

	private void saveParamBilan() {
		service.updateList(getParamBilanList());
	}

	private void saveParamCR() {
		service.updateList(getParamCRList());
	}

	private void prepare(TabsEnum type) {
		loadCompteList(type);
		loadRefList(type, true);

	}

	private void loadRefList(TabsEnum type, boolean onlyDetail) {
		switch (type) {
		case BILAN:
			loadRefBilanList(onlyDetail);
			break;
		case CR:
			loadRefCRList(onlyDetail);
			loadRefTab6List(onlyDetail);
			break;
		case ABE:
			loadRefTab2List(onlyDetail);
			break;
		case EFE:
			setRefEFEList(service.loadReference(type.getType(), onlyDetail));
			break;
		default:
			break;
		}
	}

	private void loadCompteList(TabsEnum type) {
		setCompteList(service.getCompteList(getExercice(), getCodeBudget(), type.getType()));

	}

	private void loadParamBilan() {
		setParamBilanList(loadParamList(TabsEnum.BILAN.getType()));
	}

	private void loadParamCR() {
		setParamCRList(loadParamList(TabsEnum.CR.getType()));

	}

	private List<CfParam> loadParamList(String type) {
		List<CfParam> paramList = null;
		paramList = service.loadParamCF(getExercice(), getCodeBudget(), type);
		if ((paramList.isEmpty()))// create
			paramList = createParamList(type);
		else {// update
			completeData(paramList);
		}
		return paramList;

	}

	private void completeData(List<CfParam> paramList) {
		SimpleEntity compte = null;
		for (CfParam p : paramList) {
			compte = Helper.getSelectedSimpleEntity(p.getCodeCompte(), getCompteList());
			p.setLibCompte(compte.getDesignation());
		}

	}

	private List<CfParam> createParamList(String type) {
		logger.debug("createParamList");
		List<CfParam> paramList = new ArrayList<CfParam>();
		Integer exercice = getExercice();
		String codeBudget = getCodeBudget();
		service.refreshBalance(exercice, codeBudget);

		CfParam param = null;
		for (SimpleEntity s : compteList) {
			param = new CfParam(exercice, codeBudget, type, s.getCode(), s.getDesignation());
			paramList.add(param);
		}
		return paramList;
	}

	private void loadCf() throws MissingConfiguration {
		logger.debug("loadCf : start");
		loadCfData(getExercice(), PeriodeEnum.Annuel_def.getValue(), null);
		loadCfVentilationByNature(getExercice());

		logger.debug("loadCf : end");

	}
	private void loadSuiviSf() throws MissingConfiguration {
		logger.debug("loadSuiviSf : start");
		loadCfVentilationByNature(getExercice());
		logger.debug("loadSuiviSf : end");

	}

	private void loadCfVentilationByNature(Integer exercice) {
		setCfVentilationList(service.getVentilationSfByNature(exercice));
		
	}

	private void loadSuiviCf() throws MissingConfiguration {
		logger.debug("loadCf : start");
		Date dateFinPeriode = getDateFin();
		Integer periodeTresorerie = getPeriodeRealise(getPeriodeSuivi());
		loadCfData(getExercice(), periodeTresorerie, dateFinPeriode);
		setDepNivDest(Integer.toString(defaultLevel));
		setRecNivOrig(defaultLevel);
		loadTDD();
		if (getEdition().getPlanTresorerie() != null) {
			loadTROFromPlt(getEdition().getPlanTresorerie());
		} else {
			loadTRO();
		}

		logger.debug("loadCf : end");

	}

	private Integer getPeriodeRealise(Integer periode) {
		if (periode == null)
			return null;
		if (periode > PeriodeEnum.December.getValue())
			return periode;
		return periode + 1;
	}

	private void loadCfData(Integer exercice, Integer periodeTresorerie, Date dateFinPeriode)
			throws MissingConfiguration {
		logger.debug("loadCfData PERIODE {}, dateFinPeriode={} : start", periodeTresorerie, dateFinPeriode);

		String dateFinStr = null;
		if (dateFinPeriode != null) {
			dateFinStr = Helper.toSimpleFormat2(dateFinPeriode);
		}
		Edition e = new Edition();
		e.setExercice(exercice);
		e.setCodeBudget(getCodeBudget());
		e.setNomConfig(getNomConfig());
		e.setTab7(null);
		//loadTab6(e, dateFinStr);
		loadTab8(e, dateFinStr);

		if (periodeTresorerie != null) {
			e.setTab7(BudgetHelper.getPlanTresorerieBean().loadPlanTresorerie(exercice, periodeTresorerie));
			loadEfeFromPLT(e, periodeTresorerie);
			e.getAbe().initialize();
			setCfRapproche(new Rapprochement(e.getAbe(), e.getTab7()));
		}
		loadTab6(e, dateFinStr);
		setEdition(e);		
		// chargement TAB3
		setDepNivDest(Integer.toString(defaultLevel));
		setRecNivOrig(defaultLevel);
		loadTDD();
		loadTRO();
		

		BudgetHelper.getEditionBean().setEditionBi(e);
	}

	private void resetTab2Depenses() {
		getEdition().getAbe().resetDepense();
	}

	private void resetTab3TDD() {
		getEdition().getTab3().setTdd(new TDD(getEdition()));

	}

	private void loadTDD() {
		resetTab3TDD();
		resetTab2Depenses();
		Integer niveau = getDepNivDestInt();
		String dateFinStr = null;
		if (getDateFin() != null) {
			dateFinStr = Helper.toSimpleFormat2(getDateFin());
		}
		if (niveau == null)
			return;

		List<DepByDest> depByDestList = service.getCpAeParDest(getExercice(), niveau, dateFinStr);
		TDD tdd = getEdition().getTab3().getTdd();
		tdd.setDepByDestList(depByDestList);
		tdd.setTotale(new DepByDest(new SimpleEntity(TDD.TOT_PREFIX, "")));
		getEdition().getTab3().getTro().setTotale(new RecByOrig(new SimpleEntity(TRO.TOT_PREFIX, "")));
		getEdition().getTab3().setDefaults();
		// MAJ ABE
		ABE abe = getEdition().getAbe();

		double[] AE = EditionBean.extractDepenseByNatGrp(tdd.getMapTotalAE());
		double[] CP = EditionBean.extractDepenseByNatGrp(tdd.getMapTotalCP());
		abe.getDepence().setMontants(AE, CP);
		abe.setDefaults();
		// MAJ EFE
		EFE efe = getEdition().getEfe();
		efe.getSoldBudgDeficit().setMontantDouble(abe.getSoldBudgDeficit().getMontantDouble());
		efe.getSoldBudgExcedent().setMontantDouble(abe.getSoldBudgExcedent().getMontantDouble());
		efe.setDefaults();

	}

	private void resetTab3TRO() {
		getEdition().getTab3().setTro(new TRO(getEdition()));

	}

	private void resetTab2Recettes() {
		getEdition().getAbe().resetRecette();
	}

	private void loadTRO() {
		resetTab3TRO();
		resetTab2Recettes();
		refreshRecetteRecouveData();
		Integer niveau = getRecNivOrig();
		Integer exercice = Integer.parseInt(getSearchBean().getExercice());
		String codeBudg = getSearchBean().getCodeBudget();
		logger.debug("loadRecetOrigin:niveau={} ", niveau);
		if (niveau == null)
			return;
		List<SimpleEntity> list = BudgetHelper.getBudgetService().getRecetteByOrigine(exercice, codeBudg, null, null,
				null, null, null, null, null, null, null, niveau);
		List<RecByOrig> recByOrigList = createListRecByOrig(exercice, codeBudg, list);
		fillTRO(recByOrigList);
	}

	private void loadTROFromPlt(PlanTresorerie<DetailLigneTresorerie> planTresorerie) {
		resetTab3TRO();
		resetTab2Recettes();
		RecByOrig recByOrigFromPlt = createRecByOrigFromPlt(planTresorerie);
		List<RecByOrig> recByOrigList = new ArrayList<RecByOrig>();
		recByOrigList.add(recByOrigFromPlt);
		fillTRO(recByOrigList);
	}

	private void fillTRO(List<RecByOrig> recByOrigList) {
		TRO tro = getEdition().getTab3().getTro();
		tro.setRecByOrigList(recByOrigList);
		tro.setTotale(new RecByOrig(new SimpleEntity(TRO.TOT_PREFIX, "")));
		getEdition().getTab3().getTdd().setTotale(new DepByDest(new SimpleEntity(TDD.TOT_PREFIX, "")));
		getEdition().getTab3().setDefaults();
		// MAJ TAB2
		ABE abe = getEdition().getAbe();
		double[] montantsRec = EditionBean.extractRecetteByNatGrp(tro.getMapTotal());
		;
		abe.getRecette().setMontants(montantsRec);
		abe.setDefaults();
		// MAJ EFE
		EFE efe = getEdition().getEfe();
		efe.getSoldBudgDeficit().setMontantDouble(abe.getSoldBudgDeficit().getMontantDouble());
		efe.getSoldBudgExcedent().setMontantDouble(abe.getSoldBudgExcedent().getMontantDouble());
		efe.setDefaults();
		// MAJ TAB8
		Tab8 tab8 = getEdition().getTab8();
		tab8.getRecetR22().getN().setMontantDouble(abe.getRecette().getR22());
		tab8.getRecetR24().getN().setMontantDouble(abe.getRecette().getR24());
		tab8.getRecetR25().getN().setMontantDouble(abe.getRecette().getR25());
		tab8.setTotaux();
	}

	private RecByOrig createRecByOrigFromPlt(PlanTresorerie<DetailLigneTresorerie> planTresorerie) {
		Integer periode = planTresorerie.getPeriode() - 1;
		RecByOrig rec = new RecByOrig(new SimpleEntity("", ""));
		rec.getDataR11().setMontantDouble(planTresorerie.getSomme(periode, 3, null));
		rec.getDataR12().setMontantDouble(planTresorerie.getSomme(periode, 4, null));
		rec.getDataR13().setMontantDouble(planTresorerie.getSomme(periode, 5, null));
		rec.getDataR14().setMontantDouble(planTresorerie.getSomme(periode, 6, null));
		rec.getDataR15().setMontantDouble(planTresorerie.getSomme(periode, 7, null));
		rec.getDataR22().setMontantDouble(planTresorerie.getSomme(periode, 8, null));
		rec.getDataR24().setMontantDouble(planTresorerie.getSomme(periode, 9, null));
		rec.getDataR25().setMontantDouble(planTresorerie.getSomme(periode, 10, null));
		return rec;
	}

	private void loadTab8(Edition e, String dateFin) {
		service.setCpSumLigneTresorerieRF(e, dateFin);
		service.setAeSumLigneTresorerieRF(e, dateFin);
		e.getTab8().setTotaux();

	}

	private void loadBilan() {
		logger.debug("loadBilan : start");
		setBilan(new Bilan());
		List<DataItem> items = getBilan().getItems();
		if (isBilanValide()) {
			loadItemsFromDemat(getExercice(), getCodeBudget(), TabsEnum.BILAN, items);
		} else {
			loadBalance(TabsEnum.BILAN.getType());
			loadItemsFromBalance(items);
		}
		if (isWithLoadAnt())
			loadItemsAnt(getBilan().getItemsAnt(), TabsEnum.BILAN);
		logger.debug("loadBilan : setDefaults");
		getBilan().setDefaults();
	}

	private void loadItemsFromDemat(Integer exercice, String codeBudget, TabsEnum type, List<DataItem> items) {
		Double net = null;
		Map<String, Double> dematData = loadDematData(exercice, codeBudget, type);
		if (dematData == null)
			return;
		for (DataItem item : items) {
			if (item == null)
				continue;
			net = dematData.get(item.getUid());
			if (net != null)
				item.setMontantDouble(net);
		}
	}

	private Map<String, Double> loadDematData(Integer exercice, String codeBudget, TabsEnum type) {
		Map<String, Double> dematData = null;
		switch (type) {
		case BILAN:
		case CR:
			dematData = service.getCfDematData(exercice, codeBudget, type.getType());
			break;
		case ABE:
			List<? extends GenericDemat> list = service.getDematList(type.getEntityType(), getCfExport());
			dematData = convertToMap(list);
			break;
		default:
			break;
		}
		return dematData;
	}

	public static Map<String, Double> convertToMap(List<? extends GenericDemat> dataList) {
		Map<String, Double> map = new HashMap<String, Double>();
		for (GenericDemat item : dataList) {
			map.put(item.getLibelle(), item.getMontant());
		}
		return map;
	}

	private Boolean getEtatCF(TabsEnum type) {

		return service.getEtatCF(getExercice(), getCodeBudget(), type.getType());
	}

	private void loadItemsAnt(List<DataItem> itemsAnt, TabsEnum type) {

		Map<String, Double> dematData = loadDematData(getExercice() - 1, getCodeBudget(), type);
		loadRefList(type, false);
		Double net = null;
		CfReference ref = null;
		for (DataItem item : itemsAnt) {
			if (item == null)
				continue;
			ref = getCfReference(type, item.getUid());
			if (ref == null)
				continue;
			net = dematData.get(ref.getConcerneAnt());
			if (net != null)
				item.setMontantDouble(net);
		}

	}

	private void loadCR() {
		TabsEnum CR = TabsEnum.CR;
		setCr(new CR());
		List<DataItem> items = getCr().getItems();
		if (isCrValide()) {
			loadItemsFromDemat(getExercice(), getCodeBudget(), CR, items);
		} else {
			loadBalance(CR.getType());
			loadItemsFromBalance(items);
		}
		if (isWithLoadAnt())
			loadItemsAnt(getCr().getItemsAnt(), CR);
		getCr().setDefaults();

	}

	private void loadItemsFromBalance(List<DataItem> items) {
		Double net = null;
		for (DataItem item : items) {
			if (item == null)
				continue;
			net = getBalance().get(item.getUid());
			if (net != null)
				item.setMontantDouble(net);
		}
	}

	private void loadBalance(String type) {
		setBalance(service.getBalance(getExercice(), getCodeBudget(), type));

	}

	public Edition getCf() {
		return getEdition();
	}

	public void setCf(Edition cf) {
		logger.debug("setCf: " + cf);
		setEdition(cf);
		;
	}

	@Override
	public void reset() {
		logger.debug("reset : start");
		setEdition(null);
		setParamBilanList(null);
		setRefBilanList(null);
		setParamCRList(null);
		setRefCRList(null);
		setRefTab6List(null);
		setBalance(null);
		setBilan(null);
		setCr(null);
		setCompteList(null);
		setBilanValide(null);
		setCrValide(null);
		setExportList(null);
		setCfExportList(null);
		setDepNivDest(null);
		setRecNivOrig(null);
		setSuiviByEnvelopData(null);
		setWithLoadPeriod(false);
		BudgetHelper.getEditionBean().reset();
		setPeriodeSuivi(null);
		setPeriodeSuiviList(null);
		setPeriodeList(null);
		setCodeExport(null);
		setCfVentilationList(null);

	}

	public void resetSuiviCF() {
		setCf(null);
		setCfRapproche(null);
		setDepNivDest(null);
		setRecNivOrig(null);

	}

	public List<CfParam> getParamBilanList() {
		return paramBilanList;
	}

	public void setParamBilanList(List<CfParam> paramBilanList) {
		this.paramBilanList = paramBilanList;
	}

	public List<CfReference> getRefBilanList() {
		return refBilanList;
	}

	private void loadRefBilanList(boolean onlyDetail) {
		setRefBilanList(service.loadReference(TabsEnum.BILAN.getType(), onlyDetail));
	}

	private void loadRefCRList(boolean onlyDetail) {
		setRefCRList(service.loadReference(TabsEnum.CR.getType(), onlyDetail));
	}

	private void loadRefTab6List(boolean onlyDetail) {
		setRefTab6List(service.loadReference(TabsEnum.TAB6.getType(), onlyDetail));
	}

	private void loadRefTab2List(boolean onlyDetail) {
		setRefTab2List(service.loadReference(TabsEnum.ABE.getType(), onlyDetail));
	}

	public void setRefBilanList(List<CfReference> refBilanList) {
		this.refBilanList = refBilanList;
	}

	public List<CfParam> getParamCRList() {
		return paramCRList;
	}

	public void setParamCRList(List<CfParam> paramCRList) {
		this.paramCRList = paramCRList;
	}

	public List<CfReference> getRefCRList() {
		return refCRList;
	}

	public void setRefCRList(List<CfReference> refCRList) {
		this.refCRList = refCRList;
	}

	public List<CfReference> getRefTab6List() {
		return refTab6List;
	}

	public void setRefTab6List(List<CfReference> refTab6List) {
		this.refTab6List = refTab6List;
	}

	public List<SimpleEntity> getCompteList() {
		return compteList;
	}

	public void setCompteList(List<SimpleEntity> compteList) {
		this.compteList = compteList;
	}

	public Map<String, Double> getBalance() {
		return balance;
	}

	public void setBalance(Map<String, Double> balance) {
		this.balance = balance;
	}

	public boolean isWithLoadAnt() {
		return withLoadAnt;
	}

	public void setWithLoadAnt(boolean withLoadAnt) {
		if (this.withLoadAnt != withLoadAnt) {
			this.withLoadAnt = withLoadAnt;
			reset();
		}
	}

	public Bilan getBilan() {
		return bilan;
	}

	public void setBilan(Bilan bilan) {
		this.bilan = bilan;
	}

	public CR getCr() {
		return cr;
	}

	public void setCr(CR cr) {
		this.cr = cr;
	}

	public boolean isBilanValide() {
		if (bilanValide == null)
			bilanValide = getEtatCF(TabsEnum.BILAN);
		return bilanValide;
	}

	public void setBilanValide(Boolean bilanValide) {
		this.bilanValide = bilanValide;
	}

	public boolean isCrValide() {
		if (crValide == null)
			crValide = getEtatCF(TabsEnum.CR);
		return crValide;
	}

	public void setCrValide(Boolean crValide) {
		this.crValide = crValide;
	}

	public List<CfReference> getRefTab2List() {
		return refTab2List;
	}

	public void setRefTab2List(List<CfReference> refTab2List) {
		this.refTab2List = refTab2List;
	}

	public void onCfTabChange(TabChangeEvent event) {

		logger.info("---- changement d'onglet CF ----");
		getEdition().getTab4().setDefaults();

	}

	public List<CfReference> getRefEFEList() {
		return refEFEList;
	}

	public void setRefEFEList(List<CfReference> refEFEList) {
		this.refEFEList = refEFEList;
	}

	public List<CfReference> getRefSPE1List() {
		return refSPE1List;
	}

	public void setRefSPE1List(List<CfReference> refSPE1List) {
		this.refSPE1List = refSPE1List;
	}

	public List<CfReference> getRefSPE2List() {
		return refSPE2List;
	}

	public void setRefSPE2List(List<CfReference> refSPE2List) {
		this.refSPE2List = refSPE2List;
	}

	@Override
	protected List<Export> filterPeriod(List<Export> allList) {
		logger.debug("filterPeriod: start");
		return allList;
	}

	public boolean isRequiredDataDone() {
		logger.debug("isRequiredDataDone: isPlTresEnabled={} , getPeriodeSuivi={}, getDateFin={}", isPlTresEnabled(),
				getPeriodeSuivi(), getDateFin());
		if (!super.isRequiredDataDone())
			return false;
		switch (getCurrentAction()) {
		case SUIVI_SF:
			return true;
		case SUIVI_CF:
			if (isPlTresEnabled() && getPeriodeSuivi() == null)
				return false;
			else if (!isPlTresEnabled() && getDateFin() == null)
				return false;
			break;
		default:
			if (getSelectedExport() == null)
				return false;
			break;
		}
		logger.debug("isRequiredDataDone: true");
		return true;
	}

	public List<Export> getCfExportList() {
		if ((cfExportList == null) && isCommonRequiredDone()) {
			List<Periode> periodList = BudgetHelper.getPlanTresorerieService().getPeriodeList(getExercice());
			Periode annuel = getPeriodePLT(periodList, PeriodeEnum.Annuel_def.getValue());
			if (annuel == null)
				return null;
			else {
				cfExportList = new ArrayList<>();
				Export cf = getAnnelDefExport();
				cfExportList.add(cf);
				setCodeExport(cf.getCode());
			}
		}
		return cfExportList;
	}

	public void setCfExportList(List<Export> cfExportList) {
		this.cfExportList = cfExportList;
	}

	@Override
	public void onRecNivDestChange() {
		logger.info("#### onRecNivDestChange ");
		loadTRO();

	}

	@Override
	public void onDepNivDestChange() {
		logger.info("#### onDepNivDestChange ");
		loadTDD();
	}

	private void loadTab6(Edition e, String dateFinStr) {

		service.loadTab6FromCtaBalance(e, dateFinStr);
		// VFR
		VFR vfr = e.getTab6().getVfr();
		PlanTresorerie<DetailLigneTresorerie> plan = e.getPlanTresorerie();
		if (plan != null) {
			vfr.getVariTresorie().setMontant(plan.getSoldeMois().getTotaleVariationAnnuelle());
			vfr.getNivTresorie().setMontant(plan.getSoldeCumule().getMontant12());

		}
		// refresh totaux
		e.getTab6().refreshTotaux();
	}

	public StreamedContent getXlsCfValid() {
		ExcelHandler excel = null;
		logger.debug("getXlsCfValid: start");
		StreamedContent returnStreamedContent = null;
		logger.debug("getXlsCfValid: cf=" + getEdition());
		if (isPlTresEnabled()) {
			excel = new ExcelHandler(ExcelModelEnum.DEMAT_CF, null);
		} else {
			excel = new ExcelHandler(ExcelModelEnum.DEMAT_CF_PLT_SAISIE, null);
		}

		returnStreamedContent = excel.getExcelFile();
		logger.debug("getXlsCfValid: end");
		return returnStreamedContent;

	}

	public Double[] getBcgAndNet(NatGrpEnum natGrp, int exercice) {

		String codeNatGrp = null;
		Double result[] = new Double[2];
		Double mntNb = null;
		Double mntNet = null;
		Double mntBcg = null;
		if (suiviByEnvelopData == null)
			suiviByEnvelopData = BudgetHelper.getCommonService().getSuiviEnvelopList(exercice);
		if (suiviByEnvelopData != null)
			for (Map<String, Object> data : suiviByEnvelopData) {
				codeNatGrp = (String) data.get(nat_grp);
				if (codeNatGrp == null)
					continue;
				if (natGrp != NatGrpEnum.parse(codeNatGrp.trim()))
					continue;
				mntNet = (Double) data.get(net);
				mntNb = (Double) data.get(nb);

				mntBcg = mntNb + mntNet;

				result[0] = mntBcg;
				result[1] = mntNet;
			}
		logger.debug("getBcgAndNet: {} - {}--> mntBcg={},mntNet={}  ", exercice, natGrp, result[0], result[1]);
		return result;

	}

	public List<Map<String, Object>> getSuiviByEnvelopData() {
		return suiviByEnvelopData;
	}

	public void setSuiviByEnvelopData(List<Map<String, Object>> suiviByEnvelopData) {
		this.suiviByEnvelopData = suiviByEnvelopData;
	}

	public Rapprochement getCfRapproche() {
		return cfRapproche;
	}

	public void setCfRapproche(Rapprochement cfRapproche) {
		this.cfRapproche = cfRapproche;
	}

	public boolean isWithLoadPeriod() {
		return withLoadPeriod;
	}

	public void setWithLoadPeriod(boolean withLoadPeriod) {
		this.withLoadPeriod = withLoadPeriod;
	}

	public Integer getPeriodeSuivi() {
		return periodeSuivi;
	}

	public void setPeriodeSuivi(Integer periodeSuivi) {
		this.periodeSuivi = periodeSuivi;
		setDateFin(calculateDateFin(periodeSuivi));
	}

	public List<PeriodeEnum> getPeriodeSuiviList() {

		if ((periodeSuiviList == null) && isCommonRequiredDone()) {
			periodeSuiviList = new ArrayList<>();
			for (Periode p : getPeriodeList()) {
				if (p.getEtat() != EtatPeriodeEnum.Cloture)
					continue;
				periodeSuiviList.add(PeriodeEnum.parse(p.getNumero()));
			}
			if (periodeSuiviList.size() == 1)
				setPeriodeSuivi(periodeSuiviList.get(0).getValue());
		}
		return periodeSuiviList;
	}

	public List<Periode> getPeriodeList() {
		if ((periodeList == null) && isCommonRequiredDone()) {
			periodeList = BudgetHelper.getPlanTresorerieService().getPeriodeList(getExercice());
		}

		return periodeList;
	}

	public void setPeriodeList(List<Periode> periodeList) {
		this.periodeList = periodeList;
	}

	public void setPeriodeSuiviList(List<PeriodeEnum> periodeSuiviList) {
		this.periodeSuiviList = periodeSuiviList;
	}

	public void resetDynamicList() {

	}

	public Date calculateDateFin(Integer periodeSuivi) {
		if (periodeSuivi == null)
			return null;
		if (!isCommonRequiredDone())
			return null;
		if (isPlTresEnabled()) {
			PeriodeEnum p = PeriodeEnum.parse(periodeSuivi);
			if (p == null)
				return null;
			String codeExport = p.getCodeInfoCentre();
			Export export = getExportByCode(codeExport);
			return getArretPeriode(export, null);
		}
		return null;
	}

	public Date getDateFin() {
		if (!isCommonRequiredDone())
			return null;

		return dateFin;
	}

	public void setDateFin(Date dateFin) {
		logger.debug("setDateFin: {}", dateFin);
		this.dateFin = dateFin;
	}

	public boolean isPlTresEnabled() {
		return plTresEnabled;
	}

	public void setPlTresEnabled(boolean plTresEnabled) {
		this.plTresEnabled = plTresEnabled;
		if (!plTresEnabled) {
			setPeriodeSuivi(null);
		}
		setDateFin(null);
	}

	public List<VentilationImput> getCfVentilationList() {
		return cfVentilationList;
	}

	public void setCfVentilationList(List<VentilationImput> cfVentilationList) {
		this.cfVentilationList = cfVentilationList;
	}
	public StreamedContent getXlsDematCFSaisie() {
		ExcelHandler excel = null;
		logger.debug("getXlsDematCFSaisie: start");
		StreamedContent returnStreamedContent = null;
	

			excel = new ExcelHandler(ExcelModelEnum.DEMAT_CF_SAISIE, null);


		returnStreamedContent = excel.getExcelFile();
		logger.debug("getXlsDematCFSaisie: end");
		return returnStreamedContent;

	}
	public StreamedContent getXlsSF() {
		ExcelHandler excel = null;
		logger.debug("getXlsSF: start");
		StreamedContent returnStreamedContent = null;
	
//		if (isPlTresEnabled()) {
//			excel = new ExcelHandler(ExcelModelEnum.DEMAT_CF_SAISIE, null);
//		} else {
			excel = new ExcelHandler(ExcelModelEnum.SUIVI_IMPUT_SF, null);
//		}

		returnStreamedContent = excel.getExcelFile();
		logger.debug("getXlsSF: end");
		return returnStreamedContent;

	}
	/***
	 * Permet d'enregistrer les données valideées par la saisie CF, afin de générer les fichers demat 06-annuel defiitif
	 */
	public void saveCfDataForDemat() {
		logger.debug("saveCfDataForDemat: start");
		super.saveDemat(null);// pour code export encours
		super.saveDemat(getCfExport());// pour l'export 07
		logger.debug("saveCfDataForDemat: end");
	}
	public String generateInfocentre() {

		String returnString = BudgetHelper.getNavigationBean().goToInfocentreOutcome();
		
		return returnString;
	}


}
