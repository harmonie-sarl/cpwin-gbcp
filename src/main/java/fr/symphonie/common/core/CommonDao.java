package fr.symphonie.common.core;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import fr.symphonie.common.model.cpwin.Tiers_;
import fr.symphonie.common.util.Constant;
import fr.symphonie.cpwin.model.Tiers;
import fr.symphonie.cpwin.model.nantes.EmailStatus;
import fr.symphonie.cpwin.model.nantes.EtudiantStatus;
import fr.symphonie.cpwin.model.pk.SepaBicPK;
import fr.symphonie.cpwin.model.sepa.Actor;
import fr.symphonie.cpwin.model.sepa.Bic;
import fr.symphonie.cpwin.model.sepa.Protocol;
import fr.symphonie.tools.meta4dai.model.PaymentItem;
import fr.symphonie.tools.meta4dai.model.Period;
import fr.symphonie.tools.nantes.model.Etudiant;
import fr.symphonie.tools.sct.model.PaymentModeEnum;
import fr.symphonie.tools.sct.model.RefundsBatch;
import fr.symphonie.tools.sct.model.RefundsItem;
import fr.symphonie.tools.signature.model.Signature;

@Repository
public class CommonDao implements fr.symphonie.common.core.ICommonDao{
	private static final Logger logger =LoggerFactory.getLogger(CommonDao.class);
	private EntityManager em;

	@PersistenceContext
	public void setEntityManager(EntityManager entityManager) {
		this.em = entityManager;
	}

	public EntityManager getEntityManager() {
		return em;
	}

	

	@Override
	public <T> T  update(T entity) {
		logger.debug("update : {}", entity);
		return em.merge(entity);
	}
	@Override
	public <T> void insert(T entity) {
		logger.debug("insert: {}",entity);
		em.persist(entity);
	}
	@Override
	public Protocol getProtocol(final String protocole) {
		String requette="select p from Protocol p WHERE p.code='"+protocole+"'";
		TypedQuery<Protocol> q=em.createQuery(requette, Protocol.class);
		Protocol p=q.getSingleResult();
		return p;
	}
	@Override
	public List<Bic> findBicList(SepaBicPK pk, String bic) {
//		if (logger.isDebugEnabled()) {
//			logger.debug("findBicList("+pk+","+bic+") - start"); //$NON-NLS-1$
//		}
		CriteriaBuilder cb=em.getCriteriaBuilder();
		CriteriaQuery<Bic> cq=cb.createQuery(Bic.class);
		Root<Bic> root=cq.from(Bic.class);
		Expression<String> codeBank=root.get("bankCode");
		Expression<String> counterBankCode=root.get("counterBankCode");
		Expression<String> bicExpression=root.get("value");
		cq.select(root);
		Predicate p = cb.conjunction();
	
		if(pk!=null)
		{
		p = cb.and(p, cb.equal(codeBank, pk.getBankCode()));
		p = cb.and(p, cb.equal(counterBankCode, pk.getCounterBankCode()));
		}
		if((bic!=null)&&(!bic.isEmpty()))
		{
		p = cb.and(p, cb.equal(bicExpression,bic.toUpperCase()));
		}
		cq.where(p);
		TypedQuery<Bic> query=em.createQuery(cq);
		List<Bic> result=query.getResultList();
//		if (logger.isDebugEnabled()) {
//			logger.debug("findBicList : size="+result+" - end"); //$NON-NLS-1$
//		}
		return result;
		
	
	}
	@Override
	public List<Integer> getVagueExercises() {
		String requette="select distinct p.exercice from RefundsItem p order by p.exercice";
		
		TypedQuery<Integer> q=em.createQuery(requette, Integer.class);
		List<Integer> list=q.getResultList();
		return list;
	}

	@Override
	public List<Integer> getVagueIds(int exercice, boolean onlyNotGeneratedVague) {
		String requette="select distinct p.noVague from RefundsItem p WHERE p.exercice="+exercice+"";
		if(onlyNotGeneratedVague) {
			requette+=" and (p.noEcriture IS NULL and p.paymentMode in ('"+PaymentModeEnum.RBSF.getValue()+"','"+PaymentModeEnum.RBSI.getValue()+"'))";
		}
		TypedQuery<Integer> q=em.createQuery(requette, Integer.class);
		List<Integer> list=q.getResultList();
		return list;
	}

