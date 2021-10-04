package fr.symphonie.tools.gts.core;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaDelete;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.logging.log4j.util.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import fr.symphonie.common.util.Constant;
import fr.symphonie.tools.gts.model.Article;
import fr.symphonie.tools.gts.model.ArticleDetails;
import fr.symphonie.tools.gts.model.ClientGts;
import fr.symphonie.tools.gts.model.ImportedData;
import fr.symphonie.tools.gts.model.LiqRecette;
import fr.symphonie.tools.gts.model.PeriodeGts;

@Repository
public class GtsDao {
	private static final Logger logger = LoggerFactory.getLogger(GtsDao.class);

	
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

	public List<Article> getArticles(String searchCondition) {
		logger.debug("getArticles : searchCondition={}",searchCondition);
		List<Article> result = null;
		
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<Article> cq = cb.createQuery(Article.class);
			Root<Article> root = cq.from(Article.class);
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
						
			TypedQuery<Article> query = em.createQuery(cq);
			if(Strings.isBlank(searchCondition))query.setMaxResults(Constant.maxResult);
			result = query.getResultList();
			logger.debug("getArticles : size of result --> {}",result.size());
		return result;
	}
	
	public Article getArticle(String code) {
		logger.debug("getArticle : code={}",code);
		List<Article> result = null;
		
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<Article> cq = cb.createQuery(Article.class);
			Root<Article> root = cq.from(Article.class);
			cq.select(root);
			
			Predicate p = cb.conjunction();	
			p = cb.and(p,cb.equal(root.get("code"), code));
			cq.where(p);

			TypedQuery<Article> query = em.createQuery(cq);
			result = query.getResultList();
			logger.debug("getArticle : size of result --> {}",result.size());
		if ((result == null) || (result.isEmpty()))
			return null;
		return result.get(0);

	}
	public List<PeriodeGts> getPeriodes(Integer exercice, Integer numPeriode, Boolean ferme ) {
		List<PeriodeGts> result = null;
		
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<PeriodeGts> cq = cb.createQuery(PeriodeGts.class);
		Root<PeriodeGts> root = cq.from(PeriodeGts.class);
		cq.select(root);
		
		Predicate p = cb.conjunction();		
		p = cb.and(p, cb.equal(root.get("exercice"), exercice));
		if(numPeriode!=null)
		p = cb.and(p,cb.equal(root.get("numero"), numPeriode));
		if(ferme!=null)
			p = cb.and(p,cb.equal(root.get("closed"), ferme));
		cq.where(p);		
		TypedQuery<PeriodeGts> query = em.createQuery(cq);
		result = query.getResultList();
		return result;
		
	}
	public List<Integer> getListNumPeriode(Integer exercice) {		
		List<Integer> result = null;
		
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Integer> cq = cb.createQuery(Integer.class);
		
		Root<PeriodeGts> root = cq.from(PeriodeGts.class);
		Expression<Integer> numero=root.get("numero");
		cq.select(numero);
		
		Predicate p = cb.conjunction();		
		p = cb.and(p, cb.equal(root.get("exercice"), exercice));
		
		cq.where(p);		
		TypedQuery<Integer> query = em.createQuery(cq);
		result = query.getResultList();
		return result;
	}





	public List<ClientGts> getClientGtsList(String searchCondition) {
		logger.debug("getClientGtsList : searchCondition={}",searchCondition);
		List<ClientGts> result = null;
		
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<ClientGts> cq = cb.createQuery(ClientGts.class);
			Root<ClientGts> root = cq.from(ClientGts.class);
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
			
			TypedQuery<ClientGts> query = em.createQuery(cq);
			if(Strings.isBlank(searchCondition))query.setMaxResults(Constant.maxResult);
			result = query.getResultList();
			logger.debug("getClientGtsList : size of result --> {}",result.size());
		return result;
	}
	
