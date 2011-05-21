package org.fastorm.reader;

import org.fastorm.api.IFastOrmContainer;
import org.fastorm.dataSet.IDataSet;

public interface IEntityReaderThin {

	<T> Iterable<IDataSet> dataSets(IFastOrmContainer fastOrm);

}
