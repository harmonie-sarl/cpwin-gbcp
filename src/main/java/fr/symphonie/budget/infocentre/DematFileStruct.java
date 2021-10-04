package fr.symphonie.budget.infocentre;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.symphonie.budget.core.model.edition.TabsEnum;
import fr.symphonie.budget.core.model.pluri.BalanceItem;


public class DematFileStruct {
	private static final Logger logger = LoggerFactory.getLogger(DematFileStruct.class);
	private TabsEnum type;
	private Identification head;
	private List<Detail> body;
	private EndLine footer;
	
	public DematFileStruct() {
		super();
		this.body=new ArrayList<>();

	}
	public DematFileStruct(TabsEnum type,Map<String, String> dematParams) {
		this();
		this.type=type;
		this.head=new Identification(dematParams);
	}

	public Identification getHead() {
		return head;
	}
	public void setHead(Identification head) {
		this.head = head;
	}
	public List<Detail> getBody() {
		return body;
	}
	public void setBody(List<Detail> body) {
		this.body = body;
	}
	public EndLine getFooter() {
		return footer;
	}
	public void setFooter(EndLine footer) {
		this.footer = footer;
	}
	public String getGeneratedFileName(){
		String separator="_";
		StringBuilder name=new StringBuilder();
		name.append(getType().getType());
		name.append(separator);
		name.append(getHead().getAttribut(AttributType.Identifiant.getName()).toString());
		name.append(separator);
		name.append("1");
		name.append(separator);
		name.append(getHead().getAttribut(AttributType.Code_budget.getName()).toString());
		name.append(separator);
		name.append(getHead().getAttribut(AttributType.Exercice.getName()).toString());
		name.append(separator);
		name.append(getHead().getAttribut(AttributType.Rang.getName()).toString());
				

		return name.toString();
	}
		
	public TabsEnum getType() {
		return type;
	}
	public void setType(TabsEnum type) {
		this.type = type;
	}

	public abstract class Line{
		private Attribut type;
		private Attribut numero;
		private Integer filler;
		public abstract Attribut[] getAttributs();
		
		public Line() {
			super();
			this.type=new Attribut(AttributType.TYPE);
			this.numero=new Attribut(AttributType.NUMERO);		
		}
		
		public Attribut getAttribut(String name){
			if(getAttributs()==null) return null;
			for(Attribut attr: getAttributs()){
				if(attr.getType().getName().equals(name)) return attr;
			}
			return null;
		}
		
		public Attribut getType() {
			return type;
		}
		public void setType(Attribut type) {
			this.type = type;
		}
		public Attribut getNumero() {
			return numero;
		}
		public void setNumero(Attribut numero) {
			this.numero = numero;
		}
		public Integer getFiller() {
			return filler;
		}
		public void setFiller(Integer filler) {
			this.filler = filler;
		}

		@Override
		public String toString() {
			StringBuilder retour=new StringBuilder("");
			String zoneEspace="";
			retour.append(getType().toString());
			retour.append(getNumero().toString());
			if(getAttributs()!=null){
				for(Attribut attr:getAttributs())retour.append(attr.toString());
			}
			if(getFiller()!=null){
				for(int i=0; i<getFiller();i++)zoneEspace+=" ";
				retour.append(zoneEspace);
			}
			logger.debug("Line N° {} -> {}",getNumero(),retour.toString());
			return retour.toString();
		}
		
		
	}
	public enum AttributType{
		TYPE("type",1,TypeEnum.Alpha),
		NUMERO("numero",5,TypeEnum.Numeric),
		Identifiant("identifiant",10,TypeEnum.Numeric),
		Type_doc("type_doc",2,TypeEnum.Alpha),
		Code_nomenclature("code_nomenclature",2,TypeEnum.Alpha),
		Code_budget("code_budget",2,TypeEnum.Alpha),
		Exercice("exercice",4,TypeEnum.Alpha),
		Rang("rang",2,TypeEnum.Alpha),
		Date_arret("date_arret",8,TypeEnum.Alpha),
		Siren("siren",9,TypeEnum.Numeric),
		Siret("siret",14,TypeEnum.Numeric)	,
		Libelle("libelle",2,TypeEnum.Alpha),
		Montant("montant",15,TypeEnum.Monnaie),
		Type_compte("Type_compte",1,TypeEnum.Numeric),
		Compte("compte",15,TypeEnum.Alpha),
		Debbe("Debbe",15,TypeEnum.Monnaie),
		Debcum("Debcum",15,TypeEnum.Monnaie),
		Debtot("Debtot",15,TypeEnum.Monnaie),
		Crebe("Crebe",15,TypeEnum.Monnaie),
		Crecum("Crecum",15,TypeEnum.Monnaie),
		Cretot("Cretot",15,TypeEnum.Monnaie),
		Bsdeb("Bsdeb",15,TypeEnum.Monnaie),
		Bscre("Bscre",15,TypeEnum.Monnaie)
		
		;
		private String name;
		private int size;
		private TypeEnum type;
		 AttributType(String name,int size,TypeEnum type){
			this.name=name;
			this.size=size;
			this.type=type;
		}
		public String getName() {
			return name;
		}
		public int getSize() {
			return size;
		}
		public TypeEnum getType() {
			return type;
		}
		 
	}
	public class Attribut {
		private AttributType type;
		private Object value;
		public Attribut(AttributType type, Object value) {
			this(type);
			this.value = value;
		}
		
