package net.gtamps.android.renderer.mesh.buffermanager;

import android.opengl.GLES20;
import net.gtamps.android.renderer.RenderCapabilities;
import net.gtamps.android.renderer.utils.OpenGLUtils;
import net.gtamps.shared.Utils.Logger;

import javax.microedition.khronos.opengles.GL10;
import javax.microedition.khronos.opengles.GL11;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;

public class Vbo {

    public int vertexBufferId;
    public int indexBufferId;
    public int normalBufferId;
    public int colorBufferId;
    public int textureCoordinateBufferId;

    public FloatBuffer vertexBuffer;
    public FloatBuffer normalBuffer;
    public FloatBuffer colorBuffer;
    public FloatBuffer textureCoordinateBuffer;
    public ShortBuffer indexBuffer;

    private boolean isAllocated;

    public Vbo() {
    }

    private void positionZero() {
        if (vertexBuffer != null) vertexBuffer.position(0);
        if (indexBuffer != null) indexBuffer.position(0);
        if (normalBuffer != null) normalBuffer.position(0);
        if (colorBuffer != null) colorBuffer.position(0);
        if (textureCoordinateBuffer != null) textureCoordinateBuffer.position(0);
    }

    private void allocBuffersGLES20() {
        // get buffer ids
        final int[] buffer = new int[5];
        GLES20.glGenBuffers(5, buffer, 0);
        vertexBufferId = buffer[0];
        indexBufferId = buffer[1];
        normalBufferId = buffer[2];
        colorBufferId = buffer[3];
        textureCoordinateBufferId = buffer[4];

        // bind vertex buffer
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, vertexBufferId);
        GLES20.glBufferData(GLES20.GL_ARRAY_BUFFER, vertexBuffer.capacity() * OpenGLUtils.BYTES_PER_FLOAT, vertexBuffer, GLES20.GL_STATIC_DRAW);

        // bind index buffer
        GLES20.glBindBuffer(GLES20.GL_ELEMENT_ARRAY_BUFFER, indexBufferId);
        GLES20.glBufferData(GLES20.GL_ELEMENT_ARRAY_BUFFER, indexBuffer.capacity() * OpenGLUtils.BYTES_PER_SHORT, indexBuffer, GLES20.GL_STATIC_DRAW);

