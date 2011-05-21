package org.fastorm.defns;

import org.fastorm.temp.ISecondaryTempTableMaker;
import org.fastorm.temp.IPrimaryTempTableMaker;

public interface IMakerAndEntityDefnFoldVisitor<From, To> {

	From acceptPrimary(IPrimaryTempTableMaker maker, IEntityDefn primary);

	From acceptChild(ISecondaryTempTableMaker maker, IEntityDefn parent, IEntityDefn child);

}
