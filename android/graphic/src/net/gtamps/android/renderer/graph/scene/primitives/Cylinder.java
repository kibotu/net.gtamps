package net.gtamps.android.renderer.graph.scene.primitives;

import net.gtamps.android.renderer.graph.ProcessingState;
import net.gtamps.android.renderer.graph.RenderableNode;
import net.gtamps.android.renderer.mesh.Mesh;
import net.gtamps.android.renderer.mesh.Uv;
import net.gtamps.shared.Utils.math.Color4;
import net.gtamps.shared.Utils.math.MathUtils;
import net.gtamps.shared.Utils.math.Vector3;
import org.jetbrains.annotations.NotNull;

import javax.microedition.khronos.opengles.GL10;

public class Cylinder extends RenderableNode {

    private int segments;
    private float innerRadius;
    private float outerRadius;
    private float height;

    public Cylinder(int segments, float outerRadius, float innerRadius, float height) {
        this.segments = segments;
        this.height = height;
        this.outerRadius = outerRadius;
        this.innerRadius = innerRadius;
    }

    public Cylinder() {
        this(20, 1, 0.9f, 2);
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
        this.mesh = new Mesh(segments * 4, segments * 8);

        Color4 emissive = material.getEmission();

        addHorizontalSurface(false, height / +2);
        addHorizontalSurface(true, height / -2);
        addVerticalSurface(true);
        addVerticalSurface(false);
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


    private void addHorizontalSurface(boolean isTopSide, float zOffset) {

        int indexOffset = mesh.vertices.size();
        float step = MathUtils.deg2Rad(360f / segments);

        // vertices

        Color4 col = isTopSide ? new Color4(255, 0, 0, 255) : new Color4(0, 255, 0, 255);

        for (int i = 0; i < segments; i++) {
            float angle = (float) i * step;

            // outer
            float x1 = (float) Math.sin(angle) * outerRadius;
            float y1 = (float) Math.cos(angle) * outerRadius;
            float z1 = zOffset;
            Uv uv1 = new Uv(x1, y1);
            Vector3 n1 = Vector3.createNew(0, 0, isTopSide ? -1 : +1);
            mesh.vertices.addVertex(Vector3.createNew(x1, y1, z1), n1, col, uv1);

            // inner
            float x2 = (float) Math.sin(angle) * innerRadius;
            float y2 = (float) Math.cos(angle) * innerRadius;
            float z2 = zOffset;
            Uv uv2 = new Uv(x2, y2);
            Vector3 n2 = Vector3.createNew(0, 0, isTopSide ? -1 : +1);
            mesh.vertices.addVertex(Vector3.createNew(x2, y2, z2), n2, col, uv2);
        }

        // indices

        for (int i = 2; i <= segments; i++) {
            int a = indexOffset + i * 2 - 3 - 1;
            int b = indexOffset + i * 2 - 2 - 1;
            int c = indexOffset + i * 2 - 1 - 1;
            int d = indexOffset + i * 2 - 0 - 1;
            addQuad(a, b, c, d, isTopSide);
        }

        int a = indexOffset + segments * 2 - 1 - 1; // ... connect last segment
        int b = indexOffset + segments * 2 - 0 - 1;
        int c = indexOffset + 0;
        int d = indexOffset + 1;
        addQuad(a, b, c, d, isTopSide);
    }

    private void addVerticalSurface(boolean isOuter) {
        int off = (int) (mesh.vertices.size() / 2);

        for (int i = 0; i < segments - 1; i++) {
            int ul = i * 2;
            int bl = ul + off;
            int ur = i * 2 + 2;
            int br = ur + off;

            if (!isOuter) {
                ul++;
                bl++;
                ur++;
                br++;
            }
            addQuad(ul, bl, ur, br, isOuter);
        }

        int ul = (segments - 1) * 2;
        int bl = ul + off;
        int ur = 0 * 2;
        int br = ur + off;

        if (!isOuter) {
            ul++;
            bl++;
            ur++;
            br++;
        }

        addQuad(ul, bl, ur, br, isOuter);
    }

    private void addQuad(int ul, int bl, int ur, int br, boolean flipped) {
        if (!flipped) {
            mesh.faces.add((short) ul, (short) bl, (short) ur);
            mesh.faces.add((short) bl, (short) br, (short) ur);
        } else {
            mesh.faces.add((short) ur, (short) br, (short) ul);
            mesh.faces.add((short) br, (short) bl, (short) ul);
        }
    }
}
