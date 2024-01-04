package fr.symphonie.budget.ui.excel;

import fr.symphonie.budget.ui.excel.edition.BiBrExcelData;
import fr.symphonie.budget.ui.excel.edition.InfocentreExcelData;
import fr.symphonie.budget.ui.excel.edition.InfocentreExcelData2024;
import fr.symphonie.budget.ui.excel.tb.TBComptDepense;
import fr.symphonie.budget.ui.excel.tb.TBComptRecette;
import fr.symphonie.budget.ui.excel.tb.TBComptRendGest;
import fr.symphonie.budget.ui.excel.tb.TBParEnvelop;
import fr.symphonie.budget.ui.excel.tb.TBSuiviCp;
import fr.symphonie.tools.gts.ui.excel.GtsArticLeList;
import fr.symphonie.tools.gts.ui.excel.GtsLBList;
import fr.symphonie.tools.gts.ui.excel.GtsLiqAGenerer;

public enum ExcelModelEnum {

	EDITION_BI_BR("edition.xls",new BiBrExcelData() ),
	TB_ENVELOP_LIST("tbParEnveloppe.xls",new TBParEnvelop()),
	TB_COMPT_RENDU_GEST("tbComptRenduGest.xls",new TBComptRendGest()),
	TB_SUIVI_CP("tbSuivi_cp.xls",new TBSuiviCp()),
	TB_CC_DEPENSE("cc_depense.xls",new TBComptDepense()),
	TB_CC_RECETTE("cc_recette.xls",new TBComptRecette()),
	ENVELOP_BUDG_LIST("envelopBudg_List.xls",new EnvelopBudgList()), LIGN_BUDG_LIST("ligneBudg_List.xls",new LigneBudgList()),
	DETAILS_SUIVICP_ANT("details_cp_ant.xls", new DetailCPAntList()),
	DETAILS_SUIVIAECP("details_ae_cp.xls", new DetailAECPList()),
	DETAILS_EJ_PLURI("details_ej_pluri.xls", new DetailEjPluriList()),
	DETAILS_SUIVICP_EXEC("details_cp_exec.xls", new DetailCPExecList()),
	DETAILS_SUIVI_RECET_ANT("details_suivi_recet_ant.xls", new DetailSuiviRecetAntList()),
	DETAILS_SUIVI_RECET_EXEC("details_suivi_recet_exec.xls", new DetailSuiviRecetExecList()),
	DETAILS_SUIVI_RECET_TOTAL("details_suivi_recet.xls", new DetailSuiviRecetList()),
	RECETTES_PAR_ORIGIN("recettes_par_origin.xls", new RecetByOriginList()),
	DEMAT_INFOCENTRE_BI("infocentre_periodBi.xls",new InfocentreExcelData()),
	DEMAT_INFOCENTRE_BI_2024("infocentre_periodBi_2024.xls",new InfocentreExcelData2024()),
	DEMAT_INFOCENTRE("infocentre.xls",new InfocentreExcelData()),
	DEMAT_INFOCENTRE_2024("infocentre2024.xls",new InfocentreExcelData2024()),
	DEMAT_INFOCENTRE_06("infocentre_periode06.xls",new InfocentreExcelData() ),
	DEMAT_INFOCENTRE_06_2024("infocentre_periode06_2024.xls",new InfocentreExcelData2024() ),
	PlAN_TRESORERIE("plan_tresorerie.xls",new TresorerieExcelData() ),
	PlAN_TRESORERIE_2024("plan_tresorerie2024.xls",new TresorerieExcelData2024() ),
	SUIVI_HONORAIRES("suivi_honoraires.xls",new HonoraireList()),
	GTS_IMPORT_LB("recetLignBudg.xls",new GtsLBList()),
	GTS_IMPORT_ARTICL("recetArticle.xls",new GtsArticLeList()),
	GTS_IMPORT_LIQAG("liquAGenerer.xls",new GtsLiqAGenerer()),
	DEMAT_CF_SAISIE("cf_saisie.xls",new CFSaisiList()), 
	DEMAT_CF("suivi_cf.xls",new suiviCFList()), 
	DEMAT_CF_PLT_SAISIE("cf_plt_saisie.xls",new CFSaisiList()),
	IMPORT_BI("import_BI.xls", new ModelImportBiList()),
	IMPORT_BR("import_BR.xls", new ModelImportBiList()),
	SUIVI_IMPUT_SF("suivi_imput_sf.xls",new SFImputList()),
	REMB_CLIENT_CONSULT("rembours_consultTiers.xls",new RBConsultTiersList()),
	REMB_VAGUEDETAILS_CONSULT("rembours_vagueDetails.xls",new RBVagueDetailList()),
	META4DAI_SAISIE("META4DAI-Saisie.xls",new DAISaisiList()),
	META4DAI_CONSULT("META4DAI-Consult.xls",new DAIConsultList()),
	META4DAI_SAISIE_TRAITE("META4DAI-Saisie-traite.xls",new DAISaisiPTList()),
	DAS_TIERS_CONSULT ("das_tiers_existants.xls",new ExistantList());
	private String fileName;
	private ExcelDocType docType;
	private ExcelModelEnum(String fileName) {
		this.fileName = fileName;
	}
	

	private ExcelModelEnum(String fileName, ExcelDocType docType) {
		this.fileName = fileName;
		this.setDocType(docType);
	}


	public String getFileName() {
		return fileName;
	}


	private void setDocType(ExcelDocType docType) {
		this.docType = docType;
	}


	public ExcelDocType getDocType() {
		return docType;
	}
}
