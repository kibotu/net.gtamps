package net.gtamps.android.core.renderer.graph.primitives;

import net.gtamps.android.core.Registry;
import net.gtamps.android.core.renderer.mesh.texture.BufferedTexture;
import net.gtamps.android.core.renderer.mesh.texture.SpriteTetxure;
import net.gtamps.shared.state.State;
import org.jetbrains.annotations.NotNull;

public class AnimatedSprite extends Sprite {

    private BufferedTexture bufferedTexture;
    private float lastProgress;

    public AnimatedSprite() {
        bufferedTexture = null;
        lastProgress = 0;
    }

    public void setBufferedTexture(BufferedTexture bufferedTexture) {
        this.bufferedTexture = bufferedTexture;
        setTextureId(bufferedTexture.textureId);
        setTextureBufferId(bufferedTexture.floatBufferId);
        SpriteTetxure spriteTetxure = bufferedTexture.getAnimation(State.Type.IDLE)[0];
        setTextureBufferOffsetId(spriteTetxure.offsetId);
        dimension.set(spriteTetxure.width,spriteTetxure.height,0);
        scaling.set(1,1,0);
    }

    public void animate(float percentage, @NotNull State.Type state) {

        if (lastProgress == percentage) {
            return;
        }
        final SpriteTetxure[] animation = bufferedTexture.getAnimation(state);

        lastProgress = percentage;
        percentage *= 100;
        percentage %= 100;
        final int index = (int) (animation.length / 100f * percentage);

        setTextureBufferOffsetId(animation[index].offsetId);
        dimension.set(animation[index].width, animation[index].height,0);
    }

    public void loadBufferedTexture(int textureResourceId, int textureCoordinateResourceId, boolean generateMipMap) {
        setBufferedTexture(Registry.getTextureLibrary().loadBufferedTexture(textureResourceId, textureCoordinateResourceId, generateMipMap));
        enableMipMap(generateMipMap);
    }
}
