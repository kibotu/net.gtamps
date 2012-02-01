package net.gtamps.android.graphics.graph.scene.mesh;

/**
 * Simple VO with three properties representing vertex indicies.
 * Is not necessary for functioning of engine, just a convenience.
 */
public class Face {

    public short a;
    public short b;
    public short c;

    public Face(short a, short b, short c) {
        this.a = a;
        this.b = b;
        this.c = c;
    }

    /**
     * Convenience method to cast int arguments to short's
     */
    public Face(int a, int b, int c) {
        this((short) a, (short) b, (short) c);
    }
}
