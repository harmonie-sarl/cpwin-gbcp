package fr.symphonie.budget.core.dao;

import java.util.List;

import javax.persistence.EntityManager;

import fr.symphonie.budget.core.model.plt.DetailLigneTresorerie;
import fr.symphonie.budget.core.model.plt.Ecriture;
import fr.symphonie.budget.core.model.plt.EncDecEnum;
import fr.symphonie.budget.core.model.plt.GenericLigneTresorerie;
import fr.symphonie.budget.core.model.plt.GlobalEnum;
import fr.symphonie.budget.core.model.plt.LigneTresorerie;
import fr.symphonie.budget.core.model.plt.ParamCompteLtr;

public interface IPlanTresorerieDao {
	List<DetailLigneTresorerie> getDetailTresorerie(int exercice,int periode,EncDecEnum type);

	List<Integer> getPeriodeList(int exercice);

	List<LigneTresorerie> getLigneTresorerie(Integer exercice, GlobalEnum categorie, EncDecEnum type);

	List<Ecriture> getEcritures(Integer exercice, Integer periode, int noLigne);

	 <T extends GenericLigneTresorerie> void updateDetailLigneTresorerie(T  ligne);

	void updateEcriture(Ecriture ecriture);

	<T extends GenericLigneTresorerie> List<T> getLignesTresorerie(Integer exercice, Integer periode, EncDecEnum type, Class<T> entityType);

	<T> T save(T o);

	List<ParamCompteLtr> getPltParamList(Integer exercice, String searchCondition);

	EntityManager getEm();



}
