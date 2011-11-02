package net.gtamps.android.game.scenes;

import net.gtamps.android.R;
import net.gtamps.android.core.renderer.graph.primitives.AnimatedSprite;
import net.gtamps.android.core.renderer.graph.primitives.Camera;
import net.gtamps.android.core.renderer.graph.primitives.TextSprite;

public class Menu extends BasicScene {

    @Override
    public void onCreate() {
        setActiveCamera(new Camera(0, 0, 1, 0, 0, 0, 0, 1, 0));

        AnimatedSprite background = new AnimatedSprite();
        background.loadBufferedTexture(R.drawable.menubackground,R.raw.menu,true);
        background.setPosition(-0.16601562f,0,0);
        background.setDimension(1,1,0);
        background.setScaling(1.67f,1,0);
        add(background);

        TextSprite start = new TextSprite("Start");
        start.setPosition(-0.5f,0.1f,0);
        add(start);

        TextSprite options = new TextSprite("Options");
        options.setPosition(-0.5f,0,0);
        add(options);

        TextSprite quit = new TextSprite("Quit");
        quit.setPosition(-0.5f,-0.1f,0);
        add(quit);
    }

    @Override
    public void onDirty() {
    }
}
