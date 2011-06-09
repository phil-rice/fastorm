package org.fastorm.dataSet;

import org.fastorm.utilities.maps.IMutableSimpleMapWithIndex;
import org.fastorm.utilities.maps.ISimpleMap;

/**
 * This changes the data in the database when the read/modify/write cycle is over.
 * 
 * <p>
 * Updates for primitives (strings, integers...) are fairly obvious: you get a mutable map and modify it
 * <p>
 * Inserting into the primary table is obvious. To add to a collection you need to use the insert (IEntityDefn) method.
 * <p>
 * <p>
 * When you have a collection (for example from a one to many relationship) you need to call delete on the child map that you want to delete. Putting to the key will not currently work.
 * 
 * */
public interface IMutableDataSet extends IDataSet {

	IMutableSimpleMapWithIndex<String, Object> getMutableMap(int i);

	IMutableSimpleMapWithIndex<String, Object> getMutableMapById(Object id);

	/** This adds data to the mutable data set. Child collections will be processed as well */
	void insert(ISimpleMap<String, Object> data);

	/**
	 * @params
	 * @param primaryObject
	 *            is the object that you want to add to. It must already exist in the IMutableDataSet (i.e. found by getMutableMap, or by getting items from that map)
	 * @param collectionKey
	 *            is the key to the collection within that map that the newValues will be added to
	 * @param newValues
	 *            is a map of the data that will be added. It is an error to specify the linking data. If a key is not present, then it will be set to null
	 */
	void addToCollection(ISimpleMap<String, Object> primaryObject, String collectionKey, ISimpleMap<String, Object> newValues);

}
