package net.gtamps.android.game;

import android.os.SystemClock;
import net.gtamps.android.core.input.InputEngine;
import net.gtamps.android.core.renderer.BasicRenderActivity;
import net.gtamps.android.core.renderer.Registry;
import net.gtamps.android.core.renderer.graph.scene.BasicScene;
import net.gtamps.android.game.content.EntityView;
import net.gtamps.android.game.content.scenes.Hud;
import net.gtamps.android.game.content.scenes.Menu;
import net.gtamps.android.game.content.scenes.World;
import net.gtamps.shared.Config;
import net.gtamps.shared.Utils.Logger;
import net.gtamps.shared.Utils.math.Vector3;
import net.gtamps.shared.game.GameObject;
import net.gtamps.shared.game.entity.Entity;
import net.gtamps.shared.game.event.GameEvent;
import net.gtamps.shared.game.player.Player;
import net.gtamps.shared.serializer.ConnectionManager;
import net.gtamps.shared.serializer.communication.Message;
import net.gtamps.shared.serializer.communication.MessageFactory;
import net.gtamps.shared.serializer.communication.Sendable;
import net.gtamps.shared.serializer.communication.SendableType;
import net.gtamps.shared.serializer.communication.data.FloatData;
import net.gtamps.shared.serializer.communication.data.PlayerData;
import net.gtamps.shared.serializer.communication.data.UpdateData;

import java.util.ArrayList;
import java.util.List;

public class Game implements BasicRenderActivity.IRenderActivity {

    private boolean isRunning;
    private boolean isPaused;
    private long startTime;
    private long finalDelta;
    private final InputEngine inputEngine;
    private final ArrayList<BasicScene> scenes;
    private boolean isDragging = false;
    private final Hud hud;
    private final World world;
    private final Menu menu;
    private final ConnectionManager connection;

    public Game() {
        isRunning = true;
        isPaused = false;
        inputEngine = InputEngine.getInstance();
        scenes = new ArrayList<BasicScene>();
        hud = new Hud();
        world = new World();
        menu = new Menu();
        connection = new ConnectionManager();
    }

    public void onCreate() {

        // create world
        scenes.add(world);
//        world.getScene().setVisible(false);

        // hud
        scenes.add(hud);
//        hud.getScene().setVisible(false);

        scenes.add(menu);
        menu.getScene().setVisible(false);

        // connect
//        connection.checkConnection();
//
//        Logger.I(this, "Connecting to " + Config.SERVER_DEFAULT_HOST_ADDRESS + ":" + Config.SERVER_DEFAULT_PORT + " " + (connection.isConnected() ? "successful." : "failed."));
//        connection.start();
//        connection.add(MessageFactory.createSessionRequest());
    }

//    int menuloop = 0;

    @Override
    public void onDrawFrame() {
        if (!isRunning || isPaused) {
            return;
        }

        // check connection
//        connection.checkConnection();

        // handle inbox messages
        while (!connection.isEmpty()) {
            Message message = connection.poll();
            for (int i = 0; i < message.sendables.list.size(); i++) {
                handleMessage(message.sendables.list.get(i), message);
            }
        }

        onDrawFrame2();
    }

    private void onDrawFrame2() {

        // zoom
        if (inputEngine.getZoomState()) {
            setZoomByDistance(inputEngine.getZoomDistance());
        }

        // on touch
        if (inputEngine.getDownState()) {
//            Utils.log(TAG, "finger down");
            isDragging = true;

//            if(menuloop % 3 == 0) menu.showStartScreen();
//            if(menuloop % 3 == 1) menu.showOptionsScreen();
//            if(menuloop % 3 == 2) menu.showServerListScreen();
//            menuloop++;

//            EntityView view = world.getScene().getEntityView((int) (Math.random() * world.getScene().getObjects3dCount()));
//            if(view != null) world.setActiveObject(view);
        }

        // on release
        if (inputEngine.getUpState()) {
//            Utils.log(TAG, "finger up");
            isDragging = false;
            hud.getCursor().setPosition(inputEngine.getPointerPosition());
        }

        Vector3 temp3 = Vector3.createNew();
        if (isDragging) {
//            Utils.log(TAG, "is dragging");
            Vector3 viewportSize = scenes.get(1).getActiveCamera().getViewportSize();
            Vector3 pos = inputEngine.getPointerPosition();
            Vector3 temp = pos.sub(viewportSize).mulInPlace(1).addInPlace(viewportSize);
            hud.getCursor().setPosition(temp);
            world.getActiveView().getObject3d().getPosition().addInPlace(temp);
            scenes.get(0).getActiveCamera().move(temp);

            float angle = Vector3.XAXIS.angleInBetween(pos) - 90;
            world.getActiveView().getObject3d().setRotation(0, 0, angle);
            hud.getRing().setRotation(0, 0, angle);

            Vector3 temp2 = Vector3.createNew(temp);
            temp2.normalize();
            temp2.invert();
            temp2.mulInPlace(40);

            Vector3 camPos = scenes.get(0).getActiveCamera().getPosition();
            temp3.set(temp2.x, temp2.y, camPos.z).addInPlace(world.getActiveView().getObject3d().getPosition());

            // send driving impulses
            fireImpulse(angle, temp);

//            temp.recycle();
            scenes.get(0).getActiveCamera().setPosition(temp3);
        }
        scenes.get(0).getActiveCamera().setTarget(world.getActiveView().getObject3d().getPosition());

        // Compute elapsed time
        finalDelta = SystemClock.elapsedRealtime() - startTime;

        // new start time
        startTime = SystemClock.elapsedRealtime();

        // animate
    }

