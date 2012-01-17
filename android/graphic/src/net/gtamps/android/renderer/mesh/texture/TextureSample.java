package net.gtamps.android.renderer.mesh.texture;

import net.gtamps.android.renderer.Registry;
import net.gtamps.android.renderer.RenderCapabilities;
import net.gtamps.shared.Utils.Logger;

import java.util.LinkedList;

public class TextureSample {

    /**
     * static pool for available GL_TEXTURE ids
     */
    private static LinkedList<Integer> availableTextures;

    public void allocate() {
        if (isAllocated) return;
        textureId = Registry.getTextureLibrary().loadTexture(textureResourceId, hasMipMap);
        isAllocated = true;
    }

    public enum Type {
        colorMap, color2Map, normalMap, alphaMap, specularMap;
    }

    public TextureSample(int textureResourceId, Type type, boolean hasMipMap) {
        if (availableTextures == null) {
            availableTextures = new LinkedList<Integer>();
            for (int i = 0; i < RenderCapabilities.maxTextureUnits(); i++) {
                availableTextures.add(i);
            }
        }
        if (availableTextures.isEmpty()) {
            Logger.e(this, "GL_TEXTURE resources exceeded.");
        }
        activeTextureId = availableTextures.poll();
        this.textureResourceId = textureResourceId;
        isAllocated = false;
        this.hasMipMap = hasMipMap;
        this.type = type;
    }

    public TextureSample(int resourceId, Type type) {
        this(resourceId, type, true);
    }

    public int textureResourceId;
    public int activeTextureId;
    public int textureId;
    public boolean isAllocated;
    public boolean hasMipMap;
    public Type type;

    public void invalidate() {
        isAllocated = false;
    }
}
