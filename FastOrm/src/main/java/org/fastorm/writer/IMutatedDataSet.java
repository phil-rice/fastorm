package org.fastorm.writer;

import org.fastorm.defns.IEntityDefn;
import org.fastorm.writer.impl.IMutatedDataTable;

public interface IMutatedDataSet {

	IMutatedDataTable getTableFor(IEntityDefn entityDefn);

}
