package net.gtamps.preview.view;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.util.Collection;
import java.util.LinkedList;

public abstract class ViewNode {
	
	protected static final Color DEFAULT_COLOR = Color.WHITE;
	private static final Point ORIGIN = new Point(0,0);
	
	private final Collection<ViewNode> children = new LinkedList<ViewNode>();
	
	private Graphics2D g;
	private Point position = new Point(0,0);
	private float rotation = 0f;
	private float scale = 1f;
	
	
	private Color savedColor = null;
	private AffineTransform savedTransform = null;
	
	public final void update() {
		updateHook();
		for (final ViewNode child: children) {
			child.update();
		}
	}
	
	public final void paint(final Graphics2D g) {
		this.g = g;
		saveColor();
		saveTransform();
		transform();
		
		paintHook();
		
		for (final ViewNode child: children) {
			child.paint(g);
		}
		
		restoreTransform();
		restoreColor();
		this.g = null;
	}
	
	public void addChild(final ViewNode child) {
		children.add(child);
	}

	public Point getPosition() {
		return position;
	}
	
	public float getRotation() {
		return rotation;
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
	
	protected abstract void updateHook();
	protected abstract void paintHook();
	
	protected void setPosition(final int x, final int y) {
		position = new Point(x, y);
	}

	protected void setRotation(final float radians) {
		rotation = radians;
	}
	
	protected void setScale(final float s) {
		if (s <= 0) {
			throw new IllegalArgumentException("'s' must be > 0");
		}
		scale = s;
	}

	protected void setColor(final Color c) {
		g.setColor(c);
	}
	
	protected void setBackgroundColor(final Color c) {
		g.setBackground(c);
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

	protected void fillShape(final Shape shape) {
		g.fill(shape);
	}
	
	protected void drawString(final String string, final int x, final int y) {
		g.drawString(string, x, y);
	}
	
	protected void markLocation() {
		final int size = 10;
		final int halfSize = size >> 1;
		g.fillOval(-halfSize, -halfSize, size, size);
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
	
	private void transform() {
		if (rotation != 0) {
			g.rotate(rotation);
		}
		if (!ORIGIN.equals(position)) {
			g.translate(position.x, position.y);
		}
		if (scale != 1f) {
			g.scale(scale, scale);
		}
	}

	
	private void saveTransform() {
		savedTransform = g.getTransform();
	}

	private void restoreTransform() {
		g.setTransform(savedTransform);
	}


}

