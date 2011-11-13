package net.gtamps.shared.configuration;

import java.util.HashMap;
import java.util.Map;

final class ConfigMapBuilder extends ConfigurationBuilder {

	private final ConfigMap configMap;
	private ConfigKey selected = null;
	private final Map<String, ConfigurationBuilder> elements = new HashMap<String, ConfigurationBuilder>();

	public ConfigMapBuilder(final ConfigSource source) {
		this(source, false);
	}

	ConfigMapBuilder(final ConfigSource source, final boolean isRoot) {
		super(source);
		this.configMap = new ConfigMap(source, isRoot);
		this.selectElement("");
	}


	@Override
	public ConfigMapBuilder addConfiguration(final Configuration value) {
		final ConfigurationBuilder possiblyNewb = getSelected().addConfiguration(value);
		updateSelected(possiblyNewb);
		return this;
	}		


	@Override
	protected Configuration getConfigurationElement() {
		return this.configMap.elementCount() > 0 ? this.configMap : null;
	}

	@Override
	public ConfigurationBuilder selectElement(final String which) {
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


}