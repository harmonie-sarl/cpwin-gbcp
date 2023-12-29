package fr.symphonie.budget.core.model.edition.util;

import java.util.ArrayList;
import java.util.List;

import fr.symphonie.budget.core.model.DepRecEnum;

public enum NatGrpEnum2024 {
	NAT_GRP_1("1","Personnel",DepRecEnum.Depense),
	NAT_GRP_2("2","Fonctionnement",DepRecEnum.Depense),
	NAT_GRP_3("3","Investissement",DepRecEnum.Depense),
	NAT_GRP_4("4"," Interventions",DepRecEnum.Depense),
	NAT_GRP_R11("R11","Subvention pour charges de service public",DepRecEnum.Recette),
	NAT_GRP_R12("R12","Autres financements de l'Etat",DepRecEnum.Recette),
	NAT_GRP_R13("R13","Fiscalité affectée",DepRecEnum.Recette),
	NAT_GRP_R14("R14","Autres financements publics",DepRecEnum.Recette),
	NAT_GRP_R15("R15","Autres Recettes propres",DepRecEnum.Recette),
	NAT_GRP_R17("R17","Subvention pour charges d'investissement ",DepRecEnum.Recette),
	NAT_GRP_R21("R21","Subvention pour charges d'investissement fléchée ",DepRecEnum.Recette),
	NAT_GRP_R22("R22","Financements de l'Etat fléchés",DepRecEnum.Recette),
	NAT_GRP_R24("R24","Autres financements publics fléchés",DepRecEnum.Recette),
	NAT_GRP_R25("R25","Recettes propres fléchées ",DepRecEnum.Recette);
	



	
	        
	
	private String code;
	private String libelle;
	private DepRecEnum type;
	private NatGrpEnum2024(String code, String libelle,DepRecEnum deprec) {
		this.code = code;
		this.libelle = libelle;
		this.type=deprec;
	}
	public String getCode() {
		return code;
	}
	public String getLibelle() {
		return libelle;
	}
	public static NatGrpEnum2024 parse(String str){
		NatGrpEnum2024 enumeration=null;
		if(str==null)return enumeration;
		for(NatGrpEnum2024 item:NatGrpEnum2024.values()){
			if(item.getCode().equals(str.trim())){
				enumeration=item;
				break;
			}			
		}	
		return enumeration;
	}
	public DepRecEnum getType() {
		return type;
	}
	public static List<NatGrpEnum2024> getNatGrp(DepRecEnum type){
		List<NatGrpEnum2024> result=new ArrayList<>();
		for(NatGrpEnum2024 item:NatGrpEnum2024.values()){
			if(item.type!=type)continue;
			result.add(item);
		}
		
		return result;
	}
	
	}