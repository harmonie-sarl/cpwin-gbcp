package fr.symphonie.budget.core.service;

import static fr.symphonie.budget.core.dao.ps.ColumnName.getAutreFonc_col;
import static fr.symphonie.budget.core.dao.ps.ColumnName.getAutresProduit_col;
import static fr.symphonie.budget.core.dao.ps.ColumnName.getAutresSubv_col;
import static fr.symphonie.budget.core.dao.ps.ColumnName.getData10_col;
import static fr.symphonie.budget.core.dao.ps.ColumnName.getData11_col;
import static fr.symphonie.budget.core.dao.ps.ColumnName.getData1_col;
import static fr.symphonie.budget.core.dao.ps.ColumnName.getData2_col;
import static fr.symphonie.budget.core.dao.ps.ColumnName.getData3_col;
import static fr.symphonie.budget.core.dao.ps.ColumnName.getData4_col;
import static fr.symphonie.budget.core.dao.ps.ColumnName.getData5_col;
import static fr.symphonie.budget.core.dao.ps.ColumnName.getData6_col;
import static fr.symphonie.budget.core.dao.ps.ColumnName.getData7_col;
import static fr.symphonie.budget.core.dao.ps.ColumnName.getData8_col;
import static fr.symphonie.budget.core.dao.ps.ColumnName.getData9_col;
import static fr.symphonie.budget.core.dao.ps.ColumnName.getDepPersCp_col;
import static fr.symphonie.budget.core.dao.ps.ColumnName.getSubvEtat_col;
import static fr.symphonie.budget.core.dao.ps.ColumnName.getinterventionCp_col;
import static fr.symphonie.budget.core.dao.ps.ColumnName.mnt_col;
import static fr.symphonie.budget.core.dao.ps.ColumnName.no_ltr_col;
import static fr.symphonie.budget.core.dao.ps.ColumnName.getFiscAffect_col;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fr.symphonie.budget.core.dao.IJdbcHelper;
import fr.symphonie.budget.core.dao.jpa.DematDao;
import fr.symphonie.budget.core.dao.ps.ColumnName;
import fr.symphonie.budget.core.dao.ps.PsAcBalanceDetail;
import fr.symphonie.budget.core.dao.ps.PsCtabalanceParDate;
import fr.symphonie.budget.core.dao.ps.PsGbcpCtrlSF;
import fr.symphonie.budget.core.dao.ps.PsGbcpGetCpAeParDest;
import fr.symphonie.budget.core.dao.ps.PsGbcpGetRFCpAeparDest;
import fr.symphonie.budget.core.model.cf.CfGenericDemat;
import fr.symphonie.budget.core.model.cf.CfParam;
import fr.symphonie.budget.core.model.cf.CfReference;
import fr.symphonie.budget.core.model.demat.Export;
import fr.symphonie.budget.core.model.demat.ExportInfo;
import fr.symphonie.budget.core.model.demat.GenericDemat;
import fr.symphonie.budget.core.model.edition.Edition;
import fr.symphonie.budget.core.model.edition.Tab6;
import fr.symphonie.budget.core.model.edition.Tab8;
import fr.symphonie.budget.core.model.edition.util.CAF;
import fr.symphonie.budget.core.model.edition.util.CRP;
import fr.symphonie.budget.core.model.edition.util.DepByDest;
import fr.symphonie.budget.core.model.edition.util.TFP;
import fr.symphonie.budget.core.model.pluri.BalanceItem;
import fr.symphonie.budget.infocentre.DematFileStruct;
import fr.symphonie.budget.ui.viewmodel.VentilationImput;
import fr.symphonie.common.model.AppCfgConfig;
import fr.symphonie.exception.MissingConfiguration;
import fr.symphonie.util.model.SimpleEntity;


@Service
public class DematService {
	//private static final Logger logger = LoggerFactory.getLogger(DematService.class);
	private IJdbcHelper jdbcHelper;
	
	   @Inject
		public void setJdbcHelper(IJdbcHelper jdbcHelper) {
			this.jdbcHelper = jdbcHelper;
		}
	private DematDao dao;
	@Inject
	public void setDao(DematDao dao) {
		this.dao = dao;
	}

