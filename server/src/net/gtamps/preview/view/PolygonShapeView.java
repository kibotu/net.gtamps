package net.gtamps.preview.view;

import java.awt.Graphics2D;
import java.awt.Polygon;

import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;

public class PolygonShapeView extends ShapeView {
	
	
	public PolygonShapeView(final PolygonShape shape) {
		super(shape);
	}

	@Override
	public void paintHook(final Graphics2D g) {
		final PolygonShape polygon = (PolygonShape) shape;
		final Polygon p = new Polygon();
		for (final Vec2 v :polygon.getVertices()) {
			final Vec2 w = PreviewPerspective.worldToPix(v);
			p.addPoint((int) w.x, (int) w.y);
		}
		g.drawPolygon(p);
	}

}
