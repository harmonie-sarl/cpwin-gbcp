package fr.symphonie.budget.core.dao;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.JdbcTemplate;

import fr.symphonie.budget.core.model.DepRecEnum;
import fr.symphonie.budget.core.model.Ventillation;
import fr.symphonie.budget.core.model.edition.Tab6;
import fr.symphonie.budget.core.model.plt.Ecriture;
import fr.symphonie.budget.core.model.plt.Periode;
import fr.symphonie.budget.core.model.pluri.BudgModification;
import fr.symphonie.budget.core.model.pluri.BudgetCpDest;
import fr.symphonie.budget.core.model.pluri.ComplexEntity;
import fr.symphonie.budget.core.model.pluri.CpGestionnaire;
import fr.symphonie.budget.core.model.pluri.EtatEnum;
import fr.symphonie.budget.core.model.pluri.GlobalSuiviStruct;
import fr.symphonie.budget.core.model.pluri.ModificationCpLigne;
import fr.symphonie.budget.core.model.pluri.SuiviRecetStruct;
import fr.symphonie.cpwin.model.nantes.EtudiantPj;
import fr.symphonie.tools.meta4dai.model.LbData;
import fr.symphonie.util.model.SimpleEntity;

public interface IJdbcHelper {
	public List<String> getExercicesList(boolean onlyNotClosed);
	public List<String> getCodeBudgList(int numExec);
	public List<ComplexEntity> getGroupDestNat(int exercice);
	public List<ComplexEntity> getDestNat(int exercice,String destGrp,String natGrp, String codeBudg);
	public List<SimpleEntity> getProgramList();
	public List<SimpleEntity> getServiceList();
	SimpleEntity getNatureGroupe(String natureGroupe);
	SimpleEntity getDestinationGroupe(String destinationGroupe);
	SimpleEntity getNature(int exercice,String codeNature,  String codeBudg);
	SimpleEntity  getBudgDestination(int exercice,String codeDest,  String codeBudg);	 
	double getDepAe(int exercice, String nat_grp) ;
	double getDepCp(int exercice_ae, String nat_grp, int num_exec_cp);
    double getRecet(int exercice, String nat_grp);
	public List<String> getAllExercieCp();
	public List<SimpleEntity> getDestinationList(int exercice,String groupNat,Integer niveauDestination);
	public void updateBudgetEtatCp(Integer exercice, String etatCp);
	public EtatEnum getBudgetEtatCp(int exercice);
	//public List<SimpleEntity> getGestionnaireList(int exercice, String groupNat);
	public double getRessourceFAE(int exercice, String dep_rec, String natAgr);
	SimpleEntity getGestionnaire(String code) ;
	JdbcTemplate getJdbcTemplate();
	public List<BudgetCpDest> getAeDestinationList(int exercice,String dep_rec);
	public Double getAeDestMontant(int exercice,String dep_rec,String code_dest,String nat_grp);
	List<SimpleEntity> getListFormeSocial();
	List<BudgetCpDest> getRecetDestList(int exercice,String dep_rec);
	public String getCfgconfigValue(String valeur,Integer exercice);
	public List<CpGestionnaire> getCpGestionnaireList(int exercice,
			String codeBudget);
	public double getNoDataBudg1(int exercice, String code_nat);
	public double getNoDataBudg2(int exercice, String code_nat1, String code_nat2);
	public double getNoDataBudg3(int exercice, String code_nat, String dep_rec);
	public double getNoDataBudg4(int exercice, String code_nat);
	List<ComplexEntity> getEnvlpBdgVentilParOrigin(int execCp);
	void updateBudgetModifEtatCp(Integer exercice, String type,	Integer numero, String etatCp);
	List<SimpleEntity> getNatureGroupeList();
	public double getMontantBr(BudgModification selectedBudgModif, Integer noLbgCp);
	double getCumuleBrAvant(BudgModification selectedBudgModif, Integer noLbgCp);
	public List<ModificationCpLigne> getBrVisesList( int exercice, String codeBudg,String nat_grp );
	public Boolean getEtatEditionBI(int exercice);
	public void updateBudgetEditionBiFlag(int exercice, boolean biGenereted);
	public List<SimpleEntity> getCompteList(Integer exercice, String codeBudget, String type);
	public Map<String, Double> getBalance(Integer exercice, String codeBudget, String type);
	public Boolean getEtatCF(Integer exercice, String codeBudget, String type);
	public void updateEtatCF(Integer exercice, String codeBudget, String type, boolean etat);
	public Map<String, Double> getBpDepAe(Integer exercice);
	public Map<String, Double> getMontantDepCP(Integer exercice);
	//public Map<String, Double> getMontantBrDepCP(Integer exercice, int no_br);
	public Map<String, Double> getBrDepAEByNatGrp(Integer exercice, Integer niveau);
	public Map<String, Double> getBrDepCPByNatGrp(Integer exercice, Integer niveau);
	public Map<String, Double> getBrRecette(Integer exercice, Integer niveau);
	public Map<String, Double> getBpRecetByNatGrp(Integer exercice);
	public List<Ventillation> getBrDepAEByDest(Integer exercice, Integer niveau,String dep_rec);
	List<Ventillation> getBrDepCpByDest(Integer exercice, Integer niveau);
	public double getBrNoDataBudg1(int exercice, Integer niveau, String code_nat);
	double getBrNoDataBudg4(int exercice, Integer niveau, String code_nat);
	double getBrNoDataBudg2(int exercice, Integer niveau, String code_nat1, String code_nat2);
	public double getSoldeInitialJanvier(Integer exercice);
	public void cloturerPeriode(Integer exercice, Integer periode, String userName);
	public void updateNextTresoreriePeriode(Integer exercice, Integer periode);
	public List<Periode> getTresoreriePeriodeList(int exercice);
	public List<SimpleEntity> getDestinationTab3List(int exercice,String groupNat);
	Map<String, Double> getBudgetPrevisionnel(Integer exercice);
	public Map<Integer, Double> getCoTresorerieVentille(Integer exercice, Integer periode);
	public Map<String, Double> getAeEngage(Integer exercice, Date dateFin, String tiersNeutralise);
	public Integer getMaxNumeroBr(Integer exercice, String dateFin);
	List<Integer> getNiveauDestinationList(int exercice, DepRecEnum type);
	Integer getNumSeq(Integer exercice, String tableName,String typeJourn);
	public List<SimpleEntity> getGestionnairesList();
	public List<SimpleEntity> getCompteProduitList(Integer exercice, String codeDest, String codeNature,
			String codeProg);
	public List<SimpleEntity> getParamCompteList(int exercice);
	String[] getCtaCompteImput(Integer exercice, String imputHtd);
	public List<String> getExercicesAeList();
	void deleteGtsImport(Integer exercice, Integer periode);
	public 	GlobalSuiviStruct getGlobalSuiviRec(Boolean isTrAant,int numexec, String codeBudget, String codeDirection, String codeService,
			String codeFongDest, String codeDest, String codeProg, String codeNatureGrp, String codeNature
			,Integer noLtr,String codeTiers);
	public Integer getMaxNoEcriture(Integer exercice);
	List<SuiviRecetStruct> getDetailSuiviRec(Boolean isTrAant, int numexec, String codeBudget, String codeDirection,
			String codeService, String codeFongDest, String codeDest, String codeProg, String codeNatureGrp,
			String codeNature, Integer noLtr, String codeTiers);
	public void refreshSimulationData(Integer exercice, Integer periode);
	public List<Ecriture> getSimulationTempData(Integer exercice, Integer periode);
//	public List<SimpleEntity> getRecetteByOrigine(Integer exercice);
	public List<SimpleEntity> getRecetteByOrigine(Integer numexec, String codeBudget, String codeDirection,
			String codeService, String codeFongDest, String codeDest, String codeProg, String codeNatureGrp,
			String codeNature, Integer noLtr, String codeTiers, Integer niveau);
	public void updateSimulationTemp(Ecriture ecriture);
	public void loadTab6FromCtaBalance(Integer exercice, Tab6 tab6);
	double getFinaActifEtat(Integer exercice);
	double getFinaActifTiers(Integer exercice);
	Double getAeEngage(Integer exercice, Integer noLtr, Date dateFin, String tiersNeutralise);
	double getTotCpvint (Integer exercice, String nat_grp);
	public List<String> getDateEchangeList();
	public List<String> getDateReglementList();
	public List<String> getPaysByProtocole(String protocole);
	public Integer getLastNoVague();
	Integer getCompteur(String tableName, Integer exercice, String typeJourn);
	void updateCompteur(Integer exercice, String tableName, String typeJourn, int newCle);
	public List<Integer> verifyEi(Integer exercice, List<Integer> inputEiList);
	public List<LbData> getLbData(Integer exercice, List<Integer> inputLbiList);
	public Integer getMeta4DaiNoLbiStr(Integer exercice);
	public Integer requirementCheck(String dbOjectName, String dbOjectType);
	/**
	 * 
	 * @param exercice
	 * @param groupNat
	 * @return code_gest
	 */
	public List<String> getCodeGestList(Integer exercice, String groupNat);
	public Integer getNumeroEo(Integer exercice, String groupNat);
	public String getcodePRM(Integer exercice, String codeGest);
	public String getModePaie(String countryCode);
	public List<String> verifyEtudiant(List<String> inputKeyList);
	public List<SimpleEntity> getCompteChargeList(Integer exercice, Integer noEnv);
	public List<EtudiantPj> getEtudiantPjList(String matricule);
	public void deleteTiers(String codeTiers, boolean deleteAdresse, boolean deleteIban);
	List<SimpleEntity> getCfgCompteList(Integer exercice);
	public LbData loadLb(Integer exercice, String codeBudget, LbData lbData);
	public void addMeta4DaiNoLbiStr(Integer exercice, Integer numeroLBC);
	String getImputHtd(Integer exercice, Integer noLbi);
	List<Integer> getExercices(boolean onlyNotClosed);
	
	
}