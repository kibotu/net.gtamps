package net.gtamps.android.graphics.graph.scene.animation;

import net.gtamps.android.graphics.graph.scene.mesh.Mesh;
import net.gtamps.android.graphics.graph.scene.mesh.buffermanager.Vector3BufferManager;
import net.gtamps.android.graphics.graph.scene.primitives.Object3D;
import net.gtamps.shared.Utils.Logger;
import net.gtamps.shared.Utils.math.FloatMath;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * User: Jan Rabe
 * Date: 18/11/12
 * Time: 17:30
 */
public class AnimationObject3D extends Object3D {

    private HashMap<String, List<KeyFrame>> animations;
    private long startTime;
    private boolean isPlaying;
    private String currentFrameName;
    private int currentFrameIndex;
    private boolean loop;
    private long currentTime;
    private int interpolation;
    private int currentInterpolationStep;
    private int loopStartIndex;
    private long fps;
    private KeyFrame currentFrame;

    private Mesh original;

    public AnimationObject3D(String objectResourceID) {
        super(objectResourceID);
        original = getMesh().clone();
        currentFrameName = "";
        interpolation = 30;
    }

    public void play(String animationId) {
        if (animations == null) return;
    }

    public boolean isPlaying() {
        return isPlaying;
    }

    public void playFrame(String animationId, String frameId) {
        if (animations == null) return;

        // check if animation and frame exist
        if (currentFrame == null || !currentFrame.getId().equals(frameId)) {
            currentInterpolationStep = 0;
            currentFrame = getKeyFrame(animationId, frameId);
        }

        // update
        if (currentInterpolationStep < interpolation) {
            startTime = System.currentTimeMillis();
            updateFrame(currentFrame.getObject3D().getMesh(), original, (float) currentInterpolationStep / (float) interpolation);
        }
    }

    private void updateFrame(Mesh newM, Mesh prevM, float percent) {

        Vector3BufferManager curV = getMesh().vertices.getVertices();
        Vector3BufferManager newV = newM.vertices.getVertices();
        Vector3BufferManager prevV = prevM.vertices.getVertices();

        for (int i = 0; i < curV.size(); ++i) {
            getLinearInterpolatedPoint(prevV.getPropertyX(i), prevV.getPropertyY(i), prevV.getPropertyZ(i), newV.getPropertyX(i), newV.getPropertyY(i), newV.getPropertyZ(i), percent);
            curV.set(i, temp[0], temp[1], temp[2]);
            //Logger.d(this, i + ": ("+currentVertices.getPropertyX(i)+"|"+currentVertices.getPropertyY(i)+"|"+currentVertices.getPropertyZ(i)+") \t=> ("+newVertices.getPropertyX(i)+"|"+newVertices.getPropertyY(i)+"|"+newVertices.getPropertyZ(i)+")");
        }

        getMesh().update();

        ++currentInterpolationStep;

        Logger.i(this, currentInterpolationStep + ". "+percent+"% \t["+temp[0]+"\t|"+temp[0]+"\t|"+temp[0]+"]");
    }


    private final static float[] temp = new float[3];

    private float[] getLinearInterpolatedPoint(float x0, float y0, float z0, float x1, float y1, float z1, float percent) {
        temp[0] = x0 + ( x1 - x0 ) * percent;
        temp[1] = y0 + ( y1 - y0 ) * percent;
        temp[2] = z0 + ( z1 - z0 ) * percent;
        return temp;
    }

    public static float getEuclideanDistance(float x0, float y0, float z0, float x1, float y1, float z1) {
        return FloatMath.sqrt(Math.abs(x0 - x1) * (x0 - x1) + (y0 - y1) * (y0 - y1) + (z0 - z1) * (z0 - z1));
    }

    public void addFrame(String animationId, KeyFrame frame) {
        Logger.v(this, "Add [Animation=" + animationId + "|Frame=" + frame.getId() + "]");
        if (animations == null) animations = new HashMap<String, List<KeyFrame>>(10);
        if (animations.containsKey(animationId)) animations.get(animationId).add(frame);
        else {
            List<KeyFrame> list = new ArrayList<KeyFrame>(10);
            list.add(frame);
            animations.put(animationId, list);
        }
    }

    public KeyFrame getKeyFrame(String animationId, String frameId) {
        // check if animation exists
        List<KeyFrame> frames = animations.get(animationId);
        if (frames == null) return null;
        // check if frame exists
        KeyFrame frame = null;
        for (int i = 0; i < frames.size(); ++i) {
            if (frames.get(i).getId().equals(frameId)) frame = frames.get(i);
        }
        return frame;
    }

}
