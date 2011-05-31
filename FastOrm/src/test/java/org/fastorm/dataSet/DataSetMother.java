package org.fastorm.dataSet;

import org.fastorm.utilities.maps.ISimpleMap;
import org.fastorm.utilities.maps.SimpleMaps;

@SuppressWarnings("unchecked")
public class DataSetMother {
	public final static ISimpleMap<String, Object> a1 = SimpleMaps.makeMap("a", 1);
	public final static ISimpleMap<String, Object> b2 = SimpleMaps.makeMap("b", 2);
	public final static ISimpleMap<String, Object> c3 = SimpleMaps.makeMap("c", 3);
	public final static ISimpleMap<String, Object> d4 = SimpleMaps.makeMap("d", 4);
	public final static ISimpleMap<String, Object> e5 = SimpleMaps.makeMap("e", 5);
	public final static ISimpleMap<String, Object> f6 = SimpleMaps.makeMap("f", 6);
	public final static ISimpleMap<String, Object> g7 = SimpleMaps.makeMap("g", 7);
	public final static ISimpleMap<String, Object> h8 = SimpleMaps.makeMap("h", 8);

	public final static IDataSet abc = new DataSetMock(a1, b2, c3);
	public final static IDataSet def = new DataSetMock(d4, e5, f6);
	public final static IDataSet gh = new DataSetMock(g7, h8);

}
