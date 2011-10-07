package net.gtamps.android.game.primitives;

import java.util.ArrayList;

final public class Object3dFactory {

    public enum Type {
        CAR, HUMAN, HOUSE, BULLET, SPAWNPOINT
    }

    private Object3dFactory() {
    }

    public static IObject3d create(String name) {
        return create(Type.valueOf(name.toUpperCase()));
    }

    public static IObject3d create(Type type){
        IObject3d object3d = null;
        switch (type) {
            case CAR: object3d = createCar(); break;
            case HUMAN: object3d = createHuman(); break;
            case HOUSE: object3d = createHouse(); break;
            case BULLET: object3d = createBullet(); break;
            case SPAWNPOINT: object3d = createSpawnPoint(); break;
            default: object3d = new Cube(); break;
        }
        return object3d;
    }

    private static IObject3d createBullet() {
        return null;
    }

    private static IObject3d createSpawnPoint() {
        return null;
    }

    private static IObject3d createHouse() {
        return null;
    }

    private static IObject3d createHuman() {
        return null;
    }

    private static IObject3d createCar() {
        return new Car();
    }
}