    private long impulse = 0;

    private void fireImpulse(float angle, Vector3 force) {
        if (!connection.isConnected()) return;

        impulse += finalDelta;
        if (impulse <= Config.IMPULS_FREQUENCY) return;
        impulse = 0;

        Message message = MessageFactory.createGetUpdateRequest(ConnectionManager.currentRevId);

        // up° 22.5 to -22.5°
        if (inRange(angle, 22.5f, -22.5f)) {
            message.addSendable(new Sendable(SendableType.ACTION_ACCELERATE, new FloatData(force.getLength())));
        }

        // right up -22.5° to -67.5°

        // right -67.5° to -112.5°
        if (inRange(angle, -67.5f, -112.5f)) {
            message.addSendable(new Sendable(SendableType.ACTION_RIGHT, new FloatData(force.getLength())));
        }

        // right down -112.5° to -157.5°

        // down -157.5° to -180° and 180° to 157.5°
        if (inRange(angle, -157.5f, -180f) || inRange(angle, 180f, 157.5f)) {
            message.addSendable(new Sendable(SendableType.ACTION_DECELERATE, new FloatData(force.getLength())));
        }

        // left down 157.5° to 112.5°

        // left 112.5° to 67.5°
        if (inRange(angle, 112.5f, 67.5f)) {
            message.addSendable(new Sendable(SendableType.ACTION_LEFT, new FloatData(force.getLength())));
        }

        // left up 67.5° to 22.5°

        connection.add(message);
    }

    private boolean inRange(float value, float startRange, float endRange) {
        return value >= Math.min(startRange, endRange) && value <= Math.max(startRange, endRange);
    }

    private void handleMessage(Sendable sendable, Message message) {
        Logger.i(this, "Handles message.");
        Logger.i(this, sendable);

        switch (sendable.type) {
            case GETUPDATE_OK:

                // empty
                if (sendable.data == null) break;

                // not an update
                if (!(sendable.data instanceof UpdateData)) break;

                // update revision id
                UpdateData updateData = ((UpdateData) sendable.data);
                ConnectionManager.currentRevId = updateData.revId;

                // parse all transmitted entities
                List<GameObject> gameObjects = updateData.gameObjects.list;
                Logger.d(this, "GameObject amount: " + gameObjects.size());
                int keepTrackOfOrder = 0;
                for (int i = 0; i < gameObjects.size(); i++) {

                    GameObject go = gameObjects.get(i);

                    if (go instanceof GameEvent) {

                        handleEvent((GameEvent) go);
                        keepTrackOfOrder = 2;

                    } else if (go instanceof Entity) {
                        if (keepTrackOfOrder == 2)
                            Logger.E(this, "Server entity after game event. GameEvent fired on empty entities.");
                        keepTrackOfOrder = 1;

                        // entity
                        Entity serverEntity = (Entity) go;

                        // new or update
                        EntityView entityView = world.getViewById(serverEntity.getUid());
                        if (entityView == null) {

                            // new entity
                            entityView = new EntityView(serverEntity);
                            world.add(entityView.getObject3d());

                            // add to setup
                            Registry.getRenderer().addToSetupQueue(entityView.getObject3d());

                            Logger.i(this, "Add new entity " + serverEntity.getUid());
                        } else {
                            // update
                            entityView.update(serverEntity);
                            Logger.i(this, "Update existing entity " + serverEntity.getUid());
                        }
                    } else {
                        Logger.d(this, "NOT HANDLED UPDATE -> " + go);
                    }
                }
                break;

            case GETUPDATE_NEED:
                break;
            case GETUPDATE_BAD:
                break;
            case GETUPDATE_ERROR:
                break;

            case GETPLAYER_OK:

                // empty
                if (sendable.data == null) break;

                // not player data
                if (!(sendable.data instanceof PlayerData)) break;
                world.playerManager.setActivePlayer(((PlayerData) sendable.data).player);

                Logger.D(this, "GETPLAYER_OK " + world.playerManager.getActivePlayer());

                // get update
                connection.add(MessageFactory.createGetUpdateRequest(ConnectionManager.currentRevId));
                break;

            case GETPLAYER_NEED:
                break;
            case GETPLAYER_BAD:
                break;
            case GETPLAYER_ERROR:
                break;

            case SESSION_OK:
                ConnectionManager.currentSessionId = message.getSessionId();
                connection.add(MessageFactory.createRegisterRequest("username", "password"));
                break;
            case SESSION_NEED:
                break;
            case SESSION_BAD:
                break;
            case SESSION_ERROR:
                break;

            case JOIN_OK:
                connection.add(MessageFactory.createGetPlayerRequest());
                break;
            case JOIN_NEED:
                break;
            case JOIN_BAD:
                break;
            case JOIN_ERROR:
                break;

            case GETMAPDATA_OK:
                break;
            case GETMAPDATA_NEED:
                break;
            case GETMAPDATA_BAD:
                break;
            case GETMAPDATA_ERROR:
                break;

            case LEAVE_OK:
                break;
            case LEAVE_NEED:
                break;
            case LEAVE_BAD:
                break;
            case LEAVE_ERROR:
                break;

            case LOGIN_OK:
                connection.add(MessageFactory.createJoinRequest());
                break;
            case LOGIN_NEED:
                break;
            case LOGIN_BAD:
                break;
            case LOGIN_ERROR:
                break;

            case REGISTER_OK:
                connection.add(MessageFactory.createLoginRequest("username", "password"));
                break;
            case REGISTER_NEED:
                break;
            case REGISTER_BAD:
                break;
            case REGISTER_ERROR:
                break;
            default:
                break;
        }
    }

