package fr.symphonie.budget.ui.beans.pluri;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;

import org.primefaces.event.SelectEvent;
import org.primefaces.model.StreamedContent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.symphonie.budget.core.model.plt.DetailLigneTresorerie;
import fr.symphonie.budget.core.model.plt.Ecriture;
import fr.symphonie.budget.core.model.plt.EncDecEnum;
import fr.symphonie.budget.core.model.plt.EtatPeriodeEnum;
import fr.symphonie.budget.core.model.plt.GenericLigneTresorerie;
import fr.symphonie.budget.core.model.plt.GlobalEnum;
import fr.symphonie.budget.core.model.plt.LigneTresorerie;
import fr.symphonie.budget.core.model.plt.ParamCompteLtr;
import fr.symphonie.budget.core.model.plt.Periode;
import fr.symphonie.budget.core.model.plt.PeriodeEnum;
import fr.symphonie.budget.core.model.plt.PlanTresorerie;
import fr.symphonie.budget.core.model.plt.VentillationCO;
import fr.symphonie.budget.core.service.IPlanTresorerieService;
import fr.symphonie.budget.ui.beans.GenericBean;
import fr.symphonie.budget.ui.beans.GestionTiersBean;
import fr.symphonie.budget.ui.excel.ExcelHandler;
import fr.symphonie.budget.ui.excel.ExcelModelEnum;
import fr.symphonie.common.util.BudgetHelper;
import fr.symphonie.common.util.MsgEntry;
import fr.symphonie.cpwin.model.Tiers;
import fr.symphonie.cpwin.model.Visa;
import fr.symphonie.cpwin.model.VisaEnum;
import fr.symphonie.cpwin.model.cpt.DebitCreditEnum;
import fr.symphonie.exception.MissingConfiguration;
import fr.symphonie.util.HandlerJSFMessage;
import fr.symphonie.util.Helper;
import fr.symphonie.util.model.SimpleEntity;
import fr.symphonie.util.model.Trace;

