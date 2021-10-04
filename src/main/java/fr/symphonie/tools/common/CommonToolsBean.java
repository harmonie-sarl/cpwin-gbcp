package fr.symphonie.tools.common;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.primefaces.event.FileUploadEvent;
import org.slf4j.Logger;

import fr.symphonie.budget.ui.beans.NavigationBean.Action;
import fr.symphonie.common.IBasicBean;
import fr.symphonie.common.core.ICommonService;
import fr.symphonie.common.util.BudgetHelper;
import fr.symphonie.common.util.EmailSender;
import fr.symphonie.common.util.EmailSenderImpl;
import fr.symphonie.common.util.MsgEntry;
import fr.symphonie.common.util.Util;
import fr.symphonie.util.HandlerJSFMessage;
import fr.symphonie.util.Helper;

public abstract class CommonToolsBean implements IBasicBean{
	private String exercice;
	private String codeBudget;	
	private List<String> listBudgets=null;
	private boolean requirmentsStatus;
	private FileUploadEvent importFileUploadEvent;
	
	@Override
	public void initialize() {
		reset();
		checkRequirements();
		
	}
	public String getExercice() {
		return exercice;
	}
	public void setExercice(String exercice) {
		this.exercice = exercice;
	}
	public Integer getExerciceInt() {
		return Helper.stringToInt(getExercice());
	}
	public String getCodeBudget() {
		return codeBudget;
	}
	public void setCodeBudget(String codeBudget) {
		this.codeBudget = codeBudget;
	}
	public List<String> getListBudgets() {
		if(CollectionUtils.isEmpty(this.listBudgets)) {
			List<String> listBudgets=getCommonService().getListBudgets(getExerciceInt());
			setListBudgets(listBudgets);
			if (listBudgets.size()==1)	setCodeBudget(listBudgets.get(0));
		}
		return listBudgets;
	}
	public void setListBudgets(List<String> listBudgets) {
		this.listBudgets = listBudgets;
	}
	public void onExerciceChange() {
		getLogger().debug("onExerciceChange ...");	
		reset();
		if(StringUtils.isBlank(getExercice())) return;
		try {			
			getListBudgets();
			prepare();
			checkRequirements();
		}catch(Exception e){
			HandlerJSFMessage.addErrorMessage(Util.getErrorMessageFromException(MsgEntry.FAILED, e));
			getLogger().error("onExerciceChange: ERROR",e);
		}
		getLogger().debug("onExerciceChange .END.");	
		
	}
	
	protected abstract void executeTask(String taskName) throws Exception;
	protected abstract String[] getTasks();
	protected abstract void prepare() throws Exception;
	public abstract void reset();
	protected abstract Logger getLogger();
	protected ICommonService getCommonService() {
		return BudgetHelper.getCommonService();
	}
	public boolean isCommonRequiredDone() {
		if (StringUtils.isBlank(getExercice()))
			return false;
		if (StringUtils.isBlank(getCodeBudget()))
			return false;
		return true;
	}
	public Action getCurrentAction() {
		return BudgetHelper.getNavigationBean().getCurrentAction();
	}
	public boolean isRequirmentsStatus() {
		return requirmentsStatus;
	}
	public void setRequirmentsStatus(boolean requirmentsStatus) {
		this.requirmentsStatus = requirmentsStatus;
	}
	public void checkRequirements() {
		getLogger().debug("checkRequirements ...");	
		setRequirmentsStatus(true);
		if(getTasks()==null)return;
		
		for(String taskName: getTasks()) {
			try {
				executeTask(taskName);
			} catch (Exception e) {
				HandlerJSFMessage.addErrorMessage(Util.getErrorMessageFromException(MsgEntry.FAILED, e));
				getLogger().error("checkRequirements: KO: {}",e.getMessage());
				setRequirmentsStatus(false);
			}
		}				
		
	}

	public EmailSender getEmailSender(String contextPath) {
		return new EmailSenderImpl(contextPath, getCommonService());
	}
	public FileUploadEvent getImportFileUploadEvent() {
		return importFileUploadEvent;
	}


	public void setImportFileUploadEvent(FileUploadEvent importFileUploadEvent) {
		this.importFileUploadEvent = importFileUploadEvent;
	}
	
}
