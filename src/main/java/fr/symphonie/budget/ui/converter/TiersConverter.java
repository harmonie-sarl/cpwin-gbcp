package fr.symphonie.budget.ui.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.convert.FacesConverter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.symphonie.common.util.BudgetHelper;
import fr.symphonie.common.util.MsgEntry;
import fr.symphonie.common.util.Util;
import fr.symphonie.cpwin.model.Tiers;
import fr.symphonie.util.HandlerJSFMessage;
import fr.symphonie.util.model.SimpleEntity;
@FacesConverter(value = "convert.tiers")
public class TiersConverter  implements Converter{
	private static final Logger logger = LoggerFactory.getLogger(TiersConverter.class);

		
	@Override
	public Object getAsObject(FacesContext context, UIComponent component, String value) {
		Tiers tiers = null;
		String code=null;
		logger.debug("getAsObject input value='{}'",value);
		if (value == null)	return null;			
			
		try {
			code=Util.split(value);
			if(code!=null) {
				tiers = BudgetHelper.getTiersService().findTiers(code);
			}
//			if (!value.trim().isEmpty()) {
//				if (value.contains("-")) {
//					code=value.substring(0, value.indexOf("-"));
//					tiers = BudgetHelper.getTiersService().findTiers(code);
//				}		
				
//				if (tiers == null)
//					logger.debug("getAsObject tiers {} not found" ,code);
//				else
//					logger.debug("getAsObject tiers trouvé {}",tiers);
//				 if (tiers != null) {
//					return tiers;
//				}
//			}
		} catch (Exception ex) {
			ex.printStackTrace();
			logger.error("getAsObject: Converter Exception:value=" + value + ", error="
					+ ex.getMessage());
			throw new ConverterException(
					HandlerJSFMessage
							.createFacesMessage(MsgEntry.TIERS_CONVERSION_ERROR));
		}
		logger.debug("getAsObject output tiers {} for code '{}'",tiers,code);
		return tiers;
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component,
			Object value) {
		logger.debug("getAsString input value={}",value);
		String retour=null;
		if(value instanceof Tiers){
			retour=((Tiers) value).getCode();
		}		
		else if(value instanceof SimpleEntity){
			retour=((SimpleEntity) value).getCode();
		}
		logger.debug("getAsString retour={}",retour);
		return retour;
		
	
	}
}



