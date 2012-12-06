package net.gtamps.android.graphics.graph.scene.mesh.parser.lolreader;

import net.gtamps.android.graphics.graph.scene.animation.rigged.GLAnimation;
import net.gtamps.android.graphics.graph.scene.animation.rigged.GLBone;
import net.gtamps.android.graphics.graph.scene.mesh.Mesh;
import net.gtamps.android.graphics.graph.scene.mesh.Vertex;
import net.gtamps.android.graphics.graph.scene.mesh.buffermanager.Vector3BufferManager;
import net.gtamps.android.graphics.graph.scene.mesh.buffermanager.WeightManager;
import net.gtamps.shared.Utils.Logger;
import net.gtamps.shared.Utils.math.Matrix4;
import net.gtamps.shared.Utils.math.Quaternion;
import net.gtamps.shared.Utils.math.Vector3;

import java.io.IOException;
import java.util.*;

import static net.gtamps.shared.Utils.math.Matrix4.*;

/**
 * User: Jan Rabe
 * Date: 05/12/12
 * Time: 12:58
 */
public class Utils {

    public static final int BYTESIZE_OF_FLOAT = 4;
    private static final String TAG = Utils.class.getSimpleName();
    private static GLAnimation animation;

    // utility
    private Utils() {
    }

    public static String removeBoneNamePadding(String s) {
        int position = s.indexOf('\0');
        if (position >= 0) {
            s = s.replace("\0", "");
        }

        return s;
    }

    public static void readSklBoneNames(final BinaryReader file, int length, List<SKLBone> bones) throws IOException {
        String[] temp = file.readString(length).split("\0");
        int counter = 0;
        for (int i = 0; i < temp.length; ++i) {
            // remove single letters
            if (temp[i].length() > 1) {
                // remove weird signs in front of bone name
                bones.get(counter).name = normalizeSklBoneName(temp[i]);
                ++counter;
            }
        }
    }

    public static String normalizeSklBoneName(String s) {
        int i;
        for (i = 0; i < s.length(); ++i) {
            // bone names start with upper case letter, but can have underscore sign as 2nd letter
            if (Character.isUpperCase(s.charAt(i)) && (Character.isLetter(s.charAt(i + 1)) || s.charAt(i + 1) == '_')) {
                break;
            }
        }
        return s.substring(i);
    }

    public static float[] lookUpVector(int id, List<Float> vectors) {
        float[] result = new float[3];

        int startingPosition = id * result.length;
        for (int i = 0; i < result.length; ++i) {
            result[i] = vectors.get(startingPosition + i);
        }

        return result;
    }

    public static float[] lookUpQuaternion(int id, List<Float> quaternions) {
        float[] result = new float[4];

        int startingPosition = id * result.length;
        for (int i = 0; i < result.length; ++i) {
            result[i] = quaternions.get(startingPosition + i);
        }

        return result;
    }

    /**
     * Marry Bones with Vector
     *
     * @param w weights
     * @param i influences
     * @param m bone transformation matrices
     * @param t target vector (like position or normal)
     * @param f flag for position or directional vector
     */
    public static void marryBonesWithVector(float[] w, int[] i, Matrix4[] m, Vector3 t, float f) {


        Vector3 influence1 = getSingleBoneInfluence(m[i[0]], t, f).mulInPlace(w[0]);
        Vector3 influence2 = getSingleBoneInfluence(m[i[1]], t, f).mulInPlace(w[1]);
        Vector3 influence3 = getSingleBoneInfluence(m[i[2]], t, f).mulInPlace(w[2]);
        Vector3 influence4 = getSingleBoneInfluence(m[i[3]], t, f).mulInPlace(w[3]);

        t.addInPlace(influence1).addInPlace(influence2).addInPlace(influence3).addInPlace(influence4);

        influence1.recycle();
        influence2.recycle();
        influence3.recycle();
        influence4.recycle();
    }

    /**
     * computes single bone influence
     *
     * @param m current bone transformation Matrix
     * @param t position vector
     * @param f flag: 1 position, 0 direction
     * @return Vector3 single bone influence
     */
    private static Vector3 getSingleBoneInfluence(Matrix4 m, Vector3 t, float f) {
        float[] pI = new float[4];
        pI[0] = m.values[0] * t.x + m.values[1] * t.y + m.values[2] * t.z + m.values[3] * f;
        pI[1] = m.values[4] * t.x + m.values[5] * t.y + m.values[6] * t.z + m.values[7] * f;
        pI[2] = m.values[8] * t.x + m.values[9] * t.y + m.values[10] * t.z + m.values[11] * f;

//        pI[0] = m.values[0]  * t.x + m.values[4]  * t.y + m.values[8]  * t.z + m.values[12]  * f;
//        pI[1] = m.values[1]  * t.x + m.values[5]  * t.y + m.values[9]  * t.z + m.values[13]  * f;
//        pI[2] = m.values[2]  * t.x + m.values[6]  * t.y + m.values[10] * t.z + m.values[14]  * f;

        return Vector3.createNew(pI[0], pI[1], pI[2]);
    }

