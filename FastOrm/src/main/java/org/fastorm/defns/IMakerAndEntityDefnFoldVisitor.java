package org.fastorm.defns;

import org.fastorm.temp.IPrimaryTempTableMaker;
import org.fastorm.temp.ISecondaryTempTableMaker;

public interface IMakerAndEntityDefnFoldVisitor<From, To> {

	From acceptPrimary(IPrimaryTempTableMaker maker, IEntityDefn primary);

	From acceptChild(ISecondaryTempTableMaker maker, IEntityDefn parent, IEntityDefn child);

}
