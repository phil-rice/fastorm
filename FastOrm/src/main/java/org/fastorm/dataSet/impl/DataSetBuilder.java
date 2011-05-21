package org.fastorm.dataSet.impl;

import java.util.ArrayList;
import java.util.List;

import org.fastorm.dataSet.IDataSet;
import org.fastorm.dataSet.IDrainedTableData;
import org.fastorm.utilities.aggregators.IAggregator;

public class DataSetBuilder implements IAggregator<IDrainedTableData, IDataSet> {

	private final List<IDrainedTableData> tableData = new ArrayList<IDrainedTableData>();

	public void add(IDrainedTableData t) {
		tableData.add(t);
	}

	public IDataSet result() {
		return new DataSet(tableData);
	}
}
