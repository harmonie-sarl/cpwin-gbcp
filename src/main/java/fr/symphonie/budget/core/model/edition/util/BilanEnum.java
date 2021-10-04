package fr.symphonie.budget.core.model.edition.util;

public enum BilanEnum {

	BRUT_N("bn"), AMORT_DEP_RN("an"), NET_N("netN"), NET_ANT("netAnt");
	
	
	

	private String code;

	private BilanEnum(String code) {
		this.code = code;

	}

	public String getCode() {
		return code;
	}

	public static BilanEnum parse(String code) {
		BilanEnum enumeration = null;
		if (code == null)
			return enumeration;
		for (BilanEnum item : BilanEnum.values()) {
			if (item.getCode().equals(code.trim())) {
				enumeration = item;
				break;
			}
		}
		return enumeration;
	}
}