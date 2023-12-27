package fr.symphonie.budget.ui.beans.pluri;

import static fr.symphonie.budget.core.dao.ps.ColumnName.a_payer_col;
import static fr.symphonie.budget.core.dao.ps.ColumnName.ant_dp_emise_col;
import static fr.symphonie.budget.core.dao.ps.ColumnName.ant_rae_col;
import static fr.symphonie.budget.core.dao.ps.ColumnName.ant_rap_col;
import static fr.symphonie.budget.core.dao.ps.ColumnName.bi_col;
import static fr.symphonie.budget.core.dao.ps.ColumnName.br_acc_col;
import static fr.symphonie.budget.core.dao.ps.ColumnName.br_projet_col;
import static fr.symphonie.budget.core.dao.ps.ColumnName.ca_col;
import static fr.symphonie.budget.core.dao.ps.ColumnName.co_col;
import static fr.symphonie.budget.core.dao.ps.ColumnName.dispo_officiel_col;
import static fr.symphonie.budget.core.dao.ps.ColumnName.dispo_servv_col;
import static fr.symphonie.budget.core.dao.ps.ColumnName.dp_em_col;
import static fr.symphonie.budget.core.dao.ps.ColumnName.dp_pec_col;
import static fr.symphonie.budget.core.dao.ps.ColumnName.ej_exec_col;
import static fr.symphonie.budget.core.dao.ps.ColumnName.exec_dp_emise_col;
import static fr.symphonie.budget.core.dao.ps.ColumnName.exec_mt_ej_col;
import static fr.symphonie.budget.core.dao.ps.ColumnName.exec_rae_col;
import static fr.symphonie.budget.core.dao.ps.ColumnName.rap_ini_col;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.event.ValueChangeEvent;

import org.apache.logging.log4j.util.Strings;
import org.primefaces.event.CellEditEvent;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.event.RowEditEvent;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.TreeNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.symphonie.budget.core.model.edition.DepEnum;
import fr.symphonie.budget.core.model.edition.util.RecByOrig;
import fr.symphonie.budget.core.model.pluri.BudgModification;
import fr.symphonie.budget.core.model.pluri.BudgetCpDest;
import fr.symphonie.budget.core.model.pluri.BudgetPluriannuel;
import fr.symphonie.budget.core.model.pluri.ComplexEntity;
import fr.symphonie.budget.core.model.pluri.CpGestionnaire;
import fr.symphonie.budget.core.model.pluri.CpModification;
import fr.symphonie.budget.core.model.pluri.CpModificationItem;
import fr.symphonie.budget.core.model.pluri.CreditPaiement;
import fr.symphonie.budget.core.model.pluri.EnvelopBudg;
import fr.symphonie.budget.core.model.pluri.EtatEnum;
import fr.symphonie.budget.core.model.pluri.GlobalSuiviStruct;
import fr.symphonie.budget.core.model.pluri.LigneBudgetaireAE;
import fr.symphonie.budget.core.model.pluri.ModifEnum;
import fr.symphonie.budget.core.model.pluri.ModificationCpLigne;
import fr.symphonie.budget.core.model.pluri.NoDataBudgEntity;
import fr.symphonie.budget.core.model.pluri.SuiviAeCpStruct;
import fr.symphonie.budget.core.model.pluri.SuiviPluriStruct;
import fr.symphonie.budget.core.model.pluri.SuiviRecetStruct;
import fr.symphonie.budget.core.model.pluri.suiviCpStruct;
import fr.symphonie.budget.core.service.IBudgetPluriannuelService;
import fr.symphonie.budget.core.service.IGestionTiersService;
import fr.symphonie.budget.ui.beans.GenericBean;
import fr.symphonie.budget.ui.beans.LoaderBean;
import fr.symphonie.budget.ui.beans.NavigationBean.Action;
import fr.symphonie.budget.ui.beans.SearchBean;
import fr.symphonie.budget.ui.beans.converter.ListProgramConverter;
import fr.symphonie.budget.ui.excel.ExcelHandler;
import fr.symphonie.budget.ui.excel.ExcelModelEnum;
import fr.symphonie.budget.ui.excel.imp.BudgetImport;
import fr.symphonie.budget.ui.excel.imp.ImportStruct;
import fr.symphonie.common.util.ActionEnum;
import fr.symphonie.common.util.BudgetHelper;
import fr.symphonie.common.util.Constant;
import fr.symphonie.common.util.MsgEntry;
import fr.symphonie.common.util.Util;
import fr.symphonie.cpwin.model.Tiers;
import fr.symphonie.excel.ErrorStruct;
import fr.symphonie.excel.IErrorStruct;
import fr.symphonie.excel.IImportedStruct;
import fr.symphonie.exception.MissingConfiguration;
import fr.symphonie.util.HandlerJSFMessage;
import fr.symphonie.util.Helper;
import fr.symphonie.util.model.SimpleEntity;
import fr.symphonie.util.model.Trace;

