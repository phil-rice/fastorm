package org.fastorm.temp;

import java.util.Map;

public interface ITempTableMakerFactory {
	ISecondaryTempTableMaker findMakerFor(Map<String, String> parent, Map<String, String> child);
}
