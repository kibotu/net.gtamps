package net.gtamps.android.graphics.graph.scene.mesh.texture;

import net.gtamps.android.graphics.utils.Registry;

import java.util.ArrayList;

public class TextureManager {

    private ArrayList<Texture> textures;

    public TextureManager() {
        textures = new ArrayList<Texture>();
    }

    /**
     * Adds item to the list
     */
    public boolean add(Texture texture) {
        if (!Registry.getTextureLibrary().contains(texture.textureId)) return false;
        return textures.add(texture);
    }

    /**
     * Adds item at the given position to the list
     */
    public void add(int index, Texture texture) {
        textures.add(index, texture);
    }

    /**
     * Adds a new Texture with the given textureId to the list, and returns that texture
     */
    public Texture addById(String textureId) {
        if (!Registry.getTextureLibrary().contains(textureId)) {
            throw new Error("Could not create TextureVo using textureId \"" + textureId + "\". TextureManager does not contain that id.");
        }

        Texture t = new Texture(textureId);
        textures.add(t);
        return t;
    }

    /**
     * Adds texture as the sole item in the list, replacing any existing items
     */
    public boolean addReplace(Texture texture) {
        textures.clear();
        return textures.add(texture);
    }

    /**
     * Removes item from the list
     */
    public boolean remove(Texture texture) {
        return textures.remove(texture);
    }

    /**
     * Removes item with the given textureId from the list
     */
    public boolean removeById(String textureId) {
        Texture t = this.getById(textureId);
        if (t == null) {
            throw new Error("No match in TextureList for id \"" + textureId + "\"");
        }
        return textures.remove(t);
    }

    public void removeAll() {
        for (int i = 0; i < textures.size(); i++)
            textures.remove(0);
    }

    /**
     * Get item from the list which is at the given index position
     */
    public Texture get(int index) {
        return textures.get(index);
    }

    /**
     * Gets item from the list which has the given textureId
     */
    public Texture getById(String textureId) {
        for (int i = 0; i < textures.size(); i++) {
            String s = textures.get(i).textureId;
            if (textureId == s) {
                Texture t = textures.get(i);
                return t;
            }
        }
        return null;
    }

    public int size() {
        return textures.size();
    }

    public void clear() {
        textures.clear();
    }

    /**
     * Return a Texture array of TextureList's items
     */
    public Texture[] toArray() {
        Object[] a = textures.toArray();
        Texture[] ret = new Texture[a.length];
        for (int i = 0; i < textures.size(); i++) {
            ret[i] = (Texture) textures.get(i);
        }
        return ret;
    }

    /**
     * Returns a String Array of the textureIds of each of the items in the list
     */
    public String[] getIds() {
        // BTW this makes a casting error. Why?
        // (TextureVo[])_t.toArray();

        String[] a = new String[textures.size()];
        for (int i = 0; i < textures.size(); i++) {
            a[i] = textures.get(i).textureId;
        }
        return a;
    }
}