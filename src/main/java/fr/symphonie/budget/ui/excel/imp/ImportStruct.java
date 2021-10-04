package fr.symphonie.budget.ui.excel.imp;

import fr.symphonie.budget.core.model.pluri.EnvelopBudg;
import fr.symphonie.budget.core.model.pluri.LigneBudgetaireAE;
import fr.symphonie.excel.IAttribut;
import fr.symphonie.excel.IImportedStruct;
import fr.symphonie.excel.ImportEngine;
import fr.symphonie.util.Helper;




public class ImportStruct implements IImportedStruct {
	private int rowNum;
	
	private String groupDest;
	private String groupNat;
	private String destination;
	private String nature;
	private String programme;
	private String service;
	private Double montantAE;
	public String getGroupDest() {
		return groupDest;
	}
	public void setGroupDest(String groupDest) {
		this.groupDest = groupDest;
	}
	public String getGroupNat() {
		return groupNat;
	}
	public void setGroupNat(String groupNat) {
		this.groupNat = groupNat;
	}
	public String getDestination() {
		return destination;
	}
	public void setDestination(String destination) {
		this.destination = destination;
	}
	public String getNature() {
		return nature;
	}
	public void setNature(String nature) {
		this.nature = nature;
	}
	public String getProgramme() {
		return programme;
	}
	public void setProgramme(String programme) {
		this.programme = programme;
	}
	public String getService() {
		return service;
	}
	public void setService(String service) {
		this.service = service;
	}
	public Double getMontantAE() {
		return montantAE;
	}
	public void setMontantAE(Double montantAE) {
		this.montantAE = montantAE;
	}
	public EnvelopBudg createEnvelopBudg() {
		EnvelopBudg envelop=new EnvelopBudg();
		 envelop.setGroupDest(getGroupDest());envelop.setGroupNat(getGroupNat());
		return envelop;
	}
	public LigneBudgetaireAE createLigneBudgetaire() {
		LigneBudgetaireAE lb = new LigneBudgetaireAE();
		lb.setTrace(Helper.createTrace());
		lb.setGroupDest(getGroupDest());
		lb.setGroupNat(getGroupNat());
		lb.setDestination(getDestination());
		lb.setNature(getNature());
		lb.setProgramme(getProgramme());
		lb.setService(getService());
		lb.setMontantAE(getMontantAE());
		return lb;
	}
	@Override
	public void setValue(IAttribut attribut, Object value) {
		ImportAttributsEnum metadata=ImportAttributsEnum.parse(attribut.getPosition());
		switch(metadata){
		case GROUP_DEST:
			setGroupDest(ImportEngine.convertToString(value));
		break;
		case GROUP_NAT:
			this.setGroupNat(ImportEngine.convertToString(value));
		break;
		case DEST:
			this.setDestination(ImportEngine.convertToString(value));
		break;
		case NAT:
			this.setNature(ImportEngine.convertToString(value));
		break;
		case CODE_PROG:
			this.setProgramme(ImportEngine.convertToString(value));
		break;
		case CODE_SERV:
			this.setService(ImportEngine.convertToString(value));
		break;
		case MONTANT:
			this.setMontantAE(ImportEngine.toDouble(value));
		break;
		}
		
	}
	@Override
	public Object getValue(IAttribut attribut) {
		ImportAttributsEnum metadata=ImportAttributsEnum.parse(attribut.getPosition());
		Object value=null;
		switch(metadata){
		case GROUP_DEST:
			value= getGroupDest();
		break;
		case GROUP_NAT:
			value=  getGroupNat();
		break;
		case DEST:
			value=  getDestination();
		break;
		case NAT:
			value= getNature();
		break;
		case CODE_PROG:
			value=  getProgramme();
		break;
		case CODE_SERV:
			value=  getService();
		break;
		case MONTANT:
			value=  getMontantAE();
		break;
		}
		return value;
	}
	@Override
	public boolean isValid(IAttribut attr) {
//		Object value=null;
		boolean retour=true;
//		ImportAttributsEnum metadata=ImportAttributsEnum.parse(attr.getPosition());
//		
//		switch(metadata){
//		case GROUP_DEST:
//			value= getGroupDest();
//		break;
//		case GROUP_NAT:
//			value=  getGroupNat();
//		break;
//		case DEST:
//			value=  getDestination();
//		break;
//		case NAT:
//			value= getNature();
//		break;
//		case CODE_PROG:
//			value=  getProgramme();
//		break;
//		case CODE_SERV:
//			value=  getService();
//		break;
//		case MONTANT:
//			value=  getMontantAE();
//		break;
//		}
		return retour;
	}
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("[groupDest=");
		builder.append(groupDest);
		builder.append(", groupNat=");
		builder.append(groupNat);
		builder.append(", destination=");
		builder.append(destination);
		builder.append(", nature=");
		builder.append(nature);
		builder.append(", programme=");
		builder.append(programme);
		builder.append(", service=");
		builder.append(service);
		builder.append("]");
		return builder.toString();
	}
	public int getRowNum() {
		return rowNum;
	}
	public void setRowNum(int rowNum) {
		this.rowNum = rowNum;
	}

}
