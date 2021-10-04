package fr.symphonie.budget.core.dao.jpa;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaDelete;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import fr.symphonie.budget.core.dao.IBudgetPluriannuelDao;
import fr.symphonie.budget.core.model.DepRecEnum;
import fr.symphonie.budget.core.model.edition.EditionBiBr;
import fr.symphonie.budget.core.model.edition.TabsEnum;
import fr.symphonie.budget.core.model.edition.util.Operation;
import fr.symphonie.budget.core.model.pluri.BudgModification;
import fr.symphonie.budget.core.model.pluri.BudgetCpDest;
import fr.symphonie.budget.core.model.pluri.BudgetPluriannuel;
import fr.symphonie.budget.core.model.pluri.CpGestionnaire;
import fr.symphonie.budget.core.model.pluri.CpModification;
import fr.symphonie.budget.core.model.pluri.CpModificationItem;
import fr.symphonie.budget.core.model.pluri.CreditPaiement;
import fr.symphonie.budget.core.model.pluri.EnvelopBudg;
import fr.symphonie.budget.core.model.pluri.EtatEnum;
import fr.symphonie.budget.core.model.pluri.LigneBudgetaireAE;
import fr.symphonie.budget.core.model.pluri.ModificationCpLigne;
import fr.symphonie.common.util.Constant;


/**
 * 
 * @author JEDIDI SOUHAIB(HARMONIE)
 *
 */
@Repository
public class BudgetPluriannuelDao implements IBudgetPluriannuelDao {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = LoggerFactory.getLogger(BudgetPluriannuelDao.class);

	private EntityManager em;

	@PersistenceContext
	public void setEntityManager(EntityManager entityManager) {
		this.em = entityManager;
	}

	public EntityManager getEntityManager() {
		return em;
	}

	@Override
	public BudgetPluriannuel getBudget(int numExec, String codeBudget){
		logger.debug("getBudget("+numExec+","+codeBudget+" -start");

		BudgetPluriannuel result=null;
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<BudgetPluriannuel> cq = cb.createQuery(BudgetPluriannuel.class);
		Root<BudgetPluriannuel> root = cq.from(BudgetPluriannuel.class);

		cq.select(root).distinct(true); 

		Predicate p= cb.conjunction();			
		p=cb.and(p,cb.equal(root.get("exercice"), numExec));
		p=cb.and(p,cb.equal(root.get("codeBudget"),codeBudget));
		
		cq.where(p);
		TypedQuery<BudgetPluriannuel> query = em.createQuery(cq);
		result=query.getSingleResult();
		logger.debug("getBudget :"+result+" - end");
	
		return result;	
	}

	@Override
	public BudgetPluriannuel saveBudget(BudgetPluriannuel budget) {
				
		return em.merge(budget);
		
	}

	@Override
	public void insert(Object obj) {
		em.persist(obj);
		
	}

	@Override
	public void saveEnvelopBudg(EnvelopBudg envelopBudg) {
		em.merge(envelopBudg);
		
	}
	@Override
	public EnvelopBudg loadEnvelop(final EnvelopBudg envelopBudg, boolean withCpList,
			boolean withLbList, boolean withEnvInterne) {
		EnvelopBudg result = null;
		logger.debug("loadEnvelop : "+envelopBudg);

			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<EnvelopBudg> cq = cb.createQuery(EnvelopBudg.class);
			Root<EnvelopBudg> root = cq.from(EnvelopBudg.class);
			cq.select(root).distinct(true);
			if(withCpList)root.fetch("listCreditPaiement",JoinType.LEFT);
			if(withLbList)root.fetch("listLigneBudg",JoinType.LEFT);
			if(withEnvInterne)root.fetch("listEnvelopInterne",JoinType.LEFT);
			Predicate p = cb.conjunction();
			p = cb.and(p, cb.equal(root.get("codeBudget"), envelopBudg.getCodeBudget()));
			p = cb.and(p, cb.equal(root.get("exercice"), envelopBudg.getExercice()));
			p = cb.and(p, cb.equal(root.get("groupDest"), envelopBudg.getGroupDest()));
			p = cb.and(p, cb.equal(root.get("groupNat"), envelopBudg.getGroupNat()));

			cq.where(p);
			TypedQuery<EnvelopBudg> query = em.createQuery(cq);
			result = query.getSingleResult();

		return result;
	}
	
	
	@Override
	public List <CreditPaiement> loadCreditPaiement(int execCp, String codeBudget, String groupNat) {
		List <CreditPaiement> result = null;
		logger.debug("loadCreditPaiement : execCp={}, codeBudget={},groupNat={}",execCp,codeBudget,groupNat);
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<CreditPaiement> cq = cb.createQuery(CreditPaiement.class);
			Root<CreditPaiement> root = cq.from(CreditPaiement.class);
			cq.select(root).distinct(true);
			Predicate p = cb.conjunction();
			p = cb.and(p, cb.equal(root.get("codeBudget"),codeBudget));
			p = cb.and(p, cb.equal(root.get("exerciceCP"), execCp));
			if(groupNat!=null)
			p = cb.and(p, cb.equal(root.get("groupNat"), groupNat));
			cq.where(p);
			TypedQuery<CreditPaiement> query = em.createQuery(cq);
			result = query.getResultList();
			logger.debug("loadCreditPaiement : execCp="+execCp+", codeBudget="+codeBudget+", taille de la liste ="+(result!=null?result.size():0));

		return result;
	}

