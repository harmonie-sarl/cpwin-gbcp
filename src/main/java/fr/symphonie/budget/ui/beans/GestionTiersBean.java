package fr.symphonie.budget.ui.beans;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.activation.MimetypesFileTypeMap;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.event.ValueChangeEvent;

import org.primefaces.context.RequestContext;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.event.RowEditEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.TreeNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.symphonie.budget.core.model.util.LocalDocument;
import fr.symphonie.budget.core.service.IGestionTiersService;
import fr.symphonie.common.model.AppCfgConfig;
import fr.symphonie.common.util.Constant;
import fr.symphonie.common.util.MsgEntry;
import fr.symphonie.cpwin.model.Adresse;
import fr.symphonie.cpwin.model.CategorieDocEnum;
import fr.symphonie.cpwin.model.EtatTiersEnum;
import fr.symphonie.cpwin.model.Iban;
import fr.symphonie.cpwin.model.ModePaiement;
import fr.symphonie.cpwin.model.Service;
import fr.symphonie.cpwin.model.Tiers;
import fr.symphonie.cpwin.model.TiersDocument;
import fr.symphonie.exception.MissingConfiguration;
import fr.symphonie.util.HandlerJSFMessage;
import fr.symphonie.util.Helper;
import fr.symphonie.util.model.AdresseEnum;
import fr.symphonie.util.model.SimpleEntity;

/**
 * @author JEDIDI SOUHAIB (HARMONIE)
 * 
 */
