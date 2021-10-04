package fr.symphonie.common.util;

import java.io.ByteArrayInputStream;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

import javax.faces.context.FacesContext;
import javax.faces.event.PhaseId;

import org.apache.commons.lang.RandomStringUtils;
import org.apache.logging.log4j.util.Strings;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.symphonie.budget.core.model.edition.util.DataItem;
import fr.symphonie.util.HandlerJSFMessage;
import fr.symphonie.util.Helper;

public class Util {
	private static final Logger logger = LoggerFactory.getLogger(Util.class);
	public static String[] getNumerics(String str){
		str = str.replaceAll("[^-?0-9]+", ""); 
	    return str.trim().split(" ");
	}
	public static String getFirstNumeric(String str){
		return getNumerics(str)[0];
	}
	public static BigDecimal arrondisAEuro(String d) {

		int decimalPlace=0;
		BigDecimal bd = null;
		try{
         bd = new BigDecimal(d);
        bd = bd.setScale(decimalPlace, BigDecimal.ROUND_HALF_UP);
		}catch(Exception e)	{
			  bd = new BigDecimal(0);
		}
        return bd;
    }
	public static BigDecimal arrondisAEuro(double d) {

		int decimalPlace=0;
		BigDecimal bd = new BigDecimal(d);
		
        bd = bd.setScale(decimalPlace, BigDecimal.ROUND_HALF_UP);
		
        return bd;
    }
	public static String format(BigDecimal bd) {
		Double value=bd.doubleValue();
		Locale langue = Locale.FRENCH;
		NumberFormat nf = NumberFormat.getNumberInstance(langue);
		DecimalFormat myFormatter = (DecimalFormat) nf;
		myFormatter.applyPattern("#####0");
		return myFormatter.format(value);
	}
	
	public static boolean compareCurrency(double c1,double c2){
		boolean result=false;
		result= Helper.round(c1, 2)==Helper.round(c2, 2);
		//logger.debug(" compareCurrency : c1={}, c2={} --> {}",c1,c2,result);
		return result;
	}
	public static  BigDecimal formatMontant(String value) {
		BigDecimal bd=new BigDecimal(0);
		if(Strings.isBlank(value)) return bd;		
		
		try{
			bd=arrondisAEuro(value);
		}catch(Exception e){
		
		}
		
		return bd;
	}
	public static List<DataItem> copy(List<DataItem> items) {
		 List<DataItem> copy=new ArrayList<>();
		 for(DataItem item:items)
			try {
				copy.add((DataItem) item.clone());
			} catch (CloneNotSupportedException e) {
				e.printStackTrace();
			}
		return copy;
	}
	public static String getErrorMessageFromException(String genericKeyMsg, Exception e) {

		Throwable cause = null;
		String errorMsg=null, errorMsg1 = null,errorMsg2 = null,errorMsg3 = null;
		if (e != null) {
			cause = e;
			errorMsg1 = cause.getMessage();
			if (cause.getCause() != null) {
				cause = cause.getCause();
				errorMsg2 =  cause.getMessage();
				if (cause.getCause() != null) {
					cause = cause.getCause();
					errorMsg3 =  cause.getMessage();
				}
			}
		}
		logger.debug("errorMsg3 : {}",errorMsg3);
		logger.debug("errorMsg2 : {}",errorMsg2);
		logger.debug("errorMsg1 : {}",errorMsg1);
		errorMsg=errorMsg3!=null?errorMsg3:(errorMsg2!=null?errorMsg2:errorMsg1);
		if (genericKeyMsg != null)
			return HandlerJSFMessage.getErrorMessage(genericKeyMsg) + ": " + errorMsg;
		else
			return errorMsg;
	}
	public static int getDayOfYear(Date date) {
		Calendar calendar=Calendar.getInstance();
		calendar.setTime(date);
		return calendar.get(Calendar.DAY_OF_YEAR);
	}
	public static String formatDate(Date input, String pattern) {
		//"yyyy-MM-dd"
		SimpleDateFormat format = new SimpleDateFormat(pattern);
		return format.format(input);
	}
	public static Object getYear(Date date) {
		Calendar calendar=Calendar.getInstance();
		calendar.setTime(date);
		return calendar.get(Calendar.YEAR);
	}
	public static String controlSpecial(String str){
		if(str==null) return str;
		String formatedStr=str;
		Map<String,String> map=getSpecialCaracteres();
		for(String key:map.keySet()){
			formatedStr=formatedStr.replaceAll(key,map.get(key));
			formatedStr=formatedStr.replaceAll(key.toUpperCase(),map.get(key).toUpperCase());
		}

		formatedStr=formatedStr.replaceAll("[^a-zA-Z0-9/\\-?:().,'+\\s]","");
		return formatedStr;
		
	}
	public static String removeNotSpecialCharacters(String str) {
		if(str==null) return str;
		return str.replaceAll("[^a-zA-Z0-9/\\-?:().,'+\\s]","");
	}
	
