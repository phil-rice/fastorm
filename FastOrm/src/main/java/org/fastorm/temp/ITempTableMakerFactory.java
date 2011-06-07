package org.fastorm.temp;

import java.util.Map;

import org.fastorm.defns.IEntityDefn;

public interface ITempTableMakerFactory {
	ISecondaryTempTableMaker findReaderMakerFor(Map<String, String> parent, Map<String, String> child);

	IMutatingTempTableMaker findMutatingMakerFor(IEntityDefn entityDefn);
}
