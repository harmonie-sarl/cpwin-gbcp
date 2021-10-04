package fr.symphonie.budget.core.model;

public enum DepRecEnum {
	Depense("D"),
	Recette("R");
		private String value;

		private DepRecEnum(String value) {
			this.value = value;
		}

		public String getValue() {
			return value;
		}
		public static DepRecEnum parse(String str){
			for(DepRecEnum v: DepRecEnum.values()){
				if(v.getValue().equals(str))
					return v;
			}
			return null;
		}
}
