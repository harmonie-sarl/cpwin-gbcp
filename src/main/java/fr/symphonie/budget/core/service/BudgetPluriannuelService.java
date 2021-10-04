package fr.symphonie.budget.core.service;

import static fr.symphonie.budget.core.dao.ps.ColumnName.code_budg_col;
import static fr.symphonie.budget.core.dao.ps.ColumnName.code_dest_col;
import static fr.symphonie.budget.core.dao.ps.ColumnName.code_direction_col;
import static fr.symphonie.budget.core.dao.ps.ColumnName.code_nature_col;
import static fr.symphonie.budget.core.dao.ps.ColumnName.code_prog_col;
import static fr.symphonie.budget.core.dao.ps.ColumnName.code_service_col;
import static fr.symphonie.budget.core.dao.ps.ColumnName.dest_grp_col;
import static fr.symphonie.budget.core.dao.ps.ColumnName.nat_grp_col;
import static fr.symphonie.budget.core.dao.ps.ColumnName.nom_dest_col;
import static fr.symphonie.budget.core.dao.ps.ColumnName.nom_dest_grp_col;
import static fr.symphonie.budget.core.dao.ps.ColumnName.nom_direction_col;
import static fr.symphonie.budget.core.dao.ps.ColumnName.nom_nat_grp_col;
import static fr.symphonie.budget.core.dao.ps.ColumnName.nom_nature_col;
import static fr.symphonie.budget.core.dao.ps.ColumnName.nom_prog_col;
import static fr.symphonie.budget.core.dao.ps.ColumnName.nom_service_col;
import static fr.symphonie.budget.core.dao.ps.ColumnName.num_exec_col;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fr.symphonie.budget.core.dao.IBudgetPluriannuelDao;
import fr.symphonie.budget.core.dao.IJdbcHelper;
import fr.symphonie.budget.core.dao.ps.PsBudgListe;
import fr.symphonie.budget.core.dao.ps.PsBudgSuiviAECP;
import fr.symphonie.budget.core.dao.ps.PsDestGrpListe;
import fr.symphonie.budget.core.dao.ps.PsDestListe;
import fr.symphonie.budget.core.dao.ps.PsDirListe;
import fr.symphonie.budget.core.dao.ps.PsExecListe;
import fr.symphonie.budget.core.dao.ps.PsGbcpSuiviCP;
import fr.symphonie.budget.core.dao.ps.PsNatGrpListe;
import fr.symphonie.budget.core.dao.ps.PsNatListe;
import fr.symphonie.budget.core.dao.ps.PsProgListe;
import fr.symphonie.budget.core.dao.ps.PsRecRecouvMaj;
import fr.symphonie.budget.core.dao.ps.PsServListe;
import fr.symphonie.budget.core.dao.ps.PsbudgCpbiGest;
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
import fr.symphonie.common.util.BudgetHelper;
import fr.symphonie.util.model.SimpleEntity;

@Service(value="budgetPluriService")
public class BudgetPluriannuelService implements IBudgetPluriannuelService {
	
	
	private static final Logger logger = LoggerFactory.getLogger(BudgetPluriannuelService.class);

	private IBudgetPluriannuelDao dao;
	
	private IJdbcHelper jdbcHelper;
	
	   @Inject
		public void setJdbcHelper(IJdbcHelper jdbcHelper) {
			this.jdbcHelper = jdbcHelper;
		}

	@Inject
	public void setDao(IBudgetPluriannuelDao dao) {
		this.dao = dao;
	}
	@Override
	@Transactional(readOnly=true)
	public BudgetPluriannuel searchBudgetForSaisi(int numExec, String codeBudget)
	{
		return dao.getBudget(numExec,codeBudget);
	}
	@Override
	@Transactional(readOnly=true)
	public BudgetPluriannuel searchBudgetForAdressage(int numExec, String codeBudget)
	{
		return dao.getBudget(numExec,codeBudget);
	}
	
	@Override
	public List<SimpleEntity> getAllPrograms() {
		return 	  jdbcHelper.getProgramList();

	}
	@Override
	public List<SimpleEntity> getAllServices() {
		return 	  jdbcHelper.getServiceList();
	}
	@Override
	public List<String> getAllExercieAe() {
		return jdbcHelper.getExercicesAeList();	
	}
	@Override
	public List<String> getAllExercice(boolean onlyNotClosed) {
		return jdbcHelper.getExercicesList(onlyNotClosed);	
	
	}
	@Override
	public List<String> getListBudgets(int numExec) {	
		return 	  jdbcHelper.getCodeBudgList(numExec);	
	}
	@Override
	public List<ComplexEntity> getGroupDestList(int exercice){	
		return 	  jdbcHelper.getGroupDestNat(exercice);
	}

