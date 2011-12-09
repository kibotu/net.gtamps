package net.gtamps.server.gui;

import net.gtamps.shared.Utils.ILogger;

public class GUILoggerToGeneralAdapter implements ILogger {
	
	private static final LogType DEFAULT_LOGTYPE = LogType.SERVER;
	
	GUILogger guiLogger;
	
	public GUILoggerToGeneralAdapter(final GUILogger guiLogger) {
		if (guiLogger == null) {
			throw new IllegalArgumentException("'guiLogger' must not be 'null'");
		}
		this.guiLogger = guiLogger;
	}
	
	private LogType getLogTypeFromId(final String id) {
		LogType logType;
		try {
			logType = LogType.valueOf(LogType.class, id);
		} catch (final IllegalArgumentException e) {
			System.err.println("unknown id given for LogType: ");
			e.printStackTrace();
			logType = DEFAULT_LOGTYPE;
		}
		return logType;
	}

	@Override
	public void toast(final String id, final String message) {
		guiLogger.log(getLogTypeFromId(id), message);
	}

	@Override
	public void d(final String id, final String message) {
		guiLogger.log(getLogTypeFromId(id), message);
	}

	@Override
	public void v(final String id, final String message) {
		guiLogger.log(getLogTypeFromId(id), message);
	}

	@Override
	public void i(final String id, final String message) {
		guiLogger.log(getLogTypeFromId(id), message);
	}

	@Override
	public void w(final String id, final String message) {
		guiLogger.log(getLogTypeFromId(id), message);
	}

	@Override
	public void e(final String id, final String message) {
		guiLogger.log(getLogTypeFromId(id), message);
	}

	@Override
	public void save(final String filename) {
		throw new UnsupportedOperationException(guiLogger.getClass().getCanonicalName() + "does not support saving");
	}

}
