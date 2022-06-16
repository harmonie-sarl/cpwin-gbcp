package fr.symphonie.common.core;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fr.symphonie.budget.core.dao.IJdbcHelper;
import fr.symphonie.budget.core.dao.ps.PsBudgcompterenduImp;
import fr.symphonie.budget.core.dao.ps.PsBudgsuivicpImp;
import fr.symphonie.budget.core.dao.ps.PsBudgsuiviexecdepImp;
import fr.symphonie.budget.core.dao.ps.PsBudgsuivireccptImp;
import fr.symphonie.budget.core.dao.ps.PsBudgsuivisfcptImp;
import fr.symphonie.budget.core.dao.ps.PsCtaRembGenere;
import fr.symphonie.budget.core.dao.ps.PsPaieMeta4DAICree;
import fr.symphonie.budget.core.dao.ps.PsPaieMeta4DAICtrl;
import fr.symphonie.budget.core.dao.ps.PsPaieMeta4DAIList;
import fr.symphonie.budget.core.dao.ps.sf.PsGbcpSFCreeSimple;
import fr.symphonie.budget.core.dao.ps.tiers.PsTiersAdrCree;
import fr.symphonie.budget.core.dao.ps.tiers.PsTiersCree;
import fr.symphonie.budget.core.dao.ps.tiers.PsTiersEtatMaj;
import fr.symphonie.budget.core.dao.ps.tiers.PsTiersIbanCree;
import fr.symphonie.budget.core.model.pluri.LigneBudgetaireAE;
import fr.symphonie.cpwin.model.Tiers;
import fr.symphonie.cpwin.model.nantes.EmailStatus;
import fr.symphonie.cpwin.model.nantes.EtudiantPj;
import fr.symphonie.cpwin.model.nantes.EtudiantStatus;
import fr.symphonie.cpwin.model.pk.SepaBicPK;
import fr.symphonie.cpwin.model.sepa.Actor;
import fr.symphonie.cpwin.model.sepa.Bic;
import fr.symphonie.cpwin.model.sepa.Protocol;
import fr.symphonie.exception.MissingConfiguration;
import fr.symphonie.tools.common.model.FileImportTrace;
import fr.symphonie.tools.common.model.ImportPeriod;
import fr.symphonie.tools.meta4dai.DisplayStruct;
import fr.symphonie.tools.meta4dai.model.LbData;
import fr.symphonie.tools.meta4dai.model.PaymentItem;
import fr.symphonie.tools.meta4dai.model.Period;
import fr.symphonie.tools.nantes.model.Etudiant;
import fr.symphonie.tools.nantes.model.Operation;
import fr.symphonie.tools.nantes.model.SFParams;
import fr.symphonie.tools.sct.model.RefundsBatch;
import fr.symphonie.tools.sct.model.RefundsItem;
import fr.symphonie.tools.signature.model.Signature;
import fr.symphonie.util.HandlerJSFMessage;
import fr.symphonie.util.model.SimpleEntity;
@Service
public class CommonService implements ICommonService {
//	private static final Logger logger =LoggerFactory.getLogger(CommonService.class);
private IJdbcHelper jdbcHelper;
private ICommonDao commonDao;


	@Inject
	public void setJdbcHelper(IJdbcHelper jdbcHelper) {
		this.jdbcHelper = jdbcHelper;
	}
	public IJdbcHelper getJdbcHelper() {
		return jdbcHelper;
	}
		
	@Inject
	public void setCommonDao(ICommonDao commonDao) {
	this.commonDao = commonDao;
}
	
