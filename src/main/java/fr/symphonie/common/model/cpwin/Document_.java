package fr.symphonie.common.model.cpwin;

import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

import fr.symphonie.cpwin.model.Document;

@StaticMetamodel(Document.class)
public abstract class Document_ {

	public static volatile SingularAttribute<Document, String> code;
	public static volatile SingularAttribute<Document, String> titre;
	public static volatile SingularAttribute<Document, String> supprimer;

}

