package org.fastorm.dataGenerator;

import java.util.Map;

import org.fastorm.api.IFastOrmContainer;
import org.fastorm.defns.IEntityDefn;
import org.fastorm.defns.IMakerAndEntityDefnVisitor;
import org.fastorm.temp.IPrimaryTempTableMaker;
import org.fastorm.temp.ISecondaryTempTableMaker;
import org.fastorm.utilities.IFunction1;
import org.fastorm.utilities.Iterables;
import org.fastorm.utilities.Maps;

public interface IDataGenerator {

	static class Utils {
		public static Map<IEntityDefn, IRowGenerator> findDefaultRowGenerators(IFastOrmContainer fastOrm, final int defaultFanOut) {
			final Map<IEntityDefn, Map<String, IGenerator>> entityToColumnsToRowGenerator = Maps.newMap();
			IEntityDefn.Utils.walk(fastOrm, new IMakerAndEntityDefnVisitor() {

				public void acceptPrimary(IPrimaryTempTableMaker maker, IEntityDefn primary) throws Exception {
					maker.enrichWithGenerators(entityToColumnsToRowGenerator, primary);
				}

				public void acceptChild(ISecondaryTempTableMaker maker, IEntityDefn parent, IEntityDefn child) throws Exception {
					maker.enrichWithGenerators(entityToColumnsToRowGenerator, parent, child);

				}
			});
			return Maps.mapTheMap(entityToColumnsToRowGenerator, new IFunction1<Map<String, IGenerator>, IRowGenerator>() {
				public IRowGenerator apply(Map<String, IGenerator> from) throws Exception {
					return new RowGenerator(Iterables.list(from.values()), defaultFanOut);
				}
			});
		}
	}
}
