package net.gtamps.preview.view;

import java.awt.Point;

import org.jbox2d.dynamics.Body;

/**
 * Unrotated and unscaled body painting.
 *
 * @author Jan Rabe, Tom Wallroth, Til Boerner
 *
 */
public class BodyViewWrapper extends BodyView {

	private Point savedPosition = null;
	private float savedRotation = 0f;
	private float savedScale = 1f;
	private final BodyView bodyView;


	BodyViewWrapper(final Body body) {
		this(new BodyView(body));
	}

	BodyViewWrapper(final BodyView bodyview) {
		super(bodyview.body);
		clearChildren();
		bodyView = bodyview;
		addChild(bodyview);
		if (body.isDynamic()) {
			addChild(new VectorView(body.getLinearVelocity(), body.getLocalCenter()));
		}
	}

	@Override
	protected void updateHook() {
		setPosition(body.getPosition());
	}

	@Override
	public void paintHook() {
		setColorAccordingToBody();
		markLocation();
		if (entity != null) {
			drawString(entity.getName(), 5, 5);
			drawString(entity.x.getAsString() + ":" + entity.y.getAsString(), 5, 15);
		}
		bodyView.setPosition(ORIGIN.x, ORIGIN.y);

	}

	private void saveTransform() {
		savedPosition = getPosition();
		savedRotation = getRotation();
		savedScale = getScale();
	}

	private void restoreTransform() {
		setPosition(savedPosition.x, savedPosition.y);
		setRotation(savedRotation);
		setScale(savedScale);
		savedPosition = ORIGIN;
		savedRotation = 0f;
		savedScale = 1f;
	}




}
