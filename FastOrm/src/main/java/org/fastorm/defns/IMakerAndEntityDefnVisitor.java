package org.fastorm.defns;

import org.fastorm.temp.IPrimaryTempTableMaker;
import org.fastorm.temp.ISecondaryTempTableMaker;

public interface IMakerAndEntityDefnVisitor {

	void acceptPrimary(IPrimaryTempTableMaker maker, IEntityDefn primary) throws Exception;

	void acceptChild(ISecondaryTempTableMaker maker, IEntityDefn parent, IEntityDefn child)throws Exception;
}
