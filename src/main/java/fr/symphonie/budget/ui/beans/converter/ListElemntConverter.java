package fr.symphonie.budget.ui.beans.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@FacesConverter("listElemntConverter")
public class ListElemntConverter implements Converter {
//	private static final String SEPARATOR = "-";
	/**
	 * Logger for this class
	 */
	private static final Logger logger = LoggerFactory.getLogger(ListElemntConverter.class);

	@Override
	public Object getAsObject(FacesContext context, UIComponent component,
			String value) {
		if (logger.isDebugEnabled()) {
			logger.debug("getAsObject(value="+value+") - start"); //$NON-NLS-1$
		}
		return null;
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component,
			Object value) {
		if (logger.isDebugEnabled()) {
			logger.debug("getAsString(value="+value+") - start"); //$NON-NLS-1$
		}

		String envelopStr="";
		if(value==null)return null;

		if (logger.isDebugEnabled()) {
			logger.debug("getAsString("+envelopStr+") - end"); //$NON-NLS-1$
		}
		return envelopStr;
	}
}
