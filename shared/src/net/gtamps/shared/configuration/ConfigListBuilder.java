package net.gtamps.shared.configuration;

import java.util.ArrayList;
import java.util.List;

final class ConfigListBuilder extends ConfigBuilder {

	private final Class<?> type = List.class;
	private final List<ConfigBuilder> elements = new ArrayList<ConfigBuilder>();

	ConfigListBuilder(final ConfigSource source, final ConfigBuilder parent) {
		super(source, parent);
	}

	@Override
	public ConfigBuilder addBuilder(final ConfigBuilder cb) {
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

	protected ConfigBuilder select(int index) {
		if (index < 0) {
			//TODO warn
			index = 0;
		}
		if (index >= this.elements.size()) {
			//TODO warn
			index = this.elements.size() - 1;
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
		final ConfigList configList = new ConfigList(this.source);
		for (final ConfigBuilder e : this.elements) {
			final Configuration cfg = e.getBuild();
			if (cfg!= null) {
				configList.addConfiguration(cfg);
			}
		}
		if (configList.elementCount() == 0) {
			return null;
		} else if (configList.elementCount() == 1) {
			return configList.get(0);
		} else {
			return configList;
		}
	}

	@Override
	public Class<?> getType() {
		return this.type;
	}
}