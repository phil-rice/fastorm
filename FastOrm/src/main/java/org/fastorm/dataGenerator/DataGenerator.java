package org.fastorm.dataGenerator;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.fastorm.defns.IEntityDefn;
import org.fastorm.defns.IEntityDefnVisitor;
import org.fastorm.utilities.Maps;
import org.fastorm.utilities.WrappedException;

public class DataGenerator {

	private final Random random = new Random();

	public void walkRandomData(final Map<IEntityDefn, IRowGenerator> generators, IEntityDefn primary, int size, final IDataGeneratorVisitor visitor) {
		try {
			IRowGenerator rowGenerator = generators.get(primary);
			// System.out.println("fanout " + primary + ":" + size);
			final Map<IEntityDefn, Integer> sizeMap = Maps.newMap();
			for (int i = 0; i < size; i++) {
				final Map<IEntityDefn, List<Map<String, Object>>> data = Maps.newMap();
				IEntityDefn.Utils.walk(primary, new IEntityDefnVisitor() {
					@Override
					public void acceptPrimary(IEntityDefn primary) {
						data.put(primary, new ArrayList<Map<String, Object>>());
					}

					@Override
					public void acceptChild(IEntityDefn parent, IEntityDefn child) {
						data.put(child, new ArrayList<Map<String, Object>>());
					}
				});
				data.get(primary).add(Maps.<String, Object> newMap());
				for (final IGenerator generator : rowGenerator)
					generator.contribute(data, sizeMap, primary);
				for (IEntityDefn child1 : primary.getChildren())
					generateFor(generators, data, sizeMap, primary, child1);
				visitor.acceptPrimary(data);
				IEntityDefn.Utils.walk(primary, new IEntityDefnVisitor() {
					@Override
					public void acceptPrimary(IEntityDefn primary) {
						Maps.add(sizeMap, primary, data.get(primary).size());
					}

					@Override
					public void acceptChild(IEntityDefn parent, IEntityDefn child) {
						Maps.add(sizeMap, child, data.get(child).size());
					}
				});
			}
		} catch (Exception e) {
			throw WrappedException.wrap(e);
		}
	}

	private void generateFor(Map<IEntityDefn, IRowGenerator> generators, Map<IEntityDefn, List<Map<String, Object>>> data, Map<IEntityDefn, Integer> sizeMap, IEntityDefn parent, IEntityDefn child) {
		IRowGenerator rowGenerator = generators.get(child);
		int size = random.nextInt(rowGenerator.fanout());
		// System.out.println("fanout " + parent + " " + child + ":" + size);
		for (int i = 0; i < size; i++) {
			data.get(child).add(Maps.<String, Object> newMap());
			for (final IGenerator generator : rowGenerator)
				generator.contribute(data, sizeMap, parent, child);
			for (IEntityDefn grandChild : child.getChildren())
				generateFor(generators, data, sizeMap, child, grandChild);
		}
	}

}
