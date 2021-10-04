package fr.symphonie.common.model.cpwin;

import java.util.Date;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

import fr.symphonie.cpwin.model.Banque;

@StaticMetamodel(Banque.class)
public abstract class Banque_ {

	public static volatile SingularAttribute<Banque, String> codePays;
	public static volatile SingularAttribute<Banque, String> codeBanque;
	public static volatile SingularAttribute<Banque, String> nom;
	public static volatile SingularAttribute<Banque, Date> finDiffusion;

}

