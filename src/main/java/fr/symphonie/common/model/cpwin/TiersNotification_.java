package fr.symphonie.common.model.cpwin;

import java.util.Date;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

import fr.symphonie.cpwin.model.Tiers;
import fr.symphonie.cpwin.model.TiersNotification;

@StaticMetamodel(TiersNotification.class)
public abstract class TiersNotification_ {

	public static volatile SingularAttribute<TiersNotification, Long> id;
	public static volatile SingularAttribute<TiersNotification, Integer> ordre;
	public static volatile SingularAttribute<TiersNotification, String> type;
	public static volatile SingularAttribute<TiersNotification, Date> date;
	public static volatile SingularAttribute<TiersNotification, Tiers> tiers;

}

