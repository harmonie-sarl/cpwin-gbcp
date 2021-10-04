package fr.symphonie.budget.ui.excel;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.LoggerFactory;

import fr.symphonie.budget.core.model.edition.Bilan;
import fr.symphonie.budget.core.model.edition.CR;
import fr.symphonie.budget.core.model.edition.util.CRItem;
import fr.symphonie.budget.ui.beans.edition.CompteFinancierBean;
import fr.symphonie.budget.ui.doc.CustomHashMap;
import fr.symphonie.budget.ui.excel.edition.BiBrExcelData;
import fr.symphonie.common.util.BudgetHelper;

public class CFList extends BiBrExcelData{
	
	private static final org.slf4j.Logger logger = LoggerFactory.getLogger(CFList.class);

	
	
	public CFList() {
		super();
		
	}
	
  @Override
public Map<String, Object> getSpecificValues() {
		Map<String, Object> map = super.getSpecificValues();
		
    
	 createBilan(map);
	 createChargeFonc(map);
	 createChargeFinanc(map);
	 createChargeInterv(map);
	 createProduitFinanc(map);
	 createProduitFonct(map);
	 createOtherTot(map);

	 return map;
}

	private void createOtherTot(Map<String, Object> map) {
		CompteFinancierBean cfBean=getCfBean();
		CR cr=cfBean.getCf().getCr();
		if(cfBean.getCf()!=null){
		map.put("tot.chargeN",cr.getCharge().getTotFonct().getN().getMontant());
		map.put("tot.chargeAnt",cr.getCharge().getTotFonct().getAnt().getMontant());
		map.put("chargeInterN",cr.getCharge().getTotInter().getN().getMontant());
		map.put("tot.chargeInterAnt",cr.getCharge().getTotInter().getAnt().getMontant());
		map.put("totFoncInter",cr.getCharge().getTotFonctInter().getN().getMontant());
		map.put("totFoncInterAnt",cr.getCharge().getTotFonctInter().getAnt().getMontant());
		
		map.put("totChargFinanc",cr.getCharge().getTotFinanc().getN().getMontant());
		map.put("totChargFinancAnt",cr.getCharge().getTotFinanc().getAnt().getMontant());
		
		map.put("impotSociete",cr.getCharge().getImpot().getN().getMontant());
		map.put("impotSocieteAnt",cr.getCharge().getImpot().getAnt().getMontant());
		
		map.put("resultBenific",cr.getCharge().getBenefice().getN().getMontant());
		map.put("resultBenificAnt",cr.getCharge().getBenefice().getAnt().getMontant());
		
		map.put("totChargeN",cr.getCharge().getTotale().getN().getMontant());
		map.put("totChargeAnt",cr.getCharge().getTotale().getAnt().getMontant());
		
		map.put("totProdN",cr.getProduit().getTotale().getN().getMontant());
		map.put("totProdAnt",cr.getProduit().getTotale().getAnt().getMontant());
		
		map.put("resultPerte",cr.getProduit().getPerte().getN().getMontant());
		map.put("resultPerteAnt",cr.getProduit().getPerte().getAnt().getMontant());
		
		map.put("totaleProdN",cr.getProduit().getTotale().getN().getMontant());
		map.put("totaleProdAnt",cr.getProduit().getTotale().getAnt().getMontant());
		}
		
	
}

