package net.gtamps.game;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import net.gtamps.GTAMultiplayerServer;
import net.gtamps.ResourceLoader;
import net.gtamps.XmlElements;
import net.gtamps.game.entity.EntityManager;
import net.gtamps.game.event.EventManager;
import net.gtamps.game.physics.Box2DEngine;
import net.gtamps.game.player.PlayerManager;
import net.gtamps.game.world.MapParser;
import net.gtamps.game.world.World;
import net.gtamps.server.gui.LogType;
import net.gtamps.server.gui.Logger;
import net.gtamps.shared.communication.Command;
import net.gtamps.shared.game.entity.Entity;
import net.gtamps.shared.game.event.EventType;
import net.gtamps.shared.game.event.GameEvent;
import net.gtamps.shared.game.player.Player;

import org.jdom.Attribute;
import org.jdom.Element;
import org.jdom.JDOMException;

/**
 *	the game, as it happens on the server.
 *
 * @author jan, tom, til
 *
 */
public class GameThread extends Thread implements IGameThread {
	private static final LogType TAG = LogType.GAMEWORLD;
	private static final long EVENT_TIMEOUT = 30;
	private static final long THREAD_UPDATE_SLEEP_TIME = 20;
	private static final int PHYSICS_ITERATIONS = 20;
	
	private static int instanceCounter = 0;

	// start with value > 0
	//private long currentRevisionId = RevisionKeeper.START_REVISION;
	private RevisionKeeper revisionKeeper = new RevisionKeeper(this);
	
	private EventManager eventManager;
	private PlayerManager playerManager;
	private EntityManager entityManager;
	private World world;
	private Box2DEngine physics;
	
	//temp
	private int lastPlayerId = -1;

	private long lastTime = System.nanoTime(); 

	public GameThread(String mapPathOrNameOrWhatever) {
		super("GameThread" + (++GameThread.instanceCounter));
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
			eventManager = new EventManager();
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
	
	
	public GameThread() {
		this(null);
		Logger.i().log(LogType.GAMEWORLD, "Starting new Game!");
		//just something big.
//		physics = new Box2DEngine(-1000f,-1000f,1000f,1000f);
//		world = WorldFactory.createMap(physics);
	//	players = new HashMap<Player, Boolean>();
	}

	@Override
	public Element getUpdatesAsXML(long revisionId) {
//		List<Element> entityElements = entityManager.getUpdate(revisionId, revisionKeeper);
//		List<Element> eventElements = eventManager.getUpdate(revisionId, revisionKeeper);

//		if (revisionKeeper.updateIsRequired()) {
//			Logger.i().log(TAG, "returning update " + revisionKeeper);
//		}

//		revisionKeeper.updateRevision(this);
//		if (entityElements.size() == 0 && eventElements.size() == 0) {
//			return null;
//		}
//		Element update = new Element(XmlElements.UPDATE.tagName());
//		update.addContent(entityElements);
//		update.addContent(eventElements);
//		String revTag = XmlElements.ATTRIB_REVISION.tagName();
//		Attribute rev = new Attribute(revTag, ""+revisionKeeper.getCurrentRevision());
//		update.setAttribute(rev);
//		return update;
		return null;
	}
	
	
	public Iterable<Entity> getUpdates(long revisionId) {
		ArrayList<Entity> updates = new ArrayList<Entity>();
		if (lastPlayerId >=0) {
			updates.add(playerManager.getPlayer(lastPlayerId).getEntity());
		}
		return updates;
	}

	
	
	@Override
	public void command(int playeruid, Command cmd) {
		Player player = getPlayer(playeruid);
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

	@Override
	public Element getMapData() {
		//return world.toXMLElement(0, revisionKeeper);
		// TODO
		return null;
	}

	@Override
	public boolean joinPlayer(int uid) {
		return playerManager.spawnPlayer(uid);
	}

	@Override
	public void leavePlayer(int uid) {
		playerManager.deactivatePlayer(uid);
	}

	@Override
	public int createPlayer(String name) {
		assert name != null;
		Player player = playerManager.createPlayer(name);
		
		// TEMP
		this.lastPlayerId = player.getUid();
		
		return player.getUid();
	}
	
	@Override
	public Player getPlayer(int uid) {
		return playerManager.getPlayer(uid);
	}

	private int updates = 0;
	private float lastUpdate = 0f;
	private boolean run = true;
	
	@Override
	public void run() {
		while(run) {
			float timeElapsedInSeceonds = (float) ((System.nanoTime()-lastTime)/1000000000.0);
			long timeElapsedPhysicsCalculation = System.nanoTime();
			lastTime = System.nanoTime();
			this.physics.step(timeElapsedInSeceonds, PHYSICS_ITERATIONS);
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
				Thread.currentThread().interrupt();
			}
		}
	}


	@Override
	public void hardstop() {
		this.run = false;
		this.interrupt();
	}
}
