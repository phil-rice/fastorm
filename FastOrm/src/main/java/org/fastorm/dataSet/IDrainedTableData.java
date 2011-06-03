package org.fastorm.dataSet;

import java.util.List;

import org.fastorm.defns.IEntityDefn;
import org.fastorm.utilities.functions.IFunction1;
import org.fastorm.utilities.maps.ISimpleMap;
import org.fastorm.utilities.maps.ISimpleMapWithIndex;

public interface IDrainedTableData {

	IEntityDefn getEntityDefn();

	int getIdColumnIndex();

	int size();

	int indexOf(String key);

	ISimpleMapWithIndex<String, Object> getMap(int i);

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