        // bind normal buffer
        if (normalBuffer != null) {
            GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, normalBufferId);
            GLES20.glBufferData(GLES20.GL_ARRAY_BUFFER, normalBuffer.capacity() * OpenGLUtils.BYTES_PER_FLOAT, normalBuffer, GLES20.GL_STATIC_DRAW);
        }

        // bind color buffer
        if (colorBuffer != null) {
            GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, colorBufferId);
            GLES20.glBufferData(GLES20.GL_ARRAY_BUFFER, colorBuffer.capacity() * OpenGLUtils.BYTES_PER_FLOAT, colorBuffer, GLES20.GL_STATIC_DRAW);
        }

        // bind uv buffer
        if (textureCoordinateBuffer != null) {
            GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, textureCoordinateBufferId);
            GLES20.glBufferData(GLES20.GL_ARRAY_BUFFER, textureCoordinateBuffer.capacity() * OpenGLUtils.BYTES_PER_FLOAT, textureCoordinateBuffer, GLES20.GL_STATIC_DRAW);
        }

        // deselect buffers
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, 0);
        GLES20.glBindBuffer(GLES20.GL_ELEMENT_ARRAY_BUFFER, 0);
    }

    private void allocateBuffersGL10(GL10 gl) {
        // OpenGL 1.1-Instanz beziehen
        final GL11 gl11 = (GL11) gl;

        // get buffer ids
        final IntBuffer buffer = IntBuffer.allocate(5);
        gl11.glGenBuffers(5, buffer);
        vertexBufferId = buffer.get(0);
        indexBufferId = buffer.get(1);
        normalBufferId = buffer.get(2);
        colorBufferId = buffer.get(3);
        textureCoordinateBufferId = buffer.get(4);

        // bind vertex buffer
        gl11.glBindBuffer(GL11.GL_ARRAY_BUFFER, vertexBufferId);
        gl11.glBufferData(GL11.GL_ARRAY_BUFFER, vertexBuffer.capacity() * OpenGLUtils.BYTES_PER_FLOAT, vertexBuffer, GL11.GL_STATIC_DRAW);

        // bind index buffer
        gl11.glBindBuffer(GL11.GL_ELEMENT_ARRAY_BUFFER, indexBufferId);
        gl11.glBufferData(GL11.GL_ELEMENT_ARRAY_BUFFER, indexBuffer.capacity() * OpenGLUtils.BYTES_PER_SHORT, indexBuffer, GL11.GL_STATIC_DRAW);

        // bind normal buffer
        if (normalBuffer != null) {
            gl11.glBindBuffer(GL11.GL_ARRAY_BUFFER, normalBufferId);
            gl11.glBufferData(GL11.GL_ARRAY_BUFFER, normalBuffer.capacity() * OpenGLUtils.BYTES_PER_FLOAT, normalBuffer, GL11.GL_STATIC_DRAW);
        }

        // bind color buffer
        if (colorBuffer != null) {
            gl11.glBindBuffer(GL11.GL_ARRAY_BUFFER, colorBufferId);
            gl11.glBufferData(GL11.GL_ARRAY_BUFFER, colorBuffer.capacity() * OpenGLUtils.BYTES_PER_FLOAT, colorBuffer, GL11.GL_STATIC_DRAW);
        }

        // bind uv buffer
        if (textureCoordinateBuffer != null) {
            gl11.glBindBuffer(GL11.GL_ARRAY_BUFFER, textureCoordinateBufferId);
            gl11.glBufferData(GL11.GL_ARRAY_BUFFER, textureCoordinateBuffer.capacity() * OpenGLUtils.BYTES_PER_FLOAT, textureCoordinateBuffer, GL11.GL_STATIC_DRAW);
        }

        // deselect buffers
        gl11.glBindBuffer(GL11.GL_ARRAY_BUFFER, 0);
        gl11.glBindBuffer(GL11.GL_ELEMENT_ARRAY_BUFFER, 0);
    }

    public void allocBuffers(GL10 gl) {

        // don't allocate twice
        if (isAllocated) return;

        // set buffer pointer to position 0
        positionZero();

        // allocate buffers
        if (RenderCapabilities.supportsGLES20()) {
            allocBuffersGLES20();
        } else {
            allocateBuffersGL10(gl);
        }

        // set true if everything went well
        isAllocated = true;

        // verbose message
//        Logger.v(this, toString());
    }

    public boolean isAllocated() {
        return isAllocated;
    }

    @Override
    public int hashCode() {
        int result = vertexBufferId;
        result = 31 * result + indexBufferId;
        result = 31 * result + normalBufferId;
        result = 31 * result + colorBufferId;
        result = 31 * result + textureCoordinateBufferId;
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Vbo)) return false;

        Vbo vbo = (Vbo) o;

        if (colorBufferId != vbo.colorBufferId) return false;
        if (indexBufferId != vbo.indexBufferId) return false;
        if (normalBufferId != vbo.normalBufferId) return false;
        if (textureCoordinateBufferId != vbo.textureCoordinateBufferId) return false;
        if (vertexBufferId != vbo.vertexBufferId) return false;

        return true;
    }

    public void clearBuffer() {
        vertexBuffer.clear();
        indexBuffer.clear();
        normalBuffer.clear();
        if (colorBuffer != null) colorBuffer.clear();
        if (textureCoordinateBuffer != null) textureCoordinateBuffer.clear();
    }

    public void deallocate() {
        // clear buffers from opengl
    }

    @Override
    public String toString() {
        return "[isAllocated=" + isAllocated +
                "|vertexBId=" + vertexBufferId +
                "|indexBId=" + indexBufferId +
                "|normalBId=" + normalBufferId +
                "|colorBId=" + colorBufferId +
                "|textureCoordId=" + textureCoordinateBufferId +
                ']';
    }

    public Vbo set(FloatBuffer vertexBuffer, ShortBuffer indexBuffer, FloatBuffer normalBuffer, FloatBuffer colorBuffer, FloatBuffer textureCoordinateBuffer) {
        this.vertexBuffer = vertexBuffer;
        this.indexBuffer = indexBuffer;
        this.normalBuffer = normalBuffer;
        this.colorBuffer = colorBuffer;
        this.textureCoordinateBuffer = textureCoordinateBuffer;
        isAllocated = false;
        return this;
    }

    public Vbo set(Vbo vbo) {
        vertexBufferId = vbo.vertexBufferId;
        indexBufferId = vbo.indexBufferId;
        normalBufferId = vbo.normalBufferId;
        colorBufferId = vbo.colorBufferId;
        textureCoordinateBufferId = vbo.textureCoordinateBufferId;
        isAllocated = true;
        return this;
    }

    public void invalidate() {
        isAllocated = false;
    }
}
