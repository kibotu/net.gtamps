package net.gtamps.preview.view;

import java.awt.Shape;

import org.jbox2d.collision.shapes.PolygonShape;

public class PolygonShapeView extends ShapeView<PolygonShape> {
	
	Shape polygon;
	
	public PolygonShapeView(final PolygonShape shape) {
		super(shape);
	}
	
	@Override
	protected void updateHook() {
		polygon = getPolygon(shape.getVertices());
	}

	@Override
	public void paintHook() {
		super.paintHook();
		drawShape(polygon);
	}

}
