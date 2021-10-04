package fr.symphonie.budget.core.model.plt;

public enum GlobalEnum {
	Global("G"),
	Detail("D");
	private String value;

	private GlobalEnum(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}
	public static GlobalEnum parse(String str){
		
		for(GlobalEnum v: GlobalEnum.values()){
			if(v.getValue().equals(str))
				return v;
		}
		return null;
	}
}
