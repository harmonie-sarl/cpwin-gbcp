package fr.symphonie.budget.core.dao;

import java.util.List;

import fr.symphonie.budget.core.model.DepRecEnum;
import fr.symphonie.budget.core.model.edition.EditionBiBr;
import fr.symphonie.budget.core.model.edition.TabsEnum;
import fr.symphonie.budget.core.model.edition.util.Operation;
import fr.symphonie.budget.core.model.pluri.BudgModification;
import fr.symphonie.budget.core.model.pluri.BudgetCpDest;
import fr.symphonie.budget.core.model.pluri.BudgetPluriannuel;
import fr.symphonie.budget.core.model.pluri.CpGestionnaire;
import fr.symphonie.budget.core.model.pluri.CpModification;
import fr.symphonie.budget.core.model.pluri.CpModificationItem;
import fr.symphonie.budget.core.model.pluri.CreditPaiement;
import fr.symphonie.budget.core.model.pluri.EnvelopBudg;
import fr.symphonie.budget.core.model.pluri.EtatEnum;
import fr.symphonie.budget.core.model.pluri.LigneBudgetaireAE;
import fr.symphonie.budget.core.model.pluri.ModificationCpLigne;




/**
 * 
 * @author JEDIDI SOUHAIB(HARMONIE)
 *
 */
public interface IBudgetPluriannuelDao {

	BudgetPluriannuel getBudget(int numExec, String codeBudget);

	BudgetPluriannuel saveBudget(BudgetPluriannuel budget);

	void insert(Object e);

	void saveEnvelopBudg(EnvelopBudg envelopBudg);
	EnvelopBudg loadEnvelop(final EnvelopBudg envelopBudg, boolean withCpList,
			boolean withLbList, boolean withEnvInterne);

	List<CreditPaiement> loadCreditPaiement(int execCp, String codeBudget, String groupNat);

	List<BudgetCpDest> getBudgetCpDest(int exerciceCp, String codeBudget,
			String groupNat, Integer niveau);

	void saveBudgetCpParDest(List<BudgetCpDest> listBudgetCpDest);

	List<CpGestionnaire> getCpGestionnaireList(int exercice,
			String codeBudget, String groupNat);

	void saveCpGestionnaire(List<CpGestionnaire> listCpGestionnaire);

	void saveModificationCpLigne(ModificationCpLigne m);
	void saveCpModification(CpModification m);

	List<BudgModification> getBudgModifList(int exercice, String typeModif, EtatEnum etatCp);

	List<CpGestionnaire> getCpGestionnaireList(int exercice, String codeBudget,
			String groupNat, String gestionnaire);

	void saveDematBiBr(List<EditionBiBr> bibrList);

	void deleteDematBiBr(int exercice, String codeBudget, int noBiBr);

	List<EditionBiBr> getDematBiBrList(int exercice, String codeBudget, int noBiBr, TabsEnum dematType);

	List<Operation> getDematBiBRTab5(Integer exercice, String codeBudget, Integer niveau);

	void saveOperations(List<Operation> operationsBudg);

	double getCreditOuvert(Integer exercice, String codeBudget, String codeGest);

	Integer getMaxNoLbgCp(int exercice, String codeBudget);
	
	List<CpModification> getCpModifList(int exercice, String codeBudget);

	CpGestionnaire getCpGestionnaire(Integer exercice, String codeBudget, Integer noLbgCp);

	List<CpModificationItem> getItemscpModifsList(Integer exercice, String codeBudget, Integer modification);

	List<LigneBudgetaireAE> getInterneLbList(Integer exercice, DepRecEnum type,String codeBudget);

	LigneBudgetaireAE findLbi(Integer exercice, Integer noLbi);
	
	
}


