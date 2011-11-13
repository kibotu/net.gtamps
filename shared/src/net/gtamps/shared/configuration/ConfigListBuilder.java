package net.gtamps.shared.configuration;

import java.util.ArrayList;
import java.util.List;

final class ConfigListBuilder extends ConfigurationBuilder {

	private final ConfigList configList;
	private final List<ConfigurationBuilder> elements = new ArrayList<ConfigurationBuilder>();
	private int selected = 0;


	protected ConfigListBuilder(final ConfigSource source) {
		super(source);
		this.configList = new ConfigList(source);
		this.selectElement(0);
	}


	@Override
	public ConfigurationBuilder addConfiguration(final Configuration value) {
		final int oldSelection = this.selected;
		selectElement(this.elements.size());
		final ConfigurationBuilder possiblyNewb = getSelected().addConfiguration(value);
		updateSelected(possiblyNewb);
		selectElement(oldSelection);
		return this;
	}


	@Override
	public ConfigurationBuilder selectElement(final String which) {
		int index;
		try {
			index = Integer.parseInt(which);
		} catch (final NumberFormatException e) {
			throw new IllegalArgumentException("'which' must be parseable as integer", e);
		}
		return selectElement(index);
	}

	public ConfigurationBuilder selectElement(int index) {
		if (index < 0) {
			//TODO warn
			index = 0;
		}
		if (index > this.elements.size()) {
			//TODO warn
			index = this.elements.size();
		}
		this.selected = index;
		if (index == this.elements.size()) {
			this.elements.add(new SingletonConfigBuilder(this.source));
		}
		return this;
	}

	@Override
	public ConfigurationBuilder getSelected() {
		return this.elements.get(this.selected);
	}


	@Override
	protected Configuration getConfigurationElement() {
		// TODO Auto-generated method stub
		return null;
	}

	private void updateSelected(final ConfigurationBuilder cb) {
		this.elements.set(this.selected, cb);
	}

}