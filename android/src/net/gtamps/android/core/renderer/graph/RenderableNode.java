package net.gtamps.android.core.renderer.graph;

import android.opengl.GLES20;
import net.gtamps.android.core.renderer.RenderCapabilities;
import net.gtamps.android.core.renderer.mesh.Material;
import net.gtamps.android.core.renderer.mesh.Mesh;
import net.gtamps.android.core.renderer.shader.Shader;
import net.gtamps.shared.Utils.IDirty;
import net.gtamps.shared.Utils.Logger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.microedition.khronos.opengles.GL10;
import javax.microedition.khronos.opengles.GL11;

import static fix.android.opengl.GLES20.glDrawElements;
import static fix.android.opengl.GLES20.glVertexAttribPointer;
import static javax.microedition.khronos.opengles.GL10.*;
import static javax.microedition.khronos.opengles.GL11.GL_ARRAY_BUFFER;
import static javax.microedition.khronos.opengles.GL11.GL_ELEMENT_ARRAY_BUFFER;


public abstract class RenderableNode extends SceneNode implements IDirty {

    /**
     * Defines the mesh of the node.
     */
    @Nullable
    protected Mesh mesh;

    private boolean isDirty = true;

    /**
     * Defines if the node has vertex colors
     */
    private boolean vertexColorsEnabled = false;

    /**
     * Defines if the node needs cull-facing.
     */
    private boolean doubleSidedEnabled = false;

    /**
     * Defines if the node has textures.
     */
    private boolean texturesEnabled = false;

    /**
     * Defines if the node has normals.
     */
    private boolean normalsEnabled = false;

    /**
     * Defines if the node has color materials.
     */
    private boolean colorMaterialEnabled = false;

    /**
     * Defines if the node has lighting.
     */
    private boolean lightingEnabled = false;

    /**
     * Defines if alpha blending is enabled.
     */
    private boolean alphaEnabled = false;

    /**
     * Render state. Defines blending, shader and drawingstyle state.
     */
    private RenderState renderState = new RenderState();

    /**
     * Defines the current color material.
     */
    protected Material material = Material.LIGHT_GRAY;

    /**
     * Defines if mip maps are available.
     */
    private boolean hasMipMap = false;

    // TODO clean this up
    protected int textureId = 0;
    protected int textureBufferId = 0;
    protected int textureBufferOffsetId = 0;
    private boolean useSharedTextureCoordBuffer = false;
    private Shader.Type shaderTyp = Shader.Type.PHONG;

    public Shader.Type getShader() {
        return shaderTyp;
    }
    
    public void setShader(Shader.Type shaderTyp) {
        this.shaderTyp = shaderTyp;
    }

    public void setTextureId(int textureId) {
        this.textureId = textureId;
    }

    public void setTextureBufferId(int textureBufferId) {
        this.textureBufferId = textureBufferId;
    }

    public void setTextureBufferOffsetId(int textureBufferOffsetId) {
        this.textureBufferOffsetId = textureBufferOffsetId;
    }

    /**
     * Defines the point size, if drawn as GL_POINTS.
     */
    private float pointSize = 3f;

    /**
     * Defines if points are to be drawn smooth.
     */
    private boolean pointSmoothing = true;

    /**
     * Defines the line width if drawn as GL_LINES.
     */
    private float lineWidth = 1f;

    /**
     * Defines if lines are to be drawn smooth.
     */
    private boolean lineSmoothing = true;

    public RenderableNode(@NotNull RenderableNode other) {
        this.mesh = new Mesh(other.getMesh());
        this.dimension.set(other.dimension);
        this.scaling.set(other.scaling);
    }

    public RenderableNode() {
    }

