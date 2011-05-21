package org.fastorm.dataSet;

import org.fastorm.api.ICallback;

public interface IDataSetCallback extends ICallback<IDataSet> {

	void process(IDataSet dataSet) throws Exception;

}
