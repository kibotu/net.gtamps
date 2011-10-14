package net.gtamps.android.game;

import android.os.SystemClock;
import net.gtamps.android.R;
import net.gtamps.android.World;
import net.gtamps.android.core.graph.*;
import net.gtamps.android.core.input.InputEngine;
import net.gtamps.android.game.client.ConnectionManager;
import net.gtamps.android.game.entity.views.EntityView;
import net.gtamps.shared.communication.MessageFactory;
import net.gtamps.android.game.entity.views.Hud;
import net.gtamps.shared.Config;
import net.gtamps.shared.communication.*;
import net.gtamps.shared.game.entity.Entity;
import net.gtamps.shared.math.Vector3;
import net.gtamps.android.core.utils.Utils;
import net.gtamps.android.core.utils.parser.IParser;
import net.gtamps.android.core.utils.parser.Parser;
import net.gtamps.android.game.objects.*;

import java.util.ArrayList;

public class Game implements IGame{

    private static final String TAG = Game.class.getSimpleName();

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

        IObject3d object3d = Object3dFactory.create(Entity.Type.PLACEHOLDER);
        world.getScene().addChild(object3d);
        object3d.getNode().setPosition(10,10,3);

        // hud
        scenes.add(hud.getScene());

        // connect
        connection.connect();
        Utils.log(TAG, "\n\n\n\n\nConnecting to " + Config.SERVER_HOST_ADDRESS + ":" + Config.SERVER_PORT + " " + (connection.isConnected() ? "successful." : "failed.") + "\n\n\n\n\n");
        connection.start();

