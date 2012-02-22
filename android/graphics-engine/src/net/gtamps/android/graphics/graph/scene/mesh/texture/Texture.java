package net.gtamps.android.graphics.graph.scene.mesh.texture;

import net.gtamps.android.graphics.utils.Registry;

public class Texture {

    public int textureResourceID;
    public String textureID;
    public boolean isAllocated;
    public boolean hasMipMap;
    public Type type;

    public boolean repeatU = true;
    public boolean repeatV = true;
    public float offsetU = 0;
    public float offsetV = 0;

    public Texture(String textureID) {
        this.textureID = textureID;
    }

    /**
     * Types supported by shader.
     */
    public enum Type {
        texture_01, texture_02, texture_03, texture_04, texture_05, texture_06, texture_07, texture_08;
    }

    public Texture(int resourceId, Type type) {
        this(resourceId, type, true);
    }

    /**
     * @param textureResourceID
     * @param type
     * @param generateMipMaps
     */
    public Texture(int textureResourceID, Type type, boolean generateMipMaps) {
        this.textureResourceID = textureResourceID;
        isAllocated = false;
        this.hasMipMap = generateMipMaps;
        this.type = type;
    }

    public void allocate() {
        if (isAllocated) return;
        textureID = String.valueOf(Registry.getTextureLibrary().loadTexture(textureResourceID, hasMipMap));
        isAllocated = true;
    }

    public void invalidate() {
        isAllocated = false;
    }
}