	@Override
	public List<BudgetCpDest> getBudgetCpDest(int exerciceCp,
			String codeBudget, String groupNat,Integer niveau) {
		List <BudgetCpDest> result = null;
		logger.info("getBudgetCpDest : exerciceCp="+exerciceCp+", codeBudget="+codeBudget+", groupNat="+groupNat);

			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<BudgetCpDest> cq = cb.createQuery(BudgetCpDest.class);
			Root<BudgetCpDest> root = cq.from(BudgetCpDest.class);
			cq.select(root).distinct(true);

			Predicate p = cb.conjunction();
			p = cb.and(p, cb.equal(root.get("codeBudget"),codeBudget));
			p = cb.and(p, cb.equal(root.get("exerciceCP"), exerciceCp));
			if(niveau!=null)
			p = cb.and(p, cb.equal(root.get("niveau"), niveau));
			if(groupNat!=null)
			p = cb.and(p, cb.equal(root.get("groupNat"), groupNat));

			cq.where(p);
			TypedQuery<BudgetCpDest> query = em.createQuery(cq);
			result = query.getResultList();
			if(result!=null)logger.debug("getBudgetCpDest : taille de la liste ="+result.size());

		return result;
	}

	@Override
	public void saveBudgetCpParDest(List<BudgetCpDest> listBudgetCpDest) {
		logger.info("saveBudgetCpParDest: start");
		for(BudgetCpDest cpDest:listBudgetCpDest){
			em.merge(cpDest);
		}
		logger.info("saveBudgetCpParDest: end");
	}

	@Override
	public List<CpGestionnaire> getCpGestionnaireList(int exercice,
			String codeBudget, String groupNat) {
		List <CpGestionnaire> result = null;
		logger.info("getCpGestionnaireList : exerciceCp="+exercice+", codeBudget="+codeBudget+", groupNat="+groupNat);

			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<CpGestionnaire> cq = cb.createQuery(CpGestionnaire.class);
			Root<CpGestionnaire> root = cq.from(CpGestionnaire.class);
			cq.select(root).distinct(true);

			Predicate p = cb.conjunction();
			p = cb.and(p, cb.equal(root.get("codeBudget"),codeBudget));
			p = cb.and(p, cb.equal(root.get("exerciceCP"), exercice));
			p = cb.and(p, cb.equal(root.get("groupNat"), groupNat));

			cq.where(p);
			TypedQuery<CpGestionnaire> query = em.createQuery(cq);
			result = query.getResultList();
			if(result!=null)logger.debug("getCpGestionnaireList : taille de la liste ="+result.size());

		return result;
	}

	@Override
	public void saveCpGestionnaire(List<CpGestionnaire> listCpGestionnaire) {
		logger.info("saveCpGestionnaire : start");
		for(CpGestionnaire cpGest:listCpGestionnaire)
			em.merge(cpGest);
		
		logger.info("saveCpGestionnaire : end");
		
	}
	@Override
	public	void saveCpModification(CpModification modification)	
	{
		logger.debug("saveCpModification- start: {}",modification);	
		em.persist(modification);
		logger.debug("saveCpModification- end: ");	
	}

