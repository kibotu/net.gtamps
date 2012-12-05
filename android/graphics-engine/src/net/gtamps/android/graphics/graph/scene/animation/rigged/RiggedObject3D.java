package net.gtamps.android.graphics.graph.scene.animation.rigged;

import net.gtamps.android.graphics.graph.RenderableNode;
import net.gtamps.android.graphics.graph.scene.animation.AnimationState;
import net.gtamps.android.graphics.graph.scene.mesh.Mesh;
import net.gtamps.android.graphics.graph.scene.mesh.Vertex;
import net.gtamps.android.graphics.graph.scene.mesh.parser.lolreader.*;
import net.gtamps.shared.Utils.Logger;
import net.gtamps.shared.Utils.math.Matrix4;
import net.gtamps.shared.Utils.math.Quaternion;
import net.gtamps.shared.Utils.math.Vector3;

import javax.microedition.khronos.opengles.GL10;
import java.util.*;

import static net.gtamps.shared.Utils.math.Matrix4.*;

/**
 * Represents a model defined from an .skn and an .skl file.
 * <p/>
 * Based on the work of James Lammlein, Adrian Astley
 * <p/>
 * User: Jan Rabe
 * Date: 05/12/12
 * Time: 13:13
 */
public class RiggedObject3D extends RenderableNode {

    private HashMap<String, GLAnimation> animations;
    private Mesh mesh;
    private Mesh original;
    protected AnimationState animationState;
    private GLAnimation currentAnimation;
    private int currentFrame;

    public static final int MAX_BONE_TRANSFORMS = 128;  // max bones that can be transformed depending on shader
    private final Matrix4[] boneTransformations; // describes current animation state for all bones

    public RiggedObject3D() {
        this.animations = new HashMap<String, GLAnimation>();
        animationState = AnimationState.STOP;

        boneTransformations = new Matrix4[MAX_BONE_TRANSFORMS];
        for (int i = 0; i < boneTransformations.length; ++i) {
            boneTransformations[i] = Matrix4.createNew();
        }
    }

    @Override
    public Mesh getMesh() {
        return mesh;
    }

    @Override
    public void onCreateInternal(GL10 gl10) {
    }


    @Override
    protected void onDrawFrameInternal(GL10 gl10) {

//        switch (animationState) {
//            case PLAY:
//                playFrame(currentAnimation,currentFrame);
//                break;
//            case STOP:
//                break;
//            case PAUSE:
//                break;
//            case RESUME:
//                break;
//            default:
//                break;
//        }
    }

    public boolean create(SKNFile skn, SKLFile skl, HashMap<String, ANMFile> anms) {
        boolean result = true;

        // This function converts the handedness of the DirectX style input data
        // into the handedness OpenGL expects.
        // So, vector inputs have their Z value negated and quaternion inputs have their
        // Z and W values negated.

        // Vertex Data
        mesh = new Mesh(skn.numIndices, skn.numVertices, true, true, true);

        // Animation data.
        List<Quaternion> boneOrientations = new ArrayList<Quaternion>();
        List<Vector3> bonePositions = new ArrayList<Vector3>();
        List<Float> boneScales = new ArrayList<Float>();
        List<Integer> boneParents = new ArrayList<Integer>();
        List<String> boneNames = new ArrayList<String>();

        // Bones are not always in order between the ANM and SKL files.
        HashMap<String, Integer> boneNameToID = new HashMap<String, Integer>();
        HashMap<Integer, String> boneIDToName = new HashMap<Integer, String>();

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

            mesh.addVertex(vertex);
        }

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

        //
        // Version 0 SKL files are similar to the animation files.
        // The bone positions and orientations are relative to their parent.
        // So, we need to compute their absolute location by hand.
        //
        if (skl.version == 0) {
            //
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

        // Depending on the version of the model, the look ups change.
        if (skl.version == 2 || skl.version == 0) {
            for (int i = 0; i < mesh.vertices.getWeights().size(); ++i) {
                // I don't know why things need to be remapped, but they do apparently.

                // Sanity
                int id = mesh.vertices.getWeights().getPropertyInfluence1(i);
                if (id < skl.boneIDs.size()) {
                    mesh.vertices.getWeights().setPropertyInfluence1(i, skl.boneIDs.get(id));
                }
                // Sanity
                id = mesh.vertices.getWeights().getPropertyInfluence2(i);
                if (id < skl.boneIDs.size()) {
                    mesh.vertices.getWeights().setPropertyInfluence2(i, skl.boneIDs.get(id));
                }
                // Sanity
                id = mesh.vertices.getWeights().getPropertyInfluence3(i);
                if (id < skl.boneIDs.size()) {
                    mesh.vertices.getWeights().setPropertyInfluence3(i, skl.boneIDs.get(id));
                }
                // Sanity
                id = mesh.vertices.getWeights().getPropertyInfluence4(i);
                if (id < skl.boneIDs.size()) {
                    mesh.vertices.getWeights().setPropertyInfluence4(i, skl.boneIDs.get(id));
                }
            }
        }

        // Add the animations.
        for (Map.Entry<String, ANMFile> pairs : anms.entrySet()) {
            String animationKey = pairs.getKey();
            ANMFile animation = pairs.getValue();
            if (animations.containsKey(animationKey)) continue;
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
            animations.put(animationKey, glAnimation);
        }

        // Index Information
        for (int i = 0; i < skn.numIndices; i += 3) {
            mesh.faces.add(skn.indices.get(i), skn.indices.get(i + 1), skn.indices.get(i + 2));
        }

        //
        // Compute the final animation transforms.
        //

        for (GLAnimation animation : animations.values()) {
            // This is sort of a mess.
            // We need to make sure "parent" bones are always updated before their "children".  The SKL file contains
            // bones ordered in this manner.  However, ANM files do not always do this.  So, we sort the bones in the ANM to match the ordering in
            // the SKL file.
            Collections.sort(animation.bones); // TODO check if sorted correctly
        }

        // Create the binding transform.  (The SKL initial transform.)
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

        // Convert animations into absolute space.
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

        // Multiply the animation transforms by the binding transform.
        for (GLAnimation animation : animations.values()) {
            for (GLBone bone : animation.bones) {
                // Sanity.
                if (boneNameToID.containsKey(bone.name)) {
                    int id = boneNameToID.get(bone.name);
                    GLBone bindingBone = bindingBones.bones.get(id);
                    Logger.i(this, "transform animations" + bone.name);

                    for (int i = 0; i < bone.frames.size(); ++i) {
                        // TODO check if it is correct
                        // bone.frames[i] = bindingBone.transform * bone.frames[i];
                        bone.frames.get(i).set(bindingBone.transform.mul(bone.frames.get(i)));
                    }
                }
            }
        }

        original = mesh.clone();
        return result;
    }

    public void play(String animationID) {
        animationState = AnimationState.PLAY;
        GLAnimation glAnimation = animations.get(animationID);
        Logger.i(this, animationID);
        playFrame(currentAnimation = glAnimation, currentFrame = 150);
    }

    private void playFrame(GLAnimation glAnimation, int index) {
        if (glAnimation == null) return;
        LolReaderUtils.computeBoneTransformation(boneTransformations, glAnimation, index, index);
        LolReaderUtils.updateMesh(mesh, original, boneTransformations);
        animationState = AnimationState.STOP;
    }
}
