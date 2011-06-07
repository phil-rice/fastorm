package org.fastorm.defns;

public interface IEntityDefnVisitor {
	void accept(IEntityDefn entityDefn) throws Exception;
}
