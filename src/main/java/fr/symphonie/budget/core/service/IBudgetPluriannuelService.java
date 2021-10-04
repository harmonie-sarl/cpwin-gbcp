package fr.symphonie.budget.core.service;

import java.util.List;
import java.util.Map;

import fr.symphonie.budget.core.model.DepRecEnum;
import fr.symphonie.budget.core.model.Ventillation;
import fr.symphonie.budget.core.model.edition.EditionBiBr;
import fr.symphonie.budget.core.model.edition.Tab5;
import fr.symphonie.budget.core.model.edition.TabsEnum;
import fr.symphonie.budget.core.model.edition.util.Operation;
import fr.symphonie.budget.core.model.pluri.BudgModification;
import fr.symphonie.budget.core.model.pluri.BudgetCpDest;
import fr.symphonie.budget.core.model.pluri.BudgetPluriannuel;
import fr.symphonie.budget.core.model.pluri.ComplexEntity;
import fr.symphonie.budget.core.model.pluri.CpGestionnaire;
import fr.symphonie.budget.core.model.pluri.CpModification;
import fr.symphonie.budget.core.model.pluri.CpModificationItem;
import fr.symphonie.budget.core.model.pluri.CreditPaiement;
import fr.symphonie.budget.core.model.pluri.EnvelopBudg;
import fr.symphonie.budget.core.model.pluri.EtatEnum;
import fr.symphonie.budget.core.model.pluri.GlobalSuiviStruct;
import fr.symphonie.budget.core.model.pluri.LigneBudgetaireAE;
import fr.symphonie.budget.core.model.pluri.ModificationCpLigne;
import fr.symphonie.budget.core.model.pluri.SuiviAeCpStruct;
import fr.symphonie.budget.core.model.pluri.SuiviPluriStruct;
import fr.symphonie.budget.core.model.pluri.SuiviRecetStruct;
import fr.symphonie.budget.core.model.pluri.suiviCpStruct;
import fr.symphonie.util.model.SimpleEntity;

public interface IBudgetPluriannuelService {

	BudgetPluriannuel searchBudgetForSaisi(int numExec, String codeBudget);

	BudgetPluriannuel searchBudgetForAdressage(int numExec, String codeBudget);

	List<SimpleEntity> getAllPrograms();
	List<SimpleEntity> getAllServices();

	List<String> getAllExercieAe();

	

	List<ComplexEntity> getGroupDestList(int exercice);

	List<String> getListBudgets(int numExec);

	BudgetPluriannuel saveBudget(BudgetPluriannuel budget);

	SimpleEntity getNatureGroupe(String natureGroupe);

	SimpleEntity getDestinationGroupe(String destinationGroupe);
	
	 List<ComplexEntity> getDestNat(int exercice,String destGrp, String natGrp , String codeBudg);
	 SimpleEntity getNature(int exercice,String codeNature,  String codeBudg);
	 SimpleEntity getDestination(int exercice,String codeDest,  String codeBudg);
	void saveEnvelopBudg(EnvelopBudg envelopBudg);

	EnvelopBudg loadEnvelop(final EnvelopBudg envelopBudg, boolean withCpList,
			boolean withLbList, boolean withEnvInterne);

	SimpleEntity getBudgDestination(int exercice, String codeDest,
			String codeBudg);

	void validerImport(List<EnvelopBudg> listEnvelopBudgs);
	 double getDepAe(int exercice, String nat_grp);
	 double getDepCp(int exercice_ae, String nat_grp, int num_exec_cp) ;	
	 double getRecet(int exercice, String nat_grp) ;

	List<String> getAllExercieCp();
	List<CreditPaiement> loadCreditPaiement(int execCp, String codeBudget, String groupNat);

	List<SimpleEntity> getDestinationList(int exercice,String groupNat, Integer niveauDestination);

	List<BudgetCpDest> getBudgetCpDest(int exerciceCp, String codeBudget,
			String groupNat, Integer niveau);

	void saveBudgetCpParDest(List<BudgetCpDest> listBudgetCpDest);
	EtatEnum getBudgetEtatCp(int exerciceCp);

