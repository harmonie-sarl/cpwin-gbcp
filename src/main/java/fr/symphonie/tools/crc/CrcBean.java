package fr.symphonie.tools.crc;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.symphonie.budget.ui.beans.NavigationBean.Action;
import fr.symphonie.common.model.AppCfgConfig;
import fr.symphonie.common.util.BudgetHelper;
import fr.symphonie.common.util.Constant;
import fr.symphonie.common.util.Util;
import fr.symphonie.cpwin.core.demat.IDematService;
import fr.symphonie.cpwin.core.model.CrcItem;
import fr.symphonie.cpwin.core.model.CrcPj;
import fr.symphonie.exception.MissingConfiguration;
import fr.symphonie.tools.common.CommonToolsBean;
import fr.symphonie.tools.crc.model.ModeEnum;
import fr.symphonie.util.FileHelper;
import fr.symphonie.util.model.SimpleEntity;

public class CrcBean extends CommonToolsBean  implements Serializable {
	
	private static final long serialVersionUID = 1133157472548992618L;
	private static final Logger logger = LoggerFactory.getLogger(CrcBean.class);
	
	/**
	 * Output elements
	 */
	
	private ModeEnum selectedMode;
	private String selectedCompteComptable;
	private List<SimpleEntity> comptableList;	
	private List<CrcItem> crcList;
	private CrcItem selectedCrc;
	private byte[] globalContent;
	
	
	public ModeEnum getSelectedMode() {
		return selectedMode;
	}

	public void setSelectedMode(ModeEnum selectedMode) {
		this.selectedMode = selectedMode;
	}
	
	public  ModeEnum[] getPrintingMode() {
	    return ModeEnum.values();
	}
	
	public void onModeChange() {
		
	}

	public String getSelectedCompteComptable() {
		return selectedCompteComptable;
	}

	public void setSelectedCompteComptable(String selectedCompteComptable) {
		this.selectedCompteComptable = selectedCompteComptable;
	}

	public List<SimpleEntity> getComptableList() {
		if(comptableList==null) {
			comptableList=getCommonService().getAccountingAccounts(getExerciceInt(),getCodeBudget(),"");
		}
		return comptableList;
	}

	public void setComptableList(List<SimpleEntity> comptableList) {
		this.comptableList = comptableList;
	}
	
    public void onCompteChange() {
		setCrcList(null);
		setGlobalContent(null);
	}
    public boolean isRequiredDataDone() {
    	if (!isCommonRequiredDone())
    		return false;
//    	if (getSelectedMode()==null)
//    		return false;
    	if (getSelectedCompteComptable()==null)
    		return false;

    	return true;
    }

	@Override
	protected void prepare() {
		getComptableList();
		
	}

	@Override
	public void reset() {
		setComptableList(null);
		setSelectedMode(null);
		setListBudgets(null);
		setCodeBudget(null);
		setCrcList(null);
		setSelectedCompteComptable(null);
		setGlobalContent(null);		
	}

	@Override
	protected Logger getLogger() {
		return logger;
	}
	public boolean isPrintingAutorized() {
		
		
		return true;
	}
	public void executeSearch() {
		Action action=getCurrentAction();
		logger.debug("executeSearch: action={}",action);
		switch (action) {
		case CRC_DEPENSE:
			if(isGbcp()) {
				setCrcList(getCpwinDematService().getCrcDp(getExerciceInt(),getCodeBudget(),getSelectedCompteComptable()));
			}
			else {
			setCrcList(getCpwinDematService().getCrcMandat(getExerciceInt(),getCodeBudget(),getSelectedCompteComptable()));
			}
			break;
		case CRC_RECETTE:
			setCrcList(getCpwinDematService().getCrcRecette(getExerciceInt(),getCodeBudget(),getSelectedCompteComptable()));
			break;

		default:
			break;
		}
		loadPjList();		
		
	}
	private void loadPjList() {
		getCpwinDematService().getPjList(getExerciceInt(),getCodeBudget(),getCrcList());
		loadPjContents();
		createCrcContents();
		
	}

	private void createCrcContents() {
		getCrcList().stream()
		.filter(x-> x.getPjCount()>0)
		.forEach(crc -> {			
			crc.setContent(mergePj(crc.getPjList()));
		});
		
	}

	private byte[] mergePj(List<CrcPj> pjList) {
		byte[] result=null;
		if(pjList.size()==1) return pjList.get(0).getContent();
		List<InputStream> inputPdfList =pjList.stream()
				.filter(x -> x.getContent()!=null)
				.sorted(new Comparator<CrcPj>() {
					@Override
					public int compare(CrcPj o1, CrcPj o2) {
						return o1.getPjNumber().compareTo(o2.getPjNumber());
					}
				})
				.map(x-> new ByteArrayInputStream(x.getContent())).collect(Collectors.toList());
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		try {
			FileHelper.mergePdfFiles(inputPdfList, bos);
			result=bos.toByteArray();
		} catch (Exception e) {
			logger.error("mergePj: {}",Util.getErrorMessageFromException(null, e));
		}
		
		return result;
	}