    @Override
    protected void processInternal(ProcessingState state) {

        GL11 gl = state.getGl11();

        // Model view aktivieren
        gl.glPushMatrix();

        gl.glMatrixMode(GL_MODELVIEW);

        // Objekt verschieben
        gl.glTranslatef(position.x, position.y, position.z);

        // Object Rotieren (Roll - Pitch - Yaw)
        gl.glRotatef(rotation.x, 1, 0, 0);
        gl.glRotatef(rotation.y, 0, 1, 0);
        gl.glRotatef(rotation.z, 0, 0, 1);

        // Object skalieren
        gl.glPushMatrix();
        gl.glScalef(dimension.x * scaling.x, dimension.y * scaling.y, dimension.z * scaling.z);
        render(gl);
        gl.glPopMatrix();
        renderInternal(gl);
    }

    @Override
    protected void afterProcess(@NotNull ProcessingState state) {
        state.getGl().glPopMatrix();
    }

    @Deprecated
    private void shadeInternalWithOutVBO(@NotNull ProcessingState state) {
        if(!RenderCapabilities.supportsGLES20()) return;
        if (mesh == null) return;
        if (isDirty) onDirty(state.getGl());

        int program = shaderTyp.shader.getProgram();

        // vertices
        GLES20.glVertexAttribPointer(GLES20.glGetAttribLocation(program, "aPosition"),3, GLES20.GL_FLOAT, false, 0, mesh.getVbo().vertexBuffer);
        GLES20.glEnableVertexAttribArray(GLES20.glGetAttribLocation(program, "aPosition"));
        Logger.checkGlError(this, "aPosition");

        // the normal info
        GLES20.glVertexAttribPointer(GLES20.glGetAttribLocation(program, "aNormal"), 3, GLES20.GL_FLOAT, false,0,  mesh.getVbo().normalBuffer);
        GLES20.glEnableVertexAttribArray(GLES20.glGetAttribLocation(program, "aNormal"));
        Logger.checkGlError(this,"aNormal");

        // colors
//        GLES20.glVertexAttribPointer(GLES20.glGetAttribLocation(program, "aColor"), 4, GLES20.GL_FLOAT, false, 0, mesh.getVbo().colorBuffer);
//        GLES20.glEnableVertexAttribArray(GLES20.glGetAttribLocation(program, "aColor"));
//        OpenGLUtils.checkGlError("aColor");

        // enable texturing
        GLES20.glUniform1f(GLES20.glGetUniformLocation(program, "hasTexture"), 0f);
        Logger.checkGlError(this,"hasTexture");

        // uvs
        GLES20.glVertexAttribPointer(GLES20.glGetAttribLocation(program, "aTextCoord"), 2, GLES20.GL_FLOAT, false, 0, mesh.getVbo().textureCoordinateBuffer);
        GLES20.glEnableVertexAttribArray(GLES20.glGetAttribLocation(program, "aTextCoord"));
        Logger.checkGlError(this,"aTextCoord");

        // Draw with indices
        GLES20.glDrawElements(GLES20.GL_TRIANGLES, mesh.getVbo().indexBuffer.capacity(), GLES20.GL_UNSIGNED_SHORT, mesh.getVbo().indexBuffer);
        Logger.checkGlError(this,"glDrawElements");
    }