        connection.add(MessageFactory.createSessionRequest());

//        connection.add(MessageFactory.createRegisterRequest("username", "password"));
//        connection.add(MessageFactory.createLoginRequest("username", "password"));
//        connection.add(MessageFactory.createJoinRequest());
//        connection.add(MessageFactory.createGetPlayerRequest());
    }

    @Deprecated
    private void createParsedObject() {

        Scene scene = new Scene();
        scenes.add(scene);

        CameraNode camera =  new CameraNode(0, 0,10, 0, 0, 0, 0, 1, 0);
        scene.setActiveCamera(camera);
        scene.getBackground().setAll(0x222222);

        LightNode sun = new LightNode();
        sun.setPosition(0,0,30);
        sun.setDirection(0, 0, -1);
        sun.ambient.setAll(64, 64, 64, 255);
        sun.diffuse.setAll(128,128,128,255);
        sun.specular.setAll(64,64,64,255);
        scene.add(sun);

        IParser objParser = Parser.createParser(Parser.Type.OBJ,"net.gtamps.android:raw/camaro_obj",true);
        objParser.parse();
        scene.addChild(objParser.getParsedObject());
    }

    @Deprecated
    private void addSphere(Scene scene) {
        Sphere sphere = new Sphere();
        sphere.setScaling(3, 3, 3);
        sphere.setPosition(0, 0, 0);
        scene.add(sphere);
    }

    @Deprecated
    private Cube addCube(Scene scene) {
        Cube cube = new Cube();
        cube.setScaling(10,10,10);
        cube.setPosition(0,0,-10);
        scene.add(cube);
        cube.loadTexture(R.drawable.crate,true);
        return cube;
    }

    @Override
    public void onDrawFrame() {
        if (!isRunning || isPaused) {
            return;
        }

        // handle inbox messages
        while(!connection.isEmpty()) {
            Message message = connection.poll();
            for(int i = 0; i < message.sendables.size(); i++) {
                ISendable sendable = message.sendables.get(i);
                // message is request
//                if(sendable instanceof Request) {
//                    handleRequest((Request) sendable, message);
//                }
                // message is response
                if(sendable instanceof Response) {
                    handleResponse((Response) sendable, message);
                }
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
	        if(connection.isConnected()) connection.add(MessageFactory.createGetUpdateRequest(ConnectionManager.currentRevId));
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
            world.getActiveObject().getNode().getPosition().addInPlace(temp);
//            scenes.get(0).getActiveCamera().move(temp);

            float angle = Vector3.XAXIS.angleInBetween(pos)-90;
            world.getActiveObject().getNode().setRotation(0, 0, angle);
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


    private long impulse = 0;
    private void fireImpulse(float angle, Vector3 force) {
        if(!connection.isConnected()) return;

        impulse += finalDelta;
        if(impulse <= Config.IMPULS_FREQUENCY) return;
        impulse = 0;

        Message message = MessageFactory.createGetUpdateRequest(ConnectionManager.currentRevId);

        // up° 22.5 to -22.5°
        if(inRange(angle, 22.5f, -22.5f)) {
            message.addSendable(new Command(Command.Type.ACCELERATE,force.getLength()));
        }

        // right up -22.5° to -67.5°

        // right -67.5° to -112.5°
        if(inRange(angle, -67.5f, -112.5f)) {
            message.addSendable(new Command(Command.Type.RIGHT,force.getLength()));
        }

        // right down -112.5° to -157.5°

        // down -157.5° to -180° and 180° to 157.5°
        if(inRange(angle, -157.5f, -180f) || inRange(angle, 180f,157.5f)) {
            message.addSendable(new Command(Command.Type.DECELERATE,force.getLength()));
        }

        // left down 157.5° to 112.5°

        // left 112.5° to 67.5°
        if(inRange(angle, 112.5f, 67.5f)) {
            message.addSendable(new Command(Command.Type.LEFT,force.getLength()));
        }

        // left up 67.5° to 22.5°

        connection.add(message);
    }

    private boolean inRange(float value, float startRange, float endRange) {
        return value >= Math.min(startRange,endRange) && value <= Math.max(startRange,endRange);
    }

    private void handleResponse(Response response, Message message) {
        Utils.log(TAG, "Handles response.");
        Utils.log(TAG, ""+response.toString());

        switch (response.requestType) {
            case GETUPDATE:
                if(response.getData() == null) break;
                if(!(response.getData() instanceof UpdateData)) break;
                switch (response.status) {
                    case OK:
                        UpdateData updateData = ((UpdateData)response.getData());
                        ConnectionManager.currentRevId = updateData.revId;

                        ArrayList<Entity> entities = updateData.entites;
                        for(int i = 0; i < entities.size(); i++) {
                            Utils.log(TAG, "response size"+entities.size());
                            Entity serverEntity = entities.get(i);

                            // contains?
                            EntityView entityView = world.getScene().getObject3DById(serverEntity.getUid());
                            if(entityView == null) {
                                entityView = new EntityView(serverEntity);
                                world.getScene().addChild(entityView);
                                Utils.log(TAG, "add new entity"+serverEntity.getUid());
                            } else {
                                entityView.update(serverEntity);
                                Utils.log(TAG, "update entity"+serverEntity.getUid());
                            }
                        }
                        break;
                    case BAD: break;
                    case NEED: break;
                    case ERROR: break;
                    default: break;
                }
                break;
            case GETPLAYER:
                switch (response.status) {
                    case OK: break;
                    case BAD: break;
                    case NEED: break;
                    case ERROR: break;
                    default: break;
                }
                break;
            case SESSION:
                switch (response.status) {
                    case OK:
                        ConnectionManager.currentSessionId = message.getSessionId();
                        break;
                    case BAD: break;
                    case NEED: break;
                    case ERROR: break;
                    default: break;
                }
                break;
            case JOIN:
                switch (response.status) {
                    case OK: break;
                    case BAD: break;
                    case NEED: break;
                    case ERROR: break;
                    default: break;
                }
                break;
            case GETMAPDATA:
                switch (response.status) {
                    case OK: break;
                    case BAD: break;
                    case NEED: break;
                    case ERROR: break;
                    default: break;
                }
                break;
            case LEAVE:
                switch (response.status) {
                    case OK: break;
                    case BAD: break;
                    case NEED: break;
                    case ERROR: break;
                    default: break;
                }
                break;
            case LOGIN:
                switch (response.status) {
                    case OK: break;
                    case BAD: break;
                    case NEED: break;
                    case ERROR: break;
                    default: break;
                }
                break;
            case REGISTER:
                switch (response.status) {
                    case OK: break;
                    case BAD: break;
                    case NEED: break;
                    case ERROR: break;
                    default: break;
                }
                break;
            default: break;
        }
    }

    private void handleRequest(Request request, Message message) {
        Utils.log(TAG, "Handles request.");
        Utils.log(TAG, ""+request.toString());

        switch (request.type) {
            case GETUPDATE: break;
            case GETPLAYER: break;
            case JOIN: break;
            case SESSION: break;
            case GETMAPDATA: break;
            case LEAVE: break;
            case LOGIN: break;
            case REGISTER: break;
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
