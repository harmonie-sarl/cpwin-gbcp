package fr.symphonie.budget.core.model.plt;

public enum EncDecEnum {
Encaissement("E"),
Decaissement("D");
	private String value;

	private EncDecEnum(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}
	public static EncDecEnum parse(String str){
		//EncDecEnum result=null;
		for(EncDecEnum v: EncDecEnum.values()){
			if(v.getValue().equals(str))
				return v;
		}
		return null;
	}
}
