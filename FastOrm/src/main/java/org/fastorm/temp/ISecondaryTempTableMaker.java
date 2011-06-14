package org.fastorm.temp;

import java.util.Map;

import org.fastorm.context.IContext;
import org.fastorm.dataSet.IGetDrainedTableForEntityDefn;
import org.fastorm.defns.IEntityDefn;

public interface ISecondaryTempTableMaker extends ITempTableMaker {

	boolean accept(Map<String, String> parent, Map<String, String> child);

	/** removes the artifacts in the database needed to support this temp table maker. Example: tables, stored procedures.. */
	void clean(IContext readContext, IEntityDefn entity);

	/** creates the artifacts in the database needed to support this temp table maker. Example: tables, stored procedures. */
	void create(IContext context, IEntityDefn parent, IEntityDefn child);

	/** Called at the start of batch, after the parent startOfBatch are called. */
	int startOfBatch(IContext context, IEntityDefn parent, IEntityDefn child);

	void drain(IContext readContext, IEntityDefn parent, IEntityDefn child);

	Object findDataIn(IGetDrainedTableForEntityDefn getter, IEntityDefn parentDefn, int parentIndex, IEntityDefn childDefn, int childIndex);

}
