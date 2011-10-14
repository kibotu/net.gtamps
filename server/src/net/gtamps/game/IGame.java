package net.gtamps.game;

import net.gtamps.server.Session;
import net.gtamps.shared.communication.Command;
import net.gtamps.shared.communication.Request;

public interface IGame {
	
	public void start();
	public void stop();
	public boolean isRunning();
	public int getId();
	public String getName();
	public void handleRequest(Session s, Request r);
	public void handleCommand(Session s, Command c);
	

}
