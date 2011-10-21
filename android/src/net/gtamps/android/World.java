package net.gtamps.android;

import net.gtamps.android.core.graph.CameraNode;
import net.gtamps.android.core.graph.LightNode;
import net.gtamps.android.core.utils.parser.IParser;
import net.gtamps.android.core.utils.parser.Parser;
import net.gtamps.android.game.Scene;
import net.gtamps.android.game.objects.City;
import net.gtamps.android.game.objects.IObject3d;
import net.gtamps.android.game.objects.Object3dFactory;
import net.gtamps.android.game.objects.ParsedObject;
import net.gtamps.shared.game.entity.Entity;

public class World {

    private CameraNode camera;
    private Scene scene;
    private LightNode light;
    public static IObject3d activeObject;

    public World() {
    }

    public void init() {

         // world
        scene = new Scene();

        camera =  new CameraNode(0, 0,40, 0, 0, 0, 0, 0, 1);
        scene.setActiveCamera(camera);
        scene.getBackground().setAll(0x111111);

        addCars();
        addParsedObject();

//        light = new LightNode();
//        light.setPosition(0,0,10);
//        light.setDirection(0, 0, -1);
//        light.ambient.setAll(64, 64, 64, 255);
//        light.diffuse.setAll(128,128,128,255);
//        light.specular.setAll(64,64,64,255);
//        scene.add(light);


//
		LightNode spot = new LightNode();
		spot.setPosition(0, 2, 3);
        spot.setDirection(0,0,-1);
		spot.diffuse.setAll(255,255,255, 255);
		spot.ambient.setAll(0, 0, 0, 0);
		spot.specular.setAll(255, 255, 255, 255);
		spot.emissive.setAll(0, 0, 0, 0);
        spot.setType(LightNode.Type.POSITIONAL);
        spot.setSpotCutoffAngle(60);
        spot.setSpotExponent(4);
        spot.setAttenuation(0.6f,0,0);
		activeObject.getNode().add(spot);
        spot.setRotation(60,0,0);

        LightNode sun = new LightNode();
		sun.setPosition(0, 0, 20);
        sun.setDirection(0,0,-1);
		sun.diffuse.setAll(128,128,128, 255);
		sun.ambient.setAll(0, 0, 0, 0);
		sun.specular.setAll(128, 128, 128, 255);
		sun.emissive.setAll(0, 0, 0, 0);
        sun.setType(LightNode.Type.POSITIONAL);
        sun.setSpotCutoffAngle(60);
        sun.setSpotExponent(4);
        sun.setAttenuation(0.5f,0,0);
		scene.add(sun);

//        addPlane();
        addCity();
    }

    private void addPlane() {
        IParser objParser = Parser.createParser(Parser.Type.OBJ, "net.gtamps.android:raw/grid_obj", true);
        objParser.parse();
        ParsedObject parsedObject = objParser.getParsedObject();
        parsedObject.updateVbo();
        parsedObject.setTextureId(Registry.getTextureLibrary().loadTexture(R.drawable.grid, true));
        parsedObject.setScaling(15, 15, 1);
//        parsedObject.setDrawingStyle(OpenGLUtils.DrawingStyle.GL_LINES);
        scene.addChild(parsedObject);
    }

    private void addParsedObject() {
        IParser objParser = Parser.createParser(Parser.Type.OBJ, "net.gtamps.android:raw/camaro_obj", true);
        objParser.parse();
        ParsedObject parsedObject = objParser.getParsedObject();
        parsedObject.updateVbo();
        parsedObject.setTextureId(Registry.getTextureLibrary().loadTexture(R.drawable.camaro, true));
        scene.addChild(activeObject = parsedObject);
        parsedObject.getNode().setScaling(5,5,5);
    }

    private void addCars() {
        scene.addChild(activeObject = Object3dFactory.create(Entity.Type.CAR_CHEVROLET_CORVETTE));
        IObject3d camaro = Object3dFactory.create(Entity.Type.CAR_CAMARO);
        camaro.getNode().setPosition(-15,0,0);
        camaro.getNode().setScaling(5,5,5);
        IObject3d riviera = Object3dFactory.create(Entity.Type.CAR_RIVIERA);
        riviera.getNode().setPosition(15,0,0);
        riviera.getNode().setScaling(35,35,35);
        scene.addChild(camaro);
        scene.addChild(riviera);
    }

    private void addCity() {
        City city = new City();
        scene.add(city);
        city.setRotation(90, 0, 0);
        city.setScaling(35, 35, 35);
    }

    public Scene getScene() {
        if(scene==null) {
            init();
        }
        return scene;
    }

    public static IObject3d getActiveObject() {
        return activeObject;
    }
}
