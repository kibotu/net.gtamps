package net.gtamps.preview.view;

import org.jbox2d.common.Vec2;

public class PreviewPerspective {
	
	private static final int MOVE_AMOUNT = 5;

	private static final float ZOOM_AMOUNT = 0.05f;

	static float MIN_ZOOM = 0.01f;
	
	private static Vec2 dimension = new Vec2(0,0);
	private static final float worldToPixFactor = 10;
	private static float zoom = 1f;
	private static Vec2 topLeftOffset = new Vec2(0,0);
	
	public static void setDimension(final float x, final float y) {
		dimension = new Vec2(x, y);
	}
	
	public static Vec2 worldToPix(final Vec2 v) {
		return v.clone().mul(worldToPixFactor);
	}
	
	public static void translate(final float x, final float y) {
		topLeftOffset = new Vec2(x, y);
	}

	public static void move(final float x, final float y) {
		topLeftOffset.addLocal(new Vec2(x, y));
	}
	
	public static void up() {
		topLeftOffset.addLocal(0, -MOVE_AMOUNT);
	}
	
	public static void down() {
		topLeftOffset.addLocal(0, MOVE_AMOUNT);
	}
	
	public static void left() {
		topLeftOffset.addLocal(-MOVE_AMOUNT, 0);
	}
	
	public static void right() {
		topLeftOffset.addLocal(MOVE_AMOUNT, 0);
	}
	
	public static void zoom(final int amount) {
		final float f = zoom + ZOOM_AMOUNT * Math.signum(amount);
		zoom = Math.max(f, MIN_ZOOM);
	}
	
	public static void zoom(final int amount, final Vec2 pos) {
		zoom(amount);
		//translate(pos.x, pos.y);
	}
	
	public static void centerOn(final Vec2 point) {
		final Vec2 localizedPoint = point.add(topLeftOffset);
		final Vec2 centerOffset = dimension.mul(0.5f);
		final Vec2 newCenter = localizedPoint.sub(centerOffset);
		topLeftOffset = newCenter;
	}
	
	public static int worldLengthToPix(final float l) {
		return (int)(l*worldToPixFactor);
	}

	public static float getZoomLevel() {
		return zoom;
	}

	public static Vec2 getOffset() {
		return topLeftOffset.clone();
	}
	
}