	@Override
	public String getConfigParam(String paramName,Integer exercice) throws MissingConfiguration {
		String value=null;
		try{
		value= jdbcHelper.getCfgconfigValue(paramName,exercice);
		}
		catch(EmptyResultDataAccessException e){
			String error=HandlerJSFMessage.getErrorMessage("configuration.error");
			error=HandlerJSFMessage.formatMessage(error, new Object[]{paramName});
			throw new MissingConfiguration(error);
		}
		return value;
	}
	@Override
	public List<Map<String, Object>> getSuiviCpList(int exercice){
		return PsBudgsuivicpImp.getInstance(jdbcHelper.getJdbcTemplate()).getData(exercice);
	}
	@Override
	public List<Map<String, Object>> getCpDepenseList(int exercice) {
		return  PsBudgsuivisfcptImp.getInstance(jdbcHelper.getJdbcTemplate()).getData(exercice);
	}
	@Override
	public List<Map<String, Object>> getSuiviCompteRecette(int exercice) {
		return PsBudgsuivireccptImp.getInstance(jdbcHelper.getJdbcTemplate()).getData(exercice);
	}
	@Override
	public List<Map<String, Object>> getSuiviEnvelopList(int exercice) {
		return PsBudgsuiviexecdepImp.getInstance(jdbcHelper.getJdbcTemplate()).getData(exercice);
	}
	@Override
	public List<Map<String, Object>> getCompteRenduGest(int exercice, Date dateFin) {
		return PsBudgcompterenduImp.getInstance(jdbcHelper.getJdbcTemplate()).getData(exercice,dateFin);
	}
	@Override
	@Transactional
	public void updateObject(Object obj) {
		commonDao.update(obj);
		
	}
	@Override
	public Integer getNumSeq(int exercice, String tableName,String typeJourn) throws MissingConfiguration{
		Integer numero=null;
		try{
			numero=jdbcHelper.getNumSeq(exercice, tableName,typeJourn);
		}
		catch(EmptyResultDataAccessException e){
			String error=HandlerJSFMessage.getErrorMessage("configuration.error");
			error=HandlerJSFMessage.formatMessage(error, new Object[]{tableName});
			throw new MissingConfiguration(error);
		}
		return numero;
	}
	@Override
	public Protocol getProtocol(String protocole) {
		return commonDao.getProtocol(protocole);
	}
	@Override
	public List<Bic> findBicList(SepaBicPK pk, String bic) {
		return commonDao.findBicList(pk, bic);
	}
	@Override
	public List<String> getDateEchangeList() {
		return jdbcHelper.getDateEchangeList();
	}
	@Override
	public List<String> getDateReglementList() {
		return jdbcHelper.getDateReglementList();
	}
	@Override
	public List<String> getPaysByProtocole(String modePaie){
		return jdbcHelper.getPaysByProtocole(modePaie);
	}
	@Override
	@Transactional
	public void saveSepaPaymentList(List<RefundsItem> paymentList) {
		for(RefundsItem payment: paymentList)
		commonDao.insert(payment);
		
	}
	@Override
	public Integer getLastNoVague() {
		return jdbcHelper.getLastNoVague();
	}
	@Override
	public List<Integer> getVagueExercises() {
		return commonDao.getVagueExercises();
	}
	@Override
	public List<Integer> getVagueIds(int exercice, boolean onlyNotGeneratedVague) {
		return commonDao.getVagueIds(exercice,onlyNotGeneratedVague);
	}
	@Override
	public List<RefundsItem> getRefundsByVague(Integer noVague) {
		return commonDao.getRefundsByVague(noVague);
	}
	@Override
	public List<Actor> findRefundsTiersList(boolean searchAtBegin, String formatedPrefix) {
		return commonDao.findRefundsTiersList(searchAtBegin,formatedPrefix);
	}
	@Override
	public Integer refundsGeneration(Integer exec, String codeBudget, String paymentMode, String paymentDate, int vagueNo) {
		return PsCtaRembGenere.getInstance(jdbcHelper.getJdbcTemplate()).execute(exec, codeBudget, paymentMode, paymentDate, vagueNo);
		
	}
	@Override
	public List<RefundsItem> getRefundsByCustomer(Integer exercice, String customerName) {
		return commonDao.getRefundsByCustomer(exercice,customerName);
	}
	@Override
	public List<RefundsBatch> getVagueList(Integer exercice, long crc32) {
		return commonDao.getVagueList(exercice,crc32);
	}
	@Override
	@Transactional
	public void saveVague(RefundsBatch vague) {
		commonDao.insert(vague);		
	}
	@Override
	public List<String> getListBudgets(Integer exec) {
		return jdbcHelper.getCodeBudgList(exec);
	}
	@Override
	public Integer getLastCounterValue(Integer exercice, String counterName) throws MissingConfiguration {
		Integer value = null;
		try {
			value = jdbcHelper.getCompteur(counterName, exercice, null);
		} catch (EmptyResultDataAccessException e) {
			String error = HandlerJSFMessage.getErrorMessage("configuration.error");
			error = HandlerJSFMessage.formatMessage(error, new Object[] { counterName });
			throw new MissingConfiguration(error);
		}
		return value;
	}
	@Override
	@Transactional
	public void setCounterValue(Integer exercice,String counterName,Integer value) {
		jdbcHelper.updateCompteur(exercice, counterName, null, value);
	}
	@Override
	@Transactional
	public List<Period> getMeta4daiPeriodList(Integer exercice, String codeBudget) {
		
		return commonDao.getMeta4DaiPeriodList(exercice,codeBudget);
	}
	@Override
	public List<PaymentItem> loadMeta4DaiPaymentData(Integer exercice, String budget, String period) {
		return commonDao.loadMeta4DaiPaymentData(exercice,budget,period);
	}
	@Override
	@Transactional
	public <T> void saveList(List<T> list) {
		//for(T item:list) commonDao.insert(item);
		commonDao.saveList(list);
		
	}
	@Override
	@Transactional
	public <T> void createOrUpdateList(List<T> list) {
		for(T item:list) commonDao.update(item);
		
	}
	@Override
	public List<Integer> verifyEi(Integer exercice, List<Integer> inputEiList) {
		return jdbcHelper.verifyEi(exercice,inputEiList);
	}
	@Override
	public Integer executeMeta4DaiCheck(Integer exercice, String codeBudget, String codePeriod) {
		
		return PsPaieMeta4DAICtrl.getInstance(jdbcHelper.getJdbcTemplate()).execute(exercice, codeBudget, codePeriod);
	}
	@Override
	public List<LbData> getLbData(Integer exercice, List<Integer> inputLbiList) {
		return jdbcHelper.getLbData(exercice,inputLbiList);
	}
	@Override
	@Transactional
	public int removeMeta4DaiPaymentData(Integer exercice, String codeBudget, String codePeriod) {
		return commonDao.removeMeta4DaiPaymentData(exercice,codeBudget,codePeriod);
		
	}
	@Override
	public Integer getMeta4DaiNoLbiStr(Integer exercice) {
		return jdbcHelper.getMeta4DaiNoLbiStr(exercice);
	}
	@Override
	public void executeMeta4DaiValidate(Integer exercice, String codeBudget, String codePeriod) {
		PsPaieMeta4DAICree.getInstance(jdbcHelper.getJdbcTemplate()).execute(exercice, codeBudget, codePeriod);
		
	}
	@Override
	public List<DisplayStruct> executeMeta4DaiList(Integer exercice) {
		return PsPaieMeta4DAIList.getInstance(jdbcHelper.getJdbcTemplate()).execute(exercice);
	}
	@Override
	public Integer requirementCheck(String dbOjectName, String dbOjectType) {
		return jdbcHelper.requirementCheck(dbOjectName,dbOjectType);
	}
	@Override
	@Transactional
	public void updateEtudiantEmailStatus(Etudiant etudiant, EmailStatus status) {
		commonDao.updateEtudiantEmailStatus(etudiant.getMatricule(),status);
		
	}
	@Override
	public List<Etudiant> getEtudiantList(EtudiantStatus status) {
		return commonDao.getEtudiantList(status);
	}
	@Override
	public String createTiers(Etudiant etudiant) {
		String codeTiers = null;
		try {
		codeTiers=PsTiersCree.getInstance(jdbcHelper.getJdbcTemplate()).createTiersForEtudiant(etudiant);
		Integer numeroAdress=PsTiersAdrCree.getInstance(jdbcHelper.getJdbcTemplate()).createAdr(codeTiers, etudiant.getAdresse());
		etudiant.getAdresse().setNumero(numeroAdress);
		Integer numeroIban=PsTiersIbanCree.getInstance(jdbcHelper.getJdbcTemplate()).createIban(codeTiers, etudiant.getIban());
		etudiant.getIban().setNumero(numeroIban);
		PsTiersEtatMaj.getInstance(jdbcHelper.getJdbcTemplate()).setOuvert(codeTiers);
		}
		catch(Exception e) {
			if(codeTiers!=null) {
				jdbcHelper.deleteTiers(codeTiers,true,true);
			}
			throw e;
		}
		
		return codeTiers;
		
	}
	@Override
	public List<Etudiant> findEtudiantList(boolean searchAtBegin, String formatedPrefix) {
		return commonDao.findEtudiantList(searchAtBegin,formatedPrefix);
	}
	@Override
	public void createSF(SFParams sfParams,LigneBudgetaireAE selectedLb, List<Operation> operationList) {
		Integer no_pre_sf=null;
		PsGbcpSFCreeSimple ps=PsGbcpSFCreeSimple.getInstance(jdbcHelper.getJdbcTemplate());
		for(Operation op:operationList) {
			no_pre_sf=ps.createSF(sfParams,selectedLb,op);
			op.setNoDp(no_pre_sf);
		}
		
	}
	@Override
	public SFParams loadSfParams(Integer exercice, String groupNat,String codeGest) {
		//recherche de l'EO 
		Integer noEO=jdbcHelper.getNumeroEo(exercice,groupNat);
		//recherche PRM
		String codePRM=jdbcHelper.getcodePRM(exercice,codeGest);
		return new SFParams(codeGest, noEO, codePRM);
	}
	@Override
	public String getModePaie(String countryCode) {
		String modePaie=jdbcHelper.getModePaie(countryCode);
		return modePaie;
	}
	@Override
	public List<String> verifyEtudiant(List<String> inputKeyList) {
		return jdbcHelper.verifyEtudiant(inputKeyList);
	}
	@Override
	public List<EtudiantPj> getEtudiantPjList(String matricule) {
		return jdbcHelper.getEtudiantPjList(matricule);
	}
	
