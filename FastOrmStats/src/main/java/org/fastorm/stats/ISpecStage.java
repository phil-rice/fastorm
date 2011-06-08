package org.fastorm.stats;

import java.util.Arrays;

import org.fastorm.api.IFastOrmContainer;
import org.fastorm.api.IJob;
import org.fastorm.utilities.exceptions.WrappedException;
import org.fastorm.utilities.functions.IFunction1;

public interface ISpecStage {
	public int size();

	public IJob makeFastOrm(final IJob initial, int index) throws Exception;

	String titleFor();

	String formatFor(IJob job);

	Object valueFor(IJob job);

	public static class Utils {

		public static ISpecStage specific(final int width, final IFunction1<IJob, String> nameFunction, IFunction1<IJob, IJob>... convertors) {
			return new SpecStage<IFunction1<IJob, IJob>>(Arrays.asList(convertors)) {
				@Override
				protected IJob transform(IJob container, IFunction1<IJob, IJob> t) throws Exception {
					return t.apply(container);
				}

				@Override
				public String titleFor() {
					return String.format(formatFor(null), "Specific");
				}

				@Override
				public String formatFor(IJob job) {
					return "%-" + width + "s";
				}

				@Override
				public Object valueFor(IJob job) {
					try {
						return nameFunction.apply(job);
					} catch (Exception e) {
						throw WrappedException.wrap(e);
					}
				}

				@Override
				public String toString() {
					return "[specific ]";
				}
			};
		}

		public static ISpecStage batchSize(final Integer... sizes) {
			return new SpecStage<Integer>(Arrays.asList(sizes)) {
				@Override
				protected IJob transform(IJob job, Integer t) {
					IFastOrmContainer container = job.getContainer();
					return container.withBatchSize(t);
				}

				@Override
				public String titleFor() {
					return "BatchSize";
				}

				@Override
				public String formatFor(IJob job) {
					return "%9d";
				}

				@Override
				public Object valueFor(IJob job) {
					return job.getBatchSize();
				}

				@Override
				public String toString() {
					return "[batchSize: " + Arrays.asList(sizes) + "]";
				}
			};
		}

		public static ISpecStage temporaryTables(final Boolean... use) {
			return new SpecStage<Boolean>(Arrays.asList(use)) {
				@Override
				protected IJob transform(IJob job, Boolean t) {
					IFastOrmContainer container = job.getContainer();
					return container.withTempTables(t);
				}

				@Override
				public String toString() {
					return "[tempTables: " + Arrays.asList(use) + "]";
				}

				@Override
				public String titleFor() {
					return "UseTemp";
				}

				@Override
				public String formatFor(IJob job) {
					return "%7s";
				}

				@Override
				public Object valueFor(IJob job) {
					return job.useTemporaryTables() ? "temp" : "";
				}
			};
		}

		public static ISpecStage indexSecondaryTables(final Boolean... use) {
			return new SpecStage<Boolean>(Arrays.asList(use)) {
				@Override
				protected IJob transform(IJob job, Boolean t) {
					IFastOrmContainer container = job.getContainer();
					return container.withIndexSecondaryTables(t);
				}

				@Override
				public String toString() {
					return "[indexSecondary: " + Arrays.asList(use) + "]";
				}

				@Override
				public Object valueFor(IJob job) {
					return job.indexSecondaryTables() ? "index" : "";
				}

				@Override
				public String titleFor() {
					return "IndexSec";
				}

				@Override
				public String formatFor(IJob job) {
					return "%7s";
				}

			};
		}
	}

}
