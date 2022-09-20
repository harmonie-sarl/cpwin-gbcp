package fr.symphonie.budget.ui.beans;

import java.io.File;
import java.io.Serializable;

import javax.annotation.ManagedBean;

//import javax.faces.bean.ManagedBean;
//import javax.faces.bean.SessionScoped;
import javax.enterprise.context.SessionScoped;

import org.primefaces.model.StreamedContent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.symphonie.budget.ui.beans.NavigationBean.Action;
import fr.symphonie.budget.ui.doc.ModeleEnum;
import fr.symphonie.budget.ui.doc.TBDocxHandler;
import fr.symphonie.budget.ui.doc.TbDocxStruct;
import fr.symphonie.common.util.BudgetHelper;
import fr.symphonie.common.util.Constant;
import fr.symphonie.common.util.MsgEntry;
import fr.symphonie.util.HandlerJSFMessage;
import fr.symphonie.util.Helper;



@ManagedBean(value="commonBean")
@SessionScoped 
public class CommonBean implements Serializable {
	
	
	private static final long serialVersionUID = -5596794228252634320L;
	private static final Logger logger = LoggerFactory.getLogger(CommonBean.class);	
	private StreamedContent tBStream=null;
	public void createTBStream() {
		logger.debug("cenerateTBStream - start");
	
		File file =null;
		try{
			file=getTBFile();
			settBStream(Helper.getStreamedContentFile(file));
		} catch (Exception e) {
//			e.printStackTrace();
//			settBStream(Helper.getStreamedContentFile(Helper.getFileNotFound()));
			HandlerJSFMessage.addErrorMessage(getErrorMessageFromException(MsgEntry.FAILED, e));
			e.printStackTrace();
		}
		logger.debug("cenerateTBStream - end");
		
		}
	private File getTBFile() {
		logger.debug("getTBFile - start");
		File generatedFile=null;
		ModeleEnum docMetadata=getTBDocumentMetaData();
		generatedFile=generateTBDocxDocument(docMetadata);
//		generatedFile=new File(generatedFilePath);
		logger.debug("getTBFile - start");
		return generatedFile;
	}
	
	private ModeleEnum getTBDocumentMetaData() {
		ModeleEnum metaData=null;
		Action tbAction=BudgetHelper.getNavigationBean().getCurrentAction();
		switch(tbAction){
		case TABLEAU_BORD_PAR_ENV:
			metaData=ModeleEnum.ENVELOPPE;
			break;
		case TABLEAU_BORD_PAR_COMPREC_V2:
			metaData=ModeleEnum.SUIVI_COMPTE_REC;
			break;
		case TABLEAU_BORD_SUIVI_CP:
			metaData=ModeleEnum.SUIVI_CP;
			break;
		case TABLEAU_BORD_CR_GESTION:
			metaData=ModeleEnum.COMPTE_RENDU_GEST;
			break;
		case TABLEAU_BORD_COMPTE_DEP:
			metaData=ModeleEnum.SUIVI_COMPTE_DEP;
			break;
		default:
			break;
		}
		return metaData;
	}
	private File generateTBDocxDocument(ModeleEnum docMetadata) {
		File result=null;
		logger.debug("generateTBDocxDocument - start");
		TBDocxHandler docx=new TBDocxHandler();
		TbDocxStruct tbStruct=createTbDocxStruct();
		result=docx.generateFile(docMetadata, tbStruct);
		logger.debug("generateTBDocxDocument : result="+result+" - end");
		return result;
	}
	
	public  TbDocxStruct createTbDocxStruct() {
		TbDocxStruct tbStruct=new TbDocxStruct();
		SearchBean search=BudgetHelper.getSearchBean();
		Integer exercice=Integer.parseInt(search.getExercice());
		tbStruct.setExercice(exercice);
		tbStruct.setBudget(search.getCodeBudget());
		tbStruct.setDate(search.getDateArret());
		logger.info("tbStruct.getdate: "+tbStruct.getDate());
		logger.info("tbStruct.getBudget: "+tbStruct.getBudget());
		logger.info("tbStruct.getExercice: "+tbStruct.getExercice());
		return tbStruct;
	}
	public void reset(){
		settBStream(null);
	}
	public void settBStream(StreamedContent tBStream) {
		this.tBStream = tBStream;
	}
	public StreamedContent gettBStream() {
//		if(tBStream==null)	
			createTBStream();
			
		return tBStream;
	}
	
	public static String getErrorMessageFromException(String genericKeyMsg, Exception e) {

		Throwable cause = null;
		String errorMsg = null;
		if (e != null) {
			cause = e;
			errorMsg = cause.getMessage();
			if (cause.getCause() != null) {
				cause = cause.getCause();
				errorMsg += "\n " + cause.getMessage();
				if (cause.getCause() != null) {
					cause = cause.getCause();
					errorMsg += "\n " + cause.getMessage();
				}
			}
		}
		if (genericKeyMsg != null)
			return HandlerJSFMessage.getErrorMessage(genericKeyMsg) + ": " + errorMsg;
		else
			return errorMsg;
	}
public String getMinAjaxQL(){		
		
		return ""+ Constant.getMinAjaxQL();
	}
	public String getMaxAjaxResult(){
		
		return "" + Constant.getMaxAjaxResult();
		
	}
}
