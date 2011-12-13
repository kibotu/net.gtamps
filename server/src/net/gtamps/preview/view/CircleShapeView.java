package net.gtamps.preview.view;

import java.awt.Graphics2D;

import org.jbox2d.collision.shapes.CircleShape;
import org.jbox2d.common.Vec2;

public class CircleShapeView extends ShapeView {
	
	
	public CircleShapeView(final CircleShape shape) {
		super(shape);
	}

	@Override
	public void paintHook(final Graphics2D g) {
		final CircleShape circle = (CircleShape) shape;

		final Vec2 pos = PreviewPerspective.worldToPix(circle.m_localPosition);
		final int diam = PreviewPerspective.worldLengthToPix(circle.m_radius*2f);
		 
		g.drawOval((int)pos.x, (int)pos.y, diam, diam);
		
	}

}
