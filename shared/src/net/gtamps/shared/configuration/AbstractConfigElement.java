package net.gtamps.shared.configuration;

import net.gtamps.shared.Utils.Logger;

abstract class AbstractConfigElement implements Configuration {

	private static final long serialVersionUID = -1240349053596166316L;

	protected final static void warnIneffectiveMethod() {
		final String msg = new StringBuffer()
		.append(getCallerInfo(1, true))
		.append(" called ")
		.append(getCallerInfo(0, false))
		.append(", but method will always return 'null' for this configuration")
		.toString();
		System.err.println(msg);
		Logger.W(ConfigBuilder.class.getSimpleName(), msg);
	}

	private final static String getCallerInfo(final int backtrack, final boolean inclLineNo) {
		final StackTraceElement caller = Thread.currentThread().getStackTrace()[3 + backtrack];
		final String fullName = caller.getClassName();
		final String simpleName = fullName.substring(fullName.lastIndexOf('.') + 1, fullName.length());
		final StringBuffer infobuf = new StringBuffer(simpleName).append('.').append(caller.getMethodName());
		if (inclLineNo) {
			infobuf.append(" (line ").append(caller.getLineNumber()).append(")");
		}
		return infobuf.toString();
	}

	protected final Class<?> type;
	protected final ConfigSource source;

	AbstractConfigElement(final Class<?> type, final ConfigSource source) {
		if (type == null) {
			throw new IllegalArgumentException("'type' must not be 'null'");
		}
		if (source == null) {
			throw new IllegalArgumentException("'source' must not be 'null'");
		}
		this.type = type;
		this.source = source;
	}

	@Override
	public Class<?> getType() {
		return this.type;
	}

	@Override
	public ConfigSource getSource() {
		return this.source;
	}

	@Override
	public Configuration get(final String key) {
		warnIneffectiveMethod();
		return null;
	}

	@Override
	public Configuration get(final int index) {
		warnIneffectiveMethod();
		return null;
	}

	@Override
	public String getString() {
		warnIneffectiveMethod();
		return null;
	}

	@Override
	public Integer getInt() {
		warnIneffectiveMethod();
		return null;
	}

	@Override
	public Float getFloat() {
		warnIneffectiveMethod();
		return null;
	}

	@Override
	public Boolean getBoolean() {
		warnIneffectiveMethod();
		return null;
	}

}
