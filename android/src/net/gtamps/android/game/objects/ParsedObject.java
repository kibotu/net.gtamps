package net.gtamps.android.game.objects;

import net.gtamps.android.core.renderer.graph.ProcessingState;
import net.gtamps.android.core.renderer.graph.RenderableNode;
import net.gtamps.android.core.renderer.mesh.Mesh;
import org.jetbrains.annotations.NotNull;

import javax.microedition.khronos.opengles.GL10;

public class ParsedObject extends RenderableNode {

    public String name;

    public ParsedObject() {
    }

    public ParsedObject(int maxVertices, int maxFaces) {
        mesh = new Mesh(maxVertices, maxFaces);
    }

    @Override
    protected void renderInternal(@NotNull GL10 gl) {
    }

    @Override
    public void onDirty() {
    }

    @Override
    protected void updateInternal(float deltat) {
    }

    @Override
    protected void cleanupInternal(@NotNull ProcessingState state) {
    }

    @Override
    protected void setupInternal(@NotNull ProcessingState state) {
        if(mesh != null) return;
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
        enableMipMap(false);
    }
}
