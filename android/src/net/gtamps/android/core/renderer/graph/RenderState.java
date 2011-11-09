package net.gtamps.android.core.renderer.graph;

import javax.microedition.khronos.opengles.GL11;

import static javax.microedition.khronos.opengles.GL10.*;

public class RenderState {

    public BlendState blendState;
    public DrawingStyle drawingStyle;
    public Shader shader;

    public RenderState(BlendState blendState, DrawingStyle drawingStyle, Shader shader) {
        this.blendState = blendState;
        this.drawingStyle = drawingStyle;
        this.shader = shader;
    }

    public RenderState() {
        this.blendState = BlendState.ADDITIVE;
        this.drawingStyle = DrawingStyle.GL_TRIANGLES;
        this.shader = Shader.SMOOTH;
    }

    public enum BlendState {
        OFF(GL_ONE, GL_ONE),
        ADDITIVE(GL_SRC_ALPHA,GL_ONE_MINUS_SRC_ALPHA),
        PREMULT_ALPHA(GL_ONE,GL_ONE_MINUS_SRC_ALPHA),
        MODULATE(GL_DST_COLOR, GL_ZERO),
        MODULATE_X2(GL_DST_COLOR, GL_SRC_COLOR);
        public final int sfactor;
        public final int dfactor;
        private BlendState (int sfactor,  int dfactor) {
            this.sfactor = sfactor;
            this.dfactor = dfactor;
        }
    }

    public enum Shader {
        FLAT(GL_FLAT),
        SMOOTH(GL_SMOOTH);

        private final int value;
        private Shader (int value) {
            this.value = value;
        }
        public int getValue() {
            return value;
        }
    }

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
}
