package net.gtamps.server;

import net.gtamps.shared.communication.Request;
import net.gtamps.shared.communication.Response;

public class SessionManager extends AbstractChainedRequestHandler {
	
	public SessionManager(IResponseHandler rh, IRequestHandler next) {
		super(rh, next);
	}

	@Override
	protected boolean handleLocally(Session s, Request r) {
		boolean outcome = false;
		if (r.type.equals(Request.Type.SESSION)) {
			if (s == null) {
				s = new Session();
			}
			responseHandler.handleResponse(s, new Response(Response.Status.OK, r));
			outcome = true;
		}
		return outcome;
	}

}
