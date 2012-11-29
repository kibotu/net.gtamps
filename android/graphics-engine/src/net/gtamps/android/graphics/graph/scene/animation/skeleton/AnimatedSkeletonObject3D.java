package net.gtamps.android.graphics.graph.scene.animation.skeleton;

import net.gtamps.android.graphics.graph.scene.animation.AnimationState;
import net.gtamps.android.graphics.graph.scene.animation.Interpolation;
import net.gtamps.android.graphics.graph.scene.mesh.Mesh;
import net.gtamps.android.graphics.graph.scene.mesh.buffermanager.Vector3BufferManager;
import net.gtamps.android.graphics.graph.scene.mesh.buffermanager.WeightManager;
import net.gtamps.android.graphics.graph.scene.mesh.parser.Parser;
import net.gtamps.android.graphics.graph.scene.mesh.parser.SkeletonAnimationParser;
import net.gtamps.android.graphics.graph.scene.primitives.Object3D;
import net.gtamps.shared.Utils.Logger;
import net.gtamps.shared.Utils.math.*;
import org.jetbrains.annotations.NotNull;

import javax.microedition.khronos.opengles.GL10;
import java.util.*;

/**
 * User: Jan Rabe
 * Date: 23/11/12
 * Time: 17:20
 */
public class AnimatedSkeletonObject3D extends Object3D {

    // skl
    private ArrayList<Bone> bones;
    private int[] indicesForAnim;

    // skn
    private HashMap<String, RiotAnimation> animations;

    // skn
    protected Mesh original;

    // animation stuff
    private RiotAnimation currentAnimation;
    protected AnimationState animationState;
    protected int currentFrameIndex;
    private Mesh previousMesh;
    private int dtInterpolation;
    private long startTime;
    private int resetInterpolationTime;

    public AnimatedSkeletonObject3D(String objectResourceID) {
        this(objectResourceID,true,Parser.Type.SKN);
    }

    public AnimatedSkeletonObject3D(String objectResourceID, boolean generateMipMaps, Parser.Type type) {
        super(objectResourceID,generateMipMaps,type);
        original = getMesh().clone();
        previousMesh = original;
        animationState = AnimationState.STOP;
        resetInterpolationTime = 500;
    }

    public void addBone(@NotNull Bone bone) {
        if (bones == null) bones = new ArrayList<Bone>();
        bones.add(bone);
    }

    public void addSkeletonAnimation(String animationID, RiotAnimation animation) {
        Logger.v(this, "Add [Animation=" + animationID + "]");
        if (animations == null) animations = new HashMap<String,RiotAnimation>(10);
        if (animations.containsKey(animationID)) Logger.e(this, animationID + " already defined, overwriting it");
        animations.put(animationID,animation);
    }

    public void setBoneAnimationIndices(int[] indices) {
        this.indicesForAnim = indices;
    }

    public void play(String animationId) {
        if (animations == null) return;
        currentAnimation = animations.get(animationId);
        animationState = AnimationState.PLAY;
        startTime = System.currentTimeMillis();
        currentFrameIndex = 0;
        Logger.I(this, "Play.");
    }

//    private void playFrame(RiotAnimation currentAnimation) {
//
//        String [] boneNames = currentAnimation.getBoneKeyFrames().keySet().toArray(new String[currentAnimation.getNumBones()]);
//
//        for(int i = 0; i < currentAnimation.getNumBones(); ++i) {
//            Logger.i(this, i + " " + boneNames[i]);
//        }
//
//        animationState = AnimationState.STOP;
//    }

    private void playFrame(RiotAnimation currentAnimation) {
//
//        Iterator<Map.Entry<Bone,ArrayList<BoneKeyFrame>>> it =  currentAnimation.getBoneKeyFrames().entrySet().iterator();
//        while (it.hasNext()) {
//            Map.Entry<Bone, ArrayList<BoneKeyFrame>> pairs = it.next();
////            update(pairs.getKey(), pairs.getValue());
//        }

        animationState = AnimationState.STOP;
    }

