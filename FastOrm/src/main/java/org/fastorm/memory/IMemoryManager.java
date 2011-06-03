package org.fastorm.memory;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.fastorm.api.FastOrmOptions;
import org.fastorm.dataSet.IGetDrainedTableForEntityDefn;
import org.fastorm.dataSet.impl.DrainedLine;
import org.fastorm.dataSet.impl.DrainedLineCommonData;
import org.fastorm.dataSet.impl.DrainedTableData;
import org.fastorm.defns.IEntityDefn;
import org.fastorm.pooling.api.IObjectDefinition;
import org.fastorm.pooling.api.IPool;
import org.fastorm.pooling.api.PoolOptions;
import org.fastorm.pooling.impl.Pool;

public interface IMemoryManager {

	DrainedLine makeDrainedLine(DrainedLineCommonData commonData, ResultSet rs, int index) throws SQLException;

	DrainedTableData makeDrainedTableData(IMemoryManager memoryManager, IEntityDefn entityDefn, IGetDrainedTableForEntityDefn getter, ResultSet rs) throws SQLException;

	void dispose();

	IMemoryManager withFastOrmOptions(FastOrmOptions options);

	/** This is a prototype poolOptions. Everything except for size is determined by this */
	IMemoryManager withPoolOptions(PoolOptions poolOptions);

	static class Utils {
		public static IPool<DrainedLine> makeDrainedLinePool(PoolOptions poolOptions) {
			IObjectDefinition<DrainedLine> defn = new IObjectDefinition<DrainedLine>() {
				@Override
				public Class<DrainedLine> objectClass() {
					return DrainedLine.class;
				}

				@Override
				public DrainedLine createBlank() {
					return new DrainedLine();
				}

				@Override
				public void clean(DrainedLine oldObject) {
					oldObject.clean();
				}
			};
			IPool<DrainedLine> result = new Pool<DrainedLine>(IPool.Utils.findThinInterface(poolOptions, defn));
			result.prepopulate();
			return result;
		}

		public static IPool<DrainedTableData> makeDrainedTableDataPool(final PoolOptions poolOptions, final int maxSize) {
			IObjectDefinition<DrainedTableData> defn = new IObjectDefinition<DrainedTableData>() {
				@Override
				public Class<DrainedTableData> objectClass() {
					return DrainedTableData.class;
				}

				@Override
				public DrainedTableData createBlank() {
					return new DrainedTableData(maxSize);
				}

				@Override
				public void clean(DrainedTableData oldObject) {
					oldObject.clean();
				}
			};
			IPool<DrainedTableData> result = new Pool<DrainedTableData>(IPool.Utils.findThinInterface(poolOptions, defn));
			result.prepopulate();
			return result;
		}
	}

}
