package fr.symphonie.budget.ui.beans.converter;

import java.util.List;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.symphonie.budget.ui.beans.LoaderBean;
import fr.symphonie.util.Helper;
import fr.symphonie.util.model.SimpleEntity;


@FacesConverter("listServiceConverter")
public class ListServiceConverter implements Converter{

	
	private static final Logger logger = LoggerFactory.getLogger(ListServiceConverter.class);

	@Override
	public Object getAsObject(FacesContext context, UIComponent component,
			String value) {
		SimpleEntity service=new SimpleEntity("","");
//		if (logger.isDebugEnabled()) {
//			logger.debug("getAsObject(value="+value+") - start"); //$NON-NLS-1$
//		}
		if(value==null)return service;
		if (value.trim().isEmpty()) return service;
		
		try{
			service=createItem(value);
		if(service==null)return new SimpleEntity("","");
		service = findService(service);
//		  if (logger.isDebugEnabled()) {
//				logger.debug("getAsObject: service="+service+" - end"); //$NON-NLS-1$
//			}
		}
		catch(Exception e){
			logger.error("getAsObject: Exception: ",e);
		}
		  
		return service;
	}

	private SimpleEntity createItem(String value) {
		
//		if (logger.isDebugEnabled()) {
//			logger.debug("createItem(value="+value+") - start"); //$NON-NLS-1$
//		}

		if(value==null)return null;
		if (value.trim().isEmpty()) return null;
		SimpleEntity item=new SimpleEntity(value,"");
//	
//		if (logger.isDebugEnabled()) {
//			logger.debug("createItem(retour="+item+") - end"); //$NON-NLS-1$
//		}
		return item;
	}
	

	private SimpleEntity findService(SimpleEntity service) {
//		if (logger.isDebugEnabled()) {
//			logger.debug("findService("+service+") - start"); //$NON-NLS-1$
//		}
		SimpleEntity returnService =null;
try {
	LoaderBean loader =(LoaderBean) Helper.findBean("loaderBean");
	List<SimpleEntity> sourceList=loader.getListService();
	int index=sourceList.indexOf(service);
//	logger.info("findService: index="+index);
	
	 returnService = sourceList.get(index);
	
} catch (Exception e) {
	logger.error("findService: exception: ",e);

}
//if (logger.isDebugEnabled()) {
//	logger.debug("findService("+returnService+") - end"); //$NON-NLS-1$
//}
		return returnService;
	}
	@Override
	public String getAsString(FacesContext context, UIComponent component,Object value) {
		
//		logger.debug("getAsString :value= "+value+" - start");
		String retour=null;
		SimpleEntity item=null;
		if(value==null)return null;
		if((value!=null) &&(value instanceof SimpleEntity)){
			item=(SimpleEntity) value;
			retour=item.getCode();
			
		}
//		logger.debug("getAsString :retour= "+retour+" - end");
		return retour;
	}

}
