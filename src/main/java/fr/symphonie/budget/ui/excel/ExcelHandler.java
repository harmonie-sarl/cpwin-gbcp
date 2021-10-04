package fr.symphonie.budget.ui.excel;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.primefaces.model.StreamedContent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.symphonie.doc.MSGenerator;
import fr.symphonie.doc.TypeDocEnum;
import fr.symphonie.util.HandlerJSFMessage;
import fr.symphonie.util.Helper;
import fr.symphonie.util.model.SimpleBean;

public class ExcelHandler {
	
	private static final Logger logger = LoggerFactory.getLogger(ExcelHandler.class);
	public static final String EXCEL_TEMPLETING_PATH =Helper.getContextPath()+ File.separator+"templates"+File.separator+"xls"+File.separator;
	@SuppressWarnings("rawtypes")
	private  List data;
	private ExcelModelEnum model;
	private StreamedContent excelFile;
	private ExcelDocType documentType;
	
protected ExcelHandler() {
		super();
	}
@SuppressWarnings("rawtypes")
public ExcelHandler(ExcelModelEnum model,List data) {
	super();
	this.data = data;
	this.model = model;
	this.setDocumentType(model.getDocType());
	buildXlsFile();
}
protected final void buildXlsFile(){
	

	File file=null;
	String fileName=model.getFileName();
	Map<String,Object> context=getContext();
	Integer exercice=(Integer)context.get("exercice");
	String codeBudget=(String)context.get("budget");
	if (logger.isDebugEnabled()) {
		logger.debug("getXlsFile() fileName="+fileName+", exercice="+exercice+" - start"); //$NON-NLS-1$
		logger.debug("getXlsFile() EXPORT_OUTPUT_PATH ="+MSGenerator.EXPORT_OUTPUT_PATH);
	}

	StringBuffer savingPath=new StringBuffer( MSGenerator.EXPORT_OUTPUT_PATH);
	if(exercice!=null)savingPath.append(exercice);
	if(codeBudget!=null)savingPath.append(File.separator+codeBudget);
	savingPath.append(File.separator+getDocumentType().getFileNamePrefix()+fileName);
	String modelPath=EXCEL_TEMPLETING_PATH+fileName;
	
	file= MSGenerator.generateFileFromModel(TypeDocEnum.XLS, modelPath, savingPath.toString(),null, context);
		StreamedContent returnStreamedContent = Helper.getStreamedContentFile(file);
		if (logger.isDebugEnabled()) {
			logger.debug("getXlsFile() - end"); //$NON-NLS-1$
		}
	excelFile= returnStreamedContent;
	
}

protected Map<String,Object> getContext(){
	Map<String,Object> context=new HashMap<String,Object>();
	context.put("titre",getDocumentType().getTitle());
	context.put("params",getDocumentType(). getCriteres());
	if(getColumns()!=null)
	context.put("cols", getColumns());
	context.put("data", data);
	Map<String,String> specificColumns=getDocumentType().getSpecificColumns();
	if(specificColumns!=null){
		for(String key:specificColumns.keySet()){
			String value=specificColumns.get(key);				
			context.put(key,HandlerJSFMessage.getMessageByKey(value));
		}
	}
	Map<String,Object> specificValues=getDocumentType().getSpecificValues();
	if(specificValues!=null){
		for(String key:specificValues.keySet()){
			Object value=specificValues.get(key);				
			context.put(key,value);
		}
	}
	return context;
}
protected static List<SimpleBean> getCriteres() {
	List<SimpleBean> criteres=new ArrayList<SimpleBean>();
	
	return criteres;
}
protected final List<SimpleBean> getColumns(){
	String[] columnsKey=getDocumentType().getColumnsKey();
	if(columnsKey==null)return null;
	return Helper.getMsgCollection(getDocumentType().getColumnsKey());
}
public StreamedContent getExcelFile() {
	return excelFile;
}

private ExcelDocType getDocumentType() {
	return documentType;
}

private void setDocumentType(ExcelDocType documentType) {
	this.documentType = documentType;
}
}
