package net.gtamps.android.core.renderer.graph;

import net.gtamps.android.core.renderer.mesh.Material;
import net.gtamps.android.core.renderer.mesh.Mesh;
import net.gtamps.android.core.renderer.mesh.texture.Texture;
import net.gtamps.shared.IDirty;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.microedition.khronos.opengles.GL10;
import javax.microedition.khronos.opengles.GL11;


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
     * Defines the shader.
     */
    private IShader.Type shader = IShader.Type.SMOOTH;

    /**
     * Defines the drawing style.
     */
    private DrawingStyle drawingStyle = DrawingStyle.GL_TRIANGLES;

    /**
     * Defines the current color material.
     */
    protected Material material = Material.LIGHT_GRAY;

    /**
     * Defines the texture and it's texture matrix uv coordinates.
     */
    private Texture texture;

    /**
     * Defines if mip maps are available.
     */
    private boolean hasMipMap = false;

    // TODO clean this up
    protected int textureId = 0;
    protected int textureBufferId = 0;
    protected int textureBufferOffsetId = 0;

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

    public RenderableNode(@Nullable Mesh mesh) {
        this.mesh = mesh;
    }

    public RenderableNode() {
        this(null);
    }

    @Override
    final protected void processInternal(ProcessingState state) {

        GL11 gl = state.getGl11();

        // Model view aktivieren
        gl.glPushMatrix();

        gl.glMatrixMode(GL11.GL_MODELVIEW);

        // Objekt verschieben
        gl.glTranslatef(position.x, position.y, position.z);

        // Object Rotieren (Roll - Pitch - Yaw)
        gl.glRotatef(rotation.x, 1, 0, 0);
        gl.glRotatef(rotation.y, 0, 1, 0);
        gl.glRotatef(rotation.z, 0, 0, 1);

        // Object skalieren
        gl.glPushMatrix();
        gl.glScalef(dimension.x * scaling.x, dimension.y * scaling.y, dimension.z * scaling.z);

        // Ambiente Farbe setzen
        gl.glColor4f(material.getEmissive().r, material.getEmissive().g, material.getEmissive().b, material.getEmissive().a);

        render(gl);
        gl.glPopMatrix();
    }

    @Override
    final protected void afterProcess(@NotNull ProcessingState state) {
       state.getGl().glPopMatrix();
    }

    /**
     * Default rendering process.
     */
    final protected void render(GL11 gl) {
        if(isDirty()) {
            onDirty(gl);
        }

        // shading
        gl.glShadeModel(shader.getValue());

        // enable color materials
        if(colorMaterialEnabled) {
           gl.glEnable(GL10.GL_COLOR_MATERIAL);
        } else {
           gl.glDisable(GL10.GL_COLOR_MATERIAL);
        }

        // enable vertex colors
        if(vertexColorsEnabled) {
            gl.glBindBuffer(GL11.GL_ARRAY_BUFFER, mesh.getVbo().colorBufferId);
            gl.glColorPointer(4, GL10.GL_FLOAT,0, 0);
            gl.glEnableClientState(GL10.GL_COLOR_ARRAY);
        } else {
            gl.glDisableClientState(GL10.GL_COLOR_ARRAY);
        }

        // enable light
        if(lightingEnabled) {
            // enable vertex normals
            if(normalsEnabled) {
                gl.glBindBuffer(GL11.GL_ARRAY_BUFFER, mesh.getVbo().normalBufferId);
                gl.glNormalPointer(GL10.GL_FLOAT, 0, 0);
                gl.glEnableClientState(GL10.GL_NORMAL_ARRAY);
                gl.glEnable(GL11.GL_LIGHTING);
            } else {
                gl.glDisableClientState(GL10.GL_NORMAL_ARRAY);
            }
        } else {
            gl.glDisable(GL11.GL_LIGHTING);
        }

        // enable back-face culling
        if (doubleSidedEnabled) {
	        gl.glEnable(GL10.GL_CULL_FACE);
        } else {
            gl.glDisable(GL10.GL_CULL_FACE);
        }

        // enable alpha blending
        if(alphaEnabled) {
            gl.glEnable(GL10.GL_BLEND);
            gl.glEnable(GL10.GL_ALPHA_TEST);
        } else {
            gl.glDisable(GL10.GL_BLEND);
            gl.glDisable(GL10.GL_ALPHA_TEST);
        }

        // enable texture
        if(texturesEnabled) {
            gl.glEnable(GL10.GL_TEXTURE_2D);

            // TODO find way to use active texture instead of bind texture
		    // gl.glActiveTexture(texture.getTextureId());
            gl.glBindTexture(GL10.GL_TEXTURE_2D, texture.getTextureId());

            // enable mip maps
            if(hasMipMap) {
                gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_LINEAR_MIPMAP_NEAREST);
            } else {
                gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_NEAREST);
            }

            // texture offset for repeating textures
            if (texture.offsetU != 0 || texture.offsetV != 0) {
                gl.glMatrixMode(GL10.GL_TEXTURE);
                gl.glLoadIdentity();
                gl.glTranslatef(texture.offsetU, texture.offsetV, 0);
                gl.glMatrixMode(GL10.GL_MODELVIEW);
            }

            // draw uvs
            gl.glBindBuffer(GL11.GL_ARRAY_BUFFER, mesh.getVbo().textureCoordinateBufferId);
            gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, 0);
            gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
        } else {
            gl.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
            gl.glDisable(GL10.GL_TEXTURE_2D);
        }

        // bind vertices
        gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
        gl.glBindBuffer(GL11.GL_ARRAY_BUFFER, mesh.getVbo().vertexBufferId);
        gl.glVertexPointer(3, GL10.GL_FLOAT, 0, 0);

        // draw elements by index buffer
		gl.glBindBuffer(GL11.GL_ELEMENT_ARRAY_BUFFER, mesh.getVbo().indexBufferId);
		gl.glDrawElements(drawingStyle.ordinal(), mesh.getVbo().indexBuffer.capacity(), GL11.GL_UNSIGNED_SHORT, 0);

        // deselect buffers
        gl.glBindBuffer(GL11.GL_ARRAY_BUFFER, 0);
        gl.glBindBuffer(GL11.GL_ELEMENT_ARRAY_BUFFER, 0);

		// Vertex Array-State deaktivieren
		gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
		gl.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
		gl.glDisableClientState(GL10.GL_NORMAL_ARRAY);
		gl.glDisableClientState(GL10.GL_COLOR_ARRAY);

        // additional rendering calls
        renderInternal(gl);
    }

    /**
     * Internal render logic.
     */
    protected abstract void renderInternal(@NotNull GL10 gl);

    final public void onDirty(GL10 gl) {
        if(mesh == null) return;
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
     * Gets the current shader for this node.
     *
     * @return shader type
     */
    final public IShader.Type getShader() {
        return shader;
    }

    /**
     * Gets drawing style.
     *
     * @return drawing style
     */
    final public DrawingStyle getDrawingStyle() {
        return drawingStyle;
    }

    /**
     * Sets drawing style.
     *
     * @param drawingStyle
     */
    final public void setDrawingStyle(DrawingStyle drawingStyle) {
        this.drawingStyle = drawingStyle;
    }

    /**
     * Sets shader typ.
     *
     * @param shader
     */
    final public void setShader(IShader.Type shader) {
        this.shader = shader;
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
    public boolean hasMipMap(){
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
}