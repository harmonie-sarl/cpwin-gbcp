package fr.symphonie.budget.core.model.edition.util;

public enum AnneeEnum {

	ANT("ANT"), N("N"), N1("N1"), N2("N2"), N3("N3");

	private String code;

	private AnneeEnum(String code) {
		this.code = code;

	}

	public String getCode() {
		return code;
	}

	public static AnneeEnum parse(String code) {
		AnneeEnum enumeration = null;
		if (code == null)
			return enumeration;
		for (AnneeEnum item : AnneeEnum.values()) {
			if (item.getCode().equals(code.trim())) {
				enumeration = item;
				break;
			}
		}
		return enumeration;
	}
}