	@Override
	public List<RefundsItem> getRefundsByVague(Integer noVague) {
		String requette="select  p from RefundsItem p WHERE p.noVague="+noVague+"";
		TypedQuery<RefundsItem> q=em.createQuery(requette, RefundsItem.class);
		List<RefundsItem> list=q.getResultList();
		return list;
	}

	@Override
	public List<Actor> findRefundsTiersList(boolean searchAtBegin, String formatedPrefix) {
		logger.debug("findRefundsTiersList searchAtBegin={}, formatedPrefix={}",searchAtBegin,formatedPrefix);
		List<Actor> listTiers = null;
		String condition = (searchAtBegin) ? formatedPrefix + "%" : "%" + formatedPrefix + "%";
        boolean isFormatedPrefixNull=(formatedPrefix==null)?true:formatedPrefix.trim().isEmpty();
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Actor> cq = cb.createQuery(Actor.class);
		Root<RefundsItem> root = cq.from(RefundsItem.class);
		Expression<Actor> actor=root.get("actor")	;
		Expression<String> name=root.get("actor").get("name");
		Expression<String> firstName=root.get("actor").get("firstName");
		cq.select(actor).distinct(true);
		Predicate p = cb.conjunction();
		Predicate t = cb.conjunction();
			  if((isFormatedPrefixNull))
		             return new ArrayList<Actor>();
			if (!isFormatedPrefixNull) {
				t = cb.and(t, cb.like(name, condition));
				t = cb.or(t, cb.like(firstName, condition));
				p = cb.and(p, t);
			}
			
			cq.where(p);
			TypedQuery<Actor> query = em.createQuery(cq);
			listTiers= query.getResultList();
		
			return listTiers;
		
	}

	@Override
	public List<RefundsItem> getRefundsByCustomer(Integer exercice, String customerName) {
		List<RefundsItem> refundsList = null;
		
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<RefundsItem> cq = cb.createQuery(RefundsItem.class);
		Root<RefundsItem> root = cq.from(RefundsItem.class);
			
		Expression<String> name=root.get("actor").get("name");
		cq.select(root);
		Predicate p = cb.conjunction();
			p = cb.and(p, cb.equal(root.get("exercice"), exercice));
			if (!StringUtils.isBlank(customerName)) {
				p = cb.and(p, cb.equal(name, customerName));
			}
			
			cq.where(p);
			TypedQuery<RefundsItem> query = em.createQuery(cq);
			refundsList = query.getResultList();

		return refundsList;
	}

	@Override
	public List<RefundsBatch> getVagueList(Integer exercice, long crc32) {
		List<RefundsBatch> refundsList = null;
		
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<RefundsBatch> cq = cb.createQuery(RefundsBatch.class);
		Root<RefundsBatch> root = cq.from(RefundsBatch.class);
		cq.select(root);
		Predicate p = cb.conjunction();
		
			p = cb.and(p, cb.equal(root.get("exercice"), exercice));
			p = cb.and(p, cb.equal(root.get("crc32"), crc32));
			
			cq.where(p);
			TypedQuery<RefundsBatch> query = em.createQuery(cq);
			refundsList = query.getResultList();

		return refundsList;
	}
	@Override
	public List<Integer> getMeta4DaiExercices() {
		String requette="select distinct p.exercice from Period p ";
		TypedQuery<Integer> q=em.createQuery(requette, Integer.class);		
		List<Integer> result=q.getResultList();
		return result;
	}
	@Override
	public List<Period> getMeta4DaiPeriodList(Integer exercice, String codeBudget) {
		String requette="select p from Period p WHERE p.exercice=? and p.budget=?";
		TypedQuery<Period> q=em.createQuery(requette, Period.class);
		q.setParameter(1, exercice);
		q.setParameter(2, codeBudget);
		List<Period> result=q.getResultList();
		return result;
	}

	@Override
	public List<PaymentItem> loadMeta4DaiPaymentData(Integer exercice, String budget, String period) {
		logger.debug("loadMeta4DaiPaymentData: {} - {} - {}",exercice,budget,period);
		String requette="select p from PaymentItem p WHERE p.exercice=? and p.budget=? and p.period=?";
		TypedQuery<PaymentItem> q=em.createQuery(requette, PaymentItem.class);
		q.setParameter(1, exercice);
		q.setParameter(2, budget);
		q.setParameter(3,period);
		List<PaymentItem> result=q.getResultList();
		return result;
	}

