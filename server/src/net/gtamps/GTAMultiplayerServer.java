package net.gtamps;

import net.gtamps.server.ConnectionManager;
import net.gtamps.server.ServerChainFactory;
import net.gtamps.server.gui.LogType;
import net.gtamps.server.gui.Logger;
import net.gtamps.server.gui.ServerGUI;
import net.gtamps.shared.RandomSharedObject;

public class GTAMultiplayerServer {
	
	public static final boolean DEBUG = true;
    public static final String DEFAULT_PATH = "../assets/kompilat/";
    public static final String DEFAULT_MAP = "tinycity.xml";

	private static int uid = 0;
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Logger.getInstance().log(LogType.SERVER, "This is where it all begins.");
		ConnectionManager cm = ServerChainFactory.createServerChain();
		ServerChainFactory.startHTTPServer(DEFAULT_PATH);
//		DBHandler dbHandler = new DBHandler("db/net.net.gtamps");
//		dbHandler.createPlayer("tom", "mysecretpassword");
//		dbHandler.authPlayer("tom", "mysecretpassword");
//		dbHandler.deletePlayer("tom", "mysecretpassword");
//		dbHandler.authPlayer("tom", "mysecretpassword");
		new ServerGUI(cm);
        RandomSharedObject o = new RandomSharedObject();
        o.i = 45;
        System.out.println(o.i);
	}
	
	/**
	 * @return	a unique integer &gt;= 0
	 */
	public static int getNextUID(){
		return uid++;
	}

}