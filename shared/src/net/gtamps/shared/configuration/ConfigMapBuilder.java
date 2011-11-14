package net.gtamps.shared.configuration;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

final class ConfigMapBuilder extends ConfigurationBuilder {

	private final ConfigMap configMap;
	private ConfigKey selected = null;
	private final Map<String, ConfigurationBuilder> elements = new HashMap<String, ConfigurationBuilder>();

	public ConfigMapBuilder(final ConfigSource source) {
		super(source);
		this.configMap = new ConfigMap(source);
		this.select("");
	}

	@Override
	public ConfigMapBuilder addConfiguration(final Configuration value) {
		final ConfigurationBuilder possiblyNewb = getSelected().addConfiguration(value);
		updateSelected(possiblyNewb);
		return this;
	}		


	@Override
	public ConfigMapBuilder addSubConfiguration() {
		final ConfigurationBuilder existing = getSelected();
		if (existing != null && existing.getBuild() != null) {
			throw new IllegalStateException(this.selected + " has already been initialized to " + existing);
		}
		final ConfigurationBuilder newb = new ConfigMapBuilder(this.source);
		updateSelected(newb);
		return this;
	}		

	@Override
	public ConfigurationBuilder select(final String which) {
		final ConfigKey ckey = new ConfigKey(which);
		excludeDeepKeys(ckey);

		this.selected = ckey;
		if (getSelected() == null) {
			updateSelected(new SingletonConfigBuilder(this.source));
		}
		return this;
	}

	@Override
	public ConfigurationBuilder getSelected() {
		return this.elements.get(this.selected.head);
	}

	private void updateSelected(final ConfigurationBuilder cb) {
		this.elements.put(this.selected.head, cb);
	}

	private void excludeDeepKeys(final ConfigKey ckey) {
		if ("".equals(ckey.tail)) {
			throw new IllegalArgumentException("key level exceeds 1. deep key selection not implemented (yet): " + ckey);
		}
	}

	@Override
	public ConfigurationBuilder fixBuild() {
		for (final Entry<String, ConfigurationBuilder> e : this.elements.entrySet()) {
			final Configuration cfg = e.getValue().fixBuild().getBuild();
			if (cfg != null) {
				this.configMap.putConfiguration(e.getKey(), cfg);
			}
		}
		return this;
	}

	@Override
	public Configuration getBuild() {
		return (this.configMap.elementCount() > 0) ? this.configMap : null;
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder("ConfigMap (")
		.append(this.fixed ? "fixed): " : "building): ")
		.append(this.fixed ? this.configMap.toString() : this.elements.toString());
		return sb.toString();
	}

}