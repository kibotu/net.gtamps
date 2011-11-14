package net.gtamps.shared.configuration;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

final class ConfigListBuilder extends ConfigurationBuilder {

	private final ConfigList configList;
	private final List<ConfigurationBuilder> elements = new ArrayList<ConfigurationBuilder>();
	private int selected = 0;
	private final Class<?> type = List.class;

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
		updateSelected(firstElement);
		this.select(1);
	}

	@Override
	public ConfigurationBuilder select(final String which) {
		int index;
		try {
			index = Integer.parseInt(which);
		} catch (final NumberFormatException e) {
			final String msg = String.format("'which' must be parseable as integer, but is \"%s\"", which);
			throw new IllegalArgumentException(msg, e);
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
			this.elements.add(null);
		}
		return this;
	}

	@Override
	public ConfigurationBuilder get() {
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
	protected ConfigurationBuilder fixBuild() {
		final Iterator<ConfigurationBuilder> iter = this.elements.iterator();
		while (iter.hasNext()) {
			final ConfigurationBuilder b = iter.next();
			if (b==null) {
				iter.remove();
				continue;
			}
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
		if (this.configList.elementCount() == 0) {
			return null;
		} else if (this.configList.elementCount() == 1) {
			return this.configList.get(0);
		} else {
			return this.configList;
		}
	}

	@Override
	protected void updateSelected(final ConfigurationBuilder cb) {
		this.elements.set(this.selected, cb);
	}

	@Override
	protected Class<?> getType() {
		return this.type;
	}
}