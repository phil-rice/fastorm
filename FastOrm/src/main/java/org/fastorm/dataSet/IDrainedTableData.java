package org.fastorm.dataSet;

import java.util.List;

import org.fastorm.defns.IEntityDefn;
import org.fastorm.memory.IMemoryManager;
import org.fastorm.utilities.IFunction1;
import org.fastorm.utilities.ISimpleMap;

public interface IDrainedTableData {

	IEntityDefn getEntityDefn();

	int getIdColumnIndex();

	int size();

	Object[] getLine(int i);

	ISimpleMap<String, Object> getMap(IGetDrainedTableForEntityDefn getter, int i);

	List<String> getColumnNames();

	List<ISimpleMap<String, Object>> findWith(IGetDrainedTableForEntityDefn getter, int columnIndex, Object value);
	void dispose(IMemoryManager memoryManager);

	static class Utils {

		public static final IFunction1<IDrainedTableData, String> getEntityDefnName = new IFunction1<IDrainedTableData, String>() {
			@Override
			public String apply(IDrainedTableData from) throws Exception {
				return from.getEntityDefn().getEntityName();
			}
		};

	}


}
