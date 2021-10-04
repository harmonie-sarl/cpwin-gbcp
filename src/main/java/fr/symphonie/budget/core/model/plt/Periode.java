package fr.symphonie.budget.core.model.plt;

public class Periode {
private Integer numero;
private String etat;
private String libelle;

public Periode(Integer numero, String etat) {
	super();
	this.numero = numero;
	this.etat = etat;
}
public Integer getNumero() {
	return numero;
}
public void setNumero(Integer numero) {
	this.numero = numero;
}
public EtatPeriodeEnum getEtat() {
	return EtatPeriodeEnum.parse(etat);
}
public void setEtat(String etat) {
	this.etat = etat;
}
public String getLibelle() {
	return libelle;
}
public void setLibelle(String libelle) {
	this.libelle = libelle;
}
@Override
public int hashCode() {
	final int prime = 31;
	int result = 1;
	result = prime * result + ((numero == null) ? 0 : numero.hashCode());
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
	Periode other = (Periode) obj;
	if (numero == null) {
		if (other.numero != null)
			return false;
	} else if (!numero.equals(other.numero))
		return false;
	return true;
}
public String getDesignation(){
	String SEPARATOR=" - ";
	StringBuilder b=new StringBuilder();
	PeriodeEnum p=PeriodeEnum.parse(getNumero());
	b.append(p.getLibelle());
	b.append(" ( ");
	b.append(getEtat().getLibelle());
	b.append(" )");
	String commentaire=getEtat().getCommentaire();
	if(commentaire!=null){
		if(p==PeriodeEnum.Annuel_def)commentaire=p.getCommentaire();
		b.append(SEPARATOR);
		b.append(commentaire);
	}	
	return b.toString();
}


}
