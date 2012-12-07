package net.gtamps.android.graphics.graph.scene.animation.rigged;

import net.gtamps.android.graphics.R;
import net.gtamps.android.graphics.graph.RenderableNode;
import net.gtamps.android.graphics.graph.RootNode;
import net.gtamps.android.graphics.graph.scene.animation.AnimationState;
import net.gtamps.android.graphics.graph.scene.mesh.Mesh;
import net.gtamps.android.graphics.graph.scene.mesh.Vertex;
import net.gtamps.android.graphics.graph.scene.mesh.parser.lolreader.*;
import net.gtamps.android.graphics.graph.scene.mesh.texture.Texture;
import net.gtamps.android.graphics.graph.scene.primitives.Object3D;
import net.gtamps.android.graphics.graph.scene.primitives.Sphere;
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
        if (meshDirty) {
            mesh.update();
            meshDirty = false;
        }
    }

    boolean meshDirty = false;

    /**
     * Creates a Skeleton animated render object.
     * <p>
     * This function converts the handedness of the DirectX style input data
     * into the handedness OpenGL expects. So, vector inputs have their Z value
     * negated and quaternion inputs have their Z and W values negated.
     * </p>
     * @param skn SKNFile: Vertex position, normal, uv, indices, bone Weights and Influences
     * @param skl SKNFile: Bone position and rotation
     * @param anms list of ANMFiles: Keyframe based bone translation and rotation
     */
    public void create(SKNFile skn, SKLFile skl, HashMap<String, ANMFile> anms) {
        // vertex Data, needs skl if skl version 0 or 2
        mesh = Utils.createMesh(skn,skl);
        // animation data
        animations = Utils.createAnimations(skl,anms,mesh);
        // backup used as transformation basis
        original = mesh.clone();
    }

    public void play(String animationID) {
        animationState = AnimationState.PLAY;
        GLAnimation glAnimation = animations.get(animationID);
        Logger.i(this, animationID);
        playFrame(currentAnimation = glAnimation, currentFrame = 2);
    }

    private void playFrame(GLAnimation glAnimation, int index) {
        if (glAnimation == null) return;
        Utils.computeBoneTransformation(boneTransformations, glAnimation, 0, index);
        Utils.updateMesh(mesh, original, boneTransformations);
        meshDirty = true;
        animationState = AnimationState.STOP;
    }

    @Deprecated
    public RootNode getSkeleton() {
        String PACKAGE_NAME = "net.gtamps.android.graphics.test:raw/";

        Sphere joint = new Sphere(0.7f,20,20);
//        Object3D joint = new Object3D(PACKAGE_NAME + "bone_01_obj");

        Texture texture = new Texture(R.drawable.vintagebg, Texture.Type.u_Texture01, true);
        joint.addTexture(texture);
        joint.setScaling(0.01f, 0.01f, 0.01f);
        RootNode root = new RootNode();

        ArrayList<GLBone> bones = animations.get(PACKAGE_NAME+"ahri_dance_anm").bones;

        for (int i = 0; i < bones.size(); ++i) {

            final GLBone bone = bones.get(i);
            final RootNode node = new RootNode();
            node.add(joint);

            // get rotation and translation from frame
            Quaternion boneRot = Matrix4.createQuatFromMatrix(bone.transform);
            Vector3 bonePos = Vector3.createNew(bone.transform.values[M41], bone.transform.values[M42], bone.transform.values[M43]);

            // transform node by bone
            node.getRotation(true).setEulerAnglesFromQuaternion(boneRot);
            node.setPosition(bonePos);

            root.add(node);
        }

        return root;
    }
}
