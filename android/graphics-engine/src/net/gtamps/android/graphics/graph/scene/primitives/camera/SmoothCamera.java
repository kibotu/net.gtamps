package net.gtamps.android.graphics.graph.scene.primitives.camera;

/**
 * (c) 2010 Nicolas Gramlich
 * (c) 2011 Zynga Inc.
 *
 * @author Nicolas Gramlich
 * @since 22:11:17 - 25.03.2010
 */
public class SmoothCamera extends Camera {

    protected float mMaxVelocityX;
    protected float mMaxVelocityY;
    protected float mMaxZoomFactorChange;

    protected float mTargetCenterX;
    protected float mTargetCenterY;

    protected float mTargetZoomFactor;

    /**
     * Constructs a new camera object.
     *
     * @param positionX
     * @param positionY
     * @param positionZ
     * @param targetX
     * @param targetY
     * @param targetZ
     * @param upX
     * @param upY
     * @param upZ
     */
    public SmoothCamera(float positionX, float positionY, float positionZ, float targetX, float targetY, float targetZ, float upX, float upY, float upZ) {
        super(positionX, positionY, positionZ, targetX, targetY, targetZ, upX, upY, upZ);
    }
}

