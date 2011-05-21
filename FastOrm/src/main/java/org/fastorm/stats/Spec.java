package org.fastorm.stats;

import java.util.Arrays;
import java.util.List;

import org.fastorm.api.IFastOrm;
import org.fastorm.api.IFastOrmContainer;
import org.fastorm.utilities.AbstractFindNextIterable;
import org.fastorm.utilities.IFunction1;
import org.fastorm.utilities.Iterables;

public class Spec {

	static class SpecContext {

		static class SpecStageAndIndex {
			public int index;
			public SpecStage<?> specStage;

			public SpecStageAndIndex(SpecStage<?> specStage) {
				this.specStage = specStage;
			}

			@Override
			public String toString() {
				return "SpecStageAndIndex [index=" + index + ", specStage=" + specStage + "]";
			}
		}

		private List<SpecStageAndIndex> stages;
		boolean finished;

		public SpecContext(SpecStage<?>... stages) {
			this.stages = Iterables.list(Iterables.mapValues(new IFunction1<SpecStage, SpecStageAndIndex>() {
				@Override
				public SpecStageAndIndex apply(SpecStage from) throws Exception {
					return new SpecStageAndIndex(from);
				}
			}, stages));
		}

		void addOne() {
			int i = stages.size() - 1;
			while (true) {
				if (i < 0) {
					finished = true;
					return;
				}
				SpecStageAndIndex specStageAndIndex = stages.get(i);
				SpecStage<?> specStage = specStageAndIndex.specStage;
				if (specStageAndIndex.index < specStage.size() - 1) {
					specStageAndIndex.index += 1;
					return;
				} else
					specStageAndIndex.index = 0;
				i--;
			}
		}

		IFastOrm getFastOrm(IFastOrm initial) {
			if (finished)
				return null;
			IFastOrm value = initial;
			for (int i = 0; i < stages.size(); i++) {
				SpecStageAndIndex specStageAndIndex = stages.get(i);
				value = specStageAndIndex.specStage.makeFastOrm(value, specStageAndIndex.index);
				if (value == null) {
					finished = true;
					return null;
				}
			}
			return value;
		}

		public IFastOrm findNext(IFastOrm initial) {
			IFastOrm result = getFastOrm(initial);
			addOne();
			return result;
		}

		@Override
		public String toString() {
			return "SpecContext [stages=" + stages + ", finished=" + finished + "]";
		}
	}

	public Iterable<IFastOrm> test(final IFastOrm initial, final SpecStage<?>... stages) {
		return new AbstractFindNextIterable<IFastOrm, SpecContext>() {
			@Override
			protected SpecContext reset() throws Exception {
				return new SpecContext(stages);
			}

			@Override
			protected IFastOrm findNext(SpecContext context) throws Exception {
				return context.findNext(initial);
			}

			@Override
			public String toString() {
				return "[Spec.test stages = " + Arrays.asList(stages) + "]";
			}

		};
	}

	public static SpecStage<Integer> batchSize(final Integer... sizes) {
		return new SpecStage<Integer>(Arrays.asList(sizes)) {
			@Override
			protected IFastOrm transform(IFastOrm fastOrm, Integer t) {
				IFastOrmContainer container = fastOrm.getContainer();
				return container.withOptions(container.getOptions().withBatchSize(t));
			}

			@Override
			public String toString() {
				return "[batchSize: " + Arrays.asList(sizes) + "]";
			}
		};
	}

	public static SpecStage<Boolean> temporaryTables(final Boolean... use) {
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
		};
	}

	public static SpecStage<Boolean> indexSecondaryTables(final Boolean... use) {
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
		};
	}

}
