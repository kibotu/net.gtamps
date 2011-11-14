package net.gtamps.shared.configuration;

import java.util.ArrayList;
import java.util.List;

final class ConfigListBuilder extends ConfigurationBuilder {

	private final ConfigList configList;
	private final List<ConfigurationBuilder> elements = new ArrayList<ConfigurationBuilder>();
	private int selected = 0;
	private Class<? extends Configuration> listType = null;

	ConfigListBuilder(final ConfigSource source) {
		super(source);
		this.configList = new ConfigList(source);
		this.select(0);
	}

	/**
	 * Creates a new configListBuilder and adds {@code firstElement} to it. After this method
	 * completed, index 1 (spot for next element) will be selected. 
	 */
	ConfigListBuilder(final ConfigurationBuilder firstElement) {
		this(firstElement.source);
		this.listType = firstElement.getBuild().getClass();
		updateSelected(firstElement);
		this.select(1);
	}

	@Override
	public ConfigurationBuilder select(final String which) {
		int index;
		try {
			index = Integer.parseInt(which);
		} catch (final NumberFormatException e) {
			throw new IllegalArgumentException("'which' must be parseable as integer", e);
		}
		return select(index);
	}

	public ConfigurationBuilder select(int index) {
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
	public String toString() {
		final StringBuilder sb = new StringBuilder("ConfigList")
		.append(this.fixed ? " (fixed): " : " (building): ")
		.append(this.fixed ? this.configList.toString() : this.elements.toString());
		return sb.toString();
	}
	@Override
	protected ConfigurationBuilder addConfiguration(final Configuration value) {
		if (value == null) {
			//TODO warn
			return this;
		}
		typeCheck(value);
		final int oldSelection = this.selected;
		select(this.elements.size());
		final ConfigurationBuilder possiblyNewb = getSelected().addConfiguration(value);
		updateSelected(possiblyNewb);
		select(oldSelection);
		return this;
	}

	@Override
	protected ConfigurationBuilder fixBuild() {
		for (final ConfigurationBuilder b : this.elements) {
			final Configuration cfg = b.fixBuild().getBuild();
			if (cfg != null) {
				this.configList.addConfiguration(cfg);
			}
		}
		return this;
	}

	@Override
	protected ConfigurationBuilder unfix() {
		for (final ConfigurationBuilder b : this.elements) {
			b.unfix();
		}
		this.configList.clearList();
		this.fixed = false;
		return this;
	}

	@Override
	protected Configuration getBuild() {
		for (final ConfigurationBuilder b : this.elements) {
			final Configuration cfg = b.getBuild();
			if (cfg != null) {
				this.configList.addConfiguration(cfg);
			}
		}
		return (this.configList.elementCount() > 0) ? this.configList : null;
	}

	void updateSelected(final ConfigurationBuilder cb) {
		this.elements.set(this.selected, cb);
	}

	private void typeCheck(final Configuration value) throws IllegalArgumentException {
		if (this.listType == null) {
			this.listType = value.getClass();
		} else {
			if (value.getClass() != this.listType) {
				throw new IllegalArgumentException("wrong type: "+ value.getClass().getCanonicalName() + "this list is initialized for " + this.listType.getCanonicalName());
			}
		}
	}
}