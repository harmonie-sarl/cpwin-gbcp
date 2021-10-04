package fr.symphonie.common.util;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.Address;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.util.ByteArrayDataSource;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.symphonie.common.core.ICommonService;
import fr.symphonie.cpwin.model.nantes.EmailStatus;
import fr.symphonie.tools.nantes.model.Etudiant;
import fr.symphonie.util.FileHelper;
import fr.symphonie.util.HandlerJSFMessage;
import fr.symphonie.util.Helper;


public class EmailSenderImpl implements EmailSender {
	/**
	 * Logger for this class
	 */
	
	private static final Logger logger = LoggerFactory.getLogger(EmailSenderImpl.class);
	private static final String Content_ID="nantes-ensa-logo-%s";
	private String contextPath;
	private DataHandler attachmentDataHandler;
	private ICommonService commonService;
	private Session emailSession;
	
	public EmailSenderImpl(String contextPath,ICommonService commonService) {
		this();
		this.contextPath=contextPath;
		this.commonService=commonService;
	}
	
	public EmailSenderImpl() {
		super();
		this.emailSession=null;
	}
	public void sendSignatureEmail(String subject, String messageBody, String from, String to) {
		logger.debug("sendSignatureEmail:  subject={}, message={}", subject,messageBody);
		logger.debug("sendSignatureEmail:  from={}, to={}", from,to);
		try {
			
			final Session session = getEmailSession();
			Transport transport = session.getTransport("smtp");
			transport.connect();
			
			InternetAddress address = new InternetAddress(to);
			Message message = createSimpleHtmlMessage(session, subject, messageBody);
			message.setFrom(new InternetAddress(from));
			message.addRecipient(Message.RecipientType.TO,address);
			message.saveChanges();
			transport.sendMessage(message, new Address[] { address });
		} catch (Exception e) {
			logger.error("sendSignatureEmail : Error {}", e.getMessage());
		}
		
		logger.debug("sendSignatureEmail:  end");
	}

