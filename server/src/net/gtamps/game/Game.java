package net.gtamps.game;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import net.gtamps.GTAMultiplayerServer;
import net.gtamps.game.player.PlayerManagerFacade;
import net.gtamps.game.universe.Universe;
import net.gtamps.game.universe.UniverseFactory;
import net.gtamps.server.SessionManager;
import net.gtamps.server.User;
import net.gtamps.server.gui.LogType;
import net.gtamps.server.gui.Logger;
import net.gtamps.shared.game.GameObject;
import net.gtamps.shared.game.NullGameObject;
import net.gtamps.shared.game.event.EventType;
import net.gtamps.shared.game.event.GameEvent;
import net.gtamps.shared.game.player.Player;
import net.gtamps.shared.serializer.communication.Sendable;
import net.gtamps.shared.serializer.communication.SendableType;
import net.gtamps.shared.serializer.communication.data.PlayerData;
import net.gtamps.shared.serializer.communication.data.RevisionData;
import net.gtamps.shared.serializer.communication.data.UpdateData;


/**
 * the game, as it happens on the server.
 *
 * @author jan, tom, til
 */
public class Game implements IGame, Runnable {
    private static final LogType TAG = LogType.GAMEWORLD;
    private static final long THREAD_UPDATE_SLEEP_TIME = 20;
    private static final int PHYSICS_ITERATIONS = 20;

    private static volatile int instanceCounter = 0;

    private final int id;
    private final Thread thread;
    private final BlockingQueue<Sendable> requestQueue = new LinkedBlockingQueue<Sendable>();
    private final BlockingQueue<Sendable> commandQueue = new LinkedBlockingQueue<Sendable>();
    private final BlockingQueue<Sendable> responseQueue = new LinkedBlockingQueue<Sendable>();

    private volatile boolean run;
    private volatile boolean isActive;

    private final Universe universe;
    private final PlayerManagerFacade playerStorage;
    private final TimeKeeper gameTime;

    public Game(final String mapPath) {
        id = ++Game.instanceCounter;
        final String name = "Game " + id;
        thread = new Thread(this, name);
        universe = UniverseFactory.loadMap(mapPath);
        if (universe != null) {
            Logger.i().log(LogType.GAMEWORLD, "Starting new Game: " + universe.getName());
            run = true;
            playerStorage = new PlayerManagerFacade(universe.playerManager);
            gameTime = new TimeKeeper();
            start();
        } else {
            Logger.i().log(LogType.GAMEWORLD, "Game not loaded");
            run = false;
            playerStorage = null;
            gameTime = null;
        }
    }

    public Game() {
        this(GTAMultiplayerServer.DEFAULT_PATH + GTAMultiplayerServer.DEFAULT_MAP);
    }

    @Override
    public long getId() {
        return id;
    }

    @Override
    public String getName() {
        return universe.getName();
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
//		world.eventManager.dispatchEvent(new GameEvent(EventType.SESSION_STARTS, world));
        while (run) {
            gameTime.startCycle();
            doCycle();
            gameTime.endCycle();
            sleepIfCycleTimeRemaining();
        }
//		world.eventManager.dispatchEvent(new GameEvent(EventType.SESSION_ENDS, world));
        isActive = false;
    }

    private void doCycle() {
        universe.updateRevision(gameTime.getTotalDurationMillis());
        universe.physics.step(gameTime.getLastCycleDurationSeconds(), PHYSICS_ITERATIONS);
        //TODO
		universe.eventManager.dispatchEvent(new GameEvent(EventType.SESSION_UPDATE, NullGameObject.DUMMY));

        //for fps debugging
//		lastUpdate += timeElapsedInSeceonds;
//		updates++;
//		if(lastUpdate>5f){
//			Logger.i().log(LogType.PHYSICS, "Physics fps: "+((updates/lastUpdate)));
//			lastUpdate = 0f;
//			updates = 0;
//		}

        processCommandQueue();
        processRequestQueue();
    }

    private void sleepIfCycleTimeRemaining() {
        final long millisRemaining = THREAD_UPDATE_SLEEP_TIME - gameTime.getLastActiveDurationMillis();
        try {
            if (millisRemaining > 0) {
                Thread.sleep(millisRemaining);
            }
        } catch (final InterruptedException e) {
            // reset interrupted status?
            //Thread.currentThread().interrupt();
        }
    }


    @Override
    public boolean isActive() {
        return isActive;
    }


