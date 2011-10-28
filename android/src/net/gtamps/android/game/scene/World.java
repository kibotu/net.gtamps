package net.gtamps.android.game.scene;

import net.gtamps.android.R;
import net.gtamps.android.core.Registry;
import net.gtamps.android.core.renderer.graph.RenderableNode;
import net.gtamps.android.core.renderer.graph.primitives.Camera;
import net.gtamps.android.core.renderer.graph.primitives.Cube;
import net.gtamps.android.core.renderer.graph.primitives.Light;
import net.gtamps.android.core.renderer.mesh.parser.IParser;
import net.gtamps.android.core.renderer.mesh.parser.Parser;
import net.gtamps.android.game.PlayerManager;
import net.gtamps.android.game.objects.EntityView;
import net.gtamps.android.game.objects.Object3dFactory;
import net.gtamps.android.game.objects.ParsedObject;
import net.gtamps.shared.game.entity.Entity;
import net.gtamps.shared.math.Color4;
import org.jetbrains.annotations.NotNull;

public class World {

    private Camera camera;
    private Scene scene;
    private EntityView activeOjbect;
    public final PlayerManager playerManager;

    public World() {
        playerManager = new PlayerManager();
    }

    public void init() {

         // world
        scene = new Scene();

        camera =  new Camera(0, 0,40, 0, 0, 0, 0, 0, 1);
        scene.setActiveCamera(camera);
        scene.setBackground(Color4.DARK_GRAY);

        activeOjbect = new EntityView(new Entity(Entity.Type.CAR_RIVIERA));
        activeOjbect.getObject3d().get(0).add(getSpotLight());
        scene.addChild(activeOjbect.getObject3d());
        scene.addChild(new Cube());
        RenderableNode camaro = Object3dFactory.create(Entity.Type.CAR_CAMARO);
        camaro.setPosition(-3,0,0);
        scene.addChild(camaro);
        scene.addChild(addPlane());

        RenderableNode riviera = Object3dFactory.create(Entity.Type.CAR_RIVIERA);
        riviera.setPosition(3,0,0);
        scene.addChild(riviera);

        scene.addChild(getSunLight());
    }

    public static Light getSpotLight() {
        Light spot = new Light();
		spot.setPosition(0, 2, 3);
        spot.setDirection(0,0,-1);
		spot.diffuse.setAll(255,255,255, 255);
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
		sun.setPosition(0, 0, 20);
        sun.setDirection(0,0,-1);
		sun.diffuse.setAll(128, 128, 128, 255);
		sun.ambient.setAll(0, 0, 0, 0);
		sun.specular.setAll(128, 128, 128, 255);
		sun.emissive.setAll(0, 0, 0, 0);
        sun.setType(Light.Type.POSITIONAL);
        sun.setSpotCutoffAngle(60);
        sun.setSpotExponent(4);
        sun.setAttenuation(0.5f,0,0);
        return sun;
    }

    public static RenderableNode addPlane() {
        ParsedObject parsedObject = ParsedObject.parseObject("grid_obj",R.drawable.grid,true);
        RenderableNode parsedChild = (RenderableNode)parsedObject.get(0);
        parsedChild.setScaling(40, 40, 0);
        parsedChild.enableColorMaterialEnabled(false);
        parsedChild.enableVertexColors(false);
        parsedChild.enableNormals(true);
        parsedChild.enableTextures(true);
        parsedChild.enableDoubleSided(false);
        parsedChild.enableLighting(true);
        parsedChild.enableAlpha(true);
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

    public Scene getScene() {
        if(scene == null) {
            init();
        }
        return scene;
    }

    public EntityView getActiveObject() {
        return activeOjbect;
    }

    public void setActiveObject(@NotNull EntityView entityView) {
        this.activeOjbect = entityView;
    }
}
