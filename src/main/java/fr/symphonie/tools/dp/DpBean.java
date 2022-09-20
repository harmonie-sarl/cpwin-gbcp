package fr.symphonie.tools.dp;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.faces.event.ValueChangeEvent;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.util.Strings;
import org.primefaces.event.CellEditEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.symphonie.budget.core.model.pluri.LigneBudgetaireAE;
import fr.symphonie.budget.ui.beans.NavigationBean.Action;
import fr.symphonie.common.util.BudgetHelper;
import fr.symphonie.common.util.MsgEntry;
import fr.symphonie.common.util.SepaHelper;
import fr.symphonie.common.util.Util;
import fr.symphonie.cpwin.model.Iban;
import fr.symphonie.cpwin.model.Tiers;
import fr.symphonie.tools.common.CommonToolsBean;
import fr.symphonie.tools.common.DataListBean;
import fr.symphonie.tools.nantes.model.Operation;
import fr.symphonie.tools.nantes.model.SFParams;
import fr.symphonie.util.HandlerJSFMessage;
import fr.symphonie.util.Helper;

public class DpBean extends CommonToolsBean implements Serializable{
	
	private static final long serialVersionUID = 6872532576137763728L;
	private static final Logger logger = LoggerFactory.getLogger("dp-tools");
	private Tiers selectedTiers;
	private Operation operationToDelete;

	private String objet;
	private BigDecimal amount;
	private List<Operation> operationList;
	private SFParams params;
	private boolean generationCompleted;
	
	public List<Tiers> autoCompleteTiers(String prefix) {
		logger.debug("###### Auto Complete Tiers prefix=" + prefix);
		List<Tiers> result=null;
		try {
			result = getCommonService().findTiersList(!(Helper.isSpecialPrefix(prefix)),	Helper.removeSpecialPrefix(prefix),false);
		} catch (Exception e) {

		}

		if (logger.isDebugEnabled()) {

			logger.debug("###### Auto Complete Tiers prefix=" + prefix + ", " +((result!=null)? result.size():0)
					+ " éléments trouvés - end");
		}
		return result;
	}
	public void onTiersSelected() {
		logger.debug("onTiersSelected: getSelectedTiers={}",getSelectedTiers());
		
	}
	public void executeGeneration() {
		logger.debug("executeGeneration: Début");
		try {
			getParams().setCompteProduit(BudgetHelper.getDataListBean().getCompteCharge());
			getCommonService().createSF(getParams(),getSelectedLb(),getOperationList());			
			setGenerationCompleted(true);
			HandlerJSFMessage.addInfo(MsgEntry.SUCCES);
		} catch (Exception e) {
			HandlerJSFMessage.addErrorMessage(Util.getErrorMessageFromException(MsgEntry.FAILED, e));
		}
		logger.debug("executeGeneration: Fin");
		
	}
	public boolean isGenerationPermitted() {
		if(isGenerationCompleted())return false;
		return isRequiredDataForGenerate();
	}

	public boolean isGenerationCompleted() {
		return generationCompleted;
	}

	public void setGenerationCompleted(boolean generationCompleted) {
		this.generationCompleted = generationCompleted;
	}
public boolean isRequiredDataForGenerate() {
		
		if (!isCommonRequiredDone())
			return false;
		if (getNoLbi() == null)
			return false;	
		if (Strings.isBlank(getObjet()))
			return false;
		if (getDataListBean().getCompteCharge() == null)
			return false;
		if (Strings.isBlank(getDataListBean().getCompteClient()))
			return false;
		if (Strings.isBlank(getDataListBean().getCompteTva()))
				return false;
		if (!getParams().isValid())
			return false;	
		
		return true;


	}
	public void selectLbListener()
	{
		DataListBean dataList=getDataListBean();
		LigneBudgetaireAE lb=dataList.findDpLbi();
		dataList.setSelectedLb(lb);
		List<String> list=dataList.getCodeGestList();
		if(!CollectionUtils.isEmpty(list) && list.size()==1) {
			dataList.setCodeGest(list.get(0));
			selectCodeGestListener();
		}
		
	}
	public void selectCodeGestListener() {
		DataListBean dataList=getDataListBean();
		setParams(getCommonService().loadSfParams(getExerciceInt(),dataList.getSelectedLb().getGroupNat(),dataList.getCodeGest()));
	}
	public void selectCmptProdListener()
	{
		getDataListBean().initializCtaCompteImput();
	}
	private DataListBean getDataListBean() {
		return BudgetHelper.getDataListBean();
	}
	public Tiers getSelectedTiers() {
		return selectedTiers;
	}

