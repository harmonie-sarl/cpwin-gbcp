package fr.symphonie.tools.lemans.bt.core;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.logging.log4j.util.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import fr.symphonie.common.util.Constant;
import fr.symphonie.tools.gts.model.LiqRecette;
import fr.symphonie.tools.lemans.bt.model.Client;
import fr.symphonie.tools.lemans.bt.model.Spectacle;
import fr.symphonie.tools.lemans.bt.model.SpectacleDetails;

@Repository
public class LemansDao {
	private static final Logger logger = LoggerFactory.getLogger(LemansDao.class);

	
	private EntityManager em;
	@PersistenceContext
	public void setEntityManager(EntityManager entityManager) {
		this.em = entityManager;
	}
	public <T extends Object> void saveList(List<T> list){
		for(T o:list){
			em.merge(o);
		}
	}

	public <T extends Object> T save(T o) {
//		if(o instanceof Article) {
//			Article article=(Article)o;
//			for (ArticleDetails details : article.getDetails()) {
//			logger.debug("public <T extends Object> T save(T o) article.getDetails().size()="+details.getArticle().getDetails().size());
//			
//			}
//		}
		return em.merge(o);
	}

	public List<Spectacle> getSpectacles(String searchCondition) {
		logger.debug("getSpectacles : searchCondition={}",searchCondition);
		List<Spectacle> result = null;
		
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<Spectacle> cq = cb.createQuery(Spectacle.class);
			Root<Spectacle> root = cq.from(Spectacle.class);
			cq.select(root).distinct(true);
			
			
			if(!Strings.isBlank(searchCondition)){
			Predicate p = cb.conjunction();	
			Expression<String> codeExp=root.get("code");
			Expression<String> libelleExp=root.get("libelle");	
			Predicate pOR = cb.like(codeExp, searchCondition);
			pOR = cb.or(pOR, cb.like(libelleExp, searchCondition));
			p = cb.and(p,pOR);
			cq.where(p);
			}
						
			TypedQuery<Spectacle> query = em.createQuery(cq);
			if(Strings.isBlank(searchCondition))query.setMaxResults(Constant.maxResult);
			result = query.getResultList();
			logger.debug("getSpectacles : size of result --> {}",result.size());
		return result;
	}
	
	public Spectacle getSpectacle(String code) {
		logger.debug("getSpectacle : code={}",code);
		List<Spectacle> result = null;
		
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<Spectacle> cq = cb.createQuery(Spectacle.class);
			Root<Spectacle> root = cq.from(Spectacle.class);
			cq.select(root);
			
			Predicate p = cb.conjunction();	
			p = cb.and(p,cb.equal(root.get("code"), code));
			cq.where(p);

			TypedQuery<Spectacle> query = em.createQuery(cq);
			result = query.getResultList();
			logger.debug("getSpectacle : size of result --> {}",result.size());
		if ((result == null) || (result.isEmpty()))
			return null;
		return result.get(0);

	}
	/*
	public List<Period> getPeriodes(Integer exercice, Integer numPeriode, Boolean ferme ) {
		List<Period> result = null;
		
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Period> cq = cb.createQuery(Period.class);
		Root<Period> root = cq.from(Period.class);
		cq.select(root);
		
		Predicate p = cb.conjunction();		
		p = cb.and(p, cb.equal(root.get("exercice"), exercice));
		if(numPeriode!=null)
		p = cb.and(p,cb.equal(root.get("numero"), numPeriode));
		if(ferme!=null)
			p = cb.and(p,cb.equal(root.get("closed"), ferme));
		cq.where(p);		
		TypedQuery<Period> query = em.createQuery(cq);
		result = query.getResultList();
		return result;
		
	}
	public List<Integer> getListNumPeriode(Integer exercice) {		
		List<Integer> result = null;
		
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Integer> cq = cb.createQuery(Integer.class);
		
		Root<Period> root = cq.from(Period.class);
		Expression<Integer> numero=root.get("numero");
		cq.select(numero);
		
		Predicate p = cb.conjunction();		
		p = cb.and(p, cb.equal(root.get("exercice"), exercice));
		
		cq.where(p);		
		TypedQuery<Integer> query = em.createQuery(cq);
		result = query.getResultList();
		return result;
	}
*/




	public List<Client> getClientLemansList(String searchCondition) {
		logger.debug("getClientLemansList : searchCondition={}",searchCondition);
		List<Client> result = null;
		
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<Client> cq = cb.createQuery(Client.class);
			Root<Client> root = cq.from(Client.class);
			cq.select(root).distinct(true);
			
			if(!Strings.isBlank(searchCondition)){
			Predicate p = cb.conjunction();	
			Expression<String> codeExp=root.get("code");
			Expression<String> libelleExp=root.get("nom");	
			Predicate pOR = cb.like(codeExp, searchCondition);
			pOR = cb.or(pOR, cb.like(libelleExp, searchCondition));
			p = cb.and(p,pOR);
			cq.where(p);
			}
			
			TypedQuery<Client> query = em.createQuery(cq);
			if(Strings.isBlank(searchCondition))query.setMaxResults(Constant.maxResult);
			result = query.getResultList();
			logger.debug("getClientGtsList : size of result --> {}",result.size());
		return result;
	}
	
