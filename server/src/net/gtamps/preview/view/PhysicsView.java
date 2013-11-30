package net.gtamps.preview.view;


import java.awt.Polygon;
import java.awt.Shape;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Path2D;

import org.jbox2d.common.Vec2;

public abstract class PhysicsView extends ViewNode {

	private static final float FLOAT_TO_PIX_FACTOR = 10f;
	
	private static final float ARROWBARB_RELATIVE_SIZE = 0.05f;
	private static final float ARROWBARB_HALFANGLE = (float) Math.toRadians(45);
	
	protected void setPosition(final float x, final float y) {
		setPosition(worldToPixels(x), worldToPixels(y));
	}
	
	protected void setPosition(final Vec2 pos) {
		setPosition(pos.x, pos.y);
	}

	protected Shape getCircle(final Vec2 center, final float radius) {
		final float worldRadius = worldToPixels(radius);
		final float diam = worldRadius * 2f;
		final float x0 = worldToPixels(center.x - radius);
		final float y0 = worldToPixels(center.y - radius);
		return new Ellipse2D.Float(x0, y0, diam, diam);
	}
	
	protected Shape getPolygon(final Vec2... vertices) {
		final Polygon p = new Polygon();
		for (final Vec2 v: vertices) {
			final Vec2 w = worldToPixels(v);
			p.addPoint((int)w.x, (int)w.y);
		}
		return p;
	}
	
	protected Shape getArrow(final Vec2 direction) {
		final Vec2 end = direction.mul(FLOAT_TO_PIX_FACTOR);
		final Vec2 barbVec  = end.mul(ARROWBARB_RELATIVE_SIZE).negateLocal();
		final float sinA = (float) Math.sin(ARROWBARB_HALFANGLE);
		final float cosA = (float) Math.cos(ARROWBARB_HALFANGLE);
		final float dx = barbVec.x * cosA - barbVec.y * sinA;
		final float dy = barbVec.x * sinA + barbVec.y * cosA;

		final Path2D path = new Path2D.Float();
		path.moveTo(end.x, end.y);
		path.lineTo(0, 0);
		path.moveTo(end.x, end.y);
		path.lineTo(end.x + dx, end.y + dy);
		
		return path;
	}
	
	
	private Vec2 worldToPixels(final Vec2 v) {
		return new Vec2(worldToPixels(v.x), worldToPixels(v.y));
	}
	
	private int worldToPixels(final float worldLength) {
		return (int) (worldLength * FLOAT_TO_PIX_FACTOR);
	}

}
