package fr.symphonie.common.model.cpwin;

import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

import fr.symphonie.cpwin.model.Service;

@StaticMetamodel(Service.class)
public abstract class Service_ {

	public static volatile SingularAttribute<Service, String> code;
	public static volatile SingularAttribute<Service, String> nom;

}

