package fr.symphonie.budget.ui.doc;

import static fr.symphonie.budget.core.dao.ps.ColumnName.ar;
import static fr.symphonie.budget.core.dao.ps.ColumnName.bcg;
import static fr.symphonie.budget.core.dao.ps.ColumnName.cne;
import static fr.symphonie.budget.core.dao.ps.ColumnName.co;
import static fr.symphonie.budget.core.dao.ps.ColumnName.compte;
import static fr.symphonie.budget.core.dao.ps.ColumnName.dest1;
import static fr.symphonie.budget.core.dao.ps.ColumnName.dest2;
import static fr.symphonie.budget.core.dao.ps.ColumnName.dp_ex;
import static fr.symphonie.budget.core.dao.ps.ColumnName.dp_ex_ant;
import static fr.symphonie.budget.core.dao.ps.ColumnName.dr;
import static fr.symphonie.budget.core.dao.ps.ColumnName.ej;
import static fr.symphonie.budget.core.dao.ps.ColumnName.ej_new;
import static fr.symphonie.budget.core.dao.ps.ColumnName.lib_dest1;
import static fr.symphonie.budget.core.dao.ps.ColumnName.lib_dest2;
import static fr.symphonie.budget.core.dao.ps.ColumnName.libelle;
import static fr.symphonie.budget.core.dao.ps.ColumnName.nat_grp;
import static fr.symphonie.budget.core.dao.ps.ColumnName.nb;
import static fr.symphonie.budget.core.dao.ps.ColumnName.net;
import static fr.symphonie.budget.core.dao.ps.ColumnName.niveau;
import static fr.symphonie.budget.core.dao.ps.ColumnName.no_lbi;
import static fr.symphonie.budget.core.dao.ps.ColumnName.nom_nat_grp;
import static fr.symphonie.budget.core.dao.ps.ColumnName.op_n_budg;
import static fr.symphonie.budget.core.dao.ps.ColumnName.pct;
import static fr.symphonie.budget.core.dao.ps.ColumnName.rap;
import static fr.symphonie.budget.core.dao.ps.ColumnName.rb_tot;
import static fr.symphonie.budget.core.dao.ps.ColumnName.rec;
import static fr.symphonie.budget.core.dao.ps.ColumnName.section;
import static fr.symphonie.budget.core.dao.ps.ColumnName.sf_ej_ant;
import static fr.symphonie.budget.core.dao.ps.ColumnName.sf_ej_exer;
import static fr.symphonie.budget.core.dao.ps.ColumnName.sf_ex;
import static fr.symphonie.budget.core.dao.ps.ColumnName.sf_ex_ant;
import static fr.symphonie.budget.core.dao.ps.ColumnName.sf_net;
import static fr.symphonie.budget.core.dao.ps.ColumnName.sf_new;
import static fr.symphonie.budget.core.dao.ps.ColumnName.sf_old;
import static fr.symphonie.budget.core.dao.ps.ColumnName.sf_tot;
import static fr.symphonie.budget.core.dao.ps.ColumnName.tot;

import java.io.File;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.symphonie.budget.core.dao.ps.ColumnName;
import fr.symphonie.common.core.ICommonService;
import fr.symphonie.common.util.BudgetHelper;
import fr.symphonie.doc.MSGenerator;
import fr.symphonie.doc.TypeDocEnum;
import fr.symphonie.exception.MissingConfiguration;
import fr.symphonie.util.Helper;
import fr.symphonie.util.model.Document;
import fr.symphonie.util.model.i.Editionable;
import fr.symphonie.util.model.i.IDocumentMetaData;


public class TBDocxHandler extends BudgetDocxHandler{
	
	private static final Logger logger = LoggerFactory.getLogger(TBDocxHandler.class);
	public static final String FORMAT_MNT_PATTERN = "###,##0.00";
	private static final String FONCTIONNEMENT = "F";
	private static final String REGROUPEMENT = "R";
	private static SimpleDateFormat formatFR = new SimpleDateFormat("dd MMMM yyyy",Locale.FRENCH);
	private String nomEtabl=null;
	@Override
	protected Object getXdocContext(IDocumentMetaData docMetadata,
			Editionable editionable) {
		Map<String, Object> context=null;
		ModeleEnum model=(ModeleEnum) docMetadata;
		TbDocxStruct inputData=(TbDocxStruct) editionable;
		switch(model){
		case ENVELOPPE:
			context=getRepartParEnvelop(inputData);
			break;
		case SUIVI_CP:
			context=getSuiviCP(inputData);
			break;
		case COMPTE_RENDU_GEST:
			context=getCompteRenduGest(inputData);
			break;
		case SUIVI_COMPTE_DEP:
			context=getCompteDepense(inputData);
			break;
		case SUIVI_COMPTE_REC:
			context=getSuiviCompteRecette(inputData);
			break;
		default:
			break;
		}
		generalData(context,inputData);
		return context;
	}
	