    /**
     * Computes bone transformation matrices
     *
     * @param transforms
     * @param animation
     * @param currentFrameTime
     * @param currentFrame
     */
    public static void computeBoneTransformation(Matrix4[] transforms, GLAnimation animation, float currentFrameTime, int currentFrame) {

        //
        // Normal Case
        //
        // Interpolate between the current and next frames to calculate
        // the transform to send to the renderer.
        //

        float blend = currentFrameTime / animation.timePerFrame;

        int nextFrame = (currentFrame + 1) % animation.numberOfFrames;
        for (int i = 0; i < animation.bones.size(); ++i) {
            // Get the current frame's transform.
            Matrix4 current = animation.bones.get(i).frames.get(currentFrame);

            //
            // The interpolation code is unstable.  It creates a lot of animation anomalies.
            //
            // TODO: Fix it.
            //

            // Break it down into a vector and quaternion.
            Vector3 currentPosition = Vector3.createNew();
            currentPosition.x = current.values[M41];
            currentPosition.y = current.values[M42];
            currentPosition.z = current.values[M43];

            Quaternion currentOrientation = Matrix4.createQuatFromMatrix(current);

            // Get the next frame's transform.
            Matrix4 next = animation.bones.get(i).frames.get(150);

            // Break it down into a vector and quaternion.
            Vector3 nextPosition = Vector3.createNew();
            nextPosition.x = next.values[M41];
            nextPosition.y = next.values[M42];
            nextPosition.z = next.values[M43];

            Quaternion nextOrientation = Matrix4.createQuatFromMatrix(next);

            // Interpolate the frame data.
            currentPosition.lerpInPlace(nextPosition, blend);
            currentOrientation.slerpInPlace(nextOrientation, blend);

            // Rebuild a transform.
//            Matrix4 finalTransform = Matrix4.rotate(currentOrientation);
//            finalTransform.values[M41] = currentPosition.x;
//            finalTransform.values[M42] = currentPosition.y;
//            finalTransform.values[M43] = currentPosition.z;

            Matrix4 finalTransform = Matrix4.rotate(nextOrientation);
            finalTransform.values[M41] = nextPosition.x;
            finalTransform.values[M42] = nextPosition.y;
            finalTransform.values[M43] = nextPosition.z;

            transforms[i] = finalTransform;
        }
    }

    private static final Vector3 tempPosition = Vector3.createNew();
    private static final Vector3 tempNormal = Vector3.createNew();
    private static final float[] tempWeight = new float[4];
    private static final int[] tempInfluences = new int[4];

    public static void updateMesh(Mesh curM, Mesh nextM, Matrix4[] transM) {

        Vector3BufferManager curV = curM.vertices.getVertices();
        Vector3BufferManager curN = curM.vertices.getNormals();
        WeightManager curW = curM.vertices.getWeights();

        Vector3BufferManager nextV = nextM.vertices.getVertices();
        Vector3BufferManager nextN = nextM.vertices.getNormals();
        WeightManager nextW = nextM.vertices.getWeights();

        for (int i = 0; i < curV.size(); ++i) {

            // current position
            tempPosition.set(nextV.getPropertyX(i), nextV.getPropertyY(i), nextV.getPropertyZ(i));
//            tempNormal.set(curN.getPropertyX(i), curN.getPropertyY(i), curN.getPropertyZ(i));

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

            Utils.marryBonesWithVector(tempWeight, tempInfluences, transM, tempPosition, 1);
//            Utils.marryBonesWithVector(tempWeight, tempInfluences, boneTransformations, tempNormal, 0);

            curV.overwrite(i, tempPosition.x, tempPosition.y, tempPosition.z);
//            curN.overwrite(i, tempNormal.x, tempNormal.y, tempNormal.z);
        }
    }

