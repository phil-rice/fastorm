package org.fastorm.context;

import java.sql.Connection;

import org.fastorm.api.IFastOrmContainer;
import org.fastorm.writer.IMutatedDataSet;

public class OrmWriteContext extends AbstractContext {

	private final IMutatedDataSet dataSet;

	public OrmWriteContext(IFastOrmContainer fastOrm, Connection connection, IMutatedDataSet dataSet) {
		super(fastOrm, connection);
		this.dataSet = dataSet;
	}

	public IMutatedDataSet getDataSet() {
		return dataSet;
	}
}
