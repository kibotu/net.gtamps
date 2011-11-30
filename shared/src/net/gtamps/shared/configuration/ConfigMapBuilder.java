package net.gtamps.shared.configuration;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

final class ConfigMapBuilder extends ConfigBuilder {

	private final Class<?> type = Map.class;
	private final Map<String, ConfigBuilder> elements = new HashMap<String, ConfigBuilder>();


	protected ConfigMapBuilder(final AbstractConfigSource source) {
		this(source, null);
	}
	protected ConfigMapBuilder(final AbstractConfigSource source, final ConfigBuilder parent) {
		super(source, parent);
	}

	@Override
	public ConfigBuilder addConfig(final Configuration config) {
		final StringBuilder msgBuilder = new StringBuilder("cannot add a configuration")
		.append(" here: select() an element first.");
		throw new UnsupportedOperationException(msgBuilder.toString());
	}

	@Override
	public int getCount() {
		return elements.size();
	}

	@Override
	public Class<?> getType() {
		return this.type;
	}

	@Override
	public String toString() {
		return new StringBuilder("ConfigMap (")
		.append(this.elements.toString())
		.append(")")
		.toString();
	}

	@Override
	protected ConfigBuilder addBuilder(
			final ConfigBuilder cb) throws UnsupportedOperationException {
		final StringBuilder msgBuilder = new StringBuilder("cannot add ")
		.append(cb == null ? "null" : cb.getType().getSimpleName())
		.append(" here: select() an element first.");
		throw new UnsupportedOperationException(msgBuilder.toString());
	}

	@Override
	protected Configuration getBuild() {
		final ConfigMap configMap = new ConfigMap(source);
		for (final Entry<String, ConfigBuilder> e : this.elements.entrySet()) {
			final Configuration cfg = e.getValue().getBuild();
			if (cfg!= null) {
				configMap.putConfiguration(e.getKey(), cfg);
			}
		}
		return (configMap.getCount() > 0) ? configMap : null;
	}

	@Override
	protected ConfigBuilder select(final ConfigKey ckey) {
		if (ckey == null) {
			throw new IllegalArgumentException("'ckey' must not be 'null'");
		}
		ConfigBuilder selected = this.elements.get(ckey.head);
		if (selected == null) {
			selected = new ConfigListBuilder(this.source, this);
			this.elements.put(ckey.head, selected);
		}
		return selected;
	}

}