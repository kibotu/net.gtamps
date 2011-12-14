package net.gtamps.preview.view;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.Path2D;

import org.jbox2d.common.Vec2;

public class VectorView extends PhysicsView {

	private static final float ARROWBARB_RELATIVE_SIZE = 0.2f;

	private static final float ARROWBARB_HALFANGLE = (float) Math.toRadians(45);
	
	private final Vec2 vector;
	private final Vec2 origin;
	private Color color = DEFAULT_LINE_COLOR;

	public VectorView(final Vec2 vector, final Vec2 origin) {
		this.vector = vector;
		this.origin = origin;
	}

	@Override
	public void paint(final Graphics2D g) {
		final Color previousColor = g.getColor();
		g.setColor(color);
		
		Vec2 direction = vector;

//		DEBUG
		direction = new Vec2(9,9);
		final Shape arrow = getArrow(worldToPixels(origin), worldToPixels(direction));
		g.draw(arrow);
		
		g.setColor(previousColor);
	}
	
	public void setColor(final Color c) {
		color = c;
	}
	
	private Shape getArrow(final Vec2 origin, final Vec2 direction) {
		final Vec2 barbVec  = direction.mul(ARROWBARB_RELATIVE_SIZE).negateLocal();
		final float sinA = (float) Math.sin(ARROWBARB_HALFANGLE);
		final float cosA = (float) Math.cos(ARROWBARB_HALFANGLE);
		final float dx = barbVec.x * cosA - barbVec.y * sinA;
		final float dy = barbVec.x * sinA + barbVec.y * cosA;

		final Vec2 end = origin.add(direction);
		final Path2D path = new Path2D.Float();
		path.moveTo(end.x, end.y);
		path.lineTo(origin.x, origin.y);
		path.moveTo(end.x, end.y);
		path.lineTo(end.x + dx, end.y + dy);
		
		return path;
	}
	
}
