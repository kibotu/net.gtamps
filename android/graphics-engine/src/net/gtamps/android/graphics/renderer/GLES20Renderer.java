package net.gtamps.android.graphics.renderer;

import android.graphics.Bitmap;
import android.opengl.GLUtils;
import android.text.Html;
import com.badlogic.gdx.backends.android.AndroidGL20;
import fix.android.opengl.GLES20;
import net.gtamps.android.graphics.graph.RenderableNode;
import net.gtamps.android.graphics.graph.scene.animation.skeleton.AnimatedSkeletonObject3D;
import net.gtamps.android.graphics.graph.scene.mesh.Material;
import net.gtamps.android.graphics.graph.scene.mesh.Mesh;
import net.gtamps.android.graphics.graph.scene.mesh.buffermanager.Vbo;
import net.gtamps.android.graphics.graph.scene.mesh.texture.Texture;
import net.gtamps.android.graphics.graph.scene.mesh.texture.TextureAnimation;
import net.gtamps.android.graphics.graph.scene.mesh.texture.TextureSprite;
import net.gtamps.android.graphics.graph.scene.primitives.Light;
import net.gtamps.android.graphics.utils.OpenGLUtils;
import net.gtamps.shared.Config;
import net.gtamps.shared.Utils.Logger;
import net.gtamps.shared.Utils.math.Color4;
import net.gtamps.shared.Utils.math.Frustum;
import net.gtamps.shared.Utils.math.Matrix4;
import org.jetbrains.annotations.NotNull;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;
import javax.microedition.khronos.opengles.GL11;
import java.nio.*;

import static android.opengl.GLES20.*;

/**
 * User: Jan Rabe, Tom Walroth, Til Börner
 * Date: 31/01/12
 * Time: 18:30
 */
public class GLES20Renderer extends BasicRenderer {

    /**
     * gdx additional gl functions (ndk)
     */
    private AndroidGL20 mGlEs20;

    public GLES20Renderer(IRenderAction renderAction) {
        super(renderAction);
    }

    @Override
    public void onDrawFrameHook(GL10 unusedGL) {
        glUseProgram(activeShaderProgram);
    }

    @Override
    protected void onSurfaceCreatedHook(GL10 gl10, EGLConfig eglConfig) {
        glEnable(GL_DEPTH_TEST);
        glClearDepthf(1.0f);
        glDepthFunc(GL_LEQUAL);
        glDepthMask(true);

        // cull backface
        glEnable(GL_CULL_FACE);

        // getting additional gl functions
//        mGlEs20 = new AndroidGL20();
    }

    @Override
    protected void onSurfaceChangedHook(GL10 gl10, int width, int height) {
        Shader.load();
    }

