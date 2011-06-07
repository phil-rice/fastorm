package org.fastorm.temp.impl;

import org.fastorm.utilities.maps.ISimpleMapWithIndex;

public interface IMutableItem extends ISimpleMapWithIndex<String, Object> {

	void put(String key, Object value);

}