    private void shadeInternalWithVBO(@NotNull ProcessingState state) {
        if(!RenderCapabilities.supportsGLES20()) return;
        if (mesh == null) return;
        if (isDirty) onDirty(state.getGl());

        int program = shaderTyp.shader.getProgram();

        // send to the shader
        GLES20.glUniformMatrix4fv(GLES20.glGetUniformLocation(program, "modelViewMatrix"), 1, false, getCombinedTransformation().values, 0);
        Logger.checkGlError(this,"modelViewMatrix");

        // vertices
        GLES20.glBindBuffer(GL_ARRAY_BUFFER, mesh.getVbo().vertexBufferId);
        glVertexAttribPointer(GLES20.glGetAttribLocation(program, "vertexPosition"),3, GLES20.GL_FLOAT, false, 0, 0);
        GLES20.glEnableVertexAttribArray(GLES20.glGetAttribLocation(program, "vertexPosition"));
        Logger.checkGlError(this, "vertexPosition");

        // normals
        GLES20.glBindBuffer(GL_ARRAY_BUFFER, mesh.getVbo().normalBufferId);
        glVertexAttribPointer(GLES20.glGetAttribLocation(program, "vertexNormal"),3, GLES20.GL_FLOAT, false, 0, 0);
        GLES20.glEnableVertexAttribArray(GLES20.glGetAttribLocation(program, "vertexNormal"));
        Logger.checkGlError(this, "vertexNormal");

        // colors
//        GLES20.glBindBuffer(GL_ARRAY_BUFFER, mesh.getVbo().colorBufferId);
//        glVertexAttribPointer(GLES20.glGetAttribLocation(program, "aColor"),4, GLES20.GL_FLOAT, false, 0, 0);
//        GLES20.glEnableVertexAttribArray(GLES20.glGetAttribLocation(program, "aColor"));
//        OpenGLUtils.checkGlError("aColor");

        // material
        GLES20.glUniform4fv(GLES20.glGetUniformLocation(program, "material.emission"), 1, material.getEmission().asArray(), 0);
        Logger.checkGlError(this, "material.emission");
        GLES20.glUniform4fv(GLES20.glGetUniformLocation(program, "material.ambient"), 1, material.getAmbient().asArray(), 0);
        Logger.checkGlError(this, "material.ambient");
        GLES20.glUniform4fv(GLES20.glGetUniformLocation(program, "material.diffuse"), 1, material.getDiffuse().asArray(), 0);
        Logger.checkGlError(this, "material.diffuse");
        GLES20.glUniform4fv(GLES20.glGetUniformLocation(program, "material.specular"), 1, material.getSpecular().asArray(), 0);
        Logger.checkGlError(this, "material.specular");
        GLES20.glUniform1f(GLES20.glGetUniformLocation(program, "material.shininess"), material.getPhongExponent());
        Logger.checkGlError(this, "material.shininess");

        // enable texture
        if (texturesEnabled) {
            GLES20.glEnable(GL_TEXTURE_2D);
            GLES20.glBindTexture(GL_TEXTURE_2D, textureId);
        }

        // enable texturing
        GLES20.glUniform1f(GLES20.glGetUniformLocation(program, "hasTexture"), texturesEnabled ? 2f : 0f);
        Logger.checkGlError(this,"hasTexture");

        // uvs
        GLES20.glBindBuffer(GL_ARRAY_BUFFER, mesh.getVbo().textureCoordinateBufferId);
        glVertexAttribPointer(GLES20.glGetAttribLocation(program, "vertexUv"),2, GLES20.GL_FLOAT, false, 0, 0);
        GLES20.glEnableVertexAttribArray(GLES20.glGetAttribLocation(program, "vertexUv"));
        Logger.checkGlError(this, "vertexUv");

        // Draw with indices
        GLES20.glBindBuffer(GLES20.GL_ELEMENT_ARRAY_BUFFER, mesh.getVbo().indexBufferId);
        glDrawElements(GLES20.GL_TRIANGLES, mesh.getVbo().indexBuffer.capacity(), GLES20.GL_UNSIGNED_SHORT, 0);
        Logger.checkGlError(this,"glDrawElements");
    }

    @Override
    public void shadeInternal(@NotNull ProcessingState state) {
//        shadeInternalWithOutVBO(state);
        shadeInternalWithVBO(state);
    }

