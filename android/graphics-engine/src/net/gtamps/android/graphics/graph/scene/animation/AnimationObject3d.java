package net.gtamps.android.graphics.graph.scene.animation;

import android.widget.FrameLayout;
import net.gtamps.android.graphics.graph.scene.mesh.Mesh;
import net.gtamps.android.graphics.graph.scene.mesh.buffermanager.Vector3BufferManager;
import net.gtamps.android.graphics.graph.scene.primitives.Object3D;
import net.gtamps.shared.Utils.Logger;
import net.gtamps.shared.Utils.math.Frustum;

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
    private int loopStartIndex;
    private long fps;

    public AnimationObject3D(String objectResourceID) {
        super(objectResourceID);
    }

    public void play(String id) {
        if (animations == null) return;

    }

    public void playFrame(String animationId, String frameId) {
        if (animations == null) return;

        // check if animation exists
        List<KeyFrame> frames = animations.get(animationId);
        if(frames == null) return;

        // check if frame exists
        KeyFrame frame = null;
        for(int i = 0; i < frames.size(); ++i) {
            if(frames.get(i).equals(frameId)) frame = frames.get(i);
        }
        if(frame == null) return;

        // update
        updateFrame(frame);
    }

    private void updateFrame(KeyFrame frame) {
        Mesh currentMesh = getMesh();
        Mesh newMesh = frame.getObject3D().getMesh();

        Vector3BufferManager currentVertices = currentMesh.vertices.getVertices();
        Vector3BufferManager newVertices = newMesh.vertices.getVertices();

        for(int i = 0; i < currentVertices.size(); ++i) {
            currentVertices.set(i, newVertices.getPropertyX(i), newVertices.getPropertyY(i),newVertices.getPropertyY(i));
        }

        Logger.i(this, "updating frame");
    }


    public void addFrame(String id, KeyFrame frame) {
        if (animations == null) animations = new HashMap<String, List<KeyFrame>>(10);
        if(animations.containsKey(id)) animations.get(id).add(frame);
        else {
            List<KeyFrame> list = new ArrayList<KeyFrame>(10);
            list.add(frame);
            animations.put(id, list);
        }
    }
}
