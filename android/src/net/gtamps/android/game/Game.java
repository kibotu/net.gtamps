package net.gtamps.android.game;

import android.os.SystemClock;
import net.gtamps.android.World;
import net.gtamps.android.core.input.InputEngine;
import net.gtamps.android.game.client.ConnectionManager;
import net.gtamps.android.game.entity.views.EntityView;
import net.gtamps.android.game.entity.views.Hud;
import net.gtamps.shared.Config;
import net.gtamps.shared.Utils.Logger;
import net.gtamps.shared.communication.Message;
import net.gtamps.shared.communication.MessageFactory;
import net.gtamps.shared.communication.Sendable;
import net.gtamps.shared.communication.SendableType;
import net.gtamps.shared.communication.data.FloatData;
import net.gtamps.shared.communication.data.PlayerData;
import net.gtamps.shared.communication.data.UpdateData;
import net.gtamps.shared.game.entity.Entity;
import net.gtamps.shared.game.event.GameEvent;
import net.gtamps.shared.math.Vector3;

import java.util.ArrayList;

public class Game implements IGame{

    private boolean isRunning;
    private boolean isPaused;
    private long startTime;
    private long finalDelta;
    private final InputEngine inputEngine;
    private final ArrayList<Scene> scenes;
    private boolean isDragging = false;
    private final Hud hud;
    private final World world;
    private final ConnectionManager connection;

    public Game() {
        isRunning = true;
        isPaused = false;
        inputEngine = InputEngine.getInstance();
        scenes = new ArrayList<Scene>();
        hud = new Hud();
        world = new World();
        connection = new ConnectionManager();
    }

    public void onCreate() {

        // create world
        scenes.add(world.getScene());

        // hud
        scenes.add(hud.getScene());

        // connect
        checkConnection();

        Logger.I(this, "Connecting to " + Config.SERVER_HOST_ADDRESS + ":" + Config.SERVER_PORT + " " + (connection.isConnected() ? "successful." : "failed."));
        connection.start();
        connection.add(MessageFactory.createSessionRequest());
    }

    private int socketTimeOut = Config.MAX_SOCKET_TIMEOUT;

    @Override
    public void onDrawFrame() {
        if (!isRunning || isPaused) {
            return;
        }

        // check connection
        checkConnection();

        // handle inbox messages
        while(!connection.isEmpty()) {
            Message message = connection.poll();
            for(int i = 0; i < message.sendables.size(); i++) {
                handleMessage(message.sendables.get(i),message);
            }
        }

        // zoom
        if (inputEngine.getZoomState()){
            setZoomByDistance(inputEngine.getZoomDistance());
        }

        // on touch
        if (inputEngine.getDownState()){
//            Utils.log(TAG, "finger down");
            isDragging = true;
        }

        // on release
        if (inputEngine.getUpState()){
//            Utils.log(TAG, "finger up");
            isDragging = false;
            hud.getCursor().setPosition(inputEngine.getPointerPosition());
        }

        if(isDragging) {
//            Utils.log(TAG, "is dragging");
            Vector3 viewportSize = scenes.get(1).getActiveCamera().getViewportSize();
            Vector3 pos = inputEngine.getPointerPosition();
            Vector3 temp = pos.sub(viewportSize).mulInPlace(1).addInPlace(viewportSize);
            hud.getCursor().setPosition(temp);
//            world.getActiveObject().getNode().getPosition().addInPlace(temp);
//            scenes.get(0).getActiveCamera().move(temp);

            float angle = Vector3.XAXIS.angleInBetween(pos)-90;
//            world.getActiveObject().getNode().setRotation(0, 0, angle);
            hud.getRing().setRotation(0, 0, angle);

            Vector3 temp2 = Vector3.createNew(temp);
            temp2.normalize();
            temp2.invert();
            temp2.mulInPlace(40);

            Vector3 camPos = scenes.get(0).getActiveCamera().getPosition();
            Vector3 temp3 = Vector3.createNew(temp2.x,temp2.y,camPos.z).addInPlace(world.getActiveObject().getNode().getPosition());

            scenes.get(0).getActiveCamera().setPosition(temp3);
            scenes.get(0).getActiveCamera().setTarget(world.getActiveObject().getNode().getPosition());

            // send driving impulses
            fireImpulse(angle, temp);

//            temp.recycle();
        }

        // Compute elapsed time
        finalDelta = SystemClock.elapsedRealtime() - startTime;

        // new start time
        startTime = SystemClock.elapsedRealtime();

        // animate
    }

    private void checkConnection() {
        if(!connection.isConnected()) {
            while (!connection.connect()) {
                if(socketTimeOut <= 0) stop();
                try {
                    Thread.sleep(Config.SOCKET_TIMEOUT);
                    socketTimeOut -= Config.SOCKET_TIMEOUT;
                } catch (InterruptedException e) {
                }
                Logger.e(this, "Server unavailable. Trying to reconnect");
            }
        }
        socketTimeOut = Config.MAX_SOCKET_TIMEOUT;
    }

