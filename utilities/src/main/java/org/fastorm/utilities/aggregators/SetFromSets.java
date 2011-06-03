package org.fastorm.utilities.aggregators;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

import org.fastorm.utilities.collections.Iterables;

public class SetFromSets<T> implements Set<T> {

	private final Iterable<Set<T>> sets;

	public SetFromSets(Iterable<Set<T>> sets) {
		this.sets = sets;
	}

	@Override
	public int size() {
		int size = 0;
		for (Set<T> set : sets)
			size += set.size();
		return size;
	}

	@Override
	public boolean isEmpty() {
		for (Set<T> set : sets)
			if (!set.isEmpty())
				return false;
		return true;
	}

	@Override
	public boolean contains(Object key) {
		for (Set<T> set : sets)
			if (set.contains(key))
				return true;
		return false;
	}

	@Override
	public boolean containsAll(Collection<?> c) {
		for (Object x : c)
			if (!contains(x))
				return false;
		return true;
	}

	@Override
	public Iterator<T> iterator() {
		return Iterables.split(sets).iterator();
	}

	@Override
	public Object[] toArray() {
		throw new UnsupportedOperationException();
	}

	@Override
	public <E> E[] toArray(E[] a) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean add(T o) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean remove(Object o) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean addAll(Collection<? extends T> c) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean retainAll(Collection<?> c) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean removeAll(Collection<?> c) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void clear() {
		throw new UnsupportedOperationException();
	}

}