	@Override
	public List<BudgModification> getBudgModifList(int exercice,
			String typeModif, EtatEnum etatCp) {
		List <BudgModification> result = null;
		logger.info("getBudgModifList : exerciceCp="+exercice+", typeModif="+typeModif+", etatCp="+etatCp);

			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<BudgModification> cq = cb.createQuery(BudgModification.class);
			Root<BudgModification> root = cq.from(BudgModification.class);
			cq.select(root).distinct(true);

			Predicate p = cb.conjunction();
			p = cb.and(p, cb.equal(root.get("type"),typeModif));
			p = cb.and(p, cb.equal(root.get("exercice"), exercice));
			if(etatCp!=null)
				p = cb.and(p, cb.equal(root.get("etatCp"), etatCp.getEtat()));
			cq.where(p);
			TypedQuery<BudgModification> query = em.createQuery(cq);
			result = query.getResultList();
			if(result!=null)logger.debug("getBudgModifList : taille de la liste ="+result.size());

		return result;
	}

	@Override
	public List<CpGestionnaire> getCpGestionnaireList(int exercice,
			String codeBudget, String groupNat, String gestionnaire) {
		List <CpGestionnaire> result = null;
		logger.info("getCpGestionnaireList : exerciceCp="+exercice+", codeBudget="+codeBudget+", groupNat="+groupNat+", gestionnaire="+gestionnaire);

			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<CpGestionnaire> cq = cb.createQuery(CpGestionnaire.class);
			Root<CpGestionnaire> root = cq.from(CpGestionnaire.class);
			cq.select(root).distinct(true);

			Predicate p = cb.conjunction();
			p = cb.and(p, cb.equal(root.get("codeBudget"),codeBudget));
			p = cb.and(p, cb.equal(root.get("exerciceCP"), exercice));
			if((groupNat!=null)&&(!groupNat.trim().isEmpty()))
			p = cb.and(p, cb.equal(root.get("groupNat"), groupNat));
			p = cb.and(p, cb.equal(root.get("gestionnaire"), gestionnaire));

			cq.where(p);
			TypedQuery<CpGestionnaire> query = em.createQuery(cq);
			result = query.getResultList();
			if(result!=null)logger.debug("getCpGestionnaireList : taille de la liste ="+result.size());

		return result;
	}

	@Override
	public void saveModificationCpLigne(ModificationCpLigne modification) {
		logger.info("saveModificationCpLigne : start");
		em.persist(modification);
		logger.info("saveModificationCpLigne : end");
		
	}

	@Override
	public void saveDematBiBr(List<EditionBiBr> bibrList) {
		logger.info("saveDematBiBr: batchSize="+getBatchSize()+", list size="+bibrList.size());
		int i = 0;
        for (EditionBiBr item:bibrList){
            em.persist(item);
            i++;
            if (i % getBatchSize() == 0) {
            	logger.debug("saveDematBiBr: flush iteration="+i);
                em.flush();
                em.clear();
            }
           
        }
        em.flush();
        em.clear();
		
	}

	@Override
	public void deleteDematBiBr(int exercice, String codeBudget, int noBiBr) {
		 CriteriaBuilder cb = this.em.getCriteriaBuilder();
		CriteriaDelete<EditionBiBr> delete =cb.createCriteriaDelete(EditionBiBr.class);
		Root<EditionBiBr> root = delete.from(EditionBiBr.class);
		Predicate p = cb.conjunction();
		p = cb.and(p, cb.equal(root.get("codeBudget"),codeBudget));
		p = cb.and(p, cb.equal(root.get("exercice"), exercice));
		p = cb.and(p, cb.equal(root.get("noMouvement"), noBiBr));
		
		delete.where(p);
		em.createQuery(delete).executeUpdate();
		
	}

