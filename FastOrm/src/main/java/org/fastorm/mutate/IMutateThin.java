package org.fastorm.mutate;

import java.util.Map;

import org.fastorm.sqlDialects.ISqlStrings;
import org.fastorm.temp.ISecondaryTempTableMaker;

public interface IMutateThin {
	void process(ISqlStrings sqlStrings, Map<String, Object> entityDefn, ISecondaryTempTableMaker intermediateTempTableMaker, IMutateDataSetCallback callback);

}
