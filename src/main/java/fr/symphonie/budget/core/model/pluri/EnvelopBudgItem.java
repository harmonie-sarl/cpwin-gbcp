package fr.symphonie.budget.core.model.pluri;

import java.io.Serializable;

import fr.symphonie.util.model.SimpleEntity;

public class EnvelopBudgItem implements Cloneable, Serializable{
/**
	 * 
	 */
	private static final long serialVersionUID = 1967312399902902386L;
private int exercice;
private String codeBudget;
private SimpleEntity nature;
private SimpleEntity destination;





public EnvelopBudgItem() {
	super();
	this.nature= new SimpleEntity();
	this.destination= new SimpleEntity();
}


public EnvelopBudgItem(String nature, String destination) {
	super();
	this.nature = new SimpleEntity(nature, nature);
	this.destination = new SimpleEntity(destination, destination);;
}


/* (non-Javadoc)
 * @see java.lang.Object#hashCode()
 */
@Override
public int hashCode() {
	final int prime = 31;
	int result = 1;
	result = prime * result
			+ ((destination == null) ? 0 : destination.hashCode());
	result = prime * result + ((nature == null) ? 0 : nature.hashCode());
	return result;
}


/* (non-Javadoc)
 * @see java.lang.Object#equals(java.lang.Object)
 */
@Override
public boolean equals(Object obj) {
	if (this == obj)
		return true;
	if (obj == null)
		return false;
	if (getClass() != obj.getClass())
		return false;
	EnvelopBudgItem other = (EnvelopBudgItem) obj;
	if (destination == null) {
		if (other.destination != null)
			return false;
	} else if (!destination.equals(other.destination))
		return false;
	if (nature == null) {
		if (other.nature != null)
			return false;
	} else if (!nature.equals(other.nature))
		return false;
	return true;
}


public SimpleEntity getNature() {
	return nature;
}
public void setNature(SimpleEntity nature) {
	this.nature = nature;
}
public SimpleEntity getDestination() {
	return destination;
}
public void setDestination(SimpleEntity destination) {
	this.destination = destination;
}


@Override
public String toString() {
	return "EnvelopBudgItem [nature=" + nature + ", destination=" + destination
			+ "]";
}


public void setExercice(int exercice) {
	this.exercice = exercice;
}


public int getExercice() {
	return exercice;
}


public void setCodeBudget(String codeBudget) {
	this.codeBudget = codeBudget;
}


public String getCodeBudget() {
	return codeBudget;
}

	@Override
	protected Object clone() throws CloneNotSupportedException {
		EnvelopBudgItem envelop = (EnvelopBudgItem) super.clone();
		return envelop;
	}


	
	

}
