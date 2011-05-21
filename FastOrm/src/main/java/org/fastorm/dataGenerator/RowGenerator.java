package org.fastorm.dataGenerator;

import java.util.Iterator;
import java.util.List;

public class RowGenerator implements IRowGenerator {

	@Override
	public String toString() {
		return "RowGenerator [generators=" + generators + ", fanout=" + fanout + "]";
	}

	private List<IGenerator> generators;
	private int fanout;

	public RowGenerator(List<IGenerator> generators, int fanout) {
		this.generators = generators;
		this.fanout = fanout;
	}

	@Override
	public Iterator<IGenerator> iterator() {
		return generators.iterator();
	}

	@Override
	public int fanout() {
		return fanout;
	}

}
