package net.gtamps;

import net.gtamps.server.ControlCenter;
import net.gtamps.server.ISocketHandler;
import net.gtamps.server.ServerChainFactory;
import net.gtamps.server.gui.LogType;
import net.gtamps.server.gui.Logger;
import net.gtamps.server.gui.ServerGUI;
import net.gtamps.server.xsocket.LengthEncodedTCPSocketHandler;
import net.gtamps.shared.serializer.communication.ISerializer;
import net.gtamps.shared.serializer.communication.ObjectSerializer;

public class GTAMultiplayerServer {
	
	public static final boolean DEBUG = true;
    public static final String DEFAULT_PATH = "../assets/kompilat/";
    public static final String DEFAULT_MAP = "tinycity.xml";

    public static final ISerializer SERIALIZER = new ObjectSerializer();
    public static final ISocketHandler SOCK_HANDLER = new LengthEncodedTCPSocketHandler<ISerializer>(SERIALIZER);
    
//    public static final ISerializer SERIALIZER = new ManualTypeSerializer();
//    public static final ISocketHandler SOCK_HANDLER = new LineBasedTCPSocketHandler<ISerializer>(SERIALIZER);

    private static int uid = 0;
	
	/**
	 * @param args
	 */
	public static void main(final String[] args) {
		Logger.getInstance().log(LogType.SERVER, "This is where it all begins.");
		ServerChainFactory.createServerChain(SOCK_HANDLER);
		ServerChainFactory.startHTTPServer(DEFAULT_PATH);
//		DBHandler dbHandler = new DBHandler("db/net.net.gtamps");
//		dbHandler.createPlayer("tom", "mysecretpassword");
//		dbHandler.authPlayer("tom", "mysecretpassword");
//		dbHandler.deletePlayer("tom", "mysecretpassword");
//		dbHandler.authPlayer("tom", "mysecretpassword");
		//new ServerGUI(cm);
		new ServerGUI();
		
		//TODO tmp
		final ControlCenter cc = ControlCenter.instance;
		cc.toString();
	}
	
	/**
	 * @return	a unique integer &gt;= 0
	 */
	public static int getNextUID(){
		return uid++;
	}
}