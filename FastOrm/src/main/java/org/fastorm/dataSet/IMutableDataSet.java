package org.fastorm.dataSet;

import org.fastorm.defns.IEntityDefn;
import org.fastorm.utilities.maps.ISimpleMap;

/** this changes the data in the database when the read/modify/write cycle is over. It doesn't change any values that you read from the dataset */
public interface IMutableDataSet extends IDataSet {

	/** Changes primitive values. Do not use to change collections: use delete or insert for that */
	void change(IEntityDefn defn, Object id, String key, Object newValue);

	void delete(IEntityDefn defn, Object id);

	void insert(IEntityDefn defn, Object id, ISimpleMap<String, Object> data);

}
