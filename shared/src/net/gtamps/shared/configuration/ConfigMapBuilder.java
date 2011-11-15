package net.gtamps.shared.configuration;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

final class ConfigMapBuilder extends ConfigBuilder {

	private final Class<?> type = Map.class;
	private final Map<String, ConfigBuilder> elements = new HashMap<String, ConfigBuilder>();

	protected ConfigMapBuilder(final ConfigSource source) {
		super(source, null);
	}
	protected ConfigMapBuilder(final ConfigSource source, final ConfigBuilder parent) {
		super(source, parent);
	}

	@Override
	public String toString() {
		return new StringBuilder("ConfigMap (")
		.append(this.elements.toString())
		.append(")")
		.toString();
	}

	@Override
	protected ConfigBuilder select(final ConfigKey ckey) {
		ConfigBuilder selected = this.elements.get(ckey.head);
		if (selected == null) {
			selected = new ConfigListBuilder(this.source, this);
			this.elements.put(ckey.head, selected);
		}
		return selected;
	}

	@Override
	protected Configuration getBuild() {
		final ConfigMap configMap = new ConfigMap(this.source);
		for (final Entry<String, ConfigBuilder> e : this.elements.entrySet()) {
			final Configuration cfg = e.getValue().getBuild();
			if (cfg!= null) {
				configMap.putConfiguration(e.getKey(), cfg);
			}
		}
		return (configMap.elementCount() > 0) ? configMap : null;
	}

	@Override
	protected Class<?> getType() {
		return this.type;
	}

	@Override
	protected ConfigBuilder addBuilder(
			final ConfigBuilder cb) throws UnsupportedOperationException {
		final StringBuilder msgBuilder = new StringBuilder("cannot add ")
		.append(cb.getType().getSimpleName())
		.append(" here: select() an element first.");
		throw new UnsupportedOperationException(msgBuilder.toString());
	}

}