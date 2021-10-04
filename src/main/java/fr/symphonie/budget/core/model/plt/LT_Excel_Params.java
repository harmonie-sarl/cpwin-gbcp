package fr.symphonie.budget.core.model.plt;

public enum LT_Excel_Params {
	REC_GLOBALISEE(1,"10"),
	REC_FLECHEE(2,"16"),
	REC_FINANCIERE(11,"20"),
	OP_CPT_ENC(15,"24"),
	
	DEP_GLOBALISEE(19,"30"),
	DEP_FLECHEE(20,"35"),
	OP_FINANCIERE(29,"40"),
	OP_CPT_DEC(30,"44")
	;
	private Integer numero;
	private String position;
	private LT_Excel_Params(Integer numero, String position) {
		this.numero = numero;
		this.position = position;
	}
	public Integer getNumero() {
		return numero;
	}
	
	public String getPosition() {
		return position;
	}
	
	public static LT_Excel_Params parse(Integer numLigne){
		if(numLigne!=null)		
		for(LT_Excel_Params item:LT_Excel_Params.values()){
			if(item.getNumero().equals(numLigne))
				return item;
		}
		return null;
	}

}
