package net.gtamps.shared.configuration;

import java.io.File;
import java.net.URL;

public final class ConfigSource extends AbstractConfigSource {

	public static final ConfigSource EMPTY_SOURCE = new ConfigSource();

	private ConfigSource(){
		super("", java.lang.String.class);
	}

	public ConfigSource(final ConfigSource other) {
		super(other.name, other.type);
	}

	/**
	 * @param url	not {@code null}
	 */
	public ConfigSource(final URL url) {
		super(url.getRef() != null ? 
				url.toString().substring(url.toString().lastIndexOf(url.getRef())) :
					url.toString(),
					URL.class);
	}

	/**
	 * @param file	not {@code null}
	 */
	public ConfigSource(final File file) {
		super(file.getAbsolutePath(), File.class);
	}

	/**
	 * @param name	not {@code null}, not empty
	 */
	public ConfigSource(final String name) {
		super(name, java.lang.String.class);
		if (name == null) {
			throw new NullPointerException("'name' must not be 'null'");
		} else if ("".equals(name)) {
			throw new IllegalArgumentException("'name' must not be empty");
		}
	}

	@Override
	public String toString() {
		return new StringBuilder()
		.append("ConfigSource [")
		.append(type.getSimpleName())
		.append(": ")
		.append(name == null  || this.equals(EMPTY_SOURCE)? "EMPTY" : name)
		.append("]")
		.toString();
	}

	@Override
	public Object clone() {
		return new ConfigSource(this);
	}


}
