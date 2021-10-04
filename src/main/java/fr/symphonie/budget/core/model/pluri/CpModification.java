package fr.symphonie.budget.core.model.pluri;

import java.util.HashSet;
import java.util.Set;

import fr.symphonie.util.model.Trace;

public class CpModification {
//	private int id;
	private Integer exercice;
	private String codeBudget;	
	private Integer numero;
	//private String type;	
	//private String groupNat;
	private String objet;
	private Trace trace;
	private Set<CpModificationItem> items;
	
	public CpModification() {
		super();
		this.items=new HashSet<CpModificationItem>();
	}
//	public int getId() {
//		return id;
//	}
//	public void setId(int id) {
//		this.id = id;
//	}
	public Integer getExercice() {
		return exercice;
	}
	public void setExercice(Integer exercice) {
		this.exercice = exercice;
	}
	public String getCodeBudget() {
		return codeBudget;
	}
	public void setCodeBudget(String codeBudget) {
		this.codeBudget = codeBudget;
	}
//	public String getGroupNat() {
//		return groupNat;
//	}
//	public void setGroupNat(String groupNat) {
//		this.groupNat = groupNat;
//	}
	public String getObjet() {
		return objet;
	}
	public void setObjet(String objet) {
		this.objet = objet;
	}
	public void setTrace(Trace trace) {
		this.trace = trace;
	}
	public Trace getTrace() {
		return trace;
	}
	public void setItems(Set<CpModificationItem> items) {
		this.items = items;
	}
	public Set<CpModificationItem> getItems() {
		return items;
	}
//	public ModifEnum getType() {
//		return ModifEnum.parse(type);
//	}
//	public void setType(ModifEnum type) {
//		this.type = type.getCode();
//	}
	public Integer getNumero() {
		return numero;
	}
	public void setNumero(Integer numero) {
		this.numero = numero;
	}
	@Override
	public String toString() {
		return "CpModification [exercice=" + exercice + ", codeBudget=" + codeBudget + ", numero=" + numero + ", objet="
				+ objet + ", trace=" + trace + "]";
	}
	
	
	
}
