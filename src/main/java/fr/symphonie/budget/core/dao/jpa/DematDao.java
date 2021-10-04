package fr.symphonie.budget.core.dao.jpa;

import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import fr.symphonie.budget.core.dao.IJdbcHelper;
import fr.symphonie.budget.core.model.cf.CfGenericDemat;
import fr.symphonie.budget.core.model.cf.CfParam;
import fr.symphonie.budget.core.model.cf.CfReference;
import fr.symphonie.budget.core.model.cf.DematBilan;
import fr.symphonie.budget.core.model.cf.DematCR;
import fr.symphonie.budget.core.model.demat.Export;
import fr.symphonie.budget.core.model.demat.ExportInfo;
import fr.symphonie.budget.core.model.demat.GenericDemat;
import fr.symphonie.common.util.Constant;

@Repository
public class DematDao {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = LoggerFactory.getLogger(DematDao.class);
	private EntityManager em;
	private IJdbcHelper jdbcHelper;
	
	@PersistenceContext
	public void setEntityManager(EntityManager entityManager) {
		this.em = entityManager;
	}
	
	
	   @Inject
		public void setJdbcHelper(IJdbcHelper jdbcHelper) {
			this.jdbcHelper = jdbcHelper;
		}


	public IJdbcHelper getJdbcHelper() {
		return jdbcHelper;
	}

	public List<String> getBudgetList() {
		String[] list=new String[]{"A"};
		return Arrays.asList(list);
	}


	public List<Export> getExportList(Integer exercice, String codeBudget) {
	logger.debug("getExportList :start: exercice={}, codeBudget={}",exercice,codeBudget);	
		TypedQuery<Export> query=em.createQuery("select t from Export t where t.exercice=? and t.codeBudget=?",Export.class);
		query.setParameter(1, exercice);
		query.setParameter(2, codeBudget);
		return query.getResultList();
	}


	public List<ExportInfo> getInfoExportList() {
		TypedQuery<ExportInfo> query=em.createQuery("select t from ExportInfo t ",ExportInfo.class);
		return query.getResultList();
	}


	public List<? extends GenericDemat> getDematList(Class<? extends GenericDemat> type, Export export) {
		logger.debug("getDematList :start: type={}, name={}",type,type.getName());	
		
		TypedQuery<? extends GenericDemat> query=em.createQuery("select t from "+type.getName()+" t where t.exercice=? and t.codeBudget=? and code=?",type);
		query.setParameter(1, export.getExercice());
		query.setParameter(2, export.getCodeBudget());
		query.setParameter(3, export.getCode());
		return query.getResultList();
	}

	public void updateDemat(List<? extends GenericDemat> dematList) {
		logger.info("updateDemat: batchSize="+getBatchSize()+", list size="+dematList.size());
		int i = 0;
		for(GenericDemat demat:dematList){
			em.merge(demat);
            i++;
            if (i % getBatchSize() == 0) {
            	logger.debug("updateDemat: flush iteration="+i);
                em.flush();
                em.clear();
            }
           
        }
        em.flush();
        em.clear();
		
	}
	public void updateList(List<? extends Object> list) {
		logger.info("updateList: batchSize="+getBatchSize()+", list size="+list.size());
		int i = 0;
		for(Object obj:list){
			em.merge(obj);
            i++;
            if (i % getBatchSize() == 0) {
            	logger.debug("updateList: flush iteration="+i);
                em.flush();
                em.clear();
            }
           
        }
        em.flush();
        em.clear();
		
	}

	public List<CfParam> loadParamCF(Integer exercice, String codeBudget, String type) {
		logger.debug("loadParamCF :start: exercice={}, codeBudget={}, type={}",exercice,codeBudget,type);	
		TypedQuery<CfParam> query=em.createQuery("select t from CfParam t where t.exercice=? and t.codeBudget=? and type=?",CfParam.class);
		query.setParameter(1, exercice);
		query.setParameter(2, codeBudget);
		query.setParameter(3, type);
		return query.getResultList();
	}


	public List<CfReference> loadReference(String type,boolean onlyDetail) {
		logger.debug("loadReference :start:  type={}",type);
		String requete="select t from CfReference t where type=? ";
		if(onlyDetail) requete+="and detail=true";
		TypedQuery<CfReference> query=em.createQuery(requete,CfReference.class);
		query.setParameter(1, type);
		List<CfReference> list=query.getResultList();
		logger.debug("loadReference :end:  size={}",list.size());	
		return list;
	}


	public List<? extends CfGenericDemat> getCfDematData(Integer exercice, String codeBudget, String type) {
		logger.debug("getCfDematData :start: exercice={}, codeBudget={}, type={}",exercice,codeBudget,type);	
		TypedQuery<? extends CfGenericDemat> query=null;
			if("B".equals(type))
				query=em.createQuery("select t from DematBilan t where t.exercice=? and t.codeBudget=?",DematBilan.class);
			else if("R".equals(type))
				query=em.createQuery("select t from DematCR t where t.exercice=? and t.codeBudget=?",DematCR.class);
		query.setParameter(1, exercice);
		query.setParameter(2, codeBudget);
		return query.getResultList();
	}


	private int getBatchSize() {
		return Constant.batch_size;
	}
}