    private static final float [] temp = new float[3];
    private static final Vector3 tempV = Vector3.createNew();

    private void update(Bone bone, ArrayList<BoneKeyFrame> boneKeyFrames) {

        Vector3BufferManager curV = getMesh().vertices.getVertices();
        WeightManager curWM = getMesh().vertices.getWeights();

        Vector3 bonePos = bone.position;
        Vector3 boneScaling = bone.scaling;
        Vector3 boneRotation = bone.rotation;

        // bone transformation in frame
        float [] rot = boneKeyFrames.get(0).rot;
        float x = boneKeyFrames.get(0).x;
        float y = boneKeyFrames.get(0).y;
        float z = boneKeyFrames.get(0).z;



        for (int i = 0; i < curV.size(); ++i) {

            tempV.set(curV.getPropertyX(0), curV.getPropertyY(0),curV.getPropertyZ(0));

            Matrix4 boneTransformation1 = Matrix4.createNew();
            Matrix4 boneTransformation2 = Matrix4.createNew();
            Matrix4 boneTransformation3 = Matrix4.createNew();
            Matrix4 boneTransformation4 = Matrix4.createNew();
//
//            Vector3 currentPosition =   (curWM.getAsWeight(i).x * (u_BoneTransform[ int(in_BoneID[0]) ] * tempV) +
//                                        (curWM.getAsWeight(i).y * (u_BoneTransform[ int(in_BoneID[1]) ] * tempV) +
//                                        (curWM.getAsWeight(i).z * (u_BoneTransform[ int(in_BoneID[2]) ] * tempV) +
//                                        (curWM.getAsWeight(i).w * (u_BoneTransform[ int(in_BoneID[3]) ] * tempV);
//




            curV.overwrite(i, temp[0], temp[1], temp[2]);
        }
        getMesh().update();
    }

    private Matrix4 getBoneTransformation(Matrix4 currentFrame, Matrix4 nextFrame) {

//        float blend = currentFrameTime / animation.timePerFrame;
        float blend = 1;

        // Break it down into a vector and quaternion.
        Vector3 currentPosition = Vector3.createNew(currentFrame.getAt(12),currentFrame.getAt(13),currentFrame.getAt(14));
        Quaternion currentOrientation = Matrix4.CreateQuatFromMatrix(currentFrame);

        // Break it down into a vector and quaternion.
        Vector3 nextPosition = Vector3.createNew(nextFrame.getAt(12),nextFrame.getAt(13),nextFrame.getAt(14));
        Quaternion nextOrientation = Matrix4.CreateQuatFromMatrix(nextFrame);

        // Interpolate the frame data.
        float [] temp = Interpolation.getLinearInterpolatedPoint(currentPosition, nextPosition, blend);
        Quaternion finalOrientation = currentOrientation.slerp(nextOrientation, blend);

        // Rebuild a transform.
        float [] finalOri = finalOrientation.toAxisAngle();
        Matrix4 finalTransform = MatrixFactory.getRotationAxisAngle(finalOri);
        finalTransform.setAt(12,temp[0]);
        finalTransform.setAt(13,temp[1]);
        finalTransform.setAt(14,temp[2]);
//
//        transforms[i] = finalTransform;
        return currentFrame;
    }

    @Override
    protected void onDrawFrameInternal(GL10 gl10) {
        super.onDrawFrameInternal(gl10);

        switch (animationState) {
            case PLAY:
                playFrame(currentAnimation);
                break;
            case STOP:
                break;
            case PAUSE:
                break;
            case RESUME:
                break;
            default:
                break;
        }
    }

    public Bone getBone(String boneName) {
        for(int i = 0; i < bones.size(); ++i) {
            if(bones.get(i).name.equals(boneName)) return bones.get(i);
        }
        return null;
    }

    public ArrayList<Bone> getBones() {
        return bones;
    }

    public void addSkl(String resourceId) {
        SkeletonAnimationParser.loadSkl(resourceId,this);
    }

    public void addAnm(String resourceId) {
        SkeletonAnimationParser.loadAnm(resourceId, this);
    }
}