	@Override
	public void sendInformationMail(List<Etudiant> etudiantsCopy, String subject, String messageTemplate, String from, String site) {
		logger.debug("sendInformationMail:  subjectTemplate={}, messageTemplate={}", subject,
				messageTemplate);
		EmailStatus targetStatus=EmailStatus.ENVOYE;

		try {
			final Session session = getEmailSession();
			Transport transport = session.getTransport("smtp");
			transport.connect();
//			String from=session.getProperties().getProperty("mail.smtp.from");
//			String site=session.getProperties().getProperty("nantes.etudiant.site");
			logger.debug("sendInformationMail: site={}",site);
			etudiantsCopy.stream().forEach(x -> {
				try {
					logger.debug("sendInformationMail TO: {} {} :début", x.getNom(), x.getPrenom());
					InternetAddress address = new InternetAddress(x.getEmail());
					Message message = createHtmlMessage(session, subject, getInformationMessage(messageTemplate,site, x),getImageContentId(x.getMatricule()));
					message.setFrom(new InternetAddress(from));
					message.addRecipient(Message.RecipientType.TO,address);
					message.saveChanges();
					transport.sendMessage(message, new Address[] { address });
					x.setEmailStatus(targetStatus);
					updateEmailStatus(x, targetStatus);

					logger.debug("sendInformationMail TO: {} {} :fin", x.getNom(), x.getPrenom());

				} 
			catch (Exception e) {
					logger.error("sendInformationMail TO: {} {} : Error {}", x.getNom(), x.getPrenom(), e.getMessage());
				}
			});
			
			transport.close();
		} catch (Exception e) {
			logger.error("sendInformationMail: erreur globale: {}", e.getMessage());
			e.printStackTrace();
		}
		logger.debug("sendInformationMail: {} mail envoyé: ",etudiantsCopy.size());

	}
	@Override
	public void sendRelanceMail(List<Etudiant> etudiantsCopy, String subject, String messageTemplate, String from) {
		logger.debug("sendRelanceMail:  subjectTemplate={}, messageTemplate={}", subject,
				messageTemplate);
		EmailStatus targetStatus=EmailStatus.RELANCE;
		try {
			final Session session = getEmailSession();
			Transport transport = session.getTransport("smtp");
			transport.connect();
			//String from=session.getProperties().getProperty("mail.smtp.from");
			etudiantsCopy.stream().forEach(x -> {
				try {
					logger.debug("sendRelanceMail TO: {} {} :début", x.getNom(), x.getPrenom());
					InternetAddress address = new InternetAddress(x.getEmail());
					Message message = createHtmlMessage(session, subject, getRelanceMessage(messageTemplate, x),getImageContentId(x.getMatricule()));
					message.setFrom(new InternetAddress(from));
					message.addRecipient(Message.RecipientType.TO,address);
					message.saveChanges();
					transport.sendMessage(message, new Address[] { address });

					x.setEmailStatus(EmailStatus.RELANCE);
					updateEmailStatus(x, targetStatus);
					logger.debug("sendRelanceMail TO: {} {} :fin", x.getNom(), x.getPrenom());

				} catch (MessagingException | IOException e) {
					logger.error("sendRelanceMail TO: {} {} : Error {}", x.getNom(), x.getPrenom(), e.getMessage());
				}
			});
			
			transport.close();
		} catch (Exception  e) {
			logger.error("sendRelanceMail: erreur globale: {}", e.getMessage());
			e.printStackTrace();
		}
		logger.debug("sendRelanceMail: {} mail envoyé: ",etudiantsCopy.size());

	}
	@Override
	public void sendMissingBicMail(List<Etudiant> etudiantsCopy, String subject, String messageTemplate, String from, String to) {
		logger.debug("sendMissingBicMail:  subjectTemplate={}, messageTemplate={}", subject,
				messageTemplate);
		EmailStatus targetStatus=EmailStatus.BIC_MAIL_SEND;

		try {
			final Session session = getEmailSession();
			Transport transport = session.getTransport("smtp");
			transport.connect();
//			String from=session.getProperties().getProperty("mail.smtp.from");
//			String to=session.getProperties().getProperty("support.mail");
			
			etudiantsCopy.stream().forEach(x -> {
				try {
					logger.debug("sendMissingBicMail TO: {} {} :début", x.getNom(), x.getPrenom());
					InternetAddress address = new InternetAddress(to);
					Message message = createHtmlMessageWithPJ(x,session, subject, getMissingBicMessage(messageTemplate, x),getImageContentId(x.getMatricule()));
					message.setFrom(new InternetAddress(from));
					message.addRecipient(Message.RecipientType.TO,address);
					message.saveChanges();
					transport.sendMessage(message, new Address[] { address });
					x.setEmailStatus(targetStatus);
					updateEmailStatus(x, targetStatus);

					logger.debug("sendMissingBicMail TO: {} {} :fin", x.getNom(), x.getPrenom());

				} 
				//catch (MessagingException | IOException e) {
			catch (Exception e) {
					logger.error("sendMissingBicMail TO: {} {} : Error {}", x.getNom(), x.getPrenom(), e.getMessage());
				}
			});
			
			transport.close();
		} catch (Exception e) {
			logger.error("sendMissingBicMail: erreur globale: {}", e.getMessage());
			e.printStackTrace();
		}
		logger.debug("sendMissingBicMail: {} mail envoyé: ",etudiantsCopy.size());

	}
	private Message createSimpleHtmlMessage( Session session,
			String subject, String messageBody) throws MessagingException, IOException{
		logger.debug("createSimpleHtmlMessage: debut");
		Message message = new MimeMessage(session);
		message.setSubject(subject);		
		message.setContent(messageBody, Constant.HTML_CONTENT_TYPE);
		
		return message;
	}

	private Message createHtmlMessageWithPJ(Etudiant x, Session session,
			String subject, String messageBody, String imageContentID) throws MessagingException, IOException{
		logger.debug("createHtmlMessageWithPJ: debut");
		Message message = new MimeMessage(session);
		message.setSubject(subject);
		
		Multipart multipart=createRelatedMultipart(messageBody,imageContentID);	
		MimeBodyPart pjPart=createPjBodyPart(x);
		if(pjPart!=null) {
			logger.debug("createHtmlMessageWithPJ: pjPart not null {}",pjPart);
			multipart.addBodyPart(pjPart);
		}
		message.setContent(multipart);
		logger.debug("createHtmlMessageWithPJ: fin");
		return message;
	}

