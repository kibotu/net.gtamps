package net.gtamps.preview.view;

import java.awt.Color;

import org.jbox2d.collision.FilterData;
import org.jbox2d.collision.shapes.CircleShape;
import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.collision.shapes.Shape;

public abstract class ShapeView<T extends Shape> extends PhysicsView {

	protected Color shapeColor = DEFAULT_COLOR;


	public static ShapeView<? extends Shape> getShapeView(final Shape shape) {
		final Class<? extends Shape> type = shape.getClass();
		if (type == CircleShape.class) {
			return new CircleShapeView((CircleShape) shape);
		} else if (type == PolygonShape.class) {
			return new PolygonShapeView((PolygonShape) shape);
		} else {
			throw new IllegalArgumentException("unknown shape type");
		}
	}

	protected T shape;

	protected ShapeView(final T shape) {
		this.shape = shape;
		update();
	}

	@Override
	public void paintHook() {
		setColorsAccordingtoShape(shape);
		final FilterData filter = shape.getFilterData();
		drawString("CollsionGrp " + filter.groupIndex, 5, 35);
	}

	private void setColorsAccordingtoShape(final Shape shape) {
		if (shape.isSensor()) {
			shapeColor = new Color(0, 0, 192, 24);
		}
		//		addMixColor(shapeColor, 0.5f);
	}

}
