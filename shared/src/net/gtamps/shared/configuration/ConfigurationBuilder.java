package net.gtamps.shared.configuration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class ConfigurationBuilder {

	protected final ConfigSource source;
	protected List<ConfigurationBuilder> children = new ArrayList<ConfigurationBuilder>();
	
	public static ConfigMapBuilder buildConfig(ConfigSource source) {
		return new ConfigMapBuilder(source, true);
	}
	
	static final String[] splitAndNormalizeKey(String key) {
		return AbstractConfigElement.splitKey(AbstractConfigElement.normalizeKey(key));
	}

	protected ConfigurationBuilder(ConfigSource source) {
		this.source = source;
	}
	
	public abstract ConfigurationBuilder addConfiguration(String key, Configuration value);

	public ConfigurationBuilder addInt(String key, int value) {
		this.addConfiguration(key, new ConfigLiteralNumber(value, source));
		return this;
	}
	
	public ConfigurationBuilder addFloat(String key, float value) {
		this.addConfiguration(key, new ConfigLiteralNumber(value, source));
		return this;
	}
	
	public ConfigurationBuilder addBool(String key, boolean value) {
		this.addConfiguration(key, new ConfigLiteralBool(value, source));
		return this;
	}
	
	public ConfigurationBuilder addString(String key, String value) {
		this.addConfiguration(key, new ConfigLiteralString(value, source));
		return this;
	}
	
	abstract protected Configuration getConfigurationElement();

	
}

class ConfigMapBuilder extends ConfigurationBuilder {
	
	private final ConfigMap configMap;
	private final Map<String, Integer> childrenMap = new HashMap<String, Integer>();
	
	public ConfigMapBuilder(ConfigSource source) {
		this(source, false);
	}
	
	ConfigMapBuilder(ConfigSource source, boolean isRoot) {
		super(source);
		this.configMap = new ConfigMap(source, isRoot);
	}
	
	@Override
	public ConfigMapBuilder addConfiguration(String key, Configuration value) {
		if (key == null) {
			throw new IllegalArgumentException("'key' must not be 'null'");
		}
		String[] keyPair = splitAndNormalizeKey(key);
		
		ConfigurationBuilder targetBuilder = children.get(childrenMap.get(keyPair[0]));
		if (targetBuilder == null) {
			targetBuilder = this;
		}
		targetBuilder.
		
		
		if (childrenMap.containsKey(keyPair[0])) {
			
		}
		
		
		
		if ("".equals(keyPair[1])) {
			Configuration existing = configMap.get(keyPair[0]);
			if (existing != null) {
				if (ConfigList.class.equals(existing.getClass())) {
					// add to list
				} else {
					// make list
					// add existing to it
					// add value to it
				}
			} else {
				// put value in place for key
			}
		} else {
			
		}
		return this;
	}

	@Override
	protected Configuration getConfigurationElement() {
		return configMap;
	}
	
	
}


class ConfigListBuilder extends ConfigurationBuilder {
	
	private final ConfigList configList;

	protected ConfigListBuilder(ConfigSource source) {
		super(source);
		this.configList = new ConfigList(source);
	}

	@Override
	public ConfigurationBuilder addConfiguration(String key, Configuration value) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected Configuration getConfigurationElement() {
		return configList;
	}
	
	
}