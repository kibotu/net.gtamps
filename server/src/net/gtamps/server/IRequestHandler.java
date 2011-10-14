package net.gtamps.server;

import net.gtamps.shared.communication.Request;

public interface IRequestHandler {
	
	public void handleRequest(Session s, Request r);


}