    public static HashMap<String, GLAnimation> createAnimations(SKLFile skl, HashMap<String, ANMFile> anms, Mesh mesh) {

        // Animation data.
        List<Quaternion> boneOrientations = new ArrayList<Quaternion>();
        List<Vector3> bonePositions = new ArrayList<Vector3>();
        List<Float> boneScales = new ArrayList<Float>();
        List<Integer> boneParents = new ArrayList<Integer>();
        List<String> boneNames = new ArrayList<String>();

        // Bones are not always in order between the ANM and SKL files.
        HashMap<String, Integer> boneNameToID = new HashMap<String, Integer>();
        HashMap<Integer, String> boneIDToName = new HashMap<Integer, String>();

        // Fill animation data
        fillAnimationData(skl, boneOrientations, bonePositions, boneScales, boneParents, boneNames, boneNameToID, boneIDToName);

        // Version 0 SKL files are similar to the animation files.
        // The bone positions and orientations are relative to their parent.
        // So, we need to compute their absolute location by hand.
        if (skl.version == 0) computeAbsoluteBoneLocation(skl, boneOrientations, bonePositions);

        // Get the animations
        HashMap<String, GLAnimation> animations = getAnimations(skl, anms, boneIDToName);

        //
        // Compute the final animation transforms.
        //

        // We need to make sure "parent" bones are always updated before their
        // "children".  The SKL file contains bones ordered in this manner.
        // However, ANM files do not always do this.  So, we sort the bones
        // in the ANM to match the ordering in the SKL file.
        for (GLAnimation animation : animations.values()) {
            Collections.sort(animation.bones);
        }

        // Create the binding transform.  (The SKL initial transform.)
        GLAnimation bindingBones = getInitialBindingPose(boneOrientations, bonePositions, boneParents, boneNames);

        // Convert animations into absolute space.
        animationsToAbsoluteSpace(animations, boneNameToID, bindingBones);

        // Multiply the animation transforms by the binding transform.
        appendAnimationsOnBindingTransform(animations, boneNameToID, bindingBones);

        return animations;
    }

    private static void appendAnimationsOnBindingTransform(HashMap<String, GLAnimation> animations, HashMap<String, Integer> boneNameToID, GLAnimation bindingBones) {
        for (GLAnimation animation : animations.values()) {
            for (GLBone bone : animation.bones) {
                // Sanity.
                if (boneNameToID.containsKey(bone.name)) {
                    int id = boneNameToID.get(bone.name);
                    GLBone bindingBone = bindingBones.bones.get(id);

                    for (int i = 0; i < bone.frames.size(); ++i) {
                        // TODO check if it is correct
                        // bone.frames[i] = bindingBone.transform * bone.frames[i];
                        bone.frames.get(i).set(bindingBone.transform.mul(bone.frames.get(i)));
                    }
                }
            }
        }
    }

    private static void animationsToAbsoluteSpace(HashMap<String, GLAnimation> animations, HashMap<String, Integer> boneNameToID, GLAnimation bindingBones) {
        for (GLAnimation animation : animations.values()) {
            for (GLBone bone : animation.bones) {
                // Sanity.
                if (boneNameToID.containsKey(bone.name)) {
                    int id = boneNameToID.get(bone.name);
                    bone.parent = bindingBones.bones.get(id).parent;

                    // For each frame...
                    for (int i = 0; i < bone.frames.size(); ++i) {
                        Matrix4 parentTransform = createNew();
                        if (bone.parent >= 0) {
                            if (bone.parent < animation.bones.size()) {
                                GLBone parent = animation.bones.get(bone.parent);
                                parentTransform = parent.frames.get(i);
                            }
                        }
                        bone.frames.get(i).mulInPlace(parentTransform);
                    }
                }
            }
        }
    }

