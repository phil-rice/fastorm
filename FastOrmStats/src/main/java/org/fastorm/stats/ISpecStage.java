package org.fastorm.stats;

import java.util.Arrays;

import org.fastorm.api.IFastOrm;
import org.fastorm.api.IFastOrmContainer;
import org.fastorm.utilities.exceptions.WrappedException;
import org.fastorm.utilities.functions.IFunction1;

public interface ISpecStage {
	public int size();

	public IFastOrm makeFastOrm(final IFastOrm initial, int index) throws Exception;

	String titleFor();

	String formatFor(IFastOrm fastOrm);

	Object valueFor(IFastOrm fastOrm);

	public static class Utils {

		public static ISpecStage specific(final int width, final IFunction1<IFastOrm, String> nameFunction, IFunction1<IFastOrm, IFastOrm>... convertors) {
			return new SpecStage<IFunction1<IFastOrm, IFastOrm>>(Arrays.asList(convertors)) {
				@Override
				protected IFastOrm transform(IFastOrm container, IFunction1<IFastOrm, IFastOrm> t) throws Exception {
					return t.apply(container);
				}

				@Override
				public String titleFor() {
					return String.format(formatFor(null), "Specific");
				}

				@Override
				public String formatFor(IFastOrm fastOrm) {
					return "%-" + width + "s";
				}

				@Override
				public Object valueFor(IFastOrm fastOrm) {
					try {
						return nameFunction.apply(fastOrm);
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
				protected IFastOrm transform(IFastOrm fastOrm, Integer t) {
					IFastOrmContainer container = fastOrm.getContainer();
					return container.withOptions(container.getOptions().withBatchSize(t));
				}

				@Override
				public String titleFor() {
					return "BatchSize";
				}

				@Override
				public String formatFor(IFastOrm fastOrm) {
					return "%9d";
				}

				@Override
				public Object valueFor(IFastOrm fastOrm) {
					return fastOrm.getBatchSize();
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
				protected IFastOrm transform(IFastOrm fastOrm, Boolean t) {
					IFastOrmContainer container = fastOrm.getContainer();
					return container.withOptions(container.getOptions().withTempTables(t));
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
				public String formatFor(IFastOrm fastOrm) {
					return "%7s";
				}

				@Override
				public Object valueFor(IFastOrm fastOrm) {
					return fastOrm.getContainer().getOptions().useTemporaryTables ? "temp" : "";
				}
			};
		}

		public static ISpecStage indexSecondaryTables(final Boolean... use) {
			return new SpecStage<Boolean>(Arrays.asList(use)) {
				@Override
				protected IFastOrm transform(IFastOrm fastOrm, Boolean t) {
					IFastOrmContainer container = fastOrm.getContainer();
					return container.withOptions(container.getOptions().withIndexSecondaryTables(t));
				}

				@Override
				public String toString() {
					return "[indexSecondary: " + Arrays.asList(use) + "]";
				}

				@Override
				public Object valueFor(IFastOrm fastOrm) {
					return fastOrm.getContainer().getOptions().indexSecondaryTables ? "index" : "";
				}

				@Override
				public String titleFor() {
					return "IndexSec";
				}

				@Override
				public String formatFor(IFastOrm fastOrm) {
					return "%7s";
				}

			};
		}
	}

}