    @Override
    public int allocTexture(Bitmap texture, boolean generateMipMap) {
        int bitmapFormat = texture.getConfig() == Bitmap.Config.ARGB_8888 ? GL_RGBA : GL_RGB;
        int textureId = newTextureID();
        glBindTexture(GL_TEXTURE_2D, textureId);
        GLUtils.texImage2D(GL_TEXTURE_2D, 0, bitmapFormat, texture, 0);
        glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, generateMipMap ? GL_NEAREST_MIPMAP_NEAREST : GL_NEAREST);
        glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);
        glGenerateMipmap(GL_TEXTURE_2D);
        Logger.i(this, "[w=" + texture.getWidth() + "|h=" + texture.getHeight() + "|id=" + textureId + "|mipmaps=" + generateMipMap + "] Bitmap atlas successfully allocated.");
        texture.recycle();
        return textureId;
    }

    @Override
    public int newTextureID() {
        int[] textureIds = new int[1];
        glGenTextures(1, textureIds, 0);
        return textureIds[0];
    }

    @Override
    public void deleteTexture(int... textureIds) {
        glDeleteTextures(textureIds.length, textureIds, 0);
    }

    @Override
    public void clearScreen(Color4 bgcolor) {
        glClearColor(bgcolor.r, bgcolor.g, bgcolor.b, bgcolor.a);
        glClear(GL_DEPTH_BUFFER_BIT | GL_COLOR_BUFFER_BIT | GL_STENCIL_BUFFER_BIT);
    }

    @Override
    public void setViewPort(int x, int y, int width, int height) {
        glViewport(x, y, width, height);
        Logger.checkGlError(this, "glViewPort");
    }

    @Override
    public void applyCamera(Frustum frustum) {

        if (frustum.hasDepthTest()) glEnable(GL_DEPTH_TEST);
        else glDisable(GL_DEPTH_TEST);

        Matrix4 viewMatrix = frustum.getViewMatrix();
        Matrix4.setLookAt(viewMatrix, frustum.getPosition(), frustum.getTarget(), frustum.getUp());
        glUniformMatrix4fv(glGetUniformLocation(activeShaderProgram, "u_WorldViewProjection"), 1, false, frustum.getProjectionMatrix().values, 0);
        glUniformMatrix4fv(glGetUniformLocation(activeShaderProgram, "u_WorldView"), 1, false, viewMatrix.values, 0);
    }

    @Override
    public void draw(RenderableNode node) {
        if (Config.USEVBO) drawVbo(node);
        else drawArray(node);
    }

    @Deprecated
    private void drawArray(RenderableNode node) {
        Mesh mesh = node.getMesh();
        if (mesh == null) return;

        activeShaderProgram = node.getRenderState().getShader().getProgram();
        RenderState renderState = node.getRenderState();

        // back face culling
        if (renderState.isDoubleSided()) glEnable(GL_CULL_FACE);
        else glDisable(GL_CULL_FACE);

        // send to the shader
        glUniformMatrix4fv(glGetUniformLocation(activeShaderProgram, "modelMatrix"), 1, false, node.getCombinedTransformation().values, 0);

        // vertices
        glVertexAttribPointer(glGetAttribLocation(activeShaderProgram, "vertexPosition"), 3, GL_FLOAT, false, 0, mesh.vertices.getVertices().getBuffer());
        glEnableVertexAttribArray(glGetAttribLocation(activeShaderProgram, "vertexPosition"));

        // normals
        glVertexAttribPointer(glGetAttribLocation(activeShaderProgram, "vertexNormal"), 3, GL_FLOAT, false, 0, mesh.vertices.getNormals().getBuffer());
        glEnableVertexAttribArray(glGetAttribLocation(activeShaderProgram, "vertexNormal"));

        // material
        Material material = node.getMaterial();
        glUniform4fv(glGetUniformLocation(activeShaderProgram, "u_LightDiffuse"), 1, material.getEmission().asArray(), 0);
        glUniform1f(glGetUniformLocation(activeShaderProgram, "u_KA"),material.getAmbient().asArray()[1]);
        glUniform1f(glGetUniformLocation(activeShaderProgram, "u_KD"),material.getDiffuse().asArray()[1]);
        glUniform1f(glGetUniformLocation(activeShaderProgram, "u_KS"),material.getSpecular().asArray()[1]);
        glUniform1f(glGetUniformLocation(activeShaderProgram, "u_SExponent"), material.getShininess());

        // multiple textures
        if (!node.getTextures().isEmpty()) {
            glUniform1i(glGetUniformLocation(activeShaderProgram, "hasTextures"), 2);
            if (node.getTextures().size() > 8)
                Logger.V(this, node.getClass().getSimpleName() + " has exceeded it's max texture limit : " + node.getTextures().size() + "/" + RenderCapabilities.maxTextureUnits());
            for (int i = 0; i < node.getTextures().size(); i++) {
                Texture texture = node.getTextures().get(i);
                glActiveTexture(GL_TEXTURE0 + i);
                glBindTexture(GL_TEXTURE_2D, Integer.parseInt(texture.textureID));
                glUniform1i(glGetUniformLocation(activeShaderProgram, texture.type.name()), i);
                glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, texture.hasMipMap ? GL_LINEAR_MIPMAP_LINEAR : GL_NEAREST);
            }

            // uvs
            glVertexAttribPointer(glGetAttribLocation(activeShaderProgram, "vertexUv"), 2, GL_FLOAT, false, 0, mesh.vertices.getUvs().getBuffer());
            glEnableVertexAttribArray(glGetAttribLocation(activeShaderProgram, "vertexUv"));
        } else {
            glUniform1i(glGetUniformLocation(activeShaderProgram, "hasTextures"), 0);
        }

        // uses lightning
        glUniform1i(glGetUniformLocation(activeShaderProgram, "hasLighting"), renderState.hasLighting() ? 1 : 0);

        // Draw with indices
        glDrawElements(GL_TRIANGLES, mesh.faces.getBuffer().capacity(), GL_UNSIGNED_SHORT, mesh.faces.getBuffer());

        // unbind to avoid accidental manipulation
        glBindTexture(GL_TEXTURE_2D, 0);
    }

    @NotNull
    @Override
    public Bitmap captureScreenShot(@NotNull GL10 gl) {
            int width = viewPort.getWidth();
            int height = viewPort.getHeight();
            int screenshotSize = width * height;
            ByteBuffer bb = ByteBuffer.allocateDirect(screenshotSize * 4);
            bb.order(ByteOrder.nativeOrder());
            gl.glReadPixels(0, 0, width, height, GL10.GL_RGBA, GL10.GL_UNSIGNED_BYTE, bb);
            int pixelsBuffer[] = new int[screenshotSize];
            bb.asIntBuffer().get(pixelsBuffer);
            bb = null;
            Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
            bitmap.setPixels(pixelsBuffer, screenshotSize-width, -width, 0, 0, width, height);
            pixelsBuffer = null;

            short sBuffer[] = new short[screenshotSize];
            ShortBuffer sb = ShortBuffer.wrap(sBuffer);
            bitmap.copyPixelsToBuffer(sb);

            //Making created bitmap (from OpenGL points) compatible with Android bitmap
            for (int i = 0; i < screenshotSize; ++i) {
                short v = sBuffer[i];
                sBuffer[i] = (short) (((v&0x1f) << 11) | (v&0x7e0) | ((v&0xf800) >> 11));
            }
            sb.rewind();
            bitmap.copyPixelsFromBuffer(sb);
            return bitmap;
    }

    private void drawVbo(RenderableNode node) {
        Mesh mesh = node.getMesh();
        if (mesh == null) return;

        activeShaderProgram = node.getRenderState().getShader().getProgram();
        RenderState renderState = node.getRenderState();

        if (renderState.isDoubleSided()) glEnable(GL_CULL_FACE);
        else glDisable(GL_CULL_FACE);

        // vertices
        glBindBuffer(GL_ARRAY_BUFFER, mesh.vbo.vertexBufferID);
        GLES20.glVertexAttribPointer(glGetAttribLocation(activeShaderProgram, "in_Position"), 3, GL_FLOAT, false, 0, 0);
        glEnableVertexAttribArray(glGetAttribLocation(activeShaderProgram, "in_Position"));

        // normals
        glBindBuffer(GL_ARRAY_BUFFER, mesh.vbo.normalBufferID);
        GLES20.glVertexAttribPointer(glGetAttribLocation(activeShaderProgram, "in_Normal"), 3, GL_FLOAT, false, 0, 0);
        glEnableVertexAttribArray(glGetAttribLocation(activeShaderProgram, "in_Normal"));

        // material
        Material material = node.getMaterial();
//        glUniform4fv(glGetUniformLocation(activeShaderProgram, "u_LightDiffuse"), 1, material.getEmission().asArray(), 0);
//        glUniform1f(glGetUniformLocation(activeShaderProgram, "u_KA"),material.getAmbient().asArray()[0]);
//        glUniform1f(glGetUniformLocation(activeShaderProgram, "u_KD"),material.getDiffuse().asArray()[0]);
//        glUniform1f(glGetUniformLocation(activeShaderProgram, "u_KS"),material.getSpecular().asArray()[0]);
//        glUniform1f(glGetUniformLocation(activeShaderProgram, "u_SExponent"), material.getShininess());
        glUniform4fv(glGetUniformLocation(activeShaderProgram, "u_LightDiffuse"), 1,new float [] {1.0f, 1.0f, 1.0f, 1.0f}, 0);
        glUniform1f(glGetUniformLocation(activeShaderProgram, "u_KA"),0.45f);
        glUniform1f(glGetUniformLocation(activeShaderProgram, "u_KD"),0.1f);
        glUniform1f(glGetUniformLocation(activeShaderProgram, "u_KS"),0.15f);
        glUniform1f(glGetUniformLocation(activeShaderProgram, "u_SExponent"), 8.0f);

        // multiple textures
        if (!node.getTextures().isEmpty()) {
            glUniform1i(glGetUniformLocation(activeShaderProgram, "hasTextures"), 2);
            if (node.getTextures().size() > 8)
                Logger.V(this, node.getClass().getSimpleName() + " has exceeded it's max texture limit : " + node.getTextures().size() + "/" + RenderCapabilities.maxTextureUnits());
            for (int i = 0; i < node.getTextures().size(); i++) {
                Texture texture = node.getTextures().get(i);
                glActiveTexture(GL_TEXTURE0 + i);
                glBindTexture(GL_TEXTURE_2D, Integer.parseInt(texture.textureID));
                glUniform1i(glGetUniformLocation(activeShaderProgram, texture.type.name()), i);
                glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, texture.hasMipMap ? GL_LINEAR_MIPMAP_LINEAR : GL_NEAREST);
            }

            // uvs
            if (node.hasTextureAnimation()) {
                TextureAnimation uvsheet = node.getTextureAnimation();
                TextureSprite textureSprite = node.getTextureSprite();
                glBindBuffer(GL_ARRAY_BUFFER, uvsheet.floatBufferId);
                GLES20.glVertexAttribPointer(glGetAttribLocation(activeShaderProgram, "in_TexCoords"), 2, GL_FLOAT, false, 0, textureSprite.offsetId);
            } else {
                glBindBuffer(GL_ARRAY_BUFFER, mesh.vbo.uvBufferID);
                GLES20.glVertexAttribPointer(glGetAttribLocation(activeShaderProgram, "in_TexCoords"), 2, GL_FLOAT, false, 0, 0);

            }
            glEnableVertexAttribArray(glGetAttribLocation(activeShaderProgram, "in_TexCoords"));
        } else {
            glUniform1i(glGetUniformLocation(activeShaderProgram, "hasTextures"), 0);
        }

        // uses lightning
