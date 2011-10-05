package net.gtamps.android.game;

import android.os.SystemClock;
import android.util.Xml;
import net.gtamps.android.R;
import net.gtamps.android.core.graph.*;
import net.gtamps.android.core.input.InputEngine;
import net.gtamps.android.core.math.Vector3;
import net.gtamps.android.core.utils.Utils;
import net.gtamps.android.core.utils.parser.IParser;
import net.gtamps.android.core.utils.parser.ObjParser;
import net.gtamps.android.core.utils.parser.Parser;
import net.gtamps.android.game.client.Connector;
import net.gtamps.android.game.client.IStream;
import net.gtamps.android.game.client.TcpStream;
import net.gtamps.android.game.primitives.*;
import net.gtamps.android.game.state.State;

import javax.microedition.khronos.opengles.GL;
import java.util.ArrayList;

public class Game implements IGame{

    private static final String TAG = Game.class.getSimpleName();

    private boolean isRunning;
    private boolean isPaused;
    private long startTime;
    private InputEngine inputEngine;
    private ArrayList<Scene> scenes;
    private boolean isDragging = false;
    private Connector connector;
    private IStream stream;
    StringBuilder builder = new StringBuilder();

    public Game() {
        isRunning = true;
        isPaused = false;
        inputEngine = InputEngine.getInstance();
        scenes = new ArrayList<Scene>();
    }

    public void onCreate() {

        // TODO connect
//        initConnection();

        // TODO load lvl

        createWorld();
//        createParsedObject();
        createHud();


        // TODO handle sound
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

    private void initConnection() {
        stream = new TcpStream();
        String ip = "141.64.23.78";
        int port = 8090;
        Utils.log(TAG, "\n\n\n\n\nConnecting to " + ip + ":" + port + " " + (stream.connect(ip,port) ? "successful." : "failed.")+"\n\n\n\n\n");
        stream.startListening();

        // random message
        for(int i= 0; i < 1022; i++) {
            builder.append("x");
        }
    }

    Sprite ring;
    Sprite cursor;
    private void createHud() {
        // create scene
        Scene scene = new Scene();
        // add it to renderer
        scenes.add(scene);
        // create camera
        CameraNode camera =  new CameraNode(0, 0,1, 0, 0, 0, 0, 1, 0);
        scene.setActiveCamera(camera);
        camera.enableDepthTest(false);

        ring = new Sprite();
        scene.add(ring);

        cursor = new Sprite();
        scene.add(cursor);

        ring.loadBufferedTexture(R.drawable.hud, R.raw.hud, true);
        cursor.loadBufferedTexture(R.drawable.hud,R.raw.hud,true);
        cursor.animate(0.51f, State.Type.IDLE);

        inputEngine.setCamera(camera);
    }

    Car car;
    private void createWorld() {

        // create scene
        Scene scene = new Scene();
        // add it to renderer
        scenes.add(scene);

        // create camera
        CameraNode camera =  new CameraNode(0, 0,40, 0, 0, 0, 0, 1, 0);
        // set that camera active within scene
        scene.setActiveCamera(camera);
        scene.getBackground().setAll(0x111111);

//        addCube(scene);

        LightNode sun = new LightNode();
        sun.setPosition(0,0,30);
        sun.setDirection(0, 0, -1);
        sun.ambient.setAll(64, 64, 64, 255);
        sun.diffuse.setAll(128,128,128,255);
        sun.specular.setAll(64,64,64,255);
        scene.add(sun);

        car = new Car();
        scene.add(car);
//		light = new LightNode();
//		light.setPosition(car.getAABB().getWidth()*0.1f, car.getAABB().getHeight(), 0.2f);
//		light.diffuse.setAll(255, 255, 0, 255);
//		light.ambient.setAll(128, 128, 0, 255);
//		light.specular.setAll(128,128, 0, 255);
//		light.emissive.setAll(0, 0, 0, 0);
//        light.setType(LightNode.Type.POSITIONAL);
//        light.setAttenuation(0,0,0);
////        light.setRotation(10,0,10);
//        light.setDirection(0,3f,0);
//        light.setSpotExponent(0);
//        light.setSpotCutoffAngle(15);
//		car.add(light);

//        LightNode light2 = new LightNode();
//		light2.setPosition(car.getAABB().getWidth()*0.9f, car.getAABB().getHeight(), 0.2f);
//		light2.diffuse.setAll(255, 255, 0, 255);
//		light2.ambient.setAll(128, 128, 0, 255);
//		light2.specular.setAll(128,128, 0, 255);
//		light2.emissive.setAll(0, 0, 0, 0);
//        light2.setType(LightNode.Type.POSITIONAL);
//        light2.setAttenuation(0,0,0);
////        light.setRotation(10,0,10);
//        light2.setDirection(0,3f,0);
//        light2.setSpotExponent(10);
//        light2.setSpotCutoffAngle(15);
//        car.add(light2);

//        addCube(scene);
//        addSphere(scene);
//
        City city = new City();
        scene.add(city);
        city.setRotation(90,0,0);
        city.setScaling(20,20,20);
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

//            Utils.log(TAG, "\n\n\n\n\nSending message "+(stream.send(builder.toString().getBytes()) ? "successful." : "failed.")+"\n\n\n\n\n");

        }

        // on release
        if (inputEngine.getUpState()){
//            Utils.log(TAG, "finger up");
            isDragging = false;
            cursor.setPosition(inputEngine.getPointerPosition());
        }

        speed+=0.01f;
        if(isDragging) {
//            Utils.log(TAG, "is dragging");
            Vector3 viewportSize = scenes.get(1).getActiveCamera().getViewportSize();
            Vector3 pos = inputEngine.getPointerPosition();
            Vector3 temp = pos.sub(viewportSize).mulInPlace(1).addInPlace(viewportSize);
            cursor.setPosition(temp);
            car.getPosition().addInPlace(temp);
            scenes.get(0).getActiveCamera().moveXY(car.getPosition());
            ring.setRotation(0,0,speed);
            temp.recycle();
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
