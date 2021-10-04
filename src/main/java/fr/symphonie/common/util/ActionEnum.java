package fr.symphonie.common.util;



/**
 * @author Souhaib JEDIDI
 *
 */
public enum ActionEnum {

	CREATE("Create"),UPDATE("Update"),CONSULT("Consult")
	;

	 private  String value;
	 ActionEnum(String value){
         this.value = value;
     }
     public String value(){ return this.value; }
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	
	public static ActionEnum parse(String str){
		ActionEnum enumeration=null;
		for(ActionEnum item:ActionEnum.values()){
			if(item.value.equals(str)){
				enumeration=item;
				break;
			}			
		}
		return enumeration;
	}

}
