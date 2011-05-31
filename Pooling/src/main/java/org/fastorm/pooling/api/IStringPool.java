package org.fastorm.pooling.api;

import org.fastorm.utilities.annotations.Slow;
import org.fastorm.utilities.strings.ISimpleString;

public interface IStringPool extends IPool<ISimpleString> {

	@Slow("Slow because strings are immutable, so we should very rarely use them in fast loops")
	ISimpleString newString(String from);

	ISimpleString newString(ISimpleString string, int from, int to);

	ISimpleString newString(byte[] bytes, int from, int to);
}
