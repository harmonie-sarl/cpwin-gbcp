package fr.symphonie.budget.core.model.util;

import java.io.File;

public class LocalDocument {
	private String parentId;
	private String categorie;
	private String name;
	private String prefixPath;
	private File file;
	
	public LocalDocument() {
		super();
	}
	
	public LocalDocument(String parentId, String categorie, String name,
			String prefixPath) {
		this();
		this.parentId = parentId;
		this.categorie = categorie;
		this.name = name;
		this.prefixPath = prefixPath;
	}

	public String getParentId() {
		return parentId;
	}
	public void setParentId(String parentId) {
		this.parentId = parentId;
	}
	public String getCategorie() {
		return categorie;
	}
	public void setCategorie(String categorie) {
		this.categorie = categorie;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPath() {
		return getPrefixPath()+getParentId()+File.separator+getCategorie();
	}
	
	public File getFile() {
		return file;
	}
	public void setFile(File file) {
		this.file = file;
	}

	public void setPrefixPath(String prefixPath) {
		this.prefixPath = prefixPath;
	}

	public String getPrefixPath() {
		return prefixPath;
	}
	public boolean isDocument(){
		if(getName()==null)
			return false;
		return true;
	}
	

}
