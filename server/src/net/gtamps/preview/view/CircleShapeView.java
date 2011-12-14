package net.gtamps.preview.view;

import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;

import org.jbox2d.collision.shapes.CircleShape;
import org.jbox2d.common.Vec2;

public class CircleShapeView extends ShapeView {
	
	Ellipse2D ellipse;
	
	public CircleShapeView(final CircleShape shape) {
		super(shape);
		ellipse = circleShapeToAwtEllipse(shape);
	}

	@Override
	public void paintHook(final Graphics2D g) {
		final CircleShape circle = (CircleShape) shape;

		final Vec2 pos = circle.getLocalPosition();
		translate(pos, g);
		g.fill(ellipse);
		g.draw(ellipse);
		translate(pos.negate(), g);
		
	}

	private Ellipse2D circleShapeToAwtEllipse(final CircleShape circle) {
		final Vec2 center = circle.getLocalPosition();
		final float radius = worldToPixels(circle.m_radius);
		final float diam  = radius * 2f;
		final float x = worldToPixels(center.x) - radius;
		final float y = worldToPixels(center.y) - radius;
		final Ellipse2D ellipse = new Ellipse2D.Float(x, y, diam, diam);
		return ellipse;
	}
	
	
}