	@Override
	@Transactional
	public BudgetPluriannuel saveBudget(BudgetPluriannuel budget) {
		return dao.saveBudget(budget);
		
	}
	@Override
	public SimpleEntity getNatureGroupe(String natureGroupe)
	{
		return jdbcHelper. getNatureGroupe( natureGroupe);
	}
	@Override
	public SimpleEntity getDestinationGroupe(String destinationGroupe)
	{
		return jdbcHelper.getDestinationGroupe( destinationGroupe);
	}
	@Override
	public List<ComplexEntity> getDestNat(int exercice,String destGrp, String natGrp, String codeBudg)
	{
		return jdbcHelper.getDestNat( exercice, destGrp, natGrp,  codeBudg);
	}

	@Override
	@Transactional
	public void saveEnvelopBudg(EnvelopBudg envelopBudg) {
		saveEnvelopBudgNotTransact(envelopBudg);
	}
	
	private void saveEnvelopBudgNotTransact(EnvelopBudg envelopBudg) {
		logger.info("saveEnvelopBudg: "+envelopBudg +" - start");
		dao.saveEnvelopBudg(envelopBudg);
		logger.info("saveEnvelopBudg:  - end");
	}
	@Override
	public SimpleEntity getNature(int exercice, String codeNature,String codeBudg) {
		return jdbcHelper.getNature(exercice, codeNature, codeBudg);
	}

	@Override
	public SimpleEntity getDestination(int exercice, String codeDest,String codeBudg) {
		return jdbcHelper.getNature(exercice, codeDest, codeBudg);
	}
	@Override
	public EnvelopBudg loadEnvelop(final EnvelopBudg envelopBudg, boolean withCpList,
			boolean withLbList, boolean withEnvInterne) {
		return dao.loadEnvelop(envelopBudg,withCpList,withLbList,withEnvInterne);
	}
	
	@Override
	public SimpleEntity  getBudgDestination(int exercice,String codeDest,  String codeBudg)
	 {
		return jdbcHelper.getBudgDestination( exercice, codeDest,   codeBudg);
	}
	@Override
	@Transactional
	public void validerImport(List<EnvelopBudg> listEnvelopBudgs)
	{
		for (EnvelopBudg envelopBudg : listEnvelopBudgs) {
			saveEnvelopBudgNotTransact(envelopBudg);
		}
	}
	
	@Override
	@Transactional
	public double getDepAe(int exercice, String nat_grp) {		
		return jdbcHelper. getDepAe( exercice,  nat_grp);
	}
	@Override
	@Transactional
	public double getDepCp(int exercice_ae, String nat_grp, int num_exec_cp) {		
		return jdbcHelper.getDepCp( exercice_ae,  nat_grp,  num_exec_cp);
	}
	
	@Override
	@Transactional
	public double getRecet(int exercice, String nat_grp) {		
	return jdbcHelper.getRecet( exercice,  nat_grp);
	}

	@Override
	public List<String> getAllExercieCp() {
		return jdbcHelper.getAllExercieCp();
	}

	@Override
	public List<CreditPaiement> loadCreditPaiement(int execCp, String codeBudget, String groupNat) {
		return dao.loadCreditPaiement(execCp, codeBudget,groupNat);
	}

	@Override
	@Transactional(readOnly=true)
	public List<SimpleEntity> getDestinationList(int exercice,String groupNat, Integer niveauDestination) {

		return jdbcHelper.getDestinationList(exercice,groupNat,niveauDestination);
	}

	@Override
	public List<BudgetCpDest> getBudgetCpDest(int exerciceCp,
			String codeBudget, String groupNat, Integer niveau) {
		return dao.getBudgetCpDest(exerciceCp,codeBudget,groupNat,niveau);
	}

	@Override
	@Transactional
	public void saveBudgetCpParDest(List<BudgetCpDest> listBudgetCpDest) {
		dao.saveBudgetCpParDest(listBudgetCpDest);
	}
	@Override
	public EtatEnum getBudgetEtatCp(int exercice) {
		return jdbcHelper.getBudgetEtatCp(exercice);
	}

