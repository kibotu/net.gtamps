package net.gtamps.preview.view;

import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;

import org.jbox2d.collision.shapes.Shape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;

public class BodyView implements PhysicsView {
	
	private final List<ShapeView> shapes = new ArrayList<ShapeView>();
	private final Body body;
	
	public BodyView(final Body body) {
		this.body = body;
		for (Shape shape = body.getShapeList(); shape != null; shape = shape.getNext()) {
			shapes.add(ShapeView.getShapeView(shape));
		}
	}

	@Override
	public void paint(final Graphics2D g) {
		final Vec2 pos = body.getWorldCenter();
		final float angle = body.getAngle();
		
		g.rotate(angle);
		g.translate(pos.x * PHYSICS_PIX_FACTOR, pos.y * PHYSICS_PIX_FACTOR);
		
		for (final ShapeView shape: shapes) {
			shape.paint(g);
		}

		g.translate(-pos.x * PHYSICS_PIX_FACTOR, -pos.y * PHYSICS_PIX_FACTOR);
		g.rotate(-angle);
		
	}
	
	

}
