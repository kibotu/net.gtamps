package net.gtamps.preview.view;

import java.awt.Color;
import java.awt.Point;

public class Scene extends ViewNode {
	
	private static final Color DEFAULT_COLOR = Color.WHITE;
	private static final Color DEFAULT_BG_COLOR = Color.BLACK;
	
	private final PreviewPerspective perspective;
	
	public Scene(final PreviewPerspective perspective) {
		if (perspective == null) {
			throw new IllegalArgumentException("'perspective' must not be 'null'");
		}
		this.perspective = perspective;
		update();
	}
	
	@Override
	protected void updateHook() {
		final Point origin = perspective.getTopleft();
		final float zoom = perspective.getZoom();
		setPosition(origin.x, origin.y);
		setScale(zoom);
	}

	@Override
	protected void paintHook() {
		setColor(DEFAULT_COLOR);
		setBackgroundColor(DEFAULT_BG_COLOR);
	}

}