	public Map<String, Object> getSuiviCompteRecette(TbDocxStruct inputData) {
		double mntRec=0, mntAr=0, mntRbTot=0;
		double mntOpNonbudg=0,mntBcg=0, totalRec=0, totalAr=0,totalRbtot=0;
		double totalOpNonbudg=0,totalBcg=0;
		Map<String, Object> context=new CustomHashMap<String, Object>();
		Map<String, Object> item=null;
		int exercice=inputData.getExercice();
		List<Map<String, Object>> dataList=getCommonService().getSuiviCompteRecette(exercice);
		List<Map<String, Object>> subContext=new ArrayList<Map<String,Object>>();
		boolean isRegroupement=false;
		String niveauValue=null;
		if (dataList != null) {
			for (Map<String, Object> data : dataList) {
				item = new CustomHashMap<String, Object>();
				niveauValue=(String) data.get(niveau);
				isRegroupement=niveauValue.equalsIgnoreCase(REGROUPEMENT);
				mntRec = (Double) data.get(rec);
				mntAr = (Double) data.get(ar);
				mntRbTot = (Double) data.get(rb_tot);
				mntOpNonbudg=(Double) data.get(op_n_budg);
//				mntBcg=(Double) data.get(bcg);
				mntBcg=mntRbTot+mntOpNonbudg;
				
				item.put("regroupement", isRegroupement);
				item.put(compte, data.get(compte));
				item.put(libelle, data.get(libelle));
				item.put(rec, (mntRec));
				item.put(ar, (mntAr));
				item.put(rb_tot,(mntRbTot));
				item.put(op_n_budg,(mntOpNonbudg));
				item.put(bcg,(mntBcg));
				if(niveauValue.equalsIgnoreCase("R"))
				{
				totalRec += mntRec;
				totalAr += mntAr;
				totalRbtot += mntRbTot;
				totalOpNonbudg += mntOpNonbudg;
				totalBcg +=mntBcg;
				}
				subContext.add(item);
			}
		}
		context.put("data", subContext);
		context.put("totalRec",(totalRec));
		context.put("totalAr",(totalAr));
		context.put("totalRbtot",(totalRbtot ));
		context.put("totalOpNonbudg",(totalOpNonbudg) );
		context.put("totalBcg",(totalBcg) );
		return context;
	}

