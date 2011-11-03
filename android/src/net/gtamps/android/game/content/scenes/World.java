package net.gtamps.android.game.content.scenes;

import net.gtamps.android.R;
import net.gtamps.android.core.renderer.Registry;
import net.gtamps.android.core.renderer.graph.IShader;
import net.gtamps.android.core.renderer.graph.RenderableNode;
import net.gtamps.android.core.renderer.graph.primitives.Camera;
import net.gtamps.android.core.renderer.graph.primitives.Light;
import net.gtamps.android.core.renderer.graph.primitives.ParsedObject;
import net.gtamps.android.core.renderer.mesh.parser.IParser;
import net.gtamps.android.core.renderer.mesh.parser.Parser;
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
//        activeOjbect.getObject3d().add(getSpotLight());
        add(activeView);
        EntityView camaro = new EntityView(new Entity(Entity.Type.CAR_RIVIERA));
        camaro.getObject3d().setPosition(-3, 0, 0);
        add(camaro);

        EntityView riviera = new EntityView(new Entity(Entity.Type.CAR_RIVIERA));
        riviera.getObject3d().setPosition(3, 0, 0);
        add(riviera);
    }

    public static Light getSpotLight() {
        Light spot = new Light();
        spot.setPosition(0, 2, 3);
        spot.setDirection(0, 0, -1);
        spot.diffuse.setAll(255, 255, 255, 255);
        spot.ambient.setAll(0, 0, 0, 0);
        spot.specular.setAll(255, 255, 255, 255);
        spot.emissive.setAll(0, 0, 0, 0);
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
        sun.diffuse.setAll(128, 128, 128, 255);
        sun.ambient.setAll(0, 0, 0, 0);
        sun.specular.setAll(128, 128, 128, 255);
        sun.emissive.setAll(0, 0, 0, 0);
        sun.setType(Light.Type.POSITIONAL);
        sun.setSpotCutoffAngle(60);
        sun.setSpotExponent(4);
        sun.setAttenuation(0.5f, 0, 0);
        return sun;
    }

    public static RenderableNode addPlane() {
        ParsedObject parsedObject = ParsedObject.parseObject("grid_obj", R.drawable.grid, true);
        RenderableNode parsedChild = (RenderableNode) parsedObject.get(0);
        parsedChild.setScaling(40, 40, 0);
        parsedChild.enableColorMaterialEnabled(false);
        parsedChild.enableVertexColors(true);
        parsedChild.enableNormals(true);
        parsedChild.enableTextures(true);
        parsedChild.enableDoubleSided(false);
        parsedChild.enableLighting(true);
        parsedChild.enableAlpha(false);
        parsedChild.setRotation(0, 0, 0);
        parsedChild.setShader(IShader.Type.FLAT);
        parsedChild.enableMipMap(true);
        return parsedObject;
    }

    public static RenderableNode addLevel() {
        IParser objParser = Parser.createParser(Parser.Type.OBJ, "net.gtamps.android:raw/map1_obj", true);
        objParser.parse();
        ParsedObject parsedObject = objParser.getParsedObject();
        parsedObject.setTextureId(Registry.getTextureLibrary().loadTexture(R.drawable.grid, true));
        parsedObject.setScaling(0.5f, 0.5f, 0.5f);
        parsedObject.setPosition(0, 0, -3f);
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
