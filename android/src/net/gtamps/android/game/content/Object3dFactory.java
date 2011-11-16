package net.gtamps.android.game.content;

import net.gtamps.android.R;
import net.gtamps.android.core.renderer.graph.RenderableNode;
import net.gtamps.android.core.renderer.graph.scene.primitives.Cube;
import net.gtamps.android.core.renderer.graph.scene.primitives.ParsedObject;
import net.gtamps.android.core.renderer.graph.scene.primitives.Sphere;
import net.gtamps.shared.Utils.Logger;
import net.gtamps.shared.game.entity.Entity;

import java.util.HashMap;

final public class Object3dFactory {

    public final static String TAG = Object3dFactory.class.getSimpleName();

    private static final HashMap<Entity.Type, RenderableNode> cache = new HashMap<Entity.Type, RenderableNode>(10);

    private Object3dFactory() {
    }

    public static RenderableNode create(String name) {
        return create(Entity.Type.valueOf(name.toUpperCase()));
    }

    public static RenderableNode create(Entity.Type type) {

        if(cache.containsKey(type)) {
//            return cache.get(type).getStatic();
        }

        RenderableNode node;

        switch (type) {
            case CAR_CAMARO:
                node = createCarCamaro();
                break;
            case CAR_RIVIERA:
                node = createCarRiveria();
                break;
            case CAR_CHEVROLET_CORVETTE:
                node = createCarChevroletCorvette();
                break;
            case HUMAN:
                node = createHuman();
                break;
            case HOUSE:
                node = createHouse();
                break;
            case BULLET:
                node = createBullet();
                break;
            case SPAWNPOINT:
                node = createSpawnPoint();
                break;
            case WAYPOINT:
                node = createWayPoint();
                break;
            case CUBE:
                node = createCube();
                break;
            case SPHERE:
                node = createSphere();
                break;
            case PLACEHOLDER:
                node = createCube();
                break;
            default:
                node = createCube();
                break;
        }

        cache.put(type,node);

        return node;
    }

    private static RenderableNode createCarChevroletCorvette() {
        Logger.v(TAG, "Create chevrolet corvette_mtl.");
        RenderableNode parsedObject = ParsedObject.parseObject("corvette_obj", R.drawable.placeholder, true);
        RenderableNode parsedChild = (RenderableNode) parsedObject.get(0);
        parsedChild.enableColorMaterialEnabled(true);
        parsedChild.enableVertexColors(true);
//        parsedChild.setMaterial(Material.RED);
        parsedChild.enableNormals(true);
        parsedChild.enableTextures(false);
        parsedChild.enableDoubleSided(true);
        parsedChild.enableLighting(true);
        parsedChild.enableAlpha(false);
        parsedChild.enableMipMap(true);
        parsedChild.setScaling(0.3f, 0.3f, 0.3f);


        return parsedObject;
    }

    private static RenderableNode createCarRiveria() {
        Logger.v(TAG, "Create riveria.");
        RenderableNode parsedObject = ParsedObject.parseObject("riviera_obj", R.drawable.riviera, true);
        RenderableNode parsedChild = (RenderableNode) parsedObject.get(0);
        parsedChild.enableColorMaterialEnabled(true);
        parsedChild.enableVertexColors(true);
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
        parsedChild.enableVertexColors(true);
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
