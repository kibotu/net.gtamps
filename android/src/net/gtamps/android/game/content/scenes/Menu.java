package net.gtamps.android.game.content.scenes;

import net.gtamps.android.R;
import net.gtamps.android.core.renderer.graph.scene.BasicScene;
import net.gtamps.android.core.renderer.graph.scene.primitives.AnimatedSprite;
import net.gtamps.android.core.renderer.graph.scene.primitives.Camera;
import net.gtamps.android.core.renderer.graph.scene.primitives.NullNode;
import net.gtamps.android.core.renderer.graph.scene.primitives.TextSprite;

public class Menu extends BasicScene {

    private NullNode startScreen = new NullNode();
    private NullNode optionScreen = new NullNode();
    private NullNode serverListScreen = new NullNode();

    @Override
    public void onCreate() {
        setActiveCamera(new Camera(0, 0, 1, 0, 0, 0, 0, 1, 0));
        getActiveCamera().setZoomFactor(1.6f);

        buildStartScreen();
        buildOptionsScreen();
        buildServerListScreen();
        startScreen.setVisible(true);
        optionScreen.setVisible(false);
        serverListScreen.setVisible(false);
    }

    public void showStartScreen() {
        startScreen.setVisible(true);
        optionScreen.setVisible(false);
        serverListScreen.setVisible(false);
    }

    public void showOptionsScreen() {
        startScreen.setVisible(false);
        optionScreen.setVisible(true);
        serverListScreen.setVisible(false);
    }

    public void showServerListScreen() {
        startScreen.setVisible(false);
        optionScreen.setVisible(false);
        serverListScreen.setVisible(true);
    }

    private void buildServerListScreen() {

        // background
        AnimatedSprite serverListScreenBackground = new AnimatedSprite();
        serverListScreenBackground.loadBufferedTexture(R.drawable.serverlistscreenbackground, R.raw.menu, true);
        serverListScreen.add(serverListScreenBackground);

        // headline
        TextSprite headline = new TextSprite("Choose Server");
        float distanceLeft = -0.39f;
        headline.setPosition(distanceLeft, 0.18f, 0.0f);
        serverListScreen.add(headline);

        // add to scene
        add(serverListScreen);
    }

    private void buildOptionsScreen() {

        // background
        AnimatedSprite optionScreenBackground = new AnimatedSprite();
        optionScreenBackground.loadBufferedTexture(R.drawable.optionsscreenbackground, R.raw.menu, true);
        optionScreen.add(optionScreenBackground);

        float distanceLeft = -0.25f;

        // headline
        TextSprite headline = new TextSprite("Options");
        headline.setPosition(distanceLeft, 0.18f, 0.0f);
        optionScreen.add(headline);

        // login
        TextSprite login = new TextSprite("Login");
        login.setPosition(distanceLeft, 0.05f, 0.0f);
        optionScreen.add(login);

        // register
        TextSprite register = new TextSprite("Register");
        register.setPosition(distanceLeft, -0.02f, 0.0f);
        optionScreen.add(register);

        // graphic
        TextSprite graphic = new TextSprite("Graphic");
        graphic.setPosition(distanceLeft, -0.1f, 0.0f);
        optionScreen.add(graphic);

        // back
        TextSprite back = new TextSprite("Back");
        back.setPosition(distanceLeft, -0.17f, 0.0f);
        optionScreen.add(back);

        // add to scene
        add(optionScreen);
    }

    private void buildStartScreen() {

        // background
        AnimatedSprite startScreenBackground = new AnimatedSprite();
        startScreenBackground.loadBufferedTexture(R.drawable.startscreenbackground, R.raw.menu, true);
        startScreen.add(startScreenBackground);

        // headline
        TextSprite headline = new TextSprite("GTAMPS");
        headline.setPosition(-0.315f, 0.15f, 0.1f);
        startScreen.add(headline);

        // start button
        float distanceLeft = -0.35f;
        TextSprite start = new TextSprite("Start");
        start.setPosition(distanceLeft, 0.03f, 0);
        startScreen.add(start);

        // options button
        TextSprite options = new TextSprite("Options");
        options.setPosition(distanceLeft, -0.055f, 0);
        startScreen.add(options);

        // quit button
        TextSprite quit = new TextSprite("Quit");
        quit.setPosition(distanceLeft, -0.15f, 0);
        startScreen.add(quit);

        // add to scene
        add(startScreen);
    }

    @Override
    public void onDirty() {
    }
}