	@Override
	public List<EditionBiBr> getDematBiBrList(int exercice, String codeBudget, int noBiBr, TabsEnum dematType) {
		 CriteriaBuilder cb = this.em.getCriteriaBuilder();
			CriteriaQuery<EditionBiBr> cq =cb.createQuery(EditionBiBr.class);
			Root<EditionBiBr> root = cq.from(EditionBiBr.class);
			Predicate p = cb.conjunction();
			p = cb.and(p, cb.equal(root.get("codeBudget"),codeBudget));
			p = cb.and(p, cb.equal(root.get("exercice"), exercice));
			p = cb.and(p, cb.equal(root.get("noMouvement"), noBiBr));
			if(dematType!=null)
				p = cb.and(p, cb.equal(root.get("typeData"), dematType.getType()));
			cq.where(p);
			
		return em.createQuery(cq).getResultList();
	}

	@Override
	public List<Operation> getDematBiBRTab5(Integer exercice, String codeBudget, Integer niveau) {
		CriteriaBuilder cb = this.em.getCriteriaBuilder();
		CriteriaQuery<Operation> cq =cb.createQuery(Operation.class);
		Root<Operation> root = cq.from(Operation.class);
		Predicate p = cb.conjunction();
		p = cb.and(p, cb.equal(root.get("codeBudget"),codeBudget));
		p = cb.and(p, cb.equal(root.get("exercice"), exercice));
		p = cb.and(p, cb.equal(root.get("noMouvement"), niveau));
		
		cq.where(p);
		
	return em.createQuery(cq).getResultList();
	}

	@Override
	public void saveOperations(List<Operation> list) {
		for(Operation op:list)em.merge(op);
		
	}
	
	
	@Override
	public double getCreditOuvert(Integer exercice, String codeBudget, String codeGest) {
		double sumCreditOuvert=0d;
		List <CpGestionnaire>listCpGest=null;
		CriteriaBuilder cb = this.em.getCriteriaBuilder();
		CriteriaQuery<CpGestionnaire> cq =cb.createQuery(CpGestionnaire.class);
		Root<CpGestionnaire> root = cq.from(CpGestionnaire.class);
		Predicate p = cb.conjunction();
		p = cb.and(p, cb.equal(root.get("codeBudget"),codeBudget));
		p = cb.and(p, cb.equal(root.get("exerciceCP"), exercice));
		p = cb.and(p, cb.equal(root.get("gestionnaire"), codeGest));
		
		cq.where(p);
		
		listCpGest= em.createQuery(cq).getResultList();
		if(listCpGest!=null);
		for (CpGestionnaire cpGestionnaire : listCpGest) {
			sumCreditOuvert+=cpGestionnaire.getCreditOuvert();
		}
		return sumCreditOuvert;
	}

	@Override
	public Integer getMaxNoLbgCp(int exercice, String codeBudget) {
		logger.debug("getMaxNoLbgCp - {}/{} ",exercice,codeBudget);
		Integer result=null;
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Integer> cq = cb.createQuery(Integer.class);
		Root<CpGestionnaire> root = cq.from(CpGestionnaire.class);
		Expression<Integer> noLbgCp=root.get("noLbgCp");
		Expression<Integer> max=cb.max(noLbgCp);
		cq.select(max);

		Predicate p = cb.conjunction();
		p = cb.and(p, cb.equal(root.get("codeBudget"),codeBudget));
		p = cb.and(p, cb.equal(root.get("exerciceCP"), exercice));	

		cq.where(p);
		TypedQuery<Integer> query = em.createQuery(cq);
		result=query.getSingleResult();
		logger.debug("getMaxNoLbgCp: result={}",result);
		return (result==null?0:result);
	}
	@Override
	public List<CpModification> getCpModifList(int exercice, String codeBudget) {
		List <CpModification> result = null;
		logger.info("getCpModifList : exercice="+exercice+", codebudget="+codeBudget);

			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<CpModification> cq = cb.createQuery(CpModification.class);
			Root<CpModification> root = cq.from(CpModification.class);
			cq.select(root).distinct(true);

			Predicate p = cb.conjunction();
			
			p = cb.and(p, cb.equal(root.get("exercice"), exercice));
			if(codeBudget!=null)
				p = cb.and(p, cb.equal(root.get("codeBudget"), codeBudget));
			cq.where(p);
			TypedQuery<CpModification> query = em.createQuery(cq);
			result = query.getResultList();
			if(result!=null)logger.debug("getCpModifList : taille de la liste ="+result.size());

		return result;
	}

