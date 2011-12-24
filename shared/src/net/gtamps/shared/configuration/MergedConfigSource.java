package net.gtamps.shared.configuration;

import java.util.ArrayList;


public final class MergedConfigSource extends AbstractConfigSource {

    private static final int SOURCES_INITIAL_CAPACITY = 2;
    ArrayList<ConfigSource> sources = null;

    public MergedConfigSource(final AbstractConfigSource base, final AbstractConfigSource other) {
        super("MergedConfigSource", java.util.List.class);
        addAbstractConfigSource(base);
        addAbstractConfigSource(other);
    }

    public MergedConfigSource merge(final AbstractConfigSource otherSource) {
        return new MergedConfigSource(this, otherSource);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((sources == null) ? 0 : sources.hashCode());
        return result;
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (!super.equals(obj)) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final MergedConfigSource other = (MergedConfigSource) obj;
        if (sources == null) {
            if (other.sources != null) {
                return false;
            }
        } else if (!sources.equals(other.sources)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "MergedConfigSource [" + (sources != null ? "sources=" + sources : "") + "]";
    }

    @Override
    public Object clone() {
        return new MergedConfigSource(this, null);
    }

    private void addAbstractConfigSource(final AbstractConfigSource source) {
        if (source == null) {
            return;
        }
        final Class<? extends AbstractConfigSource> sourceClass = source.getClass();
        if (MergedConfigSource.class == sourceClass) {
            addMergedSource((MergedConfigSource) source);
        } else if (ConfigSource.class == sourceClass) {
            addSingleSource((ConfigSource) source);
        } else {
            throw new IllegalArgumentException("unknown source type: " + sourceClass.getCanonicalName());
        }
    }

    private void addSingleSource(final ConfigSource source) {
        createSourceListIfNecessary();
        sources.add(source);
    }

    private void addMergedSource(final MergedConfigSource source) {
        createSourceListIfNecessary();
        sources.ensureCapacity(sources.size() + source.sources.size());
        sources.addAll(source.sources);
    }

    private void createSourceListIfNecessary() {
        if (sources == null) {
            sources = new ArrayList<ConfigSource>(SOURCES_INITIAL_CAPACITY);
        }
    }

}
