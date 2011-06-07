package org.fastorm.defns;

public interface IEntityDefnParentChildVisitor {

	void acceptPrimary(IEntityDefn primary) throws Exception;

	void acceptChild(IEntityDefn parent, IEntityDefn child) throws Exception;
}
