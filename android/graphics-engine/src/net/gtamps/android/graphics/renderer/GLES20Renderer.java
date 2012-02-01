package net.gtamps.android.graphics.renderer;

import android.graphics.Bitmap;
import android.opengl.GLUtils;
import fix.android.opengl.GLES20;
import net.gtamps.android.graphics.graph.scene.RenderableNode;
import net.gtamps.android.graphics.graph.scene.mesh.Material;
import net.gtamps.android.graphics.graph.scene.mesh.Mesh;
import net.gtamps.android.graphics.graph.scene.mesh.buffermanager.Vbo;
import net.gtamps.android.graphics.graph.scene.mesh.texture.TextureSample;
import net.gtamps.android.graphics.utils.OpenGLUtils;
import net.gtamps.shared.Config;
import net.gtamps.shared.Utils.Logger;
import net.gtamps.shared.Utils.math.Color4;
import net.gtamps.shared.Utils.math.Frustum;
import net.gtamps.shared.Utils.math.Matrix4;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import static android.opengl.GLES20.*;

/**
 * User: Jan Rabe, Tom Walroth, Til Börner
 * Date: 31/01/12
 * Time: 18:30
 */
public class GLES20Renderer extends BasicRenderer {

    public GLES20Renderer(IRenderAction renderAction) {
        super(renderAction);
    }

    @Override
    public void onDrawFrameHook(GL10 unusedGL) {
    }

    @Override
    protected void onSurfaceCreatedHook(GL10 gl10, EGLConfig eglConfig) {
        glEnable(GL_DEPTH_TEST);
        glClearDepthf(1.0f);
        glDepthFunc(GL_LEQUAL);
        glDepthMask(true);

        // cull backface
        glEnable(GL_CULL_FACE);
        glCullFace(GL_BACK);
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
        int program = Shader.Type.PHONG.shader.getProgram();

        if (frustum.hasDepthTest()) glEnable(GL_DEPTH_TEST);
        else glDisable(GL_DEPTH_TEST);

        Matrix4 viewMatrix = frustum.getViewMatrix();
        Matrix4.setLookAt(viewMatrix, frustum.getPosition(), frustum.getTarget(), frustum.getUp());

        // send to the shader
        glUniformMatrix4fv(glGetUniformLocation(program, "projectionMatrix"), 1, false, frustum.getProjectionMatrix().values, 0);
        Logger.checkGlError(this, "projectionMatrix");

        // send to the shader
        glUniformMatrix4fv(glGetUniformLocation(program, "normalMatrix"), 1, false, frustum.getNormalMatrix().values, 0);
        Logger.checkGlError(this, "normalMatrix");

        // eye view matrix
        glUniformMatrix4fv(glGetUniformLocation(program, "viewMatrix"), 1, false, viewMatrix.values, 0);
        Logger.checkGlError(this, "viewMatrix");
    }

    @Override
    public void draw(RenderableNode node) {
        if (Config.USEVBO) drawVbo(node);
        else drawArray(node);
    }

    private void drawArray(RenderableNode node) {
        Mesh mesh = node.getMesh();
        if (mesh == null) return;

        int program = node.getRenderState().getShader().getProgram();

        // send to the shader
        glUniformMatrix4fv(glGetUniformLocation(program, "modelMatrix"), 1, false, node.getCombinedTransformation().values, 0);
        Logger.checkGlError(this, "modelMatrix");

        // vertices
        glVertexAttribPointer(glGetAttribLocation(program, "vertexPosition"), 3, GL_FLOAT, false, 0, mesh.vertices.getVertices().getBuffer());
        glEnableVertexAttribArray(glGetAttribLocation(program, "vertexPosition"));
        Logger.checkGlError(this, "vertexPosition");

        // normals
        glVertexAttribPointer(glGetAttribLocation(program, "vertexNormal"), 3, GL_FLOAT, false, 0, mesh.vertices.getNormals().getBuffer());
        glEnableVertexAttribArray(glGetAttribLocation(program, "vertexNormal"));
        Logger.checkGlError(this, "vertexNormal");

        // colors
        glVertexAttribPointer(glGetAttribLocation(program, "vertexColor"), 4, GL_FLOAT, false, 0, mesh.vertices.getColors().getBuffer());
        glEnableVertexAttribArray(glGetAttribLocation(program, "vertexColor"));
        Logger.checkGlError(this, "vertexColor");

        // material
        Material material = node.getMaterial();
        glUniform4fv(glGetUniformLocation(program, "material.emission"), 1, material.getEmission().asArray(), 0);
        Logger.checkGlError(this, "material.emission");
        glUniform4fv(glGetUniformLocation(program, "material.ambient"), 1, material.getAmbient().asArray(), 0);
        Logger.checkGlError(this, "material.ambient");
        glUniform4fv(glGetUniformLocation(program, "material.diffuse"), 1, material.getDiffuse().asArray(), 0);
        Logger.checkGlError(this, "material.diffuse");
        glUniform4fv(glGetUniformLocation(program, "material.specular"), 1, material.getSpecular().asArray(), 0);
        Logger.checkGlError(this, "material.specular");
        glUniform1f(glGetUniformLocation(program, "material.shininess"), material.getShininess());
        Logger.checkGlError(this, "material.shininess");

        // multiple textures
        if (!node.getTextureSamples().isEmpty()) {
            glUniform1i(glGetUniformLocation(program, "hasTextures"), 2);
            Logger.checkGlError(this, "hasTextures");
            if (node.getTextureSamples().size() > 8)
                Logger.V(this, node.getClass().getSimpleName() + " has exceeded it's max texture limit : " + node.getTextureSamples().size() + "/" + RenderCapabilities.maxTextureUnits());
            for (int i = 0; i < node.getTextureSamples().size() || i < 8; i++) {
                TextureSample textureSample = node.getTextureSamples().get(i);
                glActiveTexture(GL_TEXTURE0 + i);
                glBindTexture(GL_TEXTURE_2D, textureSample.textureId);
                glUniform1i(glGetUniformLocation(program, textureSample.type.name()), i);
                glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, textureSample.hasMipMap ? GL_LINEAR_MIPMAP_LINEAR : GL_NEAREST);
            }

            // uvs
            glVertexAttribPointer(glGetAttribLocation(program, "vertexUv"), 2, GL_FLOAT, false, 0, mesh.vertices.getUvs().getBuffer());
            glEnableVertexAttribArray(glGetAttribLocation(program, "vertexUv"));
            Logger.checkGlError(this, "vertexUv");
        } else {
            glUniform1i(glGetUniformLocation(program, "hasTextures"), 0);
            Logger.checkGlError(this, "hasTextures");
        }

