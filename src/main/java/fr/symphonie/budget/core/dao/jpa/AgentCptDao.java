package fr.symphonie.budget.core.dao.jpa;

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.stereotype.Repository;

import fr.symphonie.budget.core.dao.IAgentCptDao;
import fr.symphonie.cpwin.model.Banque;
import fr.symphonie.cpwin.model.Iban;
import fr.symphonie.cpwin.model.Tiers;
import fr.symphonie.cpwin.model.pk.BanquePK;




@Repository
public class AgentCptDao implements IAgentCptDao{
	
	/**
	 * Logger for this class
	 */
//	private static final Logger logger = LoggerFactory.getLogger(AgentCptDao.class);

	public static final int IBAN_CODE_BANQUE_INDEX = 5;
	public static final int COUNTRY_SIZE = 2;
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
	public void update(Object entity) {
		em.merge(entity);
	}
	@Override
	public List<Banque> searchBanquesAbsorbes(String codePays, String codeEtab,
			Date dateFinDiffusion, boolean notNullDateFD) {
		String critere="";
		if((codePays!=null)&&(!codePays.equals("")))
			critere+=" AND b.codePays=:pays";
		if((codeEtab!=null)&&(!codeEtab.equals("")))
			critere+=" AND b.codeBanque like lower(:codeEtab)";
		if(dateFinDiffusion!=null)
			critere+=" AND b.finDiffusion<=:finDiffusion";
		if(notNullDateFD)critere+=" AND b.finDiffusion is not null ";
		//ajouter cas de etatIban
		String qlString = "SELECT DISTINCT b FROM Banque b WHERE 1=1 "+critere;
		TypedQuery<Banque> query=em.createQuery(qlString, Banque.class);

		if((codePays!=null)&&(!codePays.equals("")))
			query.setParameter("pays", codePays);
		if((codeEtab!=null)&&(!codeEtab.equals("")))
			query.setParameter("codeEtab", "%"+codeEtab+"%");
		if(dateFinDiffusion!=null)
			query.setParameter("finDiffusion", dateFinDiffusion);
		return query.getResultList();
	}

	@Override
	public int getOpenedIban(BanquePK pk) {

		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Long> cq = cb.createQuery(Long.class);

		Root<Iban> root = cq.from(Iban.class);
		Expression<Long> count=cb.count(root.get("iban"));
		Expression<String> iban=root.get("iban");
		cq.select(count);
		Predicate p= cb.conjunction();
		p=cb.and(p,cb.equal(root.get("ouvertB"),true));
		p=cb.and(p,cb.equal(cb.substring(iban, 1, COUNTRY_SIZE),pk.getCodePays()));
		p=cb.and(p,cb.equal(cb.substring(iban, IBAN_CODE_BANQUE_INDEX, getCodeBanqueSize(pk)),pk.getCodeBanque()));
		cq.where(p);
		TypedQuery<Long> query = em.createQuery(cq);
		
		Long result = query.getSingleResult();
		return result.intValue();
	}

	private int getCodeBanqueSize(BanquePK pk) {
		int FR_CODE_BANQUE_SIZE=5;
	if(pk.getCodePays()=="FR")return FR_CODE_BANQUE_SIZE;
	else return pk.getCodeBanque().length();
}

	@Override
	public List<Tiers> getTiers(BanquePK banquePK) {
		List<Tiers> result=null;

		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Tiers> cq = cb.createQuery(Tiers.class);
		Root<Tiers> root = cq.from(Tiers.class);
		cq.select(root);
		Join<Tiers,Iban> iban=root.join("ibans",JoinType.INNER);
	
		Expression<String> ibanValue=iban.get("iban");
		Predicate p= cb.conjunction();
		p=cb.and(p,cb.equal(cb.substring(ibanValue, 1, COUNTRY_SIZE),banquePK.getCodePays()));
		p=cb.and(p,cb.equal(cb.substring(ibanValue, IBAN_CODE_BANQUE_INDEX, getCodeBanqueSize(banquePK)),banquePK.getCodeBanque()));

		cq.where(p);
		TypedQuery<Tiers> query = em.createQuery(cq);
		result=query.getResultList();
		return result;
	}

	@Override
	public List<Iban> getTiersIbanList(BanquePK pk, String codeTiers) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Iban> cq = cb.createQuery(Iban.class);

		Root<Iban> root = cq.from(Iban.class);
		Expression<String> iban=root.get("iban");
		Join<Iban,Tiers> tiers=root.join("tiers");
		cq.select(root);
		
		Predicate p= cb.conjunction();
//		p=cb.and(p,cb.equal(root.get("ouvertB"),true));
		p=cb.and(p,cb.equal(cb.substring(iban, 1, COUNTRY_SIZE),pk.getCodePays()));
		p=cb.and(p,cb.equal(cb.substring(iban, IBAN_CODE_BANQUE_INDEX, getCodeBanqueSize(pk)),pk.getCodeBanque()));
		p=cb.and(p,cb.equal(tiers.get("code"),codeTiers));
		cq.where(p);
		TypedQuery<Iban> query = em.createQuery(cq);
		
		return query.getResultList();
	}

	@Override
	public int getTiersOpenedIban(BanquePK pk, String codeTiers) {
//		if (logger.isDebugEnabled()) {
//			logger.debug("getTiersOpenedIban(BanquePK:"+pk.getCodePays()+"-"+pk.getCodeBanque()+","+codeTiers+") - start"); //$NON-NLS-1$
//		}

		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Long> cq = cb.createQuery(Long.class);

		Root<Iban> root = cq.from(Iban.class);
		Expression<String> iban=root.get("iban");
		Join<Iban,Tiers> tiers=root.join("tiers");
		Expression<Long> count=cb.count(root.get("iban"));
		cq.select(count);
		
		Predicate p= cb.conjunction();
		p=cb.and(p,cb.equal(root.get("ouvertB"),true));
		p=cb.and(p,cb.equal(cb.substring(iban, 1, COUNTRY_SIZE),pk.getCodePays()));
		p=cb.and(p,cb.equal(cb.substring(iban, IBAN_CODE_BANQUE_INDEX, getCodeBanqueSize(pk)),pk.getCodeBanque()));
		p=cb.and(p,cb.equal(tiers.get("code"),codeTiers));
		cq.where(p);
		TypedQuery<Long> query = em.createQuery(cq);
		

		int returnint = query.getSingleResult().intValue();
//		if (logger.isDebugEnabled()) {
//			logger.debug("getTiersOpenedIban(count="+returnint+") - end"); //$NON-NLS-1$
//		}
		return returnint;
	}
	@SuppressWarnings("unchecked")
	@Override
	public List<String> loadPaysList() {
		String QUERY_NAME="getCodepaysFromBanque";
//		String qlString = "SELECT DISTINCT b.codePays FROM Banque b";
//		Query query = em.createQuery(qlString);
		Query query = em.createNamedQuery(QUERY_NAME);
		return query.getResultList();
	}

}
