package fr.symphonie.tools.lemans.bt.ui;

import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.apache.commons.collections.CollectionUtils;
import org.primefaces.event.FileUploadEvent;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import fr.symphonie.common.util.MsgEntry;
import fr.symphonie.common.util.Util;
import fr.symphonie.tools.common.CommonToolsBean;
import fr.symphonie.tools.common.excel.IExcelImportor;
import fr.symphonie.tools.common.model.FileImportTrace;
import fr.symphonie.tools.lemans.bt.dto.VenteDto;
import fr.symphonie.tools.lemans.bt.dto.VenteItemDto;
import fr.symphonie.util.HandlerJSFMessage;
import fr.symphonie.util.Helper;
import fr.symphonie.util.model.SimpleEntity;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@ManagedBean(name = "btBean")
@ViewScoped
@Slf4j
public class BilletterieBean extends CommonToolsBean {

	@Autowired
	@Qualifier("memory")
	private IExcelImportor importService;
	@Getter
	private List<VenteDto> ventes = null;
	@Getter
	private List<VenteItemDto> venteDetails = null;

	public void fileUploadHandler(FileUploadEvent event) {
		importService.fileUploadHandler(event);
		try {
			List<FileImportTrace> vagueList = getCommonService().getImportHistoryList(getExerciceInt(),getModuleName(), importService.getCrc32Value());
			if (!vagueList.isEmpty()) {
				String msg = HandlerJSFMessage.formatMessage(HandlerJSFMessage.getMessage(MsgEntry.DUPLICAT_CRC_MSG),
						new Object[] { vagueList.get(0).getId() });
				HandlerJSFMessage.addWarnMessage(msg);
			}
		} catch (Exception e) {
			HandlerJSFMessage.addErrorMessage(Util.getErrorMessageFromException(MsgEntry.FAILED, e));
			log.error("genererSepaFr: ", e.getMessage());
		}
		
	}

	public void executeImport() {
		int VENTE_SHEET_NO = 0;
		int PAIEMENT_SHEET_NO = 1;
		this.ventes = importService.importFile(VENTE_SHEET_NO, VenteDto.class);
		if (!isErrorReportVisible()) {
			this.venteDetails = importService.importFile(PAIEMENT_SHEET_NO, VenteItemDto.class);
		}

	}

	public void executeSaveData() {

		try {
			saveImportTrace();

			// String message =
			// HandlerJSFMessage.formatMessage(HandlerJSFMessage.getMessage(MsgEntry.REMB_VALIDATION_MSG),new
			// Object[] { getNextNoVague()});
			// HandlerJSFMessage.addInfoMessage(message);
			HandlerJSFMessage.addInfo(MsgEntry.SUCCES);
		} catch (Exception e) {
			e.printStackTrace();
			HandlerJSFMessage.addErrorMessage(Util.getErrorMessageFromException(MsgEntry.FAILED, e));
			log.error("executeSaveData: Error: ", e.getMessage());
		}
	}

	public boolean isErrorReportVisible() {
		if (CollectionUtils.isEmpty(getErrorReport()))
			return false;
		return true;
	}

	public List<SimpleEntity> getErrorReport() {
		return importService.getErrorReport();
	}

	public String getErreursCount() {
		return "(" + getErrorReport().size() + ")";
	}

	@Override
	protected void executeTask(String taskName) throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	protected String[] getTasks() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void prepare() throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public void reset() {
		importService.reset();

	}

	@Override
	protected Logger getLogger() {
		return log;
	}
	private String getModuleName() {
		return "LEMANS_BT";
	}
	private void saveImportTrace() {
		log.debug("saveImportTrace ...");
		FileImportTrace vague=FileImportTrace.builder()
				.exercice(getExerciceInt())
				.module(getModuleName())
				.crc32(importService.getCrc32Value())
				.fileName(importService.getFileName())
				.trace(Helper.createTrace())
				.build();
		getCommonService().saveImportTrace(vague);	
		log.debug("saveImportTrace : end");
	}

}