	public List<String> getBudgetList() {
		return dao.getBudgetList();
	}
	public List<Export> getExportList(Integer exercice, String codeBudget) {
		return dao.getExportList(exercice,codeBudget);
	}
	public List<ExportInfo> getInfoExportList() {
		return dao.getInfoExportList();
	}
	public List<? extends GenericDemat> getDematList(Class<? extends GenericDemat> type, Export export) {
		
		return dao.getDematList(type,export);
	}
	@Transactional
	public void updateDemat(List<? extends GenericDemat> dematList) {

		 dao.updateDemat(dematList);
		
	}
	@Transactional
	public void updateList(List<? extends Object> list) {
		 dao.updateList(list);		
	}
	public List<CfParam> loadParamCF(Integer exercice, String codeBudget, String type) {
		return dao.loadParamCF(exercice,codeBudget,type);
	}
	public List<SimpleEntity> getCompteList(Integer exercice, String codeBudget, String type) {
		return jdbcHelper.getCompteList(exercice,codeBudget,type);
	}
	public List<CfReference> loadReference(String type,boolean onlyDetail) {
		return dao.loadReference(type,onlyDetail);
	}

	public void refreshBalance(Integer exercice, String codeBudget) {
		String requete="exec psbalancemaj "+exercice+",'"+codeBudget+"'";
		jdbcHelper.getJdbcTemplate().execute(requete);
		
	}
	public Map<String, Double> getBalance(Integer exercice, String codeBudget, String type) {
		return jdbcHelper.getBalance(exercice,codeBudget,type);
	}
	public Boolean getEtatCF(Integer exercice, String codeBudget, String type) {
		return jdbcHelper.getEtatCF( exercice,  codeBudget,  type);
	}
	public Map<String, Double> getCfDematData(Integer exercice, String codeBudget, String type) {
		Map<String, Double> result=new HashMap<String, Double>();
		List<? extends CfGenericDemat> dematList=dao.getCfDematData(exercice,codeBudget,type);
		for(CfGenericDemat item:dematList){
			result.put(item.getLibelle(), item.getMontant());
		}
		return result;
	}
	public void updateEtatCf(Integer exercice, String codeBudget, String type, boolean etat) {
		jdbcHelper.updateEtatCF( exercice,  codeBudget,  type,etat);
		
	}
	public Map<String, Double> getAeEngage(Integer exercice, Date dateFinPeriode, String tiersNeutralise) {
		return jdbcHelper.getAeEngage(exercice,dateFinPeriode,tiersNeutralise);
	}
	public List<BalanceItem> loadBalance(Integer exercice, String codeBudget, String dateFin, boolean isDefinitive) {
		return PsAcBalanceDetail.getInstance(jdbcHelper.getJdbcTemplate()).loadBalance(exercice, codeBudget, dateFin,isDefinitive);
	}
	public Map<String, String> loadDematParams() throws MissingConfiguration {
		Map<String, String> params=new HashMap<>();

		AppCfgConfig config=AppCfgConfig.getInstance();
		params.put(DematFileStruct.AttributType.Identifiant.getName(), config.getCodeInfoCp());
		params.put(DematFileStruct.AttributType.Code_nomenclature.getName(), config.getPlanComptable());
		params.put(DematFileStruct.AttributType.Siren.getName(), config.geSiren());
		params.put(DematFileStruct.AttributType.Siret.getName(), config.getSiret());
		return params;
	}
	public Integer getMaxNumeroBr(Integer exercice, String dateFin) {
		return jdbcHelper.getMaxNumeroBr(exercice,dateFin);
	}
	public List<DepByDest> getCpAeParDest(Integer exercice, Integer niveau, String dateFinStr) {
		return PsGbcpGetCpAeParDest.getInstance(jdbcHelper.getJdbcTemplate()).getData(exercice, niveau,dateFinStr);
	}
	public void loadTab6FromCtaBalance(Edition e, String dateFinStr) {
		//jdbcHelper.loadTab6FromCtaBalance(exercice,tab6);
		Tab6 tab6=e.getTab6();
		Map<String, Object> result=PsCtabalanceParDate.getInstance(jdbcHelper.getJdbcTemplate()).getData(e.getExercice(), e.getCodeBudget(), dateFinStr).get(0);
		CRP crp=tab6.getCrp();
		crp.getDepPersCp().setMontantDouble((double) result.get(getDepPersCp_col));
		crp.getSubvEtat().setMontantDouble((double) result.get(getSubvEtat_col));
		crp.getAutresSubv().setMontantDouble((double) result.get(getAutresSubv_col));
		crp.getAutresProduit().setMontantDouble((double) result.get(getAutresProduit_col));		
		crp.getAutreFoncPersonnel().setMontantDouble((double) result.get(getAutreFonc_col));
		crp.getIntervCasEcheant().setMontantDouble((double) result.get(getinterventionCp_col));
		crp.getFiscAffect().setMontantDouble((double) result.get(getFiscAffect_col));
		
		CAF cafp=tab6.getCaf();
		cafp.getData1().setMontantDouble((double) result.get(getData1_col));
		cafp.getData2().setMontantDouble((double) result.get(getData2_col));
		cafp.getData3().setMontantDouble((double) result.get(getData3_col));
		cafp.getData4().setMontantDouble((double) result.get(getData4_col));
		
		TFP tfp=tab6.getTfp();
		 tfp.getDepInvestCp().setMontantDouble((double) result.get(getData5_col));
		 tfp.getRembCautions().setMontantDouble((double) result.get(getData8_col));
		 tfp.getCautionRecu().setMontantDouble((double) result.get(getData9_col));
		 tfp.getRembDettes().setMontantDouble((double) result.get(getData10_col));
		 tfp.getAugDettes().setMontantDouble((double) result.get(getData11_col)); 
		 tfp.getAutreRessource().setMontantDouble(tab6.getCaf().getData4().getMontantDouble()); 
		 tfp.getFinancActifEtat().setMontantDouble((double) result.get(getData6_col));
		 tfp.getFinancActifTiersAutre().setMontantDouble((double) result.get(getData7_col));
		
		
	}
	public double getFinaActifEtat(Integer exercice) {
		return jdbcHelper.getFinaActifEtat(exercice);
	}
	public double getFinaActifTiers(Integer exercice) {
		return jdbcHelper.getFinaActifTiers(exercice);
	}
	public double getAeEngage(Integer exercice, Integer noLtr, Date dateFin, String tiersNeutralise) {
		return jdbcHelper.getAeEngage(exercice, noLtr, dateFin, tiersNeutralise);
	}
	public void setCpSumLigneTresorerieRF(Edition e, String dateFinStr) {
		List<Map<String, Object>> cpList = PsGbcpGetRFCpAeparDest.getInstance(jdbcHelper.getJdbcTemplate()).getCpData(e.getExercice(), e.getNiveau(), dateFinStr);
		Tab8 tab8=e.getTab8();
		int no_ltr = 0;
		double montant=0;
		for (Map<String, Object> map : cpList) {
			no_ltr = (int) map.get(no_ltr_col);
			montant= (double) map.get(mnt_col);
			switch (no_ltr) {
			case 25:
				tab8.getPersonnelAeCp().getN().setMontantDouble(montant);	
				break;
			case 26:
				tab8.getFonctCP().getN().setMontantDouble(montant);		
				break;
			case 27:
				tab8.getInterCP().getN().setMontantDouble(montant);		
				break;
			case 28:
				tab8.getInvesCP().getN().setMontantDouble(montant);
				break;

			default:
				break;
			}
		}
		
	}
	public void setAeSumLigneTresorerieRF(Edition e, String dateFinStr) {
		List<Map<String, Object>> cpList = PsGbcpGetRFCpAeparDest.getInstance(jdbcHelper.getJdbcTemplate()).getAeData(e.getExercice(), e.getNiveau(), dateFinStr);
		Tab8 tab8=e.getTab8();
		int no_ltr = 0;
		double montant=0;
		for (Map<String, Object> map : cpList) {
			no_ltr = (int) map.get(no_ltr_col);
			montant= (double) map.get(mnt_col);
			switch (no_ltr) {		
			case 26:
				tab8.getFonctAE().getN().setMontantDouble(montant);	
				break;
			case 27:
				tab8.getInterAE().getN().setMontantDouble(montant);
				break;
			case 28:
				tab8.getInvesAE().getN().setMontantDouble(montant);
				break;
			default:
				break;
			}
		}
	}

	public List<VentilationImput> getVentilationSfByNature(Integer exercice) {
		List<VentilationImput> result = new ArrayList<VentilationImput>();
		String accountCode, accountLabel, nature;
		double amount;
		List<Map<String, Object>> values = PsGbcpCtrlSF.getInstance(jdbcHelper.getJdbcTemplate()).execute(exercice);
		for (Map<String, Object> map : values) {
			accountCode = (String) map.get(ColumnName.imput_htd_col);
			accountLabel = (String) map.get(ColumnName.lib_cpt_col);
			nature = (String) map.get(ColumnName.nat_grp_col);
			amount = (Double) map.get(ColumnName.mt_htd_col);
			result.add(new VentilationImput(accountCode, accountLabel, nature, amount));
		}
		return result;
	}
	
}
