package fr.symphonie.tools.common;

import java.util.Optional;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.convert.FacesConverter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.symphonie.common.util.BudgetHelper;
import fr.symphonie.util.model.User;
@FacesConverter(value = "convert.user")
public class UserConverter implements Converter {
	Logger logger=LoggerFactory.getLogger("signature-tools");
	@Override
	public Object getAsObject(FacesContext context, UIComponent component, String value) {
User result=null;
		
		if(value != null && value.trim().length() > 0) {
			 try {
			String[] splits=value.split("-");
			String codeUser=splits[0].trim();
                Optional<User> item= BudgetHelper.getSignatureBean().getAllUsers().stream()
                		.filter(p->p.getLogin().equals(codeUser)).findFirst();
                if(item.isPresent()) {
                	result=item.get();
                	
                }
       
            } catch(Exception e) {
                throw new ConverterException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "Conversion Error", "Not a valid theme."));
            }
        }
		logger.debug("getAsObject:input value={} -> result={}",value,result);
		return result;
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component, Object value) {
		String result=null;
		if(value != null) {
			User user=(User)value;
            result= String.format("%s - %s", user.getLogin(),user.getLastName());
           
        }
		 logger.debug("getAsString:input value={} -> result= {}",value,result);
		return result;
	}

}
