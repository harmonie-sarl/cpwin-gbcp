package fr.symphonie.budget.core.service;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fr.symphonie.budget.core.dao.IJdbcHelper;
import fr.symphonie.budget.core.dao.IPlanTresorerieDao;
import fr.symphonie.budget.core.model.plt.DetailLigneTresorerie;
import fr.symphonie.budget.core.model.plt.Ecriture;
import fr.symphonie.budget.core.model.plt.EncDecEnum;
import fr.symphonie.budget.core.model.plt.GenericLigneTresorerie;
import fr.symphonie.budget.core.model.plt.GlobalEnum;
import fr.symphonie.budget.core.model.plt.LigneTresorerie;
import fr.symphonie.budget.core.model.plt.ParamCompteLtr;
import fr.symphonie.budget.core.model.plt.Periode;
import fr.symphonie.util.model.SimpleEntity;
@Service(value="pltService")
public class PlanTresorerieService implements IPlanTresorerieService{
	
	private IPlanTresorerieDao dao;
	private IJdbcHelper jdbcHelper;
	
	
   @Inject
	public void setJdbcHelper(IJdbcHelper jdbcHelper) {
		this.jdbcHelper = jdbcHelper;
	}
	
   @Inject
	public void setDao(IPlanTresorerieDao dao) {
		this.dao = dao;
	}


	@Override
	public List<DetailLigneTresorerie> getDetailTresorerie(int exercice, int periode, EncDecEnum type) {
		return dao.getDetailTresorerie(exercice, periode, type);
	}


	@Override
	public List<Periode> getPeriodeList(int exercice) {
		// TODO Auto-generated method stub
		return jdbcHelper.getTresoreriePeriodeList(exercice);
	}


	@Override
	public List<LigneTresorerie> getLigneTresorerie(Integer exercice, GlobalEnum categorie, EncDecEnum type) {
		return dao.getLigneTresorerie(exercice,categorie,type);
	}


	@Override
	public double getSoldeInitialJanvier(Integer exercice) {
		return jdbcHelper.getSoldeInitialJanvier(exercice);
	}

	@Override
	public List<Ecriture> getEcritures(Integer exercice, Integer periode, int noLigne) {
		return dao.getEcritures(exercice,periode,noLigne);
	}

	@Override
	public void cloturerPeriode(Integer exercice, Integer periode, String userName) {
		jdbcHelper.cloturerPeriode(exercice,periode,userName);
		
	}

	@Override
	@Transactional
	public <T extends GenericLigneTresorerie> void  saveTresorerieData(List<T> updatedList) {
		for(T ligne:updatedList)
		dao.updateDetailLigneTresorerie(ligne);
		
	}
	@Override
	@Transactional
	public  <T extends GenericLigneTresorerie> void createNextPeriode(List<T> nextPeriodList) {
		for(T ligne:nextPeriodList)
			dao.updateDetailLigneTresorerie(ligne);
		
	}

	@Override
	@Transactional
	public void updateEcriture(Ecriture ecriture) {
		dao.updateEcriture(ecriture);
		
	}

	@Override
	public <T extends GenericLigneTresorerie> List<T> getLignesTresorerie(Integer exercice, Integer periode,
			EncDecEnum type, Class<T> entityType) {
		return dao.getLignesTresorerie(exercice,periode,type,entityType);
	}

	@Override
	public Map<String, Double> getBudgetPrevisionnel(Integer exercice) {
		return jdbcHelper.getBudgetPrevisionnel(exercice);
	}

	@Override
	public Map<Integer, Double> getCoTresorerieVentille(Integer exercice, Integer periode) {
		return jdbcHelper.getCoTresorerieVentille(exercice,periode);
	}
	
	@Override
	public List<SimpleEntity> getParamCompteList(int exercice) {
		// TODO Auto-generated method stub
		return jdbcHelper.getParamCompteList(exercice);
	}
	@Transactional
	public <T extends Object> T save(T o) {
		return dao.save(o);

	}

	@Override
	public List<ParamCompteLtr> getPltParamList(Integer exercice,String searchCondition) {
		return dao.getPltParamList(exercice,searchCondition);
	}

	@Override
	public void refreshSimulationData(Integer exercice, Integer periode) {
		jdbcHelper.refreshSimulationData(exercice,periode);
		//return jdbcHelper.getSimulationTempData(exercice,periode);
		
	}
	

}
