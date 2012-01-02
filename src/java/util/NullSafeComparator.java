package util;

import java.util.Comparator;

public abstract class NullSafeComparator<T> implements Comparator<T> {
	public final int compare(T o1, T o2) {
		if (o1 == o2) {
			return 0;
		} else if (null == o1) {
			return -1;
		} else if (null == o2) {
			return 1;
		} else {
			return nullSafeCompare(o1, o2);
		}
	}

	protected abstract int nullSafeCompare(T o1, T o2);

	@SuppressWarnings("unchecked")
	public static <T extends Comparable<?>> Comparator<T> getComparableInstance() {
		return (Comparator<T>) COMPARABLE_IMPL;
	}

	private static final Comparator<Comparable<Object>> COMPARABLE_IMPL = new NullSafeComparator<Comparable<Object>>() {
		@Override
		protected int nullSafeCompare(Comparable<Object> o1, Comparable<Object> o2) {
			return o1.compareTo(o2);
		}
	};
}
