package net.gtamps.preview.view;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;

import net.gtamps.shared.game.entity.Entity;

import org.jbox2d.collision.shapes.Shape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;

public class BodyView extends PhysicsView {
	
	private final List<PhysicsView> subViews = new ArrayList<PhysicsView>();
	private final Body body;
	private final Entity entity;
	
	public BodyView(final Body body) {
		this.body = body;
		subViews.add(new VectorView(body.getLinearVelocity(), body.getLocalCenter()));
		for (Shape shape = body.getShapeList(); shape != null; shape = shape.getNext()) {
			subViews.add(ShapeView.getShapeView(shape));
		}
		final Object userData = body.getUserData();
		if (userData != null && userData instanceof Entity) {
			entity = (Entity) userData;
		} else {
			// TODO warn
			entity = null;
		}
		
	}

	@Override
	public void paint(final Graphics2D g) {
		final Vec2 pos = body.getPosition();
		final float angle = body.getAngle();
		
		final Color previousColor = g.getColor();
		rotate(angle, g);
		translate(pos, g);
		
		setColorAccordingToBody(g);
		markLocation(g);
		if (entity != null) {
			g.drawString(entity.toString(), pos.x + 10, pos.y + 10);
		}
		
		for (final PhysicsView subview: subViews) {
			subview.paint(g);
		}
		

		translate(pos.negate(), g);
		rotate(-angle, g);
		g.setColor(previousColor);
	}

	private void setColorAccordingToBody(final Graphics2D g) {
		final float alpha = 0.2f;
		Color color;
		if (body.isFrozen()) {
			color = new Color(0.2f, 0.2f, 0.2f, alpha);
		} else if (body.isSleeping()) {
			color = new Color(0.6f, 0.6f, 0.6f, alpha);
		} else if (body.isStatic()) {
			color = new Color(0.8f, 0.8f, 0.8f, alpha);
		} else if (body.isDynamic()) {
			color = new Color(0.2f, 0.2f, 0.8f, alpha);
		} else {
			color = Color.CYAN;
		}
		g.setColor(color);
	}
	
	private void markLocation(final Graphics2D g) {
		final int size = 10;
		final int halfSize = size >> 1;
		g.fillOval(-halfSize, -halfSize, size, size);
	}

}
