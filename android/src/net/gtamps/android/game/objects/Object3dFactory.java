package net.gtamps.android.game.objects;

import net.gtamps.android.core.renderer.graph.PureVboNode;
import net.gtamps.android.game.Game;
import net.gtamps.shared.game.entity.Entity;

final public class Object3dFactory {

    private static final String TAG = Game.class.getSimpleName();

    private Object3dFactory() {
    }

    public static IObject3d create(String name) {
        return create(Entity.Type.valueOf(name.toUpperCase()));
    }

    public static IObject3d create(Entity.Type type){
        IObject3d object3d = null;
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
            default: object3d = new Cube(); break;
        }
        return object3d;
    }

    private static IObject3d createCarChevroletCorvette() {
        IObject3d car = new Car(Car.Type.CHEVROLET_CORVETTE);
        ((PureVboNode)car.getNode()).enableTextures(false);
        return car;
    }

    private static IObject3d createCarRiveria() {
        IObject3d object3d = new Car(Car.Type.RIVIERA);
        object3d.getNode().setScaling(5,5,5);
        return object3d;
    }

    private static IObject3d createCarCamaro() {
        return new Car(Car.Type.CAMARO);
    }

    private static IObject3d createCube() {
        return new Cube();
    }

    @Deprecated
    public static IObject3d createParsedObject(String resource, String textureResource) {
        return new ParsedObject(0,0);
    }

    private static IObject3d createWayPoint() {
        return createCube();
    }

    private static IObject3d createBullet() {
        return createCube();
    }

    private static IObject3d createSpawnPoint() {
        return createCube();
    }

    private static IObject3d createHouse() {
        return createCube();
    }

    private static IObject3d createHuman() {
        return createCarRiveria();
    }
}