	@Override
	public CpGestionnaire getCpGestionnaire(Integer exercice, String codeBudget, Integer noLbgCp) {
		CpGestionnaire result = null;
		logger.info("getCpGestionnaireList : exerciceCp="+exercice+", codeBudget="+codeBudget+", noLbgCp="+noLbgCp);

			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<CpGestionnaire> cq = cb.createQuery(CpGestionnaire.class);
			Root<CpGestionnaire> root = cq.from(CpGestionnaire.class);
			cq.select(root).distinct(true);

			Predicate p = cb.conjunction();
			p = cb.and(p, cb.equal(root.get("codeBudget"),codeBudget));
			p = cb.and(p, cb.equal(root.get("exerciceCP"), exercice));
			p = cb.and(p, cb.equal(root.get("noLbgCp"), noLbgCp));

			cq.where(p);
			TypedQuery<CpGestionnaire> query = em.createQuery(cq);
			result = query.getSingleResult();
			if(result!=null)logger.debug("getCpGestionnaireList : taille de la liste ="+result);

		return result;
	}
	
	public List<CpModificationItem> getItemscpModifsList(Integer exercice, String codeBudget, Integer modification) {
		
		List <CpModificationItem> result = null;
		logger.info("getItemscpModifsList : exercice="+exercice+", codebudget="+codeBudget+" , modification="+modification);

			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<CpModificationItem> cq = cb.createQuery(CpModificationItem.class);
			Root<CpModificationItem> root = cq.from(CpModificationItem.class);
			cq.select(root).distinct(true);

			Predicate p = cb.conjunction();
			
			p = cb.and(p, cb.equal(root.get("exercice"), exercice));
			if(codeBudget!=null)
				p = cb.and(p, cb.equal(root.get("codeBudget"), codeBudget));
				p = cb.and(p, cb.equal(root.get("numero"), modification));
			cq.where(p);
			TypedQuery<CpModificationItem> query = em.createQuery(cq);
			result = query.getResultList();
			if(result!=null)logger.debug("getItemscpModifsList : taille de la liste ="+result.size());

		return result;
	}

	@Override
	public List<LigneBudgetaireAE> getInterneLbList(Integer exercice, DepRecEnum type,String codeBudget) {
		List <LigneBudgetaireAE> result = null;
		logger.info("getInterneLbList : exercice={}, type={}"+exercice,type);

			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<LigneBudgetaireAE> cq = cb.createQuery(LigneBudgetaireAE.class);
			Root<LigneBudgetaireAE> root = cq.from(LigneBudgetaireAE.class);
			cq.select(root).distinct(true);

			Predicate p = cb.conjunction();
			
			p = cb.and(p, cb.equal(root.get("exercice"), exercice));
			if(type!=null)
				p = cb.and(p, cb.equal(root.get("typeDepRec"), type.getValue()));
			if(codeBudget!=null)
				p = cb.and(p, cb.equal(root.get("codeBudget"), codeBudget));
				
			cq.where(p);
			TypedQuery<LigneBudgetaireAE> query = em.createQuery(cq);
			result = query.getResultList();
			if(result!=null)logger.debug("getInterneLbList : taille de la liste ="+result.size());

		return result;
	}

	@Override
	public LigneBudgetaireAE findLbi(Integer exercice, Integer noLbi) {
		LigneBudgetaireAE result = null;
		logger.info("findLbi : exercice={}, noLbi={}"+exercice,noLbi);

			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<LigneBudgetaireAE> cq = cb.createQuery(LigneBudgetaireAE.class);
			Root<LigneBudgetaireAE> root = cq.from(LigneBudgetaireAE.class);
			cq.select(root).distinct(true);

			Predicate p = cb.conjunction();
			
			p = cb.and(p, cb.equal(root.get("exercice"), exercice));
			p = cb.and(p, cb.equal(root.get("noLbi"), noLbi));
				
			cq.where(p);
			TypedQuery<LigneBudgetaireAE> query = em.createQuery(cq);
			List <LigneBudgetaireAE> list = query.getResultList();
			if((list!=null)&&(!list.isEmpty())) result=list.get(0);
		return result;
	}

	private int getBatchSize() {
		return Constant.batch_size;
	}
	
}