@ManagedBean(name = "pltBeanV1")
@SessionScoped
public class PlanTresorerieBeanV1 extends GenericBean implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -368680434184624470L;
	private static final Logger logger = LoggerFactory.getLogger(PlanTresorerieBean.class);
	@ManagedProperty(value = "#{pltService}")
	private IPlanTresorerieService service;
	
	private DetailLigneTresorerie selectedLigne;
	private Ecriture selectedEcriture;
	private ParamCompteLtr selectedParam;
	
	private Integer periode = null;
	private PlanTresorerie<DetailLigneTresorerie> plan;
	private List<Periode> periodeList;
	private List<PeriodeEnum> simulationPeriodeList;
	private List<PeriodeEnum> ajustementPeriodeList;
	private List<PeriodeEnum> concilPeriodeList;
	
	private PlanTresorerie<VentillationCO> planVentil;

	private boolean simulationExecuted;
	private boolean validateSimulationExecuted;
	private boolean ajustementExecuted;
	private EncDecEnum typEcrit;
	
	private SimpleEntity compte= new SimpleEntity();

	private Integer  ltrDep;
	private Integer  ltrRec;
	private List<SimpleEntity> paramCompteList;
	private String codeCompte;
	private List<LigneTresorerie> lignesDecais;
	private List<LigneTresorerie> lignesEncais;	
	private List<ParamCompteLtr> pltParamList = new ArrayList<>();
	private boolean updateMode;
	public void reset() {
		resetDynamicList();
		resetEnteteList();
		resetParamCompte();
	}

	public void resetDynamicList() {
		setPlan(null);
		setPlanVentil(null);
		setSelectedLigne(null);
		setSelectedEcriture(null);
		setSelectedParam(null);
		setSimulationExecuted(false);
		setAjustementExecuted(false);
		setValidateSimulationExecuted(false);
	}

	public void resetEnteteList() {
		setPeriode(null);
		setPeriodeList(null);
		setSimulationPeriodeList(null);
		setAjustementPeriodeList(null);
		setConcilPeriodeList(null);
		
	}

	public void search() {
		resetDynamicList();
		try {
			plan = loadPlanTresorerie(getExercice(), getPeriode());
		} 
	catch (Exception e) {
		HandlerJSFMessage.addErrorMessage(getErrorMessageFromException(MsgEntry.FAILED, e));
		e.printStackTrace();
	}

	}

	public void searchInitialisation() {
		logger.debug("searchInitialisation - start");
		resetDynamicList();
		Periode janvier = getPeriode(getPeriodeValue());
		try{
		if (janvier == null) {
			logger.debug("searchInitialisation - From plan ventilationCO ");
			// TODO initialisation from plan ventilation
			PlanTresorerie<VentillationCO> pVentil = loadPlan(getExercice(), null, VentillationCO.class);
			pVentil.setPeriode(PeriodeEnum.Janvier.getValue());
			this.plan = PlanTresorerie.convert(pVentil);

		} else if (janvier.getEtat() == EtatPeriodeEnum.Previsionnel) {
			logger.debug("searchInitialisation - From plan Previsionnel ");
			this.plan = loadPlan(getExercice(), getPeriode(), DetailLigneTresorerie.class);
		} else {
			HandlerJSFMessage.addError(MsgEntry.PREVISION_REALISEE);
			return;
		}

		loadCreditOuvert(plan);
		loadSoldeInitial(plan);
		plan.calculateTotaux();
		plan.sort();
		plan.prepare();
		
	} catch (Exception e) {
		HandlerJSFMessage.addErrorMessage(getErrorMessageFromException(MsgEntry.FAILED, e));
		e.printStackTrace();

	}
		logger.debug("searchInitialisation - end");
	}

	public void searchAjustement() {
		resetDynamicList();
		try {
			this.plan = loadPlan(getExercice(), getPeriode(), DetailLigneTresorerie.class);
			loadCreditOuvert(plan);
			this.plan.sort();
		} catch (Exception e) {
			HandlerJSFMessage.addErrorMessage(getErrorMessageFromException(MsgEntry.FAILED, e));
			e.printStackTrace();
		}

	}

	private void loadCreditOuvert(PlanTresorerie<DetailLigneTresorerie> p) {
		Map<Integer, Double> coList = service.getCoTresorerieVentille(p.getExercice(), p.getPeriode());
		logger.debug("loadCreditOuvert: coList.size={}", coList.size());
		Double co = null;
		BigDecimal credit = null;
		for (EncDecEnum typeOp : EncDecEnum.values()) {

			for (DetailLigneTresorerie d : p.getDetailList(typeOp)) {
				if (d.isGlobal())
					continue;
				co = coList.get(d.getNumero());
				if (co == null)
					co = new Double(0);
				credit = new BigDecimal(co).setScale(2, BigDecimal.ROUND_HALF_UP);
				d.setCreditOuvert(credit);
			}
		}
	}

	public void searchVentil() {
		resetDynamicList();
		try {
			this.planVentil = loadPlan(getExercice(), null, VentillationCO.class);
			this.planVentil.sort();
			Map<String, Double> bpByNatGrp = loadBudgetPrevisionnel(planVentil.getExercice());
			planVentil.prepareVentil(bpByNatGrp);
		} 
		 catch (Exception e) {
			HandlerJSFMessage.addErrorMessage(getErrorMessageFromException(MsgEntry.FAILED, e));
			e.printStackTrace();
		}
		

	}

	private Map<String, Double> loadBudgetPrevisionnel(Integer exercice) {
		return service.getBudgetPrevisionnel(exercice);
	}

	private <T extends GenericLigneTresorerie> PlanTresorerie<T> loadPlan(Integer exercice, Integer periode,
			Class<T> entityType) throws MissingConfiguration {
		logger.debug("loadPlan : {}/{}: {}", exercice, periode, entityType);
		PlanTresorerie<T> p = BudgetHelper.getInstanceOfPlanTresorerie(entityType);
		p.setExercice(exercice);
		p.setPeriode(periode);
		p.setNomConfig(getNomConfig());

		p.setEncaissements(loadLignesTresorerie(exercice, periode, EncDecEnum.Encaissement, entityType));
		p.setDecaissements(loadLignesTresorerie(exercice, periode, EncDecEnum.Decaissement, entityType));
		addGlobalLignes(p, EncDecEnum.Encaissement, entityType);
		addGlobalLignes(p, EncDecEnum.Decaissement, entityType);

		return p;
	}

	public PlanTresorerie<DetailLigneTresorerie> loadPlanTresorerie(Integer exercice, Integer periode) throws MissingConfiguration {

		PlanTresorerie<DetailLigneTresorerie> p = loadPlan(exercice, periode, DetailLigneTresorerie.class);
		loadSoldeInitial(p);
		p.calculateTotaux();
		p.sort();
		p.prepare();
		return p;
	}

	private void loadSoldeInitial(PlanTresorerie<DetailLigneTresorerie> p) {
		p.setSoldeInitial(new DetailLigneTresorerie());
		double solde01 = service.getSoldeInitialJanvier(p.getExercice());
		p.getSoldeInitial().setMontant01(new BigDecimal(solde01));

	}

	private <T extends GenericLigneTresorerie> List<T> loadLignesTresorerie(Integer exercice, Integer periode,
			EncDecEnum type, Class<T> entityType) {
		logger.debug("loadLignesTresorerie : {}/{}: {}", exercice, periode, entityType);
		return service.getLignesTresorerie(exercice, periode, type, entityType);

	}

	private <T extends GenericLigneTresorerie> void addGlobalLignes(PlanTresorerie<T> p, EncDecEnum type,
			Class<T> entityType) {
		logger.debug("addGlobalLignes :  {}", entityType);
		List<LigneTresorerie> globalList = service.getLigneTresorerie(p.getExercice(), GlobalEnum.Global, type);
		logger.debug("addGlobalLignes : globalList {}", globalList);
		T global = null;
		try {
			for (LigneTresorerie ligne : globalList) {
				global = entityType.newInstance();
				global.setLigne(ligne);
				p.createGlobale(global);
			}
		} catch (InstantiationException | IllegalAccessException e) {
			e.printStackTrace();
		}
	}

	public void simuler() {		
		Integer periode = getPeriode();
		Integer exercice = getExercice();
		logger.debug("simuler: periode {}/{} ",periode,exercice);
		try{
		//prepareSimulationData(exercice,periode);
		loadRealisationData(EncDecEnum.Encaissement,exercice,periode);
		loadRealisationData(EncDecEnum.Decaissement,exercice,periode);
		//Pour test
		//loadSimulationData(exercice,periode);
		plan.calculateTotaux();
		setSimulationExecuted(true);
		}
		catch (Exception e) {
			HandlerJSFMessage.addErrorMessage(getErrorMessageFromException(MsgEntry.FAILED, e));
			e.printStackTrace();
		}
		
	}
