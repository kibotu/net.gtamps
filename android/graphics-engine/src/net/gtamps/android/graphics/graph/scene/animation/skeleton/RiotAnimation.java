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

    private String magic;
    private int version;
    private int fps;
    private int numBones;
    private int numFrames;
    private ArrayList<BoneKeyFrame> boneKeyFrames;

    public RiotAnimation() {
        boneKeyFrames = new ArrayList<BoneKeyFrame>();
    }

    public String getMagic() {
        return magic;
    }

    public void setMagic(String magic) {
        this.magic = magic;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public int getFps() {
        return fps;
    }

    public void setFps(int fps) {
        this.fps = fps;
    }

    public int getNumBones() {
        return numBones;
    }

    public void setNumBones(int numBones) {
        this.numBones = numBones;
    }

    public int getNumFrames() {
        return numFrames;
    }

    public void setNumFrames(int numFrames) {
        this.numFrames = numFrames;
    }

    public void addBoneFrame(BoneKeyFrame boneKeyFrame) {
        if (boneKeyFrames == null) boneKeyFrames = new ArrayList<BoneKeyFrame>();
        Logger.v(this, "Add [BoneKeyFrame="+boneKeyFrame+"]");
        boneKeyFrames.add(boneKeyFrame);
    }
}
