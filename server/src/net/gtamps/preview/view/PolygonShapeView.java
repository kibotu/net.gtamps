package net.gtamps.preview.view;

import java.awt.Graphics2D;
import java.awt.Polygon;

import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;

public class PolygonShapeView extends ShapeView {
	
	Polygon polygon;
	
	public PolygonShapeView(final PolygonShape shape) {
		super(shape);
		polygon = polygonShapeToAwtPolygon(shape);
		
	}

	@Override
	public void paintHook(final Graphics2D g) {
		final Vec2 pos = ((PolygonShape) shape).getCentroid();
		
//		translate(pos, g);
		g.draw(polygon);
//		translate(pos.negate(), g);
		
	}
	
	private Polygon polygonShapeToAwtPolygon(final PolygonShape p) {
		final Vec2 centroid = p.getCentroid();
		final Polygon polygon = new Polygon();
		for (final Vec2 v: p.getVertices()) {
			final int x = worldToPixels(v.x);
			final int y = worldToPixels(v.y);
			polygon.addPoint(x, y);
		}
		return polygon;
	}

}
