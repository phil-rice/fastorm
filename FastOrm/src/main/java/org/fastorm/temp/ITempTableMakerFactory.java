package org.fastorm.temp;

import java.util.Map;

import org.fastorm.api.IJobOptimisations;
import org.fastorm.defns.IEntityDefn;
import org.fastorm.temp.impl.TempTableMakerFactory;

public interface ITempTableMakerFactory {
	ISecondaryTempTableMaker findReaderMakerFor(Map<String, String> parent, Map<String, String> child);

	IMutatingTempTableMaker findMutatingMakerFor(IEntityDefn entityDefn);

	static class Utils {
		public static ITempTableMakerFactory withUsualBestOptions() {
			return new TempTableMakerFactory(IJobOptimisations.Utils.usualBest());
		}

		public static ITempTableMakerFactory withStoredProcedures() {
			return new TempTableMakerFactory(IJobOptimisations.Utils.usualBest());
		}

		public static ITempTableMakerFactory withoutStoredProcedures() {
			return new TempTableMakerFactory(IJobOptimisations.Utils.usualBest());
		}
	}
}
