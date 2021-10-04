package fr.symphonie.tools.das.core;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import fr.symphonie.tools.das.model.Honoraire;
import fr.symphonie.tools.das.model.TiersDas;

@Repository
public class DasDao {
private static final Logger logger = LoggerFactory.getLogger(DasDao.class);

	
	
	private EntityManager em;
	@PersistenceContext
	public void setEntityManager(EntityManager entityManager) {
		this.em = entityManager;
	}
	
	public TiersDas getTiersDas(String numero){
		TiersDas t=em.find(TiersDas.class, numero);
		return t;
	}
	public List<TiersDas> loadTiers(List<String> codeList){
		List<TiersDas> existants=new ArrayList<>();
		TiersDas t=null;
		for(String numero:codeList){
			 t=em.find(TiersDas.class, numero);
			 if(t!=null)existants.add(t);			 
		}
		logger.debug("loadTiers Das: existants: {}/{} ",existants.size(),codeList.size());
		return existants;
	}

//	public void saveTiersDasList(List<TiersDas> tiersDasList) {
//		for(TiersDas t:tiersDasList){
//			em.merge(t);
//		}		
//	}
	public <T extends Object> void saveList(List<T> list){
		for(T o:list){
			logger.debug("saveList: entity saved ={}",o);
			em.merge(o);
		}		

	}

	public List<Honoraire> loadHonoraires(Integer exercice) {
		List<Honoraire>  result=null;
		String qlString="SELECT h from Honoraire h where h.exercice=?";
		TypedQuery<Honoraire> query=em.createQuery(qlString, Honoraire.class);
		query.setParameter(1, exercice);
		result=query.getResultList();

		return result;
	}
	

	public List<Honoraire> loadHonoraires(Integer exercice, String codeTiers ) {
		boolean isEexerciceNull=(exercice==null)||(exercice.intValue()==0);		
		boolean isCodeTiersNull=(codeTiers==null)||(codeTiers.trim().isEmpty());
		
		List<Honoraire> result=null;
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Honoraire> cq = cb.createQuery(Honoraire.class);
		Root<Honoraire> root = cq.from(Honoraire.class);

		cq.select(root).distinct(true); 
		Predicate p= cb.conjunction();			
		if(!isEexerciceNull)p=cb.and(p,cb.equal(root.get("exercice"), exercice));
		if(!isCodeTiersNull)p=cb.and(p,cb.equal(root.get("codeTiers"), codeTiers));
		
		
		cq.where(p);
		TypedQuery<Honoraire> query = em.createQuery(cq);
		result=query.getResultList();	
	
		return result;
	}
	
	public List<TiersDas> findLightTiersList(boolean searchAtBegin, String formatedPrefix,String numSiret, boolean withAll) {

		List<TiersDas> listTiers = null;
		String condition = (searchAtBegin) ? formatedPrefix + "%" : "%" + formatedPrefix + "%";
		boolean isNumSiretNull = (numSiret==null)?true:numSiret.trim().isEmpty();
        boolean isFormatedPrefixNull=(formatedPrefix==null)?true:formatedPrefix.trim().isEmpty();
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<TiersDas> cq = cb.createQuery(TiersDas.class);
		Root<TiersDas> root = cq.from(TiersDas.class);
		Predicate pTiers=null;	
		Predicate pSiret=null;	
		cq.select(root);
		Predicate p = cb.conjunction();
		try {
			  if((isFormatedPrefixNull)&&(isNumSiretNull)&&(!withAll))
		             return new ArrayList<TiersDas>();
			if (!isFormatedPrefixNull) {
				pTiers = cb.like(root.<String>get("numero"), condition);
				pTiers = cb.or(pTiers, cb.like(root.<String>get("nom"), condition));
				pTiers = cb.or(pTiers, cb.like(root.<String>get("prenom"), condition));
				pTiers = cb.or(pTiers, cb.like(root.<String>get("rs"), condition));
				p = cb.and(p, pTiers);
			}
			if (!isNumSiretNull) {				
				pSiret = cb.equal(root.get("siret"), numSiret.trim());
				p = cb.and(p, pSiret);
			}
//             if((isFormatedPrefixNull)&&(isNumSiretNull)&&(!withAll))
//             return new ArrayList<TiersDas>();
			cq.where(p);
			TypedQuery<TiersDas> query = em.createQuery(cq);
			listTiers = query.getResultList();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {

		}

		
		return listTiers;
	}
	
	public TiersDas findTiers(String codeTiers) {

		List<TiersDas> listTiers = null;
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<TiersDas> cq = cb.createQuery(TiersDas.class);
		Root<TiersDas> root = cq.from(TiersDas.class);
		cq.select(root);
		Predicate p = cb.conjunction();
		try {			
			Predicate pEqual = cb.equal(root.get("numero"), codeTiers);
			p = cb.and(p, pEqual);
			cq.where(p);
			TypedQuery<TiersDas> query = em.createQuery(cq);
			listTiers = query.getResultList();

		
		} catch (Exception e) {
			e.printStackTrace();
		} finally {

		}

		if ((listTiers == null) || listTiers.isEmpty())
			return null;

		else
			return listTiers.get(0);
	}
}