    private void handleEvent(GameEvent event) {
        Logger.i(this, "Handle event " + event);

        switch (event.getType()) {
            case ACTION_ACCELERATE:
                break;
            case ACTION_DECELERATE:
                break;
            case ACTION_ENTEREXIT:
                break;
            case ACTION_EVENT:
                break;
            case ACTION_HANDBRAKE:
                break;
            case ACTION_NOISE:
                break;
            case ACTION_SHOOT:
                break;
            case ACTION_SUICIDE:
                break;
            case ACTION_SWITCH_GUN_NEXT:
                break;
            case ACTION_SWITCH_GUN_PREV:
                break;
            case ACTION_TURNLEFT:
                break;

            case ENTITY_ACTIVATE:
                break;
            case ENTITY_BULLET_HIT:
                break;
            case ENTITY_COLLIDE:
                break;
            case ENTITY_CREATE:
                break;
            case ENTITY_DAMAGE:
                break;
            case ENTITY_DEACTIVATE:
                break;
            case ENTITY_DESTROYED:
                break;
            case ENTITY_EVENT:
                break;
            case ENTITY_REMOVE:
                break;
            case ENTITY_SENSE:
                break;
            case ENTITY_SENSE_DOOR:
                break;
            case ENTITY_SENSE_EXPLOSION:
                break;
            case ENTITY_UPDATE:
                break;

            case PLAYER_ENTERSCAR:
                break;
            case PLAYER_JOINS:
                break;
            case PLAYER_KILLED:
                break;
            case PLAYER_LOGIN:
                break;
            case PLAYER_LEAVES:
                break;
            case PLAYER_NEWENTITY:

                // source no player
                if (!(event.getSource() instanceof Player)) break;

                // player not active player
                Player player = (Player) event.getSource();
                if (!world.playerManager.getActivePlayer().equals(player)) break;

                // target no entity
                if (!(event.getTarget() instanceof Entity)) break;
                Entity serverEntity = (Entity) event.getTarget();

                // new active object
                EntityView entityView = world.getViewById(serverEntity.getUid());
                world.setActiveView(entityView);

                Logger.i(this, "PLAYER_NEWENTITY " + entityView.entity.getUid());

                break;

            case PLAYER_POWERUP:
                break;
            case PLAYER_SCORES:
                break;
            case PLAYER_SPAWNS:
                break;
            case PLAYER_WINS:
                break;

            case SESSION_ENDS:
                break;
            case SESSION_STARTS:
                break;
            case SESSION_UPDATE:
                break;
        }
    }

    /**
     * Setzt den neuen Zoomfaktor anhand der Zoomwerte des Inputs
     *
     * @param distance Distanz der beiden Zoomenden Finger
     */
    private void setZoomByDistance(float distance) {
        scenes.get(0).getActiveCamera().setZoomFactor(scenes.get(0).getActiveCamera().getZoomFactor() + distance * 0.01f);
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
    public ArrayList<BasicScene> getScenes() {
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
