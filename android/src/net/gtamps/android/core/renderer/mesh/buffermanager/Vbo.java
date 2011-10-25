package net.gtamps.android.core.renderer.mesh.buffermanager;

import net.gtamps.android.core.utils.OpenGLUtils;
import net.gtamps.shared.Utils.Logger;

import javax.microedition.khronos.opengles.GL10;
import javax.microedition.khronos.opengles.GL11;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;

public class Vbo {

    private static final String TAG = Vbo.class.getSimpleName();

    public int vertexBufferId;
    public int indexBufferId;
    public int normalBufferId;
    public int colorBufferId;
    public int textureCoordinateBufferId;

    public FloatBuffer vertexBuffer;
    public ShortBuffer indexBuffer;
    public FloatBuffer normalBuffer;
    public FloatBuffer colorBuffer;
    public FloatBuffer textureCoordinateBuffer;

    private boolean isAllocated;

    public Vbo(FloatBuffer vertexBuffer, ShortBuffer indexBuffer, FloatBuffer normalBuffer, FloatBuffer colorBuffer, FloatBuffer textureCoordinateBuffer) {
        this.vertexBuffer = vertexBuffer;
        this.indexBuffer = indexBuffer;
        this.normalBuffer = normalBuffer;
        this.colorBuffer = colorBuffer;
        this.textureCoordinateBuffer = textureCoordinateBuffer;
        isAllocated = false;
    }

    private void positionZero() {
        if(vertexBuffer != null ) vertexBuffer.position(0);
        if(indexBuffer != null) indexBuffer.position(0);
        if(normalBuffer != null) normalBuffer.position(0);
        if(colorBuffer != null) colorBuffer.position(0);
        if(textureCoordinateBuffer != null) textureCoordinateBuffer.position(0);
    }

    public void alloc(GL10 gl) {

        if(isAllocated) {
            return;
        }

        positionZero();

        // OpenGL 1.1-Instanz beziehen
        final GL11 gl11 = (GL11) gl;

        final IntBuffer buffer = IntBuffer.allocate(5);
        gl11.glGenBuffers(5, buffer);

        // get ids
        vertexBufferId = buffer.get(0);
        indexBufferId = buffer.get(1);
        normalBufferId = buffer.get(2);
        colorBufferId = buffer.get(3);
        textureCoordinateBufferId = buffer.get(4);

        // Vertex-VBO binden und beladen
        gl11.glBindBuffer(GL11.GL_ARRAY_BUFFER, vertexBufferId);
        gl11.glBufferData(GL11.GL_ARRAY_BUFFER, vertexBuffer.capacity() * OpenGLUtils.BYTES_PER_FLOAT, vertexBuffer, GL11.GL_STATIC_DRAW);

        // Index-VBO binden und beladen
        gl11.glBindBuffer(GL11.GL_ELEMENT_ARRAY_BUFFER, indexBufferId);
        gl11.glBufferData(GL11.GL_ELEMENT_ARRAY_BUFFER, indexBuffer.capacity() * OpenGLUtils.BYTES_PER_SHORT, indexBuffer, GL11.GL_STATIC_DRAW);

        // Normal-VBO binden und beladen
        gl11.glBindBuffer(GL11.GL_ARRAY_BUFFER, normalBufferId);
        gl11.glBufferData(GL11.GL_ARRAY_BUFFER, normalBuffer.capacity() * OpenGLUtils.BYTES_PER_FLOAT, normalBuffer, GL11.GL_STATIC_DRAW);

        if (colorBuffer != null) {
            // Color-VBO binden und beladen
            gl11.glBindBuffer(GL11.GL_ARRAY_BUFFER, colorBufferId);
            gl11.glBufferData(GL11.GL_ARRAY_BUFFER, colorBuffer.capacity() * OpenGLUtils.BYTES_PER_FLOAT, colorBuffer, GL11.GL_STATIC_DRAW);
        }
        if (textureCoordinateBuffer != null) {
            // Texture-VBO binden und beladen
            gl11.glBindBuffer(GL11.GL_ARRAY_BUFFER, textureCoordinateBufferId);
            gl11.glBufferData(GL11.GL_ARRAY_BUFFER, textureCoordinateBuffer.capacity() * OpenGLUtils.BYTES_PER_FLOAT, textureCoordinateBuffer, GL11.GL_STATIC_DRAW);
        }

        // Puffer abwählen
        gl11.glBindBuffer(GL11.GL_ARRAY_BUFFER, 0);
        gl11.glBindBuffer(GL11.GL_ELEMENT_ARRAY_BUFFER, 0);

        isAllocated = true;
        Logger.i(this,toString());
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
        if(colorBuffer != null) colorBuffer.clear();
        if(textureCoordinateBuffer != null) textureCoordinateBuffer.clear();
    }

    public void deallocate() {
        // clear buffers from opengl
    }

    @Override
    public String toString() {
        return  "[isAllocated=" + isAllocated +
                "|vertexBId=" + vertexBufferId +
                "|indexBId=" + indexBufferId +
                "|normalBId=" + normalBufferId +
                "|colorBId=" + colorBufferId +
                "|textureCoordId=" + textureCoordinateBufferId +
                ']';
    }
}
