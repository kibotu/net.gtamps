package net.gtamps.android.renderer.graph.scene.primitives;

import net.gtamps.android.renderer.graph.ProcessingState;
import net.gtamps.android.renderer.graph.RenderableNode;
import net.gtamps.android.renderer.mesh.Mesh;
import net.gtamps.android.renderer.mesh.Uv;
import net.gtamps.android.renderer.mesh.Vertex;
import net.gtamps.shared.Utils.math.Color4;
import net.gtamps.shared.Utils.math.Vector3;
import org.jetbrains.annotations.NotNull;

import javax.microedition.khronos.opengles.GL10;

public class Torus extends RenderableNode {

    protected float largeRadius;
    protected float smallRadius;
    protected int segmentsW;
    protected int segmentsH;

    public Torus(float largeRadius, float smallRadius, int segmentsW, int segmentsH) {
        this.largeRadius = largeRadius;
        this.smallRadius = smallRadius;
        this.segmentsW = segmentsW;
        this.segmentsH = segmentsH;
    }

    public Torus() {
        this(1, 0.5f, 30, 30);
    }

    @Override
    protected void renderInternal(@NotNull GL10 gl) {
    }

    @Override
    public RenderableNode getStatic() {
        return this;
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
        if (mesh != null) return;
        this.mesh = new Mesh(segmentsW * segmentsH * 2 * 3, segmentsW * segmentsH * 2);

        Color4 emissive = material.getEmission();

        float r1 = largeRadius;
        float r2 = smallRadius;
        int steps1 = segmentsW;
        int steps2 = segmentsH;
        float step1r = (float) ((2.0 * Math.PI) / steps1);
        float step2r = (float) ((2.0 * Math.PI) / steps2);
        float a1a = 0;
        float a1b = step1r;
        int vcount = 0;

        for (float s = 0; s < steps1; s++, a1a = a1b, a1b += step1r) {
            float a2a = 0;
            float a2b = step2r;

            for (float s2 = 0; s2 < steps2; s2++, a2a = a2b, a2b += step2r) {

                float ux1 = s / steps1;
                float ux0 = (s + 1) / steps1;
                float uy0 = s2 / steps2;
                float uy1 = (s2 + 1) / steps2;

                mesh.addVertex(getVertex(a1a, r1, a2a, r2, emissive, 1 - ux1, uy0),
                        getVertex(a1b, r1, a2a, r2, emissive, 1 - ux0, uy0),
                        getVertex(a1b, r1, a2b, r2, emissive, 1 - ux0, uy1),
                        getVertex(a1a, r1, a2b, r2, emissive, 1 - ux1, uy1));

                mesh.faces.add(vcount, vcount + 1, vcount + 2);
                mesh.faces.add(vcount, vcount + 2, vcount + 3);

                vcount += 4;
            }
        }
    }

    private Vertex getVertex(float a1, float r1, float a2, float r2, Color4 color, float u, float v) {
        Vertex vertex = new Vertex();
        vertex.normal = Vector3.createNew();

        float ca1 = (float) Math.cos(a1);
        float sa1 = (float) Math.sin(a1);
        float ca2 = (float) Math.cos(a2);
        float sa2 = (float) Math.sin(a2);

        float centerX = r1 * ca1;
        float centerZ = -r1 * sa1;

        vertex.normal.x = ca2 * ca1;
        vertex.normal.y = sa2;
        vertex.normal.z = -ca2 * sa1;

        vertex.position.x = centerX + r2 * vertex.normal.x;
        vertex.position.y = r2 * vertex.normal.y;
        vertex.position.z = centerZ + r2 * vertex.normal.z;

        vertex.uv = new Uv(u, v);
        vertex.color = color;

        return vertex;
    }

    @Override
    protected void setOptions() {
        enableColorMaterialEnabled(true);
        enableVertexColors(true);
        enableNormals(true);
//        enableTextures(false);
        enableDoubleSided(false);
        enableLighting(true);
        enableAlpha(true);
        enableMipMap(true);

//        getRenderState().drawingStyle = RenderState.DrawingStyle.GL_POINTS; // default anyway
//        setPointSize(3);
//        setPointSmoothing(true);
//        setLineWidth(1);
//        setLineSmoothing(true);
//        enableMipMap(false);
//        material = new Material(new Color4(0xff000000),new Color4(0xff330000),new Color4(0xff660000),new Color4(0xff770000),4);
//        material = Material.WHITE;
    }
}