	private void loadPjContents() {
		getCrcList().stream().map(crc -> crc.getPjList()).flatMap(x -> x.stream()).forEach(pj -> {
			try {
//				logger.debug("loadPjList: pj={} ", pj);
				byte[] content = FileHelper.loadFile(pj.getChemin());
//				logger.debug("loadPjList: pj={} --> content ={}", pj, content);
				pj.setContent(content);
			} catch (IOException e) {
				logger.error("loadPjContents: {}",Util.getErrorMessageFromException(null, e));
			}
		});

	}

	public List<CrcItem> getCrcList() {
		return crcList;
	}

	public void setCrcList(List<CrcItem> crcList) {
		this.crcList = crcList;
	}
    public boolean isCompteEnabled() {
    	if (!isCommonRequiredDone())
    		return false;
//    	if (getSelectedMode()==null)
//    		return false;
			
			return true;
		}
	protected String getSavedPath() throws MissingConfiguration {
		return Constant.getProjectRootPath().resolve("CRC").toString();
	}
	protected String getMergedFileName() throws MissingConfiguration {
		return Constant.getGeneratedCRCFileName(getExerciceInt(), getSelectedCompteComptable());
	}

	public CrcItem getSelectedCrc() {
		return selectedCrc;
	}

	public void setSelectedCrc(CrcItem selectedCrc) {
		this.selectedCrc = selectedCrc;
	}
	public StreamedContent getPiecePdfStream() {
		CrcItem crc=getSelectedCrc();
		if((crc==null)||(CollectionUtils.isEmpty(crc.getPjList()))) {
    		return new DefaultStreamedContent();
    	}
		
		return Util.getPdfStream(getSelectedCrc().getContent(),getPieceFileName(),Constant.PDF_CONTENT_TYPE);
		
	}

	public String getPieceFileName() {
		CrcItem crc = getSelectedCrc();
		if(crc==null) return null;
		return String.format(Constant.CRC_PIECE_FileName, crc.getType().name(), getExercice(), getCodeBudget(),
				crc.getPieceNumber());
	}

	public StreamedContent getGlobalPdfStream() {
		logger.debug("getGlobalPdfStream: start");

		return Util.getPdfStream(getGlobalContent(), getGlobalFileName(),Constant.PDF_CONTENT_TYPE);

	}

	public String getGlobalFileName() {
		String type = getCurrentAction() == Action.CRC_RECETTE ? "Recette" : "Depense";

		return String.format(Constant.CRC_FileName, type, getExercice(), getCodeBudget());
	}
//	private StreamedContent getPdfStream(byte[] content, String fileName) {
////		logger.debug("getPdfStream: fileName ={} : start",fileName);
//		StreamedContent stream=new DefaultStreamedContent();
//		FacesContext context = FacesContext.getCurrentInstance();
//		if (context.getCurrentPhaseId() == PhaseId.RENDER_RESPONSE) {
//            // So, we're rendering the HTML. Return a stub StreamedContent so that it will generate right URL.
//            return new DefaultStreamedContent();
//        } else {
//        	if(content==null)return new DefaultStreamedContent();
//        	else {
//        		stream= new DefaultStreamedContent(new ByteArrayInputStream(content), "application/pdf",fileName);
//        	}
//        	
//        }
////		logger.debug("getPdfStream: end");
//        return stream;
//		
//		
//	}
	/**
	 * 
	 * @return true -> DP si non mandat
	 */
	public boolean isGbcp() {
		String configuredValue="0";
		try {
			configuredValue = AppCfgConfig.getInstance().getGbcp(getExerciceInt());
		} catch (MissingConfiguration e) {
			logger.error("isGbcp: {}",Util.getErrorMessageFromException(null, e));
		}
		return configuredValue==null ? false : configuredValue.equals("1");
	}

	public byte[] getGlobalContent() {
		if (globalContent == null) {
			if (getCrcList() == null)
				return null;
			List<InputStream> inputPdfList = getCrcList().stream().filter(x -> x.getContent() != null)
					.map(x -> new ByteArrayInputStream(x.getContent())).collect(Collectors.toList());
			if(CollectionUtils.isEmpty(inputPdfList)) return null;
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			try {
				FileHelper.mergePdfFiles(inputPdfList, bos);
				globalContent = bos.toByteArray();
			} catch (Exception e) {
				logger.error("getGlobalContent: {}",Util.getErrorMessageFromException(null, e));
			}

		}
		//logger.debug("globalContent : {} : end",globalContent);
		return globalContent;
	}

	public void setGlobalContent(byte[] globalContent) {
		this.globalContent = globalContent;
	}

	@Override
	protected void executeTask(String taskName) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected String[] getTasks() {
		// TODO Auto-generated method stub
		return null;
	}

	public IDematService getCpwinDematService() {
		return BudgetHelper.getCpwinDematService();
	}

}
