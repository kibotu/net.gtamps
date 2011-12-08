package net.gtamps;

import java.io.File;
import java.io.FileNotFoundException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import net.gtamps.server.ControlCenter;
import net.gtamps.server.ISocketHandler;
import net.gtamps.server.ServerChainFactory;
import net.gtamps.server.ServerHelper;
import net.gtamps.server.gui.LogType;
import net.gtamps.server.gui.Logger;
import net.gtamps.server.gui.ServerGUI;
import net.gtamps.server.http.SimpleHttpServer;
import net.gtamps.server.xsocket.XSocketServer;
import net.gtamps.shared.configuration.ConfigBuilder;
import net.gtamps.shared.configuration.ConfigSource;
import net.gtamps.shared.configuration.Configuration;
import net.gtamps.shared.configuration.MergeConfiguration;
import net.gtamps.shared.configuration.ProtectedMergeStrategy;
import net.gtamps.shared.configuration.conversion.XMLConfigLoader;
import net.gtamps.shared.game.ServerData;
import net.gtamps.shared.serializer.communication.ISerializer;

public final class GTAMultiplayerServer {
	
	private static final String BASE_CONFIG_PATH = "../assets/config/Server.xml";

	public enum Mode { DEBUG, PRODUCTION }

    public static final boolean DEBUG = true;
    /**
     * @deprecated use configuration: common.setup.httpserver.docroot
     */
    @Deprecated
    public static final String DEFAULT_PATH = "../assets/kompilat/";
    public static final String DEFAULT_MAP = "tinycity.xml";
    

    private static GTAMultiplayerServer INSTANCE = null;
    private static ControlCenter CONTROL = null;
    private static Configuration CONFIG = ConfigBuilder.getEmptyConfiguration();

    public static void main(final String[] args){
        Logger.getInstance().log(LogType.SERVER, "This is where it all begins.");
        new GTAMultiplayerServer(DEBUG ? Mode.DEBUG : Mode.PRODUCTION);
    }
    
    public static Configuration getConfig() {
    	return CONFIG;
    }
    
    private XSocketServer gameServer;
    private SimpleHttpServer httpServer;
    
    private GTAMultiplayerServer(final Mode mode) {
    	try {
    		new ServerGUI();
    		Logger.getInstance().log(LogType.SERVER, "server GUI is up.");

    		CONFIG = loadConfig(BASE_CONFIG_PATH);
    		
	        final ISerializer serializer = initSerializer();
	        final ISocketHandler sockHandler = initSockHandler(serializer);
	        gameServer = initGameServer(sockHandler);
	        
	        httpServer = initHttpServer();
	      
	//		DBHandler dbHandler = new DBHandler("db/net.net.gtamps");
	//		dbHandler.createPlayer("tom", "mysecretpassword");
	//		dbHandler.authPlayer("tom", "mysecretpassword");
	//		dbHandler.deletePlayer("tom", "mysecretpassword");
	//		dbHandler.authPlayer("tom", "mysecretpassword");
	        
	        CONTROL = ControlCenter.instance;
	        Logger.getInstance().log(LogType.SERVER, "control center initialized: " + CONTROL.toString());
	        INSTANCE = this;
    	} catch (final Exception e) {
    		Logger.getInstance().log(LogType.SERVER, "THE END! emergency shutdown: " + exceptionToVerboseString(e));
    		XSocketServer.shutdownServer();
    		if (httpServer != null) {
    			httpServer.stopServer();
    		}
			e.printStackTrace();
		} finally {
    	}
    }

	private SimpleHttpServer initHttpServer() {
        final int port = CONFIG.select("common.setup.httpserver.port").getInt();
        final String docroot = CONFIG.select("common.setup.httpserver.docroot").getString();
		final SimpleHttpServer srv = ServerChainFactory.startHTTPServer(port, docroot);
		Logger.getInstance().log(LogType.SERVER, "http server running: " + srv.toString());
		return srv;
	}

	private XSocketServer initGameServer(final ISocketHandler sockHandler) {
		final int gameport = CONFIG.select("common.setup.gameserver.port").getInt();
		final XSocketServer srv = ServerChainFactory.createServerChain(gameport, sockHandler);
		Logger.getInstance().log(LogType.SERVER, "server running: " + srv.toString());
		return srv;
	}

	private String exceptionToVerboseString(final Exception e) {
		final StringBuilder sb =  new StringBuilder().append(e.toString());
		final StackTraceElement[] stack = e.getStackTrace();
		for(int i = 0; i < stack.length; i++) {
			sb.append('\n')
			.append(stack[i].toString());
		}
		return sb.toString();
	}

	private static ISocketHandler initSockHandler(final ISerializer serializer) throws IllegalArgumentException, InstantiationException, IllegalAccessException, InvocationTargetException, SecurityException, NoSuchMethodException, ClassNotFoundException {
		@SuppressWarnings("unchecked")
		final Constructor<ISocketHandler> constructor = (Constructor<ISocketHandler>) Class.forName(CONFIG.select("common.setup.gameserver.sockethandler.class").getString()).getConstructor(ISerializer.class);
		final ISocketHandler sockHandler = constructor.newInstance(serializer);
		Logger.getInstance().log(LogType.SERVER, "socketHandler initialized: " + sockHandler.toString());
		return sockHandler;
	}
    
    private static MergeConfiguration loadConfig(final String path) throws FileNotFoundException, RuntimeException {
		final Configuration loadedConfig = new XMLConfigLoader(ResourceLoader.getFileAsInputStream(path), new ConfigSource(new File(path))).loadConfig();
		final MergeConfiguration config = new MergeConfiguration(new ProtectedMergeStrategy(), loadedConfig);
		Logger.getInstance().log(LogType.SERVER, "configuration loaded: " + config.getSource());
    	return config;
    }
    
    private static ISerializer initSerializer() throws InstantiationException, IllegalAccessException, ClassNotFoundException {
    	final ISerializer serializer = (ISerializer) Class.forName(CONFIG.select("common.setup.gameserver.serializer.class").getString()).newInstance();
    	Logger.getInstance().log(LogType.SERVER, "serializer initialized: " + serializer.toString());
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