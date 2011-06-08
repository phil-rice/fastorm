package org.fastorm.temp;

import org.fastorm.context.WriteContext;
import org.fastorm.defns.IEntityDefn;
import org.fastorm.writer.impl.IMutatedDataTable;

public interface IMutatingTempTableMaker {

	void create(WriteContext writeContext, IEntityDefn entityDefn);

	int populateUpdate(WriteContext writeContext, IMutatedDataTable table);

	void update(WriteContext writeContext, IEntityDefn entityDefn);

}
