package fr.symphonie.tools.gts.core;

import java.util.List;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fr.symphonie.budget.core.dao.IJdbcHelper;
import fr.symphonie.tools.gts.core.ps.PsLrecCree;
import fr.symphonie.tools.gts.core.ps.PsRecFactLigneCree;
import fr.symphonie.tools.gts.core.ps.PsRecFactLigneSupprime;
import fr.symphonie.tools.gts.core.ps.PsRecFactMajAdr;
import fr.symphonie.tools.gts.model.Article;
import fr.symphonie.tools.gts.model.ArticleDetails;
import fr.symphonie.tools.gts.model.ClientGts;
import fr.symphonie.tools.gts.model.ImportedData;
import fr.symphonie.tools.gts.model.LigneRecette;
import fr.symphonie.tools.gts.model.LiqRecette;
import fr.symphonie.tools.gts.model.PeriodeEnum;
import fr.symphonie.tools.gts.model.PeriodeGts;

@Service
public class GtsService {
	private static final Logger logger = LoggerFactory.getLogger(GtsService.class);
	private GtsDao dao;
	private IJdbcHelper jdbcHelper;
	
	   @Inject
		public void setJdbcHelper(IJdbcHelper jdbcHelper) {
			this.jdbcHelper = jdbcHelper;
		}

	@Inject
	public void setDao(GtsDao dao) {
		this.dao = dao;
	}

	@Transactional
	public <T extends Object> void saveList(List<T> list) {
		dao.saveList(list);

	}

	@Transactional
	public <T extends Object> T save(T o) {
		return dao.save(o);

	}

	public List<Article> getArticles(String searchCondition) {
		return dao.getArticles(searchCondition);
	}
	
	
	public Article getArticle(String code) {
		return dao.getArticle(code);
	}

	public List<PeriodeGts> getPeriodes(Integer exercice, Integer numPeriode, Boolean ferme) {
		return dao.getPeriodes(exercice,numPeriode,ferme);
	}

	public List<Integer> getListNumPeriode(Integer exercice) {
		return dao.getListNumPeriode(exercice);
	}

	public List<ClientGts> getClientGtsList(String searchCondition) {
		return dao.getClientGtsList(searchCondition);
	}

	
	public ClientGts getClientGts(String codeClient) {
		return dao.getClientGts(codeClient) ;
	}

	@Transactional
	public void remove(Object entity) {
		dao.remove(entity);
	}
	
	@Transactional
	public Article removeArticlDetail(ArticleDetails detail) {
		return dao.removeArticlDetail(detail);
	}
	
	@Transactional
	public void saveImport(Integer numExec, List<ImportedData> list, PeriodeGts periode) {
//		jdbcHelper.deleteGtsImport(numExec, periode.getNumero());
		dao.deleteGtsImport(numExec, periode.getNumero());
		periode.setEtat(PeriodeEnum.CHARGE);
		dao.saveList(list);
		dao.save(periode);
	}
	
	public List<ImportedData> getImportedData(Integer exercice, Integer numPeriode, String codeArticle, String codeClientGts)
	{
		return dao.getImportedData(exercice, numPeriode,codeArticle,codeClientGts);
	}

	public String[] getCtaCompteImput(Integer exercice, String imputHtd) {
		return jdbcHelper.getCtaCompteImput(exercice, imputHtd);
	}

	public void genererLiqRecette(List<LiqRecette> liqRecetteList, String createur) {
		logger.debug("genererLiqRecette: Début");
		Integer no_lrec=null;
		for(LiqRecette liqRec:liqRecetteList){
			logger.debug("genererLiqRecette: isDiffere= {}:",liqRec.isDiffere());
			if(liqRec.isDiffere())continue;
			no_lrec=creerLiqRecette(liqRec,createur);
			liqRec.setNumero(no_lrec);
			//boolean totalise=false;
			int nbDetails=liqRec.getDetails().size();
			int compteur=1;
			for(LigneRecette ligne:liqRec.getDetails()){
				creerLigneRecette(ligne,compteur==nbDetails,createur);
				compteur++;
			}
		}
		logger.debug("genererLiqRecette: Fin");
		
	}

	private void creerLigneRecette(LigneRecette ligne,boolean totalise, String createur) {
		logger.debug("creerLigneRecette: Début: no_lrec={}, Numéro={},totalise={}",ligne.getNoLiqRec(),ligne.getNumero(),totalise);
		PsRecFactLigneCree.getInstance(jdbcHelper.getJdbcTemplate()).execute(ligne,totalise,createur);
		logger.debug("creerLigneRecette: Fin");
		
	}

	private Integer creerLiqRecette(LiqRecette liqRec, String createur) {
		logger.debug("creerLiqRecette: Début: noLbi={}, tiers={}, refCde={} ",liqRec.getNoLbi(),liqRec.getTiersOrigine(),liqRec.getRefCommande());
		Integer no_lrec=null;
		Integer exercice=liqRec.getExercice();
		Integer noAdresse=liqRec.getNoAdresse();
		JdbcTemplate jdbcTemp=jdbcHelper.getJdbcTemplate();
		no_lrec=PsLrecCree.getInstance(jdbcTemp).execute(liqRec,createur);		
		PsRecFactLigneSupprime.getInstance(jdbcTemp).execute(exercice, no_lrec,createur);
		
		PsRecFactMajAdr.getInstance(jdbcTemp).execute(exercice, no_lrec, noAdresse,createur);
		logger.debug("creerLiqRecette: Fin : no_lrec={}",no_lrec);
		return no_lrec;
	}
@Transactional
	public void saveGeneration(List<LiqRecette> list, PeriodeGts periode) {
		dao.saveList(list);
		if(periode!=null)
		{
			periode.setEtat(PeriodeEnum.TRAITE);
			dao.save(periode);
		}
		
		
	}

	public List<LiqRecette> getLiqRecettes(Integer exercice, Integer numPeriode) {

		return dao.getLiqRecettes(exercice, numPeriode,null);
	}

	public List<LiqRecette> getLiqRecetteDiffere(Integer exercice) {
		return dao.getLiqRecettes(exercice, null,true);
	}

	public List<ArticleDetails> getArticleDetails(Integer exercice) {
		return dao.getArticleDetails(exercice);
	}

	public List<Integer> getGtsExercices() {
		return dao.getGtsExercices();
	}

}