    /**
     * Default rendering process.
     */
    public void render(GL11 gl) {
        if (mesh == null) return;
        if (isDirty) onDirty(gl);

        // shading
//        gl.glShadeModel(renderState.shader.getValue());

        // enable color materials
        if (colorMaterialEnabled) {
            gl.glEnable(GL_COLOR_MATERIAL);
        } else {
            gl.glDisable(GL_COLOR_MATERIAL);
        }

        // enable vertex colors
        if (vertexColorsEnabled) {
            gl.glBindBuffer(GL_ARRAY_BUFFER, mesh.getVbo().colorBufferId);
            gl.glColorPointer(4, GL_FLOAT, 0, 0);
            gl.glEnableClientState(GL_COLOR_ARRAY);
        } else {
            gl.glColor4f(material.getEmission().r, material.getEmission().g, material.getEmission().b, material.getEmission().a);
            gl.glDisableClientState(GL_COLOR_ARRAY);
        }

        // enable light
        if (lightingEnabled) {
            // enable vertex normals
            if (normalsEnabled) {
                gl.glBindBuffer(GL_ARRAY_BUFFER, mesh.getVbo().normalBufferId);
                gl.glNormalPointer(GL_FLOAT, 0, 0);
                gl.glEnableClientState(GL_NORMAL_ARRAY);
                gl.glEnable(GL_LIGHTING);
            } else {
                gl.glDisableClientState(GL_NORMAL_ARRAY);
            }
        } else {
            gl.glDisable(GL_LIGHTING);
        }

        // enable back-face culling
        if (doubleSidedEnabled) {
            gl.glDisable(GL_CULL_FACE);
        } else {
            gl.glEnable(GL_CULL_FACE);
        }

        // enable alpha blending
        if (alphaEnabled) {
            gl.glEnable(GL_BLEND);
            gl.glEnable(GL_ALPHA_TEST);
            gl.glBlendFunc(renderState.blendState.sfactor, renderState.blendState.dfactor);
        } else {
            gl.glDisable(GL_BLEND);
            gl.glDisable(GL_ALPHA_TEST);
        }

        // enable texture
        if (texturesEnabled) {
            gl.glEnable(GL_TEXTURE_2D);

            // TODO find way to use active texture instead of bind texture
            // gl.glActiveTexture(texture.getTextureId());
//            Texture texture = textureManager.get(0);
//            int glId = Registry.getTextureLibrary().getTextureResourceIds().get(texture.getTextureId());
            gl.glBindTexture(GL_TEXTURE_2D, textureId);

            // enable mip maps
            if (hasMipMap) {
                gl.glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR_MIPMAP_NEAREST);
            } else {
                gl.glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
            }
//
//            // texture offset for repeating textures
//            if (textureManager.get(0).offsetU != 0 || textureManager.get(0).offsetV != 0) {
//                gl.glMatrixMode(GL_TEXTURE);
//                gl.glLoadIdentity();
//                gl.glTranslatef(textureManager.get(0).offsetU, textureManager.get(0).offsetV, 0);
//                gl.glMatrixMode(GL_MODELVIEW);
//            }

            // draw uvs
            gl.glBindBuffer(GL_ARRAY_BUFFER, useSharedTextureCoordBuffer ? textureBufferId : mesh.getVbo().textureCoordinateBufferId);
            gl.glTexCoordPointer(2, GL_FLOAT, 0, textureBufferOffsetId);
            gl.glEnableClientState(GL_TEXTURE_COORD_ARRAY);
        } else {
            gl.glDisableClientState(GL_TEXTURE_COORD_ARRAY);
            gl.glDisable(GL_TEXTURE_2D);
        }

        // drawing point setings
        if (renderState.drawingStyle.equals(RenderState.DrawingStyle.GL_POINTS)) {
            if (pointSmoothing) {
                gl.glEnable(GL_POINT_SMOOTH);
            } else {
                gl.glDisable(GL_POINT_SMOOTH);
            }
            gl.glPointSize(pointSize);
        }

