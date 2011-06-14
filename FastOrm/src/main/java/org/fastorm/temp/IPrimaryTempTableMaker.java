package org.fastorm.temp;

import org.fastorm.context.IContext;

public interface IPrimaryTempTableMaker extends ITempTableMaker {

	void clean(IContext readContext);

	void create(IContext readContext);

	int startOfBatch(IContext readContext, int page);

	void drain(IContext readContext, int page);

}
