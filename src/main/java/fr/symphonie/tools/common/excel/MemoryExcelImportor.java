package fr.symphonie.tools.common.excel;

import java.util.ArrayList;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import org.primefaces.event.FileUploadEvent;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import fr.symphonie.impor.Importable;
import fr.symphonie.util.FileHelper;
import fr.symphonie.util.model.SimpleEntity;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
@Slf4j
@Component
@Scope("prototype")
@Qualifier("memory")
public class MemoryExcelImportor implements IExcelImportor{
	private byte[] contents;
	@Getter
	private String fileName;
	@Getter
	private Long crc32Value;
	@Getter
	private List<SimpleEntity> errorReport;
	public MemoryExcelImportor() {
		super();
		reset();
	}

	@Override
	public void fileUploadHandler(FileUploadEvent event) {
		
		if(event==null)return;
		try {
			log.debug("fileUploadHandler:start");
			
			this.fileName = event.getFile().getFileName();
			event.getFile().getContentType();
			this.contents = event.getFile().getContents();
			this.crc32Value=(FileHelper.checksumInputStream(event.getFile().getInputstream()));
		
		} catch (Exception e) {
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, this.fileName + " failed to uploaded.",e.getMessage());
			e.printStackTrace();
			 FacesContext.getCurrentInstance().addMessage(null, message);
		}	
		log.debug("fileUploadHandler: end");
	   
	}
	@Override
	public <T extends Importable> List<T> importFile(int noSheet,Class<T> entityType) {	
		return ExcelImportHandler.importFile(noSheet, entityType, contents, fileName, errorReport);
	
	}

	@Override
	public void reset() {
		this.errorReport	=	new ArrayList<SimpleEntity>();
		this.contents		=	null;
		this.crc32Value		=	null;
		this.fileName		=	null;
		
	}
	
	
	
}
