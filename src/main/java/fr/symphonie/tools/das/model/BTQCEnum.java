package fr.symphonie.tools.das.model;

public enum BTQCEnum  {
	
	Bis("B","Bis"),
	Ter("T","Ter"),
	Quater("Q","Quater"),
	C("C","C");
	
	private String value;
	private String designation;
	
	BTQCEnum(String value,String designation){
		this.value=value;
		this.designation=designation;
		
	}

	
	public String getValue() {
		return value;
	}


	public void setValue(String value) {
		this.value = value;
	}


	
	
	public String getDesignation() {
		return designation;
	}


	public void setDesignation(String designation) {
		this.designation = designation;
	}


	public static BTQCEnum parse(String value){	
		for(BTQCEnum t:BTQCEnum.values()){
			if(value.equalsIgnoreCase(t.getValue()))
				return t;
		}
		return null;
		
	}
}