	private void createBilan(Map<String, Object> map) {
		CompteFinancierBean cfBean=getCfBean();
		logger.debug("createBilan: cfBean.getCf()="+cfBean.getCf());
		Bilan b=cfBean.getCf().getBilan();
//		if(cfBean.getCf()!=null){
		map.put("exerciceAE", cfBean.getExercice());
		map.put("ai.incorp.brut",b.getAi().getImmIncorp().getBrutN().getMontant());
		map.put("ai.incorp.amort",b.getAi().getImmIncorp().getAmortDeprN().getMontant());
		map.put("ai.incorp.ant",b.getAi().getImmIncorp().getNetAnt().getMontant());
		map.put("ai.corp.terr.brut",b.getAi().getTerrains().getBrutN().getMontant());
		map.put("ai.corp.terr.amort",b.getAi().getTerrains().getAmortDeprN().getMontant());
		map.put("ai.corp.terr.ant",b.getAi().getTerrains().getNetAnt().getMontant());
		map.put("ai.corp.construt.brut",b.getAi().getConstrcut().getBrutN().getMontant());
		map.put("ai.corp.constr.amort",b.getAi().getConstrcut().getAmortDeprN().getMontant());
		map.put("ai.corp.constr.ant",b.getAi().getConstrcut().getNetAnt().getMontant());
		map.put("ai.corp.inst.brut",b.getAi().getInstallTech().getBrutN().getMontant());
		map.put("ai.corp.inst.amort",b.getAi().getInstallTech().getAmortDeprN().getMontant());
		map.put("ai.corp.inst.ant",b.getAi().getInstallTech().getNetN().getMontant());
		map.put("ai.corp.collect.brut",b.getAi().getCollection().getBrutN().getMontant());
		map.put("ai.corp.collect.amort",b.getAi().getCollection().getAmortDeprN().getMontant());
		map.put("ai.corp.collect.ant",b.getAi().getCollection().getNetN().getMontant());
		map.put("ai.corp.collect.brut",b.getAi().getBiensHist().getBrutN().getMontant());
		map.put("ai.corp.collect.amort",b.getAi().getBiensHist().getAmortDeprN().getMontant());
		map.put("ai.corp.collect.ant",b.getAi().getBiensHist().getNetN().getMontant());
		map.put("ai.corp.autre.brut",b.getAi().getAutreImmobCorp().getBrutN().getMontant());
		map.put("ai.corp.autre.amort",b.getAi().getAutreImmobCorp().getAmortDeprN().getMontant());
		map.put("ai.corp.autre.ant",b.getAi().getAutreImmobCorp().getNetN().getMontant());
		map.put("ai.corp.conces.brut",b.getAi().getImmobMisConc().getBrutN().getMontant());
		map.put("ai.corp.concess.amort",b.getAi().getImmobMisConc().getAmortDeprN().getMontant());
		map.put("ai.corp.concess.ant",b.getAi().getImmobMisConc().getNetN().getMontant());
		
		map.put("ai.corp.encour.brut",b.getAi().getImmobCorpEnC().getBrutN().getMontant());
		map.put("ai.corp.encour.amort",b.getAi().getImmobCorpEnC().getAmortDeprN().getMontant());
		map.put("ai.corp.encour.ant",b.getAi().getImmobCorpEnC().getNetN().getMontant());
		
		map.put("ai.corp.avanc.brut",b.getAi().getAvanceAcmptCmd().getBrutN().getMontant());
		map.put("ai.corp.avanc.amort",b.getAi().getAvanceAcmptCmd().getAmortDeprN().getMontant());
		map.put("ai.corp.avanc.ant",b.getAi().getAvanceAcmptCmd().getNetN().getMontant());
		
		map.put("ai.corp.grev.brut",b.getAi().getImmobGreve().getBrutN().getMontant());
		map.put("ai.corp.grev.amort",b.getAi().getImmobGreve().getAmortDeprN().getMontant());
		map.put("ai.corp.grev.ant",b.getAi().getImmobGreve().getNetN().getMontant());
		
		map.put("ai.corp.vivan.brut",b.getAi().getImmobCorp().getBrutN().getMontant());
		map.put("ai.corp.vivan.amort",b.getAi().getImmobCorp().getAmortDeprN().getMontant());
		map.put("ai.corp.vivan.ant",b.getAi().getImmobCorp().getNetN().getMontant());
		
		map.put("ai.financ.brut",b.getAi().getImmobFinanc().getBrutN().getMontant());
		map.put("ai.financ.amort",b.getAi().getImmobFinanc().getAmortDeprN().getMontant());
		map.put("ai.financ.ant",b.getAi().getImmobFinanc().getNetN().getMontant());
		
		map.put("ac.stock.brut",b.getAc().getStocks().getBrutN().getMontant());
		map.put("ac.stock.amort",b.getAc().getStocks().getAmortDeprN().getMontant());
		map.put("ac.stock.ant",b.getAc().getStocks().getNetN().getMontant());
		
		map.put("ac.creancpub.brut",b.getAc().getCreanceEP().getBrutN().getMontant());
		map.put("ac.creancpub.amort",b.getAc().getCreanceEP().getAmortDeprN().getMontant());
		map.put("ac.creancpub.ant",b.getAc().getCreanceEP().getNetN().getMontant());
		
		map.put("ac.creancclient.brut",b.getAc().getCreanceCCR().getBrutN().getMontant());
		map.put("ac.creancclient.amort",b.getAc().getCreanceCCR().getAmortDeprN().getMontant());
		map.put("ac.creancclient.ant",b.getAc().getCreanceCCR().getNetN().getMontant());
		
		map.put("ac.creancredev.brut",b.getAc().getCreanceRedev().getBrutN().getMontant());
		map.put("ac.creancredev.amort",b.getAc().getCreanceRedev().getAmortDeprN().getMontant());
		map.put("ac.creancredev.ant",b.getAc().getCreanceRedev().getNetN().getMontant());
		
		map.put("ac.creancavanc.brut",b.getAc().getAvanceAcmptCmd().getBrutN().getMontant());
		map.put("ac.creancavanc.amort",b.getAc().getAvanceAcmptCmd().getAmortDeprN().getMontant());
		map.put("ac.creancavanc.ant",b.getAc().getAvanceAcmptCmd().getNetN().getMontant());
		
		map.put("ac.creanctier.brut",b.getAc().getCreanceCorrespOp().getBrutN().getMontant());
		map.put("ac.creanctier.amort",b.getAc().getCreanceCorrespOp().getAmortDeprN().getMontant());
		map.put("ac.creanctier.ant",b.getAc().getCreanceCorrespOp().getNetN().getMontant());
		
		map.put("ac.creancdeb.brut",b.getAc().getCreanceAutreDebit().getBrutN().getMontant());
		map.put("ac.creancdeb.amort",b.getAc().getCreanceAutreDebit().getAmortDeprN().getMontant());
		map.put("ac.creancdeb.ant",b.getAc().getCreanceAutreDebit().getNetN().getMontant());

		map.put("ac.chargecons.brut",b.getAc().getChargeConstat().getBrutN().getMontant());
		map.put("ac.chargecons.amort",b.getAc().getChargeConstat().getAmortDeprN().getMontant());
		map.put("ac.chargecons.ant",b.getAc().getChargeConstat().getNetN().getMontant());
		
		map.put("treso.place.brut",b.getTresA().getValMobPlac().getBrutN().getMontant());
		map.put("treso.place.amort",b.getTresA().getValMobPlac().getAmortDeprN().getMontant());
		map.put("treso.place.ant",b.getTresA().getValMobPlac().getNetN().getMontant());
		
		map.put("treso.disp.brut",b.getTresA().getDisponibilit().getBrutN().getMontant());
		map.put("treso.disp.amort",b.getTresA().getDisponibilit().getAmortDeprN().getMontant());
		map.put("treso.disp.ant",b.getTresA().getDisponibilit().getNetN().getMontant());
		
		map.put("treso.autre.brut",b.getTresA().getAutre().getBrutN().getMontant());
		map.put("treso.autre.amort",b.getTresA().getAutre().getAmortDeprN().getMontant());
		map.put("treso.autre.ant",b.getTresA().getAutre().getNetN().getMontant());
		
		map.put("compte.reg.brut",b.getTresA().getCompteReg().getBrutN().getMontant());
		map.put("compte.reg.amort",b.getTresA().getCompteReg().getAmortDeprN().getMontant());
		map.put("compte.reg.ant",b.getTresA().getCompteReg().getNetN().getMontant());
		
		map.put("ecart.conv.brut",b.getTresA().getEcartConvers().getBrutN().getMontant());
		map.put("ecart.conv.amort",b.getTresA().getEcartConvers().getAmortDeprN().getMontant());
		map.put("ecart.conv.ant",b.getTresA().getEcartConvers().getNetN().getMontant());
		
		map.put("finac.etatn",b.getFp().getFinancActEtat().getNetN().getMontant());
		map.put("finac.etatAnt",b.getFp().getFinancActEtat().getNetAnt().getMontant());
		
		map.put("finac.tiersn",b.getFp().getFinancActTiers().getNetN().getMontant());
		map.put("finac.tiersAnt",b.getFp().getFinancActTiers().getNetAnt().getMontant());
		
		map.put("fond.fondation",b.getFp().getFondProp().getNetN().getMontant());
		map.put("fond.fondationAnt",b.getFp().getFondProp().getNetAnt().getMontant());
		
		map.put("fecart.reval",b.getFp().getEcartReeval().getNetN().getMontant());
		map.put("ecart.revalAnt",b.getFp().getEcartReeval().getNetAnt().getMontant());
		
		map.put("reserveN",b.getFp().getReserve().getNetN().getMontant());
		map.put("reserveAnt",b.getFp().getReserve().getNetAnt().getMontant());
		
		map.put("reportN",b.getFp().getReportNouv().getNetN().getMontant());
		map.put("reportant",b.getFp().getReportNouv().getNetAnt().getMontant());
		
		map.put("resultatExecN",b.getFp().getResultExec().getNetN().getMontant());
		map.put("resultatExecAnt",b.getFp().getResultExec().getNetAnt().getMontant());
		
		map.put("provisReglN",b.getFp().getProvReg().getNetN().getMontant());
		map.put("provisReglant",b.getFp().getProvReg().getNetAnt().getMontant());
		
		map.put("prov.risque",b.getPrc().getProvRisq().getNetN().getMontant());
		map.put("prov.risqueAnt",b.getPrc().getProvRisq().getNetAnt().getMontant());
		
		map.put("prov.risque",b.getPrc().getProvRisq().getNetN().getMontant());
		map.put("prov.risqueAnt",b.getPrc().getProvRisq().getNetAnt().getMontant());
		
		map.put("prov.charge",b.getPrc().getProvCharg().getNetN().getMontant());
		map.put("prov.chargeAnt",b.getPrc().getProvCharg().getNetAnt().getMontant());
		
		map.put("emprunt.oblig",b.getDf().getEmpOblig().getNetN().getMontant());
		map.put("emprunt.obligAnt",b.getDf().getEmpOblig().getNetAnt().getMontant());
		
		map.put("emprunt.sousc",b.getDf().getEmpSouscrit().getNetN().getMontant());
		map.put("emprunt.souscAnt",b.getDf().getEmpSouscrit().getNetAnt().getMontant());
		
		map.put("dette.finance",b.getDf().getDetFinanc().getNetN().getMontant());
		map.put("dette.financeAnt",b.getDf().getDetFinanc().getNetAnt().getMontant());
		
		map.put("dette.fournis",b.getDnf().getDetFournisCmptRatt().getNetN().getMontant());
		map.put("dette.fournisAnt",b.getDnf().getDetFournisCmptRatt().getNetAnt().getMontant());
		
		map.put("dette.fisc",b.getDnf().getDetFiscSocial().getNetN().getMontant());
		map.put("dette.fiscAnt",b.getDnf().getDetFiscSocial().getNetAnt().getMontant());
		
		map.put("avance.recu",b.getDnf().getAvanceAcmptRecu().getNetN().getMontant());
		map.put("avance.recuAnt",b.getDnf().getAvanceAcmptRecu().getNetAnt().getMontant());
		
		map.put("dette.tiers",b.getDnf().getDetCorrespOp().getNetN().getMontant());
		map.put("dette.tiersAnt",b.getDnf().getDetCorrespOp().getNetAnt().getMontant());
		
		map.put("dette.nonfinanc",b.getDnf().getAutreDetFinanc().getNetN().getMontant());
		map.put("dette.nonfinancAnt",b.getDnf().getAutreDetFinanc().getNetAnt().getMontant());
		
		map.put("produit.const",b.getDnf().getProduiConstAvanc().getNetN().getMontant());
		map.put("produit.constAnt",b.getDnf().getProduiConstAvanc().getNetAnt().getMontant());
		
		map.put("tresor.autre",b.getTresP().getAutreElement().getNetN().getMontant());
		map.put("tresor.autreAnt",b.getTresP().getAutreElement().getNetAnt().getMontant());
		
		map.put("compte.regul",b.getTresP().getCompteRegl().getNetN().getMontant());
		map.put("compte.regulAnt",b.getTresP().getCompteRegl().getNetAnt().getMontant());
		
		map.put("ecart.conv.passif",b.getTresP().getEcartConvert().getNetN().getMontant());
		map.put("ecart.conv.passifAnt",b.getTresP().getEcartConvert().getNetAnt().getMontant());
//	}
	}
	private void createChargeFonc(Map<String, Object> map) {
		CompteFinancierBean cfBean=getCfBean();
		
		if(cfBean.getCf()!=null){
			
		List<CRItem> fonctionnement = cfBean.getCf().getCr().getCharge().getFonctionnement();
		
		  createCrList(map, fonctionnement, "chargeFonc", "chargeN", "chargeAnt", "fonct");
		}
		
	}
	
