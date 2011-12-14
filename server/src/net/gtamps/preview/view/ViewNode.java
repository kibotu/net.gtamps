package net.gtamps.preview.view;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Shape;

import org.jbox2d.common.Vec2;

public abstract class ViewNode {
	
	private static final float FLOAT_TO_PIX_FACTOR = 1f;

	Graphics2D g;
	Point position;
	float rotation;
	
	Color savedColor = null;
	
	public void paint(final Graphics2D g) {
		this.g = g;
		saveColor();
		translate();
		rotate();
		
		paintHook();
		
		undoRotate();
		undoTranslate();
		restoreColor();
	}

	public Point getPosition() {
		return position;
	}
	
	public float getRotation() {
		return rotation;
	}
	
	public int getRotationDeg() {
		return (int) (Math.toDegrees(rotation)) % 360;
	}

	@Override
	public String toString() {
		return new StringBuilder()
		.append("ViewNode [")
		.append("pos=(").append(position.x).append(", ").append(position.y).append(")")
		.append(' ')
		.append("rot=").append(Float.toString(rotation))
		.append("]")
		.toString();
	}
	
	protected abstract void paintHook();
	
	protected void setPosition(final float x, final float y) {
		position = new Point(worldToPixels(x), worldToPixels(y));
	}

	protected void setPosition(final Vec2 pos) {
		setPosition(pos.x, pos.y);
	}
	
	protected void setRotation(final float radians) {
		rotation = radians;
	}
	
	protected void setColor(final Color c) {
		g.setColor(c);
	}
	
	protected Color getColor() {
		return g.getColor();
	}
	
	protected void addMixColor(final Color c, final float ratio) {
		if (ratio < 0f || ratio > 1f) {
			throw new IllegalArgumentException("'ratio' must be between 0 and 1, is: " + ratio);
		}
		final Color isColor = g.getColor();
		g.setColor(mix(c, isColor, ratio));
	}
	
	protected void drawShape(final Shape shape) {
		g.draw(shape);
	}
	
	protected void drawString(final String string, final float x, final float y) {
		drawStringPix(string, worldToPixels(x), worldToPixels(y));
	}
	
	protected void drawStringPix(final String string, final int pixX, final int pixY) {
		g.drawString(string, pixX, pixY);
	}
	
	private int worldToPixels(final float worldLength) {
		return (int) (worldLength * FLOAT_TO_PIX_FACTOR);
	}
	
	
	private Color mix(final Color c1, final Color c2, final float ratio) {
		assert ratio >= 0f && ratio <= 1f : "ratio must be between 0 and 1";
		final int r = mix(c1.getRed(), c2.getRed(), ratio);
		final int g = mix(c1.getGreen(), c2.getGreen(), ratio);
		final int b = mix(c1.getBlue(), c2.getBlue(), ratio);
		final int alpha = mix(c1.getAlpha(), c2.getAlpha(), ratio);
		return new Color(r, g, b, alpha);
	}
	
	private int mix(final int a, final int b, final float ratio) {
		final float invRatio = 1f - ratio;
		return (int) (a * ratio + b * invRatio);
	}

	private void saveColor() {
		savedColor = g.getColor();
	}
	
	private void restoreColor() {
		g.setColor(savedColor);
	}
	
	private void rotate() {
		g.rotate(rotation);
	}
	
	private void undoRotate() {
		g.rotate(-rotation);
	}
	
	private void translate() {
		g.translate(position.x, position.y);
	}
	
	private void undoTranslate() {
		g.translate(-position.x, -position.y);
	}
	
}
