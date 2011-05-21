package org.fastorm.dataSet.impl;

import java.text.MessageFormat;
import java.util.List;

import org.fastorm.constants.FastOrmMessages;
import org.fastorm.dataSet.IDataSet;
import org.fastorm.dataSet.IDrainedTableData;
import org.fastorm.dataSet.IGetDrainedTableForEntityDefn;
import org.fastorm.defns.IEntityDefn;
import org.fastorm.memory.IMemoryManager;
import org.fastorm.utilities.ISimpleMap;
import org.fastorm.utilities.Iterables;
import org.fastorm.utilities.Optimised;
import org.fastorm.utilities.SimpleLists;

public class DataSet implements IDataSet, IGetDrainedTableForEntityDefn {

	private List<IDrainedTableData> tableData;
	private final IDrainedTableData primaryTable;

	public DataSet(List<IDrainedTableData> tableData) {
		this.tableData = tableData;
		this.primaryTable = tableData.get(0);
	}

	public int size() {
		return primaryTable.size();
	}

	public ISimpleMap<String, Object> get(final int index) {
		return primaryTable.getMap(this, index);
	}

	public List<ISimpleMap<String, Object>> getList() {
		return SimpleLists.asList(this);
	}

	public IDrainedTableData getPrimaryTable() {
		return primaryTable;
	}

	@Override
	@Optimised
	public IDrainedTableData get(IEntityDefn entityDefn) {
		for (int i = 0; i < tableData.size(); i++) {
			IDrainedTableData table = tableData.get(i);
			if (table.getEntityDefn() == entityDefn)
				return table;
		}
		// not optimised any more
		Iterable<String> legalNames = Iterables.<IDrainedTableData, String> map(tableData, IDrainedTableData.Utils.getEntityDefnName);
		throw new NullPointerException(MessageFormat.format(FastOrmMessages.cannotFindTableForEntityDefn, entityDefn.getTableName(), legalNames));
	}

	@Override
	public void dispose(IMemoryManager memoryManager) {
		List<IDrainedTableData> oldData = tableData;
		tableData = null;
		for (IDrainedTableData data : oldData) {
			data.dispose(memoryManager);
		}
	}

}