	public Client getClientLemans(String codeClient) {
		logger.debug("getClientLemans : codeClient={}",codeClient);
		List<Client> result = null;
		
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<Client> cq = cb.createQuery(Client.class);
			Root<Client> root = cq.from(Client.class);
			cq.select(root).distinct(true);
			
			Predicate p = cb.conjunction();	
			Expression<String> codeExp=root.get("code");
			Predicate pAnd = cb.equal(codeExp, codeClient);
			p = cb.and(p,pAnd);
			cq.where(p);
			
			TypedQuery<Client> query = em.createQuery(cq);
			result = query.getResultList();
			if((result==null)||(result.isEmpty()))return null;
			logger.debug("getClientLemans : size of result --> {}",result.size());
		return result.get(0);
	}





	public void remove(Object entity) {

		logger.debug("remove(Object entity) em.contains(entity) --> {}",em.contains(entity) );
		em.remove(em.contains(entity) ? entity : em.merge(entity));
		
		
	}
	
	public Spectacle removeSpectacleDetail(SpectacleDetails entity) {

		Spectacle spectacle = em.find(Spectacle.class,entity.getCode() );
		spectacle.getDetails().remove(entity);
		
		 return spectacle;
			}

//	public List<ImportedData> getImportedData(Integer exercice, Integer numPeriode, String codeSpectacle, String codeClient) {
//		logger.debug("getImportedData(exercice={},  numPeriode={})",exercice,numPeriode);
//		List<ImportedData> result = null;
//
//		CriteriaBuilder cb = em.getCriteriaBuilder();
//		CriteriaQuery<ImportedData> cq = cb.createQuery(ImportedData.class);
//		Root<ImportedData> root = cq.from(ImportedData.class);
//		cq.select(root);
//
//		Predicate p = cb.conjunction();
//		if(exercice!=null)
//		p = cb.and(p, cb.equal(root.get("exercice"), exercice));
//		if (numPeriode != null)
//			p = cb.and(p, cb.equal(root.get("numero"), numPeriode));
//		if(codeSpectacle!=null)
//			p = cb.and(p, cb.equal(root.get("codeSpectacle"), codeSpectacle));
//		if(codeClient!=null)
//			p = cb.and(p, cb.equal(root.get("codeClient"), codeClient));
//		cq.where(p);
//		TypedQuery<ImportedData> query = em.createQuery(cq);
//		result = query.getResultList();
//		
//		logger.debug("getImportedData() nombre de résultats: {}",result==null?0:result.size());
//		
//		return result;
//
//	}
//	public void deleteLemansImport(Integer numExec, Integer numero) {
//		
//		
////		logger.info("delete Import(exercice="+exercice+", periode"+ periode+")");
////		String requete="delete from gts_import where num_exec=? and no_periode=?";
////		jdbcTemplate.update(requete,new Object[] {exercice,periode});	
//	
//		 CriteriaBuilder cb = this.em.getCriteriaBuilder();
//			CriteriaDelete<ImportedData> delete =cb.createCriteriaDelete(ImportedData.class);
//			Root<ImportedData> root = delete.from(ImportedData.class);
//			Predicate p = cb.conjunction();
//			p = cb.and(p, cb.equal(root.get("exercice"),numExec));
//			p = cb.and(p, cb.equal(root.get("numero"), numero));
//			
//			
//			delete.where(p);
//			em.createQuery(delete).executeUpdate();
//	}
//	
	public List<LiqRecette> getLiqRecettes(Integer exercice, Integer numPeriode,Boolean differe) {
		logger.debug("getLiqRecettes(exercice={},  numPeriode={}, differe={})", exercice, numPeriode,differe);
		List<LiqRecette> result = null;

		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<LiqRecette> cq = cb.createQuery(LiqRecette.class);
		Root<LiqRecette> root = cq.from(LiqRecette.class);
		cq.select(root);

		Predicate p = cb.conjunction();
		if (exercice != null)
			p = cb.and(p, cb.equal(root.get("exercice"), exercice));
		if (numPeriode != null)
			p = cb.and(p, cb.equal(root.get("numPeriode"), numPeriode));
		if (differe != null)
			p = cb.and(p, cb.equal(root.get("differe"), differe));

		cq.where(p);
		TypedQuery<LiqRecette> query = em.createQuery(cq);
		result = query.getResultList();

		logger.debug("getLiqRecettes() nombre de résultats: {}", result == null ? 0 : result.size());

		return result;

	}
	public List<SpectacleDetails> getSpectacleDetails(Integer exercice) {
		logger.debug("getSpectacleDetails : exercice={}",exercice);
		
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<SpectacleDetails> cq = cb.createQuery(SpectacleDetails.class);
			Root<SpectacleDetails> root = cq.from(SpectacleDetails.class);
			cq.select(root).distinct(true);
			
			
			if(exercice!=null){
			Predicate p = cb.conjunction();	
			p = cb.and(p, cb.equal(root.get("exercice"), exercice));
			cq.where(p);
			}
						
			TypedQuery<SpectacleDetails> query = em.createQuery(cq);
			
			List<SpectacleDetails> result = query.getResultList();
			logger.debug("getSpectacleDetails : size of result --> {}",result.size());
		return result;
	}
	public List<Integer> getLemansExercices() {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Integer> cq = cb.createQuery(Integer.class);
		
		Root<SpectacleDetails> root = cq.from(SpectacleDetails.class);
		Expression<Integer> numero=root.get("exercice");
		cq.select(numero).distinct(true);
	
		TypedQuery<Integer> query = em.createQuery(cq);
		List<Integer> result = query.getResultList();
		return result;
	}
	
}