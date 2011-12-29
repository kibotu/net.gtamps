package net.gtamps.android.game.content;

import net.gtamps.android.R;
import net.gtamps.android.core.renderer.graph.RenderableNode;
import net.gtamps.android.core.renderer.graph.scene.primitives.*;
import net.gtamps.android.core.renderer.mesh.Material;
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

        if (cache.containsKey(type)) {
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
            case CYLINDER:
                node = createCylinder();
                break;
            case TORUS:
                node = createTorus();
                break;
            case PLACEHOLDER:
                node = createCube();
                break;
            default:
                node = createCube();
                break;
        }

        cache.put(type, node);

        return node;
    }

    private static RenderableNode createCarChevroletCorvette() {
//        Logger.v(TAG, "Create chevrolet corvette_mtl.");
        RenderableNode parsedObject = ParsedObject.parseObject("corvette_obj", R.drawable.placeholder, true);
        RenderableNode parsedChild = (RenderableNode) parsedObject.get(0);
        parsedChild.enableColorMaterialEnabled(true);
        parsedChild.enableVertexColors(true);
        parsedChild.enableNormals(true);
        parsedChild.enableTextures(false);
        parsedChild.enableDoubleSided(true);
        parsedChild.enableLighting(false);
        parsedChild.enableAlpha(false);
        parsedChild.enableMipMap(true);
        parsedChild.setScaling(0.3f, 0.3f, 0.3f);
        return parsedObject;
    }

    private static RenderableNode createCarRiveria() {
        Logger.v(TAG, "Create riviera.");
        RenderableNode parsedObject = ParsedObject.parseObject("riviera_obj", R.drawable.riviera, false);
        RenderableNode parsedChild = (RenderableNode) parsedObject.get(0);
        parsedChild.enableColorMaterialEnabled(true);
        parsedChild.enableVertexColors(true);
        parsedChild.enableNormals(true);
        parsedChild.enableTextures(true);
        parsedChild.enableDoubleSided(false);
        parsedChild.enableLighting(false);
        parsedChild.enableAlpha(false);
        parsedChild.enableMipMap(false);
        parsedChild.setScaling(5,5,5);
        return parsedObject;
    }

    private static RenderableNode createCarCamaro() {
        Logger.v(TAG, "Create camaro.");
        RenderableNode parsedObject = ParsedObject.parseObject("camaro_obj", R.drawable.camaro, false);
        RenderableNode parsedChild = (RenderableNode) parsedObject.get(0);
        parsedChild.enableColorMaterialEnabled(true);
        parsedChild.enableVertexColors(true);
        parsedChild.enableNormals(true);
        parsedChild.enableTextures(true);
        parsedChild.enableDoubleSided(false);
        parsedChild.enableLighting(false);
        parsedChild.enableAlpha(false);
        parsedChild.enableMipMap(false);
        return parsedObject;
    }

    private static RenderableNode createCube() {
        return new Cube();
    }

    private static RenderableNode createSphere() {
        return new Sphere();
    }

    private static RenderableNode createTorus() {
        return new Torus();
    }

    private static RenderableNode createCylinder() {
        return new Cylinder();
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

//        RenderableNode parsedObject = ParsedObject.parseObject("figure_obj", R.drawable.placeholder, false);
//        RenderableNode parsedChild = (RenderableNode) parsedObject.get(0);
//        parsedChild.enableColorMaterialEnabled(true);
//        parsedChild.enableVertexColors(true);
//        parsedChild.enableNormals(true);
//        parsedChild.enableTextures(true);
//        parsedChild.enableDoubleSided(false);
//        parsedChild.enableLighting(false);
//        parsedChild.enableAlpha(false);
//        return parsedObject;
        return createCarCamaro();
    }
}
