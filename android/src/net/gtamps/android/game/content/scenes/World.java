package net.gtamps.android.game.content.scenes;

import net.gtamps.android.R;
import net.gtamps.android.core.renderer.graph.RenderState;
import net.gtamps.android.core.renderer.graph.RenderableNode;
import net.gtamps.android.core.renderer.graph.scene.primitives.Camera;
import net.gtamps.android.core.renderer.graph.scene.primitives.Light;
import net.gtamps.android.core.renderer.graph.scene.primitives.ParsedObject;
import net.gtamps.android.game.PlayerManager;
import net.gtamps.android.game.content.EntityView;
import net.gtamps.shared.Utils.math.Color4;
import net.gtamps.shared.game.entity.Entity;
import org.jetbrains.annotations.NotNull;

public class World extends EntityScene {

    private EntityView activeView;
    public final PlayerManager playerManager;

    public World() {
        playerManager = new PlayerManager();
    }

    @Override
    public void onCreate() {
        Camera camera = new Camera(0, 0, 40, 0, 0, 0, 0, 0, 1);
        setActiveCamera(camera);
        setBackground(Color4.DARK_GRAY);

        activeView = new EntityView(new Entity(Entity.Type.CAR_RIVIERA));
//        activeView.getObject3d().add(getSpotLight());
        add(activeView);
        EntityView camaro = new EntityView(new Entity(Entity.Type.CAR_CAMARO));
        camaro.getObject3d().setPosition(-3, 0, 0);
        add(camaro);

        EntityView riviera = new EntityView(new Entity(Entity.Type.CAR_CHEVROLET_CORVETTE));
        riviera.getObject3d().setPosition(3, 0, 0);
        add(riviera);


        add(new EntityView(addPlane()));
        add(new EntityView(getSunLight()));
    }

    public static Light getSpotLight() {
        Light spot = new Light();
        spot.setPosition(0, 2, 3);
        spot.setDirection(0, 0, -1);
        spot.getMaterial().getAmbient().setAll(255, 255, 255, 255);
        spot.getMaterial().getDiffuse().setAll(255, 255, 255, 255);
        spot.getMaterial().getSpecular().setAll(255, 255, 255, 255);
        spot.setType(Light.Type.POSITIONAL);
        spot.setSpotCutoffAngle(60);
        spot.setSpotExponent(4);
        spot.setAttenuation(0.6f, 0, 0);
        spot.setRotation(60, 0, 0);
        return spot;
    }

    public static Light getSunLight() {
        Light sun = new Light();
        sun.setPosition(0, 0, 10);
        sun.setDirection(0, 0, -1);
        sun.getMaterial().getAmbient().setAll(0, 0, 0, 255);
        sun.getMaterial().getDiffuse().setAll(100, 100, 100, 255);
        sun.getMaterial().getSpecular().setAll(0, 0, 0, 255);
        sun.setType(Light.Type.DIRECTIONAL);
        sun.setSpotCutoffAngle(80);
        sun.setSpotExponent(4);
        sun.setAttenuation(0.5f, 0, 0);
        return sun;
    }

    public static RenderableNode addPlane() {
        ParsedObject parsedObject = ParsedObject.parseObject("grid_obj", R.drawable.grid, true);
        RenderableNode parsedChild = (RenderableNode) parsedObject.get(0);
        parsedChild.setScaling(40, 40, 1f);
        parsedChild.enableColorMaterialEnabled(true);
        parsedChild.enableVertexColors(false);
        parsedChild.enableNormals(true);
        parsedChild.enableTextures(true);
        parsedChild.enableDoubleSided(true);
        parsedChild.enableLighting(true);
        parsedChild.enableAlpha(false);
        parsedChild.setRotation(0, 0, 0);
        parsedChild.getRenderState().shader = RenderState.Shader.FLAT;
        parsedChild.enableMipMap(true);
        return parsedObject;
    }

    public static RenderableNode addLevel() {
        ParsedObject parsedObject = ParsedObject.parseObject("city_obj", R.drawable.grid, true);
        RenderableNode parsedChild = (RenderableNode) parsedObject.get(0);
        parsedChild.enableColorMaterialEnabled(true);
        parsedChild.enableVertexColors(true);
        parsedChild.enableNormals(true);
        parsedChild.enableTextures(true);
        parsedChild.enableDoubleSided(true);
        parsedChild.enableLighting(true);
        parsedChild.enableAlpha(false);
        parsedObject.setScaling(0.5f, 0.5f, 0.5f);
        parsedObject.setPosition(0, 0, -3f);
        parsedChild.getRenderState().shader = RenderState.Shader.FLAT;
        parsedChild.enableMipMap(true);
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
