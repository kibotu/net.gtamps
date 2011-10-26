package net.gtamps.android.core.renderer.graph.primitives;

import net.gtamps.android.core.renderer.graph.ProcessingState;
import net.gtamps.android.core.renderer.graph.RenderableNode;
import net.gtamps.android.core.renderer.mesh.Mesh;
import net.gtamps.shared.math.Color4;
import org.jetbrains.annotations.NotNull;

import javax.microedition.khronos.opengles.GL10;

public class Cube extends RenderableNode {

    public Cube(int width, int height, int depth) {
        dimension.set(width,height,depth);
    }

    public Cube() {
    }

    @Override
    protected void renderInternal(@NotNull GL10 gl) {
    }

    @Override
    protected void updateInternal(float deltat) {
    }

    @Override
    protected void cleanupInternal(@NotNull ProcessingState state) {
    }

    @Override
    protected void setupInternal(@NotNull ProcessingState state) {
        if(mesh != null)  return;

        // new mesh
        this.mesh = new Mesh(24,12);

        final float c = 1f;
        Color4 emissive = material.getEmissive();

        mesh.addVertex(c, -c, -c,0, -c, 0,emissive.r, emissive.g, emissive.b, emissive.a,0, 0);
        mesh.addVertex(c, -c, c,0, -c, 0,emissive.r, emissive.g, emissive.b, emissive.a, c, 0);
        mesh.addVertex(-c, -c, c,0, -c, 0,emissive.r, emissive.g, emissive.b, emissive.a,  c, c);
        mesh.addVertex(-c, -c, -c, 0, -c, 0,emissive.r, emissive.g, emissive.b, emissive.a, 0, c);
        mesh.addVertex(c, c, -c,  0, c, 0,emissive.r, emissive.g, emissive.b, emissive.a,0, 0);
        mesh.addVertex(-c, c, -c,0, c, 0,emissive.r, emissive.g, emissive.b, emissive.a,  c, 0);
        mesh.addVertex(-c, c, c,0, c, 0,emissive.r, emissive.g, emissive.b, emissive.a, c, c);
        mesh.addVertex(c, c, c,  0, c, 0,emissive.r, emissive.g, emissive.b, emissive.a, 0, c);
        mesh.addVertex(c, -c, -c,c, 0, 0,emissive.r, emissive.g, emissive.b, emissive.a, 0, 0);
        mesh.addVertex(c, c, -c, c, 0, 0, emissive.r, emissive.g, emissive.b, emissive.a,  c, 0);
        mesh.addVertex(c, c, c, c, 0, 0, emissive.r, emissive.g, emissive.b, emissive.a,c, c);
        mesh.addVertex(c, -c, c,c, 0, 0,emissive.r, emissive.g, emissive.b, emissive.a, 0, c);
        mesh.addVertex(c, -c, c,-0, -0, c,emissive.r, emissive.g, emissive.b, emissive.a,0, 0);
        mesh.addVertex(c, c, c, -0, -0, c,emissive.r, emissive.g, emissive.b, emissive.a, c, 0);
        mesh.addVertex(-c, c, c, -0, -0, c, emissive.r, emissive.g, emissive.b, emissive.a, c, c);
        mesh.addVertex(-c, -c, c, -0, -0, c, emissive.r, emissive.g, emissive.b, emissive.a, 0, c);
        mesh.addVertex(-c, -c, c,-c, -0, -0, emissive.r, emissive.g, emissive.b, emissive.a,  0, 0);
        mesh.addVertex(-c, c, c, -c, -0, -0,  emissive.r, emissive.g, emissive.b, emissive.a, c, 0);
        mesh.addVertex(-c, c, -c,-c, -0, -0,  emissive.r, emissive.g, emissive.b, emissive.a,   c, c);
        mesh.addVertex(-c, -c, -c, -c, -0, -0, emissive.r, emissive.g, emissive.b, emissive.a, 0, c);
        mesh.addVertex(c, c, -c,0, 0, -c,  emissive.r, emissive.g, emissive.b, emissive.a, 0, 0);
        mesh.addVertex(c, -c, -c, 0, 0, -c, emissive.r, emissive.g, emissive.b, emissive.a, c, 0);
        mesh.addVertex(-c,  -c, -c,0, 0, -c, emissive.r, emissive.g, emissive.b, emissive.a,  c, c);
        mesh.addVertex(-c, c, -c, 0, 0, -c,  emissive.r, emissive.g, emissive.b, emissive.a,0, c);

        mesh.faces.add(0, 1, 2);
        mesh.faces.add(0, 2, 3);
        mesh.faces.add(4, 5, 6);
        mesh.faces.add(4, 6, 7);
        mesh.faces.add(8, 9, 10);
        mesh.faces.add(8, 10, 11);
        mesh.faces.add(12, 13, 14);
        mesh.faces.add(12, 14, 15);
        mesh.faces.add(16, 17, 18);
        mesh.faces.add(16, 18, 19);
        mesh.faces.add(20, 21, 22);
        mesh.faces.add(20, 22, 23);

        enableColorMaterialEnabled(true);
        enableVertexColors(true);
        enableNormals(true);
        enableTextures(false);
        enableDoubleSided(false);
        enableLighting(true);
        enableAlpha(true);
//        setDrawingStyle(DrawingStyle.GL_TRIANGLES); // default anyway
//        setPointSize(3);
//        setPointSmoothing(true);
//        setLineWidth(1);
//        setLineSmoothing(true);
//        enableMipMap(false);
    }

    @Override
    public void onDirty() {
    }
}
