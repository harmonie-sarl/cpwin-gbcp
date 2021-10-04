package fr.symphonie.common.model;

import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

import fr.symphonie.util.model.SimpleEntity;

@StaticMetamodel(SimpleEntity.class)
public abstract class SimpleEntity_ {

	public static volatile SingularAttribute<SimpleEntity, String> code;
	public static volatile SingularAttribute<SimpleEntity, String> designation;

}

