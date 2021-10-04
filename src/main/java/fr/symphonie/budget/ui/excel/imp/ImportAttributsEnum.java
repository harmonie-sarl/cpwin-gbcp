package fr.symphonie.budget.ui.excel.imp;

import org.apache.poi.hssf.usermodel.HSSFCell;

import fr.symphonie.excel.IAttribut;



public enum ImportAttributsEnum implements IAttribut {
	GROUP_DEST(0,HSSFCell.CELL_TYPE_STRING,true),
	GROUP_NAT(1,HSSFCell.CELL_TYPE_STRING,true),
	DEST(2,HSSFCell.CELL_TYPE_STRING,true),NAT(3,HSSFCell.CELL_TYPE_STRING,true),
	CODE_PROG(4,HSSFCell.CELL_TYPE_STRING,true),CODE_SERV(5,HSSFCell.CELL_TYPE_STRING,true),
	MONTANT(6,HSSFCell.CELL_TYPE_NUMERIC,true);
	
	private int position;
	private int type;
	private boolean required;
	private ImportAttributsEnum(int position, int type,boolean required) {
		this.setPosition(position);
		this.setType(type);
		this.setRequired(required);
	}
	public static ImportAttributsEnum parse(int position){
		for(ImportAttributsEnum i:ImportAttributsEnum.values()){
			if(i.getPosition()==position) return i;
		}
		return null;
	}
	public void setPosition(int position) {
		this.position = position;
	}
	public int getPosition() {
		return position;
	}
	private void setType(int type) {
		this.type = type;
	}
	public int getType() {
		return type;
	}
	@Override
	public boolean isRequired() {
		return required;
	}
	private void setRequired(boolean required) {
		this.required = required;
	}
}