	public ClientGts getClientGts(String codeClient) {
		logger.debug("getClientGts : codeClient={}",codeClient);
		List<ClientGts> result = null;
		
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<ClientGts> cq = cb.createQuery(ClientGts.class);
			Root<ClientGts> root = cq.from(ClientGts.class);
			cq.select(root).distinct(true);
			
			Predicate p = cb.conjunction();	
			Expression<String> codeExp=root.get("code");
			Predicate pAnd = cb.equal(codeExp, codeClient);
			p = cb.and(p,pAnd);
			cq.where(p);
			
			TypedQuery<ClientGts> query = em.createQuery(cq);
			result = query.getResultList();
			if((result==null)||(result.isEmpty()))return null;
			logger.debug("getClientGts : size of result --> {}",result.size());
		return result.get(0);
	}





	public void remove(Object entity) {

		logger.debug("remove(Object entity) em.contains(entity) --> {}",em.contains(entity) );
		em.remove(em.contains(entity) ? entity : em.merge(entity));
		
		
	}
	
	public Article removeArticlDetail(ArticleDetails entity) {

		Article article = em.find(Article.class,entity.getCode() );
		article.getDetails().remove(entity);
		
		 return article;
			}

	public List<ImportedData> getImportedData(Integer exercice, Integer numPeriode, String codeArticle, String codeClientGts) {
		logger.debug("getImportedData(exercice={},  numPeriode={})",exercice,numPeriode);
		List<ImportedData> result = null;

		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<ImportedData> cq = cb.createQuery(ImportedData.class);
		Root<ImportedData> root = cq.from(ImportedData.class);
		cq.select(root);

		Predicate p = cb.conjunction();
		if(exercice!=null)
		p = cb.and(p, cb.equal(root.get("exercice"), exercice));
		if (numPeriode != null)
			p = cb.and(p, cb.equal(root.get("numero"), numPeriode));
		if(codeArticle!=null)
			p = cb.and(p, cb.equal(root.get("codeArticle"), codeArticle));
		if(codeClientGts!=null)
			p = cb.and(p, cb.equal(root.get("codeClientGts"), codeClientGts));
		cq.where(p);
		TypedQuery<ImportedData> query = em.createQuery(cq);
		result = query.getResultList();
		
		logger.debug("getImportedData() nombre de résultats: {}",result==null?0:result.size());
		
		return result;

	}
	public void deleteGtsImport(Integer numExec, Integer numero) {
		
		
//		logger.info("delete Import(exercice="+exercice+", periode"+ periode+")");
//		String requete="delete from gts_import where num_exec=? and no_periode=?";
//		jdbcTemplate.update(requete,new Object[] {exercice,periode});	
	
		 CriteriaBuilder cb = this.em.getCriteriaBuilder();
			CriteriaDelete<ImportedData> delete =cb.createCriteriaDelete(ImportedData.class);
			Root<ImportedData> root = delete.from(ImportedData.class);
			Predicate p = cb.conjunction();
			p = cb.and(p, cb.equal(root.get("exercice"),numExec));
			p = cb.and(p, cb.equal(root.get("numero"), numero));
			
			
			delete.where(p);
			em.createQuery(delete).executeUpdate();
	}
	
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
	public List<ArticleDetails> getArticleDetails(Integer exercice) {
		logger.debug("getArticleDetails : exercice={}",exercice);
		
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<ArticleDetails> cq = cb.createQuery(ArticleDetails.class);
			Root<ArticleDetails> root = cq.from(ArticleDetails.class);
			cq.select(root).distinct(true);
			
			
			if(exercice!=null){
			Predicate p = cb.conjunction();	
			p = cb.and(p, cb.equal(root.get("exercice"), exercice));
			cq.where(p);
			}
						
			TypedQuery<ArticleDetails> query = em.createQuery(cq);
			
			List<ArticleDetails> result = query.getResultList();
			logger.debug("getArticleDetails : size of result --> {}",result.size());
		return result;
	}
	public List<Integer> getGtsExercices() {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Integer> cq = cb.createQuery(Integer.class);
		
		Root<ArticleDetails> root = cq.from(ArticleDetails.class);
		Expression<Integer> numero=root.get("exercice");
		cq.select(numero).distinct(true);
	
		TypedQuery<Integer> query = em.createQuery(cq);
		List<Integer> result = query.getResultList();
		return result;
	}
	
}