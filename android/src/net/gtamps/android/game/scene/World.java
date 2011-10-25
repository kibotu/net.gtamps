package net.gtamps.android.game.scene;

import net.gtamps.android.R;
import net.gtamps.android.core.Registry;
import net.gtamps.android.core.renderer.graph.primitives.Camera;
import net.gtamps.android.core.renderer.graph.primitives.Light;
import net.gtamps.android.core.renderer.mesh.parser.IParser;
import net.gtamps.android.core.renderer.mesh.parser.Parser;
import net.gtamps.android.game.PlayerManager;
import net.gtamps.android.game.objects.EntityView;
import net.gtamps.android.game.objects.City;
import net.gtamps.android.game.objects.IObject3d;
import net.gtamps.android.game.objects.ParsedObject;
import net.gtamps.shared.game.entity.Entity;
import org.jetbrains.annotations.NotNull;

public class World {

    private Camera camera;
    private Scene scene;
    private EntityView activeOjbect;
    public final PlayerManager playerManager;

    public World() {
        playerManager = new PlayerManager();
    }

    public void init() {

         // world
        scene = new Scene();

        camera =  new Camera(0, 0,40, 0, 0, 0, 0, 0, 1);
        scene.setActiveCamera(camera);
        scene.getBackground().setAll(0xff222222);

//        scene.addChild(addCity());
        scene.add(getSunLight());
        IObject3d camaro = addCamaro();
        activeOjbect = new EntityView(new Entity("car_riviera"));
        activeOjbect.getObject3d().getNode().setScaling(30,30,30);
        activeOjbect.getObject3d().getNode().add(getSpotLight());
//        activeOjbect.setObject3d(camaro);
        scene.addChild(activeOjbect);
//
//        IObject3d spawnPoint = Object3dFactory.create(Entity.Type.SPAWNPOINT);
//        spawnPoint.getNode().setPosition(activeOjbect.getNode().getPosition());
//        spawnPoint.getNode().setPosition(3,0,0);
//        scene.addChild(spawnPoint);

//        addLevel();
    }

    public static Light getSpotLight() {
        Light spot = new Light();
		spot.setPosition(0, 2, 3);
        spot.setDirection(0,0,-1);
		spot.diffuse.setAll(255,255,255, 255);
		spot.ambient.setAll(0, 0, 0, 0);
		spot.specular.setAll(255, 255, 255, 255);
		spot.emissive.setAll(0, 0, 0, 0);
        spot.setType(Light.Type.POSITIONAL);
        spot.setSpotCutoffAngle(60);
        spot.setSpotExponent(4);
        spot.setAttenuation(0.6f, 0, 0);
        spot.setRotation(60, 0, 0);
        return spot;
    }

    public static Light getSunLight() {
        Light sun = new Light();
		sun.setPosition(0, 0, 20);
        sun.setDirection(0,0,-1);
		sun.diffuse.setAll(128, 128, 128, 255);
		sun.ambient.setAll(0, 0, 0, 0);
		sun.specular.setAll(128, 128, 128, 255);
		sun.emissive.setAll(0, 0, 0, 0);
        sun.setType(Light.Type.POSITIONAL);
        sun.setSpotCutoffAngle(60);
        sun.setSpotExponent(4);
        sun.setAttenuation(0.5f,0,0);
        return sun;
    }

    public static IObject3d addPlane() {
        IParser objParser = Parser.createParser(Parser.Type.OBJ, "net.gtamps.android:raw/grid_obj", true);
        objParser.parse();
        ParsedObject parsedObject = objParser.getParsedObject();
        parsedObject.updateVbo();
        parsedObject.setTextureId(Registry.getTextureLibrary().loadTexture(R.drawable.grid, true));
        parsedObject.setScaling(40, 40, 1);
//        parsedObject.setDrawingStyle(OpenGLUtils.DrawingStyle.GL_LINES);
        return parsedObject;
    }

    public static IObject3d addCamaro() {
        IParser objParser = Parser.createParser(Parser.Type.OBJ, "net.gtamps.android:raw/camaro_obj", true);
        objParser.parse();
        ParsedObject parsedObject = objParser.getParsedObject();
        parsedObject.updateVbo();
        parsedObject.setTextureId(Registry.getTextureLibrary().loadTexture(R.drawable.camaro, true));
        parsedObject.getNode().setScaling(5, 5, 5);
        parsedObject.enableDoubleSided(true);
        return parsedObject;
    }

    public static IObject3d addLevel() {
        IParser objParser = Parser.createParser(Parser.Type.OBJ, "net.gtamps.android:raw/map1_obj", true);
        objParser.parse();
        ParsedObject parsedObject = objParser.getParsedObject();
        parsedObject.updateVbo();
        parsedObject.setTextureId(Registry.getTextureLibrary().loadTexture(R.drawable.grid, true));
        parsedObject.setScaling(0.5f, 0.5f, 0.5f);
        parsedObject.setPosition(0, 0, -3f);
        return parsedObject;
    }

//    public static void addCars() {
//        scene.addChild(activeObject = Object3dFactory.create(Entity.Type.CAR_CHEVROLET_CORVETTE));
//        IObject3d camaro = Object3dFactory.create(Entity.Type.CAR_CAMARO);
//        camaro.getNode().setPosition(-15,0,0);
//        camaro.getNode().setScaling(5,5,5);
//        IObject3d riviera = Object3dFactory.create(Entity.Type.CAR_RIVIERA);
//        riviera.getNode().setPosition(15,0,0);
//        riviera.getNode().setScaling(35,35,35);
//        scene.addChild(camaro);
//        scene.addChild(riviera);
//    }

    public static IObject3d addCity() {
        City city = new City();
        city.setRotation(90, 0, 0);
        city.setScaling(35, 35, 35);
        return city;
    }

    public Scene getScene() {
        if(scene==null) {
            init();
        }
        return scene;
    }

    public EntityView getActiveObject() {
        return activeOjbect;
    }

    public void setActiveObject(@NotNull EntityView entityView) {
        this.activeOjbect = entityView;
    }
}
