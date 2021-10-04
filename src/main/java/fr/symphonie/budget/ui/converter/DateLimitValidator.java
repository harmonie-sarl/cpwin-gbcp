package fr.symphonie.budget.ui.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@FacesValidator(value = "validator.dateLimite")
public class DateLimitValidator implements Validator{

	/**
	 * Logger for this class
	 */
	private static final Logger logger = LoggerFactory
			.getLogger(DateLimitValidator.class);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * javax.faces.validator.Validator#validate(javax.faces.context.FacesContext
	 * , javax.faces.component.UIComponent, java.lang.Object)
	 */
	@Override
	public void validate(FacesContext context, UIComponent component,
			Object value) throws ValidatorException {
		String dateLimiteStr = (String) value;

		logger.debug("date limite:" + dateLimiteStr);
//		TableauBordBean tableauBordBean = (TableauBordBean) Helper
//				.findBean("tableauBordBean");		

		// Il faute decommenter ce bout de code pour le controle de date > date
		// aujourd'hui
//		if (!Helper.isValideDate(dateLimiteStr, tableauBordBean.getExercice())) {
//			throw new ValidatorException(new FacesMessage(
//					FacesMessage.SEVERITY_ERROR, "",
//					HandlerJSFMessage
//							.getErrorMessage(MsgEntry.CPWIN_DATE_LIMIT_ERROR)));
//		}
	}

}
