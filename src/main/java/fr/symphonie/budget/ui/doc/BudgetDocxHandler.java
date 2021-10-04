package fr.symphonie.budget.ui.doc;

import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.symphonie.doc.DocxHandler;
import fr.symphonie.util.Helper;
import fr.symphonie.util.model.Langue;
import fr.symphonie.util.model.i.Editionable;
import fr.symphonie.util.model.i.IDocumentMetaData;

public abstract class BudgetDocxHandler extends DocxHandler {
	
	private static final String GENERATED = "Generated_";
	private static final Logger logger = LoggerFactory.getLogger(BudgetDocxHandler.class);
	public static String TEMPLATING_PATH=Helper.getContextPath()+ File.separator+"templates"+File.separator+"doc";

	@Override
	public String getSourcePath(IDocumentMetaData docMetadata, Langue langue) {
		String retour=null;
		String fileName=getFileName(docMetadata,langue);		
		retour=TEMPLATING_PATH+File.separator+fileName;

		if (logger.isDebugEnabled()) {
			logger.debug("getSourcePath: path="+retour+" - end"); //$NON-NLS-1$
		}
		return retour;
	}

	@Override
	protected String getTargetPath(IDocumentMetaData docMetadata,
			Editionable businessObj, String ordre) {
		String targetPath=getTargetPath(docMetadata);
		logger.debug("getTargetPath:targetPath="+targetPath);
		return targetPath;
	}
	private String getTargetPath(IDocumentMetaData docMetadata) {
		StringBuilder path =new StringBuilder();

		try {
		path.append(TEMPLATING_PATH+File.separator+GENERATED);
		path.append(docMetadata.getFileName());
		}catch (Exception e) {
			e.printStackTrace();
		}
		return path.toString();
	}
	private String getFileName(IDocumentMetaData docMetadata, Langue langue) {
		return docMetadata.getFileName();
	}
}
