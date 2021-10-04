package fr.symphonie.budget.ui.beans.converter;

import java.util.StringTokenizer;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import fr.symphonie.budget.core.model.pluri.ComplexEntity;


@FacesConverter("groupDestNat.converter")
public class GroupDestNatConverter implements Converter {
//	private static final Logger logger = LoggerFactory.getLogger(GroupDestNatConverter.class);
	
	private static final String SEPARATOR = ":";
	@Override
	public Object getAsObject(FacesContext context, UIComponent component,
			String value) {
	
		
		ComplexEntity item=null;
		try{
			item=createItem(value);
			if(item==null)return null;
		}catch(Exception e){
			e.printStackTrace();
		}
		return item;
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component,
			Object value) {
		String retour="";
		if(value==null)return null;
		if((value!=null) &&(value instanceof ComplexEntity)){
			ComplexEntity item = (ComplexEntity)value;
			if((item.getEntity1().getCode()==null)&&(item.getEntity2().getCode()==null))return null;
			retour=(item.getEntity1().getCode())+SEPARATOR+(item.getEntity2().getCode());
		if(item.getEntity1().getAdditionalValue()!=null)	retour=retour+SEPARATOR+(item.getEntity1().getAdditionalValue());
			
		}

		return retour;
	}
	private ComplexEntity createItem(String value) {
         
		ComplexEntity item=null;
		if(value==null)return null;
		if (value.trim().isEmpty()) return null;
		if(!value.contains(SEPARATOR))return null;
		String[] tokns=new String[5];	
		StringTokenizer st = new StringTokenizer(value, SEPARATOR);
		int i=0;
		while (st.hasMoreElements()) {
			tokns[i]=(String) st.nextElement();
			i++;
		}
		item=new ComplexEntity();
		item.getEntity1().setCode(tokns[0]);
		item.getEntity2().setCode(tokns[1]);
		item.getEntity1().setAdditionalValue(tokns[2]);
		
		return item;
	}
}
