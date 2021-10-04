package fr.symphonie.tools.sct;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.symphonie.cpwin.model.sepa.Actor;
@FacesConverter(value = "convert.sct.actor")
public class ActorConverter implements Converter {
	private static final Logger logger =LoggerFactory.getLogger(ActorConverter.class);
	@Override
	public Object getAsObject(FacesContext context, UIComponent component, String value) {
		logger.debug("getAsObject: input String value = {}", value);
		Actor actor = new Actor();
		if (!StringUtils.isBlank(value))
			actor.setName(value);
		return actor;
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component, Object value) {
		logger.debug("getAsString: input Object value = {}",value);
		if(value!=null) {
			if(value instanceof Actor)
			return ((Actor)value).getName();
			else return (String) value;
		}
		return null;
	}

}
