package net.gtamps.android.graphics.graph.scene.animation.skeleton;

import net.gtamps.shared.Utils.Logger;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * User: Jan Rabe
 * Date: 24/11/12
 * Time: 13:33
 */
public class RiotAnimation {

    private int version;
    private float fps;
    private HashMap<String, ArrayList<BoneKeyFrame>> boneKeyFrames;
    private int numBones;
    private int numFrames;

    public RiotAnimation(int numBones, int numFrames, float fps, int version) {
        this.version = version;
        this.fps = fps;
        this.numBones = numBones;
        this.numFrames = numFrames;
    }

    public void addFrame(String boneName, BoneKeyFrame boneKeyFrame) {
        if(boneKeyFrames == null) boneKeyFrames = new HashMap<String, ArrayList<BoneKeyFrame>>(numBones);
        if (boneKeyFrames.containsKey(boneName)) boneKeyFrames.get(boneName).add(boneKeyFrame);
        else {
            ArrayList<BoneKeyFrame> list = new ArrayList<BoneKeyFrame>(numFrames);
            list.add(boneKeyFrame);
            boneKeyFrames.put(boneName, list);
        }
    }
}
