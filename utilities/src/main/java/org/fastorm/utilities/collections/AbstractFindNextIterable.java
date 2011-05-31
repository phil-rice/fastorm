package org.fastorm.utilities.collections;

import java.util.Iterator;
import java.util.NoSuchElementException;

import org.fastorm.utilities.exceptions.WrappedException;

public abstract class AbstractFindNextIterable<T, Context> implements Iterable<T> {
	abstract protected T findNext(Context context) throws Exception;

	abstract protected Context reset() throws Exception;

	@Override
	public Iterator<T> iterator() {
		try {
			return new IteratorAdaptor<T>() {
				private final Context context = reset();
				private boolean usedNext = true;
				private T next;

				@Override
				public boolean hasNext() {
					findNextItem();
					return next != null;
				}

				private void findNextItem() {
					try {
						if (usedNext) {
							usedNext = false;
							next = findNext(context);
						}
					} catch (Exception e) {
						throw WrappedException.wrap(e);
					}
				}

				@Override
				public T next() {
					findNextItem();
					if (next == null)
						throw new NoSuchElementException();
					usedNext = true;
					return next;
				}

				@Override
				public String toString() {
					return "[context=" + context + ", usedNext=" + usedNext + ", next=" + next + "]";
				}
			};
		} catch (Exception e) {
			throw WrappedException.wrap(e);
		}
	}

}
