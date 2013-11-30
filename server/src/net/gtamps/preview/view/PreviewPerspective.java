package net.gtamps.preview.view;

import java.awt.Dimension;
import java.awt.Point;

public class PreviewPerspective {
	
	private static float MIN_ZOOM = 0.01f;
	private static final float ZOOM_AMOUNT = 0.05f;
	
	private Point offset = new Point(0, 0);
	private float zoomf = 1f;
	private final Dimension viewingArea;
	
	public PreviewPerspective(final Dimension dimension) {
		viewingArea = dimension;
	}
	
	public Point getTopleft() {
		return offset;
	}
	
	public float getZoom() {
		return zoomf;
	}
	
	public Dimension getViewingArea() {
		return viewingArea;
	}
	
	public void setTopleft(final Point newOffset) {
		offset = newOffset;
	}
	
	public void setZoom(final float newZoom) {
		zoomf = Math.max(newZoom, MIN_ZOOM);
	}
	
	public void zoomIn(final int amount) {
		final float f = zoomf + ZOOM_AMOUNT * Math.signum(amount);
		setZoom(f);
	}
	
	public void zoomIn(final Point zoomPoint, final int amount) {
		// TODO
		zoomIn(amount);
	}
	
	public void setCenter(final Point center) {
		final int topX = center.x - (viewingArea.width >> 1);
		final int topY = center.y - (viewingArea.height >> 1);
		setTopleft(new Point(topX, topY));
	}
	
	public void move(final int dx, final int dy) {
		final Point topLeft = getTopleft();
		setTopleft(new Point(topLeft.x + dx, topLeft.y + dy));
	}
	
}
