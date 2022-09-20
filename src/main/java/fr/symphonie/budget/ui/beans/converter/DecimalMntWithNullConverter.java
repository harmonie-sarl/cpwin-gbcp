package fr.symphonie.budget.ui.beans.converter;

//import org.apache.log4j.Logger;


import java.math.BigDecimal;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.convert.FacesConverter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import fr.symphonie.common.util.MsgEntry;
import fr.symphonie.util.HandlerJSFMessage;

@FacesConverter(value="convert.decimalMntWithNull")
public class DecimalMntWithNullConverter implements Converter{
	/**
	 * Logger for this class
	 */
	private static final Logger logger = LoggerFactory.getLogger(DecimalMntWithNullConverter.class);

	/**
	 * Logger for this class
	 */
@Override
	public Object getAsObject(FacesContext context, UIComponent component,
			String value) {

		
			
		
		if(isMontantValide(value)){
			Object returnObject = new BigDecimal(value);
			
			 logger.info("------public Object getAsObject----returnObject="+returnObject);

			return returnObject;
		}
		
		throw new ConverterException(new FacesMessage(FacesMessage.SEVERITY_ERROR,
										"",
										HandlerJSFMessage.getErrorMessage(MsgEntry.MNT_CONVERT_ERR_MSG)));
		
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component,
			Object value) {
		
	    logger.info("1------public String getAsString-----value="+value);
	    
		BigDecimal mnt=((BigDecimal)value);
	    logger.info("2------public String getAsString-----mnt="+mnt);
	    BigDecimal bd = new BigDecimal(0);
        bd = mnt.setScale(2, BigDecimal.ROUND_HALF_UP);
        logger.info("3------public String getAsString-----bd="+bd);
		String mntString=String.valueOf(bd);
	    logger.info("4------public String getAsString-----mntString="+mntString);
		int indexOfpoint=mntString.indexOf(".");
		 logger.info("5------public String getAsString-----indexOfpoint="+indexOfpoint);
		 
		
//		String afterPoint="";
		
//		if(mntString!=null){
//		 afterPoint=mntString.substring(indexOfpoint);
//		}
		
//		if(afterPoint!=null&&!"".equals(afterPoint)){
//			if(afterPoint.length()==2)
//			mntString=mntString+"0";
//		}
		
		
           logger.info("5------public String getAsString-----mntString="+mntString);

		return mntString;
		
	
	}
	
	/**
	 * test if format montant is valide.
	 * @param obj
	 * @return true si la valeure de value est un montant valide
	 */
	private boolean isMontantValide(String value){
		
		

		String expressionReg="[0-9]*(\\.[0-9]{1,2})?";
		if((value!=null)&&(value.equals("")))return false;
		try {
			Pattern pattern = Pattern.compile(expressionReg);
			Matcher matcher = pattern.matcher(value);
			if(!matcher.matches()){
				throw new Exception();
			}
		} catch (Exception e) {
			
			return false;
		}
		 logger.info("-----private boolean isMontantValide---return true;=");
		return true;
	}

}