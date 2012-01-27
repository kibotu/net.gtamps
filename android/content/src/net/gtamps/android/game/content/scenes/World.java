package net.gtamps.android.game.content.scenes;

import java.util.LinkedList;
import java.util.List;

import net.gtamps.android.R;
import net.gtamps.android.core.input.InputEngineController;
import net.gtamps.android.core.input.event.InputEventListener;
import net.gtamps.android.core.input.layout.AbstractInputLayout;
import net.gtamps.android.core.input.layout.InputLayoutIngame;
import net.gtamps.android.core.net.AbstractEntityView;
import net.gtamps.android.core.net.IWorld;
import net.gtamps.android.renderer.graph.RenderState;
import net.gtamps.android.renderer.graph.RenderableNode;
import net.gtamps.android.renderer.graph.scene.primitives.Camera;
import net.gtamps.android.renderer.graph.scene.primitives.Light;
import net.gtamps.android.renderer.graph.scene.primitives.ParsedObject;
import net.gtamps.android.game.PlayerManager;
import net.gtamps.android.game.content.EntityView;
import net.gtamps.android.game.content.scenes.inputlistener.PlayerMovementListener;
import net.gtamps.shared.Config;
import net.gtamps.shared.Utils.Logger;
import net.gtamps.shared.Utils.math.Color4;
import net.gtamps.shared.Utils.math.MathUtils;
import net.gtamps.shared.game.entity.Entity;
import net.gtamps.shared.game.level.Tile;
import net.gtamps.shared.serializer.ConnectionManager;
import net.gtamps.shared.serializer.communication.*;
import net.gtamps.shared.serializer.communication.data.ISendableData;
import org.jetbrains.annotations.NotNull;

public class World extends EntityScene implements InputEventListener, IWorld {

    private AbstractEntityView activeView;
    private AbstractInputLayout layout;
    private NewMessage message;

    public World() {
    }

    @Override
    public void onCreate() {

        Camera camera = new Camera(0, 0, 200, 0, 0, -1, 0, 1, 0);
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

//        add(new EntityView(getSunLight()));

        // setup layout
        layout = new InputLayoutIngame();
        InputEngineController.getInstance().setLayout(layout);
        PlayerMovementListener pml = new PlayerMovementListener();
        InputEngineController.getInstance().getInputEventDispatcher().addInputEventListener(pml);
//        InputEngineController.getInstance().getInputEventDispatcher().addInputEventListener(this);

        // set dirty flag, since something has changed (input engine needs correct resolution
        setDirtyFlag();
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
        ParsedObject parsedObject = ParsedObject.parseObject("map1map_obj", R.drawable.map1map, true);
        RenderableNode parsedChild = (RenderableNode) parsedObject.get(0);
        parsedChild.enableColorMaterialEnabled(true);
        parsedChild.enableVertexColors(true);
        parsedChild.enableNormals(true);
        parsedChild.enableTextures(true);
        parsedChild.enableDoubleSided(true);
        parsedChild.enableLighting(false);
        parsedChild.enableAlpha(false);
        parsedObject.setScaling((Config.PIXEL_TO_NATIVE + 0.1f)*-1, Config.PIXEL_TO_NATIVE+ 0.1f, Config.PIXEL_TO_NATIVE);
//        parsedObject.setScaling(Config.PIXEL_TO_NATIVE, Config.PIXEL_TO_NATIVE, Config.PIXEL_TO_NATIVE);
        parsedObject.setPosition(0,- (1280*(Config.PIXEL_TO_NATIVE + 0.1f))/2, 0);
        parsedChild.setRotation(0,0, MathUtils.deg2Rad(180));
        parsedChild.getRenderState().shader = RenderState.Shader.FLAT;
        return parsedObject;
    }

    public AbstractEntityView getActiveView() {
        return activeView;
    }

    public void setActiveView(@NotNull AbstractEntityView entityView) {
        this.activeView = entityView;
    }

    @Override
    public void onSendableRetrieve(SendableType sendableType, ISendableData data) {
        Logger.D(this, sendableType);
//        if (
//			sendableType.equals(SendableType.ACTION_ACCELERATE) ||
//			sendableType.equals(SendableType.ACTION_DECELERATE) ||
//			sendableType.equals(SendableType.ACTION_LEFT) ||
//			sendableType.equals(SendableType.ACTION_RIGHT) ||
//			sendableType.equals(SendableType.ACTION_SHOOT)
//
//		) {
//
//
//        TODO use SendableFactory
//        message = NewMessageFactory.createGetUpdateRequest(ConnectionManager.INSTANCE.currentRevId);
//        message.addSendable(new NewSendable(sendableType));
//        ConnectionManager.INSTANCE.add(message);

//		}
    }

    @Override
    public void onDirty() {
        // set resolution
//        layout.getTouchWindow().setResolution((int) getScene().getActiveCamera().getDimension().x, (int) getScene().getActiveCamera().getDimension().y);
        clearDirtyFlag();
    }

	@Override
	public void add(AbstractEntityView entityView) {
		
		
	}

	@Override
	public List<AbstractEntityView> getAllEntities() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AbstractEntityView createEntityView(Entity e) {
		return new EntityView(e);
	}

	@Override
	public boolean supports2DTileMap() {
		return false;
	}

	@Override
	public void setTileMap(LinkedList<Tile> tileMap) {
		Logger.e(this, "I'm sorry Dave, I'm afraid I can't do that.");
	}

}
