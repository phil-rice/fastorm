package org.fastorm.mutate;

import java.util.Map;

import org.fastorm.utilities.IFunction1;

public interface IMutate<T> {

	void mutate(IFunction1<T, T> mutateFunction);

	void mutateMap(IFunction1<Map<String, Object>, Map<String, Object>> mutateFunction);

}