	private void createChargeInterv(Map<String, Object> map) {
		CompteFinancierBean cfBean=getCfBean();
		if(cfBean.getCf()!=null){
		List<CRItem> intervention = cfBean.getCf().getCr().getCharge().getIntervention();
		
		   createCrList(map, intervention, "chargeInter", "chargeN", "chargeAnt", "inter");
		}
	}
	
	private void createChargeFinanc(Map<String, Object> map) {
		CompteFinancierBean cfBean=getCfBean();
		if(cfBean.getCf()!=null){
		List<CRItem> intervention = cfBean.getCf().getCr().getCharge().getIntervention();
		
		  createCrList(map, intervention, "chargeFinanc", "chargeN", "chargeAnt", "financ");
		}
	}
	private void createProduitFonct(Map<String, Object> map) {
		CompteFinancierBean cfBean=getCfBean();
		if(cfBean.getCf()!=null){
		List<CRItem> produitFonc = cfBean.getCf().getCr().getProduit().getFonctionnement();
		
		  createCrList(map, produitFonc, "produitFonct", "produitN", "produitAnt", "produitFonc");
		}
	}
	private void createProduitFinanc(Map<String, Object> map) {
		CompteFinancierBean cfBean=getCfBean();
		if(cfBean.getCf()!=null){
		List<CRItem> produitFinanc = cfBean.getCf().getCr().getProduit().getFinancier();
		
		  createCrList(map, produitFinanc, "produitFinanc", "produitFinancN", "produitFinancAnt", "produitFinanc");
		}
	}

		
	

	private void createCrList(Map<String, Object> map,List<CRItem> list,String designation,String n, String ant,String nomList) {
		List<Map<String, Object>> niveau = new ArrayList<Map<String, Object>>();
		Map<String, Object> item = null;
//		List<CRItem> data = cfBean.getCf().getCr().getCharge().getFonctionnement();
//		
		
		if (list != null) {
			for (CRItem element : list) {
				item = new CustomHashMap<String, Object>();
				item.put(designation, element.getDesignation());
				item.put(n, element.getN().getMontant());
				item.put(ant, element.getAnt().getMontant());
				niveau.add(item);

				

			}
			map.put(nomList, niveau);
		}

		
	}

	public CompteFinancierBean getCfBean() {
		CompteFinancierBean bean= BudgetHelper.getCompteFinancierBean();
		logger.debug("getCfBean: bean="+bean);
		return bean;
	}

}