	@Override
	public List<CpGestionnaire> getCpGestionnaireList(int exerciceCp,
			String codeBudget, String groupNat) {
		return dao.getCpGestionnaireList(exerciceCp,codeBudget,groupNat);
	}

//	@Override
//	public List<SimpleEntity> getGestionnaireList(int exercice, String groupNat) {
//		return jdbcHelper.getGestionnaireList(exercice,groupNat);
//	}

	@Override
	@Transactional
	public double getRessourceFAE(int exercice, String dep_rec, String natAgr) {
		
		return jdbcHelper.getRessourceFAE(exercice, dep_rec, natAgr);
	}

	@Override
	@Transactional
	public void saveCpGestionnaire(List<CpGestionnaire> listCpGestionnaire) {
		dao.saveCpGestionnaire(listCpGestionnaire);
		
	}
	@Override
	@Transactional
	public SimpleEntity getGestionnaire(String code)
	{
		return jdbcHelper.getGestionnaire(code);
	}
	
	@Override
	@Transactional
	public void updateBudgetEtatCp(int exercice, String etatCp) {
		jdbcHelper.updateBudgetEtatCp(exercice, etatCp);
		
	}

	@Override
	public List<CpGestionnaire> getCpGestData(int exercice, String codeBudget,
			String groupNat) {
		return PsbudgCpbiGest.getInstance(jdbcHelper.getJdbcTemplate()).getbudgCpGest(exercice, codeBudget, groupNat);
		
		
	}

	@Override
	public List<BudgetCpDest> getAeDestinationList(int exercice, String dep_rec) {
		return jdbcHelper.getAeDestinationList(exercice,  dep_rec);
	}

	@Override
	public Double getAeDestMontant(int exercice, String dep_rec,
			String code_dest,String nat_grp) {
		return jdbcHelper.getAeDestMontant(exercice,  dep_rec, code_dest,nat_grp);
	}	
	@Override
	@Transactional
	public	void saveDeploiement(CpModification modification,List<CpGestionnaire> listCpGestionnaire)
	
	{
		dao.saveCpGestionnaire(listCpGestionnaire);
		dao.saveCpModification(modification);
	}

	@Override
	public List<BudgetCpDest> getRecetDestList(int exercice, String dep_rec) {
		return jdbcHelper.getRecetDestList(exercice,  dep_rec);
	}
//	public String getCfgconfigValue(String valeur,Integer exercice){
//		return jdbcHelper.getCfgconfigValue(valeur,exercice);
//	}

	@Override
	public List<BudgModification> getBudgModifList(int exercice,
			String typeModif, EtatEnum etatCp) {
		return dao.getBudgModifList(exercice,typeModif,etatCp);
	}

	@Override
	public List<CpGestionnaire> getCpGestionnaireList(int exercice,
			String codeBudget) {	
		return jdbcHelper.getCpGestionnaireList(exercice,codeBudget);
	}

	@Override
	@Transactional
	public void saveCreditBR(List<CpGestionnaire> updatedList, List<ModificationCpLigne> modificationList) {
		logger.info("saveCreditBR: start");	
		if(updatedList!=null)
		dao.saveCpGestionnaire(updatedList);
		if(modificationList!=null)
		for(ModificationCpLigne m:modificationList)
			dao.saveModificationCpLigne(m);
		
		logger.info("saveCreditBR: end");	
		
	}

	@Override
	@Transactional
	public double getNoDataBudg1(int exercice, String code_nat) {
		return jdbcHelper.getNoDataBudg1(exercice, code_nat);
	}
	@Override
	@Transactional
	public double getBrNoDataBudg1(int exercice,Integer niveau, String code_nat) {
		return jdbcHelper.getBrNoDataBudg1(exercice,niveau, code_nat);
	}

	@Override
	@Transactional
	public double getNoDataBudg2(int exercice, String code_nat1,
			String code_nat2) {
		return jdbcHelper.getNoDataBudg2(exercice, code_nat1, code_nat2);
	}

	@Override
	@Transactional
	public double getNoDataBudg3(int exercice, String code_nat, String dep_rec) {
		return jdbcHelper.getNoDataBudg3(exercice, code_nat, dep_rec);
	}

	@Override
	@Transactional
	public double getNoDataBudg4(int exercice, String code_nat) {
		return jdbcHelper.getNoDataBudg4(exercice, code_nat);
	}

