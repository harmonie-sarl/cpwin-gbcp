package fr.symphonie.common.core;

import java.util.Date;
import java.util.List;
import java.util.Map;

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
import fr.symphonie.util.model.SimpleEntity;

public interface ICommonService {
public String getConfigParam(String paramName,Integer exercice) throws MissingConfiguration;

List<Map<String, Object>> getSuiviCpList(int exercice);

public List<Map<String, Object>> getCpDepenseList(int exercice);

public List<Map<String, Object>> getSuiviCompteRecette(int exercice);

public List<Map<String, Object>> getSuiviEnvelopList(int exercice);

public List<Map<String, Object>> getCompteRenduGest(int exercice, Date dateFin);

void updateObject(Object obj);

Integer getNumSeq(int exercice, String tableName,String typeJourn) throws MissingConfiguration;
Protocol getProtocol(String protocole);

List<Bic> findBicList(SepaBicPK pk, String bic);

public List<String> getDateEchangeList();

public List<String> getDateReglementList();

List<String> getPaysByProtocole(String protocole);

public void saveSepaPaymentList(List<RefundsItem> paymentList);

public Integer getLastNoVague();
/**
 * Charger la liste des ids des vagues
 * @param exercice
 * @param onlyNotGeneratedVague: if true load only not generated vagues
 * @return
 */

public List<Integer> getVagueIds(int exercice, boolean onlyNotGeneratedVague);

public List<RefundsItem> getRefundsByVague(Integer noVague);

public List<Actor> findRefundsTiersList(boolean searchAtBegin, String formatedPrefix);

public Integer refundsGeneration(Integer exec, String codeBudget, String paymentMode, String dateReglement, int i);

public List<RefundsItem> getRefundsByCustomer(Integer exec, String customerName);

public List<RefundsBatch> getVagueList(Integer exercice, long crc32);

public void saveVague(RefundsBatch vague);

public List<String> getListBudgets(Integer exec);

Integer getLastCounterValue(Integer exercice, String counterName) throws MissingConfiguration;

void setCounterValue(Integer exercice, String counterName, Integer value);

public List<Period> getMeta4daiPeriodList(Integer exec, String codeBudget);

public List<PaymentItem> loadMeta4DaiPaymentData(Integer exercice, String budget, String periode);
public <T> void saveList(List<T> list);

public List<Integer> verifyEi(Integer exercice, List<Integer> inputEiList);

public Integer executeMeta4DaiCheck(Integer exercice, String codeBudget, String codePeriod);

public List<LbData> getLbData(Integer exercice, List<Integer> inputLbiList);

public int removeMeta4DaiPaymentData(Integer exercice, String codeBudget, String codePeriod);

public Integer getMeta4DaiNoLbiStr(Integer exercice);

public void executeMeta4DaiValidate(Integer exercice, String codeBudget, String codePeriod);

public List<DisplayStruct> executeMeta4DaiList(Integer exercice);

public Integer requirementCheck(String dbOjectName, String dbOjectType);

public void updateEtudiantEmailStatus(Etudiant etudiant, EmailStatus status);

public List<Etudiant> getEtudiantList(EtudiantStatus status);

public String createTiers(Etudiant x);

List<Etudiant> findEtudiantList(boolean searchAtBegin, String formatedPrefix);

public void createSF(SFParams sfParams,LigneBudgetaireAE selectedLb, List<Operation> operationList);

public SFParams loadSfParams(Integer exec, String groupNat, String codeGest);

public String getModePaie(String countryCode);

public List<String> verifyEtudiant(List<String> inputKeyList);

public List<EtudiantPj> getEtudiantPjList(String matricule);

<T> void createOrUpdateList(List<T> list);

public List<SimpleEntity> getAccountingAccounts(Integer exercice, String codeBudget, String type);

public int updateEtudiantEmailStatus(EmailStatus from, EmailStatus to);

public List<Signature> getSignatureList(String userCode);

public void saveSignature(Signature selectedSignature);

public List<Tiers> findTiersList(boolean searchAtBegin, String formatedPrefix, boolean isStudent);

public LbData loadLb(Integer exercice, String codeBudget, LbData lbData);

public void initializeMeta4Dai(Integer exercice, LbData newLBC, List<Period> periodList);

public String getImputHtd(Integer exercice, Integer numero);

public List<Integer> getOpenedExerciceList();
List<Integer> getMeta4DaiExercices();

List<Integer> getVagueExercises();

// FileImport trace
public List<FileImportTrace> getImportHistoryList(Integer exercice,String moduleName, long crc32);

public void saveImportTrace(FileImportTrace vague);

public List<ImportPeriod> getPeriodList(Integer exercice, String codeBudget, String moduleName);


}