	public Map<String, Object> getCompteDepense(TbDocxStruct inputData) {
		double montantSfEj=0; double montantSfEjAnt=0;double montantSfTot=0;
		double montantOnb=0; double montantBcg=0; double totalSfej=0; double totalSfejAnt=0; double totalSftot=0;
		double totalRvtot=0; double totalNet=0;
		double totalOnb=0,totalBcg=0;
		double mntReversement=0,mntNet=0;
		Map<String, Object> context=new CustomHashMap<String, Object>();
		Map<String, Object> item=null;
		int exercice=inputData.getExercice();
		List<Map<String, Object>> dataList=getCommonService().getCpDepenseList(exercice);
		List<Map<String, Object>> exploitation=new ArrayList<Map<String,Object>>();
		boolean isRegroupement=false;
		String niveauValue=null;
		if (dataList != null) {
			for (Map<String, Object> data : dataList) {
				item = new CustomHashMap<String, Object>();
				niveauValue=(String) data.get(niveau);
				isRegroupement=niveauValue.equalsIgnoreCase(REGROUPEMENT);
				montantSfEj = (Double) data.get(sf_ej_exer);
				montantSfEjAnt = (Double) data.get(sf_ej_ant);
				montantSfTot = (Double) data.get(sf_tot);
				montantOnb=(Double) data.get(op_n_budg);
//				montantBcg=(Double) data.get(bcg);
				mntReversement=(Double) data.get(dr);
				mntNet=(Double) data.get(sf_net);
				montantBcg=mntNet+montantOnb;
				item.put("regroupement", isRegroupement);
				item.put(compte, data.get(compte));
				item.put(libelle, data.get(libelle));
				item.put(sf_ej_exer, (montantSfEj));
				item.put(sf_ej_ant, (montantSfEjAnt));
				item.put(sf_tot,(montantSfTot));
				item.put(op_n_budg,(montantOnb));
				item.put(bcg,(montantBcg));
				item.put(dr, (mntReversement));
				item.put(sf_net, (mntNet));
				if(niveauValue.equalsIgnoreCase("R"))
				{
				totalSfej += montantSfEj;
				totalSfejAnt += montantSfEjAnt;
				totalSftot += montantSfTot;
				totalRvtot += mntReversement;
				totalNet += mntNet;
				totalOnb += montantOnb;
				totalBcg +=montantBcg;
				}
				exploitation.add(item);
			}
		}
		context.put("data", exploitation);
		context.put("totalSfej",(totalSfej ));
		context.put("totalSfejAnt",(totalSfejAnt ));
		context.put("totalSftot",(totalSftot ));
		context.put("totalRvtot",(totalRvtot ));
		context.put("totalNet",(totalNet ));
		context.put("totalOnb",(totalOnb) );
		context.put("totalBcg",(totalBcg) );
		return context;
	}

//	public Double[] getBcgAndNet(NatGrpEnum natGrp, int exercice) {
//		String codeNatGrp = null;
//		Double result[] = new Double[2];
//		Double mntNb = null;
//		Double mntNet = null;
//		Double mntBcg = null;
//
//		List<Map<String, Object>> dataList = getCommonService().getSuiviEnvelopList(exercice);
//		if (dataList != null)
//			for (Map<String, Object> data : dataList) {
//				codeNatGrp = (String) data.get(nat_grp);
//				if(codeNatGrp==null)continue;
//				if (natGrp != NatGrpEnum.parse(codeNatGrp.trim()))
//					continue;
//				mntNet = (Double) data.get(net);
//				mntNb = (Double) data.get(nb);
//				
//				mntBcg= mntNb + mntNet;
//				
//				result[0] = mntBcg;
//				result[1] = mntNet;
//			}
//
//		return result;
//
//	}
	public Map<String, Object> getRepartParEnvelop( TbDocxStruct inputData) {
		Map<String, Object> context=new CustomHashMap<String, Object>();
		double mntTot=0, mntCne=0, mntEj=0,mntCo=0,mntSfEx=0,mntSfExAnt=0,mntNonBudg=0,montantBcg=0;
		double total=0,totalSfex=0,  totalCne=0,  totalEj=0,totalSfexAnt=0,totalSftot=0,totalRvtot=0,totalNet=0,totalCo=0,totalOnb=0,totalBcg=0; 
		double totalInvCo=0, totalInvSfex=0,totalInvSfexAnt=0,totalInvSftot=0,totalInvRvtot=0,totalInvNet=0,totalInv=0,totalInvOnb=0,totalInvBcg=0; double totalInvCne=0; double totalInvEj=0;
		double mntReversement=0,mntNet=0;
		List<Map<String, Object>> exploitation=new ArrayList<Map<String,Object>>();
		List<Map<String, Object>> investissement=new ArrayList<Map<String,Object>>();
		Map<String, Object> item=null;
		
		boolean isFonctionnement=false;
		String codeNatGrp =null,sectionValue=null;;
		int exercice=inputData.getExercice();
		List<Map<String, Object>> dataList=getCommonService().getSuiviEnvelopList(exercice);
		if (dataList != null) {
			for (Map<String, Object> data : dataList) {
				sectionValue=(String) data.get(section);
				isFonctionnement=(sectionValue==null)?false:sectionValue.equalsIgnoreCase(FONCTIONNEMENT);
				codeNatGrp=(String) data.get(nat_grp);
				
				mntCo=(Double) data.get(co);
				mntEj = (Double) data.get(ej);
				mntCne = (Double) data.get(cne);
				mntSfEx=(Double) data.get(sf_ex);
				mntSfExAnt=(Double) data.get(sf_ex_ant);
				mntReversement=(Double) data.get(dr);
				mntNet=(Double) data.get(net);
				mntTot =mntSfEx+mntSfExAnt ;
				mntNonBudg = (Double) data.get(nb);
				montantBcg=mntNet+mntNonBudg;
//				totalCo +=mntCo;
				
				item = new CustomHashMap<String, Object>();
				item.put(nat_grp, codeNatGrp);
				item.put(nom_nat_grp, data.get(nom_nat_grp));
				item.put(co, mntCo);
				item.put(ej, mntEj);
				item.put(cne, (mntCne));
				item.put(sf_ex,(mntSfEx));
				item.put(sf_ex_ant,(mntSfExAnt));
				item.put(tot, (mntTot));
				item.put(nb, (mntNonBudg));
				item.put("bcg", (montantBcg));
				item.put(dr, (mntReversement));
				item.put(net, (mntNet));
				
				if(isFonctionnement){
					totalCo +=mntCo;
					totalEj +=mntEj;
					totalCne +=mntCne;
					totalSfex += mntSfEx;
					totalSfexAnt += mntSfExAnt;
					totalSftot += mntTot;
					totalRvtot += mntReversement;
					totalNet += mntNet;
					totalOnb += mntNonBudg;
					totalBcg +=montantBcg;
					exploitation.add(item);
				}
				else {
					totalInvCo +=mntCo;
					totalInvEj +=mntEj;
					totalInvCne +=mntCne;
					totalInvSfex += mntSfEx;
					totalInvSfexAnt += mntSfExAnt;
					totalInvSftot += mntTot;
					totalInvRvtot  += mntReversement;
					totalInvNet  += mntNet;
					totalInvOnb += mntNonBudg;
					totalInvBcg +=montantBcg;
					investissement.add(item);
				}
			}

		}
			
		context.put("exploit", exploitation);
		context.put("invest", investissement);
		context.put("totalCo",(totalCo ));
		context.put("totalEj",(totalEj ));
		context.put("totalCne",(totalCne ));
		context.put("totalSfex",(totalSfex ));
		context.put("totalSfexAnt",(totalSfexAnt ));
		context.put("totalSftot",(totalSftot ));
		context.put("totalRvtot",(totalRvtot ));
		context.put("totalNet",(totalNet ));
		context.put("totalOnb",(totalOnb) );
		context.put("totalBcg",(totalBcg) );
		
		context.put("totalInvCo",(totalInvCo ));
		context.put("totalInvEj",(totalInvEj ));
		context.put("totalInvCne",(totalInvCne ));
		context.put("totalInvSfex",(totalInvSfex ));
		context.put("totalInvSfexAnt",(totalInvSfexAnt ));
		context.put("totalInvSftot",(totalInvSftot ));
		context.put("totalInvRvtot",(totalInvRvtot ));
		context.put("totalInvNet",(totalInvNet ));
		context.put("totalInvOnb",(totalInvOnb) );
		context.put("totalInvBcg",(totalInvBcg) );
		context.put("tot_credi_nonEmploy",(totalCne));
		context.put("credit_annule",(totalEj));
		context.put("total",(total));
		context.put("totali",(totalInv));
		
		context.put("toti_credi_nonEmploy",(totalInvCne));
		context.put("toti_cred_annul",(totalInvEj));
		
		
		context.put("globalTot",(totalInvSftot+totalSftot));
//		context.put("globalCne",(totalCne+totalInvCne));
		context.put("globalCo",(totalCo+totalInvCo));
		context.put("globalEj",(totalEj+totalInvEj));
		context.put("globalCne",(totalCne+totalInvCne));
		context.put("globalSejEx",(totalSfex+totalInvSfex));
		context.put("globalSejAnt",(totalSfexAnt+totalInvSfexAnt));
		context.put("globalRevers",(totalRvtot+totalInvRvtot));
		context.put("globalNet",(totalNet+totalInvNet));
		context.put("globalOnb",(totalOnb+totalInvOnb));
		context.put("globalBcg",(totalBcg+totalInvBcg));
		return context;
	}
	/*
private boolean isCodeInvestissement(String codeNatGrp) {
	String[]CODE_INVEST={"3"};
	List<String> list=Arrays.asList(CODE_INVEST);
		return list.contains(codeNatGrp);
	}
*/

	
	public Map<String, Object> getSuiviCP( TbDocxStruct inputData) {
		Map<String, Object> context=new CustomHashMap<String, Object>();
		double montantTot=0, montantCne=0, montantRestap=0,montantCo=0,montantSAeEx=0, montantSAeAnt=0;
		double total=0, totalRv=0, totalNet=0,  totalCne=0,  totalRestap=0,totalCo=0,totalSAeEx=0,totalSAeAnt=0,totalInvSAeEx=0,totalInvSAeAnt=0;
		double totalInv=0,totalInvRv=0,totalInvNet=0,totalInvCo=0; double totalInvCne=0; double totalInvRestap=0;
		double mntReversement=0,mntNet=0;
		List<Map<String, Object>> exploitation=new ArrayList<Map<String,Object>>();
		List<Map<String, Object>> investissement=new ArrayList<Map<String,Object>>();
		Map<String, Object> item=null;
		String CODE_INVEST="3";
		int exercice=inputData.getExercice();
		List<Map<String, Object>> data=getCommonService().getSuiviCpList(exercice);
		if (data != null) {
			for (Map<String, Object> expl : data) {
				String codeGrp = ColumnName.nat_grp;
				montantTot = (Double) expl.get(tot);
				montantCne = (Double) expl.get(cne);
				montantRestap = (Double) expl.get(rap);
				montantCo=(Double) expl.get(co);
				montantSAeEx=(Double)expl.get(dp_ex);
				montantSAeAnt=(Double)expl.get(dp_ex_ant);
				mntReversement=(Double) expl.get(dr);
				mntNet=(Double) expl.get(net);
//				totalCo +=montantCo;
				
				item = new CustomHashMap<String, Object>();
				item.put(nat_grp, expl.get(codeGrp));
				item.put(nom_nat_grp, expl.get(nom_nat_grp));
				item.put(co, (montantCo));
				item.put(dp_ex, ((Double) expl.get(dp_ex)));
				item.put(dp_ex_ant,
						((Double) expl.get(dp_ex_ant)));
				item.put(tot, (montantTot));
				item.put(cne, (montantCne));
				item.put(rap, (montantRestap));
				item.put(dr, (mntReversement));
				item.put(net, (mntNet));
				if ((expl.get(codeGrp).equals(CODE_INVEST))) {
					totalInv += montantTot;
					totalInvCo += montantCo;
					totalInvSAeEx+=montantSAeEx;
					totalInvSAeAnt+=montantSAeAnt;
					totalInvRv += mntReversement;
					totalInvNet += mntNet;
					totalInvCne += montantCne;
					totalInvRestap += montantRestap;
					investissement.add(item);
				} else {
					total += montantTot;
					totalCo +=montantCo;
					totalSAeEx+=montantSAeEx;
					totalSAeAnt+=montantSAeAnt;
					totalRv += mntReversement;
					totalNet += mntNet;
					totalCne += montantCne;
					totalRestap += montantRestap;
					exploitation.add(item);
				}
			}

		}
			
		context.put("exploit", exploitation);
		context.put("invest", investissement);
		context.put("tot_credi_nonEmploy",(totalCne));
		context.put("restap",(totalRestap));
		context.put("total",(total));
		context.put("totalCo",(totalCo));
		context.put("totalSAeEx",(totalSAeEx));
		context.put("totalSAeAnt",(totalSAeAnt));
		context.put("totalRv",(totalRv));
		context.put("totalNet",(totalNet));
		
		context.put("totali",(totalInv));
		context.put("totalInvCo",(totalInvCo));
		context.put("totalInvSAeEx",(totalInvSAeEx));
		context.put("totalInvSAeAnt",(totalInvSAeAnt));
		context.put("totalInvRv",(totalInvRv));
		context.put("totalInvNet",(totalInvNet));
		context.put("toti_credi_nonEmploy",(totalInvCne));
		context.put("toti_restap",(totalInvRestap));
		
		
		context.put("globalTot",(totalInv+total));
		context.put("globalDpEx",(totalInvSAeEx+totalSAeEx));
		context.put("globalDpExAnt",(totalInvSAeAnt+totalSAeAnt));
		context.put("globalRevers",(totalInvRv+totalRv));
		context.put("globalNet",(totalNet+totalInvNet));
		context.put("globalCne",(totalCne+totalInvCne));
		context.put("globalRestap",(totalRestap+totalInvRestap));
		context.put("globalCo",(totalCo+totalInvCo));
		return context;
			}