    private static HashMap<String, GLAnimation> getAnimations(SKLFile skl, HashMap<String, ANMFile> anms, HashMap<Integer, String> boneIDToName) {
        HashMap<String, GLAnimation> newAnims = new HashMap<String, GLAnimation>();
        for (Map.Entry<String, ANMFile> pairs : anms.entrySet()) {
            String animationKey = pairs.getKey();
            ANMFile animation = pairs.getValue();
            if (newAnims.containsKey(animationKey)) continue;
            // Create the OpenGL animation wrapper.
            GLAnimation glAnimation = new GLAnimation();

            glAnimation.playbackFPS = animation.playbackFPS;
            glAnimation.numberOfBones = animation.numberOfBones;
            glAnimation.numberOfFrames = animation.numberOfFrames;

            // Convert ANMBone to GLBone.
            for (ANMBone bone : animation.bones) {
                GLBone glBone = new GLBone();

                if (animation.version == 4 && skl.boneIDMap.size() > 0) {
                    // Version 4 ANM files contain a hash value to represent the bone ID/name.
                    // We need to use the map from the SKL file to match the ANM bone with the correct
                    // SKL bone.

                    if (skl.boneIDMap.containsKey(bone.id)) {
                        int sklID = skl.boneIDMap.get(bone.id);
                        glBone.name = boneIDToName.get(sklID);
                    }
                } else {
                    glBone.name = bone.name;
                }

                glBone.id = bone.id;

                // Convert ANMFrame to Matrix4.
                for (ANMFrame frame : bone.frames) {
                    Matrix4 transform;

                    Quaternion quat = new Quaternion(frame.orientation[0], frame.orientation[1], -frame.orientation[2], -frame.orientation[3]);
                    transform = rotate(quat);

                    transform.values[M41] = frame.position[0];
                    transform.values[M42] = frame.position[1];
                    transform.values[M43] = -frame.position[2];

                    glBone.frames.add(transform);
                }

                glAnimation.bones.add(glBone);
            }

            glAnimation.timePerFrame = 1.0f / (float) animation.playbackFPS;

            // Store the animation.
            newAnims.put(animationKey, glAnimation);
        }
        return newAnims;
    }

    public static void remapBoneIndices(SKLFile skl, Mesh mesh) {
        for (int i = 0; i < mesh.vertices.getWeights().size(); ++i) {
            // I don't know why things need to be remapped, but they do apparently.

            // Sanity
            int id = mesh.vertices.getWeights().getPropertyInfluence1(i);
            if (id < skl.boneIDs.size()) {
                mesh.vertices.getWeights().setPropertyInfluence1(i, skl.boneIDs.get(id));
                Logger.i(TAG, "exchanging " + id + " with " + skl.boneIDs.get(id));
            }
            // Sanity
            id = mesh.vertices.getWeights().getPropertyInfluence2(i);
            if (id < skl.boneIDs.size()) {
                mesh.vertices.getWeights().setPropertyInfluence2(i, skl.boneIDs.get(id));
                Logger.i(TAG, "exchanging " + id + " with " + skl.boneIDs.get(id));
            }
            // Sanity
            id = mesh.vertices.getWeights().getPropertyInfluence3(i);
            if (id < skl.boneIDs.size()) {
                mesh.vertices.getWeights().setPropertyInfluence3(i, skl.boneIDs.get(id));
                Logger.i(TAG, "exchanging " + id + " with " + skl.boneIDs.get(id));
            }
            // Sanity
            id = mesh.vertices.getWeights().getPropertyInfluence4(i);
            if (id < skl.boneIDs.size()) {
                mesh.vertices.getWeights().setPropertyInfluence4(i, skl.boneIDs.get(id));
                Logger.i(TAG, "exchanging " + id + " with " + skl.boneIDs.get(id));
            }
        }
    }

    private static void computeAbsoluteBoneLocation(SKLFile skl, List<Quaternion> boneOrientations, List<Vector3> bonePositions) {
        // This algorithm is a little confusing since it's indexing identical data from
        // the SKL file and the local variable List<>s. The indexing scheme works because
        // the List<>s are created in the same order as the data in the SKL files.
        //
        for (int i = 0; i < skl.numBones; ++i) {

            SKLBone sklBone = skl.bones.get(i);

            // Only update non root bones.
            if (sklBone.parentID != GLBone.ROOT) {
                // Determine the parent bone.
                int parentBoneID = sklBone.parentID;

                // Update orientation.
                // Append quaternions for rotation transform B * A.
                boneOrientations.get(i).mulLeft(boneOrientations.get(parentBoneID));

                Vector3 localPosition = Vector3.createNew();
                localPosition.x = sklBone.position[0];
                localPosition.y = sklBone.position[1];
                localPosition.z = sklBone.position[2];

                // Update position. TODO check if it is correct
                // bonePositions[i] = bonePositions[parentBoneID] + Vector3.Transform(localPosition, boneOrientations[parentBoneID]);
                bonePositions.get(i).transform(boneOrientations.get(parentBoneID)).addInPlace(bonePositions.get(parentBoneID));
            }
        }
    }

