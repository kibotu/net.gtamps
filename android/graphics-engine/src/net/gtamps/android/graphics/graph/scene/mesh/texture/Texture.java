package net.gtamps.android.graphics.graph.scene.mesh.texture;

import net.gtamps.android.graphics.utils.Registry;

public class Texture {

    public int textureResourceId;
    public String textureId;
    public boolean isAllocated;
    public boolean hasMipMap;
    public Type type;

    public boolean repeatU = true;
    public boolean repeatV = true;
    public float offsetU = 0;
    public float offsetV = 0;

    public Texture(String textureId) {
        this.textureId = textureId;
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

    public Texture(int textureResourceId, Type type, boolean generateMipMaps) {
        this.textureResourceId = textureResourceId;
        isAllocated = false;
        this.hasMipMap = generateMipMaps;
        this.type = type;
    }

    public void allocate() {
        if (isAllocated) return;
        textureId = String.valueOf(Registry.getTextureLibrary().loadTexture(textureResourceId, hasMipMap));
        isAllocated = true;
    }

    public void invalidate() {
        isAllocated = false;
    }
}
