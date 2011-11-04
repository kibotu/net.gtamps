package net.gtamps.android.game.content;

import net.gtamps.android.R;
import net.gtamps.android.core.renderer.graph.RenderableNode;
import net.gtamps.android.core.renderer.graph.scene.primitives.Cube;
import net.gtamps.android.core.renderer.graph.scene.primitives.ParsedObject;
import net.gtamps.android.core.renderer.graph.scene.primitives.Sphere;
import net.gtamps.shared.Utils.Logger;
import net.gtamps.shared.game.entity.Entity;

final public class Object3dFactory {

    public final static String TAG = Object3dFactory.class.getSimpleName();

    private Object3dFactory() {
    }

    public static RenderableNode create(String name) {
        return create(Entity.Type.valueOf(name.toUpperCase()));
    }

    public static RenderableNode create(Entity.Type type) {
        switch (type) {
            case CAR_CAMARO:
                return createCarCamaro();
            case CAR_RIVIERA:
                return createCarRiveria();
            case CAR_CHEVROLET_CORVETTE:
                return createCarChevroletCorvette();
            case HUMAN:
                return createHuman();
            case HOUSE:
                return createHouse();
            case BULLET:
                return createBullet();
            case SPAWNPOINT:
                return createSpawnPoint();
            case WAYPOINT:
                return createWayPoint();
            case CUBE:
                return createCube();
            case SPHERE:
                return createSphere();
            case PLACEHOLDER:
                return createCube();
            default:
                return createCube();
        }
    }

    private static RenderableNode createCarChevroletCorvette() {
        Logger.v(TAG, "Create chevrolet corvette_mtl.");
        RenderableNode parsedObject = ParsedObject.parseObject("corvette_obj", R.drawable.placeholder, true);
        RenderableNode parsedChild = (RenderableNode) parsedObject.get(0);
        parsedChild.enableColorMaterialEnabled(true);
        parsedChild.enableVertexColors(true);
//        parsedChild.setMaterial(Material.RED);
        parsedChild.enableNormals(false);
        parsedChild.enableTextures(false);
        parsedChild.enableDoubleSided(true);
        parsedChild.enableLighting(true);
        parsedChild.enableAlpha(false);
        parsedChild.enableMipMap(false);
        return parsedObject;
    }

    private static RenderableNode createCarRiveria() {
        Logger.v(TAG, "Create riveria.");
        RenderableNode parsedObject = ParsedObject.parseObject("riviera_obj", R.drawable.riviera, true);
        RenderableNode parsedChild = (RenderableNode) parsedObject.get(0);
        parsedChild.enableColorMaterialEnabled(false);
        parsedChild.enableVertexColors(false);
        parsedChild.enableNormals(true);
        parsedChild.enableTextures(true);
        parsedChild.enableDoubleSided(true);
        parsedChild.enableLighting(true);
        parsedChild.enableAlpha(true);
        parsedChild.enableMipMap(true);
        parsedChild.setScaling(5, 5, 5);
        return parsedObject;
    }

    private static RenderableNode createCarCamaro() {
        Logger.v(TAG, "Create camaro.");
        RenderableNode parsedObject = ParsedObject.parseObject("camaro_obj", R.drawable.camaro, true);
        RenderableNode parsedChild = (RenderableNode) parsedObject.get(0);
        parsedChild.enableColorMaterialEnabled(true);
        parsedChild.enableVertexColors(false);
        parsedChild.enableNormals(true);
        parsedChild.enableTextures(true);
        parsedChild.enableDoubleSided(false);
        parsedChild.enableLighting(true);
        parsedChild.enableAlpha(false);
        parsedChild.enableMipMap(true);
        return parsedObject;
    }

    private static RenderableNode createCube() {
        Logger.v(TAG, "Create cube.");
        return new Cube();
    }

    private static RenderableNode createSphere() {
        Logger.v(TAG, "Create sphere.");
        return new Sphere(1, 20, 10);
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
