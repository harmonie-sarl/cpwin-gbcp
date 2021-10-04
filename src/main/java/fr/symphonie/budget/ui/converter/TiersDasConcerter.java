package fr.symphonie.budget.ui.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.convert.FacesConverter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.symphonie.common.util.MsgEntry;
import fr.symphonie.tools.das.core.DasService;
import fr.symphonie.tools.das.model.TiersDas;
import fr.symphonie.util.HandlerJSFMessage;
import fr.symphonie.util.Helper;
@FacesConverter(value = "convert.tiersDas")
public class TiersDasConcerter  implements Converter{
	private static final Logger logger = LoggerFactory.getLogger(TiersDasConcerter.class);

	@Override
	public Object getAsObject(FacesContext context, UIComponent component, String value) {
		TiersDas tiers = null;
		
		DasService service=(DasService)Helper.findBean("dasService");
		
		if (value == null)
			return null;
		try {
			if (!value.trim().isEmpty()) {
				if (value.contains("-")) {
					value = value.substring(0, value.indexOf("-"));
				}		
				tiers = service.findTiers(value);
			      if (tiers != null) {
					return tiers;
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			logger.error("Converter Exception:value=" + value + ", error="
					+ ex.getMessage());
			throw new ConverterException(
					HandlerJSFMessage
							.createFacesMessage(MsgEntry.TIERS_CONVERSION_ERROR));
		}
		return tiers;
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component, Object value) {
		String retour = null;
	
			retour = ((TiersDas) value).getNumero();
		
		return retour;
	}

}