@ManagedBean(name = "bpBean")
@SessionScoped
public class BudgetPluriannuelBean extends GenericBean implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -6807172266991768262L;

	/**
	 * Logger for this class
	 */
	private static final Logger logger = LoggerFactory.getLogger(BudgetPluriannuelBean.class);

	private List<EnvelopBudg> adrList;

	@ManagedProperty(value = "#{budgetPluriService}")
	private IBudgetPluriannuelService budgetPluriService;

	private ActionEnum mode = ActionEnum.CONSULT;
	private boolean saisiValide = false;
	private TreeNode adressageTree;
	private TreeNode importTree;
	private TreeNode selectedNode;
	private CreditPaiement selectedCreditP;
	private List<EnvelopBudg> listEnvelopBudg;

	private BudgetPluriannuel budget;
	private EnvelopBudg envelopBudg;
	private ComplexEntity groupDestNat;
	private ComplexEntity groupDestNatCp;
	private boolean searchBudgetExecuted;
	private String exerciceCp;
	private List<ComplexEntity> groupDestList = null;
	private List<String> exerciceCpList = null;
	private List<SimpleEntity> listPrograme = null;
	private double montanCp;
	private InputStream importFile;
	private List<BudgetCpDest> listBudgetCpDest;
	private SimpleEntity programme;
	private List<IImportedStruct> importList = null;
	private List<IErrorStruct> importErrorList = null;
	private NoDataBudgEntity data;
	private List<ComplexEntity> groupDestCpList = null;
	private List<SimpleEntity> destinationList = null;
	private List<CpGestionnaire> gestParamList = null;

	private List<CreditPaiement> listCreditPaiement = null;
	private String gestionnaire;
	private boolean depNivDestEdited = false;
	private Boolean etatCpOuvert = null;
	/**
	 * La liste des CpGestionnaire
	 */
	private List<CpGestionnaire> listCpGestionnaire = null;
	/**
	 * gére les listes des CpGestionnaire par nature de groupe
	 */
	private Map<String, List<CpGestionnaire>> deploiementData = null;
	private String objetModification;
	private BudgModification selectedBudgModif;
	private Integer numeroBudgModif;
	private List<CpGestionnaire> saisieBrList;
	private String gestUnique = null;
	private List<suiviCpStruct> suiviCpAeAnt = null;
	private Map<String, Object> globalSuiviCp = null;
	private List<BudgModification> budgRecEnCoursList = null;
	private List<CpModification> cpModificationList = null;
	private List<CpModificationItem> cpItemModifList = null;

	private GlobalSuiviStruct globalSuiviAnt;
	private GlobalSuiviStruct globalSuiviExec;
	private GlobalSuiviStruct globalSuiviTotal;

	private List<suiviCpStruct> suiviCpAeExec = null;
	private int tiersOrigineAnt;
	private int tiersOrigineExec;
	private EtatEnum etatCpBudg = null;
	private double variationBrAventiller;
	private CpGestionnaire selectedBr;

	private double creditOuvert;
	public boolean searchDepExecuted;
	public CpModification selectedModifCP;

	private List<SuiviRecetStruct> suiviRecetAnt = null;
	private List<SuiviRecetStruct> suiviRecetExec = null;
	private List<SuiviRecetStruct> suiviRecetTotal = null;
	private List<RecByOrig> recByOrigList;
	private RecByOrig recByOrigTotal;
	private List<SuiviAeCpStruct> suiviAeCp = null;
	private List<SuiviPluriStruct> suiviEjPluri = null;
	private double totCpVote = 0d;

	/**
	 * @return the selectedBudgModif
	 */
	public BudgModification getSelectedBudgModif() {
		return selectedBudgModif;
	}

	/**
	 * @param selectedBudgModif
	 *            the selectedBudgModif to set
	 */
	public void setSelectedBudgModif(BudgModification selectedBudgModif) {
		this.selectedBudgModif = selectedBudgModif;
		resetDynamicList();
	}

	private List<BudgModification> budgModifList;

	public List<IImportedStruct> getImportList() {
		return importList;
	}

	public void setImportList(List<IImportedStruct> importList) {
		this.importList = importList;
	}

	private boolean envelopChossed = false;

	private TreeNode createAdressageTree(TreeNode tree) {
		if (logger.isDebugEnabled()) {
			logger.debug("createAdressageTree(" + getBudget() + ") - start"); //$NON-NLS-1$
		}

		if (getBudget() == null)
			return null;
		tree = new DefaultTreeNode();
		TreeNode env = null;

		logger.debug("createAdressageTree(" + getBudget().getListEnvelopBudg() + ") - start"); //$NON-NLS-1$
		for (EnvelopBudg envelop : getBudget().getListEnvelopBudg()) {
			loadEnvelop(envelop);
			env = new DefaultTreeNode(envelop, tree);
			logger.debug("taille de liste envelobBudg:" + getBudget().getListEnvelopBudg().size());
			if (envelop.getListLigneBudg() == null)
				continue;
			logger.debug("taille de liste lignebudgetaire :" + envelop.getListLigneBudg().size());
			for (LigneBudgetaireAE lb : envelop.getListLigneBudg()) {
				new DefaultTreeNode(lb, env);

			}
		}

		if (logger.isDebugEnabled()) {
			logger.debug("createAdressageTree():tree=" + tree + " - end"); //$NON-NLS-1$
		}
		return tree;
	}

	private void createAdressageTree() {
		if (logger.isDebugEnabled()) {
			logger.debug("createAdressageTree(" + getBudget() + ") - start"); //$NON-NLS-1$
		}
		if (getBudget() == null)
			return;
		adressageTree = new DefaultTreeNode();
		TreeNode env = null;

		logger.debug("createAdressageTree(" + getBudget().getListEnvelopBudg() + ") - start"); //$NON-NLS-1$
		for (EnvelopBudg envelop : getBudget().getListEnvelopBudg()) {
			loadEnvelop(envelop);
			env = new DefaultTreeNode(envelop, adressageTree);
			logger.debug("taille de liste envelobBudg:" + getBudget().getListEnvelopBudg().size());
			if (envelop.getListLigneBudg() == null)
				continue;
			logger.debug("taille de liste lignebudgetaire :" + envelop.getListLigneBudg().size());
			for (LigneBudgetaireAE lb : envelop.getListLigneBudg()) {
				new DefaultTreeNode(lb, env);

			}
		}

		if (logger.isDebugEnabled()) {
			logger.debug("createAdressageTree() - end"); //$NON-NLS-1$
		}
	}

	public void onNodeExpand() {

		collapsingORexpanding(adressageTree, true);

	}

	public void onNodeImportExpand() {

		collapsingORexpanding(getImportTree(), true);

	}

	public void setBudgetPluriService(IBudgetPluriannuelService budgetPluriService) {
		this.budgetPluriService = budgetPluriService;
	}

	public BudgetPluriannuel getBudget() {
		return budget;
	}

	public void setBudget(BudgetPluriannuel budget) {

		this.budget = budget;
	}

	public void reset() {

		setSearchBudgetExecuted(false);
		setMode(null);
		setBudget(null);
		envelopBudg = null;
		setExerciceCp(null);
		groupDestNat = null;
		setMontanCp(0);
		setProgramme(null);
		setSelectedCreditP(null);
		setEtatCpOuvert(null);
		setGroupDestNatCp(null);
		setGestUnique(null);
		setTiersOrigineAnt(1);
		setTiersOrigineExec(1);
		setEtatCpBudg(null);
		setBudgModifList(null);
		setNumeroBudgModif(0);
		resetEnteteList();
		setSelectedBr(null);
		setDepNivDest(null);
		setSearchDepExecuted(false);
		setGestionnaire(null);
		setSelectedModifCP(null);
		resetDynamicList();
		setDepNivDestEdited(false);
		setRecRecouveDataRefreshed(false);
		setRecNivDestList(null);
		getLoaderBean().setListNatureGroupe(null);
		getSearchBean().setCodeNatureGrp(null);
	}

	private void resetEnteteList() {
		setGroupDestCpList(null);
		setNivDestList(null);
	}

	public void setEnvelopBudg(EnvelopBudg envelopBudg) {
		if (logger.isDebugEnabled()) {
			logger.debug("setEnvelopBudg(EnvelopBudg) - start"); //$NON-NLS-1$
		}

		this.envelopBudg = envelopBudg;

		getEnvelopBudg().saveValues();
		envelopBudg.setBudget(getBudget());
		if (envelopChossed)
			loadEnvelop(envelopBudg);
		envelopChossed = false;
		if (logger.isDebugEnabled()) {
			logger.debug("setEnvelopBudg(EnvelopBudg) - end"); //$NON-NLS-1$
		}
	}

	public EnvelopBudg getEnvelopBudg() {
		return envelopBudg;
	}

	public void searchBudget() {
		int exercice = 0;
		String codeBudget = null;
		try {
			exercice = Helper.stringToInt(getSearchBean().getExercice());
			codeBudget = getSearchBean().getCodeBudget();
			logger.info("Recherche du budget: exercice=" + exercice + ", codeBudget=" + codeBudget
					+ "......................");
			if ((exercice == 0) || (codeBudget == null) || (codeBudget.isEmpty()))
				return;
			setSearchBudgetExecuted(true);

			setBudget(budgetPluriService.searchBudgetForSaisi(exercice, codeBudget));
			loadLibellesBudget();
			if (getBudget() != null)
				logger.info("Recherche du budget terminée: budget.getCodeBudget()=" + getBudget().getCodeBudget()
						+ "......................");
		} catch (Exception e) {
			HandlerJSFMessage.addErrorMessage(getErrorMessageFromException(MsgEntry.FAILED, e));
			e.printStackTrace();
		}

	}

	private void loadLibellesBudget() {
		logger.info("Chargement des libelles pour les elements du budget.............");
		if (getBudget() == null)
			return;
		loadLibellesEnveleppBudg();
	}

	private void loadLibellesEnveleppBudg() {
		if ((getBudget().getListEnvBudg() == null) || (getBudget().getListEnvBudg().isEmpty()))
			return;
		for (EnvelopBudg envelopp : getBudget().getListEnvBudg()) {
			loadLibellesEnv(envelopp);
		}

	}

	private void loadLibellesEnv(EnvelopBudg envelopp) {
		SimpleEntity entity = null;
		if (envelopp == null)
			return;
		// Groupe destination
		entity = budgetPluriService.getDestinationGroupe(envelopp.getGroupDest());
		if (entity != null)
			envelopp.setGroupDestLib(entity.getDesignation());
		// Groupe nature
		entity = budgetPluriService.getNatureGroupe(envelopp.getGroupNat());
		if (entity != null)
			envelopp.setGroupNatLib(entity.getDesignation());
		// programme
		entity = ListProgramConverter.findProgram(new SimpleEntity(envelopp.getProgramme(), ""));
		if (entity != null)
			envelopp.setProgrammeLib(entity.getDesignation());

	}

	public void searchBudgetForAdressage() {
		if (logger.isDebugEnabled()) {
			logger.debug("searchBudgetForAdressage(" + getSearchBean().getExercice() + ") - start"); //$NON-NLS-1$
		}

		searchBudget();
		setAdressageTree(null);
	}

	public void validateEnvelop() {
		if (logger.isDebugEnabled()) {
			logger.debug("validateEnvelop() - start"); //$NON-NLS-1$
		}
		try {
			setSaisiValide(true);
			getEnvelopBudg().validateSaisie();
			if (envelopBudg.isNewEnvelop()) {
				if (!getBudget().getListEnvelopBudg().contains(envelopBudg))
					getBudget().getListEnvelopBudg().add(envelopBudg);
			}
			saveBudget();
			HandlerJSFMessage.addInfo(MsgEntry.SUCCES);
		} catch (Exception e) {
			e.printStackTrace();
			HandlerJSFMessage.addErrorMessageFromException(MsgEntry.FAILED, e);
		}

		if (logger.isDebugEnabled()) {
			logger.debug("validateEnvelop():" + getBudget().getListEnvelopBudg().size() + "montantAe: " //$NON-NLS-1$
					+ getEnvelopBudg().getMontantAE() + "resteAv: " + getBudget().getResteAV() + " - end");
		}
	}

	public boolean isMntCpListValide() {
		if (getEnvelopBudg() == null)
			return true;
		if (getEnvelopBudg().getMntVentile() < 0)
			return false;
		return true;
	}

	public void addEnvelop() {
		if (logger.isDebugEnabled()) {
			logger.debug("addEnvelop() - start"); //$NON-NLS-1$
		}
		setMode(ActionEnum.CREATE);
		envelopBudg = new EnvelopBudg(getBudget(), true);
		Trace t = Helper.createTrace();
		envelopBudg.setTrace(t);

		DialogHelper.openEnvelopBudgDialog();

		if (logger.isDebugEnabled()) {
			logger.debug("addEnvelop() - end"); //$NON-NLS-1$
		}
	}

	public void addCreditPaiement() {
		if (logger.isDebugEnabled()) {
			logger.debug("addCreditPaiement() - start"); //$NON-NLS-1$
		}
		getEnvelopBudg().addCreditPaiement(getBudget().getExercice());

		if (logger.isDebugEnabled()) {
			logger.debug("addCreditPaiement() - end"); //$NON-NLS-1$
		}

	}

	public void addLbAdressage() {
		if (logger.isDebugEnabled()) {
			logger.debug("addLbAdressage() - start :"); //$NON-NLS-1$
		}
		getEnvelopBudg().addLigneBudgetaire();

		if (logger.isDebugEnabled()) {
			logger.debug("addLbAdressage() - end: " + envelopBudg.getListLigneBudg().size()); //$NON-NLS-1$
		}
	}

	public void chooseEnvelop() {
		if (logger.isDebugEnabled()) {
			logger.debug("chooseEnvelop() - start"); //$NON-NLS-1$
		}
		setMode(ActionEnum.UPDATE);
		envelopChossed = true;
		DialogHelper.openEnvelopBudgDialog();
		setSaisiValide(false);
		if (logger.isDebugEnabled()) {
			logger.debug("chooseEnvelop() - end"); //$NON-NLS-1$
		}
	}

	public void chooseEnvelopForAdress() {
		if (logger.isDebugEnabled()) {
			logger.debug("chooseEnvelopForAdress() - start"); //$NON-NLS-1$
		}
		envelopChossed = true;
		DialogHelper.openEnvelopBudgForAdressDialog();
		if (logger.isDebugEnabled()) {
			logger.debug("chooseEnvelopForAdress() - end"); //$NON-NLS-1$
		}
	}

	public void closeEnvelopDialog() {
		if (logger.isDebugEnabled()) {
			logger.debug("closeEnvelopDialog() - start"); //$NON-NLS-1$
		}

		DialogHelper.closeDialog(null);
		if (!isSaisiValide())
			if (ActionEnum.UPDATE == getMode())
				getEnvelopBudg().rollBackValues();
		if (logger.isDebugEnabled()) {
			logger.debug("closeEnvelopDialog() - end"); //$NON-NLS-1$
		}
	}

	public void closeTiersDialog() {
		if (logger.isDebugEnabled()) {
			logger.debug("closeEnvelopDialog() - start"); //$NON-NLS-1$
		}

		DialogHelper.closeDialog(null);

		if (logger.isDebugEnabled()) {
			logger.debug("closeEnvelopDialog() - end"); //$NON-NLS-1$
		}
	}

	public void onCreditPaiementCellEdit(CellEditEvent event) {
		if (logger.isDebugEnabled()) {
			logger.debug("onCreditPaiementCellEdit(" + event.getColumn().getClientId() + ") - start"); //$NON-NLS-1$
		}
		String columnId = event.getColumn().getClientId();
		Object newValue = event.getNewValue();
		if (columnId.equals("montantCP")) {
			logger.debug("onCreditPaiementCellEdit:" + newValue + " - end");
		}

		for (CreditPaiement credit : getEnvelopBudg().getListCreditPaiement()) {
			logger.info("onCreditPaiementCellEdit:   exercice :" + credit.getExercice() + " ,montant :"
					+ credit.getMontant());
		}

		if (logger.isDebugEnabled()) {
			logger.debug("onCellEdit(CellEditEvent) - end"); //$NON-NLS-1$
		}
	}

	public void onLigneBudgetaireCellEdit(CellEditEvent event) {
		if (logger.isDebugEnabled()) {
			logger.debug("onLigneBudgetaireCellEdit(" + event.getColumn().getClientId() + ") - start"); //$NON-NLS-1$
		}
		String columnId = event.getColumn().getClientId();
		Object newValue = event.getNewValue();
		if (columnId.equals("montant alloue")) {
			logger.debug("onLigneBudgetaireCellEdit:" + newValue + " - end");
		}

		if (logger.isDebugEnabled()) {
			logger.debug("onLigneBudgetaireCellEdit(CellEditEvent) - end"); //$NON-NLS-1$
		}
	}

	public void onEnvelopItemChange(AjaxBehaviorEvent changeEvent) {

	}

	public void onEnvelopCpItemChange() {
		logger.info("#### onEnvelopCpItemChange ");
		setListBudgetCpDest(null);
		setListCpGestionnaire(null);
		setDestinationList(null);
		setGestParamList(null);
		setSearchDepExecuted(false);
	}

	public void setSearchBudgetExecuted(boolean searchBudgetExecuted) {
		this.searchBudgetExecuted = searchBudgetExecuted;
	}

	public boolean isSearchBudgetExecuted() {
		return searchBudgetExecuted;
	}

	/**
	 * @return the saisiValide
	 */
	public boolean isSaisiValide() {
		return saisiValide;

	}

	/**
	 * @param saisiValide
	 *            the saisiValide to set
	 */
	public void setSaisiValide(boolean saisiValide) {
		this.saisiValide = saisiValide;
	}

	public boolean isAddAnvelopAutorized() {
		if (getBudget() == null)
			return false;
		if (getBudget().isVisee())
			return false;
		if (getBudget().getResteAV() <= 0)
			return false;
		if (getBudget().getExercice() == 0)
			return false;
		return true;

	}

	/**
	 * @return the mode
	 */
	public ActionEnum getMode() {
		return mode;
	}

	/**
	 * @param mode
	 *            the mode to set
	 */
	public void setMode(ActionEnum mode) {
		this.mode = mode;
	}

	public void validateAdressage() {
		if (logger.isDebugEnabled()) {
			logger.debug("validateAdressage() - start: "); //$NON-NLS-1$
		}

		getEnvelopBudg().validateAdressage();
		if (logger.isDebugEnabled()) {
			logger.debug("validateAdressage() - : " + envelopBudg.getListLigneBudg().size()); //$NON-NLS-1$
		}
		saveEnvelopBudg();
		setAdressageTree(null);

		if (logger.isDebugEnabled()) {
			logger.debug("validateAdressage() - end: " + envelopBudg.getListLigneBudg().size()); //$NON-NLS-1$
		}
	}

	private void saveEnvelopBudg() {
		logger.info("saveEnvelopBudg - start");
		budgetPluriService.saveEnvelopBudg(getEnvelopBudg());
		logger.info("saveEnvelopBudg - end");
	}

	public void closeAdressageDialog() {

		DialogHelper.closeDialog(null);

	}

	public void collapsingORexpanding(TreeNode n, boolean option) {

		if (n.getChildren().size() == 0) {
			n.setSelected(false);
		} else {
			for (TreeNode s : n.getChildren()) {
				collapsingORexpanding(s, option);
			}
			n.setExpanded(option);
			n.setSelected(false);
		}
	}

	public void setSelectedNode(TreeNode selectedNode) {
		this.selectedNode = selectedNode;
	}

	public TreeNode getSelectedNode() {
		return selectedNode;
	}

	public TreeNode getAdressageTree() {

		if (adressageTree == null)
			createAdressageTree();
		return adressageTree;
	}

	public void setAdressageTree(TreeNode adressageTree) {
		this.adressageTree = adressageTree;
	}

	public void montantNlistener() {

	}

	public void onCpEditInit(RowEditEvent event) {
		if (logger.isDebugEnabled()) {
			logger.debug("onCpEditInit(RowEditEvent) - start"); //$NON-NLS-1$
		}
		if (event == null)
			return;

		setSelectedCreditP((CreditPaiement) event.getObject());

		if (logger.isDebugEnabled()) {
			logger.debug("onCpEditInit- end"); //$NON-NLS-1$
		}
	}

	public void onCpEdit(RowEditEvent event) {

	}

	public void setSelectedCreditP(CreditPaiement selectedCreditP) {
		this.selectedCreditP = selectedCreditP;
	}

	public CreditPaiement getSelectedCreditP() {
		return selectedCreditP;
	}

	public void saveBudget() {
		try {
			setBudget(budgetPluriService.saveBudget(getBudget()));
			loadLibellesBudget();
		} catch (Exception e) {
			HandlerJSFMessage.addErrorMessage(getErrorMessageFromException(MsgEntry.FAILED, e));
			e.printStackTrace();
		}

	}

	public void resetDynamicList() {
		setGroupDestList(null);
		setExerciceCpList(null);
		setListPrograme(null);
		setDestinationList(null);

		getLoaderBean().setListEnvBdgVentilParOrigin(null);
		setListBudgetCpDest(null);
		setListCpGestionnaire(null);

		setListCreditPaiement(null);
		setSaisieBrList(null);
		setSuiviCpAeAnt(null);
		setSuiviCpAeExec(null);
		setGlobalSuiviCp(null);
		setGlobalSuiviAnt(null);
		setGlobalSuiviExec(null);
		setSuiviRecetAnt(null);
		setSuiviRecetExec(null);
		setSuiviRecetTotal(null);
		setRecByOrigList(null);
		setRecByOrigTotal(new RecByOrig());
		resetDeploiement();
		BudgetHelper.getEditionBean().resetDynamicList();
		setSuiviAeCp(null);
	}

	public List<ComplexEntity> getDestNatList() {
		if (getEnvelopBudg() != null)
			return loadDestNatList(getEnvelopBudg().getExercice(), getEnvelopBudg().getGroupDest(),
					getEnvelopBudg().getGroupNat(), getSearchBean().getCodeBudget());
		return new ArrayList<ComplexEntity>();
	}

	private List<ComplexEntity> loadDestNatList(int exercice, String groupDest, String groupNat, String codeBudget) {
		List<ComplexEntity> list = null;
		list = budgetPluriService.getDestNat(exercice, groupDest, groupNat, codeBudget);
		return list;
	}

	public StreamedContent getXlsEnvelopBudg() {
		StreamedContent returnStreamedContent = null;
		logger.debug("budget:" + getBudget());
		ExcelHandler excel = new ExcelHandler(ExcelModelEnum.ENVELOP_BUDG_LIST, getBudget().getListEnvBudg());
		returnStreamedContent = excel.getExcelFile();
		if (logger.isDebugEnabled()) {
			logger.debug("getXlsEnvelopBudg() - end"); //$NON-NLS-1$
		}
		return returnStreamedContent;

	}

	public StreamedContent getXlsLigneBudg() {
		StreamedContent returnStreamedContent = null;
		ExcelHandler excel = new ExcelHandler(ExcelModelEnum.LIGN_BUDG_LIST, envelopBudg.getListLigneBudgView());
		returnStreamedContent = excel.getExcelFile();
		return returnStreamedContent;
	}

	public void searchCpParDestination() {
		boolean isSingleItem = false;
		logger.info("searchCpParDestination : - start, depNivDest=" + getDepNivDestInt());
		try {

			int exerciceCp = getExercice();
			String codeBudget = getSearchBean().getCodeBudget();
			String groupNat = getCodeGroupNat(getGroupDestNatCp());

			List<BudgetCpDest> list = getVentilationParDest(exerciceCp, codeBudget, groupNat, null);
			if ((list == null) || (list.isEmpty())) {
				list = createBudgetCpDest(exerciceCp, codeBudget, groupNat);
				if ((list != null) && (list.size() == 1))
					isSingleItem = true;
			}

			loadlibelleDestibnation(list);

			setListCreditPaiement(null);
			getListCreditPaiement();
			if (isSingleItem) {
				list.get(0).setMontant(new BigDecimal(getTotalMntCp()));
			}
			setListBudgetCpDest(list);

		} catch (Exception e) {
			HandlerJSFMessage.addErrorMessage(getErrorMessageFromException(MsgEntry.FAILED, e));
			e.printStackTrace();
		}
	}

	public void prepareVentBrParDest() {
		Action action = BudgetHelper.getNavigationBean().getCurrentAction();
		if (Action.SAISI_CP_BR == action) {
			integratedVentParDest();
			return;
		}

		boolean isSingleItem = false;
		int exercice = getExercice();
		String codeBudget = getSearchBean().getCodeBudget();
		String groupNat = getCodeGroupNat(getGroupDestNatCp());
		logger.info("prepareVentBrParDest :groupNat=" + groupNat + " - start");
		boolean isEtitableBr = isEditableBR();
		List<BudgetCpDest> list = getVentilationParDest(exercice, codeBudget, groupNat, null);

		if ((list == null) || (list.isEmpty())) {
			if (isEtitableBr)
				list = createBudgetCpDest(exercice, codeBudget, groupNat);
			if ((list != null) && (list.size() == 1))
				isSingleItem = true;
		}

		loadlibelleDestibnation(list);

		List<CpGestionnaire> listCpGest = budgetPluriService.getCpGestionnaireList(exercice, codeBudget, groupNat,
				getPreparedGestUnique());
		CpGestionnaire br = listCpGest.get(0);
		if (!getSelectedBudgModif().isEtatCpValide())
			variationBrAventiller = br.getBudgetRectificatif();
		else {
			variationBrAventiller = budgetPluriService.getMontantBr(getSelectedBudgModif(), br.getNoLbgCp());
		}

		if (isSingleItem) {
			list.get(0).setMontant(new BigDecimal(variationBrAventiller));
		}
		setListBudgetCpDest(list);

	}

	private List<BudgetCpDest> getVentilationParDest(int exercice, String codeBudget, String groupNat, Integer noBr) {
		int niveau = (noBr != null) ? noBr : getNiveau();

		logger.debug("getVentilationParDest:groupNat=" + groupNat + ", niveau=" + niveau);
		List<BudgetCpDest> list = budgetPluriService.getBudgetCpDest(exercice, codeBudget, groupNat, niveau);
		logger.debug("getVentilationParDest: list=" + list.size());
		return list;
	}

	public void searchCpParGestionnaire() {
		logger.info("searchCpParGestionnaire  - start");
		boolean isSingleItem = false;
		try {

			int exerciceCp = getExercice();
			String codeBudget = getSearchBean().getCodeBudget();
			String groupNat = getCodeGroupNat(getGroupDestNatCp());
			List<CpGestionnaire> list = budgetPluriService.getCpGestionnaireList(exerciceCp, codeBudget, groupNat);
			if ((list == null) || (list.isEmpty())) {
				list = createCpGestionnaire(exerciceCp, codeBudget, groupNat);
				if ((list != null) && (list.size() == 1))
					isSingleItem = true;
			}
			loadlibelleGestionnaire(list);
			setListCreditPaiement(null);
			getListCreditPaiement();
			if (isSingleItem) {
				list.get(0).setBudgetInitial(new BigDecimal(getTotalMntCp()));
			}
			setListCpGestionnaire(list);
			logger.info("searchCpParGestionnaire  - list.size()=" + ((list != null) ? list.size() : 0) + " - end");
		} catch (Exception e) {
			HandlerJSFMessage.addErrorMessage(getErrorMessageFromException(MsgEntry.FAILED, e));
			e.printStackTrace();
		}
	}

	private void loadlibelleGestionnaire(List<CpGestionnaire> list) {
		if (list != null)
			for (CpGestionnaire gestionnaire : list) {
				SimpleEntity entity = null;
				if (gestionnaire == null)
					continue;
				entity = budgetPluriService.getGestionnaire(gestionnaire.getGestionnaire());
				if (entity != null)
					gestionnaire.setLibelle(entity.getDesignation());
			}
	}

	private List<CpGestionnaire> createCpGestionnaire(int exerciceCp, String codeBudget, String groupNat) {

		if (logger.isDebugEnabled()) {
			logger.debug("createCpGestionnaire(exerciceCp=" + exerciceCp + ", groupNat=" + groupNat + ") - start"); //$NON-NLS-1$
		}
		List<CpGestionnaire> list = new ArrayList<CpGestionnaire>();
		List<CpGestionnaire> gestParamList = getGestParamList(exerciceCp, groupNat, codeBudget);
		logger.debug("createCpGestionnaire: gestList=" + gestParamList);
		if ((gestParamList == null) || (gestParamList.isEmpty()))
			return list;

		CpGestionnaire cpGest = null;
		for (CpGestionnaire gestParam : gestParamList) {
			if (gestParam.getGestionnaire() == null)
				continue;
			cpGest = new CpGestionnaire(exerciceCp, codeBudget, groupNat);
			cpGest.setGestionnaire(gestParam.getGestionnaire());
			cpGest.setLibelle(gestParam.getLibelle());
			cpGest.setEjEnCours(gestParam.getEjEnCours());

			cpGest.setEjReserve(gestParam.getEjEnCours());
			list.add(cpGest);

		}

		if (logger.isDebugEnabled()) {
			logger.debug("createCpGestionnaire size=" + list.size() + " - end"); //$NON-NLS-1$
		}
		return list;
	}

	private List<CpGestionnaire> getGestParamList(int exercice, String groupNat, String codeBudget) {
		logger.info("getGestParamList: exercice=" + exercice + ", groupNat=" + groupNat);
		if (gestParamList == null)
			gestParamList = budgetPluriService.getCpGestData(exercice, codeBudget, groupNat);

		logger.info("getGestParamList: " + gestParamList.size() + " - end");
		return gestParamList;
	}

	private void loadlibelleDestibnation(List<BudgetCpDest> list) {
		if (list != null)
			for (BudgetCpDest broupdest : list) {
				SimpleEntity entity = null;
				if (broupdest == null)
					continue;
				entity = budgetPluriService.getBudgDestination(broupdest.getExerciceCP(), broupdest.getDestination(),
						broupdest.getCodeBudget());
				if (entity != null)
					broupdest.setLibelle(entity.getDesignation());
			}
	}

	private String getCodeGroupNat(ComplexEntity ce) {
		switch (BudgetHelper.getNavigationBean().getCurrentAction()) {
		case VENTIL_GEST_BR:
			return getSearchBean().getNatGrp();
		default:
			break;
		}
		if (ce != null)
			if (ce.getEntity2() != null)
				return ce.getEntity2().getCode();
		return null;
	}

	private List<BudgetCpDest> createBudgetCpDest(int exercice, String codeBudget, String groupNat) {
		if (logger.isDebugEnabled()) {
			logger.debug("createBudgetCpDest(" + exercice + "," + codeBudget + "," + groupNat + ") - start"); //$NON-NLS-1$
		}

		List<SimpleEntity> destList = getDestinationList(exercice, groupNat);
		if ((destList == null) || (destList.isEmpty()))
			return null;
		List<BudgetCpDest> list = new ArrayList<BudgetCpDest>();
		BudgetCpDest cpDest = null;
		for (SimpleEntity dest : destList) {
			if (dest.getDesignation() == null)
				continue;
			cpDest = new BudgetCpDest(codeBudget, exercice, groupNat);
			cpDest.setDestination(dest.getCode());
			cpDest.setLibelle(dest.getDesignation());
			cpDest.setNiveau(getNiveau());
			list.add(cpDest);

		}

		if (logger.isDebugEnabled()) {
			logger.debug("createBudgetCpDest size=" + list.size() + " - end"); //$NON-NLS-1$
		}
		return list;
	}

	public void setListEnvelopBudg(List<EnvelopBudg> listEnvelopBudg) {
		this.listEnvelopBudg = listEnvelopBudg;
	}

	public List<EnvelopBudg> getListEnvelopBudg() {
		return listEnvelopBudg;
	}

	public void setGroupDestNat(ComplexEntity groupDestNat) {
		try {
			setExerciceCp(null);
			setProgramme(null);
			setExerciceCpList(null);
			setListPrograme(null);
			setSelectedCreditP(null);
			this.groupDestNat = groupDestNat;
			if (getBudget().getListEnvBudg() != null)
				for (EnvelopBudg env : getBudget().getListEnvBudg()) {
					if (env.getGroupDestNat().equals(this.groupDestNat)) {
						setEnvelopBudg(env);
						break;
					}
				}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public ComplexEntity getGroupDestNat() {
		return groupDestNat;
	}

	public void setExerciceCp(String exerciceCp) {
		setMontanCp(0);
		this.exerciceCp = exerciceCp;
		if (getEnvelopBudg() == null)
			return;
		CreditPaiement credit = getEnvelopBudg().getCreditPaiement(Helper.stringToInt(exerciceCp));
		setSelectedCreditP(credit);

	}

	public String getExerciceCp() {
		return exerciceCp;
	}

	public void setExerciceCpList(List<String> exerciceCpList) {

		this.exerciceCpList = exerciceCpList;
	}

	public List<String> getExerciceCpList() {
		String exec = null;
		if (exerciceCpList != null)
			return exerciceCpList;
		exerciceCpList = new ArrayList<String>();
		if (getEnvelopBudg() != null)
			if (getEnvelopBudg().getListCreditPaiement() != null)
				for (CreditPaiement credit : getEnvelopBudg().getListCreditPaiement()) {
					exec = credit.getExerciceCP() + "";
					if (!exerciceCpList.contains(exec))
						exerciceCpList.add(exec);
				}
		return exerciceCpList;
	}

	public void readInputFile(FileUploadEvent event) {

		InputStream file = null;

		if (event.getFile() == null) {
			HandlerJSFMessage.addErrorMessage("File is null");
			return;
		}

		try {
			resetImport();
			file = event.getFile().getInputstream();
			setImportFile(file);
			importVentilationByService(file);
			controlImportEnvelop();

		} catch (IOException e1) {
			e1.printStackTrace();
		} finally {
			closeImportFile();
		}

	}

	public void setAdrList(List<EnvelopBudg> adrList) {
		this.adrList = adrList;
	}

	public List<EnvelopBudg> getAdrList() {
		return adrList;
	}

	public void setMontanCp(double montanCp) {
		this.montanCp = montanCp;
	}

	public double getMontanCp() {
		CreditPaiement credit = null;
		if (getEnvelopBudg() == null)
			return 0;
		if (getExerciceCp() == null)
			return 0;
		credit = getSelectedCreditP();
		if (credit == null)
			return 0;
		montanCp = credit.getMontant();
		return montanCp;
	}

	public void setGroupDestList(List<ComplexEntity> groupDestList) {
		this.groupDestList = groupDestList;
	}

	public List<ComplexEntity> getGroupDestList() {
		if (groupDestList != null)
			return groupDestList;
		ComplexEntity cEntity = null;
		groupDestList = new ArrayList<ComplexEntity>();
		if (getBudget() != null)
			if (getBudget().getListEnvBudg() != null)
				for (EnvelopBudg env : getBudget().getListEnvBudg()) {
					cEntity = env.getGroupDestNat();
					if (!groupDestList.contains(cEntity))
						groupDestList.add(cEntity);

				}
		return groupDestList;
	}

	public void setProgramme(SimpleEntity programme) {
		this.programme = programme;
		setSelectedCreditP(null);
	}

	public SimpleEntity getProgramme() {
		return programme;
	}

	private void loadLibelleProgramme(List<SimpleEntity> prograpmmeList) {
		SimpleEntity entity = null;
		if (prograpmmeList == null)
			return;
		for (SimpleEntity program : prograpmmeList) {
			entity = ListProgramConverter.findProgram(new SimpleEntity(program.getCode(), ""));
			if (entity != null)
				program.setDesignation(entity.getDesignation());
		}
	}

	/**
	 * @return the loaderBean
	 */
	public LoaderBean getLoaderBean() {
		return BudgetHelper.getLoaderBean();
	}

	public void setListPrograme(List<SimpleEntity> listPrograme) {
		this.listPrograme = listPrograme;
	}

	public List<SimpleEntity> getListPrograme() {
		SimpleEntity program = null;
		if (listPrograme != null)
			return listPrograme;
		listPrograme = new ArrayList<SimpleEntity>();
		if (getEnvelopBudg() != null)
			for (LigneBudgetaireAE lb : getEnvelopBudg().getListLigneBudg()) {
				program = new SimpleEntity(lb.getProgramme(), "");
				if (!listPrograme.contains(program))
					listPrograme.add(program);

			}
		loadLibelleProgramme(listPrograme);

		return listPrograme;
	}

	private void loadEnvelop(EnvelopBudg env) {

	}

	private void importVentilationByService(InputStream file) throws IOException {
		importList = BudgetImport.getInstance().executeImport(file);
		importErrorList = BudgetImport.getInstance().checkImportedData(importList);
	}

	public void setListBudgetCpDest(List<BudgetCpDest> listBudgetCpDest) {
		this.listBudgetCpDest = listBudgetCpDest;
	}

	public List<BudgetCpDest> getListBudgetCpDest() {

		return listBudgetCpDest;
	}

	public void saveCreditParDestination() {
		logger.info("saveCreditParDestination : start");
		try {
			if (getListBudgetCpDest() != null)
				budgetPluriService.saveBudgetCpParDest(getListBudgetCpDest());
			searchCpParDestination();
			HandlerJSFMessage.addInfo(MsgEntry.SUCCES);
		} catch (Exception e) {
			HandlerJSFMessage.addErrorMessage(getErrorMessageFromException(MsgEntry.FAILED, e));
			e.printStackTrace();
			// HandlerJSFMessage.addErrorMessageFromException(MsgEntry.FAILED,
			// e);
		}
		logger.info("saveCreditParDestination : end");

	}

	public void saveVentBrParDest() {
		logger.info("saveVentBrParDest : start");
		try {
			if (getListBudgetCpDest() != null)
				budgetPluriService.saveBudgetCpParDest(getListBudgetCpDest());
			prepareVentBrParDest();
			HandlerJSFMessage.addInfo(MsgEntry.SUCCES);
		} catch (Exception e) {
			e.printStackTrace();
			HandlerJSFMessage.addErrorMessageFromException(MsgEntry.FAILED, e);
		}
		logger.info("saveVentBrParDest : end");

	}

	private boolean controlImportEnvelop() {

		IErrorStruct error = null;
		List<EnvelopBudg> envList = getBudget().getListEnvBudg();
		EnvelopBudg envelop = null;
		LigneBudgetaireAE lb = null;
		String msg = null;
		int exercice = getBudget().getExercice();
		String budg = getBudget().getCodeBudget();
		int index = 0;
		if ((envList == null) || (envList.isEmpty())) {
			error = new ErrorStruct();
			error.setErrorKey(MsgEntry.AUCUN_ENVELOP_MSG);
			getImportErrorList().add(error);
		} else {
			for (IImportedStruct item : importList) {

				envelop = ((ImportStruct) item).createEnvelopBudg();
				lb = ((ImportStruct) item).createLigneBudgetaire();
				envelop.setExercice(exercice);
				envelop.setExercice(exercice);
				envelop.setCodeBudget(budg);
				lb.setExercice(exercice);
				lb.setExercice(exercice);
				lb.setCodeBudget(budg);
				lb.setEnvelop(envelop);

				index = envList.indexOf(envelop);
				if (index == -1) {
					error = new ErrorStruct();
					error.setErrorKey(MsgEntry.ENVELOP_INEXISTANT_MSG);
					msg = HandlerJSFMessage.getErrorMessage(MsgEntry.ENVELOP_INEXISTANT_MSG);
					msg = HandlerJSFMessage.formatMessage(msg,
							new Object[] { envelop.getGroupDest(), envelop.getGroupNat() });
					error.setErrorMessage(msg);
					getImportErrorList().add(error);

				} else {
					envelop = envList.get(index);
					if (!envelop.getListLigneBudg().isEmpty()) {
						error = new ErrorStruct();
						error.setErrorKey(MsgEntry.ENVELOP_DEJA_VENTILLE);
						msg = HandlerJSFMessage.getErrorMessage(MsgEntry.ENVELOP_DEJA_VENTILLE);
						msg = HandlerJSFMessage.formatMessage(msg,
								new Object[] { envelop.getGroupDest(), envelop.getGroupNat() });
						error.setErrorMessage(msg);
						getImportErrorList().add(error);
					} else {
						controlLbImportData(lb);

					}

				}
			}
		}
		return (getImportErrorList().isEmpty());
	}

	private void controlLbImportData(LigneBudgetaireAE lb) {
		int serviceIndex = 0;
		int programIndex = 0;
		int destinationIndex = 0;

		IErrorStruct error = null;
		List<String> destList = getcodeDestList(lb.getExercice(), lb.getGroupDest(), lb.getGroupNat(),
				lb.getCodeBudget());
		String msg = null;
		logger.debug("servList: " + getcodeServList());
		logger.debug("service: " + lb.getServiceSE().getCode());

		serviceIndex = getcodeServList().indexOf(lb.getServiceSE().getCode());
		programIndex = getcodeProgList().indexOf(lb.getProgSE().getCode());
		destinationIndex = destList.indexOf(lb.getDestination());

		logger.debug("List de destination: " + destList);
		logger.debug("destination excel: " + lb.getDestination());

		if (serviceIndex == -1) {
			error = new ErrorStruct();
			error.setErrorKey(MsgEntry.SERVICE_INEXISTANT_MSG);
			msg = HandlerJSFMessage.getErrorMessage(MsgEntry.SERVICE_INEXISTANT_MSG);
			msg = HandlerJSFMessage.formatMessage(msg, new Object[] { lb.getServiceSE().getCode() });
			error.setErrorMessage(msg);
			getImportErrorList().add(error);

		}
		if (programIndex == -1) {
			error = new ErrorStruct();
			error.setErrorKey(MsgEntry.PROGRAM_INEXISTANT_MSG);
			msg = HandlerJSFMessage.getErrorMessage(MsgEntry.PROGRAM_INEXISTANT_MSG);
			msg = HandlerJSFMessage.formatMessage(msg, new Object[] { lb.getProgramme() });
			error.setErrorMessage(msg);
			getImportErrorList().add(error);

		}
		if (destinationIndex == -1) {
			error = new ErrorStruct();
			error.setErrorKey(MsgEntry.DESTINATION_INEXISTANT_MSG);
			msg = HandlerJSFMessage.getErrorMessage(MsgEntry.DESTINATION_INEXISTANT_MSG);
			msg = HandlerJSFMessage.formatMessage(msg, new Object[] { lb.getDestination() });
			error.setErrorMessage(msg);
			getImportErrorList().add(error);

		}

	}

	public void setImportErrorList(List<IErrorStruct> importErrorList) {
		this.importErrorList = importErrorList;
	}

	public List<IErrorStruct> getImportErrorList() {
		return importErrorList;
	}

	public void simulateImport() {
		if (logger.isDebugEnabled()) {
			logger.debug("simulateImport() - start"); //$NON-NLS-1$
		}

		try {
			if (!controlImportEnvelop())
				return;
			List<EnvelopBudg> envList = getBudget().getListEnvBudg();
			EnvelopBudg envelop = null;
			LigneBudgetaireAE lb = null;
			int exercice = getBudget().getExercice();
			String budg = getBudget().getCodeBudget();
			int index = 0;

			for (IImportedStruct item : importList) {
				envelop = ((ImportStruct) item).createEnvelopBudg();
				envelop.setExercice(exercice);
				envelop.setExercice(exercice);
				envelop.setCodeBudget(budg);

				index = envList.indexOf(envelop);
				envelop = envList.get(index);
				if (logger.isInfoEnabled()) {
					logger.info("simulateImport() - EnvelopBudg envelop=" + envelop); //$NON-NLS-1$
				}

				lb = ((ImportStruct) item).createLigneBudgetaire();
				lb.setExercice(exercice);
				lb.setExercice(exercice);
				lb.setCodeBudget(budg);
				lb.setEnvelop(envelop);
				if (logger.isInfoEnabled()) {
					logger.info("simulateImport() - LigneBudgetaireAE lb=" + lb); //$NON-NLS-1$
				}

				envelop.getListLigneBudg().add(lb);

			}
			importTree = createAdressageTree(importTree);

			onNodeImportExpand();
		} catch (Exception e) {
			logger.error("simulateImport()", e); //$NON-NLS-1$

			e.printStackTrace();
		}

		if (logger.isDebugEnabled()) {
			logger.debug("simulateImport() importTree= " + importTree + " - end"); //$NON-NLS-1$
		}
	}

	public void closeImportFile() {
		try {
			if (getImportFile() == null)
				return;
			getImportFile().close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public void setImportTree(TreeNode importTree) {
		this.importTree = importTree;
	}

	public TreeNode getImportTree() {
		return importTree;
	}

	public void setImportFile(InputStream importFile) {
		this.importFile = importFile;
	}

	public InputStream getImportFile() {
		return importFile;
	}

	public void onExerciceChange() {
		super.onExerciceChange();
		setImportErrorList(null);
		setImportList(null);
		if(Integer.valueOf(getSearchBean().getExercice())>=2024)
			getLoaderBean().setListNoLtr(new ArrayList<String>(Arrays.asList("3","4","5","6","7","8","9","10","11","12")));
		else {
			getLoaderBean().setListNoLtr(new ArrayList<String>(Arrays.asList("3","4","5","6","7","8","9","10")));
		}

	}

	public void validateImport() {
		List<EnvelopBudg> envList = getBudget().getListEnvBudg();
		if ((envList == null) || (envList.isEmpty()))
			return;
		try {
			budgetPluriService.validerImport(envList);
			HandlerJSFMessage.addInfo(MsgEntry.SUCCES);
		} catch (Exception e) {
			e.printStackTrace();
			HandlerJSFMessage.addErrorMessageFromException(MsgEntry.FAILED, e);
		}
	}

	public void resetImport() {
		setImportList(null);
		setImportErrorList(null);

	}

	private List<String> getcodeServList() {
		List<SimpleEntity> servList = getLoaderBean().getListService();
		String result = null;
		List<String> codeServList = new ArrayList<String>();
		for (SimpleEntity item : servList) {
			result = item.getCode();
			codeServList.add(result);
		}

		return codeServList;

	}

	private List<String> getcodeProgList() {

		List<SimpleEntity> progList = getLoaderBean().getListPrograme();
		String result = null;
		List<String> codeProgList = new ArrayList<String>();
		for (SimpleEntity item : progList) {
			result = item.getCode();
			codeProgList.add(result);
		}
		return codeProgList;
	}

	private List<String> getcodeDestList(int exercice, String groupDest, String groupNat, String codeBudget) {

		String result = null;
		List<ComplexEntity> listDestNat = loadDestNatList(exercice, groupDest, groupNat, codeBudget);
		List<String> codeDestList = new ArrayList<String>();
		for (ComplexEntity item : listDestNat) {
			result = item.getEntity1().getCode();
			codeDestList.add(result);
		}
		return codeDestList;
	}

	public StreamedContent getXlsEditionBr() {
		StreamedContent returnStreamedContent = null;
		ExcelHandler excel = new ExcelHandler(ExcelModelEnum.EDITION_BI_BR, null);
		returnStreamedContent = excel.getExcelFile();
		if (logger.isDebugEnabled()) {
			logger.debug("getXlsEdition() - end"); //$NON-NLS-1$
		}
		return returnStreamedContent;

	}

	public void setData(NoDataBudgEntity data) {
		this.data = data;
	}

	public NoDataBudgEntity getData() {
		if (data == null)
			data = new NoDataBudgEntity();
		loadNoDataBudg();
		return data;
	}

	public NoDataBudgEntity loadNoDataBudg() {
		if (data == null) {
			data = new NoDataBudgEntity();
		}
		// NoDataBudgEntity noBudg= new NoDataBudgEntity();
		data.setDotation(budgetPluriService.getNoDataBudg1(getExercice(), "68%"));
		data.setReprise(budgetPluriService.getNoDataBudg2(getExercice(), "78%", "7813"));
		data.setValeur_compt(budgetPluriService.getNoDataBudg1(getExercice(), "656%"));
		data.setProduit_cession(budgetPluriService.getNoDataBudg1(getExercice(), "775%"));
		data.setSubv_inves(budgetPluriService.getNoDataBudg4(getExercice(), "7813"));
		data.setRembours_caution(budgetPluriService.getNoDataBudg3(getExercice(), "165%", "D"));
		data.setCautio_recu(budgetPluriService.getNoDataBudg3(getExercice(), "165%", "R"));
		return data;
	}

	public void closeSituationPatriDialog() {

		DialogHelper.closeDialog(null);

	}

	public void setGroupDestCpList(List<ComplexEntity> groupDestCpList) {
		this.groupDestCpList = groupDestCpList;
	}

	public List<ComplexEntity> getGroupDestCpList() {

			if (groupDestCpList != null)
				return groupDestCpList;
			groupDestCpList = constructGroupDestCpList();
//			 logger.info("B----public List<ComplexEntity>:  getGroupDestCpList()-----"+groupDestCpList.size());

			if (groupDestCpList != null)
				for (ComplexEntity broupdest : groupDestCpList) {
					SimpleEntity entity = null;
					if (broupdest == null)
						continue;
					// Groupe destination
					entity = budgetPluriService.getDestinationGroupe(broupdest.getEntity1().getCode());
					if (entity != null)
						broupdest.getEntity1().setDesignation(entity.getDesignation());
					// Groupe nature
					entity = budgetPluriService.getNatureGroupe(broupdest.getEntity2().getCode());
					if (entity != null)
						broupdest.getEntity2().setDesignation(entity.getDesignation());
					// programme
				}
//			 logger.info("C----public List<ComplexEntity>	 getGroupDestCpList()-----"+groupDestCpList.size());

			return groupDestCpList;


	}

	private List<ComplexEntity> constructGroupDestCpList() {
		List<ComplexEntity> result = null;
		ComplexEntity entity = null;
		result = new ArrayList<ComplexEntity>();
		List<CreditPaiement> cpList = getListCreditPaiement();
		if (cpList == null)
			return result;
//		 logger.info("D----public List<ComplexEntity> getGroupDestCpList()-----"+cpList.size());

		for (CreditPaiement credit : cpList) {

//			 logger.info("F----public List<ComplexEntity>		 getGroupDestCpList()-----"+credit.getGroupDest()+"-"+credit.getGroupNat());

			entity = new ComplexEntity(credit.getGroupDest(), credit.getGroupNat());
			if (!result.contains(entity))
				result.add(entity);
		}
		return result;
	}

	public void setDestinationList(List<SimpleEntity> destinationList) {
		this.destinationList = destinationList;
	}

	public List<SimpleEntity> getDestinationList(int exercice, String groupNat) {
		Integer niveauDest = getDepNivDestInt();
		logger.info("getDestinationList: exercice={},groupNat={},niveauDest={} ", exercice, groupNat, niveauDest);
		if (destinationList == null)
			destinationList = budgetPluriService.getDestinationList(exercice, groupNat, niveauDest);

		logger.info("getDestinationList: " + destinationList.size() + " - end");
		return destinationList;
	}

	/**
	 * @return the listCreditPaiement
	 */
	public List<CreditPaiement> getListCreditPaiement() {

		String groupNat = getCodeGroupNat(getGroupDestNatCp());
//		 logger.debug("getListCreditPaiement: groupNat={},		 isCommonRequiredDone={}",groupNat,isCommonRequiredDone());
		if (listCreditPaiement == null) {
			if (isCommonRequiredDone())
				listCreditPaiement = budgetPluriService.loadCreditPaiement(getExercice(),
						getSearchBean().getCodeBudget(), groupNat);

		}

		return listCreditPaiement;
	}

	/**
	 * @param listCreditPaiement
	 *            the listCreditPaiement to set
	 */
	public void setListCreditPaiement(List<CreditPaiement> listCreditPaiement) {
		this.listCreditPaiement = listCreditPaiement;
	}

	public void setGroupDestNatCp(ComplexEntity groupDestNatCp) {
		this.groupDestNatCp = groupDestNatCp;
		positionnerDepNivDest();
	}

	public ComplexEntity getGroupDestNatCp() {
		return groupDestNatCp;
	}

	public double getTotalMntCpDest() {
		BigDecimal mnt = new BigDecimal(0);
		double result = 0;

		if (getListBudgetCpDest() == null)
			return result;
		for (BudgetCpDest bcpDest : getListBudgetCpDest()) {
			// logger.info("getTotalMntCpDest : bcpDest "+bcpDest);
			mnt = bcpDest.getMontant();
			if (mnt != null)
				result += mnt.doubleValue();
		}
		logger.info("getTotalMntCpDest : result " + result);
		return Helper.round(result, 2);
	}

	public boolean isDepassementTotalCpDest() {
		if (getTotalMntCp() < getTotalMntCpDest())
			return true;
		return false;
	}

	public double getTotalMntCp() {
		double result = 0;
		if (getListCreditPaiement() == null)
			return result;
		for (CreditPaiement cp : getListCreditPaiement()) {
			result += cp.getMontant();
		}
		return Helper.round(result, 2);
	}

	public boolean isMntCpDestEqual() {
		return (getTotalMntCp() == getTotalMntCpDest());
	}

	public void setEtatCpOuvert(Boolean etatCpOuvert) {
		this.etatCpOuvert = etatCpOuvert;
	}

	public boolean isEtatCpOuvert() {
		if (etatCpOuvert == null) {
			EtatEnum etatcp = getEtatCpBudg();
			etatCpOuvert = etatcp == EtatEnum.OUVERT;
			logger.debug("etatcp={} -> etatCpOuvert={}",etatcp,etatCpOuvert);
		}
		
		return etatCpOuvert;

	}

	public String fermer() {

		String returnString = BudgetHelper.getNavigationBean().goToEditionOutcome();
		setMode(null);
		return returnString;
	}

	public void setListCpGestionnaire(List<CpGestionnaire> listCpGestionnaire) {
		this.listCpGestionnaire = listCpGestionnaire;
	}

	public List<CpGestionnaire> getListCpGestionnaire() {
		return listCpGestionnaire;
	}

	public double getTotalMntCpGest() {
		BigDecimal mnt = new BigDecimal(0);
		double result = 0;
		if (getListCpGestionnaire() == null)
			return result;
		for (CpGestionnaire cpGest : getListCpGestionnaire()) {
			mnt = cpGest.getBudgetInitial();
			if (mnt != null)
				result += mnt.doubleValue();
		}
		return Helper.round(result, 2);
	}

	public boolean isDepassementTotalCpGest() {
		double totalMntCp = getTotalMntCp(), totalMntCpGest = getTotalMntCpGest();
		// logger.debug("isDepassementTotalCpGest:
		// totalMntCp="+totalMntCp+",totalMntCpGest= "+totalMntCpGest);
		if (totalMntCp < totalMntCpGest)
			return true;
		return false;
	}

	public boolean isMntCpGestEqual() {
		double mnt1 = getTotalMntCp();
		double mnt2 = getTotalMntCpGest();
		logger.debug("isMntCpGestEqual: TotalMntCp={}, TotalMntCpGest={} ",mnt1,mnt2);
		return (mnt1 == mnt2);
	}

	public void saveCreditParGestionnaire() {

		try {
			if (getListCpGestionnaire() != null) {
				updateCpGestionnaire(getListCpGestionnaire());
				budgetPluriService.saveCpGestionnaire(getListCpGestionnaire());
			}
			searchCpParGestionnaire();
			HandlerJSFMessage.addInfo(MsgEntry.SUCCES);
		} catch (Exception e) {
			HandlerJSFMessage.addErrorMessage(getErrorMessageFromException(MsgEntry.FAILED, e));
			e.printStackTrace();
		}

	}

	private void updateCpGestionnaire(List<CpGestionnaire> list) {
		if (list == null)
			return;
		CpGestionnaire first = list.get(0);
		Integer maxNoLbgCp = budgetPluriService.getMaxNoLbgCp(first.getExerciceCP(), first.getCodeBudget());
		for (CpGestionnaire cpGest : list) {
			cpGest.setCreditOuvert(cpGest.getBudgetInitial().doubleValue());
			cpGest.setDispoCo(cpGest.getCreditOuvert() - cpGest.getEjReserve());
			if (cpGest.getNoLbgCp() == null) {
				maxNoLbgCp++;
				cpGest.setNoLbgCp(maxNoLbgCp);
			}
		}

	}

	public void updateBudgetEtatCp() {
		int exercice = Helper.stringToInt(getSearchBean().getExercice());
		budgetPluriService.updateBudgetEtatCp(exercice, EtatEnum.VALIDE.getEtat());
		setEtatCpOuvert(false);
	}

	public void setGestParamList(List<CpGestionnaire> gestionnaireList) {
		this.gestParamList = gestionnaireList;
	}

	public void searchDeploiement() {
		try {

			logger.info("searchDeploiement  - start");
			List<CpGestionnaire> currentList = null;
			if (getDeploiementData() == null) {
				setDeploiementData(new HashMap<String, List<CpGestionnaire>>());
			}

			int exerciceCp = getExercice();
			String codeBudget = getSearchBean().getCodeBudget();
			String groupNat = getCodeGroupNat(getGroupDestNatCp());
			currentList = getDeploiementData().get(groupNat);
			if (currentList == null) {
				logger.info("searchDeploiement  Récuperer CpGestionnaireList pour groupNat {}", groupNat);
				currentList = budgetPluriService.getCpGestionnaireList(exerciceCp, codeBudget, groupNat);
				completeData(currentList);
				loadlibelleGestionnaire(currentList);
				getDeploiementData().put(groupNat, currentList);
			}

			setListCpGestionnaire(currentList);
			setSearchDepExecuted(true);
			logger.info("searchDeploiement: groupNat={}  - list.size()={} - end", groupNat,
					((currentList != null) ? currentList.size() : 0));
		} catch (Exception e) {
			HandlerJSFMessage.addErrorMessage(getErrorMessageFromException(MsgEntry.FAILED, e));
			e.printStackTrace();
		}
	}

	private void completeData(List<CpGestionnaire> list) {
		String codeBudget = getSearchBean().getCodeBudget();
		String groupNat = getCodeGroupNat(getGroupDestNatCp());
		List<CpGestionnaire> data = budgetPluriService.getCpGestData(getExercice(), codeBudget, groupNat);
		logger.info("completeData: data=" + data);
		CpGestionnaire item = null;
		for (CpGestionnaire cpGest : list) {
			logger.info("completeData: cpGest=" + cpGest);
			int index = data.indexOf(cpGest);
			logger.debug("completeData : index=" + index);
			if (index != -1) {
				item = data.get(index);
				cpGest.setEjEnCours(item.getEjEnCours());
				cpGest.setEjPrecedent(item.getEjPrecedent());
				cpGest.setDpAdmis(item.getDpAdmis());
			}
			cpGest.setLibelleGroupNat(loadLibelleGroupNat(groupNat));
		}

	}

	public void setObjetModification(String objetModification) {
		this.objetModification = objetModification;
	}

	public String getObjetModification() {
		return objetModification;
	}

	public double getTotalCOTempCpGest() {
		double result = 0d;
		if (getListCpGestionnaire() != null)
			for (CpGestionnaire cpGest : getListCpGestionnaire()) {
				result += cpGest.getCreditOuvertTemp();
			}
		return Helper.round(result, 2);
	}

	public double getTotalCreditOuvertCpGest() {
		double result = 0d;
		if (getListCpGestionnaire() != null)
			for (CpGestionnaire cpGest : getListCpGestionnaire()) {
				result += cpGest.getCreditOuvert();
			}
		return Helper.round(result, 2);
	}

	public double getTotalTotalReserveCpGest() {
		double result = 0d;
		if (getListCpGestionnaire() != null)
			for (CpGestionnaire cpGest : getListCpGestionnaire()) {
				result += cpGest.getTotalReserve();
			}
		return Helper.round(result, 2);
	}

	public double getTotalResteCpGest() {
		double result = 0d;
		if (getListCpGestionnaire() != null)
			for (CpGestionnaire cpGest : getListCpGestionnaire()) {
				result += cpGest.getReste();
			}
		return Helper.round(result, 2);
	}

	public double getTotalMontantCpGest() {
		double result = 0d;
		if (getListCpGestionnaire() != null)
			for (CpGestionnaire cpGest : getListCpGestionnaire()) {
				result += cpGest.getMontantTemp();
			}
		return Helper.round(result, 2);
	}

	public double getTotalDpAdmisCpGest() {
		double result = 0d;
		if (getListCpGestionnaire() != null)
			for (CpGestionnaire cpGest : getListCpGestionnaire()) {
				result += cpGest.getDpAdmis();
			}
		return Helper.round(result, 2);
	}

	public double getTotalDisponibleCpGest() {
		double result = 0d;
		if (getListCpGestionnaire() != null)
			for (CpGestionnaire cpGest : getListCpGestionnaire()) {
				result += cpGest.getDisponible();
			}
		return Helper.round(result, 2);
	}

	public double getTotalEjEnCoursCpGest() {
		double result = 0d;
		if (getListCpGestionnaire() != null)
			for (CpGestionnaire cpGest : getListCpGestionnaire()) {
				result += cpGest.getEjEnCours();
			}
		return Helper.round(result, 2);
	}

	public double getTotalEjPrecedentCpGest() {
		double result = 0d;
		if (getListCpGestionnaire() != null)
			for (CpGestionnaire cpGest : getListCpGestionnaire()) {
				result += cpGest.getEjPrecedent();
			}
		return Helper.round(result, 2);
	}

	public double getTotalEjReserveCpGest() {
		double result = 0d;
		if (getListCpGestionnaire() != null)
			for (CpGestionnaire cpGest : getListCpGestionnaire()) {
				result += cpGest.getEjReserve();
			}
		return Helper.round(result, 2);
	}

	public List<BudgetCpDest> getAllListCpDest() {

		return getVentilationParDest(getExercice(), getSearchBean().getCodeBudget(), null, null);
	}

	public List<BudgetCpDest> getAllListAeDest() {

		return budgetPluriService.getAeDestinationList(getExercice(), "D");
	}

	public boolean isActiveEnregDeploiement() {
		// logger.info("1----------public boolean
		// isActiveEnregDeploiement()---------getObjetModification="+getObjetModification());
		if (getObjetModification() == null)
			return false;
		// logger.info("2----------public boolean
		// isActiveEnregDeploiement()---------getObjetModification="+getObjetModification());
		if (getObjetModification().trim().isEmpty())
			return false;
		// logger.info("3----------public boolean
		// isActiveEnregDeploiement()---------getObjetModification="+getObjetModification());
		if (!isExistMntCpGestNotNull())
			return false;
		// logger.info("1----------public boolean
		// isActiveEnregDeploiement()---------");
		if (getTotalMontantCpGest() != 0)
			return false;
		// logger.info("2----------public boolean
		// isActiveEnregDeploiement()---------");
		if (isExsitRestCpGestNegatif())
			return false;
		// logger.info("3----------public boolean
		// isActiveEnregDeploiement()---------");
		if (isExistDispCpGestNegatif())
			return false;
		// logger.info("4----------public boolean
		// isActiveEnregDeploiement()---------");
		return true;

	}

	private boolean isExistDispCpGestNegatif() {
		// double result = 0d;
		if (getListCpGestionnaire() != null)
			for (CpGestionnaire cpGest : getListCpGestionnaire()) {
				if (cpGest.getDisponible() >= 0)
					continue;
				return true;
			}
		return false;
		// return BudgetHelper.round(result, 2);
	}

	private boolean isExsitRestCpGestNegatif() {
		// double result = 0d;
		if (getListCpGestionnaire() != null)
			for (CpGestionnaire cpGest : getListCpGestionnaire()) {
				// result += cpGest.getReste();
				// logger.info("3----------public boolean
				// isExsitRestCpGestNegatid()---------cpGest.getReste()="+cpGest.getReste());
				if (cpGest.getReste() >= 0)
					continue;
				return true;
			}
		// return BudgetHelper.round(result, 2);
		return false;
	}

	private boolean isExistMntCpGestNotNull() {

		if (getListCpGestionnaire() != null)
			for (CpGestionnaire cpGest : getListCpGestionnaire()) {

				if (cpGest.getMontantTemp() == 0)
					continue;
				return true;
			}

		return false;
	}

	/**
	 * Permet d'enregistrer les modifications de la répartition des CP par
	 * gestionnaire ainsi qu'une trace des modifications.
	 */
	public void saveDeploiement() {
		logger.debug("saveDeploiement");
		List<CpGestionnaire> updatedList = new ArrayList<>();
		CpModificationItem item = null;
		CpModification modification = null;
		Integer maxNoLbgCp = null;
		boolean first = true;
		try {

			for (CpGestionnaire cpGest : getDeploiementRecap()) {
				if (first) {
					modification = createBudgCpModification(cpGest.getExerciceCP(), cpGest.getCodeBudget(),
							getObjetModification());
					maxNoLbgCp = budgetPluriService.getMaxNoLbgCp(cpGest.getExerciceCP(), cpGest.getCodeBudget());
					first = false;
				}
				if (cpGest.getNoLbgCp() == null) {
					maxNoLbgCp++;
					cpGest.setNoLbgCp(maxNoLbgCp);
				}
				cpGest.setCreditOuvert(cpGest.getCreditOuvertTemp());
				updatedList.add(cpGest);
				item = createBudgCpModificationLigne(modification, cpGest);
				item.setMontant(cpGest.getMontant());
			}

			budgetPluriService.saveDeploiement(modification, updatedList);
			resetDeploiement();
			BudgetHelper.addSpecialSuccesMsg(MsgEntry.DEPLOI_SUCCES, new Object[] { modification.getNumero() });
		} catch (Exception e) {
			e.printStackTrace();
			HandlerJSFMessage.addErrorMessage(getErrorMessageFromException(MsgEntry.FAILED, e));

		}

	}

	public void resetDeploiement() {
		setDeploiementData(null);
		setListCpGestionnaire(null);
		setObjetModification(null);
		setCpModificationList(null);
		setCpItemModifList(null);

	}

	private CpModificationItem createBudgCpModificationLigne(CpModification modification, CpGestionnaire cpGest) {
		CpModificationItem item = new CpModificationItem(modification);
		item.setNoLbgCp(cpGest.getNoLbgCp());

		modification.getItems().add(item);
		return item;
	}

	private CpModification createBudgCpModification(Integer exercice, String codeBudg, String objetModification)
			throws MissingConfiguration {
		CpModification modification = new CpModification();
		Integer numSeq = null;
		// TODO Récuperer un numéro seq / exercice
		numSeq = BudgetHelper.getCommonService().getNumSeq(exercice, null, Constant.NO_VIR_CP_COMPTEUR_NAME);

		modification.setNumero(numSeq);
		modification.setCodeBudget(codeBudg);
		modification.setExercice(exercice);
		modification.setObjet(objetModification);
		modification.setTrace(Helper.createTrace());
		logger.debug("createBudgCpModification: {}", modification);
		return modification;
	}

	public List<BudgetCpDest> getAllListRecetDest() {

		return budgetPluriService.getRecetDestList(getExercice(), "R");
	}

	public void ajusterVentCpGest() {
		double mntDepassement = getDepassementTotalCpGest();
		String cpGestIndex = Helper.getRequestParameter("cpGestIndex");
		CpGestionnaire cpGestionnaire = getCurrentCpGestionnaire(Helper.stringToInt(cpGestIndex),
				getListCpGestionnaire());
		cpGestionnaire.setBudgetInitial(
				new BigDecimal(Helper.round(cpGestionnaire.getBudgetInitial().doubleValue() - mntDepassement, 2)));

	}

	private CpGestionnaire getCurrentCpGestionnaire(int cpGestIndex, List<CpGestionnaire> listCpGestionnaire) {
		if (listCpGestionnaire == null)
			return null;
		if (cpGestIndex == -1)
			return null;
		return listCpGestionnaire.get(cpGestIndex);
	}

	public double getDepassementTotalCpGest() {
		double totalMntCp = getTotalMntCp(), totalMntCpGest = getTotalMntCpGest();
		return totalMntCpGest - totalMntCp;

	}

	public void ajusterVentCpDest() {
		double mntDepassement = getDepassementTotalCpDest();
		String cpDestIndex = Helper.getRequestParameter("cpDestIndex");
		BudgetCpDest budgetCpDest = getCurrentBudgetCpDest(Helper.stringToInt(cpDestIndex), getListBudgetCpDest());
		budgetCpDest
				.setMontant(new BigDecimal(Helper.round(budgetCpDest.getMontant().doubleValue() - mntDepassement, 2)));

	}

	public double getDepassementTotalCpDest() {
		double totalMntCp = getTotalMntCp(), totalMntCpDest = getTotalMntCpDest();
		return totalMntCpDest - totalMntCp;

	}

	private BudgetCpDest getCurrentBudgetCpDest(int cpDestIndex, List<BudgetCpDest> listBudgetCpDest) {
		if (listBudgetCpDest == null)
			return null;
		if (cpDestIndex == -1)
			return null;
		return listBudgetCpDest.get(cpDestIndex);
	}

	public void setBudgModifList(List<BudgModification> budgModifList) {
		this.budgModifList = budgModifList;
	}

	public List<BudgModification> getBudgModifList() {

		if (budgModifList == null) {
			if (isCommonRequiredDone())
				budgModifList = loadBudgModifList();
		}

		return budgModifList;
	}

	private List<BudgModification> loadBudgModifList() {
		List<BudgModification> result = null;
		List<BudgModification> allList = null;
		int exercice = getExercice();
		if (exercice == 0)
			return null;
		String typeModif = ModifEnum.DemandeModif.getCode();

		allList = budgetPluriService.getBudgModifList(exercice, typeModif, null);
		if ((allList == null) || (allList.isEmpty()))
			result = null;
		else {
			List<EtatEnum> constraints = getEtatCpConstraint();
			if ((constraints == null) || (constraints.isEmpty()))
				result = allList;
			else {
				result = new ArrayList<BudgModification>();
				for (BudgModification budgModif : allList) {
					if (!constraints.contains(budgModif.getEtatCp()))
						continue;
					result.add(budgModif);
				}
			}
		}

		return result;
	}

	public void setNumeroBudgModif(Integer numeroBudgModif) {
		List<BudgModification> brEnCoursList = null;
		List<EtatEnum> etatsList = new ArrayList<EtatEnum>();
		etatsList.add(EtatEnum.ENCOURS);
		etatsList.add(EtatEnum.ORDO);
		BudgModification budgModif = null;
		setSelectedBudgModif(null);
		setBudgRecEnCoursList(null);
		this.numeroBudgModif = numeroBudgModif;
		if (numeroBudgModif == null)
			return;
		if (numeroBudgModif == 0)
			return;
		budgModif = findSelectedBudgModification(numeroBudgModif, getBudgModifList());
		brEnCoursList = findBrWithEtatCP(getBudgModifList(), etatsList, budgModif);
		setSelectedBudgModif(budgModif);
		setBudgRecEnCoursList(brEnCoursList);
	}

	private BudgModification findSelectedBudgModification(Integer numero, List<BudgModification> list) {
		if (numero == null)
			return null;
		if (list == null)
			return null;
		for (BudgModification budgModif : list) {
			if (budgModif.getNumero() != numero)
				continue;
			return budgModif;
		}
		return null;
	}

	/**
	 * 
	 * @param originList
	 * @param etatsList
	 * @param execludedElement
	 * @return la liste des br depuis originList ayant un etat cp contenu dans
	 *         etatsList * ce br doit etre different de execludedElement
	 */
	private List<BudgModification> findBrWithEtatCP(List<BudgModification> originList, List<EtatEnum> etatsList,
			BudgModification execludedElement) {
		List<BudgModification> result = new ArrayList<BudgModification>();
		if (originList == null)
			return null;
		if (etatsList == null)
			return null;
		for (BudgModification budgModif : originList) {
			if (execludedElement == budgModif)
				continue;
			if (!etatsList.contains(budgModif.getEtatCp()))
				continue;

			result.add(budgModif);

		}
		if (result.isEmpty())
			return null;

		logger.info("-------private List<BudgModification> findBrEtatCpIn(), result.size" + result.size());
		return result;
	}

	public Integer getNumeroBudgModif() {
		return numeroBudgModif;
	}

	public boolean isGestUnique() {
		String gestUnique = getGestUnique();
		return ((gestUnique != null) && (!gestUnique.trim().isEmpty()));
	}

	public void setSaisieBrList(List<CpGestionnaire> saisieBrList) {
		this.saisieBrList = saisieBrList;
	}

	public List<CpGestionnaire> getSaisieBrList() {
		return saisieBrList;
	}

	/**
	 * Permet de saisir un budget rectuficatif sur le budget initiale: 1-
	 * Chargement de la ventillation initiale pour le gestionnaire unique (si
	 * non RESERVE) (budgcpgest) ainsi que les br précédants
	 * (budgmodificationcpligne) 2- saisir les br par envelop (nat_grp) 3-
	 * Acomplir la ventillation par destination
	 */
	public void searchSaisieBr() {
		logger.debug("searchSaisieBr: start");
		List<CpGestionnaire> ventillationByGest = null;
		int exercice = getExercice();
		String codeBudget = getSearchBean().getCodeBudget();
		String gestionnaire = null;
		try {

			gestionnaire = getPreparedGestUnique();

			ventillationByGest = budgetPluriService.getCpGestionnaireList(exercice, codeBudget, null, gestionnaire);
			String libelle = null;
			double cumuleBrAvant = 0, variationBR = 0;
			if (ventillationByGest != null)
				for (CpGestionnaire cpGest : ventillationByGest) {
					libelle = loadLibelleGroupNat(cpGest.getGroupNat());
					cpGest.setLibelleGroupNat(libelle);
					cumuleBrAvant = budgetPluriService.getCumuleBrAvant(getSelectedBudgModif(), cpGest.getNoLbgCp());
					cpGest.setCumuleBrAvant(cumuleBrAvant);
					logger.debug("searchSaisieBr: cumuleBrAvant=" + cumuleBrAvant);
					logger.debug("searchSaisieBr: isEtatCpValide=" + getSelectedBudgModif().isEtatCpValide());
					if (!getSelectedBudgModif().isEtatCpValide()) {
						cpGest.setVaraitionBR(new BigDecimal(cpGest.getBudgetRectificatif()));
					} else {
						variationBR = budgetPluriService.getMontantBr(getSelectedBudgModif(), cpGest.getNoLbgCp());
						logger.debug("searchSaisieBr: variationBR=" + variationBR);
						cpGest.setVaraitionBR(new BigDecimal(variationBR));
					}
				}

			setSaisieBrList(ventillationByGest);
		} catch (Exception e) {
			HandlerJSFMessage.addErrorMessage(getErrorMessageFromException(MsgEntry.FAILED, e));
			e.printStackTrace();
		}
		logger.debug("searchSaisieBr: end");
	}

	private String loadLibelleGroupNat(String groupNat) {
		String libelle = null;
		SimpleEntity entite = budgetPluriService.getNatureGroupe(groupNat);
		if (entite != null)
			libelle = entite.getDesignation();

		return libelle;
	}

	public void saveCreditBR() {
		if (logger.isDebugEnabled()) {
			logger.debug("saveCreditBR() - start"); //$NON-NLS-1$
		}

		List<CpGestionnaire> updatedList = null;

		try {
			updatedList = pepareBrList();
			saveCreditBR(updatedList);
			updateEtatBudgModification(EtatEnum.ENCOURS);
			setBudgModifList(null);
			HandlerJSFMessage.addInfo(MsgEntry.SUCCES);
		} catch (Exception e) {
			logger.error("saveCreditBR()", e); //$NON-NLS-1$

			HandlerJSFMessage.addErrorMessage(getErrorMessageFromException(MsgEntry.FAILED, e));
			e.printStackTrace();
		}

		if (logger.isDebugEnabled()) {
			logger.debug("saveCreditBR() - end"); //$NON-NLS-1$
		}
	}

	private void saveCreditBR(List<CpGestionnaire> updatedList) {
		if (logger.isDebugEnabled()) {
			logger.debug("saveCreditBR(List<CpGestionnaire>) - start"); //$NON-NLS-1$
		}

		budgetPluriService.saveCreditBR(updatedList, null);

		if (logger.isDebugEnabled()) {
			logger.debug("saveCreditBR(List<CpGestionnaire>) - end"); //$NON-NLS-1$
		}
	}

	private List<ModificationCpLigne> createBrModificationList(List<CpGestionnaire> updatedList) {
		List<ModificationCpLigne> modificationList = new ArrayList<ModificationCpLigne>();
		ModificationCpLigne modification = null;

		for (CpGestionnaire cpGest : updatedList) {
			modification = new ModificationCpLigne(getSelectedBudgModif());
			modification.setMontantBR(cpGest.getBudgetRectificatif());
			modification.setNoLbgCp(cpGest.getNoLbgCp());
			modification.setTrace(Helper.createTrace());
			logger.info("createBrModificationList: " + modification);
			modificationList.add(modification);

		}
		return modificationList;
	}

	private List<CpGestionnaire> pepareBrList() {
		List<CpGestionnaire> brList = getSaisieBrList();
		for (CpGestionnaire cpGest : brList) {
			cpGest.setBudgetRectificatif(cpGest.getVaraitionBR().doubleValue());
		}

		return brList;
	}

	public void validerCreditBR() {

		try {
			if (getSaisieBrList() != null) {

			}

			HandlerJSFMessage.addInfo(MsgEntry.SUCCES);
		} catch (Exception e) {
			e.printStackTrace();
			HandlerJSFMessage.addErrorMessageFromException(MsgEntry.FAILED, e);
		}
	}

	public boolean isCompRendGestAction() {

		if (BudgetHelper.getNavigationBean().getCurrentAction() == Action.TABLEAU_BORD_CR_GESTION)
			return true;
		return false;
	}

	public void affichCorrespTabDeBord() {
		searchBudget();
	}

	public void setGestUnique(String gestUnique) {
		this.gestUnique = gestUnique;
	}

	public String getGestUnique() {
		int exercice = getExercice();
		if (gestUnique == null) {
			gestUnique = budgetPluriService.getGestUnique(exercice);
			logger.debug("getGestUnique: exercice=" + exercice + " --> " + gestUnique);
		}
		return gestUnique;
	}

	public void searchCpParDestinationOrigin() {
		boolean isSingleItem = false;
		logger.info("searchCpParDestination : - start");
		try {

			int exerciceCp = getExercice();
			String codeBudget = getSearchBean().getCodeBudget();
			String groupNat = getCodeGroupNat(getGroupDestNatCp());
			logger.info("debut de recherche cp par origine : exerciceCp" + exerciceCp + ", codeBudget=" + codeBudget
					+ ", groupNat=" + groupNat);
			List<BudgetCpDest> list = getVentilationParDest(exerciceCp, codeBudget, groupNat, null);
			if ((list == null) || (list.isEmpty())) {
				list = createBudgetCpDest(exerciceCp, codeBudget, groupNat);
				if ((list != null) && (list.size() == 1))
					isSingleItem = true;
			}

			loadlibelleDestibnation(list);
			if (isSingleItem) {
				list.get(0).setMontant(new BigDecimal(getTotalMntCp()));
			}
			logger.info("fin de recherche cp par origine , resultat= " + (list == null ? 0 : list.size()));
			setListBudgetCpDest(list);

		} catch (Exception e) {
			HandlerJSFMessage.addErrorMessage(getErrorMessageFromException(MsgEntry.FAILED, e));
			e.printStackTrace();
		}
	}

	public double getTotalMntCpOrigin() {
		try {
			Double mnt = BudgetHelper.getMontant(getGroupDestNatCp().getEntity1().getAdditionalValue());
			return mnt;
		} catch (Exception e) {
			// e.printStackTrace();
			return 0d;
		}

	}

	public boolean isDepassTotalCpDestOrigin() {
		if (getTotalMntCpOrigin() < getTotalMntCpDest())
			return true;
		return false;
	}

	public boolean isMntCpDestOriginEqual() {
		return (getTotalMntCpOrigin() == getTotalMntCpDest());
	}

	public void ajusterVentCpDestOrigin() {
		double mntDepassement = getDepassementTotalCpDestOrigin();
		String cpDestIndex = Helper.getRequestParameter("cpDestIndex");
		BudgetCpDest budgetCpDest = getCurrentBudgetCpDest(Helper.stringToInt(cpDestIndex), getListBudgetCpDest());
		double result = Helper.round(budgetCpDest.getMontant().doubleValue() - mntDepassement, 2);
		budgetCpDest.setMontant(new BigDecimal(result));

	}

	public double getDepassementTotalCpDestOrigin() {
		double totalMntCp = getTotalMntCpOrigin(), totalMntCpDest = getTotalMntCpDest();
		return totalMntCpDest - totalMntCp;

	}

	public void saveCreditParDestOrigin() {

		try {
			if (getListBudgetCpDest() != null)
				budgetPluriService.saveBudgetCpParDest(getListBudgetCpDest());
			searchCpParDestinationOrigin();
			HandlerJSFMessage.addInfo(MsgEntry.SUCCES);
		} catch (Exception e) {
			HandlerJSFMessage.addErrorMessage(getErrorMessageFromException(MsgEntry.FAILED, e));
			e.printStackTrace();
		}

	}

	public double getTotalMntCpDestBr() {
		double mnt = 0d;
		double result = 0;

		if (getListBudgetCpDest() == null)
			return result;
		for (BudgetCpDest bcpDest : getListBudgetCpDest()) {
			mnt = bcpDest.getBudgetRectificatif();
			result += mnt;
		}
		// logger.info("getTotalMntCpDestBr: result "+result);
		return Helper.round(result, 2);
	}

	public boolean isDepassTotalCpDestBr() {
		if (getVariationBrAventiller() < getTotalMntCpDestBr())
			return true;
		return false;
	}

	public boolean isMntCpDestBrEqual() {
		return (getVariationBrAventiller() == getTotalMntCpDestBr());
	}

	public void ajusterVentCpDestBr() {
		double mntDepassement = getDepassementTotalCpDestBr();
		String cpDestIndex = Helper.getRequestParameter("cpDestIndex");
		BudgetCpDest budgetCpDest = getCurrentBudgetCpDest(Helper.stringToInt(cpDestIndex), getListBudgetCpDest());
		budgetCpDest.setBudgetRectificatif(Helper.round(budgetCpDest.getBudgetRectificatif() - mntDepassement, 2));

	}

	public double getDepassementTotalCpDestBr() {
		double montantAventiler = getVariationBrAventiller(), totalVentile = getTotalMntCpDestBr();
		return totalVentile - montantAventiler;

	}

	public StreamedContent getXlsTB() {
		StreamedContent returnStreamedContent = null;
		ExcelHandler excel = null;
		try {
			Action action = BudgetHelper.getNavigationBean().getCurrentAction();
			// logger.debug("budget:"+getBudget());
			switch (action) {
			case TABLEAU_BORD_PAR_ENV:
				excel = new ExcelHandler(ExcelModelEnum.TB_ENVELOP_LIST, null);
				break;
			case TABLEAU_BORD_SUIVI_CP:
				excel = new ExcelHandler(ExcelModelEnum.TB_SUIVI_CP, null);
				break;
			case TABLEAU_BORD_COMPTE_DEP:
				excel = new ExcelHandler(ExcelModelEnum.TB_CC_DEPENSE, null);
				break;
			case TABLEAU_BORD_PAR_COMPREC_V2:
				excel = new ExcelHandler(ExcelModelEnum.TB_CC_RECETTE, null);
				break;
			case TABLEAU_BORD_CR_GESTION:
				excel = new ExcelHandler(ExcelModelEnum.TB_COMPT_RENDU_GEST, null);
				break;
			default:
				break;
			}
			returnStreamedContent = excel.getExcelFile();
			if (logger.isDebugEnabled()) {
				logger.debug("getXlsTB() - end"); //$NON-NLS-1$
			}

		} catch (Exception e) {
			HandlerJSFMessage.addErrorMessage(getErrorMessageFromException(MsgEntry.FAILED, e));
			e.printStackTrace();
		}
		return returnStreamedContent;
	}

	public boolean isImprButton() {
		SearchBean searchBean = getSearchBean();
		String codeBudg = searchBean.getCodeBudget();
		String exercice = searchBean.getExercice();
		Date dateArret = searchBean.getDateArret();
		Action action = BudgetHelper.getNavigationBean().getCurrentAction();
		if (action == Action.TABLEAU_BORD_CR_GESTION) {
			if ((codeBudg != null) && (exercice != null) && (dateArret != null)) {
				return true;
			} else {
				return false;
			}
		} else {
			if ((codeBudg != null) && (exercice != null)) {
				return true;
			}
			return false;
		}

	}

	public void searchSuiviCp() {
		if (!checkRequiredSuiviCp())
			return;
		try {
			globalSuiviCp = budgetPluriService.getGlobalSuiviCp(Integer.parseInt(getSearchBean().getExercice()),
					getSearchBean().getCodeBudget(), getSearchBean().getCodeDirection(),
					getSearchBean().getCodeService(), getSearchBean().getCodeFongDest(), getSearchBean().getCodeDest(),
					getSearchBean().getCodeProg(), getSearchBean().getCodeNatureGrp(), getSearchBean().getCodeNature());
		} catch (Exception e) {
			HandlerJSFMessage.addErrorMessage(getErrorMessageFromException(MsgEntry.FAILED, e));
			e.printStackTrace();
		}
	}

	private boolean checkRequiredSuiviCp() {

		boolean isExecNull = false;
		boolean isBudgNull = false;
		boolean isNatGrpNull = false;

		String exec = getSearchBean().getExercice();
		String codeBudg = getSearchBean().getCodeBudget();
		String natGrp = getSearchBean().getCodeNatureGrp();

		isExecNull = (exec == null) ? true : exec.trim().isEmpty();
		isBudgNull = (codeBudg == null) ? true : codeBudg.trim().isEmpty();
		isNatGrpNull = (natGrp == null) ? true : natGrp.trim().isEmpty();

		if ((isExecNull) || (isBudgNull) || (isNatGrpNull)) {
			HandlerJSFMessage.addError(fr.symphonie.util.MsgEntry.REQUIRED_MSG);
			return false;
		}
		return true;

	}

	public double getAntRap() {
		if (globalSuiviCp != null)

			return (Double) globalSuiviCp.get(ant_rap_col);
		return 0d;

	}

	public double getAntDpEmise() {
		if (globalSuiviCp != null)

			return (Double) globalSuiviCp.get(ant_dp_emise_col);
		return 0d;

	}

	public double getAntRae() {
		if (globalSuiviCp != null)

			return (Double) globalSuiviCp.get(ant_rae_col);
		return 0d;

	}

	public double getExecMtEj() {
		if (globalSuiviCp != null)

			return (Double) globalSuiviCp.get(exec_mt_ej_col);
		return 0d;

	}

	public double getExecDpEmise() {
		if (globalSuiviCp != null)

			return (Double) globalSuiviCp.get(exec_dp_emise_col);
		return 0d;

	}

	public double getExecRae() {
		if (globalSuiviCp != null)

			return (Double) globalSuiviCp.get(exec_rae_col);
		return 0d;

	}

	public double getAntCreditAnnul() {
		if (globalSuiviCp != null)

			return (Double) globalSuiviCp.get(ca_col);
		return 0d;

	}

	public String loadSuiviCpAeAnt() {
		String outCome = Action.SUIVI_CP.getOutcome();
		if (!checkRequiredSuiviCp())
			return outCome;
		try {
			setSuiviCpAeAnt(budgetPluriService.getSuiviCpAeAnt(Integer.parseInt(getSearchBean().getExercice()),
					getSearchBean().getCodeBudget(), getSearchBean().getCodeDirection(),
					getSearchBean().getCodeService(), getSearchBean().getCodeFongDest(), getSearchBean().getCodeDest(),
					getSearchBean().getCodeProg(), getSearchBean().getCodeNatureGrp(), getSearchBean().getCodeNature(),
					getTiersOrigineAnt()));

		} catch (Exception e) {
			HandlerJSFMessage.addErrorMessage(getErrorMessageFromException(MsgEntry.FAILED, e));
			e.printStackTrace();
		}
		return outCome;

	}

	public String loadSuiviCpAeExec() {
		String outCome = Action.SUIVI_CP.getOutcome();
		if (!checkRequiredSuiviCp())
			return outCome;
		try {
			setSuiviCpAeExec(budgetPluriService.getSuiviCpAeExec(Integer.parseInt(getSearchBean().getExercice()),
					getSearchBean().getCodeBudget(), getSearchBean().getCodeDirection(),
					getSearchBean().getCodeService(), getSearchBean().getCodeFongDest(), getSearchBean().getCodeDest(),
					getSearchBean().getCodeProg(), getSearchBean().getCodeNatureGrp(), getSearchBean().getCodeNature(),
					getTiersOrigineExec()));
		} catch (Exception e) {
			HandlerJSFMessage.addErrorMessage(getErrorMessageFromException(MsgEntry.FAILED, e));
			e.printStackTrace();
		}

		return outCome;

	}

	public void setSuiviCpAeAnt(List<suiviCpStruct> suiviCpAeAnt) {
		this.suiviCpAeAnt = suiviCpAeAnt;
	}

	public List<suiviCpStruct> getSuiviCpAeAnt() {
		return suiviCpAeAnt;
	}

	public List<suiviCpStruct> getSuiviCpAeExec() {
		return suiviCpAeExec;
	}

	public void setSuiviCpAeExec(List<suiviCpStruct> suiviCpAeExec) {
		this.suiviCpAeExec = suiviCpAeExec;
	}

	public void trsOrgnAntChangeListener(ValueChangeEvent e) {

	}

	public void trsOrgnExecChangeListener(ValueChangeEvent e) {

	}

	public int getTiersOrigineAnt() {
		return tiersOrigineAnt;
	}

	public void setTiersOrigineAnt(int tiersOrigineAnt) {
		this.tiersOrigineAnt = tiersOrigineAnt;
		setSuiviCpAeAnt(null);
	}

	public int getTiersOrigineExec() {
		return tiersOrigineExec;
	}

	public void setTiersOrigineExec(int tiersOrigineExec) {
		this.tiersOrigineExec = tiersOrigineExec;
		setSuiviCpAeExec(null);
	}

	public Map<String, Object> getGlobalSuiviCp() {
		return globalSuiviCp;
	}

	public void setGlobalSuiviCp(Map<String, Object> globalSuiviCp) {
		this.globalSuiviCp = globalSuiviCp;
	}

	public StreamedContent getXlsSuiviCPAnt() {
		StreamedContent returnStreamedContent = null;
		// logger.debug("budget:"+getBudget());
		loadSuiviCpAeAnt();
		ExcelHandler excel = new ExcelHandler(ExcelModelEnum.DETAILS_SUIVICP_ANT, getSuiviCpAeAnt());
		returnStreamedContent = excel.getExcelFile();
		if (logger.isDebugEnabled()) {
			logger.debug("getXlsSuiviCPAnt() - end"); //$NON-NLS-1$
		}
		return returnStreamedContent;

	}

	public StreamedContent getXlsSuiviCPExec() {
		StreamedContent returnStreamedContent = null;
		// logger.debug("budget:"+getBudget());
		loadSuiviCpAeExec();
		ExcelHandler excel = new ExcelHandler(ExcelModelEnum.DETAILS_SUIVICP_EXEC, getSuiviCpAeExec());
		returnStreamedContent = excel.getExcelFile();
		if (logger.isDebugEnabled()) {
			logger.debug("getXlsSuiviCPExec() - end"); //$NON-NLS-1$
		}
		return returnStreamedContent;

	}

	public double getBiSuiviCP() {
		if (globalSuiviCp != null)
			return (Double) globalSuiviCp.get(bi_col);
		return 0d;
	}

	public double getBrAccSuiviCP() {
		if (globalSuiviCp != null)
			return (Double) globalSuiviCp.get(br_acc_col);
		return 0d;
	}

	public double getBrProjetSuiviCP() {
		if (globalSuiviCp != null)
			return (Double) globalSuiviCp.get(br_projet_col);
		return 0d;
	}

	public double getCoSuiviCP() {
		if (globalSuiviCp != null)
			return (Double) globalSuiviCp.get(co_col);
		return 0d;
	}

	public double getDispoServvSuiviCP() {
		if (globalSuiviCp != null)
			return (Double) globalSuiviCp.get(dispo_servv_col);
		return 0d;
	}

	public double getDispoOfficielSuiviCP() {
		if (globalSuiviCp != null)
			return (Double) globalSuiviCp.get(dispo_officiel_col);
		return 0d;
	}

	public double getEjExecSuiviCP() {
		if (globalSuiviCp != null)
			return (Double) globalSuiviCp.get(ej_exec_col);
		return 0d;
	}

	public double getRapiniSuiviCP() {
		if (globalSuiviCp != null)
			return (Double) globalSuiviCp.get(rap_ini_col);
		return 0d;
	}

	public double getApayerSuiviCP() {
		if (globalSuiviCp != null)
			return (Double) globalSuiviCp.get(a_payer_col);
		return 0d;
	}

	public double getDpEmSuiviCP() {
		if (globalSuiviCp != null)
			return (Double) globalSuiviCp.get(dp_em_col);
		return 0d;
	}

	public double getDpPecSuiviCP() {
		if (globalSuiviCp != null)
			return (Double) globalSuiviCp.get(dp_pec_col);
		return 0d;
	}

	private void updateEtatBudget(EtatEnum etat) {
		logger.info("updateEtatBudget: exercice: " + getSearchBean().getExercice());
		int exercice = Helper.stringToInt(getSearchBean().getExercice());
		try {
			budgetPluriService.updateBudgetEtatCp(exercice, etat.getEtat());
			getBudget().setEtatCp(etat);
		} catch (Exception e) {
			HandlerJSFMessage.addErrorMessage(getErrorMessageFromException(MsgEntry.FAILED, e));
			e.printStackTrace();
		}

	}

	public void validerOrdo() {
		// controle sur montant ventil
		if (!controlerMntValOrdoBi(getBudget().getListEnvBudg()))
			return;
		updateEtatBudget(EtatEnum.ORDO);

	}

	private boolean controlerMntValOrdoBi(List<EnvelopBudg> listEnvBudg) {
		double totalVaration = 0d;
		int exercice = getExercice();
		String codeBudget = getSearchBean().getCodeBudget();
		List<BudgetCpDest> listVentCp = null;
		List<String> natGrpCodesList = new ArrayList<String>();
		String msg = null;
		boolean result = true;

		if (listEnvBudg == null)
			return result;
		logger.debug("controlerMntValOrdoBi, listEnvBudg.size()=" + listEnvBudg.size());
		for (EnvelopBudg evelopp : listEnvBudg) {
			if (evelopp.getTotalMntCreditPaie() == 0)
				continue;
			listVentCp = getVentilationParDest(exercice, codeBudget, evelopp.getGroupNat(), null);
			logger.debug("controlerMntValOrdoBi, listVentCp=" + listVentCp);

			if ((listVentCp == null) || (listVentCp.isEmpty())) {
				msg = HandlerJSFMessage.getErrorMessage(MsgEntry.VENTIL_MISSED_ERR);
				msg = HandlerJSFMessage.formatMessage(msg, new Object[] { evelopp.getCodeAndLibGroupNat() });
				HandlerJSFMessage.addErrorMessage(msg);
				result = false;
			}

			else {
				logger.debug("controlerMntValOrdoBi, listVentCp.size()=" + listVentCp.size());
				totalVaration = getTotalMntCpDest(listVentCp);
				
				if (evelopp.getTotalMntCreditPaie() != totalVaration)
					natGrpCodesList.add(evelopp.getCodeAndLibGroupNat());
				logger.debug("controlerMntValOrdoBr, natGrpCodesList.size()=" + natGrpCodesList.size());
			}
			

		}
		if (natGrpCodesList.isEmpty())
			return result;

		for (String groupNat : natGrpCodesList) {
			msg = HandlerJSFMessage.getErrorMessage(MsgEntry.VENTIL_A_VERIFIER_ERR);
			msg = HandlerJSFMessage.formatMessage(msg, new Object[] { groupNat });
			HandlerJSFMessage.addErrorMessage(msg);
		}

		return false;
	}

	public double getTotalMntCpDest(List<BudgetCpDest> listVentCp) {
		BigDecimal mnt = new BigDecimal(0);
		double result = 0;

		if (listVentCp == null)
			return result;
		for (BudgetCpDest bcpDest : listVentCp) {
			logger.info("getTotalMntCpDest(list) : bcpDest " + bcpDest);
			mnt = bcpDest.getMontant();
			if (mnt != null)
				result += mnt.doubleValue();
		}
		result=Helper.round(result,2);
		logger.info("getTotalMntCpDest(list) : result " + result);
		return result;
	}

	public void validerAC() {
		updateEtatBudget(EtatEnum.VALIDE);

	}

	public void refuserAC() {
		updateEtatBudget(EtatEnum.OUVERT);
	}

	private void updateEtatBudgModification(EtatEnum etat) {
		int exercice = Helper.stringToInt(getSearchBean().getExerciceBR());
		budgetPluriService.updateBudgetModifEtatCp(exercice, getSelectedBudgModif().getType().getCode(),
				getSelectedBudgModif().getNumero(), etat.getEtat());
		getSelectedBudgModif().setEtatCp(etat);

	}

	private double getTotalBudgRect(List<BudgetCpDest> listVentCp) {
		double totalVaration = 0d;
		for (BudgetCpDest budgetCpDest : listVentCp) {
			totalVaration += budgetCpDest.getBudgetRec().doubleValue();

		}
		return totalVaration;

	}

	private boolean controlerMntValOrdoBr(List<CpGestionnaire> cpGestList) {
		double totalVaration = 0d;
		int exercice = getExercice();
		String codeBudget = getSearchBean().getCodeBudget();
		List<BudgetCpDest> listVentCp = null;
		List<String> natGrpCodesList = new ArrayList<String>();
		String msg = null;
		boolean result = true;

		if (cpGestList == null)
			return result;
		logger.debug("controlerMntValOrdoBr, cpGestList.size()=" + cpGestList.size());
		for (CpGestionnaire cpGest : cpGestList) {
			listVentCp = getVentilationParDest(exercice, codeBudget, cpGest.getGroupNat(), null);
			logger.debug("controlerMntValOrdoBr, listVentCp=" + listVentCp);
			if (!Util.compareCurrency(cpGest.getVaraitionBR().doubleValue(), 0d)) {
				if ((listVentCp == null) || (listVentCp.isEmpty())) {
					msg = HandlerJSFMessage.getErrorMessage(MsgEntry.VENTIL_MISSED_ERR);
					msg = HandlerJSFMessage.formatMessage(msg, new Object[] { cpGest.getCodeAndLibGroupNat() });
					HandlerJSFMessage.addErrorMessage(msg);
					result = false;
				}

				else {
					logger.debug("controlerMntValOrdoBr, listVentCp.size()=" + listVentCp.size());
					totalVaration = getTotalBudgRect(listVentCp);
					if (!Util.compareCurrency(cpGest.getVaraitionBR().doubleValue(), totalVaration))
						natGrpCodesList.add(cpGest.getCodeAndLibGroupNat());
					logger.debug("controlerMntValOrdoBr, natGrpCodesList.size()=" + natGrpCodesList.size());
				}
			}

		}
		if (natGrpCodesList.isEmpty())
			return result;

		for (String groupNat : natGrpCodesList) {
			msg = HandlerJSFMessage.getErrorMessage(MsgEntry.VENTIL_A_VERIFIER_ERR);
			msg = HandlerJSFMessage.formatMessage(msg, new Object[] { groupNat });
			HandlerJSFMessage.addErrorMessage(msg);
		}

		return false;
	}

	public void validerOrdoBr() {
		List<CpGestionnaire> cpGestList = null;
		try {
			cpGestList = pepareBrList();
			saveCreditBR(cpGestList);
			// controle sur montant ventil
			if (!controlerMntValOrdoBr(cpGestList))
				return;
			updateEtatBudgModification(EtatEnum.ORDO);
			setBudgModifList(null);
			HandlerJSFMessage.addInfo(MsgEntry.SUCCES);
		} catch (Exception e) {
			logger.error("saveCreditBR()", e); //$NON-NLS-1$
			HandlerJSFMessage.addErrorMessage(getErrorMessageFromException(MsgEntry.FAILED, e));
			e.printStackTrace();
		}
	}

	public void validerAcBr() {
		logger.info("validerAcBr: start");
		double newCo = 0, newDispoCo;
		List<CpGestionnaire> cpGestList = null;
		List<ModificationCpLigne> modificationList = null;
		try {
			cpGestList = pepareBrList();
			modificationList = createBrModificationList(cpGestList);
			for (CpGestionnaire cpGest : cpGestList) {
				newCo = cpGest.getCreditOuvert() + cpGest.getBudgetRectificatif();
				newDispoCo = cpGest.getDispoCo() + cpGest.getBudgetRectificatif();
				cpGest.setCreditOuvert(newCo);
				cpGest.setDispoCo(newDispoCo);
				cpGest.setBudgetRectificatif(0);

			}

			budgetPluriService.saveCreditBR(cpGestList, modificationList);
			updateEtatBudgModification(EtatEnum.VALIDE);
			setBudgModifList(null);
			HandlerJSFMessage.addInfo(MsgEntry.SUCCES);
		} catch (Exception e) {
			HandlerJSFMessage.addErrorMessage(getErrorMessageFromException(MsgEntry.FAILED, e));
			e.printStackTrace();
		}
		logger.info("validerAcBr: end");

	}

	public void refuserAcBr() {
		try {
			updateEtatBudgModification(EtatEnum.ENCOURS);
			setBudgModifList(null);
		} catch (Exception e) {
			HandlerJSFMessage.addErrorMessage(getErrorMessageFromException(MsgEntry.FAILED, e));
			e.printStackTrace();
		}

	}

	public boolean isValiderOrdoAutorized() {
		try {

			switch (getBudget().getEtatCp()) {
			case OUVERT:
				return true;
			case VALIDE:
			case ORDO:
				return false;
			default:
				break;
			}
			return false;
		} catch (Exception e) {
			return false;
		}
	}

	public boolean isValiderACAutorized() {
		try {

			switch (getBudget().getEtatCp()) {
			case OUVERT:
			case VALIDE:
				return false;
			case ORDO:
				return true;
			default:
				break;
			}
			return false;
		} catch (Exception e) {
			return false;
		}
	}

	public void searchCpParGestForBr() {
		logger.info("searchCpParGestForBr  - start");
		boolean isSingleItem = false;
		try {

			int exerciceCp = getExercice();
			String codeBudget = getSearchBean().getCodeBudget();
			String groupNat = getCodeGroupNat(getGroupDestNatCp());
			List<CpGestionnaire> list = budgetPluriService.getCpGestionnaireList(exerciceCp, codeBudget, groupNat);
			if ((list == null) || (list.isEmpty())) {
				list = createCpGestionnaire(exerciceCp, codeBudget, groupNat);
				if ((list != null) && (list.size() == 1))
					isSingleItem = true;
			}
			loadlibelleGestionnaire(list);
			setListCreditPaiement(null);
			getListCreditPaiement();
			if (isSingleItem) {
				list.get(0).setBudgetInitial(new BigDecimal(getTotalMntCp()));
			}
			setListCpGestionnaire(list);
			logger.info("searchCpParGestForBr  - list.size()=" + ((list != null) ? list.size() : 0) + " - end");
		} catch (Exception e) {
			HandlerJSFMessage.addErrorMessage(getErrorMessageFromException(MsgEntry.FAILED, e));
			e.printStackTrace();
		}
	}

	public double getTotalCpGest() {
		double result = 0d;
		if (getListCpGestionnaire() != null)
			for (CpGestionnaire cpGest : getListCpGestionnaire()) {
				result += cpGest.getTotalAeRap();
			}
		return Helper.round(result, 2);
	}

	public boolean isValidBudgModifOrdoAutorized() {
		try {
			switch (getSelectedBudgModif().getEtatCp()) {
			case ENCOURS:
				return true;
			case OUVERT:
			case VALIDE:
			case ORDO:
				return false;
			default:
				break;
			}
			return false;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public boolean isValidBudgModifACAutorized() {
		try {

			switch (getSelectedBudgModif().getEtatCp()) {
			case OUVERT:
			case VALIDE:
			case ENCOURS:
				return false;
			case ORDO:
				return true;
			default:
				break;
			}
			return false;
		} catch (Exception e) {
			return false;
		}
	}

	public double getTotalDpEmisCpGest() {
		double result = 0d;
		if (getListCpGestionnaire() != null)
			for (CpGestionnaire cpGest : getListCpGestionnaire()) {
				result += cpGest.getDpEmis();
			}
		return Helper.round(result, 2);
	}

	public double getTotalDispoCoCpGest() {
		double result = 0d;
		if (getListCpGestionnaire() != null)
			for (CpGestionnaire cpGest : getListCpGestionnaire()) {
				result += cpGest.getDispoCo();
			}
		return Helper.round(result, 2);
	}

	public void saveCreditBrParGestionnaire() {

		try {
			if (getListCpGestionnaire() != null) {
				updateCpGestionnaire(getListCpGestionnaire());
				budgetPluriService.saveCpGestionnaire(getListCpGestionnaire());
			}
			HandlerJSFMessage.addInfo(MsgEntry.SUCCES);
		} catch (Exception e) {
			e.printStackTrace();
			HandlerJSFMessage.addErrorMessage(getErrorMessageFromException(MsgEntry.FAILED, e));
		}

	}

	public EtatEnum getEtatCpBudg() {
		if (etatCpBudg == null) {
			int exerciceCp = getExercice();
			etatCpBudg = budgetPluriService.getBudgetEtatCp(exerciceCp);
		}
		return etatCpBudg;

	}

	public void setEtatCpBudg(EtatEnum etatCpBudg) {
		this.etatCpBudg = etatCpBudg;
	}

	public boolean isEtatCpValide() {
		return EtatEnum.VALIDE == getEtatCpBudg();
	}

	public double getTotalMntCpGestBr() {
		double result = 0;
		if (getListCpGestionnaire() == null)
			return result;
		for (CpGestionnaire cpGest : getListCpGestionnaire()) {
			result += cpGest.getBudgetRectificatif();
		}
		return Helper.round(result, 2);
	}

	public boolean isDepassTotalCpGestBr() {
		if (getTotalMntCp() < getTotalMntCpGestBr())
			return true;
		return false;
	}

	public boolean isMntCpGestBrEqual() {
		return (getTotalMntCp() == getTotalMntCpGestBr());
	}

	public double getDepassementTotalCpGestBr() {
		double totalMntCp = getTotalMntCp(), totalMntCpDest = getTotalMntCpGestBr();
		return totalMntCpDest - totalMntCp;

	}

	public void ajusterVentCpGestBr() {
		double mntDepassement = getDepassementTotalCpGestBr();
		String cpGestIndex = Helper.getRequestParameter("cpGestIndex");
		CpGestionnaire cpGest = getCurrentCpGestionnaire(Helper.stringToInt(cpGestIndex), getListCpGestionnaire());
		cpGest.setBudgetRectificatif(Helper.round(cpGest.getBudgetRectificatif() - mntDepassement, 2));

	}

	public void selectBrListener(AjaxBehaviorEvent changeEvent) {
		if (getSelectedBudgModif() == null)
			return;
		if (isBiNonValide())
			HandlerJSFMessage.addError(MsgEntry.BI_NON_VALIDE_ERROR);
		if (isThereBrEnCours())
			HandlerJSFMessage.addError(MsgEntry.BR_EN_COURS_ERROR);
		if (!isFirstBrOuvert())
			HandlerJSFMessage.addError(MsgEntry.BR_OUVERT_ERROR);

	}

	private boolean isThereBrEnCours() {
		if ((getSelectedBudgModif() != null) && (!getSelectedBudgModif().isEtatCpOuvert()))
			return false;
		return (getBudgRecEnCoursList() != null);

	}

	public boolean isEtatCpBrOuvert() {
		if (getSelectedBudgModif() == null)
			return false;
		return getSelectedBudgModif().isEtatCpOuvert();

	}

	public List<ModificationCpLigne> getListBrVises() {
		logger.info("getListBrVises: start----: ");
		String natGrp = getSearchBean().getCodeNatureGrp();
		int exercice = Helper.stringToInt(getSearchBean().getExercice());
		String codeBudg = getSearchBean().getCodeBudget();
		logger.info("getListBrVises: natGrp: " + natGrp);
		logger.info("getListBrVises: exercice: " + exercice + "budget: " + codeBudg);
		return budgetPluriService.getBrVisesList(exercice, codeBudg, natGrp);
	}

	public boolean isSumBrVises() {
		if (getBrAccSuiviCP() != 0)
			return true;
		return false;

	}

	private String getPreparedGestUnique() {
		String gestUnique = getGestUnique();
		logger.info("getPreparedGestUnique: gestUnique=" + gestUnique);
		String gestionnaire = (isGestUnique()) ? gestUnique : Constant.RESERVE;
		logger.info("getPreparedGestUnique: gestionnaire=" + gestionnaire);
		return gestionnaire;
	}

	public List<BudgModification> getBudgRecEnCoursList() {
		return budgRecEnCoursList;
	}

	public void setBudgRecEnCoursList(List<BudgModification> budgRecEnCoursList) {
		this.budgRecEnCoursList = budgRecEnCoursList;
	}

	private List<EtatEnum> getEtatCpConstraint() {
		List<EtatEnum> constraints = null;
		Action action = BudgetHelper.getNavigationBean().getCurrentAction();
		switch (action) {
		case VENTIL_DEST_BR:
			constraints = Arrays.asList(EtatEnum.ENCOURS, EtatEnum.ORDO, EtatEnum.VALIDE);
			break;
		case VENTIL_GEST_BR:
			constraints = Arrays.asList(EtatEnum.VALIDE);
			break;

		default:
			break;
		}
		return constraints;
	}

	public boolean isEditableBR() {
		Action action = BudgetHelper.getNavigationBean().getCurrentAction();
		return isEditableBR(action);
	}

	private boolean isEditableBR(Action action) {
		if (getSelectedBudgModif() == null)
			return false;
		EtatEnum etatCpForBR = getSelectedBudgModif().getEtatCp();
		List<EtatEnum> EditableStatus = new ArrayList<EtatEnum>();

		switch (action) {
		case VENTIL_DEST_BR:
			EditableStatus = Arrays.asList(EtatEnum.ENCOURS);
			break;
		case SAISI_CP_BR:
			EditableStatus = Arrays.asList(EtatEnum.ENCOURS, EtatEnum.OUVERT);
			break;
		default:
			break;
		}

		return EditableStatus.contains(etatCpForBR);
	}

	public int getNiveau() {
		int niveau = 0;
		if (!isBI()) {
			if (getSelectedBudgModif() != null)
				niveau = getSelectedBudgModif().getNumero();
		}
		return niveau;
	}

	public double getVariationBrAventiller() {
		return variationBrAventiller;
	}

	public void setVariationBrAventiller(double variationBrAventiller) {
		this.variationBrAventiller = variationBrAventiller;
	}

	public boolean isRequiredDataDone() {

		if (!isCommonRequiredDone())
			return false;
		String groupNat = getCodeGroupNat(getGroupDestNatCp());
		BudgModification br = getSelectedBudgModif();

		Action action = BudgetHelper.getNavigationBean().getCurrentAction();
		switch (action) {
		case VENTIL_DEST_BR:
		case VENTIL_GEST_BR:
			if (Strings.isBlank(groupNat))
				return false;
			if (br == null)
				return false;
			break;
		case SAISI_CP_BR:
			if (br == null)
				return false;
			break;
		case MODIF_DEPOIEMENT:
			if (Strings.isBlank(groupNat))
				return false;
			break;
		case CONSULT_DEPOIEMENT:
			if (getSelectedModifCP() == null)
				return false;
		default:
			break;
		}

		return true;
	}

	public boolean isCommonRequiredDone() {
		if (!super.isCommonRequiredDone())
			return false;
		Action action = BudgetHelper.getNavigationBean().getCurrentAction();
		switch (action) {

		case MODIF_DEPOIEMENT:
			if (Strings.isBlank(getObjetModification()))
				return false;
			break;
		case SUIVI_AE:
			if (Strings.isBlank(getSearchBean().getCodeNatureGrp()))
				return false;
		default:
			break;
		}
		return true;
	}

	public void chooseNatForVentilBrDest() {
		if (logger.isDebugEnabled()) {
			logger.debug("chooseNatForVentilBrDest() - start"); //$NON-NLS-1$
		}
		DialogHelper.openVentilBrDestDialog();
		if (logger.isDebugEnabled()) {
			logger.debug("chooseNatForVentilBrDest() - end"); //$NON-NLS-1$
		}
	}

	public CpGestionnaire getSelectedBr() {
		return selectedBr;
	}

	public void setSelectedBr(CpGestionnaire selectedBr) {
		this.selectedBr = selectedBr;
		integratedVentParDest();
	}

	/**
	 * Preparer la liste des Destination pour acomplir la ventilation par
	 * destination: 1- Charger la ventillation pour le niveau BR 2- si non la
	 * ventillation pour le niveau BI
	 */
	private void integratedVentParDest() {
		boolean isSingleItem = false;
		CpGestionnaire brSaisie = getSelectedBr();
		List<BudgetCpDest> ventByDest = null;
		if (brSaisie == null)
			return;
		int exercice = brSaisie.getExerciceCP();
		String codeBudget = brSaisie.getCodeBudget();
		String groupNat = brSaisie.getGroupNat();
		Integer noBR = getNiveau();
		logger.info("integratedVentParDest :groupNat=" + groupNat + " - start");
		boolean isEtitableBr = isEditableBR(Action.VENTIL_DEST_BR);

		List<BudgetCpDest> allVentByDest = budgetPluriService.getBudgetCpDest(exercice, codeBudget, groupNat, null);
		ventByDest = getVentilationByNiveau(allVentByDest, noBR);
		if ((ventByDest == null) || (ventByDest.isEmpty())) {
			// Charger la ventillation BI
			if (isEtitableBr) {
				ventByDest = getVentilationByNiveau(allVentByDest, 0);
				if ((ventByDest != null) && (ventByDest.size() == 1))
					isSingleItem = true;

			}

		}
		prepareVentByDest(noBR, ventByDest, allVentByDest);

		variationBrAventiller = brSaisie.getVaraitionBR().doubleValue();

		if (isSingleItem) {
			ventByDest.get(0).setBudgetRec(new BigDecimal(variationBrAventiller));
		}
		brSaisie.setVentByDest(ventByDest);
		setListBudgetCpDest(ventByDest);

		if (ventByDest != null)
			logger.info("integratedVentParDest :variationBrAventiller=" + variationBrAventiller + ", list="
					+ ventByDest.size() + " - end");
	}

	private void prepareVentByDest(Integer noBR, List<BudgetCpDest> ventByDest, List<BudgetCpDest> allVentByDest) {
		loadlibelleDestibnation(ventByDest);
		double bi = 0, totaleBrPrecedent = 0;
		for (BudgetCpDest item : ventByDest) {

			bi = calculateBudgetInitiale(item.getDestination(), allVentByDest);
			totaleBrPrecedent = calculateTotaleBrPrecedent(noBR, item.getDestination(), allVentByDest);
			item.setBi(bi);
			item.setTotaleBrPrecedent(totaleBrPrecedent);
			if (item.getNiveau() == 0) {
				item.setNiveau(noBR);
				item.setMontant(BigDecimal.ZERO);
				item.setBudgetRectificatif(0);
				item.setCreditOuvert(0);
			}
		}

	}

	private double calculateTotaleBrPrecedent(Integer noBR, String destination, List<BudgetCpDest> allVentByDest) {
		double result = 0;
		for (BudgetCpDest item : allVentByDest) {
			if ((item.getNiveau() < noBR) && (item.getDestination().equalsIgnoreCase(destination)))
				result += item.getBudgetRectificatif();
		}
		return Helper.round(result, 2);
	}

	private double calculateBudgetInitiale(String destination, List<BudgetCpDest> allVentByDest) {
		for (BudgetCpDest item : allVentByDest) {
			if ((item.getNiveau() == 0) && (item.getDestination().equalsIgnoreCase(destination)))
				return item.getMontant().doubleValue();
		}
		return 0;
	}

	private List<BudgetCpDest> getVentilationByNiveau(List<BudgetCpDest> allVentByDest, Integer niveau) {
		if (allVentByDest == null)
			return null;
		List<BudgetCpDest> result = new ArrayList<>();
		for (BudgetCpDest item : allVentByDest) {
			if (item.getNiveau() == niveau)
				result.add(item);
		}

		return result;
	}

	public void closeVentilBrCpDestDialog() {
		if (logger.isDebugEnabled()) {
			logger.debug("closeVentilBrCpDestDialog() - start"); //$NON-NLS-1$
		}

		DialogHelper.closeDialog(null);

		if (logger.isDebugEnabled()) {
			logger.debug("closeVentilBrCpDestDialog() - end"); //$NON-NLS-1$
		}
	}

	private boolean isFirstBrOuvert() {
		if ((isEtatCpBrOuvert()) && (getBudgModifList().indexOf(getSelectedBudgModif())) > 0) {
			int firstIndex = (getBudgModifList().indexOf(getSelectedBudgModif()) - 1);
			if ((getBudgModifList().get(firstIndex) != null) && ((getBudgModifList().get(firstIndex).isEtatCpOuvert())))
				return false;
		}
		return true;
	}

	@Override
	public void search() {

	}

	@Override
	public void validate() {

	}

	/**
	 * Trouver si une ventilation à été déjà saisi Initialiser la valeure de
	 * depNivDest si elle exsite
	 */
	public void positionnerDepNivDest() {
		String groupNat = null;
		Action action = getCurrentAction();
		if (action == null)
			return;
		if (action != Action.ADRESSAGE_BUDGET_PLURI_CPDEST)
			return;
		if (!isRequiredDataDone())
			return;
		groupNat = getCodeGroupNat(getGroupDestNatCp());
		if (groupNat == null)
			return;
		List<BudgetCpDest> listVentilation = getVentilationParDest(getExercice(), getCodeBudget(), groupNat, null);
		if ((listVentilation != null) && (!listVentilation.isEmpty())) {
			String item = listVentilation.get(0).getDestination();
			if (item != null) {
				setDepNivDest(item.trim().length() + "");
				setDepNivDestEdited(true);
			}

		} else {
			setDepNivDest(null);
			setDepNivDestEdited(false);
		}
	}

	@Override
	public void setDepNivDest(String depNivDest) {
		super.setDepNivDest(depNivDest);
		setRecByOrigList(null);
		setRecByOrigTotal(new RecByOrig());
	}

	@Override
	public void onDepNivDestChange() {
		logger.info("#### onDepNivDestChange ");
		setGestParamList(null);
		resetDynamicList();
	}

	@Override
	public void onRecNivDestChange() {
		logger.info("#### onRecNivDestChange ");

		setRecByOrigList(null);
		setRecByOrigTotal(new RecByOrig());

	}

	public boolean isCreditOuvertEnabled() {
		String codeDirection = getSearchBean().getCodeDirection();
		if (codeDirection == null)
			return false;
		return (!codeDirection.trim().isEmpty());
	}

	public double getCreditOuvert() {
		logger.debug("public double getCreditOuvert(): exercice={}, code budget={}, code direction={}", getExercice(),
				getCodeBudget(), getSearchBean().getCodeDirection());
		creditOuvert = budgetPluriService.getCreditOuvert(getExercice(), getCodeBudget(),
				getSearchBean().getCodeDirection());
		return creditOuvert;
	}

	public void setCreditOuvert(double creditOuvert) {
		this.creditOuvert = creditOuvert;
	}

	public Map<String, List<CpGestionnaire>> getDeploiementData() {
		return deploiementData;
	}

	public void setDeploiementData(Map<String, List<CpGestionnaire>> deploiementData) {
		this.deploiementData = deploiementData;
	}

	public List<CpGestionnaire> getDeploiementRecap() {
		logger.debug("getDeploiementRecap : start");
		List<CpGestionnaire> updatedList = new ArrayList<>();
		if (getDeploiementData() != null)
			for (String natGrp : getDeploiementData().keySet()) {
				for (CpGestionnaire cpGest : getDeploiementData().get(natGrp)) {
					if (cpGest.getMontant() == 0)
						continue;
					updatedList.add(cpGest);
				}
			}
		logger.debug("getDeploiementRecap size={}: fin", updatedList.size());
		return updatedList;
	}

	public boolean isAddlineDepAutorised() {
		if (groupDestNatCp == null)
			return false;
		return isSearchDepExecuted();
	}

	public boolean isSearchDepExecuted() {
		return searchDepExecuted;
	}

	public void setSearchDepExecuted(boolean searchDepExecuted) {
		this.searchDepExecuted = searchDepExecuted;
	}

	public void addLigneDeploiment() {
		if (logger.isDebugEnabled()) {
			logger.debug("addLigneDeploiment() - start"); //$NON-NLS-1$
		}
		setGestionnaire(null);
		setGroupDestNatCp(groupDestNatCp(getGroupDestNatCp(), getGroupDestCpList()));
		DialogHelper.openConfirmDepDialog();

		if (logger.isDebugEnabled()) {
			logger.debug("addLigneDeploiment() - end"); //$NON-NLS-1$
		}
	}

	public void closeAddLigneDepDialog() {
		if (logger.isDebugEnabled()) {
			logger.debug("closeAddLigneDepDialog() - start"); //$NON-NLS-1$
		}

		DialogHelper.closeDialog(null);

		if (logger.isDebugEnabled()) {
			logger.debug("closeAddLigneDepDialog() - end"); //$NON-NLS-1$
		}
	}

	public void validerDeploiement() {
		if (getListCpGestionnaire() != null)
			for (CpGestionnaire cpGest : getListCpGestionnaire()) {
				if (cpGest.getMontantTemp() == 0)
					continue;
				cpGest.setMontant(cpGest.getMontantTemp());
			}
	}

	public String getGestionnaire() {
		return gestionnaire;
	}

	public void setGestionnaire(String gestionnaire) {
		this.gestionnaire = gestionnaire;
	}

	public void validateAddLigneDep() {
		CpGestionnaire cpgest = null;
		if (logger.isDebugEnabled()) {
			logger.debug("validateAddLigneDep() - start"); //$NON-NLS-1$
		}
		String groupNat = getCodeGroupNat(getGroupDestNatCp());
		try {
			if ((getGestionnaire() == null) || (getGestionnaire().trim().isEmpty()))
				return;
			if (getListCpGestionnaire() == null)
				setListCpGestionnaire(new ArrayList<CpGestionnaire>());
			cpgest = new CpGestionnaire(Helper.stringToInt(getSearchBean().getExerciceCp()),
					getSearchBean().getCodeBudget(), groupNat, getGroupDestNatCp().getEntity2().getDesignation(),
					getGestionnaire());
			if (getListCpGestionnaire().contains(cpgest))
				HandlerJSFMessage.addError(MsgEntry.DUPLICATE_LIG_DEP_MSG);

			else {
				getListCpGestionnaire().add(cpgest);
				DialogHelper.closeDialog(null);
			}

		} catch (Exception e) {
			HandlerJSFMessage.addErrorMessage(getErrorMessageFromException(MsgEntry.FAILED, e));
			e.printStackTrace();
		}

		if (logger.isDebugEnabled()) {
			logger.debug("validateAddLigneDep() - end"); //$NON-NLS-1$
		}
	}

	private ComplexEntity groupDestNatCp(ComplexEntity groupDestNatCp, List<ComplexEntity> groupDestCpList) {
		int index = 0;
		if (groupDestCpList == null)
			return null;
		if (groupDestNatCp == null)
			return null;
		index = groupDestCpList.indexOf(groupDestNatCp);
		return groupDestCpList.get(index);

	}

	public List<CpModification> getCpModificationList() {
		if (cpModificationList == null) {
			if (isCommonRequiredDone()) {
				cpModificationList = budgetPluriService.getCpModificationList(getExercice(), getCodeBudget());

			}
		}
		return cpModificationList;
	}

	public void setCpModificationList(List<CpModification> cpModificationList) {
		this.cpModificationList = cpModificationList;
	}

	public void setSelectedModifCP(CpModification selectedModifCP) {
		// logger.info("setSelectedModifCP - start"+selectedModifCP);
		this.selectedModifCP = selectedModifCP;
		setCpItemModifList(null);
	}

	public CpModification getSelectedModifCP() {
		// logger.info("getSelectedModifCP - start"+selectedModifCP);
		return selectedModifCP;
	}

	public void consultDeploiement() {
		try {

			logger.info("consultDeploiement  - start");
			List<CpModificationItem> currentList = null;

			int exerciceCp = getExercice();
			String codeBudget = getSearchBean().getCodeBudget();
			Integer modification = getSelectedModifCP().getNumero();
			logger.info("consultDeploiement  Récuperer cpItemModifList pour budgModifCp {}", modification);
			currentList = budgetPluriService.getItemscpModifsList(exerciceCp, codeBudget, modification);

			completeCpModificationItem(currentList);
			setCpItemModifList(currentList);

		} catch (Exception e) {
			HandlerJSFMessage.addErrorMessage(getErrorMessageFromException(MsgEntry.FAILED, e));
			e.printStackTrace();
		}

	}

	private void completeCpModificationItem(List<CpModificationItem> modificationItems) {
		CpGestionnaire cpGest = null;
		SimpleEntity entity = null;
		if (modificationItems != null)
			for (CpModificationItem item : modificationItems) {
				cpGest = budgetPluriService.getCpGestionnaire(item.getExercice(), item.getCodeBudget(),
						item.getNoLbgCp());
				if (cpGest == null)
					continue;

				entity = budgetPluriService.getGestionnaire(cpGest.getGestionnaire());
				if (entity != null)
					cpGest.setLibelle(entity.getDesignation());

				item.setCpGest(cpGest);
			}

	}

	public List<CpModificationItem> getCpItemModifList() {
		return cpItemModifList;
	}

	public void setCpItemModifList(List<CpModificationItem> cpItemModifList) {
		this.cpItemModifList = cpItemModifList;
	}

	public DepEnum getModifNatGrp(int index) {
		return DepEnum.parse(index);
	}

	public double getTotalMntNatGrp(String i) {
		double result = 0d;
		if (getCpItemModifList() != null)
			for (CpModificationItem item : getCpItemModifList()) {
				logger.info("item.montant= " + item.getMontant(i));
				result += item.getMontant(i);
			}
		return Helper.round(result, 2);
	}

	public boolean isExsitRestBudgCpDestNegatif() {

		if (getListBudgetCpDest() != null)
			for (BudgetCpDest cpGest : getListBudgetCpDest()) {
				if (cpGest.getReste() >= 0)
					continue;
				return true;
			}

		return false;
	}

	public boolean isSaveVentBrDestAutrsed() {
		return (isMntCpDestBrEqual()) && (isEditableBR()) && (!isExsitRestBudgCpDestNegatif());
	}

	/**
	 * Retourne la taille du champ destination pour le premier élement de la
	 * liste ListBudgetCpDest
	 * 
	 * @return
	 */
	public String getFirstNiveauDest() {
		try {
			BudgetCpDest firstItem = null;
			firstItem = getListBudgetCpDest().get(0);
			return firstItem.getDestination().trim().length() + "";
		} catch (Exception e) {
			return "";
		}

	}

	/**
	 * 
	 * @return true si le menu Budget pluriannuel > Budget Initial > Saisie CP
	 *         est actif en sasi et false dans le cas inverse
	 */
	public boolean isEditableBI() {
		Action action = BudgetHelper.getNavigationBean().getCurrentAction();
		return isEditableBI(action);
	}

	private boolean isEditableBI(Action action) {
		if (getBudget() == null)
			return false;
		EtatEnum etatCpForBI = getBudget().getEtatCp();
		List<EtatEnum> EditableStatus = new ArrayList<EtatEnum>();

		switch (action) {

		case SAISI_BUDGET_PLURI:
			EditableStatus = Arrays.asList(EtatEnum.ENCOURS, EtatEnum.OUVERT);
			break;
		default:
			break;
		}

		return EditableStatus.contains(etatCpForBI);
	}

	/**
	 * 
	 * @return true si une ventilation CP par destination a déja été saisi pour
	 *         l'enveloppe budgétaire selectionnée
	 */
	public boolean isDepNivDestEdited() {
		return depNivDestEdited;
	}

	public void setDepNivDestEdited(boolean depNivDestEdited) {
		this.depNivDestEdited = depNivDestEdited;
	}

	/**
	 * sauvegarde de la ventillation Bi par desttination
	 */
	public void saveVentBIParDest() {
		this.saveCreditParDestination();
		this.positionnerDepNivDest();
	}

	private boolean isBiNonValide() {
		if (!getSelectedBudgModif().isEtatCpOuvert())
			return false;
		BudgetPluriannuel bi = budgetPluriService.searchBudgetForSaisi(getSelectedBudgModif().getExercice(),
				getSelectedBudgModif().getCodeBudget());
		return (!bi.isValide());
	}

	public boolean isSearchBrAutorised() {
		if (getSelectedBudgModif() == null)
			return false;
		return (isRequiredDataDone() && !isBiNonValide() && !isThereBrEnCours() && isFirstBrOuvert());

	}

	public boolean isAddCpAuthorized() {
		CreditPaiement credit = null;
		if (getEnvelopBudg() != null)
			if (getEnvelopBudg().getCpList() != null) {
				credit = new CreditPaiement(getEnvelopBudg());
				credit.setExerciceCP(getBudget().getExercice());
				credit.setTrace(Helper.createTrace());

				if (getEnvelopBudg().getCpList().contains(credit))
					return false;
				else
					return true;
			}

		return false;

	}

	public List<SuiviRecetStruct> getSuiviRecetAnt() {
		return suiviRecetAnt;
	}

	public void setSuiviRecetAnt(List<SuiviRecetStruct> suiviRecetAnt) {
		this.suiviRecetAnt = suiviRecetAnt;
	}

	public StreamedContent getXlsSuiviRecetAnt() {
		StreamedContent returnStreamedContent = null;

		ExcelHandler excel = new ExcelHandler(ExcelModelEnum.DETAILS_SUIVI_RECET_ANT, getSuiviRecetAnt());
		returnStreamedContent = excel.getExcelFile();

		return returnStreamedContent;

	}

	public void searchSuiviRecette() {
		GlobalSuiviStruct suiveData = null;
		setGlobalSuiviAnt(null);
		setGlobalSuiviExec(null);
		setSuiviRecetAnt(null);
		setSuiviRecetExec(null);
		if (!checkRequiredSuiviRecette())
			return;
		try {
			refreshRecetteRecouveData();

			suiveData = budgetPluriService.getGlobalSuiviRec(true, Integer.parseInt(getSearchBean().getExercice()),
					getSearchBean().getCodeBudget(), getSearchBean().getCodeDirection(),
					getSearchBean().getCodeService(), getSearchBean().getCodeFongDest(), getSearchBean().getCodeDest(),
					getSearchBean().getCodeProg(), getSearchBean().getCodeNatureGrp(), getSearchBean().getCodeNature(),
					getSearchBean().getNoLtrInt(), getCodeTiers());
			setGlobalSuiviAnt(suiveData);

			suiveData = budgetPluriService.getGlobalSuiviRec(false, Integer.parseInt(getSearchBean().getExercice()),
					getSearchBean().getCodeBudget(), getSearchBean().getCodeDirection(),
					getSearchBean().getCodeService(), getSearchBean().getCodeFongDest(), getSearchBean().getCodeDest(),
					getSearchBean().getCodeProg(), getSearchBean().getCodeNatureGrp(), getSearchBean().getCodeNature(),
					getSearchBean().getNoLtrInt(), getCodeTiers());
			setGlobalSuiviExec(suiveData);

			suiveData = budgetPluriService.getGlobalSuiviRec(null, Integer.parseInt(getSearchBean().getExercice()),
					getSearchBean().getCodeBudget(), getSearchBean().getCodeDirection(),
					getSearchBean().getCodeService(), getSearchBean().getCodeFongDest(), getSearchBean().getCodeDest(),
					getSearchBean().getCodeProg(), getSearchBean().getCodeNatureGrp(), getSearchBean().getCodeNature(),
					getSearchBean().getNoLtrInt(), getCodeTiers());
			setGlobalSuiviTotal(suiveData);
		} catch (Exception e) {
			HandlerJSFMessage.addErrorMessage(getErrorMessageFromException(MsgEntry.FAILED, e));
			e.printStackTrace();
		}
	}

	private boolean checkRequiredSuiviRecette() {

		boolean isExecNull = false;
		boolean isBudgNull = false;

		String exec = getSearchBean().getExercice();
		String codeBudg = getSearchBean().getCodeBudget();

		isExecNull = (exec == null) ? true : exec.trim().isEmpty();
		isBudgNull = (codeBudg == null) ? true : codeBudg.trim().isEmpty();

		if ((isExecNull) || (isBudgNull)) {
			HandlerJSFMessage.addError(fr.symphonie.util.MsgEntry.REQUIRED_MSG);
			return false;
		}
		return true;

	}

	public List<SuiviRecetStruct> getSuiviRecetExec() {
		return suiviRecetExec;
	}

	public void setSuiviRecetExec(List<SuiviRecetStruct> suiviRecetExec) {
		this.suiviRecetExec = suiviRecetExec;
	}

	public void loadSuiviRecetAnt() {
		setSuiviRecetAnt(getDetailsSuiviRecet(true));
	}

	public void loadSuiviRecetExec() {
		setSuiviRecetExec(getDetailsSuiviRecet(false));
	}

	public void loadSuiviRecetTotal() {
		setSuiviRecetTotal(getDetailsSuiviRecet(null));
	}

	public StreamedContent getXlsSuiviRecetExec() {
		StreamedContent returnStreamedContent = null;

		ExcelHandler excel = new ExcelHandler(ExcelModelEnum.DETAILS_SUIVI_RECET_EXEC, getSuiviRecetExec());
		returnStreamedContent = excel.getExcelFile();

		return returnStreamedContent;

	}

	public GlobalSuiviStruct getGlobalSuiviAnt() {
		return globalSuiviAnt;
	}

	public void setGlobalSuiviAnt(GlobalSuiviStruct globalSuiviAnt) {
		this.globalSuiviAnt = globalSuiviAnt;
	}

	public GlobalSuiviStruct getGlobalSuiviExec() {
		return globalSuiviExec;
	}

	public void setGlobalSuiviExec(GlobalSuiviStruct globalSuiviExec) {
		this.globalSuiviExec = globalSuiviExec;
	}

	public String getCodeTiers() {
		if (getSearchBean().getTiers() != null)
			return getSearchBean().getTiers().getCode();
		return null;
	}

	private List<SuiviRecetStruct> getDetailsSuiviRecet(Boolean isTrAnt) {
		List<SuiviRecetStruct> suiveData = null;
		try {
			if (!checkRequiredSuiviRecette())
				return suiveData;
			suiveData = budgetPluriService.getDetailSuiviRec(isTrAnt, Integer.parseInt(getSearchBean().getExercice()),
					getSearchBean().getCodeBudget(), getSearchBean().getCodeDirection(),
					getSearchBean().getCodeService(), getSearchBean().getCodeFongDest(), getSearchBean().getCodeDest(),
					getSearchBean().getCodeProg(), getSearchBean().getCodeNatureGrp(), getSearchBean().getCodeNature(),
					getSearchBean().getNoLtrInt(), getCodeTiers());
			loadLibsDetailsSuiviRecet(suiveData);

		} catch (Exception e) {
			HandlerJSFMessage.addErrorMessage(getErrorMessageFromException(MsgEntry.FAILED, e));
			e.printStackTrace();
		}
		Collections.sort(suiveData);
		return suiveData;
	}

	private void loadLibsDetailsSuiviRecet(List<SuiviRecetStruct> suiveData) {
		if (suiveData == null)
			return;
		for (SuiviRecetStruct data : suiveData) {
			loadLibsDetailSuiviRecet(data);
		}
		loadLibsTiersSuiviRecet(suiveData);
	}

	private void loadLibsTiersSuiviRecet(List<SuiviRecetStruct> suiveData) {
		List<Tiers> tiersCach = null;
		if (suiveData == null)
			return;
		tiersCach = new ArrayList<Tiers>();
		for (SuiviRecetStruct data : suiveData) {
			loadLibLibTiers(data, tiersCach);
		}
	}

	private void loadLibsDetailSuiviRecet(SuiviRecetStruct data) {
		if (data == null)
			return;
		loadLibOrigin(data);
		loadLibService(data);
		loadLibNatGrp(data);
		loadLibProgramme(data);

	}

	private void loadLibLibTiers(SuiviRecetStruct data, List<Tiers> tiersCach) {

		Tiers result = null;
		String codeCpwin = data.getCodeTiers();
		boolean contains = false;
		if (Strings.isBlank(codeCpwin))
			return;

		contains = tiersCach.contains(data.getTiers());
	
		if (!contains) {
			IGestionTiersService service = BudgetHelper.getTiersService();
			result = service.findTiers(codeCpwin);
			tiersCach.add(result);

		} else {
			result = tiersCach.get(tiersCach.indexOf(data.getTiers()));
		}
		data.setTiers(result);

	}

	private void loadLibOrigin(SuiviRecetStruct data) {

		loadLibSuiviRecetData(data, data.getCodeOrigin(), data.getOrigin(),
				getLoaderBean().getListDestinationSuiviCp());
	}

	private void loadLibProgramme(SuiviRecetStruct data) {
		loadLibSuiviRecetData(data, data.getCodeProgramme(), data.getProgramme(),
				getLoaderBean().getListProgrammeSuiviCp());
	}

	public GlobalSuiviStruct getGlobalSuiviTotal() {
		return globalSuiviTotal;
	}

	public void setGlobalSuiviTotal(GlobalSuiviStruct globalSuiviTotal) {
		this.globalSuiviTotal = globalSuiviTotal;
	}

	public List<SuiviRecetStruct> getSuiviRecetTotal() {
		return suiviRecetTotal;
	}

	public void setSuiviRecetTotal(List<SuiviRecetStruct> suiviRecetTotal) {
		this.suiviRecetTotal = suiviRecetTotal;
	}

	public List<RecByOrig> getRecByOrigList() {
		return recByOrigList;
	}

	public void setRecByOrigList(List<RecByOrig> recByOrigList) {
		this.recByOrigList = recByOrigList;
	}

	public void loadRecetOrigin() {
		if (getRecByOrigList() != null)
			return;
		Integer niveau = getDepNivDestInt();
		Integer exercice = Integer.parseInt(getSearchBean().getExercice());
		String codeBudg = getSearchBean().getCodeBudget();
		logger.debug("loadRecetOrigin:niveau={} ", niveau);
		try {
			List<SimpleEntity> list = budgetPluriService.getRecetteByOrigine(exercice, codeBudg,
					getSearchBean().getCodeDirection(), getSearchBean().getCodeService(),
					getSearchBean().getCodeFongDest(), getSearchBean().getCodeDest(), getSearchBean().getCodeProg(),
					getSearchBean().getCodeNatureGrp(), getSearchBean().getCodeNature(), getSearchBean().getNoLtrInt(),
					getCodeTiers(), getDepNivDestInt());
			List<RecByOrig> recByOrigList = createListRecByOrig(exercice, codeBudg, list);
			setRecByOrigList(recByOrigList);
			setRecByOrigTotal(calculateTotal(recByOrigList));
		} catch (Exception e) {
			HandlerJSFMessage.addErrorMessage(getErrorMessageFromException(MsgEntry.FAILED, e));
			e.printStackTrace();
		}
	}

	private RecByOrig calculateTotal(List<RecByOrig> list) {
		RecByOrig result = new RecByOrig();
		if (list == null)
			return result;
		for (RecByOrig recByOrig : list) {
			result.getDataR11().add(recByOrig.getDataR11().getMontantDouble());
			result.getDataR12().add(recByOrig.getDataR12().getMontantDouble());
			result.getDataR13().add(recByOrig.getDataR13().getMontantDouble());
			result.getDataR14().add(recByOrig.getDataR14().getMontantDouble());
			result.getDataR15().add(recByOrig.getDataR15().getMontantDouble());
			result.getDataR22().add(recByOrig.getDataR22().getMontantDouble());
			result.getDataR24().add(recByOrig.getDataR24().getMontantDouble());
			result.getDataR25().add(recByOrig.getDataR25().getMontantDouble());

		}
		return result;

	}

	public void gotoTotalRectOrigin() {
		if (logger.isDebugEnabled()) {
			logger.debug("gotoTotalRectOrigin() - start"); //$NON-NLS-1$
		}
		DialogHelper.openRecetOriginDialog();

		if (logger.isDebugEnabled()) {
			logger.debug("gotoTotalRectOrigin() - end"); //$NON-NLS-1$
		}
	}

	public void closeTotalRectOrigin() {
		if (logger.isDebugEnabled()) {
			logger.debug("closeTotalRectOrigin() - start"); //$NON-NLS-1$
		}

		DialogHelper.closeDialog(null);

		if (logger.isDebugEnabled()) {
			logger.debug("closeTotalRectOrigin() - end"); //$NON-NLS-1$
		}
	}

	private void loadLibNatGrp(SuiviRecetStruct data) {
		loadLibSuiviRecetData(data, data.getCodeNatGrp(), data.getNatGrp(),
				getLoaderBean().getListNatureGroupeSuiviCp());

	}

	private void loadLibService(SuiviRecetStruct data) {
		loadLibSuiviRecetData(data, data.getCodeService(), data.getService(), getLoaderBean().getListServiceSuiviCp());

	}

	private void loadLibSuiviRecetData(Object data, String codeData, SimpleEntity SuiviItem,
			List<SimpleEntity> listDataSuiviRecet) {
		if (Strings.isBlank(codeData))
			return;
		SuiviItem.setDesignation(BudgetHelper.getlib(codeData, listDataSuiviRecet));

	}

	public StreamedContent getXlsSuiviRecetOrigin() {
		StreamedContent returnStreamedContent = null;
		ExcelHandler excel = new ExcelHandler(ExcelModelEnum.RECETTES_PAR_ORIGIN, getRecByOrigList());
		returnStreamedContent = excel.getExcelFile();
		return returnStreamedContent;
	}

	public StreamedContent getXlsSuiviRecetTotal() {
		StreamedContent returnStreamedContent = null;

		ExcelHandler excel = new ExcelHandler(ExcelModelEnum.DETAILS_SUIVI_RECET_TOTAL, getSuiviRecetTotal());
		returnStreamedContent = excel.getExcelFile();

		return returnStreamedContent;

	}

	public RecByOrig getRecByOrigTotal() {
		return recByOrigTotal;
	}

	public void setRecByOrigTotal(RecByOrig recByOrigTotal) {
		this.recByOrigTotal = recByOrigTotal;
	}

	public String searchSuiviAe() {
		String outCome = Action.SUIVI_AE.getOutcome();

		if (!checkRequiredSuiviCp())
			return outCome;
		try {
			setSuiviAeCp(budgetPluriService.getSuiviAeCp(Integer.parseInt(getSearchBean().getExercice()),
					getSearchBean().getCodeNatureGrp()));
			setSuiviEjPluri(budgetPluriService.getSuiviPluriEj(Integer.parseInt(getSearchBean().getExercice()),
					getSearchBean().getCodeNatureGrp()));
		} catch (Exception e) {
			HandlerJSFMessage.addErrorMessage(getErrorMessageFromException(MsgEntry.FAILED, e));
			e.printStackTrace();
		}

		return outCome;

	}

	public List<SuiviAeCpStruct> getSuiviAeCp() {
		return suiviAeCp;
	}

	public void setSuiviAeCp(List<SuiviAeCpStruct> suiviAeCp) {
		this.suiviAeCp = suiviAeCp;
	}

	public double getTotCpVote() {
		return calculateTotCPVote();
	}

	public void setTotCpVote(double totCpVote) {
		this.totCpVote = totCpVote;
		logger.debug("setTotCpVote: {}",this.totCpVote);
	}

	public double calculateTotCPVote() {

		return budgetPluriService.getSuiviAeCpTotCPVote(Integer.parseInt(getSearchBean().getExercice()),
				getSearchBean().getCodeNatureGrp());

	}

	public List<SuiviPluriStruct> getSuiviEjPluri() {
		return suiviEjPluri;
	}

	public void setSuiviEjPluri(List<SuiviPluriStruct> suiviEjPluri) {
		this.suiviEjPluri = suiviEjPluri;
	}

	public StreamedContent getXlsSuiviAECP() {
		StreamedContent returnStreamedContent = null;
		ExcelHandler excel = new ExcelHandler(ExcelModelEnum.DETAILS_SUIVIAECP, getSuiviAeCp());
		returnStreamedContent = excel.getExcelFile();
		if (logger.isDebugEnabled()) {
			logger.debug("getXlsSuiviAECP() - end"); //$NON-NLS-1$
		}
		return returnStreamedContent;

	}

	public StreamedContent getXlsEjPluri() {
		StreamedContent returnStreamedContent = null;
		ExcelHandler excel = new ExcelHandler(ExcelModelEnum.DETAILS_EJ_PLURI, getSuiviEjPluri());
		returnStreamedContent = excel.getExcelFile();
		if (logger.isDebugEnabled()) {
			logger.debug("getXlsEjPluri() - end"); //$NON-NLS-1$
		}
		return returnStreamedContent;

	}

	public double getRapInitTotal() {
		double total = 0d;
		for (SuiviAeCpStruct suivi : suiviAeCp) {
			total += suivi.getRapInitiale();
		}
		return total;
	}

	public double getCaTotal() {
		double total = 0d;
		for (SuiviAeCpStruct suivi : suiviAeCp) {
			total += suivi.getCa();
		}
		return total;
	}

	public double getComplTotal() {
		double total = 0d;
		for (SuiviAeCpStruct suivi : suiviAeCp) {
			total += suivi.getCompl();
		}
		return total;
	}

	public double getDpAntTotal() {
		double total = 0d;
		for (SuiviAeCpStruct suivi : suiviAeCp) {
			total += suivi.getDpAnt();
		}
		return total;
	}

	public double getRapAntTotal() {
		double total = 0d;
		for (SuiviAeCpStruct suivi : suiviAeCp) {
			total += suivi.getRapAnt();
		}
		return total;
	}

	public double getCoTotal() {
		double total = 0d;
		for (SuiviAeCpStruct suivi : suiviAeCp) {
			total += suivi.getCo();
		}
		return total;
	}

	public double getEjTotal() {
		double total = 0d;
		for (SuiviAeCpStruct suivi : suiviAeCp) {
			total += suivi.getEj();
		}
		return total;
	}

	public double getDpExTotal() {
		double total = 0d;
		for (SuiviAeCpStruct suivi : suiviAeCp) {
			total += suivi.getDpEx();
		}
		return total;
	}

	public double getRapExTotal() {
		double total = 0d;
		for (SuiviAeCpStruct suivi : suiviAeCp) {
			total += suivi.getRapEx();
		}
		return total;
	}

	public double getDpTotTotal() {
		double total = 0d;
		for (SuiviAeCpStruct suivi : suiviAeCp) {
			total += suivi.getDpTot();
		}
		return total;
	}

	public double getdrTotal() {
		double total = 0d;
		for (SuiviAeCpStruct suivi : suiviAeCp) {
			total += suivi.getDr();
		}
		return total;
	}

	public double getDpNetTotal() {
		double total = 0d;
		for (SuiviAeCpStruct suivi : suiviAeCp) {
			total += suivi.getDpNet();
		}
		return total;
	}

	public double getRapActTotal() {
		double total = 0d;
		for (SuiviAeCpStruct suivi : suiviAeCp) {
			total += suivi.getRapAct();
		}
		return total;
	}

	public double getDispEjTotal() {
		double total = 0d;
		for (SuiviAeCpStruct suivi : suiviAeCp) {
			total += suivi.getDispEj();
		}
		return total;
	}

	public double getRapDiffTotal() {
		double total = 0d;
		for (SuiviAeCpStruct suivi : suiviAeCp) {
			total += suivi.getRapDiff();
		}
		return total;
	}

	public double getBesoinTotal() {
		double total = 0d;
		for (SuiviAeCpStruct suivi : suiviAeCp) {
			total += suivi.getBesoin();
		}
		return total;
	}

	public double getMntTotalTot() {
		double total = 0d;
		logger.info("suiviEjPluri: " + getSuiviEjPluri());
		for (SuiviPluriStruct suivi : getSuiviEjPluri()) {
			logger.info("suivi: " + suivi);
			total += suivi.getMntTotal();
		}
		return total;
	}

	public double getPlanCpCourantTot() {
		double total = 0d;
		for (SuiviPluriStruct suivi : getSuiviEjPluri()) {
			total += suivi.getPlanCpCourant();
		}
		return total;
	}

	public double getPlanCpSupTot() {
		double total = 0d;
		for (SuiviPluriStruct suivi : getSuiviEjPluri()) {
			total += suivi.getPlanCpSup();
		}
		return total;
	}

	@Override
	protected void prepare() throws Exception {
		super.prepare();
		switch (getCurrentAction()) {
		case ADRESSAGE_BUDGET_PLURI_CPDEST:
			logger.debug("prepare: ADRESSAGE_BUDGET_PLURI_CPDEST");
			getGroupDestCpList();
			
			break;

		default:
			break;
		}
	}

}
