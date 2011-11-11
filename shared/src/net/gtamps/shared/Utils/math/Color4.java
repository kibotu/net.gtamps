package net.gtamps.shared.Utils.math;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

/**
 * A simple three component color vector. Color4 vectors are non-mutable and can
 * be passed around by value.
 */
public class Color4 implements Comparable<Color4> {

    public float r, g, b, a;

    public static final Color4 RED = new Color4(0xffff0000);
    public static final Color4 GREEN = new Color4(0xff00ff00);
    public static final Color4 BLUE = new Color4(0xff0000ff);
    public static final Color4 YELLOW = new Color4(0xffffff00);
    public static final Color4 PURPLE = new Color4(0xffff00ff);
    public static final Color4 CYAN = new Color4(0xff00ffff);
    public static final Color4 BLACK = new Color4(0xff000000);
    public static final Color4 WHITE = new Color4(0xffffffff);
    public static final Color4 LIGHT_GRAY = new Color4(0xffdddddd);
    public static final Color4 GRAY = new Color4(0xff888888);
    public static final Color4 DARK_GRAY = new Color4(0xff444444);
    public static final Color4 TRANSPARENT = new Color4(0);

    /**
     * Construct a new color vector and initialize the components.
     *
     * @param r The red component.
     * @param g The green component.
     * @param b The blue component.
     */
    public Color4(float r, float g, float b) {
        this(r, g, b, 1);
    }

    public Color4(Vector3 v) {
        this(v.x, v.y, v.z, 1);
    }

    /**
     * Convenient constructor to create more readable color values.
     *
     * @param r 0...255
     * @param g 0...255
     * @param b 0...255
     * @param a 0...255
     */
    public Color4(int r, int g, int b, int a) {
        this((float) r / 255f, (float) g / 255f, (float) b / 255f, (float) a / 255f);
    }

    /**
     * Convenient constructor for creating colors in hex notion.
     * <p>
     * 0xff000000 alpha <br />
     * 0x00ff0000 red <br />
     * 0x0000ff00 green <br />
     * 0x000000ff blue
     * </p>
     *
     * @param argb32 color value in hex notion
     */
    public Color4(int argb32) {
        this((argb32 >> 16) & 0x000000FF, (argb32 >> 8) & 0x000000FF, (argb32) & 0x000000FF, (argb32 >> 24) & 0x000000FF);
    }

    public void setAll(int argb32) {
        r = 2.55f * ((argb32 >> 16) & 0x000000FF);
        g = 2.55f * ((argb32 >> 8) & 0x000000FF);
        b = 2.55f * (argb32 & 0x000000FF);
        a = 2.55f * ((argb32 >> 24) & 0x000000FF);
    }

    public void setAll(float r, float g, float b, float a) {
        this.r = r;
        this.g = g;
        this.b = b;
        this.a = a;
    }

    public void setAll(Color4 color) {
        setAll(color.r, color.g, color.b, color.a);
    }

    /**
     * Construct a new color vector and initialize the components.
     *
     * @param r The red component.
     * @param g The green component.
     * @param b The blue component.
     * @param a The alpha component.
     */
    public Color4(float r, float g, float b, float a) {
        this.r = r;
        this.g = g;
        this.b = b;
        this.a = a;
    }

    /**
     * Calculate the sum of two colors.
     *
     * @param c The second color.
     * @return The sum.
     */
    public Color4 add(Color4 c) {
        return new Color4(r + c.r, g + c.g, b + c.b, a + c.a);
    }

    /**
     * Calculate the product of this color an a scalar.
     *
     * @param s The scalar.
     * @return The product.
     */
    public Color4 modulate(float s) {
        return new Color4(r * s, g * s, b * s, a * s);
    }

    /**
     * Perform the component wise multiplication of two colors. This is not a dot
     * product!
     *
     * @param c The second color.
     * @return The result of the multiplication.
     */
    public Color4 modulate(Color4 c) {
        return new Color4(r * c.r, g * c.g, b * c.b, a * c.a);
    }

    /**
     * Clip the color components to the interval [0.0, 1.0].
     *
     * @return The clipped color.
     */
    public Color4 clip() {
        Color4 c = new Color4(Math.min(r, 1), Math.min(g, 1), Math.min(b, 1), Math.min(a, 1));
        return new Color4(Math.max(c.r, 0), Math.max(c.g, 0), Math.max(c.b, 0), Math.max(c.a, 0));
    }

    public float[] asArray() {
        return new float[]{r, g, b, a};
    }

    public FloatBuffer asBuffer() {
        final ByteBuffer vbb = ByteBuffer.allocateDirect(Color4.size() << 2);
        vbb.order(ByteOrder.nativeOrder());
        FloatBuffer buf = vbb.asFloatBuffer();
        fillBuffer(buf);
        buf.rewind();
        return buf;
    }

    public void fillBuffer(FloatBuffer buf) {
        buf.position(0);
        buf.put(r);
        buf.put(g);
        buf.put(b);
        buf.put(a);
    }

    /**
     * Return this color in a packed pixel format suitable for use with AWT.
     *
     * @return The color as a packed pixel integer value.
     */
    public int toAwtColor() {
        Color4 c = clip();
        return (toInt(c.a) << 24) | (toInt(c.r) << 16) | (toInt(c.g) << 8) | toInt(c.b);
    }

    /**
     * Convert a floating point component from the interval [0.0, 1.0] to an
     * integral component in the interval [0, 255]
     *
     * @param c The float component.
     * @return The integer component.
     */
    public static int toInt(float c) {
        return Math.round(c * 255.0f);
    }

    @Override
    public int compareTo(Color4 o) {
        if (r != o.r)
            return (r < o.r ? -1 : 1);
        if (g != o.g)
            return (g < o.g ? -1 : 1);
        if (b != o.b)
            return (b < o.b ? -1 : 1);
        if (a != o.a)
            return (a < o.a ? -1 : 1);
        return 0;
    }

    public static int size() {
        return 4;
    }

    @Override
    public String toString() {
        return "Color4{" +
                "r=" + r +
                ", g=" + g +
                ", b=" + b +
                ", a=" + a +
                '}';
    }

    public short getRedAsShort() {
        return (short) (r / 255);
    }

    public short getGreenAsShort() {
        return (short) (g / 255);
    }

    public short getBlueAsShort() {
        return (short) (b / 255);
    }

    public short getAlphaAsShort() {
        return (short) (a / 255);
    }
}
