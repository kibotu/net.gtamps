package net.gtamps.android.game;

import android.os.SystemClock;
import net.gtamps.android.core.net.MessageHandler;
import net.gtamps.android.renderer.RenderAction;
import net.gtamps.android.renderer.graph.scene.BasicScene;
import net.gtamps.android.game.content.scenes.Hud;
import net.gtamps.android.game.content.scenes.Menu;
import net.gtamps.android.game.content.scenes.World;
import net.gtamps.shared.Config;
import net.gtamps.shared.Utils.Logger;
import net.gtamps.shared.Utils.math.Vector3;
import net.gtamps.shared.serializer.ConnectionManager;
import net.gtamps.shared.serializer.communication.Message;
import net.gtamps.shared.serializer.communication.MessageFactory;

import java.util.ArrayList;

public class Game extends RenderAction {

    private long startTime;
    private long finalDelta;
    private boolean isDragging = false;
    private final Hud hud;
    private final World world;
    private final Menu menu;
    private final ConnectionManager connection;
    private final MessageHandler messageHandler;

    public Game() {
        super();
        hud = new Hud();
        world = new World();
        menu = new Menu();
        connection = ConnectionManager.INSTANCE;
        this.messageHandler = new MessageHandler(connection, world);
    }

    public void onCreate() {

        // create world
        scenes.add(world);
//        world.getScene().setVisible(false);
//        PlayerMovementListener cl = new PlayerMovementListener();
//        InputEngineController.getInstance().getInputEventDispatcher().addInputEventListener(cl);

        // hud
//        scenes.add(hud);
//        hud.getScene().setVisible(false);

//        scenes.add(menu);
//        menu.getScene().setVisible(false);

        // connect
        connection.checkConnection();

        Logger.I(this, "Connecting to " + Config.SERVER_DEFAULT_HOST_ADDRESS + ":" + Config.SERVER_DEFAULT_PORT + " " + (connection.isConnected() ? "successful." : "failed."));
        connection.start();
        connection.add(MessageFactory.createSessionRequest());
    }

    @Override
    public void onDrawFrame() {
        if (!isRunning || isPaused) {
            return;
        }

        // check connection
        connection.checkConnection();

        // handle inbox messages
        while (!connection.isEmpty()) {
            Message message = connection.poll();
            for (int i = 0; i < message.sendables.size(); i++) {
                messageHandler.handleMessage(message.sendables.get(i), message);
            }
        }

        if (world.getActiveView() != null && world.getActiveView().getObject3d() != null) {
//            connection.add(MessageFactory.createGetUpdateRequest(connection.currentRevId));
            Vector3 temp = world.getActiveView().getObject3d().getPosition();
            world.getActiveCamera().setPosition(temp.x, temp.y, temp.z + i++%300);
            world.getActiveCamera().setTarget(world.getActiveView().getObject3d().getPosition());
        }
    }
    int i = 0;

    /**
     * Setzt den neuen Zoomfaktor anhand der Zoomwerte des Inputs
     *
     * @param distance Distanz der beiden Zoomenden Finger
     */
    private void setZoomByDistance(float distance) {
        scenes.get(0).getActiveCamera().setZoomFactor(scenes.get(0).getActiveCamera().getZoomFactor() + distance * 0.01f);
    }
}