    private long impulse = 0;
    private void fireImpulse(float angle, Vector3 force) {
        if(!connection.isConnected()) return;

        impulse += finalDelta;
        if(impulse <= Config.IMPULS_FREQUENCY) return;
        impulse = 0;

        Message message = MessageFactory.createGetUpdateRequest(ConnectionManager.currentRevId);

        // up° 22.5 to -22.5°
        if(inRange(angle, 22.5f, -22.5f)) {
            message.addSendable(new Sendable(SendableType.ACCELERATE,new FloatData(force.getLength())));
        }

        // right up -22.5° to -67.5°

        // right -67.5° to -112.5°
        if(inRange(angle, -67.5f, -112.5f)) {
            message.addSendable(new Sendable(SendableType.RIGHT,new FloatData(force.getLength())));
        }

        // right down -112.5° to -157.5°

        // down -157.5° to -180° and 180° to 157.5°
        if(inRange(angle, -157.5f, -180f) || inRange(angle, 180f,157.5f)) {
            message.addSendable(new Sendable(SendableType.DECELERATE,new FloatData(force.getLength())));
        }

        // left down 157.5° to 112.5°

        // left 112.5° to 67.5°
        if(inRange(angle, 112.5f, 67.5f)) {
            message.addSendable(new Sendable(SendableType.LEFT,new FloatData(force.getLength())));
        }

        // left up 67.5° to 22.5°

        connection.add(message);
    }

    private boolean inRange(float value, float startRange, float endRange) {
        return value >= Math.min(startRange,endRange) && value <= Math.max(startRange,endRange);
    }

    private void handleMessage(Sendable sendable, Message message) {
        Logger.i(this, "Handles message.");
        Logger.i(this, sendable);

        switch (sendable.type) {
            case GETUPDATE_OK:

                // empty
                if(sendable.data == null) break;

                // not an update
                if(!(sendable.data instanceof UpdateData)) break;

                // update revision id
                UpdateData updateData = ((UpdateData)sendable.data);
                ConnectionManager.currentRevId = updateData.revId;

                // event
                if(updateData.entites instanceof GameEvent) {

                }

                // entities
                if(updateData.entites instanceof Entity) {

                }

                // parse all transmitted entities
                ArrayList<Entity> entities = updateData.entites;
                Logger.d(this, "response size " + entities.size());
                for(int i = 0; i < entities.size(); i++) {
                    Entity serverEntity = entities.get(i);

                    // new or update
                    EntityView entityView = world.getScene().getObject3DById(serverEntity.getUid());
                    if(entityView == null) {
                        // new entity
                        entityView = new EntityView(serverEntity);
                        world.getScene().addChild(entityView);
                        Logger.i(this,"add new entity "+serverEntity.getUid());
                    } else {
                        // update
                        entityView.update(serverEntity);
                        Logger.i(this, "update entity" + serverEntity.getUid());
                    }
                }

            case GETUPDATE_NEED: break;
            case GETUPDATE_BAD: break;
            case GETUPDATE_ERROR: break;

            case GETPLAYER_OK:

                // empty
                if(sendable.data == null) break;

                // not player data
                if(!(sendable.data instanceof PlayerData)) break;
                world.playerManager.setActivePlayer(((PlayerData) sendable.data).player);

                // get update
                connection.add(MessageFactory.createGetUpdateRequest(ConnectionManager.currentRevId));
                break;

            case GETPLAYER_NEED: break;
            case GETPLAYER_BAD: break;
            case GETPLAYER_ERROR: break;

            case SESSION_OK:
                ConnectionManager.currentSessionId = message.getSessionId();
                connection.add(MessageFactory.createRegisterRequest("username", "password"));
                break;
            case SESSION_NEED: break;
            case SESSION_BAD: break;
            case SESSION_ERROR: break;

            case JOIN_OK: connection.add(MessageFactory.createGetPlayerRequest()); break;
            case JOIN_NEED: break;
            case JOIN_BAD: break;
            case JOIN_ERROR: break;

            case GETMAPDATA_OK: break;
            case GETMAPDATA_NEED: break;
            case GETMAPDATA_BAD: break;
            case GETMAPDATA_ERROR: break;

            case LEAVE_OK: break;
            case LEAVE_NEED: break;
            case LEAVE_BAD: break;
            case LEAVE_ERROR: break;

            case LOGIN_OK: connection.add(MessageFactory.createJoinRequest()); break;
            case LOGIN_NEED: break;
            case LOGIN_BAD: break;
            case LOGIN_ERROR: break;

            case REGISTER_OK: connection.add(MessageFactory.createLoginRequest("username", "password")); break;
            case REGISTER_NEED: break;
            case REGISTER_BAD: break;
            case REGISTER_ERROR: break;
            default: break;
        }
    }

    /** Setzt den neuen Zoomfaktor anhand der Zoomwerte des Inputs
     *
     * @param distance Distanz der beiden Zoomenden Finger
     */
    private void setZoomByDistance(float distance){
        scenes.get(0).getActiveCamera().setZoomFactor(scenes.get(0).getActiveCamera().getZoomFactor() + distance*0.01f);
    }

    /**
     * Defines if the game is running.
     *
     * @return <code>true</code> if is running.
     */
    public boolean isRunning() {
        return isRunning;
    }

    @Override
    public boolean isPaused() {
        return isPaused;
    }

    @Override
    public ArrayList<Scene> getScenes() {
        return scenes;
    }

    /**
     * Starts the game.
     */
    public void start() {
        isRunning = true;
        startTime = SystemClock.elapsedRealtime();
    }

    /**
     * Ends the game.
     */
    public void stop() {
        isRunning = false;
    }
}
