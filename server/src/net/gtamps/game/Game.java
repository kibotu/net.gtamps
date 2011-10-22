package net.gtamps.game;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import net.gtamps.GTAMultiplayerServer;
import net.gtamps.game.player.PlayerManagerFacade;
import net.gtamps.game.world.World;
import net.gtamps.game.world.WorldFactory;
import net.gtamps.server.Session;
import net.gtamps.server.gui.LogType;
import net.gtamps.server.gui.Logger;
import net.gtamps.shared.communication.Sendable;
import net.gtamps.shared.communication.SendableType;
import net.gtamps.shared.communication.data.PlayerData;
import net.gtamps.shared.communication.data.RevisionData;
import net.gtamps.shared.communication.data.UpdateData;
import net.gtamps.shared.game.GameObject;
import net.gtamps.shared.game.event.EventType;
import net.gtamps.shared.game.event.GameEvent;
import net.gtamps.shared.game.player.Player;


/**
 *	the game, as it happens on the server.
 *
 * @author jan, tom, til
 *
 */
public class Game implements IGame, Runnable {
	private static final LogType TAG = LogType.GAMEWORLD;
	private static final long THREAD_UPDATE_SLEEP_TIME = 20;
	private static final int PHYSICS_ITERATIONS = 20;
	
	private static volatile int instanceCounter = 0;

	private class MessagePair<T extends Sendable> {
		public T sendable;
		public Session session;
		public MessagePair(final T sendable, final Session session) {
			this.sendable = sendable;
			this.session = session;
		}
	}

	private final int id;
	private final Thread thread;
	private final BlockingQueue<MessagePair<Sendable>> requestQueue = new LinkedBlockingQueue<MessagePair<Sendable>>();
	private final BlockingQueue<MessagePair<Sendable>> commandQueue = new LinkedBlockingQueue<MessagePair<Sendable>>();
	private final BlockingQueue<Sendable> responseQueue = new LinkedBlockingQueue<Sendable>();
	
	private volatile boolean run;
	private volatile boolean isActive;

	private long lastTime = System.nanoTime();
	
	private final World world;
	private final PlayerManagerFacade playerStorage;
	
	public Game(String mapPathOrNameOrWhatever) {
		id = ++Game.instanceCounter;
		final String name = "Game " + id;
		thread = new Thread(this, name);
		if (mapPathOrNameOrWhatever == null || mapPathOrNameOrWhatever.isEmpty()) {
			mapPathOrNameOrWhatever = GTAMultiplayerServer.DEFAULT_PATH+GTAMultiplayerServer.DEFAULT_MAP;
		}
		world = WorldFactory.loadMap(mapPathOrNameOrWhatever);
		if (world != null) {
			Logger.i().log(LogType.GAMEWORLD, "Starting new Game: " + world.getName());
			run = true;
			playerStorage = new PlayerManagerFacade(world.playerManager);
		} else {
			Logger.i().log(LogType.GAMEWORLD, "Game not loaded");
			playerStorage = null;
		}
		start();
	}
	
	public Game() {
		this(null);
	}
	
	@Override
	public long getId() {
		return id;
	}

	@Override
	public String getName() {
		return world.getName();
	}
	
	@Override
	public void start() {
		thread.start();
	}
	
	@Override
	public void hardstop() {
		run = false;
		thread.interrupt();
	}
	
	@Override
	public void run() {
		isActive = true;
		world.eventManager.dispatchEvent(new GameEvent(EventType.SESSION_STARTS, world));
		while(run) {
			final float timeElapsedInSeconds = (float) ((System.nanoTime()-lastTime)/1000000000.0);
			long timeElapsedPhysicsCalculation = System.nanoTime();
			lastTime = System.nanoTime();
			world.physics.step(timeElapsedInSeconds, PHYSICS_ITERATIONS);
			world.eventManager.dispatchEvent(new GameEvent(EventType.SESSION_UPDATE, world));
			timeElapsedPhysicsCalculation = (System.nanoTime()-lastTime)/1000000;

			//for fps debugging
//			lastUpdate += timeElapsedInSeceonds;
//			updates++;
//			if(lastUpdate>5f){
//				Logger.i().log(LogType.PHYSICS, "Physics fps: "+((updates/lastUpdate)));
//				lastUpdate = 0f;
//				updates = 0;
//			}
			
			processCommandQueue();
			processRequestQueue();
			
			try {
				if(THREAD_UPDATE_SLEEP_TIME-timeElapsedPhysicsCalculation>0){
					Thread.sleep(THREAD_UPDATE_SLEEP_TIME-timeElapsedPhysicsCalculation);
				}
			} catch (final InterruptedException e) {
				// reset interrupted status?
				//Thread.currentThread().interrupt();
			}
		}
		world.eventManager.dispatchEvent(new GameEvent(EventType.SESSION_ENDS, world));
		isActive = false;
	}


