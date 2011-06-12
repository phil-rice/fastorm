package org.fastorm.dataSet.impl;

import java.util.ArrayList;
import java.util.List;

import org.fastorm.dataSet.IDrainedTableData;
import org.fastorm.dataSet.IMutableDataSet;
import org.fastorm.utilities.aggregators.IAggregator;

public class DataSetBuilder implements IAggregator<IDrainedTableData, IMutableDataSet> {

	private final List<IDrainedTableData> tableData = new ArrayList<IDrainedTableData>();

	@Override
	public void add(IDrainedTableData t) {
		tableData.add(t);
	}

	@Override
	public IMutableDataSet result() {
		return new DataSet(tableData);
	}
}
