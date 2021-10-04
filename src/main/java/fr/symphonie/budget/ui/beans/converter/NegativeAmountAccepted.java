package fr.symphonie.budget.ui.beans.converter;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.convert.FacesConverter;

import fr.symphonie.common.util.MsgEntry;
import fr.symphonie.util.HandlerJSFMessage;

@FacesConverter(value="convert.negativeAmountAccepted")
public class NegativeAmountAccepted implements Converter{
	/**
	 * Logger for this class
	 */
//	private static final Logger logger = LoggerFactory.getLogger(MontantWithNullConverter.class);

	@Override
	public Object getAsObject(FacesContext context, UIComponent component,
			String value) {

		if(isMontantValide(value)){
			Object returnObject = Double.valueOf(value).doubleValue();
			return returnObject;
		}
		
		throw new ConverterException(new FacesMessage(FacesMessage.SEVERITY_ERROR,
										"",
										HandlerJSFMessage.getErrorMessage(MsgEntry.MNT_CONVERT_ERR_MSG)));
		
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component,
			Object value) {
		double mnt=((Double)value).doubleValue();
		String mntString=String.valueOf(mnt);
		int indexOfpoint=mntString.indexOf(".");
		
		String afterPoint="";
		
		if(mntString!=null){
		 afterPoint=mntString.substring(indexOfpoint);
		}
		
		if(afterPoint!=null&&!"".equals(afterPoint)){
			if(afterPoint.length()==2)
			mntString=mntString+"0";
		}
		return mntString;
	}
	
	/**
	 * test if format montant is valide.
	 * @param obj
	 * @return true si la valeure de value est un montant valide
	 */
	private boolean isMontantValide(String value){
		

		String expressionReg="^(\\+|-)?[0-9]*(\\.[0-9]{1,2})?";
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
		
		return true;
	}
}