package net.gtamps.server.gui;

import java.util.HashMap;
import java.util.LinkedList;

public class Logger {
	
	private static HashMap<LogType,LinkedList<String>> logmap;
	private static Logger guiLogger = null;
	public static final String lock = "LOCK";
	private static boolean wasUpdated = false;
	private boolean activitySend = false;
	private boolean activityReceive= false;
	
	
	private Logger(){
		logmap = new HashMap<LogType,LinkedList<String>>();
		for(LogType t : LogType.values()){
			logmap.put(t, new LinkedList<String>());
		}
	}
	public static Logger getInstance(){
		if(guiLogger == null){
			guiLogger = new Logger();
		}
		return guiLogger;
	}
	public static Logger i(){
		return getInstance();
	}
	public void log(LogType t,String s){
		synchronized (lock) {
			wasUpdated = true;
			logmap.get(t).add(s);
			
		}
	}
	public void indicateNetworkSendActivity(){
		this.activitySend = true;
	}
	public void indicateNetworkReceiveActivity(){
		this.activityReceive = true;
	}
	public boolean getNetworkSendActivity(){
		boolean retact = this.activitySend;
		this.activitySend = false;
		return retact;
	}
	public boolean getNetworkReceiveActivity(){
		boolean retact = this.activityReceive;
		this.activityReceive = false;
		return retact;
	}
	public static LinkedList<String> getLogs(LogType t){
		synchronized (lock) {
			wasUpdated = false;
			return logmap.get(t);		
		}
	}
	public boolean wasUpdated() {
		return wasUpdated;
	}
}
