package org.fastorm.dataSet;

import org.fastorm.utilities.callbacks.ICallback;

public interface IDataSetCallback extends ICallback<IDataSet> {

	@Override
	void process(IDataSet dataSet) throws Exception;

}
