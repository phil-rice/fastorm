package org.fastorm.writer.impl;

import java.util.List;

import org.fastorm.defns.IEntityDefn;
import org.fastorm.utilities.maps.IListOfSimpleMapWithIndex;

public interface IMutatedDataTable extends IListOfSimpleMapWithIndex<String, Object> {

	IEntityDefn getEntityDefn();

	List<Integer> changedColumnIndicies();

}