		public Attribut(AttributType type) {
			super();
			this.type=type;
		}
		
		public AttributType getType() {
			return type;
		}

		public void setType(AttributType type) {
			this.type = type;
		}

		public Object getValue() {
			return value;
		}
		public void setValue(Object value) {
			this.value = value;
		}
		public String toString() {
			String retour="";
			String dataValue=null;
			int size=getType().getSize();
		//	logger.debug("toString:type={} : value={}",getType().getType(),getValue());
			switch (type.getType()) {
			case Alpha:
				dataValue=(value==null)?"":value.toString();
				if(dataValue.length()>size)retour+=dataValue.substring(0, size);
				else{
					retour+=dataValue;
					for(int i=dataValue.length();i<size;i++)retour+=" ";
				}		
				
				break;
			case Numeric:
				dataValue=(value==null)?"0":value.toString();
				if(dataValue.length()>size)dataValue=dataValue.substring(0, size);
				
				if(dataValue.length()<size)
					for(int i=dataValue.length();i<size;i++)retour+="0";
				retour+=dataValue;
				break;
			case Monnaie:
				BigDecimal montant=((BigDecimal)getValue());
				if(montant==null)montant=new BigDecimal(0);		
				dataValue=montant.abs().toPlainString();
				dataValue=dataValue.replace(".", "");
				retour+=montant.signum()<0?"-":"+";
				if(dataValue.length()<size-1)
					for(int i=dataValue.length();i<size-1;i++)retour+="0";
				retour+=dataValue;
				break;
			default:
				break;
			}
			return retour;
		}

	}
	public class EndLine extends Line {
		
		public EndLine(Integer numero) {
			this();
			getNumero().setValue(String.valueOf(numero));
		}
		public EndLine() {
			super();
			getType().setValue("9");
			setFiller(53);
		}

		@Override
		public Attribut[] getAttributs() {
			return null;
		}

	}
	public class DetailBalance extends Detail{
		Attribut[]attributs={
				new Attribut(AttributType.Type_compte,"1"),
				new Attribut(AttributType.Compte),
				new Attribut(AttributType.Debbe),	
				new Attribut(AttributType.Debcum),
				new Attribut(AttributType.Debtot),
				new Attribut(AttributType.Crebe),
				new Attribut(AttributType.Crecum),
				new Attribut(AttributType.Cretot),
				new Attribut(AttributType.Bsdeb),
				new Attribut(AttributType.Bscre)
		};
		
		public DetailBalance() {
			super();
			setFiller(null);
		}


		public DetailBalance(Integer numero,BalanceItem data) {
			this();
			getNumero().setValue(String.valueOf(numero));
			getAttribut(AttributType.Compte.getName()).setValue(data.getCompte().getCode());
			getAttribut(AttributType.Debbe.getName()).setValue(data.getDebit().getEntree());
			getAttribut(AttributType.Debcum.getName()).setValue(data.getDebit().getMontant());
			getAttribut(AttributType.Debtot.getName()).setValue(data.getDebit().getTotale());
			
			getAttribut(AttributType.Crebe.getName()).setValue(data.getCredit().getEntree());
			getAttribut(AttributType.Crecum.getName()).setValue(data.getCredit().getMontant());
			getAttribut(AttributType.Cretot.getName()).setValue(data.getCredit().getTotale());
			
			getAttribut(AttributType.Bsdeb.getName()).setValue(data.getSolde().getDebit());
			getAttribut(AttributType.Bscre.getName()).setValue(data.getSolde().getCredit());
			
		}


		@Override
		public Attribut[] getAttributs() {
			return attributs;
		}
		
	}
	public class Detail extends Line {
		Attribut[]attributs={
				new Attribut(AttributType.Libelle),
				new Attribut(AttributType.Montant)		
		};
		
		public Detail(Integer numero,String libelle,BigDecimal montant) {
			this();
			//logger.debug("Detail : numero={}, libelle={}, montant={}",numero,libelle,montant);
			if(montant==null) montant=new BigDecimal(0);
			getNumero().setValue(String.valueOf(numero));
			getAttribut(AttributType.Libelle.getName()).setValue(libelle);
			getAttribut(AttributType.Montant.getName()).setValue(montant);
			
		}
		protected Detail() {
			super();
			getType().setValue("2");
			setFiller(36);
		}


		@Override
		public Attribut[] getAttributs() {
			return attributs;
		}
		
	}
	public class Identification extends Line{
		
		Attribut[]attributs={
				new Attribut(AttributType.Identifiant),
				new Attribut(AttributType.Type_doc),
				new Attribut(AttributType.Code_nomenclature),
				new Attribut(AttributType.Code_budget),
				new Attribut(AttributType.Exercice),
				new Attribut(AttributType.Rang),
				new Attribut(AttributType.Date_arret),
				new Attribut(AttributType.Siren),
				new Attribut(AttributType.Siret),
		
		};
		
		private Identification() {
			super();
			getType().setValue("1");
			getNumero().setValue("1");
			setFiller(null);
		}

		public Identification(Map<String, String> dematParams) {
			this();			
			for(Attribut attr:getAttributs()){
				attr.setValue(dematParams.get(attr.getType().getName()));				
			}			
			
		}

		@Override
		public Attribut[] getAttributs() {
			return attributs;
		}


	}

	public enum TypeEnum {
		Alpha, Numeric, Monnaie, DateTime;
	}
	
	

}
