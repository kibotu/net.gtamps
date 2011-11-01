package net.gtamps.android.core.renderer.graph.primitives;

import net.gtamps.android.core.renderer.mesh.texture.SpriteTetxure;
import net.gtamps.shared.state.State;
import org.jetbrains.annotations.NotNull;

public class AnimatedSprite extends Sprite {

    private float lastProgress;

    public AnimatedSprite() {
        lastProgress = 0;
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
}
