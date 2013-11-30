package net.gtamps.shared.configuration;

import java.util.*;

public final class ConfigList extends AbstractList<Configuration> implements Configuration {

    private static final long serialVersionUID = -2683152299650610400L;

    static final Class<?> TYPE = java.util.List.class;

    private final List<Configuration> entries = new ArrayList<Configuration>();
    private final AbstractConfigSource source;

    ConfigList(final AbstractConfigSource source) {
        this(source, 10);
    }

    /**
     * copy entries of {@code otherList} into this list. might set a source different
     * from {@code source}.
     */
    ConfigList(final AbstractConfigSource source, final ConfigList otherList) {
        this(source, otherList.entries.size() + 10);
        this.entries.addAll(otherList.entries);
    }

    ConfigList(final AbstractConfigSource source, final int initSize) {
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
        final Configuration singleton = getSoleElementIfExists();
        return singleton != null ? singleton.getType() : TYPE;
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
        } catch (final NumberFormatException e) {
            throw new IllegalArgumentException("key does not exist: " + key, e);
        }
        return element;
    }

    @Override
    public Configuration select(final int index) {
        try {
            return this.entries.get(index);
        } catch (final IndexOutOfBoundsException e) {
            throw new IllegalArgumentException("key does not exist: " + index, e);
        }
    }

    @Override
    public Configuration get(final int index) {
        return select(index);
    }

    @Override
    public String getString() {
        if (getCount() == 0) {
            return "";
        }
        final Configuration soleElement = getSoleElementIfExists();
        return soleElement != null ? soleElement.getString() : this.entries.toString();
    }

    @Override
    public AbstractConfigSource getSource() {
        final Configuration singleton = getSoleElementIfExists();
        return singleton != null ? singleton.getSource() : this.source;
    }

    @Override
    public boolean equals(final Object o) {
        if (o == null) {
            return false;
        }
        if (this.getClass() != o.getClass()) {
            return false;
        }
        return entries.equals(((ConfigList) o).entries);
    }

    @Override
    public int hashCode() {
        return entries.hashCode();
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
        final Configuration singleton = getSoleElementIfExists();
        if (singleton != null) {
            return singleton.getInt();
        }
        throw new IllegalArgumentException("no int value");
    }

    @Override
    public Float getFloat() {
        final Configuration singleton = getSoleElementIfExists();
        if (singleton != null) {
            return singleton.getFloat();
        }
        throw new IllegalArgumentException("no float value");
    }

    @Override
    public Boolean getBoolean() {
        final Configuration singleton = getSoleElementIfExists();
        if (singleton != null) {
            return singleton.getBoolean();
        }
        throw new IllegalArgumentException("no boolean value");
    }

    @Override
    public ConfigList clone() {
        final ConfigList cloneList = new ConfigList(source);
        for (final Configuration element : entries) {
            cloneList.entries.add((Configuration) element.clone());
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

    private Configuration getSoleElementIfExists() {
        if (getCount() == 1) {
            return entries.get(0);
        }
        return null;
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