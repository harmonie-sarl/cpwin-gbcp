package fr.symphonie.budget.ui.excel.imp;

import fr.symphonie.excel.IAttribut;
import fr.symphonie.excel.IImportedStruct;
import fr.symphonie.excel.ImportEngine;

public class BudgetImport extends ImportEngine {
private static BudgetImport instance=null;

	private BudgetImport() {
	super();
}

	@Override
	protected IAttribut[] getAttributs() {
		return ImportAttributsEnum.values();
	}

	@Override
	protected IImportedStruct createImportedStruct() {
		return new ImportStruct();
	}

	@Override
	protected IAttribut getColumnMetadata(int columnIndex) {
		return ImportAttributsEnum.parse(columnIndex);
	}

	@Override
	public int getStartedLoppedPosition() {
		return 4;
	}

	

	public static BudgetImport getInstance() {
		if(instance==null)instance=new BudgetImport();
		return instance;
	}

}
