package org.fastorm.temp;

import org.fastorm.context.OrmWriteContext;
import org.fastorm.defns.IEntityDefn;
import org.fastorm.writer.impl.IMutatedDataTable;

public interface IMutatingTempTableMaker {

	void create(OrmWriteContext writeContext, IEntityDefn entityDefn);

	int populateUpdate(OrmWriteContext writeContext, IMutatedDataTable table);

	void update(OrmWriteContext writeContext, IEntityDefn entityDefn);

}
