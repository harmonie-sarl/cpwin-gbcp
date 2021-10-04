package fr.symphonie.budget.core.model.edition.util;

import java.util.ArrayList;
import java.util.List;

public enum CRMsgKey {
	//CHARGES DE FONCTIONNEMENT
	CH_FO_1("cf.cr.ch.fo.1",true,CRType.fonctionnement,new DataRefEnum[]{DataRefEnum.cf_ref_001,DataRefEnum.cf_ref_002}),
	CH_FO_2("cf.cr.ch.fo.2",true,CRType.fonctionnement,new DataRefEnum[]{DataRefEnum.cf_ref_003,DataRefEnum.cf_ref_004}),
	CH_FO_3("cf.cr.ch.fo.3",true,CRType.fonctionnement,new DataRefEnum[]{}),
	CH_FO_4("cf.cr.ch.fo.4",true,CRType.fonctionnement,new DataRefEnum[]{DataRefEnum.cf_ref_005,DataRefEnum.cf_ref_006}),
	CH_FO_5("cf.cr.ch.fo.5",true,CRType.fonctionnement,new DataRefEnum[]{DataRefEnum.cf_ref_007,DataRefEnum.cf_ref_008}),
	CH_FO_6("cf.cr.ch.fo.6",true,CRType.fonctionnement,new DataRefEnum[]{DataRefEnum.cf_ref_009,DataRefEnum.cf_ref_010}),
	CH_FO_7("cf.cr.ch.fo.7",true,CRType.fonctionnement,new DataRefEnum[]{DataRefEnum.cf_ref_011,DataRefEnum.cf_ref_012}),
	CH_FO_8("cf.cr.ch.fo.8",true,CRType.fonctionnement,new DataRefEnum[]{DataRefEnum.cf_ref_013,DataRefEnum.cf_ref_014}),
	CH_FO_9("cf.cr.ch.fo.9",true,CRType.fonctionnement,new DataRefEnum[]{DataRefEnum.cf_ref_015,DataRefEnum.cf_ref_016}),
	CH_FO_TOT("cf.cr.ch.fo.tot",true,null,new DataRefEnum[]{DataRefEnum.cf_ref_017,DataRefEnum.cf_ref_018}),
	//CHARGES D'INTERVENTION	
	CH_IN_1("cf.cr.ch.in.1",true,CRType.intervention,new DataRefEnum[]{}),
	CH_IN_2("cf.cr.ch.in.2",true,CRType.intervention,new DataRefEnum[]{DataRefEnum.cf_ref_019,DataRefEnum.cf_ref_020}),
	CH_IN_3("cf.cr.ch.in.3",true,CRType.intervention,new DataRefEnum[]{DataRefEnum.cf_ref_021,DataRefEnum.cf_ref_022}),
	CH_IN_4("cf.cr.ch.in.4",true,CRType.intervention,new DataRefEnum[]{DataRefEnum.cf_ref_023,DataRefEnum.cf_ref_024}),
	CH_IN_5("cf.cr.ch.in.5",true,CRType.intervention,new DataRefEnum[]{DataRefEnum.cf_ref_025,DataRefEnum.cf_ref_026}),
	CH_IN_6("cf.cr.ch.in.6",true,CRType.intervention,new DataRefEnum[]{DataRefEnum.cf_ref_027,DataRefEnum.cf_ref_028}),
	CH_IN_7("cf.cr.ch.in.7",true,CRType.intervention,new DataRefEnum[]{DataRefEnum.cf_ref_029,DataRefEnum.cf_ref_030}),
	
