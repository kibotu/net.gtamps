package net.gtamps.android.renderer.mesh.texture;

import java.util.ArrayList;

public class Texture {

    /**
     * The texureId in the TextureManager that corresponds to an uploaded Bitmap
     */
    public String textureId;

    /**
     * Determines if U and V ("S" and "T" in OpenGL parlance) repeat, or are 'clamped'
     * (Defaults to true, matching OpenGL's default setting)
     */
    public boolean repeatU = true;
    public boolean repeatV = true;

    /**
     * The U/V offsets for the texture (rem, normal range of U and V are 0 to 1)
     */
    public float offsetU = 0;
    public float offsetV = 0;

    /**
     * A list of TexEnvVo's that define how texture is composited in the output.
     * Normally contains just one element.
     */
    public ArrayList<TextureEnvironment> textureEnvs;

    public Texture(String textureId, ArrayList<TextureEnvironment> textureEnvVo) {
        this.textureId = textureId;
        textureEnvs = textureEnvVo;
    }

    public Texture(String textureId) {
        this.textureId = textureId;
        textureEnvs = new ArrayList<TextureEnvironment>();
        textureEnvs.add(new TextureEnvironment());
    }

    public String getTextureId() {
        return textureId;
    }
}
