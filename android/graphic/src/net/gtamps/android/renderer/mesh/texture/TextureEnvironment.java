package net.gtamps.android.renderer.mesh.texture;

import javax.microedition.khronos.opengles.GL10;

public class TextureEnvironment {

    public int pname = GL10.GL_TEXTURE_ENV_MODE;
    public int param = GL10.GL_MODULATE;

    public TextureEnvironment() {
    }

    public TextureEnvironment(int pname, int param) {
        this.pname = pname;
        this.param = param;
    }

    /**
     * Convenience method
     */
    public void setAll(int pname, int param) {
        this.pname = pname;
        this.param = param;
    }
}