	@Override
	public String getGestUnique(Integer exercice) {
		String GEST_UNIQUE = "tGestUnique";
		return jdbcHelper.getCfgconfigValue(GEST_UNIQUE,exercice);
	}

	@Override
	public List<CpGestionnaire> getCpGestionnaireList(int exercice,
			String codeBudget, String groupNat, String gestionnaire) {
		 return dao.getCpGestionnaireList(exercice,codeBudget,groupNat,gestionnaire);
	}
	
	@Override
	@Transactional(readOnly=true)
	public	List<ComplexEntity> getEnvlpBdgVentilParOrigin(int execCp)
	{
		 return jdbcHelper.getEnvlpBdgVentilParOrigin(execCp);
	}
	
	@Override
	public List<String> getListExecSuiviCp() {
		List<Map<String, Object>> dataList=null;
		
		dataList= PsExecListe.getInstance(jdbcHelper.getJdbcTemplate()).getData();
		return BudgetHelper.toDataList(dataList,num_exec_col);
		
	}
	
	@Override
	public List<String> getListBudgetSuiviCp(int execCp) {
		List<Map<String, Object>> dataList=null;
		
		dataList= PsBudgListe.getInstance(jdbcHelper.getJdbcTemplate()).getData(execCp);
		return BudgetHelper.toDataList(dataList,code_budg_col);		

	}	
	
	@Override
	public List<SimpleEntity> getListDirectionsSuiviCp(int execCp, String codeBudget) {
		
		List<Map<String, Object>> dataList=null;
		dataList= PsDirListe.getInstance(jdbcHelper.getJdbcTemplate()).getData(execCp,codeBudget);
		return BudgetHelper.toSEntityList(dataList,code_direction_col,nom_direction_col,null);
		
		

	}
	
	@Override
	public List<SimpleEntity> getListServicesSuiviCp(int execCp, String codeBudget, String codeDirection) {
		
		List<Map<String, Object>> dataList=null;
		dataList= PsServListe.getInstance(jdbcHelper.getJdbcTemplate()).getData(execCp,codeBudget,codeDirection);
		List<SimpleEntity> list= BudgetHelper.toSEntityList(dataList,code_service_col,nom_service_col,null);		
		return list;

	}
	
	@Override
	public List<SimpleEntity> getListFongDestSuiviCp(int execCp, String codeBudget ) {
		
		List<Map<String, Object>> dataList=null;
		dataList= PsDestGrpListe.getInstance(jdbcHelper.getJdbcTemplate()).getData(execCp,codeBudget);
		List<SimpleEntity> list= BudgetHelper.toSEntityList(dataList,dest_grp_col,nom_dest_grp_col,null);		
		return list;

	}

		
	@Override
	public List<SimpleEntity> getListDestinationSuiviCp(int numexec, String codeBudg , String codeFongDest, String codeDirection, String codeService) {
		List<Map<String, Object>> dataList=null;
		dataList= PsDestListe.getInstance(jdbcHelper.getJdbcTemplate()).getData(numexec,codeBudg ,codeFongDest,codeDirection,codeService) ;
		List<SimpleEntity> list= BudgetHelper.toSEntityList(dataList,code_dest_col,nom_dest_col,dest_grp_col);		
		return list;

	}
	
	@Override
	public List<SimpleEntity> getListProgrammeSuiviCp(int numexec, String codeBudg, String codeFongDest,
			String codeDest, String codeDirection, String codeService) {
		
		List<Map<String, Object>> dataList = null;
		dataList = PsProgListe.getInstance(jdbcHelper.getJdbcTemplate()).getData(numexec, codeBudg, codeFongDest,codeDest, codeDirection, codeService);
		List<SimpleEntity> list = BudgetHelper.toSEntityList(dataList, code_prog_col, nom_prog_col, null);
		return list;

	}
	
	@Override
	public List<SimpleEntity> getListNatureGroupeSuiviCp(int numexec, String codeBudg, String codeFongDest, String codeDest, String codeNature) {
		List<Map<String, Object>> dataList = null;
		dataList = PsNatGrpListe.getInstance(jdbcHelper.getJdbcTemplate()).getData( numexec,  codeBudg,  codeFongDest,  codeDest,codeNature);
		List<SimpleEntity> list = BudgetHelper.toSEntityList(dataList, nat_grp_col, nom_nat_grp_col, null);
		return list;

	}
	
