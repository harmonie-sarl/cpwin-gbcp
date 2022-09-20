package fr.symphonie.tools.common;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
//import org.apache.logging.log4j.util.Strings;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.symphonie.common.IBasicBean;
import fr.symphonie.common.core.ICommonService;
import fr.symphonie.common.util.Constant;
import fr.symphonie.common.util.EmailSender;
import fr.symphonie.common.util.EmailSenderImpl;
import fr.symphonie.common.util.MsgEntry;
import fr.symphonie.common.util.Util;
import fr.symphonie.excel.ExcelFileImportor;
import fr.symphonie.exception.MissingConfiguration;
import fr.symphonie.impor.Importable;
import fr.symphonie.util.HandlerJSFMessage;
import fr.symphonie.util.Helper;
import fr.symphonie.util.model.SimpleEntity;

public abstract class GenericExcelImportor<T extends Importable> extends ExcelFileImportor implements IBasicBean {
	private static final Logger logger = LoggerFactory.getLogger(GenericExcelImportor.class);
	/**
	 * Input elements
	 */
	private String exercice;
	private String codeBudget;
	private List<String> listBudgets = null;
	private boolean requirmentsStatus;

	@Override
	public void initialize() {
		reset();
		checkRequirements();

	}

	public boolean isCommonRequiredDone() {
		logger.debug("isCommonRequiredDone: isRequirmentsStatus={}",isRequirmentsStatus());
		if (!isRequirmentsStatus())
			return false;
		if (StringUtils.isBlank(getExercice()))
			return false;
		if (StringUtils.isBlank(getCodeBudget()))
			return false;
		return true;
	}

	public boolean isErrorReportVisible() {
		if (CollectionUtils.isEmpty(getErrorReport()))
			return false;
		return true;
	}

	public String getErreursCount() {
		return "(" + getErrorReport().size() + ")";
	}

	@Override
	protected String getSavedPath() throws MissingConfiguration {
		return Constant.getProjectRootPath().resolve(getModuleName()).toString();
	}

	@Override
	protected String getImportFileName() {
		return getSourceFileName();
	}

	@Override
	public void reset() {
		super.reset();
	}

	@Override
	protected void resetDynamicList() {
		setErrorReport(null);
	}

	protected abstract String getModuleName();

	public void addErrorToReport(T data, String message) {
		getErrorReport().add(new SimpleEntity(data.getRowNum(), message, data.toString()));
	}

	public void addErrorToReport(T data, String errorPK, String message) {
		getErrorReport().add(new SimpleEntity(data.getRowNum(), errorPK, message, data.toString()));
	}

	public void displayMessage(String msgKey, String msgArg, boolean isWarn) {

		if (msgKey == null)
			return;
		String msg = HandlerJSFMessage.getErrorMessage(msgKey);
		if (msgArg != null) {
			msg = HandlerJSFMessage.formatMessage(msg, new Object[] { msgArg });
		}

		if (isWarn)
			HandlerJSFMessage.addWarnMessage(msg);
		else
			HandlerJSFMessage.addErrorMessage(msg);

	}

	public void onExerciceChange() {
		logger.debug("onExerciceChange ...");
		reset();
		if (StringUtils.isBlank(getExercice()))
			return;
		try {
//			if(!isRequirementsValid()) {
//				boolean check=requirementsCheck();
//				setRequirementsValid(check);
//				if(!check) return;
//			}

			getListBudgets();
			prepare();
		} catch (Exception e) {
			HandlerJSFMessage.addErrorMessage(Util.getErrorMessageFromException(MsgEntry.FAILED, e));
			logger.error("onExerciceChange: ERROR", e);
		}
		logger.debug("onExerciceChange .END.");

	}

//	private boolean requirementsCheck() {
//		logger.debug("requirementsCheck ...");
//		if (getRequirements() == null)
//			return true;
//		Integer count = 0;
//		boolean result = true;
//		String dbOjectName, dbOjectType;
//		for (SimpleEntity item : getRequirements()) {
//			dbOjectName = item.getCode();
//			dbOjectType = item.getDesignation();
//			count = getCommonService().requirementCheck(dbOjectName, dbOjectType);
//			if (count == 0) {
//				displayMessage("configuration.error", dbOjectName, false);
//				result = false;
//			}
//		}
//		logger.debug("requirementsCheck .END. {}", result);
//		return result;
//	}

	protected abstract void prepare() throws Exception;

	protected abstract SimpleEntity[] getRequirements();

	public String getCodeBudget() {
		return codeBudget;
	}

	public void setCodeBudget(String codeBudget) {
		this.codeBudget = codeBudget;
	}

	public String getExercice() {
		return exercice;
	}

	public void setExercice(String exercice) {
		this.exercice = exercice;
	}

	public List<String> getListBudgets() {
		if (CollectionUtils.isEmpty(this.listBudgets)) {
			List<String> listBudgets = getCommonService().getListBudgets(getExec());
			setListBudgets(listBudgets);
			if (listBudgets.size() == 1)
				setCodeBudget(listBudgets.get(0));
		}
		return listBudgets;
	}

	protected abstract ICommonService getCommonService();

	public void setListBudgets(List<String> listBudgets) {
		this.listBudgets = listBudgets;
	}

	public Integer getExec() {
		return Helper.stringToInt(getExercice());
	}

	public boolean isRequirmentsStatus() {
		return requirmentsStatus;
	}

	public void setRequirmentsStatus(boolean requirmentsStatus) {
		this.requirmentsStatus = requirmentsStatus;
	}

	public void checkRequirements() {
		logger.debug("checkRequirements ...");
		setRequirmentsStatus(true);
		if (getTasks() == null)
			return;

		for (String taskName : getTasks()) {
			try {
				executeTask(taskName);
			} catch (Exception e) {
				HandlerJSFMessage.addErrorMessage(Util.getErrorMessageFromException(MsgEntry.FAILED, e));
				logger.error("checkRequirements: KO: {}", e.getMessage());
				setRequirmentsStatus(false);
			}
		}

	}

	protected void executeTask(String taskName) throws Exception {
		// TODO Auto-generated method stub

	}

	protected String[] getTasks() {
		// TODO Auto-generated method stub
		return null;
	}

	public EmailSender getEmailSender(String contextPath) {
		return new EmailSenderImpl(contextPath, getCommonService());
	}

}
