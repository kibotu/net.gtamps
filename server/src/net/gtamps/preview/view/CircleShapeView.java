package net.gtamps.preview.view;

import java.awt.Shape;

import org.jbox2d.collision.shapes.CircleShape;

public class CircleShapeView extends ShapeView<CircleShape> {
	
	Shape ellipse;
	
	public CircleShapeView(final CircleShape shape) {
		super(shape);
	}

	@Override
	protected void updateHook() {
		ellipse = getCircle(shape.getLocalPosition(), shape.getRadius());
	}
	
	@Override
	public void paintHook() {
		super.paintHook();
//		fillShape(ellipse);
		drawShape(ellipse);
	}

	
}
