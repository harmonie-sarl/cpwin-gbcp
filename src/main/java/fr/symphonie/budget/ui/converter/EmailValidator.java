package fr.symphonie.budget.ui.converter;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

import fr.symphonie.common.util.MsgEntry;
import fr.symphonie.util.HandlerJSFMessage;


@FacesValidator(value = "validator.email")
public class EmailValidator  implements Validator {
	public static final String EMAIL_REGEX_VALIDATION="^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
	@Override
	public void validate(FacesContext facesContext, UIComponent uIComponent, Object object) throws ValidatorException{
		String enteredEmail = (String)object;
//		Pattern p = Pattern.compile(".+@.+\\.[a-z]+");
//		Matcher m = p.matcher(enteredEmail);
//		boolean matchFound = m.matches();
		
		boolean matchFound =validate(enteredEmail);
		if (!matchFound) {
			throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "", HandlerJSFMessage.getErrorMessage(MsgEntry.CPWIN_EMAIL_INVALIDE)));
		}
	}
	
	public static boolean validate(String email) {
		Pattern p = Pattern.compile(EMAIL_REGEX_VALIDATION);
		Matcher m = p.matcher(email);
		return  m.matches();
	}

}
