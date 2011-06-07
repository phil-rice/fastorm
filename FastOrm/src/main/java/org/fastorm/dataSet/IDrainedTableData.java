package org.fastorm.dataSet;

import java.util.List;

import org.fastorm.defns.IEntityDefn;
import org.fastorm.utilities.functions.IFunction1;
import org.fastorm.utilities.maps.IListOfSimpleMapWithIndex;
import org.fastorm.utilities.maps.ISimpleMap;

public interface IDrainedTableData extends IListOfSimpleMapWithIndex<String, Object> {

	IEntityDefn getEntityDefn();

	int getIdColumnIndex();

	int indexOf(String key);

	List<ISimpleMap<String, Object>> findWith(int columnIndex, Object value);

	static class Utils {

		public static final IFunction1<IDrainedTableData, String> getEntityDefnName = new IFunction1<IDrainedTableData, String>() {
			@Override
			public String apply(IDrainedTableData from) throws Exception {
				return from.getEntityDefn().getEntityName();
			}
		};

	}

}
