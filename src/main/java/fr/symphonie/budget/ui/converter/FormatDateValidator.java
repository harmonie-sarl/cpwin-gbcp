package fr.symphonie.budget.ui.converter;

import java.util.Date;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



@FacesValidator(value = "validator.format.date")
public class FormatDateValidator implements Validator {

	/**
	 * Logger for this class
	 */
	private static final Logger logger = LoggerFactory
			.getLogger(FormatDateValidator.class);

	@Override
	public void validate(FacesContext context, UIComponent component,
			Object value) throws ValidatorException {
		Date date = (Date) value;
		logger.debug("date:" + date);
	}

}