	@Override
	public List<SimpleEntity> getListNatureSuiviCp(int numexec, String codeBudg, String codeFongDest, String codeDest,String codeProg,String codeNatureGrp, String codeDirection, String codeService) {
		List<Map<String, Object>> dataList = null;
		dataList = PsNatListe.getInstance(jdbcHelper.getJdbcTemplate()).getData(numexec, codeBudg, codeFongDest, codeDest, codeProg,codeNatureGrp, codeDirection, codeService);
		List<SimpleEntity> list = BudgetHelper.toSEntityList(dataList, code_nature_col, nom_nature_col, nat_grp_col);
		return list;

	}

	@Override
	public Map<String, Object> getGlobalSuiviCp(int numexec, String codeBudget, String codeDirection,
			String codeService, String codeFongDest, String codeDest, String codeProg, String codeNatureGrp,
			String codeNature) {
		List<Map<String, Object>> dataList = null;
		dataList = PsGbcpSuiviCP.getInstanceStyle0(jdbcHelper.getJdbcTemplate()).getData(0, numexec, codeBudget,
				codeFongDest, codeNatureGrp, codeDest, codeNature, codeDirection, codeService, codeProg, null);
		if (dataList != null)
			return dataList.get(0);
		return null;
	}
	
	@Override
	public List<suiviCpStruct> getSuiviCpAeAnt(int numexec, String codeBudget, String codeDirection, String codeService,
			String codeFongDest, String codeDest, String codeProg, String codeNatureGrp, String codeNature,
			Integer tiersOrigine) {
		List<Map<String, Object>> dataList = null;
		dataList = PsGbcpSuiviCP.getInstanceStyle12(jdbcHelper.getJdbcTemplate()).getData(1, numexec, codeBudget,
				codeFongDest, codeNatureGrp, codeDest, codeNature, codeDirection, codeService, codeProg, tiersOrigine);
		List<suiviCpStruct> list = BudgetHelper.toSuiviCPStruct(dataList);
		return list;

	}

	@Override
	public List<suiviCpStruct> getSuiviCpAeExec(int numexec, String codeBudget, String codeDirection,
			String codeService, String codeFongDest, String codeDest, String codeProg, String codeNatureGrp,
			String codeNature, Integer tiersOrigine) {
		List<Map<String, Object>> dataList = null;
		dataList = PsGbcpSuiviCP.getInstanceStyle12(jdbcHelper.getJdbcTemplate()).getData(2, numexec, codeBudget,
				codeFongDest, codeNatureGrp, codeDest, codeNature, codeDirection, codeService, codeProg, tiersOrigine);
		List<suiviCpStruct> list = BudgetHelper.toSuiviCPStruct(dataList);
		return list;

	}
	
	@Override
	public void updateBudgetModifEtatCp(Integer exercice, String type, Integer numero, String etatCp) {

		jdbcHelper.updateBudgetModifEtatCp(exercice, type, numero, etatCp);
	}

	
	@Override
	public List<SimpleEntity> getNatureGroupeList() {

		return jdbcHelper.getNatureGroupeList();
	}

	@Override
	public double getMontantBr(BudgModification selectedBudgModif, Integer noLbgCp) {
		return jdbcHelper.getMontantBr(selectedBudgModif,noLbgCp);
	}

	@Override
	public double getCumuleBrAvant(BudgModification selectedBudgModif, Integer noLbgCp) {
		return jdbcHelper.getCumuleBrAvant(selectedBudgModif,noLbgCp);
	}
	
	@Override
	public List<ModificationCpLigne> getBrVisesList( int exercice, String codeBudg,String nat_grp )  {
		return jdbcHelper.getBrVisesList(exercice,codeBudg,nat_grp);
	}

	@Override
	public Boolean getEtatEditionBI(int exercice) {
		return jdbcHelper.getEtatEditionBI(exercice);
	}

	@Override
	public void updateBudgetEditionBiFlag(int exercice, boolean biGenereted) {
		 jdbcHelper.updateBudgetEditionBiFlag(exercice, biGenereted);
		
	}

	@Override
	@Transactional
	public void saveDematBiBr(int exercice, String codeBudget, int noBiBr,List<EditionBiBr> bibrList) {
		dao.deleteDematBiBr(exercice, codeBudget, noBiBr);
		dao.saveDematBiBr(bibrList);
		
	}