//        glUniform1i(glGetUniformLocation(activeShaderProgram, "hasLighting"), renderState.hasLighting() ? 1 : 0);

        // send to the shader
        glUniformMatrix4fv(glGetUniformLocation(activeShaderProgram, "u_ModelViewMatrix"), 1, false, node.getCombinedTransformation().values, 0);

//        if(mesh.hasBones()) {
//            // draw weights
//            glBindBuffer(GL_ARRAY_BUFFER, mesh.vbo.weightBufferID);
//            GLES20.glVertexAttribPointer(glGetAttribLocation(activeShaderProgram, "in_Weights"), 4, GL_FLOAT, false, 0, 0);
//            glEnableVertexAttribArray(glGetAttribLocation(activeShaderProgram, "in_Weights"));
//
//            // draw influences
//            glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, mesh.vbo.influenceBufferID);
//            GLES20.glVertexAttribPointer(glGetAttribLocation(activeShaderProgram, "in_Influences"), 4, GL_INT, false, 0, 0);
//            glEnableVertexAttribArray(glGetAttribLocation(activeShaderProgram, "in_Influences"));
//
//            // bone transformations
//            glUniformMatrix4fv(glGetUniformLocation(activeShaderProgram, "u_BoneTransform"), AnimatedSkeletonObject3D.MAX_BONE_TRANSFORMS, false, ((AnimatedSkeletonObject3D)node).getBoneTransformations(), 0);
//        }

        // Draw with indices
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, mesh.vbo.indexBufferID);
        GLES20.glDrawElements(GL_TRIANGLES, mesh.faces.getBuffer().capacity(), GL_UNSIGNED_SHORT, 0);

        // unbind to avoid accidental manipulation
        glBindTexture(GL_TEXTURE_2D, 0);

