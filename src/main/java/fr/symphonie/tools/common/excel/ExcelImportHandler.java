package fr.symphonie.tools.common.excel;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import fr.symphonie.exception.ImportException;
import fr.symphonie.impor.AttributImport;
import fr.symphonie.impor.Importable;
import fr.symphonie.impor.TypeEnum;
import fr.symphonie.util.FileHelper;
import fr.symphonie.util.model.SimpleEntity;
import lombok.extern.slf4j.Slf4j;
@Slf4j
public class ExcelImportHandler {
	
	public static <T extends Importable> List<T> importFile(int noSheet,Class<T> entityType, byte[] contents, String fileName,List<SimpleEntity> errorReport) {
		log.debug("importFile : start");
		
		List<T> result = new ArrayList<>();
		T data =null;
		Workbook wb =null;
		try (InputStream input = new ByteArrayInputStream(contents);)
		{
		
			wb =isXls(fileName)? new HSSFWorkbook(input):new XSSFWorkbook(input);
			Sheet sheet = wb.getSheetAt(noSheet);
			Iterator<Row> rows = sheet.rowIterator();
			
		
			while (rows.hasNext()) {
				Row row =  rows.next();
				log.debug(" ### importFile : rowCount= {} --> debut",row.getRowNum());
				data =entityType.newInstance();	
				if(!ExcelImportHandler.isValidRow(data,row,errorReport)){
					continue;				
				}
				ExcelImportHandler.parse(data,row);
				result.add(data);
			}
            
		} catch (IOException | InstantiationException | IllegalAccessException 				e) {
			log.error("importFile : error : {}",e.getMessage());
		} 

		log.debug("importFile : size={}, errors={} ",result.size(),errorReport.size());
		
		return result;
	}
	
	public static <T extends Importable> void parse(T data,Row row) {
		int START_INDEX=0,END_INDEX=data.getConfigImport().getLastColumnIndex();
		Cell cell =null;
		Object cellValue=null;
		AttributImport attributConfig=null;
		for(int index=START_INDEX;index<=END_INDEX;index++){
			attributConfig=data.getConfigImport().getAttribut(index);
			if(attributConfig==null)continue;//colonne non utilisé
			 cell=row.getCell(index);
			if(cell!=null)
				try {
					cellValue=getValue(cell,attributConfig.getType());
				} catch (ImportException e) {
					cellValue=null;
				}
			else cellValue=null;
			log.debug("parse: index={}, value={}",index,cellValue);
			data.setValue(attributConfig, cellValue);
			data.setRowNum(row.getRowNum());
			
		}
	}
	public static <T extends Importable> boolean isValidRow(T data,Row row,List<SimpleEntity> errorReport) {
		List<SimpleEntity> errors=new ArrayList<>();
		String msg=null;
		Cell cell =null;
		AttributImport attributConfig=null;
		Object value=null;
		int START_INDEX=0,END_INDEX=data.getConfigImport().getLastColumnIndex();
		
		if((row==null)||(row.getRowNum()<data.getConfigImport().getStartedRowIndex())) return false;
		if(isEmptyRow(row)) return false;
		for(int i=START_INDEX; i<=END_INDEX;i++){
			attributConfig=data.getConfigImport().getAttribut(i);
			if(attributConfig==null)continue;//colonne non utilisé
			log.debug("isValidRow : début validation cell index= {}, type={}",i, attributConfig.getType());
			value=null;
			cell=row.getCell(i);
			
			try{
			value=getValue(cell,attributConfig.getType());
			
			if((attributConfig.isRequired())&&(value==null)){
//				log.debug("isValidRow Required check");
				msg=new ImportException(row.getRowNum(),i,attributConfig.isRequired()).getMessage();
				
				errors.add(new SimpleEntity(row.getRowNum(),row.getRowNum()+"/"+i,msg,"")) ;
			}
			}
			catch(Exception e){
				//log.error("isValidRow : catch exception {}",e.getMessage());
				msg=new ImportException(row.getRowNum(),cell,attributConfig.getType()).getMessage();
				log.debug("isValidRow transformed msg {}",msg);
				errors.add(new SimpleEntity(row.getRowNum(),row.getRowNum()+"/"+i,msg,"")) ;
//				log.debug("isValidRow errors.size={}, value={}",errors.size(),value);
			}
			
			
		}
//		log.debug("#### isValidRow rowNum={} , isValide={}",row.getRowNum(), errors.isEmpty());
		errorReport.addAll(errors);
		
		log.debug("#### isValidRow rowNum={} , isValide={}",row.getRowNum(), errors.isEmpty());
		
		return errors.isEmpty();
	}
	public static Object getValue(Cell cell, TypeEnum type) throws ImportException   {
		Object value=null;
		int decimalPlace=2;
		if(cell==null)return null;
		switch(type){
		case Alpha:
			value=getValueAsString(cell);
			break;
		case Numeric:
			decimalPlace=0;			
		case Monnaie:
			try{
        	BigDecimal bd= new BigDecimal(cell.getNumericCellValue());
        	value=bd.setScale(decimalPlace,BigDecimal.ROUND_HALF_UP);
			}
			catch(IllegalStateException |NumberFormatException  e){
				log.debug("Error: getValue ColumnIndex={}, error={}",cell.getColumnIndex(),e.getMessage());
				
				throw new ImportException (e);
			}
        	
			break;
		case DateTime:
			value=cell.getDateCellValue();
			 break;
		}
		log.debug("getValue ColumnIndex={}, value={} :",cell.getColumnIndex(),value);
		
	return value;
}
	public static String getValueAsString(Cell cell) {
		String value=null;
		switch (cell.getCellType())
		{
        case Cell.CELL_TYPE_NUMERIC:
        	int decimalPlace=0;
        	BigDecimal bd= new BigDecimal(cell.getNumericCellValue());
        	bd=bd.setScale(decimalPlace,BigDecimal.ROUND_HALF_UP);
        	value=bd.toPlainString();
        	
            break;
        case Cell.CELL_TYPE_STRING:
            value=cell.getStringCellValue() ;
            break;
        case Cell.CELL_TYPE_FORMULA:
        	switch(cell.getCachedFormulaResultType()) {
            case Cell.CELL_TYPE_NUMERIC:
            	BigDecimal bd1= new BigDecimal(cell.getNumericCellValue());
            	bd1=bd1.setScale(0,BigDecimal.ROUND_HALF_UP);
            	value=bd1.toPlainString();
                break;
            case Cell.CELL_TYPE_STRING:
            	value=cell.getStringCellValue() ;
                break;
        }
            break;
		default:
			break;
		}
		log.debug("### getValueAsString: type= {}, value={} ###",cell.getCellType(),value);
		return value;
	}
	public static boolean isEmptyRow(Row row) {
		boolean isEmpty = true;
		DataFormatter dataFormatter = new DataFormatter();

		if (row != null) {
			for (Cell cell : row) {
				if (dataFormatter.formatCellValue(cell).trim().length() > 0) {
					isEmpty = false;
					break;
				}
			}
		}

		return isEmpty;
	}
	public static boolean isXls(String fileName) {
		return "xls".equalsIgnoreCase(FileHelper.getFileExtension(fileName));
	}


}
