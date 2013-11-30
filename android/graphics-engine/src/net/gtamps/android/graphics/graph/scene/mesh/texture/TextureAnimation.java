package net.gtamps.android.graphics.graph.scene.mesh.texture;

import net.gtamps.shared.game.state.State;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

public class TextureAnimation {

    /**
     * Stores the float buffer id for texture coordinates.
     */
    public final int floatBufferId;

    /**
     * Stores all animation of a SpriteSheet.
     */
    private final HashMap<State.Type, TextureSprite[]> animations;

    public TextureAnimation(int floatBufferId, @NotNull TextureSprite[] textureSprites, @NotNull HashMap<State.Type, Integer[]> animations) {
        this.floatBufferId = floatBufferId;
        this.animations = TextureLibrary.convertIndicesToAnimations(textureSprites, animations);
    }

    public TextureSprite[] getAnimation(State.Type type) {
        return animations.get(type);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TextureAnimation)) return false;

        TextureAnimation that = (TextureAnimation) o;

        if (floatBufferId != that.floatBufferId) return false;
        if (!animations.equals(that.animations)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = floatBufferId;
        result = 31 * result + animations.hashCode();
        return result;
    }

    public static TextureAnimation load(int uvResourceID) {
        return TextureLibrary.loadTextureCoordinates(uvResourceID);
    }
}
