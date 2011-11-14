package net.gtamps.shared.configuration;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

final class ConfigMapBuilder extends ConfigurationBuilder {

	private final ConfigMap configMap;
	private final Class<?> type = Map.class;
	private final Map<String, ConfigurationBuilder> elements = new HashMap<String, ConfigurationBuilder>();
	private ConfigKey selected = null;

	public ConfigMapBuilder(final ConfigSource source) {
		super(source);
		this.configMap = new ConfigMap(source);
		this.select("");
	}

	@Override
	public ConfigMapBuilder addSubConfiguration() {
		final ConfigurationBuilder existing = get();
		final ConfigMapBuilder newb = new ConfigMapBuilder(this.source); 
		if (existing == null) {
			this.updateSelected(newb);
		} else {
			ConfigListBuilder listb;
			if (ConfigListBuilder.class.isAssignableFrom(existing.getClass())) {
				listb = (ConfigListBuilder) existing;
			} else {
				listb = new ConfigListBuilder(existing);
			}
			listb.updateSelected(newb);
			listb.select(2);
			this.updateSelected(listb);
		}
		return this;
	}		

	@Override
	public ConfigurationBuilder select(final String which) {
		final ConfigKey ckey = new ConfigKey(which);
		excludeDeepKeys(ckey);

		this.selected = ckey;
		//		if (getSelected() == null) {
		//			updateSelected(new SingletonConfigBuilder(this.source));
		//		}
		return this;
	}

	@Override
	public ConfigurationBuilder get() {
		return this.elements.get(this.selected.head);
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder("ConfigMap (")
		.append(this.fixed ? "fixed): " : "building): ")
		.append(this.fixed ? this.configMap.toString() : this.elements.toString());
		return sb.toString();
	}

	@Override
	protected ConfigurationBuilder fixBuild() {
		for (final Entry<String, ConfigurationBuilder> e : this.elements.entrySet()) {
			final Configuration cfg = e.getValue().fixBuild().getBuild();
			if (cfg != null) {
				this.configMap.putConfiguration(e.getKey(), cfg);
			}
		}
		return this;
	}

	@Override
	protected ConfigurationBuilder unfix() {
		for (final ConfigurationBuilder b : this.elements.values()) { 
			b.unfix();
		}
		this.configMap.clearMap();
		this.fixed = false;
		return this;
	}


	@Override
	protected Configuration getBuild() {
		return (this.configMap.elementCount() > 0) ? this.configMap : null;
	}

	@Override
	protected void updateSelected(final ConfigurationBuilder cb) {
		this.elements.put(this.selected.head, cb);
	}

	private void excludeDeepKeys(final ConfigKey ckey) {
		if ("".equals(ckey.tail)) {
			throw new IllegalArgumentException("key level exceeds 1. deep key selection not implemented (yet): " + ckey);
		}
	}

	@Override
	protected Class<?> getType() {
		return this.type;
	}

}