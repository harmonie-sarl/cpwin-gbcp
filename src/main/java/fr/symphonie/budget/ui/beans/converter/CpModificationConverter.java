package fr.symphonie.budget.ui.beans.converter;

import java.util.List;
import java.util.StringTokenizer;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import fr.symphonie.budget.core.model.pluri.CpModification;
import fr.symphonie.budget.ui.beans.pluri.BudgetPluriannuelBean;
import fr.symphonie.common.util.BudgetHelper;
import fr.symphonie.util.Helper;

@FacesConverter("cpModification.converter")
public class CpModificationConverter implements Converter {
//	private static final Logger logger = LoggerFactory.getLogger(CpModificationConverter.class);
	
	private static final String SEPARATOR = ":";
	@Override
	public Object getAsObject(FacesContext context, UIComponent component,
			String value) {		
		CpModification item=null;
		try{
			item=createItem(value);			
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
		if((value!=null) &&(value instanceof CpModification)){
			CpModification item = (CpModification)value;
			if(item.getNumero()==null)return null;
			retour=item.getNumero()+SEPARATOR+item.getObjet()+SEPARATOR+item.getTrace().getDateCreationF()+SEPARATOR+item.getTrace().getAuteurCreation();
		
		}
		
		return retour;
	}
	private CpModification createItem(String value) {
		BudgetPluriannuelBean bean=BudgetHelper.getBpBean(); 
		CpModification item=null;
		List<CpModification> list=null;
		Integer numero=null;
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
		numero=Helper.stringToInt(tokns[0]);
		list =bean.getCpModificationList();
		for (CpModification cpModification : list) {
			if(cpModification.getNumero()!=numero)continue;
			item=cpModification;break;
		}
		return item;
	}
}
