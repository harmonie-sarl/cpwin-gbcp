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


@FacesConverter("listProgramConverter")
public class ListProgramConverter implements Converter{
//	private static final String SEPARATOR = "-";
	
	private static final Logger logger = LoggerFactory.getLogger(ListProgramConverter.class);

	@Override
	public Object getAsObject(FacesContext context, UIComponent component,
			String value) {
		SimpleEntity programme=new SimpleEntity("","");
//		if (logger.isDebugEnabled()) {
//			logger.debug("getAsObject(value="+value+") - start"); //$NON-NLS-1$
//		}
		if(value==null)return programme;
		if (value.trim().isEmpty()) return programme;
		
		try{
			programme=createItem(value);
		if(programme==null)return new SimpleEntity("","");
		  programme = findProgram(programme);
//		  if (logger.isDebugEnabled()) {
//				logger.debug("getAsObject: programme="+programme+" - end"); //$NON-NLS-1$
//			}
		}
		catch(Exception e){
			logger.error("getAsObject: Exception: ",e);
			e.printStackTrace();
		}
		  
		return programme;
	}

	private SimpleEntity createItem(String value) {
		
//		if (logger.isDebugEnabled()) {
//			logger.debug("createItem(value="+value+") - start"); //$NON-NLS-1$
//		}

		if(value==null)return null;
		if (value.trim().isEmpty()) return null;
		SimpleEntity item=new SimpleEntity(value,"");
	
//		if (logger.isDebugEnabled()) {
//			logger.debug("createItem(retour="+item+") - end"); //$NON-NLS-1$
//		}
		return item;
	}
	
	public static SimpleEntity findProgram(SimpleEntity program) {
//		if (logger.isDebugEnabled()) {
//			logger.debug("findProgram("+program+") - start"); //$NON-NLS-1$
//		}
		SimpleEntity returnProgram =null;
try {
	LoaderBean loader =(LoaderBean) Helper.findBean("loaderBean");
	List<SimpleEntity> sourceList=loader.getListPrograme();
	int index=sourceList.indexOf(program);
//	logger.info("findProgram: index="+index);
	if(index<0) return new SimpleEntity("","");
	 returnProgram = sourceList.get(index);
	
} catch (Exception e) {
	logger.error("findProgram: exception: ",e);
//	e.printStackTrace();
}
//if (logger.isDebugEnabled()) {
//	logger.debug("findProgram("+returnProgram+") - end"); //$NON-NLS-1$
//}
		return returnProgram;
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