	@Override
	public void deleteDematBiBr(int exercice, String codeBudget, int noBiBr) {
		dao.deleteDematBiBr(exercice,codeBudget,noBiBr);
		
	}

	@Override
	public List<EditionBiBr> getDematBiBrList(int exercice, String codeBudget, int noBiBr, TabsEnum dematType) {
		// TODO Auto-generated method stub
		return dao.getDematBiBrList(exercice,codeBudget,noBiBr,dematType);
	}

	@Override
	public List<Operation> getDematBiBRTab5(Integer exercice, String codeBudget, Integer niveau) {
		return dao.getDematBiBRTab5(exercice,codeBudget,niveau);
	}

	@Override
	@Transactional
	public void saveDematBiBrTab5(Tab5 tab5) {
		dao.saveOperations(tab5.getOperationsBudg());
		dao.saveOperations(tab5.getOperationsnoBudg());
		
	}

	@Override
	public Map<String, Double> getBpDepAe(Integer exercice) {
		return jdbcHelper.getBpDepAe(exercice);
	}

	@Override
	public Map<String, Double> getMontantDepCP(Integer exercice) {
		return jdbcHelper.getMontantDepCP(exercice);
	}


	@Override
	public Map<String, Double> getBrDepAEByNatGrp(Integer exercice, Integer niveau) {
		return jdbcHelper.getBrDepAEByNatGrp(exercice,niveau);
	}

	@Override
	public Map<String, Double> getBrDepCPByNatGrp(Integer exercice, Integer niveau) {
		return jdbcHelper.getBrDepCPByNatGrp(exercice,niveau);
	}

	@Override
	public Map<String, Double> getBrRecette(Integer exercice, Integer niveau) {
		return jdbcHelper.getBrRecette(exercice,niveau);
	}

	@Override
	public Map<String, Double> getBpRecetByNatGrp(Integer exercice) {
		return jdbcHelper.getBpRecetByNatGrp(exercice);
	}

	@Override
	public List<Ventillation> getBrDepAEByDest(Integer exercice, Integer niveau,String dep_rec) {
		return jdbcHelper.getBrDepAEByDest(exercice,niveau,dep_rec);
	}

	@Override
	public List<Ventillation> getBrDepCpByDest(Integer exercice, Integer niveau) {
		return jdbcHelper.getBrDepCpByDest(exercice,niveau);
	}

	@Override
	public double getBrNoDataBudg2(int exercice, int niveau, String code_nat1, String code_nat2) {
		return jdbcHelper.getBrNoDataBudg2(exercice, niveau, code_nat1, code_nat2);
	}

	@Override
	public double getBrNoDataBudg4(int exercice, Integer niveau, String code_nat1) {
		return jdbcHelper.getBrNoDataBudg4(exercice, niveau, code_nat1);
	}

	@Override
	@Transactional(readOnly=true)
	public List<SimpleEntity> getDestinationTab3List(int exercice, String groupNat) {
	
		return jdbcHelper.getDestinationTab3List(exercice,groupNat);
		
}

	@Override
	public List<Integer> getNivDestList(Integer exercice, DepRecEnum type) {
		return jdbcHelper.getNiveauDestinationList(exercice, type);
	}
	
	@Override
	@Transactional(readOnly = true)
	public double getCreditOuvert(Integer exercice, String codeBudget, String codeGest) {
		return dao.getCreditOuvert(exercice,codeBudget,codeGest);
	}

	@Override
	public Integer getMaxNoLbgCp(int exercice, String codeBudget) {
		// TODO Auto-generated method stub
		return dao.getMaxNoLbgCp(exercice,codeBudget);
	}

	@Override
	public List<SimpleEntity> getGestionnairesList() {
		return jdbcHelper.getGestionnairesList();
	}
	@Override
	public List<CpModification> getCpModificationList(int exercice,
			String codeBudget){
		return dao.getCpModifList(exercice, codeBudget);
	}
	@Override
	public CpGestionnaire getCpGestionnaire(Integer exercice,
			String codeBudget, Integer noLbgCp) {
		return dao.getCpGestionnaire(exercice,codeBudget,noLbgCp);
	}
	
	@Override
	public List<CpModificationItem> getItemscpModifsList(int exerciceCp, String codeBudget, Integer modification){
		return dao.getItemscpModifsList(exerciceCp, codeBudget, modification);
	}
	@Override
	public List<LigneBudgetaireAE> getInterneLbList(Integer exercice,DepRecEnum type,String codeBudget){
		return dao.getInterneLbList(exercice,type,codeBudget);
	}

