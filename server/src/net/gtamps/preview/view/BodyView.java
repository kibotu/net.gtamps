package net.gtamps.preview.view;

import java.awt.Color;

import net.gtamps.shared.game.entity.Entity;

import org.jbox2d.collision.shapes.Shape;
import org.jbox2d.dynamics.Body;

public class BodyView extends PhysicsView {

	public static final BodyView createBodyView(final Body body) {
		// TODO boeser Hack nach '||'
		if (body.isDynamic() || ((Entity)body.getUserData()).getName().equalsIgnoreCase("spawnpoint")) {
			return new BodyViewWrapper(body);
		} else {
			return new BodyView(body);
		}
	}

	protected final Body body;
	protected final Entity entity;

	BodyView(final Body body) {
		this.body = body;
		for (Shape shape = body.getShapeList(); shape != null; shape = shape.getNext()) {
			addChild(ShapeView.createShapeView(shape));
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
	}

	protected void setColorAccordingToBody() {
		final float alpha = 1f;
		Color color;
		if (body.isFrozen()) {
			color = new Color(0.4f, 0.4f, 0.4f, alpha);
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
