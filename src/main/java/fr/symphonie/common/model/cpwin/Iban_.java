package fr.symphonie.common.model.cpwin;

import java.util.Date;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

import fr.symphonie.cpwin.model.Iban;
import fr.symphonie.cpwin.model.Tiers;

@StaticMetamodel(Iban.class)
public abstract class Iban_ {

	public static volatile SingularAttribute<Iban, String> codeTiers;
	public static volatile SingularAttribute<Iban, Integer> ordre;
	public static volatile SingularAttribute<Iban, String> iban;
	public static volatile SingularAttribute<Iban, String> detenteur;
	public static volatile SingularAttribute<Iban, String> bic;
	public static volatile SingularAttribute<Iban, String> banque;
	public static volatile SingularAttribute<Iban, Boolean> ouvert;
	public static volatile SingularAttribute<Iban, Boolean> valide;
	public static volatile SingularAttribute<Iban, Date> finDiffusion;
	public static volatile SingularAttribute<Iban, Tiers> tiers;
	
	

	

}

