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
        background.setScaling(4.1f, 3.3f, 0);
        background.setPosition(-0.15f,0,0);
        add(background);

        TextSprite text = new TextSprite("Hello World");
        text.setScaling(2f,2f,0, 2f);
        add(text);
    }

    @Override
    public void onDirty() {
    }
}
