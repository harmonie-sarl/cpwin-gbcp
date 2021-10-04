package fr.symphonie.budget.core.model.pluri;

public  enum SuiviRecEnum {
	
	rar_ini_ht("rar_ini_ht"),				
	rar_ini_ttc("rar_ini_ttc"),
	rar_net_ht("rar_net_ht"),
	rar_net_ttc("rar_net_ttc"),
//	rec_ht("rec_ht"),
//	rec_ttc("rect_ttc"),
	solde_ttc("solde_ttc"),
//	coef_tva("coef_tva"),
//	rec_ht_bis("rec_ht_bis"),
	exec_orig("exec_orig"),
	no_titrec("no_titrec"),
	tiers("tiers"),
	objet("objet"),
	code_service("code_service"),
	nat_grp("nat_grp"),
	code_prog("code_prog"),
	code_dest("code_dest");
	

	private String code;
	
	private SuiviRecEnum(String code) {
		this.code = code;
	
	}
	
	
	public String getCode() {
		return code;
	}


	


	public static SuiviRecEnum parse(String str){
		SuiviRecEnum enumeration=null;
		if(str==null)return enumeration;
		for(SuiviRecEnum item:SuiviRecEnum.values()){
			if(item.getCode().equalsIgnoreCase(str.trim())){
				enumeration=item;
				break;
			}			
		}	
		return enumeration;
	}
	}
