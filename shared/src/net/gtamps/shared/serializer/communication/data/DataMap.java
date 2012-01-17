package net.gtamps.shared.serializer.communication.data;

import java.util.Arrays;
import java.util.Iterator;
import java.util.NoSuchElementException;


public final class DataMap extends AbstractSendableData<DataMap> implements Iterable<MapEntry<?>> {

	public static final int MAX_CAPACITY = 10;

	private static final long serialVersionUID = 4291073965913737110L;

	private final MapEntry<?>[] table = new MapEntry[MAX_CAPACITY];
	private int load = 0;
	private transient int iteratorPosition = 0;
	private final transient Iterator<MapEntry<?>> iterator = new Iterator<MapEntry<?>>() {

		@Override
		public boolean hasNext() {
			return iteratorPosition < load;
		}

		@Override
		public MapEntry<?> next() {
			if (iteratorPosition >= load) {
				throw new NoSuchElementException();
			}
			return table[iteratorPosition++];
		}

		@Override
		public void remove() {
			throw new UnsupportedOperationException();
		}

	};


	public DataMap add(final MapEntry<?> entry) throws IllegalArgumentException, IllegalStateException {
		if (entry == null) {
			throw new IllegalArgumentException("'entry' must not be 'null'");
		}
		if (entry.key() == null) {
			throw new IllegalArgumentException("'entry.key()' must not be 'null'");
		}
		if (load == MAX_CAPACITY) {
			throw new IllegalStateException("cannot add entry: max capacity reached!");
		}
		table[load++] = entry;
		return this;
	}

	public AbstractSendableData<?> get(final String key) throws NoSuchElementException {
		if (key == null) {
			throw new IllegalArgumentException("'key' must not be 'null'");
		}
		for (int i = 0; i < load; i++) {
			if (table[i].key().equals(key)) {
				return table[i].value();
			}
		}
		throw new NoSuchElementException("no entry found for key: " + key);
	}

	public void resetIterator() {
		iteratorPosition = 0;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + load;
		result = prime * result + Arrays.hashCode(table);
		return result;
	}

	@Override
	public boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final DataMap other = (DataMap) obj;
		if (load != other.load) {
			return false;
		}
		if (!Arrays.equals(table, other.table)) {
			return false;
		}
		return true;
	}

	@Override
	public Iterator<MapEntry<?>> iterator() throws IllegalStateException {
		if (iteratorPosition > 0) {
			throw new IllegalStateException("iterator is not in initial state");
		}
		return iterator;
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder("DataMap { ");
		for (int i = 0; i < load; i++) {
			sb.append(table[i].toString() + ", ");
		}
		sb.append("}");
		return sb.toString();
	}

	@Override
	protected void initHook() {
		load = 0;
		resetIterator();
		Arrays.fill(table, null);
	}


	@Override
	protected void recycleHook() {
		for (int i = 0; i < load; i++) {
			table[i].recycle();
		}
	}

}
