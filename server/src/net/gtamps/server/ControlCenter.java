package net.gtamps.server;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import net.gtamps.game.GameThread;
import net.gtamps.game.IGame;
import net.gtamps.game.IGameThread;
import net.gtamps.shared.communication.Command;
import net.gtamps.shared.communication.ISendable;
import net.gtamps.shared.communication.Message;
import net.gtamps.shared.communication.Request;
import net.gtamps.shared.communication.Response;

public class ControlCenter extends Thread implements IMessageHandler {

	public static final IMessageHandler instance = new ControlCenter();
	
	public final BlockingQueue<Message> inbox = new LinkedBlockingQueue<Message>();
	public final BlockingQueue<Message> outbox = new LinkedBlockingQueue<Message>();
	public final BlockingQueue<Response> responsebox = new LinkedBlockingQueue<Response>();
	
	private boolean run = true;
	private Map<Integer, IGame> gameThreads = new HashMap<Integer, IGame>();
	private Map<Integer, Session> sessionCache = new HashMap<Integer, Session>();
	
	private ControlCenter() {
		super("ControlCenter");
		this.start();
	}
	
	public void run() {
		while(run) {
			processInbox();
			processResponsebox();
			processOutbox();
		}
	}
	
	/* (non-Javadoc)
	 * @see net.gtamps.server.IMessageHandler#receiveMessage(net.gtamps.server.Connection, net.gtamps.shared.communication.Message)
	 */
	@Override
	public void receiveMessage(Connection c, Message msg) {
		if (msg != null) {
			inbox.add(msg);
		}
	}
	
	public void handleResponse(Response response) {
		if (response != null) {
			responsebox.add(response);
		}
	}
	
	private void processInbox() {
		List<Message> workingCopy = new LinkedList<Message>();
		inbox.drainTo(workingCopy);
		for (Message msg : workingCopy) {
			Session session = SessionManager.instance.getSessionForMessage(msg);
			for (ISendable i : msg.sendables) {
				if (i instanceof Request) {
					handleRequest(session, (Request) i);
				} else if (i instanceof Command) {
					handleCommand(session, (Command) i);
				} else {
					
				}
			}
		}
		workingCopy.clear();
	}
	
	private void processResponsebox() {
		List<Response> workingCopy = new LinkedList<Response>();
		responsebox.drainTo(workingCopy);
		for (Response response : workingCopy) {
			Session s = sessionCache.get(response.requestId);
			sessionCache.remove(response.requestId);
			if (s != null) {
				sendInMessage(s, response);
			}
		}
	}
	
	private void processOutbox() {
		
	}
	
	private void handleRequest(Session session, Request request) {
		sessionCache.put(request.id, session);
		switch (request.type) {
			case SESSION:
			case REGISTER:
			case LOGIN:
				handleUnauthenticatedRequest(session, request);
				break;
			case JOIN:
				handleAuthenticatedRequest(session, request);
				break;
			case LEAVE:
			case GETMAPDATA:
			case GETPLAYER:
			case GETUPDATE:
				handlePlayingRequest(session, request);
				break;
			default:
				break;
		}
	}
	
	private void handleUnauthenticatedRequest(Session s, Request request) {
		switch(request.type) {
			case SESSION:
			case REGISTER:
			case LOGIN:
			default:
				break;
		}
	}

	private void handleAuthenticatedRequest(Session s, Request request) {
		if (!s.isAuthenticated()) {
			Response resp = new Response(Response.Status.NEED, request);
			this.handleResponse(resp);
			return;
		}
		switch(request.type) {
			case JOIN:
			default:
				break;
		}
	}

	private void handlePlayingRequest(Session s, Request request) {
		if (!s.isAuthenticated() || !s.isPlaying()) {
			Response resp = new Response(Response.Status.NEED, request);
			this.handleResponse(resp);
			return;
		}
		s.getGame().handleRequest(s, request);
	}

	
	private void handleCommand(Session s, Command command) {
		if (!s.isAuthenticated() || !s.isPlaying()) {
			return;
		}
		s.getGame().handleCommand(s, command);
	}
	
	private void sendInMessage(Session s, Response r) {
		Message msg = new Message();
		msg.setSessionId(s.getId());
		msg.addSendable(r);
		s.getConnection().send(msg);
	}
	
	private IGame createGame(String mapname) {
//		IGame game = new GameThread();
//		if (game != null) {
//			this.gameThreads.put(game.getId(), game);
//		}
//		return game;
		return null;
	}
	

	
	
}
