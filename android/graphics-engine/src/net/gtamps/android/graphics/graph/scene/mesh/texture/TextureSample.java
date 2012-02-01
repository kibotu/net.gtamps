package net.gtamps.android.graphics.graph.scene.mesh.texture;

import net.gtamps.android.graphics.utils.Registry;

public class TextureSample {

    public int textureResourceId;
    public int textureId;
    public boolean isAllocated;
    public boolean hasMipMap;
    public Type type;

    /**
     * Types supported by shader.
     */
    public enum Type {
        texture_01, texture_02, texture_03, texture_04, texture_05, texture_06, texture_07, texture_08;
    }

    public TextureSample(int resourceId, Type type) {
        this(resourceId, type, true);
    }

    public TextureSample(int textureResourceId, Type type, boolean generateMipMaps) {
        this.textureResourceId = textureResourceId;
        isAllocated = false;
        this.hasMipMap = generateMipMaps;
        this.type = type;
    }

    public void allocate() {
        if (isAllocated) return;
        textureId = Registry.getTextureLibrary().loadTexture(textureResourceId, hasMipMap);
        isAllocated = true;
    }

    public void invalidate() {
        isAllocated = false;
    }
}
