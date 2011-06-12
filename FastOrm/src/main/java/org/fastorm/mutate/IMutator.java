package org.fastorm.mutate;

import org.fastorm.dataSet.IMutableDataSet;
import org.fastorm.temp.IPrimaryTempTableMaker;
import org.fastorm.temp.impl.AllEntitiesTempTableMaker;
import org.fastorm.utilities.callbacks.ICallback;

public interface IMutator<T> {

	void readModifyWrite(IPrimaryTempTableMaker maker, ICallback<IMutableDataSet> callback);

	void readModifyWrite(AllEntitiesTempTableMaker maker, ICallback<IMutableItem> callback);

}
