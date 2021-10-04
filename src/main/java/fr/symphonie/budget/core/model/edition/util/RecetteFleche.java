package fr.symphonie.budget.core.model.edition.util;

public class RecetteFleche {
private DataItem anterieure;
private DataItem n;
private DataItem n1;
private DataItem n2;
private DataItem n3;

public RecetteFleche(DataRefEnum ref) {
	super();
	anterieure=new DataItem(ref,"ANT");
	n=new DataItem(ref,"N");
	n1=new DataItem(ref,"N1");
	n2=new DataItem(ref,"N2");
	n3=new DataItem(ref,"N3");
}
public DataItem getAnterieure() {
	return anterieure;
}
public void setAnterieure(DataItem anterieure) {
	this.anterieure = anterieure;
}
public DataItem getN() {
	return n;
}
public void setN(DataItem n) {
	this.n = n;
}
public DataItem getN1() {
	return n1;
}
public void setN1(DataItem n1) {
	this.n1 = n1;
}
public DataItem getN2() {
	return n2;
}
public void setN2(DataItem n2) {
	this.n2 = n2;
}
public DataItem getN3() {
	return n3;
}
public void setN3(DataItem n3) {
	this.n3 = n3;
}


}
