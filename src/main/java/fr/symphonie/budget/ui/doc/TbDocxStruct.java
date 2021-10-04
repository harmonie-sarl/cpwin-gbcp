package fr.symphonie.budget.ui.doc;

import java.util.Date;
import java.util.Set;

import fr.symphonie.util.model.Langue;
import fr.symphonie.util.model.i.Editionable;
import fr.symphonie.util.model.link.AttachedDocument;

public class TbDocxStruct implements Editionable{
private Integer exercice;
private String budget;
private Date date;
public Integer getExercice() {
	return exercice;
}
public void setExercice(Integer exercice) {
	this.exercice = exercice;
}
public String getBudget() {
	return budget;
}
public void setBudget(String budget) {
	this.budget = budget;
}
public Date getDate() {
	return date;
}
public void setDate(Date date) {
	this.date = date;
}
@Override
public int getId() {
	return 0;
}
@Override
public String getEntityName() {
	return null;
}
@Override
public Langue getLangueEdition() {
	return null;
}
@Override
public String getEditionPhase() {
	return null;
}
@Override
public String getCodeActivite() {
	return null;
}
@Override
public double getMontantAlloue() {
	return 0;
}
@Override
public Set<AttachedDocument> getDocuments() {
	return null;
}
@Override
public void setDocuments(Set<AttachedDocument> documents) {
	
}
@Override
public String getEmailDocDestination() {
	return null;
}
@Override
public String getEmailDocFrom() {
	return null;
}
@Override
public String getDocPath(String codeDocument, String ordreDocument) {
	return null;
}

}
