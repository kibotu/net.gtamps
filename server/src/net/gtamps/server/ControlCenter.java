package net.gtamps.server;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import net.gtamps.game.IGameThread;
import net.gtamps.shared.communication.ISendable;

public class ControlCenter extends Thread {

	public static final ControlCenter instance = new ControlCenter();
	
	public final BlockingQueue<ISendable> inbox = new LinkedBlockingQueue<ISendable>();
	public final BlockingQueue<ISendable> outbox = new LinkedBlockingQueue<ISendable>();
	
	private boolean run = true;
	private List<ISendable> workingCopy = new LinkedList<ISendable>();
	private Map<String, IGameThread> gameThreads = new HashMap<String, IGameThread>();
	
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
	
	private void processInbox() {
		inbox.drainTo(workingCopy);
		for (ISendable i : workingCopy) {
			// do something
		}
		workingCopy.clear();
	}
	
	private void processOutbox() {
		
	}
	
	private startGame
	
}
