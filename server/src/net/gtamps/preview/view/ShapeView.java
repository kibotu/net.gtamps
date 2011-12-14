package net.gtamps.preview.view;

import java.awt.Color;
import java.awt.Graphics2D;

import org.jbox2d.collision.shapes.CircleShape;
import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.collision.shapes.Shape;

public abstract class ShapeView extends PhysicsView {
	
	protected Color shapeColor = DEFAULT_LINE_COLOR;
	
	
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
		final Color previousColor = g.getColor();
		setColorsAccordingtoShape(g, shape);
		paintHook(g);
		g.setColor(previousColor);
	}

	protected abstract void paintHook(Graphics2D g);

	
	private void setColorsAccordingtoShape(final Graphics2D g, final Shape shape) {
		if (shape.isSensor()) {
			shapeColor = SENSOR_SHAPE_COLOR;
		}
		final Color givenColor = g.getColor();
		g.setColor(mix(givenColor, shapeColor, 0.9f));
	}

}
