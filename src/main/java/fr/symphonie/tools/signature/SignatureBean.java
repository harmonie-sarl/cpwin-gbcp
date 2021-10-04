package fr.symphonie.tools.signature;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
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

import fr.symphonie.budget.ui.beans.pluri.DialogHelper;
import fr.symphonie.common.model.AppCfgConfig;
import fr.symphonie.common.util.BudgetHelper;
import fr.symphonie.common.util.Constant;
import fr.symphonie.common.util.EmailSender;
import fr.symphonie.common.util.EmailSenderImpl;
import fr.symphonie.common.util.MsgEntry;
import fr.symphonie.common.util.Util;
import fr.symphonie.exception.MissingConfiguration;
import fr.symphonie.tools.common.CommonToolsBean;
import fr.symphonie.tools.signature.model.Signature;
import fr.symphonie.util.FileHelper;
import fr.symphonie.util.HandlerJSFMessage;
import fr.symphonie.util.Helper;
import fr.symphonie.util.model.User;

public class SignatureBean extends CommonToolsBean implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -5637450983868244118L;

	Logger logger=LoggerFactory.getLogger("signature-tools");

	private List<Signature> signatureList;
	private List<User> allUsers;
	private User selectedUser;
	private Signature selectedSignature;
	private boolean updateMode;
	public void executeAddOrUpdate() {
		logger.debug("executeAddOrUpdate: start");
		Signature sign=getSelectedSignature();
		try {
			if((isUpdateMode()&& sign.isUpdated()) || !isUpdateMode()) 
			{
			sign.setTrace(Helper.createTrace());
			getCommonService().saveSignature(sign);
			notifyByEmail();
			HandlerJSFMessage.addInfo(MsgEntry.SUCCES);
			}
			if(!isUpdateMode()) 
			{
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
		if(AppCfgConfig.getInstance().isMailInfoDisabled()) return;
		EmailSender sender=new EmailSenderImpl();
		String subject= HandlerJSFMessage.getMessage(MsgEntry.SIGNATURE_MAIL_SUBJECT);
		String message=getEmailMessage();
		String from=AppCfgConfig.getInstance().getMailPropertiesFrom();
		String to=AppCfgConfig.getInstance().getSignatureEmailTo();
				
		Thread sendEmailsThread = new Thread(() -> {			
			
			sender.sendSignatureEmail(subject, message, from, to);
		});
		sendEmailsThread.setName(String.format("sendSignatureNotification-%s", sendEmailsThread.getName()));
		sendEmailsThread.start();
		logger.debug("notifyByEmail: end");
		
	}
	private String getEmailMessage() {
		Signature sign=getSelectedSignature();
		
		String messageTemplate = HandlerJSFMessage.getMessage(MsgEntry.SIGNATURE_MAIL_MESSAGE);
		User currentUser=getUserByLogin(Helper.getUserName());
		User selectUser=getUserByLogin(sign.getCodeUtil());
		String currentUserStr="";
		String selectedUserStr="";
		if(currentUser!=null) {
			currentUserStr=String.format("%s (%s)", currentUser.getLastName(),currentUser.getLogin());
		}
		if(selectUser!=null) {
			selectedUserStr=String.format("%s (%s)", selectUser.getLastName(),selectUser.getLogin());
		}
		return HandlerJSFMessage.formatMessage(messageTemplate,
				new String[] {Helper.toSimpleFormatDateHours(sign.getTrace().getDateMaj()),currentUserStr,selectedUserStr});
	}
	@Override
	public boolean isCommonRequiredDone() {
		
		return getSelectedUser()!=null;
	}


	public List<Signature> getSignatureList() {
		if(signatureList==null) {
			if(isRequirmentsStatus()) {
			this.signatureList=getCommonService().getSignatureList(null);
			logger.debug("getSignatureList: size={}",signatureList!=null?signatureList.size():0);
			}
		}
		
		return signatureList;
	}

	public void setSignatureList(List<Signature> signatureList) {
		this.signatureList = signatureList;
	}
	
	public List<User> autoCompleteUser(String prefix){
		logger.debug("autoComplete: {}",prefix);
		List<User> result=null;
		Predicate<? super User> isUserMatch= x->
		{
			if(x.getLogin().toLowerCase().contains(prefix.toLowerCase()) )return true;
			if(x.getLastName().toLowerCase().contains(prefix.toLowerCase()) )return true;
		return false;
		};
		result=getAllUsers().stream()
				.filter(isUserMatch)
				.collect(Collectors.toList());
		logger.debug("autoComplete: size={}",result.size());
		return result;
		
	}

	public List<User> getAllUsers() {
		if(allUsers==null) {
			logger.debug("getAllUsers: start");
			this.allUsers=BudgetHelper.getutilCommonService().getUserList(null, null);
			logger.debug("getAllUsers: allUsers={}",allUsers.stream().map(x->x.getLogin()).collect(Collectors.toList()));
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
	protected void prepare()  {		
		
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
		logger.info("onUserSelected:getSelectedUser ",getSelectedUser().getLogin());
		if(!checkExistingActiveSignature(getSelectedUser().getLogin())) {
			getSelectedSignature().setCodeUtil(getSelectedUser().getLogin());
		}
		
        
       // getSignatureList();
		
		logger.info("onUserSelected:getSelectedSignature ",getSelectedSignature() );
	}
	private boolean checkExistingActiveSignature(String login) {
		
		long count=getSignatureList().stream()
		.filter(x-> x.getCodeUtil().equalsIgnoreCase(login))
		.filter(x -> x.getActif())
		.count();
		if(count>0) {
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
		logger.debug("getSignatStream: {}",getSelectedSignature());
		StreamedContent stream=null;
		try
		{
		stream= Util.getPdfStream(decryptData(getSelectedSignature().getSignatureData()), getSourceFileName(), Constant.PNG_CONTENT_TYPE);
		}catch(Exception e) {
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
	
		public void goToAddSignature()
		{
			resetView();
			Signature sign=new Signature();
			setSelectedSignature(sign);	
			setUpdateMode(false);
			DialogHelper.openSignatureDialog();
			
		}
		public void fileUploadHandler(FileUploadEvent event) {
		logger.debug("fileUploadHandler: file name={}",event.getFile().getFileName());
		try {
			
			if(checkSignature(event.getFile().getInputstream())) {
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
			BufferedImage bimg=null;
				bimg = ImageIO.read(inputstream);
				int width          = bimg.getWidth();
				int height         = bimg.getHeight(); 
				logger.debug("checkSignature: width={} ,height={} ",width,height);
				if((width> Constant.IMG_SIGNATURE_WIDTH) || (height> Constant.IMG_SIGNATURE_HEIGHT)) {
					String msgPattern=HandlerJSFMessage.getErrorMessage(MsgEntry.SIGNATURE_ERROR);
					String errorMessage=HandlerJSFMessage.formatMessage(msgPattern, new Object[] {Constant.IMG_SIGNATURE_WIDTH,Constant.IMG_SIGNATURE_HEIGHT});
					HandlerJSFMessage.addErrorToComponent("signatureForm:fileUp", errorMessage);
					logger.debug("checkSignature: errorMessage={} ",errorMessage);
					return false;
				}
				logger.debug("checkSignature: OK");
			return true;
			
		}
		public String getSourceFileName() {
			if (getImportFileUploadEvent()!=null) return getImportFileUploadEvent().getFile().getFileName();
			if(getSelectedSignature().getSignatureData()!=null) return 
						String.format("signature-%s.png", getSelectedSignature().getCodeUtil())	;
			return null;
		}

		/**
		 * fermer la fenettre dialog en cours 
		 */
		public void closeCurrentDialog() {
			DialogHelper.closeDialog(null);		
		}
		/**
		 * prération de la mise à jour 
		 * d'une signature
		 */
		public void goToUpdateSignature()
		{
			setUpdateMode(true);
			setImportFileUploadEvent(null);
			getSelectedSignature().setUpdated(false);
			DialogHelper.openSignatureDialog();
		}
		public boolean isValidationAutorized() {
			if(!isRequirmentsStatus())return false;
			if(getSelectedSignature()==null)return false;
			if(StringUtils.isBlank(getSelectedSignature().getCodeUtil())) return false;
			if((!isUpdateMode()) && getImportFileUploadEvent()==null)return false;
			
			return true;
		}
		public User getUserByLogin(String login){
			
			Optional<User> user=getAllUsers().stream()
					.filter(x->x.getLogin().equalsIgnoreCase(login))
					.findFirst();
			
			User result=user.isPresent()?user.get():null;
			logger.debug("getUserByLogin: user: {} -> {}",login,result.getLastName());
			return result;
			
		}
		@Override
		public void executeTask(String taskName) throws Exception {
			
			TaskEnum task=TaskEnum.valueOf(taskName);
			if(task==null) return;
			switch(task) {
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
				if(!AppCfgConfig.getInstance().isMailInfoDisabled())
				getEmailSender(null).getEmailSession();
				break;
			default:
				break;
			
			}
		}
		
		private enum TaskEnum {
			SIGNATURE_LIST,
			MailPropertiesFrom,
			SignatureEmailTo,
			MailSession
		}

		@Override
		protected String[] getTasks() {
			String[] tasks=new String[TaskEnum.values().length];
			for(TaskEnum task:TaskEnum.values()) {
				tasks[task.ordinal()]=task.name();
			}
			return tasks;
		}	
		
	
}

