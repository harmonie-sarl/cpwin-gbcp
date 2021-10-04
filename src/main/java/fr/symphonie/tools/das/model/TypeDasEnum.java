package fr.symphonie.tools.das.model;

public enum TypeDasEnum {
	RS("RS"),
	NP("NP");
	private String value;
	TypeDasEnum(String value){
		this.setValue(value);
	}
public static TypeDasEnum parse(String str){
		if(str!=null)
		for(TypeDasEnum t:TypeDasEnum.values()){
			if(str.equalsIgnoreCase(t.getValue()))
				return t;
		}
		return null;
		
	}
public String getValue() {
	return value;
}
private void setValue(String value) {
	this.value = value;
}

}
