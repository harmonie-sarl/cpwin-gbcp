package fr.symphonie.tools.nantes.model;

import java.util.Date;
import java.util.List;

import javax.persistence.Transient;

import org.apache.commons.lang.StringUtils;

import fr.symphonie.common.util.BudgetHelper;
import fr.symphonie.common.util.SepaHelper;
import fr.symphonie.cpwin.model.nantes.Adresse;
import fr.symphonie.cpwin.model.nantes.EmailStatus;
import fr.symphonie.cpwin.model.nantes.EtudiantPj;
import fr.symphonie.cpwin.model.nantes.EtudiantStatus;
import fr.symphonie.cpwin.model.nantes.Iban;
import fr.symphonie.impor.AttributImport;
import fr.symphonie.impor.ConfigImport;
import fr.symphonie.impor.Importable;
import fr.symphonie.util.model.Trace;

public class Etudiant implements Importable,Cloneable,Comparable<Etudiant>  {
	private String matricule;
	private String password;
	private String codeCpwin;
	private String nom;
	private String prenom; 
	private String email;
	private Adresse adresse;
	private Iban iban;
	private String status;
	private String emailStatus;
	private Date emailDate;
	private Trace trace;
	@Transient
	private List<EtudiantPj> pj;
	@Transient
	private String observation;
	@Transient
	private boolean updateable;
	
	public String getMatricule() {
		return matricule;
	}
	public void setMatricule(String matricule) {
		this.matricule = matricule;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getCodeCpwin() {
		return codeCpwin;
	}
	public void setCodeCpwin(String codeCpwin) {
		this.codeCpwin = codeCpwin;
	}
	public String getNom() {
		return nom;
	}
	public void setNom(String nom) {
		this.nom = nom;
	}
	public String getPrenom() {
		return prenom;
	}
	public void setPrenom(String prenom) {
		this.prenom = prenom;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public Adresse getAdresse() {
		return adresse;
	}
	public void setAdresse(Adresse adresse) {
		this.adresse = adresse;
	}
	public Iban getIban() {
		return iban;
	}
	public void setIban(Iban iban) {
		this.iban = iban;
	}
	public EtudiantStatus getStatus() {
		return EtudiantStatus.valueOf(status);
	}
	public void setStatus(EtudiantStatus status) {
		this.status = status.name();
	}
	
	public Trace getTrace() {
		return trace;
	}
	public void setTrace(Trace trace) {
		this.trace = trace;
	}
	
	public EmailStatus getEmailStatus() {
		return EmailStatus.valueOf(emailStatus);
	}
	public void setEmailStatus(EmailStatus emailStatus) {
		this.emailStatus = emailStatus.name();
	}
	public Date getEmailDate() {
		return emailDate;
	}
	public void setEmailDate(Date emailDate) {
		this.emailDate = emailDate;
	}
	@Override
	public void setValue(AttributImport attribut, Object value) {
		String strValue=null;
		
		if(value instanceof String)
			strValue=(value!=null)?((String) value).trim():null;
		
		String attributName=attribut.getName();
		switch (attributName){

		case "NOM":			 
			setNom(strValue);
			break;
		case "PRENOM":
			setPrenom(strValue);
			break;
		case "EMAIL":
			setEmail(strValue);
			break;
		case "MATRICULE":
			setMatricule(strValue);
			break;

		}
		
	}
	@Override
	public ConfigImport getConfigImport() {
		if(configImport==null) configImport=BudgetHelper.loadConfigImport("nantes.etudiant.import.config");
		return configImport;
	}
	
	@Override
	public void setRowNum(Integer rowNum) {
		this.rowNum=rowNum;
		
	}
	@Override
	public Integer getRowNum() {
		return getConfigImport().getStartedRowIndex()+rowNum;
	}
	@Transient
	private Integer rowNum;
	private static ConfigImport configImport=null;

	@Override
	public Etudiant clone() {
		Etudiant copy=null;
		try {
			copy = (Etudiant) super.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		return copy;
	}
	
	@Override
	public String toString() {
		return "Etudiant [matricule=" + matricule + ", nom=" + nom + ", prenom=" + prenom + ", email=" + email + ", status="+status+"]";
	}
	public String getNomEtPrenom() {
		return String.format("%s %s", StringUtils.isBlank(getNom())?"":getNom(),StringUtils.isBlank(getPrenom())?"":getPrenom()).trim();
	}
	public String getPaymentWay() {
		
		return SepaHelper.isFrenchIban(getIban().getIban())?"SF":"SI";
	}
	public List<EtudiantPj> getPj() {
		return pj;
	}
	public void setPj(List<EtudiantPj> pj) {
		this.pj = pj;
	}
public String getDescription(){
		
		String SEPARATOR="-";
		StringBuffer description=new StringBuffer();	
		description.append((this.codeCpwin==null)?"":this.codeCpwin);
		description.append((this.getNomEtPrenom()==null)?"":SEPARATOR+this.getNomEtPrenom());		
		String returnString = description.toString();
		return returnString;
	}
public String getObservation() {
	return observation;
}
public void setObservation(String observation) {
	this.observation = observation;
}
public boolean isValid() {
	if( (getStatus()==EtudiantStatus.NON_RENSEIGNE) && (getEmailStatus()==EmailStatus.NON)) return false;
	return true;
}
public boolean isUpdateable() {
	return updateable;
}
public void setUpdateable(boolean updateable) {
	this.updateable = updateable;
}
@Override
public int hashCode() {
	final int prime = 31;
	int result = 1;
	result = prime * result + ((matricule == null) ? 0 : matricule.hashCode());
	return result;
}
@Override
public boolean equals(Object obj) {
	if (this == obj)
		return true;
	if (obj == null)
		return false;
	if (getClass() != obj.getClass())
		return false;
	Etudiant other = (Etudiant) obj;
	if (matricule == null) {
		if (other.matricule != null)
			return false;
	} else if (!matricule.equals(other.matricule))
		return false;
	return true;
}
@Override
public int compareTo(Etudiant o) {
	return this.getMatricule().compareTo(o.getMatricule());
}
	
}
