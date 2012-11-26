package net.gtamps.android.graphics.graph.scene.animation.skeleton;

import net.gtamps.android.graphics.graph.scene.animation.morph.AnimatedObject3D;
import net.gtamps.android.graphics.graph.scene.mesh.parser.Parser;
import net.gtamps.android.graphics.graph.scene.mesh.parser.SkeletonAnimationParser;
import net.gtamps.shared.Utils.Logger;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * User: Jan Rabe
 * Date: 23/11/12
 * Time: 17:20
 */
public class AnimatedSkeletonObject3D extends AnimatedObject3D {

    private ArrayList<Bone> bones;
    private HashMap<String, ArrayList<RiotAnimation>> animations;
    private short[] indicesForAnim;

    public AnimatedSkeletonObject3D(String objectResourceID, boolean generateMipMaps, Parser.Type type) {
        super(objectResourceID, generateMipMaps, type);
    }

    public void addBone(@NotNull Bone bone) {
        if (bones == null) bones = new ArrayList<Bone>();
        bones.add(bone);
    }

    public void addSkeletonAnimation(String animationID, RiotAnimation animation) {
        Logger.v(this, "Add [Animation=" + animationID + "]");
        if (animations == null) animations = new HashMap<String, ArrayList<RiotAnimation>>(10);
        if (animations.containsKey(animationID)) animations.get(animationID).add(animation);
        else {
            ArrayList<RiotAnimation> list = new ArrayList<RiotAnimation>(50);
            list.add(animation);
            animations.put(animationID, list);
        }
    }

    public void setBoneAnimationIndices(short[] indices) {
        this.indicesForAnim = indices;
    }
}
