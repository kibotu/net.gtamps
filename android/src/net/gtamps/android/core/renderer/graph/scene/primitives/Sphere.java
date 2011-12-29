package net.gtamps.android.core.renderer.graph.scene.primitives;

import net.gtamps.android.core.renderer.graph.ProcessingState;
import net.gtamps.android.core.renderer.graph.RenderableNode;
import net.gtamps.android.core.renderer.mesh.Material;
import net.gtamps.android.core.renderer.mesh.Mesh;
import net.gtamps.shared.Utils.math.Color4;
import net.gtamps.shared.Utils.math.Vector3;
import org.jetbrains.annotations.NotNull;

import javax.microedition.khronos.opengles.GL10;

public class Sphere extends RenderableNode {

    /**
     * Sphere stacks from pole to pole.
     */
    private int stacks;
    /**
     * Sphere slices around the equator.
     */
    private int slices;

    public Sphere(float radius, int stacks, int slices) {
        dimension.set(radius, radius, radius);
        this.stacks = stacks;
        this.slices = slices;
    }

    public Sphere(Sphere sphere) {
        super(sphere);
    }

    public Sphere() {
        this(1,20,20);
    }

    @Override
    protected void setupInternal(@NotNull ProcessingState state) {
        if (mesh != null) return;

        // new mesh
        this.mesh = new Mesh((stacks + 1) * (slices + 1), stacks * slices * 2);

        Color4 emissive = material.getEmission();

        int r, c;

        Vector3 n = Vector3.createNew();
        Vector3 pos = Vector3.createNew();
        Vector3 posFull = Vector3.createNew();

        // vertices
        for (r = 0; r <= slices; r++) {
            float v = (float) r / (float) slices; // [0,1]
            float theta1 = v * (float) Math.PI; // [0,PI]

            n.set(0, 1, 0);
            n.rotateZ(theta1);

            for (c = 0; c <= stacks; c++) {
                float u = (float) c / (float) stacks; // [0,1]
                float theta2 = u * (float) (Math.PI * 2f); // [0,2PI]
                pos.set(n);
                pos.rotateY(theta2);

                posFull.set(pos);
                posFull.mulInPlace(dimension.x);

                mesh.vertices.addVertex(posFull.x, posFull.y, posFull.z, pos.x, pos.y, pos.z, emissive.r, emissive.g, emissive.b, emissive.a, u, v);
            }
        }

        // faces
        int colLength = stacks + 1;

        for (r = 0; r < slices; r++) {
            int offset = r * colLength;

            for (c = 0; c < stacks; c++) {
                int ul = offset + c;
                int ur = offset + c + 1;
                int br = offset + (int) (c + 1 + colLength);
                int bl = offset + (int) (c + 0 + colLength);

                mesh.faces.addQuad(ul, ur, br, bl);
            }
        }

        n.recycle();
        pos.recycle();
        posFull.recycle();
    }

    @Override
    protected void renderInternal(@NotNull GL10 gl) {
    }

    @Override
    public RenderableNode getStatic() {
        return new Sphere(this);
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

//        getRenderState().drawingStyle = RenderState.DrawingStyle.GL_POINTS; // default anyway
//        setPointSize(3);
//        setPointSmoothing(true);
//        setLineWidth(1);
//        setLineSmoothing(true);
//        enableMipMap(false);
//        material = new Material(new Color4(0xff000000),new Color4(0xff330000),new Color4(0xff660000),new Color4(0xff770000),4);
//        material = Material.WHITE;
    }

    @Override
    protected void updateInternal(float deltat) {
    }

    @Override
    protected void cleanupInternal(@NotNull ProcessingState state) {
    }

    @Override
    public void onDirty() {
    }
}