	public void setSelectedTiers(Tiers selectedTiers) {
		this.selectedTiers = selectedTiers;
	}
	public Iban getIban() {
		if(getSelectedTiers()==null)return null;
		if(CollectionUtils.isEmpty(getSelectedTiers().getIbanList()))return null;
		Optional<Iban> iban= getSelectedTiers().getIbanList()
				.stream()
				.filter(b-> b.isOuvert() && b.isValide() && !StringUtils.isBlank(b.getIban()))
				.findFirst();
		return iban.isPresent()?iban.get():null;
	}
	public boolean isAddOperationAuthorized() {
		if(!isRequirmentsStatus())return false;
		if(getSelectedTiers()==null)return false;
		if(getIban()==null)return false;
		return true;
	}
	public SFParams getParams() {
		return params;
	}

	public void setParams(SFParams params) {
		this.params = params;
	}
	public BigDecimal getAmount() {
		if(amount==null)amount=BigDecimal.ZERO;
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
	public String getObjet() {
		return objet;
	}

	public void setObjet(String objet) {
		this.objet = objet;
	}

	public List<Operation> getOperationList() {
		if(operationList==null)operationList=new ArrayList<Operation>();
		return operationList;
	}

	public void setOperationList(List<Operation> operationList) {
		this.operationList = operationList;
	}
	public void onDpEdit(CellEditEvent event) {

	}
	public void executeDeleteOperation() {
		getOperationList().remove(operationToDelete);
    }

	public Operation getOperationToDelete() {
		return operationToDelete;
	}

	public void setOperationToDelete(Operation operationToDelete) {
		this.operationToDelete = operationToDelete;
	}
	public void executeAddOperation() {
		logger.debug("executeAddOperation: {}, {}",getSelectedTiers(),getAmount());
		Operation op=new Operation(getSelectedTiers(),getAmount(),getObjet());
		setModePaie(op);
		
		getOperationList().add(op);
		resetSearch();
	}
	
	private void setModePaie(Operation op) {
		String modePaie=null;
		if(op.getIban()==null) return;
		if(SepaHelper.isFrenchIban(op.getIban().getIban())) {
			modePaie="SF";
		}
		else {
			modePaie=getCommonService().getModePaie(SepaHelper.getIbanCountryCode(op.getIban().getIban()));
			if(modePaie==null)modePaie="PE";
		}
		op.setModePaie(modePaie);
		
	}
	public Integer getNoLbi() {		 
		return (getSelectedLb()!=null)?getSelectedLb().getNoLbi():null;
	}

	public Integer getNoEnv() {
		return (getSelectedLb()!=null)?getSelectedLb().getNoEnv():null;
	}
	public LigneBudgetaireAE getSelectedLb() {
		return BudgetHelper.getDataListBean().getSelectedLb();
	}

	public String getGroupNat() {		
		return (getSelectedLb()!=null)?getSelectedLb().getGroupNat():null;
	}
	@Override
	protected void executeTask(String taskName) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected String[] getTasks() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void prepare() throws Exception {
	Action currentAction=BudgetHelper.getNavigationBean().getCurrentAction();
			switch (currentAction) {
			case DP_GENERATION: case ETUDIANT_GENERATION_DP:
				logger.debug("prepare: DP_GENERATION");
				initDataForGenerateDP();
				break;
				
			default:
				break;
			}
			
	     }
	private void initDataForGenerateDP() {

		DataListBean dataBean = BudgetHelper.getDataListBean();
		dataBean.setExec(getExercice());
		dataBean.setListBudgets(getListBudgets());
		dataBean.setCodeBudget(getCodeBudget());

	}

	@Override
	public void reset() {
		resetSearch();
		setParams(null);
		getDataListBean().reset();
		setObjet(null);
		dataReset();
		
	}
	private void dataReset() {
		setOperationList(null);
		setOperationToDelete(null);
		setGenerationCompleted(false);
		setParams(null);
		
	}
	private void resetSearch() {
		setSelectedTiers(null);
		setAmount(null);
		
	}

	@Override
	protected Logger getLogger() {
		return logger;
	}
	public void trsStudentChangeListener(ValueChangeEvent e) {
        resetSearch();
	}

}