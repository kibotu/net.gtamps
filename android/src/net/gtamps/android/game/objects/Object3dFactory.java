package net.gtamps.android.game.objects;

import net.gtamps.android.core.renderer.graph.RenderableNode;
import net.gtamps.android.core.renderer.graph.primitives.Cube;
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
            case PLACEHOLDER: object3d = createCube(); break;
            default: object3d = createCube(); break;
        }
        return object3d;
    }

    private static RenderableNode createCarChevroletCorvette() {
//        Object3d car = new Car(Car.Type.CHEVROLET_CORVETTE);
//        ((PureVboNode)car.getNode()).enableTextures(false);
        return null;
    }

    private static RenderableNode createCarRiveria() {
//        Object3d object3d = new Car(Car.Type.RIVIERA);
//        object3d.getNode().setScaling(5,5,5);
        return null;
    }

    private static RenderableNode createCarCamaro() {
        return new Car(Car.Type.CAMARO);
    }

    private static RenderableNode createCube() {
        return new Cube();
    }

    @Deprecated
    public static RenderableNode createParsedObject(String resource, String textureResource) {
        return new ParsedObject(0,0);
    }

    private static RenderableNode createWayPoint() {
        return createCube();
    }

    private static RenderableNode createBullet() {
        return createCube();
    }

    private static RenderableNode createSpawnPoint() {
        return createCube();
    }

    private static RenderableNode createHouse() {
        return createCube();
    }

    private static RenderableNode createHuman() {
        return createCarRiveria();
    }
}