	@SuppressWarnings("unchecked")
	public Map<String, Object> getCompteRenduGest( TbDocxStruct inputData) {
		Map<String, Object> context=new CustomHashMap<String, Object>();
		List<Map<String, Object>> niveau1=new ArrayList<Map<String,Object>>();
		List<Map<String, Object>> niveau2=null;
		Map<String, Object> itemNiveau1=null;
		Map<String, Object> itemNiveau2=null;
		int exercice=inputData.getExercice();
		Date dateFin=inputData.getDate();
		String dest1Value=null;
		List<Map<String, Object>> data=getCommonService().getCompteRenduGest(exercice,dateFin);
		if (data != null) {
			for (Map<String, Object> element : data) {
				
				dest1Value=(String) element.get(dest1);
				itemNiveau1=searchItem(dest1Value,niveau1,dest1);
				if(itemNiveau1==null){
					itemNiveau1=new CustomHashMap<String, Object>();
					itemNiveau1.put(dest1, dest1Value);
					itemNiveau1.put(lib_dest1, element.get(lib_dest1));
					itemNiveau1.put("niveau2", new ArrayList<Map<String,Object>>());
					niveau1.add(itemNiveau1);
				}
				niveau2= (List<Map<String, Object>>) itemNiveau1.get("niveau2");
				itemNiveau2=new CustomHashMap<String, Object>();
				itemNiveau2.put(dest2,  element.get(dest2));
				itemNiveau2.put(lib_dest2, element.get(lib_dest2));
				itemNiveau2.put(nat_grp, element.get(nat_grp));				
				itemNiveau2.put(no_lbi, element.get(no_lbi));
				itemNiveau2.put(co, ((Double)element.get(co)));
				itemNiveau2.put(ej_new, ((Double)element.get(ej_new)));
				itemNiveau2.put(sf_new, ((Double)element.get(sf_new)));
				itemNiveau2.put(pct, ((Double)element.get(pct)));
				itemNiveau2.put(sf_old, ((Double)element.get(sf_old)));
				
				niveau2.add(itemNiveau2);
				
				
				
			}
			context.put("niveau1", niveau1);
			context.put("exerciceAE", exercice);
			logger.info("dateFin: "+dateFin.toString());
			context.put("dateArret", formatDate(dateFin));
			context.put("exercicAnt", exercice-1);
			}
		
		return context;
			}
	private Map<String, Object> searchItem(String requestValue,
			List<Map<String, Object>> inputMap,String keyColumn) {
		Map<String, Object> result=null;
		String key=null;
		try{
		for(Map<String, Object> item:inputMap){
			key=(String) item.get(keyColumn);
			if(requestValue.equalsIgnoreCase(key)){
				result=item;
				break;
			}
		}
		}catch(Exception e){result=null;}
		return result;
	}

