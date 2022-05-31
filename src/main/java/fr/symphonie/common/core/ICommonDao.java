package fr.symphonie.common.core;

import java.util.List;

import fr.symphonie.cpwin.model.Tiers;
import fr.symphonie.cpwin.model.nantes.EmailStatus;
import fr.symphonie.cpwin.model.nantes.EtudiantStatus;
import fr.symphonie.cpwin.model.pk.SepaBicPK;
import fr.symphonie.cpwin.model.sepa.Actor;
import fr.symphonie.cpwin.model.sepa.Bic;
import fr.symphonie.cpwin.model.sepa.Protocol;
import fr.symphonie.tools.common.model.FileImportTrace;
import fr.symphonie.tools.common.model.ImportPeriod;
import fr.symphonie.tools.meta4dai.model.PaymentItem;
import fr.symphonie.tools.meta4dai.model.Period;
import fr.symphonie.tools.nantes.model.Etudiant;
import fr.symphonie.tools.sct.model.RefundsBatch;
import fr.symphonie.tools.sct.model.RefundsItem;
import fr.symphonie.tools.signature.model.Signature;

public interface  ICommonDao {
	
	<T> T  update(T entity);

	Protocol getProtocol(String protocole);

	List<Bic> findBicList(SepaBicPK pk, String bic);

	<T> void insert(T entity);

	List<Integer> getVagueIds(int exercice, boolean notGenerated);

	List<RefundsItem> getRefundsByVague(Integer noVague);

	List<Actor> findRefundsTiersList(boolean searchAtBegin, String formatedPrefix);

	List<RefundsItem> getRefundsByCustomer(Integer exercice, String customerName);

	List<RefundsBatch> getVagueList(Integer exercice, long crc32);

	List<Period> getMeta4DaiPeriodList(Integer exercice, String codeBudget);

	List<PaymentItem> loadMeta4DaiPaymentData(Integer exercice, String budget, String period);

	int removeMeta4DaiPaymentData(Integer exercice, String codeBudget, String codePeriod);

	void updateEtudiantEmailStatus(String matricule, EmailStatus status);

	List<Etudiant> getEtudiantList(EtudiantStatus status);

	List<Etudiant> findEtudiantList(boolean searchAtBegin, String formatedPrefix);

	int updateEtudiantEmailStatus(EmailStatus from, EmailStatus to);

	List<Signature> getSignatureList(String userCode);

	List<Tiers> findTiersList(boolean searchAtBegin, String formatedPrefix, boolean isStudent);

	List<Integer> getMeta4DaiExercices();

	List<Integer> getVagueExercises();

	List<FileImportTrace> getImportHistoryList(Integer exercice, String moduleName, long crc32);

	List<ImportPeriod> getPeriodList(Integer exercice, String codeBudget, String moduleName);
	List<ImportPeriod> getPeriodList(Integer exercice, String codeBudget, String moduleName,String code);

}