	private MimeBodyPart createPjBodyPart(Etudiant x) throws MessagingException, IOException {
		if(CollectionUtils.isEmpty(x.getPj()))return null;
		logger.debug("createPjBodyPart: start");
		MimeBodyPart attachPart = new MimeBodyPart();
		
		byte[] data = FileHelper.ungzip(FileHelper.decodeBase64(x.getPj().get(0).getFileData()));
		logger.debug("createPjBodyPart: data = {}",data);
		
		ByteArrayDataSource source = new ByteArrayDataSource(data,"text/pdf"); 
		source.setName(String.format(Constant.NANTES_ENSA_RIB_FileName, x.getMatricule()));
		attachPart.setDataHandler(new DataHandler(source));
		attachPart.setFileName(source.getName());
		attachPart.setHeader("Content-Type", Constant.PDF_CONTENT_TYPE);
		logger.debug("createPjBodyPart: end");
		return attachPart;
	}

	private String getMissingBicMessage(String messageTemplate, Etudiant etudiant) {
		return HandlerJSFMessage.formatMessage(messageTemplate, new String[] {etudiant.getMatricule()});
	}

	private String getImageContentId(String matricule) {
		return String.format(Content_ID, matricule);
	}
	private Message createHtmlMessage(Session session, String subject, String messageBody,String imageContentID) throws MessagingException, IOException {
		Message message = new MimeMessage(session);
		message.setSubject(subject);
		
		Multipart multipart=createRelatedMultipart(messageBody,imageContentID);		
		message.setContent(multipart);
		
		return message;
	}

	private String getImagePath() {
	
		Path path=Paths.get(contextPath).resolve("images");		
		String imagePath=path.toString()+File.separator+Constant.NANTES_ENSA_LOGO;

		return imagePath;
	}
	private Multipart createRelatedMultipart(String messageBody, String imageContentID) throws MessagingException, IOException {
		logger.debug("createRelatedMultipart: messageBody={},",messageBody);
		logger.debug("createRelatedMultipart: imageContentID={},",imageContentID);
		 // This mail has 2 part, the BODY and the embedded image
		Multipart multipart = new MimeMultipart();
		// first part (the html)
		MimeBodyPart messageBodyPart = new MimeBodyPart();
		messageBodyPart.setContent(messageBody, Constant.HTML_CONTENT_TYPE);		
		multipart.addBodyPart(messageBodyPart);
		// second part (the image)
		
			messageBodyPart = new MimeBodyPart();
			messageBodyPart.setDataHandler(getAttachmentDataHandler());
			messageBodyPart.setHeader("Content-ID", "<"+imageContentID+">");
			messageBodyPart.setDisposition(MimeBodyPart.INLINE);
			multipart.addBodyPart(messageBodyPart);	       

		return multipart;
	}

	private String getInformationMessage(String messageTemplate,String site, Etudiant etudiant) {
		return HandlerJSFMessage.formatMessage(messageTemplate, new String[] {site,etudiant.getPassword(),etudiant.getMatricule()});
	}
	private String getRelanceMessage(String messageTemplate, Etudiant etudiant) {
		return HandlerJSFMessage.formatMessage(messageTemplate, new String[] {Helper.toSimpleFormat2(etudiant.getEmailDate()),etudiant.getMatricule()});
	}

	public DataHandler getAttachmentDataHandler() {
		if(attachmentDataHandler==null) {
			DataSource fds=new FileDataSource(getImagePath());
			attachmentDataHandler=new DataHandler(fds);
		}
		return attachmentDataHandler;
	}
	private void updateEmailStatus(Etudiant etudiant, EmailStatus status) {
		this.commonService.updateEtudiantEmailStatus(etudiant,status);
		

	}
@Override
	public Session getEmailSession() throws NamingException {
		if(emailSession==null) {
			logger.debug("getEmailSession: start");
			Context initCtx = new InitialContext();
			Context envCtx = (Context) initCtx.lookup("java:comp/env");
			this.emailSession = (Session) envCtx.lookup("mail/budgSession");
			logger.debug("getEmailSession: end: emailSession -> {}",emailSession);
		}
		return emailSession;
	}

	public void setEmailSession(Session emailSession) {
		this.emailSession = emailSession;
	}
	
}
