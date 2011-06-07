package org.fastorm.dataGenerator;

import java.util.Map;

import org.fastorm.api.IFastOrmContainer;
import org.fastorm.defns.IEntityDefn;
import org.fastorm.defns.IEntityDefnParentChildVisitor;
import org.fastorm.defns.IMakerAndEntityDefnVisitor;
import org.fastorm.temp.IPrimaryTempTableMaker;
import org.fastorm.temp.ISecondaryTempTableMaker;
import org.fastorm.utilities.collections.Iterables;
import org.fastorm.utilities.functions.IFunction1;
import org.fastorm.utilities.maps.Maps;

public interface IDataGenerator {

	static class Utils {
		public static Map<IEntityDefn, IRowGenerator> findDefaultRowGenerators(IFastOrmContainer fastOrm, final int defaultFanOut) {
			return findDefaultRowGeneratorsWithExtra(fastOrm, defaultFanOut, new NoExtraDataGenerator());

		}

		public static Map<IEntityDefn, IRowGenerator> findDefaultRowGeneratorsWithExtra(IFastOrmContainer fastOrm, final int defaultFanOut, final IExtraDataGenerator extraDataGenerator) {
			final Map<IEntityDefn, Map<String, IGenerator>> entityToColumnsToRowGenerator = Maps.newMap();
			IEntityDefn.Utils.walk(fastOrm.getEntityDefn(), new IEntityDefnParentChildVisitor() {

				@Override
				public void acceptPrimary(IEntityDefn primary) throws Exception {
					extraDataGenerator.enrichValues(entityToColumnsToRowGenerator, primary);
				}

				@Override
				public void acceptChild(IEntityDefn parent, IEntityDefn child) throws Exception {
					extraDataGenerator.enrichValues(entityToColumnsToRowGenerator, child);
				}

			});
			IEntityDefn.Utils.walk(fastOrm, new IMakerAndEntityDefnVisitor() {

				@Override
				public void acceptPrimary(IPrimaryTempTableMaker maker, IEntityDefn primary) throws Exception {
					maker.enrichWithGenerators(entityToColumnsToRowGenerator, primary);
				}

				@Override
				public void acceptChild(ISecondaryTempTableMaker maker, IEntityDefn parent, IEntityDefn child) throws Exception {
					maker.enrichWithGenerators(entityToColumnsToRowGenerator, parent, child);

				}
			});
			return Maps.mapTheMap(entityToColumnsToRowGenerator, new IFunction1<Map<String, IGenerator>, IRowGenerator>() {
				@Override
				public IRowGenerator apply(Map<String, IGenerator> from) throws Exception {
					return new RowGenerator(Iterables.list(from.values()), defaultFanOut);
				}
			});
		}
	}
}
