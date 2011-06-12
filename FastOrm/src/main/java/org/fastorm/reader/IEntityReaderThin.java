package org.fastorm.reader;

import org.fastorm.context.IContext;
import org.fastorm.dataSet.IMutableDataSet;

public interface IEntityReaderThin {

	IMutableDataSet readPage(int page, IContext context);

}
