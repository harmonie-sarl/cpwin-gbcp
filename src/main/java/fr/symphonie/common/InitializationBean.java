package fr.symphonie.common;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.faces.event.AjaxBehaviorEvent;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.symphonie.budget.ui.beans.NavigationBean.Action;
import fr.symphonie.common.core.ICommonService;
import fr.symphonie.common.util.BudgetHelper;
import fr.symphonie.common.util.MsgEntry;
import fr.symphonie.common.util.Util;
import fr.symphonie.tools.gts.core.GtsService;
import fr.symphonie.tools.gts.model.ArticleDetails;
import fr.symphonie.tools.meta4dai.model.LbData;
import fr.symphonie.tools.meta4dai.model.Period;
import fr.symphonie.tools.meta4dai.model.Period.STATUS;
import fr.symphonie.util.HandlerJSFMessage;
import fr.symphonie.util.Helper;
import fr.symphonie.util.model.SimpleEntity;
import fr.symphonie.util.model.Trace;

public class InitializationBean implements IBasicBean,Serializable {
	 /**
	 * 
	 */
	private static final long serialVersionUID = 5413768996000406117L;

	private static final Logger logger = LoggerFactory.getLogger("initialisation");

	private Integer newExercice;
	private String codeBudget;
	private List<Integer> initExercices;
	private List<String> listBudgets = null;
	private List<SimpleEntity> errorReport;
	// META4DAI
	private LbData origineLBC;
	private LbData newLBC;
	private List<Period> periodList;
	private List<LbData> lbcList;
	// GTS
	private List<ArticleDetails> detailList;

	public void executeSearch() {
		Action action = getCurrentAction();
		Trace t = Helper.createTrace();
		dataReset();
		logger.debug("executeSearch: action:{}",action);
		try {
		switch (action) {
		case INITIALIZE_META4DAI:
			Integer origineLbcNo = getCommonService().getMeta4DaiNoLbiStr(getOrigineExercice());
			List<Period> periodes = getCommonService().getMeta4daiPeriodList(getOrigineExercice(), getCodeBudget());
			if(origineLbcNo==null || CollectionUtils.isEmpty(periodes)) {
				HandlerJSFMessage.addErrorMessage(String.format("Exercice %d non configuré",getOrigineExercice()));
				return;
			}
			List<LbData> list = getCommonService().getLbData(getOrigineExercice(),
					Arrays.asList(new Integer[] { origineLbcNo }));
			if(CollectionUtils.isEmpty(list)) {
				addError("LBI",getOrigineExercice(),origineLbcNo,"");
			}
			else {
				setOrigineLBC(list.get(0));
				LbData newLbData =getCommonService().loadLb(getNewExercice(), getCodeBudget(), getOrigineLBC());
				if(newLbData==null) {
					addError("LBI",getNewExercice(),getOrigineLBC().getNumero(),getOrigineLBC().toString());					
				}
				else {
					setNewLBC(newLbData);
				}
				 
			}
			periodes.stream().forEach(x -> {
				x.setTotal(BigDecimal.ZERO);
				x.setTrace(t);
				x.setEtat(STATUS.OUVERT);
				x.setExercice(getNewExercice());
			});
			setPeriodList(periodes);
			break;
		case INITIALIZE_GTS:
			List<ArticleDetails> details = getGtsService().getArticleDetails(getOrigineExercice());
			logger.debug("executeSearch: details: {}",details.size());
			if(CollectionUtils.isEmpty(details)) {
				HandlerJSFMessage.addErrorMessage(String.format("Exercice %d non configuré",getOrigineExercice()));
				return;
			}
			details.stream().forEach(x -> {
				LbData origineLbData = new LbData(x.getNoLbi(), x.getDestination().getCode(), x.getNature().getCode(),
						x.getProgramme().getCode(), x.getService().getCode(), null);
				LbData newLbData = getCommonService().loadLb(getNewExercice(), getCodeBudget(), origineLbData);
				if(newLbData==null) {
					addError("LBI",getNewExercice(),origineLbData.getNumero(),origineLbData.toString());					
				}
				else {
				String imputHtd = getCommonService().getImputHtd(getNewExercice(), newLbData.getNumero());
				if(StringUtils.isBlank(imputHtd)) {
					addError("imputHtd",getNewExercice(),newLbData.getNumero(),newLbData.toString());	
				}
				else {
				String[] compte = getGtsService().getCtaCompteImput(getNewExercice(), imputHtd);
				x.setTrace(t);
				x.setExercice(getNewExercice());
				x.setNoLbi(newLbData.getNumero());
				x.setCompteProduit(new SimpleEntity(imputHtd, ""));
				x.setImputTtc(compte[0]);
				x.setImputTva(compte[1]);
				}
				}
			});
			setDetailList(details);

			break;

		default:
			break;
		}
		} catch (Exception e) {
			HandlerJSFMessage.addErrorMessage(Util.getErrorMessageFromException(MsgEntry.FAILED, e));
		}

	}

	private void addError(String prefix,Integer exercice,Integer numero, String data) {
		String codeError=String.format("%s-Manquante-%d",prefix, numero);
		String desError=String.format("%s Manquante pour %d : %s",prefix,exercice, data);
		getErrorReport().add(new SimpleEntity(codeError, desError) );
		
	}

