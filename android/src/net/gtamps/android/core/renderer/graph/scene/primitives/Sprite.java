package net.gtamps.android.core.renderer.graph.scene.primitives;

import net.gtamps.android.core.renderer.Registry;
import net.gtamps.android.core.renderer.graph.ProcessingState;
import net.gtamps.android.core.renderer.graph.RenderableNode;
import net.gtamps.android.core.renderer.mesh.Mesh;
import net.gtamps.android.core.renderer.mesh.texture.BufferedTexture;
import net.gtamps.android.core.renderer.mesh.texture.SpriteTetxure;
import net.gtamps.shared.Utils.math.Color4;
import net.gtamps.shared.game.state.State;
import org.jetbrains.annotations.NotNull;

import javax.microedition.khronos.opengles.GL10;

public class Sprite extends RenderableNode {

    protected BufferedTexture bufferedTexture;

    public Sprite() {
        bufferedTexture = null;
    }

    public Sprite(Sprite other) {
        this.mesh = new Mesh(other.mesh);
        this.dimension.set(other.dimension);
        this.scaling.set(other.scaling);
    }

    /**
     * Constructs a new Sprite.
     *
     * @param width
     * @param height
     * @param textureId
     * @param textureCoordBufferId
     * @param textureCoordBufferOffsetId
     */
    public Sprite(float width, float height, int textureId, int textureCoordBufferId, int textureCoordBufferOffsetId, boolean useMipMap) {
        dimension.set(width, height, 0);
        setTextureId(textureId);
        setTextureBufferId(textureCoordBufferId);
        setTextureBufferOffsetId(textureCoordBufferOffsetId);
        scaling.set(1, 1, 0);
        enableMipMap(useMipMap);
    }

    public Sprite(int textureId, int textureCoordBufferid, @NotNull SpriteTetxure spriteTetxure, boolean useMipMap) {
        this(spriteTetxure.width, spriteTetxure.height, textureId, textureCoordBufferid, spriteTetxure.offsetId, useMipMap);
    }

    public void setBufferedTexture(BufferedTexture bufferedTexture) {
        this.bufferedTexture = bufferedTexture;
        setTextureId(bufferedTexture.textureId);
        setTextureBufferId(bufferedTexture.floatBufferId);
        SpriteTetxure spriteTetxure = bufferedTexture.getAnimation(State.Type.IDLE)[0];
        setTextureBufferOffsetId(spriteTetxure.offsetId);
        dimension.set(spriteTetxure.width, spriteTetxure.height, 0);
        scaling.set(1, 1, 0);
        useSharedTextureCoordBuffer(true);
    }

    public void loadBufferedTexture(int textureResourceId, int textureCoordinateResourceId, boolean generateMipMap) {
        setBufferedTexture(Registry.getTextureLibrary().loadBufferedTexture(textureResourceId, textureCoordinateResourceId, generateMipMap));
        enableMipMap(generateMipMap);
    }

    @Override
    protected void setupInternal(@NotNull ProcessingState state) {
        if (mesh != null) {
            return;
        }

        // new mesh
        this.mesh = new Mesh(4, 2);

        Color4 emissive = material.getEmissive();

        // oben rechts
        mesh.addVertex(0.5f, 0.5f, 0, 0, 0, 1, emissive.r, emissive.g, emissive.b, emissive.a, 1, 0);

        // oben links
        mesh.addVertex(-0.5f, 0.5f, 0, 0, 0, 1, emissive.r, emissive.g, emissive.b, emissive.a, 0, 0);

        // unten links
        mesh.addVertex(-0.5f, -0.5f, 0, 0, 0, 1, emissive.r, emissive.g, emissive.b, emissive.a, 0, 1);

        // unten rechts
        mesh.addVertex(0.5f, -0.5f, 0, 0, 0, 1, emissive.r, emissive.g, emissive.b, emissive.a, 1, 1);

        mesh.faces.add(0, 1, 2);
        mesh.faces.add(2, 3, 0);
    }

    @Override
    protected void renderInternal(@NotNull GL10 gl) {
    }

    @Override
    protected void setOptions() {
        enableColorMaterialEnabled(true);
        enableVertexColors(true);
        enableNormals(true);
        enableTextures(true);
        enableDoubleSided(false);
        enableLighting(false);
        enableAlpha(true);
//        setDrawingStyle(DrawingStyle.GL_TRIANGLES); // default anyway
//        setPointSize(3);
//        setPointSmoothing(true);
//        setLineWidth(1);
//        setLineSmoothing(true);
        enableMipMap(true);
    }

    @Override
    protected void updateInternal(float deltat) {
    }

    @Override
    protected void cleanupInternal(@NotNull ProcessingState state) {
        // OpenGL 1.1-Instanz beziehen
        //final GL11 gl11 = state.getGl11();

        // Puffer aufräumen
        //gl11.glDeleteBuffers(2, new int [] { _vertexVboId, _indexVboId,}, 0);
    }

    @Override
    public void onDirty() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Sprite)) return false;

        Sprite sprite = (Sprite) o;

        if (!bufferedTexture.equals(sprite.bufferedTexture)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return bufferedTexture.hashCode();
    }

    @Override
    public RenderableNode getStatic() {
        return new Sprite(this);
    }

    @Override
    public Sprite clone() {
        Sprite sprite = new Sprite(dimension.x, dimension.y, textureId, textureBufferId, textureBufferOffsetId, hasMipMap());
        sprite.setPosition(position);
        return sprite;
    }
}