        // uses lightning
        glUniform1i(glGetUniformLocation(program, "hasLighting"), node.getRenderState().hasLighting() ? 1 : 0);
        Logger.checkGlError(this, "hasLighting");

        // Draw with indices
        glDrawElements(GL_TRIANGLES, mesh.faces.getBuffer().capacity(), GL_UNSIGNED_SHORT, mesh.faces.getBuffer());
        Logger.checkGlError(this, "glDrawElements");

        // unbind to avoid accidental manipulation
        glBindTexture(GL_TEXTURE_2D, 0);
    }

    private void drawVbo(RenderableNode node) {
        Mesh mesh = node.getMesh();
        if (mesh == null) return;

        int program = node.getRenderState().getShader().getProgram();

        // send to the shader
        glUniformMatrix4fv(glGetUniformLocation(program, "modelMatrix"), 1, false, node.getCombinedTransformation().values, 0);
        Logger.checkGlError(this, "modelMatrix");

        // vertices
        glBindBuffer(GL_ARRAY_BUFFER, mesh.vbo.vertexBufferID);
        GLES20.glVertexAttribPointer(glGetAttribLocation(program, "vertexPosition"), 3, GL_FLOAT, false, 0, 0);
        glEnableVertexAttribArray(glGetAttribLocation(program, "vertexPosition"));
        Logger.checkGlError(this, "vertexPosition");

        // normals
        glBindBuffer(GL_ARRAY_BUFFER, mesh.vbo.normalBufferID);
        GLES20.glVertexAttribPointer(glGetAttribLocation(program, "vertexNormal"), 3, GL_FLOAT, false, 0, 0);
        glEnableVertexAttribArray(glGetAttribLocation(program, "vertexNormal"));
        Logger.checkGlError(this, "vertexNormal");

        // colors
        glBindBuffer(GL_ARRAY_BUFFER, mesh.vbo.colorBufferID);
        GLES20.glVertexAttribPointer(glGetAttribLocation(program, "vertexColor"), 4, GL_FLOAT, false, 0, 0);
        glEnableVertexAttribArray(glGetAttribLocation(program, "vertexColor"));
        Logger.checkGlError(this, "vertexColor");

        // material
        Material material = node.getMaterial();
        glUniform4fv(glGetUniformLocation(program, "material.emission"), 1, material.getEmission().asArray(), 0);
        Logger.checkGlError(this, "material.emission");
        glUniform4fv(glGetUniformLocation(program, "material.ambient"), 1, material.getAmbient().asArray(), 0);
        Logger.checkGlError(this, "material.ambient");
        glUniform4fv(glGetUniformLocation(program, "material.diffuse"), 1, material.getDiffuse().asArray(), 0);
        Logger.checkGlError(this, "material.diffuse");
        glUniform4fv(glGetUniformLocation(program, "material.specular"), 1, material.getSpecular().asArray(), 0);
        Logger.checkGlError(this, "material.specular");
        glUniform1f(glGetUniformLocation(program, "material.shininess"), material.getShininess());
        Logger.checkGlError(this, "material.shininess");

        // multiple textures
        if (!node.getTextureSamples().isEmpty()) {
            glUniform1i(glGetUniformLocation(program, "hasTextures"), 2);
            Logger.checkGlError(this, "hasTextures");
            if (node.getTextureSamples().size() > 8)
                Logger.V(this, node.getClass().getSimpleName() + " has exceeded it's max texture limit : " + node.getTextureSamples().size() + "/" + RenderCapabilities.maxTextureUnits());
            for (int i = 0; i < node.getTextureSamples().size(); i++) {
                TextureSample textureSample = node.getTextureSamples().get(i);
                glActiveTexture(GL_TEXTURE0 + i);
                glBindTexture(GL_TEXTURE_2D, textureSample.textureId);
                glUniform1i(glGetUniformLocation(program, textureSample.type.name()), i);
                glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, textureSample.hasMipMap ? GL_LINEAR_MIPMAP_LINEAR : GL_NEAREST);
            }

            // uvs
            glBindBuffer(GL_ARRAY_BUFFER, mesh.vbo.uvBufferID);
            GLES20.glVertexAttribPointer(glGetAttribLocation(program, "vertexUv"), 2, GL_FLOAT, false, 0, 0);
            glEnableVertexAttribArray(glGetAttribLocation(program, "vertexUv"));
            Logger.checkGlError(this, "vertexUv");
        } else {
            glUniform1i(glGetUniformLocation(program, "hasTextures"), 0);
            Logger.checkGlError(this, "hasTextures");
        }

        // uses lightning
        glUniform1i(glGetUniformLocation(program, "hasLighting"), node.getRenderState().hasLighting() ? 1 : 0);
        Logger.checkGlError(this, "hasLighting");

        // Draw with indices
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, mesh.vbo.indexBufferID);
        GLES20.glDrawElements(GL_TRIANGLES, mesh.faces.getBuffer().capacity(), GL_UNSIGNED_SHORT, 0);
        Logger.checkGlError(this, "glDrawElements");

        // unbind to avoid accidental manipulation
        glBindTexture(GL_TEXTURE_2D, 0);
    }

    @Override
    public Vbo allocBuffers(FloatBuffer vertexBuffer, FloatBuffer normalBuffer, FloatBuffer colorBuffer, FloatBuffer uvBuffer, ShortBuffer indexBuffer) {

        // position zero
        if (vertexBuffer != null) vertexBuffer.position(0);
        if (indexBuffer != null) indexBuffer.position(0);
        if (normalBuffer != null) normalBuffer.position(0);
        if (colorBuffer != null) colorBuffer.position(0);
        if (uvBuffer != null) uvBuffer.position(0);

        // get buffer ids
        final int[] buffer = new int[5];
        glGenBuffers(5, buffer, 0);
        Vbo vbo = new Vbo(buffer, true);

        // bind vertex buffer
        if (vertexBuffer != null) {
            glBindBuffer(GL_ARRAY_BUFFER, vbo.vertexBufferID);
            glBufferData(GL_ARRAY_BUFFER, vertexBuffer.capacity() * OpenGLUtils.BYTES_PER_FLOAT, vertexBuffer, GL_DYNAMIC_DRAW);
        }

        // bind index buffer
        if (indexBuffer != null) {
            glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, vbo.indexBufferID);
            glBufferData(GL_ELEMENT_ARRAY_BUFFER, indexBuffer.capacity() * OpenGLUtils.BYTES_PER_SHORT, indexBuffer, GL_DYNAMIC_DRAW);
        }

        // bind normal buffer
        if (normalBuffer != null) {
            glBindBuffer(GL_ARRAY_BUFFER, vbo.normalBufferID);
            glBufferData(GL_ARRAY_BUFFER, normalBuffer.capacity() * OpenGLUtils.BYTES_PER_FLOAT, normalBuffer, GL_DYNAMIC_DRAW);
        }

        // bind color buffer
        if (colorBuffer != null) {
            glBindBuffer(GL_ARRAY_BUFFER, vbo.colorBufferID);
            glBufferData(GL_ARRAY_BUFFER, colorBuffer.capacity() * OpenGLUtils.BYTES_PER_FLOAT, colorBuffer, GL_DYNAMIC_DRAW);
        }

        // bind uv buffer
        if (uvBuffer != null) {
            glBindBuffer(GL_ARRAY_BUFFER, vbo.uvBufferID);
            glBufferData(GL_ARRAY_BUFFER, uvBuffer.capacity() * OpenGLUtils.BYTES_PER_FLOAT, uvBuffer, GL_DYNAMIC_DRAW);
        }

        // deselect buffers
        glBindBuffer(GL_ARRAY_BUFFER, 0);
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
        
        Logger.i(this, vbo + " Vbo successfully allocated.");

        return vbo;
    }
}
