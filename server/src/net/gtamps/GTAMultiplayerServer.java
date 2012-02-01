package net.gtamps;

import java.io.File;
import java.io.FileNotFoundException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.UnsupportedLookAndFeelException;

import net.gtamps.server.ControlCenter;
import net.gtamps.server.ISocketHandler;
import net.gtamps.server.ServerChainFactory;
import net.gtamps.server.ServerException;
import net.gtamps.server.ServerHelper;
import net.gtamps.server.db.DBHandler;
import net.gtamps.server.gui.GUILogger;
import net.gtamps.server.gui.GUILoggerToGeneralAdapter;
import net.gtamps.server.gui.LogType;
import net.gtamps.server.gui.ServerGUI;
import net.gtamps.server.http.SimpleHttpServer;
import net.gtamps.server.xsocket.XSocketServer;
import net.gtamps.shared.Utils.Logger;
import net.gtamps.shared.configuration.ConfigBuilder;
import net.gtamps.shared.configuration.ConfigSource;
import net.gtamps.shared.configuration.Configuration;
import net.gtamps.shared.configuration.MergeConfiguration;
import net.gtamps.shared.configuration.ProtectedMergeStrategy;
import net.gtamps.shared.configuration.conversion.XMLConfigLoader;
import net.gtamps.shared.game.ServerData;
import net.gtamps.shared.serializer.communication.ISerializer;

public final class GTAMultiplayerServer {

	private static final String BASE_CONFIG_PATH = "../assets/config/";
	private static final String[] LOAD_CONFIG = {
		"Server.xml",
		"Entities.xml"
	};

	public enum Mode { DEBUG, PRODUCTION }

	public static final boolean DEBUG = true;
	/**
	 * @deprecated use configuration: common.setup.httpserver.docroot
	 */
	@Deprecated
	public static final String DEFAULT_PATH = "../assets/kompilat/";
	@Deprecated
	public static final String DEFAULT_MAP = "tinycity.xml";
	public static final int MAX_LOG_ENTRY_DISPLAY = 20;
	public static final boolean WRITE_SERIALIZED_MESSAGE_DUMPS = false;


	private static GTAMultiplayerServer INSTANCE = null;
	private static ControlCenter CONTROL = null;
	private static Configuration CONFIG = ConfigBuilder.getEmptyConfiguration();

	public static void main(final String[] args){
		GUILogger.getInstance().log(LogType.SERVER, "This is where it all begins.");
		INSTANCE = new GTAMultiplayerServer(DEBUG ? Mode.DEBUG : Mode.PRODUCTION);
	}

	public static Configuration getConfig() {
		return CONFIG;
	}

	public static DBHandler getDBHandler() {
		return INSTANCE.dbHandler;
	}

	private XSocketServer gameServer;
	private SimpleHttpServer httpServer;
	private DBHandler dbHandler;
	private ServerGUI gui;

	private GTAMultiplayerServer(final Mode mode) {
		try {
			Logger.setLogger(new GUILoggerToGeneralAdapter(GUILogger.getInstance()));
			Logger.e("SERVER", "logger adapter works");

			logJavaProperties();

			CONFIG = loadConfig();

			initGUI();

			final ISerializer serializer = initSerializer();
			final ISocketHandler sockHandler = initSockHandler(serializer);
			gameServer = initGameServer(sockHandler);

			httpServer = initHttpServer();
			dbHandler = initDBHandler();

			CONTROL = ControlCenter.instance;
			GUILogger.getInstance().log(LogType.SERVER, "control center initialized: " + CONTROL.toString());
		} catch (final Exception e) {
			GUILogger.getInstance().log(LogType.SERVER, "THE END! emergency shutdown:\n" + exceptionToVerboseString(e));
			XSocketServer.shutdownServer();
			if (httpServer != null) {
				httpServer.stopServer();
			}
			e.printStackTrace();
		} finally {
		}
	}

	private void logJavaProperties() {
		final String vmName = System.getProperty("java.vm.name");
		final String vmVersion = System.getProperty("java.vm.version");
		final String runtimeVersion = System.getProperty("java.runtime.version");
		final String javaProp = vmName + " v. " + vmVersion + "; runtime: " + runtimeVersion;
		System.out.println(javaProp);
		GUILogger.i().log(LogType.SERVER, javaProp);
	}

	private void initGUI() {
		final String lookAndFeel = CONFIG.select("common.setup.gui.lookandfeel.name").getString();
		try {
			for (final LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
				if (lookAndFeel.equalsIgnoreCase(info.getName())) {
					UIManager.setLookAndFeel(info.getClassName());
					break;
				}
			}
		} catch (final ClassNotFoundException e) {
			swallowThrowable(e);
		} catch (final InstantiationException e) {
			swallowThrowable(e);
		} catch (final IllegalAccessException e) {
			swallowThrowable(e);
		} catch (final UnsupportedLookAndFeelException e) {
			swallowThrowable(e);
		}
		gui = new ServerGUI();
		GUILogger.getInstance().log(LogType.SERVER, "server GUI is up.");
	}

	private void swallowThrowable(final Throwable e) {
		Logger.e("SERVER", e);
		e.printStackTrace();
	}

	private DBHandler initDBHandler() throws ServerException {
		final DBHandler dbHandler = new DBHandler("db/gtamps");
		//		assert databasePassesBasicTests(dbHandler);
		return dbHandler;
	}

