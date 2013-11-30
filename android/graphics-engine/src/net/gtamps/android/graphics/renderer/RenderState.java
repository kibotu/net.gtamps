package net.gtamps.android.graphics.renderer;

/**
 * User: Jan Rabe, Tom Walroth, Til Börner
 * Date: 01/02/12
 * Time: 12:13
 */
public class RenderState {

    private Shader.Type shader = Shader.Type.DEFAULT;
    private boolean hasLighting = true;
    private boolean isDoubleSided = false;

    public RenderState() {
    }

    public Shader.Type getShader() {
        return shader;
    }

    public void setShader(Shader.Type shader) {
        this.shader = shader;
    }

    public boolean hasLighting() {
        return hasLighting;
    }

    public void setDoubleSided(boolean isDoubleSided) {
        this.isDoubleSided = isDoubleSided;
    }

    public boolean isDoubleSided() {
        return isDoubleSided;
    }

    public void setLighting(boolean enableLighting) {
        hasLighting = enableLighting;
    }
}