	CH_IN_ENG("cf.cr.ch.in.engag",true,CRType.intervention,new DataRefEnum[]{DataRefEnum.cf_ref_033,DataRefEnum.cf_ref_034}),
	CH_FO_IN_TOT("cf.cr.ch.fo.in.tot",true,null,new DataRefEnum[]{DataRefEnum.cf_ref_035,DataRefEnum.cf_ref_036}),
	CH_IN_TOT("cf.cr.ch.in.tot",true,null,new DataRefEnum[]{DataRefEnum.cf_ref_031,DataRefEnum.cf_ref_032}),
	IMPOT("cf.cr.impot",true,CRType.intervention,new DataRefEnum[]{DataRefEnum.cf_ref_049,DataRefEnum.cf_ref_050}),
	BENEFICE("cf.cr.benefice",true,null,new DataRefEnum[]{DataRefEnum.cf_ref_051,DataRefEnum.cf_ref_052}),
	CH_TOT("cf.cr.ch.tot",true,null,new DataRefEnum[]{DataRefEnum.cf_ref_053,DataRefEnum.cf_ref_054}),
	//CHARGES FINANCIERES
	CH_FI_1("cf.cr.ch.fi.1",true,CRType.financier,new DataRefEnum[]{DataRefEnum.cf_ref_037,DataRefEnum.cf_ref_038}),
	CH_FI_2("cf.cr.ch.fi.2",true,CRType.financier,new DataRefEnum[]{DataRefEnum.cf_ref_039,DataRefEnum.cf_ref_040}),
	CH_FI_3("cf.cr.ch.fi.3",true,CRType.financier,new DataRefEnum[]{DataRefEnum.cf_ref_041,DataRefEnum.cf_ref_042}),
	CH_FI_4("cf.cr.ch.fi.4",true,CRType.financier,new DataRefEnum[]{DataRefEnum.cf_ref_043,DataRefEnum.cf_ref_044}),
	CH_FI_5("cf.cr.ch.fi.5",true,CRType.financier,new DataRefEnum[]{DataRefEnum.cf_ref_045,DataRefEnum.cf_ref_046}),
	CH_FI_TOT("cf.cr.ch.fi.tot",true,null,new DataRefEnum[]{DataRefEnum.cf_ref_047,DataRefEnum.cf_ref_048}),
	//PRODUITS DE FONCTIONNEMENT
	PR_FO_1 ("cf.cr.pr.fo.1",false,CRType.fonctionnement,new DataRefEnum[]{}),
	PR_FO_2 ("cf.cr.pr.fo.2",false,CRType.fonctionnement,new DataRefEnum[]{DataRefEnum.cf_ref_055,DataRefEnum.cf_ref_056}),
	PR_FO_3 ("cf.cr.pr.fo.3",false,CRType.fonctionnement,new DataRefEnum[]{DataRefEnum.cf_ref_057,DataRefEnum.cf_ref_058}),
	PR_FO_4 ("cf.cr.pr.fo.4",false,CRType.fonctionnement,new DataRefEnum[]{DataRefEnum.cf_ref_059,DataRefEnum.cf_ref_060}),
	PR_FO_5 ("cf.cr.pr.fo.5",false,CRType.fonctionnement,new DataRefEnum[]{DataRefEnum.cf_ref_061,DataRefEnum.cf_ref_062}),
	PR_FO_6 ("cf.cr.pr.fo.6",false,CRType.fonctionnement,new DataRefEnum[]{DataRefEnum.cf_ref_063,DataRefEnum.cf_ref_064}),
	PR_FO_7 ("cf.cr.pr.fo.7",false,CRType.fonctionnement,new DataRefEnum[]{}),
	PR_FO_8 ("cf.cr.pr.fo.8",false,CRType.fonctionnement,new DataRefEnum[]{DataRefEnum.cf_ref_065,DataRefEnum.cf_ref_066}),
	PR_FO_9 ("cf.cr.pr.fo.9",false,CRType.fonctionnement,new DataRefEnum[]{DataRefEnum.cf_ref_067,DataRefEnum.cf_ref_068}),
	PR_FO_10("cf.cr.pr.fo.10",false,CRType.fonctionnement,new DataRefEnum[]{DataRefEnum.cf_ref_069,DataRefEnum.cf_ref_070}),
	PR_FO_11("cf.cr.pr.fo.11",false,CRType.fonctionnement,new DataRefEnum[]{DataRefEnum.cf_ref_071,DataRefEnum.cf_ref_072}),
	PR_FO_12("cf.cr.pr.fo.12",false,CRType.fonctionnement,new DataRefEnum[]{DataRefEnum.cf_ref_073,DataRefEnum.cf_ref_074}),
	PR_FO_13("cf.cr.pr.fo.13",false,CRType.fonctionnement,new DataRefEnum[]{}),
	PR_FO_14("cf.cr.pr.fo.14",false,CRType.fonctionnement,new DataRefEnum[]{DataRefEnum.cf_ref_075,DataRefEnum.cf_ref_076}),
	PR_FO_15("cf.cr.pr.fo.15",false,CRType.fonctionnement,new DataRefEnum[]{DataRefEnum.cf_ref_077,DataRefEnum.cf_ref_078}),
	PR_FO_16("cf.cr.pr.fo.16",false,CRType.fonctionnement,new DataRefEnum[]{DataRefEnum.cf_ref_079,DataRefEnum.cf_ref_080}),
	PR_FO_TOT("cf.cr.pr.fo.tot",false,null,new DataRefEnum[]{DataRefEnum.cf_ref_081,DataRefEnum.cf_ref_082}),
	//PRODUITS FINANCIERS
	PR_FI_1("cf.cr.pr.fi.1",false,CRType.financier,new DataRefEnum[]{DataRefEnum.cf_ref_083,DataRefEnum.cf_ref_084}),
	PR_FI_2("cf.cr.pr.fi.2",false,CRType.financier,new DataRefEnum[]{DataRefEnum.cf_ref_085,DataRefEnum.cf_ref_086}),
	PR_FI_3("cf.cr.pr.fi.3",false,CRType.financier,new DataRefEnum[]{DataRefEnum.cf_ref_087,DataRefEnum.cf_ref_088}),
	PR_FI_4("cf.cr.pr.fi.4",false,CRType.financier,new DataRefEnum[]{DataRefEnum.cf_ref_089,DataRefEnum.cf_ref_090}),
	PR_FI_5("cf.cr.pr.fi.5",false,CRType.financier,new DataRefEnum[]{DataRefEnum.cf_ref_091,DataRefEnum.cf_ref_092}),
	PR_FI_6("cf.cr.pr.fi.6",false,CRType.financier,new DataRefEnum[]{DataRefEnum.cf_ref_093,DataRefEnum.cf_ref_094}),
	PR_FI_7("cf.cr.pr.fi.7",false,CRType.financier,new DataRefEnum[]{DataRefEnum.cf_ref_095,DataRefEnum.cf_ref_096}),
	PR_FI_8("cf.cr.pr.fi.8",false,CRType.financier,new DataRefEnum[]{DataRefEnum.cf_ref_097,DataRefEnum.cf_ref_098}),
	PR_FI_TOT("cf.cr.pr.fi.tot",false,null,new DataRefEnum[]{DataRefEnum.cf_ref_099,DataRefEnum.cf_ref_100}),
	PERTE("cf.cr.perte",false,null,new DataRefEnum[]{DataRefEnum.cf_ref_101,DataRefEnum.cf_ref_102}),
	PR_TOT("cf.cr.pr.tot",false,null,new DataRefEnum[]{DataRefEnum.cf_ref_103,DataRefEnum.cf_ref_104})
	;
	
	
	private String key;
	private boolean chargeItem;
	private CRType type;
	private DataRefEnum[] ref;
	private String style;

