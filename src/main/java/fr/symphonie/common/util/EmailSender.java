package fr.symphonie.common.util;

import java.util.List;

import javax.mail.Session;
import javax.naming.NamingException;

import fr.symphonie.tools.nantes.model.Etudiant;


public interface EmailSender {
	void sendSignatureEmail(String subject, String messageBody, String from, String to) ;

	void sendInformationMail(List<Etudiant> etudiants, String subjectTemplate, String messageTemplate, String from, String site);

	void sendRelanceMail(List<Etudiant> etudiants, String subjectTemplate, String messageTemplate, String from);

	void sendMissingBicMail(List<Etudiant> etudiants, String subjectTemplate, String messageTemplate, String from, String to);

	Session getEmailSession() throws NamingException;

}
