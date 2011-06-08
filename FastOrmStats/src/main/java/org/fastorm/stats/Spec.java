package org.fastorm.stats;

import java.util.Arrays;
import java.util.List;

import org.fastorm.api.IJob;
import org.fastorm.stats.Spec.SpecContext;
import org.fastorm.utilities.aggregators.IAggregator;
import org.fastorm.utilities.collections.AbstractFindNextIterable;
import org.fastorm.utilities.collections.Iterables;
import org.fastorm.utilities.exceptions.WrappedException;
import org.fastorm.utilities.functions.IFunction1;

public class Spec extends AbstractFindNextIterable<IJob, SpecContext> {

	private final List<ISpecStage> stages;
	private final IJob initial;
	private Integer databaseSize;

	public Spec(IJob initial, ISpecStage... stages) {
		this(initial, Arrays.asList(stages));
	}

	public Spec(IJob initial, List<ISpecStage> stages) {
		this.initial = initial;
		this.stages = stages;
	}

	private Spec(IJob initial, Integer databaseSize, List<ISpecStage> stages) {
		this.initial = initial;
		this.databaseSize = databaseSize;
		this.stages = stages;
	}

	public Spec withDatabaseSize(Integer databaseSize) {
		return new Spec(initial, databaseSize, stages);

	}

	@Override
	protected SpecContext reset() throws Exception {
		return new SpecContext();
	}

	@Override
	protected IJob findNext(SpecContext context) throws Exception {
		return context.findNext(initial);
	}

	public String title() {
		String result = Iterables.aggregate(stages, new SpecStringAggregator(databaseSize == null ? null : "DatabaseSize", null) {
			@Override
			protected String toString(IJob job, ISpecStage t) {
				return t.titleFor();
			}
		});
		return result;
	}

	public String stringFor(IJob job) {
		return Iterables.aggregate(stages, new SpecStringAggregator(databaseSize == null ? null : String.format("%12s", databaseSize), job) {
			@Override
			protected String toString(IJob job, ISpecStage t) {
				String format = t.formatFor(job);
				Object value = t.valueFor(job);
				return String.format(format, value);
			}
		});
	}

	@Override
	public String toString() {
		return "[Spec.test stages = " + stages + "]";
	}

	abstract static class SpecStringAggregator implements IAggregator<ISpecStage, String> {
		abstract protected String toString(final IJob job, ISpecStage t);

		private final IJob job;
		private final StringBuilder builder;

		private SpecStringAggregator(Object initialValue, IJob job) {
			this.job = job;
			builder = new StringBuilder();
			if (initialValue != null)
				builder.append(initialValue);
		}

		@Override
		public void add(ISpecStage t) {
			if (builder.length() > 0)
				builder.append(" ");
			builder.append(toString(job, t));

		}

		@Override
		public String result() {
			return builder.toString();
		}
	}

	static class SpecStageAndIndex {
		public int index;
		public ISpecStage specStage;

		public SpecStageAndIndex(ISpecStage specStage) {
			this.specStage = specStage;
		}

		@Override
		public String toString() {
			return "SpecStageAndIndex [index=" + index + ", specStage=" + specStage + "]";
		}
	}

	class SpecContext {
		private List<SpecStageAndIndex> stageAndIndicies;
		boolean finished;

		public SpecContext() {
			stageAndIndicies = Iterables.list(Iterables.map(stages, new IFunction1<ISpecStage, SpecStageAndIndex>() {
				@Override
				public SpecStageAndIndex apply(ISpecStage from) throws Exception {
					return new SpecStageAndIndex(from);
				}
			}));
		}

		void addOne() {
			int i = stageAndIndicies.size() - 1;
			while (true) {
				if (i < 0) {
					finished = true;
					return;
				}
				SpecStageAndIndex specStageAndIndex = stageAndIndicies.get(i);
				ISpecStage specStage = specStageAndIndex.specStage;
				if (specStageAndIndex.index < specStage.size() - 1) {
					specStageAndIndex.index += 1;
					return;
				} else
					specStageAndIndex.index = 0;
				i--;
			}
		}

		IJob getFastOrm(IJob initial) {
			try {
				if (finished)
					return null;
				IJob value = initial;
				for (int i = 0; i < stageAndIndicies.size(); i++) {
					SpecStageAndIndex specStageAndIndex = stageAndIndicies.get(i);
					value = specStageAndIndex.specStage.makeFastOrm(value, specStageAndIndex.index);
					if (value == null) {
						finished = true;
						return null;
					}
				}
				return value;
			} catch (Exception e) {
				throw WrappedException.wrap(e);
			}
		}

		public IJob findNext(IJob initial) {
			IJob result = getFastOrm(initial);
			addOne();
			return result;
		}

		@Override
		public String toString() {
			return "SpecContext [stages=" + stageAndIndicies + ", finished=" + finished + "]";
		}
	}

	public IJob getInitialFastOrm() {
		return initial;
	}

}
