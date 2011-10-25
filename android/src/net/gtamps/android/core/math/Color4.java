package net.gtamps.android.core.math;

import net.gtamps.android.core.utils.OpenGLUtils;

import java.nio.FloatBuffer;

public class Color4 {

	public short r;
	public short g;
	public short b;
	public short a;

	private FloatBuffer colorBuffer;

	public Color4()	{
        this(255,255,255,255);
        net.gtamps.shared.Utils.Logger.D(this, "test");
	}

	public Color4(int r, int g, int b, int a) {
		this.r = (short)r;
		this.g = (short)g;
		this.b = (short)b;
		this.a = (short)a;

		colorBuffer = toFloatBuffer();
	}

    public Color4(float r, float g, float b, float a) {
        this((int)r,(int)g,(int)b,(int)a);
    }

    /**
	 *  Convenience method to set all properties in one line.
	 */
	public void setAll(int r, int g, int b, int a)	{
		this.r = (short)r;
		this.g = (short)g;
		this.b = (short)b;
		this.a = (short)a;
        toFloatBuffer(colorBuffer);
	}

	/**
	 * Convenience method to set all properties off one 32-bit rgba value
	 */
	public void setAll(long argb32) {
		a = (short) ((argb32 >> 24) & 0x000000FF);
		r = (short) ((argb32 >> 16) & 0x000000FF);
		g = (short) ((argb32 >> 8) & 0x000000FF);
		b = (short) ((argb32) & 0x000000FF);
        toFloatBuffer(colorBuffer);
	}

	public short getRed() {
		return r;
	}

	public short getGreen() {
		return g;
	}

	public short getBlue() {
		return b;
	}

	public short getAlpha() {
		return a;
	}

    public float getRedNormalized() {
		return r/255f;
	}

	public float getGreenNormalized() {
		return (float)g/255f;
	}

	public float getBlueNormalized() {
		return (float)b/255f;
	}

	public float getAlphaNormalized() {
		return (float)a/255f;
	}

	/**
	 * Convenience method
	 */
	public FloatBuffer toFloatBuffer() {
		return OpenGLUtils.makeFloatBuffer4(
                (float) r / 255f,
                (float) g / 255f,
                (float) b / 255f,
                (float) a / 255f
        );
	}

	/**
	 * Convenience method
	 */
	public void toFloatBuffer(FloatBuffer floatBuffer)	{
        OpenGLUtils.addFloat4PositionZero(floatBuffer,(float)r/255f,(float)g/255f,(float)b/255f,(float)a/255f);
	}

	public FloatBuffer getColorBuffer() {
        return colorBuffer;
    }

	public void commitToFloatBuffer() {
		this.toFloatBuffer(colorBuffer);
	}

	@Override
	public String toString() {
		return "[r=" + r + "|g=" + g + "|b=" + b + "|a=" + a + "]";
	}
}
