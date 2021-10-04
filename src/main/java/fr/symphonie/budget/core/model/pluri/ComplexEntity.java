package fr.symphonie.budget.core.model.pluri;

import fr.symphonie.util.model.SimpleEntity;

public class ComplexEntity  {
private SimpleEntity entity1= new SimpleEntity();
private SimpleEntity entity2= new SimpleEntity();

public ComplexEntity() {
	super();
}

public ComplexEntity(String code1, String code2) {
	super();
	this.setEntity1(new SimpleEntity(code1, ""));
	this.setEntity2(new SimpleEntity(code2, ""));
}

public ComplexEntity(String code1,String lib1, String code2,String lib2) {
	super();
	this.setEntity1(new SimpleEntity(code1, lib1));
	this.setEntity2(new SimpleEntity(code2, lib2));
}
public SimpleEntity getEntity1() {
	return entity1;
}
public void setEntity1(SimpleEntity entity1) {
	this.entity1 = entity1;
}
public SimpleEntity getEntity2() {
	return entity2;
}
public void setEntity2(SimpleEntity entity2) {
	this.entity2 = entity2;
}

@Override
public int hashCode() {
	final int prime = 31;
	int result = 1;
	result = prime * result + ((entity1 == null) ? 0 : entity1.hashCode());
	result = prime * result + ((entity2 == null) ? 0 : entity2.hashCode());
	return result;
}

@Override
public boolean equals(Object obj) {
	if (this == obj)
		return true;
	if (obj == null)
		return false;
	if (!(obj instanceof ComplexEntity))
		return false;
	ComplexEntity other = (ComplexEntity) obj;
	if (entity1 == null) {
		if (other.entity1 != null)
			return false;
	} else if (!entity1.equals(other.entity1))
		return false;
	if (entity2 == null) {
		if (other.entity2 != null)
			return false;
	} else if (!entity2.equals(other.entity2))
		return false;
	return true;
}

@Override
public String toString() {
	return "ComplexEntity [entity1=" + entity1 + ", entity2=" + entity2 + "]";
}



}
