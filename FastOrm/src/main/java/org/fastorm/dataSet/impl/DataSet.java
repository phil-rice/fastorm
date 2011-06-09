package org.fastorm.dataSet.impl;

import java.text.MessageFormat;
import java.util.List;

import org.fastorm.constants.FastOrmMessages;
import org.fastorm.dataSet.IDrainedTableData;
import org.fastorm.dataSet.IGetDrainedTableForEntityDefn;
import org.fastorm.dataSet.IMutableDataSet;
import org.fastorm.defns.IEntityDefn;
import org.fastorm.utilities.annotations.TightLoop;
import org.fastorm.utilities.collections.Iterables;
import org.fastorm.utilities.maps.IMutableSimpleMapWithIndex;
import org.fastorm.utilities.maps.ISimpleMap;
import org.fastorm.utilities.maps.ISimpleMapWithIndex;

public class DataSet implements IMutableDataSet, IGetDrainedTableForEntityDefn {

	private final List<IDrainedTableData> tableData;

	private final IDrainedTableData primaryTable;

	public DataSet(List<IDrainedTableData> tableData) {
		this.tableData = tableData;
		this.primaryTable = tableData.get(0);
	}

	@Override
	public int size() {
		return primaryTable.size();
	}

	@Override
	public ISimpleMapWithIndex<String, Object> get(int i) {
		return primaryTable.get(i);
	}

	@Override
	public List<String> keys() {
		return primaryTable.keys();
	}

	@Override
	@TightLoop
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
	public String toString() {
		return "DataSet [primary=" + primaryTable.getEntityDefn().getEntityName() + ", tableData=" + tableData + "]";
	}

	@Override
	public IMutableSimpleMapWithIndex<String, Object> getMutableMap(int i) {
		return null;
	}

	@Override
	public IMutableSimpleMapWithIndex<String, Object> getMutableMapById(Object id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void insert(ISimpleMap<String, Object> data) {
		// TODO Auto-generated method stub

	}

	@Override
	public void addToCollection(ISimpleMap<String, Object> primaryObject, String collectionKey, ISimpleMap<String, Object> newValues) {
		// TODO Auto-generated method stub

	}

}
