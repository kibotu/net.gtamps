package net.gtamps.server;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import net.gtamps.game.IGameThread;
import net.gtamps.shared.communication.Command;
import net.gtamps.shared.communication.ISendable;
import net.gtamps.shared.communication.Message;
import net.gtamps.shared.communication.Request;
import net.gtamps.shared.communication.Response;

public class ControlCenter extends Thread {

	public static final ControlCenter instance = new ControlCenter();
	
	public final BlockingQueue<Message> inbox = new LinkedBlockingQueue<Message>();
	public final BlockingQueue<Message> outbox = new LinkedBlockingQueue<Message>();
	public final BlockingQueue<Response> responsebox = new LinkedBlockingQueue<Response>();
	
	private boolean run = true;
	private Map<String, IGameThread> gameThreads = new HashMap<String, IGameThread>();
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
	
	public void receiveMessage(Message msg) {
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
			if (s != null) {
				sendInMessage(s, response);
			}
		}
	}
	
	private void processOutbox() {
		
	}
	
	private void handleRequest(Session session, Request request) {
		switch (request.type) {
			case SESSION:
			case REGISTER:
			case LOGIN:
			case JOIN:
			case LEAVE:
			case GETMAPDATA:
			case GETPLAYER:
				break;
			case GETUPDATE:
				break;
			default:
				break;
		}
	}
	
	private void handleCommand(Session session, Command command) {
		
	}
	
	private void sendInMessage(Session s, Response r) {
		Message msg = new Message();
		msg.setSessionId(s.getId());
		msg.addSendable(r);
		s.getConnection().send(msg);
	}
	
	private IGameThread getGamethreadForSession(Session s) {
		return gameThreads.get(s.getId());
	}
	
}
