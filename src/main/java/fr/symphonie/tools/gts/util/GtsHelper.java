package fr.symphonie.tools.gts.util;

import fr.symphonie.tools.gts.ui.ReferentielBean;
import fr.symphonie.util.Helper;

public class GtsHelper {
	public static final String Billetterie_OBJ="CNCS - Interface Billetterie";
	
	public static ReferentielBean getReferentielBean(){
		String beanName="gtsRefBean";
		return (ReferentielBean) Helper.findBean(beanName);
	}

}