	List<CpGestionnaire> getCpGestionnaireList(int exerciceCp,
			String codeBudget, String groupNat);

//	List<SimpleEntity> getGestionnaireList(int exercice, String groupNat);
    double getRessourceFAE(int exercice, String dep_rec, String natAgr);

	void saveCpGestionnaire(List<CpGestionnaire> listCpGestionnaire);
	SimpleEntity getGestionnaire(String code);

	void updateBudgetEtatCp(int exercice, String etatCp);

	List<CpGestionnaire> getCpGestData(int exercice, String codeBudget,
			String groupNat);
    List<BudgetCpDest> getAeDestinationList(int exercice,String dep_rec);
    Double getAeDestMontant(int exercice,String dep_rec,String code_dest,String nat_grp);

	void saveDeploiement(CpModification modification,List<CpGestionnaire> listCpGestionnaire);

	List<BudgetCpDest> getRecetDestList(int exercice, String dep_rec);
	//String getCfgconfigValue(String valeur,Integer exercice);

	List<BudgModification> getBudgModifList(int exercice, String typeModif, EtatEnum etatCp);

	List<CpGestionnaire> getCpGestionnaireList(int exercice, String codeBudget);

	void saveCreditBR(List<CpGestionnaire> updatedList, List<ModificationCpLigne> modificationList);
    double getNoDataBudg1(int exercice, String code_nat);
    double getNoDataBudg2(int exercice, String code_nat1, String code_nat2);
    double getNoDataBudg3(int exercice, String code_nat, String dep_rec);
    double getNoDataBudg4(int exercice, String code_nat);

	String getGestUnique(Integer exercice);

	List<CpGestionnaire> getCpGestionnaireList(int exerciceCP,
			String codeBudget, String groupNat, String gestionnaire);
    

	List<ComplexEntity> getEnvlpBdgVentilParOrigin(int execCp);

	List<String> getListExecSuiviCp();	

	List<String> getListBudgetSuiviCp(int execCp);

	List<SimpleEntity> getListDirectionsSuiviCp(int execCp, String codeBudget);

	List<SimpleEntity> getListServicesSuiviCp(int execCp, String codeBudget, String codeDirection);

	List<SimpleEntity> getListFongDestSuiviCp(int execCp, String codeBudget);

	List<SimpleEntity> getListDestinationSuiviCp(int numexec, String codeBudg, String codeFongDest,
			String codeDirection, String codeService);

	List<SimpleEntity> getListProgrammeSuiviCp(int numexec, String codeBudg, String codeFongDest, String codeDest,
			String codeDirection, String codeService);

	List<SimpleEntity> getListNatureGroupeSuiviCp(int numexec, String codeBudg, String codeFongDest, String codeDest, String codeNature);

	List<SimpleEntity> getListNatureSuiviCp(int numexec, String codeBudg, String codeFongDest, String codeDest,	String codeProg, String codeNatureGrp,String codeDirection, String codeService);


	Map<String, Object> getGlobalSuiviCp(int numexec, String codeBudget, String codeDirection, String codeService,
			String codeFongDest, String codeDest, String codeProg, String codeNatureGrp, String codeNature
			 );
	
	List<suiviCpStruct> getSuiviCpAeAnt(int numexec, String codeBudget, String codeDirection,
			String codeService, String codeFongDest, String codeDest, String codeProg, String codeNatureGrp,
			String codeNature, Integer tiersOrigine);
	
	List<suiviCpStruct> getSuiviCpAeExec(int numexec, String codeBudget, String codeDirection,
			String codeService, String codeFongDest, String codeDest, String codeProg, String codeNatureGrp,
			String codeNature, Integer tiersOrigine);

	void updateBudgetModifEtatCp(Integer exercice, String type, Integer numero, String etatCp);
	List<SimpleEntity> getNatureGroupeList();

	double getMontantBr(BudgModification selectedBudgModif, Integer noLbgCp);

	double getCumuleBrAvant(BudgModification selectedBudgModif, Integer noLbgCp);
	
	List<ModificationCpLigne> getBrVisesList( int exercice, String codeBudg,String nat_grp );

	Boolean getEtatEditionBI(int exercice);

