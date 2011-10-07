package net.gtamps.android.game;

import android.os.SystemClock;
import net.gtamps.android.R;
import net.gtamps.android.core.graph.*;
import net.gtamps.android.core.input.InputEngine;
import net.gtamps.android.game.client.MessageFactory;
import net.gtamps.android.game.client.StreamInputListener;
import net.gtamps.android.game.entity.views.Hud;
import net.gtamps.shared.Config;
import net.gtamps.shared.communication.Command;
import net.gtamps.shared.math.Vector3;
import net.gtamps.android.core.utils.Utils;
import net.gtamps.android.core.utils.parser.IParser;
import net.gtamps.android.core.utils.parser.Parser;
import net.gtamps.android.game.client.IStream;
import net.gtamps.android.game.client.TcpStream;
import net.gtamps.android.game.objects.*;
import net.gtamps.shared.state.State;

import java.util.ArrayList;

public class Game implements IGame{

    private static final String TAG = Game.class.getSimpleName();

    private boolean isRunning;
    private boolean isPaused;
    private long startTime;
    private InputEngine inputEngine;
    private ArrayList<Scene> scenes;
    private boolean isDragging = false;
    private Hud hud;
    private TcpStream stream;

    public Game() {
        isRunning = true;
        isPaused = false;
        inputEngine = InputEngine.getInstance();
        scenes = new ArrayList<Scene>();
        hud = new Hud();
    }

    public void onCreate() {

        LightNode sun = new LightNode();
        sun.setPosition(0,0,30);
        sun.setDirection(0, 0, -1);
        sun.ambient.setAll(64, 64, 64, 255);
        sun.diffuse.setAll(128,128,128,255);
        sun.specular.setAll(64,64,64,255);

        // world
        Scene scene = new Scene();
        scenes.add(scene);
        scene.add(sun);
        scene.add(new Car());

        City city = new City();
        scene.add(city);
        city.setRotation(90, 0, 0);
        city.setScaling(20, 20, 20);

        CameraNode camera =  new CameraNode(0, 0,40, 0, 0, 0, 0, 1, 0);
        // set that camera active within scene
        scene.setActiveCamera(camera);
        scene.getBackground().setAll(0x111111);

        // hud
        scenes.add(hud.getScene());

        // connect
        stream = new TcpStream();
        StreamInputListener listener = new StreamInputListener();
        stream.connect(Config.SERVER_HOST_ADDRESS, Config.SERVER_PORT);
        Utils.log(TAG, "\n\n\n\n\nConnecting to " + Config.SERVER_HOST_ADDRESS + ":" + Config.SERVER_PORT + " " + (stream.isConnected() ? "successful." : "failed.") + "\n\n\n\n\n");
        stream.addStreamInputListener(listener);

        if(stream.isConnected()) stream.start();
    }

    private void createParsedObject() {

        Scene scene = new Scene();
        scenes.add(scene);

        CameraNode camera =  new CameraNode(0, 0,40, 0, 0, 0, 0, 1, 0);
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

    private void addSphere(Scene scene) {
        Sphere sphere = new Sphere();
        sphere.setScaling(3, 3, 3);
        sphere.setPosition(0, 0, 0);
        scene.add(sphere);
    }

    private Cube addCube(Scene scene) {
        Cube cube = new Cube();
        cube.setScaling(10,10,10);
        cube.setPosition(0,0,-10);
        scene.add(cube);
        cube.loadTexture(R.drawable.crate,true);
        return cube;
    }

    float speed = 0;

    @Override
    public void onDrawFrame() {
        if (!isRunning || isPaused) {
            return;
        }

        // zoom
        if (inputEngine.getZoomState()){
            setZoomByDistance(inputEngine.getZoomDistance());
        }

        // on touch
        if (inputEngine.getDownState()){
//            Utils.log(TAG, "finger down");
            isDragging = true;
            if(stream.isConnected()) stream.send(MessageFactory.createCommand(Command.ACCELERATE).toString().getBytes());

//            Utils.log(TAG, "\n\n\n\n\nSending message "+(stream.send(builder.toString().getBytes()) ? "successful." : "failed.")+"\n\n\n\n\n");

        }

        // on release
        if (inputEngine.getUpState()){
//            Utils.log(TAG, "finger up");
            isDragging = false;
            hud.getCursor().setPosition(inputEngine.getPointerPosition());
        }

        speed+=0.01f;
        if(isDragging) {
//            Utils.log(TAG, "is dragging");
            Vector3 viewportSize = scenes.get(1).getActiveCamera().getViewportSize();
            Vector3 pos = inputEngine.getPointerPosition();
            Vector3 temp = pos.sub(viewportSize).mulInPlace(1).addInPlace(viewportSize);
            hud.getCursor().setPosition(temp);
//            car.getPosition().addInPlace(temp);
//            scenes.get(0).getActiveCamera().moveXY(car.getPosition());
//            hud.getRing().setRotation(0, 0, speed);
//            temp.recycle();
        }

        // Compute elapsed time
        final long finalDelta = SystemClock.elapsedRealtime() - startTime;

        // new start time
        startTime = SystemClock.elapsedRealtime();
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
