package fr.symphonie.common.model.cpwin;

import java.util.Date;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

import fr.symphonie.cpwin.model.Document;
import fr.symphonie.cpwin.model.Tiers;
import fr.symphonie.cpwin.model.TiersDocument;

@StaticMetamodel(TiersDocument.class)
public abstract class TiersDocument_ {

	public static volatile SingularAttribute<TiersDocument, Integer> id;
	public static volatile SingularAttribute<TiersDocument, Integer> exercice;
	public static volatile SingularAttribute<TiersDocument, Date> date;
	public static volatile SingularAttribute<TiersDocument, String> titre;
	public static volatile SingularAttribute<TiersDocument, Integer> ordre;
	public static volatile SingularAttribute<TiersDocument, Tiers> tiers;
	public static volatile SingularAttribute<TiersDocument, Document> document;

}

