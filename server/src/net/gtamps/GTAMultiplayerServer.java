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
    		Logger.getInstance().log(LogType.SERVER, "configuration loaded: " + CONFIG.getSource());
    		
	        final ISerializer serializer = initSerializer();
	        Logger.getInstance().log(LogType.SERVER, "serializer initialized: " + serializer.toString());
	        
	        final ISocketHandler sockHandler = initSockHandler(serializer);
	        Logger.getInstance().log(LogType.SERVER, "socketHandler initialized: " + sockHandler.toString());
	        
	        final int gameport = CONFIG.select("common.setup.gameserver.port").getInt();
	        gameServer = ServerChainFactory.createServerChain(gameport, sockHandler);
	        Logger.getInstance().log(LogType.SERVER, "server running: " + gameServer.toString());
	        
	        final int httpport = CONFIG.select("common.setup.httpserver.port").getInt();
	        final String docroot = CONFIG.select("common.setup.httpserver.docroot").getString();
	        httpServer = ServerChainFactory.startHTTPServer(httpport, docroot);
	        Logger.getInstance().log(LogType.SERVER, "http server running: " + httpServer.toString());
	      
	//		DBHandler dbHandler = new DBHandler("db/net.net.gtamps");
	//		dbHandler.createPlayer("tom", "mysecretpassword");
	//		dbHandler.authPlayer("tom", "mysecretpassword");
	//		dbHandler.deletePlayer("tom", "mysecretpassword");
	//		dbHandler.authPlayer("tom", "mysecretpassword");
	        
	        CONTROL = ControlCenter.instance;
	        INSTANCE = this;
	        Logger.getInstance().log(LogType.SERVER, "control center initialized: " + CONTROL.toString());
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

	private String exceptionToVerboseString(final Exception e) {
		final int maxTraceDepth = -1;
		final StringBuilder sb =  new StringBuilder().append(e.toString());
		final StackTraceElement[] stack = e.getStackTrace();
		for(int i = 0; (maxTraceDepth < 0 || i < maxTraceDepth) && i < stack.length; i++) {
			sb.append('\n')
			.append(stack[i].toString());
		}
		return sb.toString();
	}

	private static ISocketHandler initSockHandler(final ISerializer serializer) throws IllegalArgumentException, InstantiationException, IllegalAccessException, InvocationTargetException, SecurityException, NoSuchMethodException, ClassNotFoundException {
		@SuppressWarnings("unchecked")
		final Constructor<ISocketHandler> constructor = (Constructor<ISocketHandler>) Class.forName(CONFIG.select("common.setup.gameserver.sockethandler.class").getString()).getConstructor(ISerializer.class);
		return constructor.newInstance(serializer);
	}
    
    private static MergeConfiguration loadConfig(final String path) throws FileNotFoundException, RuntimeException {
		final Configuration loadedConfig = new XMLConfigLoader(ResourceLoader.getFileAsInputStream(path), new ConfigSource(new File(path))).loadConfig();
		final MergeConfiguration config = new MergeConfiguration(new ProtectedMergeStrategy(), loadedConfig);
    	return config;
    }
    
    private static ISerializer initSerializer() throws InstantiationException, IllegalAccessException, ClassNotFoundException {
    	return (ISerializer) Class.forName(CONFIG.select("common.setup.gameserver.serializer.class").getString()).newInstance();
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