	@Override
	public int removeMeta4DaiPaymentData(Integer exercice, String budget, String period) {
		logger.debug("removeMeta4DaiPaymentData: {} - {} - {}",exercice,budget,period);
		String requette="delete from PaymentItem  WHERE exercice=? and budget=? and period=?";
		Query q=em.createQuery(requette);
		q.setParameter(1, exercice);
		q.setParameter(2, budget);
		q.setParameter(3,period);
		return q.executeUpdate();

		
	}

	@Override
	public void updateEtudiantEmailStatus(String matricule, EmailStatus status) {
		Etudiant e=em.find(Etudiant.class, matricule);
		e.setEmailStatus(status);
		e.setEmailDate(new Date());
		
	}

	@Override
	public List<Etudiant> getEtudiantList(EtudiantStatus status) {
		logger.debug("getEtudiantList: status={} ",status);
		
		String requette="select e from Etudiant e";
		if(status!=null) {
			requette=String.join(" ", requette,"WHERE e.status=?");
		}
		TypedQuery<Etudiant> q=em.createQuery(requette, Etudiant.class);
		if(status!=null) {
			q.setParameter(1, status.name());
		}		
		List<Etudiant> result=q.getResultList();
		return result;
	}
	@Override
	public List<Etudiant> findEtudiantList(boolean searchAtBegin, String formatedPrefix) {
		logger.debug("findEtudiantList searchAtBegin={}, formatedPrefix={}",searchAtBegin,formatedPrefix);
		List<Etudiant> listEtudiants = null;
		String condition = (searchAtBegin) ? formatedPrefix + "%" : "%" + formatedPrefix + "%";
        boolean isFormatedPrefixNull=(formatedPrefix==null)?true:formatedPrefix.trim().isEmpty();
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Etudiant> cq = cb.createQuery(Etudiant.class);
		Root<Etudiant> root = cq.from(Etudiant.class);
		
		Expression<String> name=root.get("nom");
		Expression<String> firstName=root.get("prenom");
		Predicate p = cb.conjunction();
		Predicate t = cb.conjunction();
			  if((isFormatedPrefixNull))
		             return new ArrayList<Etudiant>();
			if (!isFormatedPrefixNull) {
				t = cb.and(t, cb.like(name, condition));
				t = cb.or(t, cb.like(firstName, condition));
				p = cb.and(p, t);
				p = cb.and(p, cb.equal(root.get("status"), EtudiantStatus.TRANSFERE.name()));
				
			}
			
			cq.where(p);
			TypedQuery<Etudiant> query = em.createQuery(cq);
			listEtudiants= query.getResultList();
		
			return listEtudiants;
		
	}

	@Override
	public int updateEtudiantEmailStatus(EmailStatus from, EmailStatus to) {
		logger.debug("updateEtudiantEmailStatus: from {} -> to {} ",from,to);
		String requette="update from Etudiant set emailStatus=?  WHERE emailStatus=?";
		Query q=em.createQuery(requette);
		q.setParameter(1, to.name());
		q.setParameter(2, from.name());
		return q.executeUpdate();
		
	}

	@Override
	public List<Signature> getSignatureList(String userCode) {
		String requette="select s from Signature s ";
		if(	userCode!=null) {
			requette=String.format("select s from Signature s WHERE s.codeUtil='%s' ",userCode);
		}
		logger.debug("getSignatureList: requette={}",requette);
		TypedQuery<Signature> q=em.createQuery(requette, Signature.class);
		List<Signature> list=q.getResultList();
		return list;
	}

	@Override
	public List<Tiers> findTiersList(boolean searchAtBegin, String formatedPrefix, boolean isStudent) {
		List<Tiers> listTiers = null;
		logger.debug("findTiersList: isStudent={}",isStudent);
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
			
				pTiers = cb.like(root.<String>get(Tiers_.code), condition);
				pTiers = cb.or(pTiers, cb.like(root.<String>get(Tiers_.nom), condition));
				if(isStudent)
					pTiers = cb.and(pTiers,cb.like(root.<String>get(Tiers_.code), String.format("%s%s", Constant.NANTES_ENSA_STUDENT_PREFIX,"%")));
				p = cb.and(p, pTiers);
			

			cq.where(p);
			TypedQuery<Tiers> query = em.createQuery(cq);
			query.setMaxResults(Constant.getMaxAjaxResult());
			listTiers = query.getResultList();
		logger.debug("findTiersList: size={}",listTiers.size());
		return listTiers;
	}

}