package net.gtamps.android.core.renderer;

import net.gtamps.shared.state.State;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

public class BufferedTexture {

    private static final String TAG = BufferedTexture.class.getSimpleName();

    /**
     * Stores the generated texture id
     */
    public final int textureId;

    /**
     * Stores the float buffer id for texture coordinates.
     */
    public final int floatBufferId;

    /**
     * Stores all animation of a SpriteSheet.
     */
    private final HashMap<State.Type, SpriteTetxure[]> animations;

    public BufferedTexture(int textureId, int floatBufferId, @NotNull SpriteTetxure[] spriteTetxures, @NotNull HashMap<State.Type, Integer[]> animations) {
        this.textureId = textureId;
        this.floatBufferId = floatBufferId;
        this.animations = convertIndicesToAnimations(spriteTetxures,animations);
    }

    /**
     * Converts a map of indices to animations and stores them into a new hashmap.
     *
     * @param spriteTetxures
     * @param indexedAnims
     * @return HashMap with animations.
     */
    public static HashMap<State.Type, SpriteTetxure[]> convertIndicesToAnimations(@NotNull SpriteTetxure[] spriteTetxures, @NotNull HashMap<State.Type, Integer[]> indexedAnims) {

        HashMap<State.Type, SpriteTetxure[]> anims = new HashMap<State.Type, SpriteTetxure[]>();

        for(HashMap.Entry<State.Type, Integer[]> entry: indexedAnims.entrySet()){
            Integer[] temp = entry.getValue();
            SpriteTetxure[] textures = new SpriteTetxure[temp.length];
            for(int i = 0; i < temp.length; i++) {
                textures[i] = spriteTetxures[temp[i]];
            }
            anims.put(entry.getKey(),textures);
        }

        indexedAnims.clear();

        return anims;
    }

    public SpriteTetxure[] getAnimation(State.Type type) {
        return animations.get(type);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BufferedTexture)) return false;

        BufferedTexture that = (BufferedTexture) o;

        if (floatBufferId != that.floatBufferId) return false;
        if (textureId != that.textureId) return false;
        if (animations != null ? !animations.equals(that.animations) : that.animations != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = textureId;
        result = 31 * result + floatBufferId;
        result = 31 * result + (animations != null ? animations.hashCode() : 0);
        return result;
    }
}
