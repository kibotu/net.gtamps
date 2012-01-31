package net.gtamps.android.graphics.renderer;

import android.graphics.Bitmap;
import net.gtamps.shared.Utils.math.Color4;
import net.gtamps.shared.Utils.math.Frustum;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * User: Jan Rabe, Tom Walroth, Til Börner
 * Date: 31/01/12
 * Time: 18:30
 */
public class GL10Renderer extends BasicRenderer {

    public GL10Renderer(IRenderAction renderAction) {
        super(renderAction);
    }

    @Override
    public void onDrawFrameHook(GL10 unusedGL) {
    }

    @Override
    protected void onSurfaceCreatedHook(GL10 gl10, EGLConfig eglConfig) {
    }

    @Override
    public int allocTexture(Bitmap texture, boolean generateMipMap) {
        return 0;
    }

    @Override
    public int newTextureID() {
        return 0;
    }

    @Override
    public void deleteTexture(int... textureIds) {
    }

    @Override
    public void clearScreen(Color4 bgcolor) {
    }

    @Override
    public void setViewPort(int x, int y, int width, int height) {
    }

    @Override
    public void applyCamera(Frustum frustum) {
    }
}
