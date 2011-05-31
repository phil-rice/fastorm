package org.fastorm.defns;

public interface IEntityDefnFoldVisitor<From, To> {

	From acceptPrimary(IEntityDefn primary);

	From acceptChild(IEntityDefn parent, IEntityDefn child);

}