	@SuppressWarnings("unused")
	private boolean databasePassesBasicTests(final DBHandler dbHandler) {
		assert 0 <= dbHandler.createPlayer("test", "mysecretpassword");
		assert 0 <= dbHandler.authPlayer("test", "mysecretpassword");
		dbHandler.deletePlayer("test", "mysecretpassword");
		assert 0 > dbHandler.authPlayer("test", "mysecretpassword");
		return true;
	}

	private SimpleHttpServer initHttpServer() {
		final int port = CONFIG.select("common.setup.httpserver.port").getInt();
		final String docroot = CONFIG.select("common.setup.httpserver.docroot").getString();
		final SimpleHttpServer srv = ServerChainFactory.startHTTPServer(port, docroot);
		GUILogger.getInstance().log(LogType.SERVER, "http server running: " + srv.toString());
		return srv;
	}

	private XSocketServer initGameServer(final ISocketHandler sockHandler) {
		final int gameport = CONFIG.select("common.setup.gameserver.port").getInt();
		final XSocketServer srv = ServerChainFactory.createServerChain(gameport, sockHandler);
		GUILogger.getInstance().log(LogType.SERVER, "server running: " + srv.toString());
		return srv;
	}

	private String exceptionToVerboseString(final Throwable e) {
		return exceptionToVerboseString(new StringBuilder(), e).toString();
	}

	private StringBuilder exceptionToVerboseString(final StringBuilder sb, final Throwable e) {
		sb.append(e.toString());
		final StackTraceElement[] stack = e.getStackTrace();
		for(int i = 0; i < stack.length; i++) {
			sb.append('\n').append('\t').append(stack[i].toString());
		}
		sb.append('\n');
		final Throwable cause = e.getCause();
		if (cause != null) {
			exceptionToVerboseString(sb, cause);
		}
		return sb;
	}

	private static ISocketHandler initSockHandler(final ISerializer serializer) throws IllegalArgumentException, InstantiationException, IllegalAccessException, InvocationTargetException, SecurityException, NoSuchMethodException, ClassNotFoundException {
		final Configuration sockConfig = CONFIG.select("common.setup.gameserver.sockethandler");
		final Class<ISocketHandler> sockClass = getClassFromConfig(sockConfig, ISocketHandler.class);
		final Constructor<ISocketHandler> constructor = sockClass.getConstructor(ISerializer.class);
		final ISocketHandler sockHandler = constructor.newInstance(serializer);
		GUILogger.getInstance().log(LogType.SERVER, "socketHandler initialized: " + sockHandler.toString());
		return sockHandler;
	}

	private static <T> Class<T> getClassFromConfig(final Configuration config, final Class<T> expectedType) throws ClassNotFoundException {
		Class<T> finalClass;
		try {
			final String className = config.select("class").getString();
			final Class<?> clazz = Class.forName(className);
			if (! expectedType.isAssignableFrom(clazz)) {
				throw new IllegalArgumentException("class referenced in config is not assignable from expected class");
			}
			finalClass = expectedType.getClass().cast(clazz);
		} catch (final IllegalArgumentException e1) {
			try {
				final Configuration typeConfig = getTypeConfig(config);
				finalClass = getClassFromConfig(typeConfig, expectedType);
			} catch (final IllegalArgumentException e2) {
				e2.initCause(e1);
				throw new IllegalArgumentException("expected class information not found in config", e2);
			}
		}
		return finalClass;
	}

	private static Configuration getTypeConfig(final String typename) {
		final Configuration types = CONFIG.select("def").select("type");
		Configuration typeConfig = null;
		for (final Configuration config: types) {
			if (typename.equalsIgnoreCase(config.select("name").getString())) {
				typeConfig = config;
				break;
			}
		}
		return typeConfig;
	}

	private static Configuration getTypeConfig(final Configuration configWithTypeKey) {
		final String typename = configWithTypeKey.select("type").getString();
		return getTypeConfig(typename);
	}

	private static MergeConfiguration loadConfig() throws FileNotFoundException, RuntimeException {
		final MergeConfiguration config = new MergeConfiguration(new ProtectedMergeStrategy());
		Configuration loadedConfig = null;
		for (int i = 0; i < LOAD_CONFIG.length; i++) {
			final String path = BASE_CONFIG_PATH + LOAD_CONFIG[i];
			loadedConfig = new XMLConfigLoader(ResourceLoader.getFileAsInputStream(path), new ConfigSource(new File(path))).loadConfig();
			config.merge(loadedConfig);
		}
		GUILogger.getInstance().log(LogType.SERVER, "configuration loaded: " + config.getSource());
		return config;
	}

	private static ISerializer initSerializer() throws InstantiationException, IllegalAccessException, ClassNotFoundException {
		final Configuration serializerConfig = CONFIG.select("common.setup.gameserver.serializer");
		final Class<ISerializer> serializerClass = getClassFromConfig(serializerConfig, ISerializer.class);
		final ISerializer serializer = serializerClass.newInstance();
		GUILogger.getInstance().log(LogType.SERVER, "serializer initialized: " + serializer.toString());
		return serializer;
	}

	public static ControlCenter getControlCenter() {
		return CONTROL;
	}

	public static ServerData getGameServerData() {
		final XSocketServer srv = INSTANCE.gameServer;
		return new ServerData(srv.getLocalIPAddress(), srv.getLocalPort());
	}

	public static ServerData getHttpServerData() {
		final String httpHost = ServerHelper.getLocalIP();
		final int httpPort = CONFIG.select("common.setup.httpserver.port").getInt();
		return new ServerData(httpHost, httpPort);
	}


}