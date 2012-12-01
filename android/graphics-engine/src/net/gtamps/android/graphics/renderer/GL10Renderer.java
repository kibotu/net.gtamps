package net.gtamps.android.graphics.renderer;

import android.graphics.Bitmap;
import net.gtamps.android.graphics.graph.RenderableNode;
import net.gtamps.android.graphics.graph.scene.mesh.Mesh;
import net.gtamps.android.graphics.graph.scene.mesh.buffermanager.Vbo;
import net.gtamps.android.graphics.graph.scene.primitives.Light;
import net.gtamps.android.graphics.utils.OpenGLUtils;
import net.gtamps.shared.Utils.math.Color4;
import net.gtamps.shared.Utils.math.Frustum;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;
import javax.microedition.khronos.opengles.GL11;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;

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
    protected void onSurfaceChangedHook(GL10 gl10, int width, int height) {
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

    @Override
    public void draw(RenderableNode node) {
    }

    @Override
    public Vbo allocBuffers(FloatBuffer vertexBuffer, FloatBuffer normalBuffer, FloatBuffer uvBuffer, ShortBuffer indexBuffer, FloatBuffer weightBuffer, IntBuffer influenceBuffer) {
        return null;
    }

    @Override
    public Vbo allocBuffers(FloatBuffer vertexBuffer, FloatBuffer normalBuffer, FloatBuffer uvBuffer, ShortBuffer indexBuffer) {
        return null;
    }

    @Override
    public void applyLight(Light light) {
    }

    @Override
    public int allocate(FloatBuffer floatBuffer) {

        GL11 gl11 = (GL11) gl10;

        // generate id
        final IntBuffer buffer = IntBuffer.allocate(1);
        gl11.glGenBuffers(1, buffer);
        int id = buffer.get(0);

        // bind float buffer to generated id
        gl11.glBindBuffer(GL11.GL_ARRAY_BUFFER, id);
        gl11.glBufferData(GL11.GL_ARRAY_BUFFER, floatBuffer.capacity() * OpenGLUtils.BYTES_PER_FLOAT, floatBuffer, GL11.GL_STATIC_DRAW);

        return id;
    }

    @Override
    public void update(Mesh mesh) {
    }
}
