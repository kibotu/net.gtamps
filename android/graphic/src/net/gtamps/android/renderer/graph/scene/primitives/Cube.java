package net.gtamps.android.renderer.graph.scene.primitives;

import net.gtamps.android.renderer.graph.ProcessingState;
import net.gtamps.android.renderer.graph.RenderableNode;
import net.gtamps.android.renderer.mesh.Mesh;
import net.gtamps.shared.Utils.math.Color4;
import org.jetbrains.annotations.NotNull;

import javax.microedition.khronos.opengles.GL10;

public class Cube extends RenderableNode {

    public Cube(int width, int height, int depth) {
        dimension.set(width, height, depth);
    }

    public Cube(Cube other) {
        this.mesh = new Mesh(other.mesh);
        this.dimension.set(other.dimension);
        this.scaling.set(other.scaling);
    }

    public Cube() {
        this(1, 1, 1);
    }

    @Override
    protected void renderInternal(@NotNull GL10 gl) {
    }

    @Override
    public RenderableNode getStatic() {
        return new Cube(this);
    }

    @Override
    protected void setOptions() {
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
        enableMipMap(true);
//        material = new Material(new Color4(0xff000000),new Color4(0xff330000),new Color4(0xff660000),new Color4(0xff770000),5);
    }

    @Override
    protected void updateInternal(float deltat) {
    }

    @Override
    protected void cleanupInternal(@NotNull ProcessingState state) {
    }

    @Override
    protected void setupInternal(@NotNull ProcessingState state) {
        if (mesh != null) return;
        this.mesh = new Mesh(24, 12);

        final float c = 1f;
        Color4 emissive = material.getEmission();

        mesh.addVertex(c, -c, -c, 0, -c, 0, emissive.r, emissive.g, emissive.b, emissive.a, 0, 0);
        mesh.addVertex(c, -c, c, 0, -c, 0, emissive.r, emissive.g, emissive.b, emissive.a, c, 0);
        mesh.addVertex(-c, -c, c, 0, -c, 0, emissive.r, emissive.g, emissive.b, emissive.a, c, c);
        mesh.addVertex(-c, -c, -c, 0, -c, 0, emissive.r, emissive.g, emissive.b, emissive.a, 0, c);
        mesh.addVertex(c, c, -c, 0, c, 0, emissive.r, emissive.g, emissive.b, emissive.a, 0, 0);
        mesh.addVertex(-c, c, -c, 0, c, 0, emissive.r, emissive.g, emissive.b, emissive.a, c, 0);
        mesh.addVertex(-c, c, c, 0, c, 0, emissive.r, emissive.g, emissive.b, emissive.a, c, c);
        mesh.addVertex(c, c, c, 0, c, 0, emissive.r, emissive.g, emissive.b, emissive.a, 0, c);
        mesh.addVertex(c, -c, -c, c, 0, 0, emissive.r, emissive.g, emissive.b, emissive.a, 0, 0);
        mesh.addVertex(c, c, -c, c, 0, 0, emissive.r, emissive.g, emissive.b, emissive.a, c, 0);
        mesh.addVertex(c, c, c, c, 0, 0, emissive.r, emissive.g, emissive.b, emissive.a, c, c);
        mesh.addVertex(c, -c, c, c, 0, 0, emissive.r, emissive.g, emissive.b, emissive.a, 0, c);
        mesh.addVertex(c, -c, c, -0, -0, c, emissive.r, emissive.g, emissive.b, emissive.a, 0, 0);
        mesh.addVertex(c, c, c, -0, -0, c, emissive.r, emissive.g, emissive.b, emissive.a, c, 0);
        mesh.addVertex(-c, c, c, -0, -0, c, emissive.r, emissive.g, emissive.b, emissive.a, c, c);
        mesh.addVertex(-c, -c, c, -0, -0, c, emissive.r, emissive.g, emissive.b, emissive.a, 0, c);
        mesh.addVertex(-c, -c, c, -c, -0, -0, emissive.r, emissive.g, emissive.b, emissive.a, 0, 0);
        mesh.addVertex(-c, c, c, -c, -0, -0, emissive.r, emissive.g, emissive.b, emissive.a, c, 0);
        mesh.addVertex(-c, c, -c, -c, -0, -0, emissive.r, emissive.g, emissive.b, emissive.a, c, c);
        mesh.addVertex(-c, -c, -c, -c, -0, -0, emissive.r, emissive.g, emissive.b, emissive.a, 0, c);
        mesh.addVertex(c, c, -c, 0, 0, -c, emissive.r, emissive.g, emissive.b, emissive.a, 0, 0);
        mesh.addVertex(c, -c, -c, 0, 0, -c, emissive.r, emissive.g, emissive.b, emissive.a, c, 0);
        mesh.addVertex(-c, -c, -c, 0, 0, -c, emissive.r, emissive.g, emissive.b, emissive.a, c, c);
        mesh.addVertex(-c, c, -c, 0, 0, -c, emissive.r, emissive.g, emissive.b, emissive.a, 0, c);

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
    }

    @Override
    public void onDirty() {
    }
}
