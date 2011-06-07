package org.fastorm.mutate;

import java.util.Map;

import org.fastorm.dataSet.IMutableDataSet;
import org.fastorm.temp.IPrimaryTempTableMaker;
import org.fastorm.temp.impl.AllEntitiesTempTableMaker;
import org.fastorm.temp.impl.IMutableItem;
import org.fastorm.utilities.callbacks.ICallback;
import org.fastorm.utilities.functions.IFunction1;

public interface IMutate<T> {

	void mutate(IFunction1<T, T> mutateFunction);

	void mutateMap(IFunction1<Map<String, Object>, Map<String, Object>> mutateFunction);

	void readModifyWrite(IPrimaryTempTableMaker maker, ICallback<IMutableDataSet> callback);

	void readModifyWrite(AllEntitiesTempTableMaker maker, ICallback<IMutableItem> iCallback);

}
