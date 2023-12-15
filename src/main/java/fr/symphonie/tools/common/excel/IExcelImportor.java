package fr.symphonie.tools.common.excel;

import java.util.List;

import org.primefaces.event.FileUploadEvent;

import fr.symphonie.impor.Importable;
import fr.symphonie.util.model.SimpleEntity;

public interface IExcelImportor {
	void fileUploadHandler(FileUploadEvent event) ;
	<T extends Importable> List<T> importFile(int noSheet,Class<T> entityType);
	Long getCrc32Value();
	List<SimpleEntity> getErrorReport();
	String getFileName();
	void reset();

}
