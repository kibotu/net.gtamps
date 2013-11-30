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
    private HashMap<Bone, ArrayList<BoneKeyFrame>> boneKeyFrames;
    private int numBones;
    private int numFrames;
    private HashMap<Bone, ArrayList<BoneKeyFrame>> boneKeyFrames2;

    public RiotAnimation(int numBones, int numFrames, float fps, int version) {
        this.version = version;
        this.fps = fps;
        this.numBones = numBones;
        this.numFrames = numFrames;
    }

    public void addFrame(Bone bone, BoneKeyFrame boneKeyFrame) {
        if(boneKeyFrames == null) boneKeyFrames = new HashMap<Bone, ArrayList<BoneKeyFrame>>(numBones);
        if (boneKeyFrames.containsKey(bone)) boneKeyFrames.get(bone).add(boneKeyFrame);
        else {
            ArrayList<BoneKeyFrame> list = new ArrayList<BoneKeyFrame>(numFrames);
            list.add(boneKeyFrame);
            boneKeyFrames.put(bone, list);
        }
    }

    public int getVersion() {
        return version;
    }

    public float getFps() {
        return fps;
    }

    public HashMap<Bone, ArrayList<BoneKeyFrame>> getBoneKeyFrames() {
        return boneKeyFrames;
    }

    public ArrayList<BoneKeyFrame> getBoneKeyFrames(Bone bone) {
        return boneKeyFrames.get(bone);
    }

    public int getNumBones() {
        return numBones;
    }

    public int getNumFrames() {
        return numFrames;
    }
}
