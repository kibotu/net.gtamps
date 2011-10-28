package net.gtamps.android.game.objects;

import net.gtamps.android.R;
import net.gtamps.android.core.renderer.graph.RenderableNode;
import net.gtamps.android.core.renderer.graph.primitives.Cube;
import net.gtamps.android.core.renderer.graph.primitives.Sphere;
import net.gtamps.android.core.renderer.mesh.parser.IParser;
import net.gtamps.android.core.renderer.mesh.parser.Parser;
import net.gtamps.shared.Utils.Logger;
import net.gtamps.shared.game.entity.Entity;

final public class Object3dFactory {

    public final static String TAG = Object3dFactory.class.getSimpleName();

    private Object3dFactory() {
    }

    public static RenderableNode create(String name) {
        return create(Entity.Type.valueOf(name.toUpperCase()));
    }

    public static RenderableNode create(Entity.Type type){
        RenderableNode object3d = null;
        switch (type) {
            case CAR_CAMARO: object3d = createCarCamaro(); break;
            case CAR_RIVIERA: object3d = createCarRiveria(); break;
            case CAR_CHEVROLET_CORVETTE: object3d = createCarChevroletCorvette(); break;
            case HUMAN: object3d = createHuman(); break;
            case HOUSE: object3d = createHouse(); break;
            case BULLET: object3d = createBullet(); break;
            case SPAWNPOINT: object3d = createSpawnPoint(); break;
            case WAYPOINT: object3d = createWayPoint(); break;
            case CUBE: object3d = createCube(); break;
            case SPHERE: object3d = createSphere(); break;
            case PLACEHOLDER: object3d = createCube(); break;
            default: object3d = createCube(); break;
        }
        return object3d;
    }

    private static RenderableNode createCarChevroletCorvette() {
        Logger.v(TAG, "Create chevrolet corvette.");
//        Object3d car = new Car(Car.Type.CHEVROLET_CORVETTE);
//        ((PureVboNode)car.getNode()).enableTextures(false);
        return null;
    }

    private static RenderableNode createCarRiveria() {
        Logger.v(TAG, "Create riveria.");
//        Object3d object3d = new Car(Car.Type.RIVIERA);
//        object3d.getNode().setScaling(5,5,5);
        return null;
    }

    private static RenderableNode createCarCamaro() {
        Logger.v(TAG, "Create camaro.");
        return createParsedObject("camaro_obj", R.drawable.camaro);
    }

    private static RenderableNode createCube() {
        Logger.v(TAG, "Create cube.");
        return new Cube();
    }

    private static RenderableNode createSphere() {
        Logger.v(TAG, "Create sphere.");
        return new Sphere(1,20,10);
    }

    public static RenderableNode createParsedObject(String objname, int textureResource) {
        Logger.v(TAG, "Parsing object...");
        IParser objParser = Parser.createParser(Parser.Type.OBJ, "net.gtamps.android:raw/"+objname, true);
        objParser.parse();
        ParsedObject parsedObject = objParser.getParsedObject();
        return parsedObject;
    }

    private static RenderableNode createWayPoint() {
        Logger.v(TAG, "Create way point.");
        return createCube();
    }

    private static RenderableNode createBullet() {
        Logger.v(TAG, "Create bullet.");
        return createCube();
    }

    private static RenderableNode createSpawnPoint() {
        Logger.v(TAG, "Create spawnpoint.");
        return createCube();
    }

    private static RenderableNode createHouse() {
        Logger.v(TAG, "Create house.");
        return createCube();
    }

    private static RenderableNode createHuman() {
        Logger.v(TAG, "Create human.");
        return createCarRiveria();
    }
}
