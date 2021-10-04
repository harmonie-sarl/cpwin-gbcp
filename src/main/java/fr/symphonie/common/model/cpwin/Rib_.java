package fr.symphonie.common.model.cpwin;

import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

import fr.symphonie.cpwin.model.Rib;
import fr.symphonie.cpwin.model.Tiers;

@StaticMetamodel(Rib.class)
public abstract class Rib_ {

	public static volatile SingularAttribute<Rib, String> codeTiers;
	public static volatile SingularAttribute<Rib, Integer> ordre;
	public static volatile SingularAttribute<Rib, String> detenteur;
	public static volatile SingularAttribute<Rib, String> domiciliation;
	public static volatile SingularAttribute<Rib, String> codeBanque;
	public static volatile SingularAttribute<Rib, String> numeroCompte;
	public static volatile SingularAttribute<Rib, String> codeGuichet;
	public static volatile SingularAttribute<Rib, String> cle;
	public static volatile SingularAttribute<Rib, String> valide;
	public static volatile SingularAttribute<Rib, Tiers> tiers;

}

