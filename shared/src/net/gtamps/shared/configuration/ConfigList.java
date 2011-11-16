package net.gtamps.shared.configuration;

import java.util.AbstractCollection;
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

public final class ConfigList extends AbstractList<Configuration> implements Configuration {

	private static final long serialVersionUID = -2683152299650610400L;

	private final Class<?> type = List.class;
	private final List<Configuration> entries = new ArrayList<Configuration>();
	private final ConfigSource source;

	ConfigList(final ConfigSource source) {
		this(source, 10);
	}

	ConfigList(final ConfigSource source, final int initSize) {
		if (source == null) {
			throw new IllegalArgumentException("'source' must not be 'null'");
		}
		if (initSize < 0) {
			throw new IllegalArgumentException("'initSize' must not be >= 0");
		}
		this.source = source;
	}

	@Override
	public Class<?> getType() {
		return this.type;
	}

	@Override
	public int getCount() {
		return this.entries.size();
	}

	@Override
	public Configuration select(final String key) {
		Configuration element = null;
		try {
			element = get(Integer.valueOf(key));
		} catch (final NumberFormatException e){
			// hickey-dee-doo!
		}
		return element;
	}

	@Override
	public Configuration select(final int index) {
		return this.entries.get(index);
	}

	@Override
	public Configuration get(final int index) {
		return select(index);
	}

	@Override
	public String getString() {
		return this.entries.toString();
	}

	@Override
	public ConfigSource getSource() {
		return this.source;
	}


	@Override
	public int size() {
		return this.entries.size();
	}

	boolean addConfiguration(final Configuration cfg) {
		return this.entries.add(cfg);
	}

	Configuration removeConfiguration(final int index) {
		checkIndex(index);
		return this.entries.remove(index);
	}

	Configuration getConfiguration(final int index) {
		checkIndex(index);
		return this.entries.get(index);
	}

	void clearList() {
		this.entries.clear();
	}

	private void checkIndex(final int index) {
		if (index < 0) {
			throw new IllegalArgumentException("'index' must be >= 0");
		}
		if (index >= getCount()) {
			throw new IndexOutOfBoundsException(String.format(
					"index out of bounds (%d): %d", size(), index));
		}
	}

	@Override
	public Integer getInt() {
		//TODO warn
		return null;
	}

	@Override
	public Float getFloat() {
		//TODO warn
		return null;
	}

	@Override
	public Boolean getBoolean() {
		//TODO warn
		return null;
	}

	@Override
	public ConfigList clone() {
		final ConfigList cloneList = new ConfigList(source);
		for (final Configuration element : entries) {
			cloneList.entries.add(element.clone());
		}
		return cloneList;
	}

	@Override
	public Collection<String> getKeys() {
		return StringRange.get(entries.size());
	}

	@Override
	public Iterator<Configuration> iterator() {
		return Collections.unmodifiableCollection(entries).iterator();
	}


	private static class StringRange extends AbstractCollection<String> {

		private static StringRange lastRange = null;

		public static StringRange get(final int ceiling) {
			if (lastRange == null || lastRange.ceiling != ceiling) {
				lastRange = new StringRange(ceiling);
			}
			return lastRange;
		}

		private final int ceiling;
		private int count = 0;

		private StringRange(final int ceiling) {
			assert ceiling >= 0;
			this.ceiling = ceiling;
		}

		@Override
		public Iterator<String> iterator() {
			return new Iterator<String>() {
				@Override
				public boolean hasNext() {
					return count < ceiling;
				}
				@Override
				public String next() {
					if (!hasNext()) {
						throw new NoSuchElementException();
					}
					return Integer.toString(count++);
				}
				@Override
				public void remove() {
					throw new UnsupportedOperationException();
				}
			};
		}

		@Override
		public int size() {
			return ceiling;
		}

	}

}