package fr.symphonie.budget.core.dao.jpa;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import fr.symphonie.budget.core.dao.IGestionTiersDao;
import fr.symphonie.common.model.User_;
import fr.symphonie.common.model.cpwin.Service_;
import fr.symphonie.common.model.cpwin.TiersDocument_;
import fr.symphonie.common.model.cpwin.Tiers_;
import fr.symphonie.common.util.Constant;
import fr.symphonie.cpwin.model.Adresse;
import fr.symphonie.cpwin.model.FormeSociale;
import fr.symphonie.cpwin.model.Iban;
import fr.symphonie.cpwin.model.ModePaiement;
import fr.symphonie.cpwin.model.Service;
import fr.symphonie.cpwin.model.Tiers;
import fr.symphonie.cpwin.model.TiersDocument;
import fr.symphonie.util.model.User;


/**
 * 
 * @author JEDIDI SOUHAIB(HARMONIE)
 *
 */
@Repository
public class GestionTiersDao implements IGestionTiersDao{
	
	private static final Logger logger = LoggerFactory.getLogger(GestionTiersDao.class);	
	private EntityManager em;

	@PersistenceContext
	public void setEntityManager(EntityManager entityManager) {
		this.em = entityManager;
	}

	public EntityManager getEntityManager() {
		return em;
	}
	
	
	
	@Override
	public void insert(Object entity) {
		em.persist(entity);		
	}

	@Override
	public Object update(Object entity) {		
		return  em.merge(entity);
	}
	
	@Override
	public void remove(Object entity) {
		em.remove(entity);
	}
	@Override
	public List<Tiers> searchTiers(String codeTiers, String numSiren, String etat, Date dateDebut, Date dateFin) {
		List<Tiers> resultList=null;
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Tiers> cq = cb.createQuery(Tiers.class);
		Root<Tiers> root = cq.from(Tiers.class);
		cq.orderBy(cb.asc(root.get(Tiers_.code)));
		cq.select(root).distinct(true);			
		
		Predicate p= cb.conjunction();
		Expression<String> e1=root.get(Tiers_.code);
		Expression<String> e2=root.get(Tiers_.nom);
		Expression<String> e3=root.get(Tiers_.numSiren);
				
		Predicate pOR=cb.like(e1,"%"+codeTiers+"%");
		pOR=cb.or(pOR,cb.like(e2,"%"+codeTiers+"%"));
		p=cb.and(p,pOR);
		Expression<Date> exp=root.get("trace").get("dateMaj");
		if(numSiren!=null)
			p=cb.and(p,cb.like(e3,"%"+numSiren+"%"));
		if(etat!=null)
			p=cb.and(p,cb.equal(root.get(Tiers_.etat),etat));

		if(dateDebut!=null)
			p=cb.and(p,cb.greaterThanOrEqualTo(exp,dateDebut));
		if(dateFin!=null)
			p=cb.and(p,cb.lessThan(exp, dateFin));
		cq.where(p);	
		
		TypedQuery<Tiers> query = em.createQuery(cq);		
		resultList=query.getResultList();
		return resultList;
		
	}
	
	
	@Override
	public Tiers getTiersWithIban(String codeTiers) {		
		Tiers result=null;
		try{
			
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<Object> cq = cb.createQuery(Object.class);
			Root<Tiers> root = cq.from(Tiers.class);

			cq.multiselect(root,root.get("ibans")).distinct(true);			
			Predicate p= cb.conjunction();
			p=cb.and(p,cb.equal(root.get(Tiers_.code),codeTiers));
			cq.where(p);		
			TypedQuery<Object> query = em.createQuery(cq);
			List<Object> resultList=query.getResultList();
			if((resultList==null)||(resultList.isEmpty()))
				result=new Tiers();
			else{
				result=(Tiers) ((Object[])resultList.get(0))[0];
				Set<Iban> ibs=new HashSet<Iban>();
				for(Object trs:resultList){
					Object[] res=(Object[]) trs;
//					result=(Tiers) res[0];					
					Iban ib=(Iban) res[1];
					ibs.add(ib);
				}
				result.setIbans(ibs);
			}				
			
		}catch (NoResultException e) {
			result=new Tiers();
		}
		catch (Exception e) {
			e.printStackTrace();
			result=new Tiers();
		}
		return result;
	}
	
	@Override
	public Tiers getTiersWoIban(String codeTiers) {
		
		Tiers result=null;
		try{	
			
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<Tiers> cq = cb.createQuery(Tiers.class);
			Root<Tiers> root = cq.from(Tiers.class);			
			Predicate p= cb.conjunction();
			p=cb.and(p,cb.equal(root.get(Tiers_.code),codeTiers));
			cq.where(p);		
			TypedQuery<Tiers> query = em.createQuery(cq);
			List<Tiers> resultList=query.getResultList();
			if((resultList==null)||(resultList.isEmpty()))
				result=new Tiers();
			else{
				result=(Tiers) resultList.get(0);				
			}			
			
		}catch (NoResultException e) {
			result=new Tiers();
		}
		catch (Exception e) {
			e.printStackTrace();
			result=new Tiers();
		}
		return result;
	}
	
