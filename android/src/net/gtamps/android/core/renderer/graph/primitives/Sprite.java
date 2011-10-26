package net.gtamps.android.core.renderer.graph.primitives;

import net.gtamps.android.core.renderer.graph.ProcessingState;
import net.gtamps.android.core.renderer.graph.RenderableNode;
import net.gtamps.android.core.renderer.mesh.Mesh;
import net.gtamps.shared.math.Color4;
import org.jetbrains.annotations.NotNull;

import javax.microedition.khronos.opengles.GL10;

public class Sprite extends RenderableNode {

    public Sprite() {
    }

    @Override
    protected void setupInternal(@NotNull ProcessingState state) {
        if(mesh != null) {
            return;
        }

        // new mesh
        this.mesh = new Mesh(4,2);

        Color4 emissive = material.getEmissive();

        // oben rechts
        mesh.addVertex(1, 1, 0, 0, 0, 1, emissive.r, emissive.g, emissive.b, emissive.a, 1, 0);

        // oben links
        mesh.addVertex(-1, 1, 0, 0, 0, 1, emissive.r, emissive.g, emissive.b, emissive.a, 0, 0);

        // unten links
        mesh.addVertex(-1, -1, 0, 0, 0, 1, emissive.r, emissive.g, emissive.b, emissive.a, 0, 1);

        // unten rechts
        mesh.addVertex(1, -1, 0, 0, 0, 1, emissive.r, emissive.g, emissive.b, emissive.a, 1, 1);

        mesh.faces.add(0, 1, 2);
        mesh.faces.add(2, 3, 0);

        enableColorMaterialEnabled(false);
        enableVertexColors(false);
        enableNormals(false);
        enableTextures(false);
        enableDoubleSided(false);
        enableLighting(false);
        enableAlpha(true);
//        setDrawingStyle(DrawingStyle.GL_TRIANGLES); // default anyway
//        setPointSize(3);
//        setPointSmoothing(true);
//        setLineWidth(1);
//        setLineSmoothing(true);
        enableMipMap(false);
    }

    @Override
    protected void renderInternal(@NotNull GL10 gl) {
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
}