//	private void prepareSimulationData(Integer exercice,Integer periode) {
//		BudgetHelper.refreshRecetteRecouveData(exercice);
//		service.refreshSimulationData(exercice,periode);
//	}

	private void loadRealisationData(EncDecEnum type, Integer exercice,Integer periode) {
		List<Ecriture> ecritures = null;
		
		for (DetailLigneTresorerie d : getPlan().getDetailList(type)) {
			if (d.isGlobal())
				continue;
			ecritures = service.getEcritures(exercice, periode, d.getNumero());
			d.setEcritureList(ecritures);
			d.calculateRealisation();
		}

	}

	private void cloturer() {
		logger.debug("cloturer période: {}/{}", getExercice(), getPeriode());
		service.cloturerPeriode(getExercice(), getPeriode(), Helper.getUserName());

	}

	public void validateSimulation() {

		logger.debug("Valider simulation: Début");
		try {
			cloturer();
			createNextPeriode(getPeriode());
			
			setSimulationPeriodeList(null);
			setPeriodeList(null);
			setPeriode(null);
			setValidateSimulationExecuted(true);
			HandlerJSFMessage.addInfo(MsgEntry.SUCCES);
		} catch (Exception e) {
			logger.error("cloturer: error: ", e);
			HandlerJSFMessage.addErrorMessage(getErrorMessageFromException(MsgEntry.FAILED, e));
			e.printStackTrace();
		}

		logger.debug("Valider simulation: Fin");
	}

	private void createNextPeriode(Integer currentPeriode) {
		List<DetailLigneTresorerie> nextPeriodList = new ArrayList<>();
		PeriodeEnum periode=PeriodeEnum.parse(currentPeriode);
		// si december créer deux période 13 et 14
		Integer maxPeriod =(periode==PeriodeEnum.December)? currentPeriode + 2:currentPeriode + 1;
		Trace trace = Helper.createTrace();
		DetailLigneTresorerie copy=null;
		EtatPeriodeEnum etat=null;
		for(Integer nextPeriod=currentPeriode + 1;nextPeriod<=maxPeriod;nextPeriod++)
		{
		etat=(nextPeriod.equals(PeriodeEnum.Annuel_prov.getValue()))?EtatPeriodeEnum.Cloture:EtatPeriodeEnum.Previsionnel;
		for (EncDecEnum typeOp : EncDecEnum.values()) {

			for (DetailLigneTresorerie d : plan.getDetailList(typeOp)) {
				if (d.isGlobal())
					continue;
				try {
					copy=d.clone();
				} catch (CloneNotSupportedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				copy.setPeriode(nextPeriod);
				copy.setEtat(etat);

				copy.setTrace(trace);
				nextPeriodList.add(copy);
			}

		}
		}
		logger.debug("createNextPeriode: updatedList: {} ", nextPeriodList.size());

		service.createNextPeriode( nextPeriodList);
		

	}

	public boolean isCommonRequiredDone() {

		return (getExercice() != null) && (getExercice() != 0);
	}

	public boolean isRequiredDataDone() {
		switch (getCurrentAction()) {
		case SIMUL_TRESORERIE:
		case AJUST_TRESORERIE:
		case CONSULT_TRESORERIE:
		case CONSULT_TRESORERIE_2024:
		case INIT_TRESORERIE_2024:case CONCIL_TRESORERIE:
			return (isCommonRequiredDone()) && (getPeriode() != null);

		case VENTIL_TRESORERIE_2024:
			return (isCommonRequiredDone());			
		case PARAM_TRESORERIE:
			return (isCommonRequiredDone());

		default:
			break;
		}
		return false;
	}

	public Integer getPeriode() {
		return periode;
	}

	public void setPeriode(Integer periode) {
		this.periode = periode;
	}

	@Override
	public void validate() {

	}

	public void setService(IPlanTresorerieService service) {
		this.service = service;
	}

	@Override
	public void onExerciceChange() {
		
		reset();
	}

	public List<DetailLigneTresorerie> getEncaissements() {
		return (List<DetailLigneTresorerie>) (getPlan() != null ? getPlan().getEncaissements() : null);
	}

	public List<DetailLigneTresorerie> getDecaissements() {
		return (List<DetailLigneTresorerie>) (getPlan() != null ? getPlan().getDecaissements() : null);
	}

	public PlanTresorerie<DetailLigneTresorerie> getPlan() {
		return plan;
	}

	public void setPlan(PlanTresorerie<DetailLigneTresorerie> plan) {
		this.plan = plan;
	}

	public boolean isSimulationExecuted() {
		return simulationExecuted;
	}

	public void setSimulationExecuted(boolean simulationExecuted) {
		this.simulationExecuted = simulationExecuted;
	}

	public DetailLigneTresorerie getSelectedLigne() {
		return selectedLigne;
	}

	public void setSelectedLigne(DetailLigneTresorerie selectedLigne) {
		this.selectedLigne = selectedLigne;
		loadLibelles(selectedLigne);
	}

	public int getPeriodeValue() {
		if (getPeriode() == null)
			return 0;
		return getPeriode().intValue();
	}

	public void openEcrituresDialog() {
		DialogHelper.openEcrituresView();
	}

	public void closeEcrituresDialog() {
		DialogHelper.closeDialog(null);
		simuler();
	}

	public void validateEcriture() {
		logger.debug("Valider ecriture: Début");
		DetailLigneTresorerie ltr1 = null, ltr2 = null;
		Ecriture ecr = null;
		try {
			ecr = getSelectedEcriture();
			ltr1 = getPlan().getDetailLigne(ecr.getNumeroTemp1());
			ltr2 = getPlan().getDetailLigne(ecr.getNumeroTemp2());
			ecr.setLtr1(ltr1);
			ecr.setLtr2(ltr2);
			if (!controlerEricture())
				return;

			ecr.prepareValidate();

			service.updateEcriture(getSelectedEcriture());
			HandlerJSFMessage.addInfo(MsgEntry.SUCCES);
		} catch (Exception e) {
			logger.error("Valider ecriture: error: ", e);
			HandlerJSFMessage.addErrorMessageFromException(MsgEntry.FAILED, e);
		}

		logger.debug("Valider ecriture: Fin");
	}

	private boolean controlerEricture() {
		Ecriture ecr = getSelectedEcriture();
		String error = null;
		if (ecr == null)
			return true;
		DebitCreditEnum sens = ecr.getSens();
		BigDecimal montant = new BigDecimal(ecr.getMontant());
		BigDecimal mtLtr1 = new BigDecimal(ecr.getMontantTemp1());
		BigDecimal mtLtr2 = new BigDecimal(ecr.getMontantTemp2());
		String errorParam = null;
		if (ecr.getMontantTemp1() != 0)
			if (((ecr.getLtr1() == null) || (!ecr.getLtr1().isSameSens(sens)) && (mtLtr1.signum() == montant.signum()))
					|| ((ecr.getLtr1().isSameSens(sens)) && (mtLtr1.signum() != montant.signum()))) {
				errorParam = "Mt_Ltr1";

			}

		if (ecr.getMontantTemp2() != 0)
			if (((ecr.getLtr2() == null) || (!ecr.getLtr2().isSameSens(sens)) && (mtLtr2.signum() == montant.signum()))
					|| ((ecr.getLtr2().isSameSens(sens)) && (mtLtr2.signum() != montant.signum()))) {
				errorParam = "Mt_Ltr2";

			}

		if (errorParam != null) {
			error = HandlerJSFMessage.getErrorMessage(MsgEntry.MNT_ECRITURE);
			error = HandlerJSFMessage.formatMessage(error, new String[] { errorParam });
			HandlerJSFMessage.addErrorMessage(error);
			return false;
		}

		if (!getSelectedEcriture().isTotalValide()) {
			HandlerJSFMessage.addError(MsgEntry.TOTAL_VENTILE_ECRITURE_ERROR);
			return false;
		}

		return true;

	}

	public boolean isActiveSimulate() {
		return (!isSimulationExecuted()) && (!isValidateSimulationExecuted());
	}

	public boolean isActiveValidateSimulate() {
		return isSimulationExecuted() && (!isValidateSimulationExecuted());
	}

	public List<SimpleEntity> getAllNoLignes() {
		List<SimpleEntity> result = getPlan().getNoLignesEncass();
		result.addAll(getPlan().getNoLignesDecaiss());

		return result;
	}

	public Ecriture getSelectedEcriture() {
		return selectedEcriture;
	}

	public void setSelectedEcriture(Ecriture selectedEcriture) {
		if (selectedEcriture != null) {
			selectedEcriture.setMontantTemp1(selectedEcriture.getMontantLigne1());
			selectedEcriture.setMontantTemp2(selectedEcriture.getMontantLigne2());
			selectedEcriture.setNumeroTemp1(selectedEcriture.getNumeroLigne1());
			selectedEcriture.setNumeroTemp2(selectedEcriture.getNumeroLigne2());
		}
		this.selectedEcriture = selectedEcriture;

	}

	public List<PeriodeEnum> getSimulationPeriodeList() {
		if ((simulationPeriodeList == null) && isCommonRequiredDone()) {
			simulationPeriodeList = new ArrayList<>();

			for (Periode p : getPeriodeList()) {
				if (p.getEtat() != EtatPeriodeEnum.Valide)
					continue;
				simulationPeriodeList.add(PeriodeEnum.parse(p.getNumero()));
			}
			if (simulationPeriodeList.size() == 1)
				setPeriode(simulationPeriodeList.get(0).getValue());
		}
		return simulationPeriodeList;
	}

	public void setSimulationPeriodeList(List<PeriodeEnum> simulationPeriodeList) {
		this.simulationPeriodeList = simulationPeriodeList;
	}

	public List<Periode> getPeriodeList() {
		if ((periodeList == null) && isCommonRequiredDone()) {
			periodeList = getPeriodeList(getExercice());
		}

		return periodeList;
	}
	public List<Periode> getPeriodeList(Integer exercice) {

		return service.getPeriodeList(exercice);
	}

	public void setPeriodeList(List<Periode> periodeList) {
		this.periodeList = periodeList;
	}

	public boolean isValidateSimulationExecuted() {
		return validateSimulationExecuted;
	}

	public void setValidateSimulationExecuted(boolean validateSimulationExecuted) {
		this.validateSimulationExecuted = validateSimulationExecuted;
	}

	public boolean isUpdateEcritureAutorized() {
		return (isSimulationExecuted() && (!isValidateSimulationExecuted()));
	}

	public PlanTresorerie<VentillationCO> getPlanVentil() {
		return planVentil;
	}

	public void setPlanVentil(PlanTresorerie<VentillationCO> planVentil) {
		this.planVentil = planVentil;
	}

	public boolean periodEditable(Integer index) {
		Periode period = null;
		int position = 0;
		List<Periode> periodeList = getPeriodeList();
		if (periodeList == null)
			return false;
		if (periodeList.isEmpty())
			return false;
		try {
			position = periodeList.indexOf(new Periode(index, null));
			if (position == -1) {

				return true;
			}
			period = periodeList.get(position);
			switch (getCurrentAction()) {
			case AJUST_TRESORERIE:
				return (period.getEtat() == EtatPeriodeEnum.Previsionnel);
			case VENTIL_TRESORERIE:
				return (period.getEtat() != EtatPeriodeEnum.Cloture);
			case INIT_TRESORERIE:case CONCIL_TRESORERIE:
				return true;
			default:
				return false;
			}
		} catch (NullPointerException e) {
			return false;
		}
	}

	public List<PeriodeEnum> getAjustementPeriodeList() {
		if ((ajustementPeriodeList == null) && isCommonRequiredDone()) {
			ajustementPeriodeList = new ArrayList<>();

			for (Periode p : getPeriodeList()) {
				if (p.getEtat() != EtatPeriodeEnum.Previsionnel)
					continue;
				ajustementPeriodeList.add(PeriodeEnum.parse(p.getNumero()));
			}
			if (ajustementPeriodeList.size() == 1)
				setPeriode(ajustementPeriodeList.get(0).getValue());

		}
		return ajustementPeriodeList;
	}

	public void saveAjustementData() {
		logger.debug("saveAjustementData: start ");
		savePlanTresorerie(plan);
		logger.debug("saveAjustementData: End ");
	}

	public void validateAjustement() {
		List<DetailLigneTresorerie> updatedList = new ArrayList<>();

		Trace trace = Helper.createTrace();
		Visa visa = new Visa();
		visa.setVisa(VisaEnum.Accepte);
		visa.setAuteur(trace.getAuteurMaj());
		visa.setDate(trace.getDateMaj());
		validatePlanTresorerie(plan, EtatPeriodeEnum.Valide, visa, null);
		
		try {
			validatePlanTresorerie(plan, EtatPeriodeEnum.Valide, visa, null);
			setAjustementPeriodeList(null);
			setPeriodeList(null);
			setPeriode(null);
			setAjustementExecuted(true);
			HandlerJSFMessage.addInfo(MsgEntry.SUCCES);
		} catch (Exception e) {
			logger.error("validateAjustement: error: ", e);
			HandlerJSFMessage.addErrorMessage(getErrorMessageFromException(MsgEntry.FAILED, e));
			e.printStackTrace();
		}
		logger.debug("validateAjustement: updatedList: {} ", updatedList.size());
	}

	public boolean isTotalEcartZero() {
		return getPlan().getTotaleEcart().compareTo(BigDecimal.ZERO) == 0;
	}

	public void saveVentillation() {
		logger.debug("saveVentillation: start ");
		savePlanTresorerie(planVentil);
		
		logger.debug("saveVentillation: Fin ");
	}

	public boolean isAjustementExecuted() {
		return ajustementExecuted;
	}

	public void setAjustementExecuted(boolean ajustementExecuted) {
		this.ajustementExecuted = ajustementExecuted;
	}

	public void setAjustementPeriodeList(List<PeriodeEnum> ajustementPeriodeList) {
		this.ajustementPeriodeList = ajustementPeriodeList;
	}

	public StreamedContent getXlsTresorerie() {
		logger.debug("getXlsTresorerie: start ");
		StreamedContent returnStreamedContent = null;
		ExcelHandler excel = new ExcelHandler(ExcelModelEnum.PlAN_TRESORERIE, null);
		returnStreamedContent = excel.getExcelFile();

		return returnStreamedContent;

	}

	public boolean isJanvierClos() {
		return isPeriodeClos(01);
	}

	public boolean isPeriodeClos(int index) {
		Periode periode = getPeriode(index);
		if (periode == null)
			return false;
		return EtatPeriodeEnum.Cloture == periode.getEtat();
	}

	private Periode getPeriode(int index) {
		return getPeriodePLT(getPeriodeList(), index);
	}
	public Periode getPeriode(Integer exercice,int index) {
		return getPeriodePLT(getPeriodeList(exercice), index);
	}

	public void openEcritureDialog() {
		DialogHelper.openEcritureView();
	}

	public void closeEcritureDialog() {
		DialogHelper.closeDialog(null);
	}

	private void loadLibelles(DetailLigneTresorerie ligne) {
		if (ligne == null)
			return;
		if (ligne.getEcritureList() == null)
			return;
		for (Ecriture ecriture : ligne.getEcritureList()) {
			loadLibelles(ecriture);
		}

	}

	private void loadLibelles(Ecriture ecriture) {
		if (ecriture == null)
			return;
		ecriture.updateLibLigne1(getAllNoLignes());
		ecriture.updateLibLigne2(getAllNoLignes());
	}

	public void searchConsultation() {
		resetDynamicList();
		try {
			plan = loadPlanTresorerie(getExercice(), getPeriode());
			loadCreditOuvert(plan);
			this.plan.sort();
		}  catch (Exception e) {
			HandlerJSFMessage.addErrorMessage(getErrorMessageFromException(MsgEntry.FAILED, e));
			e.printStackTrace();
		}
		
	}

	public List<Periode> getPeriodeConsultList() {
		return getPeriodeList();
	}

	public void setMontantMonth(DetailLigneTresorerie lt, int index) {

		BigDecimal result =null;
		switch (getCurrentAction()) {
		case CONCIL_TRESORERIE:
			if(lt.getLigne().getTypeOp()==EncDecEnum.Encaissement)
			result = lt.getMontant(index).add(getPlan().getVarSoldeMois().getMontant(index).negate());
			else
				result = lt.getMontant(index).add(getPlan().getVarSoldeMois().getMontant(index));
			
			lt.setMontant(index, result);
			getPlan().calculateVarSoldeMois();
			
			break;
			
			default:
				result = (lt.getEcart()).add(lt.getMontant(index));
				lt.setMontant(index, result);
		}
		logger.debug("setMontantMonth : valeur ajusté= {}",result);
		
		
	}

	public List<PeriodeEnum> getInitialPeriodeList() {
		List<PeriodeEnum> initialPeriodeList = new ArrayList<>();
		initialPeriodeList.add(PeriodeEnum.Janvier);
		setPeriode(initialPeriodeList.get(0).getValue());

		return initialPeriodeList;
	}
	
	public List<PeriodeEnum> getConcilPeriodeList() {
		if ((concilPeriodeList == null) && isCommonRequiredDone()) {
			concilPeriodeList = new ArrayList<>();
		Periode annuel=getPeriode(PeriodeEnum.Annuel_def.getValue());
		
		if(annuel!=null)
		{
				concilPeriodeList.add(PeriodeEnum.Annuel_def);
				setPeriode(annuel.getNumero());
		
		}
		}

		return concilPeriodeList;
	}
	public void setConcilPeriodeList(List<PeriodeEnum> concilPeriodeList) {
		this.concilPeriodeList = concilPeriodeList;
	}


	public void validateInitialisation() {
		
		try {
			validatePlanTresorerie(plan, EtatPeriodeEnum.Previsionnel, null, null);

			HandlerJSFMessage.addInfo(MsgEntry.SUCCES);
		} catch (Exception e) {
			logger.error("validateInitialisation: error: ", e);
			HandlerJSFMessage.addErrorMessage(getErrorMessageFromException(MsgEntry.FAILED, e));
			e.printStackTrace();
		}
		logger.debug("validateInitialisation: End ");
	}

	public List<ParamCompteLtr> getPltParamList() {
		return pltParamList;
	}

	public void setPltParamList(List<ParamCompteLtr> pltParamList) {
		this.pltParamList = pltParamList;
	}

	public void searchParam() {
		try {
			logger.debug("searchParam début : ",getCodeCompte());
			setPltParamList(service.getPltParamList(getExercice(),BudgetHelper.prepareSearchKey(getCodeCompte())));
			for(ParamCompteLtr p:getPltParamList()){
				p.setTiers(BudgetHelper.getTiersService().getTiersWoIban(p.getCodeTiers()));
			}
			logger.debug("searchParam PltParamList.size= {} : ",getPltParamList().size());

		} catch (Exception e) {
			HandlerJSFMessage.addErrorMessage(getErrorMessageFromException(MsgEntry.FAILED, e));
			e.printStackTrace();
		}
	}



	public void editParamCompte() {
		setUpdateMode(true);
		
		logger.debug("editParamCompte : {}",getSelectedParam());
		
		DialogHelper.openParamView();
		logger.debug("editParamCompte : Fin");
	}

	

	public void closeParamDialog() {
		DialogHelper.closeDialog(null);
	}

	public ParamCompteLtr getSelectedParam() {
		return selectedParam;
	}

	public void setSelectedParam(ParamCompteLtr selectedParam) {
		this.selectedParam = selectedParam;
		logger.debug("setSelectedParam: {}",selectedParam);
		if(getSelectedParam()==null)return;
		
		
		if(getSelectedParam().getNoLtrRec()!=null)
		setTypEcrit(EncDecEnum.Encaissement);
		else if(getSelectedParam().getNoLtrDep()!=null)
			setTypEcrit(EncDecEnum.Decaissement);
			
		getTiersBean().setTiers(getSelectedParam().getTiers());
	}

	public EncDecEnum getTypEcrit() {
		return typEcrit;
	}

	public void setTypEcrit(EncDecEnum typEcrit) {
		if((typEcrit!=null)&&(typEcrit!=this.typEcrit)){
			if(getSelectedParam()!=null){
				if(typEcrit==EncDecEnum.Decaissement)getSelectedParam().setNoLtrRec(null);
				if(typEcrit==EncDecEnum.Encaissement)getSelectedParam().setNoLtrDep(null);
			}
		}
		this.typEcrit = typEcrit;
	}

	public EncDecEnum[] getEcritures() {
		return EncDecEnum.values();
	}

	public boolean isEcritAutorized() {
		try {

			switch (getTypEcrit()) {
			case Encaissement:
				return true;
			case Decaissement:

				return false;
			default:
				break;
			}
			return false;
		} catch (Exception e) {
			return false;
		}
	}

	public void onTiersSelect(SelectEvent event) {		
	}

	public GestionTiersBean getTiersBean() {
		return BudgetHelper.getGestionTiersBean();
	}

	public Tiers getTiersForSearch() {
		Tiers tiersForSearch=getTiersBean().getTiers();
		if(tiersForSearch!=null)
		logger.debug("getTiersForSearch: "+tiersForSearch.getDescription());
		return tiersForSearch;
	}

	public void setTiersForSearch(Tiers tiersForSearch) {
		if(tiersForSearch!=null)logger.debug("setTiersForSearch: "+tiersForSearch.getDescription());
		getTiersBean().setTiers(tiersForSearch);
		
	}
	public boolean isComptAutorized() {
		
			return true;
		
	}
	public void goToAddParam(){
		if (logger.isDebugEnabled()) {
			logger.debug("goToAddParam() - start"); //$NON-NLS-1$
		}
		setTypEcrit(null);
		setTiersForSearch(null);		
		setUpdateMode(false);
		ParamCompteLtr compt = new ParamCompteLtr();
		compt.setExercice(getExercice());
		compt.setTrace(Helper.createTrace());
		setSelectedParam(compt);
		DialogHelper.openParamView();
		
		if (logger.isDebugEnabled()) {
			logger.debug("goToAddParam() - end"); //$NON-NLS-1$
		}
	}

	private void resetParamCompte() {		
		setCodeCompte(null);
		setParamCompteList(null);
		setPltParamList(null);		
		setSelectedParam(null);
		setLignesDecais(null);
		setLignesEncais(null);
	}

	public void addOrUpdateParam(){
		logger.info("addOrUpdateParam  ParamCompte: {}",getSelectedParam());
		ParamCompteLtr param=getSelectedParam();
		if(getPltParamList()==null)
			setPltParamList(new ArrayList<ParamCompteLtr>());
		try {
			
			if(	(!isUpdateMode()) && (getPltParamList().contains(param))){
				HandlerJSFMessage.addError(MsgEntry.DUPLICATE_COMPTE_MSG);
				return;
			}
			if(!param.checkRequired()){
			HandlerJSFMessage.addError(MsgEntry.CPWIN_REQUIRED);
			return;
			}
			logger.debug("addOrUpdateParam: TiersForSearch: {}",getTiersForSearch().getCode());
				param.setCodeTiers(getTiersForSearch().getCode());
				logger.debug("addOrUpdateParam: ParamCompteLtr: {}",param);
				param=service.save(param);
				param.setTiers(BudgetHelper.getTiersService().getTiersWoIban(param.getCodeTiers()));
				setSelectedParam(param);

			if(!isUpdateMode())
			getPltParamList().add(param);
			closeParamDialog();
			HandlerJSFMessage.addInfo(MsgEntry.SUCCES);
		
		} catch (Exception e) {
			HandlerJSFMessage.addErrorMessageFromException(MsgEntry.FAILED, e);
			e.printStackTrace();
		}
		
		if (logger.isDebugEnabled()) {
			logger.debug("addOrUpdateParam() - end"); //$NON-NLS-1$
		}
	}


	public Integer getLtrDep() {
		return ltrDep;
	}

	public void setLtrDep(Integer ltrDep) {
		this.ltrDep = ltrDep;
	}

	public Integer getLtrRec() {
		return ltrRec;
	}

	public void setLtrRec(Integer ltrRec) {
		this.ltrRec = ltrRec;
	}

	public List<SimpleEntity> getParamCompteList() {
		if ((paramCompteList == null) && isCommonRequiredDone()) {
			paramCompteList = service.getParamCompteList(getExercice());
		}
		
		return paramCompteList;
	}

	public void setParamCompteList(List<SimpleEntity> paramCompteList) {
		this.paramCompteList = paramCompteList;
	}

	public SimpleEntity getCompte() {
		return compte;
	}

	public void setCompte(SimpleEntity compte) {
		this.compte = compte;
	}

	public String getCodeCompte() {
		return codeCompte;
	}

	public void setCodeCompte(String codeCompte) {
		this.codeCompte = codeCompte;
	}

	public List<LigneTresorerie> getLignesDecais() {
		if(lignesDecais==null){
			setLignesDecais(service.getLigneTresorerie(getExercice(), GlobalEnum.Detail, EncDecEnum.Decaissement));
		}
		return lignesDecais;
	}

	public void setLignesDecais(List<LigneTresorerie> lignesDecais) {
		this.lignesDecais = lignesDecais;
	}

	public List<LigneTresorerie> getLignesEncais() {
		if(lignesEncais==null){
			setLignesEncais(service.getLigneTresorerie(getExercice(), GlobalEnum.Detail, EncDecEnum.Encaissement));
		}
		return lignesEncais;
	}

	public void setLignesEncais(List<LigneTresorerie> lignesEncais) {
		this.lignesEncais = lignesEncais;
	}

	public boolean isUpdateMode() {
		return updateMode;
	}

	public void setUpdateMode(boolean updateMode) {
		this.updateMode = updateMode;
	}
	
	
	public void searchConciliation() {
		resetDynamicList();
		Periode annuel=getPeriode(getPeriode());
		try {
			if((annuel!=null)&&(annuel.getEtat()==EtatPeriodeEnum.Cloture))
			{
			setPlan(null);
			HandlerJSFMessage.addError(MsgEntry.ANNUEL_REALISEE);
			return;
			}
			this.plan = loadPlan(getExercice(), getPeriode(), DetailLigneTresorerie.class);
			loadCreditOuvert(plan);
			loadSoldeInitial(plan);
			plan.calculateTotaux();
			plan.calculateVarSoldeMois();
			plan.sort();
			plan.prepare();
		
		} catch (Exception e) {
			HandlerJSFMessage.addErrorMessage(getErrorMessageFromException(MsgEntry.FAILED, e));
			e.printStackTrace();
		}
	}
	
	
	public void saveConciliationData() {
		savePlanTresorerie(plan);
	}
	
	
	public boolean isVarSoldeMoisZero() {
		return getPlan().getTotaleVarSoldeMois().compareTo(BigDecimal.ZERO) == 0;
	}

	public void validateConciliation() {
		Visa visaReal=new Visa();
		visaReal.setVisa(VisaEnum.Accepte);
		Trace trace=Helper.createTrace();
		visaReal.setAuteur(trace.getAuteurMaj());
		visaReal.setDate(trace.getDateMaj());
		try{
		validatePlanTresorerie(plan, EtatPeriodeEnum.Cloture, null, visaReal);
		setConcilPeriodeList(null);
		setPeriodeList(null);
		setPeriode(null);
		setPlan(null);
		HandlerJSFMessage.addInfo(MsgEntry.SUCCES);
		}catch(Exception e){
			HandlerJSFMessage.addErrorMessage(getErrorMessageFromException(MsgEntry.FAILED, e));
			e.printStackTrace();
		}
	}
	
	private <T extends GenericLigneTresorerie> void savePlanTresorerie(PlanTresorerie<T> plt) {
		List<T> updatedList = new ArrayList<>();

		Trace trace = Helper.createTrace();
		try {
		for (EncDecEnum typeOp : EncDecEnum.values()) {

			for (T d : plt.getDetailList(typeOp)) {
				if (d.isGlobal())
					continue;
				if(d.getTrace()==null)d.setTrace(trace);
				else {
				d.getTrace().setAuteurMaj(trace.getAuteurMaj());
				d.getTrace().setDateMaj(trace.getDateMaj());
				}
				updatedList.add(d);
			}

		}
		service.saveTresorerieData(updatedList);
			HandlerJSFMessage.addInfo(MsgEntry.SUCCES);
			logger.debug("savePlanTresorerie: updatedList: {} ", updatedList.size());
		} catch (Exception e) {
			HandlerJSFMessage.addErrorMessage(getErrorMessageFromException(MsgEntry.FAILED, e));
			e.printStackTrace();
		}
		
	}
	
	public void validatePlanTresorerie(PlanTresorerie<DetailLigneTresorerie> plt,EtatPeriodeEnum etat,Visa visaPrev,Visa visaReal  ) {
		List<DetailLigneTresorerie> updatedList = new ArrayList<>();

		Trace trace = Helper.createTrace();
		for (EncDecEnum typeOp : EncDecEnum.values()) {

			for (DetailLigneTresorerie d : plt.getDetailList(typeOp)) {
				if (d.isGlobal())	continue;
				d.setEtat(etat);
				if(visaPrev!=null)	d.setPrevisionnel(visaPrev);
				if(visaReal!=null)	d.setRealise(visaReal);
				if(d.getTrace()==null)d.setTrace(trace);
				else{
				d.getTrace().setAuteurMaj(trace.getAuteurMaj());
				d.getTrace().setDateMaj(trace.getDateMaj());
				}

				updatedList.add(d);
			}

		}

			service.saveTresorerieData(updatedList);
			
	}
}


	
	