//        Logger.v(this, "drawing "+node.uID + " at " +  node.getCombinedTransformation());
    }

    public int [] genBuffer(int size) {
        int[] buffer = new int[size];
        glGenBuffers(1, buffer, 0);
        return buffer;
    }

    private final int[] vboIDBuffer = new int[6];

    @Override
    public Vbo allocBuffers(FloatBuffer vertexBuffer, FloatBuffer normalBuffer, FloatBuffer uvBuffer, ShortBuffer indexBuffer, FloatBuffer weightBuffer, IntBuffer influenceBuffer) {

        glGenBuffers(6, vboIDBuffer, 0);

        Vbo vbo = new Vbo(vboIDBuffer, true);

        // bind vertex buffer
        if (vertexBuffer != null) {
            glBindBuffer(GL_ARRAY_BUFFER, vbo.vertexBufferID);
            glBufferData(GL_ARRAY_BUFFER, vertexBuffer.capacity() * OpenGLUtils.BYTES_PER_FLOAT, vertexBuffer, GL_DYNAMIC_DRAW);
        }

        // bind index buffer
        if (indexBuffer != null) {
            glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, vbo.indexBufferID);
            glBufferData(GL_ELEMENT_ARRAY_BUFFER, indexBuffer.capacity() * OpenGLUtils.BYTES_PER_SHORT, indexBuffer, GL_STATIC_DRAW);
        }

        // bind normal buffer
        if (normalBuffer != null) {
            glBindBuffer(GL_ARRAY_BUFFER, vbo.normalBufferID);
            glBufferData(GL_ARRAY_BUFFER, normalBuffer.capacity() * OpenGLUtils.BYTES_PER_FLOAT, normalBuffer, GL_DYNAMIC_DRAW);
        }

        // bind uv buffer
        if (uvBuffer != null) {
            glBindBuffer(GL_ARRAY_BUFFER, vbo.uvBufferID);
            glBufferData(GL_ARRAY_BUFFER, uvBuffer.capacity() * OpenGLUtils.BYTES_PER_FLOAT, uvBuffer, GL_STATIC_DRAW);
        }

        // bind weight buffer
        if (weightBuffer != null) {
            glBindBuffer(GL_ARRAY_BUFFER, vbo.weightBufferID);
            glBufferData(GL_ARRAY_BUFFER, weightBuffer.capacity() * OpenGLUtils.BYTES_PER_FLOAT, weightBuffer, GL_STATIC_DRAW);
        }

        // bind influence buffer
        if (influenceBuffer != null) {
            glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, vbo.influenceBufferID);
            glBufferData(GL_ELEMENT_ARRAY_BUFFER, influenceBuffer.capacity() * OpenGLUtils.BYTES_PER_SHORT, influenceBuffer, GL_STATIC_DRAW);
        }

        // deselect buffers
        glBindBuffer(GL_ARRAY_BUFFER, 0);
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);

        Logger.i(this, vbo + " Vbo successfully allocated.");

        return vbo;
    }

    @Override
    public Vbo allocBuffers(FloatBuffer vertexBuffer, FloatBuffer normalBuffer, FloatBuffer uvBuffer, ShortBuffer indexBuffer) {
        glGenBuffers(6, vboIDBuffer, 0);

        Vbo vbo = new Vbo(vboIDBuffer, true);

        // bind vertex buffer
        if (vertexBuffer != null) {
            glBindBuffer(GL_ARRAY_BUFFER, vbo.vertexBufferID);
            glBufferData(GL_ARRAY_BUFFER, vertexBuffer.capacity() * OpenGLUtils.BYTES_PER_FLOAT, vertexBuffer, GL_STATIC_DRAW);
        }

        // bind index buffer
        if (indexBuffer != null) {
            glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, vbo.indexBufferID);
            glBufferData(GL_ELEMENT_ARRAY_BUFFER, indexBuffer.capacity() * OpenGLUtils.BYTES_PER_SHORT, indexBuffer, GL_STATIC_DRAW);
        }

        // bind normal buffer
        if (normalBuffer != null) {
            glBindBuffer(GL_ARRAY_BUFFER, vbo.normalBufferID);
            glBufferData(GL_ARRAY_BUFFER, normalBuffer.capacity() * OpenGLUtils.BYTES_PER_FLOAT, normalBuffer, GL_STATIC_DRAW);
        }

        // bind uv buffer
        if (uvBuffer != null) {
            glBindBuffer(GL_ARRAY_BUFFER, vbo.uvBufferID);
            glBufferData(GL_ARRAY_BUFFER, uvBuffer.capacity() * OpenGLUtils.BYTES_PER_FLOAT, uvBuffer, GL_STATIC_DRAW);
        }

        // deselect buffers
        glBindBuffer(GL_ARRAY_BUFFER, 0);
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);

        Logger.i(this, vbo + " Vbo successfully allocated.");

        return vbo;
    }

    @Override
    public void applyLight(Light light) {
        glUniform3fv(glGetUniformLocation(activeShaderProgram, "u_LightDirection"), 1, light.getDirection().asArray(), 0);
        glUniform3fv(glGetUniformLocation(activeShaderProgram, "u_LightPosition"), 1, light.getPosition().asArray(), 0);
    }

    @Override
    public int allocate(FloatBuffer floatBuffer) {

        // generate id
        final IntBuffer buffer = IntBuffer.allocate(1);
        glGenBuffers(1, buffer);
        int id = buffer.get(0);

        // bind float buffer to generated id
        glBindBuffer(GL11.GL_ARRAY_BUFFER, id);
        glBufferData(GL11.GL_ARRAY_BUFFER, floatBuffer.capacity() * OpenGLUtils.BYTES_PER_FLOAT, floatBuffer, GL11.GL_STATIC_DRAW);

        return id;
    }

    @Override
    public void update(Mesh mesh) {

        Logger.v(this, "starting to update vertex buffer");

        // bind vertex buffer
        if (mesh.vertices.getVertices().getBuffer() != null) {
            mesh.vertices.getVertices().getBuffer().flip();
            glBindBuffer(GL_ARRAY_BUFFER, mesh.vbo.vertexBufferID);
            glBufferData(GL_ARRAY_BUFFER, mesh.vertices.getVertices().getBuffer().capacity() * OpenGLUtils.BYTES_PER_FLOAT, mesh.vertices.getVertices().getBuffer(), GL_DYNAMIC_DRAW);
//            glBindBuffer(GL_ARRAY_BUFFER, mesh.vbo.normalBufferID);
//            glBufferData(GL_ARRAY_BUFFER, mesh.vertices.getNormals().getBuffer().capacity() * OpenGLUtils.BYTES_PER_FLOAT, mesh.vertices.getNormals().getBuffer(), GL_STATIC_DRAW);
        }

        // deselect buffers
        glBindBuffer(GL_ARRAY_BUFFER, 0);

        Logger.v(this, "update vertex buffer successful");
    }
}