        // drawing line settings
        if (renderState.drawingStyle.equals(RenderState.DrawingStyle.GL_LINES) || renderState.drawingStyle.equals(RenderState.DrawingStyle.GL_LINE_LOOP) || renderState.drawingStyle.equals(RenderState.DrawingStyle.GL_LINE_STRIP)) {
            if (lineSmoothing) {
                gl.glEnable(GL_LINE_SMOOTH);
            } else {
                gl.glDisable(GL_LINE_SMOOTH);
            }
            gl.glLineWidth(lineWidth);
        }

        // bind vertices
        gl.glBindBuffer(GL_ARRAY_BUFFER, mesh.getVbo().vertexBufferId);
        gl.glEnableClientState(GL_VERTEX_ARRAY);
        gl.glVertexPointer(3, GL_FLOAT, 0, 0);

        // actually draw the mesh by index buffer
        gl.glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, mesh.getVbo().indexBufferId);
        gl.glDrawElements(renderState.drawingStyle.getValue(), mesh.getVbo().indexBuffer.capacity(), GL_UNSIGNED_SHORT, 0);

        // deselect buffers
        gl.glBindBuffer(GL_ARRAY_BUFFER, 0);
        gl.glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);

        // Vertex Array-State deaktivieren
        gl.glDisableClientState(GL_VERTEX_ARRAY);
        gl.glDisableClientState(GL_TEXTURE_COORD_ARRAY);
        gl.glDisableClientState(GL_NORMAL_ARRAY);
        gl.glDisableClientState(GL_COLOR_ARRAY);
    }

    /**
     * Internal render logic.
     */
    protected abstract void renderInternal(@NotNull GL10 gl);

    final public void onDirty(GL10 gl) {
        mesh.setup(gl);
        clearDirtyFlag();
    }

    /**
     * Gets mesh.
     *
     * @return mesh
     */
    final public Mesh getMesh() {
        return mesh;
    }

    /**
     * Sets new mesh.
     *
     * @param mesh
     */
    final public void setMesh(@NotNull Mesh mesh) {
        this.mesh = mesh;
    }

    /**
     * Returns if node has vertex colors.
     *
     * @return <code>true</code> if it has vertex colors
     */
    final public boolean isVertexColorsEnabled() {
        return vertexColorsEnabled;
    }

    /**
     * Enables vertex colors.
     *
     * @param vertexColorsEnabled
     */
    final public void enableVertexColors(boolean vertexColorsEnabled) {
        this.vertexColorsEnabled = vertexColorsEnabled;
    }

    /**
     * Returns if cull-facing is enabled.
     *
     * @return <code>true</code> if it is cull-faced.
     */
    final public boolean isDoubleSidedEnabled() {
        return doubleSidedEnabled;
    }

    final public void enableDoubleSided(boolean doubleSidedEnabled) {
        this.doubleSidedEnabled = doubleSidedEnabled;
    }

    /**
     * Returns if node has uvs.
     *
     * @return <code>true</code> if it has uvs.
     */
    final public boolean isTexturesEnabled() {
        return texturesEnabled;
    }

    /**
     * Enable uvs.
     *
     * @param texturesEnabled
     */
    final public void enableTextures(boolean texturesEnabled) {
        this.texturesEnabled = texturesEnabled;
    }

    /**
     * Returns if node has normals.
     *
     * @return <code>true</code> if it has normals.
     */
    final public boolean isNormalsEnabled() {
        return normalsEnabled;
    }

    /**
     * Enables normals.
     *
     * @param normalsEnabled
     */
    final public void enableNormals(boolean normalsEnabled) {
        this.normalsEnabled = normalsEnabled;
    }

    /**
     * Returns if node has color materials.
     *
     * @return <code>true</code> if it has color materials.
     */
    final public boolean isColorMaterialEnabled() {
        return colorMaterialEnabled;
    }

    /**
     * Enable color materials.
     *
     * @param colorMaterialEnabled
     */
    final public void enableColorMaterialEnabled(boolean colorMaterialEnabled) {
        this.colorMaterialEnabled = colorMaterialEnabled;
    }

    /**
     * Returns if lighting is enabled for this node.
     *
     * @return <code>true</code> if it has lighting
     */
    final public boolean isLightingEnabled() {
        return lightingEnabled;
    }

    /**
     * Enables lighting.
     *
     * @param lightingEnabled
     */
    final public void enableLighting(boolean lightingEnabled) {
        this.lightingEnabled = lightingEnabled;
    }

    /**
     * Gets point size.
     *
     * @return point size
     */
    final public float getPointSize() {
        return pointSize;
    }

    /**
     * Sets points size.
     *
     * @param pointSize
     */
    final public void setPointSize(float pointSize) {
        this.pointSize = pointSize;
    }

    /**
     * Returns if points are drawn smooth.
     *
     * @return <code>true</code> if smooth
     */
    final public boolean isPointSmoothing() {
        return pointSmoothing;
    }

    /**
     * Enables smooth point shading.
     *
     * @param pointSmoothing <code>true</code> if smooth
     */
    final public void setPointSmoothing(boolean pointSmoothing) {
        this.pointSmoothing = pointSmoothing;
    }


    /**
     * Gets current line width.
     *
     * @return line width
     */
    final public float getLineWidth() {
        return lineWidth;
    }

    /**
     * Sets new line width.
     *
     * @param lineWidth
     */
    final public void setLineWidth(float lineWidth) {
        this.lineWidth = lineWidth;
    }

    /**
     * Returns if lines are drawn smoothly.
     *
     * @return <code>true</code> if smooth
     */
    final public boolean isLineSmoothing() {
        return lineSmoothing;
    }

    /**
     * Enables using mip map.
     *
     * @param enableMipMap
     */
    public void enableMipMap(boolean enableMipMap) {
        hasMipMap = enableMipMap;
    }

    /**
     * Returns if mip maps are enabled.
     *
     * @return <code>true</code> if enabled
     */
    public boolean hasMipMap() {
        return hasMipMap;
    }

    /**
     * Enables smooth line drawing.
     *
     * @param lineSmoothing <code>true</code> if smooth
     */
    final public void setLineSmoothing(boolean lineSmoothing) {
        this.lineSmoothing = lineSmoothing;
    }

    /**
     * Returns if alpha is enabled.
     *
     * @return <code>true</code> if is enabled
     */
    final public boolean hasAlphaEnabled() {
        return alphaEnabled;
    }

    /**
     * Enables alpha blending.
     *
     * @param alphaEnabled
     */
    final public void enableAlpha(boolean alphaEnabled) {
        this.alphaEnabled = alphaEnabled;
    }

    @Override
    final public boolean isDirty() {
        return isDirty;
    }

    @Override
    final public void setDirtyFlag() {
        isDirty = true;
    }

    @Override
    final public void clearDirtyFlag() {
        isDirty = false;
    }

    public Material getMaterial() {
        return material;
    }

    public void setMaterial(Material material) {
        this.material = material;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RenderableNode)) return false;

        RenderableNode that = (RenderableNode) o;

        if (textureBufferId != that.textureBufferId) return false;
        if (textureBufferOffsetId != that.textureBufferOffsetId) return false;
        if (textureId != that.textureId) return false;
        if (!mesh.equals(that.mesh)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = mesh.hashCode();
        result = 31 * result + textureId;
        result = 31 * result + textureBufferId;
        result = 31 * result + textureBufferOffsetId;
        return result;
    }

    public void useSharedTextureCoordBuffer(boolean isUsing) {
        useSharedTextureCoordBuffer = isUsing;
    }

    public RenderState getRenderState() {
        return renderState;
    }

    public void setRenderState(RenderState renderState) {
        this.renderState = renderState;
    }

    public abstract RenderableNode getStatic();

    @Override
    protected void onResumeInternal(ProcessingState state) {
        if(mesh == null) return;
        mesh.invalidate();
    }
}