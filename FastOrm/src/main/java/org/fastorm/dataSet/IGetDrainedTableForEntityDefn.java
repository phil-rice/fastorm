package org.fastorm.dataSet;

import org.fastorm.defns.IEntityDefn;

public interface IGetDrainedTableForEntityDefn {

	IDrainedTableData get(IEntityDefn entityDefn);
}
