package fr.symphonie.common.model.cpwin;

import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

import fr.symphonie.cpwin.model.ModePaiement;

@StaticMetamodel(ModePaiement.class)
public abstract class ModePaiement_ {

	public static volatile SingularAttribute<ModePaiement, String> code;
	public static volatile SingularAttribute<ModePaiement, String> nom;

}

