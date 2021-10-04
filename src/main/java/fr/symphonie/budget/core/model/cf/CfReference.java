package fr.symphonie.budget.core.model.cf;

public class CfReference {
private String code;
private String type;
private String description;
private boolean detail;
private String zoneExcel;
private Integer numeroLigne;
//private String numeroMinefi;
private String concerneAnt;
public String getCode() {
	return code;
}
public void setCode(String code) {
	this.code = code;
}
public String getType() {
	return type;
}
public void setType(String type) {
	this.type = type;
}
public String getDescription() {
	return description;
}
public void setDescription(String description) {
	this.description = description;
}
public boolean isDetail() {
	return detail;
}
public void setDetail(boolean detail) {
	this.detail = detail;
}
public String getZoneExcel() {
	return zoneExcel;
}
public void setZoneExcel(String zoneExcel) {
	this.zoneExcel = zoneExcel;
}
//public String getNumeroMinefi() {
//	return numeroMinefi;
//}
//public void setNumeroMinefi(String numeroMinefi) {
//	this.numeroMinefi = numeroMinefi;
//}

public String getConcerneAnt() {
	return concerneAnt;
}
public Integer getNumeroLigne() {
	return numeroLigne;
}
public void setNumeroLigne(Integer numeroLigne) {
	this.numeroLigne = numeroLigne;
}
public void setConcerneAnt(String concerneAnt) {
	this.concerneAnt = concerneAnt;
}

	public String getCodeAndLibelle() {
		String separator = " - ";
		
		StringBuilder s = new StringBuilder();
		if ((code == null) || (code.trim().isEmpty()))
			return null;
		
		if (getZoneExcel() != null) {
//			s.append(separator);
			s.append(getZoneExcel());
		}
		if (getDescription() != null) {
			s.append(separator);
			s.append(getDescription());
		}

		
		return s.toString();
	}

}