    private static void fillAnimationData(SKLFile skl, List<Quaternion> boneOrientations, List<Vector3> bonePositions, List<Float> boneScales, List<Integer> boneParents, List<String> boneNames, HashMap<String, Integer> boneNameToID, HashMap<Integer, String> boneIDToName) {

        // Animation data
        for (int i = 0; i < skl.numBones; ++i) {

            SKLBone sklBone = skl.bones.get(i);

            Quaternion orientation = new Quaternion();
            if (skl.version == 0) {
                // Version 0 SKLs contain a quaternion.
                orientation.x = sklBone.orientation[0];
                orientation.y = sklBone.orientation[1];
                orientation.z = -sklBone.orientation[2];
                orientation.w = -sklBone.orientation[3];
            } else {
                // Other SKLs contain a rotation matrix.

                // Create a matrix from the orientation values.
                Matrix4 transform = createNew();

                transform.values[M11] = sklBone.orientation[0];
                transform.values[M21] = sklBone.orientation[1];
                transform.values[M31] = sklBone.orientation[2];

                transform.values[M12] = sklBone.orientation[4];
                transform.values[M22] = sklBone.orientation[5];
                transform.values[M32] = sklBone.orientation[6];

                transform.values[M13] = sklBone.orientation[8];
                transform.values[M23] = sklBone.orientation[9];
                transform.values[M33] = sklBone.orientation[10];

                // Convert the matrix to a quaternion.
                orientation = createQuatFromMatrix(transform);
                orientation.z = -orientation.z;
                orientation.w = -orientation.w;
            }

            boneOrientations.add(orientation);

            // Create a vector from the position values.
            Vector3 position = Vector3.createNew();
            position.x = sklBone.position[0];
            position.y = sklBone.position[1];
            position.z = -sklBone.position[2];
            bonePositions.add(position);

            boneNames.add(sklBone.name);
            boneNameToID.put(sklBone.name, i);
            boneIDToName.put(i, sklBone.name);

            boneScales.add(sklBone.scale);
            boneParents.add(sklBone.parentID);
        }
    }

    /**
     * Creates a new Mesh based on the data in the .skn file.
     *
     * @param skn SKNFile filled with mesh data
     * @return newMesh new Mesh and ready to be allocated
     */
    public static Mesh createMesh(SKNFile skn) {
        Mesh newMesh = new Mesh(skn.numIndices, skn.numVertices, true, true, true);

        for (int i = 0; i < skn.numVertices; ++i) {

            SKNVertex sknVertex = skn.vertices.get(i);
            Vertex vertex = new Vertex();

            // Position Information
            vertex.position.x = sknVertex.position[0];
            vertex.position.y = sknVertex.position[1];
            vertex.position.z = -sknVertex.position[2];

            // Normal Information
            vertex.normal.x = sknVertex.normal[0];
            vertex.normal.y = sknVertex.normal[1];
            vertex.normal.z = -sknVertex.normal[2];

            // Tex Coords Information
            vertex.uv.u = sknVertex.uv[0];
            vertex.uv.v = sknVertex.uv[1];

            // Bone Index Information
            vertex.weights.influence1 = sknVertex.boneIndex[0];
            vertex.weights.influence2 = sknVertex.boneIndex[1];
            vertex.weights.influence3 = sknVertex.boneIndex[2];
            vertex.weights.influence4 = sknVertex.boneIndex[3];

            // Bone Weight Information
            vertex.weights.x = sknVertex.weights[0];
            vertex.weights.y = sknVertex.weights[1];
            vertex.weights.z = sknVertex.weights[2];
            vertex.weights.w = sknVertex.weights[3];

            newMesh.addVertex(vertex);
        }

        // Index Information
        for (int i = 0; i < skn.numIndices; i += 3) {
            newMesh.faces.add(skn.indices.get(i), skn.indices.get(i + 1), skn.indices.get(i + 2));
        }

        return newMesh;
    }

    public static GLAnimation getInitialBindingPose(List<Quaternion> boneOrientations, List<Vector3> bonePositions, List<Integer> boneParents, List<String> boneNames) {
        GLAnimation bindingBones = new GLAnimation();
        for (int i = 0; i < boneOrientations.size(); ++i) {
            GLBone bone = new GLBone();

            bone.name = boneNames.get(i);
            bone.parent = boneParents.get(i);

            bone.transform = rotate(boneOrientations.get(i));
            bone.transform.values[M41] = bonePositions.get(i).x;
            bone.transform.values[M42] = bonePositions.get(i).y;
            bone.transform.values[M43] = bonePositions.get(i).z;

            bone.transform = bone.transform.getInverted();

            bindingBones.bones.add(bone);
        }
        return bindingBones;
    }
}
