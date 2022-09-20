package fr.symphonie.budget.core.dao.jpa;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

//import org.apache.logging.log4j.util.Strings;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import fr.symphonie.budget.core.dao.IPlanTresorerieDao;
import fr.symphonie.budget.core.model.plt.DetailLigneTresorerie;
import fr.symphonie.budget.core.model.plt.Ecriture;
import fr.symphonie.budget.core.model.plt.EncDecEnum;
import fr.symphonie.budget.core.model.plt.GenericLigneTresorerie;
import fr.symphonie.budget.core.model.plt.GlobalEnum;
import fr.symphonie.budget.core.model.plt.LigneTresorerie;
import fr.symphonie.budget.core.model.plt.ParamCompteLtr;
@Repository
public class PlanTresorerieDao implements IPlanTresorerieDao {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = LoggerFactory.getLogger(PlanTresorerieDao.class);

	
	
	private EntityManager em;
	@PersistenceContext
	public void setEntityManager(EntityManager entityManager) {
		this.em = entityManager;
	}
	
	@Override
	public EntityManager getEm() {
		return em;
	}


	@Override
	public List<DetailLigneTresorerie> getDetailTresorerie(int exercice, int periode, EncDecEnum type) {
		logger.debug("DAO: getDetailTresorerie:Params: {} / {} - {} ",exercice,periode,type);
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<DetailLigneTresorerie> cq = cb.createQuery(DetailLigneTresorerie.class);
		Root<DetailLigneTresorerie> root = cq.from(DetailLigneTresorerie.class);
		Join<DetailLigneTresorerie, LigneTresorerie> ligneTres=root.join("ligne");
		cq.select(root).distinct(true);
		Predicate p = cb.conjunction();
		p = cb.and(p, cb.equal(root.get("periode"),periode));
		p = cb.and(p, cb.equal(root.get("exercice"), exercice));
		if(type!=null)
		p = cb.and(p, cb.equal(ligneTres.get("typeOp"), type.getValue()));
		cq.where(p);
		cq.orderBy(cb.asc(ligneTres.get("ordre")));
		TypedQuery<DetailLigneTresorerie> query = em.createQuery(cq);
		List<DetailLigneTresorerie> result = query.getResultList();
		logger.debug("DAO: getDetailTresorerie: Result size {}  ",result.size());
		return result;
	}

	@Override
	public List<Integer> getPeriodeList(int exercice) {
		logger.debug("DAO: getPeriodeList :Params: {} ",exercice);
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Integer> cq = cb.createQuery(Integer.class);
		Root<DetailLigneTresorerie> root = cq.from(DetailLigneTresorerie.class);
		Expression<Integer> periode=root.get("periode");
		cq.select(periode).distinct(true);
		Predicate p = cb.conjunction();
		p = cb.and(p, cb.equal(root.get("exercice"),exercice));
		
		cq.where(p);
		//cq.orderBy(cb.asc(periode));
		TypedQuery<Integer> query = em.createQuery(cq);
		List<Integer> result = query.getResultList();
		logger.debug("DAO: getPeriodeList: Result size {}  ",result.size());
		return result;
	}

	@Override
	public List<LigneTresorerie> getLigneTresorerie(Integer exercice, GlobalEnum categorie, EncDecEnum type) {
		logger.debug("DAO: getLigneTresorerie :Params: {} - {} - {} ",exercice,categorie,type);
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<LigneTresorerie> cq = cb.createQuery(LigneTresorerie.class);
		Root<LigneTresorerie> root = cq.from(LigneTresorerie.class);

		cq.select(root).distinct(true);
		Predicate p = cb.conjunction();
		p = cb.and(p, cb.equal(root.get("exercice"), exercice));
		if(categorie!=null)
		p = cb.and(p, cb.equal(root.get("categorie"),categorie.getValue()));
		if(type!=null)
		p = cb.and(p, cb.equal(root.get("typeOp"), type.getValue()));
		cq.where(p);
		cq.orderBy(cb.asc(root.get("ordre")));
		TypedQuery<LigneTresorerie> query = em.createQuery(cq);
		List<LigneTresorerie> result = query.getResultList();
		logger.debug("DAO: getLigneTresorerie: Result size {}  ",result.size());
		return result;
	}

