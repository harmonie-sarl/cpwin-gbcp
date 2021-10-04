package fr.symphonie.budget.core.service;

import java.util.List;
import java.util.Map;

import fr.symphonie.budget.core.model.plt.DetailLigneTresorerie;
import fr.symphonie.budget.core.model.plt.Ecriture;
import fr.symphonie.budget.core.model.plt.EncDecEnum;
import fr.symphonie.budget.core.model.plt.GenericLigneTresorerie;
import fr.symphonie.budget.core.model.plt.GlobalEnum;
import fr.symphonie.budget.core.model.plt.LigneTresorerie;
import fr.symphonie.budget.core.model.plt.ParamCompteLtr;
import fr.symphonie.budget.core.model.plt.Periode;
import fr.symphonie.util.model.SimpleEntity;

public interface IPlanTresorerieService {

	List<DetailLigneTresorerie> getDetailTresorerie(int exercice,int periode,EncDecEnum type);
	List<Periode> getPeriodeList(int exercice);
	List<LigneTresorerie> getLigneTresorerie(Integer exercice, GlobalEnum categorie, EncDecEnum type);
	double getSoldeInitialJanvier(Integer exercice);
	List<Ecriture> getEcritures(Integer exercice, Integer periode, int noLigne);
	void cloturerPeriode(Integer exercice, Integer periode, String userName);
	 <T extends GenericLigneTresorerie> void saveTresorerieData(List<T> updatedList);
	 <T extends GenericLigneTresorerie> void createNextPeriode(List<T> nextPeriodList);
	void updateEcriture(Ecriture selectedEcriture);
	<T extends GenericLigneTresorerie> List<T> getLignesTresorerie(Integer exercice, Integer periode, EncDecEnum type, Class<T> entityType);
	Map<String, Double> getBudgetPrevisionnel(Integer exercice);
	Map<Integer, Double> getCoTresorerieVentille(Integer exercice, Integer periode);
	List<SimpleEntity> getParamCompteList(int exercice);
	public <T extends Object> T save(T o) ;
	List<ParamCompteLtr> getPltParamList(Integer exercice, String searchCondition);
	void refreshSimulationData(Integer exercice, Integer periode);
	
}