    @Override
    public void handleSendable(final Sendable sendable) {
        if (sendable == null) {
            throw new IllegalArgumentException("'r' must not be null");
        }
        switch (sendable.type) {
            case ACTION_ACCELERATE:
            case ACTION_DECELERATE:
            case ACTION_ENTEREXIT:
            case ACTION_LEFT:
            case ACTION_RIGHT:
            case ACTION_SHOOT:
            case ACTION_SUICIDE:
                commandQueue.add(sendable);
                break;
            case GETMAPDATA:
            case GETPLAYER:
            case GETUPDATE:
            case JOIN:
            case LEAVE:
                requestQueue.add(sendable);
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
        final List<Sendable> commandPairs = new LinkedList<Sendable>();
        commandQueue.drainTo(commandPairs);
        for (final Sendable sendable : commandPairs) {
            command(sendable);
        }
        commandPairs.clear();
    }

    private void processRequestQueue() {
        final List<Sendable> requestPairs = new LinkedList<Sendable>();
        requestQueue.drainTo(requestPairs);
        for (final Sendable sendable : requestPairs) {
            final Sendable response = processRequest(sendable);
            handleResponse(response);
        }
        requestPairs.clear();
    }

    private Sendable processRequest(final Sendable request) {
        Sendable response = null;
        if (!(request.type.equals(SendableType.JOIN) || SessionManager.instance.isPlaying(request.sessionId))) {
            return request.createResponse(request.type.getNeedResponse());
        }
        switch (request.type) {
            case JOIN:
                response = join(request);
                break;
            case GETMAPDATA:
//					response = getMapData(session, request);
                break;
            case GETPLAYER:
                response = getPlayer(request);
                break;
            case GETUPDATE:
                response = getUpdate(request);
                break;
            case LEAVE:
                response = leave(request);
                break;
            default:
                break;

        }
        return response;
    }


    private void handleResponse(final Sendable r) {
        assert r != null;
        responseQueue.add(r);
    }

    private void command(final Sendable cmd) {
        final User user = SessionManager.instance.getUserForSession(cmd.sessionId);
        final Player player = playerStorage.getPlayerForUser(user);
        if (player == null) {
            return;
        }
        EventType type = null;
        switch (cmd.type) {
            case ACTION_ACCELERATE:
                type = EventType.ACTION_ACCELERATE;
                break;
            case ACTION_DECELERATE:
                type = EventType.ACTION_DECELERATE;
                break;
            case ACTION_LEFT:
                type = EventType.ACTION_TURNLEFT;
                break;
            case ACTION_RIGHT:
                type = EventType.ACTION_TURNRIGHT;
                break;
            case ACTION_ENTEREXIT:
                type = EventType.ACTION_ENTEREXIT;
                Logger.i().log(TAG, "ENTER/EXIT received");
                break;
            case ACTION_SHOOT:
                type = EventType.ACTION_SHOOT;
                break;
            case ACTION_HANDBRAKE:
                type = EventType.ACTION_HANDBRAKE;
                break;
            case ACTION_SUICIDE:
                type = EventType.ACTION_SUICIDE;
        }
        if (type != null) {
            universe.eventManager.dispatchEvent(new GameEvent(type, player));
        }
    }


    private Sendable join(final Sendable sendable) {
        assert sendable.type.equals(SendableType.JOIN);
        final User user = SessionManager.instance.getUserForSession(sendable.sessionId);
        final Player player = playerStorage.joinUser(user);
        if (player == null) {
            return sendable.createResponse(SendableType.JOIN_BAD);
        }
        SessionManager.instance.joinSession(sendable.sessionId, this);
        return sendable.createResponse(SendableType.JOIN_OK);
    }

    private Sendable leave(final Sendable sendable) {
        assert sendable.type.equals(SendableType.LEAVE);
        final User user = SessionManager.instance.getUserForSession(sendable.sessionId);
        playerStorage.leaveUser(user);
        SessionManager.instance.leaveSession(sendable.sessionId);
        return sendable.createResponse(SendableType.LEAVE_OK);
    }

    private Sendable getPlayer(final Sendable request) {
        assert request.type.equals(SendableType.GETPLAYER);
        final User user = SessionManager.instance.getUserForSession(request.sessionId);
        final Player player = playerStorage.getPlayerForUser(user);
        if (player == null) {
            return request.createResponse(SendableType.GETPLAYER_NEED);
        }
        final Sendable response = request.createResponse(SendableType.GETPLAYER_OK);
        response.data = new PlayerData(player);
        return response;
    }


    private Sendable getUpdate(final Sendable sendable) {
        assert sendable.type.equals(SendableType.GETUPDATE);
        final User user = SessionManager.instance.getUserForSession(sendable.sessionId);
        final Player player = playerStorage.getPlayerForUser(user);
        if (player == null) {
            return sendable.createResponse(SendableType.GETPLAYER_NEED);
        }
        final long baseRevision = ((RevisionData) sendable.data).revisionId;
        final ArrayList<GameObject> entities = universe.entityManager.getUpdate(baseRevision);
        final ArrayList<GameObject> events = universe.eventManager.getUpdate(baseRevision);
        final UpdateData update = new UpdateData(baseRevision, universe.getRevision());
        update.gameObjects = new ArrayList<GameObject>();
        update.gameObjects.addAll(entities);
        update.gameObjects.addAll(events);
        final Sendable updateResponse = sendable.createResponse(SendableType.GETUPDATE_OK);
        updateResponse.data = update;
        return updateResponse;
    }

}
