package net.gtamps.android.graphics.graph.scene.animation.skeleton;

import net.gtamps.android.graphics.graph.scene.animation.AnimationState;
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

    // bone transformations
    private final Matrix4 [] boneTransformations; // describes current animation state for all bones
    public static final int MAX_BONE_TRANSFORMS = 128;  // max bones that can be transformed depending on shader
    private final float[] boneTransformationData = new float[MAX_BONE_TRANSFORMS * Matrix4.SIZE]; // 2-d array for shader array

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
        boneTransformations = new Matrix4[MAX_BONE_TRANSFORMS];

        for(int i = 0; i < boneTransformations.length; ++i) {
            boneTransformations[i] = Matrix4.createNew();
        }
    }

    public void addBone(@NotNull Bone bone) {
        if (bones == null) bones = new ArrayList<Bone>();
        bones.add(bone);
    }

    public void addSkeletonAnimation(String animationID, @NotNull RiotAnimation animation) {
        Logger.v(this, "Add [Animation=" + animationID + "|Frames="+animation.getNumFrames()+"|Bones="+animation.getNumBones()+"]");
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

    public float[] getBoneTransformations() {

        int j = 0;
        for(int i = 0; i < boneTransformationData.length; i+=16) {
            boneTransformationData[i] =  boneTransformations[j].values[0];
            boneTransformationData[i+1] =  boneTransformations[j].values[1];
            boneTransformationData[i+2] =  boneTransformations[j].values[2];
            boneTransformationData[i+3] =  boneTransformations[j].values[3];

            boneTransformationData[i+4] =  boneTransformations[j].values[4];
            boneTransformationData[i+5] =  boneTransformations[j].values[5];
            boneTransformationData[i+6] =  boneTransformations[j].values[6];
            boneTransformationData[i+7] =  boneTransformations[j].values[7];

            boneTransformationData[i+8] =  boneTransformations[j].values[8];
            boneTransformationData[i+9] =  boneTransformations[j].values[9];
            boneTransformationData[i+10] =  boneTransformations[j].values[10];
            boneTransformationData[i+11] =  boneTransformations[j].values[11];

            boneTransformationData[i+12] =  boneTransformations[j].values[12];
            boneTransformationData[i+13] =  boneTransformations[j].values[13];
            boneTransformationData[i+14] =  boneTransformations[j].values[14];
            boneTransformationData[i+15] =  boneTransformations[j].values[15];
//            Logger.i(this, "i="+i + " j="+j);
            ++j;
        }

        return boneTransformationData;
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

    private void playFrame(@NotNull RiotAnimation currentAnimation) {

        Iterator<Map.Entry<Bone,ArrayList<BoneKeyFrame>>> it =  currentAnimation.getBoneKeyFrames().entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<Bone, ArrayList<BoneKeyFrame>> pairs = it.next();
            setBoneTransformation(pairs.getKey(), pairs.getValue(), 0);
        }

        updateMesh();

        animationState = AnimationState.STOP;
    }

    private static final float [] tempF = new float [3];
    private static final Vector3 tempPosition = Vector3.createNew();
    private static final float [] tempWeight = new float[4];
    private static final int [] tempInfluences = new int[4];

    private void updateMesh() {

        //v.position = v.weights[0] * boneTransformations[v.influence[0]].mul(v.pos, 1) +
        // and normal

        Vector3BufferManager curV = getMesh().vertices.getVertices();
        WeightManager curW = getMesh().vertices.getWeights();

        for (int i = 0; i < curV.size(); ++i) {

            // current position
            tempPosition.set(curV.getPropertyX(i),curV.getPropertyY(i),curV.getPropertyZ(i));

            // current weight
            tempWeight[0] = curW.getPropertyWeightX(i);
            tempWeight[1] = curW.getPropertyWeightY(i);
            tempWeight[2] = curW.getPropertyWeightZ(i);
            tempWeight[3] = curW.getPropertyWeightW(i);

            // current weight
            tempInfluences[0] = curW.getPropertyInfluence1(i);
            tempInfluences[1] = curW.getPropertyInfluence2(i);
            tempInfluences[2] = curW.getPropertyInfluence3(i);
            tempInfluences[3] = curW.getPropertyInfluence4(i);

            curV.overwrite(i, tempPosition.x, tempPosition.y, tempPosition.z);
        }
        getMesh().update();
    }

    private void setBoneTransformation(Bone bone, ArrayList<BoneKeyFrame> frames, int frame) {
        if(bone.id >= bones.size()) return;

        BoneKeyFrame boneKeyFrame = frames.get(frame);
        Matrix4 transform = boneTransformations[bone.id];
        Quaternion quat = boneKeyFrame.rotation;
        quat.toMatrix(transform);
        transform.values[14] = boneKeyFrame.x;
        transform.values[14] = boneKeyFrame.y;
        transform.values[15] = boneKeyFrame.z;
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

    public Bone getBone(int nameHash) {
        for(int i = 0; i < bones.size(); ++i) {
            if(bones.get(i).nameHash == nameHash) return bones.get(i);
        }
        return null;
    }

    public void transformChildBonesAlongParents(int version) {

        if(version == 0) {

            int i;
            for (i = 0; i < bones.size(); ++i) {

                // Determine the parent bone.
                final Bone child = bones.get(i);
                final int parentBoneID = child.parentID;

                // Only update non root bones.
                if (parentBoneID != Bone.ROOT_BONE_ID) {

                    Bone parent = bones.get(parentBoneID);
                    // Update orientation.
                    // Append quaternions for rotation transform B * A.
                    child.rotation.mulLeft(parent.rotation);

                    // Update position.
                    // child.pos = parent.pos + transform(child.pos, parent.rot)
                    parent.rotation.transformVector(child.position).addInPlace(parent.position);
                }
            }
            Logger.v(this, i + " child & parent bones transformed.");
        }
    }
}
