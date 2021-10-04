package fr.symphonie.budget.core.model.edition;

import fr.symphonie.budget.core.model.cf.CfGenericDemat;
import fr.symphonie.budget.core.model.cf.DematBilan;
import fr.symphonie.budget.core.model.cf.DematCR;
import fr.symphonie.budget.core.model.demat.DematABE;
import fr.symphonie.budget.core.model.demat.DematABP;
import fr.symphonie.budget.core.model.demat.DematCAFP;
import fr.symphonie.budget.core.model.demat.DematCRP;
import fr.symphonie.budget.core.model.demat.DematEFE;
import fr.symphonie.budget.core.model.demat.DematEFP;
import fr.symphonie.budget.core.model.demat.DematSPP2;
import fr.symphonie.budget.core.model.demat.DematTFP;
import fr.symphonie.budget.core.model.demat.DematTSBCE;
import fr.symphonie.budget.core.model.demat.GenericDemat;

public enum TabsEnum {
	ABP("ABP",DematABP.class,true,true),
	CRP("CRP",DematCRP.class,true,true),
	TFP("TFP",DematTFP.class,true,true),
	SPP2("SPP2",DematSPP2.class,true,true),
	EFP("EFP",DematEFP.class,true,true),
	CAFP("CAFP",DematCAFP.class,true,true),
	ABE("ABE",DematABE.class,false,true),
	EFE("EFE",DematEFE.class,false,true),
	TAB8("TAB8",null,true,false),
	//TAB10("TAB10",null,true,false),
	TAB10("TSBCE",DematTSBCE.class,true,true),
	TAB3_TDD("TDD",null,true,false),
	TAB3_TRO("TRO",null,true,false),
	BILAN("B",null,DematBilan.class,false,false),
	CR("R",null,DematCR.class,false,false),
	TAB6("TAB6",null,false,false),
	BALANCE("BAL",null,false,false)
	;
	private String type;
	private Class<? extends GenericDemat> entityType;
	private Class<? extends CfGenericDemat> cfDematType;
	private boolean bibr;
	private boolean demat;
//	 private static final Logger logger = LoggerFactory.getLogger(TabsEnum.class);
	public boolean isDemat() {
		return demat;
	}
	private TabsEnum(String type,Class<? extends GenericDemat> entityType) {
		this.setEntityType(entityType);
		this.setType(type);
		this.setBibr(false);
	}
	private TabsEnum(String type,Class<? extends GenericDemat> entityType,boolean bibr,boolean demat) {
		this(type,entityType);
		this.setBibr(bibr);
		this.demat=demat;
		
	}
	private TabsEnum(String type,Class<? extends GenericDemat> entityType,Class<? extends CfGenericDemat> cfDematType,boolean bibr,boolean demat) {
		this(type,entityType,bibr,demat);
		this.cfDematType=cfDematType;
		
	}
	public Class<? extends GenericDemat> getEntityType() {
		return entityType;
	}

	private void setEntityType(Class<? extends GenericDemat> entityType) {
		this.entityType = entityType;
	}

	public String getType() {
		return type;
	}

	private void setType(String type) {
		this.type = type;
	}
	
	public static TabsEnum parse(String str){
		TabsEnum enumeration=null;
		
		if(str==null)return enumeration;
		for(TabsEnum item:TabsEnum.values()){
			if(item.getType().equals(str.trim())){
				enumeration=item;
				break;
			}			
		}	
		//logger.debug("parse : str='{}' ->{}",str,enumeration);
		return enumeration;
	}
	public boolean isBibr() {
		return bibr;
	}
	private void setBibr(boolean bibr) {
		this.bibr = bibr;
	}
	public Class<? extends CfGenericDemat> getCfDematType() {
		return cfDematType;
	}
	
}