	@Override
	public List<Ecriture> getEcritures(Integer exercice, Integer periode, int noLigne) {
		//logger.debug("DAO: getEcritures :Params: {} / {} - {} ",exercice,periode,noLigne);
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Ecriture> cq = cb.createQuery(Ecriture.class);
		Root<Ecriture> root = cq.from(Ecriture.class);
		
		cq.select(root);
		Predicate p = cb.conjunction();
		p = cb.and(p, cb.equal(root.get("periode"),periode));
		//p = cb.and(p, cb.function("datepart", Integer.class, args)(root.get("periode"),periode));
		p = cb.and(p, cb.equal(root.get("exerciceLigne"), exercice));
		Predicate predicatNoLigne = cb.conjunction();
		predicatNoLigne=cb.or(cb.equal(root.get("numeroLigne1"),noLigne),cb.equal(root.get("numeroLigne2"),noLigne));
		p=cb.and(p,predicatNoLigne);
		cq.where(p);
		
		TypedQuery<Ecriture> query = em.createQuery(cq);
		List<Ecriture> result = query.getResultList();
	//	logger.debug("DAO: getEcritures : Result size {}  ",result.size());
		return result;
	}

	@Override
	public  <T extends GenericLigneTresorerie> void updateDetailLigneTresorerie(T ligne) {
		
		em.merge(ligne);
	}

	@Override
	public void updateEcriture(Ecriture ecriture) {
		logger.debug("updateEcriture : {}/{}/{}",ecriture.getExercice(),ecriture.getNumero(),ecriture.getNumeroOP());
		String requete ="update Ecriture e set numeroLigne1=?,numeroLigne2=?,montantLigne1=?,montantLigne2=? where exercice=? and numero=? and numeroOP=?";
		Query q=em.createQuery(requete);
		q.setParameter(1, ecriture.getNumeroLigne1());
		q.setParameter(2, ecriture.getNumeroLigne2());
		q.setParameter(3, ecriture.getMontantLigne1());
		q.setParameter(4, ecriture.getMontantLigne2());
		q.setParameter(5, ecriture.getExercice());
		q.setParameter(6, ecriture.getNumero());
		q.setParameter(7, ecriture.getNumeroOP());
		q.executeUpdate();
	}

	@Override
	public <T extends GenericLigneTresorerie> List<T> getLignesTresorerie(Integer exercice, Integer periode,
			EncDecEnum type, Class<T> entityType) {
		logger.debug("DAO: getLignesTresorerie: {} / {} - {} ",exercice,periode,type);
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<T> cq = cb.createQuery(entityType);
		Root<T> root = cq.from(entityType);
		Join<T, LigneTresorerie> ligneTres=root.join("ligne");
		cq.select(root).distinct(true);
		Predicate p = cb.conjunction();
		if(periode!=null)p = cb.and(p, cb.equal(root.get("periode"),periode));
		p = cb.and(p, cb.equal(root.get("exercice"), exercice));
		if(type!=null)
		p = cb.and(p, cb.equal(ligneTres.get("typeOp"), type.getValue()));
		cq.where(p);
		cq.orderBy(cb.asc(ligneTres.get("ordre")));
		TypedQuery<T> query = em.createQuery(cq);
		List<T> result = query.getResultList();
		logger.debug("DAO: getLignesTresorerie: Result size {}  ",result.size());
		return result;
	}
	
	public <T extends Object> T save(T o) {
		return em.merge(o);
	}

	@Override
	public List<ParamCompteLtr> getPltParamList(Integer exercice,String searchCondition) {
		
		logger.debug("DAO: getPltParamList: {} ",searchCondition);
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<ParamCompteLtr> cq = cb.createQuery(ParamCompteLtr.class);
		Root<ParamCompteLtr> root = cq.from(ParamCompteLtr.class);
		cq.select(root);
		Predicate p = cb.conjunction();
		p = cb.and(p, cb.equal(root.get("exercice"), exercice));
		Expression<String> codeExp=root.get("code");
		if(!StringUtils.isBlank(searchCondition))
		p = cb.and(p, cb.like(codeExp, searchCondition));
		
		cq.where(p);
		
		TypedQuery<ParamCompteLtr> query = em.createQuery(cq);
		List<ParamCompteLtr> result = query.getResultList();
		logger.debug("DAO: getPltParamList : Result size {}  ",result.size());
		return result;
	}

	

}
