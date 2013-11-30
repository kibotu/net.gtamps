package net.gtamps.preview.view;

import java.awt.Color;
import java.awt.Shape;

import org.jbox2d.common.Vec2;

public class VectorView extends PhysicsView {

	private static final float MIN_LENGTH_TO_SHOW = 0.3f;
	
	private final Vec2 vector;
	private final Vec2 origin;
	private final Color color = Color.WHITE;
	
	private Shape arrow = null;

	public VectorView(final Vec2 vector, final Vec2 origin) {
		this.vector = vector;
		this.origin = origin;
		update();
	}
	
	@Override
	public void updateHook() {
		setPosition(origin);
		if (vector.length() < MIN_LENGTH_TO_SHOW) {
			arrow = null;
		}
		arrow = getArrow(vector);
	}
		
	
	@Override
	public void paintHook() {
		if (arrow == null) {
			return;
		}
		setColor(color);
		drawShape(arrow);
	}
	
	
	
}
