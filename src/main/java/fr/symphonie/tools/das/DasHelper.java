package fr.symphonie.tools.das;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

//import org.apache.logging.log4j.util.Strings;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.symphonie.common.util.Constant;
import fr.symphonie.common.util.MsgEntry;
import fr.symphonie.common.util.Util;
import fr.symphonie.cpwin.model.Adresse;
import fr.symphonie.cpwin.model.Tiers;
import fr.symphonie.impor.AttributImport;
import fr.symphonie.tools.das.Das2FileStruct.ILine;
import fr.symphonie.tools.das.model.AdresseDas;
import fr.symphonie.tools.das.model.Honoraire;
import fr.symphonie.tools.das.model.TiersDas;
import fr.symphonie.tools.das.model.TypeDasEnum;
import fr.symphonie.tools.das.model.TypeVoieEnum;
import fr.symphonie.util.HandlerJSFMessage;
import fr.symphonie.util.Helper;
import fr.symphonie.util.model.SimpleEntity;



public class DasHelper {
	private static final Logger logger = LoggerFactory.getLogger(DasHelper.class);
	/**
	 * Permet la chargement du fichier Excel des tiers DAS
	 * @param inputFile chemin du fichier Excel des tiers DAS
	 * @return list de SimpleEntity
	 */
	public static List<SimpleEntity> importTiersList(String inputFile) {
		logger.debug("importTiersList : inputFile={} ",inputFile);
		List<SimpleEntity> result = new ArrayList<>();
		POIFSFileSystem fs =null;
		HSSFWorkbook wb =null;
		int rowCount=0;
		try (InputStream input = new FileInputStream(inputFile);)
		{
		
			fs = new POIFSFileSystem(input);
			wb = new HSSFWorkbook(fs);
			HSSFSheet sheet = wb.getSheetAt(Constant.NO_TIERS_SHEET);
			Iterator<Row> rows = sheet.rowIterator();
			SimpleEntity data = null;
		
			while (rows.hasNext()) {
				rowCount++;
				HSSFRow row = (HSSFRow) rows.next();
				data = parseTiers(row);
				if (data != null)
					result.add(data);
			}
            
		} catch (IOException e) {
			e.printStackTrace();
		} 
		
		if((rowCount!=0)&&(result.isEmpty()))HandlerJSFMessage.addError(MsgEntry.NO_VALID_DATA_FILE_MSG);

		logger.debug("importTiersList : size={} ",result.size());
		
		return result;
	}
	public static List<Honoraire> importHonoraires(String inputFile) {
		logger.debug("importHonoraires : inputFile={} ",inputFile);
		List<Honoraire> result = new ArrayList<>();
		POIFSFileSystem fs =null;
		HSSFWorkbook wb =null;
		int rowCount=0;
		try (InputStream input = new FileInputStream(inputFile);)
		{
			fs = new POIFSFileSystem(input);
			wb = new HSSFWorkbook(fs);
			HSSFSheet sheet = wb.getSheetAt(Constant.NO_HONORAIRES_SHEET);
			Iterator<Row> rows = sheet.rowIterator();
			Honoraire data = null;
			while (rows.hasNext()) {
				rowCount++;
				HSSFRow row = (HSSFRow) rows.next();
				data = parseHonoraire(row);
				if (data != null)
					result.add(data);
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
		if((rowCount!=0)&&(result.isEmpty()))HandlerJSFMessage.addError(MsgEntry.NO_VALID_DATA_FILE_MSG);
		logger.debug("importHonoraires : size={} ",result.size());
		
		return result;
	}
	private static Honoraire parseHonoraire(HSSFRow row) {
		Honoraire ligne = null;
		int START_INDEX=0,END_INDEX=23;
		String cellValue=null;
		if(!isValidRow(row))return null;
		ligne = new Honoraire();
		for(int index=START_INDEX;index<END_INDEX;index++){
			Cell cell=row.getCell(index);
			if(cell!=null)	cellValue=getValueAsString(cell);
			else cellValue=null;
			logger.debug("parseHonoraire: index={}, value={}",index,cellValue);
			ligne.setValue(index,cellValue);
			
		}
		logger.debug("parseHonoraire: {}",ligne);
		
		return ligne;
	}
	private static SimpleEntity parseTiers(HSSFRow row) {
		SimpleEntity tiers = null;
		int START_INDEX=0,END_INDEX=2;
		String cellValue=null;
		if(!isValidRow(row))return null;
		tiers = new SimpleEntity();
		for(int index=START_INDEX;index<END_INDEX;index++){
			Cell cell=row.getCell(index);
			if(cell!=null)	cellValue=getValueAsString(cell);
			else cellValue=null;
//			logger.debug("parseTiers: index={}, value={}",index,cellValue);
			setTiersValues(index,cellValue,tiers);
			
		}
		return tiers;
	}
	private static void setTiersValues(int index, String value, SimpleEntity data) {
		TiersStruct s=TiersStruct.parse(index);
		switch(s){
		case CODE:
			data.setCode(value);
			break;
		case TYPE:
			data.setDesignation(value);
			break;
		}
		
	}
	private static boolean isValidRow(HSSFRow row){
		logger.debug("isValidRow rowNum={}",row.getRowNum());
		HSSFCell firstCell = row.getCell(0);
		if(firstCell==null)return false;
		return (HSSFCell.CELL_TYPE_NUMERIC== firstCell.getCellType());
	}
	private static String getValueAsString(Cell cell) {
		String value=null;
		switch (cell.getCellType())
		{
        case HSSFCell.CELL_TYPE_NUMERIC:
//        	DataFormatter formatter = new DataFormatter();        	
//        	value= formatter.formatCellValue(cell);
        	int decimalPlace=(cell.getColumnIndex()==0)?0:2;
        	BigDecimal bd= new BigDecimal(cell.getNumericCellValue());
        	bd=bd.setScale(decimalPlace,BigDecimal.ROUND_HALF_UP);
        	value=bd.toPlainString();
        	
            break;
        case HSSFCell.CELL_TYPE_STRING:
            value=cell.getStringCellValue() ;
            break;
		default:
			break;
		}
		return value;
	}
//	private static Object getValue(Cell cell) {
//		Object value=null;
//		
//		switch (cell.getCellType())
//		{
//        case HSSFCell.CELL_TYPE_NUMERIC:
//        	value=cell.getNumericCellValue() ;
//            break;
//        case  HSSFCell.CELL_TYPE_STRING:
//            value=cell.getStringCellValue() ;
//            break;
//		default:
//			break;
//		}
//		logger.debug("getValue: index={}/{} --> type={}, value={}",cell.getColumnIndex(),cell.getRowIndex(),cell.getCellType(),value);
//		return value;
//	}
	public enum TiersStruct {
		CODE(0),
		TYPE(1)	;
		
		int position;
		TiersStruct(int pos){
			this.position=pos;
		}
		public static TiersStruct parse(int pos){
			for(TiersStruct s:TiersStruct.values()){
				if(s.position==pos) return s;
			}
			
			return null;
		}
	}
	public static TiersDas convert(Tiers cpwinTiers,TypeDasEnum type) {
		TiersDas tiersDas=new TiersDas(cpwinTiers.getCode());
		tiersDas.setTrace(Helper.createTrace());
		//tiersDas.setSiren(cpwinTiers.getNumSiren());
		tiersDas.setSiret(cpwinTiers.getSiret());
		tiersDas.setAdresse(convertAdresse(cpwinTiers.getAdresse()));
		String nom=null;
		String[] blocs=null;
		if(type!=null){
			tiersDas.setType(type);
		switch (type) {
		case RS:
			tiersDas.setRs(cpwinTiers.getNom());			
			break;
		case NP:
			if((StringUtils.isBlank(cpwinTiers.getNomPersonne()))&&(StringUtils.isBlank(cpwinTiers.getPrenom())))
			{			
				try
				{
				blocs=cpwinTiers.getNom().split(" ");
				tiersDas.setNom(blocs[0]);
				tiersDas.setPrenom(blocs[1]);	
				}
				catch(Exception e){}
				
			}
			else
			{
			nom=StringUtils.isBlank(cpwinTiers.getNomPersonne())?cpwinTiers.getNom():cpwinTiers.getNomPersonne();
			tiersDas.setNom(nom);			
			tiersDas.setPrenom(cpwinTiers.getPrenom());	
			}
			break;
		}
		}
		return tiersDas;
	}
	private static AdresseDas convertAdresse(Adresse adresse) {
		AdresseDas adrDas=new AdresseDas();
		String[] adrs=getAdresseCandidate(adresse);
		adrDas.getVoie().setNumero(getNumVoie(adrs));
		adrDas.getVoie().setNatureEtNom(getNatureEtNomVoie(adrs));
		adrDas.setCodePostale(adresse.getCodePostale());
		adrDas.setBureau(adresse.getBureauPostale());
		
		return adrDas;
	}
	private static String[] getAdresseCandidate(Adresse adr){
		String[] retour=new String[2];
		String regContainsDigits=".*\\d+.*";
		if ((adr.getAdresse1()!=null)&&(!adr.getAdresse1().isEmpty())&&(adr.getAdresse1().matches(regContainsDigits)) ){
			if( Character.isDigit(adr.getAdresse1().charAt(0))) retour[0]=adr.getAdresse1();
			else retour[1]=adr.getAdresse1();		
		}
		else if ((adr.getAdresse2()!=null)&&(!adr.getAdresse2().isEmpty())&&(adr.getAdresse2().matches(regContainsDigits))  ){
			if( Character.isDigit(adr.getAdresse2().charAt(0))) retour[0]=adr.getAdresse2();
			else retour[1]=adr.getAdresse2();
			
		}
		return retour;
	}
	public static String getNumVoie(String[] adrs){
		String nVoie=null;
		String strategie1=adrs[0];
		String strategie2=adrs[1];
		
		String nVoieOrigin = null;
		String[] nVoieparts = null;
		String nVoie1 = null;
		String nVoie2 = null;
		Integer nVoieFinal =null;
	
		if(strategie1!=null) nVoie=Util.getFirstNumeric(strategie1);
		else if(strategie2!=null) nVoie=Util.getFirstNumeric(strategie2);
		if(nVoie!=null){
			if(nVoie.endsWith("-"))nVoie=nVoie.replace("-", "");
		}
		try {
			nVoieOrigin=nVoie;
			nVoieparts=nVoieOrigin.split("-");
			nVoie1 = nVoieparts[0].trim();
			nVoie2 = nVoieparts[1].trim();		
			nVoieFinal=Integer.min(Integer.parseInt(nVoie1), Integer.parseInt(nVoie2));			
			return Integer.toString(nVoieFinal);
		} catch (Exception e) {
			return nVoie;
		}
	
	}
	public static String getNatureEtNomVoie(String[] adrs){
		String adresse=null;
		//String[] adrs=getAdresseCandidate(adr);
		String strategie1=adrs[0];
		String strategie2=adrs[1];
		if(strategie1!=null)adresse=strategie1;
		else if(strategie2!=null)adresse=strategie2;
		
		if(adresse!=null){
		adresse=adresse.replaceAll("[0-9]", "");
		adresse=adresse.replaceAll("bis", "");
		adresse=adresse.replaceAll(",", "");
		adresse=adresse.trim();
		}
		if(adresse!=null){
			if(adresse.startsWith("-"))adresse=adresse.replaceAll("-", "");
			if(adresse.startsWith("/"))adresse=adresse.replaceAll("/", "");
			adresse=adresse.trim();
			adresse=formatNatureEtNomVoie(adresse);
		}
		
		return adresse;
	}
	private static String formatNatureEtNomVoie(String adresse ) {
		String retour = adresse;
		if (retour.length() <= 26)
			return retour;
		retour = formatSquare(retour);
		if (retour.length() <= 26)
			return retour;
		retour = formatArticle(retour);
		if (retour.length() <= 26)
			return retour;
		retour = formatArticle2(retour);
		return retour;
	}
	
	private static String formatSquare(String adresse) {
		return formatNatureEtNomVoie(adresse, 0);
	}

	private static String formatArticle(String adresse) {
		return formatNatureEtNomVoie(adresse, 1);
	}

	private static String formatArticle2(String adresse) {
		return formatNatureEtNomVoie(adresse, 2);
	}
	
	private static String formatNatureEtNomVoie(String adresse, int niveau) {
		String retour = "";
		String[] blocs = adresse.split(" ");
		TypeVoieEnum type = TypeVoieEnum.parse(blocs[niveau]);
		if (type != null) {
			blocs[niveau] = type.getAbreviation();
		}
		for (String s : blocs) {
			if (s.trim().isEmpty())
				continue;
			retour += s + " ";
		}
		return retour.trim();
	}
	
	public static List<String> getCodesTiers(List<Honoraire> list) {
		List<String> codeList=new ArrayList<>();
		for(Honoraire s:list ){
			codeList.add(s.getBeneficiaire().getNumero());
		}
		return codeList;
	}
	public static String display(ILine enregistrement) {
		StringBuilder retour=new StringBuilder("");
		List<AttributImport> list=Arrays.asList(enregistrement.getAttributs());
		Comparator<AttributImport> c=new Comparator<AttributImport>(){
			@Override
			public int compare(AttributImport o1,AttributImport o2) {
				 return o1.getIndex().compareTo(o2.getIndex());
			}
			};			
		
		list.sort(c);
		for(AttributImport attr:list){
			retour.append(attr.display());
		}
		
		
		return retour.toString();
	}
}
