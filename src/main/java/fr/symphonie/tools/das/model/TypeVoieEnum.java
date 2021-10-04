package fr.symphonie.tools.das.model;

public enum TypeVoieEnum {
	AVENUE("Avenue","AV"),
	ALLEE("Allée","ALL"),
	Boulevard("Boulevard","BD"),
	RUE("rue","R"),
	square("square","SQ"),
	impasse("impasse","IMP"),
	Place("Place","PL"),
	Promenade("Promenade","PROM"),
	Route("Route","RTE"),
	De("de",""),
	Du("du",""),
	La("la","");
	private String designation;
	private String abreviation;
	
	TypeVoieEnum(String designation,String abreviation){
		this.designation=designation;
		this.abreviation=abreviation;
	}

	public String getDesignation() {
		return designation;
	}

	public String getAbreviation() {
		return abreviation;
	}
	
	public static TypeVoieEnum parse(String str){
		
		//TypeVoieEnum retour=null;
		for(TypeVoieEnum t:TypeVoieEnum.values()){
			if(str.equalsIgnoreCase(t.getDesignation()))
				return t;
		}
		return null;
		
	}
}
