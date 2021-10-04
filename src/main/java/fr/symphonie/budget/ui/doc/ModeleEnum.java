package fr.symphonie.budget.ui.doc;

import fr.symphonie.doc.TypeDocEnum;
import fr.symphonie.util.model.i.Editionable;
import fr.symphonie.util.model.i.IDocumentMetaData;



public enum ModeleEnum  implements IDocumentMetaData {
	
	/* XLS doc */
	ENVELOP_BUDG_LIST("envelopBudg_List.xls"),
	EDITION_BI("editions_bi.xls"),
	/* docx  */
	ENVELOPPE("PAR_ENVELOP","par_enveloppe.docx",TypeDocEnum.DOCX),
//	COMPTE_REC_V2("COMPTE_REC_V2","par_compt_recV2.docx",TypeDocEnum.DOCX),
	SUIVI_CP("SUIVI_CP","suivi_cp.docx",TypeDocEnum.DOCX),
	COMPTE_RENDU_GEST("COMPTE_RENDU_GEST","compte_rendu_gest.docx",TypeDocEnum.DOCX),
	SUIVI_COMPTE_DEP("SUIVI_COMPTE_DEP","par_ccdep.docx",TypeDocEnum.DOCX),
	SUIVI_COMPTE_REC("SUIVI_COMPTE_REC","par_compt_recV2.docx",TypeDocEnum.DOCX);
	private String modelName;
	private String fileName;	
	private TypeDocEnum type;
	
	private ModeleEnum(String fileName_fr) {
		this.fileName = fileName_fr;

	}
	private ModeleEnum(String modelName,String fileName_fr,TypeDocEnum type) {
		this(fileName_fr);
		this.modelName=modelName;
		this.type=type;
	}	
	public String getModelName() {
		return modelName;
	}

	@Override
	public boolean isDocx() {
		if(getType()==null)return false;
		return (getType()==TypeDocEnum.DOCX);
	}
	public TypeDocEnum getType() {
		return type;
	}

	public static IDocumentMetaData parse(String str){
		IDocumentMetaData enumeration=null;
		if(str==null)return enumeration;
		for(IDocumentMetaData item:ModeleEnum.values()){
			if(item.getModelName()==null)continue;
			if(item.getModelName().equals(str.trim())){
				enumeration=item;
				break;
			}			
		}	
		return enumeration;
	}
	@Override
	public String getFileName() {
		return fileName;
	}
	@Override
	public String getFileName(String langue) {
		return getFileName();
	}
	@Override
	public boolean isLettre() {
		return false;
	}
	@Override
	public boolean isConfidentiel() {
		return false;
	}
	@Override
	public boolean isAppropriate(Editionable businessObject) {
		return false;
	}
	@Override
	public boolean isTelechargable() {
		return false;
	}

}
