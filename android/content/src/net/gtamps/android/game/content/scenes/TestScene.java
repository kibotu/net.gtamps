package net.gtamps.android.game.content.scenes;

import net.gtamps.android.R;
import net.gtamps.android.renderer.graph.RenderState;
import net.gtamps.android.renderer.graph.RenderableNode;
import net.gtamps.android.renderer.graph.scene.primitives.*;
import net.gtamps.android.game.PlayerManager;
import net.gtamps.android.game.content.EntityView;
import net.gtamps.shared.Config;
import net.gtamps.shared.Utils.math.Color4;
import org.jetbrains.annotations.NotNull;

public class TestScene extends EntityScene {

    private EntityView activeView;
    public final PlayerManager playerManager;

    public TestScene() {
        playerManager = new PlayerManager();
    }

    @Override
    public void onCreate() {

        Camera camera = new Camera(0, 0, 25, 0, 0, -1, 0, 1, 0);
        setActiveCamera(camera);
        setBackground(Color4.DARK_GRAY);
//
//        activeView = new EntityView(new Entity(Entity.Type.CAR_RIVIERA));
//        activeView.getObject3d().add(getSpotLight());
//        add(activeView);
//        EntityView camaro = new EntityView(new Entity(Entity.Type.CAR_CAMARO));
//        camaro.getObject3d().setPosition(-3, 0, 0);
//        add(camaro);
//
//        EntityView riviera = new EntityView(new Entity(Entity.Type.CAR_CHEVROLET_CORVETTE));
//        riviera.getObject3d().setPosition(3, 0, 0);
//        add(riviera);


        Cube cube = new Cube();
        cube.setPosition(-3, 0, 0);
        cube.setTextureResourceId(R.drawable.crate);

        Sphere sphere = new Sphere(1, 20, 20);
        sphere.setPosition(3, 0, 0);
        sphere.setTextureResourceId(R.drawable.earth);

        Torus torus = new Torus();
        torus.setPosition(3, 3, 0);
        torus.setTextureResourceId(R.drawable.crate);

        Cylinder cylinder = new Cylinder();
        cylinder.setPosition(-3, 3, 0);
        cylinder.setTextureResourceId(R.drawable.crate);

        add(new EntityView(torus));
        add(new EntityView(cube));
        add(new EntityView(sphere));
        add(new EntityView(cylinder));
//        add(new EntityView(Object3dFactory.create(Entity.Type.CAR_CAMARO)));
//        add(new EntityView(getSunLight()));
    }

    public static Light getSpotLight() {
        Light spot = new Light();
        spot.setPosition(0, 3, 3);
//        spot.setDirection(0, 0, -1);
        spot.getMaterial().getAmbient().setAll(128, 128, 128, 128);
        spot.getMaterial().getDiffuse().setAll(128, 128, 128, 128);
        spot.getMaterial().getSpecular().setAll(128, 128, 128, 128);
        spot.setType(Light.Type.POSITIONAL);
        spot.setSpotCutoffAngle(90);
        spot.setSpotExponent(16);
        spot.setAttenuation(0.6f, 0.1f, 0f);
        spot.setRotation(60, 0, 0);
        return spot;
    }

    public static Light getSunLight() {
        Light sun = new Light();
        sun.setPosition(0, 0, 20);
//        sun.setDirection(0, 0, -1);
        sun.getMaterial().getAmbient().setAll(128, 128, 128, 128);
        sun.getMaterial().getDiffuse().setAll(128, 128, 128, 128);
        sun.getMaterial().getSpecular().setAll(128, 128, 128, 128);
        sun.setType(Light.Type.POSITIONAL);
        sun.setSpotCutoffAngle(160);
        sun.setSpotExponent(22);
        sun.setAttenuation(1, 0, 0);
        return sun;
    }

    public static RenderableNode addPlane() {
        ParsedObject parsedObject = ParsedObject.parseObject("grid_obj", R.drawable.grid, true);
        RenderableNode parsedChild = (RenderableNode) parsedObject.get(0);
        parsedChild.setScaling(40, 40, 1f);
        parsedChild.enableColorMaterialEnabled(true);
        parsedChild.enableVertexColors(true);
        parsedChild.enableNormals(true);
        parsedChild.enableTextures(true);
        parsedChild.enableDoubleSided(true);
        parsedChild.enableLighting(false);
        parsedChild.enableAlpha(false);
        parsedChild.setRotation(0, 0, 0);
        parsedChild.getRenderState().shader = RenderState.Shader.SMOOTH;
        return parsedObject;
    }

    public static RenderableNode addLevel() {
        ParsedObject parsedObject = ParsedObject.parseObject("map1_obj", R.drawable.grid, true);
        RenderableNode parsedChild = (RenderableNode) parsedObject.get(0);
        parsedChild.enableColorMaterialEnabled(true);
        parsedChild.enableVertexColors(true);
        parsedChild.enableNormals(true);
        parsedChild.enableTextures(true);
        parsedChild.enableDoubleSided(true);
        parsedChild.enableLighting(false);
        parsedChild.enableAlpha(false);
        parsedObject.setScaling(Config.PIXEL_TO_NATIVE, Config.PIXEL_TO_NATIVE, Config.PIXEL_TO_NATIVE);
        parsedObject.setPosition(0, 0, -3f);
        parsedChild.getRenderState().shader = RenderState.Shader.FLAT;
        return parsedObject;
    }

    public EntityView getActiveView() {
        return activeView;
    }

    public void setActiveView(@NotNull EntityView entityView) {
        this.activeView = entityView;
    }

    @Override
    public void onDirty() {
    }
}
