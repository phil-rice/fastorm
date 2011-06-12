package org.fastorm.temp;

import org.fastorm.context.IContext;
import org.fastorm.dataSet.IDrainedTableData;
import org.fastorm.defns.IEntityDefn;

public interface IMutatingTempTableMaker {

	void create(IContext writeContext, IEntityDefn entityDefn);

	int populateUpdate(IContext writeContext, IDrainedTableData table);

	void update(IContext writeContext, IEntityDefn entityDefn);

}