	public void executeValidation() {
		Action action = getCurrentAction();
		try {
		switch (action) {
		case INITIALIZE_META4DAI:
			getCommonService().initializeMeta4Dai(getNewExercice(), getNewLBC(), getPeriodList());
			break;
		case INITIALIZE_GTS:
			getCommonService().saveList(getDetailList());
			break;

		default:
			break;
		}
		headerReset();
		
		HandlerJSFMessage.addInfo(MsgEntry.SUCCES);
		} catch (Exception e) {
			HandlerJSFMessage.addErrorMessage(Util.getErrorMessageFromException(MsgEntry.FAILED, e));
		}

	}

	@Override
	public void initialize() {
		reset();

	}

	private void headerReset() {
		setNewExercice(null);
		setCodeBudget(null);
		setInitExercices(null);
	}

	private void dataReset() {
		setNewLBC(null);
		setOrigineLBC(null);
		setDetailList(null);
		setPeriodList(null);
		setLbcList(null);
		setErrorReport(null);
	}

	public Integer getNewExercice() {
		return newExercice;
	}

	public void setNewExercice(Integer newExercice) {
		this.newExercice = newExercice;
	}

	public Integer getOrigineExercice() {
		if(getNewExercice()==null) return null;
		return getNewExercice() - 1;
	}

	public String getCodeBudget() {
		return codeBudget;
	}

	public void setCodeBudget(String codeBudget) {
		this.codeBudget = codeBudget;
	}

	private Action getCurrentAction() {
		return BudgetHelper.getNavigationBean().getCurrentAction();
	}

	public boolean isCommonRequiredDone() {
		if (getNewExercice() == null)
			return false;
		if (StringUtils.isBlank(getCodeBudget()))
			return false;
		return true;
	}

	private ICommonService getCommonService() {
		return BudgetHelper.getCommonService();
	}

	private GtsService getGtsService() {
		return BudgetHelper.getGtsService();
	}

	public LbData getOrigineLBC() {
		return origineLBC;
	}

	public void setOrigineLBC(LbData origineLBC) {
		this.origineLBC = origineLBC;
	}

	public LbData getNewLBC() {
		return newLBC;
	}

	public void setNewLBC(LbData newLBC) {
		this.newLBC = newLBC;
	}

	public List<Period> getPeriodList() {
		return periodList;
	}

	public void setPeriodList(List<Period> periodList) {
		this.periodList = periodList;
	}

	public List<ArticleDetails> getDetailList() {
		return detailList;
	}

	public void setDetailList(List<ArticleDetails> detailList) {
		this.detailList = detailList;
	}

	public List<Integer> getInitExercices() {

		if (initExercices == null) {

			List<Integer> configuredExercices = null;
			Action action = getCurrentAction();
			switch (action) {
			case INITIALIZE_META4DAI:
				configuredExercices = getCommonService().getMeta4DaiExercices();
				break;
			case INITIALIZE_GTS:
				configuredExercices = getGtsService().getGtsExercices();
				break;
			default:
				break;

			}
			initExercices = new ArrayList<Integer>();
			List<Integer> allExercices = getCommonService().getOpenedExerciceList();
			for (Integer x : allExercices) {
				if (!configuredExercices.contains(x))
					initExercices.add(x);
			}

		}
		return initExercices;
	}

	public void setInitExercices(List<Integer> initExercices) {
		this.initExercices = initExercices;
	}

	public List<String> getListBudgets() {
		if(getNewExercice()==null) return null;
		if (CollectionUtils.isEmpty(this.listBudgets)) {
			List<String> listBudgets = getCommonService().getListBudgets(getNewExercice());
			setListBudgets(listBudgets);
			if (listBudgets.size() == 1)
				setCodeBudget(listBudgets.get(0));
		}
		return listBudgets;
	}

	public void setListBudgets(List<String> listBudgets) {
		this.listBudgets = listBudgets;
	}

	public void onExerciceChange() {
		dataReset();
		if (getNewExercice() == null)
			return;
		getListBudgets();
	}

	public List<LbData> getLbcList() {
		if (this.lbcList == null && getNewLBC() != null) {
			this.lbcList = new ArrayList<LbData>();
			this.lbcList.add(getNewLBC());
		}
		return lbcList;
	}

	public void setLbcList(List<LbData> lbcList) {
		this.lbcList = lbcList;
	}
	public boolean isInitDataVisible() {
		Action action = getCurrentAction();
		if(!isCommonRequiredDone())return false;
		if(isErrorReportVisible())return false;
		switch (action) {
		case INITIALIZE_META4DAI:
			if(CollectionUtils.isEmpty(getLbcList())) return false;
			if(CollectionUtils.isEmpty(getPeriodList())) return false;
			break;
		case INITIALIZE_GTS:
			if(CollectionUtils.isEmpty(getDetailList())) return false;
			break;
		default:
			break;
		}	
		return true;
	}
	
	public void onCodeBudgChange(AjaxBehaviorEvent event){

		dataReset();
	}
	public List<SimpleEntity> getErrorReport() {
		if(errorReport==null)errorReport=new ArrayList<>();
		return errorReport;
	}
	public void setErrorReport(List<SimpleEntity> errorReport) {
		this.errorReport = errorReport;
	}
	public boolean isDataVisible()
	   {
		if(isErrorReportVisible()) return true;
		if(isInitDataVisible())return true;
		return false; 
	   }
	public boolean isErrorReportVisible() {
		if (CollectionUtils.isEmpty(getErrorReport()))
			return false;
		return true;
	}

	@Override
	public void reset() {
		headerReset();
		dataReset();
		
	}

}