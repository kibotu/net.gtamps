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
	
	private boolean run = true;
	private Map<String, IGameThread> gameThreads = new HashMap<String, IGameThread>();
	private Map<String, Request> requestCache = new HashMap<String, Request>();
	
	private ControlCenter() {
		super("ControlCenter");
		this.start();
	}
	
	public void run() {
		while(run) {
			processInbox();
			processOutbox();
		}
	}
	
	public void receiveMessage(Message msg) {
		inbox.add(msg);
	}
	
	public void handleResponse(Response response) {
		
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
	
	private void processOutbox() {
		
	}
	
	private void handleRequest(Session session, Request request) {
		
	}
	
	private void handleCommand(Session session, Command command) {
		
	}
	
}
