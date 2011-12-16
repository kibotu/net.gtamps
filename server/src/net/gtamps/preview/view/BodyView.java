package net.gtamps.preview.view;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import net.gtamps.shared.game.entity.Entity;

import org.jbox2d.collision.shapes.Shape;
import org.jbox2d.dynamics.Body;

public class BodyView extends PhysicsView {
	
	private final List<ViewNode> subViews = new ArrayList<ViewNode>();
	private final Body body;
	private final Entity entity;
	
	public BodyView(final Body body) {
		this.body = body;
		addChild(new VectorView(body.getLinearVelocity(), body.getLocalCenter()));
		for (Shape shape = body.getShapeList(); shape != null; shape = shape.getNext()) {
			addChild(ShapeView.getShapeView(shape));
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
	protected void updateHook() {
		setPosition(body.getPosition());
		setRotation(body.getAngle());
	}
	
	@Override
	public void paintHook() {
		
		setColorAccordingToBody();
		markLocation();
		if (entity != null) {
			final String name = entity.getName();
//			if (name.equalsIgnoreCase("spawnpoint")
//					|| name.equalsIgnoreCase("car")
//					|| name.equalsIgnoreCase("human")) {
				drawString(entity.getName(), 5, 5);
				drawString(entity.x.getAsString() + ":" + entity.y.getAsString(), 5, 15);
//			}
		}

	}

	private void setColorAccordingToBody() {
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
		setColor(color);
	}
	

}
