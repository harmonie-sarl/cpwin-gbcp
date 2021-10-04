package fr.symphonie.common.model.cpwin;

import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

import fr.symphonie.cpwin.model.FormeSociale;

@StaticMetamodel(FormeSociale.class)
public abstract class FormeSociale_ {

	public static volatile SingularAttribute<FormeSociale, Integer> code;
	public static volatile SingularAttribute<FormeSociale, String> nom;
	public static volatile SingularAttribute<FormeSociale, String> soumisCmp;
	public static volatile SingularAttribute<FormeSociale, String> confidentiel;

}

