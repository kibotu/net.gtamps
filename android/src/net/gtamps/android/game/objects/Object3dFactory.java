package net.gtamps.android.game.objects;

import net.gtamps.shared.game.entity.Entity;

final public class Object3dFactory {

    private Object3dFactory() {
    }

    public static IObject3d create(String name) {
        return create(Entity.Type.valueOf(name.toUpperCase()));
    }

    public static IObject3d create(Entity.Type type){
        IObject3d object3d = null;
        switch (type) {
            case CAR: object3d = createCar(); break;
            case HUMAN: object3d = createHuman(); break;
            case HOUSE: object3d = createHouse(); break;
            case BULLET: object3d = createBullet(); break;
            case SPAWNPOINT: object3d = createSpawnPoint(); break;
            case WAYPOINT: object3d = createWayPoint(); break;
            default: object3d = new Cube(); break;
        }
        return object3d;
    }

    @Deprecated
    public static IObject3d createParsedObject(String resource, String textureResource) {
        return new ParsedObject(0,0);
    }

    private static IObject3d createWayPoint() {
        return new Cube();
    }

    private static IObject3d createBullet() {
        return new Cube();
    }

    private static IObject3d createSpawnPoint() {
        return new Cube();
    }

    private static IObject3d createHouse() {
        return new Cube();
    }

    private static IObject3d createHuman() {
        return new Cube();
    }

    private static IObject3d createCar() {
        return new Car();
    }
}
