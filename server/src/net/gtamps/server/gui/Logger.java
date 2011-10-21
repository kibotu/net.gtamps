package net.gtamps.server.gui;

import java.util.HashMap;
import java.util.LinkedList;

public class Logger {
	
	private static HashMap<LogType,LinkedList<String>> logmap;
	private static HashMap<LogType, Boolean> updateFlags;
	private static Logger guiLogger = null;
	public static final String lock = "LOCK";
	private boolean activitySend = false;
	private boolean activityReceive= false;
	
	
	private Logger(){
		logmap = new HashMap<LogType,LinkedList<String>>();
		updateFlags = new HashMap<LogType, Boolean>();
		resetUpdateFlags();
		for(final LogType t : LogType.values()){
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
	public void log(final LogType t,final String s){
		synchronized (lock) {
			updateFlags.put(t, true);
			logmap.get(t).add(s);
			
		}
	}
	public void indicateNetworkSendActivity(){
		activitySend = true;
	}
	public void indicateNetworkReceiveActivity(){
		activityReceive = true;
	}
	public boolean getNetworkSendActivity(){
		final boolean retact = activitySend;
		activitySend = false;
		return retact;
	}
	public boolean getNetworkReceiveActivity(){
		final boolean retact = activityReceive;
		activityReceive = false;
		return retact;
	}
	public static LinkedList<String> getLogs(final LogType t){
		synchronized (lock) {
			updateFlags.put(t, false);
			return logmap.get(t);
		}
	}
	public boolean wasUpdated() {
		for (final LogType t : LogType.values()) {
			if (updateFlags.get(t) == true) {
				return true;
			}
		}
		return false;
	}
	public boolean wasUpdated(final LogType type) {
		return updateFlags.get(type);
	}
	private void resetUpdateFlags() {
		for (final LogType type : LogType.values()) {
			updateFlags.put(type, false);
		}
	}
}
