package net.gtamps.preview.view;

import java.awt.Color;
import java.awt.Graphics2D;

import org.jbox2d.common.Vec2;

public abstract class PhysicsView {
	
	public static final float PHYSICS_PIX_FACTOR = 1f;
	
	protected static final Color DEFAULT_LINE_COLOR = Color.WHITE;
	protected static final Color DEFAULT_BACK_COLOR = Color.BLACK;
	protected static final Color SENSOR_SHAPE_COLOR = new Color(0, 0, 192, 48);
	
	public abstract void paint(Graphics2D g);
	
	protected final int worldToPixels(final float worldLength) {
		return PreviewPerspective.worldLengthToPix(worldLength);
	}
	
	protected final Vec2 worldToPixels(final Vec2 pos) {
		return new Vec2(worldToPixels(pos.x), worldToPixels(pos.y));
	}
	
	protected final void translate(final Vec2 pos, final Graphics2D g) {
		final float tx = worldToPixels(pos.x);
		final float ty = worldToPixels(pos.y);
		g.translate(tx, ty);
	}

	protected final void rotate(final float radians, final Graphics2D g) {
		g.rotate(radians);
	}
	
	protected final Color mix(final Color c1, final Color c2, final float ratio) {
		assert ratio >= 0f && ratio <= 1f : "ratio must be between 0 and 1";
		final int r = mix(c1.getRed(), c2.getRed(), ratio);
		final int g = mix(c1.getGreen(), c2.getGreen(), ratio);
		final int b = mix(c1.getBlue(), c2.getBlue(), ratio);
		final int alpha = mix(c1.getAlpha(), c2.getAlpha(), ratio);
		return new Color(r, g, b, alpha);
	}
	
	private final int mix(final int a, final int b, final float ratio) {
		final float invRatio = 1f - ratio;
		return (int) (a * ratio + b * invRatio);
	}
	
}