	CRMsgKey(String key, boolean charge,CRType type,DataRefEnum[] ref) {
		this.key=key;
		this.chargeItem=charge;
		this.type=type;
		this.ref=ref;
		
	}
	CRMsgKey(String key, boolean charge,CRType type,DataRefEnum[] ref,String style) {
		this(key,charge,type,ref);
		this.style=style;
		
	}
	
	CRMsgKey(String key, boolean charge,CRType type,DataRefEnum ref) {
		this.key=key;
		this.chargeItem=charge;
		this.type=type;
		//this.ref=ref;
		
	}
	CRMsgKey(String key, boolean charge,CRType type,DataRefEnum ref,String style) {
		this(key,charge,type,ref);
		this.style=style;
		
	}
	public String getKey() {
		return key;
	}


	public boolean isChargeItem() {
		return chargeItem;
	}

	public CRType getType() {
		return type;
	}

	public DataRefEnum[] getRef() {
		return ref;
	}

	public static List<CRMsgKey> getGroupe(boolean charge,CRType type){
		List<CRMsgKey> list=new ArrayList<CRMsgKey>();
		for(CRMsgKey v:CRMsgKey.values()){
			if(v.getType()==null)continue;
			if(v.isChargeItem()!=charge)continue;
			if(v.getType()!=type)continue;
			list.add(v);
		}
		return list;
	}

	public String getStyle() {
		return style;
	}

	public void setStyle(String style) {
		this.style = style;
	}
	

}