	void updateBudgetEditionBiFlag(int exercice, boolean biGenereted);

	void saveDematBiBr(int exercice, String codeBudget, int noBiBr, List<EditionBiBr> bibrList);

	void deleteDematBiBr(int exercice, String codeBudget, int noBiBr);

	List<EditionBiBr> getDematBiBrList(int exercice, String codeBudget, int noBiBr, TabsEnum dematType);

	List<Operation> getDematBiBRTab5(Integer exercice, String codeBudget, Integer niveau);

	void saveDematBiBrTab5(Tab5 tab5);

	Map<String, Double> getBpDepAe(Integer exercice);

	Map<String, Double> getMontantDepCP(Integer exercice);

	//Map<String, Double> getMontantBrDepCP(Integer exercice, int niveau);

	Map<String, Double> getBrDepAEByNatGrp(Integer exercice, Integer niveau);

	Map<String, Double> getBrDepCPByNatGrp(Integer exercice, Integer niveau);

	Map<String, Double> getBrRecette(Integer exercice, Integer niveau);

	Map<String, Double> getBpRecetByNatGrp(Integer exercice);

	List<Ventillation> getBrDepAEByDest(Integer exercice, Integer niveau,String dep_rec);

	List<Ventillation> getBrDepCpByDest(Integer exercice, Integer niveau);

	double getBrNoDataBudg1(int exercice, Integer niveau, String string);

	double getBrNoDataBudg2(int exercice, int niveau, String code_nat1, String code_nat2);

	double getBrNoDataBudg4(int exercice, Integer niveau, String code_nat1);
	
	List<SimpleEntity> getDestinationTab3List(int exercice,String groupNat);

	List<Integer> getNivDestList(Integer exercice, DepRecEnum depense);
	double getCreditOuvert(Integer exercice, String codeBudget, String codeGest);

	Integer getMaxNoLbgCp(int exercice, String codeBudget);
	public List<SimpleEntity> getGestionnairesList();
	List<CpModification> getCpModificationList(int exercice,
			String codeBudget);

	CpGestionnaire getCpGestionnaire(Integer exercice, String codeBudget, Integer noLbgCp);

	 List<CpModificationItem> getItemscpModifsList(int exerciceCp, String codeBudget, Integer modification);

	List<LigneBudgetaireAE> getInterneLbList(Integer exercice, DepRecEnum type,String codeBudget);

	List<SimpleEntity> getCompteProduitList(Integer exercice, String codeDest, String codeNature, String codeProg);

	List<String> getAllExercice(boolean onlyNotClosed);
	
	GlobalSuiviStruct getGlobalSuiviRec(Boolean isTrAant,int numexec, String codeBudget, String codeDirection, String codeService,
			String codeFongDest, String codeDest, String codeProg, String codeNatureGrp, String codeNature
			,Integer noLtr ,String codeTiers);

		Integer getMaxNoEcriture(Integer exercice);

		void refreshRecetteRecouveData(Integer exercice);

		List<SuiviRecetStruct> getDetailSuiviRec(Boolean isTrAant, int numexec, String codeBudget, String codeDirection,
				String codeService, String codeFongDest, String codeDest, String codeProg, String codeNatureGrp,
				String codeNature, Integer noLtr, String codeTiers);

		LigneBudgetaireAE getLbi(Integer exercice, Integer noLbi);

		List<SimpleEntity> getRecetteByOrigine(Integer  numexec, String codeBudget, String codeDirection, String codeService,
				String codeFongDest, String codeDest, String codeProg, String codeNatureGrp, String codeNature
				,Integer noLtr ,String codeTiers, Integer niveau);

		List<SuiviAeCpStruct> getSuiviAeCp(int numexec, String codeNatureGrp);
		List<SuiviPluriStruct> getSuiviPluriEj(int numexec, String codeNatureGrp);
		double getSuiviAeCpTotCPVote(int numexec, String codeNatureGrp);

		List<String> getCodeGestList(Integer exercice, String groupNat);

		String[] getCtaCompteImput(Integer exercice, String compteProduit);

		List<SimpleEntity> getCompteChargeList(Integer exercice, Integer noEnv);
}

