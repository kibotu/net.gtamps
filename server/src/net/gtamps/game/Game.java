package net.gtamps.game;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import net.gtamps.GTAMultiplayerServer;
import net.gtamps.ResourceLoader;
import net.gtamps.game.entity.EntityManager;
import net.gtamps.game.event.EventManager;
import net.gtamps.game.physics.Box2DEngine;
import net.gtamps.game.player.PlayerManager;
import net.gtamps.game.world.MapParser;
import net.gtamps.game.world.World;
import net.gtamps.server.Session;
import net.gtamps.server.gui.LogType;
import net.gtamps.server.gui.Logger;
import net.gtamps.shared.communication.Command;
import net.gtamps.shared.communication.ISendable;
import net.gtamps.shared.communication.Request;
import net.gtamps.shared.communication.Response;
import net.gtamps.shared.game.entity.Entity;
import net.gtamps.shared.game.event.EventType;
import net.gtamps.shared.game.event.GameEvent;
import net.gtamps.shared.game.player.Player;

import org.jdom.Element;
import org.jdom.JDOMException;


/**
 *	the game, as it happens on the server.
 *
 * @author jan, tom, til
 *
 */
public class Game extends Thread implements IGame {
	private static final LogType TAG = LogType.GAMEWORLD;
	private static final long EVENT_TIMEOUT = 30;
	private static final long THREAD_UPDATE_SLEEP_TIME = 20;
	private static final int PHYSICS_ITERATIONS = 20;
	
	private static int instanceCounter = 0;

	// start with value > 0
	//private long currentRevisionId = RevisionKeeper.START_REVISION;
//	private RevisionKeeper revisionKeeper = new RevisionKeeper(this);
	
	private class MessagePair<T extends ISendable> {
		public T sendable;
		public Session session;
		public MessagePair(T sendable, Session session) {
			this.sendable = sendable;
			this.session = session;
		}
	}
	
	private final BlockingQueue<MessagePair<Request>> requestQueue = new LinkedBlockingQueue<MessagePair<Request>>();
	private final BlockingQueue<MessagePair<Command>> commandQueue = new LinkedBlockingQueue<MessagePair<Command>>();
	
	private boolean isRunning = false;
	private boolean run = true;
	
	private EventManager eventManager;
	private PlayerManager playerManager;
	private EntityManager entityManager;
	private World world;
	private Box2DEngine physics;
	
	//temp
	private int lastPlayerId = -1;

	private long lastTime = System.nanoTime(); 

	public Game(String mapPathOrNameOrWhatever) {
		super("GameThread" + (++Game.instanceCounter));
		if (mapPathOrNameOrWhatever == null || mapPathOrNameOrWhatever.isEmpty()) {
			mapPathOrNameOrWhatever = GTAMultiplayerServer.DEFAULT_PATH+GTAMultiplayerServer.DEFAULT_MAP;
		}
		Element mapXML;
			try {
				mapXML = ResourceLoader.getFileAsXml(mapPathOrNameOrWhatever);
			} catch (JDOMException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return;
			}
		MapParser mapParser = new MapParser(mapXML);
		{
			world = mapParser.getWorld();
			physics = mapParser.getPhysics();
			eventManager = new EventManager(EVENT_TIMEOUT);
			entityManager = new EntityManager(world);
			playerManager = new PlayerManager(world, entityManager);
			
			eventManager.addEventListener(EventType.ACTION_EVENT, playerManager);
			eventManager.addEventListener(EventType.SESSION_EVENT, entityManager);
			eventManager.addEventListener(EventType.SESSION_EVENT, playerManager);
			playerManager.addEventListener(EventType.PLAYER_EVENT, eventManager);
			entityManager.addEventListener(EventType.ENTITY_EVENT, eventManager);
			
			mapParser.populateWorld(entityManager);
			
			eventManager.dispatchEvent(new GameEvent(EventType.SESSION_STARTS, world));
			this.start();
		}
	}
	
	
	public Game() {
		this(null);
		Logger.i().log(LogType.GAMEWORLD, "Starting new Game!");
		//just something big.
//		physics = new Box2DEngine(-1000f,-1000f,1000f,1000f);
//		world = WorldFactory.createMap(physics);
	//	players = new HashMap<Player, Boolean>();
	}

	
	
