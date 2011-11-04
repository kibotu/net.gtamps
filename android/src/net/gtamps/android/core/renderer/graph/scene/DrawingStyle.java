package net.gtamps.android.core.renderer.graph.scene;

import javax.microedition.khronos.opengles.GL11;

public enum DrawingStyle {

    // opengl es
    GL_POINTS(GL11.GL_POINTS),
    GL_LINES(GL11.GL_LINES),
    GL_LINE_LOOP(GL11.GL_LINE_LOOP),
    GL_LINE_STRIP(GL11.GL_LINE_STRIP),
    GL_TRIANGLES(GL11.GL_TRIANGLES),
    GL_TRIANGLE_STRIP(GL11.GL_TRIANGLE_STRIP),
    GL_TRIANGLE_FAN(GL11.GL_TRIANGLE_FAN);

    // opengl 2.0
//    GL_QUADS(GL11.GL_QUADS),
//    GL_QUAD_STRIP(GL11.GL_QUAD_STRIP),
//    GL_POLYGON(GL11.GL_POLYGON);

    private final int value;

    private DrawingStyle(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}