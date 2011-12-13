package net.gtamps.preview.view;

import java.awt.Color;
import java.awt.Graphics2D;

import org.jbox2d.collision.shapes.CircleShape;
import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.collision.shapes.Shape;

public abstract class ShapeView implements PhysicsView {
	
	protected Color fillColor = Color.BLACK;
	protected Color lineColor = Color.WHITE;
	
	
	public static ShapeView getShapeView(final Shape shape) {
		final Class<? extends Shape> type = shape.getClass();
		if (type == CircleShape.class) {
			return new CircleShapeView((CircleShape) shape);
		} else if (type == PolygonShape.class) {
			return new PolygonShapeView((PolygonShape) shape);
		} else {
			throw new IllegalArgumentException("unknown shape type");
		}
	}
	
	protected Shape shape;
	
	protected ShapeView(final Shape shape) {
		this.shape = shape;
	}
	
	@Override
	public final void paint(final Graphics2D g) {
		setColorsAccordingtoShape(g, shape);
		paintHook(g);
	}

	protected abstract void paintHook(Graphics2D g);
	
	private void setColorsAccordingtoShape(final Graphics2D g, final Shape shape) {
		g.setColor(lineColor);
		g.setBackground(fillColor);
	}

}
