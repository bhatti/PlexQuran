package com.plexobject.quran.utils;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.NoSuchElementException;

public class SortedList<T> implements List<T> {
	private Comparator<T> comparator;
	private List<T> list;

	public SortedList(Comparator<T> comparator) {
		this.comparator = comparator;
		list = new ArrayList<T>();
	}

	public static <T> SortedList<T> copy(Collection<T> c, Comparator<T> cmp) {
		SortedList<T> list = new SortedList<T>(cmp);
		Iterator<T> iter = c.iterator();
		while (iter.hasNext()) {
			list.add(iter.next());
		}
		return list;
	}

	public SortedList<T> copy(Comparator<T> cmp) {
		SortedList<T> list = new SortedList<T>(cmp);
		Iterator<T> iter = iterator();
		while (iter.hasNext()) {
			list.add(iter.next());
		}
		return list;
	}

	public static <T> SortedList<T> copy(SortedList<T> c) {
		return copy(c, c.comparator);
	}

	@Override
	public int size() {
		return list.size();
	}

	@Override
	public boolean isEmpty() {
		return list.isEmpty();
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean contains(Object o) {
		int index = Collections.binarySearch(list, (T) o, comparator);
		if (index < 0)
			return false;
		return true;
	}

	@Override
	public Iterator<T> iterator() {
		return list.iterator();
	}

	@Override
	public Object[] toArray() {
		return list.toArray();
	}

	@SuppressWarnings("hiding")
	@Override
	public <T> T[] toArray(T[] a) {
		return list.toArray(a);
	}

	@Override
	public boolean add(T e) {
		int index = Collections.binarySearch(list, e, comparator);
		if (index < 0)
			index = (-(index) - 1);
		if (index < 0)
			index = 0;
		list.add(index, e);
		return true;
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean remove(Object o) {
		int index = Collections.binarySearch(list, (T) o, comparator);
		if (index >= 0 && index < list.size()) {
			list.remove(index);
			return true;
		}
		return false;
	}

	@Override
	public boolean containsAll(Collection<?> c) {
		return list.containsAll(c);
	}

	@Override
	public boolean addAll(Collection<? extends T> c) {
		Iterator<? extends T> it = c.iterator();
		while (it.hasNext())
			add(it.next());
		return true;
	}

	@Override
	public boolean addAll(int index, Collection<? extends T> c) {
		return addAll(c);
	}

	@Override
	public boolean removeAll(Collection<?> c) {
		int size = size();
		for (Object o : c) {
			remove(o);
		}
		return size() < size;
	}

	@Override
	public boolean retainAll(Collection<?> c) {
		return list.retainAll(c);
	}

	@Override
	public void clear() {
		list.clear();
	}

	@Override
	public T get(int index) {
		return list.get(index);
	}

	public T getFirst() {
		if (list.size() == 0)
			throw new NoSuchElementException();
		return list.get(0);
	}

	public T getLast() {
		if (list.size() == 0)
			throw new NoSuchElementException();
		return list.get(list.size() - 1);
	}

	@Override
	public T set(int index, T element) {
		throw new RuntimeException("set not supported");
	}

	@Override
	public void add(int index, T element) {
		throw new RuntimeException("add at specific index not supported");
	}

	@Override
	public T remove(int index) {
		if (index < 0)
			throw new IllegalArgumentException(
					"SortedList.remove got negative index");
		return list.remove(index);
	}

	@SuppressWarnings("unchecked")
	@Override
	public int indexOf(Object o) {
		int n = Collections.binarySearch(list, (T) o, comparator);
		if (n < 0)
			n = -1;
		return n;
	}

	@Override
	public int lastIndexOf(Object o) {
		return list.lastIndexOf(o);
	}

	@Override
	public ListIterator<T> listIterator() {
		return list.listIterator();
	}

	@Override
	public ListIterator<T> listIterator(int index) {
		return list.listIterator(index);
	}

	@Override
	public List<T> subList(int fromIndex, int toIndex) {
		return list.subList(fromIndex, toIndex);
	}

	public void print(PrintStream writer) {
		ListIterator<T> iterator = listIterator(0);
		while (iterator.hasNext()) {
			writer.print(iterator.next() + ", ");
		}
		writer.println();
	}

	public T removeLast() {
		if (list.size() == 0)
			throw new NoSuchElementException();
		return list.remove(list.size() - 1);
	}

	public T removeFirst() {
		if (list.size() == 0)
			throw new NoSuchElementException();
		return list.remove(0);
	}
}
