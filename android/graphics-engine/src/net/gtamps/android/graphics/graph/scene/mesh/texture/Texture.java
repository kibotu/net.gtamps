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
        u_Texture01, u_Texture02, u_Texture03, u_Texture04, u_Texture05, u_Texture06, u_Texture07, u_Texture08;
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
