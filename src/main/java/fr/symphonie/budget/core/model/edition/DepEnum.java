package fr.symphonie.budget.core.model.edition;

public enum DepEnum {
	PERSONNEL ( 0),
	FONCTIONNEMENT(1),
	INVESTISSEMENT (2),
	INTERVENTION (3);
	private int index;
	DepEnum(int index){
		setIndex(index);
	}
	public int getIndex() {
		return index;
	}
	private void setIndex(int index) {
		this.index = index;
	}
	public static DepEnum parse(int code){
		for(DepEnum v: DepEnum.values()){
			if(v.getIndex()==code)
				return v;
		}
		return null;
	}
	
}
