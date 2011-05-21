package org.fastorm.defns;

public interface IEntityDefnVisitor {

	void acceptPrimary(IEntityDefn primary) throws Exception;

	void acceptChild(IEntityDefn parent, IEntityDefn child) throws Exception;
}
