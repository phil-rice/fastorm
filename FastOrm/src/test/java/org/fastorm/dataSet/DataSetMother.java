package org.fastorm.dataSet;

import org.fastorm.utilities.maps.ISimpleMapWithIndex;
import org.fastorm.utilities.maps.SimpleMaps;

@SuppressWarnings("unchecked")
public class DataSetMother {
	public final static ISimpleMapWithIndex<String, Object> a1 = SimpleMaps.makeMapWithIndex("a", 1);
	public final static ISimpleMapWithIndex<String, Object> b2 = SimpleMaps.makeMapWithIndex("b", 2);
	public final static ISimpleMapWithIndex<String, Object> c3 = SimpleMaps.makeMapWithIndex("c", 3);
	public final static ISimpleMapWithIndex<String, Object> d4 = SimpleMaps.makeMapWithIndex("d", 4);
	public final static ISimpleMapWithIndex<String, Object> e5 = SimpleMaps.makeMapWithIndex("e", 5);
	public final static ISimpleMapWithIndex<String, Object> f6 = SimpleMaps.makeMapWithIndex("f", 6);
	public final static ISimpleMapWithIndex<String, Object> g7 = SimpleMaps.makeMapWithIndex("g", 7);
	public final static ISimpleMapWithIndex<String, Object> h8 = SimpleMaps.makeMapWithIndex("h", 8);

	public final static IMutableDataSet abc = new DataSetMock(a1, b2, c3);
	public final static IMutableDataSet def = new DataSetMock(d4, e5, f6);
	public final static IMutableDataSet gh = new DataSetMock(g7, h8);

}
