package org.fastorm.dataSet;

import org.fastorm.defns.IEntityDefn;
import org.fastorm.utilities.maps.IListOfMutableSimpleMapWithIndex;
import org.fastorm.utilities.maps.ISimpleMap;

/** this changes the data in the database when the read/modify/write cycle is over. It doesn't change any values that you read from the dataset */
public interface IMutableDataSet extends IListOfMutableSimpleMapWithIndex<String, Object> {

	IDataSet immutableDataSet();

	void delete(int index);

	void delete(Object id);

	void insert(ISimpleMap<String, Object> data);

	void insert(IEntityDefn defn, Object id, ISimpleMap<String, Object> data);

	void delete(IEntityDefn defn, Object id);

}
