package net.gtamps.server;

import net.gtamps.shared.communication.Response;

public interface IResponseHandler {
	
	public void handleResponse(Session s, Response response);

}
