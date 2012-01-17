package net.gtamps.android.game.content.scenes;

import net.gtamps.android.R;
import net.gtamps.android.core.input.InputEngineController;
import net.gtamps.android.core.input.event.InputEventListener;
import net.gtamps.android.core.input.layout.AbstractInputLayout;
import net.gtamps.android.core.input.layout.InputLayoutIngame;
import net.gtamps.android.renderer.graph.scene.BasicScene;
import net.gtamps.android.renderer.graph.scene.primitives.AnimatedSprite;
import net.gtamps.android.renderer.graph.scene.primitives.Camera;
import net.gtamps.android.renderer.graph.scene.primitives.Sprite;
import net.gtamps.shared.Utils.Logger;
import net.gtamps.shared.game.state.State;
import net.gtamps.shared.serializer.ConnectionManager;
import net.gtamps.shared.serializer.communication.NewMessage;
import net.gtamps.shared.serializer.communication.SendableType;
import net.gtamps.shared.serializer.communication.data.ISendableData;

public class Hud extends BasicScene implements InputEventListener {

    private AnimatedSprite ring;
    private AnimatedSprite cursor;
    private AbstractInputLayout layout;

    public Hud() {
        super();
    }

    @Override
    public void onCreate() {
        // setup camera
        Camera camera = new Camera(0, 0, 1, 0, 0, 0, 0, 1, 0);
//        Camera camera = new Camera(0, 0, 100, 0, 0, -1, 0, 1, 0);
        setActiveCamera(camera);
        camera.enableDepthTest(false);

        // add ring
        ring = new AnimatedSprite();
        ring.setVisible(true);
        add(ring);

        // add cursor
        cursor = new AnimatedSprite();
        cursor.setVisible(false);
        add(cursor);

        // add textures
        ring.loadBufferedTexture(R.drawable.hud, R.raw.hud, true);
        cursor.loadBufferedTexture(R.drawable.hud, R.raw.hud, true);
        cursor.animate(0.51f, State.Type.IDLE);

        // setup layout
        layout = new InputLayoutIngame();
        InputEngineController.getInstance().setLayout(layout);
        InputEngineController.getInstance().getInputEventDispatcher().addInputEventListener(this);

        // set dirty flag, since something has changed (input engine needs correct resolution
        setDirtyFlag();
    }

    private NewMessage message;

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
        

        //TODO use SendableFactory
//        message = NewMessageFactory.createGetUpdateRequest(ConnectionManager.INSTANCE.currentRevId);
//        message.addSendable(new NewSendable(sendableType, data));
//        ConnectionManager.INSTANCE.add(message);
        
        
//		}
    }

    public Sprite getCursor() {
        return cursor;
    }

    public Sprite getRing() {
        return ring;
    }

    @Override
    public void onDirty() {
        // set resolution
        layout.getTouchWindow().setResolution((int) getScene().getActiveCamera().getDimension().x, (int) getScene().getActiveCamera().getDimension().y);
        clearDirtyFlag();
    }
}
