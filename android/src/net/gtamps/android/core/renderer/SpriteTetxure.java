package net.gtamps.android.core.renderer;

/**
 * Container Class for holding runtime needed variables of textures.
 */
public class SpriteTetxure {

    /**
     * For resizing purposes of nodes.
     */
    public final float width;

    /**
     * For resizing purposes of nodes.
     */
    public final float height;

    /**
     * Offset id
     */
    public final int offsetId;

    /**
     * Constructs the GL Texture.
     *
     * @param width
     * @param height
     */
    public SpriteTetxure(final float width, final float height, final int offsetId) {
        this.width = width;
        this.height = height;
        this.offsetId = offsetId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SpriteTetxure)) return false;

        SpriteTetxure spriteTetxure = (SpriteTetxure) o;

        if (Float.compare(spriteTetxure.height, height) != 0) return false;
        if (offsetId != spriteTetxure.offsetId) return false;
        if (Float.compare(spriteTetxure.width, width) != 0) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = (width != +0.0f ? Float.floatToIntBits(width) : 0);
        result = 31 * result + (height != +0.0f ? Float.floatToIntBits(height) : 0);
        result = 31 * result + offsetId;
        return result;
    }
}
