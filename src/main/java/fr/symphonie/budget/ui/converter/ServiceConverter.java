package fr.symphonie.budget.ui.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.convert.FacesConverter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.symphonie.budget.core.service.IGestionTiersService;
import fr.symphonie.budget.ui.converter.ServiceConverter;
import fr.symphonie.common.util.MsgEntry;
import fr.symphonie.cpwin.model.Service;
import fr.symphonie.util.HandlerJSFMessage;
import fr.symphonie.util.Helper;

@FacesConverter(value = "convert.service")
public class ServiceConverter implements Converter {

	/**
	 * Logger for this class
	 */
	private static final Logger logger = LoggerFactory.getLogger(ServiceConverter.class);

	@Override
	public Object getAsObject(FacesContext context, UIComponent component,
			String value) {	

		try {

			Service se = null;

			if ((!value.equals("")) && (!value.equals(" "))) {
				IGestionTiersService gestionTiersService = (IGestionTiersService) Helper
						.findBean("gestionTiersService");
				se = gestionTiersService.getService(value);

			}
			if (se == null) {
				se = new Service();
//				se.setCode(value);
			}

//			if (logger.isDebugEnabled()) {
//				logger.debug("convert.service:getAsObject - end"); //$NON-NLS-1$
//			}
//			logger.info("public Object getAsObject(FacesContext context, UIComponent component,String value)  se.getCode()="+se.getCode());
			return se;
		} catch (Exception ex) {

			logger.error("Exception",ex);
			throw new ConverterException(HandlerJSFMessage.createFacesMessage(MsgEntry.SERVICE_CONVERSION_ERROR));
		}

	}

	@Override
	public String getAsString(FacesContext context, UIComponent component,
			Object value) {
		Service se = (Service) value;
		return se.getCode();
	}
}



