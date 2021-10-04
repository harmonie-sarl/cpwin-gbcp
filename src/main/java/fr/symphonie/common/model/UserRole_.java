package fr.symphonie.common.model;

import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

import fr.symphonie.util.model.UserRole;

@StaticMetamodel(UserRole.class)
public abstract class UserRole_ {

	public static volatile SingularAttribute<UserRole, String> role;
	public static volatile SingularAttribute<UserRole, String> designation;

}

