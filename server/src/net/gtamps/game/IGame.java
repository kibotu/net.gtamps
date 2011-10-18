package net.gtamps.game;

import java.util.Collection;

import net.gtamps.server.Session;
import net.gtamps.shared.communication.Command;
import net.gtamps.shared.communication.Request;
import net.gtamps.shared.communication.Response;

public interface IGame {
	
	public void start();
	public void hardstop();
	public boolean isRunning();
	public long getId();
	public String getName();
	public void handleRequest(Session s, Request r);
	public void handleCommand(Session s, Command c);
	void drainResponseQueue(Collection<Response> target);
	

}