	private static Map<String,String> getSpecialCaracteres(){
		Map<String,String> CARACTERES_SPECIAL=new Hashtable<String, String>();
		CARACTERES_SPECIAL.put("ö", "o");CARACTERES_SPECIAL.put("ô", "o");
		CARACTERES_SPECIAL.put("é", "e");CARACTERES_SPECIAL.put("è", "e");
		CARACTERES_SPECIAL.put("ê", "e");
		CARACTERES_SPECIAL.put("ä", "a");CARACTERES_SPECIAL.put("â", "a");CARACTERES_SPECIAL.put("à", "a");
		CARACTERES_SPECIAL.put("ß", "s");
		CARACTERES_SPECIAL.put("û", "u");CARACTERES_SPECIAL.put("ü", "u");
		CARACTERES_SPECIAL.put("ç", "c");
		return CARACTERES_SPECIAL;
		
	}
	public static String formatCurrencyWithoutSeparator(BigDecimal bd) {
		if(bd==null)bd=BigDecimal.ZERO;
		return String.format("%.0f", bd.doubleValue() * 100);
	}
	public static String generateCommonLangPassword() {
	    String upperCaseLetters = RandomStringUtils.random(2, 65, 90, true, true);
	    String lowerCaseLetters = RandomStringUtils.random(2, 97, 122, true, true);
	    String numbers = RandomStringUtils.randomNumeric(2);
	    String specialChar = RandomStringUtils.random(2, 33, 47, false, false);
	    String totalChars = RandomStringUtils.randomAlphanumeric(2);
	    String combinedChars = upperCaseLetters.concat(lowerCaseLetters)
	      .concat(numbers)
	      .concat(specialChar)
	      .concat(totalChars);
	    List<Character> pwdChars = combinedChars.chars()
	      .mapToObj(c -> (char) c)
	      .collect(Collectors.toList());
	    Collections.shuffle(pwdChars);
	    String password = pwdChars.stream()
	      .collect(StringBuilder::new, StringBuilder::append, StringBuilder::append)
	      .toString();
	    return password;
	}
	public static StreamedContent getPdfStream(byte[] content, String fileName,String contentType ) {

		StreamedContent stream=new DefaultStreamedContent();
		FacesContext context = FacesContext.getCurrentInstance();
		if (context.getCurrentPhaseId() == PhaseId.RENDER_RESPONSE) {
            // So, we're rendering the HTML. Return a stub StreamedContent so that it will generate right URL.
            return new DefaultStreamedContent();
        } else {
        	if(content==null)return new DefaultStreamedContent();
        	else {
        		stream= new DefaultStreamedContent(new ByteArrayInputStream(content), "application/pdf",fileName);
        	}
        	
        }
//		logger.debug("getPdfStream: end");
        return stream;		
	}
	
	public static String split(String inputStr, String splitCharacter,int index) {
		String result=null;
		try {
		String[] splits=inputStr.split(splitCharacter);
		 result=splits[index].trim();
		}
		catch(Exception e) {
			
		}
		return result;
		
	}
	public static String split(String inputStr) {
		
		return split(inputStr,"-",0);
		
	}
}
