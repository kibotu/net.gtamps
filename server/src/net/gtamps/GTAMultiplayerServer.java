package net.gtamps;

import java.io.File;
import java.io.FileNotFoundException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import net.gtamps.server.ControlCenter;
import net.gtamps.server.ISocketHandler;
import net.gtamps.server.ServerChainFactory;
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
import net.gtamps.shared.serializer.communication.ISerializer;

public final class GTAMultiplayerServer {
	
	public enum Mode { DEBUG, PRODUCTION }

    public static final boolean DEBUG = true;
    public static final String DEFAULT_PATH = "../assets/kompilat/";
    public static final String DEFAULT_MAP = "tinycity.xml";

    private static Configuration CONFIG = ConfigBuilder.getEmptyConfiguration();
//    public static final ISerializer SERIALIZER = initSerializer();
//    public static final ISocketHandler SOCK_HANDLER = new LengthEncodedTCPSocketHandler<ISerializer>(SERIALIZER);

//    public static final ISerializer SERIALIZER = new ManualTypeSerializer();
//    public static final ISocketHandler SOCK_HANDLER = new LineBasedTCPSocketHandler<ISerializer>(SERIALIZER);

    public static void main(final String[] args){
        Logger.getInstance().log(LogType.SERVER, "This is where it all begins.");
        new GTAMultiplayerServer(DEBUG ? Mode.DEBUG : Mode.PRODUCTION);
    }
    
    public static Configuration getConfig() {
    	return CONFIG;
    }
    
    @SuppressWarnings("unused")
	private GTAMultiplayerServer(final Mode mode) {
    	XSocketServer gameServer = null;
    	final SimpleHttpServer httpServer = null;
    	try {
    		new ServerGUI();
    		Logger.getInstance().log(LogType.SERVER, "server GUI is up.");
    		CONFIG = loadConfig("../assets/config/Server.xml");
    		Logger.getInstance().log(LogType.SERVER, "configuration loaded: " + CONFIG.getSource());
	        final ISerializer serializer = initSerializer();
	        Logger.getInstance().log(LogType.SERVER, "serializer initialized: " + serializer.toString());
	        final ISocketHandler sockHandler = initSockHandler(serializer);
	        Logger.getInstance().log(LogType.SERVER, "socketHandler initialized: " + sockHandler.toString());
	        gameServer = ServerChainFactory.createServerChain(sockHandler);
	        Logger.getInstance().log(LogType.SERVER, "server running: " + gameServer.toString());
	//      httpServer = ServerChainFactory.startHTTPServer(DEFAULT_PATH);
	//		DBHandler dbHandler = new DBHandler("db/net.net.gtamps");
	//		dbHandler.createPlayer("tom", "mysecretpassword");
	//		dbHandler.authPlayer("tom", "mysecretpassword");
	//		dbHandler.deletePlayer("tom", "mysecretpassword");
	//		dbHandler.authPlayer("tom", "mysecretpassword");
	        
	        final ControlCenter cc = ControlCenter.instance;
	        Logger.getInstance().log(LogType.SERVER, "control center initialized: " + cc.toString());
    	} catch (final Exception e) {
    		XSocketServer.shutdownServer();
    		if (httpServer != null) {
    			httpServer.stopServer();
    		}
			e.printStackTrace();
		} finally {
    	}
    }

	private static ISocketHandler initSockHandler(final ISerializer serializer) throws IllegalArgumentException, InstantiationException, IllegalAccessException, InvocationTargetException, SecurityException, NoSuchMethodException, ClassNotFoundException {
		final Constructor<ISocketHandler> constructor = (Constructor<ISocketHandler>) Class.forName(CONFIG.select("server.setup.tcp.sockethandler.class").getString()).getConstructor(ISerializer.class);
		return constructor.newInstance(serializer);
	}
    
    private static MergeConfiguration loadConfig(final String path) throws FileNotFoundException, RuntimeException {
		final Configuration loadedConfig = new XMLConfigLoader(ResourceLoader.getFileAsInputStream(path), new ConfigSource(new File(path))).loadConfig();
		final MergeConfiguration config = new MergeConfiguration(new ProtectedMergeStrategy(), loadedConfig);
    	return config;
    }
    
    private static ISerializer initSerializer() throws InstantiationException, IllegalAccessException, ClassNotFoundException {
    	return (ISerializer) Class.forName(CONFIG.select("server.setup.tcp.serializer.class").getString()).newInstance();
    }

}