	@Override
	public List<SimpleEntity> getCompteProduitList(Integer exercice, String codeDest, String codeNature,
			String codeProg) {
		
		return jdbcHelper.getCompteProduitList(exercice,codeDest,codeNature,codeProg);
	}
	
	@Override
	public GlobalSuiviStruct getGlobalSuiviRec(Boolean isTrAant,int numexec, String codeBudget, String codeDirection,
			String codeService, String codeFongDest, String codeDest, String codeProg, String codeNatureGrp,
			String codeNature, Integer noLtr, String codeTiers) {		
	
		return jdbcHelper.getGlobalSuiviRec(isTrAant,numexec, codeBudget, codeDirection, codeService, codeFongDest, codeDest,
				codeProg, codeNatureGrp, codeNature, noLtr, codeTiers);
	}
	
	@Override
	public Integer getMaxNoEcriture(Integer exercice) {
		return jdbcHelper.getMaxNoEcriture(exercice);
	}

	@Override
	public void refreshRecetteRecouveData(Integer exercice) {
		PsRecRecouvMaj.getInstance(jdbcHelper.getJdbcTemplate()).execute(exercice, null);
		
	}
	
	@Override
	public List<SuiviRecetStruct> getDetailSuiviRec(Boolean isTrAant, int numexec, String codeBudget, String codeDirection,
			String codeService, String codeFongDest, String codeDest, String codeProg, String codeNatureGrp,
			String codeNature, Integer noLtr, String codeTiers)
	{
		return jdbcHelper.getDetailSuiviRec( isTrAant,  numexec,  codeBudget,  codeDirection,
				 codeService,  codeFongDest,  codeDest,  codeProg,  codeNatureGrp,
				 codeNature,  noLtr,  codeTiers);
	}

	@Override
	public LigneBudgetaireAE getLbi(Integer exercice, Integer noLbi) {

		return dao.findLbi(exercice,noLbi);
	}

	@Override
	public List<SimpleEntity> getRecetteByOrigine(Integer numexec, String codeBudget, String codeDirection,
			String codeService, String codeFongDest, String codeDest, String codeProg, String codeNatureGrp,
			String codeNature, Integer noLtr, String codeTiers,Integer niveau) {
		
		return jdbcHelper.getRecetteByOrigine( numexec,  codeBudget,  codeDirection,
				 codeService,  codeFongDest,  codeDest,  codeProg,  codeNatureGrp,
				 codeNature,  noLtr,  codeTiers,niveau);
	}
	@Override
	public List<SuiviAeCpStruct> getSuiviAeCp(int numexec, String codeNatureGrp) {
		List<Map<String, Object>> dataList = null;
		dataList = PsBudgSuiviAECP.getInstanceStyle1(jdbcHelper.getJdbcTemplate()).getData(1, numexec, 
				 codeNatureGrp);
		List<SuiviAeCpStruct> list = BudgetHelper.toSuiviAeCPStruct(dataList);
		return list;

	}
	
	@Override
	public double getSuiviAeCpTotCPVote(int numexec, String codeNatureGrp) {
	return jdbcHelper.getTotCpvint(numexec, codeNatureGrp);
		

	}
	@Override
	public List<SuiviPluriStruct> getSuiviPluriEj(int numexec, String codeNatureGrp) {
		List<Map<String, Object>> dataList = null;
		dataList = PsBudgSuiviAECP.getInstanceStyle3(jdbcHelper.getJdbcTemplate()).getData(3, numexec, 
				 codeNatureGrp);
		List<SuiviPluriStruct> list = BudgetHelper.toSuiviPluriStruct(dataList);
		return list;

	}

	@Override
	public List<String> getCodeGestList(Integer exercice, String groupNat) {
		return jdbcHelper.getCodeGestList(exercice, groupNat);
	}

	@Override
	public String[] getCtaCompteImput(Integer exercice, String imputHtd) {
		return jdbcHelper.getCtaCompteImput(exercice, imputHtd);
	}

	@Override
	public List<SimpleEntity> getCompteChargeList(Integer exercice, Integer noEnv) {
		return jdbcHelper.getCompteChargeList(exercice,noEnv);
	}
	

	
	
}
