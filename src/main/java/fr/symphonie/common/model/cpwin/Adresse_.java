package fr.symphonie.common.model.cpwin;

import fr.symphonie.cpwin.model.Adresse;
import fr.symphonie.cpwin.model.Tiers;
import fr.symphonie.util.model.SimpleEntity;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@StaticMetamodel(Adresse.class)
public abstract class Adresse_ {

	public static volatile SingularAttribute<Adresse, Integer> ordre;
	public static volatile SingularAttribute<Adresse, String> codeTiers;
	public static volatile SingularAttribute<Adresse, String> adresse1;
	public static volatile SingularAttribute<Adresse, String> adresse2;
	public static volatile SingularAttribute<Adresse, String> ville;
	public static volatile SingularAttribute<Adresse, String> codePostale;
	public static volatile SingularAttribute<Adresse, SimpleEntity> pays;
	public static volatile SingularAttribute<Adresse, String> bureauPostale;
	public static volatile SingularAttribute<Adresse, String> type;
	public static volatile SingularAttribute<Adresse, Tiers> tiers;

}