	@Override
	public boolean isDisplayableDoc(Document doc, Editionable businessObj) {
		return false;
	}
	
	public  File generateFile(ModeleEnum docMetadata,TbDocxStruct editionable ){
		File pdfFile=null;
		String generatedDocxPath=generate(docMetadata, editionable, null);
//		logger.info("generatedDocxPath: "+generatedDocxPath);
		File xdocFile=new File(generatedDocxPath);
//		logger.info("xdocFile: "+xdocFile);
//		logger.info("FilenameUtils.removeExtension(xdocFile.getName()): "+FilenameUtils.removeExtension(xdocFile.getName()));
		String pfdFilename=FilenameUtils.removeExtension(xdocFile.getName())+TypeDocEnum.PDF.getExtension();
//		logger.info("pfdFilename1: "+pfdFilename);
//		logger.info("xdocFile.getAbsolutePath(): "+xdocFile.getParentFile());
		
		pfdFilename=xdocFile.getParentFile()+File.separator+pfdFilename;
//		logger.info("pfdFilename: "+pfdFilename);
		pdfFile=MSGenerator.convertXdocToPDF(xdocFile, pfdFilename);
//		logger.info("pdfFile2: "+pdfFile);
		return pdfFile;
	}
	private ICommonService getCommonService(){
		return BudgetHelper.getCommonService();
	}
	public static String formatMontant(Double value) {
		Locale langue = Helper.getCurrentLocal();
		NumberFormat nf = NumberFormat.getNumberInstance(langue);
		DecimalFormat myFormatter = (DecimalFormat) nf;
		myFormatter.applyPattern(FORMAT_MNT_PATTERN);
		return myFormatter.format(value);
	}
	public void generalData(Map<String, Object> context, TbDocxStruct inputData) {
		int exercice=inputData.getExercice();
		if(context==null)context=new CustomHashMap<String, Object>();
		context.put("numExerc",""+ exercice);
		context.put("nomEtabliss", getNomEtabl());
		
	}

	public void setNomEtabl(String nomEtabl) {
		this.nomEtabl = nomEtabl;
	}

	public String getNomEtabl() {
		if(nomEtabl==null){
			try {
				nomEtabl=getCommonService().getConfigParam("tEtablisInitiales",null);
			} catch (MissingConfiguration e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return nomEtabl;
	}
	public String formatDate(Date date) {	
		if(date==null) return "";	
		 return  formatFR.format(date);
		
		
	}
	
}
