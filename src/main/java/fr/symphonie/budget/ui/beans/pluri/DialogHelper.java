package fr.symphonie.budget.ui.beans.pluri;

import java.util.HashMap;
import java.util.Map;

import org.primefaces.context.RequestContext;

public class DialogHelper {
	//private static final Logger logger = LoggerFactory.getLogger(DialogHelper.class);
	
	private static void openDialog(String sourceName, Map<String,Object> options) {
        RequestContext.getCurrentInstance().openDialog(sourceName, options, null);

    }
	private static Map<String,Object> defaultOptions(){
		 Map<String,Object> options = new HashMap<String, Object>();
	        options.put("modal", true);
	        options.put("draggable", true);
	        options.put("resizable", true);
	        options.put("height", 600);
	        options.put("width", 1200);
	        options.put("contentHeight", "100%");
	        options.put("contentWidth", "100%");
	        options.put("position", "center");
	        return options;
	}
	public static void openEnvelopBudgDialog(){
		String sourceName="budgetPluriView";
		Map<String, Object> options=defaultOptions();
		openDialog(sourceName, options);
	}
	
	public static void openEnvelopBudgForAdressDialog(){
		String sourceName="ventillationCp_service_View";
//		Map<String, Object> options=defaultOptions();
		Map<String, Object> options=initOptions(true, false, false, 600, 1200);
		openDialog(sourceName, options);
	}
	public static void closeDialog(Object returnedObj){
		   RequestContext.getCurrentInstance().closeDialog(returnedObj);
	}
	
	private static Map<String,Object> initOptions(boolean modal, boolean draggable,boolean resizable,int contentHeight,int contentWidth){
		 Map<String,Object> options = new HashMap<String, Object>();
	        options.put("modal", modal);
	        options.put("draggable", draggable);
	        //options.put("resizable", resizable);
	        options.put("resizable", true);
	        options.put("height", contentHeight);
	        options.put("width", contentWidth);
	        options.put("position", "center");
	        options.put("contentHeight", "100%");
	        options.put("contentWidth", "100%");
	        return options;
	}
	
	public static void openEnregUserDialog(){		
//		Map<String, Object> options=defaultOptions();
//		openDialog(sourceName, options);		
		String sourceName="enreg_user_pop";
		Map<String, Object> options=initOptions(true, true, true, 600, 1000);
		openDialog(sourceName, options);
	}
	
	public static void openVentilBrDestDialog(){
		String sourceName="ventilBrDestView";
		Map<String, Object> options=initOptions(true, true, true, 800, 1200);
		openDialog(sourceName, options);
	}
	
	
	
	public static void openValidatEditDialog(){		
		String sourceName="validate_edition_dlg";
		Map<String, Object> options=initOptions(true, true, true, 1000, 1000);
		openDialog(sourceName, options);
	}
	public static void openEcrituresView(){
		String sourceName="ecrituresView";
		Map<String, Object> options=initOptions(true, true, true, 800, 1250);
		openDialog(sourceName, options);
	}
	
	public static void openEcritureView(){
		String sourceName="ecritureView";
		Map<String, Object> options=initOptions(true, true, false, 450, 900);
		openDialog(sourceName, options);
	}
	
	public static void openTiersView(){
		String sourceName="tiersView";
		Map<String, Object> options=initOptions(true, true, false, 550, 950);
		openDialog(sourceName, options);
	}
	public static void openHonoraireView(){
		String sourceName="honoraireView";
		Map<String, Object> options=initOptions(true, true, false, 670, 870);
		openDialog(sourceName, options);
	}
	
	public static void openConfirmDepDialog(){
		String sourceName="confirmDepView";
		Map<String, Object> options=initOptions(true, true, false, 200, 650);
		openDialog(sourceName, options);
	}
	public static void openParamView(){
		String sourceName="paramView";
		Map<String, Object> options=initOptions(true, true, false, 450, 900);
		openDialog(sourceName, options);
	}
	
	
	public static void openArticleDialog(){
		String sourceName="articleView";
		Map<String, Object> options=initOptions(true, true, false, 670, 1250);
		openDialog(sourceName, options);
	}
	public static void openDetailArticleDialog(){
		String sourceName="detailArticleView";
		Map<String, Object> options=initOptions(true, true, false, 500, 1200);
		openDialog(sourceName, options);
	}
	public static void openPeriodeGtsDialog(){
		String sourceName="periodeGtsView";
		Map<String, Object> options=initOptions(true, true, false, 200, 450);
		openDialog(sourceName, options);
	}
	public static void openClientDialog(){
		String sourceName="clientView";
		Map<String, Object> options=initOptions(true, true, false, 400, 1000);
		openDialog(sourceName, options);
	}
	
	public static void openRecetOriginDialog(){
		String sourceName="recetOriginView";
		Map<String, Object> options=initOptions(true, true, false,400, 1250);
		openDialog(sourceName, options);
	}
	public static void openEiView(){
		String sourceName="detailsEIView";
		Map<String, Object> options=initOptions(true, true, false, 550, 950);
		openDialog(sourceName, options);
	}
	public static void openSignatureDialog(){
		String sourceName="signatureView";
		Map<String, Object> options=initOptions(true, true, false, 650, 1150);
		openDialog(sourceName, options);
	}
}
