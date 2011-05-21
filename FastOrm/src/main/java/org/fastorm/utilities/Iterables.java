package org.fastorm.utilities;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicInteger;

import org.fastorm.api.ICallback;
import org.fastorm.constants.FastOrmMessages;
import org.fastorm.utilities.aggregators.IAggregator;

public class Iterables {

	public static <To> Iterable<To> times(final int count, final IFunction1<Integer, To> fn) {
		return new AbstractFindNextIterable<To, AtomicInteger>() {
			@Override
			protected To findNext(AtomicInteger context) throws Exception {
				int index = context.getAndIncrement();
				if (index >= count)
					return null;
				else
					return fn.apply(index);
			}

			@Override
			protected AtomicInteger reset() throws Exception {
				return new AtomicInteger();
			}
		};
	}

	public static <From, To> Future<To> fold(ExecutorService service, final Iterable<From> iterable, final IFoldFunction<From, To> foldFunction, final To initial) {
		return service.submit(new Callable<To>() {
			@Override
			public To call() throws Exception {
				return fold(foldFunction, iterable, initial);
			}
		});
	}

	public static <From, To> To aggregate(Iterable<From> from, IAggregator<From, To> aggregator) {
		for (From f : from)
			aggregator.add(f);
		return aggregator.result();
	}

	public static <T> Iterable<T> filter(final Iterable<T> from, final IFunction1<T, Boolean> acceptor) {
		return new AbstractFindNextIterable<T, Iterator<T>>() {
			@Override
			protected T findNext(Iterator<T> context) {
				try {
					while (context.hasNext()) {
						T item = context.next();
						if (acceptor.apply(item))
							return item;
					}
					return null;
				} catch (Exception e) {
					throw WrappedException.wrap(e);
				}
			}

			@Override
			protected Iterator<T> reset() throws Exception {
				return from.iterator();
			}
		};
	}

	public static <From, Acc> Acc fold(IFoldFunction<From, Acc> foldFn, Iterable<From> from, Acc initial) {
		Acc value = initial;
		for (From item : from)
			value = foldFn.apply(item, value);
		return value;
	}

	@SuppressWarnings("unchecked")
	public static <T> Future<Void> processCallbacks(ExecutorService service, final Iterable<T> iterable, final ICallback<T> callback) {
		return (Future<Void>) service.submit(new Runnable() {
			@Override
			public void run() {
				processCallbacks(iterable, callback);
			}
		});
	}

	public static <T> void processCallbacks(Iterable<T> iterable, ICallback<T> callback) {
		try {
			for (T t : iterable)
				callback.process(t);
		} catch (Exception e) {
			throw WrappedException.wrap(e);
		}
	}

	public static <From, To> Iterable<To> mapValues(final IFunction1<From, To> convertor, From... from) {
		return map(iterable(from), convertor);
	}

	public static <From, To> Iterable<To> map(final Iterable<From> from, final IFunction1<From, To> convertor) {
		if (from == null)
			throw new NullPointerException();
		return new Iterable<To>() {
			@Override
			public Iterator<To> iterator() {
				return new Iterator<To>() {
					private final Iterator<From> iterator = from.iterator();

					@Override
					public boolean hasNext() {
						return iterator.hasNext();
					}

					@Override
					public To next() {
						try {
							return convertor.apply(iterator.next());
						} catch (Exception e) {
							throw WrappedException.wrap(e);
						}
					}

					@Override
					public void remove() {
						iterator.remove();
					}
				};
			}
		};

	}

	public static <T> Iterable<T> iterable(final Iterator<T> iterator) {
		return new Iterable<T>() {
			@Override
			public Iterator<T> iterator() {
				return iterator;
			}
		};
	}

	public static <T> List<T> list(Iterable<T> iterable) {
		if (iterable instanceof List)
			return (List<T>) iterable;
		ArrayList<T> result = new ArrayList<T>();
		for (T t : iterable)
			result.add(t);
		return result;
	}

	public static <T> List<T> list(Iterator<T> iterator) {
		ArrayList<T> result = new ArrayList<T>();
		while (iterator.hasNext())
			result.add(iterator.next());
		return result;
	}

	public static <From, To> Iterable<To> split(final Iterable<From> from, final IFunction1<From, Iterable<To>> convertor) {
		return new AbstractFindNextIterable<To, SplitContext<From, To>>() {

			@Override
			protected To findNext(SplitContext<From, To> context) {
				return context.findNext();
			}

			@Override
			protected SplitContext<From, To> reset() throws Exception {
				return new SplitContext<From, To>(from.iterator(), convertor);
			}
		};
	}

	public static <From extends Iterable<To>, To> Iterable<To> split(final Iterable<From> from) {
		return new AbstractFindNextIterable<To, SplitContext<From, To>>() {

			@Override
			protected To findNext(SplitContext<From, To> context) {
				return context.findNext();
			}

			@Override
			protected SplitContext<From, To> reset() throws Exception {
				return new SplitContext<From, To>(from.iterator(), Functions.<From, Iterable<To>> identity());
			}
		};
	}

	public static <T> ISimpleList<T> simpleList(Iterable<T> from) {
		return SimpleLists.fromList(list(from));
	}

	public static <T> Iterable<T> iterable(T[] ts) {
		return Arrays.asList(ts);
	}

}

class SplitContext<From, To> {
	public Iterator<From> fromiterator;
	public Iterator<To> toiterator;
	private final IFunction1<From, Iterable<To>> convertor;

	public SplitContext(Iterator<From> fromiterator, IFunction1<From, Iterable<To>> convertor) {
		this.fromiterator = fromiterator;
		this.convertor = convertor;
	}

	public To findNext() {
		try {
			while (toiterator == null || !toiterator.hasNext())
				if (fromiterator.hasNext()) {
					From next = fromiterator.next();
					if (next == null)
						throw new NullPointerException(FastOrmMessages.cannotHaveNullInIterableBeingProcessedBySplit);
					Iterable<To> nextIterator = convertor.apply(next);
					if (nextIterator == null)
						throw new NullPointerException(MessageFormat.format(FastOrmMessages.cannotHaveNullReturnedBySplitFunction, convertor.getClass(), next));
					toiterator = nextIterator.iterator();
				} else
					return null;
			return toiterator.hasNext() ? toiterator.next() : null;
		} catch (Exception e) {
			throw WrappedException.wrap(e);
		}
	}

}
