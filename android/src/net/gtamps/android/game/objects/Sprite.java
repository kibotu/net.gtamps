package net.gtamps.android.game.objects;

import net.gtamps.android.Registry;
import net.gtamps.android.core.graph.SceneNode;
import net.gtamps.android.core.graph.SpriteNode;
import net.gtamps.android.core.renderer.BufferedTexture;
import net.gtamps.android.core.renderer.SpriteTetxure;
import net.gtamps.shared.game.entity.Entity;
import net.gtamps.shared.state.State;
import org.jetbrains.annotations.NotNull;

public class Sprite extends SpriteNode implements IObject3d {

    private BufferedTexture bufferedTexture;
    private float lastProgress;

    public Sprite() {
        bufferedTexture = null;
        lastProgress = 0;
    }

    @Override
    public SceneNode getNode() {
        return this;
    }

    public void setBufferedTexture(BufferedTexture bufferedTexture) {
        this.bufferedTexture = bufferedTexture;
        setTextureId(bufferedTexture.textureId);
        setTextureBufferId(bufferedTexture.floatBufferId);
        SpriteTetxure spriteTetxure = bufferedTexture.getAnimation(State.Type.IDLE)[0];
        setTextureBufferOffsetId(spriteTetxure.offsetId);
        setDimension(spriteTetxure.width,spriteTetxure.height);
        setScaling(1,1,0);
    }

    public void animate(float percentage, @NotNull State.Type state) {

        if (lastProgress == percentage) {
            return;
        }
        final SpriteTetxure [] animation = bufferedTexture.getAnimation(state);

        lastProgress = percentage;
        percentage *= 100;
        percentage %= 100;
        final int index = (int) (animation.length / 100f * percentage);

        setTextureBufferOffsetId(animation[index].offsetId);
        setDimension(animation[index].width, animation[index].height);
    }

    public void loadBufferedTexture(int textureResourceId, int textureCoordinateResourceId, boolean generateMipMap) {
        setBufferedTexture(Registry.getTextureLibrary().loadBufferedTexture(textureResourceId, textureCoordinateResourceId, generateMipMap));
        enableMipMap(generateMipMap);
    }
}