@ManagedBean
@SessionScoped
public class GestionTiersBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;	
	private static final Logger logger = LoggerFactory.getLogger(GestionTiersBean.class);	
	@ManagedProperty(value = "#{gestionTiersService}")
	private IGestionTiersService gestionTiersService;
	private List<Tiers> tiersList;
	private List<CategorieDocEnum> docsList;
	private List<EtatTiersEnum> etatsTiersList;
	private List<AdresseEnum> typeAdressesList;
	private List<SimpleEntity> formeSocialeList;
	private List<ModePaiement> modePaiementList;
	private List<Service> serviceList;
	private Tiers tiers;
	private TiersDocument currentTiersDoc;
	private Adresse adresseToDelete;
	private Iban iban;
	private Date dateDebut;
	private Date dateFin;
	private String codeTiers;
	private String numSiren;
	private String etat;
	private String codeDocument;
	private String libDocument;
	private String ajaxTitleMsg;
	private String pageTitle;
	
	private int activeTvIndex = COORDONNES_TAB;	
	private boolean active;
	private boolean activeAdd;
	private FileUploadEvent fileUploadEvent;	
	private StreamedContent fileDocument;
	public static String PATH_FILE_NOT_FOUND=File.separator+"pages"+File.separator+"print";
	private static final int COORDONNES_TAB = 0, IBAN_TAB = 1;	

	private Adresse adresse;
	private List<LocalDocument> tiersDocList=null;
	private LocalDocument selectedDocument=null;
	private boolean updateMode=false;
	private TreeNode documentTree;
	
	/**
	 * Enumération des actions de chargement des listes
	 */
	enum AVAction{
		DOCUMENT,ETAT_TIERS,TYPE_ADRESSE, FORME_SOCIALE,MODE_PAIEMENT
		;
	}
	/**
	 * Charger des listes 
	 * @param action
	 */
	private void load(AVAction action) {	
		switch (action) {		
		case DOCUMENT:
			docsList=new ArrayList<CategorieDocEnum>();
			
			docsList.add(CategorieDocEnum.ADMINISTRATIFS);
			docsList.add(CategorieDocEnum.CONTRACTUELS);
			docsList.add(CategorieDocEnum.ACTES_BUDG);
			docsList.add(CategorieDocEnum.AUTRE);
			break;	
		case ETAT_TIERS:
			etatsTiersList=new ArrayList<EtatTiersEnum>();
			for(EtatTiersEnum  etat : EtatTiersEnum.values()){
				etatsTiersList.add(etat);
			}
			break;	
		case TYPE_ADRESSE:
			typeAdressesList=new ArrayList<AdresseEnum>();
			for(AdresseEnum  type : AdresseEnum.values()){
				typeAdressesList.add(type);
			}
			break;	
		case FORME_SOCIALE:
			logger.info("Chargement de la liste des formes sociales .........");
			formeSocialeList=gestionTiersService.getListFormeSocial();
			if(formeSocialeList!=null)logger.info("nombre des éléménts chargés: "+formeSocialeList.size());
			break;
		case MODE_PAIEMENT:
			logger.info("Chargement de la liste des modes de paiements .........");
            modePaiementList=gestionTiersService.getAllModePaiement();
        	if(modePaiementList!=null)logger.info("nombre des éléménts chargés: "+modePaiementList.size());
			break;
		default:
			break;
		}

	}

	/**
	 * Chercher les documents du tiers sélectionné
	 */
	public void searchTiers() {
		try {
			if (checkDate())
				tiersList = gestionTiersService.searchTiers(codeTiers,
						(numSiren.equals("") ? null : numSiren),
						(etat.equals("") ? null : etat), dateDebut,
						changeDateFinValues());
			            setDocumentTree(null);
		} catch (Exception e) {			
		
			HandlerJSFMessage.addErrorMessage(getErrorMessageFromException(MsgEntry.FAILED, e));
			e.printStackTrace();
		}
	}

	/**
	 * Vérifier la date
	 * @return
	 */
	private boolean checkDate() {
		boolean valid = true;
		if ((dateDebut != null) && (dateFin != null)
				&& (dateDebut.after(dateFin))) {
			HandlerJSFMessage.addErrorForComponent("detailsForm:dateAu",
					MsgEntry.DATE_DEB_SUPERIEUR);
			valid = false;
		}
		return valid;
	}

	/**
	 * Corriger la date fin en ajoutant un jour pour prendre en compte la date de ce jour
	 * @return
	 */
	@SuppressWarnings("deprecation")
	private Date changeDateFinValues() {
		Date result = null;
		if (dateFin != null) {
			result = (Date) dateFin.clone();
			result.setDate(result.getDate() + 1);
		}
		return result;
	}
	
	/**
	 * Consulter le tiers sélectionné
	 */
	public void consultTiers() {
		if (logger.isDebugEnabled()) {
			logger.debug("consultTiers() - start"); //$NON-NLS-1$
		}

		try {
			setUpdateMode(false);
			active=false;
			activeAdd=false;
			getSelectedTiers();
		} catch (Exception e) {
			logger.error("consultTiers()", e); //$NON-NLS-1$
			HandlerJSFMessage.addErrorMessage(getErrorMessageFromException(MsgEntry.FAILED, e));
			e.printStackTrace();
		}

		if (logger.isDebugEnabled()) {
			logger.debug("consultTiers() - end"); //$NON-NLS-1$
		}
	}
	
	/**
	 * Consulter le tiers sélectionné avec redirection
	 * @return
	 */
	public String consultTiersWPage() {
		if (logger.isDebugEnabled()) {
			logger.debug("consultTiersWPage() - start"); //$NON-NLS-1$
		}

		this.setPageTitle(HandlerJSFMessage.getMessage(NavigationBean.Action.CONSULTATION_TIERS.getMsgKey()));
		consultTiers();
		String returnString = NavigationBean.Action.CONSULTATION_TIERS.getOutcome();
		if (logger.isDebugEnabled()) {
			logger.debug("consultTiersWPage() - end"); //$NON-NLS-1$
		}
		return returnString;
	}
	
	/**
	 * Récupérer le tiers sélectionné
	 * @throws MissingConfiguration 
	 */
	private void getSelectedTiers() throws MissingConfiguration{
		if (logger.isDebugEnabled()) {
			logger.debug("getSelectedTiers() - start"); //$NON-NLS-1$
		}

		setActiveTvIndex(COORDONNES_TAB);
		String index = Helper.getRequestParameter("selectedTiersIndex");
		tiers = tiersList.get(Integer.parseInt(index));
		logger.debug("code du tiers selectione: "+tiers.getCode());
		loadTiersAttributes();		

		if (logger.isDebugEnabled()) {
			logger.debug("getSelectedTiers() - end"); //$NON-NLS-1$
		}
	}
    
	/**
	 * Chargement des attributs du tiers
	 * @throws MissingConfiguration 
	 */
	private void loadTiersAttributes() throws MissingConfiguration {
		loadTiersDocuments();
		loadTiersIbans();
		loadTiersFormeSociale();
	}
	
	/**
	 * Charger les documents du tiers
	 * @throws MissingConfiguration 
	 */
	private void loadTiersDocuments() throws MissingConfiguration {		
		List<LocalDocument> tiersDocList=new ArrayList<LocalDocument>();
		String path=null;
		String codeTiers=getTiers().getCode();
		String racineTiersDocPath=generateRacineTiersPath()+codeTiers;
		File directory=null;
		LocalDocument tiersDoc=null;
		for(CategorieDocEnum docType:getDocsList()){
			path=racineTiersDocPath+File.separator+docType.getCode();
			directory=new File(path);
			if((!directory.exists())|(!directory.isDirectory()))continue;
			String[] children = directory.list();
			if(children.length==0)continue;
			for(String docName:children){
				tiersDoc=new LocalDocument(codeTiers,docType.getCode(),docName,generateRacineTiersPath());
				tiersDocList.add(tiersDoc);
			}
		}

		setTiersDocList(tiersDocList);
	}

	/**
	 * Charger les ibans du tiers
	 */
	private void loadTiersIbans() {
		Tiers trs = gestionTiersService.getTiersWithIban(tiers.getCode());
		tiers.setIbans(trs.getIbans());
		setIban(null);
	}

	/**
	 * Charger la forme sociale du tiers
	 */
	private void loadTiersFormeSociale() {
		String libFormeSoc=null;
		SimpleEntity formeSoc=SimpleEntity.getSelectedSimpleEntity(tiers.getFormeSociale(), this.getFormeSocialeList());
		libFormeSoc=(formeSoc!=null)?formeSoc.getDesignation():"";
		tiers.setLibFormeSociale(libFormeSoc);
	}

	/**
	 * Préparer la mise à jour du tiers
	 */
	public void updateTiers() {
		try {
			setUpdateMode(true);
			active=true;
			activeAdd=false;
			getSelectedTiers();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Mettre à jour le tiers avec redirection
	 * @return
	 */
	public String updateTiersWPage(){
		this.setPageTitle(HandlerJSFMessage.getMessage(NavigationBean.Action.MODIFICATION_TIERS.getMsgKey()));
		updateTiers();
		return NavigationBean.Action.MODIFICATION_TIERS.getOutcome();
	}
	
	/**
	 * Préparer l'ajout d'un tiers
	 */
	public void addTiers(){
		this.tiers=new Tiers();
		this.tiers.setTrace(Helper.createTrace());
//		this.tiers.getAdresse().setTrace(Helper.createTrace());
//		this.getTiers().getAdresse().setCodeTiers(this.tiers.getCode());
		active=true;
		activeAdd=true;
	}
	
	/**
	 * Préparer l'ajout d'un tiers avec redirection
	 */
	public String addTiersWPage(){
		this.setPageTitle(HandlerJSFMessage.getMessage(NavigationBean.Action.SAISI_TIERS.getMsgKey()));
		addTiers();
		return NavigationBean.Action.SAISI_TIERS.getOutcome();
	}
	
	/**
	 * Ajouter ou mettre à jour un tiers
	 */
	public void updateTiersActionListener(){
		updateTiersOperation();
		setDocumentTree(null);
			
	}
	
	/**
	 * Mettre à jour un tiers
	 */
	private boolean updateTiersOperation(){
		boolean valid=true;
		try {
			valid=checkTiersFields();
			if(valid){
				if(activeAdd){					
					gestionTiersService.insert(tiers);
				}
				else{
					if(tiers.getDocuments()!=null)
						for(TiersDocument d:tiers.getDocuments()){
							logger.debug("updateTiersOperation :"+d.getExercice()+","+d.getDocument().getCode());
						}
				gestionTiersService.update(tiers);	
				}
				loadTiersDocuments();
//				loadTiersAttributes();
			    tiers.resetNewAdresses();
			    if(activeAdd && addToListRecherche()){
			    	if(tiersList==null)
			    		tiersList=new ArrayList<Tiers>();
//			    	if(!tiersList.contains(tiers))tiersList.add(tiers);
			        tiersList.add(tiers);
			    }
			    activeAdd=false;
			    HandlerJSFMessage.addInfo(MsgEntry.ENREG_TIERS_SU_MSG);
			}			
		} catch (Exception e) {
			e.printStackTrace();
			valid=false;
			HandlerJSFMessage.addError(MsgEntry.ENREG_TIERS_FAIL_MSG);
		}
		return valid;
	}
	
	/**
	 * Mettre à jour un tiers
	 */
	public String updateTiersAction(){
		if(updateTiersOperation()) return fermer();
		return "";
	}
	
	/**
	 * Vérifier l'ajout du tiers à la liste de recherche
	 * @return
	 */
	private boolean addToListRecherche() {
		boolean valid=true;
		if((codeTiers!=null)&&(!tiers.getCode().contains(codeTiers))&&(!tiers.getNom().contains(codeTiers)))
			valid=false;
		else if((numSiren!=null)&&(!numSiren.equals(""))&&(!tiers.getNumSiren().equalsIgnoreCase(numSiren)))
			valid=false;
		else if((etat!=null)&&(!etat.equals(""))&&(!tiers.getEtat().equalsIgnoreCase(etat)))
			valid=false;
		else if((dateDebut!=null)&&(tiers.getTrace().getDateMaj()!=null)&&(tiers.getTrace().getDateMaj().before(dateDebut)))
			valid=false;
		else if((dateFin!=null)&&(tiers.getTrace().getDateMaj()!=null)&&(tiers.getTrace().getDateMaj().after(dateFin)))
			valid=false;
		return valid;
	}
	
	/**
	 * Valider les champs du tiers
	 * @return
	 */
	private boolean checkTiersFields() {
		boolean valid=true;
		if ((!checkTiers())||(!validateEmail())||(!checkAdressesFields()))
			valid=false;
		else {
			valid=tiers.checkRequiredIban();
			if (valid)return valid;
			setActiveTvIndex(IBAN_TAB);		
			HandlerJSFMessage.addError(MsgEntry.INVALIDE_IBAN);			
		}
		return valid;
	}

	/**
	 * Valider les champs du tiers
	 * @return
	 */
	private boolean checkTiers() {
		boolean valid=true;
		if ((tiers.getCode() == null) || (tiers.getCode().trim().equals(""))
				|| (tiers.getNom() == null)
				|| (tiers.getNom().trim().equals(""))
				|| (tiers.getEnseigne() == null)
				|| (tiers.getEnseigne().trim().equals(""))
				|| (tiers.getNumSiren() == null)
				|| (tiers.getNumSiren().trim().equals(""))
				|| (tiers.getEtat() == null) || (tiers.getEtat().isEmpty())
				|| (tiers.getModepaie() == null)
				|| (tiers.getModepaie().trim().equals(""))
				|| (tiers.getCodeService()== null)				
				|| (tiers.getCodeService().trim().equals(""))
				) {
			HandlerJSFMessage.addError(MsgEntry.REQUIRED_FIELDS);
			valid=false;
		}else if(activeAdd){
			Tiers trs=gestionTiersService.getTiersWoIban(tiers.getCode());		
			if(trs.getCode()!=null){
				HandlerJSFMessage.addError(MsgEntry.TIERS_EXISTE);
				valid=false;
			}
		}
		return valid;
	}
	
	/**
	 * Valider l'email du tiers
	 * @return
	 */
	private boolean validateEmail() {
		boolean valid=true;
		if((tiers.getMail()!=null)&&(!tiers.getMail().trim().equals(""))){
			Pattern p = Pattern.compile(".+@.+\\.[a-z]+");
			Matcher m = p.matcher(tiers.getMail());
			valid = m.matches();
			if(!valid)
				HandlerJSFMessage.addError(MsgEntry.CPWIN_EMAIL_INVALIDE);
		}
		return valid;
	}
	
	/**
	 * Valider les champs des adresses du tiers
	 * @return
	 */
	private boolean checkAdressesFields(){
		int count=0;
		if(tiers.getAdresses()!=null){
			for(Adresse adr : tiers.getAdresses()){
				if(!checkRequiredAdresseFields(adr)){
					HandlerJSFMessage.addError(MsgEntry.CPWIN_ADRESSE_REQUIRED);
					setActiveTvIndex(COORDONNES_TAB);
					return false;
				}else if(adr.getType().equalsIgnoreCase(AdresseEnum.OFFICIELLE.getValue()))
					count++;
			}
		}
		if(count>1){
			HandlerJSFMessage.addError(MsgEntry.CPWIN_ADRESSE_OFFICIELLE);
			setActiveTvIndex(COORDONNES_TAB);
			return false;
		}
		return true;
	}
	
	/**
	 * Valider les champs obligatoires de l'adresse du tiers
	 * @return
	 */
	private boolean checkRequiredAdresseFields(Adresse adresse){
		if((adresse.getAdresse1()==null)||(adresse.getAdresse2()==null)||(adresse.getBureauPostale()==null)||(adresse.getCodePostale()==null)||(adresse.getVille()==null)
				||(adresse.getPays()==null)||(adresse.getContact().getTelephone()==null)||(adresse.getContact().getFax()==null)||(adresse.getType().equals("")))
			return false;
		return true;
	}
	
	/**
	 * Ajouter un document
	 * @param actionEvent
	 */
	public void addDocument() {	
		
		boolean valid=false;
		RequestContext requestContext = RequestContext.getCurrentInstance();
		valid=checkDocFields();
		if (!valid){
			return;
		}		
		try {
			if(addTiersDocument()){
			setCodeDocument(null);
			setLibDocument(null);		
			setFileUploadEvent(null);
			setDocumentTree(null);

			requestContext.addCallbackParam("isValid", true);
			}
			else requestContext.addCallbackParam("isValid", false);
		} catch (MissingConfiguration e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	private boolean checkDocFields(){
		boolean valid=true;
		if((getCodeDocument()==null) || (getCodeDocument().equals(""))){
			HandlerJSFMessage.addErrorForComponent("detailsForm:codeList",MsgEntry.CPWIN_REQUIRED);
			valid=false;
		}
		if(getFileUploadEvent()==null){
			HandlerJSFMessage.addErrorForComponent("detailsForm:fileUp",MsgEntry.DOCUMENT_REQUIRED);
			valid=false;
		}
		return valid;
	}
	
	/**
	 * Ajouter un document au tiers
	 * @throws MissingConfiguration 
	 */
	private boolean addTiersDocument() throws MissingConfiguration {
		String fileName=updateFileName(getFileUploadEvent().getFile().getFileName());
		CategorieDocEnum c=(getCodeDocument()!=null)?CategorieDocEnum.parse(getCodeDocument()):CategorieDocEnum.AUTRE;
		String path=generateRacineTiersPath();
		if(path==null){
			HandlerJSFMessage.addErrorForComponent("detailsForm:fileUp",MsgEntry.tiers_demat_path);
			return false;
		}
		LocalDocument tiersDoc=new LocalDocument(getTiers().getCode(),c.getCode(),fileName,path);
		
		getTiersDocList().add(tiersDoc);
		String dirPath=tiersDoc.getPath();
		File dir=new File(dirPath);
		if(!dir.exists())	dir.mkdirs();
		uploadFile(dirPath+File.separator+ fileName);
		return true;
	}
	
	/**
	 * Faire l'upload du fichier
	 */
	public void uploadFile(String outputFile) {
		int BUFFER_SIZE = 6124;
		File result = new File(outputFile);
		try {
			FileOutputStream fileOutputStream = new FileOutputStream(result);
			byte[] buffer = new byte[BUFFER_SIZE];
			int bulk;
			InputStream inputStream = getFileUploadEvent().getFile()
					.getInputstream();
			while (true) {
				bulk = inputStream.read(buffer);
				if (bulk < 0) {
					break;
				}
				fileOutputStream.write(buffer, 0, bulk);
				fileOutputStream.flush();
			}
			fileOutputStream.close();
			inputStream.close();
			setFileUploadEvent(null);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Corriger le nom du fichier
	 * @param fileName
	 * @param ordre
	 * @return
	 */
	private String updateFileName(String fileName) {
		fileName=fileName.replaceAll(" ", "_");	
		return fileName;
	}
	
	/**
	 * Supprimer les documents non sauvegardés du tiers
	 */
	public void deleteUnsavedDocs(){
		File file;
		try {
			if((tiers.getDocuments()!=null)&&(tiers.getDocuments().size()!=0)){
				for(TiersDocument doc : tiers.getDocuments()){
					if(doc.getId()==0){
						file = new File(generateTiersDocPath(CategorieDocEnum.parse(doc.getDocument().getCode()), doc.getOrdre()));
						file.delete();
					} 					
				}					
			}
		}catch (FileNotFoundException e) {			
			e.printStackTrace();
		}catch (Exception e) {			
			e.printStackTrace();
		}
	}
	/**
	 * Supprimer les documents non sauvegardés du tiers avec redirection
	 */
	public String deleteUnsavedDocsWPage(){
		deleteUnsavedDocs();
		return fermer();
	}
	
	/**
	 * Construire le chemin de sauvegarde du document
	 * @param typeDocument
	 * @param ordre
	 * @return
	 * @throws FileNotFoundException
	 * @throws MissingConfiguration 
	 */
	private String generateTiersDocPath(CategorieDocEnum typeDocument, int ordre) throws FileNotFoundException, MissingConfiguration {

		String path=generateRacineTiersDocPath();
		switch (typeDocument) {
		case AVIS_REGLT:
			path+=File.separator+String.valueOf(getCurrentTiersDoc().getExercice())+File.separator+CategorieDocEnum.AVIS_REGLT.getCode()+File.separator+getCurrentTiersDoc().getTitre()+"."+Constant.PDF;
			break;
		default:
			path+=File.separator+typeDocument.getCode()+File.separator+getAutreFileName(path+File.separator+typeDocument.getCode(), ordre);//+currentTiersDoc.getTitre()+"_"+String.valueOf(ordre);
			break;
		}
		logger.debug("generateTiersDocPath: path="+path);
		return path;
	}
	
	/**
	 * Récupérer le nom du document de type <<AUTRE>>
	 * @param sourceDirPath
	 * @param ordre
	 * @return
	 * @throws FileNotFoundException
	 */
	private String getAutreFileName(String sourceDirPath, int ordre) throws FileNotFoundException {
		int dotPos, charPos,ordreFile;
		File file=new File(sourceDirPath);
		if (file.isDirectory()) {
            String[] children = file.list();
            for (int i=0; i<children.length; i++) {            	
            	dotPos = children[i].lastIndexOf(".");
            	charPos = children[i].lastIndexOf("_");
        		ordreFile = Integer.parseInt(children[i].substring(charPos+1, dotPos));            	
                 if (ordreFile == ordre)
 					return children[i];
            }
        }
		throw new FileNotFoundException();
	}
	
	/**
	 * Supprimer un document
	 */
	public void deleteDocument(){
		LocalDocument doc=getSelectedDocument();
		getTiersDocList().remove(doc);
		removeDocumentFile();
		setDocumentTree(null);
	}
	
	/**
	 * Supprimer un fichier document
	 */
	private void removeDocumentFile() {
		LocalDocument doc=getSelectedDocument();
		String filePath=doc.getPath()+File.separator+doc.getName();
		logger.debug("getFileDocument : "+filePath);
		try{
			File file = new File(filePath);				
			file.delete();
		
		}catch (Exception e) {			
			e.printStackTrace();
		}
	}
	
	/**
	 * Supprimer une adresse
	 */
	public void deleteAdresse(){
		if(!adresseToDelete.isNewAdresse())
			gestionTiersService.deleteAdresse(adresseToDelete.getCodeTiers(), adresseToDelete.getOrdre());	
		tiers.getAdresses().remove(adresseToDelete);
	}
	
	/**
	 * Listener lors du changement du critère de recherche
	 * @param action
	 */
	public void onSearchTiersChange(ActionEvent action){
		try {
			resetSearchTiers();
		} catch (Exception e) {
			e.printStackTrace();
		}		
	}
	
	private void resetSearchTiers(){
		tiersList=null;
	}
	
	/**
	 * Listener lors du changement du critère de recherche date début modification
	 * @param event
	 */
	public void onDateDebChange(AjaxBehaviorEvent event){

			resetSearchTiers();
	}
	
	
	/**
	 * Listener lors du changement du critère de recherche date fin modification
	 * @param event
	 */
	public void onDateFinChange(AjaxBehaviorEvent event){

			resetSearchTiers();
	}
	
	
	/**
	 * Listener lors du changement du code tiers
	 * @param event
	 */
	public void codeTiersListener(ValueChangeEvent event){
		if((event!=null)&&(!event.getNewValue().toString().trim().equals(""))){
			tiers.setCode(event.getNewValue().toString());
			if(tiers.getIbans()!=null){
				for(Iban iban : tiers.getIbans()){
					iban.setCodeTiers(tiers.getCode());
				}
			}
			if(tiers.getAdresses()!=null){
				for(Adresse adresse : tiers.getAdresses()){
					adresse.setCodeTiers(tiers.getCode());
				}
			}
			if(tiers.getDocuments()!=null){
				for(TiersDocument document : tiers.getDocuments()){
					document.setTiers(tiers);
				}
			}
		}
	}
	
	
	/**
	 * declanché en cas de déselection d'un élémént de la liste des ibans
	 * 
	 * @param event
	 */
	public void onIbanUnselect(UnselectEvent event) {
		if (logger.isDebugEnabled()) {
			logger.debug("onIbanUnselect(UnselectEvent) - start"); //$NON-NLS-1$
		}
            if(event==null)return;

//		  setIban(null);
	
		if (logger.isDebugEnabled()) {
			logger.debug("onIbanUnselect- end"); //$NON-NLS-1$
		}
	}
	
	/**
	 * declanché en cas de selection d'un élémént de la liste des ibans
	 * 
	 * @param event
	 */
	public void onIbanSelect(SelectEvent event) {
		if (logger.isDebugEnabled()) {
			logger.debug("onIbanSelect(SelectEvent) - start"); //$NON-NLS-1$
		}
            if(event==null)return;

		  setIban((Iban)event.getObject());
//		 getIban().setValide(OuiNonEnum.OUI);
		
	
		if (logger.isDebugEnabled()) {
			logger.debug("onIbanSelect- end"); //$NON-NLS-1$
		}
	   
	}
	
	/**
	 * préparation de l'ajout d'un nouveau iban au tiers en cours
	 */
	public void prepareAddIban() {
		if(this.getTiers().getCode()==null){
			HandlerJSFMessage.addError(MsgEntry.CODE_TIERS_REQUIRED);
			return;
		}
		try {
			this.iban = getTiers().addNewIban();
			iban.setBanque("");
			logger.info("Préparation, de l'ajout d'un nouveau iban: code tiers="
					+ this.getTiers().getCode() + ", taille de la liste des ibans="
					+ getTiers().getIbans().size());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void resetTiersElements() {
		codeTiers=null;
		numSiren=null;
		etat=null;
		dateDebut=null;
		dateFin=null;
		tiersList=null;
	}	
	
	/**
	 * Charger la liste des services
	 * @param prefix
	 * @return
	 */
	public List<Service> autoCompleteService(String prefix) {
		if (logger.isInfoEnabled()) {
		logger.info("###### Auto Complete Service prefix="+prefix+" - start...");
		}
		boolean searchAtBegin=!(Helper.isSpecialPrefix(prefix));
		String formatedPrefix=Helper.removeSpecialPrefix(prefix);
		List<Service> retour=gestionTiersService.getService(formatedPrefix, searchAtBegin);
		if (logger.isInfoEnabled())  {
			if(retour!=null)
			logger.info("###### Auto Complete Service prefix="+prefix+", "+retour.size()+" éléments trouvés - end");
			}
		if (logger.isInfoEnabled())  {
			if(retour==null)
			logger.info("###### Auto Complete Service prefix="+prefix+", aucun élément trouvé - end");
		}
		return retour;
	}
	
	/**
	 * Fermer la page
	 * @return
	 */
	public String fermer(){
		return ((NavigationBean)(Helper.findBean("navigationBean"))).getCurrentAction().getOutcome();
	}
	
	/**
	 * Getters et setters
	 */
	public List<Tiers> getTiersList() {
		return tiersList;
	}

	public void setTiersList(List<Tiers> tiersList) {
		this.tiersList = tiersList;
	}

	public void setGestionTiersService(IGestionTiersService gestionTiersService) {
		this.gestionTiersService = gestionTiersService;
	}

	public IGestionTiersService getGestionTiersService() {
		return gestionTiersService;
	}

	public String getCodeTiers() {
		return codeTiers;
	}

	public void setCodeTiers(String codeTiers) {
		this.codeTiers = codeTiers;
	}

	public void setNumSiren(String numSiren) {
		this.numSiren = numSiren;
	}

	public String getNumSiren() {
		return numSiren;
	}

	public void setEtat(String etat) {
		this.etat = etat;
	}

	public String getEtat() {
		return etat;
	}

	public void setDateDebut(Date dateDebut) {
		this.dateDebut = dateDebut;
	}

	public Date getDateDebut() {
		return dateDebut;
	}

	public void setDateFin(Date dateFin) {
		this.dateFin = dateFin;
	}

	public Date getDateFin() {
		return dateFin;
	}
	
	public void setActive(boolean active) {
		this.active = active;
	}

	public boolean isActive() {
		return active;
	}
	
	public void setActiveAdd(boolean activeAdd) {
		this.activeAdd = activeAdd;
	}

	public boolean isActiveAdd() {
		return activeAdd;
	}
	
	public void setActiveTvIndex(int activeTvIndex) {
		this.activeTvIndex = activeTvIndex;
	}

	public int getActiveTvIndex() {
		return activeTvIndex;
	}
	public void setTiers(Tiers tiers) {
		this.tiers = tiers;
	}

	public Tiers getTiers() {
		return tiers;
	}
	
	public String getCodeDocument() {
		return codeDocument;
	}

	public void setCodeDocument(String codeDocument) {
		this.codeDocument = codeDocument;
	}
	
	public void setDocsList(List<CategorieDocEnum> docsList) {
		this.docsList = docsList;
	}

	public List<CategorieDocEnum> getDocsList() {
		if(docsList==null)
			load(AVAction.DOCUMENT);
		return docsList;
	}
	
	public String getLibDocument() {
		return libDocument;
	}

	public void setLibDocument(String libDocument) {
		this.libDocument = libDocument;
	}
	
	/**
	 * Recuperer le fichier pour l'upload
	 * 
	 * @param event
	 */
	public void handleFileUpload(FileUploadEvent event) {
		setFileUploadEvent(event);
	}
	
	
	public FileUploadEvent getFileUploadEvent() {
		return fileUploadEvent;
	}

	public void setFileUploadEvent(FileUploadEvent fileUploadEvent) {
		this.fileUploadEvent = fileUploadEvent;
	}
	
	public TiersDocument getCurrentTiersDoc() {
		return currentTiersDoc;
	}

	public void setCurrentTiersDoc(TiersDocument currentTiersDoc) {
		this.currentTiersDoc = currentTiersDoc;
	}
	
	private String generateRacineTiersDocPath() throws MissingConfiguration{
		return generateRacineTiersPath()+currentTiersDoc.getTiers().getCode();
	}
	private String generateRacineTiersPath() throws MissingConfiguration{
		String path=null;
		// path=gestionTiersService.getTiersDematPath();
		path=AppCfgConfig.getInstance().getTiersDematPath();
		if(path!=null)
			path+=File.separator;
		return path;
	}
	
	public Adresse getAdresseToDelete() {
		return adresseToDelete;
	}

	public void setAdresseToDelete(Adresse adresseToDelete) {
		this.adresseToDelete = adresseToDelete;
	}
	
	public void setEtatsTiersList(List<EtatTiersEnum> etatsTiersList) {
		this.etatsTiersList = etatsTiersList;
	}

	public List<EtatTiersEnum> getEtatsTiersList() {
		if(etatsTiersList==null)
			load(AVAction.ETAT_TIERS);
		return etatsTiersList;
	}
	
	public Iban getIban() {
		return iban;
	}

	public void setIban(Iban iban) {
		this.iban = iban;
	}
	
	public StreamedContent getFileDocument() {		
		InputStream stream;
		LocalDocument doc=getSelectedDocument();
		String filePath=doc.getPath()+File.separator+doc.getName();
		logger.debug("getFileDocument : "+filePath);
		try {
			File file = new File(filePath);
			stream = new FileInputStream(file);
			fileDocument = new DefaultStreamedContent(stream,
					new MimetypesFileTypeMap().getContentType(file),
					file.getName());
		} catch (FileNotFoundException e) {			
			e.printStackTrace();
			File file = new File(FacesContext.getCurrentInstance()
					.getExternalContext().getRealPath("")
					+ File.separator
					+ PATH_FILE_NOT_FOUND
					+ File.separator + "File_not_found.pdf");
			try {
				stream = new FileInputStream(file);
				fileDocument = new DefaultStreamedContent(stream,
						new MimetypesFileTypeMap().getContentType(file),
						file.getName());
			} catch (FileNotFoundException e1) {
				e1.printStackTrace();
			}
		}
		catch(Exception e){
			
			e.printStackTrace();
		}

		return fileDocument;
	}

	public void setFileDocument(StreamedContent fileDocument) {
		this.fileDocument = fileDocument;
	}
	
	public void addAdresse(ActionEvent action){
		tiers.addAdresse();
	}
	
	public void setTypeAdressesList(List<AdresseEnum> typeAdressesList) {
		this.typeAdressesList = typeAdressesList;
	}

	public List<AdresseEnum> getTypeAdressesList() {
		if(typeAdressesList==null)
			load(AVAction.TYPE_ADRESSE);
		return typeAdressesList;
	}

	public List<SimpleEntity> getFormeSocialeList() {
		if(formeSocialeList==null)
			load(AVAction.FORME_SOCIALE );
		return formeSocialeList;
	}

	public List<ModePaiement> getModePaiementList() {
		if(modePaiementList==null)
			load(AVAction.MODE_PAIEMENT);
		return modePaiementList;
	}

	public List<Service> getServiceList() {
		return serviceList;
	}
	
	public String getMinAjaxQL(){		
		
		return ""+ Constant.getMinAjaxQL();
	}
	public String getMaxAjaxResult(){
		
		return "" + Constant.getMaxAjaxResult();
		
	}

	public void setAjaxTitleMsg(String ajaxTitleMsg) {
		this.ajaxTitleMsg = ajaxTitleMsg;
	}

	public String getAjaxTitleMsg() {
		
		if(ajaxTitleMsg!=null)return ajaxTitleMsg;
		ajaxTitleMsg=HandlerJSFMessage.getMessageByKey(MsgEntry.AJAX_MSG);
		ajaxTitleMsg=HandlerJSFMessage.formatMessage(ajaxTitleMsg, new Object[]{getMinAjaxQL()});
		return ajaxTitleMsg;
	}

	public void setPageTitle(String pageTitle) {
		this.pageTitle = pageTitle;
	}

	public String getPageTitle() {
		return pageTitle;
	}
	
	
	 public void onAdressEditInit(RowEditEvent event) {
			if (logger.isDebugEnabled()) {
				logger.debug("onAdressEditInit(RowEditEvent) - start"); //$NON-NLS-1$
			}
	            if(event==null)return;

			  setAdresse((Adresse)event.getObject());
		
			if (logger.isDebugEnabled()) {
				logger.debug("onAdressEditInit- end"); //$NON-NLS-1$
			}
		    }
	 
	  public void onAdressEdit(RowEditEvent event) {
			
	    }
	  
	  /**
		 * @return the adresse
		 */
		public Adresse getAdresse() {
			return adresse;
		}

		/**
		 * @param adresse the adresse to set
		 */
		public void setAdresse(Adresse adresse) {
			this.adresse = adresse;
		}

		public void setTiersDocList(List<LocalDocument> tiersDocList) {
			this.tiersDocList = tiersDocList;
		}

		public List<LocalDocument> getTiersDocList() {
			return tiersDocList;
		}

		public void setSelectedDocument(LocalDocument selectedDocument) {
			this.selectedDocument = selectedDocument;
		}

		public LocalDocument getSelectedDocument() {
			return selectedDocument;
		}
		public boolean isMajIbanAutorized(){
			return false;
		}
		public boolean isMajAdresseAutorized(){
			return false;
		}
		public boolean isMajGeneralAutorized(){
			return false;
		}
		public boolean isMajDocAutorized(){
			return true;
		}
		
			
		public boolean isGeneralTab(){
			if(updateMode)
			{setActive(isMajGeneralAutorized());}
			return true;
			
		}
		public boolean isAdressTab(){
			if(updateMode)
			{setActive(isMajAdresseAutorized());}
			return true;
			
		}
		public boolean isIbanTab(){
			if(updateMode)
			{setActive(isMajIbanAutorized());}
			if(getTiersDocList()==null)
				return false;
			return true;
			
		}
		public boolean isDocTab(){
			if(updateMode)
			{setActive(isMajDocAutorized());}
			return true;
			
		}

		public void setUpdateMode(boolean updateMode) {
			this.updateMode = updateMode;
		}

		public boolean isUpdateMode() {
			return updateMode;
		}

		public void setDocumentTree(TreeNode documentTree) {
			this.documentTree = documentTree;
		}

		public TreeNode getDocumentTree() {
			 if(documentTree==null)createDocumentTree();
			return documentTree;
		}

		public void createDocumentTree() {
			if (logger.isDebugEnabled()) {
				logger.debug("createdocumentTree("+getTiers()+") - start"); 
			}
			
			if(getTiers()==null)return ;
			documentTree=new DefaultTreeNode();
	    	TreeNode doc=null; 
	    	logger.debug("createDocumentTree("+getTiersDocList()+") - start"); 
	    	for(CategorieDocEnum cat:getDocsList())
    		{
	    		  doc=new DefaultTreeNode(new LocalDocument(null, cat.getCode(), null, null),documentTree);
	    		
	    	   for(LocalDocument document:getTiersDocList()){
	    		   
	    		   if(cat.getCode().equalsIgnoreCase(document.getCategorie())){
	    			 
	    			   new DefaultTreeNode(document,doc);
	    			 
	    		   }
	    	   }
         
			if (logger.isDebugEnabled()) {
				logger.debug("createDocumentTree():tree="+documentTree+" - end"); //$NON-NLS-1$
			}
	    	}
			
			
		}
		  
		public void onNodeImportExpand(){
		    	
		    	collapsingORexpanding(getDocumentTree(),true);
		    	
		    }
		
		public void collapsingORexpanding(TreeNode n, boolean option) {
			
		    if(n.getChildren().size() == 0) {
		        n.setSelected(false);
		    }
		    else {
		        for(TreeNode s: n.getChildren()) {
		            collapsingORexpanding(s, option);
		        }
		        n.setExpanded(option);
		        n.setSelected(false);
		    }
		}
		public List<Tiers> autoCompleteTiers(String prefix) {
			logger.debug("###### Auto Complete Tiers prefix="+ prefix);
			List<Tiers> tiersList=null;			
			try{
				 tiersList=searchLightTiers(prefix);
			}
			catch(Exception e){
				
			}
			
			if (logger.isDebugEnabled()) {
			
		logger.debug("###### Auto Complete Tiers prefix="+ prefix+ ", "+ tiersList.size()+ " éléments trouvés - end");
			}
			return tiersList;
		}
		public  List<Tiers> searchLightTiers(String prefix) {
			boolean searchAtBegin = !(Helper.isSpecialPrefix(prefix));
			String formatedPrefix = Helper.removeSpecialPrefix(prefix);
			List<Tiers> retour = gestionTiersService.findLightTiersList(searchAtBegin, formatedPrefix );
			return retour;
		}
		
		public Adresse loadTiersAdress(int noAdress, String codeTiers){
			
			Adresse adresse=gestionTiersService.getAdressCpwinTiers(noAdress,codeTiers);
			return adresse;
		}
		public static String getErrorMessageFromException(String genericKeyMsg, Exception e) {

			Throwable cause = null;
			String errorMsg = null;
			if (e != null) {
				cause = e;
				errorMsg = cause.getMessage();
				if (cause.getCause() != null) {
					cause = cause.getCause();
					errorMsg += "\n " + cause.getMessage();
					if (cause.getCause() != null) {
						cause = cause.getCause();
						errorMsg += "\n " + cause.getMessage();
					}
				}
			}
			if (genericKeyMsg != null)
				return HandlerJSFMessage.getErrorMessage(genericKeyMsg) + ": " + errorMsg;
			else
				return errorMsg;
		}
}