	@Override
	public boolean isActive() {
		return isActive;
	}


	@Override
	public void handleSendable(final Session s, final Sendable r) {
		if (s == null) {
			throw new IllegalArgumentException("'s' must not be null");
		}
		if (r == null) {
			throw new IllegalArgumentException("'r' must not be null");
		}
		switch(r.type) {
			case ACCELERATE:
			case DECELERATE:
			case ENTEREXIT:
			case LEFT:
			case RIGHT:
			case SHOOT:
			case SUICIDE:
				commandQueue.add(new MessagePair<Sendable>(r, s));
				break;
			case GETMAPDATA:
			case GETPLAYER:
			case GETUPDATE:
			case JOIN:
			case LEAVE:
				requestQueue.add(new MessagePair<Sendable>(r, s));
				break;
			default:
				break;
		}
	}

	@Override
	public void drainResponseQueue(final Collection<Sendable> target) {
		responseQueue.drainTo(target);
	}

	private void processCommandQueue() {
		final List<MessagePair<Sendable>> commandPairs = new LinkedList<MessagePair<Sendable>>();
		commandQueue.drainTo(commandPairs);
		for (final MessagePair<Sendable> pair : commandPairs) {
			command(pair.session, pair.sendable);
		}
		commandPairs.clear();
	}
	
	private void processRequestQueue() {
		final List<MessagePair<Sendable>> requestPairs = new LinkedList<MessagePair<Sendable>>();
		requestQueue.drainTo(requestPairs);
		for (final MessagePair<Sendable> pair: requestPairs) {
			final Sendable response = processRequest(pair.session, pair.sendable);
			handleResponse(pair.session, response);
		}
		requestPairs.clear();
	}
	
	private Sendable processRequest(final Session session, final Sendable request) {
		Sendable response = null;
		if (session.getUser() == null) {
			return request.createResponse(request.type.getNeedResponse());
		}
			switch(request.type) {
				case JOIN:
					response = join(session, request);
					break;
				case GETMAPDATA:
//					response = getMapData(session, request);
					break;
				case GETPLAYER:
					response = getPlayer(session, request);
					break;
				case GETUPDATE:
					response = getUpdate(session, request);
					break;
				case LEAVE:
					response = leave(session, request);
					break;
				default:
					break;
					
			}
		return response;
	}
	

	private void handleResponse(final Session s, final Sendable r) {
		assert s != null;
		assert r != null;
		responseQueue.add(r);
	}
	
	private void command(final Session session, final Sendable cmd) {
		final Player player = playerStorage.getPlayerForUser(session.getUser());
		if (player == null) {
			return;
		}
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
		case SUICIDE:
			type = EventType.ACTION_SUICIDE;
		}
		if (type != null) {
			world.eventManager.dispatchEvent(new GameEvent(type, player));
		}
	}

	
	private Sendable join(final Session session, final Sendable sendable) {
		assert sendable.type.equals(SendableType.JOIN);
		final Player player = playerStorage.joinUser(session.getUser());
		if (player == null) {
			return sendable.createResponse(SendableType.JOIN_BAD);
		}
		session.setGame(this);
		return sendable.createResponse(SendableType.JOIN_OK);
	}
	
	private Sendable leave(final Session session, final Sendable sendable) {
		assert sendable.type.equals(SendableType.LEAVE);
		playerStorage.leaveUser(session.getUser());
		session.setGame(null);
		return sendable.createResponse(SendableType.LEAVE_OK);
	}

	private Sendable getPlayer(final Session session, final Sendable request) {
		assert request.type.equals(SendableType.GETPLAYER);
		final Player player = playerStorage.getPlayerForUser(session.getUser());
		if (player == null) {
			return request.createResponse(SendableType.GETPLAYER_NEED);
		}
		final Sendable response = request.createResponse(SendableType.GETPLAYER_OK);
		response.data = new PlayerData(player);
		return response;
	}


	private Sendable getUpdate(final Session session, final Sendable sendable) {
		final long baseRevision = ((RevisionData) sendable.data).revisionId;
		final ArrayList<GameObject> entities = world.entityManager.getUpdate(baseRevision);
		final UpdateData update = new UpdateData(baseRevision, world.getRevision());
		update.gameObjects = entities;
		final Sendable updateResponse = sendable.createResponse(SendableType.GETUPDATE_OK);
		updateResponse.data = update;
		return updateResponse;
	}
	
}
