package fr.symphonie.budget.core.model.plt;

import fr.symphonie.cpwin.model.cpt.DebitCreditEnum;
import fr.symphonie.util.model.Trace;

public class LigneTresorerie {
	private Integer exercice;
	private Integer numero;
	private String typeOp; //"ENC/DEC"
	private String categorie;
	private String type;
	
	private Integer numeroParent;
	private Integer ordre;
	private String libelle;	
	private Trace trace;
	
	
	public LigneTresorerie() {
		super();
	}
	
	public LigneTresorerie(Integer exercice, Integer numero) {
		this();
		this.exercice = exercice;
		this.numero = numero;
	}

	public Integer getExercice() {
		return exercice;
	}
	public void setExercice(Integer exercice) {
		this.exercice = exercice;
	}
	public String getLibelle() {
		return libelle;
	}
	public void setLibelle(String designation) {
		this.libelle = designation;
	}

	public Integer getOrdre() {
		return this.ordre;
	}
	public void setOrdre(Integer ordre) {
		this.ordre = ordre;
	}
	public Boolean isGlobal() {
		if(getCategorie()==null)return false;
		return getCategorie()==GlobalEnum.Global;
	}
	public Boolean isBudgetaire() {
		if(getType()==null)return false;
		return getType().equals("B");
	}
	public Boolean isComptable() {
		if((getType()==null)||(getType().trim().isEmpty()))return false;
		return getType().trim().equals("C");
	}
	public Integer getNumero() {
		return numero;
	}
	public void setNumero(Integer numero) {
		this.numero = numero;
	}
	public Integer getNumeroParent() {
		return numeroParent;
	}
	public void setNumeroParent(Integer numeroParent) {
		this.numeroParent = numeroParent;
	}

	
	public GlobalEnum getCategorie() {
		return GlobalEnum.parse(categorie);
	}
	public void setCategorie(GlobalEnum categorie) {
		this.categorie = categorie.getValue();
	}
	public Trace getTrace() {
		return trace;
	}
	public void setTrace(Trace trace) {
		this.trace = trace;
	}
	public EncDecEnum getTypeOp() {
		return EncDecEnum.parse(typeOp);
	}
	public void setTypeOp(EncDecEnum typeOp) {
		this.typeOp = typeOp.getValue();
	}
	
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}	
	public boolean isSameSens(DebitCreditEnum sens) {
		boolean result=false;
		switch (getTypeOp()) {
		case Decaissement:
			result=(sens==DebitCreditEnum.DEBIT);
			break;
		case Encaissement:
			result=(sens==DebitCreditEnum.CREDIT);
			break;
		
		}
		return result;
	}

	public String getDesignation(){
		StringBuilder sb=new StringBuilder();
		String SEPARATOR="-";
		sb.append(getNumero());
		sb.append(SEPARATOR);		
		sb.append(getLibelle());
		sb.append(SEPARATOR);		
		sb.append(getTypeOp().getValue());
		
		return sb.toString();
	}
	public String getExcelPosition(){
		LT_Excel_Params param=LT_Excel_Params.parse(getNumero());
		return (param!=null)?param.getPosition():"";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((exercice == null) ? 0 : exercice.hashCode());
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
		LigneTresorerie other = (LigneTresorerie) obj;
		if (exercice == null) {
			if (other.exercice != null)
				return false;
		} else if (!exercice.equals(other.exercice))
			return false;
		if (numero == null) {
			if (other.numero != null)
				return false;
		} else if (!numero.equals(other.numero))
			return false;
		return true;
	}
	
}
