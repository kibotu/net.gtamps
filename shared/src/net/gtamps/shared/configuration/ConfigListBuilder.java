package net.gtamps.shared.configuration;

import java.util.ArrayList;
import java.util.List;

final class ConfigListBuilder extends ConfigBuilder {

    private final Class<?> type = List.class;
    private final List<ConfigBuilder> elements = new ArrayList<ConfigBuilder>();
    private final ConfigList additionalConfigs;

    ConfigListBuilder(final AbstractConfigSource source, final ConfigBuilder parent) {
        super(source, parent);
        additionalConfigs = new ConfigList(source);
    }

    @Override
    protected ConfigBuilder addBuilder(final ConfigBuilder cb) {
        if (parent != null && cb == parent) {
            throw new IllegalArgumentException("trying to add this builder's parent as child builder. no circles allowed in this list builder!");
        }
        this.elements.add(cb);
        return this;
    }

    @Override
    protected ConfigBuilder select(final ConfigKey ckey) {
        int index;
        try {
            index = Integer.parseInt(ckey.head);
        } catch (final NumberFormatException e) {
            final String msg = String.format("'key' must be parseable as integer, but is \"%s\"", ckey.head);
            throw new IllegalArgumentException(msg, e);
        }
        return select(index);
    }

    protected ConfigBuilder select(final int index) {
        if (index < 0) {
            throw new IllegalArgumentException("index must be >= 0, but is " + index);
        }
        if (index >= this.elements.size()) {
            throw new IndexOutOfBoundsException("index (" + index + ") out of bounds, list size is: " + elements.size());
        }
        return this.elements.get(index);
    }

    @Override
    public String toString() {
        return new StringBuilder("ConfigList")
                .append(this.elements.toString())
                .append(")")
                .toString();
    }

    @Override
    protected Configuration getBuild() {
        final ConfigList configList = new ConfigList(source, additionalConfigs);
        for (final ConfigBuilder e : this.elements) {
            final Configuration cfg = e.getBuild();
            if (cfg != null) {
                configList.addConfiguration(cfg);
            }
        }
        if (configList.getCount() == 0) {
            return null;
        } else if (configList.getCount() == 1) {
            return configList.get(0);
        } else {
            return configList;
        }
    }

    @Override
    public Class<?> getType() {
        return this.type;
    }

    @Override
    public ConfigBuilder addConfig(final Configuration config) {
        if (config == null) {
            return this;
        }
        additionalConfigs.addConfiguration(config);
        return this;
    }

    @Override
    public int getCount() {
        return additionalConfigs.size() + elements.size();
    }
}