	@Override
	public List<TiersDocument> searchTiersDoc(String codeTiers) {
		List<TiersDocument> tiersDocs=null;
//		Tiers result=null;
		try{
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<TiersDocument> cq = cb.createQuery(TiersDocument.class);
			Root<TiersDocument> root = cq.from(TiersDocument.class);
			cq.orderBy(cb.asc(root.get(TiersDocument_.id)));
			cq.select(root).distinct(true);			
			Predicate p= cb.conjunction();
			p=cb.and(p,cb.equal(root.get(TiersDocument_.tiers).get(Tiers_.code), codeTiers));
			cq.where(p);		
			TypedQuery<TiersDocument> query = em.createQuery(cq);		
			tiersDocs=query.getResultList();
		}catch (NoResultException e) {
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return tiersDocs;
	}
	
	
	@Override
	public void deleteDocument(int id) {
		Query q = em.createQuery ("delete TiersDocument doc where doc.id = :id");
		q.setParameter("id", id);
		q.executeUpdate();
	}
	
	@Override
	public void deleteAdresse(String codeTiers, int ordre) {
		Query q = em.createQuery ("delete Adresse adr where adr.codeTiers = :codeTiers and adr.ordre = :ordre");
		q.setParameter("codeTiers", codeTiers);
		q.setParameter("ordre", ordre);
		q.executeUpdate();
	}
	
	
	@Override
	public List<FormeSociale> getAllFormeSociale() {
		List<FormeSociale> result = new ArrayList<FormeSociale>();
		try{
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<FormeSociale> cq = cb.createQuery(FormeSociale.class);
			Root<FormeSociale> root = cq.from(FormeSociale.class);		
			cq.select(root).distinct(true);			
//			Predicate p= cb.conjunction();					
			TypedQuery<FormeSociale> query = em.createQuery(cq);		
			result=query.getResultList();
		}catch (NoResultException e) {
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	@Override
	public List<ModePaiement> getAllModePaiement() {
		List<ModePaiement> result= new ArrayList<ModePaiement>();
		try{
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<ModePaiement> cq = cb.createQuery(ModePaiement.class);
			Root<ModePaiement> root = cq.from(ModePaiement.class);		
			cq.select(root).distinct(true);			
//			Predicate p= cb.conjunction();					
			TypedQuery<ModePaiement> query = em.createQuery(cq);		
			result=query.getResultList();
		}catch (NoResultException e) {
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	@Override
	public List<Service> getService(String chaine, boolean searchAtBegin) {
		List<Service> result = new ArrayList<Service>();
		String condition = (searchAtBegin) ? chaine + "%" : "%" + chaine + "%";
		try {
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<Service> cq = cb.createQuery(Service.class);
			Root<Service> root = cq.from(Service.class);
			// cq.orderBy(cb.asc(root.get(Service_.code)));
			cq.select(root).distinct(true);
			Predicate p = cb.conjunction();

			Predicate pOR = cb.like(root.get(Service_.code), condition);
			pOR = cb.or(pOR, cb.like(root.get(Service_.nom), condition));
			p = cb.and(p,pOR);

			// p=cb.and(p,cb.equal(root.get(Service_.code), condition));
			cq.where(p);
			TypedQuery<Service> query = em.createQuery(cq);
			result = query.getResultList();
		} catch (NoResultException e) {
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;

	}

	@Override
	public Service getService(String code) {
		List<Service> result = new ArrayList<Service>();
		Service service = null;
		// String condition = (searchAtBegin) ? chaine + "%" : "%" + chaine +
		// "%";
		try {
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<Service> cq = cb.createQuery(Service.class);
			Root<Service> root = cq.from(Service.class);
			// cq.orderBy(cb.asc(root.get(Service_.code)));
			cq.select(root).distinct(true);
			Predicate p = cb.conjunction();
			//
			// Predicate pOR = cb.like(root.get(Service_.code), condition);
			// pOR = cb.or(pOR, cb.like(root.get(Service_.nom), condition));
			// p = cb.and(p,pOR);

			p = cb.and(p, cb.equal(root.get(Service_.code), code));
			cq.where(p);
			TypedQuery<Service> query = em.createQuery(cq);
			result = query.getResultList();

			if (result != null && result.size() > 0)
				service = result.get(0);
			return service;
		}

		catch (Exception e) {
			e.printStackTrace();
		}
		return service;

	}
	
	@Override
	public Service getTiersService(String codeTiers) {
		
		List<Service> result = new ArrayList<Service>();
		Service service = new Service();	
		try {			
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<Service> cq = cb.createQuery(Service.class);
			Root<Tiers> root = cq.from(Tiers.class);	
//			Join<Tiers,Service> serviceJ=root.join(Tiers_.service);	
			Expression<Service> serviceExp=root.get(Tiers_.service);
			cq.select(serviceExp).distinct(true);
			Predicate p= cb.conjunction();
			p = cb.and(p, cb.equal(root.get(Tiers_.code), codeTiers));			
			cq.where(p);
			TypedQuery<Service> query = em.createQuery(cq);
			result = query.getResultList();
			if (result != null && result.size() > 0)
				service = result.get(0);
			return service;
		}

		catch (Exception e) {
			e.printStackTrace();
		}
		return service;

	}

	@Override
	public User getUser(String login) {
		List<User> result = new ArrayList<User>();
		User user = new User();
		try {
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<User> cq = cb.createQuery(User.class);
			Root<User> root = cq.from(User.class);
		
			cq.select(root).distinct(true);
			Predicate p = cb.conjunction();
			p = cb.and(p, cb.equal(root.get(User_.login), login));
			cq.where(p);
			TypedQuery<User> query = em.createQuery(cq);
			result = query.getResultList();
			if (result != null && result.size() > 0)
				user = result.get(0);
			return user;
		}

		catch (Exception e) {
			e.printStackTrace();
		}
		return user;

	}

	@Override
	public List<Tiers> findLightTiersList(boolean searchAtBegin, String formatedPrefix) {
		List<Tiers> listTiers = null;
		String condition = (searchAtBegin) ? formatedPrefix + "%" : "%" + formatedPrefix + "%";
		
        boolean isFormatedPrefixNull=(formatedPrefix==null)?true:formatedPrefix.trim().isEmpty();
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Tiers> cq = cb.createQuery(Tiers.class);
		Root<Tiers> root = cq.from(Tiers.class);
		Predicate pTiers=null;	
		
		cq.multiselect(root.<String>get(Tiers_.code),root.<String>get(Tiers_.nom));
		Predicate p = cb.conjunction();
		
			  if((isFormatedPrefixNull))
		             return new ArrayList<Tiers>();
			if (!isFormatedPrefixNull) {
				pTiers = cb.like(root.<String>get(Tiers_.code), condition);
				pTiers = cb.or(pTiers, cb.like(root.<String>get(Tiers_.nom), condition));

				p = cb.and(p, pTiers);
			}

			cq.where(p);
			TypedQuery<Tiers> query = em.createQuery(cq);
			query.setMaxResults(Constant.getMaxAjaxResult());
			listTiers = query.getResultList();
		logger.debug("findLightTiersList: size={}",listTiers.size());
		return listTiers;
		
	}
	public Tiers findTiers(String codeTiers) {
		logger.debug("findTiers: codeTiers={}",codeTiers);
		Tiers result=null;
		List<Tiers> listTiers = null;
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Tiers> cq = cb.createQuery(Tiers.class);
		Root<Tiers> root = cq.from(Tiers.class);
		root.fetch("ibans", JoinType.LEFT);
		cq.select(root);
		Predicate p = cb.conjunction();
		try {			
			Predicate pEqual = cb.equal(root.get(Tiers_.code), codeTiers);
			p = cb.and(p, pEqual);
			cq.where(p);
			TypedQuery<Tiers> query = em.createQuery(cq);
			listTiers = query.getResultList();
		
		} catch (Exception e) {
			logger.error("findTiers: error: {}",e.getMessage());
		} 

		if (!CollectionUtils.isEmpty(listTiers))
			result=listTiers.get(0);
		logger.debug("findTiers: result={}",result);
			return result;
	}

	@Override
	public Adresse getAdressCpwinTiers(int noAdress, String codeTiers) {
		
		logger.debug("getAdressCpwinTiers("+noAdress+","+codeTiers+" -start");
		Adresse result = null;
		
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Adresse> cq = cb.createQuery(Adresse.class);
		Root<Adresse> root = cq.from(Adresse.class);

		cq.select(root).distinct(true); 

		Predicate p= cb.conjunction();		
		if(noAdress!=0)
		p=cb.and(p,cb.equal(root.get("ordre"), noAdress));
		p=cb.and(p,cb.equal(root.get("codeTiers"),codeTiers));
		
		cq.where(p);
		TypedQuery<Adresse> query = em.createQuery(cq);
		result=query.getSingleResult();
		logger.debug("getAdressCpwinTiers :"+result+" - end");
	
		return result;	
	}

	
	
}