	@Override
	public List<SimpleEntity> getAccountingAccounts(Integer exercice, String codeBudget, String type) {
		return jdbcHelper.getCfgCompteList(exercice);
	}
	@Override
	@Transactional
	public int updateEtudiantEmailStatus(EmailStatus from, EmailStatus to) {
		return commonDao.updateEtudiantEmailStatus(from,to);
		
	}
	
	@Override
	@Transactional
	public List<Signature> getSignatureList(String userCode) {
		return commonDao.getSignatureList(userCode);
	}
	@Override
	@Transactional
	public void saveSignature(Signature signature) {
		if(signature.getId()==null) {
			commonDao.insert(signature);
		}
		else {
			commonDao.update(signature);
		}
		
		
	}
	@Override
	public List<Tiers> findTiersList(boolean searchAtBegin, String formatedPrefix, boolean isStudent) {
		
		return commonDao.findTiersList( searchAtBegin,  formatedPrefix,  isStudent);
	}
	@Override
	public LbData loadLb(Integer exercice, String codeBudget,LbData lbData) {
		return jdbcHelper.loadLb(exercice,codeBudget,lbData);
	}

	@Override
	@Transactional
	public void initializeMeta4Dai(Integer exercice, LbData newLBC, List<Period> periodList) {
		addMeta4DaiNoLbiStr(exercice,newLBC.getNumero());
		periodList.stream().forEach(x -> commonDao.insert(x) );		
	}
	private void addMeta4DaiNoLbiStr(Integer exercice, Integer numeroLBC) {
		jdbcHelper.addMeta4DaiNoLbiStr(exercice,numeroLBC);
		
	}
	@Override
	public String getImputHtd(Integer exercice, Integer noLbi) {
		return jdbcHelper.getImputHtd(exercice, noLbi);
	}
	@Override
	public List<Integer> getOpenedExerciceList() {
		return jdbcHelper.getExercices(true);
	}
	@Override
	public List<Integer> getMeta4DaiExercices() {
		return commonDao.getMeta4DaiExercices();
	}
	@Override
	public List<FileImportTrace> getImportHistoryList(Integer exercice, String moduleName, long crc32) {
		return commonDao.getImportHistoryList(exercice,moduleName,crc32);
	}
	@Override
	@Transactional
	public void saveImportTrace(FileImportTrace vague) {
		commonDao.insert(vague);	
		
	}
	@Override
	public List<ImportPeriod> getPeriodList(Integer exercice, String codeBudget, String moduleName) {
		return commonDao.getPeriodList(exercice,codeBudget,moduleName);
	}
	
}
