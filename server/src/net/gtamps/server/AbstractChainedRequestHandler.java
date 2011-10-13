package net.gtamps.server;

import net.gtamps.shared.communication.Request;


public abstract class AbstractChainedRequestHandler implements IRequestHandler {

	protected final IResponseHandler responseHandler;
	protected final IRequestHandler nextInstance;

	public AbstractChainedRequestHandler(IResponseHandler rh) {
		this(rh, null);
	}
	
	public AbstractChainedRequestHandler(IResponseHandler rh, IRequestHandler nextInstance) {
		this.responseHandler = rh;
		this.nextInstance = nextInstance;
	}
	
	@Override
	public void handleRequest(Session s, Request r) {
		if (!handleLocally(s, r) && nextInstance != null) {
			nextInstance.handleRequest(s, r);
		}
	}
	
	protected abstract boolean handleLocally(Session s, Request r); 

}
