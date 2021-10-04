package fr.symphonie.budget.ui.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.convert.FacesConverter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import fr.symphonie.cpwin.model.Banque;


@FacesConverter(value = "convert.etablissement")
public class EtablissementConverter implements Converter{

	/**
	 * Logger for this class
	 */
	private static final Logger logger = LoggerFactory
			.getLogger(EtablissementConverter.class);

	@Override
	public Object getAsObject(FacesContext context, UIComponent component,
			String value) {

		try {
			logger.debug("EtablissementConverter: getAsObject() - value: " + value);
			
			return new Banque();
		}
		catch(ConverterException ce){
			logger.error(ce.getMessage());
			throw ce;
		}
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component,
			Object value) {
		logger.debug("EtablissementConverter: getAsString() - object: " + value);
		return "";
	}

}

