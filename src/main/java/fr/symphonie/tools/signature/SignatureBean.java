package fr.symphonie.tools.signature;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import javax.imageio.ImageIO;

import org.apache.commons.lang.StringUtils;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.StreamedContent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.symphonie.budget.ui.beans.NavigationBean.Licence;
import fr.symphonie.budget.ui.beans.pluri.DialogHelper;
import fr.symphonie.common.core.ICommonService;
import fr.symphonie.common.model.AppCfgConfig;
import fr.symphonie.common.util.BudgetHelper;
import fr.symphonie.common.util.Constant;
import fr.symphonie.common.util.EmailSender;
import fr.symphonie.common.util.EmailSenderImpl;
import fr.symphonie.common.util.MsgEntry;
import fr.symphonie.common.util.Util;
import fr.symphonie.config.ConfigParam;
import fr.symphonie.exception.MissingConfiguration;
import fr.symphonie.tools.common.CommonToolsBean;
import fr.symphonie.tools.signature.model.Signature;
import fr.symphonie.util.FileHelper;
import fr.symphonie.util.HandlerJSFMessage;
import fr.symphonie.util.Helper;
import fr.symphonie.util.model.User;

public class SignatureBean extends CommonToolsBean implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -5637450983868244118L;

	Logger logger = LoggerFactory.getLogger("signature-tools");

	private List<Signature> signatureList;
	private List<User> allUsers;
	private User selectedUser;
	private Signature selectedSignature;
	private boolean updateMode;

	protected ICommonService getCommonService() {
		return BudgetHelper.getCommonService();
	}

	/**
	 * Signatures valid params
	 */
	private ConfigParam sfSignatLicence = new ConfigParam("fsignature_sf");
	private ConfigParam dpSignatLicence = new ConfigParam("fsignature_dp");
	private ConfigParam drSignatLicence = new ConfigParam("fsignature_dr");
	private ConfigParam dvSignatLicence = new ConfigParam("fsignature_dv");
	private ConfigParam daSignatLicence = new ConfigParam("fsignature_da");
	private ConfigParam ejSignatLicence = new ConfigParam("fsignature_ej");
	private ConfigParam arSignatLicence = new ConfigParam("fsignature_ar");
	private ConfigParam asfSignatLicence = new ConfigParam("fsignature_asf");
	private ConfigParam vsfSignatLicence = new ConfigParam("fsignature_vsf");
	private ConfigParam liqSignatLicence = new ConfigParam("fsignature_liq");
	private ConfigParam orSignatLicence = new ConfigParam("fsignature_or");
	private ConfigParam opSignatLicence = new ConfigParam("fsignature_op");
	private ConfigParam lrecSignatLicence = new ConfigParam("fsignature_lrec");
	private ConfigParam mdtSignatLicence = new ConfigParam("fSignature_mdt");
	private ConfigParam trSignatLicence = new ConfigParam("fSignature_tr");

	/**
	 * Signature required Params
	 */
	private ConfigParam daSignatRequised = new ConfigParam("fSignature_da_requise");
	private ConfigParam ejSignatRequised = new ConfigParam("fSignature_ej_requise");
	private ConfigParam liqSignatRequised = new ConfigParam("fSignature_liq_requise");
	private ConfigParam asfSignatRequised = new ConfigParam("fSignature_asf_requise");
	private ConfigParam vsfSignatRequised = new ConfigParam("fSignature_vsf_requise");
	private ConfigParam orSignatRequised = new ConfigParam("fSignature_or_requise");
	private ConfigParam opSignatRequised = new ConfigParam("fSignature_op_requise");
	private ConfigParam lrecSignatRequised = new ConfigParam("fSignature_lrec_requise");
	private ConfigParam arSignatRequised = new ConfigParam("fSignature_ar_requise");
	private ConfigParam mdtSignatRequised = new ConfigParam("fSignature_mdt_requise");
	private ConfigParam trSignatRequised = new ConfigParam("fSignature_tr_requise");

	public void executeAddOrUpdate() {
		logger.debug("executeAddOrUpdate: start");
		Signature sign = getSelectedSignature();
		try {
			if ((isUpdateMode() && sign.isUpdated()) || !isUpdateMode()) {
				sign.setTrace(Helper.createTrace());
				getCommonService().saveSignature(sign);
				notifyByEmail();
				HandlerJSFMessage.addInfo(MsgEntry.SUCCES);
			}
			if (!isUpdateMode()) {
				getSignatureList().add(sign);
				logger.debug("executeAddOrUpdate: élément added to list");
			}

		} catch (Exception e) {
			HandlerJSFMessage.addErrorMessage(Util.getErrorMessageFromException(MsgEntry.FAILED, e));
		}
		logger.debug("executeAddOrUpdate: end");
	}

	private void notifyByEmail() throws MissingConfiguration {
		logger.debug("notifyByEmail: start");
		if (AppCfgConfig.getInstance().isMailInfoDisabled())
			return;
		EmailSender sender = new EmailSenderImpl();
		String subject = HandlerJSFMessage.getMessage(MsgEntry.SIGNATURE_MAIL_SUBJECT);
		String message = getEmailMessage();
		String from = AppCfgConfig.getInstance().getMailPropertiesFrom();
		String to = AppCfgConfig.getInstance().getSignatureEmailTo();

		Thread sendEmailsThread = new Thread(() -> {

			sender.sendSignatureEmail(subject, message, from, to);
		});
		sendEmailsThread.setName(String.format("sendSignatureNotification-%s", sendEmailsThread.getName()));
		sendEmailsThread.start();
		logger.debug("notifyByEmail: end");

	}

	private String getEmailMessage() {
		Signature sign = getSelectedSignature();

		String messageTemplate = HandlerJSFMessage.getMessage(MsgEntry.SIGNATURE_MAIL_MESSAGE);
		User currentUser = getUserByLogin(Helper.getUserName());
		User selectUser = getUserByLogin(sign.getCodeUtil());
		String currentUserStr = "";
		String selectedUserStr = "";
		if (currentUser != null) {
			currentUserStr = String.format("%s (%s)", currentUser.getLastName(), currentUser.getLogin());
		}
		if (selectUser != null) {
			selectedUserStr = String.format("%s (%s)", selectUser.getLastName(), selectUser.getLogin());
		}
		return HandlerJSFMessage.formatMessage(messageTemplate, new String[] {
				Helper.toSimpleFormatDateHours(sign.getTrace().getDateMaj()), currentUserStr, selectedUserStr });
	}

	@Override
	public boolean isCommonRequiredDone() {

		return getSelectedUser() != null;
	}

	public List<Signature> getSignatureList() {
		if (signatureList == null) {
			if (isRequirmentsStatus()) {
				this.signatureList = getCommonService().getSignatureList(null);
				logger.debug("getSignatureList: size={}", signatureList != null ? signatureList.size() : 0);
			}
		}

		return signatureList;
	}

	public void setSignatureList(List<Signature> signatureList) {
		this.signatureList = signatureList;
	}

	public List<User> autoCompleteUser(String prefix) {
		logger.debug("autoComplete: {}", prefix);
		List<User> result = null;
		Predicate<? super User> isUserMatch = x -> {
			if (x.getLogin().toLowerCase().contains(prefix.toLowerCase()))
				return true;
			if (x.getLastName().toLowerCase().contains(prefix.toLowerCase()))
				return true;
			return false;
		};
		result = getAllUsers().stream().filter(isUserMatch).collect(Collectors.toList());
		logger.debug("autoComplete: size={}", result.size());
		return result;

	}

	public List<User> getAllUsers() {
		if (allUsers == null) {
			logger.debug("getAllUsers: start");
			this.allUsers = BudgetHelper.getutilCommonService().getUserList(null, null);
			logger.debug("getAllUsers: allUsers={}",
					allUsers.stream().map(x -> x.getLogin()).collect(Collectors.toList()));
		}
		return allUsers;
	}

	public void setAllUsers(List<User> allUsers) {
		this.allUsers = allUsers;
	}

	public User getSelectedUser() {
		return selectedUser;
	}

	public void setSelectedUser(User selectedUser) {
		this.selectedUser = selectedUser;
	}

	@Override
	protected void prepare() {

	}

	@Override
	public void reset() {
		setSignatureList(null);
		resetView();

	}

	private void resetView() {
		setSelectedUser(null);
		setSelectedSignature(null);
		setImportFileUploadEvent(null);
	}

	@Override
	protected Logger getLogger() {
		return logger;
	}

	public void onUserSelected() {
		logger.info("onUserSelected:getSelectedUser ", getSelectedUser().getLogin());
		if (!checkExistingActiveSignature(getSelectedUser().getLogin())) {
			getSelectedSignature().setCodeUtil(getSelectedUser().getLogin());
		}

		// getSignatureList();

		logger.info("onUserSelected:getSelectedSignature ", getSelectedSignature());
	}

	private boolean checkExistingActiveSignature(String login) {

		long count = getSignatureList().stream().filter(x -> x.getCodeUtil().equalsIgnoreCase(login))
				.filter(x -> x.getActif()).count();
		if (count > 0) {
			logger.error("checkExistingActiveSignature: true");
			HandlerJSFMessage.addError(MsgEntry.SIGNATURE_DUPLICAT);
			return true;
		}
		return false;
	}

	public void onItemChange() {

	}

	public Signature getSelectedSignature() {
		return selectedSignature;
	}

	public void setSelectedSignature(Signature selectedSignature) {
		this.selectedSignature = selectedSignature;
	}

	public StreamedContent getSignatStream() {
		logger.debug("getSignatStream: {}", getSelectedSignature());
		StreamedContent stream = null;
		try {
			stream = Util.getPdfStream(decryptData(getSelectedSignature().getSignatureData()), getSourceFileName(),
					Constant.PNG_CONTENT_TYPE);
		} catch (Exception e) {
			e.printStackTrace();
		}
		logger.debug("getSignatStream: end");
		return stream;

	}

	public boolean isUpdateMode() {
		return updateMode;
	}

	public void setUpdateMode(boolean updateMode) {
		this.updateMode = updateMode;
	}

	public void goToAddSignature() {
		resetView();
		Signature sign = new Signature();
		setSelectedSignature(sign);
		setUpdateMode(false);
		DialogHelper.openSignatureDialog();

	}

	public void fileUploadHandler(FileUploadEvent event) {
		logger.debug("fileUploadHandler: file name={}", event.getFile().getFileName());
		try {

			if (checkSignature(event.getFile().getInputstream())) {
				getSelectedSignature().setSignatureData(encryptData(event.getFile().getContents()));
				setImportFileUploadEvent(event);
			}

		} catch (IOException e) {
			HandlerJSFMessage.addErrorMessage(Util.getErrorMessageFromException(MsgEntry.FAILED, e));
		}

	}

	private byte[] encryptData(byte[] contents) {
		return FileHelper.encodeBase64(contents);
	}

	private byte[] decryptData(byte[] contents) {
		return FileHelper.decodeBase64(contents);
	}

	private boolean checkSignature(InputStream inputstream) throws IOException {
		BufferedImage bimg = null;
		bimg = ImageIO.read(inputstream);
		int width = bimg.getWidth();
		int height = bimg.getHeight();
		logger.debug("checkSignature: width={} ,height={} ", width, height);
		if ((width > Constant.IMG_SIGNATURE_WIDTH) || (height > Constant.IMG_SIGNATURE_HEIGHT)) {
			String msgPattern = HandlerJSFMessage.getErrorMessage(MsgEntry.SIGNATURE_ERROR);
			String errorMessage = HandlerJSFMessage.formatMessage(msgPattern,
					new Object[] { Constant.IMG_SIGNATURE_WIDTH, Constant.IMG_SIGNATURE_HEIGHT });
			HandlerJSFMessage.addErrorToComponent("signatureForm:fileUp", errorMessage);
			logger.debug("checkSignature: errorMessage={} ", errorMessage);
			return false;
		}
		logger.debug("checkSignature: OK");
		return true;

	}

	public String getSourceFileName() {
		if (getImportFileUploadEvent() != null)
			return getImportFileUploadEvent().getFile().getFileName();
		if (getSelectedSignature().getSignatureData() != null)
			return String.format("signature-%s.png", getSelectedSignature().getCodeUtil());
		return null;
	}

	/**
	 * fermer la fenettre dialog en cours
	 */
	public void closeCurrentDialog() {
		DialogHelper.closeDialog(null);
	}

	/**
	 * prération de la mise à jour d'une signature
	 */
	public void goToUpdateSignature() {
		setUpdateMode(true);
		setImportFileUploadEvent(null);
		getSelectedSignature().setUpdated(false);
		DialogHelper.openSignatureDialog();
	}

	public boolean isValidationAutorized() {
		if (!isRequirmentsStatus())
			return false;
		if (getSelectedSignature() == null)
			return false;
		if (StringUtils.isBlank(getSelectedSignature().getCodeUtil()))
			return false;
//			if((!isUpdateMode()) && getImportFileUploadEvent()==null)return false;
//			if(!isUpdateMode() && !isRequiredSignaturePJ()) return false;
		if (!isUpdateMode() && isRequiredSignaturePJ() && getImportFileUploadEvent() == null)
			return false;
		if (isUpdateMode() && isVisiblePj() && getImportFileUploadEvent() == null)
			return false;

		return true;
	}

	public User getUserByLogin(String login) {

		Optional<User> user = getAllUsers().stream().filter(x -> x.getLogin().equalsIgnoreCase(login)).findFirst();

		User result = user.isPresent() ? user.get() : null;
		logger.debug("getUserByLogin: user: {} -> {}", login, result.getLastName());
		return result;

	}

	@Override
	public void executeTask(String taskName) throws Exception {

		TaskEnum task = TaskEnum.valueOf(taskName);
		if (task == null)
			return;
		switch (task) {
		case MailPropertiesFrom:
			AppCfgConfig.getInstance().getMailPropertiesFrom();
			break;
		case SIGNATURE_LIST:
			getSignatureList();
			break;
		case SignatureEmailTo:
			AppCfgConfig.getInstance().getSignatureEmailTo();
			break;
		case MailSession:
			if (!AppCfgConfig.getInstance().isMailInfoDisabled())
				getEmailSender(null).getEmailSession();
			break;
		default:
			break;

		}
	}

	private enum TaskEnum {
		SIGNATURE_LIST, MailPropertiesFrom, SignatureEmailTo, MailSession
	}

	@Override
	protected String[] getTasks() {
		String[] tasks = new String[TaskEnum.values().length];
		for (TaskEnum task : TaskEnum.values()) {
			tasks[task.ordinal()] = task.name();
		}
		return tasks;
	}

	public String findUserName(String codeUtil) {
		return getUserByLogin(codeUtil).getLastName();

	}

	public boolean isAutorisedParam(String param) {
		String value = null;
		try {
			value = getCommonService().getConfigParam(param, null);
		} catch (MissingConfiguration e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		logger.info("isAutorisedParam: param : value {}", param, value);
		return value.equals("0") ? false : value.equals("1");

	}

	public void onSelectEj() {

		getSelectedSignature().setSeuilEj(BigDecimal.ZERO);

	}

	public void onSelectDa() {

		getSelectedSignature().setSeuilDa(BigDecimal.ZERO);

	}

	public void onSelectOp() {

		getSelectedSignature().setSeuilOp(BigDecimal.ZERO);

	}

	public void onSelectAr() {

		getSelectedSignature().setSeuilAr(BigDecimal.ZERO);

	}

	public void onSelectLiq() {

		getSelectedSignature().setSeuilLiq(BigDecimal.ZERO);

	}

	public void onSelectLrec() {

		getSelectedSignature().setSeuilLrec(BigDecimal.ZERO);

	}

	public void onSelectVsf() {

		getSelectedSignature().setSeuilVsf(BigDecimal.ZERO);

	}

	public void onSelectAsf() {

		getSelectedSignature().setSeuilAsf(BigDecimal.ZERO);

	}

	public void onSelectOr() {

		getSelectedSignature().setSeuilOr(BigDecimal.ZERO);

	}

	public void onSelectMdt() {

		getSelectedSignature().setSeuilMdt(BigDecimal.ZERO);

	}

	public void onSelectTr() {

		getSelectedSignature().setSeuilTr(BigDecimal.ZERO);

	}

	public boolean isOneActSelected() {

		if (getSelectedSignature().isDa())
			return true;
		if (getSelectedSignature().isEj())
			return true;
		if (getSelectedSignature().isAsf())
			return true;
		if (getSelectedSignature().isVsf())
			return true;
		if (getSelectedSignature().isOp())
			return true;
		if (getSelectedSignature().isAr())
			return true;
		if (getSelectedSignature().isLiq())
			return true;
		if (getSelectedSignature().isLrec())
			return true;
		if (getSelectedSignature().isLiqOr())
			return true;
		if (getSelectedSignature().isMdt())
			return true;
		if (getSelectedSignature().isTr())
			return true;
		return false;
	}

	private boolean checkRequiseSignature(Licence licence) {
		String configuredValue = "0";
		try {
			switch (licence) {
			case SIGNATURE_SF:
				configuredValue = getSfSignatLicence();
				break;
			case SIGNATURE_DP:
				configuredValue = getDpSignatLicence();
				break;
			case SIGNATURE_DR:
				configuredValue = getDrSignatLicence();
				break;
			case SIGNATURE_DV:
				configuredValue = getDvSignatLicence();
				break;
			case SIGNATURE_DA:
				configuredValue = getDaSignatLicence();
				break;
			case SIGNATURE_EJ:
				configuredValue = getEjSignatLicence();
				break;
			case SIGNATURE_ASF:
				configuredValue = getAsfSignatLicence();
				break;
			case SIGNATURE_VSF:
				configuredValue = getVsfSignatLicence();
				break;
			case SIGNATURE_LIQ:
				configuredValue = getLiqSignatLicence();
				break;
			case SIGNATURE_OR:
				configuredValue = getOrSignatLicence();
				break;
			case SIGNATURE_OP:
				configuredValue = getOpSignatLicence();
				break;
			case SIGNATURE_LREC:
				configuredValue = getLrecSignatLicence();
				break;
			case SIGNATURE_AR:
				configuredValue = getArSignatLicence();
				break;
			case SIGNATURE_MDT:
				configuredValue = getMdtSignatLicence();
				break;
			case SIGNATURE_TR:
				configuredValue = getTrSignatLicence();
				break;
			case SIGNATURE_DA_REQUISE:
				configuredValue = getDaSignatRequise();
				break;
			case SIGNATURE_EJ_REQUISE:
				configuredValue = getEjSignatRequise();
				break;
			case SIGNATURE_ASF_REQUISE:
				configuredValue = getAsfSignatRequise();
				break;
			case SIGNATURE_VSF_REQUISE:
				configuredValue = getVsfSignatRequise();
				break;
			case SIGNATURE_LIQ_REQUISE:
				configuredValue = getLiqSignatRequise();
				break;
			case SIGNATURE_OR_REQUISE:
				configuredValue = getOrSignatRequise();
				break;
			case SIGNATURE_OP_REQUISE:
				configuredValue = getOpSignatRequise();
				break;
			case SIGNATURE_LREC_REQUISE:
				configuredValue = getLrecSignatRequise();
				break;
			case SIGNATURE_AR_REQUISE:
				configuredValue = getArSignatRequise();
				break;
			case SIGNATURE_MDT_REQUISE:
				configuredValue = getMdtSignatRequise();
				break;
			case SIGNATURE_TR_REQUISE:
				configuredValue = getTrSignatRequise();
				break;

			default:
				break;
			}
		} catch (MissingConfiguration e) {
			// logger.error(e.getMessage());
		}

		return configuredValue == null ? false : configuredValue.equals("1");
	}

	public boolean isSfSignAuthorized() {
		return checkRequiseSignature(Licence.SIGNATURE_SF);
	}

	public boolean isDpSignAuthorized() {
		return checkRequiseSignature(Licence.SIGNATURE_DP);
	}

	public boolean isDrSignAuthorized() {
		return checkRequiseSignature(Licence.SIGNATURE_DR);
	}

	public boolean isDvSignAuthorized() {
		return checkRequiseSignature(Licence.SIGNATURE_DV);
	}

	public boolean isDaSignAuthorized() {
		return checkRequiseSignature(Licence.SIGNATURE_DA);
	}

	public boolean isEjSignAuthorized() {
		return checkRequiseSignature(Licence.SIGNATURE_EJ);
	}

	public boolean isAsfSignAuthorized() {
		return checkRequiseSignature(Licence.SIGNATURE_ASF);
	}

	public boolean isVsfSignAuthorized() {
		return checkRequiseSignature(Licence.SIGNATURE_VSF);
	}

	public boolean isLiqSignAuthorized() {
		return checkRequiseSignature(Licence.SIGNATURE_LIQ);
	}

	public boolean isOrSignAuthorized() {
		return checkRequiseSignature(Licence.SIGNATURE_OR);
	}

	public boolean isOpSignAuthorized() {
		return checkRequiseSignature(Licence.SIGNATURE_OP);
	}

	public boolean isLrecSignAuthorized() {
		return checkRequiseSignature(Licence.SIGNATURE_LREC);
	}

	public boolean isArSignAuthorized() {
		return checkRequiseSignature(Licence.SIGNATURE_AR);
	}

	public boolean isMdtSignAuthorized() {
		return checkRequiseSignature(Licence.SIGNATURE_MDT);
	}

	public boolean isTrSignAuthorized() {
		return checkRequiseSignature(Licence.SIGNATURE_TR);
	}

	public boolean isDaSignatureRequired() {
		return checkRequiseSignature(Licence.SIGNATURE_DA_REQUISE);
	}

	public boolean isEjSignatureRequired() {
		return checkRequiseSignature(Licence.SIGNATURE_EJ_REQUISE);
	}

	public boolean isAsfSignatureRequired() {
		return checkRequiseSignature(Licence.SIGNATURE_ASF_REQUISE);
	}

	public boolean isVsfSignatureRequired() {
		return checkRequiseSignature(Licence.SIGNATURE_VSF_REQUISE);
	}

	public boolean isLiqSignatureRequired() {
		return checkRequiseSignature(Licence.SIGNATURE_LIQ_REQUISE);
	}

	public boolean isOrSignatureRequired() {
		return checkRequiseSignature(Licence.SIGNATURE_OR_REQUISE);
	}

	public boolean isOpSignatureRequired() {
		return checkRequiseSignature(Licence.SIGNATURE_OP_REQUISE);
	}

	public boolean isLrecSignatureRequired() {
		return checkRequiseSignature(Licence.SIGNATURE_LREC_REQUISE);
	}

	public boolean isArSignatureRequired() {
		return checkRequiseSignature(Licence.SIGNATURE_AR_REQUISE);
	}

	public boolean isMdtSignatureRequired() {
		return checkRequiseSignature(Licence.SIGNATURE_MDT_REQUISE);
	}

	public boolean isTrSignatureRequired() {
		return checkRequiseSignature(Licence.SIGNATURE_TR_REQUISE);
	}

	public boolean isRequiredSignaturePJ() {
		
		if (isDaSignatureRequired() && (getSelectedSignature().isDa()))
			return true;
		if (isEjSignatureRequired() && (getSelectedSignature().isEj()))
			return true;
		if (isAsfSignatureRequired() && (getSelectedSignature().isAsf()))
			return true;
		if (isVsfSignatureRequired() && (getSelectedSignature().isVsf()))
			return true;
		if (isOpSignatureRequired() && (getSelectedSignature().isOp()))
			return true;
		if (isArSignatureRequired() && (getSelectedSignature().isAr()))
			return true;
		if (isLiqSignatureRequired() && (getSelectedSignature().isLiq()))
			return true;
		if (isLrecSignatureRequired() && (getSelectedSignature().isLrec()))
			return true;
		if (isOrSignatureRequired() && (getSelectedSignature().isLiqOr()))
			return true;
		if (isMdtSignatureRequired() && (getSelectedSignature().isMdt()))
			return true;
		if (isTrSignatureRequired() && (getSelectedSignature().isTr()))
			return true;
		return false;

	}

	public boolean isVisiblePj() {
		if (isUpdateMode() && isRequiredSignaturePJ() && getSourceFileName() == null)
			return true;
		if (!isUpdateMode() && getSelectedUser() != null && isRequiredSignaturePJ())
			return true;
		return false;

	}

	private String getDbConfigValue(ConfigParam param) throws MissingConfiguration {
		return getDbConfigValue(param, null);
	}

	private String getDbConfigValue(ConfigParam param, Integer exercise) throws MissingConfiguration {
		if ((!param.isLoaded()) || (!param.isSessionScoped())) {
			param.setValue(getCommonService().getConfigParam(param.getName(), exercise));
			if (!StringUtils.isBlank(param.getValue())) {
				param.setLoaded(true);
			}

			logger.debug(param.toString());
		}
		return param.getValue();
	}

	public String getSfSignatLicence() throws MissingConfiguration {
		return getDbConfigValue(sfSignatLicence);
	}

	public String getDpSignatLicence() throws MissingConfiguration {
		return getDbConfigValue(dpSignatLicence);
	}

	public String getDrSignatLicence() throws MissingConfiguration {
		return getDbConfigValue(drSignatLicence);
	}

	public String getDvSignatLicence() throws MissingConfiguration {
		return getDbConfigValue(dvSignatLicence);
	}

	public String getDaSignatLicence() throws MissingConfiguration {
		return getDbConfigValue(daSignatLicence);
	}

	public String getEjSignatLicence() throws MissingConfiguration {
		return getDbConfigValue(ejSignatLicence);
	}

	public String getArSignatLicence() throws MissingConfiguration {
		return getDbConfigValue(arSignatLicence);
	}

	public String getAsfSignatLicence() throws MissingConfiguration {
		return getDbConfigValue(asfSignatLicence);
	}

	public String getVsfSignatLicence() throws MissingConfiguration {
		return getDbConfigValue(vsfSignatLicence);
	}

	public String getLiqSignatLicence() throws MissingConfiguration {
		return getDbConfigValue(liqSignatLicence);
	}

	public String getOrSignatLicence() throws MissingConfiguration {
		return getDbConfigValue(orSignatLicence);
	}

	public String getOpSignatLicence() throws MissingConfiguration {
		return getDbConfigValue(opSignatLicence);
	}

	public String getLrecSignatLicence() throws MissingConfiguration {
		return getDbConfigValue(lrecSignatLicence);
	}

	public String getMdtSignatLicence() throws MissingConfiguration {
		return getDbConfigValue(mdtSignatLicence);
	}

	public String getTrSignatLicence() throws MissingConfiguration {
		return getDbConfigValue(trSignatLicence);
	}

	public String getMdtSignatRequise() throws MissingConfiguration {
		return getDbConfigValue(mdtSignatRequised);
	}

	public String getTrSignatRequise() throws MissingConfiguration {
		return getDbConfigValue(trSignatRequised);
	}

	public String getDaSignatRequise() throws MissingConfiguration {
		return getDbConfigValue(daSignatRequised);
	}

	public String getEjSignatRequise() throws MissingConfiguration {
		return getDbConfigValue(ejSignatRequised);
	}

	public String getLiqSignatRequise() throws MissingConfiguration {
		return getDbConfigValue(liqSignatRequised);
	}

	public String getAsfSignatRequise() throws MissingConfiguration {
		return getDbConfigValue(asfSignatRequised);
	}

	public String getVsfSignatRequise() throws MissingConfiguration {
		return getDbConfigValue(vsfSignatRequised);
	}

	public String getOrSignatRequise() throws MissingConfiguration {
		return getDbConfigValue(orSignatRequised);
	}

	public String getOpSignatRequise() throws MissingConfiguration {
		return getDbConfigValue(opSignatRequised);
	}

	public String getLrecSignatRequise() throws MissingConfiguration {
		return getDbConfigValue(lrecSignatRequised);
	}

	public String getArSignatRequise() throws MissingConfiguration {
		return getDbConfigValue(arSignatRequised);
	}
}