	private Iterable<Entity> getUpdates(long revisionId) {
		ArrayList<Entity> updates = new ArrayList<Entity>();
		if (lastPlayerId >=0) {
			updates.add(playerManager.getPlayer(lastPlayerId).getEntity());
		}
		return updates;
	}

	
	


	
	@Override
	public void run() {
		while(run) {
			float timeElapsedInSeconds = (float) ((System.nanoTime()-lastTime)/1000000000.0);
			long timeElapsedPhysicsCalculation = System.nanoTime();
			lastTime = System.nanoTime();
			this.physics.step(timeElapsedInSeconds, PHYSICS_ITERATIONS);
			this.eventManager.dispatchEvent(new GameEvent(EventType.SESSION_UPDATE, world));
			timeElapsedPhysicsCalculation = (System.nanoTime()-lastTime)/1000000;

			//for fps debugging
//			lastUpdate += timeElapsedInSeceonds;
//			updates++;
//			if(lastUpdate>5f){
//				Logger.i().log(LogType.PHYSICS, "Physics fps: "+((updates/lastUpdate)));
//				lastUpdate = 0f;
//				updates = 0;
//			}
			try {
				if(THREAD_UPDATE_SLEEP_TIME-timeElapsedPhysicsCalculation>0){
					Thread.sleep(THREAD_UPDATE_SLEEP_TIME-timeElapsedPhysicsCalculation);
					//System.out.println(this);
				}
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				//e.printStackTrace();
			}
		}
	}


	@Override
	public boolean isRunning() {
		// TODO Auto-generated method stub
		return false;
	}


	@Override
	public void handleRequest(Session s, Request r) {
		if (s == null) {
			throw new IllegalArgumentException("'s' must not be null");
		}
		if (r == null) {
			throw new IllegalArgumentException("'r' must not be null");
		}
		this.requestQueue.add(new MessagePair<Request>(r, s));
	}


	@Override
	public void handleCommand(Session s, Command c) {
		if (s == null) {
			throw new IllegalArgumentException("'s' must not be null");
		}
		if (c == null) {
			throw new IllegalArgumentException("'c' must not be null");
		}
		this.commandQueue.add(new MessagePair<Command>(c, s));
	}

	private void processCommandQueue() {
		List<MessagePair<Command>> commandPairs = new LinkedList<MessagePair<Command>>();
		this.commandQueue.drainTo(commandPairs);
		for (MessagePair<Command> pair : commandPairs) {
			int puid = getPlayerUid(pair.session);
			this.command(puid, pair.sendable);
		}
		commandPairs.clear();
	}
	
	private void processRequestQueue() {
		List<MessagePair<Request>> requestPairs = new LinkedList<MessagePair<Request>>();
		this.requestQueue.drainTo(requestPairs);
		for (MessagePair<Request> pair: requestPairs) {
			int puid = getPlayerUid(pair.session);
			Response response = null;
			switch(pair.sendable.type) {
				case JOIN:
					response = joinPlayer(session, pair.sendable); 
					break;
				case LEAVE: 
					response = leavePlayer(puid, pair.sendable); 
					break;
				case GETMAPDATA: break;
				case GETPLAYER: break;
				case GETUPDATE: 
					response = update(puid, pair.sendable); 
					break;
				default:
					break;
			}
			this.handleResponse(s, response);
		}
		requestPairs.clear();
	}
	
	private void handleResponse(Session s, Response r) {
		assert s != null;
		assert r != null;
		responseHandler.responseQueue.add(r);
	}
	
	private int getPlayerUid(Session s) {
		//TODO
		return lastPlayerId;
	}
	
	private void command(int playeruid, Command cmd) {
		Player player = playerManager.getPlayer(playeruid);
		assert player != null;
		assert cmd != null;
		
		EventType type = null;
		switch(cmd.type) {
		case ACCELERATE:
			type = EventType.ACTION_ACCELERATE;
			break;
		case DECELERATE:
			type = EventType.ACTION_DECELERATE;
			break;
		case LEFT:
			type = EventType.ACTION_TURNLEFT;
			break;
		case RIGHT:
			type = EventType.ACTION_TURNRIGHT;
			break;
		case ENTEREXIT:
			type = EventType.ACTION_ENTEREXIT;
			Logger.i().log(TAG, "ENTER/EXIT received" );
			break;
		case SHOOT:
			type = EventType.ACTION_SHOOT;
			break;
		case HANDBRAKE:
			type = EventType.ACTION_HANDBRAKE;
			break;
		}
		if (type != null) {
			eventManager.dispatchEvent(new GameEvent(type, player));
		}
	}

	private void getMapData() {
		//return world.toXMLElement(0, revisionKeeper);
		// TODO
		return null;
	}

	private Response joinPlayer(int uid, Request sendable) {
		if (!playerManager.hasPlayer(uid)) {
			return new Response(Response.Status.NEED, sendable);
		}
		boolean ok = playerManager.spawnPlayer(uid);
		Response.Status status = ok ? Response.Status.OK : Response.Status.BAD;
		return new Response(status, sendable);
	}

	private void leavePlayer(int uid) {
		playerManager.deactivatePlayer(uid);
	}

	private int createPlayer(String name) {
		assert name != null;
		Player player = playerManager.createPlayer(name);
		
		// TEMP
		this.lastPlayerId = player.getUid();
		
		return player.getUid();
	}
	
	private void getPlayer(int uid) {
		return playerManager.getPlayer(uid);
	}
	

}
